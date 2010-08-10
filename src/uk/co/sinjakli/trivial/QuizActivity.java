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
import android.os.Bundle;


public class QuizActivity extends Activity {
	
	private final ArrayList<Question> questions = new ArrayList<Question>();
	
	public void onCreate(final Bundle savedInstanceState) {
		
		// Load up all questions available from the files
		try {
			for (final String fileName : getAssets().list("questions/")) {
				final InputStream input = getAssets().open("questions/" + fileName);
				
				final BufferedReader reader = new BufferedReader(new InputStreamReader(input));
				
				String inputLine;
				// Populate the questions ArrayList
				while (null != (inputLine = reader.readLine())) {
					if (inputLine.startsWith("//")) {
						break;
					}
					questions.add(Question.parse(inputLine));
				}
			}
		} catch (final IOException e) {
			// TODO: Don't rethrow, give user feedback, log stack trace
			throw new RuntimeException("IO Error reading question file.", e); // Dev String
		} catch (final NullPointerException e) {
			// TODO: Don't rethrow, give user feedback, log stack trace, this happening indicates major failure
			throw new RuntimeException("Attempted to open non-existant file.", e); // Dev String
		}
	
		// Randomise the output order of the questions
		Collections.shuffle(questions);
	}
}
