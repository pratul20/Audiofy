package com.example.android.authenticationapp;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.android.authenticationapp.model.Music;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class FetchSongs {

    public void getSongs(ArrayList<Music> musicArrayList) {
        ArrayList<String> songIds = new ArrayList<>();
        FirebaseFirestore db =FirebaseFirestore.getInstance();


        db.collection("Music").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String songId = document.getId();
//                        songIds.add(songId);
//                        Log.d("ids", "id is "+songId);
                        DocumentReference docRef = db.collection("Music").document(songId);
                        Log.d("docs", "doc fetched - "+songId);
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Log.d("details", "DocumentSnapshot data: " + document.getData());
                                        String musicId = (String) document.getData().get("musicId");
                                        String musicLink = (String) document.getData().get("musicLink");
                                        String musicType = (String) document.getData().get("musicType");
                                        String musicName = (String) document.getData().get("musicName");
                                        String musicDuration = (String) document.getData().get("musicDuration");
                                        String thumbnail = (String) document.getData().get("thumbnail");
                                        musicArrayList.add(new Music(musicId,musicName,musicType,musicDuration,thumbnail,musicLink,false));
                                        Log.d("size","size is "+musicArrayList.size());
                                    } else {
                                        Log.d("error", "No such document");
                                    }
                                } else {
                                    Log.d("error", "get failed with ", task.getException());
                                }
                            }
                        });

                    }
                } else {
                    Log.d("error", "Error getting documents: ", task.getException());
                }
            }

        });


//        Log.d("size","size is "+songIds.size());
//
//        for(int i=0;i<songIds.size();i++) {
//            String id = songIds.get(i);
//            DocumentReference docRef = db.collection("Music").document(id);
//            Log.d("docs", "doc fetched - "+id);
//            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                    if (task.isSuccessful()) {
//                        DocumentSnapshot document = task.getResult();
//                        if (document.exists()) {
//                            Log.d("details", "DocumentSnapshot data: " + document.getData());
//                            String musicId = (String) document.getData().get("musicId");
//                            String musicLink = (String) document.getData().get("musicLink");
//                            String musicType = (String) document.getData().get("musicType");
//                            String musicName = (String) document.getData().get("musicName");
//                            String musicDuration = (String) document.getData().get("musicDuration");
//                            String thumbnail = (String) document.getData().get("thumbnail");
//                            musicArrayList.add(new Music(musicId,musicName,musicType,musicDuration,thumbnail,musicLink));
//                        } else {
//                            Log.d("error", "No such document");
//                        }
//                    } else {
//                        Log.d("error", "get failed with ", task.getException());
//                    }
//                }
//            });
//
//        }
    }

}
