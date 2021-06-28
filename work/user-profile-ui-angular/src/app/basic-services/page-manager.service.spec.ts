import { TestBed } from '@angular/core/testing';

import { AdvBsPageManagerService } from './page-manager.service';

describe('AdvBsPageManagerService', () => {
  let service: AdvBsPageManagerService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AdvBsPageManagerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
