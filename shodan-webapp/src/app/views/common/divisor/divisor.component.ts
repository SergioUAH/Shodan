import { Component, OnInit, ChangeDetectorRef, Output, EventEmitter } from '@angular/core';
import { environment } from 'src/environments/environment';
import { MatTableDataSource } from '@angular/material/table';
import { RestService } from 'src/app/config/services/rest-service';
import { QueryElement } from '../query-form/query-form.component';

const QUERY_HISTORY = '/target/getQueries'
@Component({
  selector: 'app-divisor',
  templateUrl: './divisor.component.html',
  styleUrls: ['./divisor.component.scss']
})
export class DivisorComponent implements OnInit {
  history;
  loading: boolean;

  @Output() searchQuery: EventEmitter<QueryElement> = new EventEmitter<QueryElement>();

  constructor(public http: RestService, public cdr: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.getQueries();
  }

  getQueries() {
    console.log("Get Queries");
    this.loading = true;
    this.http.getCall(environment.url + QUERY_HISTORY)
      .subscribe(data => {
        this.history = data;
        this.loading = false;
        this.cdr.markForCheck();
      },
      error => {
        console.log("Error", error);
        this.history = [];
        this.loading = false;
        this.cdr.markForCheck();
      });
  }

  addQuery(query) {
    this.searchQuery.emit(query);
    console.log(query);
  }

}
