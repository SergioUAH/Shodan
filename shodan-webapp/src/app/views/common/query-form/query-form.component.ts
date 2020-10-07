import { SPACE, TAB } from '@angular/cdk/keycodes';
import { Component, OnInit, ViewChild, ElementRef, AfterViewInit, Output, ChangeDetectorRef, Input, EventEmitter } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormControl } from '@angular/forms';

import { map, startWith } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { MatAutocomplete, MatAutocompleteSelectedEvent } from '@angular/material/autocomplete';
import { MatChipInputEvent } from '@angular/material/chips';

import { RestService } from 'src/app/config/services/rest-service';
import { FILTROS_SHODAN } from '../filtro/filtros';
import { environment } from 'src/environments/environment';
import { MatOption } from '@angular/material/core';
import { MatTableDataSource } from '@angular/material/table';

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

const REST_URL = "/target/search";

@Component({
  selector: 'app-query-form',
  templateUrl: './query-form.component.html',
  styleUrls: ['./query-form.component.scss']
})
export class QueryFormComponent implements OnInit, AfterViewInit {
  formBuilder: FormBuilder;
  form: FormGroup;
  stringQuery: string;

  myControl = new FormControl();
  options = FILTROS_SHODAN;
  filteredOptions: Observable<string[]>;

  visible = true;
  selectable = true;
  removable = true;
  separatorKeysCodes: number[] = [SPACE, TAB];
  filters: string[] = [];

  responseData: any;

  @Input() query: QueryElement;
  @Output() searchQuery: EventEmitter<QueryElement> = new EventEmitter<QueryElement>();

  @ViewChild('filterInput') filterInput: ElementRef<HTMLInputElement>;
  @ViewChild('auto') matAutocomplete: MatAutocomplete;
  constructor(public http: RestService, public cdr: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.query = new QueryElement();
    this.stringQuery = "";
    this.formBuilder = new FormBuilder();
    //this.createForm();

    this.filteredOptions = this.myControl.valueChanges.pipe(
      startWith(null),
      map((filter: string | null) => filter ? this._filter(filter) : this.options.slice()));

    this.cdr.markForCheck();
  }

  ngAfterViewInit() {
    console.log(this.filterInput);
  }

  add(event: MatChipInputEvent): void {
    const input = event.input;
    const value = event.value;

    if ((value || '').trim()) {
      this.filters.push(value.trim());
    }

    if (input) {
      input.value = '';
    }

    this.myControl.setValue(null);
  }

  remove(filter: string): void {
    const index = this.filters.indexOf(filter);

    if (index >= 0) {
      this.filters.splice(index, 1);
    }
  }

  selected(event: MatAutocompleteSelectedEvent): void {
    this.filterInput.nativeElement.value = event.option.viewValue;
    this.myControl.setValue(null);
  }

  private _filter(value: string): string[] {
    const filterValue = value.toLowerCase();

    return this.options.filter(filter=> filter.toLowerCase().indexOf(filterValue) === 0);
  }

  // createForm() {
  //   this.form = this.formBuilder.group({
  //     id: null,
  //     query: this.query.query,
  //     ip: [this.query.id, [Validators.pattern('^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$')]],
  //     country: [this.query.country],
  //     device: [this.query.device],
  //     geo: [this.query.geo],
  //     hostname: [this.query.hostname],
  //     isp: [this.query.isp],
  //     link: [this.query.link],
  //     net: [this.query.net],
  //     org: [this.query.org],
  //     os: [this.query.os],
  //     port: [this.query.port],
  //     postal: [this.query.postal],
  //     product: [this.query.product],
  //     region: [this.query.region],
  //     state: [this.query.state],
  //     version: [this.query.version],
  //   });
  // }

  searchDevices() {
    this.query.query = this.filters.join(' ');
    this.searchQuery.emit(this.query);
  }

}
