/*
 *    Copyright 2010 Chris Sinjakli
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
	
	private Question(final String questionType, final String question, final ArrayList<String> answers) {
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
	public static Question parse(final String input) {
		
		final List<String> questionComponents;
		final String questionType;
		final String question;
		final ArrayList<String> answers = new ArrayList<String>();
		
		// Split the input on commas
		if (null != input) {
			questionComponents = Arrays.asList(input.split(","));
		} else {
			throw new IllegalArgumentException("Question to be parsed was null."); // Dev String
		}
		
		// Extract the component parts of the question
		if (4 > questionComponents.size()) {
			questionType = questionComponents.get(0).trim();
			question = questionComponents.get(1).trim();
			
			for (final String s : questionComponents.subList(2, questionComponents.size())) {
				answers.add(s.trim());
			}
			
		} else {
			throw new IllegalArgumentException("Question had insufficient parts. Note that at least two answers are required."); // Dev String
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