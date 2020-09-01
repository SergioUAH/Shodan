import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TestSecurityModalComponent } from './test-security-modal.component';

describe('TestSecurityModalComponent', () => {
  let component: TestSecurityModalComponent;
  let fixture: ComponentFixture<TestSecurityModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TestSecurityModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TestSecurityModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
