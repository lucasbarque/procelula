package estacio.br.com.procelula.Dados;

public class Usuario {
    public static final int PERMISSAO_BASICA = 0;
    public static final int PERMISSAO_LIDER  = 1;
    public static final int PERMISSAO_PASTOR = 2;

    public static final String ID_USUARIO_SP       = "ID_USUARIO";
    public static final String NOME_SP             = "NOME";
    public static final String SOBRENOME_SP        = "SOBRENOME";
    public static final String DATA_NASCIMENTO_SP  = "DATA_NASCIMENTO";
    public static final String LOGIN_SP            = "LOGIN";
    public static final String PERMISSAO_SP        = "PERMISSAO;";

    private int id;
    private String nome;
    private String senha;
    private String sobrenome;
    private String dataNascimento;
    private String email;
    private Celula celula;
    //private Escala escala; //TODO
    private String login;
    private int permissao;

    public Usuario() {
        permissao = PERMISSAO_BASICA;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }



    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Celula getCelula() {
        return celula;
    }

    public void setCelula(Celula celula) {
        this.celula = celula;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getPermissao() {
        return permissao;
    }

    public void setPermissao(int permissao) {
        this.permissao = permissao;
    }

    @Override
    public String toString() {
        return getNome() +  " " + getSobrenome() + " - Dia " + getDataNascimento();
    }



}
