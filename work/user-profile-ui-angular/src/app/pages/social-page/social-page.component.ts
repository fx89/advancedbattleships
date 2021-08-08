import { Component, OnInit } from '@angular/core';
import { AdvBsUiDataService } from 'src/app/basic-services/data-service.service';
import { AdvBsPageManagerService } from 'src/app/basic-services/page-manager.service';

@Component({
  selector: 'app-social-page',
  templateUrl: './social-page.component.html',
  styleUrls: ['./social-page.component.less']
})
export class SocialPageComponent implements OnInit {

  activeTabIndex : Number = 0;

  firends : any[];
  selectedFriend : any;

  friendIconFunction : Function = (friend) => "da";
  friendTitleFunction : Function = (friend) => "mna";
  friendDscriptionFunction : Function = (friend) => "ola";


  constructor(
    public navigation : AdvBsPageManagerService,
    private dataService : AdvBsUiDataService
  ) {
    this.dataService.socialRepository.getCustomOperationWithLoadingModal(
      "getCurrentUserFriends",
      undefined,
      (ret) => {
        this.firends = ret;
      }
    );
  }

  ngOnInit(): void {
    if (this.navigation.getPageParam("action") == "join-a-party") {
        this.activeTabIndex = 1;
     }
  }

  friendSelectionChanged(friend : any) {

  }
}
