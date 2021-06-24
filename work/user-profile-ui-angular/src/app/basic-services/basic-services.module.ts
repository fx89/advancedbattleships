import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdvBsUiDataService } from './data-service.service';
import { LiteNgConfigurationModule } from '@desolatetimelines/lite-ng';



@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    LiteNgConfigurationModule
  ],
  providers: [
    AdvBsUiDataService
  ]
})
export class AppBasicServicesModule { }
