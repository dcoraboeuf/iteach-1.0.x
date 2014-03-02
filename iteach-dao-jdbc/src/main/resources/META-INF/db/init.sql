CREATE TABLE VERSION (
  VALUE   INTEGER   NOT NULL,
  UPDATED TIMESTAMP NOT NULL
);

CREATE TABLE ACCOUNT (
  ID            INTEGER      NOT NULL AUTO_INCREMENT,
  ADMINISTRATOR BOOLEAN      NOT NULL,
  MODE          VARCHAR(10)  NOT NULL,
  IDENTIFIER    VARCHAR(200) NOT NULL,
  PASSWORD      VARCHAR(300) NOT NULL,
  EMAIL         VARCHAR(100) NOT NULL,
  NAME          VARCHAR(80)  NOT NULL,
  VERIFIED      BOOLEAN      NOT NULL,
  DISABLED      BOOLEAN      NOT NULL,
  CONSTRAINT ACCOUNT_PK PRIMARY KEY (ID),
  CONSTRAINT ACCOUNT_UQ_IDENTIFIER UNIQUE (IDENTIFIER),
  CONSTRAINT ACCOUNT_UQ_EMAIL UNIQUE (EMAIL)
);

CREATE TABLE TOKEN (
  TOKEN     VARCHAR(200) NOT NULL,
  TOKENTYPE VARCHAR(20)  NOT NULL,
  TOKENKEY  VARCHAR(80)  NOT NULL,
  CREATION  TIMESTAMP    NOT NULL,
  CONSTRAINT TOKEN_PK PRIMARY KEY (TOKEN)
);
