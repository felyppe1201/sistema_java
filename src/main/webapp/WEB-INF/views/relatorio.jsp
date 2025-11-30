<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Relatórios Consolidados - Professor</title>
    <link
      rel="stylesheet"
      type="text/css"
      href="${pageContext.request.contextPath}/assets/css/reset.css"
    />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/relatorio.css">
    
</head>
<body>

    <div id="app-container">
        
        <!-- Header -->
        <header class="header">
            <div class="container header-content">
                <h1 class="header-title">Sistema de Avaliação</h1>
                <nav class="header-nav">
                    <a href="#" class="nav-link">Dashboard</a>
                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-logout">Sair</a>
                </nav>
            </div>
        </header>

        <!-- Main Content -->
        <main class="container main-content">
            
            <div class="card intro-card">
                <h2 class="card-title">Relatórios Consolidados</h2>
                <p class="intro-text">
                    Bem-vindo(a), Professor(a) 
                    <c:out value="${nomeProfessor}" default="[Nome do Professor]"/>!
                </p>
                <p class="alert alert-info">
                    AVISO: Os dados apresentados são **consolidados e anonimizados** (RF19), garantindo a privacidade dos estudantes.
                </p>
            </div>

            <!-- Exibir Mensagem de Erro (se houver) -->
            <c:if test="${not empty erroRelatorio}">
                <div class="alert alert-error" role="alert">
                    <p class="alert-title">Erro ao Carregar Dados</p>
                    <p class="alert-message"><c:out value="${erroRelatorio}"/></p>
                </div>
            </c:if>

            <!-- Exibir Mensagem de Nenhum Dado (se a lista estiver vazia) -->
            <c:choose>
                <c:when test="${empty relatorios}">
                    <div class="card empty-state" role="alert">
                        <div class="empty-icon"></div> 
                        <h3 class="empty-title">Nenhum Relatório Disponível</h3>
                        <p class="empty-message">Parece que ainda não há dados consolidados para as suas turmas.</p>
                    </div>
                </c:when>
                
                <c:otherwise>
                    <!-- Loop sobre a lista de RelatoriosTurmaDTO -->
                    <c:forEach var="relatorio" items="${relatorios}">
                        
                        <!-- Cartão de Relatório por Turma -->
                        <div class="card report-card">
                            
                            <!-- Título do Relatório (Turma e Disciplina) -->
                            <h3 class="report-title">
                                Turma: <c:out value="${relatorio.codigoTurma}" default="Turma Desconhecida"/> 
                                (<c:out value="${relatorio.nomeDisciplina}" default="Disciplina Desconhecida"/>)
                            </h3>
                            
                            <!-- Estatísticas Agregadas em Cards Menores -->
                            <div class="stats-grid">
                                
                                <div class="stat-card stat-enrolled">
                                    <p class="stat-label">Total de Alunos Matriculados</p>
                                    <p class="stat-value">
                                        <c:out value="${relatorio.totalAlunosMatriculados}" default="N/D"/>
                                    </p>
                                </div>

                                <div class="stat-card stat-collected">
                                    <p class="stat-label">Respostas Coletadas</p>
                                    <p class="stat-value">
                                        <c:out value="${relatorio.totalRespostasColetadas}" default="N/D"/>
                                    </p>
                                </div>

                                <div class="stat-card stat-completion">
                                    <p class="stat-label">Taxa de Conclusão</p>
                                    <p class="stat-value">
                                        <c:set var="taxaConclusao" value="${relatorio.taxaConclusao * 100}"/>
                                        <fmt:formatNumber value="${taxaConclusao}" maxFractionDigits="1"/>%
                                    </p>
                                </div>
                            </div>

                            <!-- Detalhes da Avaliação (Scores por Critério) -->
                            <h4 class="section-title">
                                Estatísticas por Questão Fechada
                            </h4>
                            
                            <div class="question-list">
                                <!-- Iteração sobre a lista de EstatisticaQuestaoDTO -->
                                <c:forEach var="questao" items="${relatorio.estatisticasQuestoes}" varStatus="i">
                                    <div class="question-item">
                                        
                                        <!-- Título da Questão e Score -->
                                        <div class="question-header">
                                            <span class="question-text">
                                                <c:out value="${questao.codigoQuestao}"/>: <c:out value="${questao.textoQuestao}"/>
                                            </span>
                                            <div class="score-display">
                                                <span class="score-label">Score Médio</span>
                                                <span class="score-value">
                                                    <fmt:formatNumber value="${questao.scoreTotal}" maxFractionDigits="2"/>
                                                </span>
                                            </div>
                                        </div>

                                        <!-- Detalhes das Alternativas -->
                                        <p class="alternatives-label">Distribuição de Respostas:</p>
                                        <div class="alternatives-list">
                                            <c:forEach var="alternativa" items="${questao.alternativas}">
                                                <div class="alternative-item">
                                                    <span class="alternative-text">
                                                        <c:out value="${alternativa.textoAlternativa}"/>
                                                    </span>
                                                    
                                                    <!-- NOVO MÉTODO: Geração de Classe CSS Dinâmica -->
                                                    <c:set var="percentValue" value="${alternativa.percentualResposta * 100}"/>
                                                    <!-- Arredonda a porcentagem para o número inteiro mais próximo -->
                                                    <fmt:formatNumber value="${percentValue}" maxFractionDigits="0" var="roundedPercent"/>
                                                    
                                                    <!-- Define o nome da classe como 'width-XX' (ex: 'width-75') -->
                                                    <c:set var="widthClass" value="width-${roundedPercent}"/>

                                                    <div class="progress-bar-container">
                                                        <!-- A classe de largura é injetada aqui -->
                                                        <div class="progress-bar <c:out value="${widthClass}"/>">
                                                        </div>
                                                    </div>
                                                    
                                                    <!-- Valor Percentual -->
                                                    <span class="alternative-percent">
                                                        <c:set var="percentualAlt" value="${percentValue}"/>
                                                        <fmt:formatNumber value="${percentualAlt}" maxFractionDigits="0"/>%
                                                    </span>
                                                </div>
                                            </c:forEach>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>

                        </div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>

        </main>

        <!-- Footer -->
        <footer class="footer">
            <div class="container footer-content">
                <p>&copy; 2025 Sistema de Avaliação. Todos os direitos reservados.</p>
            </div>
        </footer>
    </div>

</body>
</html>