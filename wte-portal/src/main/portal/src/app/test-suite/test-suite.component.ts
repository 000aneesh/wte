import { Component, OnInit } from '@angular/core';
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
  private sub: any;
  public rows: Array<any>;

  public data: Array<any>;
  constructor(private route: ActivatedRoute, private testSuiteService: TestSuiteService) { }

  ngOnInit() {
    this.sub = this.route.params.subscribe(params => {
      this.testCase = params['testCase'];
    });


    this.testSuiteService.getTestRecords(this.testCase).subscribe(response => {
      var respArray = JSON.parse(JSON.stringify(response));

      this.data = respArray;//JSON.parse(JSON.stringify(response));
    },
      (error) => {// error
      },
      () => {// completed

      });


  }

  carClicked(event){
    alert('asas');
  }

}