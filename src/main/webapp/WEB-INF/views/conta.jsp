<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Minha Conta</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/navbar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/generico.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/perfil.css">

</head>
<body class="bg-gray m-0">

    <nav class="navbar no-select">
        <div class="nav-logo">Sistema Acadêmico</div>

        <button class="nav-toggle" onclick="toggleMenu()">☰</button>

        <ul id="nav-links" class="nav-links">
            <li><a href="${pageContext.request.contextPath}/dashboard">Painel</a></li>
            <li><a href="${pageContext.request.contextPath}/conta">Conta</a></li>
            <li><a href="${pageContext.request.contextPath}/auth/logout">Logout</a></li>
        </ul>
    </nav>
    
    <div class="perfil-container p-4">
        <h1 class="text-blue text-left m-0 m-b-4">Minha Conta</h1>

        <c:if test="${not empty feedbackMessage}">
            <div class="feedback-message 
                <c:choose>
                    <c:when test="${feedbackSuccess}">feedback-success</c:when>
                    <c:otherwise>feedback-error</c:otherwise>
                </c:choose>" style="display: block;">
                ${feedbackMessage}
            </div>
        </c:if>

        <div class="info-card bg-white p-6 flex flex-col gap-4 rounded-lg m-b-4">
            
            <h2 class="text-blue font-bold text-lg m-0">Informações de Perfil</h2>
            <hr class="w-full border-gray">

            <div class="flex flex-wrap gap-8 justify-start">

                <div class="flex-col perfil-item">
                    <span class="info-label">NOME COMPLETO</span>
                    <span class="info-value">${nome}</span>
                </div>

                <div class="flex-col perfil-item">
                    <span class="info-label">EMAIL CADASTRADO</span>
                    <span class="info-value">${email}</span>
                </div>
                
            </div>

            <c:if test="${not empty cursoNome and not empty periodoAtual}">
                <div class="flex flex-wrap gap-8 justify-start m-t-4">
                    
                    <div class="flex-col perfil-item">
                        <span class="info-label">CURSO</span>
                        <span class="info-value">${cursoNome}</span>
                    </div>

                    <div class="flex-col perfil-item">
                        <span class="info-label">PERÍODO ATUAL</span>
                        <span class="info-value">${periodoAtual}° Período</span>
                    </div>
                    
                </div>
            </c:if>
            
        </div>

        <div class="info-card bg-white p-6 rounded-lg">
            <h2 class="text-blue font-bold text-lg m-0 m-b-4">Gerenciamento e Segurança</h2>

            <div class="form-section m-b-4">
                <h3 class="text-blue font-bold text-md m-t-0 m-b-3">Alterar Email de Login</h3>

                <form method="POST" action="${pageContext.request.contextPath}/conta" class="flex flex-col gap-3">
                    <input type="hidden" name="action" value="updateEmail"> 
                    
                    <div class="form-group">
                        <label for="novoEmail">Novo Endereço de Email</label>
                        <input type="email" id="novoEmail" name="novoEmail" required placeholder="exemplo@email.com">
                    </div>
                    
                    <div class="form-group">
                        <label for="senhaAtualEmail">Sua Senha Atual (Confirmação)</label>
                        <input type="password" id="senhaAtualEmail" name="senhaAtual" required placeholder="Digite sua senha atual">
                    </div>

                    <div class="flex justify-end">
                        <button type="submit" class="action-button bg-blue-dark">Salvar Novo Email</button>
                    </div>
                </form>
            </div>
            
            <hr class="w-full border-gray m-t-4 m-b-4">

            <div class="form-section">
                <h3 class="text-blue font-bold text-md m-t-0 m-b-3">Alterar Senha de Acesso</h3>

                <form method="POST" action="${pageContext.request.contextPath}/conta" class="flex flex-col gap-3">
                    <input type="hidden" name="action" value="updatePassword"> 
                    
                    <div class="form-group">
                        <label for="senhaAntiga">Senha Antiga</label>
                        <input type="password" id="senhaAntiga" name="senhaAntiga" required placeholder="Sua senha atual">
                    </div>
                    
                    <div class="form-group">
                        <label for="novaSenha">Nova Senha</label>
                        <input type="password" id="novaSenha" name="novaSenha" required placeholder="Nova senha (mínimo 6 caracteres)">
                    </div>

                    <div class="form-group">
                        <label for="confirmaNovaSenha">Confirme a Nova Senha</label>
                        <input type="password" id="confirmaNovaSenha" name="confirmaNovaSenha" required placeholder="Repita a nova senha">
                    </div>

                    <div class="flex justify-end">
                        <button type="submit" class="action-button bg-blue-dark">Mudar Senha</button>
                    </div>
                </form>
            </div>
        </div>

    </div>
    
    
    <script>
        function toggleMenu() {
            const navLinks = document.getElementById('nav-links');
            navLinks.classList.toggle('nav-open');
        }
    </script>
</body>
</html>