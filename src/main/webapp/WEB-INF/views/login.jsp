<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>

<html>
  <head>
    <title>Login</title>
    <link
      rel="stylesheet"
      type="text/css"
      href="${pageContext.request.contextPath}/assets/css/generico.css"
    />
  </head>
  <body class="bg-gray flex justify-center items-center h-full">
    <div class="bg-white p-5 rounded-lg w-25vw flex flex-col items-center">
      <h2 class="text-blue text-center">LOGIN</h2>

      ```
      <form
        action="${pageContext.request.contextPath}/auth/login"
        method="post"
        class="flex flex-col w-full mt-3"
      >
        <label class="font-bold">Email:</label>
        <input type="text" name="email" required />

        <label class="font-bold mt-2">Senha:</label>
        <input type="password" name="senha" required />

        <button type="submit" class="mt-4">Entrar</button>
      </form>

      <% String erro = request.getParameter("erro"); if (erro != null) { %>
      <p class="text-red mt-2">Credenciais inválidas.</p>
      <% } %>
    </div>
    ```
  </body>
</html>
