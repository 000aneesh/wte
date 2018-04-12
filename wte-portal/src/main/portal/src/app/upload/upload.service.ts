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


  longPollingAjax() {

    return this._http.get('/pollBroadcast')
      .map((response: Response) => {
        if (response.status < 200 || response.status >= 300) {
          throw new Error('This request has failed ' + response.status);
        } else {
          return response.json();
        }
      })
      .catch(this._errorHandler);
  }

  uploadFile(req) {

    return this._http.post('/upload', req)
      .map((response: Response) => {
        if (response.status < 200 || response.status >= 300) {
          throw new Error('This request has failed ' + response.status);
        } else {
          return response.json();
        }
      })
      .catch(this._errorHandler);
  }

  longPollingAjax1() {

    return Observable.interval(1000)
      .switchMap(() => this._http.get('/pollBroadcastStr?input=' + 1)
        .map((response: Response) => {
          if (response.status < 200 || response.status >= 300) {
            throw new Error('This request has failed ' + response.status);
          } else {
            return response.json();
          }
        })
        .catch(this._errorHandler));
  }

  _errorHandler(_error: Response) {
    // document.getElementsByTagName('html')[0].classList.remove('loading');
    return Observable.throw(_error || 'server error 404');
  }

  pushFileToStorage(file: File): Observable<HttpEvent<{}>> {
    let formdata: FormData = new FormData();

    formdata.append('file', file);

    const req = new HttpRequest('POST', '/post', formdata, {
      reportProgress: true,
      responseType: 'text'
    });

    return this.http.request(req);
  }


  getFiles(): Observable<any> {
    return this.http.get('/getallfiles');
  }
}