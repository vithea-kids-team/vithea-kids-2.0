import { Pipe, PipeTransform } from '@angular/core';
import { Child } from '../models/child';

@Pipe({
    name: 'childFilter',
    pure: false
})
export class ChildFilter implements PipeTransform {
    transform(items: Array<Child>, filter: string): Array<Child> {
        if (!items || !filter) {
            return items;
        }
        // filter items array, items which match and return true will be kept, false will be filtered out
        filter = filter.toLowerCase();
        return items.filter(item => {
            return (item.firstName + ' ' +  item.lastName).toLowerCase().indexOf(filter) !== -1;
        });
    }
}