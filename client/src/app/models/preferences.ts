import { Resource } from './resource';

export class Preferences {
    public greetingMessage: string;
    public exerciseReinforcementMessage: string;
    public sequenceReinforcementMessage: string;
    public animatedCharacterResourceId: number;
    public animatedCharacterResourcePath: string;
    public promptingStrategy: string;
    public promptingColor: boolean;
    public promptingSize: boolean;
    public promptingScratch: boolean;
    public promptingHide: boolean;
    public reinforcementStrategy: string;
    public reinforcementResourceId: number;
    public reinforcementResourcePath: string;
    public emotions: boolean;
}
