package com.example.android.authenticationapp;

import static com.example.android.authenticationapp.R.color.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;

import com.airbnb.lottie.LottieAnimationView;
import com.example.android.authenticationapp.adapter.HomeMusicAdapter;
import com.example.android.authenticationapp.model.Music;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;

public class homeactivity extends AppCompatActivity implements HomeMusicAdapter.OnSongClickListener{

    private RecyclerView recyclerView;
    private HomeMusicAdapter homeMusicAdapter;
//    public ArrayList<Music> musicArrayList;
    private ArrayAdapter<String> arrayAdapter;
    ProgressDialog dialog;
    Toolbar toolbar;
    EditText searchField;
    ImageButton searchButton;
    public static LottieAnimationView play;
    private String musicNameToBeSearched;
    private FirebaseFirestore db;
    SlideInBottomAnimationAdapter animationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homeactivity);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        searchField=findViewById(R.id.searchBox);
        searchButton=findViewById(R.id.searchButton);
        recyclerView = findViewById(R.id.recyclerView);
        play=findViewById(R.id.animation);
        String input=searchField.getText().toString();
        Utils.statusBarcolor(homeactivity.this,R.color.light);

        Utils.statusBarcolor(homeactivity.this, light);
        db = FirebaseFirestore.getInstance();
//        musicArrayList = new ArrayList<Music>();
        dialog = new ProgressDialog(homeactivity.this);
        dialog.setMessage("Loading Songs...");
        dialog.show();


        class fetchSongs extends AsyncTask<Void, Integer, Long> {
            protected Long doInBackground(Void... voids) {
                new FetchSongs().getSongs(AllSongs.musicArrayList);

                return 1l;
            }

            protected void onProgressUpdate(Integer... progress) {

            }


            protected void onPostExecute(Long result) {
                dialog.dismiss();
                homeMusicAdapter.setNewList(AllSongs.musicArrayList);
                Log.d("size","size in home activity is "+AllSongs.musicArrayList.size());
            }
        }



//      Initialize RecyclerView
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(homeactivity.this));

//      recyclerview animation adapter
        homeMusicAdapter = new HomeMusicAdapter(homeactivity.this, AllSongs.musicArrayList, homeactivity.this::onSongClick);
        animationAdapter=new SlideInBottomAnimationAdapter(homeMusicAdapter);
        animationAdapter.setDuration(900);
        animationAdapter.setInterpolator(new AccelerateDecelerateInterpolator());
        animationAdapter.setFirstOnly(false);


//        Using RecyclerView

        recyclerView.setAdapter(animationAdapter);
        new fetchSongs().execute();

        //        Sample Music
//        Music music = new Music("tu aake dekh le", "2:39");
//        musicArrayList.add(music);
//        Music music2 = new Music("Song Name 2", "3:33");
//        musicArrayList.add(music2);


//        Search Button OnClickListener Implementation

        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(input.isEmpty())
                {
                    recyclerView.setAdapter(homeMusicAdapter);
                }
            }
        });


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchMusic(AllSongs.musicArrayList);
            }
        });
    }

    private void searchMusic(ArrayList<Music> arr) {

        musicNameToBeSearched=searchField.getText().toString();
            if(musicNameToBeSearched.isEmpty())
            {
                recyclerView.setAdapter(animationAdapter);
            }
        else {
                Log.d("ved", "" + musicNameToBeSearched);
                ArrayList<Music> songs = new ArrayList<Music>();
                HomeMusicAdapter filteredSongs = new HomeMusicAdapter(homeactivity.this, songs);
                for (Music music : arr) {
                    if (music.getMusicName() != null && music.getMusicName().contains(musicNameToBeSearched)) {
                        songs.add(music);
                    }
                }
                SlideInBottomAnimationAdapter animationAdapter=new SlideInBottomAnimationAdapter(filteredSongs);
                animationAdapter.setDuration(800);
                animationAdapter.setInterpolator(new AccelerateDecelerateInterpolator());
                animationAdapter.setFirstOnly(false);
                recyclerView.setAdapter(animationAdapter);
            }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int item_id = item.getItemId();

        if(item_id==R.id.detect_emotion_menu) {
            Intent intent = new Intent(homeactivity.this, CameraActivity.class);
            startActivity(intent);
        }

        else if(item_id == R.id.settings_menu) {

        }

        return true;
    }

    @Override
    public void onSongClick(int position, LottieAnimationView view) {
        Music music = AllSongs.musicArrayList.get(position);
//        String url = music.getMusicLink();
//        String thumbnail = music.getThumbnail();
        Intent intent = new Intent(this, MusicScreenActivity.class);
        Bundle args = new Bundle();
//        args.putSerializable("arraylist",(Serializable)AllSongs.musicArrayList);
        args.putInt("position",position);
        intent.putExtra("BUNDLE",args);
        startActivity(intent);

//        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mediaPlayer) {
//                view.setVisibility(View.GONE);
//            }
//        });
    }

}