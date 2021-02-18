package com.Dicta.DictaMaths;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Deconnexion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Boolean vider = deleteFile("users5.txt");
        setContentView(R.layout.activity_main);
    }
}