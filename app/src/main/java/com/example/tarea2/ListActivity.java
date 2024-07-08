package com.example.tarea2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import model.ResponseAPIJson;

public class ListActivity extends AppCompatActivity {

    private ListView lista;
    private ArrayList<ResponseAPIJson> listJson = new ArrayList<>();
    private Button nextAcitivty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        lista = findViewById(R.id.lista);
        nextAcitivty = findViewById(R.id.btnAtras);

        nextAcitivty.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            if(intent.resolveActivity(getPackageManager()) != null){
                startActivity(intent);
            }
        });

        getAllPosts();
    }


    public void getAllPosts() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://jsonplaceholder.typicode.com/posts";
        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    Type listType = new TypeToken<ArrayList<ResponseAPIJson>>() {}.getType();
                    ArrayList<ResponseAPIJson> posts = new Gson().fromJson(response, listType);
                    listJson.addAll(posts);
                    ArrayList<String> listConvert = (ArrayList<String>) listJson.stream()
                            .map(x -> "Titulo: " + x.getTitle() + "\n\n Cuerpo: " + x.getBody())
                            .collect(Collectors.toList());
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listConvert);
                    lista.setAdapter(adapter);
                }, error -> {
            Toast.makeText(this, "Error al obtener los posts", Toast.LENGTH_SHORT).show();
        });
        queue.add(request);
    }

}