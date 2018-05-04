import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { TestSuiteService } from './test-suite.service';

@Component({
  selector: 'app-test-suite',
  templateUrl: './test-suite.component.html',
  styleUrls: ['./test-suite.component.css'],
  providers: [TestSuiteService]
})
export class TestSuiteComponent implements OnInit {

  testCase: string;
  //template: string;
  data: Array<any>;
  modalTitle: string;
  testResult: string;
  resultKeys:  Array<string>;
  sourcePage: string;
  display = 'none';

  constructor(private route: ActivatedRoute, private testSuiteService: TestSuiteService) { }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.testCase = params['testCase'];
      this.sourcePage = params['sourcePage'];
      console.log(this.testCase);
    });

	 if (this.sourcePage === 'processingStatus') {
	    this.testSuiteService.getTestRecords(this.testCase).subscribe(response => {
	      this.data = JSON.parse(JSON.stringify(response));
	    },
	      (error) => {// error
	      },
	      () => {// completed
	
	      });
	   }
	   if (this.sourcePage === 'history') {
		    this.testSuiteService.getObjectFromXml(this.testCase).subscribe(response => {
		      this.data = JSON.parse(JSON.stringify(response));
		    },
		      (error) => {// error
		      },
		      () => {// completed
		
		      });
	   }
     

  }
  showDetails(event,testCase) {
    this.display = 'block';
    this.modalTitle = 'Details';
    //this.testResult = 'Geting Details... Please wait...';    
    //alert(JSON.stringify(testCase.testresult.testStatus));
    //alert( Object.keys(testCase.testresult.testStatus));
    this.resultKeys=Object.keys(testCase.testresult.testStatus);
    this.testResult = testCase.testresult.testStatus;
    
  }

  onCloseHandled() {
    this.display = 'none';
  }

}