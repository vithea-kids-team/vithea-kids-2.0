export class Exercise {
    public id: number
    public type: string
    public topic: string
    public level: string
    public question: string
    public stimulusText: string
    public stimulus: Array<string>
    public rightAnswer: string
    public rightAnswerImg: any
    public answers: Array<string>
    public answersImg: Array<string>
    public sequenceId : number
}
