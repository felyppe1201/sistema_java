<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Cadastro de Disciplina</title>
    </head>
<body>
    <header class="gov-header">
        <h1>Administração Acadêmica</h1>
        <a href="${pageContext.request.contextPath}/admin/disciplinas">Voltar</a>
    </header>

    <div class="container" style="max-width: 600px;">
        <h2>${empty disciplina.id ? 'Nova Disciplina' : 'Editar Disciplina'}</h2>

        <form action="${pageContext.request.contextPath}/admin/disciplinas" method="post">
            <input type="hidden" name="id" value="${disciplina.id}">

            <div class="form-group">
                <label>Nome da Disciplina:</label>
                <input type="text" name="nome" value="${disciplina.nome}" required placeholder="Ex: Cálculo I">
            </div>

            <div class="form-group">
                <label>Curso Vinculado:</label>
                <select name="idCurso" required>
                    <option value="">Selecione um Curso...</option>
                    <c:forEach var="c" items="${listaCursos}">
                        <option value="${c.id}" ${disciplina.idCurso == c.id ? 'selected' : ''}>${c.nome}</option>
                    </c:forEach>
                </select>
            </div>

            <button type="submit" class="btn">Salvar Registro</button>
        </form>
    </div>
</body>
</html>