import { Exercise } from './exercise';

export class ExerciseLog {
    public exerciseId: number;
    public type: string;
    public hourInit: string;
    public hourEnd: string;
    public numberAttempts: number;
    public attemps: Array<string>;
    public skipped: boolean;
    public reinforcementStrategy: string;
    public promptingStrategy: string;
    public promptingType: Array<string>;
    public exercise: Exercise;
}
