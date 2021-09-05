import { Component, OnInit, EventEmitter } from '@angular/core';
import { AdvBsUiDataService } from 'src/app/basic-services/data-service.service';
import { AdvBsPageManagerService } from 'src/app/basic-services/page-manager.service';
import { DialogButtonSpec, LiteNgMsgboxService } from '@desolatetimelines/lite-ng';
import { AdvBsPathsService } from 'src/app/basic-services/paths-service.service';

@Component({
  selector: 'app-social-page',
  templateUrl: './social-page.component.html',
  styleUrls: ['./social-page.component.less']
})
export class SocialPageComponent implements OnInit {

  activeTabIndex : Number = 0;

  unattendedFriendRequests : any[];

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
    private dataService : AdvBsUiDataService,
    private paths : AdvBsPathsService,
    private msgBox : LiteNgMsgboxService
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

    if (action == "show-friends") this.activeTabIndex = 0
    if (action == "show-parties") this.activeTabIndex = 1
    if (action == "join-a-party") this.activeTabIndex = 1
    if (action == "view-messages") this.activeTabIndex = 2

    this.refreshActiveTab()
  }

  onActiveTabIndexChanged(activeTabIndex) {
    this.activeTabIndex = activeTabIndex
    this.refreshActiveTab()
  }

  refreshActiveTab() {
    if (this.activeTabIndex == 0) {
      this.refreshFriendsTab()
    }

    if (this.activeTabIndex == 1) {
      this.refreshPartiesTab()
    }

    if (this.activeTabIndex == 2) {
      this.refreshMessagesTab()
    }
  }

  refreshFriendsTab() {
    this.refreshFriendsList()
    this.refreshFriendRequests()
  }

  refreshPartiesTab() {
    // TODO: implement
  }

  refreshMessagesTab() {
    // TODO: implement
  }

  refreshFriendsList() {
    // TODO: implement
  }

  refreshFriendRequests() {
    this.dataService.socialRepository.getCustomOperationWithLoadingModal(
      "getUnattendedUserFriendInvitations", undefined,
      (ret) => {
        this.unattendedFriendRequests = ret
      }
    )
  }

  acceptFriendRequest(request) {
    this.dataService.socialRepository.saveCustomOperationWithLoadingModal(
      "acceptFriendRequest", undefined,
      new Map([["friendUserUniqueToken", request.friendUserUniqueToken]]),
      () => {
        this.refreshFriendRequests()
        this.refreshFriendsList()
      }
    )
  }

  rejectFriendRequest(request) {
    this.msgBox.show(
      "Reject friend request",
      "Are you sure you want to reject the friend request from " + request.nickName + "?",
      () => { this.reallyRejectFriendRequest(request) },
      () => { },
      undefined, undefined,
      "Yes", "No"
    )
  }

  reallyRejectFriendRequest(request) {
    this.dataService.socialRepository.saveCustomOperationWithLoadingModal(
      "rejectFriendRequest", undefined,
      new Map([["friendUserUniqueToken", request.friendUserUniqueToken]]),
      () => {
        this.refreshFriendRequests()
        this.refreshFriendsList()
      }
    )
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

  getFriendIconSrc(request) : string {
    return this.paths.getLogoUrl(request.logoName)
  }
}
