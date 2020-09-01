import { Component, OnInit, Inject, ChangeDetectorRef } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ModalComponent } from '../modal/modal.component';
import { FormBuilder } from '@angular/forms';
import { RestService } from 'src/app/config/services/rest-service';
import { WebSocketService } from 'src/app/config/services/web-socket-service';
import { environment } from 'src/environments/environment';
import { Client }from '@stomp/stompjs';
import * as SockJS from 'sockjs-client';

const REST_URL = "/target/testDevices";

@Component({
  selector: 'app-test-security-modal',
  templateUrl: './test-security-modal.component.html',
  styleUrls: ['./test-security-modal.component.scss']
})
export class TestSecurityModalComponent implements OnInit {
  device;

  dataSource;
  loading;
  detail;
  selection: any;
  responseData: any;
  devices: any;
  textarea: string;

  endPointController: string = '/logger/connectionLog';
  endPointWS: string = 'http://localhost:8085/log';

  constructor(public dialogRef: MatDialogRef<TestSecurityModalComponent>, @Inject(MAT_DIALOG_DATA) public data: any,
    private formBuilder: FormBuilder, private http: RestService, private webSocket: WebSocketService, private cdr: ChangeDetectorRef ) { }

  ngOnInit(): void {
    this.loading = true;
    this.devices = this.data.content;
    this.testSecurity(this.devices);
  }

  openConnection() {
    let webSocket = new SockJS(this.endPointWS);
    const client = new Client(webSocket);
    client.onConnect = function (frame) {
      client.subscribe(this.endPointController, function (stompEvent) {
       this.listenEvent(stompEvent);
      });

      client.onStompError = function (frame) {
        console.log('Broker reported error: ' + frame.headers['message']);
        console.log('Additional details: ' + frame.body);
      };

      client.activate();
    };
  }

  listenEvent(stompEvent: any) {
    this.textarea = JSON.stringify(stompEvent.body);
  }

  // getDetail() {
  //   this.http.getCall(environment.url + REST_URL + this.row.id)
  //   .subscribe(data => {
  //     console.log("Successful");
  //     this.responseData = data;
  //     console.log(this.responseData);
  //    this.loading = false;
  //     this.cdr.markForCheck();
  //   },
  //   error => {
  //     console.log("Error", error);
  //     this.loading = false;
  //     this.cdr.markForCheck();
  //   });
  // }

  onClose() {
    this.dialogRef.close();
  }

  // testSecurity() {
  //   this.http.getCall(environment.url + TEST_SECURITY_URL + this.row.id)
  //   .subscribe(data => {
  //     console.log("Successful");
  //     this.responseData = data;
  //     console.log(this.responseData);
  //     this.loading = false;
  //     this.cdr.markForCheck();
  //   },
  //   error => {
  //     console.log("Error", error);
  //     this.loading = false;
  //     this.cdr.markForCheck();
  //   });
  // }
  testSecurity(devices) {
    console.log(devices);
    this.openConnection();
    this.http.postCall(environment.url + REST_URL, devices)
      .subscribe(data => {
        console.log("Successful");
        this.responseData = data;
        console.log(this.responseData);
        // this.dataSource = new MatTableDataSource(this.responseData);
      },
        error => {
          console.log("Error", error);
          // this.webSocket.closeConnection();
          // this.dataSource = new MatTableDataSource();
        });
  }

}

