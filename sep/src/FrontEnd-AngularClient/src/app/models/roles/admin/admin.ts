export class Admin {

  eMail!:string;
  password!:string;
  lastName!:string;
  firstName!:string;
  birthDate:string ='';
  role: string = 'ADMIN';


  public getRole(): string {
    return this.role;
  }

  public setRole(value: string) {
    this.role = value;
  }

}
