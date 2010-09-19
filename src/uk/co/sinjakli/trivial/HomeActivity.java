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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class HomeActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		final Button startQuiz = (Button) findViewById(R.id.btn_start_quiz);
		startQuiz.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				// Set up Intent to launch QuizActivity
				final Intent i = new Intent(HomeActivity.this, QuizActivity.class);
				
				// Retrieve user input for number of questions
				final EditText numberOfQuestionsInput = (EditText) findViewById(R.id.input_number_of_questions);
				final int numberOfQuestions;
				try {
					numberOfQuestions = Integer.parseInt(numberOfQuestionsInput.getText().toString());
				} catch (final NumberFormatException e) {
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.invalid_number_of_questions), Toast.LENGTH_LONG).show();
					return;
				}
				
				// Check that the user has requested at least one question
				if (numberOfQuestions < 1) {
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.invalid_number_of_questions), Toast.LENGTH_LONG).show();
					return;
				}
				
				// Add number of questions to the Intent's extras
				i.putExtra("numberOfQuestions", numberOfQuestions);
				
				// Launch QuizActivity
				startActivity(i);
			}
		});
	}
}
