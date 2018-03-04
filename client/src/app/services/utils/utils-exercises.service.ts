import { Injectable } from '@angular/core';
import { ReturnStatement } from '@angular/compiler/src/output/output_ast';

@Injectable()

export class UtilsExercisesService {

    constructor() { }

    validateTopic(topic) {
        if (topic === undefined) {
          return true;
        } else {
          return false;
        }
    }

    validateLevel(level) {
        if (level === undefined) {
          return true;
        } else {
          return false;
        }
    }

    validateQuestion(question) {
        if (question === undefined) {
          return true;
        } else {
          let question2 = question.replace(/\s+/g, '');
          if (question2.length === 0) {
            return true;
          } else {
            return  false;
          }
        }
    }

    validateRightAnswerImage(rightAnswer) {
        if (rightAnswer.length === 0) {
            return true;
        } else {
            return false;
        }
    }

    validateRightAnswerText(rightAnswer) {
      if (rightAnswer === undefined) {
        return true;
      } else {
        let rightAnswer2 = rightAnswer.replace(/\s+/g, '');
        if (rightAnswer2.length === 0) {
          return true;
        } else {
          return false;
        }
      }
  }
}
