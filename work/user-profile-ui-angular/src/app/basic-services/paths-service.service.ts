import { Injectable } from '@angular/core';
import { LiteNgConfigurationService } from '@desolatetimelines/lite-ng';

@Injectable({
  providedIn: 'root'
})
export class AdvBsPathsService {

  constructor(
    private config : LiteNgConfigurationService
  ) { }

  public getIconUrl(iconName:string) : string {
    return this.config.getAttributeValue("backendUrl") + "/content/userIcon?fileName=" + encodeURIComponent(iconName) + ".png";
  }
}
