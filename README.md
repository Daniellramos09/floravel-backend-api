# 🌸 Florável — Backend API

API REST em **Java 21** e **Spring Boot** para a plataforma **Florável**, responsável pelo acolhimento e suporte à saúde mental materna no puerpério.

---

## 🛠️ Tecnologias e Dependências

- **Java 21** (LTS)
- **Spring Boot**
- **Spring Data JPA** / Hibernate
- **Spring Web**
- **Bean Validation** (`jakarta.validation`)
- **MySQL Connector/J** & **H2 Database** (para desenvolvimento e testes)
- **Lombok**
- **Spring Boot DevTools**

---

## 🚀 Como executar o projeto

### Pré-requisitos
- JDK 21 instalado e configurado na variável `JAVA_HOME`.
- MySQL Server rodando (ou H2 para ambiente local).

### Execução via Maven Wrapper:

```bash
# Windows (PowerShell / CMD)
.\mvnw.cmd spring-boot:run

# Linux / Mac
./mvnw spring-boot:run
```

---

## 📋 Módulos Principais

1. **Rede de Apoio:** Catálogo de psicólogas com especialidade perinatal, luto, triagem, CRP e agendamentos com valor social ou Fundo Florável.
2. **Flores Solidárias / Gamificação:** Acompanhamento de flores acumuladas, resgate de consultas gratuitas e indicação de mães.
3. **Diário & Triagem:** Registro emocional da mãe e cálculo da escala de triagem (EPDS).
4. **Rodas de Conversa:** Gestão de encontros virtuais semanais e inscrições.
