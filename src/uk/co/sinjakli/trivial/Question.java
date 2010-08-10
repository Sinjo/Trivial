/**
 * @author Chris Sinjakli
 */

package uk.co.sinjakli.trivial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

final class Question {
	
	private final String questionType;
	private final String question;
	private final ArrayList<String> answers;
	
	private Question(String questionType, String question, ArrayList<String> answers) {
		this.questionType = questionType;
		this.question = question;
		this.answers = answers;
	}
	
	/**
	 * Takes a comma separated String of the question type, the question, and the answers, and returns a {@link Question} representation of them.
	 * 
	 * @param input The input String made up of the type of question, the question itself, and the answers (comma separated).
	 * @return a {@link Question} representation of the input string.
	 */
	public static Question parse(String input) {
		
		final List<String> questionComponents;
		final String questionType;
		final String question;
		final ArrayList<String> answers = new ArrayList<String>();
		
		// Split the input on commas
		try {
		questionComponents = Arrays.asList(input.split(","));
		} catch (final NullPointerException e) {
			throw new RuntimeException("Question to be parsed was null.", e); // Dev String
		}
		
		// Extract the component parts of the question
		try {
			questionType = questionComponents.get(0).trim();
			question = questionComponents.get(1).trim();
			
			for (final String s : questionComponents.subList(2, questionComponents.size())) {
				answers.add(s.trim());
			}
			
		} catch (final IndexOutOfBoundsException e) {
			throw new RuntimeException("Question provided had an invalid format: Insufficient Parts.", e); // Dev String
		}
		
		// Construct and return a new Question
		return new Question(questionType, question, answers);
		
	}

	/**
	 * Returns the type of the {@link Question}
	 * 
	 * @return the type of the {@link Question}
	 */
	public final String getQuestionType() {
		return questionType;
	}

	/**
	 * Returns the question element of the {@link Question}
	 * 
	 * @return the question element of the {@link Question}
	 */
	public final String getQuestion() {
		return question;
	}

	/**
	 * Returns the answers, in a random order, as an ArrayList of String
	 * 
	 * @return the answers, in a random order, as an ArrayList of String
	 */
	public final ArrayList<String> getAnswers() {
		// ArryList is not immutable, create a defensive copy
		final ArrayList<String> answers = new ArrayList<String>(this.answers);
		Collections.shuffle(answers);
		return answers;
	}

	/**
	 * Returns the correct answer to the {@link Question}
	 * 
	 * @return the correct answer to the {@link Question}
	 */
	public final String getAnswer() {
		return answers.get(2);
	}

}