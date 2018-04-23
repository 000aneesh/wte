export class ProcessStatus {
  fileUpload: String = '';
  fileGeneration: String = '';
  fileCopy: String = '';
  process1: String = '';
  process2: String = '';
  process3: String = '';
}

export class ProcessProgress {
//fileUpload: Number = 0;
  fileGeneration: Number = 0;
  fileCopy: Number = 0;
  process1: Number = 0;
  process2: Number = 0;
  process3: Number = 0;
}

export class NextProcess {
  initialProcess: String = 'fileUpload';
  fileUpload: String = 'fileGeneration';
  fileGeneration: String = 'fileCopy';
  fileCopy: String = 'process1';
  process1: String = 'process2';
  process2: String = 'process3';
  process3: String = '';
}
