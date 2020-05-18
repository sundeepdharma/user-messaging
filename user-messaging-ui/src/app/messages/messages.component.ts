import { Observable } from 'rxjs';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserMessage } from './../model/usermessage.model';
import { MessagesService } from './../messages.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { MessageCount } from '../model/messagecount.model';

@Component({
  selector: 'app-messages',
  templateUrl: './messages.component.html',
  styleUrls: ['./messages.component.css'],
})
export class MessagesComponent implements OnInit, OnDestroy {
  messageCounts: MessageCount[];
  userMessage: MessageCount;
  userMessages: UserMessage[];
  messageCountInterval: any;
  userMessagesInterval: any;
  messageForm: FormGroup;
  messagePost: UserMessage;
  submitted: boolean;

  constructor(
    private _messagesService: MessagesService,
    private _router: Router,
    private _formBuilder: FormBuilder
  ) {}

  ngOnInit(): void {
    if (!this._messagesService.isLoggedInVal()) {
      this._router.navigate(['login']);
    }

    this.submitted = false;
    this.messageForm = this._formBuilder.group({
      message: ['', [Validators.required, Validators.maxLength(255)]],
    });

    this.getMessageCount();
    this.messageCountInterval = setInterval(() => {
      this.getMessageCount();
    }, 5000);

    if (this._messagesService.getUserMessage() != undefined) {
      this.userMessage = this._messagesService.getUserMessage();
      this.getMessages();
    }
    this.userMessagesInterval = setInterval(() => {
      this.getMessages();
    }, 5000);
  }

  get messageFormControls() {
    return this.messageForm.controls;
  }

  getMessageCount(): void {
    if (this._messagesService.getUsername() != undefined) {
      this._messagesService.getUserMessageCount().subscribe(
        (messageCounts) => {
          this.messageCounts = messageCounts;
        },
        (error) => {
          this.handle(error);
        }
      );
    }
  }

  handleOnFocus(): void {
    this.getUserMessages(this.userMessage);
  }

  getUserMessages(userMessage): void {
    if (
      this.userMessage == undefined ||
      this.userMessage.sender != userMessage.sender
    ) {
      this.userMessage = userMessage;
    }

    this._messagesService.setUserMessage(this.userMessage);
    if (this.messageCountInterval) {
      clearInterval(this.messageCountInterval);
    }
    this.updateReadMessages();
    this.getMessages();
  }
  getMessages(): void {
    if (this._messagesService.getUserMessage() != undefined) {
      this._messagesService.getUserMessages().subscribe(
        (userMessages) => {
          this.userMessages = userMessages;
        },
        (error) => {
          this.handle(error);
        }
      );
    }
  }

  onSubmit() {
    this.submitted = true;
    if (this.messageForm.invalid) {
      return;
    }
    if (this.messagePost === undefined) {
      this.messagePost = new UserMessage();
    }
    this.messagePost.sender = this._messagesService.getUsername();
    this.messagePost.receiver = this.userMessage.sender;
    this.messagePost.message = this.messageForm.value.message;
    this.sendUserMessage();
  }

  getUserLastName(): string {
    return this._messagesService.getUserLastName();
  }

  getUserFirstName(): string {
    return this._messagesService.getUserFirstName();
  }

  sendUserMessage(): void {
    this._messagesService.sendUserMessage(this.messagePost).subscribe(
      (result) => {
        if (result) {
          this.getMessages();
          this.messageForm.reset();
          this.submitted = false;
        }
      },
      (error) => {
        this.handle(error);
      }
    );
  }

  updateReadMessages(): void {
    this._messagesService.updateReadMessages().subscribe(
      (result) => {
        this.getMessageCount();
        this.messageCountInterval = setInterval(() => {
          this.getMessageCount();
        }, 5000);
      },
      (error) => {
        this.handle(error);
      }
    );
  }

  handle(error: any): void {
    if (error.status != undefined) {
      if (error.status == 401) {
        this._messagesService.logout();
        this._router.navigate(['login']);
      }
    }
  }

  ngOnDestroy(): void {
    if (this.messageCountInterval) {
      clearInterval(this.messageCountInterval);
    }
    if (this.userMessagesInterval) {
      clearInterval(this.userMessagesInterval);
    }
  }
}
