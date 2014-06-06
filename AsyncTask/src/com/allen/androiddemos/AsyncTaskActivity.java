package com.allen.androiddemos;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AsyncTaskActivity extends Activity {
    EditText mEditText;
    Button mStartTaskButton;
    TextView mResultTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.async_task_activity);

        //Initialize the views
        mEditText = (EditText)findViewById(R.id.async_edit_text);
        mStartTaskButton = (Button)findViewById(R.id.async_button);
        mResultTextView = (TextView)findViewById(R.id.async_text_view);

        //Set on click listener for button
        mStartTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mEditText.getText().length()>0){

                    //Execute our AsyncTask call
                    new SampleTaskInBackground().execute(mEditText.getText().toString());
                }else {
                    //Ask user to enter at least 1 character
                    Toast.makeText(AsyncTaskActivity.this,R.string.enter_character,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Custom asyncTask
    public class SampleTaskInBackground extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            try {
                for (int i = 0; i < 5; i++) {
                    Thread.sleep(500);  //Wait 0.5 seconds for every loop
                    result = result + strings[0];  //append
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {

            mResultTextView.setText(s);
        }
    }
}
