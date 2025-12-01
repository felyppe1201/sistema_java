<%@ page contentType="text/html;charset=UTF-8" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="fn"
uri="jakarta.tags.functions" %>

<!DOCTYPE html>
<html lang="pt-BR">
  <head>
    <meta charset="utf-8" />
    <title>Criar Formulário - ${processo != null ? processo.nome : ''}</title>

    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/assets/css/navbar.css"
    />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/assets/css/criar-forms.css"
    />
  </head>
  <body>
    <nav class="navbar no-select">
      <div class="nav-logo">Sistema Acadêmico - PROFESSOR</div>
      <button class="nav-toggle" onclick="toggleMenu()">☰</button>
      <ul id="nav-links" class="nav-links">
        <li>
          <a href="${pageContext.request.contextPath}/dashboard">Painel</a>
        </li>
        <li><a href="${pageContext.request.contextPath}/conta">Conta</a></li>
        <li>
          <a href="${pageContext.request.contextPath}/auth/logout">Logout</a>
        </li>
      </ul>
    </nav>

    <div class="page-container">
      <h1>Criar / Editar Formulário</h1>
      <p class="muted">
        Processo: <strong>${processo != null ? processo.nome : 'N/D'}</strong>
      </p>

      <c:if test="${not empty msgSuccess}">
        <div class="msg-success">${msgSuccess}</div>
      </c:if>
      <c:if test="${not empty msgError}">
        <div class="msg-error">${msgError}</div>
      </c:if>

      <form
        id="formHeader"
        method="post"
        action="${pageContext.request.contextPath}/lecio/criar-forms"
        onsubmit="return validateHeader();"
      >
        <input type="hidden" name="id_process" value="${processo.id}" />
        <c:if test="${not empty formulario}">
          <input type="hidden" name="id_form" value="${formulario.id}" />
        </c:if>

        <div class="info-box">
          <label for="titulo">Título do Formulário (obrigatório)</label>
          <input
            id="titulo"
            type="text"
            name="titulo"
            value="${formulario != null ? formulario.titulo : ''}"
            maxlength="255"
            required
          />

          <label class="checkbox-inline">
            <input type="checkbox" id="identificado" name="identificado"
            ${formulario != null && formulario.identificado ? "checked" :
            (formulario == null ? "checked" : "")} /> Aluno identificado (aluno
            precisa se identificar)
          </label>

          <div style="margin-top: 10px">
            <button type="submit" class="btn-primary">Salvar</button>
            <a
              class="btn-cancel"
              href="${pageContext.request.contextPath}/lecio/process?id=${processo.id}"
              >Voltar</a
            >
          </div>
        </div>
      </form>

      <h2 style="margin-top: 18px">Questões</h2>

      <c:if test="${empty questoes}">
        <div class="info-box">
          <p class="muted">Nenhuma questão cadastrada para este formulário.</p>
        </div>
      </c:if>

      <div class="questions-area">
        <c:forEach var="q" items="${questoes}" varStatus="status">
          <div class="question-block">
            <div class="q-header">
              <div>
                <strong>Questão ${status.count}</strong>
                <span style="margin-left: 12px; color: #666">[${q.tipo}]</span>
                <span style="margin-left: 12px; color: #666"
                  >Obrigatória: ${q.obrigatoria ? "Sim" : "Não"}</span
                >
              </div>
              <div>
                <a
                  class="btn-primary"
                  href="${pageContext.request.contextPath}/lecio/questao?id_questao=${q.id}"
                  >Editar</a
                >

                <form
                  style="display: inline-block"
                  method="post"
                  action="${pageContext.request.contextPath}/lecio/criar-forms"
                  onsubmit="return confirm('Confirmar exclusão desta questão?');"
                >
                  <input type="hidden" name="action" value="deleteQuestao" />
                  <input type="hidden" name="id_questao" value="${q.id}" />
                  <input
                    type="hidden"
                    name="id_form"
                    value="${formulario.id}"
                  />
                  <input
                    type="hidden"
                    name="id_process"
                    value="${processo.id}"
                  />
                  <button type="submit" class="btn-remove">Excluir</button>
                </form>
              </div>
            </div>

            <p style="margin: 6px 0">
              <strong>Enunciado:</strong>
            </p>

            <p class="textoq" style="margin: 6px 0">
              <c:out value="${q.texto}" />
            </p>

            <c:if test="${not empty q.pesoQuestao}">
              <p style="margin: 6px 0; color: #444">
                <strong>Peso da questão:</strong> ${q.pesoQuestao}
              </p>
            </c:if>
          </div>
        </c:forEach>
      </div>

      <div style="margin-top: 16px">
        <c:if test="${not empty formulario}">
          <a
            class="btn-primary"
            href="${pageContext.request.contextPath}/lecio/questao?form=${formulario.id}&new=true"
            >+ Adicionar Nova Questão</a
          >
        </c:if>
        <c:if test="${empty formulario}">
          <button
            type="button"
            class="btn-primary"
            onclick="alert('Salve primeiro o cabeçalho do formulário para poder adicionar questões.');"
          >
            + Adicionar Nova Questão
          </button>
        </c:if>
      </div>
    </div>

    <script>
      function validateHeader() {
        const t = document.getElementById("titulo").value;
        if (!t || t.trim() === "") {
          alert("Título do formulário é obrigatório.");
          return false;
        }
        return true;
      }
    </script>
  </body>
</html>
