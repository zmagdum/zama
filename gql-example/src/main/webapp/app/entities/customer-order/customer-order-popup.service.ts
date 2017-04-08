import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { CustomerOrder } from './customer-order.model';
import { CustomerOrderService } from './customer-order.service';
@Injectable()
export class CustomerOrderPopupService {
    private isOpen = false;
    constructor (
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private customerOrderService: CustomerOrderService

    ) {}

    open (component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.customerOrderService.find(id).subscribe(customerOrder => {
                customerOrder.orderDate = this.datePipe
                    .transform(customerOrder.orderDate, 'yyyy-MM-ddThh:mm');
                this.customerOrderModalRef(component, customerOrder);
            });
        } else {
            return this.customerOrderModalRef(component, new CustomerOrder());
        }
    }

    customerOrderModalRef(component: Component, customerOrder: CustomerOrder): NgbModalRef {
        let modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.customerOrder = customerOrder;
        modalRef.result.then(result => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        });
        return modalRef;
    }
}
