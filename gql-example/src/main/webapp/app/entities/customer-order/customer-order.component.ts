import { Component, OnInit, OnDestroy } from '@angular/core';
import { Response } from '@angular/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager, ParseLinks, PaginationUtil, AlertService } from 'ng-jhipster';

import { CustomerOrder } from './customer-order.model';
import { CustomerOrderService } from './customer-order.service';
import { ITEMS_PER_PAGE, Principal } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-customer-order',
    templateUrl: './customer-order.component.html'
})
export class CustomerOrderComponent implements OnInit, OnDestroy {
customerOrders: CustomerOrder[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private customerOrderService: CustomerOrderService,
        private alertService: AlertService,
        private eventManager: EventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.customerOrderService.query().subscribe(
            (res: Response) => {
                this.customerOrders = res.json();
            },
            (res: Response) => this.onError(res.json())
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInCustomerOrders();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId (index: number, item: CustomerOrder) {
        return item.id;
    }



    registerChangeInCustomerOrders() {
        this.eventSubscriber = this.eventManager.subscribe('customerOrderListModification', (response) => this.loadAll());
    }


    private onError (error) {
        this.alertService.error(error.message, null, null);
    }
}
