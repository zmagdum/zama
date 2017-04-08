import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { ProductComponent } from './product.component';
import { ProductDetailComponent } from './product-detail.component';
import { ProductPopupComponent } from './product-dialog.component';
import { ProductDeletePopupComponent } from './product-delete-dialog.component';

import { Principal } from '../../shared';


export const productRoute: Routes = [
  {
    path: 'product',
    component: ProductComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Products'
    }
  }, {
    path: 'product/:id',
    component: ProductDetailComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Products'
    }
  }
];

export const productPopupRoute: Routes = [
  {
    path: 'product-new',
    component: ProductPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Products'
    },
    outlet: 'popup'
  },
  {
    path: 'product/:id/edit',
    component: ProductPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Products'
    },
    outlet: 'popup'
  },
  {
    path: 'product/:id/delete',
    component: ProductDeletePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Products'
    },
    outlet: 'popup'
  }
];
