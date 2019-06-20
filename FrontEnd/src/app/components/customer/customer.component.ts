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
        // if (!result) {
          alert('Updated Successfully...');
          this.manually = true;
          // this.customerList.push(this.selectcustomer);
          this.allCustomer();
          this.clear();
        // } else {
        //   alert('not update successfully....');
        // }
      }
    );
  } else {
      alert('Invalid Data input..!');
    }
  }



  searchCustomer (): void {
    this.customerService.searchCustomer(this.selectcustomer.cid).subscribe(
      (result) => {
        console.log('search customer working.......');
        this.selectcustomer = result;
        console.log(this.selectcustomer);
      }
    );
  }


  deleteCustomer (): void {
    if (confirm('Are you want to delete this customer')) {
      console.log('delete btn working.....');
      this.customerService.deleteCusomer(this.selectcustomer.cid).subscribe(
        (result) => {
          alert('Customer Successfully Deleted...');
          console.log(result);
          this.allCustomer();
          this.clear();
        }
      );
    }
  }


  private clear() {
    this.selectcustomer = new Customer('', '', '', '');
  }

}
