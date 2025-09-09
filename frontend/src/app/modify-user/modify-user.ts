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
            state: { user: this.id, username: this.username }
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

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.type = params.get('type') ?? '';
      this.role = this.type.toUpperCase();
      console.log('Type:', this.type);
    });
  }
}
