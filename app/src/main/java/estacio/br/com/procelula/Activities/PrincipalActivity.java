package estacio.br.com.procelula.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.HashMap;

import estacio.br.com.procelula.BuildConfig;
import estacio.br.com.procelula.R;
import estacio.br.com.procelula.Utils.RequestHandler;
import estacio.br.com.procelula.Utils.TipoMsg;
import estacio.br.com.procelula.Utils.Utils;

public class PrincipalActivity extends AppCompatActivity {

    public static final String UPLOAD_URL = "http://www.vidasnoaltar.com/web_services/getVersao.php";
    private LinearLayout aviso;
    private LinearLayout programacao;
    private LinearLayout aniversariante;
    private LinearLayout ge;
    private LinearLayout celula;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        getAviso().setOnTouchListener ((View.OnTouchListener) this);
        getProgramacao().setOnTouchListener((View.OnTouchListener) this);
        getAniversariante().setOnTouchListener((View.OnTouchListener) this);
        getGe().setOnTouchListener((View.OnTouchListener) this);
        getCelula().setOnTouchListener((View.OnTouchListener) this);

        mToolbar = (Toolbar) findViewById(R.id.th_main);
        mToolbar.setTitle("ProCélula");
        setSupportActionBar(mToolbar);

        new CheckVersao().execute();
    }

    //Cria o menu da actionbar (barra no topo da tela)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    //Método executado ao selecionar opção da actionbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_sair) {
            Utils.limpaSharedPreferences(this);
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }else if (id == R.id.creditos) {
            Utils.showMsgAlertOK(PrincipalActivity.this,"Créditos de Desenvolvimento", "Lucas Barque, Fernando, Vinicius e Jean\n Versão 1.1.1", TipoMsg.INFO);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class CheckVersao extends AsyncTask<Void, Void, String> {
        private final int RETORNO_SUCESSO = 0;
        private final int RETORNO_FALHOU = 1;

        ProgressDialog progressDialog;

        private RequestHandler rh = new RequestHandler();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mostra janela de progresso
            progressDialog = ProgressDialog.show(PrincipalActivity.this, "Aguarde por favor", "Verificando dados...", true);
        }
        @Override
        protected String doInBackground(Void... params) {
            HashMap<String,String> data = new HashMap<>();
            String result = rh.sendGetRequest(UPLOAD_URL);
            return result;
        }

        @Override
        protected void onPostExecute(String ultimaVersao) {
            progressDialog.dismiss();
            if (ultimaVersao != null) {
                int versao = BuildConfig.VERSION_CODE;
                if (!Integer.toString(versao).equals(ultimaVersao)) {
                    Utils.mostraMensagemDialog(PrincipalActivity.this, "Por favor, atualize a versão do seu App para a mais recente.", "Atualizar",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                    try {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                    } catch (android.content.ActivityNotFoundException anfe) {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                    }
                                }
                            });
                }
            }

            super.onPostExecute(ultimaVersao);
        }
    }


    public boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            switch (view.getId()) {
                case R.id.aviso:
                    ((ImageView) findViewById(R.id.imageview_aviso)).setImageResource(R.drawable.aviso_pressed);
                    break;
                case R.id.aniversariante:
                    ((ImageView) findViewById(R.id.imageview_aniversariantes)).setImageResource(R.drawable.aniversariante_pressed);
                    break;
            }
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            switch (view.getId()) {
                case R.id.aviso:
                    ((ImageView) findViewById(R.id.imageview_aviso)).setImageResource(R.drawable.aviso);
                    Intent intentAviso = new Intent(this, AvisoActivity.class);
                    startActivity(intentAviso);
                    break;

                case R.id.programacao:
                    Intent intentProgramacao = new Intent(this, ProgramacaoActivity.class);
                    startActivity(intentProgramacao);
                    break;

                case R.id.aniversariante:
                    ((ImageView) findViewById(R.id.imageview_aniversariantes)).setImageResource(R.drawable.aniversariante);
                    Intent intentAniversariante = new Intent(this, AniversariantesActivity.class);
                    startActivity(intentAniversariante);
                    break;

                case R.id.ge:
                    Intent intentGE = new Intent(this, GEActivity.class);
                    startActivity(intentGE);
                    break;

                case R.id.celula:
                    Intent intentCelula = new Intent(this, CelulaActivity.class);
                    startActivity(intentCelula);
                    break;
            }
            return true;
        } else {
            switch (view.getId()) {
                case R.id.aviso:
                    ((ImageView) findViewById(R.id.imageview_aviso)).setImageResource(R.drawable.aviso);
                    break;
                case R.id.aniversariante:
                    ((ImageView) findViewById(R.id.imageview_aniversariantes)).setImageResource(R.drawable.aniversariante);
                    break;
            }
        }
        return false;
    }

    private LinearLayout getAviso() {
        if (aviso == null) {
            aviso = (LinearLayout) findViewById(R.id.aviso);
        }
        return aviso;
    }


    private LinearLayout getProgramacao() {
        if (programacao == null) {
            programacao = (LinearLayout) findViewById(R.id.programacao);
        }
        return programacao;
    }

    private LinearLayout getAniversariante() {
        if (aniversariante == null) {
            aniversariante = (LinearLayout) findViewById(R.id.aniversariante);
        }
        return aniversariante;
    }

    private LinearLayout getGe() {
        if (ge == null) {
            ge = (LinearLayout) findViewById(R.id.ge);
        }
        return ge;
    }

    private LinearLayout getCelula() {
        if (celula == null) {
            celula = (LinearLayout) findViewById(R.id.celula);
        }
        return celula;
    }
}
