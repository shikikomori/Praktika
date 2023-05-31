package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static  final  String KEY_INDEX="index";
    private Button mTrueButton;
    private  Button mFasleButton;
    private  Button mNextButton;

    private Button mDeceitButton;
    private TextView mQuestionTextView;
    private Button  mBackButton;
    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_android,true),
            new Question(R.string.question_liner,false),
            new Question(R.string.question_service,false),
            new Question(R.string.question_res,true),
            new Question(R.string.question_manifest,true),
            new Question(R.string.question_ber,true),
            new Question(R.string.question_ger,false),
            new Question(R.string.question_red,true),
            new Question(R.string.question_der,false),
            new Question(R.string.question_ker,true),
    };
    private void updateQuestion(){
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }
    private void checkAnswer(boolean userPressedTrue){
        boolean answerIsTrue=
                mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;
        if (mIsDeceiter){
            messageResId=R.string.judgment_toast;
        }else {

        }
        if (userPressedTrue == answerIsTrue){
            messageResId=R.string.correct_toast;
        }  else {
            messageResId= R.string.incorrect_toast;
        }
        Toast.makeText(this,messageResId, Toast.LENGTH_SHORT).show();
    }
    private int mCurrentIndex = 0;
    private boolean mIsDeceiter;
    private static final int REQUEST_CODE_DECEIT = 0;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate(Bundle) вызван");
        setContentView(R.layout.activity_main);
        mQuestionTextView = (TextView)findViewById(R.id.question_text_view);
        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener(){
            @Override
                    public void onClick(View v){
                checkAnswer(true);
            }
        });
        mFasleButton = (Button)findViewById(R.id.false_button);
        mFasleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               checkAnswer(false);
            }
        });
        mNextButton = (Button)findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex +1)% mQuestionBank.length;
                updateQuestion();
            }
        });
            mBackButton = (Button)findViewById(R.id.back_button);
            mBackButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   if (mCurrentIndex == 0) {
                       mBackButton.setVisibility(View.INVISIBLE);
                   }else {
                       mCurrentIndex = (mCurrentIndex -1)% mQuestionBank.length;
                       mIsDeceiter=false;
                       mBackButton.setVisibility(View.INVISIBLE);
                   }
                    updateQuestion();
                }

            });
            mDeceitButton= (Button) findViewById(R.id.deceit_button);
            mDeceitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean answerIsTrue = mQuestionBank[mCurrentIndex]
                            .isAnswerTrue();
                    Intent i = DeceitActivity.newIntent(MainActivity.this,answerIsTrue);
                    startActivityForResult(i,REQUEST_CODE_DECEIT);
                }
            });
        if (savedInstanceState  != null){
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX,0);

        }
        updateQuestion();

    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG,"onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX,mCurrentIndex);

    }
    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG,"onStart() вызвана");
    }
    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG,"onPause() вызван");
    }
    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG,"onResume() вызвана");
    }
    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG,"onStop() вызвана" );
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG,"onDestroy() вызвана");
    }
    @Override
    protected void onActivityResult(int reguestCode, int resultCode,Intent data){
        if (reguestCode != Activity.RESULT_OK){
            return;
        }
        if (reguestCode == REQUEST_CODE_DECEIT){
            if (data == null){
                return;
            }
            mIsDeceiter= DeceitActivity.wasAnswerShow(data);
        }
    }


}