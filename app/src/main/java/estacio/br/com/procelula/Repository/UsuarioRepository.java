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

import estacio.br.com.procelula.Dados.Celula;
import estacio.br.com.procelula.Dados.Usuario;
import estacio.br.com.procelula.Utils.Constantes;

/**
 * Created by Barque on 28/01/2017.
 */

public class UsuarioRepository extends SQLiteOpenHelper {
    public UsuarioRepository(Context context){
        super(context, Constantes.BD_NOME, null, Constantes.BD_VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE tb_usuarios ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "usuarios_celula_id INTEGER,"+
                "nome TEXT(255) NOT NULL,"+
                "sobrenome TEXT(255) NOT NULL,"+
                "login TEXT(100) NOT NULL,"+
                "senha TEXT(255) NOT NULL,"+
                "email TEXT(100) NOT NULL,"+
                "nascimento DATE NOT NULL,"+
                "perfil INTEGER(1) NOT NULL,"+
                "token TEXT(255) NOT NULL,"+
                "created DATETIME DEFAULT CURRENT_TIMESTAMP,"+
                "modified DATETIME DEFAULT CURRENT_TIMESTAMP";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        onCreate(sqLiteDatabase);
    }

    public void salvarUsuario(Usuario usuario){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = getContentValuesUsuario(usuario);
        db.insert("tb_usuarios", null, contentValues);
    }

    @NonNull
    private ContentValues getContentValuesUsuario(Usuario usuario){
        ContentValues contentValues = new ContentValues();
        contentValues.put("usuarios_celula_id", usuario.getCelula().getId_celula());
        contentValues.put("nome", usuario.getNome());
        contentValues.put("sobrenome", usuario.getSobrenome());
        contentValues.put("login", usuario.getLogin());
        contentValues.put("senha", usuario.getSenha());
        contentValues.put("email", usuario.getEmail());
        contentValues.put("nascimento", usuario.getDataNascimento());
        contentValues.put("perfil", usuario.getPermissao());
        contentValues.put("token", "");
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

    public List<Usuario> listarUsuarios(){
        List<Usuario> lista = new ArrayList<Usuario>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("tb_usuarios", null, null, null, null, null, "created");

        while(cursor.moveToNext()){
            Usuario usuario = new Usuario();
            setUsuarioFromCursor(cursor, usuario);

            lista.add(usuario);
        }
        return lista;
    }

    public Usuario consultarUsuarioPorId(int Id_usuario){
        Usuario usuario = new Usuario();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query("tb_usuarios", null, "id = ?", new String[]{String.valueOf(Id_usuario)}, null, null, "created");

        if(cursor.moveToNext()){
            setUsuarioFromCursor(cursor, usuario);
        }
        return usuario;
    }

    public void setUsuarioFromCursor(Cursor cursor, Usuario usuario){
        usuario.setId(cursor.getInt(cursor.getColumnIndex("id")));

        Celula celula = new Celula();
        celula.setId_celula(cursor.getInt(cursor.getColumnIndex("usuarios_celula_id")));
        usuario.setCelula(celula);

        usuario.setNome(cursor.getString(cursor.getColumnIndex("nome")));
        usuario.setSobrenome(cursor.getString(cursor.getColumnIndex("sobrenome")));
        usuario.setLogin(cursor.getString(cursor.getColumnIndex("login")));
        usuario.setSenha(cursor.getString(cursor.getColumnIndex("senha")));
        usuario.setEmail(cursor.getString(cursor.getColumnIndex("email")));
        usuario.setDataNascimento(cursor.getString(cursor.getColumnIndex("nascimento")));
        usuario.setPermissao(cursor.getInt(cursor.getColumnIndex("perfil")));

    }
    public void atualizarUsuario(Usuario usuario){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = getContentValuesUsuario(usuario);
        db.update("tb_usuarios", contentValues, "id = ?", new String[]{String.valueOf(usuario.getId())});
    }
    public void removerUsuarioPorId(int Id_usuario){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete("tb_usuarios", "id = ?", new String[]{String.valueOf(Id_usuario)});
    }

}
