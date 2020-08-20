package com.betobatista.ceep.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.betobatista.ceep.R;
import com.betobatista.ceep.dao.NotaDAO;
import com.betobatista.ceep.model.Nota;

import java.io.Serializable;

import static com.betobatista.ceep.ui.activity.NotaActivitiesConstants.NOTA;
import static com.betobatista.ceep.ui.activity.NotaActivitiesConstants.RESULT_CODE;

public class FormularioNotaActivity extends AppCompatActivity {


    private int posicao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_nota);

        Intent intent = getIntent();
        if(intent.hasExtra(NOTA) && intent.hasExtra("posicao")){
            Nota nota = (Nota) intent.getSerializableExtra(NOTA);
            TextView titulo = findViewById(R.id.formulario_nota_titulo);
            titulo.setText(nota.getTitulo());
            TextView descricao = findViewById(R.id.formulario_nota_descricao);
            descricao.setText(nota.getDescricao());

            posicao = intent.getIntExtra("posicao", -1);
        }
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
        intent.putExtra(NOTA, nota);
        intent.putExtra("posicao", posicao);
        setResult(RESULT_CODE, intent);
    }

    private Nota criaNota() {
        EditText titulo = findViewById(R.id.formulario_nota_titulo);
        EditText descricao = findViewById(R.id.formulario_nota_descricao);
        return new Nota(titulo.getText().toString(), descricao.getText().toString());
    }

    private boolean isMenuSalvaNota(@NonNull MenuItem item) {
        return item.getItemId() == R.id.menu_formulario_nota_ic_salva;
    }
}