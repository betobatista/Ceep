package com.betobatista.ceep.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.betobatista.ceep.R;
import com.betobatista.ceep.dao.NotaDAO;
import com.betobatista.ceep.model.Nota;
import com.betobatista.ceep.ui.recyclerview.adapter.ListaNotasAdapter;

import java.util.List;

import static com.betobatista.ceep.ui.activity.NotaActivitiesConstants.NOTA;
import static com.betobatista.ceep.ui.activity.NotaActivitiesConstants.REQUEST_CODE;
import static com.betobatista.ceep.ui.activity.NotaActivitiesConstants.RESULT_CODE;

public class ListaNotasActivity extends AppCompatActivity {


    private ListaNotasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);

        List<Nota> todasNotas = pegarTodasNotas();
        configuraRecyclerView(todasNotas);

        configuraBotaoInsereNota();
    }

    private void configuraBotaoInsereNota() {
        TextView insereNota = findViewById(R.id.lista_notas_insere_nota);
        insereNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vaiParaFormularioNotaActivity();
            }
        });
    }

    private void vaiParaFormularioNotaActivity() {
        Intent intent = new Intent(ListaNotasActivity.this, FormularioNotaActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    private List<Nota> pegarTodasNotas() {
        NotaDAO dao = new NotaDAO();
        return dao.todos();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (isResultadoComNota(requestCode, resultCode, data)) {
            Nota nota = (Nota) data.getSerializableExtra(NOTA);
            adiciona(nota);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void adiciona(Nota nota) {
        new NotaDAO().insere(nota);
        adapter.adiciona(nota);
    }

    private boolean isResultadoComNota(int requestCode, int resultCode, @Nullable Intent data) {
        return isCodigoRequisicao(requestCode) && isCodigoResultado(resultCode) && isNota(data);
    }

    boolean isNota(@Nullable Intent data) {
        return data.hasExtra(NOTA);
    }

    boolean isCodigoResultado(int resultCode) {
        return resultCode == RESULT_CODE;
    }

    boolean isCodigoRequisicao(int requestCode) {
        return requestCode == REQUEST_CODE;
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void configuraRecyclerView(List<Nota> todasNotas) {
        RecyclerView listaNotas = findViewById(R.id.lista_notas_recyclerview);
        configuraAdapter(todasNotas, listaNotas);
    }

    private void configuraAdapter(List<Nota> todasNotas, RecyclerView listaNotas) {
        adapter = new ListaNotasAdapter(this, todasNotas);
        listaNotas.setAdapter(adapter);
    }
}