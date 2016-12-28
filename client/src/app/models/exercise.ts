import { Resource } from './resource'

export class Exercise {
    public exerciseId: number
    public type: string
    public topic: string
    public level: string
    public question: string
    public stimulus: Array<Resource>
    public rightAnswer: string
    public answers: Array<string>
    public author: string
}