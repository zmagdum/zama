import { Component, OnInit, OnDestroy } from '@angular/core';
import { Response } from '@angular/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager, ParseLinks, PaginationUtil, AlertService } from 'ng-jhipster';

import { SalesPerson } from './sales-person.model';
import { SalesPersonService } from './sales-person.service';
import { ITEMS_PER_PAGE, Principal } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-sales-person',
    templateUrl: './sales-person.component.html'
})
export class SalesPersonComponent implements OnInit, OnDestroy {
salesPeople: SalesPerson[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private salesPersonService: SalesPersonService,
        private alertService: AlertService,
        private eventManager: EventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.salesPersonService.query().subscribe(
            (res: Response) => {
                this.salesPeople = res.json();
            },
            (res: Response) => this.onError(res.json())
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInSalesPeople();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId (index: number, item: SalesPerson) {
        return item.id;
    }



    registerChangeInSalesPeople() {
        this.eventSubscriber = this.eventManager.subscribe('salesPersonListModification', (response) => this.loadAll());
    }


    private onError (error) {
        this.alertService.error(error.message, null, null);
    }
}
