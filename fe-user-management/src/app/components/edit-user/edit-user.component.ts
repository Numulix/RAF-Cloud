import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { ApiService } from 'src/app/services/api.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-edit-user',
  templateUrl: './edit-user.component.html',
  styleUrls: ['./edit-user.component.css']
})
export class EditUserComponent implements OnInit, OnDestroy {

  id!: number;
  sub: any;

  user: any;

  editUserForm!: FormGroup;

  constructor(
    public userService: UserService,
    private toastr: ToastrService,
    private api: ApiService,
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.sub = this.route.params.subscribe((params: Params) => {
      this.id = +params['id'];
    })

    this.editUserForm = this.fb.group({
      name: ['', [Validators.required]],
      surname: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      password: ['default', [Validators.required]],
      permission: this.fb.group({
        canReadUser: [false],
        canCreateUser: [false],
        canUpdateUser: [false],
        canDeleteUser: [false]
      })
    })

    this.api.getUserById(this.id).subscribe(res => {
      this.user = res;
      console.log(this.user);
      
      this.editUserForm.patchValue({
        name: res.name,
        surname: res.surname,
        email: res.email,
        permission: {
          canReadUser: res.permission.canReadUser == 1 ? true : false,
          canCreateUser: res.permission.canCreateUser == 1 ? true : false,
          canUpdateUser: res.permission.canUpdateUser == 1 ? true : false,
          canDeleteUser: res.permission.canDeleteUser == 1 ? true : false,
        }
      })
    })
  }

  ngOnDestroy(): void {
    this.sub.unsubscribe();
  }

  handleSubmit() {
    this.editUserForm.patchValue({
      permission: {
        canReadUser: this.editUserForm.get('permission.canReadUser')?.value == false ? 0 : 1,
        canCreateUser: this.editUserForm.get('permission.canCreateUser')?.value == false ? 0 : 1,
        canUpdateUser: this.editUserForm.get('permission.canUpdateUser')?.value == false ? 0 : 1,
        canDeleteUser: this.editUserForm.get('permission.canDeleteUser')?.value == false ? 0 : 1,
      }
    })

    if (!this.editUserForm.valid) {
      this.toastr.info('Form is invalid');
      return;
    }

    this.api.editUser(this.user.id, this.editUserForm.value).subscribe(
      res => {
        this.toastr.success('User editted successfully')
        this.router.navigate(['/']);
      }, err => {
        this.toastr.error('Something went wrong')
      }
    )
  }

}
