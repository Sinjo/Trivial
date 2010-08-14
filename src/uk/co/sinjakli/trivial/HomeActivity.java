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

public class HomeActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		final Button startQuiz = (Button) findViewById(R.id.btn_start_quiz);
		startQuiz.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				final Intent i = new Intent(HomeActivity.this, QuizActivity.class);
				startActivity(i);
			}

		});
	}
}
