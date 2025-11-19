<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Dashboard do Professor</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/navbar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/generico.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/professorDashboard.css">
    
</head>
<body class="bg-gray">
    <nav class="navbar no-select">
        <div class="nav-logo">Sistema Acadêmico - PROFESSOR</div>
        <button class="nav-toggle" onclick="toggleMenu()">☰</button>
        <ul id="nav-links" class="nav-links">
            <li><a href="${pageContext.request.contextPath}/dashboard/professor">Painel</a></li>
            <li><a href="${pageContext.request.contextPath}/conta">Conta</a></li>
            <li><a href="${pageContext.request.contextPath}/auth/logout">Logout</a></li>
        </ul>
    </nav>
    
    <div class="dashboard-container">
        <h1>Dashboard do Professor</h1>
        <p class="text-secondary">Bem-vindo, ${professor.nome}!</p>

        <!-- Container das Abas -->
        <div class="m-t-5">
            
            <!-- Cabeçalho das Abas -->
            <div class="tabs-header">
                <button class="tab-button active" onclick="openTab('visao-geral', this)">Visão Geral</button>
                <button class="tab-button" onclick="openTab('turmas', this)">Minhas Turmas</button>
                <button class="tab-button" onclick="openTab('formularios', this)">Formulários</button>
            </div>

            <!-- Conteúdo da Aba 1: VISÃO GERAL -->
            <div id="visao-geral" class="tab-content active">
                <h2>Informações de Perfil</h2>
                <div class="info-card bg-white p-6 flex flex-col gap-3 rounded-lg m-b-4 text-left">
                     <div class="flex-col">
                        <span class="info-label">NOME COMPLETO</span>
                        <span class="info-value text-xl">${professor.nome}</span>
                    </div>
                     <div class="flex-col">
                        <span class="info-label">CARGO</span>
                        <span class="info-value text-lg">${professor.cargo}</span>
                    </div>
                    <div class="flex-col">
                        <span class="info-label">EMAIL CADASTRADO</span>
                        <span class="info-value text-lg">${professor.email}</span>
                    </div>
                </div>

                <h2>Indicadores Rápidos</h2>
                 <div class="card-grid">
                    <div class="info-card">
                        <div class="card-value">${turmas.size()}</div>
                        <div class="card-label">Total de Turmas Ativas</div>
                    </div>
                    
                    <div class="info-card">
                        <div class="card-value">${avaliacoesPendentes}</div>
                        <div class="card-label">Avaliações Pendentes</div>
                    </div>

                    <div class="info-card">
                        <div class="card-value">${alunosEmRisco}</div>
                        <div class="card-label">Alunos em Risco</div>
                    </div>
                </div>
            </div>

            <!-- Conteúdo da Aba 2: MINHAS TURMAS (Ação de gerenciar notas removida) -->
            <div id="turmas" class="tab-content">
                <h2>Turmas em Andamento</h2>
                <div class="bg-white rounded-lg p-4 shadow">
                    <c:choose>
                        <c:when test="${not empty turmas}">
                            <table class="turmas-table">
                                <thead>
                                    <tr>
                                        <th>ID Turma</th>
                                        <th>Disciplina</th>
                                        <th>Período</th>
                                        <!-- Coluna de Ações removida -->
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${turmas}" var="turma">
                                        <tr>
                                            <td>${turma.id}</td>
                                            <td>${turma.disciplina.nome}</td> 
                                            <td>${turma.periodo}°</td>
                                            <!-- Célula de Ações removida -->
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </c:when>
                        <c:otherwise>
                            <p class="text-secondary p-4">Você não está lecionando nenhuma turma neste momento.</p>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <!-- Conteúdo da Aba 3: FORMULÁRIOS (Simplificada para apenas um botão) -->
            <div id="formularios" class="tab-content">
                <h2>Criação de Formulários</h2>
                <p class="text-secondary">Clique abaixo para iniciar o processo de criação de um novo formulário (avaliação, pesquisa, etc.).</p>
                
                <div class="m-t-4">
                    <!-- Botão único "Criar Formulário" -->
                    <a href="${pageContext.request.contextPath}/formulario/criar" class="btn btn-primary bg-blue action-link p-3 rounded-lg">Criar Formulário</a>
                    <p class="text-red m-t-4">**Atenção:** O URL `formulario/criar` é um placeholder. Ajuste-o conforme o mapeamento real do seu projeto.</p>
                </div>
            </div>
        </div>
    </div>
    <script>
        // Função para alternar o menu responsivo
        function toggleMenu() {
            const navLinks = document.getElementById('nav-links');
            if (navLinks.classList.contains('active') || navLinks.classList.contains('nav-open')) {
                navLinks.classList.remove('active');
                navLinks.classList.remove('nav-open');
            } else {
                navLinks.classList.add('active');
            }
        }
        // Função para alternar as abas
        function openTab(tabId, element) {
            // Remove a classe 'active' de todos os conteúdos
            document.querySelectorAll('.tab-content').forEach(content => {
                content.classList.remove('active');
            });

            // Remove a classe 'active' de todos os botões
            document.querySelectorAll('.tab-button').forEach(btn => {
                btn.classList.remove('active');
            });

            // Adiciona a classe 'active' ao conteúdo da aba clicada
            document.getElementById(tabId).classList.add('active');

            // Adiciona a classe 'active' ao botão clicado
            element.classList.add('active');
        }
    </script>
</body>
</html>