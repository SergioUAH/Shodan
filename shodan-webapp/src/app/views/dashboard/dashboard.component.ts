import { Component, OnInit, ChangeDetectorRef, Input, ViewChild, SimpleChanges } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { RestService } from 'src/app/config/services/rest-service';
import { environment } from 'src/environments/environment';
import { DivisorComponent } from '../common/divisor/divisor.component';

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
const GET_HACKED_DEVICES_URL = "/target/getAllHacked";

const GET_WORDLISTS_URL = "/files/getWordlists";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  dataSource: MatTableDataSource<any>;
  dataSourceTab2: MatTableDataSource<any>;
  title = 'shodan-webapp';

  displayedColumns = ['checked', 'ip', 'hostname', 'os', 'port', 'country', 'city'];
  displayedColumnsTab2 = ['ip', 'port', 'user', 'password', 'country', 'city'];

  queryMap: Map<string, string>;
  responseData: any;
  responseDataTab2: any;
  query: QueryElement;
  loading = true;
  loadingTab2 = true;

  wordlists: string[];
  selectedWordlists: string[];

  @ViewChild(DivisorComponent, { static: true }) divisorComp: DivisorComponent;
  constructor(public http: RestService, public cdr: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.getWordlists();
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
        this.parseResponseData(this.responseData);
        this.dataSource = new MatTableDataSource(this.responseData);
        this.loading = false;
        this.divisorComp.getQueries();
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
        this.parseResponseData(this.responseData);
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

  getHackedDevices() {
    console.log("Recargar hacked hosts");
    this.loadingTab2 = true;
    this.http.getCall(environment.url + GET_HACKED_DEVICES_URL)
      .subscribe(data => {
        this.responseDataTab2 = data;
        this.dataSourceTab2 = new MatTableDataSource(this.responseDataTab2);
        this.loadingTab2 = false;
        this.cdr.markForCheck();
      },
        error => {
          console.log("Error", error);
          this.dataSourceTab2 = new MatTableDataSource();
          this.loadingTab2 = false;
          this.cdr.markForCheck();
        });
  }

  getWordlists() {
    this.loading = true;
    this.http.getCall(environment.url + GET_WORDLISTS_URL)
      .subscribe(data => {
        this.responseData = data;
        this.wordlists = this.responseData;
        this.selectedWordlists = [this.wordlists[0], this.wordlists[0]];
      },
        error => {
          console.log("Error", error);
          this.wordlists = [];
        });
  }

  parseResponseDataTab2(resp: any) {
    resp.forEach(function (part, index) {
      resp[index] = {
        ...resp[index],
        country: resp[index].location.country,
        city: resp[index].location.city
      };
      delete resp[index].location;
    }, resp);

    return resp;
  }

  parseResponseData(resp) {
    resp.forEach(function (part, index) {
      resp[index] = {
        ...resp[index],
        country: resp[index].location.country,
        city: resp[index].location.city
      };
      delete resp[index].location;
    }, resp);

    return resp;
  }

  loadTabData(tabEvent) {
    switch (tabEvent.index) {
      case 0:
        this.getDevices();
        break;
      case 1:
        this.getHackedDevices();
        break;
      default:
        break;
    }
  }

  updateWordlists(wordlists) {
    this.selectedWordlists = wordlists;
    this.cdr.markForCheck();
  }

}
