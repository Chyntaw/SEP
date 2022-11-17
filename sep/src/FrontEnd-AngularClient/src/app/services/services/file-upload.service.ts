import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';
import {Liga} from "../../models/liga";

@Injectable({
  providedIn: 'root'
})
export class FileUploadService {
  private baseUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) { }

  upload(file: File, name: string, picture: File): Observable<object> {

    const formData: FormData = new FormData();
    formData.append('file', file);
    formData.append('name', name);
    formData.append('picture', picture);

    return this.http.post(`${this.baseUrl}/liga/upload`, formData)
  }

  uploadWithoutPicture(file: File, name: string): Observable<object>{
    const formData: FormData = new FormData();
    formData.append('file', file);
    formData.append('name', name);

    return this.http.post(`${this.baseUrl}/liga/upload`, formData)
  }


  getFiles(): Observable<any> {
    return this.http.get(`${this.baseUrl}/files`);
  }
}
