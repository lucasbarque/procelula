package estacio.br.com.procelula.Dao;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import estacio.br.com.procelula.Dados.Celula;
import estacio.br.com.procelula.Utils.ConnectionManager;
import estacio.br.com.procelula.Utils.Utils;

public class CelulaDAO {
    private final String TABELA = "celulas";

    //retorna celula por meio do id
    public Celula retornaCelulaPorId(int idCelula) throws SQLException {
        Celula celula = null;
        ResultSet rs = null;
        PreparedStatement statement = null;
        Connection conexao = null;

        conexao = ConnectionManager.getConnection();
        try {
            //Garantir no banco que o login será único
            statement = conexao.prepareStatement(
                    " SELECT id, nome, lider, dia, horario, local, jejum, periodo, versiculo  " +
                            "   FROM celulas                                                                             " +
                            " WHERE id = ?                                                                       ");

            statement.setInt(1, idCelula);
            rs = statement.executeQuery();

            while (rs.next()) {
                celula = new Celula();
                celula.setId_celula(rs.getInt(1));
                celula.setNome(rs.getString(2));
                celula.setLider(rs.getString(3));
                celula.setDia(rs.getString(4));
                celula.setHorario(Utils.converteHoraApp(rs.getString(5)));
                celula.setLocal_celula(rs.getString(6));
                celula.setDia_jejum(rs.getString(7));
                celula.setPeriodo(rs.getString(8));
                celula.setVersiculo(rs.getString(9));
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
        return celula;
    }

    public ArrayList<Celula> retornaCelulas() throws SQLException {
        ArrayList<Celula> celulas = new ArrayList<>();
        Celula celula = null;
        ResultSet rs = null;
        PreparedStatement statement = null;
        Connection conexao = null;

        conexao = ConnectionManager.getConnection();
        try {
            //Garantir no banco que o login será único
            statement = conexao.prepareStatement(
                    " SELECT id, nome, lider, dia, horario, local, jejum, periodo, versiculo  " +
                    "   FROM celulas                                                                    " +
                    "  ORDER BY nome                                                                   ");

            rs = statement.executeQuery();

            while (rs.next()) {
                celula = new Celula();
                celula.setId_celula(rs.getInt(1));
                celula.setNome(rs.getString(2));
                celula.setLider(rs.getString(3));
                celula.setDia(rs.getString(4));
                celula.setHorario(Utils.converteHoraApp(rs.getString(5)));
                celula.setLocal_celula(rs.getString(6));
                celula.setDia_jejum(rs.getString(7));
                celula.setPeriodo(rs.getString(8));
                celula.setVersiculo(rs.getString(9));
                celulas.add(celula);
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
        return celulas;
    }

    //metodo que retorna a imagem da celula passada por parametro e salva no caminho especificado
    //TODO verificar possibilidade de implementacao de um cache de imagens
//    public boolean retornaCelulaImagem(Celula celula, String caminhoArquivo, String nomeArquivo) throws SQLException {
//        InputStream isCelula = null;
//        ResultSet rs = null;
//        PreparedStatement statement = null;
//        Connection conexao = null;
//
//        conexao = ConnectionManager.getConnection();
//        try {
//            statement = conexao.prepareStatement(
//                    " SELECT imagem            " +
//                            "   FROM celulas       " +
//                            "  WHERE id = ? ");
//
//            statement.setInt(1, celula.getId_celula());
//            rs = statement.executeQuery();
//
//            if (rs.next()) {
//                isCelula = rs.getBinaryStream(1);
//            }
//
//            Utils.downloadImagemBanco(caminhoArquivo, isCelula, nomeArquivo);
//
//            return true;
//        } catch (Exception e) {
//            //TODO LOG ERRO
//            e.printStackTrace();
//        } finally {
//            try {
//                if (rs != null) {
//                    rs.close();
//                }
//                if (statement != null) {
//                    statement.close();
//                }
//                if (conexao != null) {
//                    conexao.close();
//                }
//            } catch (Exception mysqlEx) {
//                //TODO LOG ERRO
//                mysqlEx.printStackTrace();
//            }
//        }
//        return false;
//    }
}
