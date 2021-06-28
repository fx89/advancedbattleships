import { Component, OnInit, EventEmitter } from '@angular/core';
import { LiteNgConfigurationService, LiteNgMsgboxService, LiteNgMenuButtonDefinition } from '@desolatetimelines/lite-ng';
import { AdvBsPageManagerService } from 'src/app/basic-services/page-manager.service';
import { AdvBsUiDataService } from 'src/app/basic-services/data-service.service';

@Component({
  selector: 'app-main-menu-page',
  templateUrl: './main-menu-page.component.html',
  styleUrls: ['./main-menu-page.component.less']
})
export class MainMenuPageComponent implements OnInit {

  userNickName : string = "";
  isFirstLogin : boolean = false;

  userMenuShowEvent : EventEmitter<any> = new EventEmitter<any>();

  constructor(
    public navigation : AdvBsPageManagerService,
    public config : LiteNgConfigurationService,
    public dataService : AdvBsUiDataService,
    public msgBox : LiteNgMsgboxService
  ) { }

  ngOnInit(): void {
    this.dataService.securityRepository?.getCustomOperationWithLoadingModal("getCurrentUser", undefined, (ret : any) => {
      this.userNickName = ret.nickName;
      this.isFirstLogin = ret.firstLogin;
    });
  }

  public getVersion() : string {
    return this.config.getAttributeValue("version");
  }

  public getUserIconPathName() : string {
    return this.config.getAttributeValue("backendUrl") + "/content/userLogo"
  }

  openUserOptions() {
    this.userMenuShowEvent.emit();
  }

  userMenuItems : LiteNgMenuButtonDefinition[] = [
    {  
      text: "Edit Preferences",
      iconPathName: <any>null,
      action: () => { this.msgBox.show("Not implemented", "This feature is not yet implemented."); }
    },
    {  
      text: "Merge Account",
      iconPathName: <any>null,
      action: () => { this.msgBox.show("Not implemented", "This feature is not yet implemented."); }
    },
    {  
      text: "Log Out",
      iconPathName: <any>null,
      action: () => { this.msgBox.show("Not implemented", "This feature is not yet implemented."); }
    }
  ];
}
