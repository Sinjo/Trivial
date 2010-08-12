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
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


public class QuizActivity extends Activity {
	
	// Private Constants
	private static final String TAG = "MyActivity";
	
	// Load available resources
	private final Resources res = getResources();
	
	private final ArrayList<Question> questions = new ArrayList<Question>();
	
	public void onCreate(final Bundle savedInstanceState) {
		
		// Load up all questions available from the files
		try {
			for (final String fileName : getAssets().list("questions/")) {
				final InputStream input = getAssets().open("questions/" + fileName);
				final BufferedReader reader = new BufferedReader(new InputStreamReader(input));
				
				// Populate the questions ArrayList
				String inputLine;
				int numberOfFailedParses = 0;
				while (null != (inputLine = reader.readLine())) {
					// Ignore any comments in the question file
					if (!inputLine.startsWith("//")) {
						try {
							questions.add(Question.parse(inputLine));
						} catch (final IllegalArgumentException e) {
							numberOfFailedParses++;
							Log.e(TAG, "Unable to parse question: " + inputLine, e); // Dev String
						}
					}
				}
				
				// If any questions failed to parse, summarise how many failed
				if (0 < numberOfFailedParses) {
					Log.e(TAG, numberOfFailedParses + " questions were unable to be parsed"); // Dev String
					Toast.makeText(getApplicationContext(), String.format(res.getString(R.plurals.question_reading_parse_fail_number), numberOfFailedParses), Toast.LENGTH_LONG).show();
				}
			}
		} catch (final IOException e) {
			Log.e(TAG, "Error reading questions.", e); // Dev String
			Toast.makeText(getApplicationContext(), res.getString(R.string.question_reading_ioexception), Toast.LENGTH_LONG).show();
		}
	
		// Randomise the output order of the questions
		Collections.shuffle(questions);
	}
}
