import {Component, OnInit, OnDestroy} from '@angular/core';
import {UploadService} from './upload.service';
import {Subject} from 'rxjs/Subject';
import 'rxjs/add/observable/interval';
import 'rxjs/add/operator/takeUntil';
import { FormGroup, FormControl, FormBuilder, FormArray, Validators } from '@angular/forms';

import {Location} from '@angular/common';
import {Router} from '@angular/router';

import { FileUploader } from 'ng2-file-upload';

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
uploadForm: FormGroup;
  destroy$: Subject<boolean>;

 uploader:FileUploader = new FileUploader({url:'http://localhost:3001/upload'});
 
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
			testCase: new FormControl(""),
			template: new FormControl(""),
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

removeExistingFile(){
	if(this.uploader && this.uploader.queue[0]){
	this.uploader.queue[0].remove();
	}
}

onSubmit(){
	if(this.uploadForm && this.uploadForm.value && this.uploadForm.value.testCase && this.uploadForm.value.template && this.uploader && this.uploader.queue[0]){
			
	this.uploadForm.value.file = this.uploader.queue[0].file;
	
	}
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
