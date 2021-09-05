import { Component, OnInit, EventEmitter } from '@angular/core';
import { AdvBsUiDataService } from 'src/app/basic-services/data-service.service';
import { AdvBsPageManagerService } from 'src/app/basic-services/page-manager.service';
import { DialogButtonSpec } from '@desolatetimelines/lite-ng';

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

  friendSearchDialigShowEvent : EventEmitter<any> = new EventEmitter<any>();
  friendSearchDialigHideEvent : EventEmitter<any> = new EventEmitter<any>();
  friendSearchDialogExternalValidationMessageEvent : EventEmitter<any> = new EventEmitter<any>();
  soughtFriendNickName : string;

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

    this.dataService.socialRepository.addErrFilter((err) =>
      err.error == "AdvancedBattleshipsFriendNotFoundSocialException"
    );

    this.dataService.socialRepository.errorEventEmitter.subscribe(err => {
      if (err.error == "AdvancedBattleshipsFriendNotFoundSocialException") {
        this.friendSearchDialogExternalValidationMessageEvent.emit(err.message);
      }
    });
  }

  ngOnInit(): void {
    let action = this.navigation.getPageParam("action")

    if (action == "join-a-party") this.activeTabIndex = 1;
    if (action == "view-messages") this.activeTabIndex = 2;
  }

  friendSelectionChanged(friend : any) {

  }

  onFindFriendButtonClick() {
    this.friendSearchDialigShowEvent.emit();
  }

  searchFriendDialogCustomButtons : DialogButtonSpec[] = [
    {
      text : "Search",
      onClick : () => {
        this.dataService.socialRepository.saveCustomOperationWithLoadingModal(
            "inviteFriend",
            undefined,
            new Map([["nickName", this.soughtFriendNickName]]),
            () => {
              this.friendSearchDialigHideEvent.emit();
            }
          );
      }
    }
  ];
}
