--------------------------------------------------------
--  已建立檔案 - 星期三-十一月-03-2021   
--------------------------------------------------------
DROP TABLE "TxAuthGroup" cascade constraints;
--------------------------------------------------------
--  DDL for Table TxAuthGroup
--------------------------------------------------------

  CREATE TABLE "TxAuthGroup" 
   (	"AuthNo" VARCHAR2(6), 
	"AuthItem" VARCHAR2(20), 
	"Desc" VARCHAR2(60), 
	"Status" NUMBER(1,0) DEFAULT 0, 
	"CreateDate" DATE, 
	"CreateEmpNo" VARCHAR2(6), 
	"LastUpdate" DATE, 
	"LastUpdateEmpNo" VARCHAR2(6), 
	"BranchNo" VARCHAR2(4), 
	"LevelFg" NUMBER(1,0) DEFAULT 0
   ) ;

   COMMENT ON COLUMN "TxAuthGroup"."AuthNo" IS '權限群組編號';
   COMMENT ON COLUMN "TxAuthGroup"."AuthItem" IS '權限群組名稱';
   COMMENT ON COLUMN "TxAuthGroup"."Desc" IS '權限群組說明';
   COMMENT ON COLUMN "TxAuthGroup"."Status" IS '狀態';
   COMMENT ON COLUMN "TxAuthGroup"."CreateDate" IS '建檔日期時間';
   COMMENT ON COLUMN "TxAuthGroup"."CreateEmpNo" IS '建檔人員';
   COMMENT ON COLUMN "TxAuthGroup"."LastUpdate" IS '最後更新日期時間';
   COMMENT ON COLUMN "TxAuthGroup"."LastUpdateEmpNo" IS '最後更新人員';
   COMMENT ON COLUMN "TxAuthGroup"."BranchNo" IS '使用單位別';
   COMMENT ON COLUMN "TxAuthGroup"."LevelFg" IS '櫃員等級';
   COMMENT ON TABLE "TxAuthGroup"  IS '權限群組檔';
REM INSERTING into "TxAuthGroup"
SET DEFINE OFF;
Insert into "TxAuthGroup" ("AuthNo","AuthItem","Desc","Status","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo","BranchNo","LevelFg") values ('0000T1','放款部經辦','限放款部經辦',0,null,null,to_date('2021-10-25 17:18:48','YYYY-MM-DD HH24:MI:SS'),'001703','0000',3);
Insert into "TxAuthGroup" ("AuthNo","AuthItem","Desc","Status","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo","BranchNo","LevelFg") values ('0000S1','放款部主管','限放款部主管',0,null,null,to_date('2021-11-01 15:55:53','YYYY-MM-DD HH24:MI:SS'),'001709','0000',1);
Insert into "TxAuthGroup" ("AuthNo","AuthItem","Desc","Status","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo","BranchNo","LevelFg") values ('0025S1','信託部主管','限信託部主管',0,null,null,to_date('2021-11-01 15:54:57','YYYY-MM-DD HH24:MI:SS'),'001709','0025',1);
Insert into "TxAuthGroup" ("AuthNo","AuthItem","Desc","Status","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo","BranchNo","LevelFg") values ('0025T1','信託部經辦','限信託部經辦',0,null,null,to_date('2021-02-17 12:55:25','YYYY-MM-DD HH24:MI:SS'),'001709','0025',3);
Insert into "TxAuthGroup" ("AuthNo","AuthItem","Desc","Status","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo","BranchNo","LevelFg") values ('0000NE','放款部債協',null,0,to_date('2021-02-09 14:18:46','YYYY-MM-DD HH24:MI:SS'),'001709',to_date('2021-10-20 17:26:25','YYYY-MM-DD HH24:MI:SS'),'001715','0000',3);
Insert into "TxAuthGroup" ("AuthNo","AuthItem","Desc","Status","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo","BranchNo","LevelFg") values ('0000T2','放款審查科','放款審查科',0,to_date('2021-03-12 18:49:21','YYYY-MM-DD HH24:MI:SS'),'001709',to_date('2021-03-12 18:49:21','YYYY-MM-DD HH24:MI:SS'),null,'0000',3);
Insert into "TxAuthGroup" ("AuthNo","AuthItem","Desc","Status","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo","BranchNo","LevelFg") values ('0000T3','放款服務課','放款服務課',0,to_date('2021-03-12 18:55:02','YYYY-MM-DD HH24:MI:SS'),'001709',to_date('2021-09-01 14:55:58','YYYY-MM-DD HH24:MI:SS'),'001709','0000',3);
--------------------------------------------------------
--  DDL for Index TxAuthGroup_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "TxAuthGroup_PK" ON "TxAuthGroup" ("AuthNo") 
  ;
--------------------------------------------------------
--  Constraints for Table TxAuthGroup
--------------------------------------------------------

  ALTER TABLE "TxAuthGroup" ADD CONSTRAINT "TxAuthGroup_PK" PRIMARY KEY ("AuthNo")
  USING INDEX  ENABLE;
  ALTER TABLE "TxAuthGroup" MODIFY ("Status" NOT NULL ENABLE);
