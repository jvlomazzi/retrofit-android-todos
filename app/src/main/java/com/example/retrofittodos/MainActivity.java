package com.example.retrofittodos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    TodosService tdService;
    ListView listView;
    EditText editUserId, editId, editTitle, editCompleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void actionButtonListeners(View view){
        Todos td = new Todos();
        tdService = Api.getClient().create(TodosService.class);

        switch (view.getId()){
            case R.id.btnListar:
                tdService = Api.getClient().create(TodosService.class);
                Call<List<Todos>> get = tdService.get();

                get.enqueue(new Callback<List<Todos>>() {
                    @Override
                    public void onResponse(Call<List<Todos>> call, Response<List<Todos>> response) {
                        List<Todos> todos = response.body();
                        TodosAdapter adapter = new TodosAdapter(getApplicationContext(), todos);
                        listView = findViewById(R.id.listView);
                        listView.setAdapter(adapter);
                    }

                    @Override
                    public void onFailure(Call<List<Todos>> call, Throwable t) {
                        System.out.println("resposta: " + call);
                    }
                });
                break;

            case R.id.btnAdicionar:
                editUserId = findViewById(R.id.editUserId);
                editId = findViewById(R.id.editId);
                editCompleted = findViewById(R.id.editCompleted);
                editTitle = findViewById(R.id.editTitle);

                td.setUserId(Integer.parseInt(editUserId.getText().toString()));
                td.setId(Integer.parseInt(editId.getText().toString()));
                td.setCompleted(Boolean.parseBoolean(editCompleted.getText().toString()));
                td.setTitle(editTitle.getText().toString());

                Call<Todos> post = tdService.post(td);

                post.enqueue(new Callback<Todos>() {
                    @Override
                    public void onResponse(Call<Todos> call, Response<Todos> response) {
                        System.out.println("resposta: " + response);
                        Toast toast = Toast.makeText(getApplicationContext(), "Atividade adicionada com sucesso!", Toast.LENGTH_LONG);
                        toast.show();
                    }

                    @Override
                    public void onFailure(Call<Todos> call, Throwable t) {
                        System.out.println("resposta: " + call);
                    }
                });
                break;
        }
    }

}