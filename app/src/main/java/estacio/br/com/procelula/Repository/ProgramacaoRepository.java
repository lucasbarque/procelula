package estacio.br.com.procelula.Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;



import java.util.ArrayList;
import java.util.List;

import estacio.br.com.procelula.Dados.Programacao;
import estacio.br.com.procelula.Utils.Constantes;


public class ProgramacaoRepository extends SQLiteOpenHelper {

    public ProgramacaoRepository(Context context){
        super(context, Constantes.BD_NOME, null, Constantes.BD_VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE tb_programacoes ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "programacoes_celula_id INTEGER,"+
                "nome TEXT(50) NOT NULL,"+
                "data DATE NOT NULL,"+
                "horario TEXT(50) NOT NULL,"+
                "local TEXT NOT NULL,"+
                "telefone TEXT(20) NOT NULL,"+
                "valor TEXT(20) NOT NULL";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        onCreate(sqLiteDatabase);
    }

    public void salvarProgramacao(Programacao programacao){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = getContentValuesProgramacao(programacao);
        db.insert("tb_programacoes", null, contentValues);
    }

    @NonNull
    private ContentValues getContentValuesProgramacao(Programacao programacao){
        ContentValues contentValues = new ContentValues();
        contentValues.put("programacoes_celula_id", programacao.getId_celula());
        contentValues.put("nome", programacao.getNome());
        contentValues.put("data", programacao.getData_prog());
        contentValues.put("horario", programacao.getHorario());
        contentValues.put("local", programacao.getLocal_prog());
        contentValues.put("telefone", programacao.getTelefone());
        contentValues.put("valor", programacao.getValor());
        return contentValues;
    }

    public List<Programacao> listarProgramacaos(){
        List<Programacao> lista = new ArrayList<Programacao>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("tb_programacoes", null, null, null, null, null, "nome");

        while(cursor.moveToNext()){
            Programacao programacao = new Programacao();
            setProgramacaoFromCursor(cursor, programacao);

            lista.add(programacao);
        }
        return lista;
    }

    public void setProgramacaoFromCursor(Cursor cursor, Programacao programacao){
        programacao.setId_programacao(cursor.getInt(cursor.getColumnIndex("id")));
        programacao.setNome(cursor.getString(cursor.getColumnIndex("nome")));
        programacao.setId_celula(cursor.getInt(cursor.getColumnIndex("programacoes_celula_id")));
        programacao.setData_prog(cursor.getString(cursor.getColumnIndex("data")));
        programacao.setHorario(cursor.getString(cursor.getColumnIndex("horario")));
        programacao.setLocal_prog(cursor.getString(cursor.getColumnIndex("local")));
        programacao.setTelefone(cursor.getString(cursor.getColumnIndex("telefone")));
        programacao.setValor(cursor.getString(cursor.getColumnIndex("valor")));

    }
    public void atualizarProgramacao(Programacao programacao){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = getContentValuesProgramacao(programacao);
        db.update("tb_programacoes", contentValues, "id = ?", new String[]{String.valueOf(programacao.getId_programacao())});
    }
    public void removerProgramacaoPorId(int Id_programacao){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("tb_programacoes", "id = ?", new String[]{String.valueOf(Id_programacao)});
    }

}
