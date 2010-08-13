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

import android.app.Activity;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


public class QuizActivity extends Activity {
	
	// Private Constants
	private static final String TAG = "QuizActivity";
	
	// Load available resources and assets
	private final Resources res = getResources();
	private final AssetManager assets = getAssets();
	
	// Load up all questions available from the files
	private final ArrayList<Question> questions = loadQuestions("questions/");
	
	public void onCreate(final Bundle savedInstanceState) {

		// Randomise the output order of the questions
		Collections.shuffle(questions);
	}

	/**
	 * Loads questions stored in all files at the specified file path.
	 * 
	 * @param questionFilePath The file path from which questions should be loaded.
	 * @return An {@link ArrayList} of all questions found at the specified path.
	 */
	private ArrayList<Question> loadQuestions(final String questionFilePath) {
		final ArrayList<Question> questions = new ArrayList<Question>();
		try {
			int failedParsesTotal = 0;
			for (final String fileName : assets.list(questionFilePath)) {
				final InputStream input = assets.open(questionFilePath + fileName);
				final BufferedReader reader = new BufferedReader(new InputStreamReader(input));
				
				// Populate the questions ArrayList
				String inputLine;
				int failedParses = 0;
				while (null != (inputLine = reader.readLine())) {
					// Ignore any comments in the question file
					if (!inputLine.startsWith("//")) {
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
					Log.e(TAG, failedParses + " questions were unable to be parsed in file " + questionFilePath + fileName); // Dev String
				}
			}
			
			// If any questions failed to parse within any file, summarise (to the user) how many failed
			if (0 < failedParsesTotal) {
				Toast.makeText(getApplicationContext(), String.format(res.getQuantityString(R.plurals.question_reading_parse_fail_number, failedParsesTotal), failedParsesTotal), Toast.LENGTH_LONG).show();
			}
			
		} catch (final IOException e) {
			Log.e(TAG, "Error reading questions.", e); // Dev String
			Toast.makeText(getApplicationContext(), res.getString(R.string.question_reading_ioexception), Toast.LENGTH_LONG).show();
		}
		
		return questions;
	}
}
