import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
//import {TestRunRequest} from '../model/testrunrequest';
import { ProcessStatus, ProcessProgress, NextProcess, Process } from '../model/process';

import { UploadService } from './upload.service';
import { Subject } from 'rxjs/Subject';
import 'rxjs/add/observable/interval';
import 'rxjs/add/operator/takeUntil';
import { FormGroup, FormControl, FormBuilder, FormArray, Validators } from '@angular/forms';
import { HttpClient, HttpResponse, HttpEventType } from '@angular/common/http';

import { Location } from '@angular/common';
import { Router } from '@angular/router';
import { TabsetComponent } from 'ngx-bootstrap';

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
  progress: number;
  processProgress: ProcessProgress;
  testCase: string;
  fileName: string;
  fileLocation: string;
  templateName: string;
  processNameList: Array<string>;
  processList: Array<Process>;
  processObjct: Process;
  lastProcess: string;
  inputMsg = '';
  outputMsg = 'initial val';
  i = 0;
  continueProcessing = false;
  uploadForm: FormGroup;
  destroy$: Subject<boolean>;
  downloadLink: string;
  templateList: Array<any>;
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
    // this.template = '';
    this.continueProcessing = true;

    this.uploadForm = this.builder.group({
      testCase: new FormControl(''),
      template: new FormControl(''),
      file: new FormData()
    });

    this.processNameList = this.uploadService.getProcessList();

    this.lastProcess = this.processNameList[this.processNameList.length - 1];

    this.uploadService.getTemplates().subscribe(response => {
      this.templateList = response;
    },
      (error) => {
      });
  }



  /* getDummyStatus(event, init) {
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
   } */

  initTask() {
    this.processProgress = new ProcessProgress();
    this.processStatus = new ProcessStatus();
    this.runningProcess = this.processNameList && this.processNameList.length > 0 ?
      this.processNameList[0] : ''; // this.nextProcess.initialProcess.toString();
    this.uploadTabs.tabs[1].disabled = false;
    this.uploadTabs.tabs[1].active = true;
  }

  onSubmit() {
    // this.processProgress.fileUpload = 0;
    if (this.uploadForm && this.uploadForm.value && this.uploadForm.value.testCase
      && this.uploadForm.value.template && this.selectedFiles && this.selectedFiles.item(0)) {

      //  this.progress.percentage = 0;
      this.currentFileUpload = this.selectedFiles.item(0);
      this.uploadForm.value.file = this.selectedFiles.item(0);

      this.uploadTabs.tabs[1].disabled = false;
      this.uploadTabs.tabs[1].active = true;
      this.downloadLink = undefined;

      this.processList = new Array<Process>();

      this.uploadService.uploadFile(this.currentFileUpload).subscribe(response => {
        if (response instanceof HttpResponse) {
          this.initTask();
          const respObj = JSON.parse(response.body);
          this.testCase = this.uploadForm.value.testCase;

          //this.downloadLink = '/files/' + respObj.fileLocation + '/output_file.txt';
          this.downloadLink = '/files/' + respObj.fileLocation + '/' + respObj.fileLocation + '.txt';

          this.processStatus['FileUpload'] = 'success';
          this.completedProcess = 'fileUpload';
          this.completedProcessStatus = 'success';

          this.fileName = respObj.fileName;
          this.templateName = this.uploadForm.value.template;
          this.fileLocation = respObj.fileLocation;

          //  if (this.completedProcessStatus === 'success' && !this.processStatus[this.lastProcess]) {
          this.runningProcess = this.getNextProcess(this.completedProcess);
          //          this.initTestCaseRun(this.testCase, respObj.fileName, this.uploadForm.value.template, respObj.fileLocation);
          // this.getDummyStatus(event, this.runningProcess);
          //  }

        }
      },
        (error) => {
          this.processStatus['FileUpload'] = 'error';
          this.completedProcess = 'FileUpload';
          this.completedProcessStatus = 'error';
        },
        () => {

          this.initTestCaseRun(this.testCase, this.fileName, this.templateName, this.fileLocation);
        });

    }
  }

  initTestCaseRun(testCase: string, fileName: string, templateKey: string, resultFolderName: string) {
    this.uploadService.initTestCaseRun(testCase, fileName, templateKey, resultFolderName).subscribe(response => {
    },
      (error) => {// error
      },
      () => {// completed
        //  this.processProgress[this.processNameList[0]] = 0;
        this.progress = 0;
        this.processObjct = new Process(this.processNameList[0], 'IN_PROGRESS', 0);
        this.updateProcessList(this.processObjct);
        this.getResult(this.testCase, this.processNameList[0]);
      });
  }

  getResult(testCase: string, executionStep: string) {
    /*  if (this.processProgress[executionStep] < 75) {
          this.processProgress[executionStep] = this.processProgress[executionStep] + Math.floor(Math.random() * 25)
          console.log(this.processProgress);
        }*/
    /*  this.timerVar = setInterval(() => {
        if (this.progress < 75) {
          this.progress = this.progress + Math.floor(Math.random() * 25);
          console.log(executionStep + ' : ' + this.progress);
          this.processObjct = new Process(executionStep, 'IN_PROGRESS', this.progress);
          this.updateProcessList(this.processObjct);
        }
      }, 100);*/
    this.uploadService.getResult(testCase, executionStep).subscribe(response => {
      if (response === 'IN_PROGRESS') {
        if (this.progress < 75) {
          this.progress = this.progress + Math.floor(Math.random() * 25);
          console.log(executionStep + ' : ' + this.progress);
          this.processObjct = new Process(executionStep, 'IN_PROGRESS', this.progress);
          this.updateProcessList(this.processObjct);
        }
        setTimeout(() => {
          this.getResult(testCase, executionStep);
        }, 1000);

      } else if (response === 'COMPLETED') {
        // clearInterval(this.timerVar);
        this.processObjct = new Process(executionStep, 'COMPLETED', 100);
        this.updateProcessList(this.processObjct);

        //   	this.processProgress[executionStep] = 100;
        this.getResultDetails(testCase, executionStep);
        var nxtProcess = this.getNextProcess(executionStep);
        if (nxtProcess) {
          this.progress = 0;
          this.processObjct = new Process(nxtProcess, 'IN_PROGRESS', 0);
          this.updateProcessList(this.processObjct);
          this.getResult(testCase, nxtProcess);

        }
      } else {
        // clearInterval(this.timerVar);
      }
    },
      (error) => {// error
      },
      () => {// completed
      });
  }

  getResultDetails(testCase: string, executionStep: string) {
    this.uploadService.getResultDetails(testCase, executionStep).subscribe(response => {
      if (response) {
        this.processObjct = this.getProcessByName(executionStep);
        this.processObjct.totalRecordsCount = response['totalRecordsCount'];
        this.processObjct.validRecordsCount = response['validRecordsCount'];
        this.processObjct.invalidRecordsCount = response['invalidRecordsCount'];
        this.updateProcessList(this.processObjct);

      }
    },
      (error) => {// error
      },
      () => {// completed

      });
  }

  selectFile(event) {
    this.selectedFiles = event.target.files;
  }

  ngOnDestroy() {
  }

  getNextProcess(curProcess): string {
    var index = this.processNameList.indexOf(curProcess);
    var nextItem = '';
    if (index >= 0 && index < this.processNameList.length - 1) {
      nextItem = this.processNameList[index + 1]
    }
    return nextItem;
  }

  getProcessByName(processName: string) {
    let processObj: Process;
    for (let proc of this.processList) {
      if (proc.name === processName) {
        processObj = proc;
        break;
      }
    }
    return processObj;
  }

  updateProcessList(processObj: Process) {
    let updated: boolean = false;
    for (let index in this.processList) {
      if (this.processList[index].name === processObj.name) {
        this.processList[index] = processObj;
        updated = true;
      }
    }
    if (!updated) {
      this.processList.push(processObj);
    }

  }

}
