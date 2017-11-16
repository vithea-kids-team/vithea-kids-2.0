import { Pipe, PipeTransform } from '@angular/core';
import { Resource } from '../models/resource';

@Pipe({
    name: 'resourceTypeFilter',
    pure: false
})
export class ResourceTypeFilter implements PipeTransform {
    transform(items: Array<Resource>, stimulusanswers: boolean, reinforcement: boolean): Array<Resource> {

        if (!items || (stimulusanswers && reinforcement)) {
            return items;
        }

        if (!stimulusanswers && !reinforcement) {
            return [];
        }

        return items.filter(item => {
            return (!stimulusanswers && item.resourceArea.toLowerCase() !== 'stimuli') ||
            (!reinforcement && item.resourceArea.toLowerCase() !== 'reinforcement');
        });
    }
}
