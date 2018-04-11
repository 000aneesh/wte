import { Component, OnInit } from '@angular/core';
import { UploadService } from './upload.service';

@Component({
  selector: 'app-upload',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.css'],
  providers: [UploadService]
})
export class UploadComponent implements OnInit {

  constructor(private uploadService: UploadService) {  }

  ngOnInit() {
  }

    inputMsg = '';
  outputMsg = 'initial val';
  errorMsg = '';
  
  longPollingClick(event) {
  console.log('in getMsg');
  	             this.uploadService.longPollingAjax().subscribe( Response => {
		              console.log(Response);
		              this.outputMsg = Response;
		              
		           }, resFileError => this.errorMsg = resFileError);
  }
  
}
