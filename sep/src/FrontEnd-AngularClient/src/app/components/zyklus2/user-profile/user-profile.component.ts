import {Component, OnInit} from "@angular/core";
import {GetUserServiceService} from "../../../services/getUserService.service";
import {User} from "../../../models/roles/user/user";
import {ActivatedRoute} from "@angular/router";



@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']

})
export class UserProfileComponent implements OnInit{


  user:User = new User();
  userToShow!: User | any

  retrievedImage: string | any;
  base64Data: string | any;
  retrieveResonse: string | any;



  constructor(private getUserService: GetUserServiceService, private route:ActivatedRoute) { }


  getUser(): void{
    this.getUserService.getUser(this.user.eMail).subscribe(res=>{
      this.userToShow = res
      if(this.userToShow.image != null){
        this.getImage()
      }
    })
  }

  getImage(){
    this.getUserService.getImage(this.user.eMail).subscribe(res=>{
      this.retrieveResonse = res;
      this.base64Data = this.retrieveResonse.picByte;
      this.retrievedImage = 'data:image/jpeg;base64,' + this.base64Data;
    })
  }



  addUser(): void {
    const loggedInEMail:string  | null = sessionStorage.getItem('eMail');

    if(loggedInEMail){
      this.getUserService.addUser(loggedInEMail, this.userToShow.eMail).subscribe(data =>{
        alert("Freund hinzugefügt");
      }, error1 => alert("Freund nicht hinzugefügt"))
    }
  }


  ngOnInit() {
    if(this.route.queryParams){
      this.route.queryParams.subscribe(params=>{
        this.user.eMail = params["eMail"]
        this.getUser()
      })
    }
  }
}
