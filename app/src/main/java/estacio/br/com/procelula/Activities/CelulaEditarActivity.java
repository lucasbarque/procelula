package estacio.br.com.procelula.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import estacio.br.com.procelula.Dados.Celula;
import estacio.br.com.procelula.R;
import estacio.br.com.procelula.Utils.Utils;


public class CelulaEditarActivity  extends AppCompatActivity {

    private Celula celula;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_celula);

        celula = Utils.retornaCelulaSharedPreferences(this);

        ((EditText) findViewById(R.id.edittext_nome_lider)).setText(celula.getLider());
        //((Spinner) findViewById(R.id.data_celula))
        ((EditText) findViewById(R.id.horario_celula)).setText(celula.getHorario());
        ((EditText) findViewById(R.id.edittext_local)).setText(celula.getLocal_celula());
        //((Spinner) findViewById(R.id.edittext_dia_jejum))
        //((Spinner) findViewById(R.id.edittext_dia_semana))
        ((EditText) findViewById(R.id.edittext_versiculo)).setText(celula.getVersiculo());
    }
}
