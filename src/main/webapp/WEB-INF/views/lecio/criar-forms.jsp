<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
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
      <div class="nav-logo">Criar Questão</div>
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

    <div class="page-container">
      <h1>Criar Formulário</h1>
      <p class="muted">Processo: ${processo != null ? processo.nome : 'N/D'}</p>

      <c:if test="${not empty msgSuccess}"
        ><div class="msg-success">${msgSuccess}</div></c:if
      >
      <c:if test="${not empty msgError}"
        ><div class="msg-error">${msgError}</div></c:if
      >

      <form
        id="mainForm"
        method="post"
        action="${pageContext.request.contextPath}/lecio/criar-forms"
        onsubmit="return validateAndSubmit();"
      >
        <input type="hidden" name="id_process" value="${processo.id}" />
        <input type="hidden" id="q_count" name="q_count" value="0" />

        <div class="info-box">
          <label>Título do Formulário (obrigatório)</label>
          <input
            type="text"
            name="titulo"
            id="titulo"
            required
            maxlength="255"
          />

          <label class="checkbox-inline">
            <input type="checkbox" name="identificado" checked /> Identificado
            (aluno precisa se identificar)
          </label>
        </div>

        <div id="questionsArea" class="questions-area"></div>

        <div style="margin-top: 12px">
          <button type="button" class="btn-primary" onclick="addQuestion()">
            + Adicionar Questão
          </button>
        </div>

        <div style="margin-top: 16px">
          <button type="submit" class="btn-primary">Salvar Formulário</button>
          <a
            class="btn-cancel"
            href="${pageContext.request.contextPath}/lecio/process?id=${processo.id}"
            >Cancelar</a
          >
        </div>
      </form>
    </div>

    <script>
      let qIndex = 0;

      function addQuestion() {
        const idx = qIndex++;
        const area = document.getElementById("questionsArea");

        const block = document.createElement("div");
        block.className = "question-block";
        block.id = `q_${idx}`;

        block.innerHTML = `
            <input type="hidden" name="q_${idx}_exists" value="1">
            <div class="q-header">
              <strong>Questão ${idx + 1}</strong>
              <button type="button" class="btn-remove" onclick="removeQuestion(${idx})">Remover</button>
            </div>

            <label>Enunciado:</label>
            <textarea name="q_${idx}_texto" rows="3" required></textarea>

            <div style="margin-top:8px">
              <label>Tipo:</label>
              <select name="q_${idx}_tipo" onchange="updateQuestionType(${idx})">
                <option value="obj">Objetiva</option>
                <option value="vf">Verdadeiro/Falso</option>
                <option value="disc">Dissertativa</option>
              </select>

              <label style="margin-left:12px">Obrigatória:</label>
              <input type="checkbox" name="q_${idx}_obrigatoria">

              <label style="margin-left:12px">Peso (questão):</label>
              <input type="text" name="q_${idx}_peso" placeholder="ex: 1.00" class="peso-input" />
            </div>

            <div id="q_${idx}_options" class="opts-container"></div>
            <input type="hidden" id="q_${idx}_opt_count" name="q_${idx}_opt_count" value="0">
        `;

        area.appendChild(block);

        // atualiza contador global (essa função apenas marca o índice máximo criado)
        document.getElementById("q_count").value = qIndex;

        // inicializa a UI da questão com base no select atual
        initQuestion(idx);
      }

      function initQuestion(idx) {
        // chama updateQuestionType imediatamente (o select já existe)
        updateQuestionType(idx);
      }

      function removeQuestion(idx) {
        const el = document.getElementById(`q_${idx}`);
        if (el) el.remove();
      }

      function updateQuestionType(idx) {
        const tipoSelect = document.querySelector(
          `select[name="q_${idx}_tipo"]`
        );
        if (!tipoSelect) return;
        const tipo = tipoSelect.value;
        const optsContainer = document.getElementById(`q_${idx}_options`);
        const optCountEl = document.getElementById(`q_${idx}_opt_count`);
        // reset container
        optsContainer.innerHTML = "";
        optCountEl.value = "0";

        if (tipo === "disc") {
          // nada a fazer (dissertativa não tem opções)
          return;
        }

        // cria botão de adicionar opção
        const addBtn = document.createElement("button");
        addBtn.type = "button";
        addBtn.className = "btn-add-opt";
        addBtn.textContent =
          tipo === "vf" ? "Adicionar Afirmação (VF)" : "Adicionar Opção";
        addBtn.onclick = () => {
          if (tipo === "vf") addOptionVF(idx);
          else addOption(idx);
        };
        optsContainer.appendChild(addBtn);

        // para VF podemos pré-adicionar duas opções V/F se quiser — não obriguei
        // deixei vazio para o autor adicionar afirmações livremente.
      }

      function addOption(qIdx) {
        const container = document.getElementById(`q_${qIdx}_options`);
        const countEl = document.getElementById(`q_${qIdx}_opt_count`);
        let count = parseInt(countEl.value || "0", 10);

        const row = document.createElement("div");
        row.className = "opt-row";
        row.id = `q_${qIdx}_opt_${count}`;

        row.innerHTML = `
            <input type="text" name="q_${qIdx}_opt_${count}_texto" placeholder="Texto da opção" required style="width:55%">
            <label class="inline"><input type="checkbox" name="q_${qIdx}_opt_${count}_correta" /> Correta</label>
            <input type="text" name="q_${qIdx}_opt_${count}_peso" placeholder="peso (opcional)" class="peso-input" />
            <button type="button" class="btn-remove-opt" onclick="removeOption(${qIdx}, ${count})">Remover</button>
        `;

        container.appendChild(row);
        count++;
        countEl.value = String(count);
      }

      function addOptionVF(qIdx) {
        const container = document.getElementById(`q_${qIdx}_options`);
        const countEl = document.getElementById(`q_${qIdx}_opt_count`);
        let count = parseInt(countEl.value || "0", 10);

        const row = document.createElement("div");
        row.className = "opt-row";
        row.id = `q_${qIdx}_opt_${count}`;

        row.innerHTML = `
            <input type="text" name="q_${qIdx}_opt_${count}_texto" placeholder="Afirmação (ex: 'O professor explica bem')" required style="width:55%">
            <label>Valor:
              <select name="q_${qIdx}_opt_${count}_respostavf">
                <option value="true">Verdadeiro</option>
                <option value="false">Falso</option>
              </select>
            </label>
            <input type="text" name="q_${qIdx}_opt_${count}_peso" placeholder="peso (opcional)" class="peso-input" />
            <button type="button" class="btn-remove-opt" onclick="removeOption(${qIdx}, ${count})">Remover</button>
        `;

        container.appendChild(row);
        count++;
        countEl.value = String(count);
      }

      function removeOption(qIdx, optIdx) {
        const el = document.getElementById(`q_${qIdx}_opt_${optIdx}`);
        if (!el) return;
        el.remove();

        // decrementa contador (simples) — não reindexamos ids; servidor ignora opções vazias
        const countEl = document.getElementById(`q_${qIdx}_opt_count`);
        let c = parseInt(countEl.value || "0", 10);
        c = Math.max(0, c - 1);
        countEl.value = String(c);
      }

      function validateAndSubmit() {
        const titulo = document.getElementById("titulo").value;
        if (!titulo || titulo.trim() === "") {
          alert("Título do formulário é obrigatório.");
          return false;
        }

        // valida pesos no cliente: formato números com até 2 decimais e >= 1 (se preenchidos)
        const pesoInputs = document.querySelectorAll(".peso-input");
        const pesoRegex = /^\d+(\.\d{1,2})?$/;
        for (let i = 0; i < pesoInputs.length; i++) {
          const v = pesoInputs[i].value.trim();
          if (v === "") continue;
          if (!pesoRegex.test(v.replace(",", "."))) {
            alert(
              "Formato de peso inválido. Use número com até 2 casas decimais (ex: 1.00)."
            );
            return false;
          }
          const num = parseFloat(v.replace(",", "."));
          if (isNaN(num) || num < 1.0) {
            alert("Peso deve ser >= 1.00.");
            return false;
          }
        }

        // checar existe ao menos uma questão com texto
        const qcount = parseInt(
          document.getElementById("q_count").value || "0",
          10
        );
        let found = false;
        for (let i = 0; i < qcount; i++) {
          const exists = document.querySelector(`input[name="q_${i}_exists"]`);
          if (!exists) continue;
          const texto = document.querySelector(`textarea[name="q_${i}_texto"]`);
          if (texto && texto.value.trim() !== "") {
            found = true;
            break;
          }
        }
        if (!found) {
          alert("Adicione pelo menos uma questão com enunciado.");
          return false;
        }

        return true;
      }
    </script>
  </body>
</html>
