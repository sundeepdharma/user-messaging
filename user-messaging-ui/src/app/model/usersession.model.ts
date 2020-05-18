export class UserSession {
  isLoggedIn: boolean = false;
  accessToken: string;
  refreshToken: string;
  username: string;
  validTill: Date;
}
