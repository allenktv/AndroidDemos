package com.allen.regex;

import android.app.Activity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class RegexActivity extends Activity {

    EditText mEditText;
    Button mTestButton;
    TextView mResultView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //Set up views
        mEditText = (EditText)findViewById(R.id.edit_text);
        mTestButton = (Button)findViewById(R.id.test_button);
        mResultView = (TextView)findViewById(R.id.test_result);

        mTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mEditText.getText().length()==0){
                    //If text is empty
                    Toast.makeText(RegexActivity.this,R.string.empty_string,Toast.LENGTH_SHORT).show();
                }else {
                    //Set "true" if regex is matched, false if not
                    String result = String.valueOf(checkRegex(mEditText.getText().toString()));
                    mResultView.setText(result);
                }
            }
        });
    }

    public boolean checkRegex(String s){
        //Enter regex here
        return s.matches("\\(?\\d{3}[)-]?\\d{3}-?\\d{4}"); //Verifies that the text is a phone number
    }
}
