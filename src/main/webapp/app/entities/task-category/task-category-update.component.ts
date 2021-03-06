import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { ITaskCategory, TaskCategory } from 'app/shared/model/task-category.model';
import { TaskCategoryService } from './task-category.service';

@Component({
  selector: 'jhi-task-category-update',
  templateUrl: './task-category-update.component.html'
})
export class TaskCategoryUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    name: [],
    description: [],
    image: [],
    minPrice: [],
    createdAt: [],
    updatedAt: [],
    deletedAt: []
  });

  constructor(protected taskCategoryService: TaskCategoryService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ taskCategory }) => {
      this.updateForm(taskCategory);
    });
  }

  updateForm(taskCategory: ITaskCategory) {
    this.editForm.patchValue({
      id: taskCategory.id,
      name: taskCategory.name,
      description: taskCategory.description,
      image: taskCategory.image,
      minPrice: taskCategory.minPrice,
      createdAt: taskCategory.createdAt != null ? taskCategory.createdAt.format(DATE_TIME_FORMAT) : null,
      updatedAt: taskCategory.updatedAt != null ? taskCategory.updatedAt.format(DATE_TIME_FORMAT) : null,
      deletedAt: taskCategory.deletedAt != null ? taskCategory.deletedAt.format(DATE_TIME_FORMAT) : null
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const taskCategory = this.createFromForm();
    if (taskCategory.id !== undefined) {
      this.subscribeToSaveResponse(this.taskCategoryService.update(taskCategory));
    } else {
      this.subscribeToSaveResponse(this.taskCategoryService.create(taskCategory));
    }
  }

  private createFromForm(): ITaskCategory {
    return {
      ...new TaskCategory(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      description: this.editForm.get(['description']).value,
      image: this.editForm.get(['image']).value,
      minPrice: this.editForm.get(['minPrice']).value,
      createdAt:
        this.editForm.get(['createdAt']).value != null ? moment(this.editForm.get(['createdAt']).value, DATE_TIME_FORMAT) : undefined,
      updatedAt:
        this.editForm.get(['updatedAt']).value != null ? moment(this.editForm.get(['updatedAt']).value, DATE_TIME_FORMAT) : undefined,
      deletedAt:
        this.editForm.get(['deletedAt']).value != null ? moment(this.editForm.get(['deletedAt']).value, DATE_TIME_FORMAT) : undefined
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITaskCategory>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
