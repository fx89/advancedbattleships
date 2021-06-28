import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AdvBsPageManagerService {
  private pagesHistory : string[] = [];

  public currentPage : string = <any>undefined;
  private mainPage : string = <any>undefined;

  constructor() { }

  public setMainPage(mainPage : string) {
    this.mainPage = mainPage;
    this.currentPage = mainPage;
  }

  public navigateToPage(pageName : string) {
    this.pagesHistory.push(pageName);
    this.currentPage = pageName;
  }

  public navigateBack() {
    if (this.pagesHistory.length > 0) {
      this.currentPage = <any>this.pagesHistory.pop();
    }
  }

  public navigateToMain() {
    this.navigateToPage(this.mainPage);
  }
}
