package noel.example.com.customizeexoplayer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MotionEventCompat;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.PictureInPictureParams;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.util.Rational;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.AdaptiveMediaSourceEventListener;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.swipper.library.CircularSeekBar;
import com.swipper.library.CustomView;
import com.swipper.library.SeekView;
import com.swipper.library.Swipper;

import java.util.concurrent.TimeUnit;

import static android.view.MotionEvent.INVALID_POINTER_ID;

public class MainActivity extends AppCompatActivity {

   PlayerView playerView;
    ProgressBar progressBar;
    ImageView btfullscreen,btvolume,btbright,btpip;
    SimpleExoPlayer simpleExoPlayer;















    boolean flag=false;

    String videourl="https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";


    private String onHorizontal;
    private String onVertical;
    private String onCircular;
    public static String color = "#FB5B0A";
    private CustomView customView;
    private SeekView seekView;
    private float brightness;
    private CircularSeekBar circularSeekBar;
    private boolean checkBrightness = true;
    private int mActivePointerId = INVALID_POINTER_ID;
    private int numberOfTaps = 0;
    private long lastTapTimeMs = 0;
    private long touchDownMs = 0;
    private double volper;
    private float seekdistance = 0;
    private float distanceCovered = 0;
    private boolean checkSeek = true;
    private MediaPlayer mediaPlayer;
    private int maxVolume;
    private int currentVolume;
    private double per;
    private AudioManager audio;
    private boolean checkVolume = true;

    private static final String TAG = MainActivity.class.getSimpleName();
    ActionBar actionBar;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);







        playerView=findViewById(R.id.player_view);
        progressBar=findViewById(R.id.progress_bar);
        btfullscreen=findViewById(R.id.bt_fullscreen);
        btvolume=findViewById(R.id.volume);
        btbright=findViewById(R.id.bright);
        btpip=findViewById(R.id.btn_minimize);
        actionBar = getActionBar();


        btpip.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view)
            {
                Display d = getWindowManager()
                        .getDefaultDisplay();
                Point p = new Point();
                d.getSize(p);
                int width = p.x;
                int height = p.y;

                Rational ratio
                        = new Rational(width, height);
                PictureInPictureParams.Builder
                        pip_Builder
                        = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    pip_Builder = new PictureInPictureParams
                    .Builder();
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    pip_Builder.setAspectRatio(ratio).build();
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    enterPictureInPictureMode(pip_Builder.build());
                }
            }
        });





















        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        Uri videouri= Uri.parse(videourl);

        LoadControl loadControl=new DefaultLoadControl();

        BandwidthMeter bandwidthMeter=new DefaultBandwidthMeter();

        TrackSelector trackSelector= new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));




        simpleExoPlayer= ExoPlayerFactory.newSimpleInstance(this,trackSelector,loadControl);

        DefaultHttpDataSourceFactory dataSourceFactory=new DefaultHttpDataSourceFactory("exoplayer_video");

        ExtractorsFactory extractorsFactory=new DefaultExtractorsFactory();



        MediaSource mediaSource=new ExtractorMediaSource(videouri,dataSourceFactory,extractorsFactory,null,null);

        playerView.setPlayer(simpleExoPlayer);




        playerView.getKeepScreenOn();
        simpleExoPlayer.prepare(mediaSource);
        simpleExoPlayer.setPlayWhenReady(true);
        simpleExoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {


                if (playbackState==Player.STATE_BUFFERING){

                    progressBar.setVisibility(View.VISIBLE);

                }else  if (playbackState==Player.STATE_READY){
                    progressBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });


        btfullscreen.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SourceLockedOrientationActivity")
            @Override
            public void onClick(View view) {
                if (flag){

                    btfullscreen.setImageDrawable(getResources().getDrawable(R.drawable.ic_fullscreen));

                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                    flag=false;
                } else {
                    btfullscreen.setImageDrawable(getResources().getDrawable(R.drawable.ic_fullscreen_exit));

                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    flag=true;
                }
            }
        });



        btbright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Brightness(Swipper.Orientation.VERTICAL);
            }
        });




        btvolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Volume(Swipper.Orientation.VERTICAL);
            }
        });

        set(this);













    }










    public static void setColor(String s) {
        color = s;
    }
    public void Brightness(Swipper.Orientation orientation) {

        if (orientation.equals(Swipper.Orientation.VERTICAL))
            onVertical = "Brightness";
        else if (orientation.equals(Swipper.Orientation.HORIZONTAL))
            onHorizontal = "Brightness";
        else if (orientation.equals(Swipper.Orientation.CIRCULAR))
            onCircular = "Brightness";
    }

    public void Volume(Swipper.Orientation orientation) {

        if (orientation.equals(Swipper.Orientation.VERTICAL))
            onVertical = "Volume";
        else if (orientation.equals(Swipper.Orientation.HORIZONTAL))
            onHorizontal = "Volume";
        else if (orientation.equals(Swipper.Orientation.CIRCULAR))
            onCircular = "Volume";
    }


    public void set(Context context) {
        customView = new CustomView(context);
        seekView = new SeekView(context);
        brightness = android.provider.Settings.System.getFloat(getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, -1);
        WindowManager.LayoutParams layout = getWindow().getAttributes();
        layout.screenBrightness = brightness / 255;
//        layout.screenBrightness = 1;
        getWindow().setAttributes(layout);
        customView.setProgress((int) ((brightness / 255) * 100));
        customView.setProgress((int)((200/255) * 100));
        customView.setProgressText(Integer.valueOf((int) ((brightness / 255) * 100)).toString() + "%");
        circularSeekBar = new CircularSeekBar(context);
        audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVolume = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }


    public void changeBrightness(float X, float Y, float x, float y, float distance, String type) {
        customView.setTitle("Brightness");
        seekView.hide();
        if (type == "Y" && x == X) {
            distance = distance / 270;
            if (y < Y) {
                commonBrightness(distance);
            } else {
                commonBrightness(-distance);
            }
        } else if (type == "X" && y == Y) {
            distance = distance / 160;
            if (x > X) {
                commonBrightness(distance);
            } else {
                commonBrightness(-distance);
            }
        }
    }
    public void commonBrightness(float distance) {
        WindowManager.LayoutParams layout = getWindow().getAttributes();
        if (getWindow().getAttributes().screenBrightness + distance <= 1 && getWindow().getAttributes().screenBrightness + distance >= 0) {
            customView.show();
            if ((int) ((getWindow().getAttributes().screenBrightness + distance) * 100) > 100) {
                customView.setProgress(100);
                customView.setProgressText("100");
            } else if ((int) ((getWindow().getAttributes().screenBrightness + distance) * 100) < 0) {
                customView.setProgress(0);
                customView.setProgressText("0");
            } else {
                customView.setProgress((int) ((getWindow().getAttributes().screenBrightness + distance) * 100));
                customView.setProgressText(Integer.valueOf((int) ((getWindow().getAttributes().screenBrightness + distance) * 100)).toString() + "%");
            }
            layout.screenBrightness = getWindow().getAttributes().screenBrightness + distance;
            getWindow().setAttributes(layout);
        }
    }



    public void changeVolume(float X, float Y, float x, float y, float distance, String type) {
        customView.setTitle("  Volume  ");
        seekView.hide();
        if (type == "Y" && x == X) {
            if (y < Y) {
                distance = distance / 100;
                commonVolume(distance);
            } else {
                distance = distance / 150;
                commonVolume(-distance);
            }
        } else if (type == "X" && y == Y) {
            if (x > X) {
                distance = distance / 200;
                commonVolume(distance);
            } else {
                distance = distance / 250;
                commonVolume(-distance);
            }
        }
    }

    public void commonVolume(float distance) {
        currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
        per = (double) currentVolume / (double) maxVolume;
        Log.e("per", per + "");
        if (per + distance <= 1 && per + distance >= 0) {
            customView.show();
            if (distance > 0.05 || distance < -0.05) {
                customView.setProgress((int) ((per + distance) * 100));
                customView.setProgressText((int) ((per + distance) * 100) + "%");
                volper = (per + (double) distance);
                audio.setStreamVolume(AudioManager.STREAM_MUSIC, (int) (volper * 15), 0);
            }
        }
    }
    public void enableBrightness() {
        checkBrightness = true;
    }
    public void disableBrightness() {
        checkBrightness = false;
    }
    public void enableSeek() {
        checkSeek = true;
    }
    public void disableVolume() {
        checkVolume = false;
    }
    public void enableVolume() {
        checkVolume = true;
    }



    public boolean onTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                seekdistance = 0;
                distanceCovered = 0;
                touchDownMs = System.currentTimeMillis();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                final float x = ev.getX();
                final float y = ev.getY();
                distanceCovered = getDistance(x, y, ev);
                try {
                    if (onVertical == "Brightness" && checkBrightness == true)
                        changeBrightness(ev.getHistoricalX(0, 0), ev.getHistoricalY(0, 0), x, y, distanceCovered, "Y");
                    else if (onVertical == "Volume" && checkVolume == true)
                        changeVolume(ev.getHistoricalX(0, 0), ev.getHistoricalY(0, 0), x, y, distanceCovered, "Y");
                    else if (onVertical == "Seek" && checkSeek == true) {
                        changeSeek(ev.getHistoricalX(0, 0), ev.getHistoricalY(0, 0), x, y, distanceCovered, "Y");
                    }
                    if (onHorizontal == "Brightness" && checkBrightness == true)
                        changeBrightness(ev.getHistoricalX(0, 0), ev.getHistoricalY(0, 0), x, y, distanceCovered, "X");
                    else if (onHorizontal == "Volume" && checkVolume == true)
                        changeVolume(ev.getHistoricalX(0, 0), ev.getHistoricalY(0, 0), x, y, distanceCovered, "X");
                    else if (onHorizontal == "Seek" && checkSeek == true) {
                        changeSeek(ev.getHistoricalX(0, 0), ev.getHistoricalY(0, 0), x, y, distanceCovered, "X");
                    }
                } catch (IllegalArgumentException e) {

                } catch (IndexOutOfBoundsException e) {

                }

                break;
            }

            case MotionEvent.ACTION_UP: {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (customView.isVisible())
                            customView.hide();
                        if (seekView.isVisible())
                            seekView.hide();
                    }
                }, 2000);

                if ((System.currentTimeMillis() - touchDownMs) > ViewConfiguration.getTapTimeout()) {
                    numberOfTaps = 0;
                    lastTapTimeMs = 0;
                    break;
                }

                if (numberOfTaps > 0 && (System.currentTimeMillis() - lastTapTimeMs) < ViewConfiguration.getDoubleTapTimeout()) {
                    numberOfTaps += 1;
                } else {
                    numberOfTaps = 1;
                }

                lastTapTimeMs = System.currentTimeMillis();
                if (numberOfTaps == 2) {
                    if (onCircular == "Brightness") {
                        circularSeekBar.setType("Brightness");
                        if (circularSeekBar.isVisibile())
                            circularSeekBar.hide();
                        else
                            circularSeekBar.show();
                    } else if (onCircular == "Volume") {
                        circularSeekBar.setType("Volume");
                        if (circularSeekBar.isVisibile())
                            circularSeekBar.hide();
                        else
                            circularSeekBar.show();
                    }
                }
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }
            case MotionEvent.ACTION_CANCEL: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }
            case MotionEvent.ACTION_POINTER_UP: {

                final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);

                if (pointerId == mActivePointerId) {
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
                }
                break;
            }
        }
        return true;
    }

    public void changeSeek(float X, float Y, float x, float y, float distance, String type) {

        if (type == "Y" && x == X) {
            distance = distance / 300;
            if (y < Y) {
                seekCommon(distance);
            } else {
                seekCommon(-distance);
            }
        } else if (type == "X" && y == Y) {
            distance = distance / 200;
            if (x > X) {
                seekCommon(distance);
            } else {
                seekCommon(-distance);
            }
        }
    }


    public void seekCommon(float distance) {
        seekdistance += distance * 60000;
        seekView.show();
        if (simpleExoPlayer != null) {
            Log.e("after", simpleExoPlayer.getCurrentPosition() + (int) (distance * 60000) + "");
            Log.e("seek distance", (int) (seekdistance) + "");
            if (simpleExoPlayer.getCurrentPosition() + (int) (distance * 60000) > 0 && simpleExoPlayer.getCurrentPosition() + (int) (distance * 60000) < simpleExoPlayer.getDuration() + 10) {
                simpleExoPlayer.seekTo(simpleExoPlayer.getCurrentPosition() + (int) (distance * 60000));
                if (seekdistance > 0)
                    seekView.setText("+" + Math.abs((int) (seekdistance / 60000)) + ":" + String.valueOf(Math.abs((int) ((seekdistance) % 60000))).substring(0, 2) + "(" + (int) ((simpleExoPlayer.getCurrentPosition() + (int) (distance * 60000)) / 60000) + ":" + String.valueOf((int) ((simpleExoPlayer.getCurrentPosition() + (int) (distance * 60000)) % 60000)).substring(0, 2) + ")");
                else
                    seekView.setText("-" + Math.abs((int) (seekdistance / 60000)) + ":" + String.valueOf(Math.abs((int) ((seekdistance) % 60000))).substring(0, 2) + "(" + (int) ((simpleExoPlayer.getCurrentPosition() + (int) (distance * 60000)) / 60000) + ":" + String.valueOf((int) ((simpleExoPlayer.getCurrentPosition() + (int) (distance * 60000)) % 60000)).substring(0, 2) + ")");
            }
        } else if (playerView != null) {
            Log.e("after", simpleExoPlayer.getCurrentPosition() + (int) (distance * 60000) + "");
            Log.e("seek distance", (int) (seekdistance) + "");
            if (simpleExoPlayer.getCurrentPosition() + (int) (distance * 60000) > 0 && simpleExoPlayer.getCurrentPosition() + (int) (distance * 60000) < simpleExoPlayer.getDuration() + 10) {
                simpleExoPlayer.seekTo(simpleExoPlayer.getCurrentPosition() + (int) (distance * 60000));
                if (seekdistance > 0)
                    seekView.setText("+" + Math.abs((int) (seekdistance / 60000)) + ":" + String.valueOf(Math.abs((int) ((seekdistance) % 60000))).substring(0, 2) + "(" + (int) ((simpleExoPlayer.getCurrentPosition() + (int) (distance * 60000)) / 60000) + ":" + String.valueOf((int) ((simpleExoPlayer.getCurrentPosition() + (int) (distance * 60000)) % 60000)).substring(0, 2) + ")");
                else
                    seekView.setText("-" + Math.abs((int) (seekdistance / 60000)) + ":" + String.valueOf(Math.abs((int) ((seekdistance) % 60000))).substring(0, 2) + "(" + (int) ((simpleExoPlayer.getCurrentPosition() + (int) (distance * 60000)) / 60000) + ":" + String.valueOf((int) ((simpleExoPlayer.getCurrentPosition() + (int) (distance * 60000)) % 60000)).substring(0, 2) + ")");

            }
        }
    }


    public void Seek(Swipper.Orientation orientation, PlayerView v) {

        if (orientation.equals(Swipper.Orientation.VERTICAL))
            onVertical = "Seek";
        else if (orientation.equals(Swipper.Orientation.HORIZONTAL))
            onHorizontal = "Seek";
        playerView = v;
    }

    public void Seek(Swipper.Orientation orientation, MediaPlayer v) {

        if (orientation.equals(Swipper.Orientation.VERTICAL))
            onVertical = "Seek";
        else if (orientation.equals(Swipper.Orientation.HORIZONTAL))
            onHorizontal = "Seek";
        mediaPlayer = v;
    }

    float getDistance(float startX, float startY, MotionEvent ev) {
        float distanceSum = 0;
        final int historySize = ev.getHistorySize();
        for (int h = 0; h < historySize; h++) {
            float hx = ev.getHistoricalX(0, h);
            float hy = ev.getHistoricalY(0, h);
            float dx = (hx - startX);
            float dy = (hy - startY);
            distanceSum += Math.sqrt(dx * dx + dy * dy);
            startX = hx;
            startY = hy;
        }
        float dx = (ev.getX(0) - startX);
        float dy = (ev.getY(0) - startY);
        distanceSum += Math.sqrt(dx * dx + dy * dy);
        return distanceSum;
    }



   /* protected  void onPause(){
        super.onPause();
        simpleExoPlayer.setPlayWhenReady(false);
        simpleExoPlayer.getPlaybackState();

    }*/     //hidden for background play

    @Override
    protected void onRestart() {
        super.onRestart();
        simpleExoPlayer.setPlayWhenReady(true);
        simpleExoPlayer.getPlaybackState();


    }











}
