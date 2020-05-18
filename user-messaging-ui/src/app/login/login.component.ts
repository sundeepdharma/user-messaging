import { SessionService } from './../session.service';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  submitted = false;
  errorMessage: boolean = false;

  constructor(
    private _sessionService: SessionService,
    private _formBuilder: FormBuilder,
    private _router: Router
  ) {}

  ngOnInit(): void {
    this.loginForm = this._formBuilder.group({
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
    });
  }

  get login() {
    return this.loginForm.controls;
  }

  onSubmit() {
    this.submitted = true;

    // stop here if form is invalid
    if (this.loginForm.invalid) {
      return;
    }

    this._sessionService.accessTokenParams.username = this.loginForm.value.username;
    this._sessionService.accessTokenParams.password = this.loginForm.value.password;

    this._sessionService.getToken().subscribe(
      (access) => {
        this._sessionService.setUserSession(access);
        this.errorMessage = false;
        this.setUserSession();
      },
      (error) => {
        this.errorMessage = true;
      }
    );
  }

  setUserSession() {
    this._sessionService.getUser().subscribe(
      (user) => {
        this._sessionService.setUser(user);
        this._sessionService.setLoggedIn(true);
        this.navigateToUserHome();
      },
      (error) => {}
    );
  }

  navigateToUserHome() {
    this._router.navigate(['messages']);
  }
}
