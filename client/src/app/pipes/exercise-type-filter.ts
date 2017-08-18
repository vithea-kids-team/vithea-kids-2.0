import { Pipe, PipeTransform } from '@angular/core';
import { Exercise } from '../models/exercise';

@Pipe({
    name: 'exerciseTypeFilter',
    pure: false
})
export class ExerciseTypeFilter implements PipeTransform {
    transform(items: Array<Exercise>, image: boolean, text: boolean): Array<Exercise> {
        if (!items || (image && text)) {
            return items;
        }

        if (!image && !text) {
            return [];
        }

        return items.filter(item => {
            return (!text && item.type.toLowerCase() !== 'text') || (!image && item.type.toLowerCase() !== 'image');
        });
    }
}
