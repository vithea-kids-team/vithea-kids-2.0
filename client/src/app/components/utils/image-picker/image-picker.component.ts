import { Component, Input, Provider, forwardRef } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { Resource } from '../../../models/resource'

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

  private _items: Array<Resource> = [];

  get items(): Array<Resource> { return this._items }

  set items(i: Array<Resource>) {
    if (i !== this._items) {
      this._items = i;
      this.onChange(i);
    }
  }
  writeValue(i: Array<Resource>) {
    this._items = i;
    this.onChange(i);
  }

  onChange = (_) => { };
  onTouched = () => { };
  registerOnChange(fn: (_: any) => void): void { this.onChange = fn; }
  registerOnTouched(fn: () => void): void { this.onTouched = fn; }

}