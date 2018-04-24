package br.com.alura.agenda.activity;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import br.com.alura.agenda.R;
import br.com.alura.agenda.model.Prova;

public class ProvaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prova);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Prova prova = (Prova) intent.getSerializableExtra("prova");

        TextView tvMateria = findViewById(R.id.prova_materia);
        TextView tvData = findViewById(R.id.prova_data);
        ListView lvTopicos = findViewById(R.id.prova_topicos);

        tvMateria.setText(prova.getMateria());
        tvData.setText(prova.getData());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, prova.getTopicos());
        lvTopicos.setAdapter(adapter);
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
