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
    error = '';


  constructor(private http: HttpClient, private router: Router) {
    const nav = this.router.getCurrentNavigation();
    const state = nav?.extras.state as { user?: string, username?: string };
    this.user = state?.user ?? '';
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
    const formData = new FormData();
    formData.append('id', String(this.id));
    if (this.phonenumber && this.phonenumber.trim() !== "") {
      formData.append('phonenumber', this.phonenumber);
    }
    if (this.presentation && this.presentation.trim() !== "") {
      formData.append('presentation', this.presentation);
    }
    formData.append('user', String(this.user));
    if (this.photo) {
      const base64 = await this.fileToBase64(this.photo);
      formData.append('photo', base64 as string);
    } else {
      formData.append('photo', '');
    }

    this.http.post('/api/user/coaches', formData, { responseType: 'text' }).subscribe({
      next: () => {
        console.log('Coach creado exitosamente');
        this.router.navigate(['/ruta-destino']);
        this.error = '';
      },
      error: (err) => {
        console.error('Error al crear el usuario:', err);
        this.error = 'Error al crear el usuario';
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
