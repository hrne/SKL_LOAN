drop table "CdConvertCode" purge;

create table "CdConvertCode" (
  "CodeType" varchar2(20),
  "orgCode" varchar2(20),
  "orgItem" nvarchar2(50),
  "newCode" varchar2(20),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "CdConvertCode" add constraint "CdConvertCode_PK" primary key("CodeType", "orgCode");

comment on table "CdConvertCode" is '代碼轉換檔';
comment on column "CdConvertCode"."CodeType" is '代碼轉換類別';
comment on column "CdConvertCode"."orgCode" is '原始代碼';
comment on column "CdConvertCode"."orgItem" is '原始代碼說明';
comment on column "CdConvertCode"."newCode" is '新貸中代碼';
comment on column "CdConvertCode"."CreateDate" is '建檔日期時間';
comment on column "CdConvertCode"."CreateEmpNo" is '建檔人員';
comment on column "CdConvertCode"."LastUpdate" is '最後更新日期時間';
comment on column "CdConvertCode"."LastUpdateEmpNo" is '最後更新人員';

INSERT INTO "CdConvertCode" ("CodeType", "orgCode", "orgItem", "newCode","CreateDate", "CreateEmpNo", "LastUpdate", "LastUpdateEmpNo") 
VALUES ('CustTypeCode', '@', '固特利契轉', '00', systimestamp,'001709', systimestamp,'001709');
INSERT INTO "CdConvertCode" ("CodeType", "orgCode", "orgItem", "newCode","CreateDate", "CreateEmpNo", "LastUpdate", "LastUpdateEmpNo") 
VALUES ('CustTypeCode', '0', '一般', '00', systimestamp,'001709', systimestamp,'001709');
INSERT INTO "CdConvertCode" ("CodeType", "orgCode", "orgItem", "newCode","CreateDate", "CreateEmpNo", "LastUpdate", "LastUpdateEmpNo") 
VALUES ('CustTypeCode', '1', '員工', '01', systimestamp,'001709', systimestamp,'001709');
INSERT INTO "CdConvertCode" ("CodeType", "orgCode", "orgItem", "newCode","CreateDate", "CreateEmpNo", "LastUpdate", "LastUpdateEmpNo") 
VALUES ('CustTypeCode', '2', '首購', '02', systimestamp,'001709', systimestamp,'001709');
INSERT INTO "CdConvertCode" ("CodeType", "orgCode", "orgItem", "newCode","CreateDate", "CreateEmpNo", "LastUpdate", "LastUpdateEmpNo") 
VALUES ('CustTypeCode', '3', '關企公司', '03', systimestamp,'001709', systimestamp,'001709');
INSERT INTO "CdConvertCode" ("CodeType", "orgCode", "orgItem", "newCode","CreateDate", "CreateEmpNo", "LastUpdate", "LastUpdateEmpNo") 
VALUES ('CustTypeCode', '4', '關企員工', '04', systimestamp,'001709', systimestamp,'001709');
INSERT INTO "CdConvertCode" ("CodeType", "orgCode", "orgItem", "newCode","CreateDate", "CreateEmpNo", "LastUpdate", "LastUpdateEmpNo") 
VALUES ('CustTypeCode', '5', '保戶', '05', systimestamp,'001709', systimestamp,'001709');
INSERT INTO "CdConvertCode" ("CodeType", "orgCode", "orgItem", "newCode","CreateDate", "CreateEmpNo", "LastUpdate", "LastUpdateEmpNo") 
VALUES ('CustTypeCode', '6', '團體戶', '06', systimestamp,'001709', systimestamp,'001709');
INSERT INTO "CdConvertCode" ("CodeType", "orgCode", "orgItem", "newCode","CreateDate", "CreateEmpNo", "LastUpdate", "LastUpdateEmpNo") 
VALUES ('CustTypeCode', '7', '二等親屬', '07', systimestamp,'001709', systimestamp,'001709');
INSERT INTO "CdConvertCode" ("CodeType", "orgCode", "orgItem", "newCode","CreateDate", "CreateEmpNo", "LastUpdate", "LastUpdateEmpNo") 
VALUES ('CustTypeCode', '8', '受災戶', '00', systimestamp,'001709', systimestamp,'001709');
INSERT INTO "CdConvertCode" ("CodeType", "orgCode", "orgItem", "newCode","CreateDate", "CreateEmpNo", "LastUpdate", "LastUpdateEmpNo") 
VALUES ('CustTypeCode', '9', '新二階員工', '09', systimestamp,'001709', systimestamp,'001709');
INSERT INTO "CdConvertCode" ("CodeType", "orgCode", "orgItem", "newCode","CreateDate", "CreateEmpNo", "LastUpdate", "LastUpdateEmpNo") 
VALUES ('CustTypeCode', 'A', '信義房屋', '00', systimestamp,'001709', systimestamp,'001709');
INSERT INTO "CdConvertCode" ("CodeType", "orgCode", "orgItem", "newCode","CreateDate", "CreateEmpNo", "LastUpdate", "LastUpdateEmpNo") 
VALUES ('CustTypeCode', 'B', '千禧房貸', '00', systimestamp,'001709', systimestamp,'001709');
INSERT INTO "CdConvertCode" ("CodeType", "orgCode", "orgItem", "newCode","CreateDate", "CreateEmpNo", "LastUpdate", "LastUpdateEmpNo") 
VALUES ('CustTypeCode', 'C', '青年優惠', '00', systimestamp,'001709', systimestamp,'001709');
INSERT INTO "CdConvertCode" ("CodeType", "orgCode", "orgItem", "newCode","CreateDate", "CreateEmpNo", "LastUpdate", "LastUpdateEmpNo") 
VALUES ('CustTypeCode', 'D', '2000億優惠', '00', systimestamp,'001709', systimestamp,'001709');
INSERT INTO "CdConvertCode" ("CodeType", "orgCode", "orgItem", "newCode","CreateDate", "CreateEmpNo", "LastUpdate", "LastUpdateEmpNo") 
VALUES ('CustTypeCode', 'E', '退休員工', '00', systimestamp,'001709', systimestamp,'001709');
INSERT INTO "CdConvertCode" ("CodeType", "orgCode", "orgItem", "newCode","CreateDate", "CreateEmpNo", "LastUpdate", "LastUpdateEmpNo") 
VALUES ('CustTypeCode', 'F', '菁英專案', '00', systimestamp,'001709', systimestamp,'001709');
INSERT INTO "CdConvertCode" ("CodeType", "orgCode", "orgItem", "newCode","CreateDate", "CreateEmpNo", "LastUpdate", "LastUpdateEmpNo") 
VALUES ('CustTypeCode', 'G', '東方帝國', '00', systimestamp,'001709', systimestamp,'001709');
INSERT INTO "CdConvertCode" ("CodeType", "orgCode", "orgItem", "newCode","CreateDate", "CreateEmpNo", "LastUpdate", "LastUpdateEmpNo") 
VALUES ('CustTypeCode', 'H', '2000億優惠', '00', systimestamp,'001709', systimestamp,'001709');
INSERT INTO "CdConvertCode" ("CodeType", "orgCode", "orgItem", "newCode","CreateDate", "CreateEmpNo", "LastUpdate", "LastUpdateEmpNo") 
VALUES ('CustTypeCode', 'I', '自然人3戶', '00', systimestamp,'001709', systimestamp,'001709');
INSERT INTO "CdConvertCode" ("CodeType", "orgCode", "orgItem", "newCode","CreateDate", "CreateEmpNo", "LastUpdate", "LastUpdateEmpNo") 
VALUES ('CustTypeCode', 'J', '花木釀宅', '00', systimestamp,'001709', systimestamp,'001709');
INSERT INTO "CdConvertCode" ("CodeType", "orgCode", "orgItem", "newCode","CreateDate", "CreateEmpNo", "LastUpdate", "LastUpdateEmpNo") 
VALUES ('CustTypeCode', 'K', '央行 990624', '00', systimestamp,'001709', systimestamp,'001709');
INSERT INTO "CdConvertCode" ("CodeType", "orgCode", "orgItem", "newCode","CreateDate", "CreateEmpNo", "LastUpdate", "LastUpdateEmpNo") 
VALUES ('CustTypeCode', 'L', '增貸管制件', '00', systimestamp,'001709', systimestamp,'001709');
INSERT INTO "CdConvertCode" ("CodeType", "orgCode", "orgItem", "newCode","CreateDate", "CreateEmpNo", "LastUpdate", "LastUpdateEmpNo") 
VALUES ('CustTypeCode', 'M', '整合貸', '00', systimestamp,'001709', systimestamp,'001709');
INSERT INTO "CdConvertCode" ("CodeType", "orgCode", "orgItem", "newCode","CreateDate", "CreateEmpNo", "LastUpdate", "LastUpdateEmpNo") 
VALUES ('CustTypeCode', 'N', '8000億優惠', '00', systimestamp,'001709', systimestamp,'001709');
INSERT INTO "CdConvertCode" ("CodeType", "orgCode", "orgItem", "newCode","CreateDate", "CreateEmpNo", "LastUpdate", "LastUpdateEmpNo") 
VALUES ('CustTypeCode', 'O', '央行 991231', '00', systimestamp,'001709', systimestamp,'001709');
INSERT INTO "CdConvertCode" ("CodeType", "orgCode", "orgItem", "newCode","CreateDate", "CreateEmpNo", "LastUpdate", "LastUpdateEmpNo") 
VALUES ('CustTypeCode', 'P', '優惠轉貸', '00', systimestamp,'001709', systimestamp,'001709');
INSERT INTO "CdConvertCode" ("CodeType", "orgCode", "orgItem", "newCode","CreateDate", "CreateEmpNo", "LastUpdate", "LastUpdateEmpNo") 
VALUES ('CustTypeCode', 'Q', '永慶房屋', '00', systimestamp,'001709', systimestamp,'001709');
INSERT INTO "CdConvertCode" ("CodeType", "orgCode", "orgItem", "newCode","CreateDate", "CreateEmpNo", "LastUpdate", "LastUpdateEmpNo") 
VALUES ('CustTypeCode', 'R', '優惠重購', '00', systimestamp,'001709', systimestamp,'001709');
INSERT INTO "CdConvertCode" ("CodeType", "orgCode", "orgItem", "newCode","CreateDate", "CreateEmpNo", "LastUpdate", "LastUpdateEmpNo") 
VALUES ('CustTypeCode', 'S', '88風災', '00', systimestamp,'001709', systimestamp,'001709');
INSERT INTO "CdConvertCode" ("CodeType", "orgCode", "orgItem", "newCode","CreateDate", "CreateEmpNo", "LastUpdate", "LastUpdateEmpNo") 
VALUES ('CustTypeCode', 'T', 'VIP減帳管', '00', systimestamp,'001709', systimestamp,'001709');
INSERT INTO "CdConvertCode" ("CodeType", "orgCode", "orgItem", "newCode","CreateDate", "CreateEmpNo", "LastUpdate", "LastUpdateEmpNo") 
VALUES ('CustTypeCode', 'U', '整合貸減帳', '00', systimestamp,'001709', systimestamp,'001709');
INSERT INTO "CdConvertCode" ("CodeType", "orgCode", "orgItem", "newCode","CreateDate", "CreateEmpNo", "LastUpdate", "LastUpdateEmpNo") 
VALUES ('CustTypeCode', 'V', '991231減帳', '00', systimestamp,'001709', systimestamp,'001709');
INSERT INTO "CdConvertCode" ("CodeType", "orgCode", "orgItem", "newCode","CreateDate", "CreateEmpNo", "LastUpdate", "LastUpdateEmpNo") 
VALUES ('CustTypeCode', 'W', '久福專案', '00', systimestamp,'001709', systimestamp,'001709');
INSERT INTO "CdConvertCode" ("CodeType", "orgCode", "orgItem", "newCode","CreateDate", "CreateEmpNo", "LastUpdate", "LastUpdateEmpNo") 
VALUES ('CustTypeCode', 'X', '高價住宅', '00', systimestamp,'001709', systimestamp,'001709');
INSERT INTO "CdConvertCode" ("CodeType", "orgCode", "orgItem", "newCode","CreateDate", "CreateEmpNo", "LastUpdate", "LastUpdateEmpNo") 
VALUES ('CustTypeCode', 'Y', '法人購置宅', '00', systimestamp,'001709', systimestamp,'001709');