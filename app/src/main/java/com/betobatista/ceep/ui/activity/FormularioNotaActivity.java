package com.betobatista.ceep.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.betobatista.ceep.R;
import com.betobatista.ceep.model.Nota;

import static com.betobatista.ceep.ui.activity.NotaActivitiesConstants.CHAVE_POSICAO;
import static com.betobatista.ceep.ui.activity.NotaActivitiesConstants.CHAVE_NOTA;
import static com.betobatista.ceep.ui.activity.NotaActivitiesConstants.POSICAO_INVALIDA;

public class FormularioNotaActivity extends AppCompatActivity {


    public static final String TITLE_APPBAR_INSERE = "Insere nota";
    public static final String TITLE_APPBAR_ALTERA = "Altera nota";
    private int posicao = POSICAO_INVALIDA;
    private TextView titulo;
    private TextView descricao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_nota);

        setTitle(TITLE_APPBAR_INSERE);
        inicializaCampos();

        Intent intent = getIntent();
        if (intent.hasExtra(CHAVE_NOTA)) {
            setTitle(TITLE_APPBAR_ALTERA);
            Nota nota = (Nota) intent.getSerializableExtra(CHAVE_NOTA);
            preencheCampos(nota);

            posicao = intent.getIntExtra(CHAVE_POSICAO, POSICAO_INVALIDA);
        }
    }

    private void preencheCampos(Nota nota) {
        titulo.setText(nota.getTitulo());
        descricao.setText(nota.getDescricao());
    }

    private void inicializaCampos() {
        titulo = findViewById(R.id.formulario_nota_titulo);
        descricao = findViewById(R.id.formulario_nota_descricao);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_formulario_nota_salva, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (isMenuSalvaNota(item)) {
            Nota nota = criaNota();
            retornaNota(nota);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void retornaNota(Nota nota) {
        Intent intent = new Intent();
        intent.putExtra(CHAVE_NOTA, nota);
        intent.putExtra(CHAVE_POSICAO, posicao);
        setResult(Activity.RESULT_OK, intent);
    }

    private Nota criaNota() {
        return new Nota(titulo.getText().toString(), descricao.getText().toString());
    }

    private boolean isMenuSalvaNota(@NonNull MenuItem item) {
        return item.getItemId() == R.id.menu_formulario_nota_ic_salva;
    }
}