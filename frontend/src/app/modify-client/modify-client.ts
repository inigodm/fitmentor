import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { v7 as uuidv7 } from 'uuid';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-modify-client',
  imports: [CommonModule, FormsModule],
  templateUrl: './modify-client.html',
  styleUrl: './modify-client.scss'
})
export class ModifyClient {
    id = uuidv7();
    goals = "";
    age = 0;
    injuries = "";
    weight = 0;
    equipmentAccess = false;
    preferedTrainingStyle = "";
    phonenumber = "";
    user = "";
    coach = "";
    error = '';


  constructor(private http: HttpClient, private router: Router) {
      const nav = this.router.getCurrentNavigation();
      const state = nav?.extras.state as { user?: string, username?: string };
      this.user = state?.user ?? '';
  }

    modifyClient() {
      console.log("pesa" + this.weight);
      console.log("por tantop" + Math.round(this.weight * 1000));
      this.http.put('/api/user/clients',
        { id: this.id,
          goals: this.goals,
          age: this.age,
          injuries: this.injuries,
          weight: Math.round(this.weight * 1000),
          equipmentAccess: this.equipmentAccess ? 1 : 0,
          preferedTrainingStyle: this.preferedTrainingStyle,
          phonenumber: this.phonenumber,
          user: this.user,
          coach: this.coach },
        { responseType: 'text'}).subscribe({
        next: () => {
          console.log('Usuario creado exitosamente');
          this.router.navigate(['/client']);
          this.error = '';
        },
        error: (err) => {
          console.error('Error al crear el usuario:', err);
          this.error = 'Error al crear el usuario';
        }
      });
    }
  }
