import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager } from 'ng-jhipster';

import { SalesPerson } from './sales-person.model';
import { SalesPersonPopupService } from './sales-person-popup.service';
import { SalesPersonService } from './sales-person.service';

@Component({
    selector: 'jhi-sales-person-delete-dialog',
    templateUrl: './sales-person-delete-dialog.component.html'
})
export class SalesPersonDeleteDialogComponent {

    salesPerson: SalesPerson;

    constructor(
        private salesPersonService: SalesPersonService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
    }

    clear () {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete (id: number) {
        this.salesPersonService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'salesPersonListModification',
                content: 'Deleted an salesPerson'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-sales-person-delete-popup',
    template: ''
})
export class SalesPersonDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private salesPersonPopupService: SalesPersonPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            this.modalRef = this.salesPersonPopupService
                .open(SalesPersonDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
