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

import estacio.br.com.procelula.Dados.GrupoEvangelistico;
import estacio.br.com.procelula.Utils.Constantes;



public class GERepository extends SQLiteOpenHelper {

    public GERepository(Context context){
        super(context, Constantes.BD_NOME, null, Constantes.BD_VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE tb_ges ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "ges_celula_id INTEGER,"+
                "nome TEXT(255) NOT NULL,"+
                "dias INT(3) NOT NULL";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        onCreate(sqLiteDatabase);
    }

    public void salvarGE(GrupoEvangelistico ge){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = getContentValuesGE(ge);
        db.insert("tb_ges", null, contentValues);
    }

    @NonNull
    private ContentValues getContentValuesGE(GrupoEvangelistico ge){
        ContentValues contentValues = new ContentValues();
        contentValues.put("ges_celula_id", ge.getId_ge());
        contentValues.put("nome", ge.getNome());
        contentValues.put("dias", ge.getDias());
        return contentValues;
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public List<GrupoEvangelistico> listarGES(){
        List<GrupoEvangelistico> lista = new ArrayList<GrupoEvangelistico>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("tb_ges", null, null, null, null, null, "data");

        while(cursor.moveToNext()){
            GrupoEvangelistico ge = new GrupoEvangelistico();
            setAvisoFromCursor(cursor, ge);

            lista.add(ge);
        }
        return lista;
    }

    public GrupoEvangelistico consultarGEPorId(int idGE){
        GrupoEvangelistico ge = new GrupoEvangelistico();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query("tb_ges", null, "id = ?", new String[]{String.valueOf(idGE)}, null, null, "data");

        if(cursor.moveToNext()){
            setAvisoFromCursor(cursor, ge);
        }
        return ge;
    }

    public void setAvisoFromCursor(Cursor cursor, GrupoEvangelistico ge){
        ge.setId_ge(cursor.getInt(cursor.getColumnIndex("id")));
        ge.setId_celula(cursor.getInt(cursor.getColumnIndex("ges_celula_id")));
        ge.setNome(cursor.getString(cursor.getColumnIndex("nome")));
        ge.setDias(cursor.getInt(cursor.getColumnIndex("dias")));
    }
    public void atualizarGE(GrupoEvangelistico ge){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = getContentValuesGE(ge);
        db.update("tb_ges", contentValues, "id = ?", new String[]{String.valueOf(ge.getId_ge())});
    }
    public void removerGEPorId(int idGE){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete("tb_ges", "id = ?", new String[]{String.valueOf(idGE)});
    }
}
