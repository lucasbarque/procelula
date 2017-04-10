package estacio.br.com.procelula.Dados;


public class Celula {
    public static final String ID_CELULA_SP             = "ID_CELULA_SP";
    public static final String NOME_CELULA_SP           = "NOME_CELULA_SP";
    public static final String LIDER_CELULA_SP          = "LIDER_CELULA_SP";
    public static final String DIA_CELULA_SP            = "DIA_CELULA_SP";
    public static final String HORARIO_CELULA_SP        = "HORARIO_CELULA_SP";
    public static final String LOCAL_CELULA_SP          = "LOCAL_CELULA_SP";
    public static final String DIA_JEJUM_CELULA_SP      = "DIA_JEJUM_CELULA_SP";
    public static final String PERIODO_CELULA_SP        = "PERIODO_CELULA_SP";
    public static final String VERSICULO_CELULA_SP      = "VERSICULO_CELULA_SP";
//    public static final String CAMINHO_IMAGEM_CELULA_SP = "CAMINHO_IMAGEM_CELULA_SP";

    public static final int DIA_SEMANA_NENHUM = 0;
    public static final int DIA_SEMANA_DOMINGO = 1;
    public static final int DIA_SEMANA_SEGUNDA = 2;
    public static final int DIA_SEMANA_TERCA = 3;
    public static final int DIA_SEMANA_QUARTA = 4;
    public static final int DIA_SEMANA_QUINTA = 5;
    public static final int DIA_SEMANA_SEXTA = 6;
    public static final int DIA_SEMANA_SABADO = 7;

//    public static String DIRETORIO_IMAGENS_CELULA = "/celula";
//    public static String NOME_PADRAO_IMAGEM_CELULA = "celImg.png";

    private int id_celula;
    private String nome;
    private String lider;
    private String dia;
    private String horario;
    private String local_celula;
    private String dia_jejum;
    private String periodo;
    private String versiculo;
//    private Blob imagem;

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

    public String getLider() {
        return lider;
    }

    public void setLider(String lider) {
        this.lider = lider;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getLocal_celula() {
        return local_celula;
    }

    public void setLocal_celula(String local_celula) {
        this.local_celula = local_celula;
    }

    public String getDia_jejum() {
        return dia_jejum;
    }

    public void setDia_jejum(String dia_jejum) {
        this.dia_jejum = dia_jejum;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getVersiculo() {
        return versiculo;
    }

    public void setVersiculo(String versiculo) {
        this.versiculo = versiculo;
    }

//    public Blob getImagem() {
//        return imagem;
//    }
//
//    public void setImagem(Blob imagem) {
//        this.imagem = imagem;
//    }

    @Override
    public String toString() {
        return getNome();
    }

    public String converteDiaCelula() {
        if (getDia() != null) {
            try {
                return converteDiaSemana(Integer.parseInt(getDia()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String converteDiaJejum() {
        if (getDia_jejum() != null) {
            try {
                return converteDiaSemana(Integer.parseInt(getDia_jejum()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private String converteDiaSemana(int diaSemana) {
        switch (diaSemana) {
            case DIA_SEMANA_NENHUM:
                return "";
            case DIA_SEMANA_DOMINGO:
                return "Domingo";
            case DIA_SEMANA_SEGUNDA:
                return "Segunda";
            case DIA_SEMANA_TERCA:
                return "Terça";
            case DIA_SEMANA_QUARTA:
                return "Quarta";
            case DIA_SEMANA_QUINTA:
                return "Quinta";
            case DIA_SEMANA_SEXTA:
                return "Sexta";
            case DIA_SEMANA_SABADO:
                return "Sábado";
        }
        return null;
    }
}
