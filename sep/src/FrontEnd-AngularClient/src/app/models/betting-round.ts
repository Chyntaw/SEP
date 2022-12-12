import {User} from "./roles/user/user";

export class BettingRound {
  ligaID!:number;
  name!:string;
  isPrivate:boolean= false;
  corrScorePoints!:number;
  corrGoalPoints!:number;
  corrWinnerPoints!:number;
  passwordTipprunde?:string;
  id!:number;
  participants!:User[];


}
