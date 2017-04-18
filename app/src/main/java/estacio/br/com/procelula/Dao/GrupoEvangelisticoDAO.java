package estacio.br.com.procelula.Dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import estacio.br.com.procelula.Dados.Celula;
import estacio.br.com.procelula.Dados.GrupoEvangelistico;
import estacio.br.com.procelula.Utils.ConnectionManager;


public class GrupoEvangelisticoDAO {

    private final String TABELA = "grupos_evangelisticos"; //Nome da tabela do banco dados que a classe vai trabalhar

    public ArrayList<GrupoEvangelistico> retornaGruposEvangelisticos(Celula celula) throws SQLException {
        ArrayList<GrupoEvangelistico> gruposEvangelisticos = new ArrayList<>();
        GrupoEvangelistico grupoEvangelistico = null;
        ResultSet rs = null;
        PreparedStatement statement = null;
        Connection conexao = null;

        conexao = ConnectionManager.getConnection();

        try {
            statement = conexao.prepareStatement(
                    " SELECT id, ges_celula_id, nome, datediff(now(), data) as dias "+
                    "   FROM grupos_evangelisticos                                            " +
                    " WHERE ges_celula_id = ?  ORDER BY data DESC ;                           ");

            statement.setInt(1, celula.getId_celula());
            rs = statement.executeQuery();
            while (rs.next()) {
                grupoEvangelistico = new GrupoEvangelistico();
                grupoEvangelistico.setId_ge(rs.getInt(1));
                grupoEvangelistico.setId_celula(rs.getInt(2));
                grupoEvangelistico.setNome(rs.getString(3));
                grupoEvangelistico.setDias(rs.getInt(4));
                gruposEvangelisticos.add(grupoEvangelistico);
            }
        } catch (Exception e) {
            //TODO LOG ERRO
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
                //TODO LOG ERRO
                mysqlEx.printStackTrace();
            }
        }
        return gruposEvangelisticos;
    }

    public boolean insereGE(GrupoEvangelistico grupoEvangelistico) throws SQLException {
        Connection conexao = null;
        PreparedStatement statement = null;
        boolean inserido = false;
        conexao = ConnectionManager.getConnection();
        try {
            statement = conexao.prepareStatement(
                    " INSERT INTO grupos_evangelisticos (ges_celula_id, nome, data) values (?,?,NOW())");
            statement.setInt   (1, grupoEvangelistico.getId_celula());
            statement.setString(2, grupoEvangelistico.getNome());

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

    public boolean deletaGe(List<GrupoEvangelistico> grupoEvangelisticos) throws SQLException {
        Connection conexao = null;
        PreparedStatement statement = null;
        boolean sucesso = false;
        conexao = ConnectionManager.getConnection();
        try {

            String delCommand = " DELETE FROM grupos_evangelisticos WHERE id IN (";
            for (int i = 0; i < grupoEvangelisticos.size() - 1; i++) {
                delCommand = delCommand.concat("?,");
            }
            delCommand = delCommand.concat("?)");

            statement = conexao.prepareStatement(delCommand);

            for (int i = 1; i <= grupoEvangelisticos.size(); i++) {
                statement.setInt(i, grupoEvangelisticos.get(i-1).getId_ge());
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

