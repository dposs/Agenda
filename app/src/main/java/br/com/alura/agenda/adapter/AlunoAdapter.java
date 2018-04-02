package br.com.alura.agenda.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import br.com.alura.agenda.R;
import br.com.alura.agenda.model.Aluno;

public class AlunoAdapter extends BaseAdapter {

    private Context context;
    private List<Aluno> alunos;
    private int emptyPicturePadding;

    public AlunoAdapter(Context context, List<Aluno> alunos) {
        this.context = context;
        this.alunos = alunos;

        float density = this.context.getResources().getDisplayMetrics().density;
        this.emptyPicturePadding = (int) (10 * density + 0.5f);
    }

    @Override
    public int getCount() {
        return alunos.size();
    }

    @Override
    public Object getItem(int position) {
        return alunos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return alunos.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Aluno aluno = alunos.get(position);

        LayoutInflater inflater = LayoutInflater.from(context);

        // Reuse View
        // View view = convertView != null ? convertView : inflater.inflate(R.layout.list_item_aluno, parent, false);

        View view = inflater.inflate(R.layout.list_item_aluno, parent, false);

        LinearLayout layout = view.findViewById(R.id.item_aluno_layout);
        TextView tvNome = view.findViewById(R.id.item_aluno_nome);
        TextView tvTelefone = view.findViewById(R.id.item_aluno_telefone);
        ImageView ivFoto = view.findViewById(R.id.item_aluno_foto);

        if (aluno.getNome() != null && !aluno.getNome().isEmpty()) {
            tvNome.setText(aluno.getNome());
        } else {
            layout.removeView(tvNome);
        }

        if (aluno.getTelefone() != null && !aluno.getTelefone().isEmpty()) {
            tvTelefone.setText(aluno.getTelefone());
        } else {
            layout.removeView(tvTelefone);
        }

        if (aluno.getCaminhoFoto() != null) {
            ivFoto.setImageURI(Uri.fromFile(new File(aluno.getCaminhoFoto())));
            ivFoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            ivFoto.setPadding(emptyPicturePadding, emptyPicturePadding, emptyPicturePadding, emptyPicturePadding);
        }

        return view;
    }
}
