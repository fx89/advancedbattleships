import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdvBsUiDataService } from './data-service.service';
import { LiteNgConfigurationModule } from '@desolatetimelines/lite-ng';
import { AdvBsPageManagerService } from './page-manager.service';



@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    LiteNgConfigurationModule
  ],
  providers: [
    AdvBsUiDataService,
    AdvBsPageManagerService
  ]
})
export class AppBasicServicesModule { }
