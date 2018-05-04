import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';

@Injectable()
export class TestSuiteService {

constructor(private http: HttpClient) {}

  getTestRecords(testCase: string) { 
    return this.http.get('/getTestRecords?testCase=' + testCase);
  }
  
  getObjectFromXml(testCase: string) { 
    return this.http.get('/xmlToObject?testCase=' + testCase);
  }

}