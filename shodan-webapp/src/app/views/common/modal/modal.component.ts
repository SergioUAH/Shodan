import { Component, OnInit, ChangeDetectionStrategy, ViewEncapsulation, Inject, ChangeDetectorRef, Output, EventEmitter } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { RestService } from 'src/app/config/services/rest-service';
import { environment } from 'src/environments/environment';
import { Content } from '@angular/compiler/src/render3/r3_ast';
import { QueryElement } from '../query-form/query-form.component';

export class Host {
  id: number;
  ip: string;
  hostname: string;
  organization: string;
  country: string;
  city: string;
}

const REST_URL = '/target/';
const HACKED_HOST_DETAIL_URL = '/target/';
const TEST_SECURITY_URL = '/target/testSecurity/';

@Component({
  selector: 'app-modal',
  templateUrl: './modal.component.html',
  styleUrls: ['./modal.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None
})
export class ModalComponent implements OnInit {
  displayedHeadersList = [['ip', 'port', 'operatingSystem', 'hostname', 'title', 'product', 'version', 'isp', 'transport', 'uptime', 'domain', 'isSslEnabled', 'deviceType'], ['ip', 'port', 'user', 'password', 'country', 'city']];
  device;
  displayedHeaders;
  row;
  dataSource;
  loading;
  type;
  selection: any;
  responseData: any;

  constructor(public dialogRef: MatDialogRef<ModalComponent>, @Inject(MAT_DIALOG_DATA) public data: any,
    private formBuilder: FormBuilder, public http: RestService, private cdr: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.loading = true;
    this.row = this.data.content;
    this.type = this.data.type;
    this.displayedHeaders = this.displayedHeadersList[this.type];
    if(this.type == 0) {
      this.getDetail();
    } else {
      this.responseData = this.data.content;
      this.loading = false;
      this.cdr.markForCheck();
    }
  }

  getDetail() {
    this.http.getCall(environment.url + REST_URL + this.row.id)
      .subscribe(data => {
        this.responseData = data;
        this.loading = false;
        this.cdr.markForCheck();
      },
        error => {
          console.log("Error", error);
          this.loading = false;
          this.cdr.markForCheck();
        });
  }

  onClose() {
    this.dialogRef.close();
  }

  testSecurity() {
    this.dialogRef.close(this.row.id);
  }

  goToURL() {
    if(this.responseData.port == 443) {
      window.open(`https://${this.responseData.ip}:${this.responseData.port}`, '_blank');
    } else {
      window.open(`http://${this.responseData.ip}:${this.responseData.port}`, '_blank');
    }
  }

}
