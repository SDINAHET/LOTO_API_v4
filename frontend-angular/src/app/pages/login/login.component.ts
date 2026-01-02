// import { Component } from '@angular/core';

// @Component({
//   selector: 'app-login',
//   imports: [],
//   templateUrl: './login.component.html',
//   styleUrl: './login.component.scss'
// })
// export class LoginComponent {

// }

import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../services/auth.service';

type MsgType = 'ok' | 'bad' | null;

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {
  private fb = inject(FormBuilder);
  private auth = inject(AuthService);
  private router = inject(Router);

  showPassword = false;
  hasSubmitted = false;

  msgType: MsgType = null;
  msgText = '';

  loading = false;

  form = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(4)]],
  });

  ngOnInit(): void {
    const tokenCookie = this.getCookie('jwtToken');
    const tokenLS = localStorage.getItem('jwtToken');
    if (tokenCookie || tokenLS) {
      this.router.navigateByUrl('');
    }
  }

  togglePassword(): void {
    this.showPassword = !this.showPassword;
  }

  submit(): void {
    this.clearMsg();
    this.hasSubmitted = true;

    if (this.form.invalid) {
      if (this.form.controls.email.invalid) return this.showMsg('bad', '❌ Email invalide.');
      if (this.form.controls.password.invalid) return this.showMsg('bad', '❌ Mot de passe invalide.');
      return this.showMsg('bad', '❌ Formulaire invalide.');
    }

    this.loading = true;

    const { email, password } = this.form.getRawValue();
    this.auth.login({ email: email!, password: password! }).subscribe({
      next: () => {
        this.showMsg('ok', '✅ Login OK ! Redirection...');
        this.startLotoBallsTransition();

        setTimeout(() => this.router.navigateByUrl(''), 1600);
      },
      error: () => {
        this.showMsg('bad', '❌ Login NOK : identifiants incorrects ou serveur indisponible.');
      },
      complete: () => {
        setTimeout(() => (this.loading = false), 800);
      },
    });
  }

  showMsg(type: MsgType, text: string): void {
    this.msgType = type;
    this.msgText = text;
  }
  clearMsg(): void {
    this.msgType = null;
    this.msgText = '';
  }

  private getCookie(name: string): string | null {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop()!.split(';').shift() ?? null;
    return null;
  }

  private rand(min: number, max: number): number {
    return Math.random() * (max - min) + min;
  }

  startLotoBallsTransition(): void {
    const layer = document.getElementById('ballsLayer');
    if (!layer) return;

    layer.innerHTML = '';
    layer.classList.add('show');

    const count = 24;
    for (let i = 0; i < count; i++) {
      const ball = document.createElement('div');
      ball.className = 'ball';

      ball.style.setProperty('--left', `${this.rand(0, 100)}vw`);
      ball.style.setProperty('--size', `${this.rand(38, 62)}px`);
      ball.style.setProperty('--dur', `${this.rand(1.6, 2.6)}s`);
      ball.style.setProperty('--spin', `${this.rand(0.7, 1.4)}s`);
      ball.style.setProperty('--delay', `${this.rand(0, 0.7)}s`);

      ball.textContent = String(Math.floor(this.rand(1, 50)));
      layer.appendChild(ball);
    }

    setTimeout(() => {
      layer.classList.remove('show');
      layer.innerHTML = '';
    }, 3200);
  }
}
