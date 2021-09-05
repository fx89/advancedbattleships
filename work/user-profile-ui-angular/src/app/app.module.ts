import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppComponent } from './app.component';
import { LiteNgLoadingModalModule, LiteNgMessageBoxModule, LiteNgToastModule, LiteNgHttpDataModule, LiteNgConfigurationModule, LiteNgIconModule, LiteNgTabsModule, LiteNgMenuModule, LiteNgDialogModule, LiteNgTextFieldModule, LiteNgPushbuttonModule } from "@desolatetimelines/lite-ng";
import { AppBasicServicesModule } from './basic-services/basic-services.module';
import { MainMenuPageComponent } from './pages/main-menu-page/main-menu-page.component';
import { BattleshipTemplateEditorComponent } from './pages/battleship-template-editor/battleship-template-editor.component';
import { UserProfileComponent } from './components/user-profile/user-profile.component';
import { AppUtilitiesModule } from './utilities/utilities.module';
import { LiteNgListModule } from '@desolatetimelines/lite-ng';
import { BsTemplateHullPanelComponent } from './components/bs-template-hull-panel/bs-template-hull-panel.component';
import { LiteNgAccordionModule } from '@desolatetimelines/lite-ng';
import { SocialPageComponent } from './pages/social-page/social-page.component';
import { UserNotificationsScrollerComponent } from './components/user-notifications-scroller/user-notifications-scroller.component';

@NgModule({
  declarations: [
    AppComponent,
    MainMenuPageComponent,
    BattleshipTemplateEditorComponent,
    UserProfileComponent,
    BsTemplateHullPanelComponent,
    SocialPageComponent,
    UserNotificationsScrollerComponent
  ],
  imports: [
  // Framework modules
     BrowserModule,

  // LiteNG modules
     LiteNgLoadingModalModule,
     LiteNgMessageBoxModule,
     LiteNgToastModule,
     LiteNgHttpDataModule,
     LiteNgConfigurationModule,
     LiteNgIconModule,
     LiteNgTabsModule,
     LiteNgDialogModule,
     LiteNgMenuModule,
     LiteNgTextFieldModule,
     LiteNgPushbuttonModule,
     LiteNgListModule,
     LiteNgAccordionModule,

  // Application modules
     AppBasicServicesModule,
     AppUtilitiesModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
