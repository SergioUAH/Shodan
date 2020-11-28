import { Component, OnInit, Input, ChangeDetectorRef, ViewChild, ElementRef, EventEmitter, Output } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { FormBuilder, FormGroup, FormControl } from '@angular/forms';
import { startWith, map } from 'rxjs/operators';
import { MatChipInputEvent } from '@angular/material/chips';
import { MatAutocompleteSelectedEvent, MatAutocomplete } from '@angular/material/autocomplete';
import { FILTROS_TABLA, FILTROS_SHODAN } from '../filtro/filtros';
import { SPACE, TAB } from '@angular/cdk/keycodes';
import { Observable } from 'rxjs';
import { RestService } from 'src/app/config/services/rest-service';
import { environment } from 'src/environments/environment';

const GET_WORDLISTS_URL = "/files/getWordlists";

@Component({
  selector: 'app-filtro',
  templateUrl: './filtro.component.html',
  styleUrls: ['./filtro.component.scss']
})
export class FiltroComponent implements OnInit {
  @Input() isTableFilter: Boolean;
  @Input() data: MatTableDataSource<any>;
  @Input() wordlists: string[];
  search: string;

  userWordlist: string;
  passWordlist: string;
  dataConst: MatTableDataSource<any>;
  formBuilder: FormBuilder;
  form: FormGroup;
  stringQuery: string;

  myControl = new FormControl();
  filteredOptions: Observable<string[]>;
  visible = true;
  selectable = true;
  removable = true;
  filters: string[] = [];

  loading: boolean;
  responseData: any;

  @Output() updateWordlists: EventEmitter<any> = new EventEmitter<any>();

  @ViewChild('filterInput') filterInput: ElementRef<HTMLInputElement>;
  @ViewChild('auto') matAutocomplete: MatAutocomplete;

  constructor(public http: RestService, public cdr: ChangeDetectorRef) { }

  ngOnInit() {
    this.dataConst = this.data;
    this.userWordlist = this.wordlists[0];
    this.passWordlist = this.wordlists[0];
    this.cdr.markForCheck();
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataConst.filter = filterValue.trim().toLowerCase();
  }

  updateWordlistEvent() {
    let wordlistsSelected = [
      this.userWordlist,
      this.passWordlist
    ];
    this.updateWordlists.emit(wordlistsSelected);
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

}
