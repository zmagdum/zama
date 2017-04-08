import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { SalesPersonComponent } from './sales-person.component';
import { SalesPersonDetailComponent } from './sales-person-detail.component';
import { SalesPersonPopupComponent } from './sales-person-dialog.component';
import { SalesPersonDeletePopupComponent } from './sales-person-delete-dialog.component';

import { Principal } from '../../shared';


export const salesPersonRoute: Routes = [
  {
    path: 'sales-person',
    component: SalesPersonComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'SalesPeople'
    }
  }, {
    path: 'sales-person/:id',
    component: SalesPersonDetailComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'SalesPeople'
    }
  }
];

export const salesPersonPopupRoute: Routes = [
  {
    path: 'sales-person-new',
    component: SalesPersonPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'SalesPeople'
    },
    outlet: 'popup'
  },
  {
    path: 'sales-person/:id/edit',
    component: SalesPersonPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'SalesPeople'
    },
    outlet: 'popup'
  },
  {
    path: 'sales-person/:id/delete',
    component: SalesPersonDeletePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'SalesPeople'
    },
    outlet: 'popup'
  }
];
