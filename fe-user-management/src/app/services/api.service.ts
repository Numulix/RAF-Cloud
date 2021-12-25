import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { LoginRequest, User } from '../models';

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  constructor(private http: HttpClient) {}

  public login(formData: LoginRequest) {
    return this.http.post<any>(`${environment.BASE_URL}/auth/login`, {
      ...formData,
    });
  }

  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${environment.BASE_URL}/api/users/all`);
  }
}
