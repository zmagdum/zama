import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { OrderItem } from './order-item.model';
import { OrderItemService } from './order-item.service';

@Component({
    selector: 'jhi-order-item-detail',
    templateUrl: './order-item-detail.component.html'
})
export class OrderItemDetailComponent implements OnInit, OnDestroy {

    orderItem: OrderItem;
    private subscription: any;

    constructor(
        private orderItemService: OrderItemService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
    }

    load (id) {
        this.orderItemService.find(id).subscribe(orderItem => {
            this.orderItem = orderItem;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

}
