import { Exercise } from './exercise';
import { Child } from './child';

export class Sequence {
    public exerciseId: number;
    public sequenceId: number;
    public childId: number;
    public sequenceName: string;
    public exercisesToAdd: Array<number>;
    public exercisesOrder: Array<number>;
    public childrenToAssign: Array<number>;
}
