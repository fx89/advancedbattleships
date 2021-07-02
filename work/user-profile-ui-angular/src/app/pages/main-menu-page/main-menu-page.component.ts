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

  preferencesChanged : boolean = false;

  userMenuShowEvent : EventEmitter<any> = new EventEmitter<any>();
  userMenuHideEvent : EventEmitter<any> = new EventEmitter<any>();

  userPreferencesDialogShowEvent : EventEmitter<any> = new EventEmitter<any>();

  availableLogosDialogShowEvent : EventEmitter<any> = new EventEmitter<any>();
  availableLogosDialogHideEvent : EventEmitter<any> = new EventEmitter<any>();
  availableLogos : string[] = [];
  currentLogoSrc : string = "";

  availableWallpapersDialogShowEvent : EventEmitter<any> = new EventEmitter<any>();
  availableWallpapersDialogHideEvent : EventEmitter<any> = new EventEmitter<any>();
  availableWallpapers : string[] = [];
  currentWallpaperSrc : string = "";

  availableStylesheetsDialogShowEvent : EventEmitter<any> = new EventEmitter<any>();
  availableStylesheetsDialogHideEvent : EventEmitter<any> = new EventEmitter<any>();
  availableStylesheets : string[] = [];
  currentStylesheetThumbSrc : string = "";

  nicknameDialogShowEvent : EventEmitter<any> = new EventEmitter<any>();
  nicknameDialogHideEvent : EventEmitter<any> = new EventEmitter<any>();
  tempUserNickName : string = "";
  nickStatus : number = 0;

  constructor(
    public navigation : AdvBsPageManagerService,
    public config : LiteNgConfigurationService,
    public dataService : AdvBsUiDataService,
    public msgBox : LiteNgMsgboxService
  ) {
    this.dataService.securityRepository?.addErrFilter((err : any) => {
      return err?.path == "/security/setCurrentUserNickName";
    });
    this.dataService.securityRepository?.errorEventEmitter.subscribe((err) => {
      if (err?.path == "/security/setCurrentUserNickName") {
        this.nickStatus = 2;
      }
    });
  }

  ngOnInit(): void {
    this.dataService.securityRepository?.getCustomOperationWithLoadingModal("getCurrentUser", undefined, (ret : any) => {
      this.userNickName = ret.nickName;
      this.isFirstLogin = ret.firstLogin;
    });

    this.dataService.contentRepository?.getCustomOperationWithLoadingModal("getUserConfig", undefined, (ret : any) => {
      this.currentLogoSrc = this.getLogoFilePathName(ret.currentLogoName);
      this.currentWallpaperSrc = this.getWallpaperFilePathName(ret.currentWallpaperName);
      this.currentStylesheetThumbSrc = this.getStylesheetThumbPathName(ret.currentStylesheetName);
    });
  }

  public getVersion() : string {
    return this.config.getAttributeValue("version");
  }

  public getCurrentLogoPathName() : string {
    return this.config.getAttributeValue("backendUrl") + "/content/userLogo"
  }

  public getLogoFilePathName(logoName : string) {
    return this.config.getAttributeValue("backendUrl") + "/content/logo?logoName=" + logoName;
  }

  public getWallpaperImagUrl() : string {
    return this.config.getAttributeValue("backendUrl") + "/content/userWallpaper?mimeType=png";
  }

  public getWallpaperFilePathName(wallpaperName : string) {
    return this.config.getAttributeValue("backendUrl") + "/content/wallpaperImage?wallpaperName=" + wallpaperName;
  }

  public getStylesheetThumbPathName(stylesheetName : string) {
    return this.config.getAttributeValue("backendUrl") + "/content/stylesheetThumb?stylesheetName=" + stylesheetName;
  }

  openUserOptions() {
    this.userMenuShowEvent.emit();
  }

  userMenuItems : LiteNgMenuButtonDefinition[] = [
    {  
      text: "Edit Preferences",
      iconPathName: <any>null,
      action: () => { this.userMenuHideEvent.emit(); this.userPreferencesDialogShowEvent.emit(); }
    },
    {  
      text: "Merge Accounts",
      iconPathName: <any>null,
      action: () => { this.msgBox.show("Not implemented", "This feature is not yet implemented."); }
    },
    {  
      text: "Log Out",
      iconPathName: <any>null,
      action: () => { this.msgBox.show("Not implemented", "This feature is not yet implemented."); }
    }
  ];

  clickLogoCard() {
    this.dataService.contentRepository?.getCustomOperationWithLoadingModal("getAvailableLogos", undefined, (ret:any) => {
      this.availableLogos = ret;
      this.availableLogosDialogShowEvent.emit();
    });
  }

  clickNicknameCard() {
    this.tempUserNickName = this.userNickName;
    this.nicknameDialogShowEvent.emit();
  }

  clickIconThemeCard() {
    this.dataService.contentRepository?.getCustomOperationWithLoadingModal("getAvailableStylesheets", undefined, (ret:any) => {
      this.availableStylesheets = ret;
      this.availableStylesheetsDialogShowEvent.emit();
    });
  }

  clickWallpaperCard() {
    this.dataService.contentRepository?.getCustomOperationWithLoadingModal("getAvailableWallpapers", undefined, (ret:any) => {
      this.availableWallpapers = ret;
      this.availableWallpapersDialogShowEvent.emit();
    })
  }

  setUserLogo(logoName : string) {
    this.dataService.contentRepository?.saveCustomOperationWithLoadingModal(
      "updateUserLogo", undefined, new Map([["logoName", logoName]]),
      () => {
        this.currentLogoSrc = this.getLogoFilePathName(logoName);
        this.availableLogosDialogHideEvent.emit();
        this.preferencesChanged = true;
      }
    )
  }

  setUserWallpaper(wallpaperName : string) {
    this.dataService.contentRepository?.saveCustomOperationWithLoadingModal(
      "updateUserWallpaper", undefined, new Map([["wallpaperName", wallpaperName]]),
      () => {
        this.currentWallpaperSrc = this.getWallpaperFilePathName(wallpaperName);
        this.availableWallpapersDialogHideEvent.emit();
        this.preferencesChanged = true;
      }
    )
  }

  setUserStylesheet(stylesheetName : string) {
    this.dataService.contentRepository?.saveCustomOperationWithLoadingModal(
      "updateUserStylesheet", undefined, new Map([["stylesheetName", stylesheetName]]),
      () => {
        this.currentStylesheetThumbSrc = this.getStylesheetThumbPathName(stylesheetName);
        this.availableStylesheetsDialogHideEvent.emit();
        this.preferencesChanged = true;
      }
    )
  }

  setUserNickname() {
    if (this.tempUserNickName == this.userNickName) {
      this.nicknameDialogHideEvent.emit();
    } else {
      this.dataService.securityRepository?.saveCustomOperationWithLoadingModal("setCurrentUserNickName", undefined, new Map([["userNickName", this.tempUserNickName]]), (ret:any) => {
        this.nickStatus = 0;
        this.userNickName = this.tempUserNickName;
        setTimeout(() => { this.nicknameDialogHideEvent.emit(); }, 1000 );
      });
    }
  }

  onUserPreferencesDialogClosed() {
    if (this.preferencesChanged) {
      this.preferencesChanged = false;
      this.msgBox.showSimpleMsgBox("Applying changes", "The page will be reloaded to apply configuration changes", () => {
        document.location.reload();
      });
    }
  }

  onNickValueChanged() {
    this.nickStatus = 1;
  }

}
