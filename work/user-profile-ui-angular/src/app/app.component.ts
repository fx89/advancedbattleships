import { AfterViewInit, Component } from '@angular/core';
import { LiteNgLoadingModalService, LiteNgMsgboxService, LiteNgToastService } from '@desolatetimelines/lite-ng';
import { AdvBsUiDataService } from './basic-services/data-service.service';

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
    private dataService : AdvBsUiDataService
  ) {
      this.dataService.securityRepository?.errorEventEmitter.subscribe((err:any) => {
          // TODO: any state-resetting operations which might be required upon error
      });
  }

  ngAfterViewInit() {
    //this.loadingModal.show();
    this.msgBox.show("My title aaa", "My message aaa");
    //this.toast.showInfo("Err title", "Err desc");

    

    this.dataService.securityRepository?.getCustomOperationWithLoadingModal("getCurrentUser", undefined, (ret:any) => {
      console.log(ret);
    });
  }
}
