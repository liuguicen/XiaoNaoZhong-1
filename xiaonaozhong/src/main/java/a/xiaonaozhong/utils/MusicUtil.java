package a.xiaonaozhong.utils;

import java.io.IOException;

import android.app.Service;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;


public class MusicUtil {
	Boolean hasStop=true;
	MediaPlayer mPlayer;
	AudioManager audioManager;
	Context context;
	public MusicUtil(Context context)
	{
		mPlayer=new MediaPlayer();
		audioManager=(AudioManager)context.getSystemService(Service.AUDIO_SERVICE);
	}
	public void startMusic(String path) throws IllegalArgumentException, SecurityException, IllegalStateException, IOException
	{
		reduceVolume();
		mPlayer.setDataSource(path); 
		mPlayer.prepare();
		mPlayer.start();
		addVoice();
		hasStop=false;
	}
	public void stopMusic(){
		mPlayer.stop();
		hasStop=true;
	}
	public boolean hasStopMusic(){
		return hasStop;
	}
	public void addVoice()
	{
		audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, 0);
	}
	public void reduceVolume()
	{
		audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, 0);
	}
}
