<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>Turma ${turma.codigoTurma}</title>
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/assets/css/navbar.css"
    />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/assets/css/turma-crud.css"
    />
  </head>
  <body class="bg-gray">
    <nav class="navbar no-select">
      <div class="nav-logo">Sistema Acadêmico - PROFESSOR</div>
      <button class="nav-toggle" onclick="toggleMenu()">☰</button>
      <ul id="nav-links" class="nav-links">
        <li>
          <a href="${pageContext.request.contextPath}/dashboard/professor"
            >Painel</a
          >
        </li>
        <li><a href="${pageContext.request.contextPath}/conta">Conta</a></li>
        <li>
          <a href="${pageContext.request.contextPath}/auth/logout">Logout</a>
        </li>
      </ul>
    </nav>

    <h1>${turma.codigoTurma}</h1>
    <p>
      Disciplina: ${turma.disciplina != null ? turma.disciplina.nome : 'N/D'}
    </p>
    <p>Período: ${turma.periodo}</p>

    <!-- Mensagens flash -->
    <c:if test="${not empty msgSuccess}">
      <div class="msg-success">${msgSuccess}</div>
    </c:if>
    <c:if test="${not empty msgError}">
      <div class="msg-error">${msgError}</div>
    </c:if>

    <hr />

    <h2>Processos Avaliativos</h2>

    <div class="processos-container">
      <c:choose>
        <c:when test="${not empty processos}">
          <c:forEach var="p" items="${processos}">
            <div class="process-item">
              <!-- Botão de acessar -->
              <a
                class="process-btn"
                href="${pageContext.request.contextPath}/lecio/process?id=${p.id}"
              >
                ${p.nome}
              </a>

              <!-- Botão de excluir -->
              <form
                method="post"
                action="${pageContext.request.contextPath}/lecio/turma"
                onsubmit="return confirmDelete(this);"
              >
                <input type="hidden" name="action" value="delete" />
                <input type="hidden" name="idProcesso" value="${p.id}" />
                <input type="hidden" name="id" value="${turma.id}" />

                <button type="submit" class="btn-delete-small">Excluir</button>
              </form>
            </div>
          </c:forEach>
        </c:when>
        <c:otherwise>
          <p>Nenhum processo avaliativo ativo nesta turma.</p>
        </c:otherwise>
      </c:choose>
    </div>

    <hr />

    <h2>Novo Processo Avaliativo</h2>

    <form
      id="createForm"
      method="post"
      action="${pageContext.request.contextPath}/lecio/turma"
      onsubmit="return disableOnSubmit(this);"
    >
      <input type="hidden" name="action" value="create" />
      <input type="hidden" name="id" value="${turma.id}" />

      <label for="nome">Nome do Processo:</label>
      <input
        id="nome"
        type="text"
        name="nome"
        required
        maxlength="255"
        style="width: 60%"
      />

      <button type="submit" class="btn-primary">Criar</button>
    </form>

    <script>
      function confirmDelete(form) {
        return confirm("Confirma exclusão (soft delete) deste processo?");
      }
      function disableOnSubmit(form) {
        const btn = form.querySelector('button[type="submit"]');
        if (btn) btn.disabled = true;
        return true;
      }
    </script>
  </body>
</html>
