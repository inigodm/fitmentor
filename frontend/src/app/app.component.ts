import { Component } from '@angular/core';
import { RouterOutlet, Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, CommonModule, RouterModule],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'frontend';
    constructor(public router: Router) {}
    isLoggedIn(): boolean {
      // Aquí puedes verificar la existencia del JWT o cualquier lógica de autenticación
      return !!localStorage.getItem('auth_token'); // Ejemplo: verifica si hay un JWT en localStorage
    }

    logout(): void {
      localStorage.removeItem('auth_token');
      this.router.navigate(['/login'])
    }
}
