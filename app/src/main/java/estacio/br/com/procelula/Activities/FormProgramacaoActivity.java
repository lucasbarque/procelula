package estacio.br.com.procelula.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import estacio.br.com.procelula.Dados.Celula;
import estacio.br.com.procelula.Dados.Programacao;
import estacio.br.com.procelula.R;
import estacio.br.com.procelula.Utils.RequestHandler;
import estacio.br.com.procelula.Utils.TipoMsg;
import estacio.br.com.procelula.Utils.Utils;

public class FormProgramacaoActivity extends ActionBarActivity implements View.OnClickListener{
    private static final int ALTURA_MAX_IMAGEM = 200;
    private static final int LARGURA_MAX_IMAGEM = 200;

    public static final String UPLOAD_URL = "http://vidasnoaltar.com/web_services/insert_programacao.php";
    public static final String UPLOAD_KEY_ID_CELULA = "id_celula";
    public static final String UPLOAD_KEY_NOME      = "nome";
    public static final String UPLOAD_KEY_DATA      = "data";
    public static final String UPLOAD_KEY_HORARIO   = "horario";
    public static final String UPLOAD_KEY_LOCAL     = "local_prog";
    public static final String UPLOAD_KEY_TELEFONE  = "telefone";
    public static final String UPLOAD_KEY_VALOR     = "valor";
    public static final String UPLOAD_KEY_IMAGEM    = "image";

    public static int REQUEST_IMAGEM = 1;

    private Celula celula;

    private EditText editTextNome;
    private EditText editTextData;
    private EditText editTextHorario;
    private EditText editTextEndereco;
    private EditText editTexTelefone;
    private EditText editTextValor;
    private Button buttonSalvar;
    private ImageView imagemProgramacao;
    private Toolbar mToolbar;
    private TextWatcher telefone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_programacao);

        celula = Utils.retornaCelulaSharedPreferences(this);
        insereListener();
        mToolbar = (Toolbar) findViewById(R.id.th_add_programacao);
        setSupportActionBar(mToolbar);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

//TODO fazer funcionar
//        if(editTexTelefone.length() < 14){
//            telefone = Mask.insert("(##)####-####", editTexTelefone);
//        } else{
//            telefone = Mask.insert("(##)#####-####", editTexTelefone);
//        }
    }

    private void insereListener() {
        getButtonSalvar().setOnClickListener(this);
        getEditTextData().setOnClickListener(this);
        getEditTextHorario().setOnClickListener(this);
        getImagemProgramacao().setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_salvar:
                if (verificaCampos()) {
                    Programacao programacao = new Programacao();
                    programacao.setId_celula(celula.getId_celula());
                    programacao.setNome(getEditTextNome().getText().toString());
                    programacao.setData_prog(getEditTextData().getText().toString());
                    programacao.setHorario(getEditTextHorario().getText().toString());
                    programacao.setLocal_prog(getEditTextEndereco().getText().toString());
                    programacao.setTelefone(getEditTextTelefone().getText().toString());
                    programacao.setValor(getEditTextValor().getText().toString());

                    new InsereTask().execute(programacao);
                }
                break;
            case R.id.data_programacao:
                Utils.mostraDatePickerDialog(this, getEditTextData());
                break;
            case R.id.horario_programacao:
                Utils.mostraTimePickerDialog(this, getEditTextHorario());
                break;
            case R.id.imageview_programacao:
                selecionarImagem();
                break;
        }
    }

    private class InsereTask extends AsyncTask<Programacao, Void, Integer> {
        ProgressDialog progressDialog;
        private final int INSERCAO_SUCESSO = 0;
        private final int INSERCAO_FALHOU = 1;
        private final int INSERCAO_FALHA_SQLEXCEPTION = 2;

        private boolean insereImagem = false;
        private RequestHandler rh = new RequestHandler();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mostra janela de progresso
            progressDialog = ProgressDialog.show(FormProgramacaoActivity.this, "Aguarde por favor", "Verificando dados...", true);
            insereImagem = (getImagemProgramacao().getTag() != null) && ((boolean)getImagemProgramacao().getTag() == true);
        }

        @Override
        protected Integer doInBackground(Programacao... programacoes) {
            if (programacoes.length > 0) {

                Programacao programacao = programacoes[0];

                HashMap<String,String> data = new HashMap<>();
                data.put(UPLOAD_KEY_ID_CELULA, Integer.toString(programacao.getId_celula()));
                data.put(UPLOAD_KEY_NOME, programacao.getNome());
                data.put(UPLOAD_KEY_DATA, Utils.converteDataBanco(programacao.getData_prog()));
                data.put(UPLOAD_KEY_HORARIO, programacao.getHorario());
                data.put(UPLOAD_KEY_LOCAL, programacao.getLocal_prog());
                data.put(UPLOAD_KEY_TELEFONE, programacao.getTelefone());
                data.put(UPLOAD_KEY_VALOR, programacao.getValor());
                if (insereImagem) {
                    String uploadImage = Utils.getStringImage(Utils.getAmostraImagem(ALTURA_MAX_IMAGEM, LARGURA_MAX_IMAGEM, getApplicationContext().getFilesDir().getAbsolutePath() +
                            Programacao.DIRETORIO_IMAGENS_PROGRAMACAO + "/" + Programacao.NOME_PADRAO_IMAGEM_PROGRAMACAO_ENVIAR));
                    data.put(UPLOAD_KEY_IMAGEM, uploadImage);
                }

                String result = rh.sendPostRequest(UPLOAD_URL,data);
                return "0".equals(result) ? INSERCAO_SUCESSO : INSERCAO_FALHOU;
            } else {
                return INSERCAO_FALHOU;
            }
        }

        @Override
        protected void onPostExecute(Integer resultadoInsercao) {
            progressDialog.dismiss();
            switch (resultadoInsercao) {
                case INSERCAO_SUCESSO:
                    Utils.showMessageToast(FormProgramacaoActivity.this, "Inserido com sucesso.");
                    setResult(RESULT_OK, getIntent());
                    finish();
                    break;
                case INSERCAO_FALHOU:
                    Utils.showMsgAlertOK(FormProgramacaoActivity.this,"Erro", "Não foi possível finalizar o cadastro. Por favor, informe o erro ao administrador do sistema.", TipoMsg.ERRO);
                    break;
                case INSERCAO_FALHA_SQLEXCEPTION:
                    Utils.showMsgAlertOK(FormProgramacaoActivity.this,"Erro de Conexão", "Não foi possível finalizar o cadastro. Verifique sua conexão com a internet e tente novamente.", TipoMsg.ERRO);
                    break;
            }
            super.onPostExecute(resultadoInsercao);
        }
    }

    private void selecionarImagem() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Selecione a imagem"), REQUEST_IMAGEM);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        InputStream stream = null;
        if (requestCode == REQUEST_IMAGEM && resultCode == RESULT_OK) {
            try {
                stream = getContentResolver().openInputStream(data.getData());

                Utils.escreveArquivo(stream, getApplicationContext().getFilesDir().getAbsolutePath() + Programacao.DIRETORIO_IMAGENS_PROGRAMACAO, Programacao.NOME_PADRAO_IMAGEM_PROGRAMACAO_ENVIAR);

                getImagemProgramacao().setImageBitmap(Utils.getAmostraImagem(200, 200, getApplicationContext().getFilesDir().getAbsolutePath() + Programacao.DIRETORIO_IMAGENS_PROGRAMACAO + "/" + Programacao.NOME_PADRAO_IMAGEM_PROGRAMACAO_ENVIAR));
                getImagemProgramacao().setTag(true);

            }
            catch(FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (stream != null)
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }

        }
    }

    private boolean verificaCampos() {
        boolean camposPreenchidos = true;
        if (getEditTextNome().getText().length() <= 0) {
            getEditTextNome().setError("Por favor, digite o Nome da Programação");
            camposPreenchidos = false;
        }

        if (getEditTextData().getText().length() <= 0) {
            getEditTextData().setError("Por favor, digite a data da programação");
            camposPreenchidos = false;
        }

        if (getEditTextHorario().getText().length() <= 0) {
            getEditTextHorario().setError("Por favor, digite o horário da programação");
            camposPreenchidos = false;
        }

        if (getEditTextEndereco().getText().length() <= 0) {
            getEditTextEndereco().setError("Por favor, digite o endereço");
            camposPreenchidos = false;
        }

        if (getEditTextTelefone().getText().length() <= 0) {
            getEditTextTelefone().setError("Por favor, digite o telefone");
            camposPreenchidos = false;
        }

        if (getEditTextValor().getText().length() <= 0) {
            getEditTextValor().setError("Por favor, digite o valor em R$ que será gasto aproximadamente");
            camposPreenchidos = false;
        }

        return camposPreenchidos;
    }

    public EditText getEditTextNome() {
        if (editTextNome == null) {
            editTextNome = (EditText) findViewById(R.id.edittext_nome);
        }
        return editTextNome;
    }

    public EditText getEditTextData() {
        if (editTextData == null) {
            editTextData = (EditText) findViewById(R.id.data_programacao);
        }
        return editTextData;
    }

    public EditText getEditTextHorario() {
        if (editTextHorario == null) {
            editTextHorario = (EditText) findViewById(R.id.horario_programacao);
        }
        return editTextHorario;
    }

    public EditText getEditTextEndereco() {
        if (editTextEndereco == null) {
            editTextEndereco = (EditText) findViewById(R.id.edittext_endereco);
        }
        return editTextEndereco;
    }

    public EditText getEditTextTelefone() {
        if (editTexTelefone == null) {
            editTexTelefone = (EditText) findViewById(R.id.edittext_telefone);
        }
        return editTexTelefone;
    }

    public EditText getEditTextValor() {
        if (editTextValor == null) {
            editTextValor = (EditText) findViewById(R.id.edittext_valor);
        }
        return editTextValor;
    }

    public ImageView getImagemProgramacao() {
        if (imagemProgramacao == null) {
            imagemProgramacao = (ImageView) findViewById(R.id.imageview_programacao);
        }
        return imagemProgramacao;
    }

    public Button getButtonSalvar() {
        if (buttonSalvar == null) {
            buttonSalvar = (Button) findViewById(R.id.button_salvar);
        }
        return buttonSalvar;
    }

}
