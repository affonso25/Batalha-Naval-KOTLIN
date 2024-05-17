const val MENU_PRINCIPAL = 100
const val MENU_DEFINIR_TABULEIRO = 101
const val MENU_DEFINIR_TABULEIRO_NAVIOS = 102
const val MENU_JOGAR = 103
const val MENU_LER_FICHEIRO = 104
const val MENU_GRAVAR_FICHEIRO = 105
const val SAIR = 106

var numLinhas = -1
var numColunas = -1

var tabuleiroHumano: Array<Array<Char?>> = emptyArray()
var tabuleiroComputador: Array<Array<Char?>> = emptyArray()

var tabuleiroPalpitesDoHumano: Array<Array<Char?>> = emptyArray()
var tabuleiroPalpitesDoComputador: Array<Array<Char?>> = emptyArray()




fun calculaNumNavios(nLinhas: Int, nColunas: Int): Array<Int>{
    return when (nColunas) {
        4 -> arrayOf(2,0,0,0)
        5 -> arrayOf(1,1,1,0)
        7 -> arrayOf(2,1,1,1)
        8 -> arrayOf(2,2,1,1)
        10 -> arrayOf(3,2,1,1)
        else -> arrayOf()
    }
}

fun criaTabuleiroVazio(numLinhas: Int, numColunas: Int): Array<Array<Char?>>{
    return Array(numLinhas) {arrayOfNulls<Char?>(numColunas)}
}

fun coordenadaContida(tabuleiro: Array<Array<Char?>>, cordenadasLinha: Int, cordenadasColuna: Int):Boolean{
    val tamanhoTabuleiro = tabuleiro.size
    return (cordenadasLinha>1 && cordenadasColuna>1)&&
            (cordenadasLinha<=tamanhoTabuleiro && cordenadasColuna <= tamanhoTabuleiro)
}

fun limparCoordenadasVazias(cordenadas: Array<Pair<Int, Int>>):Array<Pair<Int,Int>>{
    var count = 0
    for (par in cordenadas) {
        if (par != Pair(0, 0)) {
            count++
        }
    }
    val resultado = Array(count) {Pair(0,0)}
    var count2 = 0
    for (par in cordenadas) {
        if (par != Pair(0, 0)) {
            resultado[count2] = par
            count2++
        }
    }
    return resultado
}

fun juntarCoordenadas(coordenadas1: Array<Pair<Int, Int>>, coordenadas2: Array<Pair<Int, Int>>):Array<Pair<Int, Int>>{
    return coordenadas1 + coordenadas2
}

fun gerarCoordenadasNavio(tabuleiro: Array<Array<Char?>>, cordenadasLinha: Int, cordenadasColuna: Int,
                          orientacao: String, dimensao: Int):Array<Pair<Int,Int>>{
    val coordenadas = Array(dimensao) {Pair(0,0)}
    if (!coordenadaContida(tabuleiro,cordenadasLinha,cordenadasColuna)) {
        return emptyArray()
    }
    for (num in 0 until dimensao) {
        val linha: Int
        val coluna: Int
        when (orientacao) {
            "N" -> {linha = cordenadasLinha - num; coluna = cordenadasColuna}
            "S" -> {linha = cordenadasLinha + num; coluna = cordenadasColuna}
            "E" -> {linha = cordenadasLinha; coluna = cordenadasColuna + num}
            "O" -> {linha = cordenadasLinha; coluna = cordenadasColuna - num}
            else -> {return emptyArray()}
        }
        if (!coordenadaContida(tabuleiro,cordenadasLinha,cordenadasColuna)) {
            return emptyArray()
        }
        coordenadas[num] = Pair(linha,coluna)
    }
    return coordenadas
}

fun gerarCoordenadasFronteira(tabuleiro: Array<Array<Char?>>, cordenadasLinha: Int, cordenadasColuna: Int,
                              orientacao: String, dimensao: Int):Array<Pair<Int,Int>>{
    if (!coordenadaContida(tabuleiro,cordenadasLinha,cordenadasColuna)){
        return emptyArray()
    }
    var fronteira = emptyArray<Pair<Int,Int>>()
    for (count in 0 until dimensao) {
        val linhaFronteira: Int
        val colunaFronteira: Int
        when (orientacao) {
            "N" -> {linhaFronteira = cordenadasLinha-count; colunaFronteira = cordenadasColuna}
            "S" -> {linhaFronteira = cordenadasLinha+count; colunaFronteira = cordenadasColuna}
            "E" -> {linhaFronteira = cordenadasLinha; colunaFronteira = cordenadasColuna+count}
            "O" -> {linhaFronteira = cordenadasLinha; colunaFronteira = cordenadasColuna-count}
            else -> {
                return emptyArray()
            }
        }
        if (coordenadaContida(tabuleiro, linhaFronteira, colunaFronteira)) {
            for (count2 in -1..1) {
                for (count3 in -1..1) {
                    val linhaFronteira2 = linhaFronteira + count2
                    val colunaFronteira2 = colunaFronteira + count3
                    if (coordenadaContida(tabuleiro,linhaFronteira2,colunaFronteira2) &&
                        (linhaFronteira2 != linhaFronteira || colunaFronteira2 != colunaFronteira)) {
                        fronteira += Pair(linhaFronteira2,colunaFronteira2)
                    }
                }
            }
        } else {
            return emptyArray()
        }
    }
    return fronteira
}

fun estaLivre(tabuleiro: Array<Array<Char?>>, coordenadas: Array<Pair<Int,Int>>): Boolean {
    for (linha in tabuleiro) {
        for (coordenadaAtual in linha) {
            if (coordenadaAtual != null && coordenadaAtual != ' ') {
                return false
            }
        }
    }
    return true
}

fun insereNavioSimples(tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int, dimensao: Int): Boolean {
    if (coordenadaContida(tabuleiro,linha,coluna)) {
        if (estaLivre(tabuleiro, arrayOf(Pair(linha,coluna)))) {
            tabuleiro[linha-1][coluna-1] = '1'
            return true
        }
    }
    return false
}

fun insereNavio(tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int, orientacao: String, dimensao: Int): Boolean {

    if (linha !in 1..tabuleiro.size && coluna !in 1..tabuleiro[0].size) return false

    var novaLinha = linha
    var novaColuna = coluna

    for (i in 0 until dimensao) {
        if (novaLinha !in 1..tabuleiro.size && novaColuna !in 1..tabuleiro[0].size ||
            tabuleiro[novaLinha - 1][novaColuna - 1] != null) {
            return false
        }
        when (orientacao) {
            "N" -> novaLinha--
            "S" -> novaLinha++
            "O" -> novaColuna--
            "E" -> novaColuna++
        }
    }

    for (i in -1..1) {
        for (j in -1..1) {
            val linhaSeguinte = linha + i
            val colunaSeguinte = coluna + j

            if (linhaSeguinte in 1..tabuleiro.size && colunaSeguinte in 1..tabuleiro[0].size &&
                tabuleiro[linhaSeguinte - 1][colunaSeguinte- 1] != null) {
                return false
            }
        }
    }

    novaLinha = linha
    novaColuna = coluna
    for (i in 0 until dimensao) {
        tabuleiro[novaLinha - 1][novaColuna - 1] = when (dimensao) {
            1 -> '1'
            2 -> '2'
            3 -> '3'
            4 -> '4'
            else -> return false
        }
        when (orientacao) {
            "N" -> novaLinha--
            "S" -> novaLinha++
            "O" -> novaColuna--
            "E" -> novaColuna++
        }
    }
    return true
}

fun navioCompleto(tabuleiroPalpites: Array<Array<Char?>>, linha: Int, coluna: Int): Boolean {

    if (!coordenadaContida(tabuleiroPalpites, linha, coluna)) {
        return false
    }
    return tabuleiroPalpites[linha - 1][coluna - 1] == '1'
}

fun preencheTabuleiroComputador(tabuleiro: Array<Array<Char?>>, navios: Array<Int>) {

    var submarinos = 0
    val tipoDeTabuleiro = tabuleiro.size

    while (submarinos < 2) {

        val linha = (0 until tipoDeTabuleiro).random()
        val coluna = (0 until tipoDeTabuleiro).random()

        if (tabuleiro[linha][coluna] == null) {
            var ocupado = false

            for (linhamovel in -1..1) {
                for (colunamovel in -1..1) {
                    val verificarEmCimaEBaixo = linha + linhamovel
                    val verificarAEsquerdaEDireita = coluna + colunamovel
                    if (verificarEmCimaEBaixo in 0 until tipoDeTabuleiro && verificarAEsquerdaEDireita in 0 until tipoDeTabuleiro &&
                        tabuleiro[verificarEmCimaEBaixo][verificarAEsquerdaEDireita] == '1') {
                        ocupado = true
                    }
                }
            }
            if (!ocupado) {
                tabuleiro[linha][coluna] = '1'
                submarinos++
            }
        }
    }
}

fun obtemMapa(tabuleiro: Array<Array<Char?>>, verificacao: Boolean): Array<String?> {

    val legenda = criaLegendaHorizontal(tabuleiro[0].size)
    var terreno : Array<String?> = arrayOf("| $legenda |")
    var linhas = 1

    if (tabuleiro[0].size !in 1..26) {
        return arrayOf("")
    } else {
        while (linhas <= tabuleiro.size) {
            var texto = ""
            var repeticoesColuna = 0
            while (repeticoesColuna <= tabuleiro[0].size){
                repeticoesColuna++
                texto += if (repeticoesColuna != tabuleiro[linhas - 1].size + 1) {
                    if (tabuleiro[linhas - 1][repeticoesColuna - 1] != null) {
                        "| ${tabuleiro[linhas - 1][repeticoesColuna - 1]} "
                    } else {
                        if (verificacao) {
                            "| ~ "
                        } else {
                            "| ? "
                        }
                    }
                } else {
                    "| $linhas"
                }
            }
            terreno += texto
            linhas++
        }
    }
    return terreno
}


fun lancarTiro(tabuleiroReal: Array<Array<Char?>>, tabuleiroPalpites: Array<Array<Char?>>,
               coordenadas: Pair<Int, Int>):String {
    val linha = coordenadas.first
    val coluna = coordenadas.second

    if (!coordenadaContida(tabuleiroReal, linha, coluna)) {
        return "Coordenada inválida"
    }

    when (tabuleiroReal[linha - 1][coluna - 1]) {
        '1' -> {
            tabuleiroPalpites[linha - 1][coluna - 1] = '1'
            return "Tiro num submarino."
        }
        else -> {
            tabuleiroPalpites[linha - 1][coluna - 1] = 'X'
            return "Agua."
        }
    }
}

fun geraTiroComputador(tabuleiroPalpites: Array<Array<Char?>>): Pair<Int, Int> {

    var coordenadasEscolhidas = emptyArray<Pair<Int,Int>>()

    for (i in tabuleiroPalpites.indices) {
        for (j in 0 until tabuleiroPalpites[0].size) {
            if (tabuleiroPalpites[i][j] == null) {
                coordenadasEscolhidas += Pair(i+1,j+1)
            }
        }
    }
    return coordenadasEscolhidas.random()
}

fun contarNaviosDeDimensao(tabuleiroPalpites: Array<Array<Char?>>, dimensao: Int): Int {

    var count = 0

    for (linha in tabuleiroPalpites.indices) {
        for (coluna in 0 until tabuleiroPalpites[linha].size) {
            if (tabuleiroPalpites[linha][coluna] != null) {
                val podeInserir = when (tabuleiroPalpites[linha][coluna]) {
                    '1' -> dimensao == 1
                    '2' -> dimensao == 2
                    '3' -> dimensao == 3
                    '4' -> dimensao == 4
                    else -> false
                }

                if (podeInserir) {
                    count++
                }
            }
        }
    }
    return count
}

fun venceu(tabuleiroPalpites: Array<Array<Char?>>): Boolean {
    val navios = calculaNumNavios(tabuleiroPalpites.size, tabuleiroPalpites[0].size)

    return (contarNaviosDeDimensao(tabuleiroPalpites,1) == navios[0] &&
            contarNaviosDeDimensao(tabuleiroPalpites,2) == navios[1] &&
            contarNaviosDeDimensao(tabuleiroPalpites,3) == navios[2] &&
            contarNaviosDeDimensao(tabuleiroPalpites,4) == navios[3])
}

fun lerJogo(nomeFicheiro: String, tipoTabuleiro: Int): Array<Array<Char?>> {
    return arrayOf()
}

fun gravarJogo(nomeFicheiro: String, tabuleiroRealHumano: Array<Array<Char?>>,
               tabuleiroPalpitesHumano: Array<Array<Char?>>, tabuleiroRealComputador: Array<Array<Char?>>,
               tabuleiroPalpitesComputador: Array<Array<Char?>>) {
}

fun tamanhoTabuleiroValido(nlinhas:Int?,ncolunas:Int?):Boolean {
    return (ncolunas == nlinhas && (nlinhas == 4 || nlinhas == 5 || nlinhas == 7 || nlinhas == 8 || nlinhas == 10) &&
            (ncolunas == 4 || ncolunas == 5 || ncolunas == 7 || ncolunas == 8 || ncolunas == 10))
}

fun processaCoordenadas(coordenadas: String, nlinhas: Int, ncolunas: Int): Pair<Int,Int>? {
    var linhaReal = -1
    var colunaReal = -1
    val abc = 'A'..'J'
    if ((coordenadas == "") || (coordenadas.length != 4 && coordenadas.length != 3)) {
        return null
    }
    if (coordenadas.length == 3) {
        if (coordenadas[1] != ',') {
            return null
        }
        val numstr = "${coordenadas[0]}"
        val linha = numstr.toIntOrNull()
        if (linha == null || linha !in 1..nlinhas) {
            return null
        }

        linhaReal = linha

        val valorLetra = (coordenadas[2]).code
        val coluna = valorLetra - 64
        if  (coluna !in 1..ncolunas) {
            return null
        }

        colunaReal = coluna
    }

    if (coordenadas.length == 4) {
        if (coordenadas[2] != ',') {
            return null
        }

        val numstr = "${coordenadas[0]}${coordenadas[1]}"
        val linha = numstr.toIntOrNull()
        if (linha == null || linha !in 1..nlinhas){
            return null
        }

        linhaReal = linha

        val valorLetra = (coordenadas[3]).code
        val coluna = valorLetra - 64
        if (valorLetra - 64 !in 1..ncolunas) {
            return null
        }

        colunaReal = coluna
    }
    return Pair(linhaReal,colunaReal)
}

fun criaLegendaHorizontal(numColunas: Int?): String {

    if (numColunas == null) {
        return ""
    }

    val primeiroCaracter = 'A'
    val ultimoCaracter = (primeiroCaracter + numColunas - 1).toChar()

    var tabela = ""
    var letra = primeiroCaracter
    while (letra <= ultimoCaracter) {
        tabela += if (letra == primeiroCaracter) "$letra" else " | $letra"
        letra++
    }
    return tabela
}

fun menu(){
    println("\n> > Batalha Naval < <\n\n1 - Definir Tabuleiro e Navios\n2 - Jogar\n3 - Gravar\n4 - Ler\n0 - Sair\n")
}

fun menuPrincipal():Int{
    menu()
    var menu = readln().toIntOrNull()
    while (menu !in (-1..4)) {
        println("!!! Opcao invalida, tente novamente")
        menu = readln().toIntOrNull()
    }
    when (menu) {
        1 -> return MENU_DEFINIR_TABULEIRO
        2 -> return MENU_JOGAR
        3 -> return MENU_LER_FICHEIRO
        4 -> return MENU_GRAVAR_FICHEIRO
        0 -> return SAIR
        -1 -> return SAIR
    }
    return MENU_PRINCIPAL
}




fun main() {
    val invalido = "!!! Tem que primeiro definir o tabuleiro do jogo, tente novamente"
    fun menuPrincipal(): Int {
        println()
        println("> > Batalha Naval < <\n\n" +
                "1 - Definir Tabuleiro e Navios\n" +
                "2 - Jogar\n" +
                "3 - Gravar\n" +
                "4 - Ler\n" +
                "0 - Sair\n")
        do {
            val menu = readln().toIntOrNull()
            when (menu) {
                1 -> return MENU_DEFINIR_TABULEIRO_NAVIOS
                2 -> return MENU_JOGAR
                3 -> return MENU_GRAVAR_FICHEIRO
                4 -> return MENU_LER_FICHEIRO
                0 -> return 0
                null -> println("!!! Opcao invalida, tente novamente")
                else -> println("!!! Opcao invalida, tente novamente")
            }
        } while (menu != 0)
        return menuPrincipal()
    }

    fun contaSubmarinos(tabuleiro: Array<Array<Char?>>): Int {
        var contador = 0
        for (linha in tabuleiro) {
            for (celula in linha) {
                if (celula == '1') {
                    contador++
                }
            }
        }
        return contador
    }

    fun menuDefinirTabuleiroENavios(): Int {
        if (numLinhas == 4) { println("Tabuleiros ja foram definidos")
            return MENU_PRINCIPAL
        }
        println("\n> > Batalha Naval < <\n\nDefina o tamanho do tabuleiro:\nQuantas linhas?")
        var quantasLinhas: Int?
        do { quantasLinhas = readln().toIntOrNull()
            if (quantasLinhas == -1){ return MENU_PRINCIPAL
            }
        } while (quantasLinhas == null)
        var quantasColunas: Int?
        do { println("Quantas colunas?")
            quantasColunas = readln().toIntOrNull()
            if (quantasColunas == -1) { return MENU_PRINCIPAL
            }
        } while (quantasColunas == null)
        if (!tamanhoTabuleiroValido(quantasLinhas,quantasColunas)) { println("Tamanho tabuleiro inválido")
            return MENU_PRINCIPAL
        }
        numLinhas = quantasLinhas
        numColunas= quantasColunas
        tabuleiroComputador = criaTabuleiroVazio(numLinhas, numColunas)
        tabuleiroHumano = criaTabuleiroVazio(quantasLinhas,quantasColunas)
        tabuleiroPalpitesDoComputador = criaTabuleiroVazio(numLinhas, numColunas)
        tabuleiroPalpitesDoHumano= criaTabuleiroVazio(numLinhas, numColunas)
        for (linha in obtemMapa(tabuleiroHumano,true)) { println(linha)
        }
        for (i in 1 .. 2) { println("Insira as coordenadas de um submarino:\nCoordenadas? (ex: 6,G)")
            var coordenadas: Pair<Int,Int>? = null
            while (coordenadas == null) { val coordenadasInseridas = readlnOrNull()?: ""
                val coordenadasValidas = processaCoordenadas(coordenadasInseridas,quantasLinhas,quantasColunas)
                if (coordenadasValidas != null) { coordenadas = coordenadasValidas
                    if (tabuleiroHumano[coordenadas.first - 1][coordenadas.second - 1] != null) {
                        println("!!! Posicionamento invalido, tente novamente")
                        coordenadas = null
                    } else {
                        val coordenadaBemInserida = insereNavio(tabuleiroHumano,coordenadas.first,coordenadas.second, "N",1)
                        if (!coordenadaBemInserida) { println("!!! Posicionamento invalido, tente novamente")
                            coordenadas = null
                        } else { for (linha in obtemMapa(tabuleiroHumano,true)) { println(linha)
                        }
                        }
                    }
                } else { println("Coordenadas invalidas, tente novamente")
                }
            }
        }
        println("Pretende ver o mapa gerado para o Computador? (S/N)")
        var verOMapa: String?
        do { val numeroNavios = calculaNumNavios(quantasLinhas,quantasColunas)
            tabuleiroComputador = criaTabuleiroVazio(numLinhas, numColunas)
            val preenche = preencheTabuleiroComputador(tabuleiroComputador, numeroNavios)
            verOMapa = readln()
            when (verOMapa) {"-1" -> return MENU_PRINCIPAL
                "S" -> {
                    preencheTabuleiroComputador(tabuleiroComputador, numeroNavios)
                    val mapaComputador = obtemMapa(tabuleiroComputador,true)
                    for (linha in mapaComputador) { println(linha)
                    }
                    val submarinosNoComputador = contaSubmarinos(tabuleiroComputador)
                    numeroNavios[0] = 0 + submarinosNoComputador
                    return MENU_PRINCIPAL
                }
                "N" -> return MENU_PRINCIPAL
                else -> println("!!! Opcao invalida\nPretende ver o mapa gerado para o Computador? (S/N)")
            }
        } while (verOMapa != "S" || verOMapa != "N" || verOMapa != "-1")
        return MENU_PRINCIPAL
    }

    fun aguardarEnter() {
        println("Prima enter para continuar...")
        readlnOrNull() // Aguarda a entrada do usuário (pode ser Enter ou qualquer outra coisa)
    }

    fun menuJogar(): Int {
        if (numLinhas == -1) {
            println("!!! Tem que primeiro definir o tabuleiro do jogo, tente novamente")
            return MENU_PRINCIPAL
        }

        var jogoContinua = true
        var rodada = 1

        while (jogoContinua) {

            val mapaHumano = obtemMapa(tabuleiroPalpitesDoHumano, false)
            for (linha in mapaHumano) {
                println(linha)
            }

            println("Indique a posição que pretende atingir\nCoordenadas? (ex: 6,G)")
            val coordenadasHumano = processaCoordenadas(readlnOrNull() ?: "", numLinhas, numColunas)

            if (coordenadasHumano != null) {
                val resultadoHumano = lancarTiro(tabuleiroComputador, tabuleiroPalpitesDoHumano, coordenadasHumano)
                if (resultadoHumano == "Tiro num submarino.") {
                    println(">>> HUMANO >>>$resultadoHumano Navio ao fundo!")
                } else {
                    println(">>> HUMANO >>>$resultadoHumano")
                }
            } else {
                println("Coordenadas inválidas. Tente novamente.")
            }

            // Verifica se o humano venceu
            if (venceu(tabuleiroPalpitesDoHumano)) {
                println("PARABENS! Venceu o jogo!")
                jogoContinua = false
            } else {
                // Jogada do computador
                val coordenadasComputador = geraTiroComputador(tabuleiroPalpitesDoComputador)
                val resultadoComputador = lancarTiro(tabuleiroHumano, tabuleiroPalpitesDoComputador, coordenadasComputador)
                println("Computador lancou tiro para a posicao $coordenadasComputador")
                println(">>> COMPUTADOR >>>$resultadoComputador")
                var enterPressionado1 = false
                while (!enterPressionado1) {
                    println("Prima enter para continuar")
                    if (readlnOrNull().isNullOrEmpty()) {
                        enterPressionado1 = true
                    }
                }

                // Verifica se o computador venceu
                if (venceu(tabuleiroPalpitesDoComputador)) {
                    println("O computador venceu! Melhor sorte da próxima vez.")
                    jogoContinua = false
                }
            }
            rodada++
        }
        var enterPressionado = false
        while (!enterPressionado) {
            println("Prima enter para voltar ao menu principal")
            if (readlnOrNull().isNullOrEmpty()) {
                enterPressionado = true
            }
        }

        return MENU_PRINCIPAL
    }



    fun menuLerFicheiro(): Int {
        if (numLinhas == -1) {
            println(invalido)
            return MENU_PRINCIPAL
        }
        return menuPrincipal()
    }

    fun menuGravarFicheiro(): Int {
        if (numLinhas == -1) {
            println(invalido)
            return MENU_PRINCIPAL
        }
        return menuPrincipal()
    }

    var menuAtual = MENU_PRINCIPAL

    while (true) {
        menuAtual = when (menuAtual) {
            MENU_PRINCIPAL -> menuPrincipal()
            MENU_DEFINIR_TABULEIRO_NAVIOS -> menuDefinirTabuleiroENavios()
            MENU_JOGAR -> menuJogar()
            MENU_LER_FICHEIRO -> menuLerFicheiro()
            MENU_GRAVAR_FICHEIRO -> menuGravarFicheiro()
            SAIR -> return
            else -> return
        }
    }
}