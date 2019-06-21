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
  selectedcustomer: Customer = new Customer('', '', '', '');
  selectedcustomernew: Customer = new Customer('', '', '', '');
  // cust: Customer = null;
  itemList: Array<Item> = [];
  selectitem: Item = new Item('', '', 0, 0);
  selecteditemnew: Item = new Item('', '', 0, 0);
  // item: Item = null;

  @ViewChild('frmcustomer') frmcustomer: NgForm;
  // @ViewChild('frmItem') frmItem: NgForm;
  // @ViewChild('frmOrders') frmOrders: NgForm;

  constructor(private customerService: CustomerService, private itemService: ItemService) { }

  ngOnInit() {
  this.allCustomer();
  // this.customerID();
  this.allFruitItem();
  // this.itemID();
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




// private customerID() {
//   this.customerService.getAllCustomer().subscribe(
//     value =>
//       this.customerList = value
//   );
// }

// private itemID() {
//   this.itemService.getAllItem().subscribe(
//     value =>
//       this.itemList = value
//   );
// }



searchCustomer (event: any): void {
  for (this.selectedcustomer of this.customerList) {
    if (this.selectedcustomer.cid === event.target.value) {
      this.selectedcustomernew = this.selectedcustomer;
    }
  }
}




searchItem(event: any): void {
  for (this.selectitem of this.itemList) {
    if (this.selectitem.itemcode === event.target.value) {
      this.selecteditemnew = this.selectitem;
    }
  }
}



// selectCustomer (customer: Customer): void {
//   this.selectedcustomer = customer;
// this.cust =  Object.assign({}, customer);
// this.manually = true;
// }


// selectFruitItem (fruititem: Item): void {
//   this.selectitem = fruititem;
// this.selectitem =  Object.assign({}, fruititem);
// this.manually = true;
// }


}


