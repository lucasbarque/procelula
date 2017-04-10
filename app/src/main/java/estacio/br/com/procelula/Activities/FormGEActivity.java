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

import estacio.br.com.procelula.Dados.Celula;
import estacio.br.com.procelula.Dados.GrupoEvangelistico;
import estacio.br.com.procelula.Dao.GrupoEvangelisticoDAO;
import estacio.br.com.procelula.R;
import estacio.br.com.procelula.Utils.TipoMsg;
import estacio.br.com.procelula.Utils.Utils;

/**
 * Created by barque on 26/04/2016.
 */
public class FormGEActivity extends ActionBarActivity implements View.OnClickListener {
        private Celula celula;
        private EditText edittextNome;
        private Button buttonSalvar;
        private Toolbar mToolbar;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_form_ge);

            celula = Utils.retornaCelulaSharedPreferences(this);
            insereListener();
            mToolbar = (Toolbar) findViewById(R.id.th_add_ge);
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
                    GrupoEvangelistico grupoEvangelistico = new GrupoEvangelistico();
                    grupoEvangelistico.setId_celula(celula.getId_celula());
                    grupoEvangelistico.setNome(getEdittextNome().getText().toString());

                    new InsereTask().execute(grupoEvangelistico);
                }
                break;
        }
    }

    private class InsereTask extends AsyncTask<GrupoEvangelistico, Void, Integer> {
        ProgressDialog progressDialog;
        private final int INSERCAO_SUCESSO = 0;
        private final int INSERCAO_FALHOU = 1;
        private final int INSERCAO_FALHA_SQLEXCEPTION = 2;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mostra janela de progresso
            progressDialog = ProgressDialog.show(FormGEActivity.this, "Aguarde por favor", "Verificando dados...", true);
        }

        @Override
        protected Integer doInBackground(GrupoEvangelistico... grupoEvangelisticos) {
            if (grupoEvangelisticos.length > 0) {
                try {
                    if (new GrupoEvangelisticoDAO().insereGE(grupoEvangelisticos[0])) {
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
                    Utils.showMessageToast(FormGEActivity.this, "Inserido com sucesso.");
                    setResult(RESULT_OK, getIntent());
                    finish();
                    //TODO terminar tela e voltar pra tela de login
                    break;
                case INSERCAO_FALHA_SQLEXCEPTION:
                    Utils.showMsgAlertOK(FormGEActivity.this,"Erro", "Não foi possível finalizar o cadastro. Verifique sua conexão com a internet e tente novamente.", TipoMsg.ERRO);
                    break;
            }
            super.onPostExecute(resultadoInsercao);
        }
    }

    private boolean verificaCampos() {
        boolean camposPreenchidos = true;
        if (getEdittextNome().getText().length() <= 0) {
            getEdittextNome().setError("Por favor, digite o Nome do GE");
            camposPreenchidos = false;
        }

        return camposPreenchidos;
    }

    public EditText getEdittextNome() {
        if (edittextNome == null) {
            edittextNome = (EditText) findViewById(R.id.edittext_nome);
        }
        return edittextNome;
    }

    public Button getButtonSalvar() {
        if (buttonSalvar == null) {
            buttonSalvar = (Button) findViewById(R.id.button_salvar);
        }
        return buttonSalvar;
    }

}
