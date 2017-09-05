import { Resource } from './resource';

export class Preferences {
    public preferencesId: number;
    public greetingMessage: string;
    public exerciseReinforcementMessage: string;
    public sequenceReinforcementMessage: string;
    public animatedCharacterResourceId: number;
    public animatedCharacterResourcePath: string;
    public promptingStrategy: string;
    public promptingHide: boolean;
    public promptingRead: boolean;
    public promptingScratch: boolean;
    public promptingSize: boolean;
    public promptingColor: boolean;
    public sequenceExercisesCapitalization: string;
    public sequenceExercisesOrder: string;
    public reinforcementStrategy: string;
    public reinforcementResourceId: number;
    public reinforcementResourcePath: string;
    public emotions: boolean;
}
