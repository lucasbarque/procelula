package estacio.br.com.procelula.Activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;



import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import estacio.br.com.procelula.Dados.Celula;
import estacio.br.com.procelula.Dados.Usuario;
import estacio.br.com.procelula.Dao.CelulaDAO;
import estacio.br.com.procelula.Dao.UsuarioDAO;
import estacio.br.com.procelula.R;
import estacio.br.com.procelula.Utils.TipoMsg;
import estacio.br.com.procelula.Utils.Utils;

public class RegistrarActivity extends ActionBarActivity implements View.OnClickListener {

    private EditText nome;
    private EditText sobrenome;
    private EditText data_nascimento;
    private Spinner celulas;
    private EditText login; //TODO definir padrao de login
    private EditText senha; //TODO definir padrao de senha
    private EditText confirma_senha;
    private Button registrar;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        new PopulaCelulasTask().execute();
        insereListeners();
        mToolbar = (Toolbar) findViewById(R.id.th_add_registrar);
        setSupportActionBar(mToolbar);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void insereListeners() {
        getRegistrar().setOnClickListener(this);
        getDataNascimento().setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.registrar:
                if (verificaDadosUsuario()) {
                    Usuario usuario = montaUsuario();
                    new InsereTask().execute(usuario);
                }

                //permissao
                //celula
                //insere
                break;
            case R.id.data_nascimento:
                Utils.mostraDatePickerDialog(this, getDataNascimento());
                break;
        }
    }

    private boolean verificaDadosUsuario() {
        boolean camposPreenchidos = true;
        if (getNome().getText().length() <= 0) {
            getNome().setError("Por favor, digite seu nome");
            camposPreenchidos = false;
        }

        if (getSobrenome().getText().length() <= 0) {
            getSobrenome().setError("Por favor, digite seu sobrenome");
            camposPreenchidos = false;
        }

        getDataNascimento().setError(null);
        if (getDataNascimento().getText().length() <= 0) {
            getDataNascimento().setError("Por favor, selecione sua data de nascimento");
            camposPreenchidos = false;
        } else {
            if (getDataNascimento().getTag() != null) {
                if (((Calendar) getDataNascimento().getTag()).get(Calendar.YEAR) > Calendar.getInstance().get(Calendar.YEAR)) {
                    Toast.makeText(this, "Digite uma data válida por favor.", Toast.LENGTH_LONG).show();
                    getDataNascimento().setError("Por favor, digite uma data válida.");
                    camposPreenchidos = false;
                }
            }
        }

        if (getLogin().getText().length() <= 0) {
            getLogin().setError("Por favor, digite um nome para seu login");
            camposPreenchidos = false;
        }

        if (getSenha().getText().length() <= 0) {
            getSenha().setError("Por favor, digite uma senha");
            camposPreenchidos = false;
        } else {
            if (!getSenha().getText().toString().equals(getConfirmaSenha().getText().toString())) {
                getConfirmaSenha().setError("Campo confirmação de senha não confere com a senha digitada");
                camposPreenchidos = false;
            }
        }

        return camposPreenchidos;
    }

    private Usuario montaUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNome(getNome().getText().toString());
        usuario.setSobrenome(getSobrenome().getText().toString());
        usuario.setDataNascimento(getDataNascimento().getText().toString());
        if (getCelulas().getSelectedItem() != null)
            usuario.setCelula((Celula) getCelulas().getSelectedItem());
        usuario.setLogin(getLogin().getText().toString());
        usuario.setSenha(getSenha().getText().toString());
        return usuario;
    }

    private class InsereTask extends AsyncTask<Usuario, Void, Integer> {
        ProgressDialog progressDialog;
        private final int INSERCAO_SUCESSO = 0; // sucesso na operacao de login
        private final int INSERCAO_FALHOU = 1; // ocorreu uma falha na operacao de login. Provavelmente causada por erro de digitacao do usuario ou inexistencia de cadastro
        private final int INSERCAO_FALHA_SQLEXCEPTION = 2; //provavel falha de conexao

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mostra janela de progresso
            progressDialog = ProgressDialog.show(RegistrarActivity.this, "Aguarde por favor", "Verificando dados...", true);
        }

        @Override
        protected Integer doInBackground(Usuario... usuarios) {
            if (usuarios.length > 0) {
                try {
                    if (new UsuarioDAO().insereUsuario(usuarios[0])) {
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
        protected void onPostExecute(Integer resultadoLogin) {
            progressDialog.dismiss();
            switch (resultadoLogin) {
                case INSERCAO_SUCESSO:
                    Utils.showMessageToast(RegistrarActivity.this, "Cadastrado com sucesso.");
                    setResult(RESULT_OK, getIntent());
                    finish();
                    break;
                case INSERCAO_FALHA_SQLEXCEPTION:
                    Utils.showMsgAlertOK(RegistrarActivity.this, "Erro de Conexão","Não foi possível finalizar o cadastro. Verifique sua conexão com a internet e tente novamente.", TipoMsg.ERRO);
                    break;
            }
            super.onPostExecute(resultadoLogin);
        }
    }


    //metodo responsável por buscar os dados das celulas no banco (acesso remoto) e popular o spinner de celulas.
    //seguindo a boa pratica de separar a interecao externa da thread principal
    private class PopulaCelulasTask extends AsyncTask<Usuario, Void, Integer> {
        ArrayList<Celula> celulas;
        ProgressDialog progressDialog;
        private final int RETORNO_SUCESSO = 0; //
        private final int FALHA_SQLEXCEPTION = 1; // provavel falha de conexao

        //metodo executado pela thread principal antes de qualquer outro processamento. Nesse caso utilizado para
        // inicializar a lista de celulas e mostrar o dialog de progresso para o usuario
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            celulas = new ArrayList<Celula>();
            //mostra janela de progresso
            progressDialog = ProgressDialog.show(RegistrarActivity.this, "Carregando", "Verificando dados...", true);
        }

        //metodo que executa as tarefas de acesso a banco e retorno das celulas em uma thread separada.
        // Como o proprio nome do metodo diz em background (ou em segundo plano)
        @Override
        protected Integer doInBackground(Usuario... usuarios) {
            try {
                celulas = new CelulaDAO().retornaCelulas();
            } catch (SQLException e) {
                e.printStackTrace();
                return FALHA_SQLEXCEPTION;
                //TODO LOG ERRO
            }
            return RETORNO_SUCESSO;
        }

        //metodo executado apos finalizacao do metodo doInBackground. Sendo assim ja e possivel usar a lista de celulas
        // retornada
        @Override
        protected void onPostExecute(Integer resultadoLogin) {
            progressDialog.dismiss();
            switch (resultadoLogin) {
                case RETORNO_SUCESSO:
                    getCelulas().setAdapter(new ArrayAdapter<Celula>(RegistrarActivity.this, android.R.layout.simple_list_item_1, celulas));
                    break;
                case FALHA_SQLEXCEPTION:
                    //nao foi possivel carregar as celulas, sendo assim uma mensagem de erro eh exibida e a tela eh encerrada
                    Utils.showMsgAlertOK(RegistrarActivity.this,"Erro de Conexão", "Não foi possível carregar as células. Verifique sua conexão e tente novamente.", TipoMsg.ERRO);
                    break;
            }
            super.onPostExecute(resultadoLogin);
        }
    }

    private EditText getNome() {
        if (nome == null) {
            nome = (EditText) findViewById(R.id.nome);
        }
        return nome;
    }

    private EditText getSobrenome() {
        if (sobrenome == null) {
            sobrenome = (EditText) findViewById(R.id.sobrenome);
        }
        return sobrenome;
    }

    private EditText getDataNascimento() {
        if (data_nascimento == null) {
            data_nascimento = (EditText) findViewById(R.id.data_nascimento);
        }
        return data_nascimento;
    }

    private Spinner getCelulas() {
        if (celulas == null) {
            celulas = (Spinner) findViewById(R.id.celulas);
        }
        return celulas;
    }

    private EditText getLogin() {
        if (login == null) {
            login = (EditText) findViewById(R.id.login);
        }
        return login;
    }

    private EditText getSenha() {
        if (senha == null) {
            senha = (EditText) findViewById(R.id.senha);
        }
        return senha;
    }

    private EditText getConfirmaSenha() {
        if (confirma_senha == null) {
            confirma_senha = (EditText) findViewById(R.id.confirma_senha);
        }
        return confirma_senha;
    }

    private Button getRegistrar() {
        if (registrar == null) {
            registrar = (Button) findViewById(R.id.registrar);
        }
        return registrar;
    }

}
