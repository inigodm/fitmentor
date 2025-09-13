import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-client-home',
  imports: [],
  templateUrl: './client-home.html',
  styleUrl: './client-home.scss'
})
export class ClientHome implements OnInit {
      userId = "";
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

    async registerWithBiometrics() {
    this.http.get('/webauthn/register/challenge?userId=' + this.userId)
      .subscribe((options: any) => {
          this.startRegistration(options.challenge)
      });
    }
  
  base64urlToUint8Array(base64url: string): Uint8Array {
    const base64 = base64url.replace(/-/g, '+').replace(/_/g, '/');
    const pad = '='.repeat((4 - (base64.length % 4)) % 4);
    return Uint8Array.from(atob(base64 + pad), c => c.charCodeAt(0));
  }

  uint8ArrayToBase64url(bytes: Uint8Array): string {
  let binary = "";
  bytes.forEach(b => binary += String.fromCharCode(b));
  return btoa(binary)
    .replace(/\+/g, "-")
    .replace(/\//g, "_")
    .replace(/=+$/, "");
}

  async startRegistration(challenge: string) {
    const publicKeyCredentialCreationOptions: PublicKeyCredentialCreationOptions = {
      challenge: this.base64urlToUint8Array(challenge),  // aquí usas tu helper
      rp: {
        name: "FitMentor",
        id: "fitmentor.com"
      },
      user: {
        id: this.base64urlToUint8Array(this.userId), // el userId también tiene que ser un buffer
        name: this.user,
        displayName: this.user
      },
      pubKeyCredParams: [
        { type: "public-key", alg: -7 },   // ES256
        { type: "public-key", alg: -257 }  // RS256
      ],
      authenticatorSelection: {
        authenticatorAttachment: "platform",
        userVerification: "required"
      },
      timeout: 60000,
      attestation: "direct"
    };

    const credential = await navigator.credentials.create({
      publicKey: publicKeyCredentialCreationOptions
    }) as PublicKeyCredential;

    this.sendToBackend(credential);
  }

sendToBackend(credential: PublicKeyCredential) {
  const attestationResponse = credential.response as AuthenticatorAttestationResponse;

  const body = {
    id: credential.id, // string
    rawId: this.uint8ArrayToBase64url(new Uint8Array(credential.rawId)),
    type: credential.type,
    clientDataJSON: this.uint8ArrayToBase64url(new Uint8Array(attestationResponse.clientDataJSON)),
    attestationObject: this.uint8ArrayToBase64url(new Uint8Array(attestationResponse.attestationObject)),
    userId: this.userId // string UUID que ya usaste en el challenge
  };

  this.http.post('/webauthn/register', body).subscribe({
    next: () => console.log("✅ Registro FIDO2 completado"),
    error: err => console.error("❌ Error en el registro", err)
  });
}




  ngOnInit(): void {
    this.http.get('/api/user/clients')
      .subscribe({
        next: (data: any) => {
          this.userId = data.user;
          this.goals = data.goals;
          this.age = data.age;
          this.phonenumber = data.phonenumber;
          this.equipmentAccess = data.equipmentAccess == 1;
          this.weight = data.weight;
        },
        error: (err) => {
          console.error('Error en la llamada a /api/user/clients', err);
        }
      });
  }
}
