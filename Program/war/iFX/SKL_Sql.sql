alter session set "_ORACLE_SCRIPT"=true;

--密碼不過期
alter profile default limit password_life_time unlimited;

--查詢DBF位置
select * from dba_data_files;

--建立個別使用者表空間和臨時表空間
--請Replace相應位置{Locate} like /home/oracle19/oracle/oradata/ORACLE19C
--webadmin
create tablespace WEBADMIN_DATA datafile '{Locate}/webadmin_data.dbf' size 1024M autoextend on next 100M maxsize 10240M extent management local autoallocate;
create temporary tablespace WEBADMIN_TEMP tempfile '{Locate}/webadmin_temp.dbf' size 200M autoextend on next 40M maxsize 2048M;

--itxadminAS400 轉換資料使用
create bigfile tablespace ITXADMINAS400_DATA datafile '{Locate}/itxadminAS400_data.dbf' size 80G autoextend on next 4G maxsize 120G extent management local autoallocate;
create bigfile temporary tablespace ITXADMINAS400_TEMP tempfile '{Locate}/itxadminAS400_temp.dbf' size 10G autoextend on next 2G maxsize 20G;
--itxadmin
create bigfile tablespace ITXADMIN_DATA datafile '{Locate}/itxadmin_data.dbf' size 40G autoextend on next 4G maxsize 80G extent management local autoallocate;
create bigfile temporary tablespace ITXADMIN_TEMP tempfile '{Locate}/itxadmin_temp.dbf' size 4G autoextend on next 1G maxsize 10G;
--itxadminDay
create bigfile tablespace ITXADMINDAY_DATA datafile '{Locate}/itxadminday_data.dbf' size 40G autoextend on next 4G maxsize 80G extent management local autoallocate;
create bigfile temporary tablespace ITXADMINDAY_TEMP tempfile '{Locate}/itxadminday_temp.dbf' size 4G autoextend on next 1G maxsize 10G;
--itxadminMon
create bigfile tablespace ITXADMINMON_DATA datafile '{Locate}/itxadminmon_data.dbf' size 40G autoextend on next 4G maxsize 80G extent management local autoallocate;
create bigfile temporary tablespace ITXADMINMON_TEMP tempfile '{Locate}/itxadminmon_temp.dbf' size 4G autoextend on next 1G maxsize 10G;
--itxadminHist
create bigfile tablespace ITXADMINHIST_DATA datafile '{Locate}/itxadminhist_data.dbf' size 40G autoextend on next 4G maxsize 80G extent management local autoallocate;
create bigfile temporary tablespace ITXADMINHIST_TEMP tempfile '{Locate}/itxadminhist_temp.dbf' size 4G autoextend on next 1G maxsize 10G;

--創建使用者
create user webadmin identified by "1qaz2wsx" default tablespace WEBADMIN_DATA temporary tablespace WEBADMIN_TEMP;

create user itxadminas400 identified by "1qaz2wsx" default tablespace ITXADMINAS400_DATA temporary tablespace ITXADMINAS400_TEMP;
create user itxadmin identified by "1qaz2wsx" default tablespace ITXADMIN_DATA temporary tablespace ITXADMIN_TEMP;
create user itxadminDay identified by "1qaz2wsx" default tablespace ITXADMINDAY_DATA temporary tablespace ITXADMINDAY_TEMP;
create user itxadminMon identified by "1qaz2wsx" default tablespace ITXADMINMON_DATA temporary tablespace ITXADMINMON_TEMP;
create user itxadminHist identified by "1qaz2wsx" default tablespace ITXADMINHIST_DATA temporary tablespace ITXADMINHIST_TEMP;

--給予連線權限
grant create session,create table,create view,create sequence,unlimited tablespace to webadmin;

grant create session,create table,create view,create sequence,unlimited tablespace to itxadminas400;
grant create session,create table,create view,create sequence,unlimited tablespace to itxadmin;
grant create session,create table,create view,create sequence,unlimited tablespace to itxadminDay;
grant create session,create table,create view,create sequence,unlimited tablespace to itxadminMon;
grant create session,create table,create view,create sequence,unlimited tablespace to itxadminHist;


--Crate by webadmin==============================================================================================================S
--FX_CODE_LIST
CREATE TABLE webadmin."FX_CODE_LIST" (
  "ID" NUMBER(11,0) NOT NULL,
  "HELP" NVARCHAR2(30) NOT NULL,
  "SEGMENT" NVARCHAR2(30) NOT NULL,
  "XEY" NVARCHAR2(30),
  "CONTENT" NVARCHAR2(300) NOT NULL,
  "DISPLAY_ORDER" NUMBER(11,0),
  "UPDATE_DATE" DATE,
  "UPDATE_TIME" DATE
);

ALTER TABLE
  webadmin."FX_CODE_LIST"
ADD
  CONSTRAINT "CODE_LIST_PK" PRIMARY KEY ("ID");

CREATE SEQUENCE webadmin.FX_CODE_LIST_SEQ MINVALUE 1 MAXVALUE 2147483647 INCREMENT BY 1 START WITH 1 NOCACHE CYCLE;

--CREATE TRIGGER FX_CODE_LIST_INSERT BEFORE INSERT ON FX_CODE_LIST FOR EACH ROW BEGIN
--SELECT
--  FX_CODE_LIST_SEQ.NEXTVAL INTO : NEW.ID
--FROM
--  DUAL;
--END;

CREATE UNIQUE INDEX CODE_LIST_IDX ON webadmin.FX_CODE_LIST(
  "HELP" ASC, "SEGMENT" ASC, "XEY" ASC
);
CREATE INDEX CODE_LIST_IDX1 ON webadmin.FX_CODE_LIST("HELP" ASC);
--------------------------------------------------------------------------------------------------------------------------------
--FX_FILE_IMPORT
CREATE TABLE webadmin."FX_FILE_IMPORT" (
  "ORIGINAL" NVARCHAR2(15) NOT NULL,
  "IMPORT_ID" NVARCHAR2(30) NOT NULL,
  "CREATION_DATE" TIMESTAMP,
  "JOB_INSTANCE_ID" NUMBER(11,0)
);
ALTER TABLE
  webadmin."FX_FILE_IMPORT"
ADD
  CONSTRAINT "FILE_IMPORT_PK" PRIMARY KEY ("ORIGINAL", "IMPORT_ID");
--------------------------------------------------------------------------------------------------------------------------------
--FX_TRAN_DOC_BUF
CREATE TABLE webadmin."FX_TRAN_DOC_BUF" (
  "ID" NUMBER(19,0) NOT NULL,
  "DOC_ID" NUMBER(19,0) NOT NULL,
  "BUF_INDEX" NUMBER(6,0) NOT NULL,
  "BUFFER" CLOB NOT NULL
);
CREATE SEQUENCE webadmin.FX_TRAN_DOC_BUF_SEQ MINVALUE 1 MAXVALUE 9223372036854775807 INCREMENT BY 1 START WITH 1 CACHE 20 CYCLE NOORDER;

--CREATE TRIGGER FX_TRAN_DOC_BUF_INSERT BEFORE INSERT ON FX_TRAN_DOC_BUF FOR EACH ROW BEGIN
--SELECT
--  FX_TRAN_DOC_BUF_SEQ.NEXTVAL INTO : NEW.ID
--FROM
--  DUAL;
--END;

ALTER TABLE
  webadmin."FX_TRAN_DOC_BUF"
ADD
  CONSTRAINT "TRAN_DOC_BUF_PK" PRIMARY KEY ("ID");
CREATE INDEX "TRAN_DOC_BUF_IDX" ON webadmin."FX_TRAN_DOC_BUF" ("DOC_ID" ASC);
--------------------------------------------------------------------------------------------------------------------------------
--FX_LOCAL_RIM
CREATE TABLE webadmin."FX_LOCAL_RIM" (
  "ID" NUMBER(11,0) NOT NULL,
  "TABLENM" NVARCHAR2(8) NOT NULL,
  "XEY" NVARCHAR2(20) NOT NULL,
  "DATA" CLOB
);
CREATE SEQUENCE webadmin.FX_LOCAL_RIM_SEQ MINVALUE 1 MAXVALUE 2147483647 INCREMENT BY 1 START WITH 1 CACHE 20 CYCLE NOORDER;
--CREATE TRIGGER FX_LOCAL_RIM_INSERT BEFORE INSERT ON FX_LOCAL_RIM FOR EACH ROW BEGIN
--SELECT
--  FX_LOCAL_RIM_SEQ.NEXTVAL INTO : NEW.ID
--FROM
--  DUAL;
--END;
ALTER TABLE
  webadmin."FX_LOCAL_RIM"
ADD
  CONSTRAINT "LOCAL_RIM_PK" PRIMARY KEY ("ID");
CREATE UNIQUE INDEX "LOCAL_RIM_IDX" ON webadmin."FX_LOCAL_RIM" ("TABLENM" ASC, "XEY" ASC);
--------------------------------------------------------------------------------------------------------------------------------
--FX_TXCD
CREATE TABLE webadmin."FX_TXCD" (
  "TXCD" CHAR(5) NOT NULL,
  "SDAY" CHAR(8),
  "TYPE" NUMBER(6,0),
  "SBTYP" CHAR(5),
  "SECNO" NUMBER(6,0),
  "MTXCD" CHAR(5),
  "TXDNM" NVARCHAR2(120),
  "TXDSC" NVARCHAR2(240),
  "TXDFG" NUMBER(6,0),
  "HCODE" NUMBER(6,0),
  "PASS" NUMBER(6,0),
  "BRSET" NUMBER(6,0),
  "TLRFG" CHAR(10),
  "STATS" NUMBER(6,0)
);
ALTER TABLE
  webadmin."FX_TXCD"
ADD
  CONSTRAINT "TXCD_PK" PRIMARY KEY ("TXCD");
CREATE INDEX "TXCD_TYPE_IDX" ON webadmin."FX_TXCD" ("TXCD" ASC, "TYPE" ASC);
--------------------------------------------------------------------------------------------------------------------------------
--FX_TXCD_GRBRFG
CREATE TABLE webadmin."FX_TXCD_GRBRFG" (
  "TXCD" CHAR(5) NOT NULL,
  "FXLVL" NUMBER(6,0) NOT NULL,
  "BRFG" NUMBER(6,0),
  "ACBRNO" CHAR(4),
  "CHOP" NUMBER(6,0),
  "OCHOP" NUMBER(6,0)
);

ALTER TABLE webadmin."FX_TXCD_GRBRFG"
	ADD CONSTRAINT "TXCD_GRBRFG_PK" PRIMARY KEY	("TXCD", "FXLVL");
--------------------------------------------------------------------------------------------------------------------------------
--FX_TICKER
CREATE TABLE webadmin."FX_TICKER" (
  "ID" NUMBER(19,0) NOT NULL,
  "DATED" DATE,
  "TIME" DATE,
  "BRNO" CHAR(4),
  "CONTENT" NVARCHAR2(500),
  "STOP_TIME" NUMBER(19,0),
  "TICKNO" CHAR(5)
);

CREATE SEQUENCE webadmin.FX_TICKER_SEQ MINVALUE 1 MAXVALUE 9223372036854775807 INCREMENT BY 1 START WITH 1 CACHE 20 CYCLE NOORDER;

--CREATE TRIGGER FX_TICKER_INSERT BEFORE INSERT ON FX_TICKER FOR EACH ROW BEGIN
--SELECT
--  FX_TICKER_SEQ.NEXTVAL INTO : NEW.ID
--FROM
--  DUAL;
--END;

ALTER TABLE  webadmin."FX_TICKER"
ADD CONSTRAINT "TICKER_PK" PRIMARY KEY ("ID");

CREATE INDEX "TICKER_IDX" ON webadmin."FX_TICKER" ("STOP_TIME" ASC);
CREATE INDEX "TICKER_IDX1" ON webadmin."FX_TICKER" ("BRNO" ASC);
--------------------------------------------------------------------------------------------------------------------------------
CREATE TABLE webadmin."FX_SBCTL" (
  "TYPE" NUMBER(6,0) NOT NULL,
  "BRNO" CHAR(4) NOT NULL,
  "TLRNO" NVARCHAR2(20) NOT NULL,
  "SBTYP" CHAR(5) NOT NULL,
  "SDAY" CHAR(8),
  "DRELCD" NUMBER(6,0),
  "DABRNO" CHAR(4),
  "DRBRNO" CHAR(4),
  "ORELCD" NUMBER(6,0),
  "ORBRNO" CHAR(4),
  "STATS" NUMBER(6,0)
);

ALTER TABLE webadmin."FX_SBCTL"
ADD CONSTRAINT "SBCTL_PK" PRIMARY KEY ("TYPE", "BRNO", "TLRNO", "SBTYP");
--------------------------------------------------------------------------------------------------------------------------------
--FX_RQSP_CODE
CREATE TABLE webadmin."FX_RQSP_CODE" (
  "RQSP_ID" NUMBER(19,0) NOT NULL,
  "NO" NUMBER(10,0) NOT NULL,
  "CODE" CHAR(4),
  "TEXT" NVARCHAR2(500)
);

ALTER TABLE webadmin."FX_RQSP_CODE"
ADD CONSTRAINT "RQSP_CODE_PK" PRIMARY KEY ("RQSP_ID", "NO");
--------------------------------------------------------------------------------------------------------------------------------
--FX_RQSP
CREATE TABLE webadmin."FX_RQSP" (
  "ID" NUMBER(19,0) NOT NULL,
  "JNL_ID" NUMBER(19,0),
  "SEND_TIMES" NUMBER(10,0),
  "DATED" DATE,
  "TIME" DATE,
  "BRNO" CHAR(4),
  "SUPNO" NVARCHAR2(6),
  "TLRNO" NVARCHAR2(6),
  "OVRTYPE" NUMBER(6,0)
);

CREATE SEQUENCE webadmin.FX_RQSP_SEQ MINVALUE 1 MAXVALUE 9223372036854775807 INCREMENT BY 1 START WITH 1 CACHE 20 CYCLE NOORDER;

--CREATE TRIGGER FX_RQSP_INSERT BEFORE INSERT ON FX_RQSP FOR EACH ROW BEGIN
--SELECT
--  FX_RQSP_SEQ.NEXTVAL INTO : NEW.ID
--FROM
--  DUAL;
--END;

ALTER TABLE webadmin."FX_RQSP"
ADD CONSTRAINT "RQSP_PK" PRIMARY KEY ("ID");
--------------------------------------------------------------------------------------------------------------------------------
--FX_OVR_SCREEN
CREATE TABLE webadmin."FX_OVR_SCREEN" (
  "ID" NUMBER(10,0) NOT NULL ,
  "OVR_ID" NUMBER(10,0) NOT NULL,
  "INDX" NUMBER(6,0) NOT NULL,
  "BUF" CLOB
);

CREATE SEQUENCE webadmin.FX_OVR_SCREEN_SEQ MINVALUE 1 MAXVALUE 2147483647 INCREMENT BY 1 START WITH 1 CACHE 20 CYCLE NOORDER;

--CREATE TRIGGER FX_OVR_SCREEN_INSERT BEFORE INSERT ON FX_OVR_SCREEN FOR EACH ROW BEGIN
--SELECT
--  FX_OVR_SCREEN_SEQ.NEXTVAL INTO : NEW.ID
--FROM
--  DUAL;
--END;
--------------------------------------------------------------------------------------------------------------------------------
--FX_OVR
CREATE TABLE webadmin."FX_OVR" (
  "ID" NUMBER(10,0) NOT NULL,
  "BRN" NVARCHAR2(4) NOT NULL,
  "TLRNO" NVARCHAR2(6) NOT NULL,
  "DATED" DATE NOT NULL,
  "TIMET" DATE NOT NULL,
  "TXCD" CHAR(5),
  "RQSP" NVARCHAR2(4),
  "RQSP_MSG" NVARCHAR2(300),
  "SUPBRN" NVARCHAR2(4),
  "SUPNO" NVARCHAR2(6),
  "SUP_TIME" DATE,
  "STATUS" NUMBER(6,0),
  "MESSAGE" NVARCHAR2(200)
);

CREATE SEQUENCE webadmin.FX_OVR_SEQ MINVALUE 1 MAXVALUE 2147483647 INCREMENT BY 1 START WITH 1 CACHE 20 CYCLE NOORDER;

--CREATE TRIGGER FX_OVR_INSERT BEFORE INSERT ON FX_OVR FOR EACH ROW BEGIN
--SELECT
--  FX_OVR_SEQ.NEXTVAL INTO : NEW.ID
--FROM
--  DUAL;
--END;

ALTER TABLE webadmin."FX_OVR"
ADD CONSTRAINT "OVR_PK" PRIMARY KEY ("ID");
--------------------------------------------------------------------------------------------------------------------------------
--FX_MSG_CENTER
CREATE TABLE webadmin."FX_MSG_CENTER" (
  "ID" NUMBER(19,0) NOT NULL,
  "BRNO" CHAR(4),
  "TLRNO" CHAR(6),
  "RCV_DATE" DATE,
  "RCV_TIME" DATE,
  "MSGNO" CHAR(5),
  "CONTENT" NVARCHAR2(300),
  "VALID_TIME" NUMBER(19,0)
);

CREATE SEQUENCE webadmin.FX_MSG_CENTER_SEQ MINVALUE 1 MAXVALUE 9223372036854775807 INCREMENT BY 1 START WITH 1 CACHE 20 CYCLE NOORDER;

--CREATE TRIGGER FX_MSG_CENTER_INSERT BEFORE INSERT ON FX_MSG_CENTER FOR EACH ROW BEGIN
--SELECT
--  FX_MSG_CENTER_SEQ.NEXTVAL INTO : NEW.ID
--FROM
--  DUAL;
--END;

ALTER TABLE webadmin."FX_MSG_CENTER"
ADD CONSTRAINT "MSG_CENTER_PK" PRIMARY KEY ("ID");

CREATE INDEX "MSG_CENTER_IDX" ON webadmin."FX_MSG_CENTER" ("BRNO" ASC, "TLRNO" ASC, "MSGNO" ASC);
--------------------------------------------------------------------------------------------------------------------------------
--FX_MSG_BOX
CREATE TABLE webadmin."FX_MSG_BOX" (
  "ID" NUMBER(19,0) NOT NULL,
  "BRNO" CHAR(4),
  "TLRNO" CHAR(6),
  "MSGNO" CHAR(5) NOT NULL,
  "CONTENT" NVARCHAR2(500),
  "RCV_DATE" DATE,
  "RCV_TIME" DATE,
  "VALID_TIME" NUMBER(19,0),
  "VIEW_DATE" DATE,
  "VIEW_TIME" DATE,
  "DONE" CHAR(1) DEFAULT 'N'
);

CREATE SEQUENCE webadmin.FX_MSG_BOX_SEQ MINVALUE 1 MAXVALUE 9223372036854775807 INCREMENT BY 1 START WITH 1 CACHE 20 CYCLE NOORDER;

--CREATE TRIGGER FX_MSG_BOX_INSERT BEFORE INSERT ON FX_MSG_BOX FOR EACH ROW BEGIN
--SELECT
--  FX_MSG_BOX_SEQ.NEXTVAL INTO : NEW.ID
--FROM
--  DUAL;
--END;

ALTER TABLE webadmin."FX_MSG_BOX"
ADD CONSTRAINT "MSG_BOX_PK" PRIMARY KEY ("ID");

CREATE INDEX "MSG_BOX_IDX" ON webadmin."FX_MSG_BOX" ("BRNO" ASC);
CREATE INDEX "MSG_BOX_IDX1" ON webadmin."FX_MSG_BOX" ("BRNO" ASC, "TLRNO" ASC, "DONE" ASC);
--------------------------------------------------------------------------------------------------------------------------------
--FX_HELP_LIST
CREATE TABLE webadmin."FX_HELP_LIST" (
  "ID" INTEGER NOT NULL,
  "HELP" NVARCHAR2(30) NOT NULL,
  "SEGMENT" NVARCHAR2(30) NOT NULL,
  "ACTIVE_DATE" CHAR(8) NOT NULL,
  "VERSION" CHAR(1) DEFAULT '0' NOT NULL,
  "IMPORT_TIME" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  "RESIDENT" CHAR(1) DEFAULT 'Y',
  "CONTENT" CLOB,
  "JSON" CLOB
);

CREATE SEQUENCE webadmin.FX_HELP_LIST_SEQ MINVALUE 1 MAXVALUE 2147483647 INCREMENT BY 1 START WITH 1 CACHE 20 CYCLE NOORDER;

--CREATE TRIGGER FX_HELP_LIST_INSERT BEFORE INSERT ON FX_HELP_LIST FOR EACH ROW BEGIN
--SELECT
--  FX_HELP_LIST_SEQ.NEXTVAL INTO : NEW.ID
--FROM
--  DUAL;
--END;

ALTER TABLE webadmin."FX_HELP_LIST"
ADD CONSTRAINT "HELP_LIST_PK" PRIMARY KEY ("ID");

CREATE INDEX "HELP_LIST_IDX_HELP_SEGMENT_VER" ON webadmin."FX_HELP_LIST" (
  "HELP" ASC, "SEGMENT" ASC, "ACTIVE_DATE" DESC, "VERSION" DESC
);
--------------------------------------------------------------------------------------------------------------------------------
--FX_TRAN_DOC_LOG
CREATE TABLE webadmin."FX_TRAN_DOC_LOG" (
  "ID" NUMBER(19,0) NOT NULL,
  "DOC_ID" NUMBER(19,0) NOT NULL,
  "PRINT_BRN" NVARCHAR2(4),
  "PRINT_DATE" DATE NOT NULL,
  "PRINT_TIME" DATE NOT NULL,
  "PRINT_TLRNO" NVARCHAR2(6)
);

CREATE SEQUENCE webadmin.FX_TRAN_DOC_LOG_SEQ MINVALUE 1 MAXVALUE 9223372036854775807 INCREMENT BY 1 START WITH 1 CACHE 20 CYCLE NOORDER;

--CREATE TRIGGER FX_TRAN_DOC_LOG_INSERT BEFORE INSERT ON FX_TRAN_DOC_LOG FOR EACH ROW BEGIN
--SELECT
--  FX_TRAN_DOC_LOG_SEQ.NEXTVAL INTO : NEW.ID
--FROM
--  DUAL;
--END;

ALTER TABLE  webadmin."FX_TRAN_DOC_LOG"
ADD CONSTRAINT "TRAN_DOC_LOG_PK" PRIMARY KEY ("ID");

CREATE INDEX "TRAN_DOC_LOG_IDX" ON webadmin."FX_TRAN_DOC_LOG" ("DOC_ID" ASC, "PRINT_BRN" ASC);
--------------------------------------------------------------------------------------------------------------------------------
--FX_JOURNAL_TITA
CREATE TABLE webadmin."FX_JOURNAL_TITA" (
  "ID" NUMBER(19,0) NOT NULL,
  "TITA" CLOB
);

ALTER TABLE webadmin."FX_JOURNAL_TITA"
ADD CONSTRAINT "JOURNAL_TITA_PK" PRIMARY KEY ("ID");
--------------------------------------------------------------------------------------------------------------------------------
--FX_TRAN_DOC
CREATE TABLE webadmin."FX_TRAN_DOC" (
  "DOC_ID" NUMBER(19,0) NOT NULL,
  "JNL_ID" NUMBER(19,0) NOT NULL,
  "DOC_NAME" NVARCHAR2(30) NOT NULL,
  "DOC_PROMPT" NVARCHAR2(300),
  "DOC_PARAMETER" NVARCHAR2(500),
  "SRH_TXCODE" NVARCHAR2(5),
  "SRH_KINBR" NVARCHAR2(4),
  "SRH_RBRNO" NVARCHAR2(4),
  "SRH_FBRNO" NVARCHAR2(4),
  "SRH_ACBRNO" NVARCHAR2(4),
  "SRH_PBRNO" NVARCHAR2(4),
  "SRH_CIFKEY" NVARCHAR2(20),
  "SRH_MRKEY" NVARCHAR2(20),
  "SRH_CURRENCY" NVARCHAR2(3),
  "SRH_TXAMT" NVARCHAR2(20),
  "SRH_BATNO" NVARCHAR2(20),
  "SRH_BUSDATE" NVARCHAR2(8),
  "SRH_TEMP" NVARCHAR2(20)
);

CREATE SEQUENCE webadmin.FX_TRAN_DOC_SEQ MINVALUE 1 MAXVALUE 9223372036854775807 INCREMENT BY 1 START WITH 1 CACHE 20 CYCLE NOORDER;

--CREATE TRIGGER FX_TRAN_DOC_INSERT BEFORE INSERT ON FX_TRAN_DOC FOR EACH ROW BEGIN
--SELECT
--  FX_TRAN_DOC_SEQ.NEXTVAL INTO : NEW.DOC_ID
--FROM
--  DUAL;
--END;

ALTER TABLE webadmin."FX_TRAN_DOC"
ADD CONSTRAINT "TRAN_DOC_PK" PRIMARY KEY ("DOC_ID");

CREATE INDEX "TRAN_DOC_IDX" ON webadmin."FX_TRAN_DOC" (
  "JNL_ID" ASC,
  "SRH_KINBR" ASC, "SRH_RBRNO" ASC,
  "SRH_FBRNO" ASC, "SRH_ACBRNO" ASC,
  "SRH_PBRNO" ASC, "SRH_CIFKEY" ASC,
  "SRH_MRKEY" ASC, "SRH_BATNO" ASC,
  "SRH_BUSDATE" ASC, "DOC_NAME" ASC,
  "SRH_TEMP" ASC, "SRH_TXCODE" ASC
);
--------------------------------------------------------------------------------------------------------------------------------
--FX_USERPUB
CREATE TABLE webadmin."FX_USERPUB" (
  "ID" NUMBER(19,0) NOT NULL,
  "TABLENM" NVARCHAR2(10),
  "XEY" NVARCHAR2(10),
  "DATED" DATE,
  "TIME" DATE,
  "LOCATE" NVARCHAR2(50),
  "USERINFO" CLOB,
  "BRNO" NVARCHAR2(4),
  "LVEL" NVARCHAR2(1),
  "NAME" NVARCHAR2(12),
  "HTTPSESSIONID" NVARCHAR2(80),
  "SCRIPTSESSIONID" NVARCHAR2(120),
  "DAPKND" NVARCHAR2(30),
  "OAPKND" NVARCHAR2(30),
  "CLDEPT" NVARCHAR2(01),
  "PWDD" NVARCHAR2(60),
  "OVRTOKEN" NVARCHAR2(30),
  "LASTJNLSEQ" NUMBER(11,0) DEFAULT -1
);

CREATE SEQUENCE webadmin.FX_USERPUB_SEQ MINVALUE 1 MAXVALUE 9223372036854775807 INCREMENT BY 1 START WITH 1 CACHE 20 CYCLE NOORDER;

--CREATE TRIGGER FX_USERPUB_INSERT BEFORE INSERT ON FX_USERPUB FOR EACH ROW BEGIN
--SELECT
--  FX_USERPUB_SEQ.NEXTVAL INTO : NEW.ID
--FROM
--  DUAL;
--END;

ALTER TABLE webadmin."FX_USERPUB"
ADD CONSTRAINT "FX_USERPUB_PK" PRIMARY KEY ("ID");

CREATE INDEX "FX_USERPUB_IDX" ON webadmin."FX_USERPUB" ("TABLENM" ASC, "XEY" ASC);
--------------------------------------------------------------------------------------------------------------------------------
--FX_JOURNAL
CREATE TABLE webadmin."FX_JOURNAL" (
  "ID" NUMBER(19,0) NOT NULL,
  "JNLDATE" DATE,
  "JNLTIME" DATE,
  "BUSDATE" NVARCHAR2(8),
  "CALDAY" NUMBER(8,0),
  "BRN" NVARCHAR2(4),
  "RBRNO" NVARCHAR2(4),
  "FBRNO" NVARCHAR2(4),
  "ACBRNO" NVARCHAR2(4),
  "PBRNO" NVARCHAR2(4),
  "TLRNO" NVARCHAR2(6),
  "LVEL" NVARCHAR2(1),
  "TXNO" NVARCHAR2(8),
  "SUPNO" NVARCHAR2(6),
  "TXCODE" NVARCHAR2(5),
  "TRANSTATUS" NUMBER(11,0),
  "TRANFLAG" NVARCHAR2(10),
  "MSGID" NVARCHAR2(5),
  "ERRMSG" NVARCHAR2(300),
  "MRKEY" NVARCHAR2(20),
  "CURRENCY" NVARCHAR2(3),
  "TXAMT" NVARCHAR2(20),
  "CIFKEY" NVARCHAR2(20),
  "BATNO" NVARCHAR2(20),
  "TITA_RESV" CLOB,
  "OVRED" NUMBER(6,0),
  "RQSP" NVARCHAR2(4),
  "SEND_TIMES" NUMBER(11,0),
  "TEMP" NVARCHAR2(20)
);

CREATE SEQUENCE webadmin.FX_JOURNAL_SEQ MINVALUE 1 MAXVALUE 9223372036854775807 INCREMENT BY 1 START WITH 1 CACHE 20 CYCLE NOORDER;

--CREATE TRIGGER FX_JOURNAL_INSERT BEFORE INSERT ON FX_JOURNAL FOR EACH ROW BEGIN
--SELECT
--  FX_JOURNAL_SEQ.NEXTVAL INTO : NEW.ID
--FROM
--  DUAL;
--END;

ALTER TABLE webadmin."FX_JOURNAL"
ADD CONSTRAINT "FX_JOURNAL_PK" PRIMARY KEY ("ID");

CREATE INDEX "FX_JOURNAL_IDX" ON webadmin."FX_JOURNAL" (
  "BUSDATE" ASC, "BRN" ASC, "TLRNO" ASC,
  "TXNO" ASC, "TXCODE" ASC, "MRKEY" ASC,
  "CIFKEY" ASC, "RBRNO" ASC, "FBRNO" ASC,
  "ACBRNO" ASC, "PBRNO" ASC, "BATNO" ASC
);
--------------------------------------------------------------------------------------------------------------------------------
--FX_SWIFT_UNSO_MSG
CREATE TABLE webadmin."FX_SWIFT_UNSO_MSG" (
  "ID" NUMBER(19,0) NOT NULL,
  "BRNODEPT" CHAR(5) NOT NULL,
  "SRHDAY" NVARCHAR2(8) NOT NULL,
  "FILE_NAME" NVARCHAR2(60) NOT NULL,
  "RCV_DATE" DATE,
  "RCV_TIME" DATE,
  "FTBSDY" NVARCHAR2(8),
  "MSGTYP" NVARCHAR2(4) NOT NULL,
  "MSGSTATUS" NVARCHAR2(2),
  "OSN" NVARCHAR2(6),
  "ENTSEQ" NVARCHAR2(6),
  "FILE_PATH" NVARCHAR2(200) NOT NULL,
  "FILE_SIZE" NUMBER(11,0),
  "LAST_PRINT_DATE" DATE,
  "LAST_PRINT_TIME" DATE,
  "PRINT_TIMES" NUMBER(11,0)
);

CREATE SEQUENCE webadmin.FX_SWIFT_UNSO_MSG_SEQ MINVALUE 1 MAXVALUE 9223372036854775807 INCREMENT BY 1 START WITH 1 CACHE 20 CYCLE NOORDER;

--CREATE TRIGGER FX_SWIFT_UNSO_MSG_INSERT BEFORE INSERT ON FX_SWIFT_UNSO_MSG FOR EACH ROW BEGIN
--SELECT
--  FX_SWIFT_UNSO_MSG_SEQ.NEXTVAL INTO : NEW.ID
--FROM
--  DUAL;
--END;

ALTER TABLE webadmin."FX_SWIFT_UNSO_MSG"
ADD CONSTRAINT "FX_SWIFT_UNSO_MSG_PK" PRIMARY KEY (
    "ID", "BRNODEPT", "FILE_NAME", "SRHDAY"
);

CREATE INDEX "FX_SWIFT_UNSO_MSG_IDX" ON webadmin."FX_SWIFT_UNSO_MSG" (
  "BRNODEPT" ASC, "SRHDAY" ASC, "FILE_NAME" ASC
);

CREATE INDEX "FX_SWIFT_UNSO_MSG_IDX1" ON webadmin."FX_SWIFT_UNSO_MSG" (
  "BRNODEPT" ASC, "SRHDAY" ASC, "PRINT_TIMES" ASC
);
--------------------------------------------------------------------------------------------------------------------------------
--FX_SWIFT_PRINTER
CREATE TABLE webadmin."FX_SWIFT_PRINTER" (
  "BRN" CHAR(10) NOT NULL,
  "PRIMARY_PRINTER" NVARCHAR2(200),
  "ACK_PRINTER" NVARCHAR2(200),
  "NAK_PRINTER" NVARCHAR2(200),
  "ALT_PRINTER" NVARCHAR2(200),
  "ALT_MSG_LIST" CLOB,
  "PRI_ALLOWIP" NVARCHAR2(20) DEFAULT '' NOT NULL
);

CREATE UNIQUE INDEX "FX_SWIFT_PRINTER_IDX" ON webadmin."FX_SWIFT_PRINTER" ("BRN" ASC);

ALTER TABLE webadmin."FX_SWIFT_PRINTER"
ADD  CONSTRAINT "FX_SWIFT_PRINTER_PK" PRIMARY KEY ("BRN");
--------------------------------------------------------------------------------------------------------------------------------
--FTP_USER
CREATE TABLE itxadmin."FTP_USER" (      
   userid VARCHAR2(64) NOT NULL,       
   userpassword VARCHAR2(64),      
   homedirectory VARCHAR2(128) NOT NULL,             
   enableflag VARCHAR2(1) DEFAULT 'Y',    
   writepermission VARCHAR2(1) DEFAULT 'N',       
   idletime NUMBER(19,0) DEFAULT 0,             
   uploadrate NUMBER(19,0) DEFAULT 0,             
   downloadrate NUMBER(19,0) DEFAULT 0,
   maxloginnumber NUMBER(19,0) DEFAULT 0,
   maxloginperip NUMBER(19,0) DEFAULT 0
);

ALTER TABLE itxadmin."FTP_USER"
ADD  CONSTRAINT "FTP_USER_PK" PRIMARY KEY ("USERID");

insert all
into "FTP_USER" (userid, userpassword, homedirectory, enableflag, writepermission, idletime, uploadrate, downloadrate, maxloginnumber, maxloginperip)
  values ('admin', '1qaz2wsx', 'C:\SKL\iTX_Log\', 'Y','Y',3000, 48000000, 48000000, 20, 2)
into "FTP_USER" (userid, userpassword, homedirectory, enableflag, writepermission, idletime, uploadrate, downloadrate, maxloginnumber, maxloginperip)
  values ('itx', '1qaz2wsx', 'C:\SKL\iTX_Log\', 'Y','Y',3000, 48000000, 48000000, 20, 2)
select * from dual;
--------------------------------------------------------------------------------------------------------------------------------
--SpringBatch
--1、批量實例表
CREATE TABLE webadmin.FX_BATCH_JOB_INSTANCE  (
    JOB_INSTANCE_ID NUMBER(19,0)  NOT NULL PRIMARY KEY ,
    VERSION NUMBER(19,0) ,
    JOB_NAME VARCHAR2(100) NOT NULL,
    JOB_KEY VARCHAR2(32) NOT NULL,
    constraint JOB_INST_UN unique (JOB_NAME, JOB_KEY)
);

--2、批量執行表
CREATE TABLE webadmin.FX_BATCH_JOB_EXECUTION  (
    JOB_EXECUTION_ID NUMBER(19,0)  NOT NULL PRIMARY KEY ,
    VERSION NUMBER(19,0)  ,
    JOB_INSTANCE_ID NUMBER(19,0) NOT NULL,
    CREATE_TIME TIMESTAMP NOT NULL,
    START_TIME TIMESTAMP DEFAULT NULL ,
    END_TIME TIMESTAMP DEFAULT NULL ,
    STATUS VARCHAR2(10) ,
    EXIT_CODE VARCHAR2(2500) ,
    EXIT_MESSAGE VARCHAR2(2500) ,
    LAST_UPDATED TIMESTAMP,
    JOB_CONFIGURATION_LOCATION VARCHAR(2500) NULL,
    constraint JOB_INST_EXEC_FK foreign key (JOB_INSTANCE_ID)
    references webadmin.FX_BATCH_JOB_INSTANCE(JOB_INSTANCE_ID)
);

--3、批量執行參數列
CREATE TABLE webadmin.FX_BATCH_JOB_EXECUTION_PARAMS  (
    JOB_EXECUTION_ID NUMBER(19,0) NOT NULL ,
    TYPE_CD VARCHAR2(6) NOT NULL ,
    KEY_NAME VARCHAR2(100) NOT NULL ,
    STRING_VAL VARCHAR2(250) ,
    DATE_VAL TIMESTAMP DEFAULT NULL ,
    LONG_VAL NUMBER(19,0) ,
    DOUBLE_VAL NUMBER ,
    IDENTIFYING CHAR(1) NOT NULL ,
    constraint JOB_EXEC_PARAMS_FK foreign key (JOB_EXECUTION_ID)
    references webadmin.FX_BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
);

--4、批量步驟執行表
CREATE TABLE webadmin.FX_BATCH_STEP_EXECUTION  (
    STEP_EXECUTION_ID NUMBER(19,0)  NOT NULL PRIMARY KEY ,
    VERSION NUMBER(19,0) NOT NULL,
    STEP_NAME VARCHAR2(100) NOT NULL,
    JOB_EXECUTION_ID NUMBER(19,0) NOT NULL,
    START_TIME TIMESTAMP NOT NULL ,
    END_TIME TIMESTAMP DEFAULT NULL ,
    STATUS VARCHAR2(10) ,
    COMMIT_COUNT NUMBER(19,0) ,
    READ_COUNT NUMBER(19,0) ,
    FILTER_COUNT NUMBER(19,0) ,
    WRITE_COUNT NUMBER(19,0) ,
    READ_SKIP_COUNT NUMBER(19,0) ,
    WRITE_SKIP_COUNT NUMBER(19,0) ,
    PROCESS_SKIP_COUNT NUMBER(19,0) ,
    ROLLBACK_COUNT NUMBER(19,0) ,
    EXIT_CODE VARCHAR2(2500) ,
    EXIT_MESSAGE VARCHAR2(2500) ,
    LAST_UPDATED TIMESTAMP,
    constraint JOB_EXEC_STEP_FK foreign key (JOB_EXECUTION_ID)
    references webadmin.FX_BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
);

--5、批量步驟執行內容表
CREATE TABLE webadmin.FX_BATCH_STEP_EXECUTION_CONTEXT  (
    STEP_EXECUTION_ID NUMBER(19,0) NOT NULL PRIMARY KEY,
    SHORT_CONTEXT VARCHAR2(2500) NOT NULL,
    SERIALIZED_CONTEXT CLOB ,
    constraint STEP_EXEC_CTX_FK foreign key (STEP_EXECUTION_ID)
    references webadmin.FX_BATCH_STEP_EXECUTION(STEP_EXECUTION_ID)
);

--6、批量任務執行上下文；
CREATE TABLE webadmin.FX_BATCH_JOB_EXECUTION_CONTEXT  (
    JOB_EXECUTION_ID NUMBER(19,0) NOT NULL PRIMARY KEY,
    SHORT_CONTEXT VARCHAR2(2500) NOT NULL,
    SERIALIZED_CONTEXT CLOB ,
    constraint JOB_EXEC_CTX_FK foreign key (JOB_EXECUTION_ID)
    references webadmin.FX_BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
);

--1、批量任務序列號
create sequence webadmin.FX_BATCH_JOB_SEQ
minvalue 0
maxvalue 9999999999999999999
start with 0
increment by 1
cache 20 CYCLE NOORDER;

--2、批量任務執行序列號
create sequence webadmin.FX_BATCH_JOB_EXECUTION_SEQ
minvalue 0
maxvalue 9999999999999999999
start with 0
increment by 1
cache 20 CYCLE NOORDER;


--3、批量步驟執行序列號
create sequence webadmin.FX_BATCH_STEP_EXECUTION_SEQ
minvalue 0
maxvalue 9999999999999999999
start with 0
increment by 1
cache 20 CYCLE NOORDER;
--Crate by webadmin==============================================================================================================End



--Crate by itxadmin & itxadminDay & itxadminMon & itxadminHist===================================================================S
---------------------------------------------------------------------------------------------------------------------------------
--SpringBatch
--1、批量實例表
CREATE TABLE itxadmin.FX_BATCH_JOB_INSTANCE  (
    JOB_INSTANCE_ID NUMBER(19,0)  NOT NULL PRIMARY KEY ,
    VERSION NUMBER(19,0) ,
    JOB_NAME VARCHAR2(100) NOT NULL,
    JOB_KEY VARCHAR2(32) NOT NULL,
    constraint JOB_INST_UN unique (JOB_NAME, JOB_KEY)
);

--2、批量執行表
CREATE TABLE itxadmin.FX_BATCH_JOB_EXECUTION  (
    JOB_EXECUTION_ID NUMBER(19,0)  NOT NULL PRIMARY KEY ,
    VERSION NUMBER(19,0)  ,
    JOB_INSTANCE_ID NUMBER(19,0) NOT NULL,
    CREATE_TIME TIMESTAMP NOT NULL,
    START_TIME TIMESTAMP DEFAULT NULL ,
    END_TIME TIMESTAMP DEFAULT NULL ,
    STATUS VARCHAR2(10) ,
    EXIT_CODE VARCHAR2(2500) ,
    EXIT_MESSAGE VARCHAR2(2500) ,
    LAST_UPDATED TIMESTAMP,
    JOB_CONFIGURATION_LOCATION VARCHAR(2500) NULL,
    constraint JOB_INST_EXEC_FK foreign key (JOB_INSTANCE_ID)
    references itxadmin.FX_BATCH_JOB_INSTANCE(JOB_INSTANCE_ID)
);

--3、批量執行參數列
CREATE TABLE itxadmin.FX_BATCH_JOB_EXECUTION_PARAMS  (
    JOB_EXECUTION_ID NUMBER(19,0) NOT NULL ,
    TYPE_CD VARCHAR2(6) NOT NULL ,
    KEY_NAME VARCHAR2(100) NOT NULL ,
    STRING_VAL VARCHAR2(250) ,
    DATE_VAL TIMESTAMP DEFAULT NULL ,
    LONG_VAL NUMBER(19,0) ,
    DOUBLE_VAL NUMBER ,
    IDENTIFYING CHAR(1) NOT NULL ,
    constraint JOB_EXEC_PARAMS_FK foreign key (JOB_EXECUTION_ID)
    references itxadmin.FX_BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
);

--4、批量步驟執行表
CREATE TABLE itxadmin.FX_BATCH_STEP_EXECUTION  (
    STEP_EXECUTION_ID NUMBER(19,0)  NOT NULL PRIMARY KEY ,
    VERSION NUMBER(19,0) NOT NULL,
    STEP_NAME VARCHAR2(100) NOT NULL,
    JOB_EXECUTION_ID NUMBER(19,0) NOT NULL,
    START_TIME TIMESTAMP NOT NULL ,
    END_TIME TIMESTAMP DEFAULT NULL ,
    STATUS VARCHAR2(10) ,
    COMMIT_COUNT NUMBER(19,0) ,
    READ_COUNT NUMBER(19,0) ,
    FILTER_COUNT NUMBER(19,0) ,
    WRITE_COUNT NUMBER(19,0) ,
    READ_SKIP_COUNT NUMBER(19,0) ,
    WRITE_SKIP_COUNT NUMBER(19,0) ,
    PROCESS_SKIP_COUNT NUMBER(19,0) ,
    ROLLBACK_COUNT NUMBER(19,0) ,
    EXIT_CODE VARCHAR2(2500) ,
    EXIT_MESSAGE VARCHAR2(2500) ,
    LAST_UPDATED TIMESTAMP,
    constraint JOB_EXEC_STEP_FK foreign key (JOB_EXECUTION_ID)
    references itxadmin.FX_BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
);

--5、批量步驟執行內容表
CREATE TABLE itxadmin.FX_BATCH_STEP_EXECUTION_CONTEXT  (
    STEP_EXECUTION_ID NUMBER(19,0) NOT NULL PRIMARY KEY,
    SHORT_CONTEXT VARCHAR2(2500) NOT NULL,
    SERIALIZED_CONTEXT CLOB ,
    constraint STEP_EXEC_CTX_FK foreign key (STEP_EXECUTION_ID)
    references itxadmin.FX_BATCH_STEP_EXECUTION(STEP_EXECUTION_ID)
);

--6、批量任務執行上下文；
CREATE TABLE itxadmin.FX_BATCH_JOB_EXECUTION_CONTEXT  (
    JOB_EXECUTION_ID NUMBER(19,0) NOT NULL PRIMARY KEY,
    SHORT_CONTEXT VARCHAR2(2500) NOT NULL,
    SERIALIZED_CONTEXT CLOB ,
    constraint JOB_EXEC_CTX_FK foreign key (JOB_EXECUTION_ID)
    references itxadmin.FX_BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
);

--1、批量任務序列號
create sequence itxadmin.FX_BATCH_JOB_SEQ
minvalue 0
maxvalue 9999999999999999999
start with 0
increment by 1
cache 20 CYCLE NOORDER;

--2、批量任務執行序列號
create sequence itxadmin.FX_BATCH_JOB_EXECUTION_SEQ
minvalue 0
maxvalue 9999999999999999999
start with 0
increment by 1
cache 20 CYCLE NOORDER;


--3、批量步驟執行序列號
create sequence itxadmin.FX_BATCH_STEP_EXECUTION_SEQ
minvalue 0
maxvalue 9999999999999999999
start with 0
increment by 1
cache 20 CYCLE NOORDER;
---------------------------------------------------------------------------------------------------------------------------------