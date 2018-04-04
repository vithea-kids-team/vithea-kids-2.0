import { SelectionArea } from './selectionArea';
export class Exercise {
    public exerciseId: number
    public sequenceId: number
    public exerciseName: string // for filter purposes, it is equal to exercise question description
    public type: string
    public topic: string
    public level: string
    public question: string

    // multiple choice
    public stimulus: string
    public answers:  Array<string>
    public rightAnswers: Array<string>
    public distractors: Array<string>

    // to remove
    public distractor1: string
    public distractor2: string
    public distractor3: string
    public stimulusText: string;
    public rightAnswerIds: Array<number>;
    public distractorIds: Array<number>;
    public stimulusImageId: number;
    public rightAnswer: string
    public rightAnswerImg: any
    public answersImg1: string
    public answersImg2: string
    public answersImg3: string
    public answersImg: Array<string>

    // selectionImage
    public widthOriginal: number;
    public heightOriginal: number;
    public selectionsAreas: Array<SelectionArea>;

}
