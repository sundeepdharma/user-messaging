import { User } from './model/user.model';
import { SessionService } from './session.service';
import { Component } from '@angular/core';
import { THIS_EXPR } from '@angular/compiler/src/output/output_ast';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent {
  isLoggedIn: boolean;
  loggedInUser: User;

  constructor(
    private _sessionService: SessionService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this._sessionService.isLoggedIn().subscribe((value) => {
      this.isLoggedIn = value;
    });
    this._sessionService.getLoggedInUser().subscribe((user) => {
      this.loggedInUser = user;
    });
  }

  logout() {
    this._sessionService.logout();
    this.router.navigate(['login']);
  }
}
