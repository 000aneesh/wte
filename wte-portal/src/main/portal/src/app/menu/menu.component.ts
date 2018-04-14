import {Component, OnInit} from '@angular/core';
import {Router, NavigationStart} from '@angular/router';
import {Location} from '@angular/common';
import {GeneralConstants} from '../shared/constants';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['menu.component.css'],
  providers: []
})
export class MenuComponent implements OnInit {
  menu = GeneralConstants.MENU;
  activeMenu = '';
  // Header Drop-down toggle mode
  toggleFlag: number;
  router: Router;
  location: Location;
  constructor(private _location: Location, private _router: Router) {
    this.router = _router;
    this.location = _location;
  }

  ngOnInit() {
    this.router.events.subscribe((val) => {
      if (this.location.path() === '/upload') {
        this.activeMenu = this.menu.UPLOAD;
      } else if (this.location.path() === '/admin') {
        this.activeMenu = this.menu.ADMIN;
      }
    });
  }




  // redirect to 'Upload' module
  onUpload(event) {
  }
  // redirect to 'Admin' module
  onAdmin(event) {
  }
}
