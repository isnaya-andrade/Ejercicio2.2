package com.example.tarea2;

import static java.lang.Integer.parseInt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.stream.Collectors;

import model.ResponseAPIJson;

public class MainActivity extends AppCompatActivity {

    private TextView txtId;
    private Button btnSalvar, nextAcitivty;
    private ListView lista;
    private ArrayList<ResponseAPIJson> listJson = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        txtId = findViewById(R.id.editTextText);
        nextAcitivty = findViewById(R.id.btnActivity);
        btnSalvar = findViewById(R.id.btnSalvar);
        lista = findViewById(R.id.listView);
        btnSalvar.setOnClickListener(v -> {
            if(txtId.getText().toString().isEmpty()){
                txtId.setError("Ingrese un id");
                return;
            }
            getById(parseInt(txtId.getText().toString()));
        });
        nextAcitivty.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListActivity.class);
            if(intent.resolveActivity(getPackageManager()) != null){
                startActivity(intent);
            }
        });
    }

    public void getById(Integer id){
        if(id != null && id != 0){
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "https://jsonplaceholder.typicode.com/posts/" + id;
            StringRequest request = new StringRequest(Request.Method.GET, url,
                    response -> {
                        ResponseAPIJson content = new Gson().fromJson(response, ResponseAPIJson.class);
                        listJson.add(content);
                        ArrayList<String> listConvert = (ArrayList<String>) listJson.stream().map(x -> "Titulo: "+x.getTitle() + "\n\n Cuerpo: " + x.getBody()).collect(Collectors.toList());
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listConvert);
                        lista.setAdapter(adapter);
                    }, error ->{
                        txtId.setError("Error al obtener el id");
                    }
            );
            queue.add(request);
        }else{
            txtId.setError("Ingrese un Id que no sea 0.");
        }
    }
}