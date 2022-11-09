import { Component, OnInit } from '@angular/core';
import {FileUploadService} from "../../services/services/file-upload.service";
import {Liga} from "../../models/liga";

@Component({
  selector: 'app-create-league',
  templateUrl: './create-league.component.html',
  styleUrls: ['./create-league.component.css']
})
export class CreateLeagueComponent implements OnInit {
  selectedFiles?: FileList;
  currentFile?: File;
  liga:Liga = new Liga();

  constructor(private fileUploadService: FileUploadService) { }

  selectFile(event: any): void {
    this.selectedFiles = event.target.files;
  }

  upload(): void {
    if (this.selectedFiles) {
      const file: File | null = this.selectedFiles.item(0);

      if (file) {
        this.currentFile = file;

        this.fileUploadService.upload(this.currentFile).subscribe()
        alert("Liga wurde erstellt")
      }}
  }


  ngOnInit(): void {

  }

}
