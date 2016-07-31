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

	/**
	 * @return the exerciseId
	 */
	public Long getExerciseId() {
		return exerciseId;
	}

	/**
	 * @param exerciseId the exerciseId to set
	 */
	public void setExerciseId(Long exerciseId) {
		this.exerciseId = exerciseId;
	}

	/**
	 * @return the topic
	 */
	public Topic getTopic() {
		return topic;
	}

	/**
	 * @param topic the topic to set
	 */
	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	/**
	 * @param topicId of the topic to set
	 */
	public void setTopic(Long topicId) {
		Topic topic = Topic.findTopicById(topicId);
		if (topic == null)
			throw new NullPointerException("Topic does not exist");
		Logger.debug("New exercise :: setTopic: " + topic.getTopicDescription());
		this.topic = topic;
	}


	/**
	 * @return the level
	 */
	public Level getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(Level level) {
		this.level = level;
	}

	/**
	 * @param levelid of the level to set
	 */
	public void setLevel(Long levelId) {
		Level level = Level.findLevelById(levelId);
		if (level == null)
			throw new NullPointerException("Level does not exist");
		Logger.debug("New exercise :: setLevel: " + level.getLevelDescription());
		this.level = level;
	}

	/**
	 * @return the question
	 */
	public Question getQuestion() {
		return question;
	}

	/**
	 * @param question the question to set
	 */
	public void setQuestion(Question question) {
		this.question = question;
	}

	/**
	 * @param questionDescription of the question to set
	 */
	public void setQuestion(String questionDescription, Long stimulus) {
		Question question = new Question();
		question.setQuestionDescription(questionDescription);
		if(stimulus != 0)
			question.setStimulus(stimulus);
		question.save();
		Logger.debug("New exercise :: setQuestion: " + question.getQuestionDescription() + " (" + question.getQuestionId() + ")");
		this.question = question;
	}

	/**
	 * @return the rightAnswer
	 */
	public Answer getRightAnswer() {
		return rightAnswer;
	}

	/**
	 * @param rightAnswer the rightAnswer to set
	 */
	public void setRightAnswer(Answer rightAnswer) {
		this.rightAnswer = rightAnswer;
	}

	/**
	 * @param rightAnswerDescription the rightAnswer to set
	 */
	public void setRightAnswer(String rightAnswerDescription) {
		Answer rightAnswer = new Answer();
		rightAnswer.setAnswerDescription(rightAnswerDescription); 
		rightAnswer.save();
		Logger.debug("New exercise :: setRightAnswer: " + rightAnswer.getAnswerDescription() +" (" + rightAnswer.getAnswerId() + ")");
		this.rightAnswer = rightAnswer;
		this.answers.add(rightAnswer);
	}

	/**
	 * @return the answers
	 */
	public List<Answer> getAnswers() {
		return answers;
	}

	/**
	 * @param answersDescription of the answers to set
	 */
	public void setAnswers(List<String> answerDescriptions) {		
		for(Iterator<String> i = answerDescriptions.iterator(); i.hasNext(); ) {
			String item = i.next();
			Answer answer = new Answer();
			answer.setAnswerDescription(item);
			answer.save();
			Logger.debug("New exercise :: addDistractor: " + answer.getAnswerDescription() +" (" + answer.getAnswerId() + ")");
			this.answers.add(answer);
		}		
	}


	/**
	 * @return the author
	 */
	public Caregiver getAuthor() {
		return author;
	}

	/**
	 * @param author the author to set
	 */
	public void setAuthor(Caregiver author) {
		this.author = author;
	}
	
	public static final Finder<Long, Exercise> find = new Finder<>(Exercise.class);

	public static List<Exercise> findByAuthor(Caregiver author) {
		Logger.debug("Looking for exercises from: " + author.getCaregiverLogin().getUserName());
		return find
		.where()
		.eq("author_caregiver_id", author.getCaregiverLogin().getLoginId())
		.findList();
	}
}