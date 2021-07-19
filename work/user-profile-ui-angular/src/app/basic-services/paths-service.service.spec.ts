import { TestBed } from '@angular/core/testing';

import { AdvBsPathsService } from './paths-service.service';

describe('AdvBsPathsService', () => {
  let service: AdvBsPathsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AdvBsPathsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
