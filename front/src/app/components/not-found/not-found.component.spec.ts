import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NotFoundComponent } from './not-found.component';
import { expect } from '@jest/globals'; 

import { By } from '@angular/platform-browser';

describe('NotFoundComponent', () => {
  let component: NotFoundComponent;
  let fixture: ComponentFixture<NotFoundComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [NotFoundComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(NotFoundComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display a not found message', () => {
    const element = fixture.debugElement.nativeElement;
    const textContent = element.textContent.toLowerCase();
  
    expect(textContent).toContain('page not found !');
  });

  it('should render the template without errors', () => {
    const compiled = fixture.nativeElement;
    expect(compiled).toBeTruthy();
  });
});
