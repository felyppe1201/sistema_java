<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
  <head>
    <title>Login</title>
    <meta
      name="viewport"
      content="width=device-width,initial-scale=1,viewport-fit=cover"
    />
    <link
      rel="stylesheet"
      type="text/css"
      href="${pageContext.request.contextPath}/assets/css/login.css"
    />
  </head>

  <body class="page-container">
    <div class="login-card">
      <h2 class="login-title">LOGIN</h2>

      <form
        action="${pageContext.request.contextPath}/auth/login"
        method="post"
        class="login-form"
      >
        <label for="email">Email</label>
        <input
          id="email"
          type="text"
          name="email"
          required
          autocomplete="username"
        />

        <label for="senha">Senha</label>
        <input
          id="senha"
          type="password"
          name="senha"
          required
          autocomplete="current-password"
        />

        <button type="submit">Entrar</button>
      </form>

      <% String erro = request.getParameter("erro"); if (erro != null) { %>
      <p class="erro-msg">Credenciais inválidas.</p>
      <% } %>

      <div class="login-footer" aria-hidden="true">
        © <%= java.time.Year.now() %> Sistema de provas
      </div>
    </div>
  </body>
</html>
