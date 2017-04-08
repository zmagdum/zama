import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { OrderItemComponent } from './order-item.component';
import { OrderItemDetailComponent } from './order-item-detail.component';
import { OrderItemPopupComponent } from './order-item-dialog.component';
import { OrderItemDeletePopupComponent } from './order-item-delete-dialog.component';

import { Principal } from '../../shared';


export const orderItemRoute: Routes = [
  {
    path: 'order-item',
    component: OrderItemComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'OrderItems'
    }
  }, {
    path: 'order-item/:id',
    component: OrderItemDetailComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'OrderItems'
    }
  }
];

export const orderItemPopupRoute: Routes = [
  {
    path: 'order-item-new',
    component: OrderItemPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'OrderItems'
    },
    outlet: 'popup'
  },
  {
    path: 'order-item/:id/edit',
    component: OrderItemPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'OrderItems'
    },
    outlet: 'popup'
  },
  {
    path: 'order-item/:id/delete',
    component: OrderItemDeletePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'OrderItems'
    },
    outlet: 'popup'
  }
];
