import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { v7 as uuidv7 } from 'uuid';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-create-user',
  templateUrl: './modify-user.html',
  styleUrl: './modify-user.scss',
  imports: [CommonModule, FormsModule]
})
export class CreateUser implements OnInit {
  id = uuidv7();
  username = '';
  email = '';
  password = '';
  passwordCheck = '';
  type = '';
  role = '';
  error = '';

  constructor(private route: ActivatedRoute, private http: HttpClient, private router: Router) {}

  createUser() {
    if (this.password != this.passwordCheck) {
      this.error = "Las contraseñas no coinciden";
    } else {
      this.http.put('/api/user',
        { id: this.id, username: this.username, password: this.password, email: this.email, role: this.role },
        { responseType: 'text'}).subscribe({
        next: () => {
          var url = '/create/' + this.type;
          this.router.navigate([url], {
            state: { user: this.id, username: this.username, email: this.email }
          });
          this.error = '';
        },
        error: (err) => {
          console.error('Error al crear el usuario:', err);
          this.error = 'Error al crear el usuario';
        }
      });
    }
  }

  async registerWithBiometrics() {
    this.http.get('/webauthn/register/challenge?userId=' + this.id)
      .subscribe((options: any) => {
        options.challenge = this.base64urlToUint8Array(options.challenge);
          options.user.id = this.base64urlToUint8Array(options.user.id);
          console.log("Challenge: " + options.challenge);
          console.log("UserId: " + options.user.id);
      });
    }
  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.type = params.get('type') ?? '';
      this.role = this.type.toUpperCase();
      console.log('Type:', this.type);
    });
  }

  base64urlToUint8Array(base64url: string): Uint8Array {
    const base64 = base64url.replace(/-/g, '+').replace(/_/g, '/');
    const pad = '='.repeat((4 - (base64.length % 4)) % 4);
    return Uint8Array.from(atob(base64 + pad), c => c.charCodeAt(0));
  }



}
