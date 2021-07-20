import { Component, OnInit, EventEmitter } from '@angular/core';
import { AdvBsUiDataService } from 'src/app/basic-services/data-service.service';
import { AdvBsPageManagerService } from 'src/app/basic-services/page-manager.service';
import { AdvBsPathsService } from 'src/app/basic-services/paths-service.service';
import { chrAllowedNumbersOnly, DialogButtonSpec, LiteNgConfigurationService, LiteNgMsgboxService } from '@desolatetimelines/lite-ng';
import { nvl } from 'src/app/utilities/lang-utils';
import { arrayDiff } from 'src/app/utilities/array-utils';
import { copyHullArray } from 'src/app/utilities/battleship-utils';

@Component({
  selector: 'app-battleship-template-editor',
  templateUrl: './battleship-template-editor.component.html',
  styleUrls: ['./battleship-template-editor.component.less']
})
export class BattleshipTemplateEditorComponent implements OnInit {
  nbrOnlyFunction : Function = chrAllowedNumbersOnly;

  newBattleshipTemplateDialogShowEvent : EventEmitter<any> = new EventEmitter<any>();
  newBattleshipTemplateDialogHideEvent : EventEmitter<any> = new EventEmitter<any>();
  battleshipTemplateName : string = "";
  battleshipTemplateWidth : string = "";
  battleshipTemplateHeight : string = "";
  validationError : string = "";
  selectedBattleshipTemplate : any = undefined;
  userBattleshipTemplates : any[] = [];

  editBattleshipTemplateDialogShowEvent : EventEmitter<any> = new EventEmitter<any>();
  editBattleshipTemplateDialogHideEvent : EventEmitter<any> = new EventEmitter<any>();

  hideNotAllowedFunction : Function = () => false;

  isValidationError : Function = (err:any) => (<string>(err?.error)) == "AdvancedBattleshipsInventoryValidationException";

  subsystemRefTypes! : any;

  TOOL_PENCIL : number = 1;
  TOOL_FILL : number = 2;
  selectedTool : number = 1; // TOOL_PENCIL;

  selectedBattleshipTemplateSubsystems : any[] = undefined;
  selectedSubsystemRef : any;

  SELECTION_TYPE_TOOL : number = 1;
  SELECTION_TYPE_SUBSYSTEM : number = 2;
  selectionType : number = 1;

  undoData : any[] = [];
  lastUndoState : any;
  redoState : any;

  hullPanelRecomputeEvent : EventEmitter<any> = new EventEmitter<any>();
  battleshipTempalteEditorValidationMessageEvent : EventEmitter<string> = new EventEmitter<string>();

  battleshipTemplateEditorCustomButtons : DialogButtonSpec[] = [
    new DialogButtonSpec("Undo", () => {
      if (this.undoData.length > 0) {

        this.redoState = {
          hull: this.selectedBattleshipTemplate.hull,
          subsystems : this.selectedBattleshipTemplateSubsystems
        }

        this.lastUndoState = this.undoData.pop();

        this.selectedBattleshipTemplate.hull = this.lastUndoState.hull;
        this.saveSelectedBattleshipTemplateHull();

        this.undoSelectedBattleshipSubsystems(this.lastUndoState.subsystems, this.selectedBattleshipTemplateSubsystems);
        this.selectedBattleshipTemplateSubsystems = this.lastUndoState.subsystems;

      } else {
        this.msgbox.showSimpleMsgBox("Cannot undo", "You haven't made any modifications since the last time the edit form was open.");
      }
    })
  ];

  private undoSelectedBattleshipSubsystems(lastStateSubsystemsArray, currenttStateSubsystemsArray) {
    // Check for removed subsystem
    let removedSubsystems : any[]
        = arrayDiff(
            lastStateSubsystemsArray,
            currenttStateSubsystemsArray,
            (e1, e2) => e1.uniqueToken == e2.uniqueToken
          );

    // Check for added subsystem
    let addedSubsystems : any[]
        = arrayDiff(
            currenttStateSubsystemsArray,
            lastStateSubsystemsArray,
            (e1, e2) => e1.uniqueToken == e2.uniqueToken
          );

    // Add previously removed subsystems
    if (removedSubsystems) {
      removedSubsystems.forEach(subsystem => {
        this.addBattleshipTemplateSubsystem(
          subsystem.subsystemRef,
          subsystem.position.x,
          subsystem.position.y,
          () => { }
        );
      })
    }

    // Remove previously added subsystems
    if (addedSubsystems) {
      addedSubsystems.forEach(subsystem => {
        this.removeBattleshipTemplateSubsystem(subsystem.uniqueToken);
      });
    }
  }

  constructor(
    public navigation : AdvBsPageManagerService,
    private dataService : AdvBsUiDataService,
    private msgbox : LiteNgMsgboxService,
    private paths : AdvBsPathsService
  ) {
    dataService.battleshipTemplatesRepository?.addErrFilter(this.isValidationError);
    dataService.battleshipTemplatesRepository?.errorEventEmitter.subscribe((err:any) => {
      if (this.isValidationError(err)) {
        this.validationError = err.message;
        this.battleshipTempalteEditorValidationMessageEvent.emit(this.validationError);
        this.selectedBattleshipTemplate.hull = this.undoData.pop().hull;
      }
    });

    dataService.subsystemsRepository.errorEventEmitter.subscribe(err => {
      if (err.error == "AdvancedBattleshipsInventoryValidationException" && this.lastUndoState) {
        this.undoData.push(this.lastUndoState);
        this.selectedBattleshipTemplateSubsystems = this.redoState.subsystems;
        this.validationError = err.message;
        this.battleshipTempalteEditorValidationMessageEvent.emit(this.validationError);
      }
    });

    this.loadUserBattleshipTemplates();
    this.loadBattleshipTemplateSubsystems();
  }

  ngOnInit(): void {
    
  }

  private loadUserBattleshipTemplates() {
    this.dataService.battleshipTemplatesRepository?.getCustomOperationWithLoadingModal(
      "getUserBattleshipTemplates",
      undefined,
      (ret:any) => {
        this.userBattleshipTemplates = ret;
        this.selectedBattleshipTemplate = undefined;
      }
    );
  }

  private loadBattleshipTemplateSubsystems() {
    this.dataService.subsystemsRepository?.getCustomOperationWithLoadingModal(
      "getSubsystemRefs", undefined,
      (ret:any) => {
        this.subsystemRefTypes = [];

        ret.forEach(subsystemRef => {
          // Get the subsystem type --- not using Map() because it's hard to handle in the view template
          let subSysType : any = null;
          this.subsystemRefTypes.forEach(element => {
            if (element.name == subsystemRef.type.name) {
              subSysType = element;
            }
          });
          if (subSysType == null) {
            subSysType = { name: subsystemRef.type.name, subsystems: [] };
            this.subsystemRefTypes.push(subSysType);
          }

          // Add the subsystem to the subsystems array of the subsystem type
          subSysType.subsystems.push(subsystemRef);
        });
      }
    );
  }

  private loadSelectedBattleshipSubsystems() {
    this.dataService.subsystemsRepository.getCustomOperationWithLoadingModal(
      "getBattleshipTemplateSubsystems",
      new Map([["battleshipTemplateUniqueToken", this.selectedBattleshipTemplate.uniqueToken]]),
      (ret) => { this.selectedBattleshipTemplateSubsystems = ret; }
    );
  }

  public backToMain() {
    this.navigation.navigateToMain();
  }

  onNewBattleshipTemplateButtonClick() {
    this.newBattleshipTemplateDialogShowEvent.emit();
  }

  onNewBattleshipTemplateDialogCreateButtonClick() {
    this.validationError = "";
    this.dataService.battleshipTemplatesRepository?.saveCustomOperationWithLoadingModal(
      "createNewBattleshipTemplate", undefined,
      new Map([
        ["templateName",     this.battleshipTemplateName        ],
        ["width"       , nvl(this.battleshipTemplateWidth , "0")],
        ["height"      , nvl(this.battleshipTemplateHeight, "0")]
      ]),
      (ret:any) => {
        this.selectedBattleshipTemplate = ret;
        this.userBattleshipTemplates.push(ret);
        this.newBattleshipTemplateDialogHideEvent.emit();
      }
    );
  }

  onNewBattleshipTemplateDialogCancelButtonClick() {
    this.newBattleshipTemplateDialogHideEvent.emit();
  }

  onEditBattleshipTemplateButtonClick() {
    if (this.selectedBattleshipTemplate) {
      this.showBattleshipTemplateEditingDialog();
    } else {
      this.msgbox.showSimpleMsgBox("Not selected", "You must select a battleship template before you can edit it");
    }
  }

  onEditBattleshipTemplateDialogExitButtonClick() {
    this.editBattleshipTemplateDialogHideEvent.emit();
    this.selectedBattleshipTemplateSubsystems = undefined;
  }

  onDeleteBattleshipTemplateButtonClick() {
    if (this.selectedBattleshipTemplate) {
      this.msgbox.show(
        "Deletion confirmaton",
        "Are you sure about deleting the batleship template [" + this.selectedBattleshipTemplate.name + "] ?",
        () => {
          this.dataService.battleshipTemplatesRepository?.deleteCustomOperationWithLoadingModal(
            "deleteBattleshipTemplate",
            this.selectedBattleshipTemplate.uniqueToken, // body
            undefined,
            () => {
              this.loadUserBattleshipTemplates();
            }
          );
        },
        () => {},
        undefined, undefined,
        "Yes", "No"
      );
    } else {
      this.msgbox.showSimpleMsgBox("Not selected", "You must select a battleship template from the list before pushing the delete button");
    }
  }

  showBattleshipTemplateEditingDialog() {
    this.undoData = [];
    this.loadSelectedBattleshipSubsystems();
    this.editBattleshipTemplateDialogShowEvent.emit();
  }

  onHullChange(hullChange:any) {
    let oldHull : any = copyHullArray(this.selectedBattleshipTemplate.hull);
    let oldSubsystems : any = Object.assign([], this.selectedBattleshipTemplateSubsystems);
    this.undoData.push({
      hull: oldHull,
      subsystems: oldSubsystems
    });

    if (this.selectionType == this.SELECTION_TYPE_TOOL) {
      this.executeTool(hullChange.x, hullChange.y);
    }

    if (this.selectionType == this.SELECTION_TYPE_SUBSYSTEM) {
      this.executeSubsystemPlacement(hullChange.x, hullChange.y);
    }
    
  }

  private executeTool(x:number, y:number) {
    if (this.selectedTool == this.TOOL_PENCIL) {
      this.executePencil(x, y);
    }

    if (this.selectedTool == this.TOOL_FILL) {
      this.executeFill(x, y);
    }

    this.saveSelectedBattleshipTemplateHull();
  }

  private executePencil(x:number, y:number) {
    this.selectedBattleshipTemplate.hull[y][x] = !this.selectedBattleshipTemplate.hull[y][x];
  }

  private executeFill(x:number, y:number) {
    this.selectedBattleshipTemplate.hull[y][x] = true;

    if (y-1 >= 0 && this.selectedBattleshipTemplate.hull[y-1][x] == false) {
      this.executeFill(x, y-1);
    }

    if (y+1 < this.selectedBattleshipTemplate.hullSize.y && this.selectedBattleshipTemplate.hull[y+1][x] == false) {
      this.executeFill(x, y+1);
    }

    if (x-1 >= 0 && this.selectedBattleshipTemplate.hull[y][x-1] == false) {
      this.executeFill(x-1, y);
    }

    if (x+1 < this.selectedBattleshipTemplate.hullSize.x && this.selectedBattleshipTemplate.hull[y][x+1] == false) {
      this.executeFill(x+1, y);
    }
  }

  private saveSelectedBattleshipTemplateHull() {
    this.dataService.battleshipTemplatesRepository?.saveCustomOperationWithLoadingModal(
      "setBattleshipTemplateHull",
      this.selectedBattleshipTemplate.hull,
      new Map([["battleshipTemplateUniqueToken", this.selectedBattleshipTemplate.uniqueToken]]),
      (ret) => {
        this.updateSelectedBattleshipTemplateStatistics(ret);
      }
    );
  }

  private executeSubsystemPlacement(placeAtX:number, placeAtY:number) {
    this.addBattleshipTemplateSubsystem(
      this.selectedSubsystemRef,
      placeAtX, placeAtY,
      (ret) => {
        this.selectedBattleshipTemplateSubsystems.push(ret);
        this.updateSelectedBattleshipTemplateStatistics(ret);
        this.hullPanelRecomputeEvent.emit();
      }
    );
  }

  private addBattleshipTemplateSubsystem(
    subsystemRef:any, placeAtX:number, placeAtY:number, resultHandler:Function
  ) {
    this.dataService.subsystemsRepository.saveCustomOperationWithLoadingModal(
      "addBattleshipTemplateSubsystem",
      undefined,
      new Map([
        ["battleshipTemplateUniqueToken", this.selectedBattleshipTemplate.uniqueToken],
        ["subsystemRefUniqueToken", subsystemRef.uniqueToken],
        ["posX", placeAtX],
        ["posY", [placeAtY]]
      ]),
      (ret) => {
        resultHandler(ret);
      }
    );
  }

  private removeBattleshipTemplateSubsystem(subsystemUniqueToken:string) {
    this.dataService.subsystemsRepository.deleteCustomOperationWithLoadingModal(
      "deleteBattleshipTemplateSubsystem",
      undefined,
      new Map([["subsystemUniqueToken", subsystemUniqueToken]]),
      (ret) => {
        this.updateSelectedBattleshipTemplateStatistics(ret);
      }
    );
  }

  private updateSelectedBattleshipTemplateStatistics(src) {
    const actualSrc : any = src.battleshipTemplate ? src.battleshipTemplate : src;

    this.selectedBattleshipTemplate.cost      = actualSrc.cost;
    this.selectedBattleshipTemplate.energy    = actualSrc.energy;
    this.selectedBattleshipTemplate.firepower = actualSrc.firepower;
  }

  onHullConstructionToolsPencilButtonClick() {
    this.selectionType = this.SELECTION_TYPE_TOOL;
    this.selectedTool = this.TOOL_PENCIL;
  }

  onHullConstructionToolsFillButtonClick() {
    this.selectionType = this.SELECTION_TYPE_TOOL;
    this.selectedTool = this.TOOL_FILL;
  }

  onSubsystemRefCardClicked(subsystemRef) {
    this.selectionType = this.SELECTION_TYPE_SUBSYSTEM;
    this.selectedSubsystemRef = subsystemRef;
  }

  getIconUrl(iconName:string) : string {
    return this.paths.getIconUrl(iconName);
  }
}
