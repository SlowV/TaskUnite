import { Moment } from 'moment';
import { ITask } from 'app/shared/model/task.model';
import { ITaskerCategory } from 'app/shared/model/tasker-category.model';

export interface ITaskCategory {
  id?: number;
  name?: string;
  description?: string;
  image?: string;
  minPrice?: number;
  createdAt?: Moment;
  updatedAt?: Moment;
  deletedAt?: Moment;
  tasks?: ITask[];
  taskerCategories?: ITaskerCategory[];
}

export class TaskCategory implements ITaskCategory {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string,
    public image?: string,
    public minPrice?: number,
    public createdAt?: Moment,
    public updatedAt?: Moment,
    public deletedAt?: Moment,
    public tasks?: ITask[],
    public taskerCategories?: ITaskerCategory[]
  ) {}
}
