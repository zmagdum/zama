import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager } from 'ng-jhipster';

import { CustomerOrder } from './customer-order.model';
import { CustomerOrderPopupService } from './customer-order-popup.service';
import { CustomerOrderService } from './customer-order.service';

@Component({
    selector: 'jhi-customer-order-delete-dialog',
    templateUrl: './customer-order-delete-dialog.component.html'
})
export class CustomerOrderDeleteDialogComponent {

    customerOrder: CustomerOrder;

    constructor(
        private customerOrderService: CustomerOrderService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
    }

    clear () {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete (id: number) {
        this.customerOrderService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'customerOrderListModification',
                content: 'Deleted an customerOrder'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-customer-order-delete-popup',
    template: ''
})
export class CustomerOrderDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private customerOrderPopupService: CustomerOrderPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            this.modalRef = this.customerOrderPopupService
                .open(CustomerOrderDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
