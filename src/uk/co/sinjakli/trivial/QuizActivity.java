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
	
	public void onCreate(Bundle savedInstanceState) {
		
		// Load up all questions available from the files
		try {
			for (final String s : getAssets().list("questions/")) {
				final InputStream input = getAssets().open("questions/" + s);
				
				final BufferedReader reader = new BufferedReader(new InputStreamReader(input));
				
				// Populate our questions ArrayList
				while (reader.ready()) {
					questions.add(Question.parse(reader.readLine()));
				}
			}
		} catch (final IOException e) {
			throw new RuntimeException("IO Error reading question file.", e); // Dev String
		} catch (final NullPointerException e) {
			throw new RuntimeException("Attempted to open non-existant file.", e); // Dev String
		}
	
		// Randomise the output order of the questions
		Collections.shuffle(questions);
	}
}
