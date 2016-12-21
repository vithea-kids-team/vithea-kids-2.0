package models;

import java.util.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.avaje.ebean.Model;

import play.Logger;

@Entity
public class Exercise extends Model {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long exerciseId;
	
	@ManyToOne
	private Topic topic;
	
	@ManyToOne
	private Level level;
	
	@ManyToOne
	private Question question;
	
	@OneToOne
	private Answer rightAnswer;
	
	@ManyToMany
	private List<Answer> answers;
	
	@ManyToOne
	private Caregiver author;

	public Long getExerciseId() {
		return exerciseId;
	}

	public void setExerciseId(Long exerciseId) {
		this.exerciseId = exerciseId;
	}

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	public void setTopic(Long topicId) {
		Topic topic = Topic.findTopicById(topicId);
		if (topic == null)
			throw new NullPointerException("Topic does not exist");
		Logger.debug("New exercise :: setTopic: " + topic.getTopicDescription());
		this.topic = topic;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public void setLevel(Long levelId) {
		Level level = Level.findLevelById(levelId);
		if (level == null)
			throw new NullPointerException("Level does not exist");
		Logger.debug("New exercise :: setLevel: " + level.getLevelDescription());
		this.level = level;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public void setQuestion(String questionDescription, Long stimulus) {
		Question question = new Question();
		question.setQuestionDescription(questionDescription);
		if(stimulus != 0)
			question.setStimulus(stimulus);
		question.save();
		Logger.debug("New exercise :: setQuestion: " + question.getQuestionDescription() + " (" + question.getQuestionId() + ")");
		this.question = question;
	}

	public Answer getRightAnswer() {
		return rightAnswer;
	}

	public void setRightAnswer(Answer rightAnswer) {
		this.rightAnswer = rightAnswer;
	}

	public void setRightAnswer(String rightAnswerDescription, Long resource) {
		Answer rightAnswer = new Answer();
		rightAnswer.setAnswerDescription(rightAnswerDescription); 
		rightAnswer.setStimulus(resource);
		rightAnswer.save();
		Logger.debug("New exercise :: setRightAnswer: " + rightAnswer.getAnswerDescription() +" (" + rightAnswer.getAnswerId() + ")");
		this.rightAnswer = rightAnswer;
		this.answers.add(rightAnswer);
	}

	public List<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<String> answerDescriptions, List<Long> answerStimulus) {
		Iterator<String> i = answerDescriptions.iterator(); 
		Iterator<Long> j = answerStimulus.iterator();		
		while(i.hasNext() || j.hasNext()) {
			String description = i.next();
			Long stimulus = j.next();
			Answer answer = new Answer();
			answer.setAnswerDescription(description);
			answer.setStimulus(stimulus);
			answer.save();
			Logger.debug("New exercise :: addDistractor: " + answer.getAnswerDescription() +" (" + answer.getAnswerId() + ")");
			this.answers.add(answer);
		}		
	}

	public Caregiver getAuthor() {
		return author;
	}

	public void setAuthor(Caregiver author) {
		this.author = author;
	}
	
	public static final Finder<Long, Exercise> find = new Finder<>(Exercise.class);

	public static List<Exercise> findByAuthor(Caregiver author) {
		Logger.debug("Looking for exercises from: " + author.getCaregiverLogin().getUsername());
		return find
		.where()
		.eq("author_caregiver_id", author.getCaregiverLogin().getLoginId())
		.findList();
	}
}