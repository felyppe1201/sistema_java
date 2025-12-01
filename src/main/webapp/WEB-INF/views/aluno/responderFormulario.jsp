<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8" />
    <title>Responder Formulário</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/responderFormulario.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/navbar.css" />
</head>

<body>

<nav class="navbar no-select">
    <div class="nav-logo">Sistema Acadêmico</div>
    <button class="nav-toggle" onclick="toggleMenu()">☰</button>
    <ul id="nav-links" class="nav-links">
        <li><a href="${pageContext.request.contextPath}/dashboard">Painel</a></li>
        <li><a href="${pageContext.request.contextPath}/conta">Conta</a></li>
        <li><a href="${pageContext.request.contextPath}/auth/logout">Logout</a></li>
    </ul>
</nav>

<div class="container-geral">

    <a class="voltarbtn" href="javascript:history.back()">
        <span class="icon">←</span> Voltar
    </a>

    <h1 class="titulo-form">${formulario.titulo}</h1>

    <form method="post" action="${pageContext.request.contextPath}/aluno/responderFormulario">

        <input type="hidden" name="formularioId" value="${formularioId}">
        <input type="hidden" name="usuarioId" value="${usuarioId}">

        <c:forEach var="q" items="${formulario.questoes}" varStatus="qs">

            <div class="questao-card">

                <div class="questao-header">
                    <span class="questao-numero">Questão ${qs.index + 1}</span>

                    <c:if test="${q.obrigatoria}">
                        <span class="obrigatoria">*</span>
                    </c:if>

                    <div class="questao-texto">${q.texto}</div>
                </div>


                <div class="opcoes-container">

                    <!-- OBJETIVA (checkboxes) -->
                    <c:if test="${q.tipo == 'obj'}">
                        <c:forEach var="op" items="${q.opcoes}" varStatus="os">

                            <label class="opcao-item">
                                <input
                                        type="checkbox"
                                        name="questao_obj_${q.id}"
                                        value="${op.id}"
                                />

                                <span class="opcao-letra">
                                    ${fn:toUpperCase(fn:substring('abcdefghijklmnopqrstuvwxyz', os.index, os.index+1))}
                                </span>

                                <span class="opcao-texto">${op.texto}</span>
                            </label>

                        </c:forEach>
                    </c:if>


                    <!-- VERDADEIRO/FALSO CORRETO -->
                    <c:if test="${q.tipo == 'vf'}">

                        <c:forEach var="op" items="${q.opcoes}">
                            
                            <div class="vf-bloco">
                                <div class="vf-enunciado">${op.texto}</div>

                                <div class="vf-opcoes">
                                    <label class="vf-item">
                                        <input 
                                            type="radio"
                                            name="questao_vf_${q.id}_opcao_${op.id}"
                                            value="V"
                                            <c:if test="${q.obrigatoria}">required</c:if>
                                        />
                                        <span>V</span>
                                    </label>

                                    <label class="vf-item">
                                        <input 
                                            type="radio"
                                            name="questao_vf_${q.id}_opcao_${op.id}"
                                            value="F"
                                            <c:if test="${q.obrigatoria}">required</c:if>
                                        />
                                        <span>F</span>
                                    </label>
                                </div>
                            </div>

                        </c:forEach>

                    </c:if>


                    <!-- DISSERTATIVA -->
                    <c:if test="${q.tipo == 'disc'}">
                        <textarea
                                name="questao_disc_${q.id}"
                                class="campo-texto"
                                placeholder="Digite sua resposta..."
                                <c:if test="${q.obrigatoria}">required</c:if>
                        ></textarea>
                    </c:if>

                </div>

            </div>

        </c:forEach>

        <button type="submit" class="btn-enviar">Enviar Respostas</button>

    </form>

</div>

</body>
</html>
