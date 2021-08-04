package com.kamaa.myplaylist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gauravk.audiovisualizer.visualizer.BarVisualizer;

import java.io.File;
import java.nio.file.Files;
import java.sql.SQLOutput;
import java.util.ArrayList;

public class playlist extends AppCompatActivity {
    Button play,prev ,next,forward,backward;
    TextView txtstart,textstop,txtsongname;
    SeekBar seekBar;
    BarVisualizer barVisualizer;
    ImageView image;
    String sname;
    public static final String EXTRA_NAME ="song_name";
    static MediaPlayer mediaPlayer ;
    int position;
    ArrayList<File> audio;
    Thread seekerbar;
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        getSupportActionBar().setTitle( "Now playing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        play =(Button)findViewById(R.id.play);
        prev =(Button)findViewById(R.id.prev);
        next=(Button)findViewById(R.id.next);
        forward =(Button)findViewById(R.id.forward);
        backward =(Button)findViewById(R.id.backward);
        txtstart =(TextView)findViewById(R.id.textstart);
        textstop =(TextView)findViewById(R.id.textstop);
        txtsongname =(TextView)findViewById(R.id.txtsongname);

        seekBar =(SeekBar)findViewById(R.id.seekbar);
        image =(ImageView)findViewById(R.id.imagev);

        barVisualizer =(BarVisualizer)findViewById(R.id.blast);
        if (mediaPlayer !=null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        Intent i =getIntent();
        Bundle bundle =i.getExtras();
        audio = (ArrayList)bundle.getParcelableArrayList("audio");
        String songName = i.getStringExtra("songname");
        position =bundle.getInt("pos",0);
        //putting song name on textview
        txtsongname.setSelected(true);
        Uri uri = Uri.parse(audio.get(position).toString());
        sname =audio.get(position).getName();
        txtsongname.setText(sname);
        mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
        mediaPlayer.start();
        seekerbar =new Thread(){
            @Override
            public void run() {
                int totalduration =mediaPlayer.getDuration();
                int currentpos =0;
                while (currentpos<totalduration){
                    try {
                        sleep(500);
                        currentpos =mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentpos);

                    }
                    catch (Exception ex){
                        System.out.println("Too many comand issused  to media player");
                        //e.printStackTrace();
                    }
                }
            }
        };
        seekBar.setMax(mediaPlayer.getDuration());
        seekerbar.start();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
        String endtime =timer(mediaPlayer.getDuration());
        textstop.setText(endtime);
        //updating time as the song sing
        final Handler handler=new Handler();
        final int delay =1000;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String currenttime =timer(mediaPlayer.getCurrentPosition());
                txtstart.setText(currenttime);
                handler.postDelayed(this,delay);
            }
        } ,delay);
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()+10000);
                }
            }
        });
        backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()-1000);
                }
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()){
                    play.setBackgroundResource(R.drawable.ic_play);
                    mediaPlayer.pause();
                }
                else
                {
                    play.setBackgroundResource(R.drawable.ic_pause);
                    mediaPlayer.start();
                }
            }
        });
        //playing next song after song is complete
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                next.performClick();
            }
        });
        int audiosession =mediaPlayer.getAudioSessionId();
        if (audiosession !=-1){
            barVisualizer.setAudioSessionId(audiosession);
        }
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position =((position+1)%audio.size());
                Uri uri =Uri .parse(audio.get(position).toString());
                mediaPlayer =MediaPlayer.create(getApplicationContext(),uri);
                sname =audio.get(position).getName();
                txtsongname.setText(sname);
                mediaPlayer.start();
                play.setBackgroundResource(R.drawable.ic_pause);
                int audiosession =mediaPlayer.getAudioSessionId();
                if (audiosession !=-1){
                    barVisualizer.setAudioSessionId(audiosession);
                }
                animation(image);
            }
        });
       prev.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        mediaPlayer.stop();
        mediaPlayer.release();
        position =((position-1)<0)?(audio.size()-1):(position-1);
        Uri uri =Uri .parse(audio.get(position).toString());
        mediaPlayer =MediaPlayer.create(getApplicationContext(),uri);
        sname =audio.get(position).getName();
        txtsongname.setText(sname);
        mediaPlayer.start();
        play.setBackgroundResource(R.drawable.ic_pause);
        animation(image);
        int audiosession =mediaPlayer.getAudioSessionId();
        if (audiosession !=-1){
            barVisualizer.setAudioSessionId(audiosession);
        }

    }
});


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return  true;
    }

    public  void  animation (View view){
        //animating the music icon
        ObjectAnimator anime =ObjectAnimator.ofFloat(image,"rotation",0f ,360f);
        anime.setDuration(1000);
        AnimatorSet animeset = new AnimatorSet();
        animeset.playTogether(anime);
        animeset.start();
    }
    //converting miles second from mediaplayer class to second and minutes
    public String timer (int duration){
        String time="";
        int min =duration/1000/60;
        int sec =duration /1000%60;
        time +=min+":";
        if (sec<10) {
            time += 0;


        }
            time+=sec;


        return time;
    }
}