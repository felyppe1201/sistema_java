<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
  <head>
    <title>Dashboard</title>
  </head>
  <body>
    <h2>Bem-vindo ao sistema!</h2>

    <p>
      Usuário logado: <%= ((br.edu.avaliacao.models.Usuario)
      session.getAttribute("usuario")).getEmail() %>
    </p>

    <a href="<%= request.getContextPath() %>/auth/logout">Sair</a>
  </body>
</html>
