import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
// RxJS
import { Observable } from 'rxjs';

@Injectable()
export class RestService {

  constructor(private http: HttpClient) { }

  getCall(URL: string): Observable<HttpResponse<object>> {
    return this.http.get<HttpResponse<object>>(URL);
  }

  putCall(URL: string, dto: object): Observable<HttpResponse<object>> {
    return this.http.put<HttpResponse<object>>(URL, dto, {});
  }

  postCall(URL: string, dto: object): Observable<HttpResponse<object>> {
    return this.http.post<HttpResponse<object>>(URL, dto, {});
  }

  postToImportFile(URL: string, file: File): Observable<HttpResponse<object>> {
    const formData: FormData = new FormData();
    formData.append('file', file, file.name);
    return this.http.post<HttpResponse<object>>(URL, formData, {});
  }

}
