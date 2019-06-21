import {Component, OnInit, ViewChild} from '@angular/core';
import {Customer} from '../../dto/customer';
import {Item} from '../../dto/item';
import {NgForm} from '@angular/forms';
import {CustomerService} from '../../service/customer.service';
import {ItemService} from '../../service/item.service';
import {Orders} from '../../dto/orders';
import {ItemDetail} from '../../dto/ItemDetail';
import {OrderService} from '../../service/order.service';

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.css']
})
export class OrderComponent implements OnInit {


  customerList: Array<Customer> = [];
  selectedcustomer: Customer = new Customer('', '', '');
  selectedcustomernew: Customer = new Customer('', '', '');
  selectedcustomernew1: Customer = new Customer('', '', '');
  itemList: Array<Item> = [];
  selectitem: Item = new Item('', '', 0, 0);
  selecteditemnew: Item = new Item('', '', 0, 0);
  selecteditemnew1: Item = new Item('', '', 0, 0);

  orderlist: Orders[] = [];
  selectedorders: Orders = new Orders('', '', '', '');
  orderdetaillist: ItemDetail[] = [];
  selecteditemdetails: ItemDetail = new ItemDetail('', '', '', '');
  orders: Orders;
  Total: 0;
  FullTotal: 0;


  @ViewChild('frmcustomer') frmcustomer: NgForm;
  @ViewChild('frmItem') frmItem: NgForm;
  @ViewChild('frmOrders') frmOrders: NgForm;

  constructor(private customerService: CustomerService, private itemService: ItemService, private orderservice: OrderService) { }

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


  saveOrder(): void {
    this.selectedorders = this.orders;

    this.orderservice.saveOrder(this.selectedorders)
      .subscribe(resp => {
        if (resp) {
          for (this.selecteditemdetails of this.orderdetaillist) {
            this.orderservice.saveOrderDetails(this.selecteditemdetails)
              .subscribe(resp1 => {
                if (resp && resp1) {
                  alert('Order has been saved successfully....');
                } else {
                  alert('Failed to save the Order.....');
                }
              });
          }
        }
      });

  }





}


