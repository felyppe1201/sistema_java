<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Gerenciar Turma</title>
    </head>
<body>
    <header class="gov-header">
        <h1>Gestão de Turmas</h1>
        <a href="${pageContext.request.contextPath}/admin/turmas">Voltar</a>
    </header>

    <div class="container" style="max-width: 700px;">
        <h2>Dados da Turma</h2>

        <form action="${pageContext.request.contextPath}/admin/turmas" method="post">
            <input type="hidden" name="id" value="${turma.id}">

            <div style="display: flex; gap: 20px;">
                <div class="form-group" style="flex: 1;">
                    <label>Código da Turma:</label>
                    <input type="text" name="codigoTurma" value="${turma.codigoTurma}" required placeholder="Ex: T-EDS-2025">
                </div>
                <div class="form-group" style="flex: 1;">
                    <label>Período Letivo:</label>
                    <input type="number" name="periodo" value="${turma.periodo}" required placeholder="Ex: 20251">
                </div>
            </div>

            <div class="form-group">
                <label>Disciplina:</label>
                <select name="idDisciplina" required>
                    <option value="">Selecione a Disciplina...</option>
                    <c:forEach var="d" items="${listaDisciplinas}">
                        <option value="${d.id}" ${turma.idDisciplina == d.id ? 'selected' : ''}>${d.nome}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label>Número de Vagas:</label>
                <input type="number" name="numeroVagas" value="${turma.numeroVagas}" required style="width: 100px;">
            </div>

            <button type="submit" class="btn">Salvar Turma</button>
        </form>
    </div>
</body>
</html>