
Projeto Maven (WAR) - Sistema de Avaliação (esqueleto)
-----------------------------------------------------
Estrutura criada:
- src/main/java/br/edu/avaliacao/models   (entidades / DTOs)
- src/main/java/br/edu/avaliacao/repository (DAOs esqueletos)
- src/main/java/br/edu/avaliacao/utils   (ConnectionFactory)
- src/main/java/br/edu/avaliacao/servlet (Servlets esqueleto)
- src/main/webapp/WEB-INF/web.xml
- src/main/webapp/index.jsp, login.jsp

Instruções rápidas:
1) Ajuste as configurações de conexão no ConnectionFactory.java
2) Build: mvn clean package
3) Deploy em Tomcat/Payara/Jetty (WAR gerado em target/avaliacao.war)
