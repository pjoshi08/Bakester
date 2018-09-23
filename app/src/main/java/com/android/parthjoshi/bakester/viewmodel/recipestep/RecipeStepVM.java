package com.android.parthjoshi.bakester.viewmodel.recipestep;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.net.Uri;
import android.view.Surface;
import android.widget.ImageView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import com.squareup.picasso.Picasso;

public class RecipeStepVM extends ViewModel {

    private SimpleExoPlayer exoPlayer;
    private MyExoPlayerListener listener;
    private MutableLiveData<String> videoError;
    private MutableLiveData<SimpleExoPlayer> simpleExoPlayer;
    private String shortDescription;
    private String description;
    private long playBackPosition = 0;
    private int currentWindow;
    private boolean playWhenReady;
    private String stepInfo;

    public RecipeStepVM(){

        simpleExoPlayer = new MutableLiveData<>();
        videoError = new MutableLiveData<>();
        listener = new MyExoPlayerListener();
        playWhenReady = true;
    }

    public void initializePlayer(Context context, Uri videoUri){
        if(exoPlayer == null && context != null) {

            TrackSelection.Factory adaptiveTrackSelectionFactory = new AdaptiveTrackSelection.Factory(
                    new DefaultBandwidthMeter());

            exoPlayer = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(context),
                    new DefaultTrackSelector(adaptiveTrackSelectionFactory), new DefaultLoadControl());

            // Set the ExoPlayer.EventListener to this fragment.
            exoPlayer.addListener(listener);
            exoPlayer.setAudioDebugListener(listener);
            exoPlayer.setVideoDebugListener(listener);
            setSimpleExoPlayer(exoPlayer);
            exoPlayer.setPlayWhenReady(playWhenReady);

            //exoPlayer.seekTo(currentWindow, playBackPosition);

            // Prepare the MediaSource
            String userAgent = Util.getUserAgent(context, "Bakester");
            MediaSource mediaSource = new ExtractorMediaSource(videoUri,
                    new DefaultDataSourceFactory(context, userAgent),
                    new DefaultExtractorsFactory(),
                    null,
                    null);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);

        }
    }

    public long getPlayBackPosition() {
        return playBackPosition;
    }

    public int getCurrentWindow() {
        return currentWindow;
    }

    public void setShortDescription(String shortDescription){
        this.shortDescription = shortDescription;
    }

    public void setStepDescription(String description){
        this.description = description;
    }

    public String getStepInfo() {
        return stepInfo;
    }

    public void setStepInfo(int stepNumber, int stepCount) {
        stepInfo = "";
        this.stepInfo = "Step " + String.valueOf(stepNumber) + "/" + String.valueOf(stepCount);
    }

    public void setThumbnail(ImageView imageView, String thumbnailUrl){

        Picasso.get().load(thumbnailUrl).into(imageView);
    }

    private class MyExoPlayerListener implements ExoPlayer.EventListener, AudioRendererEventListener, VideoRendererEventListener{

        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest) {

        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

        }

        @Override
        public void onLoadingChanged(boolean isLoading) {

        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
            setVideoError("Error Playing Video");
        }

        @Override
        public void onPositionDiscontinuity() {

        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

        }

        @Override
        public void onAudioEnabled(DecoderCounters counters) {

        }

        @Override
        public void onAudioSessionId(int audioSessionId) {

        }

        @Override
        public void onAudioDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {

        }

        @Override
        public void onAudioInputFormatChanged(Format format) {

        }

        @Override
        public void onAudioTrackUnderrun(int bufferSize, long bufferSizeMs, long elapsedSinceLastFeedMs) {

        }

        @Override
        public void onAudioDisabled(DecoderCounters counters) {

        }

        @Override
        public void onVideoEnabled(DecoderCounters counters) {

        }

        @Override
        public void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {

        }

        @Override
        public void onVideoInputFormatChanged(Format format) {

        }

        @Override
        public void onDroppedFrames(int count, long elapsedMs) {

        }

        @Override
        public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {

        }

        @Override
        public void onRenderedFirstFrame(Surface surface) {

        }

        @Override
        public void onVideoDisabled(DecoderCounters counters) {

        }
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public MutableLiveData<String> getVideoError() {
        return videoError;
    }

    private void setVideoError(String videoError) {
        this.videoError.setValue(videoError);
    }

    public MutableLiveData<SimpleExoPlayer> getSimpleExoPlayer() {
        return simpleExoPlayer;
    }

    private void setSimpleExoPlayer(SimpleExoPlayer simpleExoPlayer) {
        this.simpleExoPlayer.setValue(simpleExoPlayer);
    }

    public void setCurrentWindow(int currentWindow) {
        this.currentWindow = currentWindow;
    }

    public void setPlayBackPosition(long playBackPosition) {
        this.playBackPosition = playBackPosition;
    }

    public void releasePlayer(){
        if(exoPlayer != null) {
            //playBackPosition = exoPlayer.getCurrentPosition();
            //currentWindow = exoPlayer.getCurrentWindowIndex();
            playWhenReady = exoPlayer.getPlayWhenReady();
            exoPlayer.removeListener(listener);
            exoPlayer.setVideoListener(null);
            exoPlayer.setVideoDebugListener(null);
            exoPlayer.setAudioDebugListener(null);
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
            setSimpleExoPlayer(null);
        }

        //onCleared();
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        stepInfo = "";
        shortDescription = "";
        description = "";

        releasePlayer();
    }
}
