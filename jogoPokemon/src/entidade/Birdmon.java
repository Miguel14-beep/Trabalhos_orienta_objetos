package entidade;

import java.util.ArrayList;

import main.SistemaElementos;

public class Birdmon {

    protected String nome;
    protected String tipo;

    public Habilidade[] habilidades;

    protected int nivel;
    protected int experiencia;

    protected int vida;
    protected int vidaMaxima;

    protected int ph;
    protected int phMaximo;

    protected int ataque;
    protected int defesa;
    protected int velocidade;

    protected int bonusVelocidade;

    public ArrayList<EfeitoAtivo> efeitosAtivos;

    public Birdmon(
            String nome,
            String tipo,
            int nivel,
            int vidaMaxima,
            int phMaximo,
            int ataque,
            int defesa,
            int velocidade) {

        this.nome = nome;
        this.tipo = tipo;

        this.nivel = nivel;
        this.experiencia = 0;

        this.vidaMaxima = vidaMaxima;
        this.vida = vidaMaxima;

        this.phMaximo = phMaximo;
        this.ph = phMaximo;

        this.ataque = ataque;
        this.defesa = defesa;
        this.velocidade = velocidade;

        this.bonusVelocidade = 0;

        this.efeitosAtivos = new ArrayList<>();
    }

    // =========================
    // VIDA
    // =========================

    public void receberDano(int dano) {

        if (dano < 0) {
            dano = 0;
        }

        vida -= dano;

        if (vida < 0) {
            vida = 0;
        }
    }

    public void curar(int quantidade) {

        if (quantidade <= 0) {
            return;
        }

        vida += quantidade;

        if (vida > vidaMaxima) {
            vida = vidaMaxima;
        }
    }

    // =========================
    // EXPERIÊNCIA
    // =========================

    public void ganharExperiencia(int quantidade) {

        if (quantidade <= 0) {
            return;
        }

        experiencia += quantidade;
    }

    // =========================
    // PH
    // =========================

    public boolean gastarPh(int quantidade) {

        if (quantidade < 0) {
            return false;
        }

        if (ph < quantidade) {
            return false;
        }

        ph -= quantidade;

        return true;
    }

    public void recuperarPh(int quantidade) {

        if (quantidade <= 0) {
            return;
        }

        ph += quantidade;

        if (ph > phMaximo) {
            ph = phMaximo;
        }
    }

    // =========================
    // VELOCIDADE
    // =========================

    public void adicionarBonusVelocidade(int quantidade) {

        bonusVelocidade += quantidade;

        if (bonusVelocidade < 0) {
            bonusVelocidade = 0;
        }
    }

    public void zerarBonusVelocidade() {

        bonusVelocidade = 0;
    }

    // =========================
    // ATAQUE COM EFEITOS
    // =========================

    public int getAtaqueComEfeitos() {

        int ataqueFinal = ataque;

        for (EfeitoAtivo efeito : efeitosAtivos) {

            if (efeito == null || efeito.habilidade == null) {
                continue;
            }

            Habilidade habilidade = efeito.habilidade;

            if ("ataque".equalsIgnoreCase(habilidade.atributo)) {
                ataqueFinal += habilidade.efeito;
            }
        }

        if (ataqueFinal < 0) {
            ataqueFinal = 0;
        }

        return ataqueFinal;
    }

    // =========================
    // DEFESA COM EFEITOS
    // =========================

    public int getDefesaComEfeitos() {

        int defesaFinal = defesa;

        for (EfeitoAtivo efeito : efeitosAtivos) {

            if (efeito == null || efeito.habilidade == null) {
                continue;
            }

            Habilidade habilidade = efeito.habilidade;

            if ("defesa".equalsIgnoreCase(habilidade.atributo)) {
                defesaFinal += habilidade.efeito;
            }
        }

        if (defesaFinal < 0) {
            defesaFinal = 0;
        }

        return defesaFinal;
    }

    // =========================
    // VELOCIDADE COM EFEITOS
    // =========================

    public int getVelocidadeComEfeitos() {

        int velocidadeFinal = velocidade + bonusVelocidade;

        for (EfeitoAtivo efeito : efeitosAtivos) {

            if (efeito == null || efeito.habilidade == null) {
                continue;
            }

            Habilidade habilidade = efeito.habilidade;

            if ("velocidade".equalsIgnoreCase(habilidade.atributo)) {
                velocidadeFinal += habilidade.efeito;
            }
        }

        if (velocidadeFinal < 0) {
            velocidadeFinal = 0;
        }

        return velocidadeFinal;
    }

    // =========================
    // DANO BASE
    // =========================

    public int calcularDano(Birdmon defensor) {

        if (defensor == null) return 0;

        int ataqueBase = getAtaqueComEfeitos();

        double multiplicador = SistemaElementos.multiplicador(
            getTipo(),
            defensor.getTipo()
        );

        int danoAtaque = (int) Math.round(ataqueBase * multiplicador);

        int dano = danoAtaque - (defensor.getDefesaComEfeitos() / 2);

        if (dano < 1) {
            dano = 1;
        }

        return dano;
    }

    // =========================
    // GETTERS
    // =========================

    public String getNome() {
        return nome;
    }

    public String getTipo() {
        return tipo;
    }

    public int getNivel() {
        return nivel;
    }

    public int getExperiencia() {
        return experiencia;
    }

    public int getVida() {
        return vida;
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public int getPh() {
        return ph;
    }

    public int getPhMaximo() {
        return phMaximo;
    }

    public int getAtaque() {
        return ataque;
    }

    public int getDefesa() {
        return defesa;
    }

    public int getVelocidade() {
        return velocidade;
    }

    public int getBonusVelocidade() {
        return bonusVelocidade;
    }

    public boolean estaDerrotado() {
        return vida <= 0;
    }
}