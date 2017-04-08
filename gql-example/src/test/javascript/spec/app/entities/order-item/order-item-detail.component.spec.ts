import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { MockBackend } from '@angular/http/testing';
import { Http, BaseRequestOptions } from '@angular/http';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils } from 'ng-jhipster';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { OrderItemDetailComponent } from '../../../../../../main/webapp/app/entities/order-item/order-item-detail.component';
import { OrderItemService } from '../../../../../../main/webapp/app/entities/order-item/order-item.service';
import { OrderItem } from '../../../../../../main/webapp/app/entities/order-item/order-item.model';

describe('Component Tests', () => {

    describe('OrderItem Management Detail Component', () => {
        let comp: OrderItemDetailComponent;
        let fixture: ComponentFixture<OrderItemDetailComponent>;
        let service: OrderItemService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                declarations: [OrderItemDetailComponent],
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
                    OrderItemService
                ]
            }).overrideComponent(OrderItemDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(OrderItemDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OrderItemService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new OrderItem(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.orderItem).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
