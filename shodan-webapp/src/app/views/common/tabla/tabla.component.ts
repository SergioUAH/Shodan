import { Component, OnInit, Input, ViewChild, ChangeDetectorRef } from '@angular/core';
import { MatDialogModule, MatDialogRef, MatDialog } from '@angular/material/dialog';
import { ModalComponent } from '../modal/modal.component';
import { RestService } from '../../../config/services/rest-service';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { MatSort } from '@angular/material/sort';
import { SelectionModel } from '@angular/cdk/collections';
import { environment } from 'src/environments/environment';
import { TestSecurityModalComponent } from '../test-security-modal/test-security-modal.component';

const REST_URL_DELETE_DEVICES = "/target/deleteHosts";
@Component({
  selector: 'app-tabla',
  templateUrl: './tabla.component.html',
  styleUrls: ['./tabla.component.scss']
})
export class TablaComponent implements OnInit {

  @Input() displayedColumns: string[];
  @Input() hasPaginator: true | false;
  @Input() data: MatTableDataSource<any>;

  dataSource;
  responseData;
  loading = true;
  selection = new SelectionModel<any>(true, []);

  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  @ViewChild(MatSort, {static: true}) sort: MatSort;
  constructor(public dialog: MatDialog, private cdr: ChangeDetectorRef, public http: RestService) { }

  ngOnInit() {
    this.dataSource = this.data;
    this.cdr.markForCheck();
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  showDetail(row) {
    const dialogRef = this.dialog.open(ModalComponent,
      {
        width: "30%",
        height: "50%",
        data:
        {
          isDetail: true,
          content: row,
        }
      });
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.testDevices(result);
      }
    });
  }

  /** Whether the number of selected elements matches the total number of rows. */
  isAllSelected() {
    const numSelected = this.selection.selected.length;
    const numRows = this.dataSource.data.length;
    return numSelected === numRows;
  }

  /** Selects all rows if they are not all selected; otherwise clear selection. */
  masterToggle() {
    this.isAllSelected() ?
        this.selection.clear() :
        this.dataSource.data.forEach(row => this.selection.select(row));
  }

  checkboxLabel(row?: any): string {
    if (!row) {
      return `${this.isAllSelected() ? 'select' : 'deselect'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.position + 1}`;
  }

  testDevices(id) {
    let devices = id ? [id] : this.selection.selected.map(device => device.id);
    const dialogRef = this.dialog.open(TestSecurityModalComponent,
      {
        width: "30%",
        height: "50%",
        data:
        {
          isDetail: true,
          content: devices,
        }
      });
    dialogRef.afterClosed();
  }

  deleteDevices() {
    this.loading = true;
    this.http.getCall(environment.url + REST_URL_DELETE_DEVICES)
    .subscribe(data => {
      this.responseData = data;
      console.log(this.responseData);
      this.dataSource = new MatTableDataSource();
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
      this.loading = false;
      this.cdr.markForCheck();
    },
    error => {
      console.log("Error", error);
      this.dataSource = new MatTableDataSource();
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
      this.loading = false;
      this.cdr.markForCheck();
    });
  }

}
