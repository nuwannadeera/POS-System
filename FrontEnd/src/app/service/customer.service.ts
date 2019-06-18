import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Customer} from '../dto/customer';
import {Observable} from 'rxjs';
import {environment} from '../../environments/environment';



@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  readonly baseUrl = environment.APIUrl + '/customer';

  constructor(private http: HttpClient) {}

  addCustomer(customer: Customer): Observable<boolean> {
    return this.http.post<boolean>(this.baseUrl, customer);
  }

  updateCustomer(customer: Customer): Observable<boolean> {
    return this.http.put<boolean>(this.baseUrl, customer);
  }

  deleteCusomer(id): Observable<Customer> {
    return this.http.delete<Customer>(this.baseUrl + '/delete/' + id);
    console.log(id);
  }

  searchCustomer(id): Observable<Customer> {
    return this.http.get<Customer>(this.baseUrl + '/search/' + id);
    console.log(id);
  }

  getAllCustomer(): Observable<Array<Customer>> {
    return this.http.get<Array<Customer>>(this.baseUrl);
  }
}
