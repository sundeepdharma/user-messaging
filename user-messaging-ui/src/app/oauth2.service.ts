import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class Oauth2Service {
  private _clientId: string;
  private _clientSecret: string;
  private _username: string;
  private _password: string;
  private _grantType: string;

  private _tokenInfo: any;

  constructor(private _http: HttpClient) {
    this._clientId = 'user-messaging-frontend';
    this._clientSecret = 'JdkjshdbksH$PPP87g987f';
    this._grantType = 'password';
  }

  get username(): string {
    return this._username;
  }

  set username(val: string) {
    this._username = val;
  }

  get password(): string {
    return this._password;
  }

  set password(val: string) {
    this._password = val;
  }

  getToken(): Observable<any> {
    const params = new HttpParams()
      .set('client_id', this._clientId)
      .set('client_secret', this._clientSecret)
      .set('username', this._username)
      .set('password', this._password)
      .set('grant_type', this._grantType)
      .set('scope', 'read write');
    return this._http.post<any>('http://localhost:8081/oauth/token', params);
  }
}
