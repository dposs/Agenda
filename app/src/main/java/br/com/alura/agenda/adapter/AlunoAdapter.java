package br.com.alura.agenda.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import br.com.alura.agenda.R;
import br.com.alura.agenda.model.Aluno;

public class AlunoAdapter extends BaseAdapter {

    private Context context;
    private List<Aluno> alunos;

    public AlunoAdapter(Context context, List<Aluno> alunos) {
        this.context = context;
        this.alunos = alunos;
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
        View view = convertView != null ? convertView : inflater.inflate(R.layout.list_item_aluno, parent, false);

        TextView tvNome = view.findViewById(R.id.item_aluno_nome);
        tvNome.setText(aluno.getNome());

        TextView tvTelefone = view.findViewById(R.id.item_aluno_telefone);
        tvTelefone.setText(aluno.getTelefone());

        ImageView ivFoto = view.findViewById(R.id.item_aluno_foto);

        if (aluno.getCaminhoFoto() != null) {
            //Bitmap bitmap = BitmapFactory.decodeFile(aluno.getCaminhoFoto());
            //Bitmap bitmapReduzido = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
            //ivFoto.setImageBitmap(bitmapReduzido);
            ivFoto.setImageURI(Uri.fromFile(new File(aluno.getCaminhoFoto())));
            ivFoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

        return view;
    }
}
