import {Component, OnInit, OnDestroy, ViewChild} from '@angular/core';
//import {TestRunRequest} from '../model/testrunrequest';
import {ProcessStatus, ProcessProgress, NextProcess} from '../model/process';

import {UploadService} from './upload.service';
import {Subject} from 'rxjs/Subject';
import 'rxjs/add/observable/interval';
import 'rxjs/add/operator/takeUntil';
import {FormGroup, FormControl, FormBuilder, FormArray, Validators} from '@angular/forms';
import {HttpClient, HttpResponse, HttpEventType} from '@angular/common/http';

import {Location} from '@angular/common';
import {Router} from '@angular/router';
import {TabsetComponent} from 'ngx-bootstrap';

@Component({
  selector: 'app-upload',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.css'],
  providers: [UploadService]
})
export class UploadComponent implements OnInit, OnDestroy {

  @ViewChild('uploadTabs') uploadTabs: TabsetComponent;

  router: Router;
  selectedFiles: FileList;
  currentFileUpload: File;
  progress: {percentage: number} = {percentage: 0};
  processProgress: ProcessProgress;
  testCase: string;
  processList: Array<string>;
  lastProcess: string;
  inputMsg = '';
  outputMsg = 'initial val';
  i = 0;
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

    this.uploadForm = this.builder.group({
      testCase: new FormControl(''),
      template: new FormControl(''),
      file: new FormData()
    });

    this.processList = this.uploadService.getProcessList();

    this.lastProcess = this.processList[this.processList.length - 1];

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
    //this.uploadTabs.tabs[1].disabled = false;
    //this.uploadTabs.tabs[1].active = true;
    if (this.runningProcess === this.nextProcess.initialProcess.toString()) {
      this.initTask();
    }

    this.timerVar = setInterval(() => {
      if (this.processProgress[this.runningProcess] < 80) {
        this.processProgress[this.runningProcess] = this.processProgress[this.runningProcess] + Math.floor(Math.random() * 20);
      }
      console.log(this.runningProcess + ' : ' + this.processProgress[this.runningProcess]);
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
        if (this.completedProcessStatus === 'success' && !this.processStatus && this.processStatus['process3']) {
          this.runningProcess = this.nextProcess[this.completedProcess];
          this.getDummyStatus(event, this.runningProcess);
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

  initTask() {
    this.processProgress = new ProcessProgress();
    this.processStatus = new ProcessStatus();
    this.runningProcess = this.processList && this.processList.length > 0 ?
      this.processList[0] : ''; // this.nextProcess.initialProcess.toString();
    this.uploadTabs.tabs[1].disabled = false;
    this.uploadTabs.tabs[1].active = true;
  }

  onSubmit() {
    // this.processProgress.fileUpload = 0;
    if (this.uploadForm && this.uploadForm.value && this.uploadForm.value.testCase
      && this.uploadForm.value.template && this.selectedFiles && this.selectedFiles.item(0)) {

      this.progress.percentage = 0;
      this.currentFileUpload = this.selectedFiles.item(0);
      this.uploadForm.value.file = this.selectedFiles.item(0);

      this.uploadTabs.tabs[1].disabled = false;
      this.uploadTabs.tabs[1].active = true;
      this.downloadLink = undefined;

      this.uploadService.uploadFile(this.currentFileUpload).subscribe(response => {
        //   if (response.type === HttpEventType.UploadProgress) {
        //     this.processProgress.fileUpload = Math.round(100 * event.loaded / event.total);
        //   } else if (response instanceof HttpResponse) {
        if (response instanceof HttpResponse) {
          this.initTask();
          const respObj = JSON.parse(response.body);
          this.testCase = this.uploadForm.value.testCase;
          //          const testRunRequest: TestRunRequest = new TestRunRequest;
          //          testRunRequest.fileName = respObj.fileName;
          //          testRunRequest.resultFolderName = respObj.fileLocation;
          //          testRunRequest.templateKey = this.uploadForm.value.template;
          //          testRunRequest.testCase = this.uploadForm.value.testCase;

          //this.downloadLink = '/files/' + respObj.fileLocation + '/output_file.txt';
          this.downloadLink = '/files/' + respObj.fileLocation + '/' + respObj.fileName;

          this.processStatus['FileUpload'] = 'success';
          this.completedProcess = 'fileUpload';
          this.completedProcessStatus = 'success';
          //this.initTestRun(testRunRequest);

        }
      },
        (error) => {
          this.processStatus['FileUpload'] = 'error';
          this.completedProcess = 'FileUpload';
          this.completedProcessStatus = 'error';
        },
        () => {
          console.log('completed: ' + this.continueProcessing);
          if (this.completedProcessStatus === 'success' && !this.processStatus[this.lastProcess]) {
            this.runningProcess = this.getNextProcess(this.completedProcess);
            this.initTestRun(this.testCase, this.runningProcess);
            // this.getDummyStatus(event, this.runningProcess);
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

  initTestRun(testCase: string, executionStep: string) {
    this.uploadService.getResult(testCase, executionStep).subscribe(response => {
    //  if (response instanceof HttpResponse) {
      debugger;
        console.log('getTestRun!');
    //  }
    },
      (error) => {// error
      },
      () => {// completed
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

  getNextProcess(curProcess): string {
    var index = this.processList.indexOf(curProcess);
    var nextItem = '';
    if (index >= 0 && index < this.processList.length - 1) {
      nextItem = this.processList[index + 1]
    }
    return nextItem;
  }

}
