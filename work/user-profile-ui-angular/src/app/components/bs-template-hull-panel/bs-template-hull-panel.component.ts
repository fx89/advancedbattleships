import { Component, EventEmitter, Input, OnInit, Output, OnChanges, ApplicationRef, ChangeDetectorRef } from '@angular/core';
import { AdvBsPathsService } from 'src/app/basic-services/paths-service.service';
import { v4 as uuid } from 'uuid';

@Component({
  selector: 'bs-template-hull-panel',
  templateUrl: './bs-template-hull-panel.component.html',
  styleUrls: ['./bs-template-hull-panel.component.less']
})
export class BsTemplateHullPanelComponent implements OnInit, OnChanges {

  @Input()
  id : string = "_" + uuid();

  @Input()
  hull! : boolean[][];

  @Input()
  hullWidth! : number;

  @Input()
  hullHeight! : number;

  @Input()
  subsystems : any[];

  subsystemsMatrix : any[][];

  @Output()
  hullChangeEvent : EventEmitter<any> = new EventEmitter<any>();

  @Input()
  additionalClass : string = " ";

  @Input()
  recomputeEvent : EventEmitter<any> = new EventEmitter<any>();

  widthPct! : number;
  heightPct! : number;
  marginTopPct! : number;
  marginLeftPct! : number;

  cellSizePct : number = 0;

  constructor(
    private paths : AdvBsPathsService
  ) { }

  ngOnInit(): void {
    this.recompute();

    this.recomputeEvent.subscribe(() => {
      this.recompute();
    });
  }

  ngOnChanges() {
    this.recompute();
  }

  private recompute() {
    this.computeRatios();
    this.computeCellSizePct();

    if (this.subsystems) {
      this.computeSubsystemsMatrix();
    }
  }

  private computeRatios() {
    // Compute thhe width / height ratios
    if (this.hullWidth > this.hullHeight) {
      this.widthPct = 100;
      this.heightPct = this.hullHeight / this.hullWidth * 100;
    } else {
      this.heightPct = 100;
      this.widthPct = this.hullWidth / this.hullHeight * 100;
    }

    // Compute the margins
    if (this.hullWidth > this.hullHeight) {
      this.marginTopPct = 50 - (this.heightPct / 2);
    } else {
      this.marginTopPct = 0;
    }
    if (this.hullWidth < this.hullHeight) {
      this.marginLeftPct = 50 - (this.widthPct / 2);
    } else {
      this.marginLeftPct = 0;
    }
  }

  private computeCellSizePct() {
    this.cellSizePct = 100 / this.hullWidth;
  }

  private computeSubsystemsMatrix() {
    this.subsystemsMatrix = [];
    for (let y = 0 ; y < this.hullHeight ; y++) {
      this.subsystemsMatrix[y] = [];
      for (let x = 0 ; x < this.hullWidth ; x++) {
        this.subsystemsMatrix[y][x] = this.searchSubsystemAt(x,y);
      }
    }
  }

  private searchSubsystemAt(x:number, y:number) : any {
    for(let subsystem of this.subsystems) {
      if (subsystem?.position?.x == x && subsystem?.position?.y == y) {
        console.log(subsystem);
        return subsystem;
      }
    }
    return undefined;
  }

  getClassName() : string {
    return "battleship-template-hull-panel " + this.additionalClass;
  }

  getContainerId() : string {
    return this.id + "_container";
  }

  getTableId() : string {
    return this.id + "_table"; 
  }

  counter(i: number) {
    return new Array(i);
  }

  onCellClicked(x:any, y:any) {
    if (this.hullChangeEvent) {
      this.hullChangeEvent.emit( { x:x, y:y } );
    }
  }

  getIconUrl(iconName : string) : string {
    return this.paths.getIconUrl(iconName);
  }

}
