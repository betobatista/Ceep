package com.betobatista.ceep.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.betobatista.ceep.R;
import com.betobatista.ceep.dao.NotaDAO;
import com.betobatista.ceep.model.Nota;
import com.betobatista.ceep.ui.recyclerview.adapter.ListaNotasAdapter;
import com.betobatista.ceep.ui.recyclerview.adapter.listener.OnItemClickListener;
import com.betobatista.ceep.ui.recyclerview.helper.callback.NotaItemTouchHelperCallback;

import java.util.List;

import static com.betobatista.ceep.ui.activity.NotaActivitiesConstants.CHAVE_POSICAO;
import static com.betobatista.ceep.ui.activity.NotaActivitiesConstants.CHAVE_NOTA;
import static com.betobatista.ceep.ui.activity.NotaActivitiesConstants.POSICAO_INVALIDA;
import static com.betobatista.ceep.ui.activity.NotaActivitiesConstants.REQUEST_CODE_INSERIR;
import static com.betobatista.ceep.ui.activity.NotaActivitiesConstants.REQUEST_CODE_ALTERAR;

public class ListaNotasActivity extends AppCompatActivity {


    public static final String TITLE_APPBAR = "Notas";
    private ListaNotasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);

        setTitle(TITLE_APPBAR);

        List<Nota> todasNotas = pegarTodasNotas();
        configuraRecyclerView(todasNotas);

        configuraBotaoInsereNota();
    }

    private void configuraBotaoInsereNota() {
        TextView insereNota = findViewById(R.id.lista_notas_insere_nota);
        insereNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vaiParaFormularioNotaActivityInsere();
            }
        });
    }

    private void vaiParaFormularioNotaActivityInsere() {
        Intent intent = new Intent(ListaNotasActivity.this, FormularioNotaActivity.class);
        startActivityForResult(intent, REQUEST_CODE_INSERIR);
    }

    private List<Nota> pegarTodasNotas() {
        NotaDAO dao = new NotaDAO();
        return dao.todos();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (isResultadoNotaSalva(requestCode, data)) {
            if(isCodigoResultado(resultCode)) {
                Nota nota = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                adiciona(nota);
            }
        }
        if (isResultadoNotaAlterada(requestCode, data)) {
            if(isCodigoResultado(resultCode)) {
                Nota nota = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                int posicao = data.getIntExtra(CHAVE_POSICAO, POSICAO_INVALIDA);
                if (isPosicaoValida(posicao)) {
                    altera(nota, posicao);
                }
            }
        }
    }

    private void altera(Nota nota, int posicao) {
        new NotaDAO().altera(posicao, nota);
        adapter.altera(posicao, nota);
    }

    private boolean isPosicaoValida(int posicao) {
        return posicao > POSICAO_INVALIDA;
    }

    private boolean isResultadoNotaAlterada(int requestCode, @Nullable Intent data) {
        return requestCode == 2 &&  isNota(data) && data != null && data.hasExtra(CHAVE_POSICAO);
    }

    private void adiciona(Nota nota) {
        new NotaDAO().insere(nota);
        adapter.adiciona(nota);
    }

    private boolean isResultadoNotaSalva(int requestCode, @Nullable Intent data) {
        return isCodigoRequisicao(requestCode) && isNota(data);
    }

    boolean isNota(@Nullable Intent data) {
        return data != null && data.hasExtra(CHAVE_NOTA);
    }

    boolean isCodigoResultado(int resultCode) {
        return resultCode == Activity.RESULT_OK;
    }

    boolean isCodigoRequisicao(int requestCode) {
        return requestCode == REQUEST_CODE_INSERIR;
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void configuraRecyclerView(List<Nota> todasNotas) {
        RecyclerView listaNotas = findViewById(R.id.lista_notas_recyclerview);
        configuraAdapter(todasNotas, listaNotas);
        configuraItemTouchHelper(listaNotas);
    }

    private void configuraItemTouchHelper(RecyclerView listaNotas) {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new NotaItemTouchHelperCallback(adapter));
        itemTouchHelper.attachToRecyclerView(listaNotas);
    }

    private void configuraAdapter(List<Nota> todasNotas, RecyclerView listaNotas) {
        adapter = new ListaNotasAdapter(this, todasNotas);
        listaNotas.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Nota nota, int position) {
                vaiParaFormularioNotaActivityAlterar(nota, position);
            }
        });
    }

    private void vaiParaFormularioNotaActivityAlterar(Nota nota, int position) {
        Intent intent = new Intent(ListaNotasActivity.this, FormularioNotaActivity.class);
        intent.putExtra(CHAVE_NOTA, nota);
        intent.putExtra(CHAVE_POSICAO, position);
        startActivityForResult(intent, REQUEST_CODE_ALTERAR);
    }
}