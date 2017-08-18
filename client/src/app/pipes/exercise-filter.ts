import { Pipe, PipeTransform } from '@angular/core';
import { Exercise } from '../models/exercise';

@Pipe({
    name: 'exerciseFilter',
    pure: false
})
export class ExerciseFilter implements PipeTransform {
    transform(items: Array<Exercise>, filter: string): Array<Exercise> {
        if (!items || !filter) {
            return items;
        }
        // filter items array, items which match and return true will be kept, false will be filtered out
        filter = filter.toLowerCase();
        return items.filter(item => {
            return item.exerciseName.toLowerCase().indexOf(filter) !== -1;
        });
    }
}
