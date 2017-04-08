import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GqlExampleSharedModule } from '../../shared';

import {
    CustomerOrderService,
    CustomerOrderPopupService,
    CustomerOrderComponent,
    CustomerOrderDetailComponent,
    CustomerOrderDialogComponent,
    CustomerOrderPopupComponent,
    CustomerOrderDeletePopupComponent,
    CustomerOrderDeleteDialogComponent,
    customerOrderRoute,
    customerOrderPopupRoute,
} from './';

let ENTITY_STATES = [
    ...customerOrderRoute,
    ...customerOrderPopupRoute,
];

@NgModule({
    imports: [
        GqlExampleSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        CustomerOrderComponent,
        CustomerOrderDetailComponent,
        CustomerOrderDialogComponent,
        CustomerOrderDeleteDialogComponent,
        CustomerOrderPopupComponent,
        CustomerOrderDeletePopupComponent,
    ],
    entryComponents: [
        CustomerOrderComponent,
        CustomerOrderDialogComponent,
        CustomerOrderPopupComponent,
        CustomerOrderDeleteDialogComponent,
        CustomerOrderDeletePopupComponent,
    ],
    providers: [
        CustomerOrderService,
        CustomerOrderPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GqlExampleCustomerOrderModule {}
