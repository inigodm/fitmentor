import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { v7 as uuidv7 } from 'uuid';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-modify-coach',
  imports: [CommonModule, FormsModule],
  templateUrl: './modify-coach.html',
  styleUrl: './modify-coach.scss'
})
export class ModifyCoach {
    id = uuidv7();
    phonenumber = "";
    presentation = "";
    photo: File | null = null;
    user = "";
    username = "";
    email = "";
    error = '';


  constructor(private http: HttpClient, private router: Router) {
    const nav = this.router.getCurrentNavigation();
    const state = nav?.extras.state as { user?: string, username?: string, email?: string };
    this.user = state?.user ?? '';
    this.username = state?.username ?? '';
    this.email = state?.email ?? '';
  }
  photoPreview: string | null = null;

  onPhotoSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files[0]) {
      const reader = new FileReader();
      reader.onload = e => this.photoPreview = reader.result as string;
      reader.readAsDataURL(input.files[0]);
      this.photo = input.files[0];
    }
  }

  async modifyCoach() {
    let photoBase64 = null;
    if (this.photo) {
      photoBase64 = await this.fileToBase64(this.photo);
    }

    const requestBody: any = {
      id: String(this.id),
      user: String(this.user),
      username: this.username,
      email: this.email
    };

    if (this.phonenumber && this.phonenumber.trim() !== "") {
      requestBody.phonenumber = this.phonenumber;
    }
    if (this.presentation && this.presentation.trim() !== "") {
      requestBody.presentation = this.presentation;
    }
    if (photoBase64) {
      requestBody.photo = photoBase64 as string;
    }

    this.http.post('/api/user/coaches', requestBody, { responseType: 'text' }).subscribe({
      next: () => {
        console.log('Coach creado exitosamente');
        this.router.navigate(['/ruta-destino']);
        this.error = '';
      },
      error: (err) => {
        console.error('Error al crear el coach:', err);
        this.error = 'Error al crear el coach';
      }
    });
  }

  fileToBase64(file: File): Promise<string | ArrayBuffer | null> {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.onload = () => resolve(reader.result);
      reader.onerror = reject;
      reader.readAsDataURL(file);
    });
  }
}
