import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {Orders} from '../dto/orders';
import {Observable} from 'rxjs';
import {ItemDetail} from '../dto/ItemDetail';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  readonly baseUrl = environment.APIUrl + '/order';
  readonly baseUrl1 = environment.APIUrl + '/itemdetail';

  constructor(private http: HttpClient) { }

  saveOrder(order: Orders): Observable<boolean> {
    console.log(order);
    return this.http.post<boolean>(this.baseUrl, order);
  }

  saveOrderDetails(orderdtl: ItemDetail): Observable<boolean> {
    console.log(orderdtl);
    return this.http.post<boolean>(this.baseUrl1, orderdtl);
  }
}
