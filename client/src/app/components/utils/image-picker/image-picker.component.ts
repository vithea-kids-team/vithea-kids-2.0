import { Component, EventEmitter, Input, Output, Provider, forwardRef} from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { Resource } from '../../../models/resource';
import { ResourcesService } from '../../../services/resources/resources.service';

export const CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR: any = {
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => ImagePickerComponent),
    multi: true
};

@Component({
  selector: 'image-picker',
  templateUrl: './image-picker.component.html',
  styleUrls: ['./image-picker.component.css'],
  providers: [CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR]
})
export class ImagePickerComponent implements ControlValueAccessor {

  @Input() multiSelect: boolean;
  @Input() selected: Resource;
  @Output() onSelected = new EventEmitter<String>();

  public _items: Array<Resource> = [];

  constructor(public resourcesService: ResourcesService) {}

  get items(): Array<Resource> {
    if (!this._items) {
      this._items = [];
    }
    if (this.selected != null) {
      this.onSelected.emit('getItems');
      this._items.forEach((x) => {
        if (x.resourceId === this.selected.resourceId) {
          x.selected = true;
        }
      });
    }

    return this._items;
  }

  set items(i: Array<Resource>) {
    if (i !== this._items) {
      this._items = i;
      this.onSelected.emit('setItems');
      this.onChange(i);
    }
  }

  writeValue(i: Array<Resource>) {
    this._items = i;
    this.onSelected.emit('writeValue');
    this.onChange(i);
  }

  toggleItem(item) {
    item.selected = !item.selected;
    this.selected = null;

    this.onSelected.emit('toggleItems');

    if (!this.multiSelect) {
      this._items.forEach((x) => {
        if (x !== item) {
          x.selected = false;
        }
      });
    }
  }

  onChange = (_) => { };
  onTouched = () => { };
  registerOnChange(fn: (_: any) => void): void { this.onChange = fn; }
  registerOnTouched(fn: () => void): void { this.onTouched = fn; }
}
