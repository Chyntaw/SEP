export class User {

  eMail!:string;
  password!:string;
 lastName!:string;
  firstName!:string;
  birthDate!:string;
  profilePicture?:File;  //? macht das Attribut optional
  role: string = 'BASIC';
  code?:string;






}
