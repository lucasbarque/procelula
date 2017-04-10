package estacio.br.com.procelula.Dados;

import android.widget.ListView;

import java.io.Serializable;

public class Aviso implements Serializable {
    private int id_aviso;
    private int id_celula;
    private String titulo;
    private String conteudo;
    private ListView avisoslist;



    public int getId_aviso() {
        return id_aviso;
    }

    public void setId_aviso(int id_aviso) {
        this.id_aviso = id_aviso;
    }

    public int getId_celula() {
        return id_celula;
    }

    public void setId_celula(int id_celula) {
        this.id_celula = id_celula;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public ListView getAvisoslist() {
        return avisoslist;
    }

    @Override
    public String toString() {
        return getTitulo();
    }

    public void setAvisoslist(ListView avisoslist) {
        this.avisoslist = avisoslist;
    }
}
