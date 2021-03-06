package com.framgia.soundclound.screen.local;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.framgia.soundclound.BR;
import com.framgia.soundclound.data.model.Track;
import com.framgia.soundclound.data.source.TrackRepository;
import com.framgia.soundclound.data.source.local.SharePreferences;
import com.framgia.soundclound.data.source.remote.TrackRemoteDataSource;
import com.framgia.soundclound.screen.moretrack.MoreTrackFragment;
import com.framgia.soundclound.screen.moretrack.MoreTrackLocalFragment;
import com.framgia.soundclound.screen.playtrack.PlayTrackActivity;
import com.framgia.soundclound.util.Constant;
import com.google.gson.Gson;

/**
 * Created by ADMIN on 1/7/2018.
 */

public class TrackLocalViewModel extends BaseObservable implements TrackClickListener,
        OptionClickListener {
    private static final String TAG = TrackLocalViewModel.class.toString();

    private TrackLocalAdapter mTrackLocalAdapter;
    private Context mContext;
    private TrackRepository mTrackRepository;
    
    public TrackLocalViewModel(Context context) {
        mContext = context;
        mTrackRepository = new TrackRepository(TrackRemoteDataSource.getInstance());
        mTrackLocalAdapter = new TrackLocalAdapter();
        mTrackLocalAdapter.setTrackClickLisener(this);
        mTrackLocalAdapter.setOptionClickListener(this);
        if (checkPermisson()) {
            // TODO: 1/11/2018  getData
            getTracks();
        }
    }

    public void getTracks() {
        mTrackLocalAdapter.getData(mTrackRepository.getLocalTrack());
    }

    @Bindable
    public TrackLocalAdapter getTrackLocalAdapter() {
        return mTrackLocalAdapter;
    }

    public void setTrackLocalAdapter(TrackLocalAdapter trackLocalAdapter) {
        mTrackLocalAdapter = trackLocalAdapter;
        notifyPropertyChanged(BR.trackLocalAdapter);
    }

    @Override
    public void onItemTrackClick(Track track, int position) {
          SharePreferences.getInstance().putListTrack(new Gson().toJson(
                mTrackLocalAdapter.getmTracks()));
        SharePreferences.getInstance().putTrack(new Gson().toJson(track));
        SharePreferences.getInstance().putIndex(position);
        mContext.startActivity(PlayTrackActivity.getInstance(mContext));
    }

    public boolean checkPermisson() {
        boolean isAllow = true;
        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            isAllow = false;
            ActivityCompat.requestPermissions((Activity) mContext,
                    Constant.PERMISSON, Constant.REQUEST_READ_STORAGE);
        }
        return isAllow;
    }

    @Override
    public void onOptionClick(Track track) {
        Log.d(TAG, track.toString());
        MoreTrackLocalFragment.newInstance(track)
                .show(((AppCompatActivity) mContext).getSupportFragmentManager(), null);

    }
}
