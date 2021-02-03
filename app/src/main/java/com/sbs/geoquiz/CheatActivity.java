package com.sbs.geoquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {
    private TextView mAnswerTextView;
    private Button mShowButton;
    private int isCheater = 0;
    private int clickCount = 0;
    private static final String EXTRA_BOOL = "extra_bool";
    private static final String CHEAT_CODE = "cheat_code";
    private static final String CHEAT_CODE2 = "cheat_code2";
    private static final String COUNT = "count";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
        if (savedInstanceState != null) {
            isCheater = savedInstanceState.getInt(CHEAT_CODE2, 0);
            setCheated(isCheater);
            clickCount = savedInstanceState.getInt(COUNT, 0);
        }
        mAnswerTextView = findViewById(R.id.answer_text_view);
        mShowButton = findViewById(R.id.show_button);
        mShowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean answered = getBoolean();
                checkAnswer(answered);
                isCheater = 1;
                setCheated(isCheater);
                clickCount++;
                setButtonEnabled();
            }
        });
    }

    public static Intent getIntent(Context context, boolean answered) {
        Intent intent = new Intent(context, CheatActivity.class);
        intent.putExtra(EXTRA_BOOL, answered);
        return intent;
    }

    private boolean getBoolean() {
        return getIntent().getBooleanExtra(EXTRA_BOOL, false);
    }

    private void checkAnswer(boolean currectAnswer) {
        if (currectAnswer) {
            mAnswerTextView.setText(R.string.true_button);
        } else {
            mAnswerTextView.setText(R.string.false_button);
        }
    }

    private void setCheated(int isCheated) {
        isCheater = isCheated;
        Intent intent = new Intent();
        intent.putExtra(CHEAT_CODE, isCheater);
        setResult(RESULT_OK, intent);
    }

    public static int isCheated(Intent intent) {
        return intent.getIntExtra(CHEAT_CODE, 0);
    }

    private void setButtonEnabled() {
        if (clickCount >= 3) {
            mShowButton.setEnabled(false);
        } else {
            mShowButton.setEnabled(true);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CHEAT_CODE2, isCheater);
        outState.putInt(COUNT, clickCount);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setButtonEnabled();
    }
}