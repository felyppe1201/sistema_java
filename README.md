## Trabalho da Disciplina - Linguagem de Programação Orientada a Objetos(DS142)
Integrantes do Trabalho:
Felyppe Marcelo Silva - GRR20242028
João Pedro Rocha Senna - GRR20241287
Paulo Roberto Gomes Barroso Schiochet - GRR20242749
### O trabalho é um sistema de avaliação com a integração de formulários e relatórios administrativos em um ambiente acadêmico.
- O trabalho é para realizar aplicação de avaliações, gestões de formulários e de usuários, configuração de contextos avaliativos, e relatórios com resultados, via Web Browser, feito seguindo os requisitos da matéria de DS142 com o Professor Dieval. O sistema foi feito em cima da linguagem de programação Java(versão 17) para fins da utilização e aprendizado sobre JPA.

## Como iniciar o sistema?
Para iniciar dentro do sistema de avaliação, é necessário a seguinte sequência de inputs vindo de você(o usuário) para visualizar e mexer no sistema.
Apache: Utilizamos de servidor Apache Tomcat 10.x, não se esqueça de configurar-lo para ter acesso ao sistema.
Passo zero: Caso haja a presença da pasta Target, remova ela. Sua presença faz com que a aplicação não rode como devido.
Boot-up: Caso não haja a presença da pasta target, utilize o comando ./mvnw clean package, ele criara um arquivo .war, o que possibilita rodar o programa localmente. Logo após isso, utilize o comando ./startup.sh, assim o programa irá rodar localmente dentro da sua máquina e você poderá acessar ele enquanto não houver interrupções 
O login: Você será levado a tela de login, nosso banco de dados há maneiras de logar dentro do sistema, mas sinta-se livre para criar seu próprio usuário, seja ele administrador, professor ou aluno. Certifique-se que seu banco de dados está conectado com nosso sistema através do persistence.xml, lá iremos puxar dados para poder-mos obter os logins, resultados, formulários etc.

Desligamento: Se tiver satisfeito ou deseja não utilizar mais o nosso sistema, não se esqueça de rodar ./shutdown.sh para desligar o sistema localmente.
