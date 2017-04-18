package estacio.br.com.procelula.Dados;

import java.io.Serializable;


public class GrupoEvangelistico implements Serializable {

    private int id_ge;
    private int id_celula;
    private String nome;
    private int dias;

    public int getId_ge() {
        return id_ge;
    }

    public void setId_ge(int id_ge) {
        this.id_ge = id_ge;
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

    public int getDias() {
        return dias;
    }

    public void setDias(int dias) {
        this.dias = dias;
    }

    @Override
    public String toString() {
        return getNome() + " (" + Integer.toString(getDias()) + " dias)" ;
    }
}
