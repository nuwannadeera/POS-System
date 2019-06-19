import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
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
  selectcustomer: Customer = new Customer('', '', '', '');
  manually = false;
  cust: Customer = null;

  // @ViewChild('c1name') c1name: ElementRef;
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
    console.log(this.customerList);
  }

  tblRowClick(customer: Customer): void {
    this.selectcustomer = Object.assign({}, customer);
    console.log('clicked table row is..' + this.selectcustomer);
  }

  saveCustomer (): void {
    if (!this.frmCustomers.invalid) {

      this.customerService.addCustomer(this.selectcustomer).subscribe(
        (result) => {
          if (result) {
            alert('Customer saved Successfully...');
            this.customerList.push(this.selectcustomer);
            this.clear();
            console.log(result);
          }
        }
      );
    } else {
      alert('Invalid Data input..!');
    }
  }



  updateCustomer (): void {
    console.log('update btn working...');
    if (!this.frmCustomers.invalid) {

      this.customerService.updateCustomer(this.selectcustomer).subscribe(
      (result) => {
        console.log(result);
        if (result) {
          alert('Updated Successfully...');
          this.manually = true;
          this.customerList.push(this.selectcustomer);
          this.clear();
        }
      }
    );
  } else {
      alert('Invalid Data input..!');
    }
  }

  private clear() {
    // this.customerList = new Array<Customer>();
    this.selectcustomer = new Customer('', '', '', '');

  }

}
