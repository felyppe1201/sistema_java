<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Turma: ${turma.disciplina.nome}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/turma.css"> 
</head>
<body class="m0">
    <c:set var="context" value="${pageContext.request.contextPath}" />
    <header>
        <div class="container">
            <div class="logo"><a href="${context}/lecio/dashboard">Lecio</a></div>
            <nav>
                <a href="${context}/lecio/turmas">Minhas Turmas</a>
                <a href="${context}/lecio/perfil">Perfil</a>
                <a href="${context}/lecio/logout">Sair</a>
            </nav>
        </div>
    </header>
    <div class="container">
        <div class="card">
            <h1>Turma: ${turma.disciplina.nome}</h1>
            <p><strong>Disciplina:</strong> ${turma.disciplina.nome}</p>
            <p><strong>Período Letivo:</strong> ${turma.periodo}</p>
            <p><strong>Código da Turma:</strong> ${turma.id}</p>
            
            <a href="#" class="btn btn-secondary" style="margin-top: 15px;">Gerenciar Alunos</a>
        </div>

        <h2 style="display: flex; justify-content: space-between; align-items: center;">
            Processos Avaliativos
            <a href="${context}/lecio/processo?turmaId=${turma.id}&action=new" class="btn btn-primary" style="font-size: 0.9rem;">+ Novo Processo</a>
        </h2>
        
        <c:choose>
            <c:when test="${not empty processos}">
                <ul class="processos-list">
                    <c:forEach var="processo" items="${processos}">
                        <li class="processo-item">
                            <div class="processo-details">
                                <span class="processo-name">${processo.nome}</span>
                                <span class="processo-periodo">(Período ${processo.periodo})</span>

                                <c:choose>
                                    <c:when test="${processo.ativo}">
                                        <span class="status-badge status-ativo">ATIVO</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="status-badge status-arquivado">ARQUIVADO</span>
                                    </c:otherwise>
                                </c:choose>
                                
                            </div>
                            <div class="processo-actions">
                                <a href="${context}/lecio/processo?id=${processo.id}" class="btn btn-primary">Gerenciar</a>
                                <a href="${context}/lecio/processo?id=${processo.id}&action=notas" class="btn btn-secondary">Notas</a>
                            </div>
                        </li>
                    </c:forEach>
                </ul>
            </c:when>
            <c:otherwise>
                <div class="no-data">
                    Não há processos avaliativos cadastrados para esta turma.
                    <p><a href="${context}/lecio/processo?turmaId=${turma.id}&action=new">Clique aqui para criar o primeiro!</a></p>
                </div>
            </c:otherwise>
        </c:choose>

        <h2 style="margin-top: 40px;">Relatórios e Ações</h2>
        <div class="card">
            <a href="#" class="btn btn-secondary">Gerar Relatório Final</a>
            <a href="#" class="btn btn-secondary">Ver Processos Arquivados</a>
        </div>
        
    </div>

</body>
</html>