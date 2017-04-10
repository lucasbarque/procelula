package estacio.br.com.procelula.Dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import estacio.br.com.procelula.Dados.Celula;
import estacio.br.com.procelula.Dados.Usuario;
import estacio.br.com.procelula.Utils.ConnectionManager;
import estacio.br.com.procelula.Utils.Utils;

/**
 * Created by thiago on 06/03/2016.
 */
public class UsuarioDAO {
    private final String TABELA = "usuarios";

    // retorna o usuario correspondente ao login
    public Usuario retornaUsuarioLogin(String login) throws SQLException {
        return retornaUsuarioLogin(login, null);
    }

    // retorna o usuario correspondente aos dados login e senha
    public Usuario retornaUsuarioLogin(String login, String senha) throws SQLException {
        Usuario usuario = null;
        Celula celula = null;
        ResultSet rs = null;
        PreparedStatement statement = null;
        Connection conexao = null;

        conexao = ConnectionManager.getConnection();
        try {
            String stmt =
                    " SELECT  u.id, u.usuarios_celula_id, u.nome,           " +
                    "         u.sobrenome, u.nascimento, u.login, u.perfil,     " +
                    "         c.nome, c.lider, c.dia, c.horario, c.local,        " +
                    "         c.jejum, c.periodo, c.versiculo            " +
                    "  FROM usuarios u left join celulas c on (u.usuarios_celula_id = c.id) " +
                    "  WHERE login = ?                                                  ";

            if (senha != null) {
                stmt = stmt.concat(" and senha = ?");
            }

            //TODO Garantir no banco que o login será único
            statement = conexao.prepareStatement(stmt);

            //TODO tratar sqlinjection
            statement.setString(1, login);

            if (senha != null)
                statement.setString(2, senha);

            rs = statement.executeQuery();

            if (rs.next()) {
                usuario = new Usuario();
                usuario.setId(rs.getInt(1));
                usuario.setNome(rs.getString(3));
                usuario.setSobrenome(rs.getString(4));
                usuario.setDataNascimento(rs.getString(5));
                usuario.setLogin(rs.getString(6));
                usuario.setPermissao(rs.getInt(7));

                celula = new Celula();
                celula.setId_celula(rs.getInt(2));
                celula.setNome(rs.getString(8));
                celula.setLider(rs.getString(9));
                celula.setDia(rs.getString(10));
                celula.setHorario(Utils.converteHoraApp(rs.getString(11)));
                celula.setLocal_celula(rs.getString(12));
                celula.setDia_jejum(rs.getString(13));
                celula.setPeriodo(rs.getString(14));
                celula.setVersiculo(rs.getString(15));
//                celula.setImagem(rs.getBlob(17));
                usuario.setCelula(celula);
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
        return usuario;
    }

    public ArrayList<Usuario> retornaAniversariantes(Celula celula) throws SQLException {
        Usuario usuario = null;
        ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
        ResultSet rs = null;
        PreparedStatement statement = null;
        Connection conexao = null;

        conexao = ConnectionManager.getConnection();
        try {
            statement = conexao.prepareStatement(
                    " SELECT   id, id_celula, nome,      " +
                    "          sobrenome, nascimento, login, permissao " +
                    "   FROM usuarios                                        " +
                    "   WHERE MONTH( nascimento ) = MONTH( NOW( ) )    " +
                    "       AND id_celula = ?                               " +
                    "   ORDER BY nascimento                            ");

            statement.setInt(1, celula.getId_celula());
            rs = statement.executeQuery();

            while (rs.next()) {
                usuario = new Usuario();
                usuario.setId(rs.getInt(1));
                usuario.setNome(rs.getString(4));
                usuario.setSobrenome(rs.getString(5));
                usuario.setDataNascimento(Utils.converteDataNiver(rs.getString(6)));
                usuario.setLogin(rs.getString(7));
                usuario.setPermissao(rs.getInt(8));
                usuarios.add(usuario);
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
        return usuarios;
    }

    public boolean insereUsuario(Usuario usuario) throws SQLException {
        Connection conexao = null;
        PreparedStatement statement = null;
        boolean inserido = false;
        conexao = ConnectionManager.getConnection();
        try {
            statement = conexao.prepareStatement(
                    " INSERT INTO usuarios (nome, sobrenome, nascimento, login, senha, id_celula, permissao) values (?,?,?,?,?,?,?)");
            statement.setString(1, usuario.getNome());
            statement.setString(2, usuario.getSobrenome());
            statement.setString(3, Utils.converteDataBanco(usuario.getDataNascimento()));
            statement.setString(4, usuario.getLogin());
            statement.setString(5, usuario.getSenha());
            statement.setInt(6, (usuario.getCelula() != null) ? usuario.getCelula().getId_celula() : -1);
            statement.setInt(7, usuario.getPermissao());

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

    // retorna os usuarios pertencentes a celula "celula"
    public ArrayList<Usuario> retornaUsuarios(Celula celula) throws SQLException {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        Usuario usuario = null;
        ResultSet rs = null;
        PreparedStatement statement = null;
        Connection conexao = null;

        conexao = ConnectionManager.getConnection();
        try {
            statement = conexao.prepareStatement(
                    " SELECT id, id_celula, nome, sobrenome, nascimento, login, senha, permissao " +
                    "   FROM usuarios                                                                          " +
                    "  WHERE id_celula = ?");

            statement.setInt(1, celula.getId_celula());

            rs = statement.executeQuery();

            while (rs.next()) {
                usuario = new Usuario();
                usuario.setId(rs.getInt(1));
                usuario.setCelula(celula);
                usuario.setNome(rs.getString(3));
                usuario.setSobrenome(rs.getString(4));
                usuario.setDataNascimento(rs.getString(5));
                usuario.setLogin(rs.getString(6));
                usuario.setSenha(rs.getString(7));
                usuario.setPermissao(rs.getInt(8));
                usuarios.add(usuario);
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
        return usuarios;
    }

}
