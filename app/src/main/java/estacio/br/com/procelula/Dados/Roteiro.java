package estacio.br.com.procelula.Dados;

import java.sql.Blob;

public class Roteiro {
    private int id_roteiro;
    private String titulo;
    private String data_roteiro;
    private Blob roteiro;

    public int getId_roteiro() {
        return id_roteiro;
    }

    public void setId_roteiro(int id_roteiro) {
        this.id_roteiro = id_roteiro;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getData_roteiro() {
        return data_roteiro;
    }

    public void setData_roteiro(String data_roteiro) {
        this.data_roteiro = data_roteiro;
    }

    public Blob getRoteiro() {
        return roteiro;
    }

    public void setRoteiro(Blob roteiro) {
        this.roteiro = roteiro;
    }

    @Override
    public String toString() {
        return getTitulo() + " - " + getData_roteiro();
    }
}
