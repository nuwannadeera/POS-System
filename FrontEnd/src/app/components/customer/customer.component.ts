import {Component, OnInit, ViewChild} from '@angular/core';
import {Customer} from '../../dto/customer';
import {NgForm} from '@angular/forms';
import {CustomerService} from '../../service/customer.service';

@Component({
  selector: 'app-customer',
  templateUrl: './customer.component.html',
  styleUrls: ['./customer.component.css']
})
export class CustomerComponent implements OnInit {
  customerList: Array<Customer> = [];
  selectcustomer: Customer = new Customer('', '', '', Number(''));
  manually = false;
  cust: Customer = null;

  @ViewChild('frmCustomers') frmCustomers: NgForm;
  constructor(private customerService: CustomerService) { }

  ngOnInit() {
    this.allCustomer();
  }

  private allCustomer(): void {
    this.customerService.getAllCustomer().subscribe(
      value =>
        this.customerList = value
    );
  }

  tblRowClick(customer: Customer): void {
    this.selectcustomer = Object.assign({}, customer);
  }

  saveCustomer (): void {
    if (!this.frmCustomers.invalid) {

      this.customerService.addCustomer(this.selectcustomer).subscribe(
        (result) => {
          if (result) {
            alert('Customer saved Successfully...');
            this.customerList.push(this.selectcustomer);
            // this.allCustomer();
            this.clear();
          }
        }
      );
    } else {
      alert('Invalid Data input..!');
    }
  }


  private clear() {

    this.customerList = new Array<Customer>();
    this.selectcustomer = new Customer('', '', '', Number(''));

  }

}
