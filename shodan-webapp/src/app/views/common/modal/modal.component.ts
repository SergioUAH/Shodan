import { Component, OnInit, ChangeDetectionStrategy, ViewEncapsulation, Inject, ChangeDetectorRef } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { RestService } from 'src/app/config/services/rest-service';
import { environment } from 'src/environments/environment';
import { Content } from '@angular/compiler/src/render3/r3_ast';

export class Host {
  id: number;
  ip: string;
  hostname: string;
  organization: string;
  country: string;
  city: string;
}

const REST_URL = '/target/';
const TEST_SECURITY_URL = '/target/testSecurity/';

@Component({
  selector: 'app-modal',
  templateUrl: './modal.component.html',
  styleUrls: ['./modal.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None
})
export class ModalComponent implements OnInit {
  displayedHeaders = ['ip', 'port', 'operatingSystem', 'hostname', 'title', 'product', 'version', 'isp','transport', 'uptime', 'domain', 'isSslEnabled', 'deviceType'];
  device;

  row;
  dataSource;
  loading;
  detail;
  selection: any;
  responseData: any;

  constructor(public dialogRef: MatDialogRef<ModalComponent>, @Inject(MAT_DIALOG_DATA) public data: any,
    private formBuilder: FormBuilder, public http: RestService, private cdr: ChangeDetectorRef ) { }

  ngOnInit(): void {
    this.loading = true;
    this.row = this.data.content;
    console.log(this.row);
    this.getDetail();
  }

  getDetail() {
    this.http.getCall(environment.url + REST_URL + this.row.id)
    .subscribe(data => {
      console.log("Successful");
      this.responseData = data;
      console.log(this.responseData);
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
    this.http.getCall(environment.url + TEST_SECURITY_URL + this.row.id)
    .subscribe(data => {
      console.log("Successful");
      this.responseData = data;
      console.log(this.responseData);
      this.loading = false;
      this.cdr.markForCheck();
    },
    error => {
      console.log("Error", error);
      this.loading = false;
      this.cdr.markForCheck();
    });
  }

  goToURL() {
    window.open(`http://${this.responseData.ip}:${this.responseData.port}`, '_blank');
  }

}
