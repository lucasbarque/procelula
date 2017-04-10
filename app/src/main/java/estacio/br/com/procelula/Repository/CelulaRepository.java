package estacio.br.com.procelula.Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;



import java.util.ArrayList;
import java.util.List;

import estacio.br.com.procelula.Dados.Celula;
import estacio.br.com.procelula.Utils.Constantes;


/**
 * Created by Barque on 28/01/2017.
 */

public class CelulaRepository extends SQLiteOpenHelper {

    public CelulaRepository(Context context){
        super(context, Constantes.BD_NOME, null, Constantes.BD_VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE tb_celulas ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "nome TEXT(255) NOT NULL,"+
                "lider TEXT(255) NOT NULL,"+
                "dia TEXT(100) NOT NULL,"+
                "horario TEXT(255) NOT NULL,"+
                "local TEXT NOT NULL,"+
                "jejum TEXT(50) NOT NULL,"+
                "periodo TEXT(50) NOT NULL,"+
                "versiculo TEXT NOT NULL";

        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        onCreate(sqLiteDatabase);
    }

    public void salvarCelula(Celula celula){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = getContentValuesCelula(celula);
        db.insert("tb_celulas", null, contentValues);
    }

    @NonNull
    private ContentValues getContentValuesCelula(Celula celula){
        ContentValues contentValues = new ContentValues();
        contentValues.put("nome", celula.getNome());
        contentValues.put("lider", celula.getLider());
        contentValues.put("dia", celula.getDia());
        contentValues.put("horario", celula.getHorario());
        contentValues.put("local", celula.getLocal_celula());
        contentValues.put("jejum", celula.getDia_jejum());
        contentValues.put("periodo", celula.getPeriodo());
        contentValues.put("versiculo", celula.getVersiculo());
        return contentValues;
    }

    public List<Celula> listarCelulas(){
        List<Celula> lista = new ArrayList<Celula>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("tb_celulas", null, null, null, null, null, "nome");

        while(cursor.moveToNext()){
            Celula celula = new Celula();
            setCelulaFromCursor(cursor, celula);

            lista.add(celula);
        }
        return lista;
    }

    public void setCelulaFromCursor(Cursor cursor, Celula celula){
        celula.setId_celula(cursor.getInt(cursor.getColumnIndex("id")));
        celula.setNome(cursor.getString(cursor.getColumnIndex("nome")));
        celula.setLider(cursor.getString(cursor.getColumnIndex("lider")));
        celula.setDia(cursor.getString(cursor.getColumnIndex("dia")));
        celula.setHorario(cursor.getString(cursor.getColumnIndex("horario")));
        celula.setLocal_celula(cursor.getString(cursor.getColumnIndex("local")));
        celula.setDia_jejum(cursor.getString(cursor.getColumnIndex("jejum")));
        celula.setPeriodo(cursor.getString(cursor.getColumnIndex("periodo")));
        celula.setVersiculo(cursor.getString(cursor.getColumnIndex("versiculo")));

    }
    public void atualizarCelula(Celula celula){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = getContentValuesCelula(celula);
        db.update("tb_celulas", contentValues, "id = ?", new String[]{String.valueOf(celula.getId_celula())});
    }
    public void removerCelulaPorId(int Id_celula){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("tb_celulas", "id = ?", new String[]{String.valueOf(Id_celula)});
    }

}
