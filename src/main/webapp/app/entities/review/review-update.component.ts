import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IReview, Review } from 'app/shared/model/review.model';
import { ReviewService } from './review.service';
import { ITask } from 'app/shared/model/task.model';
import { TaskService } from 'app/entities/task/task.service';
import { IUserExtend } from 'app/shared/model/user-extend.model';
import { UserExtendService } from 'app/entities/user-extend/user-extend.service';

@Component({
  selector: 'jhi-review-update',
  templateUrl: './review-update.component.html'
})
export class ReviewUpdateComponent implements OnInit {
  isSaving: boolean;

  tasks: ITask[];

  userextends: IUserExtend[];

  editForm = this.fb.group({
    id: [],
    content: [],
    start: [],
    status: [],
    createdAt: [],
    updatedAt: [],
    deletedAt: [],
    taskId: [],
    userId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected reviewService: ReviewService,
    protected taskService: TaskService,
    protected userExtendService: UserExtendService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ review }) => {
      this.updateForm(review);
    });
    this.taskService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ITask[]>) => mayBeOk.ok),
        map((response: HttpResponse<ITask[]>) => response.body)
      )
      .subscribe((res: ITask[]) => (this.tasks = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.userExtendService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IUserExtend[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUserExtend[]>) => response.body)
      )
      .subscribe((res: IUserExtend[]) => (this.userextends = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(review: IReview) {
    this.editForm.patchValue({
      id: review.id,
      content: review.content,
      start: review.start,
      status: review.status,
      createdAt: review.createdAt != null ? review.createdAt.format(DATE_TIME_FORMAT) : null,
      updatedAt: review.updatedAt != null ? review.updatedAt.format(DATE_TIME_FORMAT) : null,
      deletedAt: review.deletedAt != null ? review.deletedAt.format(DATE_TIME_FORMAT) : null,
      taskId: review.taskId,
      userId: review.userId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const review = this.createFromForm();
    if (review.id !== undefined) {
      this.subscribeToSaveResponse(this.reviewService.update(review));
    } else {
      this.subscribeToSaveResponse(this.reviewService.create(review));
    }
  }

  private createFromForm(): IReview {
    return {
      ...new Review(),
      id: this.editForm.get(['id']).value,
      content: this.editForm.get(['content']).value,
      start: this.editForm.get(['start']).value,
      status: this.editForm.get(['status']).value,
      createdAt:
        this.editForm.get(['createdAt']).value != null ? moment(this.editForm.get(['createdAt']).value, DATE_TIME_FORMAT) : undefined,
      updatedAt:
        this.editForm.get(['updatedAt']).value != null ? moment(this.editForm.get(['updatedAt']).value, DATE_TIME_FORMAT) : undefined,
      deletedAt:
        this.editForm.get(['deletedAt']).value != null ? moment(this.editForm.get(['deletedAt']).value, DATE_TIME_FORMAT) : undefined,
      taskId: this.editForm.get(['taskId']).value,
      userId: this.editForm.get(['userId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReview>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackTaskById(index: number, item: ITask) {
    return item.id;
  }

  trackUserExtendById(index: number, item: IUserExtend) {
    return item.id;
  }
}
