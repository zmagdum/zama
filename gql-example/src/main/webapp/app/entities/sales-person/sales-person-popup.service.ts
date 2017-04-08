import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { SalesPerson } from './sales-person.model';
import { SalesPersonService } from './sales-person.service';
@Injectable()
export class SalesPersonPopupService {
    private isOpen = false;
    constructor (
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private salesPersonService: SalesPersonService

    ) {}

    open (component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.salesPersonService.find(id).subscribe(salesPerson => {
                salesPerson.hireDate = this.datePipe
                    .transform(salesPerson.hireDate, 'yyyy-MM-ddThh:mm');
                this.salesPersonModalRef(component, salesPerson);
            });
        } else {
            return this.salesPersonModalRef(component, new SalesPerson());
        }
    }

    salesPersonModalRef(component: Component, salesPerson: SalesPerson): NgbModalRef {
        let modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.salesPerson = salesPerson;
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
