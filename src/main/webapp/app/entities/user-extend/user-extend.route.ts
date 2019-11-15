import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { UserExtend } from 'app/shared/model/user-extend.model';
import { UserExtendService } from './user-extend.service';
import { UserExtendComponent } from './user-extend.component';
import { UserExtendDetailComponent } from './user-extend-detail.component';
import { UserExtendUpdateComponent } from './user-extend-update.component';
import { UserExtendDeletePopupComponent } from './user-extend-delete-dialog.component';
import { IUserExtend } from 'app/shared/model/user-extend.model';

@Injectable({ providedIn: 'root' })
export class UserExtendResolve implements Resolve<IUserExtend> {
  constructor(private service: UserExtendService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IUserExtend> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<UserExtend>) => response.ok),
        map((userExtend: HttpResponse<UserExtend>) => userExtend.body)
      );
    }
    return of(new UserExtend());
  }
}

export const userExtendRoute: Routes = [
  {
    path: '',
    component: UserExtendComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'taskUniteApp.userExtend.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: UserExtendDetailComponent,
    resolve: {
      userExtend: UserExtendResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'taskUniteApp.userExtend.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: UserExtendUpdateComponent,
    resolve: {
      userExtend: UserExtendResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'taskUniteApp.userExtend.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: UserExtendUpdateComponent,
    resolve: {
      userExtend: UserExtendResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'taskUniteApp.userExtend.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const userExtendPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: UserExtendDeletePopupComponent,
    resolve: {
      userExtend: UserExtendResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'taskUniteApp.userExtend.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];