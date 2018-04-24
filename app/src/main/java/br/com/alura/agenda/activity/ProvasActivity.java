package br.com.alura.agenda.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

import br.com.alura.agenda.R;
import br.com.alura.agenda.factory.SnackbarFactory;
import br.com.alura.agenda.model.Prova;

public class ProvasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provas);

        List<String> topicosMatematica = Arrays.asList("Equações de 2º Grau", "Trigonometria");
        Prova provaMatematica = new Prova("Matemática", "25/05/2018", topicosMatematica);

        List<String> topicosPortugues = Arrays.asList("Sujeito", "Objeto direto", "Objeto indireto");
        Prova provaPortugues = new Prova("Português", "05/06/2018", topicosPortugues);

        List<Prova> provas = Arrays.asList(provaMatematica, provaPortugues);

        ArrayAdapter<Prova> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, provas);

        ListView lvProvas = findViewById(R.id.provas_list);
        lvProvas.setAdapter(adapter);

        final LinearLayout provasLayout = findViewById(R.id.provas_layout);

        lvProvas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Prova prova = (Prova) parent.getItemAtPosition(position);
                SnackbarFactory.create(ProvasActivity.this, provasLayout, "Prova de " + prova).show();
            }
        });
    }
}
