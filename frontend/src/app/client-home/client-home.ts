import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-client-home',
  imports: [],
  templateUrl: './client-home.html',
  styleUrl: './client-home.scss'
})
export class ClientHome implements OnInit {
      id = "";
      goals = "";
      age = 0;
      injuries = "";
      weight = 0;
      equipmentAccess = false;
      preferedTrainingStyle = "";
      phonenumber = "";
      user = "";
      coach = "";

  constructor(private http: HttpClient) {

  }

  ngOnInit(): void {
    this.http.get('/api/user/clients')
          .subscribe((data: any) => {
            this.id = data.id;
            this.goals = data.goals;
            this.age = data.age;
            this.phonenumber = data.phonenumber;
            this.equipmentAccess = data.equipmentAccess == 1;
            this.weight = data.weight;
    });
  }
}
