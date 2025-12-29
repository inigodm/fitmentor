import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModifyCoach } from './modify-coach';

describe('ModifyCoach', () => {
  let component: ModifyCoach;
  let fixture: ComponentFixture<ModifyCoach>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ModifyCoach]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModifyCoach);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
