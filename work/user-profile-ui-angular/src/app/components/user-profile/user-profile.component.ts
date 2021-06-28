import { Component, Input, Output, OnInit, EventEmitter } from '@angular/core';

@Component({
  selector: 'user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.less']
})
export class UserProfileComponent implements OnInit {

  @Input()
  username : string = "";

  @Input()
  iconPathName : string = "";

  @Output()
  click : EventEmitter<any> = new EventEmitter<any>();

  constructor() { }

  ngOnInit(): void {
  }

  triggerClickEvent() {
    this.click.emit();
  }
}
