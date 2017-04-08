import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { MockBackend } from '@angular/http/testing';
import { Http, BaseRequestOptions } from '@angular/http';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils } from 'ng-jhipster';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { SalesPersonDetailComponent } from '../../../../../../main/webapp/app/entities/sales-person/sales-person-detail.component';
import { SalesPersonService } from '../../../../../../main/webapp/app/entities/sales-person/sales-person.service';
import { SalesPerson } from '../../../../../../main/webapp/app/entities/sales-person/sales-person.model';

describe('Component Tests', () => {

    describe('SalesPerson Management Detail Component', () => {
        let comp: SalesPersonDetailComponent;
        let fixture: ComponentFixture<SalesPersonDetailComponent>;
        let service: SalesPersonService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                declarations: [SalesPersonDetailComponent],
                providers: [
                    MockBackend,
                    BaseRequestOptions,
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    {
                        provide: Http,
                        useFactory: (backendInstance: MockBackend, defaultOptions: BaseRequestOptions) => {
                            return new Http(backendInstance, defaultOptions);
                        },
                        deps: [MockBackend, BaseRequestOptions]
                    },
                    SalesPersonService
                ]
            }).overrideComponent(SalesPersonDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SalesPersonDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SalesPersonService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new SalesPerson(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.salesPerson).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
