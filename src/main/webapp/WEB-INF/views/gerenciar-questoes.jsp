<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Gerenciar Questões - ${formulario.titulo}</title>
    <style>
        .questao-card {
            background: #fff;
            border: 1px solid #e5e7eb;
            border-left: 5px solid #1E3A8A; /* Destaque azul */
            padding: 20px;
            margin-bottom: 20px;
            border-radius: 4px;
        }
        .tipo-badge {
            background: #e0f2fe; color: #0369a1; padding: 2px 8px; border-radius: 4px; font-size: 0.8em; font-weight: bold;
        }
        .opcao-lista { margin-left: 20px; margin-top: 10px; border-left: 2px solid #eee; padding-left: 10px; }
        .btn-sm { padding: 4px 8px; font-size: 0.8rem; }
    </style>
</head>
<body>
    <header class="gov-header">
        <h1>Editor de Avaliação: ${formulario.titulo}</h1>
        <a href="${pageContext.request.contextPath}/admin/formularios">Voltar</a>
    </header>

    <div class="container">
        <div style="background: #f0f9ff; padding: 20px; border-radius: 8px; border: 1px dashed #1E3A8A; margin-bottom: 30px;">
            <h3 style="margin-top:0; color:#1E3A8A;">+ Adicionar Nova Questão</h3>
            
            <form action="${pageContext.request.contextPath}/admin/questoes" method="post">
                <input type="hidden" name="acao" value="nova_questao">
                <input type="hidden" name="idFormulario" value="${formulario.id}">
                
                <div class="form-group">
                    <label>Enunciado da Questão:</label>
                    <input type="text" name="texto" required placeholder="Ex: Qual a capital do Brasil?">
                </div>
                
                <div style="display: flex; gap: 20px;">
                    <div class="form-group" style="flex:1">
                        <label>Tipo:</label>
                        <select name="tipo" required>
                            <option value="disc">Discursiva (Aberta)</option>
                            <option value="obj">Objetiva (Múltipla Escolha)</option>
                        </select>
                    </div>
                    <div class="form-group" style="flex:1; display:flex; align-items:center; margin-top:15px;">
                        <input type="checkbox" name="obrigatoria" id="obrig" checked>
                        <label for="obrig" style="margin:0 0 0 5px;">Obrigatoria</label>
                    </div>
                </div>
                
                <button type="submit" class="btn">Adicionar Questão</button>
            </form>
        </div>

        <hr>
        <h2>Questões Existentes</h2>

        <c:if empty="${questoes}">
            <p style="color: #666; text-align: center;">Nenhuma questão cadastrada ainda.</p>
        </c:if>

        <c:forEach var="q" items="${questoes}" varStatus="status">
            <div class="questao-card">
                <div style="display:flex; justify-content:space-between;">
                    <strong>Questão ${status.count} <span class="tipo-badge">${q.tipo == 'obj' ? 'Objetiva' : 'Discursiva'}</span></strong>
                    
                    <form action="${pageContext.request.contextPath}/admin/questoes" method="post" style="display:inline;">
                        <input type="hidden" name="acao" value="excluir_questao">
                        <input type="hidden" name="idQuestao" value="${q.id}">
                        <input type="hidden" name="idFormulario" value="${formulario.id}">
                        <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('Apagar questão?')">X</button>
                    </form>
                </div>
                
                <p style="margin-top: 10px; font-size: 1.1rem;">${q.texto}</p>

                <c:if test="${q.tipo == 'obj'}">
                    <div class="opcao-lista">
                        <p style="font-size: 0.9rem; color: #555;">Alternativas:</p>
                        <ul>
                            <c:forEach var="opt" items="${todasOpcoes}">
                                <c:if test="${opt.idQuestao == q.id}">
                                    <li>${opt.texto}</li>
                                </c:if>
                            </c:forEach>
                        </ul>

                        <form action="${pageContext.request.contextPath}/admin/questoes" method="post" style="margin-top: 10px; display: flex; gap: 5px;">
                            <input type="hidden" name="acao" value="nova_opcao">
                            <input type="hidden" name="idFormulario" value="${formulario.id}"> <input type="hidden" name="idQuestao" value="${q.id}">
                            
                            <input type="text" name="textoOpcao" placeholder="Nova alternativa..." required style="padding: 4px;">
                            <button type="submit" class="btn btn-sm">+ Add</button>
                        </form>
                    </div>
                </c:if>
            </div>
        </c:forEach>
    </div>
</body>
</html>