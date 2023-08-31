package com.devkraft.karmahealth.Screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.devkraft.karmahealth.Model.FlagSecureHelper;
import com.devkraft.karmahealth.R;
import com.devkraft.karmahealth.Utils.Constants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class FullScreenActivity extends AppCompatActivity {
    private ImageView closeIv;
    YouTubePlayerView youTubePlayerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        intIds();
        handleClickEvent();
        addYouTubeViewToLifeCycle();
        getValueFromIntentAndLoadUrl();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
    }
    @Override
    public Object getSystemService(@NonNull String name) {
        Object result=super.getSystemService(name);

        return(FlagSecureHelper.getWrappedSystemService(result, name));
    }

    private void getValueFromIntentAndLoadUrl() {
        Intent intent  = getIntent();
        if(intent != null){
            String videoUrl = intent.getStringExtra(Constants.VIDEO_URL);
            loadUrlIntoPlayer(videoUrl);
        }
    }

    private void loadUrlIntoPlayer(String videoId) {
        if(videoId != null){
            youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                    youTubePlayer.loadVideo(videoId, 0);
                }
            });
        }
    }

    private void addYouTubeViewToLifeCycle() {
        getLifecycle().addObserver(youTubePlayerView);
    }

    private void handleClickEvent() {
        closeIv.setOnClickListener(view -> finish());
    }

    private void intIds() {
        closeIv = findViewById(R.id.closeIv);
        youTubePlayerView = findViewById(R.id.youtube_player_view);
    }

    private void setFullScreenActivity() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);




    }
}