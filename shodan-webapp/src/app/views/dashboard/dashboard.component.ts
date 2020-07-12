import { Component, OnInit, ChangeDetectorRef, Input, ViewChild, SimpleChanges } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { RestService } from 'src/app/config/services/rest-service';
import { QueryFormComponent } from '../common/query-form/query-form.component';
import { environment } from 'src/environments/environment';

export class HostElement {
  id: number;
  checked: boolean;
  ip: string;
  hostname: string;
  os: string;
  country: string;
  city: string;
}

export class QueryElement {
  id: number;
  query: string;
  facets: string;
  // all: string;
  // asn: string;
  // city: string;
  // country: string;
  // cpe: string;
  // device: string;
  // geo: string;
  // has_ipv6: string;
  // has_screenshot: string;
  // has_ssl: string;
  // has_vuln: string;
  // hash: string;
  // hostname: string;
  // ip: string;
  // isp: string;
  // link: string;
  // net: string;
  // org: string;
  // os: string;
  // port: string;
  // postal: string;
  // product: string;
  // region: string;
  // scan: string;
  // shodan_module: string;
  // state: string;
  // version: string;
};

// const DATA: HostElement[] = [
//   {id: 1, checked: false, ip: '192.168.1.24', hostname: 'host1', os: 'Windows', country: 'ES', city: 'Alcalá'},
//   {id: 2, checked: false, ip: '192.168.1.37', hostname: 'host2', os: 'Windows', country: 'ES', city: 'Madrid'},
//   {id: 3, checked: false, ip: '127.0.0.1', hostname: 'host3', os: 'Windows', country: 'ES', city: 'Madrid'},
// ];

const SEARCH_DEVICES_URL = "/target/search";
const GET_DEVICES_URL = "/target/getAll";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  dataSource: MatTableDataSource<any>;

  title = 'shodan-webapp';

  displayedColumns = ['checked','ip', 'hostname', 'os', 'port', 'country', 'city'];

  queryMap: Map<string,string>;
  responseData: any;

  query: QueryElement;
  loading = false;

  constructor(public http: RestService, public cdr: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.getDevices();
    this.cdr.detectChanges();
  }

  ngAfterViewInit() {
    this.cdr.detectChanges();
  }

  searchDevices(searchQuery) {
    console.log(searchQuery);
    this.loading = true;
    this.http.postCall(environment.url + SEARCH_DEVICES_URL, searchQuery)
      .subscribe(data => {
        this.responseData = data;
        console.log(this.responseData);
        this.dataSource = new MatTableDataSource(this.responseData);
        this.loading = false;
        this.cdr.markForCheck();
      },
      error => {
        console.log("Error", error);
        this.dataSource = new MatTableDataSource();
        this.loading = false;
        this.cdr.markForCheck();
      });
  }

  getDevices() {
    this.loading = true;
    this.http.getCall(environment.url + GET_DEVICES_URL)
      .subscribe(data => {
        this.responseData = data;
        console.log(this.responseData);
        this.dataSource = new MatTableDataSource(this.responseData);
        this.loading = false;
        this.cdr.markForCheck();
      },
      error => {
        console.log("Error", error);
        this.dataSource = new MatTableDataSource();
        this.loading = false;
        this.cdr.markForCheck();
      });
  }
}
