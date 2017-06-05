export class Exercise {
    public exerciseId: number
    public type: string
    public topic: string
    public level: string
    public question: string
    public stimulusText: string
    public stimulus: number
    public rightAnswer: string
    public rightAnswerImg: any
    public answers: Array<string>
    public answersImg: Array<string>
    public sequenceId : number
}
