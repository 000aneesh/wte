import {NgModule} from '@angular/core';
import {RouterModule, Routes, CanActivate} from '@angular/router';
import {AppComponent} from './app.component';
import {HomeComponent} from './home/home.component';
import {UploadComponent} from './upload/upload.component';
import {AdminComponent} from './admin/admin.component';
// Router
const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'upload', component: UploadComponent },
  { path: 'admin', component: AdminComponent },
  { path: '', redirectTo: '/upload', pathMatch: 'full' }
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
  HomeComponent,
  UploadComponent,
  AdminComponent
];
