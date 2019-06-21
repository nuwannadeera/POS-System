import {Component, OnInit, ViewChild} from '@angular/core';
import {Customer} from '../../dto/customer';
import {Item} from '../../dto/item';
import {NgForm} from '@angular/forms';
import {CustomerService} from '../../service/customer.service';
import {ItemService} from '../../service/item.service';

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.css']
})
export class OrderComponent implements OnInit {


  customerList: Array<Customer> = [];
  customer: Customer = new Customer('', '', '', '');
  manually = false;
  cust: Customer = null;
  itemList: Array<Item> = [];
  item: Item = new Item('', '', 0, 0);
  item: Item = null;

  @ViewChild('frmOrder') frmOrder: NgForm;
  constructor(private customerService: CustomerService, private itemService: ItemService) { }

  ngOnInit() {
  this.allCustomer();
  this.customerID();
  this.allFruitItem();
  this.itemID();
  }

private allCustomer(): void {
  this.customerService.getAllCustomer().subscribe(
  value =>
    this.customerList = value
);
}

private allFruitItem(): void {
  this.itemService.getAllItem().subscribe(
  value =>
    this.itemList = value
);
}




private customerID() {
  this.customerService.getAllCustomer().subscribe(
    value =>
      this.customerList = value
  );
}

private itemID() {
  this.itemService.getAllItem().subscribe(
    value =>
      this.itemList = value
  );
}



searchCustomer (event: any): void {
  this.customerService.searchCustomer(this.customer.cid).subscribe(
  (result) => {
    console.log('combo search working.......');
    this.customer = result;
  }
);
}




searchFruitItem(event: any): void {
  this.itemService.searchItem(this.item.itemcode).subscribe(
  (result) => {
    this.item = result;
  }
);
}






selectCustomer (customer: Customer): void {
  this.customer = customer;
this.cust =  Object.assign({}, customer);
this.manually = true;
}

selectFruitItem (fruititem: Item): void {
  this.item = fruititem;
this.item =  Object.assign({}, fruititem);
this.manually = true;
}


}


