import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { User } from 'src/app/models';
import { ApiService } from 'src/app/services/api.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  userList: User[] = []
  hasPermission: boolean = true;

  canUpdate: number = this.userService.permissions.canUpdateUser
  canDelete: number = this.userService.permissions.canDeleteUser

  constructor(
    private api: ApiService,
    private userService: UserService,
    private toastr: ToastrService
  ) { }

  ngOnInit(): void {
    if (this.userService.permissions.canReadUser == 1) {
      this.getAllUsers();
    } else {
      this.hasPermission = false;
    }
  }

  getAllUsers() {
    this.api.getAllUsers().subscribe(
      response => {
        this.userList = response
      }
    )
  }

  handleDelete(id: number) {
    this.api.deleteUser(id).subscribe(
      res => {
        this.toastr.success('User deleted')
        this.getAllUsers();
      }, err => {
        console.log(err);
      }
    )
  }

}
