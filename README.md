## **Trabalho da Disciplina – Linguagem de Programação Orientada a Objetos (DS142)**

### **Integrantes**

- **Felyppe Marcelo da Silva** – GRR20242028
    
- **João Pedro Rocha Senna** – GRR20241287
    
- **Paulo Roberto Gomes Barroso Schiochet** – GRR20242749
    

## **O trabalho é um sistema de avaliação com a integração de formulários e relatórios administrativos em um ambiente acadêmico.**

- O trabalho é para realizar aplicação de avaliações, gestões de formulários e de usuários, configuração de contextos avaliativos, e relatórios com resultados, via Web Browser, feito seguindo os requisitos da matéria de DS142 com o Professor Dieval. O sistema foi feito em cima da linguagem de programação Java(versão 17) para fins da utilização e aprendizado sobre JPA.

---

## **Como iniciar o sistema**

### **1. Configurar o Apache Tomcat**

Instale e configure o **Apache Tomcat 10.x**.  
O sistema depende dele para rodar localmente.

---

## **2. Criar o banco de dados**

No **MySQL Workbench**, crie um banco chamado **facul**.

A pasta **database** do projeto contém o script completo. Basta copiar e executar.

---

## **3. Criar o arquivo `.env`**

Crie um arquivo chamado `.env` dentro de:

```
src/main/resources
```

Conteúdo do arquivo:

```
DB_URL=jdbc:mysql://localhost:3306/facul
DB_USER=root
DB_PASS=1234
```

Troque os valores pelos seus reais.

Exemplo visual do Workbench mostrando nome do servidor e usuário:  

![Exemplo do MySQL](https://github.com/felyppe1201/sistema_java/blob/main/src/main/webapp/assets/mysql-config.png?raw=true)

---
## **4. Preparar o projeto (caso a pasta target exista ou não)**

### **Se a pasta `target` existir(no diretório raiz do projeto)**

Apague ela manualmente.

### **Se a pasta `target` não existir**

Abra um terminal no diretório raiz do projeto:

```
C:\Users\usuario\Área de Trabalho\sistema_java
```

E execute:

```
./mvn clean package
```

Se der erro:

```
mvn package
```

Isso criará a pasta `target` e dentro dela um arquivo `.war`.

---

## **5. Implantar no Tomcat**

Pegue o arquivo `.war` gerado na pasta `target` e mova para:

```
C:\apache-tomcat-10.1.49\webapps
```

---

## **6. Iniciar o servidor**

Abra um terminal dentro da pasta `bin` do Tomcat:

```
C:\apache-tomcat-10.1.49\bin
```

Execute para iniciar:

```
./startup.bat
```

O sistema será carregado localmente. Agora você pode abrir o navegador e acessar:

```
http://localhost:8080/avaliacao
```

---

## **7. Login**

Os usuários existentes são:

| Cargo:        | E-mail:          |
| ------------- | ---------------- |
| Administrador | admin@teste.com  |
| Coordenador   | coord@teste.com  |
| Professor     | prof@teste.com   |
| Aluna         | aluna1@teste.com |
| Aluno         | aluno2@teste.com |

### **Todos usam a senha padrão 123**

Certifique-se de que o **persistence.xml** está corretamente configurado para puxar dados do seu MySQL.

---

## **8. Encerrar o servidor**

No mesmo diretório `bin` do Tomcat, execute:

```sh
./shutdown.bat
```

---



