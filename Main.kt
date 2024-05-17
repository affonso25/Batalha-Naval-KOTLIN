//--------------------------------------------- 1.00 VARIAVEIS CONSTANTES ---------------------------------------------
const val MENU_PRINCIPAL = 100
const val MENU_DEFINIR_TABULEIRO = 101
const val MENU_DEFINIR_NAVIUS = 102
const val MENU_JOGAR = 103
const val MENU_LER_FICHEIRO = 104
const val MENU_GRAVAR_FICHEIRO = 105
const val SAIR = 106

var jogar = 0
var ler = 0
var gravar = 0


var numLinhas = -1
var numColunas = -1

var cordenadasLinha = -1
var cordenadasColuna = -1


var tabuleiroHumano:Array<Array<Char?>> = emptyArray()
var tabuleiroComputador:Array<Array<Char?>> = emptyArray()

var tabuleiroPalpitesDoHumano:Array<Array<Char?>> = emptyArray()
var tabuleiroPalpitesDoComputador:Array<Array<Char?>> = emptyArray()

fun definirPrimeiroTabuleiro(){
    println("!!! Tem que primeiro definir o tabuleiro do jogo, tente novamente")
}

fun porImplementar(){
    println("!!! POR IMPLEMENTAR, tente novamente")
}
//------------------------------------------------ 1.01 MENU PRINCIPAL ------------------------------------------------
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

//-------------------------------------------- 1.02 MENU DEFINIR TABULEIRO --------------------------------------------
fun menuDefinirTabuleiro():Int{
    println("\n> > Batalha Naval < <\n")
    println("Defina o tamanho do tabuleiro:")
    println("Quantas linhas?")
    val nlinhas = readln().toIntOrNull()
    if (nlinhas == -1) return menuPrincipal()
    if (nlinhas == 4 || nlinhas == 5 || nlinhas == 7 || nlinhas == 8 || nlinhas == 10){
        numLinhas = nlinhas
        println("Quantas colunas?")
        val ncolunas = readln().toIntOrNull()
        if (ncolunas == -1) return menuPrincipal()
        if (ncolunas == 4 || ncolunas == 5 || ncolunas == 7 || ncolunas == 8 || ncolunas == 10){
            if (tamanhoTabuleiroValido(nlinhas,ncolunas)){
                numColunas = ncolunas
                val tabuleiro = criaTabuleiroVazio(nlinhas, ncolunas)
                return intermediaria2(nlinhas, ncolunas, tabuleiro)
            } else println("Tamanho de tabuleiro invalido")
        }else println("!!! Numero de colunas invalidas, tente novamente")
    }else println("!!! Numero de linhas invalidas, tente novamente")
    return menuDefinirTabuleiro()
}

fun tamanhoTabuleiroValido(nlinhas:Int?,ncolunas:Int?):Boolean {
    return (ncolunas == nlinhas && (nlinhas == 4 || nlinhas == 5 || nlinhas == 7 || nlinhas == 8 || nlinhas == 10) &&
            (ncolunas == 4 || ncolunas == 5 || ncolunas == 7 || ncolunas == 8 || ncolunas == 10))
}

fun criaLegendaHorizontal(ncolunas: Int?):String {
    val abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    var cordenadas = ""
    var count = 0
    if (ncolunas in 1..26) {
        while (ncolunas != null && ncolunas > count) {
            cordenadas += abc[count].toString()
            count++
            if (ncolunas != count){
                cordenadas = "$cordenadas | "
            }
        }
    }
    return cordenadas
}

fun criaTabuleiroVazio(nlinhas: Int, ncolunas: Int): Array<Array<Char?>>{
    return Array(nlinhas) { Array(ncolunas) { null } }
}

fun obtemMapa(tabuleiro: Array<Array<Char?>>, verificacao: Boolean):Array<String>{
    val nlinhas = 4
    val ncolunas = 4
    var terreno = arrayOf("")
    var tabuleiroReal = arrayOf(arrayOf(""))
    if (verificacao) {
        for (linha in 0 until nlinhas) {
            for (coluna in 0 until ncolunas) {
                when (tabuleiro[linha][coluna]) {
                    null -> tabuleiroReal[linha][coluna] = "~"
                    '1' -> tabuleiroReal[linha][coluna] = "1"
                    '2' -> tabuleiroReal[linha][coluna] = "2"
                    '3' -> tabuleiroReal[linha][coluna] = "3"
                    '4' -> tabuleiroReal[linha][coluna] = "4"
                }
                tabuleiroReal[linha][coluna] = "${tabuleiroReal[linha][coluna]} |"
            }
            terreno += tabuleiroReal[linha]
        }
    }
    return terreno
}

fun intermediaria2(nlinhas:Int?, ncolunas:Int?, tabuleiro: Array<Array<Char?>>):Int{
    println(obtemMapa(tabuleiro, verificacao = true))
    println("Insira as coordenadas de um submarino:")
    return menuDefinirNavios(nlinhas,ncolunas)
}


fun letrasParaNumeros(cordenadaLetra: Char):Int {
    var cordenadaNumero = 0
    when(cordenadaLetra){
        'A' -> cordenadaNumero = 1
        'B' -> cordenadaNumero = 2
        'C' -> cordenadaNumero = 3
        'D' -> cordenadaNumero = 4
        'E' -> cordenadaNumero = 5
        'F' -> cordenadaNumero = 6
        'G' -> cordenadaNumero = 7
        'H' -> cordenadaNumero = 8
        'I' -> cordenadaNumero = 9
        'J' -> cordenadaNumero = 10
    }
    return cordenadaNumero
}
//--------------------------------------------- 1.03 MENU DEFENIR NAVIUS ----------------------------------------------
fun menuDefinirNavios(nlinhas:Int?,ncolunas: Int?):Int {
    println("Coordenadas? (ex: 6,G)")
    val coordenadas = readln()
    if (coordenadas == "-1") return MENU_PRINCIPAL
    if (ncolunas != null && nlinhas != null) {
        if (processaCoordenadas(coordenadas, nlinhas, ncolunas) != null) {
            println("Insira a orientacao do navio:")
            orientacao(coordenadas)
        } else {
            println("!!! Coordenadas invalidas, tente novamente")
            return menuDefinirNavios(nlinhas, ncolunas)
        }
    }
    return MENU_PRINCIPAL
}

fun orientacao(coordenadas: String):Int{
    println("Orientacao? (N, S, E, O)")
    val orientacao = readln()
    if (orientacao == "-1") return MENU_PRINCIPAL
    if (orientacao != "N" && orientacao != "S" && orientacao != "E" && orientacao != "O") {
        println("!!! Orientacao invalida, tente novamente")
        return orientacao(coordenadas)
    }
    jogar+1
    ler+1
    gravar+1
    return MENU_PRINCIPAL
}

fun processaCoordenadas(coordenadas: String, nlinhas: Int, ncolunas: Int):Pair<Int,Int>? {
    var cordenadasEmPair = Pair(0,0)
    if (coordenadas == "" || (coordenadas.length != 3 && coordenadas.length != 4)) {
        return null
    }
    if (coordenadas.length == 3) {
        val cordenadasEmString = "${coordenadas[0]}"
        val cordenadasEmInt = cordenadasEmString.toIntOrNull()
        val letra = (coordenadas[2]).code
        if ((coordenadas[1] != ',') || (cordenadasEmInt == null || cordenadasEmInt !in 1..nlinhas) ||
            (letra - 64 !in 1..ncolunas)){
            return null
        }
        cordenadasEmPair = Pair(coordenadas[0].code,coordenadas[2].code)
    }
    if (coordenadas.length == 4) {
        if (coordenadas[2] != ',') {
            return null
        }
        val cordenadasEmString = "${coordenadas[0]}${coordenadas[1]}"
        val cordenadasEmInt = cordenadasEmString.toIntOrNull()
        val letra = (coordenadas[3]).code
        if ((cordenadasEmInt == null || cordenadasEmInt !in 1..nlinhas) ||
            (letra - 64 !in 1..ncolunas)) {
            return null
        }
        val primeiraCordenada = coordenadas[0].toString() + coordenadas[1].toString()
        cordenadasEmPair = Pair(primeiraCordenada.toInt(),coordenadas[3].code)
    }
    return cordenadasEmPair
}

fun calculaNumNavios(nlinhas: Int,ncolunas: Int): Array<Pair<Int,Int>>{
    var submarinos = 0
    var contraTorpedeiros = 0
    var naviosTanque = 0
    var portaAvioes = 0
    var navios = arrayOf(Pair(submarinos,contraTorpedeiros),Pair(naviosTanque,portaAvioes))
    if (nlinhas == 4 && ncolunas == 4){
        submarinos = 2
    }
    if (nlinhas == 5 && ncolunas == 5){
        submarinos = 1
        contraTorpedeiros = 1
        naviosTanque = 1
    }
    if (nlinhas == 7 && ncolunas == 7){
        submarinos = 2
        contraTorpedeiros = 1
        naviosTanque = 1
        portaAvioes = 1
    }
    if (nlinhas == 8 && ncolunas == 8){
        submarinos = 2
        contraTorpedeiros = 2
        naviosTanque = 1
        portaAvioes = 1
    }
    if (nlinhas == 10 && ncolunas == 10){
        submarinos = 3
        contraTorpedeiros = 2
        naviosTanque = 1
        portaAvioes = 1
    }
    else{
        navios = arrayOf()
    }
    return navios
}


fun coordenadaContida(tabuleiro: Array<Array<Char?>>, cordenadasLinha: Int, cordenadasColuna: Int): Boolean{
    val tamanhoTabuleiro = tabuleiro.size
    return (cordenadasLinha>1 && cordenadasColuna>1)&&
            (cordenadasLinha<=tamanhoTabuleiro && cordenadasColuna <= tamanhoTabuleiro)
}

fun limparCoordenadasVazias(cordenadas: Array<Pair<Int, Int>>): Array<Pair<Int,Int>>{
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
fun juntarCoordenadas(cordenadas: Array<Pair<Int, Int>>,cordenadas2: Array<Pair<Int, Int>>): Array<Pair<Int,Int>>{
    val tamanho = cordenadas.size + cordenadas2.size
    val resultado = Array(tamanho) {Pair(0, 0)}
    var count = 0
    for (par in cordenadas) {
        resultado[count] = par
        count++
    }
    for (par in cordenadas2) {
        resultado[count] = par
        count++
    }
    return resultado
}

fun gerarCoordenadasNavio(tabuleiro: Array<Array<Char?>>, cordenadasLinha: Int, cordenadasColuna: Int,
                          orientacao: String, dimensao: Int): Array<Pair<Int,Int>>{
    var cordenadasNavio = arrayOf(Pair(0,0))
    if (coordenadaContida(tabuleiro,cordenadasLinha,cordenadasColuna)){
        if (dimensao == 1) {
            cordenadasNavio = arrayOf(Pair(cordenadasLinha, cordenadasColuna))
        }
        if (dimensao == 2) {
            when (orientacao) {
                "N" -> cordenadasNavio = arrayOf(Pair(cordenadasLinha, cordenadasColuna),
                    Pair(cordenadasLinha-1, cordenadasColuna))
                "S" -> cordenadasNavio = arrayOf(Pair(cordenadasLinha, cordenadasColuna),
                    Pair(cordenadasLinha+1, cordenadasColuna))
                "O" -> cordenadasNavio = arrayOf(Pair(cordenadasLinha, cordenadasColuna),
                    Pair(cordenadasLinha, cordenadasColuna-1))
                "E" -> cordenadasNavio = arrayOf(Pair(cordenadasLinha, cordenadasColuna),
                    Pair(cordenadasLinha, cordenadasColuna+1))
            }
        }
        if (dimensao == 3) {
            when (orientacao) {
                "N" -> cordenadasNavio = arrayOf(Pair(cordenadasLinha, cordenadasColuna),
                    Pair(cordenadasLinha-1, cordenadasColuna),Pair(cordenadasLinha-2, cordenadasColuna))
                "S" -> cordenadasNavio = arrayOf(Pair(cordenadasLinha, cordenadasColuna),
                    Pair(cordenadasLinha+1, cordenadasColuna),Pair(cordenadasLinha+2, cordenadasColuna))
                "O" -> cordenadasNavio = arrayOf(Pair(cordenadasLinha, cordenadasColuna),
                    Pair(cordenadasLinha, cordenadasColuna-1),Pair(cordenadasLinha, cordenadasColuna-2))
                "E" -> cordenadasNavio = arrayOf(Pair(cordenadasLinha, cordenadasColuna),
                    Pair(cordenadasLinha, cordenadasColuna+1),Pair(cordenadasLinha, cordenadasColuna+2))
            }
        }
        if (dimensao == 4) {
            when (orientacao) {
                "N" -> cordenadasNavio = arrayOf(Pair(cordenadasLinha, cordenadasColuna),
                    Pair(cordenadasLinha-1, cordenadasColuna),Pair(cordenadasLinha-2, cordenadasColuna),
                    Pair(cordenadasLinha-3, cordenadasColuna))
                "S" -> cordenadasNavio = arrayOf(Pair(cordenadasLinha, cordenadasColuna),
                    Pair(cordenadasLinha+1, cordenadasColuna),Pair(cordenadasLinha+2, cordenadasColuna),
                    Pair(cordenadasLinha+3, cordenadasColuna))
                "O" -> cordenadasNavio = arrayOf(Pair(cordenadasLinha, cordenadasColuna),
                    Pair(cordenadasLinha, cordenadasColuna-1),Pair(cordenadasLinha, cordenadasColuna-2),
                    Pair(cordenadasLinha, cordenadasColuna-3))
                "E" -> cordenadasNavio = arrayOf(Pair(cordenadasLinha, cordenadasColuna),
                    Pair(cordenadasLinha, cordenadasColuna+1),Pair(cordenadasLinha, cordenadasColuna+2),
                    Pair(cordenadasLinha, cordenadasColuna+3))
            }
        }
    }else{
        return emptyArray()
    }
    return limparCoordenadasVazias(cordenadasNavio)
}

fun gerarCoordenadasFronteira(tabuleiro: Array<Array<Char?>>, cordenadasLinha: Int, cordenadasColuna: Int,
                              orientacao: String, dimensao: Int): Array<Pair<Int,Int>> {
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

fun estaLivre(tabuleiro: Array<Array<Char?>>, cordenadas: Array<Pair<Int, Int>>): Boolean{
    for ((linha,coluna) in cordenadas){
        if (tabuleiro[linha][coluna] != null) {
            return false
        }
    }
    return true
}

fun insereNavioSimples(tabuleiro: Array<Array<Char?>>, cordenadasLinha: Int, cordenadasColuna: Int,
                       dimensao: Int):Boolean{
    val coordenadas = arrayOf(Pair(cordenadasLinha,cordenadasColuna))

    if(coordenadaContida(tabuleiro,cordenadasLinha,cordenadasColuna) && estaLivre(tabuleiro,coordenadas)){
        if (dimensao == 1){
            tabuleiro[cordenadasLinha][cordenadasColuna] = '1'
        }
        if (dimensao == 2){
            tabuleiro[cordenadasLinha][cordenadasColuna] = '2'
            tabuleiro[cordenadasLinha][cordenadasColuna+1] = '2'
        }
        if (dimensao == 3){
            tabuleiro[cordenadasLinha][cordenadasColuna] = '3'
            tabuleiro[cordenadasLinha][cordenadasColuna+1] = '3'
            tabuleiro[cordenadasLinha][cordenadasColuna+2] = '3'
        }
        if (dimensao == 4){
            tabuleiro[cordenadasLinha][cordenadasColuna] = '4'
            tabuleiro[cordenadasLinha][cordenadasColuna+1] = '4'
            tabuleiro[cordenadasLinha][cordenadasColuna+2] = '4'
            tabuleiro[cordenadasLinha][cordenadasColuna+3] = '4'
        }
        return true
    }
    return false
}

fun insereNavio(tabuleiro: Array<Array<Char?>>, cordenadasLinha: Int, cordenadasColuna: Int,
                orientacao: String, dimensao: Int): Boolean{
    val coordenadas = arrayOf(Pair(cordenadasLinha,cordenadasColuna))
    var linhaParaOrientacao = 0
    var colunaParaOrientacao = 0
    when (orientacao) {
        "N" -> linhaParaOrientacao = -1
        "S" -> linhaParaOrientacao = 1
        "O" -> colunaParaOrientacao = 1
        "E" -> colunaParaOrientacao = -1
    }
    if(estaLivre(tabuleiro, juntarCoordenadas(
                gerarCoordenadasNavio(tabuleiro,cordenadasLinha,cordenadasColuna, orientacao,dimensao),
                gerarCoordenadasFronteira(tabuleiro,cordenadasLinha,cordenadasColuna,orientacao,dimensao)))){
        if (dimensao == 1){
            tabuleiro[cordenadasLinha][cordenadasColuna] = '1'
        }
        if (dimensao == 2){
            tabuleiro[cordenadasLinha][cordenadasColuna] = '2'
            tabuleiro[cordenadasLinha+linhaParaOrientacao][cordenadasColuna+colunaParaOrientacao] = '2'
        }
        if (dimensao == 3){
            tabuleiro[cordenadasLinha][cordenadasColuna] = '3'
            tabuleiro[cordenadasLinha+linhaParaOrientacao][cordenadasColuna+colunaParaOrientacao] = '3'
            tabuleiro[(cordenadasLinha+linhaParaOrientacao)*2][(cordenadasColuna+colunaParaOrientacao)*2] = '3'
        }
        if (dimensao == 4){
            tabuleiro[cordenadasLinha][cordenadasColuna] = '4'
            tabuleiro[cordenadasLinha+linhaParaOrientacao][cordenadasColuna+colunaParaOrientacao] = '4'
            tabuleiro[(cordenadasLinha+linhaParaOrientacao)*2][(cordenadasColuna+colunaParaOrientacao)*2] = '4'
            tabuleiro[(cordenadasLinha+linhaParaOrientacao)*3][(cordenadasColuna+colunaParaOrientacao)*3] = '4'
        }
        return true
    }
    return false
}
fun preencheTabuleiroComputador(tabuleiro: Array<Array<Char?>>, navios: Array<Int>){
    var submarinos = navios[0]
    var contraTorpedeiros = navios[1]
    var naviosTanque = navios[2]
    var portaAvioes = navios[3]

    val nlinhas = tabuleiro.size
    val ncolunas = tabuleiro[0].size

    val orientacao = arrayOf("N","S","O","E").random()
    var linhaParaOrientacao = 0
    var colunaParaOrientacao = 0
    when (orientacao) {
        "N" -> linhaParaOrientacao = -1
        "S" -> linhaParaOrientacao = 1
        "O" -> colunaParaOrientacao = 1
        "E" -> colunaParaOrientacao = -1
    }
    while (submarinos > 0) {
        val linha = (0 until nlinhas).random()
        val coluna = (0 until ncolunas).random()

        if (tabuleiro[linha][coluna] == null) {
            tabuleiro[linha][coluna] = '1'
            submarinos--
        }
    }
    while (contraTorpedeiros > 0) {
        val linha = (0 until nlinhas).random()
        val coluna = (0 until ncolunas).random()

        if (tabuleiro[linha][coluna] == null) {
            tabuleiro[linha][coluna] = '2'
            tabuleiro[linha+linhaParaOrientacao][coluna+colunaParaOrientacao] = '2'
            contraTorpedeiros--
        }
    }
    while (naviosTanque > 0) {
        val linha = (0 until nlinhas).random()
        val coluna = (0 until ncolunas).random()

        if (tabuleiro[linha][coluna] == null) {
            tabuleiro[linha][coluna] = '3'
            tabuleiro[linha+linhaParaOrientacao][coluna+colunaParaOrientacao] = '3'
            tabuleiro[(linha+linhaParaOrientacao)*2][(coluna+colunaParaOrientacao)*2] = '3'
            naviosTanque--
        }
    }
    while (portaAvioes > 0) {
        val linha = (0 until nlinhas).random()
        val coluna = (0 until ncolunas).random()

        if (tabuleiro[linha][coluna] == null) {
            tabuleiro[linha][coluna] = '4'
            tabuleiro[linha+linhaParaOrientacao][coluna+colunaParaOrientacao] = '4'
            tabuleiro[(linha+linhaParaOrientacao)*2][(coluna+colunaParaOrientacao)*2] = '4'
            tabuleiro[(linha+linhaParaOrientacao)*3][(coluna+colunaParaOrientacao)*3] = '4'
            portaAvioes--
        }
    }
    tabuleiroComputador = tabuleiro
}

fun navioCompleto(tabuleiroPalpites: Array<Array<Char?>>, cordenadasLinha: Int, cordenadasColuna: Int): Boolean{
    var numEmChar = '0'
    when (tabuleiroPalpites[cordenadasLinha][cordenadasColuna]){
        '2' -> numEmChar = '2'
        '3' -> numEmChar = '3'
        '4' -> numEmChar = '4'
    }

    val cordenadasMaisUM = ((tabuleiroPalpites[cordenadasLinha+1][cordenadasColuna] == numEmChar) ||
            (tabuleiroPalpites[cordenadasLinha-1][cordenadasColuna] == numEmChar) ||
            (tabuleiroPalpites[cordenadasLinha][cordenadasColuna+1] == numEmChar) ||
            (tabuleiroPalpites[cordenadasLinha][cordenadasColuna-1] == numEmChar))
    val cordenadasMaisDois = ((tabuleiroPalpites[cordenadasLinha+2][cordenadasColuna] == numEmChar) ||
            (tabuleiroPalpites[cordenadasLinha-2][cordenadasColuna] == numEmChar) ||
            (tabuleiroPalpites[cordenadasLinha][cordenadasColuna+2] == numEmChar) ||
            (tabuleiroPalpites[cordenadasLinha][cordenadasColuna-2] == numEmChar))
    val cordenadasMaisTres = ((tabuleiroPalpites[cordenadasLinha+3][cordenadasColuna] == numEmChar) ||
            (tabuleiroPalpites[cordenadasLinha-3][cordenadasColuna] == numEmChar) ||
            (tabuleiroPalpites[cordenadasLinha][cordenadasColuna+3] == numEmChar) ||
            (tabuleiroPalpites[cordenadasLinha][cordenadasColuna-3] == numEmChar))

    if (tabuleiroPalpites[cordenadasLinha][cordenadasColuna] != null){
        if (tabuleiroPalpites[cordenadasLinha][cordenadasColuna] == '1'){
            return true
        }
        if ((tabuleiroPalpites[cordenadasLinha][cordenadasColuna] == '2') && cordenadasMaisUM){
            return true
        }
        if ((tabuleiroPalpites[cordenadasLinha][cordenadasColuna] == '3') && cordenadasMaisUM && cordenadasMaisDois){
            return true
        }
        if ((tabuleiroPalpites[cordenadasLinha][cordenadasColuna] == '4') &&
            cordenadasMaisUM && cordenadasMaisDois && cordenadasMaisTres){
            return true
        }
    }
    return false
}



fun lancarTiro(tabuleiroComputador:Array<Array<Char?>>, tabuleiroPalpitesHumano:Array<Array<Char?>>,
               cordenadas: Pair<Int,Int>): String{
    var linha = ""
    var coluna = ""
    val cordenadasParaString = cordenadas.toString()
    if (cordenadasParaString.length == 3){
        linha = cordenadasParaString[0].toString()
        coluna = letrasParaNumeros(cordenadasParaString[2]).toString()
    }
    if (cordenadasParaString.length == 4){
        linha = cordenadasParaString[0].toString() + cordenadasParaString[1].toString()
        coluna = letrasParaNumeros(cordenadasParaString[3]).toString()
    }
    val linhaParaInt = linha.toInt()
    val colunaParaInt = coluna.toInt()
    var mensagem = ""

    when (tabuleiroComputador[linhaParaInt][colunaParaInt]){
        null -> mensagem = "Agua."
        '1' -> mensagem = "Tiro num submarino."
        '2' -> mensagem = "Tiro num contra-torpedeiro."
        '3' -> mensagem = "Tiro num navio-tanque."
        '4' -> mensagem = "Tiro num porta-aviões."
    }
    when (mensagem){
        "Agua." -> tabuleiroPalpitesHumano[linhaParaInt][colunaParaInt] = 'X'
        "Tiro num submarino." -> tabuleiroPalpitesHumano[linhaParaInt][colunaParaInt] = '1'
        "Tiro num contra-torpedeiro." -> tabuleiroPalpitesHumano[linhaParaInt][colunaParaInt] = '2'
        "Tiro num navio-tanque." -> tabuleiroPalpitesHumano[linhaParaInt][colunaParaInt] = '3'
        "Tiro num porta-aviões." -> tabuleiroPalpitesHumano[linhaParaInt][colunaParaInt] = '4'
    }
    tabuleiroPalpitesDoHumano = tabuleiroPalpitesHumano
    return mensagem
}

fun lancarTiroComputador(tabuleiroHumano:Array<Array<Char?>>, tabuleiroPalpitesComputador:Array<Array<Char?>>,
                         cordenadas: Pair<Int,Int>): String{
    var linha = ""
    var coluna = ""
    val cordenadasParaString = cordenadas.toString()
    if (cordenadasParaString.length == 3){
        linha = cordenadasParaString[0].toString()
        coluna = letrasParaNumeros(cordenadasParaString[2]).toString()
    }
    if (cordenadasParaString.length == 4){
        linha = cordenadasParaString[0].toString() + cordenadasParaString[1].toString()
        coluna = letrasParaNumeros(cordenadasParaString[3]).toString()
    }
    val linhaParaInt = linha.toInt()
    val colunaParaInt = coluna.toInt()
    var mensagem = ""

    when (tabuleiroHumano[linhaParaInt][colunaParaInt]){
        null -> mensagem = "Agua."
        '1' -> mensagem = "Tiro num submarino."
        '2' -> mensagem = "Tiro num contra-torpedeiro."
        '3' -> mensagem = "Tiro num navio-tanque."
        '4' -> mensagem = "Tiro num porta-aviões."
    }
    when (mensagem){
        "Agua." -> tabuleiroPalpitesComputador[linhaParaInt][colunaParaInt] = 'X'
        "Tiro num submarino." -> tabuleiroPalpitesComputador[linhaParaInt][colunaParaInt] = '1'
        "Tiro num contra-torpedeiro." -> tabuleiroPalpitesComputador[linhaParaInt][colunaParaInt] = '2'
        "Tiro num navio-tanque." -> tabuleiroPalpitesComputador[linhaParaInt][colunaParaInt] = '3'
        "Tiro num porta-aviões." -> tabuleiroPalpitesComputador[linhaParaInt][colunaParaInt] = '4'
    }
    tabuleiroPalpitesDoComputador = tabuleiroPalpitesComputador
    return mensagem
}

fun geraTiroComputador(tabuleiro: Array<Array<Char?>>):Pair<Int,Int>{
    val nlinhas = tabuleiro.size
    val ncolunas = tabuleiro[0].size
    var linha = (0 until nlinhas).random()
    var coluna = (0 until ncolunas).random()
    var tiro = Pair(0,0)
    while (tabuleiroPalpitesDoComputador[linha][coluna]!=null){
        if (tabuleiroPalpitesDoComputador[linha][coluna]!=null){
            linha = (0 until nlinhas).random()
            coluna = (0 until ncolunas).random()
        }else{
            tiro = Pair(linha,coluna)
        }
    }
    return tiro
}

fun contarNaviosDeDimensao(tabuleiro: Array<Array<Char?>>, dimensao: Int): Int{
    val nlinhas = tabuleiro.size
    val ncolunas = tabuleiro[0].size
    var count = 0
    var resultado = 0
    for (linha in 1..nlinhas) {
        for (coluna in 1..ncolunas) {
            if (tabuleiro[linha][coluna] == dimensao.toChar()){
                count++
            }
        }
    }
    when (dimensao){
        1-> resultado = count
        2 ->
            if (count>1){
                if (count % 2 == 0){
                    resultado = count/2
                }else{
                    resultado = (count-1)/2
                }
            }else{
            resultado = 0
            }
        3 ->
            if (count % 3 == 0){
                resultado = count/3
            }else{
                resultado = 0
            }
        4 -> if (count % 4 == 0){
            resultado = count/4
        }else{
            resultado = 0
        }
    }
    return resultado
}

fun intermedia3(tabuleiro: Array<Array<Char?>>, nlinhas: Int,ncolunas: Int):Array<Int>{
    val numNavios = calculaNumNavios(nlinhas,ncolunas).toString()
    val posicao1 = numNavios[0].toInt()
    val posicao2 = numNavios[0].toInt()
    val posicao3 = numNavios[0].toInt()
    val posicao4 = numNavios[0].toInt()
    return arrayOf(posicao1,posicao2,posicao3,posicao4)
}


fun venceu(tabuleiro: Array<Array<Char?>>):Boolean{
    val nlinhas = tabuleiro.size
    val ncolunas = tabuleiro[0].size
    val dimensao = tabuleiro.size
    val numNavios = intermedia3(tabuleiro, nlinhas, ncolunas)
    return numNavios[0] == contarNaviosDeDimensao(tabuleiro,dimensao) &&
            numNavios[1] == contarNaviosDeDimensao(tabuleiro,dimensao) &&
            numNavios[2] == contarNaviosDeDimensao(tabuleiro,dimensao) &&
            numNavios[3] == contarNaviosDeDimensao(tabuleiro,dimensao)
}

//-------------------------------------------------- 1.03 MENU JOGAR --------------------------------------------------
fun menuJogar():Int{
    if(jogar>0) {
        porImplementar()


    }else{
        definirPrimeiroTabuleiro()
    }
    return MENU_PRINCIPAL
}
//---------------------------------------------- 1.04 MENU LER FICHEIRO -----------------------------------------------
fun menuLerFicheiro():Int{
    if(ler>0) {
        porImplementar()
    }else{
        definirPrimeiroTabuleiro()
    }
    return MENU_PRINCIPAL
}
//--------------------------------------------- 1.05 MENU GRAVAR FICHEIRO ---------------------------------------------
fun menuGravarFicheiro():Int{
    if(gravar>0) {
        porImplementar()
    }else{
        definirPrimeiroTabuleiro()
    }
    return MENU_PRINCIPAL
}
//---------------------------------------------------------------------------------------------------------------------


fun lerJogo(nomeFicheiro: String,tipoTabuleiro: Int ):Array<Array<Char?>>{
    return arrayOf(arrayOf('A','B'), arrayOf('C','D'))
}

fun gravarJogo(nomeFicheiro: String ,tabuleiro1: Array<Array<Char?>>,tabuleiro2: Array<Array<Char?>>,tabuleiro3: Array<Array<Char?>>,
               tabuleiro4: Array<Array<Char?>>){
}


//----------------------------------------------- 1.06 MENU ACTUAL/SAIR -----------------------------------------------
fun main() {
    var menuActual = MENU_PRINCIPAL
    while (true) {
        menuActual = when (menuActual) {
            MENU_PRINCIPAL -> menuPrincipal()
            MENU_DEFINIR_TABULEIRO -> menuDefinirTabuleiro()

            MENU_JOGAR -> menuJogar()
            MENU_LER_FICHEIRO -> menuLerFicheiro()
            MENU_GRAVAR_FICHEIRO -> menuGravarFicheiro()
            SAIR -> return
            else -> return

        }
    }
}