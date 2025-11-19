<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Cadastro de Usuário</title>
    </head>
<body>
    <header class="gov-header">
        <h1>Administração</h1>
        <a href="${pageContext.request.contextPath}/admin/usuarios">Voltar</a>
    </header>

    <div class="container" style="max-width: 600px;">
        <h2>${empty usuario.id || usuario.id == 0 ? 'Novo Cadastro' : 'Editar Usuário'}</h2>

        <form action="${pageContext.request.contextPath}/admin/usuarios" method="post">
            <input type="hidden" name="id" value="${usuario.id}">

            <div class="form-group">
                <label>Nome Completo:</label>
                <input type="text" name="nome" value="${usuario.nome}" required>
            </div>

            <div class="form-group">
                <label>E-mail Institucional:</label>
                <input type="email" name="email" value="${usuario.email}" required>
            </div>

            <div class="form-group">
                <label>Senha:</label>
                <input type="password" name="senha" placeholder="${not empty usuario.id ? '(Deixe em branco para não alterar)' : ''}" ${empty usuario.id ? 'required' : ''}>
            </div>

            <div class="form-group">
                <label>Perfil de Acesso:</label>
                <select name="cargo" required>
                    <option value="">Selecione...</option>
                    <option value="alu" ${usuario.cargo == 'alu' ? 'selected' : ''}>Aluno</option>
                    <option value="prof" ${usuario.cargo == 'prof' ? 'selected' : ''}>Professor</option>
                    <option value="coord" ${usuario.cargo == 'coord' ? 'selected' : ''}>Coordenador</option>
                    <option value="adm" ${usuario.cargo == 'adm' ? 'selected' : ''}>Administrador</option>
                </select>
            </div>

            <div class="form-group" style="display: flex; align-items: center;">
                <input type="checkbox" name="ativo" id="ativo" style="width: auto; margin-right: 10px;" ${usuario.ativo ? 'checked' : ''}>
                <label for="ativo" style="margin-bottom: 0;">Usuário Ativo no Sistema</label>
            </div>

            <div style="margin-top: 20px;">
                <button type="submit" class="btn">Salvar Dados</button>
                <a href="${pageContext.request.contextPath}/admin/usuarios" class="btn btn-outline" style="margin-left: 10px; border: none;">Cancelar</a>
            </div>
        </form>
    </div>
</body>
</html>