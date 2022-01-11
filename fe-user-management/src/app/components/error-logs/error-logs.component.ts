import { Component, OnInit } from '@angular/core';
import { ErrorMessage } from 'src/app/models';
import { ApiService } from 'src/app/services/api.service';

@Component({
  selector: 'app-error-logs',
  templateUrl: './error-logs.component.html',
  styleUrls: ['./error-logs.component.css']
})
export class ErrorLogsComponent implements OnInit {

  errorLogs: ErrorMessage[] = [];

  constructor(
    private api: ApiService
  ) { }

  ngOnInit(): void {
    this.api.getErrorLogs().subscribe(
      (res: ErrorMessage[]) => {
        this.errorLogs = res;
      },
      err => {
        console.log(err);
      }
    );
  }

}
