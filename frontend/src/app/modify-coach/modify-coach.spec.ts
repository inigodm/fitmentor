import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ModifyCoach } from './modify-coach';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

describe('ModifyCoach', () => {
  let component: ModifyCoach;
  let fixture: ComponentFixture<ModifyCoach>;
  let httpMock: HttpTestingController;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        ModifyCoach,
        HttpClientTestingModule,
        RouterTestingModule,
        FormsModule
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModifyCoach);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize with default values', () => {
    expect(component.isNutritionist).toBeFalse();
    expect(component.isFitness).toBeFalse();
    expect(component.phonenumber).toBe('');
    expect(component.presentation).toBe('');
  });

  it('should send correct data including checkboxes when modifyCoach is called', async () => {
    // Set component properties
    component.user = 'test-user-id';
    component.username = 'testuser';
    component.email = 'test@example.com';
    component.phonenumber = '123456789';
    component.presentation = 'Test Presentation';
    component.isNutritionist = true;
    component.isFitness = false;

    // Trigger the method
    await component.modifyCoach();

    // Expect a POST request
    const req = httpMock.expectOne('/api/user/coaches');
    expect(req.request.method).toBe('POST');

    // Verify request body
    expect(req.request.body).toEqual(jasmine.objectContaining({
      user: 'test-user-id',
      username: 'testuser',
      email: 'test@example.com',
      phonenumber: '123456789',
      presentation: 'Test Presentation',
      isNutritionist: true,
      isFitness: false
    }));

    // Respond with success
    req.flush('OK');
  });

  it('should send both checkboxes as true if selected', async () => {
    component.isNutritionist = true;
    component.isFitness = true;

    await component.modifyCoach();

    const req = httpMock.expectOne('/api/user/coaches');
    expect(req.request.body.isNutritionist).toBeTrue();
    expect(req.request.body.isFitness).toBeTrue();

    req.flush('OK');
  });
});
