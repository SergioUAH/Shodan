import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ImportFilesComponent } from './import-files.component';

describe('ImportFilesComponent', () => {
  let component: ImportFilesComponent;
  let fixture: ComponentFixture<ImportFilesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ImportFilesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ImportFilesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
