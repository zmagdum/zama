import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { LocationComponent } from './location.component';
import { LocationDetailComponent } from './location-detail.component';
import { LocationPopupComponent } from './location-dialog.component';
import { LocationDeletePopupComponent } from './location-delete-dialog.component';

import { Principal } from '../../shared';


export const locationRoute: Routes = [
  {
    path: 'location',
    component: LocationComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Locations'
    }
  }, {
    path: 'location/:id',
    component: LocationDetailComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Locations'
    }
  }
];

export const locationPopupRoute: Routes = [
  {
    path: 'location-new',
    component: LocationPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Locations'
    },
    outlet: 'popup'
  },
  {
    path: 'location/:id/edit',
    component: LocationPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Locations'
    },
    outlet: 'popup'
  },
  {
    path: 'location/:id/delete',
    component: LocationDeletePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Locations'
    },
    outlet: 'popup'
  }
];
