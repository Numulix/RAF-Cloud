import { Component, OnInit } from '@angular/core';
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

  constructor(
    private api: ApiService,
    private userService: UserService
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

}
