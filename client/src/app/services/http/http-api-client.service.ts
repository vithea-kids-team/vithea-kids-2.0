import {Injectable} from '@angular/core';
import {Http, RequestOptionsArgs, Response, Headers} from '@angular/http';
import { Router } from '@angular/Router'
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/catch';

@Injectable()
export class HttpApiClient {

  SERVER : string = 'vithea-kids-api';
  
  constructor(private http: Http, public router : Router) {}
  private addHeaders(options?: RequestOptionsArgs) {
    if(options == null || options == undefined) {
      options = { headers: new Headers() };
    }
    
    if(!options.headers.has('Content-Type')) {
      options.headers.append('Content-Type', 'application/json');
    }
    
    if(!options.headers.has('Accept')) {
      options.headers.append('Accept', 'application/json');
    }
    
    if(!options.headers.has('Authorization')) {
      let jwt = localStorage.getItem('Authorization');
      options.headers.append('Authorization', jwt); 
    }
    
    return options;
  }
  get(url: string, options?: RequestOptionsArgs): Observable<Response> {
    options = this.addHeaders(options);
      return this.http.get(this.SERVER + url, options)
      .map(res => {
        return res;
      });
  }
  post(url: string, body: Object, options?: RequestOptionsArgs): Observable<Response> {
    options = this.addHeaders(options);
    return this.http.post(this.SERVER + url, body, options);
  }
  put(url: string, body: string, options?: RequestOptionsArgs): Observable<Response>{
    options = this.addHeaders(options);
    return this.http.put(this.SERVER + url, body, options);
  }
  delete(url: string, options?: RequestOptionsArgs): Observable<Response>{
    options = this.addHeaders(options);
    return this.http.delete(this.SERVER + url, options);
  }
  patch(url: string, body: string, options?: RequestOptionsArgs): Observable<Response>{
    options = this.addHeaders(options);
    return this.http.patch(this.SERVER + url, body, options);
  }
  head(url: string, options?: RequestOptionsArgs): Observable<Response>{
    options = this.addHeaders(options);
    return this.http.head(this.SERVER + url, options);
  }
  upload(url: string, files:File[]): Observable<any> {
    return Observable.create(observer => {
            let formData: FormData = new FormData();
            let xhr: XMLHttpRequest = new XMLHttpRequest();
            for (let i = 0; i < files.length; i++) {
                formData.append("File", files[i], files[i].name);
            }
            xhr.onreadystatechange = () => {
                if (xhr.readyState === 4) {
                    if (xhr.status === 200) {
                        observer.next(JSON.parse(xhr.response));
                        observer.complete();
                    } else {
                        observer.error(xhr.response);
                    }
                }
            };
            xhr.open('PUT', this.SERVER + url , true);
            xhr.setRequestHeader('Accept', 'application/json');
            xhr.setRequestHeader('Authorization',localStorage.getItem('Authorization'));
            xhr.send(formData);
        });
  }
}