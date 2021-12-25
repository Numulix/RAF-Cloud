import { Injectable } from '@angular/core';
import { Permissions } from '../models';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private _loggedIn: boolean = false;
  private _permissions!: Permissions;
  private _jwtToken: string = '';

  constructor() { }

  get loggedIn(): boolean {
    return this._loggedIn;
  }

  get permissions(): Permissions {
    return this._permissions;
  }

  set jwtToken(token: string) {
    this._jwtToken = token;
  }

  toggleLogin() {
    this._loggedIn = !this._loggedIn;
  }

  setPermissions(token: string) {
    const perms = JSON.parse(atob(token.split('.')[1])).permissions;
    this._permissions = perms;
  }

  login(token: string) {
    this.jwtToken = token;
    this.setPermissions(token);
  }

  checkJWTToken(): boolean {
    const token = localStorage.getItem('jwt_token');
    if (token) {
      this.setPermissions(token);
      this._loggedIn = true;
      return true;
    }
    return false;
  }

  isLoggedIn(): boolean {
    if (this._loggedIn == true) return true
    return this.checkJWTToken();
  }  

}
