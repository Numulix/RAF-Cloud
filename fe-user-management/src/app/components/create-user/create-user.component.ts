import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { ApiService } from 'src/app/services/api.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-create-user',
  templateUrl: './create-user.component.html',
  styleUrls: ['./create-user.component.css']
})
export class CreateUserComponent implements OnInit {

  newUserForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private toastr: ToastrService,
    private api: ApiService,
    public userService: UserService
  ) { }

  ngOnInit(): void {
    this.newUserForm = this.fb.group({
      name: ['', [Validators.required]],
      surname: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]],
      permission: this.fb.group({
        canReadUser: [false],
        canCreateUser: [false],
        canUpdateUser: [false],
        canDeleteUser: [false]
      })
    })
  }

  get form() {
    return this.newUserForm;
  }

  handleSubmit() {
    this.newUserForm.patchValue({
      permission: {
        canReadUser: this.form.get('permission.canReadUser')?.value == false ? 0 : 1,
        canCreateUser: this.form.get('permission.canCreateUser')?.value == false ? 0 : 1,
        canUpdateUser: this.form.get('permission.canUpdateUser')?.value == false ? 0 : 1,
        canDeleteUser: this.form.get('permission.canDeleteUser')?.value == false ? 0 : 1,
      }
    })
    if (!this.newUserForm.valid) {
      this.toastr.info('Form is invalid');
      return;
    }
    this.api.createNewUser(this.newUserForm.value).subscribe(
      res => {
        this.newUserForm.reset()
        this.toastr.success('User created successfully');
      }, err => {
        if (err.status == 400) {
          this.toastr.error('User with that email already exists')
        } else {
          this.toastr.error('Server error')
        }
      }
    )
  }

}
