import { ErrorIntercept } from './error.interceptor';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AppRoutingModule, RoutingComponents } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { ReplaceLineBreaks } from './utils/replacelinebreaks.pipe';

@NgModule({
  declarations: [
    AppComponent,
    RoutingComponents,
    LoginComponent,
    ReplaceLineBreaks,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: ErrorIntercept, multi: true },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
