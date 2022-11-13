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

  upload(file: File, name: string, picture: File): Observable<HttpEvent<any>> {

    const formData: FormData = new FormData();
    formData.append('file', file);
    formData.append('name', name);
    formData.append('picture', picture);

    const req = new HttpRequest('POST', `${this.baseUrl}/liga/upload`, formData, {
      reportProgress: true,
      responseType: 'json'
    });

    return this.http.request(req);
  }

  uploadWithoutPicture(file: File, name: string): Observable<HttpEvent<any>>{
    const formData: FormData = new FormData();
    formData.append('file', file);
    formData.append('name', name);

    const req = new HttpRequest('POST', `${this.baseUrl}/liga/upload`, formData,{
      reportProgress: true,
      responseType: 'json'
    });

    return this.http.request(req);
  }


  getFiles(): Observable<any> {
    return this.http.get(`${this.baseUrl}/files`);
  }
}
