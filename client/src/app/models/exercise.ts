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
    public distractor1: string
    public distractor2: string
    public distractor3: string
    public answers: Array<string>
    public answersImg: Array<string>
    public sequenceId: number
}
