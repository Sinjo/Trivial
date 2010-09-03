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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class QuizActivity extends Activity {
	
	// Private Constants
	private static final String TAG = "QuizActivity";
	
	// Private member variables
	private ArrayList<Question> questions;
	private long seed;
	private int currentQuestion;
	private int correctAnswers;
	private int incorrectAnswers;
	
	/**
	 * Sets up the initial state of the QuizActivity by loading the questions from the asset files.
	 * 
	 * @param savedInstanceState Any state which was saved about an ongoing quiz at an earlier time.
	 */
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		questions = new ArrayList<Question>();
		
		// Either load the seed if it has been stored, or create one and store it if it hasn't
		if (null != savedInstanceState) {
			if (savedInstanceState.containsKey("seed")) {
				seed = savedInstanceState.getLong("seed");
			} else {
				seed = new Random().nextLong();
			}
			
			if (savedInstanceState.containsKey("currentQuestion")) {
				currentQuestion = savedInstanceState.getInt("currentQuestion");
			} else {
				currentQuestion = 0;
			}
			
			if (savedInstanceState.containsKey("correctAnswers")) {
				correctAnswers = savedInstanceState.getInt("correctAnswers");
			} else {
				correctAnswers = 0;
			}
			
			if (savedInstanceState.containsKey("incorrectAnswers")) {
				incorrectAnswers = savedInstanceState.getInt("incorrectAnswers");
			} else {
				incorrectAnswers = 0;
			}	
		} else {
			// There is no saved instance data, set it all up from scratch
			seed = new Random().nextLong();
			currentQuestion = 0;
			correctAnswers = 0;
			incorrectAnswers = 0;
		}
		
		new LoadQuestionsTask().execute("questions");
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putLong("seed", seed);
		outState.putInt("currentQuestion", currentQuestion);
		super.onSaveInstanceState(outState);
	}

	/**
	 * Loads questions stored in all files at the specified file path. Returns an empty {@link ArrayList} if there are no questions found.
	 * 
	 * @param questionFilePath The file path from which questions should be loaded.
	 * @return An {@link ArrayList} of all questions found at the specified path.
	 */
	private ArrayList<Question> loadQuestions(final String questionFilePath) {
		final ArrayList<Question> questions = new ArrayList<Question>();
		try {
			int failedParsesTotal = 0;
			for (final String fileName : getAssets().list(questionFilePath)) {
				final InputStream input = getAssets().open(questionFilePath + "/" + fileName);
				final BufferedReader reader = new BufferedReader(new InputStreamReader(input));
				
				// Populate the questions ArrayList
				String inputLine;
				int failedParses = 0;
				while (null != (inputLine = reader.readLine())) {
					// Ignore any comments in the question file
					if (!inputLine.startsWith("//") && !(inputLine.length() == 0)) {
						try {
							questions.add(Question.parse(inputLine));
						} catch (final IllegalArgumentException e) {
							failedParses++;
							Log.e(TAG, "Unable to parse question: " + inputLine, e); // Dev String
						}
					}
				}
				
				// If any questions failed to parse within the current file, summarise how many failed
				if (0 < failedParses) {
					failedParsesTotal += failedParses;
					Log.e(TAG, failedParses + " questions were unable to be parsed in file " + questionFilePath + "/" + fileName); // Dev String
				}
			}
			
			// If any questions failed to parse within any file, summarise (to the user) how many failed
			if (0 < failedParsesTotal) {
				Toast.makeText(getApplicationContext(), String.format(getResources().getQuantityString(R.plurals.question_reading_parse_fail_number, failedParsesTotal), failedParsesTotal), Toast.LENGTH_LONG).show();
			}
			
		} catch (final IOException e) {
			Log.e(TAG, "Error reading questions.", e); // Dev String
			Toast.makeText(getApplicationContext(), getResources().getString(R.string.question_reading_exception), Toast.LENGTH_LONG).show();
		}
		
		return questions;
	}
	
	private class LoadQuestionsTask extends AsyncTask<String, Integer, Void> {
		
		// TODO: Move some of the logic from loadQuestions into here. loadQuestions should just deal with an individual file.
		@Override
		protected Void doInBackground(String... params) {
			
			// Load all questions available at each path given as an argument
			for (String path : params) {
				questions.addAll(loadQuestions(path));
			}
			
			// Randomise the output order of the questions
			final Random rand = new Random(seed);
			Collections.shuffle(questions, rand);
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			
			// If no questions were loaded, close the Activity
			if (questions.isEmpty()) {
				// TODO: This could return some sort of status to the HomeActivity, which could then display a failure message
				QuizActivity.this.finish();
				return;
			}
			
			setContentView(R.layout.quiz);
			
			// Display the topic of the question
			TextView quizTopic = (TextView) findViewById(R.id.quiz_topic);
			quizTopic.setText(questions.get(currentQuestion).getQuestionType());
			
			// Display the question
			TextView quizQuestion = (TextView) findViewById(R.id.quiz_question);
			quizQuestion.setText(questions.get(currentQuestion).getQuestion());
			
			// Display the answers to the question
			ListView quizAnswers = (ListView) findViewById(R.id.quiz_answers);
			// TODO: Use my own TextView for this list to control appearance, make it look like the rest of the app
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(QuizActivity.this, android.R.layout.simple_list_item_1, questions.get(currentQuestion).getAnswers().toArray(new String[0]));
			quizAnswers.setAdapter(adapter);
			
			// Set up an OnClickListener for the ListView
			quizAnswers.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					String selectedAnswer = ((TextView) view).getText().toString();
					if (selectedAnswer.equals(questions.get(currentQuestion).getAnswer())) {
						correctAnswers++;
						Log.v(TAG, "Correct answer chosen");
					} else {
						incorrectAnswers++;
						Log.v(TAG, "Incorrect answer chosen");
					}
				}
			});
		}
	}
}
