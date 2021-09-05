import { Component, OnInit } from '@angular/core';
import { AdvBsUiDataService } from 'src/app/basic-services/data-service.service';
import { AdvBsPageManagerService } from 'src/app/basic-services/page-manager.service';
import { AdvBsPathsService } from 'src/app/basic-services/paths-service.service';
import { absGetCookie, absSetCokie } from 'src/app/utilities/cookie-utils';

@Component({
  selector: 'abs-user-notifications-scroller',
  templateUrl: './user-notifications-scroller.component.html',
  styleUrls: ['./user-notifications-scroller.component.less']
})
export class UserNotificationsScrollerComponent implements OnInit {

  private MAX_NOTIFICATIONS_ON_SCREEN : number = 3;

  notifications : any[] = [];

  private pollableNotificationTypes : string[] = [];

  constructor(
    private dataService : AdvBsUiDataService,
    private paths : AdvBsPathsService,
    private navigation : AdvBsPageManagerService
  ) { }

  ngOnInit(): void {
    this.initPollableMessageTypes()
    this.initPolling()
  }

  private initPollableMessageTypes() : void {
    let theCookie = absGetCookie("ABS_USER_PREFS_MESSAGING_SHOWABLE_TYPES")

    if (theCookie) {
      this.pollableNotificationTypes = theCookie.split(",")
    } else {
      this.pollableNotificationTypes = [
        "friend request",
        "party request",
        "game invitation"
      ]
      absSetCokie("ABS_USER_PREFS_MESSAGING_SHOWABLE_TYPES", this.pollableNotificationTypes, 999)
    }
  }

  private initPolling() : void {
    this.dataService.systemParametersRepository
      .getCustomOperationWithLoadingModal(
          "getMessagingProperties",
          undefined,
          (ret) => {
            this.MAX_NOTIFICATIONS_ON_SCREEN = ret.maxNotificationsOnScreen
            setInterval(() => {
                this.pollForNotifications()
            }, ret.pollingIntervalMS)
          }
        )
  }

  private pollForNotifications() : void {
    this.dataService.messagingRepository
      .getCustomOperation("getUserNotifications", undefined) // This has to happen without the loading modal
      .subscribe(ret => {
          if (ret && ret.length > 0) {
              this.showNotifications(ret)
          }
      })
  }

  private showNotifications(notifications) : void {
    // Replace the former array, to trick Angular to update the view
    let newNotificationsArray = []
    for (let n of this.notifications) {
      newNotificationsArray.push(n)
    }
    this.notifications = newNotificationsArray

    // Add any new notifications
    notifications.forEach(notification => {
      if (this.notificationIsPollable(notification)) {
        this.notifications.push(notification)
      }

      if (this.notifications.length >= this.MAX_NOTIFICATIONS_ON_SCREEN) {
        this.showTooManyNotificationsNotification()
        return
      }
    })
  }

  private notificationIsPollable(notification) : boolean {
    for (let n of this.pollableNotificationTypes) {
      if (notification.messageType.name == n) return true
    }
 
    return false;
  }

  private showTooManyNotificationsNotification() : void {
    this.notifications = [
      {
        title: "There are new messages waiting for you",
        messageType: {
          name: "multiple messages"
        }
      }
    ]
  }

  public popNotification(notification) : void {
    setTimeout( () => {
        let ret : any[] = []

        // This also ensures the notifications attribute is changed,
        // triggering the refresh of the page
        for (let n of this.notifications) {
          if (!(n == notification)) {
            ret.push(n)
          }
        }
    
        this.notifications = ret
    }, 500)
  }

  getNotificationIcon(notification) : string {
    return this.paths.getStylesheetResourceUrl(notification.messageType.name.replace(" ", "_"))
  }

  public activateNotification(notification) {
    this.popNotification(notification)

    if (notification.messageType.name == "friend request") {
      this.navigation.navigateToPage("social")
      this.navigation.putPageParam("action", "show-friends")
    }

    if (notification.messageType.name == "party request") {
      this.navigation.navigateToPage("social")
      this.navigation.putPageParam("action", "show-parties")
    }

    // TODO: add more notification types as needed
  }
}
