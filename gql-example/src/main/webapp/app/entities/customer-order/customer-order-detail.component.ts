import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CustomerOrder } from './customer-order.model';
import { CustomerOrderService } from './customer-order.service';

@Component({
    selector: 'jhi-customer-order-detail',
    templateUrl: './customer-order-detail.component.html'
})
export class CustomerOrderDetailComponent implements OnInit, OnDestroy {

    customerOrder: CustomerOrder;
    private subscription: any;

    constructor(
        private customerOrderService: CustomerOrderService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
    }

    load (id) {
        this.customerOrderService.find(id).subscribe(customerOrder => {
            this.customerOrder = customerOrder;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

}
