import {TestRunRequest} from '../model/testrunrequest';
import {ProcessStatus, ProcessProgress, NextProcess} from '../model/process';

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
  processProgress: ProcessProgress;
  inputMsg = '';
  outputMsg = 'initial val';
  i = 0;
  selectedIndex: number;
  continueProcessing = false;
  uploadForm: FormGroup;
  destroy$: Subject<boolean>;
  downloadLink: string;
  templateList: Array<any>;
  template: string;
  completedProcess: string;
  completedProcessStatus: string;
  runningProcess: string;
  processStatus: ProcessStatus;
  timerVar: any;
  nextProcess: NextProcess = new NextProcess();
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
    this.selectedIndex = 0;

    this.uploadForm = this.builder.group({
      testCase: new FormControl(''),
      template: new FormControl(''),
      file: new FormData()
    });

    /*  this.templateList = [
        {_key: 'key 1', object: {url: 'testUrl 1', xpath: 'some xpath 1'}},
        {_key: 'key 2', object: {url: 'testUrl 2', xpath: 'some xpath 2'}},
        {_key: 'key 3', object: {url: 'testUrl 3', xpath: 'some xpath 3'}},
        {_key: 'key 4', object: {url: 'testUrl 4', xpath: 'some xpath 4'}}
  ];*/

    this.uploadService.getTemplates().subscribe(response => {
      this.templateList = response;
    },
      (error) => {
      });
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

  getDummyStatus(event, init) {
    //    this.processProgress.percentage = 0;
    this.selectedIndex = 1;
    if (init) {
      this.processProgress = new ProcessProgress();
      this.processStatus = new ProcessStatus();
      this.runningProcess = this.nextProcess.initialProcess.toString();
    }

    this.timerVar = setInterval(() => {
      if (this.processProgress[this.runningProcess] < 80) {
        this.processProgress[this.runningProcess] = this.processProgress[this.runningProcess] + Math.floor(Math.random() * 20);
      }
      console.log(this.processProgress[this.runningProcess]);
    }, 1000);
    this.uploadService.getDummyStatus(init)
      .subscribe(
      (response) => {
        this.processStatus[response.process] = response.status;
        this.completedProcess = response.process;
        this.completedProcessStatus = response.status;
        clearInterval(this.timerVar);
      },
      (error) => {
      },
      () => {
        console.log('completed: ' + this.continueProcessing);
        if (this.completedProcessStatus === 'success' && !this.processStatus.process3) {
          this.runningProcess = this.nextProcess[this.completedProcess];
          this.getDummyStatus(event, false);
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
    if (this.uploadForm && this.uploadForm.value && this.uploadForm.value.testCase
      && this.uploadForm.value.template && this.selectedFiles && this.selectedFiles.item(0)) {

      this.progress.percentage = 0;
      this.currentFileUpload = this.selectedFiles.item(0);
      this.uploadForm.value.file = this.selectedFiles.item(0);

      this.selectedIndex = 1;
      this.downloadLink = undefined;
      this.uploadService.uploadFile(this.currentFileUpload).subscribe(event => {
        if (event.type === HttpEventType.UploadProgress) {
          this.progress.percentage = Math.round(100 * event.loaded / event.total);
        } else if (event instanceof HttpResponse) {
          const respObj = JSON.parse(event.body);

          const testRunRequest: TestRunRequest = new TestRunRequest;
          testRunRequest.fileName = respObj.fileName;
          testRunRequest.resultFolderName = respObj.fileLocation;
          testRunRequest.templateKey = this.uploadForm.value.template;
          testRunRequest.testCase = this.uploadForm.value.testCase;

          this.downloadLink = '/files/' + respObj.fileLocation + '/output_file.txt';

          this.initTestRun(testRunRequest);

        }
      });

      // this.selectedFiles = undefined;

      /*
        this.uploadService.uploadFile(this.uploadForm.value)
          .subscribe(
          (response) => {// success

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

  initTestRun(testRunRequest: TestRunRequest) {
    this.progress.percentage = 0;
    this.uploadService.getTestRun(testRunRequest).subscribe(event => {
      if (event.type === HttpEventType.UploadProgress) {
        this.progress.percentage = Math.round(100 * event.loaded / event.total);
      } else if (event instanceof HttpResponse) {
        console.log('getTestRun!');
      }
    });
  }

  selectFile(event) {
    this.selectedFiles = event.target.files;
  }

  upload() {
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
