import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { CustomerOrderComponent } from './customer-order.component';
import { CustomerOrderDetailComponent } from './customer-order-detail.component';
import { CustomerOrderPopupComponent } from './customer-order-dialog.component';
import { CustomerOrderDeletePopupComponent } from './customer-order-delete-dialog.component';

import { Principal } from '../../shared';


export const customerOrderRoute: Routes = [
  {
    path: 'customer-order',
    component: CustomerOrderComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'CustomerOrders'
    }
  }, {
    path: 'customer-order/:id',
    component: CustomerOrderDetailComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'CustomerOrders'
    }
  }
];

export const customerOrderPopupRoute: Routes = [
  {
    path: 'customer-order-new',
    component: CustomerOrderPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'CustomerOrders'
    },
    outlet: 'popup'
  },
  {
    path: 'customer-order/:id/edit',
    component: CustomerOrderPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'CustomerOrders'
    },
    outlet: 'popup'
  },
  {
    path: 'customer-order/:id/delete',
    component: CustomerOrderDeletePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'CustomerOrders'
    },
    outlet: 'popup'
  }
];
