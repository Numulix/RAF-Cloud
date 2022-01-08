import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { Machine } from 'src/app/models';
import { ApiService } from 'src/app/services/api.service';

@Component({
  selector: 'app-machines',
  templateUrl: './machines.component.html',
  styleUrls: ['./machines.component.css']
})
export class MachinesComponent implements OnInit {

  machines: Machine[] = []

  constructor(
    private api: ApiService,
    private toastr: ToastrService
  ) { }

  ngOnInit(): void {
    this.api.getAllMachines().subscribe(
      (data: Machine[]) => {
        this.machines = data
      }
    )
  }

  startMachine(id: number) {
    this.api.startMachine(id).subscribe(
      () => {
        this.toastr.success('Machine started');
        this.api.getAllMachines().subscribe(
          (data: Machine[]) => {
            this.machines = data
          }
        )
      }
    )
  }

  stopMachine(id: number) {
    this.api.stopMachine(id).subscribe(
      () => {
        this.toastr.success('Machine stopped');
        this.api.getAllMachines().subscribe(
          (data: Machine[]) => {
            this.machines = data
          }
        )
      }
    )
  }

  restartMachine(id: number) {
    this.api.restartMachine(id).subscribe(
      () => {
        this.toastr.success('Machine restarted');
        this.api.getAllMachines().subscribe(
          (data: Machine[]) => {
            this.machines = data
          }
        )
      }
    )
  }
}
