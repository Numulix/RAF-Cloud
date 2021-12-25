import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { ApiService } from 'src/app/services/api.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  username: string = '';
  password: string = '';

  constructor(
    private toastr: ToastrService,
    private api: ApiService,
    private userService: UserService,
    private router: Router
  ) { }

  ngOnInit(): void {
  }

  handleLogin() {
    this.api.login(
      {
        username: this.username,
        password: this.password
      }
    ).subscribe(
      (response) => {
        localStorage.setItem('jwt_token', response.jwt);
        this.userService.login(response.jwt);
        this.router.navigate(['/']);
        this.toastr.success('Logged in!')
      },
      (err) => {
        if (err.status == 401)
          this.toastr.error('Bad credentials');
        else 
          this.toastr.error('Server error');
      }
    )
  }

}
