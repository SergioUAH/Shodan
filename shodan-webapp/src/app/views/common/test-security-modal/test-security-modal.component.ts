import { Component, OnInit, Inject, ChangeDetectorRef, AfterViewInit, resolveForwardRef, ElementRef, ViewChild } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ModalComponent } from '../modal/modal.component';
import { FormBuilder } from '@angular/forms';
import { RestService } from 'src/app/config/services/rest-service';
import { WebSocketService } from 'src/app/config/services/web-socket-service';
import { environment } from 'src/environments/environment';
import { Client, Message, Stomp } from '@stomp/stompjs';

const REST_URL = "/target/testDevices";

@Component({
  selector: 'app-test-security-modal',
  templateUrl: './test-security-modal.component.html',
  styleUrls: ['./test-security-modal.component.scss']
})
export class TestSecurityModalComponent implements OnInit, AfterViewInit {
  device;

  dataSource;
  loading;
  detail;
  selection: any;
  responseData: any;
  devices: any;
  textarea: string;

  endPointController: string = '/topic/connectionLog';
  endPointWS: string = '/logging';

  client: Client;
  wordlists: string[];

  @ViewChild('logger') logger: ElementRef;
  constructor(public dialogRef: MatDialogRef<TestSecurityModalComponent>, @Inject(MAT_DIALOG_DATA) public data: any,
    private formBuilder: FormBuilder, private http: RestService, private webSocket: WebSocketService, private cdr: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.loading = true;
    this.devices = this.data.content;
    this.wordlists = this.data.wordlists;
    this.connectWebSocket();
    this.testSecurity(this.devices);
  }

  ngAfterViewInit(): void {

  }

  listenEvent(stompEvent) {
    let body = JSON.parse(stompEvent.body)
    this.textarea = this.textarea == undefined ? `\n${body.text}\n` : this.textarea + `\n${body.text}\n`;
    this.logger.nativeElement.scrollTop = this.logger.nativeElement.scrollHeight;
    //this.textarea = `\n${body.timeStamp} --> ${body.text}\n`;
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


  connectWebSocket() {
    const OLD_THIS = this;
    this.webSocket.openConnection(function (stompEvent) {
      console.log("MENSAJE RECIBIDO --> " + stompEvent);
      OLD_THIS.listenEvent(stompEvent);
    });
  }

  async testSecurity(devices) {
    await this.delay(1000);
    this.webSocket.sendMessage("HOLAAAAAAAAAAAAAAAA");
    const dto = {
      ids: devices,
      wordlists: this.wordlists
    }
    this.http.postCall(environment.url + REST_URL, dto)
      .subscribe(data => {
        this.responseData = data;
        console.log(this.responseData);
        this.webSocket.closeConnection();
      },
        error => {
          console.log("Error", error);
          this.webSocket.closeConnection();
          this.onClose();
        });
  }

  delay(ms: number) {
    return new Promise( resolve => setTimeout(resolve, ms) );
  }
}


