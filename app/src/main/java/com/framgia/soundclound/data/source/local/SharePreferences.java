package com.framgia.soundclound.data.source.local;

import android.content.Context;
import android.content.SharedPreferences;
import com.framgia.soundclound.data.model.Track;
import com.framgia.soundclound.data.model.User;
import com.framgia.soundclound.util.Constant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sony on 1/12/2018.
 */

public class SharePreferences {

    private static final String KEY = "soundclound";

    private static final String LIST_TRACK = "listtrack";

    private static final String TRACK = "track";

    private static final String INDEX = "index";

    private static final String GENRE = "genre";

    private static final String SHUFFLE = "SHUFFLE";

    private static final String REPEAT = "REPEAT";

    private static final String OBJECT = "OBJECT";

    private static final String IS_LOGIN = "IS_LOGIN";

    private SharedPreferences mSharedPreferences;

    private static SharePreferences instance;

    public static SharePreferences getInstance() {
        return instance;
    }

    public static void init(Context context) {
        instance = new SharePreferences(context);
    }

    public SharePreferences(Context context) {
        mSharedPreferences = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
    }


    public Track getTrack() {
        return new Gson().fromJson(mSharedPreferences.getString(TRACK, null), Track.class);
    }

    public void setLogin(final boolean b) {
        mSharedPreferences.edit().putBoolean(IS_LOGIN, b).apply();
    }

    public boolean isLogin() {
        return mSharedPreferences.getBoolean(IS_LOGIN, false);
    }

    public void putTrack(String track) {
        mSharedPreferences.edit().putString(TRACK, track).apply();
    }

    public boolean addTrack(Track track) {
        if (track == null) {
            return false;
        }
        List<Track> tracks = getListTrack();
        if (tracks == null) {
            tracks = new ArrayList<>();
            tracks.add(track);
            SharePreferences.getInstance().putListTrack(new Gson().toJson(tracks));
            return true;
        } else {
            for (Track trackTemp : tracks) {
                if (track.getUri().equals(trackTemp.getUri())) {
                    return false;
                }
            }
            tracks.add(track);
            SharePreferences.getInstance().putListTrack(new Gson().toJson(tracks));
        }
        return true;
    }

    public List<Track> getListTrack() {
        String tracks = mSharedPreferences.getString(LIST_TRACK, null);
        Type listType = new TypeToken<ArrayList<Track>>() {
        }.getType();
        return new Gson().fromJson(tracks, listType);
    }

    public void putListTrack(String tracks) {
        mSharedPreferences.edit().putString(LIST_TRACK, tracks).apply();
    }

    public void putIndex(int index) {
        mSharedPreferences.edit().putInt(INDEX, index).apply();
    }

    public void putSuff(boolean shuff) {
        mSharedPreferences.edit().putBoolean(SHUFFLE, shuff).apply();
    }

    public void putRepeat(boolean repaet) {
        mSharedPreferences.edit().putBoolean(REPEAT, repaet).apply();
    }

    public int getIndex() {
        return mSharedPreferences.getInt(INDEX, Constant.INDEX_DEFAULTE);
    }

    public boolean getSuff() {
        return mSharedPreferences.getBoolean(SHUFFLE, false);
    }

    public boolean getREpeat() {
        return mSharedPreferences.getBoolean(REPEAT, false);
    }

    public void putGenre(String genre) {
        mSharedPreferences.edit().putString(GENRE, genre).apply();
    }

    public String getGenre() {
        return mSharedPreferences.getString(GENRE, "");
    }

    public List<User> getUser() {
        String tracks = mSharedPreferences.getString(OBJECT, null);
        Type listType = new TypeToken<List<User>>() {
        }.getType();
        return new Gson().fromJson(tracks, listType);
    }

    public User getUserLogin(String email, String pass) {
        List<User> users = getUser();
        if (users == null) {
            return null;
        }
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email) && user.getPass().equalsIgnoreCase(pass)) {
                setLogin(true);
                return user;
            }
        }
        return null;
    }

    public void putsUser(String users) {
        mSharedPreferences.edit().putString(OBJECT, users).apply();
    }

    public int addUser(String email, String pass) {
        if (email == null || pass == null) {
            return -1;
        }
        User user = new User(email, pass);
        List<User> userList = getUser();
        if (userList == null) {
            userList = new ArrayList<>();
            userList.add(user);
            SharePreferences.getInstance().putsUser(new Gson().toJson(userList));
            return 0;
        } else {
            for (User user1 : userList) {
                if (user1.getEmail().equalsIgnoreCase(email)) {
                    setLogin(true);
                    return 1;
                }
            }
            userList.add(user);
            SharePreferences.getInstance().putsUser(new Gson().toJson(userList));
            return 0;
        }
    }
}
