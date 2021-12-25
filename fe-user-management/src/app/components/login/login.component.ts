import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { ApiService } from 'src/app/services/api.service';

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
    private api: ApiService
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
