package estacio.br.com.procelula.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import java.sql.SQLException;
import java.util.ArrayList;

import estacio.br.com.procelula.Dados.Celula;
import estacio.br.com.procelula.Dados.Programacao;
import estacio.br.com.procelula.Dados.Usuario;
import estacio.br.com.procelula.Dao.ProgramacaoDAO;
import estacio.br.com.procelula.R;
import estacio.br.com.procelula.Utils.AdapterDelete;
import estacio.br.com.procelula.Utils.TipoMsg;
import estacio.br.com.procelula.Utils.Utils;

public class ProgramacaoActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {
    public static final int REQUEST_SALVAR = 1;
    public static final String PROGRAMACAO_SELECIONADA = "programacao_selecionada";
    private ListView listview_programacoes;
    private static final String STATE_LISTA_PROGRAMACOES = "STATE_LISTA_PROGRAMACOES";
    private Celula celula;
    private Toolbar mToolbar;
    private ArrayList<Programacao> mListaProgramacoes;

    private ImageView imageViewListaVazia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programacao);


        if (savedInstanceState == null) {
            new PopulaProgramacoesTask().execute(getSPCelula());
        } else {
            if (savedInstanceState.get(STATE_LISTA_PROGRAMACOES) != null) {
                mListaProgramacoes = (ArrayList<Programacao>) savedInstanceState.get(STATE_LISTA_PROGRAMACOES);
                getListViewProgramacao().setAdapter(new AdapterDelete<Programacao>(this, mListaProgramacoes));
            }
        }

        insereListeners();

        mToolbar = (Toolbar) findViewById(R.id.th_programacao);
        mToolbar.setTitle("Programações");
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

    @Override
    public void onSaveInstanceState(Bundle estadoDeSaida) { //TODO fazer tratamento de giro de tela nas outras telas
        super.onSaveInstanceState(estadoDeSaida);
        if (getListViewProgramacao().getAdapter() != null) {
            estadoDeSaida.putSerializable(STATE_LISTA_PROGRAMACOES, mListaProgramacoes);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SALVAR && resultCode == RESULT_OK) {
            new PopulaProgramacoesTask().execute(getSPCelula());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int permissaoUsuario = 0;
        try {
            permissaoUsuario = Integer.parseInt(Utils.retornaSharedPreference(this, LoginActivity.PERMISSAO_SP, "0"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (permissaoUsuario == Usuario.PERMISSAO_LIDER || permissaoUsuario == Usuario.PERMISSAO_PASTOR) {
            getMenuInflater().inflate(R.menu.menu_programacao, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_adicionar) {
            Intent intent = new Intent(this, FormProgramacaoActivity.class);
            startActivityForResult(intent, REQUEST_SALVAR);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void insereListeners() {
        int permissaoUsuario = 0;
        permissaoUsuario = Integer.parseInt(Utils.retornaSharedPreference(this, LoginActivity.PERMISSAO_SP, "0"));

        getListViewProgramacao().setOnItemClickListener(this);
        if (permissaoUsuario == Usuario.PERMISSAO_LIDER || permissaoUsuario == Usuario.PERMISSAO_PASTOR) {
            getListViewProgramacao().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            getListViewProgramacao().setSelected(true);
        }



        getListViewProgramacao().setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            int selectionCounter;

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                ((AdapterDelete) getListViewProgramacao().getAdapter()).limpaItensSelecionados();
                selectionCounter = 0;
                // TODO Auto-generated method stub

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // TODO Auto-generated method stub
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.menu_delete, menu);
                return true;
            }

            @Override
            public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
                // TODO Auto-generated method stub
                switch (item.getItemId()) {
                    case R.id.action_deletar:
                        new RemoveProgramacaoTask(
                                ((AdapterDelete<Programacao>) getListViewProgramacao().getAdapter()).getItensSelecionados(),
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        mode.finish();
                                    }
                                }
                        ).execute();
                        return true;
                    default:
                        return false;
                }

            }

            @Override
            public void onItemCheckedStateChanged(ActionMode mode,
                                                  int position, long id, boolean checked) {
                if (checked) {
                    selectionCounter++;
                    ((AdapterDelete) getListViewProgramacao().getAdapter()).selectedItem(position, position);

                } else {
                    selectionCounter--;
                    ((AdapterDelete)getListViewProgramacao().getAdapter()).removeSelection(position);
                }
                if (selectionCounter > 1){
                    mode.setTitle(selectionCounter + " Selecionados");
                }else{
                    mode.setTitle(selectionCounter + " Selecionado");
                }

            }
        });
    }
    //metodo responsável por buscar os dados das programacoes no banco (acesso remoto) e popular a lista de programacoes.
    private class PopulaProgramacoesTask extends AsyncTask<Celula, Void, Integer> {
        ArrayList<Programacao> programacoes;
        ProgressDialog progressDialog;
        private final int RETORNO_SUCESSO = 0; //
        private final int FALHA_SQLEXCEPTION = 1; // provavel falha de conexao

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            programacoes = new ArrayList<Programacao>();
            //mostra janela de progresso
            progressDialog = ProgressDialog.show(ProgramacaoActivity.this, "Carregando", "Aguarde por favor...", true);
        }

        @Override
        protected Integer doInBackground(Celula... celulas) {
            try {

                if( getSPCelula() != null){
                    mListaProgramacoes = new ProgramacaoDAO().retornaProgramacoes(celulas[0]);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return FALHA_SQLEXCEPTION;
            }
            return RETORNO_SUCESSO;
        }

        //metodo executado apos finalizacao do metodo doInBackground. Sendo assim ja e possivel usar a lista de roteiros
        // retornada
        @Override
        protected void onPostExecute(Integer resultadoLogin) {
            progressDialog.dismiss();
            switch (resultadoLogin) {
                case RETORNO_SUCESSO:
                    if (mListaProgramacoes.size() > 0) {
                        getImageViewListaVazia().setVisibility(View.GONE);
                        getListViewProgramacao().setVisibility(View.VISIBLE);
                    }else {
                        getImageViewListaVazia().setVisibility(View.VISIBLE);
                        getListViewProgramacao().setVisibility(View.GONE);
                    }
                    getListViewProgramacao().setAdapter(new AdapterDelete<Programacao>(getApplicationContext(), mListaProgramacoes));
                    break;
                case FALHA_SQLEXCEPTION:
                    //nao foi possivel carregar as programacoes, sendo assim uma mensagem de erro eh exibida e a tela eh encerrada
                    Utils.mostraMensagemDialog(ProgramacaoActivity.this, "Não foi possível carregar as programações. Verifique sua conexão e tente novamente.",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            });
                    break;
            }
            super.onPostExecute(resultadoLogin);
        }
    }

    private class RemoveProgramacaoTask extends AsyncTask<Void, Void, Integer> {
        ProgressDialog progressDialog;
        private final int DELETE_SUCESSO = 0;
        private final int DELETE_FALHOU = 1;
        private final int DELETE_FALHA_SQLEXCEPTION = 2;

        private ArrayList<Programacao> programacaoRemover;
        private Runnable tarefa;

        public RemoveProgramacaoTask(ArrayList<Programacao> programacoesRemover, Runnable tarefa) {
            this.programacaoRemover = programacoesRemover;
            this.tarefa = tarefa;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mostra janela de progresso
            progressDialog = ProgressDialog.show(ProgramacaoActivity.this, "Aguarde por favor", "Removendo dados...", true);
        }

        @Override
        protected Integer doInBackground(Void... params) {
            if (programacaoRemover.size() > 0) {
                try {
                    if (new ProgramacaoDAO().deletaProgramacoes(programacaoRemover)) {
                        return DELETE_SUCESSO;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    return DELETE_FALHA_SQLEXCEPTION;
                    //TODO LOG ERRO
                }
            } else {
                return DELETE_FALHOU;
            }
            return DELETE_FALHOU;
        }

        @Override
        protected void onPostExecute(Integer resultadoInsercao) {
            progressDialog.dismiss();
            switch (resultadoInsercao) {
                case DELETE_SUCESSO:
                    Utils.showMessageToast(ProgramacaoActivity.this, "Programação(s) removida(s) com sucesso.");
                    ((AdapterDelete)getListViewProgramacao().getAdapter()).removeItem();
                    tarefa.run();
                    break;
                case DELETE_FALHA_SQLEXCEPTION:
                    Utils.showMsgAlertOK(ProgramacaoActivity.this,"Erro de Conexão", "Não foi possível finalizar a operação. Verifique sua conexão com a internet e tente novamente.", TipoMsg.ERRO);
                    break;
            }
            super.onPostExecute(resultadoInsercao);
        }
    }


    private ListView getListViewProgramacao() {
        if (listview_programacoes == null) {
            listview_programacoes = (ListView) findViewById(R.id.listview_programacoes);
        }
        return listview_programacoes;
    }
    private ImageView getImageViewListaVazia() {
        if (imageViewListaVazia == null) {
            imageViewListaVazia = (ImageView) findViewById(R.id.imageview_lista_vazia);
        }
        return imageViewListaVazia;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
        switch (adapterView.getId()) {
            case R.id.listview_programacoes:
                Programacao programacao = (Programacao) adapterView.getItemAtPosition(pos);
                Intent intent = new Intent(this, ProgramacaoSelecionadaActivity.class);
                intent.putExtra(PROGRAMACAO_SELECIONADA, programacao);
                startActivity(intent);
                break;
        }
    }

}