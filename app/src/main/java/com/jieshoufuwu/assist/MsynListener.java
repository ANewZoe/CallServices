package com.jieshoufuwu.assist;

import android.media.MediaPlayer;
import android.os.Bundle;

import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SynthesizerListener;

public class MsynListener implements SynthesizerListener {
	
	public static int music=0;//
	MediaPlayer mPlayer;

	@Override
	public void onBufferProgress(int arg0, int arg1, int arg2, String arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCompleted(SpeechError arg0) {
		// TODO Auto-generated method stub
		MsynListener.music=0;
	}

	@Override
	public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSpeakBegin() {
		// TODO Auto-generated method stub
		MsynListener.music=1;
	}

	@Override
	public void onSpeakPaused() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSpeakProgress(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSpeakResumed() {
		// TODO Auto-generated method stub

	}

}
