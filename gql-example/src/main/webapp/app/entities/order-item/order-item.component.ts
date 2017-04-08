import { Component, OnInit, OnDestroy } from '@angular/core';
import { Response } from '@angular/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager, ParseLinks, PaginationUtil, AlertService } from 'ng-jhipster';

import { OrderItem } from './order-item.model';
import { OrderItemService } from './order-item.service';
import { ITEMS_PER_PAGE, Principal } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-order-item',
    templateUrl: './order-item.component.html'
})
export class OrderItemComponent implements OnInit, OnDestroy {
orderItems: OrderItem[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private orderItemService: OrderItemService,
        private alertService: AlertService,
        private eventManager: EventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.orderItemService.query().subscribe(
            (res: Response) => {
                this.orderItems = res.json();
            },
            (res: Response) => this.onError(res.json())
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInOrderItems();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId (index: number, item: OrderItem) {
        return item.id;
    }



    registerChangeInOrderItems() {
        this.eventSubscriber = this.eventManager.subscribe('orderItemListModification', (response) => this.loadAll());
    }


    private onError (error) {
        this.alertService.error(error.message, null, null);
    }
}
