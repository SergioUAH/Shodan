import { Component, OnInit, Input, ChangeDetectorRef, ViewChild, ElementRef } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { FormBuilder, FormGroup, FormControl } from '@angular/forms';
import { startWith, map } from 'rxjs/operators';
import { MatChipInputEvent } from '@angular/material/chips';
import { MatAutocompleteSelectedEvent, MatAutocomplete } from '@angular/material/autocomplete';
import { FILTROS_TABLA, FILTROS_SHODAN } from '../filtro/filtros';
import { SPACE, TAB } from '@angular/cdk/keycodes';
import { Observable } from 'rxjs';
@Component({
  selector: 'app-filtro',
  templateUrl: './filtro.component.html',
  styleUrls: ['./filtro.component.scss']
})
export class FiltroComponent implements OnInit {
  @Input() isTableFilter: Boolean;
  @Input() data: MatTableDataSource<any>;
  search: string;
  columnas;

  selectedColumn = 'nombre';
  dataConst: MatTableDataSource<any>;
  formBuilder: FormBuilder;
  form: FormGroup;
  stringQuery: string;

  myControl = new FormControl();
  options;
  filteredOptions: Observable<string[]>;
  visible = true;
  selectable = true;
  removable = true;
  separatorKeysCodes: number[] = [SPACE, TAB];
  filters: string[] = [];

  @ViewChild('filterInput') filterInput: ElementRef<HTMLInputElement>;
  @ViewChild('auto') matAutocomplete: MatAutocomplete;
  constructor(private cdr: ChangeDetectorRef) { }

  ngOnInit() {
    console.log(this.data);
    this.dataConst = this.data;
    // if(this.isTableFilter) {
    //   this.options = FILTROS_TABLA;
    //   this.stringQuery = "";
    //   this.formBuilder = new FormBuilder();
    //   //this.createForm();
    //   this.filteredOptions = this.myControl.valueChanges.pipe(
    //   startWith(null),
    //   map((filter: string | null) => filter ? this._filter(filter) : this.options.slice()));
    //   // this.initFilter();
    // } else {
    //   this.options = FILTROS_SHODAN;
    // }
    this.cdr.markForCheck();
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataConst.filter = filterValue.trim().toLowerCase();
  }

  ngAfterViewInit() {
    this.cdr.markForCheck();
    console.log(this.data);
  }

  // private initFilter() {
  //   this.data.filterPredicate =
  //   (data: any, filtersJson: string) => {
  //     const matchFilter = [];
  //     const filters = JSON.parse(filtersJson);

  //     filters.forEach(filter => {
  //       const val = data[filter.id] === null ? '' : String(data[filter.id]);
  //         matchFilter.push(val.toLowerCase().includes(String(filter.value).toLowerCase()));
  //     });
  //     return matchFilter.every(Boolean);
  //   };
  // }

  // applyFilter(filterValue: string) {
  //   if(filterValue == undefined){
  //     return;
  //   }
  //   //this.applyDateFilters();
  //   this.search=filterValue;
  //   const tableFilters = [];
  //   tableFilters.push({
  //     id: this.selectedColumn,
  //     value: filterValue
  //   });

  //   this.data.filter = JSON.stringify(tableFilters);
  //   if (this.data.paginator) {
  //     this.data.paginator.firstPage();
  //   }
  // }

  // getValue(column : String) {
  //   this.columnas.get(column);
  // }

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
}
