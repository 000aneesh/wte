import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-test-suite',
  templateUrl: './test-suite.component.html',
  styleUrls: ['./test-suite.component.css']
})
export class TestSuiteComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }
  
  public rows:Array<any> = this.getData();
  
  public columns:Array<any> = [
    {title: 'Column 1', name: 'column1'},
    {title: 'Column 2', name: 'column2'},
    {title: 'Column 3', name: 'column3'},
    {title: 'Column 4', name: 'column4'}
  ];
  

  public config:any = {  
    className: ['table-striped', 'table-bordered']
  };

  public constructor() {
  }

  public ngOnInit():void {
  }


  getData():any {
    return [
    {column1:'asas',column2:'asasqw',column3:'asasas',column4:'qwqasas'}
    {column1:'dfdfdf',column2:'asaas',column3:'aasasas',column4:'ghdfdf'}
    {column1:'aerwsfs',column2:'adferes',column3:'ghgthf',column4:'erergr'}
    {column1:'yjyj',column2:'yjas',column3:'assd',column4:'aasass'}
    ];

}