import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface LoginPayload {
  email: string;
  password: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly apiBase =
    (location.hostname === 'localhost' || location.hostname === '127.0.0.1')
      ? 'http://localhost:8082'
      : 'https://stephanedinahet.fr';

  constructor(private http: HttpClient) {}

  login(payload: LoginPayload): Observable<void> {
    return this.http.post<void>(
      `${this.apiBase}/api/auth/login3`,
      payload,
      { withCredentials: true }
    );
  }
}
