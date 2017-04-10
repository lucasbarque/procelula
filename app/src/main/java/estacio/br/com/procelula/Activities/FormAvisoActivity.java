package estacio.br.com.procelula.Activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.sql.SQLException;

import estacio.br.com.procelula.Dados.Aviso;
import estacio.br.com.procelula.Dados.Celula;
import estacio.br.com.procelula.Dao.AvisoDAO;
import estacio.br.com.procelula.R;
import estacio.br.com.procelula.Utils.TipoMsg;
import estacio.br.com.procelula.Utils.Utils;

public class FormAvisoActivity extends ActionBarActivity implements View.OnClickListener{

    private Celula celula;

    private EditText editTextTitulo;
    private EditText editTextConteudo;
    private Button buttonSalvar;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_aviso);

        celula = Utils.retornaCelulaSharedPreferences(this);
        insereListener();
        mToolbar = (Toolbar) findViewById(R.id.th_add_aviso);
        setSupportActionBar(mToolbar);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void insereListener() {
        getButtonSalvar().setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_salvar:
                if (verificaCampos()) {
                    Aviso aviso = new Aviso();
                    aviso.setId_celula(celula.getId_celula());
                    aviso.setTitulo(getEditTextTitulo().getText().toString());
                    aviso.setConteudo(getEditTextConteudo().getText().toString());

                    new InsereTask().execute(aviso);
                }
                break;
        }
    }

    private class InsereTask extends AsyncTask<Aviso, Void, Integer> {
        ProgressDialog progressDialog;
        private final int INSERCAO_SUCESSO = 0;
        private final int INSERCAO_FALHOU = 1;
        private final int INSERCAO_FALHA_SQLEXCEPTION = 2;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mostra janela de progresso
            progressDialog = ProgressDialog.show(FormAvisoActivity.this, "Aguarde por favor", "Verificando dados...", true);
        }

        @Override
        protected Integer doInBackground(Aviso... avisos) {
            if (avisos.length > 0) {
                try {
                    if (new AvisoDAO().insereAviso(avisos[0])) {
                        return INSERCAO_SUCESSO;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    return INSERCAO_FALHA_SQLEXCEPTION;
                    //TODO LOG ERRO
                }
            } else {
                return INSERCAO_FALHOU;
            }
            return INSERCAO_FALHOU;
        }

        @Override
        protected void onPostExecute(Integer resultadoInsercao) {
            progressDialog.dismiss();
            switch (resultadoInsercao) {
                case INSERCAO_SUCESSO:

                    setResult(RESULT_OK, getIntent());
                    finish();
                    break;
                case INSERCAO_FALHA_SQLEXCEPTION:
                    Utils.showMsgAlertOK(FormAvisoActivity.this, "Erro", "Não foi possível finalizar o cadastro. Verifique sua conexão com a internet e tente novamente.", TipoMsg.ERRO);
                    break;
            }
            super.onPostExecute(resultadoInsercao);
        }
    }

    private boolean verificaCampos() {
        boolean camposPreenchidos = true;
        if (getEditTextTitulo().getText().length() <= 0) {
            getEditTextTitulo().setError("Por favor, digite o título");
            camposPreenchidos = false;
        }

        if (getEditTextConteudo().getText().length() <= 0) {
            getEditTextConteudo().setError("Por favor, digite o conteúdo");
            camposPreenchidos = false;
        }
        return camposPreenchidos;
    }

    public EditText getEditTextTitulo() {
        if (editTextTitulo == null) {
            editTextTitulo = (EditText) findViewById(R.id.edittext_titulo);
        }
        return editTextTitulo;
    }

    public EditText getEditTextConteudo() {
        if (editTextConteudo == null) {
            editTextConteudo = (EditText) findViewById(R.id.edittext_conteudo);
        }
        return editTextConteudo;
    }

    public Button getButtonSalvar() {
        if (buttonSalvar == null) {
            buttonSalvar = (Button) findViewById(R.id.button_salvar);
        }
        return buttonSalvar;
    }

}
