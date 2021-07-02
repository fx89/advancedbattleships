import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppComponent } from './app.component';
import { LiteNgLoadingModalModule, LiteNgMessageBoxModule, LiteNgToastModule, LiteNgHttpDataModule, LiteNgConfigurationModule, LiteNgIconModule, LiteNgTabsModule, LiteNgMenuModule, LiteNgDialogModule, LiteNgTextFieldModule, LiteNgPushbuttonModule } from "@desolatetimelines/lite-ng";
import { AppBasicServicesModule } from './basic-services/basic-services.module';
import { MainMenuPageComponent } from './pages/main-menu-page/main-menu-page.component';
import { BattleshipTemplateEditorComponent } from './pages/battleship-template-editor/battleship-template-editor.component';
import { UserProfileComponent } from './components/user-profile/user-profile.component';

@NgModule({
  declarations: [
    AppComponent,
    MainMenuPageComponent,
    BattleshipTemplateEditorComponent,
    UserProfileComponent
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

  // Application modules
     AppBasicServicesModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
