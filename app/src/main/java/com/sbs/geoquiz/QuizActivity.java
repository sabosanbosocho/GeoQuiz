package com.sbs.geoquiz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mQuestionTextView;
    private Button mTrueButton, mFalseButton, mCheatButton;
    private ImageButton mPrevImageButton, mNextImageButton;
    private int mCurrentIndex = 0;
    private static final int REQUEST_CODE1 = 1;
    private boolean mIsCheater;
    private double currectAnswer = 0;
    private double answered = 0;
    private int cheatCount = 0;
    private static final String TAG = "QuizActivity";
    private static final String EXTRA_INDEX = "extra_index";
    private static final String IS_CHEATED = "is_cheated";
    private static final String IS_ANSWERED = "is_answered";
    private static final String CURRECT = "currect";
    private static final String ANSWERED = "answered";
    private static final String COUNT = "count";
    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_1, 0, true, 0),
            new Question(R.string.question_2, 0, false, 0),
            new Question(R.string.question_3, 0, true, 0),
            new Question(R.string.question_4, 0, false, 0),
            new Question(R.string.question_5, 0, true, 0),
            new Question(R.string.question_6, 0, false, 0),
            new Question(R.string.question_7, 0, true, 0),
            new Question(R.string.question_8, 0, false, 0),
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Log.d(TAG, "onCreate called");
        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(EXTRA_INDEX, 0);
            int[] arrayInt = savedInstanceState.getIntArray(IS_ANSWERED);
            for (int i = 0; i < mQuestionBank.length; i++) {
                mQuestionBank[i].setAnswered(arrayInt[i]);
            }
            int[] cheatArray = savedInstanceState.getIntArray(IS_CHEATED);
            for (int i = 0; i < cheatArray.length; i++) {
                mQuestionBank[i].setIsCheated(cheatArray[i]);
            }
            answered = savedInstanceState.getDouble(ANSWERED, 0);
            currectAnswer = savedInstanceState.getDouble(CURRECT, 0);
            cheatCount = savedInstanceState.getInt(COUNT, 0);
        }
        mQuestionTextView = findViewById(R.id.question_text_view);
        mNextImageButton = findViewById(R.id.next_button);
        mPrevImageButton = findViewById(R.id.prev_button);
        mTrueButton = findViewById(R.id.true_button);
        mFalseButton = findViewById(R.id.false_button);
        mCheatButton = findViewById(R.id.cheat_button);
        updateQuestion(0);
        mQuestionTextView.setOnClickListener(this::onClick);
        mPrevImageButton.setOnClickListener(this::onClick);
        mNextImageButton.setOnClickListener(this::onClick);
        mTrueButton.setOnClickListener(this::onClick);
        mFalseButton.setOnClickListener(this::onClick);
        mCheatButton.setOnClickListener(this::onClick);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.question_text_view:
                updateQuestion(1);
                break;
            case R.id.cheat_button:
                boolean currectAnswer = mQuestionBank[mCurrentIndex].isTrueAnswer();
                Intent intent = CheatActivity.getIntent(QuizActivity.this, currectAnswer);
                startActivityForResult(intent, REQUEST_CODE1);
                cheatCount++;
                break;
            case R.id.false_button:
                checkAnswer(false);
                break;
            case R.id.true_button:
                checkAnswer(true);
                break;
            case R.id.prev_button:
                updateQuestion(-1);
                break;
            case R.id.next_button:
                updateQuestion(1);
                break;
            default:
        }
    }

    private void setButtonEnabled() {
        int i = mQuestionBank[mCurrentIndex].getAnswered();
        if (i == 0) {
            mTrueButton.setEnabled(true);
            mFalseButton.setEnabled(true);
        } else {
            mTrueButton.setEnabled(false);
            mFalseButton.setEnabled(false);
        }
        if (cheatCount >= 3) {
            mCheatButton.setEnabled(false);
        } else {
            mCheatButton.setEnabled(true);
        }
    }

    private void checkAnswer(boolean userPressed) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isTrueAnswer();
        int messageId = 0;
        int cheater = mQuestionBank[mCurrentIndex].getIsCheated();
        if (cheater != 1) {
            if (userPressed == answerIsTrue) {
                messageId = R.string.correct_toast;
                mQuestionBank[mCurrentIndex].setAnswered(1);
                currectAnswer++;
                answered++;
            } else {
                messageId = R.string.incorrect_toast;
                mQuestionBank[mCurrentIndex].setAnswered(-1);
                answered++;
            }
            if (answered < mQuestionBank.length) {
                Toast.makeText(this, messageId, Toast.LENGTH_SHORT).show();
            } else {
                double score = 0;
                score = currectAnswer / mQuestionBank.length * 100;
                Toast.makeText(this, messageId, Toast.LENGTH_SHORT).show();
                Toast.makeText(this, String.valueOf(score).toString(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.cheat_check, Toast.LENGTH_SHORT).show();
        }
        setButtonEnabled();
    }

    private void updateQuestion(int i) {
        if (i == 1) {
            mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
        } else if (i == -1) {
            if (mCurrentIndex == 0) {
                mCurrentIndex = mQuestionBank.length - 1;
            } else {
                mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
            }
        } else if (i == 0) {
            mCurrentIndex = mCurrentIndex;
        }
        int question = mQuestionBank[mCurrentIndex].getResId();
        mQuestionTextView.setText(question);
        setButtonEnabled();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSave called");
        outState.putInt(EXTRA_INDEX, mCurrentIndex);
        int[] arrayInt = new int[mQuestionBank.length];
        for (int i = 0; i < arrayInt.length; i++) {
            arrayInt[i] = mQuestionBank[i].getAnswered();
        }
        outState.putIntArray(IS_ANSWERED, arrayInt);
        int[] cheatArray = new int[mQuestionBank.length];
        for (int i = 0; i < cheatArray.length; i++) {
            cheatArray[i] = mQuestionBank[i].getIsCheated();
        }
        outState.putIntArray(IS_CHEATED, cheatArray);
        outState.putDouble(CURRECT, currectAnswer);
        outState.putDouble(ANSWERED, answered);
        outState.putInt(COUNT, cheatCount);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE1) {
            if (data == null) {
                return;
            } else {
                int isCheater = 0;
                isCheater = CheatActivity.isCheated(data);
                mQuestionBank[mCurrentIndex].setIsCheated(isCheater);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setButtonEnabled();
    }
}