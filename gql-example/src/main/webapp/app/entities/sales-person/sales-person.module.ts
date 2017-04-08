import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GqlExampleSharedModule } from '../../shared';

import {
    SalesPersonService,
    SalesPersonPopupService,
    SalesPersonComponent,
    SalesPersonDetailComponent,
    SalesPersonDialogComponent,
    SalesPersonPopupComponent,
    SalesPersonDeletePopupComponent,
    SalesPersonDeleteDialogComponent,
    salesPersonRoute,
    salesPersonPopupRoute,
} from './';

let ENTITY_STATES = [
    ...salesPersonRoute,
    ...salesPersonPopupRoute,
];

@NgModule({
    imports: [
        GqlExampleSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        SalesPersonComponent,
        SalesPersonDetailComponent,
        SalesPersonDialogComponent,
        SalesPersonDeleteDialogComponent,
        SalesPersonPopupComponent,
        SalesPersonDeletePopupComponent,
    ],
    entryComponents: [
        SalesPersonComponent,
        SalesPersonDialogComponent,
        SalesPersonPopupComponent,
        SalesPersonDeleteDialogComponent,
        SalesPersonDeletePopupComponent,
    ],
    providers: [
        SalesPersonService,
        SalesPersonPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GqlExampleSalesPersonModule {}
