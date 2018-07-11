import { ExerciseLog } from './ExerciseLog';

export class SequenceLog {
    public sequenceId: number;
    public name: string;
    public date: string;
    public hourInit: string;
    public hourEnd: string;
    public correctExercices: number;
    public skippedExercises: number;
    public totalExercises: number;
    public percentage: string;
    public exercises: Array<ExerciseLog>;
}
