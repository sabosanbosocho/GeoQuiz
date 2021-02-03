package com.sbs.geoquiz;

import java.util.UUID;

public class Question {
    private int mResId;
    private int mAnswered;
    private int mIsCheated;

    public int getResId() {
        return mResId;
    }

    public void setResId(int resId) {
        mResId = resId;
    }

    public int getAnswered() {
        return mAnswered;
    }

    public void setAnswered(int answered) {
        mAnswered = answered;
    }

    public boolean isTrueAnswer() {
        return mTrueAnswer;
    }

    public void setTrueAnswer(boolean trueAnswer) {
        mTrueAnswer = trueAnswer;
    }

    private boolean mTrueAnswer;

    public int getIsCheated() {
        return mIsCheated;
    }

    public void setIsCheated(int isCheated) {
        mIsCheated = isCheated;
    }

    public Question(int resId, int answered, boolean trueAnswer, int isCheated) {
        mAnswered = answered;
        mResId = resId;
        mTrueAnswer = trueAnswer;
        mIsCheated = isCheated;
    }
}
