import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { CustomerOrder } from './customer-order.model';
import { CustomerOrderPopupService } from './customer-order-popup.service';
import { CustomerOrderService } from './customer-order.service';
import { SalesPerson, SalesPersonService } from '../sales-person';
import { OrderItem, OrderItemService } from '../order-item';
import { Customer, CustomerService } from '../customer';

@Component({
    selector: 'jhi-customer-order-dialog',
    templateUrl: './customer-order-dialog.component.html'
})
export class CustomerOrderDialogComponent implements OnInit {

    customerOrder: CustomerOrder;
    authorities: any[];
    isSaving: boolean;

    salespeople: SalesPerson[];

    orderitems: OrderItem[];

    customers: Customer[];
    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private customerOrderService: CustomerOrderService,
        private salesPersonService: SalesPersonService,
        private orderItemService: OrderItemService,
        private customerService: CustomerService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.salesPersonService.query().subscribe(
            (res: Response) => { this.salespeople = res.json(); }, (res: Response) => this.onError(res.json()));
        this.orderItemService.query().subscribe(
            (res: Response) => { this.orderitems = res.json(); }, (res: Response) => this.onError(res.json()));
        this.customerService.query().subscribe(
            (res: Response) => { this.customers = res.json(); }, (res: Response) => this.onError(res.json()));
    }
    clear () {
        this.activeModal.dismiss('cancel');
    }

    save () {
        this.isSaving = true;
        if (this.customerOrder.id !== undefined) {
            this.customerOrderService.update(this.customerOrder)
                .subscribe((res: CustomerOrder) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        } else {
            this.customerOrderService.create(this.customerOrder)
                .subscribe((res: CustomerOrder) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        }
    }

    private onSaveSuccess (result: CustomerOrder) {
        this.eventManager.broadcast({ name: 'customerOrderListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError (error) {
        this.isSaving = false;
        this.onError(error);
    }

    private onError (error) {
        this.alertService.error(error.message, null, null);
    }

    trackSalesPersonById(index: number, item: SalesPerson) {
        return item.id;
    }

    trackOrderItemById(index: number, item: OrderItem) {
        return item.id;
    }

    trackCustomerById(index: number, item: Customer) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-customer-order-popup',
    template: ''
})
export class CustomerOrderPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private customerOrderPopupService: CustomerOrderPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if ( params['id'] ) {
                this.modalRef = this.customerOrderPopupService
                    .open(CustomerOrderDialogComponent, params['id']);
            } else {
                this.modalRef = this.customerOrderPopupService
                    .open(CustomerOrderDialogComponent);
            }

        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
