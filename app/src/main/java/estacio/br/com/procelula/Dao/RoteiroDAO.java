package estacio.br.com.procelula.Dao;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import estacio.br.com.procelula.Dados.Roteiro;
import estacio.br.com.procelula.Utils.ConnectionManager;
import estacio.br.com.procelula.Utils.Utils;


public class RoteiroDAO {
    public ArrayList<Roteiro> retornaRoteiros() throws SQLException {
        ArrayList<Roteiro> roteiros = new ArrayList<>();
        Roteiro roteiro = null;
        ResultSet rs = null;
        PreparedStatement statement = null;
        Connection conexao = null;

        conexao = ConnectionManager.getConnection();
        try {
            statement = conexao.prepareStatement(
                    " SELECT id, titulo, data " +
                    "   FROM roteiros                         " +
                    "  ORDER BY data desc             ");

            rs = statement.executeQuery();

            while (rs.next()) {
                roteiro = new Roteiro();
                roteiro.setId_roteiro(rs.getInt(1));
                roteiro.setTitulo(rs.getString(2));
                roteiro.setData_roteiro(Utils.converteDataApp(rs.getString(3)));
                roteiros.add(roteiro);
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
        return roteiros;
    }

    //TODO terminar metodo com retorno que indique se a imagem foi retornada com sucesso e inserir em classe
    // separada tratamento de armazenamento de cache
    public int retornaRoteiroImagem(Roteiro roteiro, String caminhoSalvaArquivo) throws SQLException {
        InputStream isRoteiro = null;
        ResultSet rs = null;
        PreparedStatement statement = null;
        Connection conexao = null;

        conexao = ConnectionManager.getConnection();
        try {
            statement = conexao.prepareStatement(
                    " SELECT roteiros        " +
                    "   FROM roteiros       " +
                    "  WHERE id = ? ");

            statement.setInt(1, roteiro.getId_roteiro());
            rs = statement.executeQuery();

            if (rs.next()) {
                isRoteiro = rs.getBinaryStream(1);
            }

            OutputStream outputStream = new FileOutputStream(new File(caminhoSalvaArquivo + "/teste.jpg"));
            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = isRoteiro.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
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
        return 0;
    }
}
