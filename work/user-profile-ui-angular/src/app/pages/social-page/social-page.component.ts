import { Component, OnInit, EventEmitter } from '@angular/core';
import { AdvBsUiDataService } from 'src/app/basic-services/data-service.service';
import { AdvBsPageManagerService } from 'src/app/basic-services/page-manager.service';
import { DialogButtonSpec, LiteNgMsgboxService } from '@desolatetimelines/lite-ng';
import { AdvBsPathsService } from 'src/app/basic-services/paths-service.service';
import { scrollToBottom } from 'src/app/utilities/dom-utils';

@Component({
  selector: 'app-social-page',
  templateUrl: './social-page.component.html',
  styleUrls: ['./social-page.component.less']
})
export class SocialPageComponent implements OnInit {
  CHAT_REFRESH_INTERVAL : number = 1000;

  currentUser : any;

  activeTabIndex : Number = 0;

  unattendedFriendRequests : any[];

  firends : any[];
  selectedFriend : any;

  friendIconFunction : Function = (friend) => this.paths.getLogoUrl(friend.logoName);
  friendIconOverlayFunction : Function = (friend) => this.paths.getStylesheetResourceUrl("icon-overlay-" + friend.presence);
  friendTitleFunction : Function = (friend) => friend.nickName;
  friendDscriptionFunction : Function = (friend) => friend.presence;

  friendSearchDialigShowEvent : EventEmitter<any> = new EventEmitter<any>();
  friendSearchDialigHideEvent : EventEmitter<any> = new EventEmitter<any>();
  friendSearchDialogExternalValidationMessageEvent : EventEmitter<any> = new EventEmitter<any>();
  soughtFriendNickName : string;

  refreshFriendStatusesTask : any;

  selectedChatMessagesWindow : any[] = [];
  selectedChatLastMessageId : number = 0;
  selectedChatPartyChannelUniqueToken : string = null;
  newChatMessage : string = "";

  constructor(
    public navigation : AdvBsPageManagerService,
    private dataService : AdvBsUiDataService,
    private paths : AdvBsPathsService,
    private msgBox : LiteNgMsgboxService
  ) {
    this.refreshFriendsList();
    this.startRefreshFriendStatusesTask();

    this.dataService.socialRepository.addErrFilter((err) =>
      err.error == "AdvancedBattleshipsFriendNotFoundSocialException"
    );

    this.dataService.socialRepository.errorEventEmitter.subscribe(err => {
      if (err.error == "AdvancedBattleshipsFriendNotFoundSocialException") {
        this.friendSearchDialogExternalValidationMessageEvent.emit(err.message);
      }
    });

    // TODO: create a common service to cache this data, which is also used by the main-menu-page.component
    this.dataService.securityRepository?.getCustomOperationWithLoadingModal("getCurrentUser", undefined, (ret : any) => {
      this.currentUser = ret
    })
  }

  ngOnInit(): void {
    let action = this.navigation.getPageParam("action")

    if (action == "show-friends") this.activeTabIndex = 0
    if (action == "show-parties") this.activeTabIndex = 1
    if (action == "join-a-party") this.activeTabIndex = 1
    if (action == "view-messages") this.activeTabIndex = 2

    this.refreshActiveTab()
    this.continouslyRefreshCurrentChat()
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

    this.clearSelectedFriend()
  }

  clearSelectedFriend() {
    this.selectedChatPartyChannelUniqueToken = null;
    this.selectedFriend = null;
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

  private startRefreshFriendStatusesTask() {
    this.refreshFriendStatusesTask
      = setInterval(() => this.refreshFriendStatuses(), 5000) // TODO: 5000 should be configurable (in the back-end)
  }

  private stopRefreshFriendStatusesTask() {
    if (this.refreshFriendStatusesTask) {
      clearTimeout(this.refreshFriendStatusesTask)
    }
  }

  refreshFriendStatuses() {
    this.dataService.socialRepository.getCustomOperation(
      "getUserFriendStatuses",
      undefined
    ).subscribe(ret => {
      this.firends.forEach(friend => {
        friend.presence = ret[friend.friendUserUniqueToken]
      })
    })
  }

  refreshFriendsList() {
    this.dataService.socialRepository.getCustomOperationWithLoadingModal(
      "getCurrentUserFriends",
      undefined,
      (ret) => {
        this.firends = ret;
      }
    );
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
      this.selectedFriend = friend
      this.selectedChatPartyChannelUniqueToken = this.selectedFriend.privatePartyUniqueToken
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

  // TODO: make generic chatting components
  continouslyRefreshCurrentChat() {
      setInterval(() => {
          // This should only run if there is a party channel selected
          if (this.selectedChatPartyChannelUniqueToken){
              // Get any new messages
              this.dataService.chatRepository.getCustomOperation(
                "getNewPartyChannelMessages",
                new Map([
                  ["partyUniqueToken", this.selectedChatPartyChannelUniqueToken],
                  ["lastRegisteredMessageId", this.selectedChatLastMessageId.toString()]
                ])
            ).subscribe(
                // And add them to the friend's messaging queue
                (ret) => {
                    if (ret?.length > 0) {
                        this.selectedChatMessagesWindow.push(...ret)
                        this.selectedChatLastMessageId = ret[ret.length - 1].id

                        setTimeout(
                          () => scrollToBottom(document.getElementsByClassName("friend-chat-container")[0]),
                          200
                        )
                        
                    }
                }
            )
          }
      }, this.CHAT_REFRESH_INTERVAL)
  }

  onNewChatMessageFieldKeyUp(event) {
    if (event.key == "Enter" && this.selectedChatPartyChannelUniqueToken) {
      this.sendNewChatMessageToCurrentChannel()
    }
  }

  sendNewChatMessageToCurrentChannel() {
      if(this.newChatMessage && this.newChatMessage != "") {
        if (this.selectedChatPartyChannelUniqueToken)
        this.dataService.chatRepository.saveCustomOperation(
            "addChatMessageToPartyChannel",
            null,
            new Map([
                ["partyUniqueToken", this.selectedChatPartyChannelUniqueToken],
                ["message", this.newChatMessage]
            ])
        ).subscribe(() => {
            this.newChatMessage = ""
            // TODO: make a new type of text field that has an integrated loading wheel
        })
      }
  }

  userTokenToNickName(userToken) {
    if (this.isCurrentUser(userToken)) {
      return this.currentUser?.nickName
    }

    for (let friend of this.firends) {
      if (friend.friendUserUniqueToken == userToken) {
          return friend.nickName
      }
    }

    return "UNKNOWN"
  }

  isCurrentUser(userToken) {
    return this.currentUser?.uniqueToken == userToken
  }
}
