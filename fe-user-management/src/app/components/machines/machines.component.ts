import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { Machine } from 'src/app/models';
import { ApiService } from 'src/app/services/api.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-machines',
  templateUrl: './machines.component.html',
  styleUrls: ['./machines.component.css']
})
export class MachinesComponent implements OnInit {

  machines: Machine[] = []
  scheduleDate!: string;

  constructor(
    private api: ApiService,
    private toastr: ToastrService,
    public userService: UserService
  ) { }

  ngOnInit(): void {
    this.api.getAllMachines().subscribe(
      (data: Machine[]) => {
        this.machines = data
      }
    )
  }

  showDate() {
    console.log(this.scheduleDate.replace('T', ' ')); 
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
  
  scheduleStartMachine(id: number) {
    if (!this.scheduleDate) {
      this.toastr.error('Please select a date');
      return;
    }
    this.api.scheduleStartMachine(id, this.scheduleDate.replace('T', ' ')).subscribe(
      () => {
        this.toastr.success('Machine start scheduled');
      }
    )
  }

  scheduleStopMachine(id: number) {
    if (!this.scheduleDate) {
      this.toastr.error('Please select a date');
      return;
    }
    this.api.scheduleStopMachine(id, this.scheduleDate.replace('T', ' ')).subscribe(
      () => {
        this.toastr.success('Machine stop scheduled');
      }
    )
  }

  scheduleRestartMachine(id: number) {
    if (!this.scheduleDate) {
      this.toastr.error('Please select a date');
      return;
    }
    this.api.scheduleRestartMachine(id, this.scheduleDate.replace('T', ' ')).subscribe(
      () => {
        this.toastr.success('Machine restart scheduled');
      }
    )
  }
}
