import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppComponent } from './app.component';
import { LiteNgLoadingModalModule, LiteNgMessageBoxModule, LiteNgToastModule, LiteNgHttpDataModule, LiteNgConfigurationModule } from "@desolatetimelines/lite-ng";
import { AppBasicServicesModule } from './basic-services/basic-services.module';

@NgModule({
  declarations: [
    AppComponent
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

  // Application modules
     AppBasicServicesModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
