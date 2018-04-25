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
  data: Array<any>;
  modalTitle: string;
  modalBody: string;
  display = 'none';

  constructor(private route: ActivatedRoute, private testSuiteService: TestSuiteService) { }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.testCase = params['testCase'];
    });


    this.testSuiteService.getTestRecords(this.testCase).subscribe(response => {
      this.data = JSON.parse(JSON.stringify(response));
    },
      (error) => {// error
      },
      () => {// completed

      });

  }
  showDetails(event) {
    this.display = 'block';
    this.modalTitle = 'Details';
    this.modalBody = 'Geting Details... Please wait...';
    setTimeout(() => {
      this.modalBody = 'Data updated';
    }, 2000);
  }

  onCloseHandled() {
    this.display = 'none';
  }

}