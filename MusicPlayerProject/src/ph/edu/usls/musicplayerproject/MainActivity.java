package ph.edu.usls.musicplayerproject;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;

class Mp3Filter implements FilenameFilter{
	public boolean accept(File dir, String name){
		return (name.endsWith(".mp3") || name.endsWith(".MP3"));
	}
}

public class MainActivity extends ListActivity implements OnClickListener{
	SeekBar seek_bar;
	Handler seekHandler = new Handler();
	private static final String SD_PATH = new String("/sdcard/Music/");
	private List<String> songs = new ArrayList<String>();
	private MediaPlayer mp;
	private int position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		updatePlaylist();

		getInit();
		seekUpdation();
	}
	public void getInit() {
		Button stop = (Button) findViewById(R.id.stopBtn);
		Button pause = (Button) findViewById(R.id.pauseBtn);
		Button play = (Button) findViewById(R.id.playBtn);
		seek_bar = (SeekBar) findViewById(R.id.seek_bar);
		stop.setOnClickListener(this);
		pause.setOnClickListener(this);
		play.setOnClickListener(this);		
		mp = MediaPlayer.create(this, Uri.parse(SD_PATH + songs.get(position)));
		seek_bar.setMax(mp.getDuration()); 
	}
	Runnable run = new Runnable() {
		@Override 
		public void run() {
			seekUpdation(); 
		} 
	};
	private void seekUpdation() {
		// TODO Auto-generated method stub
		seek_bar.setProgress(mp.getCurrentPosition());
		seekHandler.postDelayed(run, 1000);
	}
	public int getPosn(){
		  return mp.getCurrentPosition();
		}
	public void playPrev(){
		 
		}
	@Override
	protected void onListItemClick(ListView list, View view, int position, long id) {
		try{		
			mp.reset();
			mp.setDataSource(SD_PATH + (songs.get(position)));
			mp.prepare();
			mp.start();	
			mp.setLooping(true);
			
		}catch(IOException e) {
			Log.v(getString(R.string.app_name), e.getMessage());
		}
	}
	private void updatePlaylist() {
		// TODO Auto-generated method stub
		File home = new File(SD_PATH);
		if (home.listFiles(new Mp3Filter()).length > 0){
			for (File file : home.listFiles(new Mp3Filter())){
				songs.add(file.getName());
			}
			ArrayAdapter<String> songList = new ArrayAdapter<String>(this,R.layout.song_item,songs);
			setListAdapter(songList);
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.playBtn:
			mp.start();
			break;
		case R.id.pauseBtn:
			mp.pause();
			break;
		case R.id.stopBtn:
			try {
				mp.reset();
				mp.stop();
				mp.reset();
				mp.setDataSource(SD_PATH + songs.get(position));
				mp.prepare();
			} catch(IOException e) {
				Log.v(getString(R.string.app_name), e.getMessage());
			}
			break;	

		}
	}
}

