import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModifyClient } from './modify-client';

describe('ModifyClient', () => {
  let component: ModifyClient;
  let fixture: ComponentFixture<ModifyClient>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ModifyClient]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModifyClient);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
