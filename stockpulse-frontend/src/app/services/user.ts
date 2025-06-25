// src/app/services/user.service.ts
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class UserService {
  private baseUrl = 'http://localhost:8081/api/users'; // ✅ Use full backend URL

  constructor(private http: HttpClient) {}

  register(username: string, password: string): Observable<any> {
  return this.http.post(`${this.baseUrl}/register`, {
    username,
    password
  });
}

  login(username: string, password: string): Observable<any> {
  return this.http.post(`${this.baseUrl}/login`, {
    username,
    password
  });
}


  getAllUsers(): Observable<any> {
    return this.http.get(this.baseUrl);
  }
}
