import {Component, OnInit, ViewChild} from '@angular/core';
import {Item} from '../../dto/item';
import {NgForm} from '@angular/forms';
import {ItemService} from '../../service/item.service';
import {log} from 'util';

@Component({
  selector: 'app-item',
  templateUrl: './item.component.html',
  styleUrls: ['./item.component.css']
})

export class ItemComponent implements OnInit {
  itemList: Array<Item> = [];
  selectitem: Item = new Item('', '', Number(''), Number(''));
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
    this.itemService.saveItem(this.selectitem).subscribe(
      (result) => {
        alert('Iem saved Successfully...');
        this.allItem();
        this.clear();
      }
    );
  }

  private clear() {
    this.itemList = new Array<Item>();
    this.selectitem = new Item('', '', Number(''), Number(''));

  }
}
