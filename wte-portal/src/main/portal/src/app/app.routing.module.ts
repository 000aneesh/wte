import {NgModule} from '@angular/core';
import {RouterModule, Routes, CanActivate} from '@angular/router';
import {AppComponent} from './app.component';
import {MenuComponent} from './menu/menu.component';
import {UploadComponent} from './upload/upload.component';
import {AdminComponent} from './admin/admin.component';
import {TestSuiteComponent} from './test-suite/test-suite.component';

// Router
const routes: Routes = [
  {path: '', component: UploadComponent},
  {path: 'admin', component: AdminComponent},
  {path: 'testSuite/:testCase/:sourcePage', component: TestSuiteComponent},
  {path: 'upload', redirectTo: '/', pathMatch: 'full'}
];

// angular
@NgModule({
  imports: [
    RouterModule.forRoot(routes)
  ],
  exports: [
    RouterModule
  ]
})

export class AppRoutingModule {

}

export const RoutingComponents = [
  MenuComponent,
  UploadComponent,
  AdminComponent,
  TestSuiteComponent
];
