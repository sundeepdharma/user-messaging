import { User } from './model/user.model';
import { AccessTokenParams } from './model/accesstokenparams.model';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, Subject, BehaviorSubject } from 'rxjs';
import { UserSession } from './model/usersession.model';
import { AccessCredentialToken } from './model/accesscredentialtoken.model';

@Injectable({
  providedIn: 'root',
})
export class SessionService {
  baseUrl = 'http://localhost:8081';

  private loggedIn: BehaviorSubject<boolean>;
  private userObs: BehaviorSubject<User>;

  userSession: UserSession = new UserSession();
  accessTokenParams: AccessTokenParams = new AccessTokenParams();
  headers: any;
  user = new User();

  constructor(private _http: HttpClient) {
    this.loggedIn = new BehaviorSubject<boolean>(false);
    this.userObs = new BehaviorSubject<User>(new User());
  }

  setLoggedIn(value): void {
    this.loggedIn.next(value);
  }

  isLoggedIn(): Observable<boolean> {
    return this.loggedIn.asObservable();
  }

  isLoggedInVal(): boolean {
    return this.loggedIn.getValue();
  }

  setUserSession(access: AccessCredentialToken) {
    this.userSession.accessToken = access.access_token;
    this.userSession.isLoggedIn = true;
    this.userSession.validTill = this.getVaildTill(access.expires_in);
    this.headers = new HttpHeaders({
      Authorization: `Bearer ${this.userSession.accessToken}`,
    });
  }

  getToken(): Observable<any> {
    const params = new HttpParams()
      .set('client_id', this.accessTokenParams.clientId)
      .set('client_secret', this.accessTokenParams.clienSecret)
      .set('username', this.accessTokenParams.username)
      .set('password', this.accessTokenParams.password)
      .set('grant_type', this.accessTokenParams.grantType)
      .set('scope', this.accessTokenParams.scope);
    return this._http.post<any>(`${this.baseUrl}/oauth/token`, params);
  }

  setUser(val: User) {
    this.user = val;
    this.userObs.next(val);
  }

  getLoggedInUser(): Observable<User> {
    return this.userObs.asObservable();
  }

  getLoggedInUserValue(): User {
    return this.userObs.getValue();
  }

  getUser(): Observable<User> {
    const headers = this.headers;
    return this._http.get<User>(
      `${this.baseUrl}/api/users/v1/${this.accessTokenParams.username}`,
      {
        headers: this.headers,
      }
    );
  }

  private getVaildTill(seconds: number) {
    var time = new Date();
    time.setSeconds(time.getSeconds() + seconds);
    return time;
  }

  logout() {
    this.setLoggedIn(false);
    this.setUser(new User());
  }
}
