package com.em_and_ei.company.librarycourse.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.em_and_ei.company.librarycourse.enviroment.BooksProvider;
import com.em_and_ei.company.librarycourse.enviroment.Variables;
import com.em_and_ei.company.librarycourse.models.Book;
import com.em_and_ei.company.librarycourse.models.SharedPreferencesSingleton;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.List;

public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

}