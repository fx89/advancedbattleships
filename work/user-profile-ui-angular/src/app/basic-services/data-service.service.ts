import { Injectable } from '@angular/core';
import { LiteNgConfigurationService, LiteNgHttpRepositoryFactoryService, LiteNgLoadingModalWrappedHttpRepository } from '@desolatetimelines/lite-ng';
import { HTTP_CLIENT_REDIRECT_DECISION_SYNTAX_ERROR } from '@desolatetimelines/lite-ng';

@Injectable({
  providedIn: 'root'
})
export class AdvBsUiDataService {
  public securityRepository : LiteNgLoadingModalWrappedHttpRepository | undefined;

  private backendUrl : string = "";
  private loginFormUrl : string = "";

  constructor(
      private config : LiteNgConfigurationService,
      private repoFactory : LiteNgHttpRepositoryFactoryService
  ) {
      this.backendUrl = config.getAttributeValue("backendUrl");
      this.loginFormUrl = config.getAttributeValue("loginFormUrl");

      this.securityRepository = this.createRepository("security", "AUTHENTICATING", "please wait while your session is established");
  }

  private createRepository(baseUrl : string, loadingTitle : string, loadingMessage : string) {
      return this.repoFactory.newLoadingModalWrappedHttpRepository(
          this.backendUrl + "/" + baseUrl,
          loadingTitle,
          loadingMessage,
          HTTP_CLIENT_REDIRECT_DECISION_SYNTAX_ERROR,
          this.loginFormUrl
      );
  }


}
