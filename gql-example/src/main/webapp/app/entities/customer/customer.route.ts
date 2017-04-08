import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { CustomerComponent } from './customer.component';
import { CustomerDetailComponent } from './customer-detail.component';
import { CustomerPopupComponent } from './customer-dialog.component';
import { CustomerDeletePopupComponent } from './customer-delete-dialog.component';

import { Principal } from '../../shared';


export const customerRoute: Routes = [
  {
    path: 'customer',
    component: CustomerComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Customers'
    }
  }, {
    path: 'customer/:id',
    component: CustomerDetailComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Customers'
    }
  }
];

export const customerPopupRoute: Routes = [
  {
    path: 'customer-new',
    component: CustomerPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Customers'
    },
    outlet: 'popup'
  },
  {
    path: 'customer/:id/edit',
    component: CustomerPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Customers'
    },
    outlet: 'popup'
  },
  {
    path: 'customer/:id/delete',
    component: CustomerDeletePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Customers'
    },
    outlet: 'popup'
  }
];
