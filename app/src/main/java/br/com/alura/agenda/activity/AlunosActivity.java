package br.com.alura.agenda.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import br.com.alura.agenda.R;
import br.com.alura.agenda.activity.util.RequestCode;
import br.com.alura.agenda.adapter.AlunoAdapter;
import br.com.alura.agenda.dao.AlunoDAO;
import br.com.alura.agenda.factory.SnackbarFactory;
import br.com.alura.agenda.model.Aluno;
import br.com.alura.agenda.task.SendAlunosTask;

public class AlunosActivity extends AppCompatActivity {

    private AlunoDAO alunoDAO;

    private Aluno selectedAluno;

    private CoordinatorLayout layout;
    private ListView list;
    private FloatingActionButton fabCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_alunos);

        layout = findViewById(R.id.alunos_layout);
        list = findViewById(R.id.alunos_list);
        fabCreate = findViewById(R.id.alunos_fab_create);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> list, View item, int position, long id) {
                Aluno aluno = (Aluno) AlunosActivity.this.list.getItemAtPosition(position);

                Intent intentForm = new Intent(AlunosActivity.this, AlunoActivity.class);
                intentForm.putExtra("aluno", aluno);
                startActivityForResult(intentForm, RequestCode.EDIT_ALUNO);
            }
        });

        fabCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentFormAluno = new Intent(AlunosActivity.this, AlunoActivity.class);
                startActivityForResult(intentFormAluno, RequestCode.CREATE_ALUNO);
            }
        });

        requestPermissions();

        registerForContextMenu(list);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAlunos();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_alunos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_alunos_enviar:
                AlunoDAO dao = new AlunoDAO(this);
                List<Aluno> alunos = dao.getAll();
                dao.close();

                new SendAlunosTask(this).execute(alunos.toArray(new Aluno[]{}));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        selectedAluno = (Aluno) list.getItemAtPosition(info.position);

        if (selectedAluno.getTelefone() != null && !selectedAluno.getTelefone().isEmpty()) {
            MenuItem itemLigar = menu.add("Ligar");
            itemLigar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    callAluno(selectedAluno);
                    return false;
                }
            });

            MenuItem itemSMS = menu.add("Enviar SMS");
            Intent intentSMS = new Intent(Intent.ACTION_VIEW);
            intentSMS.setData(Uri.parse("sms:" + selectedAluno.getTelefone()));
            itemSMS.setIntent(intentSMS);
        }

        if (selectedAluno.getEndereco() != null && !selectedAluno.getEndereco().isEmpty()) {
            MenuItem itemMapa = menu.add("Visualizar no mapa");
            Intent intentMapa = new Intent(Intent.ACTION_VIEW);
            intentMapa.setData(Uri.parse("geo:0,0?q=" + selectedAluno.getEndereco()));
            itemMapa.setIntent(intentMapa);
        }

        if (selectedAluno.getSite() != null && !selectedAluno.getSite().isEmpty()) {
            String site = selectedAluno.getSite().startsWith("http") ? selectedAluno.getSite() : "http://" + selectedAluno.getSite();

            MenuItem itemSite = menu.add("Visitar site");
            Intent intentSite = new Intent(Intent.ACTION_VIEW);
            intentSite.setData(Uri.parse(site));
            itemSite.setIntent(intentSite);
        }

        MenuItem itemDeletar = menu.add("Deletar");
        itemDeletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                deleteAluno(selectedAluno);
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Aluno aluno;

            switch (requestCode) {
                case RequestCode.CREATE_ALUNO:
                    aluno = (Aluno) data.getSerializableExtra("aluno");
                    SnackbarFactory.create(AlunosActivity.this, layout, "Aluno(a) " + aluno.getNome() + " cadastrado(a) com sucesso.").show();
                    break;

                case RequestCode.EDIT_ALUNO:
                    aluno = (Aluno) data.getSerializableExtra("aluno");
                    SnackbarFactory.create(AlunosActivity.this, layout, "Aluno(a) " + aluno.getNome() + " alterado(a) com sucesso.").show();
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) throws SecurityException {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == RequestCode.REQUEST_CALL_PHONE_PERMISSION) {
            if (permissions[0].equals(Manifest.permission.CALL_PHONE) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callAluno(selectedAluno);
            }
        }
    }

    /**
     * Carrega a lista de Alunos.
     */
    private void loadAlunos() {
        alunoDAO = new AlunoDAO(this);
        List<Aluno> alunos = alunoDAO.getAll();
        alunoDAO.close();

        AlunoAdapter adapter = new AlunoAdapter(this, alunos);
        list.setAdapter(adapter);
    }

    /**
     * Realiza uma chamada Telefonica para o Aluno.
     *
     * @param aluno
     */
    private void callAluno(Aluno aluno) {
        int callPhonePermission = ActivityCompat.checkSelfPermission(AlunosActivity.this, Manifest.permission.CALL_PHONE);

        if (callPhonePermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AlunosActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    RequestCode.REQUEST_CALL_PHONE_PERMISSION);

            return;
        }

        Intent intentLigar = new Intent(Intent.ACTION_CALL);
        intentLigar.setData(Uri.parse("tel:" + aluno.getTelefone()));
        startActivity(intentLigar);
    }

    /**
     * Exclui o Aluno.
     *
     * @param aluno
     */
    private void deleteAluno(Aluno aluno) {
        alunoDAO = new AlunoDAO(AlunosActivity.this);
        alunoDAO.delete(aluno);
        alunoDAO.close();

        loadAlunos();

        SnackbarFactory.create(AlunosActivity.this, layout, "Aluno(a) " + selectedAluno.getNome() + " removido(a) com sucesso.").show();
    }

    private void requestPermissions() {
        int receiveSMSPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);

        if (receiveSMSPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECEIVE_SMS},
                    RequestCode.REQUEST_RECEIVE_SMS_PERMISSION);

            return;
        }
    }
}
