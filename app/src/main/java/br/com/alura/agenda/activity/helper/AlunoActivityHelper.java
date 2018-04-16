package br.com.alura.agenda.activity.helper;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import br.com.alura.agenda.R;
import br.com.alura.agenda.activity.AlunoActivity;
import br.com.alura.agenda.model.Aluno;

public class AlunoActivityHelper {

    private Aluno aluno;

    private EditText campoNome;
    private EditText campoEndereco;
    private EditText campoTelefone;
    private EditText campoSite;
    private RatingBar campoNota;
    private ImageView campoFoto;

    public AlunoActivityHelper(AlunoActivity activity) {
        this.aluno = new Aluno();

        campoNome = activity.findViewById(R.id.aluno_nome);
        campoEndereco = activity.findViewById(R.id.aluno_endereco);
        campoTelefone = activity.findViewById(R.id.aluno_telefone);
        campoSite = activity.findViewById(R.id.aluno_site);
        campoNota = activity.findViewById(R.id.aluno_nota);
        campoFoto = activity.findViewById(R.id.aluno_foto);
    }

    public Aluno getAluno() {
        aluno.setNome(campoNome.getText().toString());
        aluno.setEndereco(campoEndereco.getText().toString());
        aluno.setTelefone(campoTelefone.getText().toString());
        aluno.setSite(campoSite.getText().toString());
        aluno.setNota(Double.valueOf(campoNota.getProgress()));
        aluno.setCaminhoFoto((String) campoFoto.getTag());

        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;

        campoNome.setText(aluno.getNome());
        campoEndereco.setText(aluno.getEndereco());
        campoTelefone.setText(aluno.getTelefone());
        campoSite.setText(aluno.getSite());
        campoNota.setProgress(aluno.getNota().intValue());

        if (aluno.getCaminhoFoto() != null && !aluno.getCaminhoFoto().isEmpty()) {
            this.setImage(aluno.getCaminhoFoto());
        }
    }

    public void setImage(Uri uri) {
        campoFoto.setImageURI(null);
        campoFoto.setImageURI(uri);
        campoFoto.setTag(uri.getPath());
    }

    public void setImage(String path) {
        campoFoto.setImageBitmap(BitmapFactory.decodeFile(path));
        campoFoto.setTag(path);
    }
}
