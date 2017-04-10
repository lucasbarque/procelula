package estacio.br.com.procelula.Activities;

import android.app.ProgressDialog;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.sql.SQLException;
import java.util.ArrayList;

import estacio.br.com.procelula.Dados.Celula;
import estacio.br.com.procelula.Dados.Usuario;
import estacio.br.com.procelula.Dao.UsuarioDAO;
import estacio.br.com.procelula.R;
import estacio.br.com.procelula.Utils.TipoMsg;
import estacio.br.com.procelula.Utils.Utils;


public class AniversariantesActivity extends ActionBarActivity {

    private ListView listview_aniversariantes;
    private Toolbar mToolbar;
    private ImageView imageViewListaVazia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aniversariantes);
        new PopulaAniversariantesTask().execute(getSPCelula());

        mToolbar = (Toolbar) findViewById(R.id.th_aniversariante);
        mToolbar.setTitle("Aniversariantes");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private Celula getSPCelula() {
        return Utils.retornaCelulaSharedPreferences(this);
    }

    private class PopulaAniversariantesTask extends AsyncTask<Celula, Void, Integer> {
        ArrayList<Usuario> aniversariantes;
        ProgressDialog progressDialog;
        private final int RETORNO_SUCESSO = 0; //
        private final int FALHA_SQLEXCEPTION = 1; // provavel falha de conexao

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            aniversariantes = new ArrayList<Usuario>();
            progressDialog = ProgressDialog.show(AniversariantesActivity.this, "Carregando", "Aguarde por favor...", true);
        }

        @Override
        protected Integer doInBackground(Celula... celulas) {
            try {if(celulas.length > 0){
                aniversariantes = new UsuarioDAO().retornaAniversariantes(celulas[0]);
            }
            } catch (SQLException e) {
                e.printStackTrace();
                return FALHA_SQLEXCEPTION;
                //TODO LOG ERRO
            }
            return RETORNO_SUCESSO;
        }

        @Override
        protected void onPostExecute(Integer resultado) {
            progressDialog.dismiss();
            switch (resultado) {
                case RETORNO_SUCESSO:
                    if (aniversariantes.size() > 0) {
                        getImageViewListaVazia().setVisibility(View.GONE);
                        getListViewAniversariantes().setVisibility(View.VISIBLE);
                    }else {
                        getImageViewListaVazia().setVisibility(View.VISIBLE);
                        getListViewAniversariantes().setVisibility(View.GONE);
                    }
                    getListViewAniversariantes().setAdapter(new ArrayAdapter<Usuario>(AniversariantesActivity.this, R.layout.custom_list_item_3, aniversariantes));

                    break;
                case FALHA_SQLEXCEPTION:
                    Utils.showMsgAlertOK(AniversariantesActivity.this,"ERRO", "Não foi possível carregar os aniversariantes. Verifique sua conexão e tente novamente.", TipoMsg.ERRO);
                    break;
            }
            super.onPostExecute(resultado);
        }
    }

    private ListView getListViewAniversariantes() {
        if (listview_aniversariantes == null) {
            listview_aniversariantes = (ListView) findViewById(R.id.listview_aniversariantes);
        }
        return listview_aniversariantes;
    }
    private ImageView getImageViewListaVazia() {
        if (imageViewListaVazia == null) {
            imageViewListaVazia = (ImageView) findViewById(R.id.imageview_lista_vazia);
        }
        return imageViewListaVazia;
    }
}
