import { Resource } from './resource';

export class Child {
    public childId: number
    public firstName: string
    public lastName: string
    public birthDate: string
    public gender: string
    public username: string
    public password: string
    public confirmpassword: string
    public createdUtc: string
    public enabled: boolean
    public sequenceId: number
    public personalMessagesList
    public sequencesList
    public animatedCharacter: {
        name: string,
        resoureceArea: string,
        resourceId: number,
        resourcePath: string
    }
    public prompting: {
        promptingStrategy: string,
        promptingColor: boolean,
        promptingSize: boolean,
        promptingScratch: boolean,
        promptingHide: boolean,
        promptingRead: boolean
    }
    public reinforcement: {
        reinforcementStrategy: string,
        reinforcementResource: Resource
    }
    public sequenceExercisesPreferences: {
        sequenceExercisesOrder: string,
        sequenceExercisesCapitalization: string
    }

    public emotions: boolean
}
