package estacio.br.com.procelula.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import estacio.br.com.procelula.Dados.Aviso;
import estacio.br.com.procelula.Dados.Celula;
import estacio.br.com.procelula.Utils.ConnectionManager;

/**
 * Created by barque on 24/03/2016.
 */
public class AvisoDAO {

    private final String TABELA = "avisos"; //Nome da tabela do banco dados que a classe vai trabalhar

    public ArrayList<Aviso> retornaAvisos(Celula celula) throws SQLException {
        ArrayList<Aviso> avisos = new ArrayList<>();
        Aviso aviso = null;
        ResultSet rs = null;
        PreparedStatement statement = null;
        Connection conexao = null;
        conexao = ConnectionManager.getConnection();

        try {
            statement = conexao.prepareStatement(
                    " SELECT id, avisos_celula_id, titulo, conteudo "+
                            "   FROM avisos                                          " +
                            " WHERE avisos_celula_id = ? ORDER BY id desc;            ");

            statement.setInt(1, celula.getId_celula());
            rs = statement.executeQuery();
            while (rs.next()) {
                aviso = new Aviso();
                aviso.setId_aviso(rs.getInt(1));
                aviso.setId_celula(rs.getInt(2));
                aviso.setTitulo(rs.getString(3));
                aviso.setConteudo(rs.getString(4));
                avisos.add(aviso);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (conexao != null) {
                    conexao.close();
                }
            } catch (Exception mysqlEx) {
                mysqlEx.printStackTrace();
            }
        }
        return avisos;
    }

    public boolean insereAviso(Aviso aviso) throws SQLException {
        Connection conexao = null;
        PreparedStatement statement = null;
        boolean inserido = false;
        conexao = ConnectionManager.getConnection();
        try {
            statement = conexao.prepareStatement(
                    " INSERT INTO avisos (avisos_celula_id, titulo, conteudo) values (?,?,?)");
            statement.setInt   (1, aviso.getId_celula());
            statement.setString(2, aviso.getTitulo());
            statement.setString(3, aviso.getConteudo());

            int row = statement.executeUpdate();
            if (row > 0) {
                inserido = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            //TODO LOG ERRO
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conexao != null) {
                    conexao.close();
                }
            } catch (Exception mysqlEx) {
                System.out.println(mysqlEx.toString());
                //TODO LOG ERRO
            }
        }
        return inserido;
    }

    public boolean deletaAvisos(List<Aviso> avisos) throws SQLException {
        Connection conexao = null;
        PreparedStatement statement = null;
        boolean sucesso = false;
        conexao = ConnectionManager.getConnection();
        try {

            String delCommand = " DELETE FROM avisos WHERE id IN (";
            for (int i = 0; i < avisos.size() - 1; i++) {
                delCommand = delCommand.concat("?,");
            }
            delCommand = delCommand.concat("?)");

            statement = conexao.prepareStatement(delCommand);

            for (int i = 1; i <= avisos.size(); i++) {
                statement.setInt(i, avisos.get(i-1).getId_aviso());
            }

            int row = statement.executeUpdate();
            if (row > 0) {
                sucesso = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            //TODO LOG ERRO
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conexao != null) {
                    conexao.close();
                }
            } catch (Exception mysqlEx) {
                System.out.println(mysqlEx.toString());
                //TODO LOG ERRO
            }
        }
        return sucesso;
    }
}

