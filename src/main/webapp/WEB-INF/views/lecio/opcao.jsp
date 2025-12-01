<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>${isNew ? "Criar Opção" : "Editar Opção"}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/opcao.css"/>
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
    <h2>${isNew ? "Criar Opção" : "Editar Opção"}</h2>

    <c:if test="${not empty msgError}">
        <div class="msg-error">${msgError}</div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/lecio/opcao">
        <input type="hidden" name="questao" value="${questao.id}"/>

        <c:if test="${not isNew}">
            <input type="hidden" name="opcao" value="${opcao.id}"/>
        </c:if>

        <label>Texto da Opção:</label>
        <input type="text" name="texto" maxlength="255" required
               value="${opcao != null ? opcao.texto : ''}" />

        <c:choose>
            <c:when test="${questao.tipo == 'vf'}">
                <div class="form-row">
                    <label>Resposta VF:</label>
                    <label><input type="radio" name="respostavf" value="true"
                        <c:if test="${opcao != null && opcao.respostavf == true}">checked</c:if>
                    /> Verdadeiro</label>
                    <label><input type="radio" name="respostavf" value="false"
                        <c:if test="${opcao != null && opcao.respostavf == false}">checked</c:if>
                    /> Falso</label>
                </div>
            </c:when>
            <c:when test="${questao.tipo == 'obj'}">
                <div class="form-row">
                    <label class="chk-obg">
                        <input type="checkbox" name="correta"
                            <c:if test="${opcao != null && opcao.correta}">checked</c:if> />
                        Correta
                    </label>
                </div>
            </c:when>
        </c:choose>
        <div class="peso-container">
            <label class="peso-label" for="peso">Peso:</label>
            <input type="number" id="peso" name="peso" class="peso-input"
                min="1" step="0.01"
                value="${pesoValor != null ? pesoValor : 1}">
        </div>

        <div style="margin-top:16px">
            <button type="submit" class="btnSalvar">${isNew ? "Criar" : "Salvar"}</button>
            <a class="btn-cancel" href="${pageContext.request.contextPath}/lecio/questao?id_questao=${questao.id}">Voltar</a>
        </div>
    </form>
</div>

</body>
</html>
