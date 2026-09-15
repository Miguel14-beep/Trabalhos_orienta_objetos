package main;

import entidade.Birdmon;
import entidade.EfeitoAtivo;
import entidade.Habilidade;

public class LogicaBatalha {

    private Birdmon jogador;
    private Birdmon adversario;
    private Birdmon atacanteAtual;

    private boolean ultimaCapturaSucesso;

    public LogicaBatalha(
            Birdmon jogador,
            Birdmon adversario) {

        this.jogador = jogador;
        this.adversario = adversario;

        ultimaCapturaSucesso = false;

        determinarPrimeiro();
    }

    // =========================
    // CAPTURA
    // =========================

    public String tentarCapturar(Item item) {

        ultimaCapturaSucesso = false;

        if (item == null) {
            return "Item inválido.";
        }

        if (item.getTipo() != TipoItem.CAPTURA) {
            return "Esse item não é uma Birdbola.";
        }

        if (item.getQuantidade() <= 0) {
            return "Você não possui esse item.";
        }

        if (adversario == null) {
            return "Não existe Birdmon para capturar.";
        }

        if (adversario.estaDerrotado()) {
            return "O Birdmon está derrotado.";
        }

        // Consome a Birdbola
        item.reduzirQuantidade();

        // Quanto menor a vida, maior a chance
        double vidaRestante =
                (double) adversario.getVida()
                / adversario.getVidaMaxima();

        double vidaPerdida = 1.0 - vidaRestante;

        double chance =
                (item.getValorEfeito() / 100.0)
                + (vidaPerdida * 0.40);

        // Limites da chance
        if (chance < 0) {
            chance = 0;
        }

        if (chance > 0.95) {
            chance = 0.95;
        }

        double sorte = Math.random();

        if (sorte <= chance) {

            ultimaCapturaSucesso = true;

            return "Você capturou "
                    + adversario.getNome()
                    + "!";
        }

        // Captura falhou: passa o turno
        passarTurno();

        int porcentagem = (int) (chance * 100);

        return "A captura falhou! Chance: "
                + porcentagem
                + "%";
    }

    public boolean foiUltimaCapturaBemSucedida() {
        return ultimaCapturaSucesso;
    }

    // =========================
    // ITENS
    // =========================

    public String usarItem(Item item) {

        if (item == null) {
            return "Item inválido.";
        }

        if (item.getQuantidade() <= 0) {
            return "Você não possui esse item.";
        }

        if (item.getTipo() == TipoItem.CAPTURA) {
            return tentarCapturar(item);
        }

        if (jogador == null) {
            return "Nenhum Birdmon selecionado.";
        }

        item.reduzirQuantidade();

        switch (item.getTipo()) {

            case CURA:

                jogador.curar(item.getValorEfeito());

                passarTurno();

                return jogador.getNome()
                        + " recuperou "
                        + item.getValorEfeito()
                        + " de vida!";

            case BUFF_ATAQUE:

                adicionarEfeito(
                        jogador,
                        new Habilidade(
                                "Buff de Ataque",
                                "ataque",
                                item.getValorEfeito(),
                                0
                        ),
                        item.getDuracaoTurnos()
                );

                passarTurno();

                return jogador.getNome()
                        + " recebeu um aumento de ataque!";

            case BUFF_DEFESA:

                adicionarEfeito(
                        jogador,
                        new Habilidade(
                                "Buff de Defesa",
                                "defesa",
                                item.getValorEfeito(),
                                0
                        ),
                        item.getDuracaoTurnos()
                );

                passarTurno();

                return jogador.getNome()
                        + " recebeu um aumento de defesa!";

            default:

                passarTurno();

                return "Item utilizado.";
        }
    }

    // =========================
    // DETERMINAR PRIMEIRO
    // =========================

    private void determinarPrimeiro() {

        int velocidadeJogador =
                calcularVelocidade(jogador);

        int velocidadeAdversario =
                calcularVelocidade(adversario);

        if (velocidadeJogador >= velocidadeAdversario) {
            atacanteAtual = jogador;
        } else {
            atacanteAtual = adversario;
        }
    }

    private int calcularVelocidade(Birdmon birdmon) {

        if (birdmon == null) {
            return 0;
        }

        return birdmon.getVelocidadeComEfeitos();
    }

    // =========================
    // ATAQUE
    // =========================

    public boolean atacar(
            Birdmon atacante,
            Birdmon defensor) {

        if (atacante == null || defensor == null) {
            return false;
        }

        if (atacante.estaDerrotado()
                || defensor.estaDerrotado()) {

            return false;
        }

        // 90% de chance de acertar
        boolean acertou = Math.random() >= 0.10;

        if (acertou) {

            int dano = calcularDano(
                    atacante,
                    defensor
            );

            defensor.receberDano(dano);

            atacante.zerarBonusVelocidade();

            return true;
        }

        // Se errar, recebe o bônus de velocidade
        if (atacante.getBonusVelocidade() == 0) {
            atacante.adicionarBonusVelocidade(15);
        }

        return false;
    }

    // =========================
    // CALCULAR DANO
    // =========================

    public int calcularDano(
            Birdmon atacante,
            Birdmon defensor) {

        if (atacante == null || defensor == null) {
            return 0;
        }

        return atacante.calcularDano(defensor);
    }

    // =========================
    // ATAQUE COM EFEITOS
    // =========================

    public int calcularAtaque(Birdmon birdmon) {

        if (birdmon == null) {
            return 0;
        }

        return birdmon.getAtaqueComEfeitos();
    }

    // =========================
    // TROCAR BIRDMON
    // =========================

    public void trocarBirdmon(Birdmon novoBirdmon) {

        if (novoBirdmon == null) {
            return;
        }

        if (novoBirdmon.estaDerrotado()) {
            return;
        }

        jogador = novoBirdmon;

        // Ao trocar, o adversário ganha o turno.
        atacanteAtual = adversario;
    }

    // =========================
    // HABILIDADE
    // =========================

    public boolean usarHabilidade(
            Birdmon usuario,
            Habilidade habilidade) {

        if (usuario == null
                || habilidade == null) {

            return false;
        }

        if (usuario.estaDerrotado()) {
            return false;
        }

        if (usuario.getPh() < habilidade.custoPH) {
            return false;
        }

        if (!usuario.gastarPh(habilidade.custoPH)) {
            return false;
        }

        // Cria o efeito temporário
        adicionarEfeito(
                usuario,
                habilidade,
                habilidade.duracao
        );

        return true;
    }

    // =========================
    // ADICIONAR EFEITO
    // =========================

    private void adicionarEfeito(
            Birdmon usuario,
            Habilidade habilidade,
            int duracao) {

        if (usuario == null || habilidade == null) {
            return;
        }

        EfeitoAtivo novoEfeito =
                new EfeitoAtivo(habilidade);

        novoEfeito.turnosRestantes = duracao;

        usuario.efeitosAtivos.add(novoEfeito);
    }

    // =========================
    // ATUALIZAR EFEITOS
    // =========================

    public void atualizarEfeitos() {

        if (jogador != null) {
            atualizarEfeitosBirdmon(jogador);
        }

        if (adversario != null) {
            atualizarEfeitosBirdmon(adversario);
        }
    }

    private void atualizarEfeitosBirdmon(
            Birdmon birdmon) {

        if (birdmon == null) {
            return;
        }

        for (int i = birdmon.efeitosAtivos.size() - 1;
                i >= 0;
                i--) {

            EfeitoAtivo efeito =
                    birdmon.efeitosAtivos.get(i);

            if (efeito == null) {

                birdmon.efeitosAtivos.remove(i);

                continue;
            }

            efeito.diminuirTurno();

            if (efeito.terminou()) {

                birdmon.efeitosAtivos.remove(i);
            }
        }
    }

    // =========================
    // PASSAR TURNO
    // =========================

    public void passarTurno() {

        atualizarEfeitos();

        if (atacanteAtual == jogador) {

            atacanteAtual = adversario;

        } else {

            atacanteAtual = jogador;
        }
    }

    // =========================
    // EXECUTAR ATAQUE
    // =========================

    public boolean executarAtaque() {

        Birdmon atacante =
                getAtacanteAtual();

        Birdmon defensor =
                getDefensorAtual();

        if (atacante == null || defensor == null) {
            return false;
        }

        boolean acertou =
                atacar(atacante, defensor);

        passarTurno();

        return acertou;
    }

    // =========================
    // GETTERS
    // =========================

    public Birdmon getJogador() {
        return jogador;
    }

    public Birdmon getAdversario() {
        return adversario;
    }

    public Birdmon getAtacanteAtual() {
        return atacanteAtual;
    }

    public Birdmon getDefensorAtual() {

        if (atacanteAtual == jogador) {
            return adversario;
        }

        return jogador;
    }
}