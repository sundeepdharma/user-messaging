import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { User } from './../model/user.model';
import { UserService } from './../user.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css'],
})
export class UserComponent implements OnInit {
  users: User[];
  userForm: FormGroup;
  submitted: boolean;
  enabledOptions: any[] = [
    { label: 'Enabled', value: true },
    { label: 'Disabled', value: false },
  ];
  roleOptions: string[] = ['ADMIN', 'USER'];
  isAdmin: boolean;
  isAdd: boolean;
  loggedInUser: User = new User();
  userLabel: string;
  enablePassword = false;
  addOrUpdateUser: User = new User();
  alertEnable = false;
  updateResult = false;
  updateText = '';

  constructor(
    private _userService: UserService,
    private _router: Router,
    private _formBuilder: FormBuilder
  ) {}

  ngOnInit(): void {
    if (!this._userService.isLoggedInVal()) {
      this._router.navigate(['login']);
    }

    if (this._userService.isLoggedInVal()) {
      this.loggedInUser = this.getUser();
      this.isAdmin = this.isAdminUser();
    }

    this.userForm = this._formBuilder.group({
      username: [
        '',
        [
          Validators.required,
          Validators.minLength(4),
          Validators.maxLength(100),
        ],
      ],
      password: [
        '',
        [
          Validators.required,
          Validators.minLength(8),
          Validators.maxLength(100),
        ],
      ],
      firstName: ['', [Validators.required, Validators.maxLength(100)]],
      lastName: ['', [Validators.required, Validators.maxLength(100)]],
      enabled: [true, [Validators.required]],
      roles: ['USER', [Validators.required]],
    });

    this.getUsers();
  }

  get userFormControls() {
    return this.userForm.controls;
  }

  getUsers() {
    this._userService.getUsers().subscribe(
      (users) => {
        this.users = users;
      },
      (error) => {}
    );
  }

  showPassword() {
    this.enablePassword = true;
  }

  getUser(): User {
    return this._userService.getUser();
  }

  getLabel(): string {
    if (this.isAdd) {
      return 'Add';
    } else {
      return 'Update';
    }
  }

  populateUserForm(user: User): void {
    this.enablePassword = false;
    this.isAdd = false;
    this.userLabel = 'Update';
    this.userForm.controls.username.setValue(user.username);
    this.userForm.controls.password.setValue(user.password);
    this.userForm.controls.firstName.setValue(user.firstName);
    this.userForm.controls.lastName.setValue(user.lastName);
    this.userForm.controls.enabled.setValue(user.enabled);
    this.userForm.controls.roles.setValue(user.roles.split(','));
    this.alertEnable = false;
  }

  onSubmit() {
    this.submitted = true;
    if (this.userForm.invalid) {
      return;
    }

    this.addOrUpdateUser.username = this.userForm.value.username;
    this.addOrUpdateUser.password = this.userForm.value.password;
    this.addOrUpdateUser.firstName = this.userForm.value.firstName;
    this.addOrUpdateUser.lastName = this.userForm.value.lastName;
    this.addOrUpdateUser.enabled = this.userForm.value.enabled;
    this.addOrUpdateUser.roles = this.userForm.value.roles.join();
    this._userService.addOrUpdateUser(this.addOrUpdateUser).subscribe(
      (result) => {
        this.updateResult = result;
        if (result) {
          this.updateText = 'User saved successfully';
        } else {
          this.updateText = 'User not saved';
        }
        this.alertEnable = true;
        this.submitted = false;
        this.getUsers();
      },
      (error) => {
        this.handleError(error);
        this.updateText = `User not saved - ${error.message}`;
        this.alertEnable = true;
        this.submitted = false;
      }
    );
  }

  isAdminUser(): boolean {
    return this._userService.isAdminUser();
  }

  addUser(): void {
    this.isAdd = true;
    this.enablePassword = true;
    this.userLabel = 'Add';
    this.userForm.reset();
    this.alertEnable = false;
  }

  handleError(error: any): void {
    if (error.status != undefined) {
      if (error.status == 401) {
        this._userService.logout();
        this._router.navigate(['login']);
      }
    }
  }
}
