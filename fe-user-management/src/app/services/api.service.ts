import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { LoginRequest } from '../models';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  constructor(
    private http: HttpClient
  ) { }

  public login(formData: LoginRequest) {
    return this.http.post<any>(
      `${environment.BASE_URL}/auth/login`,
      {
        ...formData
      }
    )
  }
}
