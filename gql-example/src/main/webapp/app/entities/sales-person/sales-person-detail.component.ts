import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { SalesPerson } from './sales-person.model';
import { SalesPersonService } from './sales-person.service';

@Component({
    selector: 'jhi-sales-person-detail',
    templateUrl: './sales-person-detail.component.html'
})
export class SalesPersonDetailComponent implements OnInit, OnDestroy {

    salesPerson: SalesPerson;
    private subscription: any;

    constructor(
        private salesPersonService: SalesPersonService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
    }

    load (id) {
        this.salesPersonService.find(id).subscribe(salesPerson => {
            this.salesPerson = salesPerson;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

}
