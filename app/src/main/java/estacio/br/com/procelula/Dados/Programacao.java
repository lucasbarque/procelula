package estacio.br.com.procelula.Dados;

import android.graphics.Bitmap;
import android.widget.TextView;

import java.io.Serializable;

/**
 * Created by barque on 20/03/2016.
 */
public class Programacao implements Serializable {

    public static String DIRETORIO_IMAGENS_PROGRAMACAO = "/programacao";
    public static String NOME_PADRAO_IMAGEM_PROGRAMACAO = "progImg.png";
    public static String NOME_PADRAO_IMAGEM_PROGRAMACAO_ENVIAR = "progImgEnv.png";

    private int id_programacao;
    private int id_celula;
    private String nome;
    private String data_prog;
    private String horario;
    private String local_prog;
    private String telefone;
    private String valor;
    private Bitmap imagem;
    private TextView NaoExiste;

    public TextView getNaoExiste() {
        return NaoExiste;
    }

    public void setNaoExiste(TextView naoExiste) {
        NaoExiste = naoExiste;
    }

    public int getId_programacao() {
        return id_programacao;
    }

    public void setId_programacao(int id_programacao) {
        this.id_programacao = id_programacao;
    }

    public int getId_celula() {
        return id_celula;
    }

    public void setId_celula(int id_celula) {
        this.id_celula = id_celula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getData_prog() {
        return data_prog;
    }

    public void setData_prog(String data_prog) {
        this.data_prog = data_prog;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getLocal_prog() {
        return local_prog;
    }

    public void setLocal_prog(String local_prog) {
        this.local_prog = local_prog;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public Bitmap getImagem() {
        return imagem;
    }

    public void setImagem(Bitmap imagem) {
        this.imagem = imagem;
    }

    @Override
    public String toString() {
        return getNome();
    }
}
