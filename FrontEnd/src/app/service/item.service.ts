import { Injectable } from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Item} from '../dto/item';

@Injectable({
  providedIn: 'root'
})
export class ItemService {

  readonly baseUrl = environment.APIUrl + '/item';
  readonly baseUrl1 = environment.APIUrl + '/item?itemcode=';

  constructor(private http: HttpClient) {}

  saveItem(item: Item): Observable<boolean> {
    return this.http.post<boolean>(this.baseUrl, item);
  }

  updateItem(item: Item): Observable<boolean> {
    return this.http.put<boolean>(this.baseUrl1 + item.itemcode, item);
  }

  deleteItem(itemcode): Observable<Item> {
    return this.http.delete<Item>(this.baseUrl1 + itemcode);
    console.log(itemcode);
  }

  searchItem(itemcode): Observable<Item> {
    return this.http.get<Item>(this.baseUrl1 + itemcode);
    console.log(itemcode);
  }

  getAllItem(): Observable<Array<Item>> {
    return this.http.get<Array<Item>>(this.baseUrl);
  }
}
