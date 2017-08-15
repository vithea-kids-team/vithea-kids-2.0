import { Pipe, PipeTransform } from '@angular/core';
import { Sequence } from '../models/sequence';

@Pipe({
    name: 'sequenceFilter',
    pure: false
})
export class SequenceFilter implements PipeTransform {
    transform(items: Array<Sequence>, filter: string): Array<Sequence> {
        if (!items || !filter) {
            return items;
        }
        // filter items array, items which match and return true will be kept, false will be filtered out
        filter = filter.toLowerCase();
        return items.filter(item => {
            return (item.sequenceName).toLowerCase().indexOf(filter) !== -1;
        });
    }
}
