package com.android.parthjoshi.bakester.view;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.parthjoshi.bakester.R;
import com.android.parthjoshi.bakester.data.model.Recipe;
import com.android.parthjoshi.bakester.databinding.FragmentStepDetailBinding;
import com.android.parthjoshi.bakester.util.Constants;
import com.android.parthjoshi.bakester.viewmodel.recipestep.RecipeStepVM;
import com.android.parthjoshi.bakester.viewmodel.recipestep.RecipeStepVMFactory;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.util.Util;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepDetailFragment extends Fragment {

    private Recipe.Steps step;
    private FragmentStepDetailBinding binding;
    private RecipeStepVM vm;
    private boolean twoPane;
    private SimpleExoPlayer exoPlayer;
    private int stepNumber = 0;
    private int stepCount;
    private OnStepChangeListener listener;
    private long playbackPosition = 0;
    private int currentWindow;

    @Inject RecipeStepVMFactory factory;

    @Inject
    public StepDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_step_detail, container, false);
        binding.setLifecycleOwner(this);

        vm = ViewModelProviders.of(this, factory).get(RecipeStepVM.class);
        binding.setViewmodel(vm);

        if(savedInstanceState != null) {
            step = savedInstanceState.getParcelable(Constants.STEP_KEY);
            stepNumber = savedInstanceState.getInt(Constants.STEP_NUMBER_KEY);
            stepCount = savedInstanceState.getInt(Constants.STEP_COUNT_KEY);
            twoPane = savedInstanceState.getBoolean(Constants.TWO_PANE_KEY);
            currentWindow = vm.getCurrentWindow();
            playbackPosition = vm.getPlayBackPosition();

            if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
                    && !twoPane){
                hideSysteUI();
            }else
                showUI();

            setObservers();

            binding.prevStep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.prevStep(stepNumber);
                    onDestroy();
                }
            });

            binding.nextStep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.nextStep(stepNumber);
                    onDestroy();
                }
            });

            return binding.getRoot();
        }

        resolveMediaContent();

        setObservers();

        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
                && !twoPane){
            hideSysteUI();
        }

        vm.setShortDescription(step.getShortDescription());
        if(step.getStepId() != 0)
            vm.setStepDescription(step.getDescription());

        if(!twoPane)
            vm.setStepInfo(stepNumber, stepCount);
        else
            binding.stepCounter.setVisibility(View.GONE);

        binding.prevStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.prevStep(stepNumber);
                onDestroy();
            }
        });

        binding.nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.nextStep(stepNumber);
                onDestroy();
            }
        });

        return binding.getRoot();
    }

    private void showUI() {

        if(step.getVideoURL() != null && !step.getVideoURL().isEmpty()) {
            binding.exoPlayerView.setVisibility(View.VISIBLE);
            binding.stepThumbnail.setVisibility(View.GONE);

        } else if(!step.getThumbnailURL().isEmpty()){
            if(!step.getThumbnailURL().contains(".mp4")) {
                binding.exoPlayerView.setVisibility(View.GONE);
                binding.stepThumbnail.setVisibility(View.VISIBLE);
            }
        } else {
            binding.exoPlayerView.setVisibility(View.GONE);
            binding.stepThumbnail.setVisibility(View.GONE);
        }

        binding.tvShortDescription.setVisibility(View.VISIBLE);
        binding.tvStepDescription.setVisibility(View.VISIBLE);
        binding.stepCounter.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);

        if (!twoPane) {
            if(context instanceof OnStepChangeListener)
                listener = (OnStepChangeListener) context;
            else
                throw new RuntimeException(context.toString() + "must implement OnStepChangeListener");
        }
    }

    public void setStep(Recipe.Steps step){
        if(step != null)
            this.step = step;
    }

    public void setStepExtras(int stepNumber, int stepCount){
        this.stepCount = stepCount;
        this.stepNumber = stepNumber;
    }

    private void setObservers(){

        vm.getVideoError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String videoError) {
                Toast.makeText(getActivity(), videoError, Toast.LENGTH_LONG).show();
            }
        });

        vm.getSimpleExoPlayer().observe(this, new Observer<SimpleExoPlayer>() {
            @Override
            public void onChanged(@Nullable SimpleExoPlayer simpleExoPlayer) {
                exoPlayer = simpleExoPlayer;
                binding.exoPlayerView.setPlayer(exoPlayer);

                binding.exoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
                if(exoPlayer != null) {
                    exoPlayer.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                    exoPlayer.seekTo(currentWindow, playbackPosition);
                }
            }
        });
    }

    private void resolveMediaContent(){
        if(step.getVideoURL() != null && !step.getVideoURL().isEmpty()) {
            vm.initializePlayer(getContext(), Uri.parse(step.getVideoURL()));
        } else if(!step.getThumbnailURL().isEmpty()){
            if(!step.getThumbnailURL().contains(".mp4")) {
                binding.exoPlayerView.setVisibility(View.GONE);
                binding.stepThumbnail.setVisibility(View.VISIBLE);
                vm.setThumbnail(binding.stepThumbnail, step.getThumbnailURL());
            } else if(step.getThumbnailURL().contains(".mp4"))
                vm.initializePlayer(getContext(), Uri.parse(step.getThumbnailURL()));
        } else {
            binding.exoPlayerView.setVisibility(View.GONE);
            binding.stepThumbnail.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(Constants.STEP_KEY, step);
        outState.putInt(Constants.STEP_NUMBER_KEY, stepNumber);
        outState.putInt(Constants.STEP_COUNT_KEY, stepCount);
        outState.putBoolean(Constants.TWO_PANE_KEY, twoPane);
        if(exoPlayer != null) {
            vm.setPlayBackPosition(exoPlayer.getCurrentPosition());
            vm.setCurrentWindow(exoPlayer.getCurrentWindowIndex());
        }
    }

    public void setTwoPane(boolean twoPane) {
        this.twoPane = twoPane;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void hideSysteUI(){
        binding.exoPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LOW_PROFILE);

        binding.tvShortDescription.setVisibility(View.GONE);
        binding.tvStepDescription.setVisibility(View.GONE);
        binding.stepCounter.setVisibility(View.GONE);

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) binding.exoPlayerView.getLayoutParams();
        params.width = ConstraintLayout.LayoutParams.MATCH_PARENT;
        params.height = ConstraintLayout.LayoutParams.MATCH_PARENT;
        binding.exoPlayerView.setLayoutParams(params);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(Util.SDK_INT <= 23 || exoPlayer == null){
            resolveMediaContent();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(Util.SDK_INT <= 23)
            vm.releasePlayer();
    }

    public interface OnStepChangeListener{
        void prevStep(int position);
        void nextStep(int position);
    }
}
