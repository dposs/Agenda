package br.com.alura.agenda.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

import br.com.alura.agenda.R;
import br.com.alura.agenda.model.Prova;

public class ProvasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provas);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        List<String> topicosMatematica = Arrays.asList("Equações de 2º Grau", "Trigonometria");
        Prova provaMatematica = new Prova("Matemática", "25/05/2018", topicosMatematica);

        List<String> topicosPortugues = Arrays.asList("Sujeito", "Objeto direto", "Objeto indireto");
        Prova provaPortugues = new Prova("Português", "05/06/2018", topicosPortugues);

        List<Prova> provas = Arrays.asList(provaMatematica, provaPortugues);

        ArrayAdapter<Prova> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, provas);

        ListView lvProvas = findViewById(R.id.provas_list);
        lvProvas.setAdapter(adapter);

        lvProvas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Prova prova = (Prova) parent.getItemAtPosition(position);

                Intent intentProva = new Intent(ProvasActivity.this, ProvaActivity.class);
                intentProva.putExtra("prova", prova);
                startActivity(intentProva);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
