package estacio.br.com.procelula.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;

import estacio.br.com.procelula.Dados.Programacao;
import estacio.br.com.procelula.Dao.ProgramacaoDAO;
import estacio.br.com.procelula.R;
import estacio.br.com.procelula.Utils.Utils;

public class ProgramacaoSelecionadaActivity extends ActionBarActivity implements View.OnClickListener {

    private TextView textview_nome_programacao;
    private TextView textview_data;
    private TextView textview_telefone;
    private TextView textview_horario;
    private TextView textview_valor;
    private TextView textview_mapa;
    private ImageView imageview_imagem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programacao_selecionada);

        if (getIntent().getSerializableExtra(ProgramacaoActivity.PROGRAMACAO_SELECIONADA) != null) {
            Programacao programacao = (Programacao) getIntent().getSerializableExtra(ProgramacaoActivity.PROGRAMACAO_SELECIONADA);
            montaTelaProgramacao(programacao);
            insereListeners();
        } else {
            Utils.mostraMensagemDialog(this, "Erro ao abrir programação.", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
        }
    }

    private void insereListeners() {
        getImageview_imagem().setOnClickListener(this);
    }

    private void montaTelaProgramacao(Programacao programacao) {
        getTextview_nome_programacao().setText(programacao.getNome());
        getTextview_data().setText(programacao.getData_prog());
        getTextview_horario().setText(programacao.getHorario());
        getTextview_telefone().setText(programacao.getTelefone());
        getTextview_valor().setText(programacao.getValor());
        getTextview_mapa().setText(programacao.getLocal_prog());
        getImage(programacao.getId_programacao());
        //new mostraImagemProgramacaoTask().execute(programacao);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageview_imagem:
               // Intent intent = new Intent(this, ImagemAmpliadaActivity.class);
              //  intent.putExtra(ImagemAmpliadaActivity.EXTRA_CAMINHO_IMAGEM, getApplicationContext().getFilesDir().getAbsolutePath() + Programacao.DIRETORIO_IMAGENS_PROGRAMACAO + "/" + Programacao.NOME_PADRAO_IMAGEM_PROGRAMACAO); //TODO arrumar caminhos das imagens
             //   startActivity(intent);
                break;
        }
    }

    //metodo responsável por buscar imagem da programacao
    //TODO verificar necessidade do metodo
    private class mostraImagemProgramacaoTask extends AsyncTask<Programacao, Void, Integer> {
        String caminhoImagem;
        ProgressDialog progressDialog;
        private final int RETORNO_SUCESSO = 0; //
        private final int FALHA_SQLEXCEPTION = 1; // provavel falha de conexao

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            caminhoImagem = getApplicationContext().getFilesDir().getAbsolutePath() + Programacao.DIRETORIO_IMAGENS_PROGRAMACAO;
            //mostra janela de progresso
            progressDialog = ProgressDialog.show(ProgramacaoSelecionadaActivity.this, "Carregando", "Aguarde por favor...", true);
        }

        @Override
        protected Integer doInBackground(Programacao... programacoes) {
            try {
                if (programacoes.length > 0) {
                    new ProgramacaoDAO().retornaProgramacaoImagem(programacoes[0], caminhoImagem, Programacao.NOME_PADRAO_IMAGEM_PROGRAMACAO);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return FALHA_SQLEXCEPTION;
                //TODO LOG ERRO
            }
            return RETORNO_SUCESSO;
        }

        @Override
        protected void onPostExecute(Integer resultadoLogin) {
            progressDialog.dismiss();
            switch (resultadoLogin) {
                case RETORNO_SUCESSO:
                    getImageview_imagem().setImageBitmap(BitmapFactory.decodeFile(caminhoImagem + "/" + Programacao.NOME_PADRAO_IMAGEM_PROGRAMACAO));
                    break;
                case FALHA_SQLEXCEPTION:
                    break;
            }
            super.onPostExecute(resultadoLogin);
        }
    }

    private void getImage(int id) {
        class GetImage extends AsyncTask<String,Void,Bitmap> {
            ProgressDialog loading;
            int idProgramacao;

            public GetImage(int idProgramacao) {
                this.idProgramacao = idProgramacao;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ProgramacaoSelecionadaActivity.this, "Buscando imagem...", null,true,true);
            }

            @Override
            protected void onPostExecute(Bitmap b) {
                super.onPostExecute(b);
                loading.dismiss();
                getImageview_imagem().setImageBitmap(b);
            }

            @Override
            protected Bitmap doInBackground(String... params) {
                String add = "http://vidasnoaltarmda.com/web_services/getImagem_programacao.php?id="+idProgramacao;
                URL url = null;
                Bitmap image = null;
                try {
                    url = new URL(add);
                    InputStream stream = url.openConnection().getInputStream();
                    Utils.escreveArquivo(stream, getApplicationContext().getFilesDir().getAbsolutePath() + Programacao.DIRETORIO_IMAGENS_PROGRAMACAO, Programacao.NOME_PADRAO_IMAGEM_PROGRAMACAO);
                    image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return image;
            }
        }

        GetImage gi = new GetImage(id);
        gi.execute();
    }

    public TextView getTextview_nome_programacao() {
        if (textview_nome_programacao == null) {
            textview_nome_programacao = (TextView) findViewById(R.id.textview_nome_programacao);
        }
        return textview_nome_programacao;
    }

    public TextView getTextview_data() {
        if (textview_data == null) {
            textview_data = (TextView) findViewById(R.id.textview_data);
        }
        return textview_data;
    }

    public TextView getTextview_telefone() {
        if (textview_telefone == null) {
            textview_telefone = (TextView) findViewById(R.id.textview_telefone);
        }
        return textview_telefone;
    }

    public TextView getTextview_horario() {
        if (textview_horario == null) {
            textview_horario = (TextView) findViewById(R.id.textview_horario);
        }
        return textview_horario;
    }

    public TextView getTextview_valor() {
        if (textview_valor == null) {
            textview_valor = (TextView) findViewById(R.id.textview_valor);
        }
        return textview_valor;
    }

    public TextView getTextview_mapa() {
        if (textview_mapa == null) {
            textview_mapa = (TextView) findViewById(R.id.textview_mapa);
        }
        return textview_mapa;
    }

    public ImageView getImageview_imagem() {
        if (imageview_imagem == null) {
            imageview_imagem = (ImageView) findViewById(R.id.imageview_imagem);
        }
        return imageview_imagem;
    }
}
