<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib uri="http://java.sun.com/jsp/jstl/core"
prefix="c" %> <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="pt-br">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Relatórios Consolidados - Professor</title>
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/assets/css/relatorio.css"
    />
    <link
      rel="stylesheet"
      type="text/css"
      href="${pageContext.request.contextPath}/assets/css/navbar.css"
    />
  </head>
  <body>
    <nav class="navbar no-select">
      <div class="nav-logo">Sistema Acadêmico</div>
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

    <div id="app-container">
      <main class="container main-content">
        <div class="card intro-card">
          <h2 class="card-title">Relatórios Consolidados</h2>
          <p class="intro-text">
            Bem-vindo(a), Professor(a)
            <c:out value="${nomeProfessor}" default="[Nome do Professor]" />!
          </p>
        </div>

        <c:if test="${not empty erroRelatorio}">
          <div class="alert alert-error" role="alert">
            <p class="alert-title">Erro ao Carregar Dados</p>
            <p class="alert-message"><c:out value="${erroRelatorio}" /></p>
          </div>
        </c:if>

        <c:choose>
          <c:when test="${empty relatorios}">
            <div class="card empty-state" role="alert">
              <div class="empty-icon"></div>
              <h3 class="empty-title">Nenhum Relatório Disponível</h3>
              <p class="empty-message">
                Parece que ainda não há dados consolidados para as suas turmas.
              </p>
            </div>
          </c:when>

          <c:otherwise>
            <c:forEach var="relatorio" items="${relatorios}">
              <div class="card report-card">
                <h3 class="report-title">
                  Turma:
                  <c:out
                    value="${relatorio.codigoTurma}"
                    default="Turma Desconhecida"
                  />
                  (<c:out
                    value="${relatorio.nomeDisciplina}"
                    default="Disciplina Desconhecida"
                  />)
                </h3>

                <div class="stats-grid">
                  <div class="stat-card stat-enrolled">
                    <p class="stat-label">Total de Alunos Matriculados</p>
                    <p class="stat-value">
                      <c:out
                        value="${relatorio.totalAlunosMatriculados}"
                        default="0"
                      />
                    </p>
                  </div>

                  <div class="stat-card stat-collected">
                    <p class="stat-label">Respostas Coletadas</p>
                    <p class="stat-value">
                      <c:out
                        value="${relatorio.totalRespostasColetadas}"
                        default="0"
                      />
                    </p>
                  </div>

                  <div class="stat-card stat-completion">
                    <p class="stat-label">Taxa de Conclusão</p>
                    <p class="stat-value">
                      <c:set
                        var="taxaConclusao"
                        value="${relatorio.taxaConclusao != null ? relatorio.taxaConclusao * 100 : 0}"
                      />
                      <fmt:formatNumber
                        value="${taxaConclusao}"
                        maxFractionDigits="0"
                      />%
                    </p>
                  </div>
                </div>

                <c:forEach var="form" items="${relatorio.formularios}">
                  <h4 class="section-title">
                    Formulário: <c:out value="${form.titulo}" />
                  </h4>

                  <c:if test="${not form.identificado}">
                    <div class="card anonimo-card">
                      <p class="anonimo-text">
                        Formulário anônimo — estatísticas por alternativa não
                        estão disponíveis.
                      </p>
                      <p class="meta-info">
                        Submissões:
                        <c:out value="${form.totalSubmissions}" default="0" />
                      </p>
                    </div>
                  </c:if>

                  <c:if test="${form.identificado}">
                    <c:forEach
                      var="questao"
                      items="${form.estatisticasQuestoes}"
                    >
                      <div class="question-item">
                        <div class="question-header">
                          <span class="question-text">
                            <c:out value="${questao.codigoQuestao}" />:
                            <c:out value="${questao.textoQuestao}" />
                          </span>

                          <div class="score-display">
                            <span class="score-label">Score Total</span>
                            <span class="score-value">
                              <fmt:formatNumber
                                value="${questao.scoreTotal}"
                                maxFractionDigits="0"
                              />
                              <span class="score-percent">%</span>
                            </span>
                          </div>
                        </div>

                        <p class="alternatives-label">
                          Distribuição de Respostas:
                        </p>

                        <div class="alternatives-list">
                          <c:forEach
                            var="alternativa"
                            items="${questao.alternativas}"
                          >
                            <div class="alternative-item">
                              <span class="alternative-text">
                                <c:out
                                  value="${alternativa.textoAlternativa}"
                                />
                              </span>

                              <c:choose>
                                <c:when
                                  test="${alternativa.percentualV != 0.0 || alternativa.percentualF != 0.0}"
                                >
                                  <div class="vf-container">
                                    <div class="vf-row">
                                      <div class="vf-label">V</div>
                                      <div class="vf-bar-wrap">
                                        <c:set
                                          var="pv"
                                          value="${alternativa.percentualV != null ? alternativa.percentualV * 100 : 0}"
                                        />
                                        <fmt:formatNumber
                                          value="${pv}"
                                          maxFractionDigits="0"
                                          var="pvInt"
                                        />
                                        <div
                                          class="vf-bar v"
                                          style="width:${pvInt}%"
                                        ></div>
                                      </div>
                                      <div class="vf-percent">
                                        <fmt:formatNumber
                                          value="${pv}"
                                          maxFractionDigits="0"
                                        />%
                                      </div>
                                    </div>

                                    <div class="vf-row">
                                      <div class="vf-label">F</div>
                                      <div class="vf-bar-wrap">
                                        <c:set
                                          var="pf"
                                          value="${alternativa.percentualF != null ? alternativa.percentualF * 100 : 0}"
                                        />
                                        <fmt:formatNumber
                                          value="${pf}"
                                          maxFractionDigits="0"
                                          var="pfInt"
                                        />
                                        <div
                                          class="vf-bar f"
                                          style="width:${pfInt}%"
                                        ></div>
                                      </div>
                                      <div class="vf-percent">
                                        <fmt:formatNumber
                                          value="${pf}"
                                          maxFractionDigits="0"
                                        />%
                                      </div>
                                    </div>
                                  </div>
                                </c:when>

                                <c:otherwise>
                                  <div class="progress-bar-container">
                                    <c:set
                                      var="p"
                                      value="${alternativa.percentualResposta != null ? alternativa.percentualResposta * 100 : 0}"
                                    />
                                    <fmt:formatNumber
                                      value="${p}"
                                      maxFractionDigits="0"
                                      var="pInt"
                                    />
                                    <div
                                      class="progress-bar"
                                      style="width:${pInt}%"
                                    ></div>
                                  </div>
                                  <span class="alternative-percent">
                                    <fmt:formatNumber
                                      value="${p}"
                                      maxFractionDigits="0"
                                    />%
                                  </span>
                                </c:otherwise>
                              </c:choose>
                            </div>
                          </c:forEach>
                        </div>
                      </div>
                    </c:forEach>
                  </c:if>
                </c:forEach>
              </div>
            </c:forEach>
          </c:otherwise>
        </c:choose>
      </main>
    </div>

    <script>
      function toggleMenu() {
        const links = document.getElementById("nav-links");
        links.classList.toggle("show");
      }
    </script>
  </body>
</html>
