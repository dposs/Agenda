package br.com.alura.agenda.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import br.com.alura.agenda.converter.AlunoConverter;
import br.com.alura.agenda.model.Aluno;
import br.com.alura.agenda.util.WebClient;

public class SendAlunosTask extends AsyncTask<Aluno, Void, String> {

    private Context context;
    private ProgressDialog dialog;

    public SendAlunosTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        dialog = ProgressDialog.show(context, "Envio de Alunos", "Os Alunos estão sendo enviados. Por favor aguarde a conclusão.", true, true);
    }

    @Override
    protected String doInBackground(Aluno[] alunos) {
        AlunoConverter converter = new AlunoConverter();
        String alunosJSON = converter.toJSON(Arrays.asList(alunos));

        WebClient webClient = new WebClient();
        return webClient.post(alunosJSON);
    }

    @Override
    protected void onPostExecute(String result) {
        dialog.dismiss();
        Toast.makeText(context, result, Toast.LENGTH_LONG).show();
    }
}
