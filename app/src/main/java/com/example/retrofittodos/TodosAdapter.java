package com.example.retrofittodos;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodosAdapter extends BaseAdapter {
    Context appContext;
    List<Todos> appCollection;
    LayoutInflater ltInflater;
    TextView userId, id, title, completed;
    Button remover, editar;
    TodosService tdService;

    public TodosAdapter(final Context appContext, final List<Todos> appCollection) {
        this.appContext = appContext;
        this.appCollection = appCollection;

    }

    @Override
    public int getCount() {
        return this.appCollection!=null ? this.appCollection.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return this.appCollection.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(appContext).inflate(R.layout.todo_item, parent, false);
        }
        final Todos todos = (Todos)getItem(position);

        userId = convertView.findViewById(R.id.txtUserId);
        id = convertView.findViewById(R.id.txtId);
        title = convertView.findViewById(R.id.txtTitle);
        completed = convertView.findViewById(R.id.txtCompleted);
        remover = convertView.findViewById(R.id.btnRemover);

        userId.setText(String.valueOf(todos.getUserId()));
        id.setText(String.valueOf(todos.getId()));
        title.setText(todos.getTitle());
        remover.setHint(String.valueOf(todos.getId()));

        if(todos.isCompleted()){
            completed.setText("Realizado");
            convertView.setBackgroundColor(Color.rgb(51, 181, 94));
        }else{
            completed.setText("Não realizado");
            convertView.setBackgroundColor(Color.rgb(51, 181, 94));
            int alpha = (int)(0.7 * 255.0f);
            convertView.setBackgroundColor(Color.argb(alpha, 189, 3, 3));
        }


        final Button buttonRemover = (Button) convertView.findViewById(R.id.btnRemover);
        buttonRemover.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                tdService = Api.getClient().create(TodosService.class);
                int deleteId = Integer.valueOf(String.valueOf((buttonRemover.getHint())));

                Call<Void> delete = tdService.delete(deleteId);
                delete.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        System.out.println("resposta: " + response);
                        Toast toast = Toast.makeText(appContext, "Atividade removida com sucesso!", Toast.LENGTH_LONG);
                        toast.show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        System.out.println("resposta: " + call);

                    }
                });

            }
        });

        editar = convertView.findViewById(R.id.btnEditar);
        editar.setHint(String.valueOf(todos.getId()));
        final Button buttonEditar = (Button) convertView.findViewById(R.id.btnEditar);
        buttonEditar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v) {
                tdService = Api.getClient().create(TodosService.class);
                int editarId = Integer.valueOf(String.valueOf((buttonEditar.getHint())));
                final Call<Todos> todoById = tdService.get(editarId);
                todoById.enqueue(new Callback<Todos>() {
                    @Override
                    public void onResponse(Call<Todos> call, Response<Todos> response) {
                        Todos todos =  response.body();
                        iniciarEdicao(v, todos);
                    }

                    @Override
                    public void onFailure(Call<Todos> call, Throwable t) {
                        System.out.println(call);
                    }
                });
//                intent.putExtra(EXTRA_MESSAGE, message) ;
            }
        });

        return convertView;
    }

    public void iniciarEdicao(View view, Todos td){
        Intent intent = new Intent(appContext, EditarActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("userId", String.valueOf(td.getUserId()));
        intent.putExtra("id", String.valueOf(td.getId()));
        intent.putExtra("title", td.getTitle());
        intent.putExtra("completed", String.valueOf(td.isCompleted()));
        view.getContext().startActivity(intent);
    }
}
