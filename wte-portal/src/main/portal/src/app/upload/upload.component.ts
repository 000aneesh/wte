import {Component, OnInit, OnDestroy} from '@angular/core';
import {UploadService} from './upload.service';
import {Subject} from 'rxjs/Subject';
import 'rxjs/add/observable/interval';
import 'rxjs/add/operator/takeUntil';

import {Location} from '@angular/common';
import {Router} from '@angular/router';

@Component({
  selector: 'app-upload',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.css'],
  providers: [UploadService]
})
export class UploadComponent implements OnInit, OnDestroy {

  inputMsg = '';
  outputMsg = 'initial val';
  i = 0;
  continueProcessing = false;

  destroy$: Subject<boolean>;

  constructor(private uploadService: UploadService) {
    //    this.continueProcessing = true;
    this.destroy$ = new Subject<boolean>();

    /* router.events.subscribe((val) => {
       if (location.path() !== '/upload') {
         this.continueProcessing = false;
       }
     });*/

  }

  ngOnInit() {
    this.continueProcessing = true;
  }

  longPolling(event) {
    console.log('in longPolling');
    this.uploadService.longPollingAjax()
      .subscribe(
      (response) => {
        this.outputMsg = response.data;
        console.log(this.i + ' : ' + this.outputMsg);
      },
      (error) => {
      },
      () => {
        console.log('completed: ' + this.continueProcessing);
        if (this.continueProcessing) {
          this.longPolling(event);
        }
      }
      );
  }

  longPolling1(event) {
    this.destroy$ = new Subject<boolean>();

    console.log('in longPolling1');
    this.uploadService.longPollingAjax1()
      .takeUntil(this.destroy$)
      .subscribe(
      (response) => {
        this.outputMsg = response.data;
        console.log(this.i + ' : ' + this.outputMsg);
        this.i++;
        if (this.i > 5) {

          this.unsubscribeDestroy();
        }
      },
      (error) => {
        // handle error
      },
      () => {
        console.log('completed');
      }
      );
  }




  ngOnDestroy() {
    this.continueProcessing = false;
    this.unsubscribeDestroy();
  }

  unsubscribeDestroy() {
    console.log('in destroy method');
    this.i = 0;
    this.destroy$.next(true);
    // Now let's also unsubscribe from the subject itself:
    if (!this.destroy$) {
      this.destroy$.unsubscribe();
    }
  }
}
