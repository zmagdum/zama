import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { SalesPerson } from './sales-person.model';
import { SalesPersonPopupService } from './sales-person-popup.service';
import { SalesPersonService } from './sales-person.service';
import { Department, DepartmentService } from '../department';
import { CustomerOrder, CustomerOrderService } from '../customer-order';

@Component({
    selector: 'jhi-sales-person-dialog',
    templateUrl: './sales-person-dialog.component.html'
})
export class SalesPersonDialogComponent implements OnInit {

    salesPerson: SalesPerson;
    authorities: any[];
    isSaving: boolean;

    departments: Department[];

    customerorders: CustomerOrder[];
    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private salesPersonService: SalesPersonService,
        private departmentService: DepartmentService,
        private customerOrderService: CustomerOrderService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.departmentService.query().subscribe(
            (res: Response) => { this.departments = res.json(); }, (res: Response) => this.onError(res.json()));
        this.customerOrderService.query().subscribe(
            (res: Response) => { this.customerorders = res.json(); }, (res: Response) => this.onError(res.json()));
    }
    clear () {
        this.activeModal.dismiss('cancel');
    }

    save () {
        this.isSaving = true;
        if (this.salesPerson.id !== undefined) {
            this.salesPersonService.update(this.salesPerson)
                .subscribe((res: SalesPerson) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        } else {
            this.salesPersonService.create(this.salesPerson)
                .subscribe((res: SalesPerson) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        }
    }

    private onSaveSuccess (result: SalesPerson) {
        this.eventManager.broadcast({ name: 'salesPersonListModification', content: 'OK'});
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

    trackDepartmentById(index: number, item: Department) {
        return item.id;
    }

    trackCustomerOrderById(index: number, item: CustomerOrder) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-sales-person-popup',
    template: ''
})
export class SalesPersonPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private salesPersonPopupService: SalesPersonPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if ( params['id'] ) {
                this.modalRef = this.salesPersonPopupService
                    .open(SalesPersonDialogComponent, params['id']);
            } else {
                this.modalRef = this.salesPersonPopupService
                    .open(SalesPersonDialogComponent);
            }

        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
