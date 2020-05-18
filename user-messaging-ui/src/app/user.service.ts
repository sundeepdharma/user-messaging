import { User } from './model/user.model';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { SessionService } from './session.service';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(
    private _sessionService: SessionService,
    private _http: HttpClient
  ) {}

  isLoggedInVal() {
    return this._sessionService.isLoggedInVal();
  }

  getUsers(): Observable<User[]> {
    const headers = this._sessionService.headers;
    return this._http.get<User[]>(
      `${this._sessionService.baseUrl}/api/users/v1`,
      {
        headers,
      }
    );
  }

  getLoggedInUser(): User {
    return this._sessionService.getLoggedInUserValue();
  }

  addOrUpdateUser(user: User): Observable<boolean> {
    const headers = this._sessionService.headers;
    return this._http.post<boolean>(
      `${this._sessionService.baseUrl}/api/users/v1`,
      user,
      {
        headers,
      }
    );
  }

  isAdminUser(): boolean {
    return (
      this._sessionService.getLoggedInUserValue().roles.indexOf('ADMIN') > -1
    );
  }

  getUser(): User {
    return this._sessionService.getLoggedInUserValue();
  }

  logout(): void {
    this._sessionService.logout();
  }
}
