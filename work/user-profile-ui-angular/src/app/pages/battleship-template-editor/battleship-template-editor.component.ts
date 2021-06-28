import { Component, OnInit } from '@angular/core';
import { AdvBsPageManagerService } from 'src/app/basic-services/page-manager.service';

@Component({
  selector: 'app-battleship-template-editor',
  templateUrl: './battleship-template-editor.component.html',
  styleUrls: ['./battleship-template-editor.component.less']
})
export class BattleshipTemplateEditorComponent implements OnInit {

  constructor(public navigation : AdvBsPageManagerService) { }

  ngOnInit(): void {
  }

}
