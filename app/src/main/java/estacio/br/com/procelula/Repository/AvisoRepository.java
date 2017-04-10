package estacio.br.com.procelula.Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import estacio.br.com.procelula.Dados.Aviso;
import estacio.br.com.procelula.Utils.Constantes;


public class AvisoRepository extends SQLiteOpenHelper {

    public AvisoRepository(Context context){
        super(context, Constantes.BD_NOME, null, Constantes.BD_VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE tb_avisos ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "avisos_celula_id INTEGER,"+
                "titulo TEXT(255) NOT NULL,"+
                "conteudo TEXT NOT NULL"+
                "created DATETIME DEFAULT CURRENT_TIMESTAMP,"+
                "modified DATETIME DEFAULT CURRENT_TIMESTAMP";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        onCreate(sqLiteDatabase);
    }

    public void salvarAviso(Aviso aviso){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = getContentValuesAviso(aviso);
        db.insert("tb_avisos", null, contentValues);
    }

    @NonNull
    private ContentValues getContentValuesAviso(Aviso aviso){
        ContentValues contentValues = new ContentValues();
        contentValues.put("avisos_celula_id", aviso.getId_celula());
        contentValues.put("titulo", aviso.getTitulo());
        contentValues.put("conteudo", aviso.getConteudo());
        contentValues.put("created",getDateTime());
        contentValues.put("modified",getDateTime());
        return contentValues;
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public List<Aviso> listarAvisos(){
        List<Aviso> lista = new ArrayList<Aviso>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("tb_avisos", null, null, null, null, null, "created");

        while(cursor.moveToNext()){
            Aviso aviso = new Aviso();
            setAvisoFromCursor(cursor, aviso);

            lista.add(aviso);
        }
        return lista;
    }

    public Aviso consultarAvisoPorId(int Id_aviso){
        Aviso aviso = new Aviso();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query("tb_avisos", null, "id = ?", new String[]{String.valueOf(Id_aviso)}, null, null, "created");

        if(cursor.moveToNext()){
            setAvisoFromCursor(cursor, aviso);
        }
        return aviso;
    }

 public void setAvisoFromCursor(Cursor cursor, Aviso aviso){
    aviso.setId_aviso(cursor.getInt(cursor.getColumnIndex("id")));
    aviso.setId_celula(cursor.getInt(cursor.getColumnIndex("avisos_celula_id")));
    aviso.setTitulo(cursor.getString(cursor.getColumnIndex("titulo")));
    aviso.setConteudo(cursor.getString(cursor.getColumnIndex("conteudo")));
 }
 public void atualizarAviso(Aviso aviso){
     SQLiteDatabase db = this.getWritableDatabase();
     ContentValues contentValues = getContentValuesAviso(aviso);
     db.update("tb_avisos", contentValues, "id = ?", new String[]{String.valueOf(aviso.getId_aviso())});
 }
 public void removerAvisoPorId(int Id_aviso){
     SQLiteDatabase db = this.getWritableDatabase();

     db.delete("tb_avisos", "id = ?", new String[]{String.valueOf(Id_aviso)});
 }
}
