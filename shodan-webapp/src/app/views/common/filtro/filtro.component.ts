import { Component, OnInit, Input, ChangeDetectorRef } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { MatSort } from '@angular/material/sort';
import { MatSelect} from '@angular/material/select';

@Component({
  selector: 'app-filtro',
  templateUrl: './filtro.component.html',
  styleUrls: ['./filtro.component.scss']
})
export class FiltroComponent implements OnInit {
  @Input() detail: number;
  @Input() hasDateFilters: boolean;
  @Input() data: MatTableDataSource<any>;
  search: string;
  initialDateStr: string;
  finalDateStr: string;
  initialDate: any;
  finalDate: any;
  columnas;

  selectedColumn = 'nombre';
  dataConst: MatTableDataSource<any>;

  constructor(private cdr: ChangeDetectorRef) { }

  ngOnInit() {
    this.dataConst = this.data;
    this.initFilter();
  }

  ngAfterViewInit() {
    this.cdr.markForCheck();
  }

  private initFilter() {
    this.data.filterPredicate =
    (data: any, filtersJson: string) => {
      const matchFilter = [];
      const filters = JSON.parse(filtersJson);

      filters.forEach(filter => {
        const val = data[filter.id] === null ? '' : String(data[filter.id]);
          matchFilter.push(val.toLowerCase().includes(String(filter.value).toLowerCase()));
      });
      return matchFilter.every(Boolean);
    };
  }

  applyFilter(filterValue: string) {
    this.ngOnInit();
    if(filterValue == undefined){
      return;
    }
    //this.applyDateFilters();
    this.search=filterValue;
    const tableFilters = [];
    tableFilters.push({
      id: this.selectedColumn,
      value: filterValue
    });

    this.data.filter = JSON.stringify(tableFilters);
    if (this.data.paginator) {
      this.data.paginator.firstPage();
    }
  }

  getValue(column : String) {
    this.columnas.get(column);
  }
}
