import { Injectable } from '@angular/core';

@Injectable()
export class Configuration {
    public Server: string = "http://localhost:9090/";
    public ApiUrl: string = "app/";
    public ServerWithApiUrl = this.Server + this.ApiUrl;
}