package com.example.retrofittodos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarActivity extends AppCompatActivity {
    EditText userId, id, title, completed;
    TodosService tdService;
    String userIdText, idText, titleText, completedText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);
        Intent intent = getIntent();

        userIdText = intent.getStringExtra("userId");
        idText = intent.getStringExtra("id");
        titleText = intent.getStringExtra("title");
        completedText = intent.getStringExtra("completed");

        userId = findViewById(R.id.editUserIdEditar);
        userId.setText(userIdText);

        id = findViewById(R.id.editIdEditar);
        id.setText(idText);

        title = findViewById(R.id.editTitleEditar);
        title.setText(titleText);

        completed = findViewById(R.id.editCompletedEditar);
        completed.setText(completedText);


    }

    public void listenerEditar(View view){
        Todos todos = new Todos();
        todos.setId(Integer.parseInt(id.getText().toString()));
        Integer alteracoesFeitas = 0;

        if(!userIdText.equals(userId.getText().toString())){
            System.out.println("PASSOU COMPARACAO");
            todos.setUserId(Integer.parseInt(userId.getText().toString()));
            alteracoesFeitas++;
        }
        if(!titleText.equals(title.getText().toString())){
            todos.setTitle(title.getText().toString());
            alteracoesFeitas++;
        }
        if(!completedText.equals(completed.getText().toString())){
            todos.setCompleted(Boolean.parseBoolean(completed.getText().toString()));
            alteracoesFeitas++;
        }

        tdService = Api.getClient().create(TodosService.class);

        //Verifica se é necessario PUT ou PATCH de acordo com os campos alterados
        if(alteracoesFeitas == 3){
            System.out.println("passou put");
            //Caso todos os campos forem alterados
            Call<Todos> put = tdService.put(todos.getId(), todos);

            put.enqueue(new Callback<Todos>() {
                @Override
                public void onResponse(Call<Todos> call, Response<Todos> response) {
                    System.out.println("resposta: " + response);
                    Toast toast = Toast.makeText(getApplicationContext(), "Atividade adicionada com sucesso!", Toast.LENGTH_LONG);
                    toast.show();
                    finish();
                }

                @Override
                public void onFailure(Call<Todos> call, Throwable t) {
                    System.out.println("Erro: " + call);
                }
            });
        }else{
            System.out.println("passou patch");
            //Caso apenas alguns campos selecionados forem alterados
            Call<Todos> patch = tdService.patch(todos.getId(), todos);

            patch.enqueue(new Callback<Todos>() {
                @Override
                public void onResponse(Call<Todos> call, Response<Todos> response) {
                    Todos todos = response.body();
                    System.out.println("resposta: " + response);
                    Toast toast = Toast.makeText(getApplicationContext(), "Atividade editada com sucesso!", Toast.LENGTH_LONG);
                    toast.show();
                    finish();
                }

                @Override
                public void onFailure(Call<Todos> call, Throwable t) {
                    System.out.println("Erro: " + call);

                }
            });
        }
    }
}
