<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Responder Avaliação</title>
    <style>
        body { font-family: sans-serif; background: #f3f4f6; padding: 20px; }
        .container { max-width: 800px; margin: 0 auto; background: #fff; padding: 30px; border-radius: 8px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); }
        .header-prova { border-bottom: 2px solid #1E3A8A; margin-bottom: 20px; padding-bottom: 10px; }
        .questao-box { margin-bottom: 25px; padding: 15px; background: #fafafa; border-left: 4px solid #ccc; }
        .questao-box:hover { border-left-color: #1E3A8A; background: #fff; }
        label { display: block; margin-bottom: 8px; }
        textarea { width: 100%; height: 100px; padding: 10px; }
        .btn-enviar { background: #166534; color: white; padding: 15px 30px; border: none; cursor: pointer; font-size: 1.1rem; border-radius: 4px; width: 100%; }
        .btn-enviar:hover { background: #14532d; }
        .tag-anonimo { background: #333; color: #fff; padding: 2px 6px; font-size: 0.8rem; border-radius: 4px; }
    </style>
</head>
<body>
    <div class="container">
        <div class="header-prova">
            <h1 style="color: #1E3A8A;">${formulario.titulo}</h1>
            <p><strong>Turma:</strong> ${turma.codigoTurma}</p>
            <p>
                <strong>Modo:</strong> 
                <c:choose>
                    <c:when test="${formulario.identificado}">
                        <span style="color: #1E3A8A;">Identificado (Seu nome aparecerá)</span>
                    </c:when>
                    <c:otherwise>
                        <span class="tag-anonimo">Anônimo (Sigiloso)</span>
                    </c:otherwise>
                </c:choose>
            </p>
        </div>

        <form action="${pageContext.request.contextPath}/aluno/responder" method="post">
            <input type="hidden" name="idForm" value="${formulario.id}">
            <input type="hidden" name="idTurma" value="${turma.id}">

            <c:forEach var="q" items="${questoes}" varStatus="status">
                <div class="questao-box">
                    <p><strong>${status.count}. ${q.texto}</strong> 
                       ${q.obrigatoria ? '<span style="color:red">*</span>' : ''}
                    </p>

                    <c:if test="${q.tipo == 'disc'}">
                        <textarea name="resp_${q.id}" ${q.obrigatoria ? 'required' : ''}></textarea>
                    </c:if>

                    <c:if test="${q.tipo == 'obj'}">
                        <c:forEach var="opt" items="${todasOpcoes}">
                            <c:if test="${opt.idQuestao == q.id}">
                                <label style="font-weight: normal; cursor: pointer;">
                                    <input type="radio" name="resp_${q.id}" value="${opt.id}" ${q.obrigatoria ? 'required' : ''}>
                                    ${opt.texto}
                                </label>
                            </c:if>
                        </c:forEach>
                    </c:if>
                </div>
            </c:forEach>

            <button type="submit" class="btn-enviar" onclick="return confirm('Confirma o envio das respostas?')">
                Enviar Avaliação Finalizar
            </button>
        </form>
    </div>
</body>
</html>