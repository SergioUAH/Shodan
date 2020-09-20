import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

//Angular Material Modules
import { MatSelectModule } from '@angular/material/select';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatOptionModule } from '@angular/material/core';
import { MatButtonModule } from '@angular/material/button';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTableModule } from '@angular/material/table';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatChipsModule, MAT_CHIPS_DEFAULT_OPTIONS } from '@angular/material/chips';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { MatListModule } from '@angular/material/list';
import { MatTooltipModule } from '@angular/material/tooltip';
import { DashboardComponent } from './views/dashboard/dashboard.component';

import { ModalComponent } from './views/common/modal/modal.component';
import { FiltroComponent } from './views/common/filtro/filtro.component';
import { TablaComponent } from './views/common/tabla/tabla.component';
import { RestService } from './config/services/rest-service';
import { WebSocketService } from './config/services/web-socket-service';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { QueryFormComponent } from './views/common/query-form/query-form.component';
import { ENTER, COMMA, TAB } from '@angular/cdk/keycodes';
import { DivisorComponent } from './views/common/divisor/divisor.component';
import { TestSecurityModalComponent } from './views/common/test-security-modal/test-security-modal.component';
import { ImportFilesComponent } from './views/import-files/import-files.component';
import { NgxFileDropModule } from 'ngx-file-drop';

@NgModule({
  declarations: [
    AppComponent,
    TablaComponent,
    ModalComponent,
    FiltroComponent,
    DashboardComponent,
    QueryFormComponent,
    DivisorComponent,
    TestSecurityModalComponent,
    ImportFilesComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    //Angular Material Modules
    MatSelectModule,
    MatDialogModule,
    MatFormFieldModule,
    MatOptionModule,
    MatButtonModule,
    MatCheckboxModule,
    MatPaginatorModule,
    MatSortModule,
    MatProgressSpinnerModule,
    MatInputModule,
    MatTableModule,
    MatAutocompleteModule,
    MatCardModule,
    MatSlideToggleModule,
    MatChipsModule,
    MatIconModule,
    MatDividerModule,
    MatListModule,
    MatTooltipModule,
    HttpClientModule,

    RouterTestingModule,
    ReactiveFormsModule,
    FormsModule,
    NgxFileDropModule,
  ],
  providers: [
    RestService,
    WebSocketService,
    { provide: MAT_CHIPS_DEFAULT_OPTIONS,
      useValue: {
        separatorKeyCodes: [TAB, COMMA]
      }
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
