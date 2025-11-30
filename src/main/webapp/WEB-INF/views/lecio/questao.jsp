<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head>
    <title>Questão</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/questao.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/navbar.css"/>
</head>
<body>
<nav class="navbar no-select">
    <div class="nav-logo">Sistema Acadêmico - PROFESSOR</div>
    <button class="nav-toggle" onclick="toggleMenu()">☰</button>
    <ul id="nav-links" class="nav-links">
        <li><a href="${pageContext.request.contextPath}/dashboard">Painel</a></li>
        <li><a href="${pageContext.request.contextPath}/conta">Conta</a></li>
        <li><a href="${pageContext.request.contextPath}/auth/logout">Logout</a></li>
    </ul>
</nav>

<div class="container">

    <h2>${isNew ? "Criar Questão" : "Editar Questão"}</h2>
    <a class="btnSalvar" href="${pageContext.request.contextPath}/lecio/criar-forms?id_form=${formId}">Voltar</a>

    <form class="m" method="post">
        <input type="hidden" name="formId" value="${formId}"/>
        <c:if test="${!isNew}">
            <input type="hidden" name="id_questao" value="${questao.id}"/>
        </c:if>

        <div class="flexcoldiv">
        <label>Enunciado:</label>
        <textarea name="texto" required>${questao != null ? questao.texto : ""}</textarea>
        </div>

        <div class="form-row">
            <label>Tipo:</label>
            <select name="tipo" required>
                <option value="obj" ${questao.tipo == 'obj' ? 'selected' : ''}>Objetiva</option>
                <option value="disc" ${questao.tipo == 'disc' ? 'selected' : ''}>Dissertativa</option>
                <option value="vf" ${questao.tipo == 'vf' ? 'selected' : ''}>Verdadeiro/Falso</option>
            </select>

            <label class="chk-obg">
                <input type="checkbox" name="obrigatoria" ${questao != null && questao.obrigatoria ? "checked" : ""}>
                Obrigatória
            </label>

            <label>Peso:</label>
            <input type="number" step="0.01" min="1" name="peso"
                   value="${pesoValor != null ? pesoValor : 1.0}" required/>
        </div>

        <button type="submit" class="btnSalvar">Salvar</button>
    </form>

    <c:if test="${!isNew && questao.tipo != 'disc'}">
        <hr/>
        <h3>Opções</h3>
        <c:if test="${opcoes.size() == 0}">
            <p>Nenhuma opção cadastrada.</p>
        </c:if>

        <div class="opcoes-container">
            <c:forEach var="op" items="${opcoes}" varStatus="loop">
                <c:set var="letra" value="${fn:substring('ABCDEFGHIJKLMNOPQRSTUVWXYZ', loop.index, loop.index+1)}"/>
                <c:set var="pesoOpcao" value="${pesosOpcoes[op.id] != null ? pesosOpcoes[op.id] : 'N/A'}"/>

                <div class="opcao-card">
                    <div class="opcao-header">
                        <span class="opcao-letra">${letra}</span>
                        <span class="opcao-info">
                            <c:choose>
                                <c:when test="${questao.tipo == 'vf'}">
                                    Resposta: <strong>${op.respostavf ? "Verdadeiro" : "Falso"}</strong>
                                </c:when>
                                <c:when test="${questao.tipo == 'obj'}">
                                    Correta: <strong>${op.correta ? "Sim" : "Não"}</strong>
                                </c:when>
                            </c:choose>
                        </span>
                        <span class="opcao-peso">
                            Peso: <strong>${pesoOpcao}</strong>
                        </span>
                    </div>

                    <div class="opcao-texto">${op.texto}</div>

                    <div class="opcao-acoes">
                        <a class="editar-opcao"
                           href="${pageContext.request.contextPath}/lecio/opcao?questao=${questao.id}&opcao=${op.id}">Editar</a>

                        <form method="post" style="display:inline;">
                            <input type="hidden" name="acao" value="removerOpcao"/>
                            <input type="hidden" name="id_opcao" value="${op.id}"/>
                            <input type="hidden" name="id_questao" value="${questao.id}"/>
                            <button type="submit" class="excluir-opcao"
                                    onclick="return confirm('Excluir esta opção?');">Excluir</button>
                        </form>
                    </div>
                </div>
            </c:forEach>
        </div>

        <a class="btnAdicionar" href="${pageContext.request.contextPath}/lecio/opcao?questao=${questao.id}">
            Adicionar Opção
        </a>
    </c:if>

</div>
</body>
</html>
