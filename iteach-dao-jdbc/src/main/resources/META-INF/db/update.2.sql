-- v2
-- Preferences

CREATE TABLE PREFERENCES (
  ID      INTEGER      NOT NULL AUTO_INCREMENT,
  TEACHER INTEGER      NOT NULL,
  NAME    VARCHAR(40)  NOT NULL,
  VALUE   VARCHAR(300) NOT NULL,
  CONSTRAINT PREFERENCES_PK PRIMARY KEY (ID),
  CONSTRAINT PREFERENCES_UQ_NAME UNIQUE (TEACHER, NAME),
  CONSTRAINT PREFERENCES_FK_TEACHER FOREIGN KEY (TEACHER) REFERENCES ACCOUNT (ID)
    ON DELETE CASCADE
);

-- @rollback

DROP TABLE IF EXISTS PREFERENCES;