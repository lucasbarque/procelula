package estacio.br.com.procelula.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.sql.SQLException;

import estacio.br.com.procelula.Dados.Usuario;
import estacio.br.com.procelula.Dao.UsuarioDAO;
import estacio.br.com.procelula.R;
import estacio.br.com.procelula.Utils.Utils;

public class LoginActivity extends ActionBarActivity implements View.OnClickListener{

    public static final String MINHAS_PREFERENCIAS = "MyPrefs" ;
    public static final String LOGIN_SP = "LOGIN"; //TODO passar constante para classe correspondente (usuario)
    public static final String NOME_SP = "NOME"; //TODO passar constante para classe correspondente (usuario)
    public static final String SOBRENOME_SP = "SOBRENOME"; //TODO passar constante para classe correspondente (usuario)
    public static final String PERMISSAO_SP = "PERMISSAO;"; //TODO passar constante para classe correspondente (usuario)

    private EditText edittext_login;
    private EditText edittext_senha;
    private Button button_entrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        insereListeners();

        //verifica se já foi realizado login anteriormente
        if (Utils.retornaSharedPreference(this, LOGIN_SP, null) != null) {
            new VerificaUsuarioTask(VerificaUsuarioTask.TIPO_VERIFICACAO_ATUALIZACAO).execute(Utils.retornaSharedPreference(this, LOGIN_SP, null));
        }

    }

    // insere listeners em todos os componentes da tela
    private void insereListeners() {
        getButtonEntrar().setOnClickListener(this);
    }

    //retorna referencia para o componente de tela
    private EditText getEditTextLogin() {
        if (edittext_login == null) {
            edittext_login = (EditText) findViewById(R.id.edittext_login);
//            edittext_login.setTypeface(font.SERIF);
        }
        return edittext_login;
    }

    //retorna referencia para o componente de tela
    private EditText getEditTextSenha() {
        if (edittext_senha == null) {
            edittext_senha = (EditText) findViewById(R.id.edittext_senha);
        }
        return edittext_senha;
    }

    //retorna referencia para o componente de tela
    private Button getButtonEntrar() {
        //se não existe referência popula o campo button_entrar com a mesma, caso contrário retorna a referência do componente
        if (button_entrar == null) {
            button_entrar = (Button) findViewById(R.id.button_entrar);
        }
        return button_entrar;
    }

    //Cria o menu da actionbar (barra no topo da tela)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    //Método executado ao selecionar opção da actionbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Executa operacoes de click (usado para gerenciar cliques nos botoes da tela de login)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_entrar:
                if (verificaCamposLoginSenha())
                    new VerificaUsuarioTask(VerificaUsuarioTask.TIPO_VERIFICACAO_LOGIN).execute(getEditTextLogin().getText().toString(), getEditTextSenha().getText().toString());
                break;
        }
    }

    private boolean verificaCamposLoginSenha() {
        boolean camposPreenchidos = true;
        if (getEditTextLogin().getText().length() <= 0) {
            getEditTextLogin().setError("Por favor, digite seu login");
            camposPreenchidos = false;
        }
        if (getEditTextSenha().getText().length() <= 0) {
            getEditTextSenha().setError("Por favor, digite sua senha");
            camposPreenchidos = false;
        }
        return camposPreenchidos;
    }

    private class VerificaUsuarioTask extends AsyncTask<String, Void, Integer> {
        public static final int TIPO_VERIFICACAO_LOGIN = 1; //verifica os dados de login e senha para primeira conexao
        public static final int TIPO_VERIFICACAO_ATUALIZACAO = 2; //verifica o login para atualizacao dos dados armazenados do usuario

        private final int LOGIN_SUCESSO = 0; // sucesso na operacao de login
        private final int LOGIN_FALHOU = 1; // ocorreu uma falha na operacao de login. Provavelmente causada por erro de digitacao do usuario ou inexistencia de cadastro
        private final int LOGIN_FALHA_SQLEXCEPTION = 2;

        private ProgressDialog progressDialog;
        private int tipoVerificacao;

        public VerificaUsuarioTask(int tipoVerificacao) {
            this.tipoVerificacao = tipoVerificacao;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mostra janela de progresso
            progressDialog = ProgressDialog.show(LoginActivity.this, "Aguarde por favor", "Verificando dados...", true);
        }

        @Override
        protected Integer doInBackground(String... dadosUsuario) {

            try {
                Usuario usuario;
                if (tipoVerificacao == TIPO_VERIFICACAO_LOGIN)
                    usuario = verificaUsuario(dadosUsuario[0], dadosUsuario[1]);
                else
                    usuario = verificaUsuario(dadosUsuario[0]);

                if (usuario != null) {
                    //TODO tratar o caso onde a celula do usuario nao eh encontrada
                    Utils.salvaSharedPreference(getApplicationContext(), LOGIN_SP, usuario.getLogin());
                    Utils.salvaSharedPreference(getApplicationContext(), NOME_SP, usuario.getNome());
                    Utils.salvaSharedPreference(getApplicationContext(), PERMISSAO_SP, Integer.toString(usuario.getPermissao()));
                    if (usuario.getCelula() != null) {
                        Utils.salvaCelulaSharedPreference(getApplicationContext(), usuario.getCelula());
                    }
                    return LOGIN_SUCESSO;
                }
                return LOGIN_FALHOU;
            } catch (SQLException e) {
                e.printStackTrace();
                //TODO LOG ERRO
                return LOGIN_FALHA_SQLEXCEPTION;
            }
        }

        @Override
        protected void onPostExecute(Integer resultadoLogin) {
            progressDialog.dismiss();
            switch (resultadoLogin) {
                case LOGIN_SUCESSO:
                    Intent intent = new Intent(LoginActivity.this, PrincipalActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case LOGIN_FALHOU:
                    Utils.mostraMensagemDialog(LoginActivity.this, "Não foi possível efetuar login. Verifique usuário e senha e tente novamente.");
                    Utils.limpaSharedPreferences(LoginActivity.this);
                    break;
                case LOGIN_FALHA_SQLEXCEPTION:
                    Utils.mostraMensagemDialog(LoginActivity.this, "Não foi possível efetuar login. Verifique sua conexão com a internet e tente novamente.");
                    break;
            }
            //TODO se enviado com sucesso limpa campos
            super.onPostExecute(resultadoLogin);
        }
    }

    //Método usado para verificar se o usuário digitado é válido e se a senha do mesmo confere com a cadastrada
    private Usuario verificaUsuario(String login, String senha) throws SQLException {
        return new UsuarioDAO().retornaUsuarioLogin(login, senha);
    }

    //Método usado para retornar informacoes atualizadas do usuário
    private Usuario verificaUsuario(String login) throws SQLException {
        return new UsuarioDAO().retornaUsuarioLogin(login);
    }


}
