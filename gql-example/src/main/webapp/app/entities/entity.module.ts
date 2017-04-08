import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { GqlExampleCustomerModule } from './customer/customer.module';
import { GqlExampleCustomerOrderModule } from './customer-order/customer-order.module';
import { GqlExampleDepartmentModule } from './department/department.module';
import { GqlExampleLocationModule } from './location/location.module';
import { GqlExampleOrderItemModule } from './order-item/order-item.module';
import { GqlExampleProductModule } from './product/product.module';
import { GqlExampleSalesPersonModule } from './sales-person/sales-person.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        GqlExampleCustomerModule,
        GqlExampleCustomerOrderModule,
        GqlExampleDepartmentModule,
        GqlExampleLocationModule,
        GqlExampleOrderItemModule,
        GqlExampleProductModule,
        GqlExampleSalesPersonModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GqlExampleEntityModule {}
