<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
  <head>
    <title>Login</title>
  </head>
  <body>
    <h2>Acesso ao Sistema</h2>

    <form action="${pageContext.request.contextPath}/auth/login" method="post">
      <label>Email:</label><br />
      <input type="text" name="email" required /><br /><br />

      <label>Senha:</label><br />
      <input type="password" name="senha" required /><br /><br />

      <button type="submit">Entrar</button>
    </form>

    <% String erro = request.getParameter("erro"); if (erro != null) { %>
    <p style="color: red">Credenciais inválidas.</p>
    <% } %>
  </body>
</html>
