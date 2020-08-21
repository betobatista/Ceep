package com.betobatista.ceep.ui.recyclerview.helper.callback;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.betobatista.ceep.dao.NotaDAO;
import com.betobatista.ceep.ui.recyclerview.adapter.ListaNotasAdapter;

public class NotaItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final ListaNotasAdapter adapter;

    public NotaItemTouchHelperCallback(ListaNotasAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int marcacoesDeDeslize = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        int marcacoesDeArrastar = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        return makeMovementFlags(marcacoesDeArrastar, marcacoesDeDeslize);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        int postionInicial = viewHolder.getAdapterPosition();
        int positionFinal = target.getAdapterPosition();
        trocaNotas(postionInicial, positionFinal);
        return true;
    }

    private void trocaNotas(int postionInicial, int positionFinal) {
        new NotaDAO().troca(postionInicial, positionFinal);
        adapter.troca(postionInicial, positionFinal);
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        removeNota(viewHolder);
    }

    private void removeNota(@NonNull RecyclerView.ViewHolder viewHolder) {
        int position = viewHolder.getAdapterPosition();
        new NotaDAO().remove(position);
        adapter.remove(position);
    }
}
