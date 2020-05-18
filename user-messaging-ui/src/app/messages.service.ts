import { UserMessage } from './model/usermessage.model';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SessionService } from './session.service';
import { Injectable } from '@angular/core';
import { MessageCount } from './model/messagecount.model';

@Injectable({
  providedIn: 'root',
})
export class MessagesService {
  userMessage: MessageCount;
  readMessageUpdate: UserMessage;

  constructor(
    private _sessionService: SessionService,
    private _http: HttpClient
  ) {}

  isLoggedInVal() {
    return this._sessionService.isLoggedInVal();
  }

  getUserMessageCount(): Observable<MessageCount[]> {
    const headers = this._sessionService.headers;
    return this._http.get<MessageCount[]>(
      `${this._sessionService.baseUrl}/api/user-messages/v1/${this._sessionService.user.username}`,
      {
        headers,
      }
    );
  }

  getUserMessageCounts(): Observable<any> {
    const headers = this._sessionService.headers;
    return this._http.get<any>(
      `${this._sessionService.baseUrl}/api/user-messages/v1/${this._sessionService.user.username}`,
      {
        headers,
      }
    );
  }

  getUsername(): string {
    return this._sessionService.user.username;
  }

  getUserLastName(): string {
    return this._sessionService.user.lastName;
  }

  getUserFirstName(): string {
    return this._sessionService.user.firstName;
  }

  getUserMessages(): Observable<UserMessage[]> {
    const headers = this._sessionService.headers;
    return this._http.get<UserMessage[]>(
      `${this._sessionService.baseUrl}/api/user-messages/v1/${this._sessionService.user.username}/${this.userMessage.sender}`,
      {
        headers,
      }
    );
  }

  sendUserMessage(userMessage): Observable<boolean> {
    const headers = this._sessionService.headers;
    return this._http.post<boolean>(
      `${this._sessionService.baseUrl}/api/user-messages/v1`,
      userMessage,
      {
        headers,
      }
    );
  }

  updateReadMessages(): Observable<boolean> {
    const headers = this._sessionService.headers;

    if (this.updateReadMessages != undefined) {
      this.readMessageUpdate = new UserMessage();
    }

    this.readMessageUpdate.sender = this.userMessage.sender;
    this.readMessageUpdate.receiver = this._sessionService.user.username;
    return this._http.put<boolean>(
      `${this._sessionService.baseUrl}/api/user-messages/v1`,
      this.readMessageUpdate,
      {
        headers,
      }
    );
  }

  setUserMessage(val: MessageCount) {
    this.userMessage = val;
  }

  getUserMessage(): MessageCount {
    return this.userMessage;
  }

  logout(): void {
    this._sessionService.logout();
  }
}
