export class Process {
  name: string;
  status: string;
  progress: number;
  totalRecordsCount: number;
  validRecordsCount: number;
  invalidRecordsCount: number;
  
  constructor(name:string, status:string, progress:number){
  this.name = name;
  this.status = status;
  this.progress = progress;
//  this.downloadLink = downloadLink;
  }
}
