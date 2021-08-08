import { AfterViewInit, Component } from '@angular/core';
import { LiteNgConfigurationService, LiteNgLoadingModalService, LiteNgMsgboxService, LiteNgToastService } from '@desolatetimelines/lite-ng';
import { AdvBsUiDataService } from './basic-services/data-service.service';
import { AdvBsPageManagerService } from './basic-services/page-manager.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.less']
})
export class AppComponent implements AfterViewInit {
  title = 'user-profile-ui-angular';

  constructor(
    private loadingModal : LiteNgLoadingModalService,
    private msgBox : LiteNgMsgboxService,
    private toast : LiteNgToastService,
    private dataService : AdvBsUiDataService,
    private config : LiteNgConfigurationService,
    public navigation : AdvBsPageManagerService
  ) {
      this.navigation.setMainPage("main");

      this.dataService.securityRepository?.errorEventEmitter.subscribe((err:any) => {
          // TODO: any state-resetting operations which might be required upon error
      });

      this.keepAlivePing();
  }

  private keepAlivePing() : void {
    this.dataService.systemParametersRepository.getCustomOperationWithLoadingModal(
      "getSessionPingSecs",
      undefined,
      ret => {
        const pingMs : number = ret * 1000;
        setInterval(
          () => this.dataService.securityRepository.getCustomOperation("ping"),
          pingMs
        )
      }
    );
  }

  ngAfterViewInit() {
    //this.loadingModal.show();
    //this.msgBox.show("MY TITLE AAA", "My message aaa");
    //this.toast.showInfo("Err title", "Err desc");
    (<any>document).getElementById('video-wallpaper').play();
  }

  getBackendUrl() : string {
    return this.config.getAttributeValue("backendUrl");
  }
}
