import { TestBed } from '@angular/core/testing';

import { AdvBsUiDataService } from './data-service.service';

describe('AdvBsUiDataService', () => {
  let service: AdvBsUiDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AdvBsUiDataService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
