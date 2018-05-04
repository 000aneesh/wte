import {HttpClient, HttpRequest, HttpEvent} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Http, Response} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/switchMap';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/throw';

@Injectable()
export class UploadService {

  constructor(private _http: Http, private http: HttpClient) {}

  uploadFile(file) {
    const formdata: FormData = new FormData();
    formdata.append('file', file);
    const req = new HttpRequest('POST', '/upload', formdata, {
      reportProgress: true,
      responseType: 'text'
    });
    return this.http.request(req).catch(this._errorHandler);
  }

  _errorHandler(_error: Response) {
    // document.getElementsByTagName('html')[0].classList.remove('loading');
    return Observable.throw(_error || 'server error 404');
  }

  getFiles(): Observable<any> {
    return this.http.get('/getallfiles');
  }

  initTestCaseRun(testCase:string, fileName:string, templateKey:string, resultFolderName:string): Observable<any> {
    const req = new HttpRequest('GET', '/testRun?testCase=' + testCase + '&fileName=' + fileName +
     '&templateKey=' + templateKey +'&resultFolderName='+ resultFolderName);
    return this.http.request(req);
  }

  getTemplates(): Observable<any> {
    return this.http.get('/getTemplates');
  }
  
   getDirectories(): Observable<any> {
    return this.http.get('/getDirectories');
  }

  getResult(testCase: string, executionStep: string) {
    return this.http.get('/getResult?testCase=' + testCase + '&executionStep=' + executionStep);
  }
  
  getResultDetails(testCase: string, executionStep: string) {
    return this.http.get('/getResultDetails?testCase=' + testCase + '&executionStep=' + executionStep);
  }

  getProcessList() {
    return ['FileGeneration', 'FTPTransfer', /*'Verification',*/ 'EDGE_TO_RAW', 'Raw_To_RA', 'RA_TO_RAW', 'Raw_To_R'];
  }

}
