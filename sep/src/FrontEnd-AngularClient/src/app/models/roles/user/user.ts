import {ImageModel} from "../../imageModel";

export class User {

  id!:string;
  eMail!:string;
  password!:string;
  lastName!:string;
  firstName!:string;
  birthDate!:string;
  role: string = 'BASIC';
  code?:string;
  profilePictureName?:any;
  imageModel?:ImageModel;






}
