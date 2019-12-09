import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { TaskUniteSharedModule } from 'app/shared/shared.module';
import { TaskUniteCoreModule } from 'app/core/core.module';
import { TaskUniteAppRoutingModule } from './app-routing.module';
import { TaskUniteHomeModule } from './home/home.module';
import { TaskUniteEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { JhiMainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ActiveMenuDirective } from './layouts/navbar/active-menu.directive';
import { ErrorComponent } from './layouts/error/error.component';
import { RevenueComponent } from './revenue/revenue.component';
import { RevenueLineChartComponent } from './revenue/revenue-line-chart/revenue-line-chart.component';
import { RevenueSecondLineChartComponent } from './revenue/revenue-second-line-chart/revenue-second-line-chart.component';

@NgModule({
  imports: [
    BrowserModule,
    TaskUniteSharedModule,
    TaskUniteCoreModule,
    TaskUniteHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    TaskUniteEntityModule,
    TaskUniteAppRoutingModule
  ],
  declarations: [
    JhiMainComponent,
    NavbarComponent,
    ErrorComponent,
    PageRibbonComponent,
    ActiveMenuDirective,
    FooterComponent,
    RevenueComponent,
    RevenueLineChartComponent,
    RevenueSecondLineChartComponent
  ],
  bootstrap: [JhiMainComponent]
})
export class TaskUniteAppModule {}
