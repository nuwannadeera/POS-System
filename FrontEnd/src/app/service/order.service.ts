import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  readonly baseUrl = environment.APIUrl + '/order';
  readonly baseUrl1 = environment.APIUrl + '/order?oid=';

  constructor(private http: HttpClient) { }


}
