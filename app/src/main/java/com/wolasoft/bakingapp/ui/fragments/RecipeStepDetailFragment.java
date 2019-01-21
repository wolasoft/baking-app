package com.wolasoft.bakingapp.ui.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;
import com.wolasoft.bakingapp.R;
import com.wolasoft.bakingapp.data.models.Step;
import com.wolasoft.bakingapp.databinding.FragmentRecipeStepDetailBinding;

public class RecipeStepDetailFragment extends Fragment implements ExoPlayer.EventListener{
    private static final String SELECTED_STEP = "selected_step";
    private static final String TAG = "RECIPE_STEP";
    private static final String KEY_VIDEO_POSITION = "video_position";
    private static final String KEY_START_WINDOWS = "auto_play";
    private static final String KEY_AUTO_PLAY = "auto_play";

    private OnFragmentStepButtonInteractionListener mListener;
    private FragmentRecipeStepDetailBinding dataBinding;
    private SimpleExoPlayer exoPlayer;
    private MediaSessionCompat mediaSessionCompat;
    private PlaybackStateCompat.Builder stateBuilder;
    private boolean isPlayerPlaying;
    private Step step;
    private int orientation;
    private int startWindow;
    private long videoPosition;
    private boolean autoPlay = true;
    private boolean isTablet;

    public RecipeStepDetailFragment() { }

    public static RecipeStepDetailFragment newInstance(Step step) {
        RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(SELECTED_STEP, step);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            step = getArguments().getParcelable(SELECTED_STEP);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dataBinding =  DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_step_detail, container, false);

        if (savedInstanceState != null) {
            step = savedInstanceState.getParcelable(SELECTED_STEP);
            autoPlay = savedInstanceState.getBoolean(KEY_AUTO_PLAY);
            startWindow = savedInstanceState.getInt(KEY_START_WINDOWS);
            videoPosition = savedInstanceState.getLong(KEY_VIDEO_POSITION);
        } else {
            clearStartPosition();
        }

        orientation = getResources().getConfiguration().orientation;
        isTablet = getResources().getBoolean(R.bool.isTablet);
        initViews();

        return dataBinding.getRoot();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        updateStartPosition();
        outState.putParcelable(SELECTED_STEP, step);
        outState.putBoolean(KEY_AUTO_PLAY, autoPlay);
        outState.putInt(KEY_START_WINDOWS, startWindow);
        outState.putLong(KEY_VIDEO_POSITION, videoPosition);
    }

    private void initViews() {
        dataBinding.instructionsTV.setText(step.getDescription());
        initializeMediaSession();
        dataBinding.previousStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPreviousPressed(step);
            }
        });

        dataBinding.nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNextPressed(step);
            }
        });

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams)
                dataBinding.playerView.getLayoutParams();

        if (orientation == Configuration.ORIENTATION_LANDSCAPE && !isTablet) {
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            dataBinding.playerView.setLayoutParams(params);
            enterFullScreenMode();
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT && !isTablet) {
            exitFullScreenMode();
        }
    }

    private void enterFullScreenMode() {
        if (dataBinding.playerView != null) {
            final View decorView = getActivity().getWindow().getDecorView();
            int uiOptions = 0;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN &&
                    Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                uiOptions = View.GONE;
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                uiOptions = View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    private void exitFullScreenMode() {
        if (dataBinding.playerView != null) {
            final View decorView = getActivity().getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    private void initializeMediaSession() {
        mediaSessionCompat = new MediaSessionCompat(getContext(), TAG);
        mediaSessionCompat.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mediaSessionCompat.setMediaButtonReceiver(null);

        stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE
                );
        mediaSessionCompat.setPlaybackState(stateBuilder.build());
        mediaSessionCompat.setCallback(new SessionCallback());
        mediaSessionCompat.setActive(true);
    }

    private void createPlayer() {
        if (exoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl control = new DefaultLoadControl();
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, control);
            exoPlayer.setPlayWhenReady(autoPlay);
            dataBinding.playerView.setPlayer(exoPlayer);
            dataBinding.playerView.setDefaultArtwork(
                    BitmapFactory.decodeResource(getContext().getResources(), R.drawable.art_work)
            );

            exoPlayer.addListener(this);

            String userAgent = Util.getUserAgent(
                    getContext(), getResources().getString(R.string.app_name));
            Uri mediaUri = Uri.parse(step.getVideoURL());
            MediaSource mediaSource =
                    new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                            getContext(), userAgent), new DefaultExtractorsFactory(), null, null
                    );

            boolean haveStartPosition = startWindow != C.INDEX_UNSET;
            if (haveStartPosition) {
                Log.d("START_POSITION", "" + startWindow + " " + videoPosition);
                exoPlayer.seekTo(startWindow, videoPosition);
            }

            exoPlayer.prepare(mediaSource, !haveStartPosition, false);
        }

        goToForeground();
    }

    private void releasePlayer() {
        if (exoPlayer != null) {
            updateStartPosition();
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    private void updateStartPosition() {
        if (exoPlayer != null) {
            autoPlay = exoPlayer.getPlayWhenReady();
            startWindow = exoPlayer.getCurrentWindowIndex();
            videoPosition = Math.max(0, exoPlayer.getContentPosition());
        }
    }

    private void clearStartPosition() {
        autoPlay = true;
        startWindow = C.INDEX_UNSET;
        videoPosition = C.TIME_UNSET;
    }

    private void goToBackground(){
        if(exoPlayer != null){
            isPlayerPlaying = exoPlayer.getPlayWhenReady();
            exoPlayer.setPlayWhenReady(false);
        }
    }

    private void goToForeground(){
        if(exoPlayer != null){
            exoPlayer.setPlayWhenReady(isPlayerPlaying);
        }
    }

    private void onPreviousPressed(Step step) {
        if (mListener != null) {
            mListener.onPreviousStepButtonClicked(step);
        }
    }

    private void onNextPressed(Step step) {
        if (mListener != null) {
            mListener.onNextStepButtonClicked(step);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        createPlayer();

        if (dataBinding.playerView != null) {
            dataBinding.playerView.onResume();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        createPlayer();

        if (dataBinding.playerView != null) {
            dataBinding.playerView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        goToBackground();

        releasePlayer();

        if (dataBinding.playerView != null) {
            dataBinding.playerView.onPause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        goToForeground();

        releasePlayer();

        if (dataBinding.playerView != null) {
            dataBinding.playerView.onPause();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentStepButtonInteractionListener) {
            mListener = (OnFragmentStepButtonInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentStepButtonInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
        if (mediaSessionCompat != null) {
            mediaSessionCompat.setActive(false);
        }
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == ExoPlayer.STATE_READY && playWhenReady) {
            int playBackSpeed = 1;
            stateBuilder.setState(
                    PlaybackStateCompat.STATE_PLAYING,
                    exoPlayer.getCurrentPosition(), playBackSpeed);
        } else if (playbackState == ExoPlayer.STATE_READY) {
            float playBackSpeed = 1f;
            stateBuilder.setState(
                    PlaybackStateCompat.STATE_PAUSED,
                    exoPlayer.getCurrentPosition(), playBackSpeed);
        }
    }

    @Override
    public void onPositionDiscontinuity(int reason) {
        if (exoPlayer.getPlaybackError() != null) {
            updateStartPosition();
        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        Toast.makeText(
                getContext(),
                getResources().getString(R.string.exo_player_error_message),
                Toast.LENGTH_SHORT)
                .show();
    }

    public interface OnFragmentStepButtonInteractionListener {
        void onNextStepButtonClicked(Step step);
        void onPreviousStepButtonClicked(Step step);
    }

    private class SessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            exoPlayer.setPlayWhenReady(autoPlay);
        }

        @Override
        public void onPause() {
            exoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            exoPlayer.seekTo(0);
        }
    }
}
