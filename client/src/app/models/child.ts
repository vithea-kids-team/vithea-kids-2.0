import { Resource } from './resource';

export class Child {
   	public childId: number
	public firstName: string	
	public lastName: string
	public birthDate: string
	public gender: string
	public username: string 
	public password: string
	public createdUtc : string
	public enabled : boolean
	public personalMessagesList
	public sequencesList 
	public animatedCharacter
	public prompting : {
		promptingStrategy: string,
		promptingColor: boolean,
		promptingSize: boolean,
		promptingScratch: boolean,
		promptingHide: boolean
	}
	public reinforcement : {
		reinforcementStrategy: string,
		reinforcementResource: Resource
	}
	public emotions : boolean
}
