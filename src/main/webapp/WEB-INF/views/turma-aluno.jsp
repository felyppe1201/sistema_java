<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Turma: ${turma.disciplina.nome}</title>
    <link
      rel="stylesheet"
      type="text/css"
      href="${pageContext.request.contextPath}/assets/css/reset.css"
    />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/navbar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/generico.css">
    <!-- Reutilizando o CSS de Turma (pois o layout é similar) -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/turma.css"> 
</head>
<body class="m0 bg-gray">

    <!-- Navbar Padrão -->
    <nav class="navbar no-select">
        <div class="nav-logo">Sistema Acadêmico</div>
        <button class="nav-toggle" onclick="toggleMenu()">☰</button>
        <ul id="nav-links" class="nav-links">
            <li><a href="${pageContext.request.contextPath}/dashboard">Painel</a></li>
            <li><a href="${pageContext.request.contextPath}/conta">Conta</a></li>
            <li><a href="${pageContext.request.contextPath}/auth/logout">Logout</a></li>
        </ul>
    </nav>

    <div class="container">
    
        <!-- Cabeçalho da Turma -->
        <div class="card">
            <h1 class="text-blue">${turma.disciplina.nome}</h1>
            <div class="flex flex-wrap gap-4">
                <div><strong>Código:</strong> ${turma.codigoTurma}</div>
                <div><strong>Período:</strong> ${turma.periodo}º</div>
            </div>
            <hr class="m-t-4 m-b-4 border-dee2e6">
            <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-secondary">Voltar ao Painel</a>
        </div>
        
        <!-- SEÇÃO: AVALIAÇÕES PENDENTES (EM ABERTO) -->
        <h2 class="m-t-5 text-blue">Avaliações Pendentes</h2>
        
        <c:choose>
            <c:when test="${not empty pendentes}">
                <ul class="processos-list">
                    <c:forEach var="processo" items="${pendentes}">
                        <li class="processo-item" style="border-left-color: var(--success-color);">
                            <div class="processo-details">
                                <span class="processo-name">${processo.nome}</span>
                                <span class="status-badge status-ativo">DISPONÍVEL</span>
                            </div>
                            <div class="processo-actions">
                                <!-- Botão para responder o formulário -->
                                <a href="${pageContext.request.contextPath}/aluno/responder?id=${processo.id}" class="btn btn-primary">Responder Agora</a>
                            </div>
                        </li>
                    </c:forEach>
                </ul>
            </c:when>
            <c:otherwise>
                <div class="no-data">
                    Nenhuma avaliação pendente para esta turma.
                </div>
            </c:otherwise>
        </c:choose>

        <!-- SEÇÃO: AVALIAÇÕES FINALIZADAS (HISTÓRICO) -->
        <h2 class="m-t-5 text-secondary">Histórico de Avaliações</h2>
        
        <c:choose>
            <c:when test="${not empty concluidos}">
                <ul class="processos-list">
                    <c:forEach var="processo" items="${concluidos}">
                        <li class="processo-item" style="border-left-color: var(--secondary-color); opacity: 0.8;">
                            <div class="processo-details">
                                <span class="processo-name">${processo.nome}</span>
                                <span class="status-badge status-arquivado">FINALIZADO</span>
                            </div>
                            <div class="processo-actions">
                                <!-- Botão apenas para visualização (se permitido) -->
                                <span class="text-secondary font-bold p-2">Enviado</span>
                            </div>
                        </li>
                    </c:forEach>
                </ul>
            </c:when>
            <c:otherwise>
                <p class="text-secondary p-4 text-center">Você ainda não finalizou nenhuma avaliação nesta turma.</p>
            </c:otherwise>
        </c:choose>
        
    </div>

    <script>
        function toggleMenu() {
            const navLinks = document.getElementById('nav-links');
            navLinks.classList.toggle('nav-open'); // Usa a classe do seu CSS de navbar
        }
    </script>
</body>
</html>