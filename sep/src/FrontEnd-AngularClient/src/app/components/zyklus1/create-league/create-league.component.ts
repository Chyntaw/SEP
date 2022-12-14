import { Component, OnInit } from '@angular/core';
import {FileUploadService} from "../../../services/services/file-upload.service";
import {Liga} from "../../../models/liga";

@Component({
  selector: 'app-create-league',
  templateUrl: './create-league.component.html',
  styleUrls: ['./create-league.component.css']
})
export class CreateLeagueComponent implements OnInit {
  selectedFiles?: FileList;
  currentFile?: File;
  pictureFiles?: FileList;
  currentPicture?: File;
  liga:Liga = new Liga();

  constructor(private fileUploadService: FileUploadService) { }

  selectFile(event: any): void {
    this.selectedFiles = event.target.files;
  }
  selectProfilePicture(event: any): void {
    this.pictureFiles = event.target.files;
  }

  upload(): void {
    if (this.selectedFiles) {
      const file: File | null = this.selectedFiles.item(0);

      if(this.pictureFiles){
        const pictureFile: File | null = this.pictureFiles?.item(0);

        if (file) {
          this.currentFile = file;
          if(pictureFile){
            this.currentPicture = pictureFile;
            this.fileUploadService.upload(this.currentFile, this.liga.name, this.currentPicture).subscribe(data=>
              alert("Liga wurde erstellt"),error => alert('Liga konnte nicht erstellt werden'))

          }
        }
      }
      else{
        if(file){
          this.currentFile = file;
          this.fileUploadService.uploadWithoutPicture(this.currentFile, this.liga.name).subscribe(data=>
            alert("Liga Ohne Bild wurde erstellt"),error => alert('Liga ohne Bild konnte nicht erstellt werden'))

        }
      }
    }
  }
// zweite if wenn kein profilbild


  ngOnInit(): void {

  }

}
