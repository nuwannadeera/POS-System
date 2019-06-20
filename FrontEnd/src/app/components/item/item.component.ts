import {Component, OnInit, ViewChild} from '@angular/core';
import {Item} from '../../dto/item';
import {NgForm} from '@angular/forms';
import {ItemService} from '../../service/item.service';

@Component({
  selector: 'app-item',
  templateUrl: './item.component.html',
  styleUrls: ['./item.component.css']
})

export class ItemComponent implements OnInit {
  itemList: Array<Item> = [];
  selectitem: Item = new Item('', '', 0, 0 );
  manually = false;
  item: Item = null;

  @ViewChild('frmItems') frmItems: NgForm;

  constructor(private itemService: ItemService) { }

  ngOnInit() {
    this.allItem();
  }

  private allItem(): void {
    this.itemService.getAllItem().subscribe(
      value =>
        this.itemList = value
    );
    console.log(this.itemList);
  }



  saveItem(): void {
    if (!this.frmItems.invalid) {
    this.itemService.saveItem(this.selectitem).subscribe(
      (result) => {
      if (result) {
        alert('Iem saved Successfully...');
        this.itemList.push(this.selectitem);
        this.clear();
        console.log(result);
      }
      }
    );
  } else {
  alert('Invalid input..');
  }
}



  updateItem (): void {
    console.log('update btn working...');
    if (!this.frmItems.invalid) {

      this.itemService.updateItem(this.selectitem).subscribe(
        (result) => {
          // if (!result) {
          alert('Updated Successfully...');
          this.manually = true;
          // this.customerList.push(this.selectcustomer);
          this.allItem();
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
    this.itemService.searchItem(this.selectitem.itemcode).subscribe(
      (result) => {
        console.log('search item working.......');
        this.selectitem = result;
        console.log(this.selectitem);
      }
    );
  }


  deleteCustomer (): void {
    if (confirm('Are you want to delete this item')) {
      console.log('delete btn working.....');
      this.itemService.deleteItem(this.selectitem.itemcode).subscribe(
        (result) => {
          alert('Item Successfully Deleted...');
          console.log(result);
          this.allItem();
          this.clear();
        }
      );
    }
  }


  private clear() {
    // this.itemList = new Array<Item>();
    this.selectitem = new Item('', '', 0, 0 );
  }

}
