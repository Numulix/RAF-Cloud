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

  createNewUser(formData: any): Observable<any> {
    return this.http.post<any>(
      `${environment.BASE_URL}/api/users/create`,
      formData
    )
  }

  getUserById(id: number): Observable<User> {
    return this.http.get<User>(`${environment.BASE_URL}/api/users/${id}`);
  }

  editUser(id: number, formData: any) {
    return this.http.put<any>(`${environment.BASE_URL}/api/users/update/${id}`, formData);
  }

  deleteUser(id: number) {
    return this.http.delete<any>(
      `${environment.BASE_URL}/api/users/delete/${id}`
    )
  }

}
