import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Review } from 'app/shared/model/review.model';
import { ReviewService } from './review.service';
import { ReviewComponent } from './review.component';
import { ReviewDetailComponent } from './review-detail.component';
import { ReviewUpdateComponent } from './review-update.component';
import { ReviewDeletePopupComponent } from './review-delete-dialog.component';
import { IReview } from 'app/shared/model/review.model';

@Injectable({ providedIn: 'root' })
export class ReviewResolve implements Resolve<IReview> {
  constructor(private service: ReviewService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IReview> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Review>) => response.ok),
        map((review: HttpResponse<Review>) => review.body)
      );
    }
    return of(new Review());
  }
}

export const reviewRoute: Routes = [
  {
    path: '',
    component: ReviewComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'taskUniteApp.review.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ReviewDetailComponent,
    resolve: {
      review: ReviewResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'taskUniteApp.review.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ReviewUpdateComponent,
    resolve: {
      review: ReviewResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'taskUniteApp.review.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ReviewUpdateComponent,
    resolve: {
      review: ReviewResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'taskUniteApp.review.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const reviewPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ReviewDeletePopupComponent,
    resolve: {
      review: ReviewResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'taskUniteApp.review.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
