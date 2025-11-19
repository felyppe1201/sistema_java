<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Gerenciar Usuários</title>
    </head>
<body>
    <header class="gov-header">
        <h1>Administração</h1>
        <a href="${pageContext.request.contextPath}/dashboard">Voltar ao Início</a>
    </header>

    <div class="container">
        <div style="display: flex; justify-content: space-between; align-items: center;">
            <h2>Lista de Usuários</h2>
            <a href="?acao=editar&id=0" class="btn">+ Novo Usuário</a>
        </div>

        <table class="gov-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nome</th>
                    <th>Email</th>
                    <th>Perfil</th>
                    <th>Status</th>
                    <th>Ações</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="u" items="${usuarios}">
                    <tr>
                        <td>${u.id}</td>
                        <td>${u.nome}</td>
                        <td>${u.email}</td>
                        <td>
                             <c:choose>
                                <c:when test="${u.cargo == 'alu'}">Aluno</c:when>
                                <c:when test="${u.cargo == 'prof'}">Professor</c:when>
                                <c:when test="${u.cargo == 'coord'}">Coordenador</c:when>
                                <c:when test="${u.cargo == 'adm'}">Admin</c:when>
                            </c:choose>
                        </td>
                        <td>
                            <span class="${u.ativo ? 'status-ativo' : 'status-inativo'}">
                                        ${u.ativo ? 'Ativo' : 'Inativo'}
                            </span>
                        </td>
                        <td class="actions">
                            <a href="?acao=editar&id=${u.id}">Editar</a>
                            <a href="?acao=excluir&id=${u.id}" onclick="return confirm('Tem certeza que deseja excluir este usuário?')" style="color: #dc2626;">Excluir</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</body>
</html>