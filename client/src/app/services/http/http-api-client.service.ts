import {Injectable} from '@angular/core';
import {Http, RequestOptionsArgs, Response, Headers} from '@angular/http';
import {Observable} from 'rxjs/Observable';

@Injectable()
export class HttpApiClient {
  
  constructor(private http: Http) {}
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
    if(url.indexOf('/app/')!=-1){
      return this.http.get(url, options)
      .map(res => {
        return res;
      });
    }
    else{
      return this.http.get(url, options)
      .map(res => {
        return res;
      });
    }
    
  }
  post(url: string, body: string, options?: RequestOptionsArgs): Observable<Response> {
    options = this.addHeaders(options);
    return this.http.post(url, body, options);
  }
  put(url: string, body: string, options?: RequestOptionsArgs): Observable<Response>{
    options = this.addHeaders(options);
    return this.http.put(url, body, options);
  }
  delete(url: string, options?: RequestOptionsArgs): Observable<Response>{
    options = this.addHeaders(options);
    return this.http.delete(url, options);
  }
  patch(url: string, body: string, options?: RequestOptionsArgs): Observable<Response>{
    options = this.addHeaders(options);
    return this.http.patch(url, body, options);
  }
  head(url: string, options?: RequestOptionsArgs): Observable<Response>{
    options = this.addHeaders(options);
    return this.http.head(url, options);
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
            xhr.open('PUT', url , true);
            xhr.setRequestHeader('Accept', 'application/json');
            xhr.setRequestHeader('Authorization',localStorage.getItem('Authorization'));
            xhr.send(formData);
        });
  }
}