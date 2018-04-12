import {Component, OnInit, OnDestroy} from '@angular/core';
import {UploadService} from './upload.service';
import {Subject} from 'rxjs/Subject';
import 'rxjs/add/observable/interval';
import 'rxjs/add/operator/takeUntil';
import {FormGroup, FormControl, FormBuilder, FormArray, Validators} from '@angular/forms';
import {HttpClient, HttpResponse, HttpEventType} from '@angular/common/http';

import {Location} from '@angular/common';
import {Router} from '@angular/router';

@Component({
  selector: 'app-upload',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.css'],
  providers: [UploadService]
})
export class UploadComponent implements OnInit, OnDestroy {

  selectedFiles: FileList;
  currentFileUpload: File;
  progress: {percentage: number} = {percentage: 0};
  inputMsg = '';
  outputMsg = 'initial val';
  i = 0;
  continueProcessing = false;
  uploadForm: FormGroup;
  destroy$: Subject<boolean>;

  templateList: Array<any>;
  template: string;
  constructor(private uploadService: UploadService, private builder: FormBuilder) {
    //    this.continueProcessing = true;
    this.destroy$ = new Subject<boolean>();

    /* router.events.subscribe((val) => {
       if (location.path() !== '/upload') {
         this.continueProcessing = false;
       }
     });*/

  }

  ngOnInit() {
    this.template = '';
    this.continueProcessing = true;

    this.uploadForm = this.builder.group({
      testCase: new FormControl(''),
      template: new FormControl(''),
      file: new FormData()
    });

    this.templateList = [
      {_key: 'key 1', object: {url: 'testUrl 1', xpath: 'some xpath 1'}},
      {_key: 'key 2', object: {url: 'testUrl 2', xpath: 'some xpath 2'}},
      {_key: 'key 3', object: {url: 'testUrl 3', xpath: 'some xpath 3'}},
      {_key: 'key 4', object: {url: 'testUrl 4', xpath: 'some xpath 4'}}
    ];
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
      });
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



  onSubmit() {
    this.progress.percentage = 0;
    this.currentFileUpload = this.selectedFiles.item(0);
    if (this.uploadForm && this.uploadForm.value && this.uploadForm.value.testCase
      && this.uploadForm.value.template && this.currentFileUpload) {

      this.progress.percentage = 0;

      this.currentFileUpload = this.selectedFiles.item(0);
      this.uploadService.pushFileToStorage(this.currentFileUpload).subscribe(event => {
        if (event.type === HttpEventType.UploadProgress) {
          this.progress.percentage = Math.round(100 * event.loaded / event.total);
        } else if (event instanceof HttpResponse) {
          console.log('File is completely uploaded!');
        }
      });

      this.selectedFiles = undefined;

      /*  debugger;
        this.uploadService.uploadFile(this.uploadForm.value)
          .subscribe(
          (response) => {// success
            debugger;
            if (response.type === HttpEventType.UploadProgress) {
              this.progress.percentage = Math.round(100 * response.loaded / response.total);
            } else if (response instanceof HttpResponse) {
              console.log('File is completely uploaded!');
            }
          },
          (error) => {// error
          },
          () => {// completed
          });
  */
    }
  }

  selectFile(event) {
    this.selectedFiles = event.target.files;
  }

  upload() {
    this.progress.percentage = 0;

    this.currentFileUpload = this.selectedFiles.item(0)
    this.uploadService.pushFileToStorage(this.currentFileUpload).subscribe(event => {
      if (event.type === HttpEventType.UploadProgress) {
        this.progress.percentage = Math.round(100 * event.loaded / event.total);
      } else if (event instanceof HttpResponse) {
        console.log('File is completely uploaded!');
      }
    })

    this.selectedFiles = undefined;
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
