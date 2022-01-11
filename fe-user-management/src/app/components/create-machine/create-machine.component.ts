import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { ApiService } from 'src/app/services/api.service';

@Component({
  selector: 'app-create-machine',
  templateUrl: './create-machine.component.html',
  styleUrls: ['./create-machine.component.css']
})
export class CreateMachineComponent implements OnInit {

  machineName: string = '';

  constructor(
    private api: ApiService,
    private toastr: ToastrService
  ) { }

  ngOnInit(): void {
  }

  createMachine() {
    if (this.machineName.trim().length === 0) {
      this.toastr.error('Machine name is required');
      return;
    }
    this.api.createMachine({ name: this.machineName.trim() }).subscribe(
      (res) => {
        this.toastr.success('Machine created successfully');
      }
    );
  }

}
