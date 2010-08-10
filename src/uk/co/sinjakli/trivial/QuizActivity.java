/**
 * @author Chris Sinjakli
 *
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
