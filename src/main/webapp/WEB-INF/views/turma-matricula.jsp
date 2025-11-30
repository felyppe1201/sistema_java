<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
  <head>
    <title>Matrícula em Turmas</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link
      rel="stylesheet"
      type="text/css"
      href="${pageContext.request.contextPath}/assets/css/reset.css"
    />
    <link
      rel="stylesheet"
      type="text/css"
      href="${pageContext.request.contextPath}/assets/css/alunoDashboard.css"
    />
    <link
      rel="stylesheet"
      type="text/css"
      href="${pageContext.request.contextPath}/assets/css/turma-matricula.css"
    />
    <link
      rel="stylesheet"
      type="text/css"
      href="${pageContext.request.contextPath}/assets/css/navbar.css"
    />
    <!-- Adicionando estilos específicos, se necessário -->
  </head>

  <body>
    <nav class="navbar no-select">
      <div class="nav-logo">Sistema Acadêmico</div>
      <button class="nav-toggle" onclick="toggleMenu()">☰</button>
      <ul id="nav-links" class="nav-links">
        <li>
          <a href="${pageContext.request.contextPath}/dashboard">Painel</a>
        </li>
        <li>
          <a href="${pageContext.request.contextPath}/turma-matricula"
            >Matrículas</a
          >
        </li>
        <li><a href="${pageContext.request.contextPath}/conta">Conta</a></li>
        <li>
          <a href="${pageContext.request.contextPath}/auth/logout">Logout</a>
        </li>
      </ul>
    </nav>

    <main class="dashboard-content no-select">
      <h1 class="titulo">Matrícula em Turmas</h1>

      <div class="info-card mb-6">
        <h2>Turmas Disponíveis</h2>
        <a href="${pageContext.request.contextPath}/dashboard" class="btn-back">
          &leftarrow; Voltar ao Painel
        </a>
      </div>
      
      <!-- Lista de Turmas -->
      <section class="turmas-section">
          <div id="enrollment-list-container" class="turmas-list">
              <!-- Conteúdo carregado por JavaScript -->
              <p class="loading-text">Carregando turmas...</p>
          </div>
      </section>
      
      <!-- Caixa de Mensagem (Feedback) -->
      <div id="message-box" class="message-box message-box-hidden">
        <!-- Mensagens de sucesso ou erro serão exibidas aqui -->
      </div>
    </main>
    
    <!-- Modal de Confirmação (Mantido como modal, pois era o que o fluxo original pedia) -->
    <div id="confirmation-modal" class="modal-backdrop modal-hidden">
        <div class="modal-content-sm">
            <h3 class="modal-title-center">Confirmar Matrícula</h3>
            <p id="confirm-message" class="modal-text-center">Deseja realmente se matricular?</p>
            
            <div class="modal-actions">
                <button id="cancel-enroll-btn" class="btn-cancel">Cancelar</button>
                <button id="confirm-enroll-btn" data-turma-id="" class="btn-confirm">Confirmar</button>
            </div>
        </div>
    </div>
    
    <script src="https://www.gstatic.com/firebasejs/11.6.1/firebase-app.js"></script>
    <script src="https://www.gstatic.com/firebasejs/11.6.1/firebase-auth.js"></script>
    <script src="https://www.gstatic.com/firebasejs/11.6.1/firebase-firestore.js"></script>

    <script type="module">
      import { initializeApp } from "https://www.gstatic.com/firebasejs/11.6.1/firebase-app.js";
      import { getAuth, signInWithCustomToken, signInAnonymously } from "https://www.gstatic.com/firebasejs/11.6.1/firebase-auth.js";
      import { getFirestore, doc, getDoc, collection, query, where, getDocs, addDoc } from "https://www.gstatic.com/firebasejs/11.6.1/firebase-firestore.js";

      // Variáveis globais fornecidas pelo ambiente (Firestore)
      const appId = typeof __app_id !== 'undefined' ? __app_id : 'default-app-id';
      const firebaseConfig = JSON.parse(typeof __firebase_config !== 'undefined' ? __firebase_config : '{}');

      // Inicialização do Firebase
      const app = initializeApp(firebaseConfig);
      const db = getFirestore(app);
      const auth = getAuth(app);

      let currentUser = null;
      let studentCourseId = null;
      let studentPeriod = null; 
      
      // Obtém o ID do usuário do JSP (simulado: substitua por uma forma segura no seu backend)
      const initialStudentId = 'aluno4_simulado'; 
      const initialPeriod = parseInt(document.getElementById('periodo-info').textContent.replace(/[^\d]/g, '') || '0', 10);

      // Funções de utilidade
      function getCollectionRef(name) {
          return collection(db, `artifacts/${appId}/public/data/${name}`);
      }

      document.addEventListener('DOMContentLoaded', async () => {
          await initializeAuth();
          
          if (currentUser) {
              currentUser.uid = initialStudentId; // Usando ID fixo para simulação
              await fetchStudentData();
              await fetchAvailableClasses();
              
              // Adiciona eventos aos botões do modal de confirmação
              document.getElementById('cancel-enroll-btn').addEventListener('click', () => {
                  document.getElementById('confirmation-modal').classList.add('modal-hidden');
              });

              document.getElementById('confirm-enroll-btn').addEventListener('click', async (e) => {
                  document.getElementById('confirmation-modal').classList.add('modal-hidden');
                  const turmaId = e.target.getAttribute('data-turma-id');
                  if (turmaId) {
                      await enrollStudent(turmaId);
                  }
              });

          } else {
              document.getElementById('enrollment-list-container').innerHTML = `<p class="error-message">Erro: Usuário não autenticado.</p>`;
          }
      });

      async function initializeAuth() {
          try {
              if (typeof __initial_auth_token !== 'undefined' && __initial_auth_token) {
                  const userCredential = await signInWithCustomToken(auth, __initial_auth_token);
                  currentUser = userCredential.user;
              } else {
                  const userCredential = await signInAnonymously(auth);
                  currentUser = userCredential.user;
              }
          } catch (error) {
              console.error("Erro na autenticação:", error);
          }
      }

      // Tenta obter os dados do aluno do Firestore, priorizando-os
      async function fetchStudentData() {
          const studentIdSimulated = currentUser.uid; 
          
          try {
              const q = query(getCollectionRef('aluno_matriculado'), where('id_usuario', '==', studentIdSimulated), where('ativo', '==', true));
              const snapshot = await getDocs(q);
            
              if (!snapshot.empty) {
                  const data = snapshot.docs[0].data();
                  studentCourseId = data.curso_id;
                  studentPeriod = typeof data.periodo === 'number' ? data.periodo : (data.periodo ? parseInt(data.periodo, 10) : null); 
                
                  if (studentPeriod !== null) {
                      document.getElementById('periodo-info').textContent = studentPeriod;
                  }
              }
          } catch (e) {
              console.error("Erro ao buscar dados do aluno no Firestore:", e);
              // Fallback: usar o valor do JSP se o Firestore falhar (menos seguro)
              studentPeriod = initialPeriod;
          }
      }

      // --- Lógica de Busca e Matrícula ---

      async function fetchAvailableClasses() {
          const container = document.getElementById('enrollment-list-container');
          container.innerHTML = `<div class="loading-ring my-6"></div><p class="loading-text">Buscando turmas elegíveis...</p>`;

          if (!studentCourseId || studentPeriod === null || studentPeriod === 0) {
              container.innerHTML = `<p class="error-message">Não foi possível determinar o curso ou período do aluno. Matrícula indisponível.</p>`;
              return;
          }

          try {
              // 1. Buscar IDs de Disciplinas que pertencem ao curso do aluno
              const disciplinesQuery = query(getCollectionRef('disciplinas'), where('curso_id', '==', studentCourseId));
              const disciplinesSnapshot = await getDocs(disciplinesQuery);
              
              const disciplineMap = new Map();
              const eligibleDisciplineIds = [];
              disciplinesSnapshot.docs.forEach(doc => {
                  disciplineMap.set(doc.id, doc.data().nome);
                  eligibleDisciplineIds.push(doc.id);
              });
              
              if (eligibleDisciplineIds.length === 0) {
                  container.innerHTML = `<p class="sem-turmas">Nenhuma disciplina vinculada ao seu curso foi encontrada.</p>`;
                  return;
              }

              // 2. Buscar turmas do período, ativas e com stat=1 (matrícula aberta)
              const allTurmasQuery = query(
                  getCollectionRef('turmas'), 
                  where('periodo', '==', studentPeriod), 
                  where('ativo', '==', true),
                  where('stat', '==', 1) 
              );
              const turmasSnapshot = await getDocs(allTurmasQuery);
              
              const turmasData = turmasSnapshot.docs.map(doc => ({ id: doc.id, ...doc.data() }));

              // 3. Filtrar turmas para garantir que a disciplina pertence ao curso
              const turmasByCourseAndPeriod = turmasData.filter(turma => 
                  eligibleDisciplineIds.includes(turma.disciplina_id)
              );
              
              // 4. Filtrar turmas em que o aluno JÁ ESTÁ matriculado
              const matriculasQuery = query(getCollectionRef('matriculas'), where('idAluno', '==', currentUser.uid), where('ativo', '==', true));
              const matriculasSnapshot = await getDocs(matriculasQuery);
              const enrolledTurmaIds = matriculasSnapshot.docs.map(doc => doc.data().idTurma);

              const turmasToDisplay = [];

              for (const turma of turmasByCourseAndPeriod) {
                  if (enrolledTurmaIds.includes(turma.id)) {
                      continue; // Aluno já está matriculado
                  }

                  // 5. Checar vagas disponíveis
                  const currentEnrollmentQuery = query(getCollectionRef('matriculas'), where('idTurma', '==', turma.id), where('ativo', '==', true));
                  const currentEnrollmentSnapshot = await getDocs(currentEnrollmentQuery);
                  const currentEnrollmentCount = currentEnrollmentSnapshot.size;

                  const vagas = parseInt(turma.numero_vagas, 10);
                  
                  if (!isNaN(vagas) && vagas > currentEnrollmentCount) {
                      const disciplineName = disciplineMap.get(turma.disciplina_id) || 'Disciplina Desconhecida';
                      turmasToDisplay.push({
                          ...turma,
                          vagas_disponiveis: vagas - currentEnrollmentCount,
                          nome_disciplina: disciplineName
                      });
                  }
              }

              renderEnrollmentList(turmasToDisplay);

          } catch (error) {
              console.error("Erro ao buscar turmas elegíveis:", error);
              container.innerHTML = `<p class="error-message">Erro ao carregar turmas: ${error.message}</p>`;
          }
      }

      function renderEnrollmentList(turmas) {
          const container = document.getElementById('enrollment-list-container');
          container.innerHTML = '';

          if (turmas.length === 0) {
              container.innerHTML = `<p class="sem-turmas">Nenhuma turma elegível disponível no momento.</p>`;
              return;
          }

          turmas.forEach(turma => {
              const turmaCard = document.createElement('div');
              turmaCard.className = 'turma-card-enroll';
              turmaCard.innerHTML = `
                  <div class="turma-info">
                      <h3>${turma.nome_disciplina}</h3>
                      <p><strong>Código:</strong> ${turma.codigo_turma}</p>
                      <p><strong>Período:</strong> ${turma.periodo}</p>
                      <p><strong>Vagas Disponíveis:</strong> <span class="vagas-count">${turma.vagas_disponiveis}</span></p>
                  </div>
                  <button data-turma-id="${turma.id}" data-turma-nome="${turma.nome_disciplina}"
                          class="btn-enroll-action">
                      Matricular
                  </button>
              `;
              container.appendChild(turmaCard);
          });

          document.querySelectorAll('.btn-enroll-action').forEach(button => {
              button.addEventListener('click', (e) => {
                  const turmaId = e.target.getAttribute('data-turma-id');
                  const turmaNome = e.target.getAttribute('data-turma-nome');
                  showConfirmationModal(turmaId, turmaNome);
              });
          });
      }
      
      function showConfirmationModal(turmaId, turmaNome) {
          document.getElementById('confirm-message').textContent = `Deseja realmente se matricular na turma de ${turmaNome}?`;
          document.getElementById('confirm-enroll-btn').setAttribute('data-turma-id', turmaId);
          document.getElementById('confirmation-modal').classList.remove('modal-hidden');
      }

      async function enrollStudent(turmaId) {
          const messageBox = document.getElementById('message-box');
          messageBox.classList.remove('message-box-error', 'message-box-success', 'message-box-hidden');
          messageBox.textContent = 'Processando matrícula...';

          try {
              if (!currentUser || !currentUser.uid) throw new Error("Usuário não autenticado ou ID de aluno inválido.");

              const newMatricula = {
                  idTurma: turmaId, 
                  idAluno: currentUser.uid, 
                  ativo: true,
                  stat: 1, 
                  data_matricula: new Date().toISOString()
              };

              await addDoc(getCollectionRef('matriculas'), newMatricula);

              // Atualiza o feedback visual
              messageBox.classList.add('message-box-success');
              messageBox.textContent = 'Matrícula realizada com sucesso!';
              
              // Recarrega a lista para mostrar a turma como indisponível e atualizar a contagem de vagas
              await fetchAvailableClasses();

          } catch (error) {
              console.error("Erro ao matricular aluno:", error);
              messageBox.classList.add('message-box-error');
              messageBox.textContent = `Erro ao matricular: ${error.message}`;
          } finally {
              setTimeout(() => {
                  messageBox.classList.add('message-box-hidden');
              }, 5000);
          }
      }

      function toggleMenu() {
        document.getElementById("nav-links").classList.toggle("active");
      }
    </script>
  </body>
</html>