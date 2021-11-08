drop table "InsuComm" purge;

create table "InsuComm" (
  "InsuYearMonth" decimal(6, 0) default 0 not null,
  "InsuCommSeq" decimal(6, 0) default 0 not null,
  "ManagerCode" varchar2(3),
  "NowInsuNo" varchar2(20),
  "BatchNo" varchar2(20),
  "InsuType" decimal(2, 0) default 0 not null,
  "InsuSignDate" decimal(8, 0) default 0 not null,
  "InsuredName" nvarchar2(60),
  "InsuredAddr" nvarchar2(60),
  "InsuredTeleph" varchar2(20),
  "InsuStartDate" decimal(8, 0) default 0 not null,
  "InsuEndDate" decimal(8, 0) default 0 not null,
  "InsuCate" decimal(2, 0) default 0 not null,
  "InsuPrem" decimal(14, 0) default 0 not null,
  "CommRate" decimal(5, 3) default 0 not null,
  "Commision" decimal(14, 0) default 0 not null,
  "TotInsuPrem" decimal(14, 0) default 0 not null,
  "TotComm" decimal(14, 0) default 0 not null,
  "RecvSeq" varchar2(14),
  "ChargeDate" decimal(8, 0) default 0 not null,
  "CommDate" decimal(8, 0) default 0 not null,
  "CustNo" decimal(7, 0) default 0 not null,
  "FacmNo" decimal(3, 0) default 0 not null,
  "FireOfficer" varchar2(6),
  "EmpId" varchar2(10),
  "EmpName" nvarchar2(20),
  "DueAmt" decimal(14, 0) default 0 not null,
  "MediaCode" varchar2(1),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "InsuComm" add constraint "InsuComm_PK" primary key("InsuYearMonth", "InsuCommSeq");

comment on table "InsuComm" is '火險佣金檔';
comment on column "InsuComm"."InsuYearMonth" is '年月份';
comment on column "InsuComm"."InsuCommSeq" is '佣金媒體檔序號';
comment on column "InsuComm"."ManagerCode" is '經紀人代號';
comment on column "InsuComm"."NowInsuNo" is '保單號碼';
comment on column "InsuComm"."BatchNo" is '批號';
comment on column "InsuComm"."InsuType" is '險別';
comment on column "InsuComm"."InsuSignDate" is '簽單日期';
comment on column "InsuComm"."InsuredName" is '被保險人';
comment on column "InsuComm"."InsuredAddr" is '被保險人地址';
comment on column "InsuComm"."InsuredTeleph" is '被保險人電話';
comment on column "InsuComm"."InsuStartDate" is '起保日期';
comment on column "InsuComm"."InsuEndDate" is '到期日期';
comment on column "InsuComm"."InsuCate" is '險種';
comment on column "InsuComm"."InsuPrem" is '保費';
comment on column "InsuComm"."CommRate" is '佣金率';
comment on column "InsuComm"."Commision" is '佣金';
comment on column "InsuComm"."TotInsuPrem" is '合計保費';
comment on column "InsuComm"."TotComm" is '合計佣金';
comment on column "InsuComm"."RecvSeq" is '收件號碼';
comment on column "InsuComm"."ChargeDate" is '收費日期';
comment on column "InsuComm"."CommDate" is '佣金日期';
comment on column "InsuComm"."CustNo" is '戶號';
comment on column "InsuComm"."FacmNo" is '額度';
comment on column "InsuComm"."FireOfficer" is '火險服務';
comment on column "InsuComm"."EmpId" is '統一編號';
comment on column "InsuComm"."EmpName" is '員工姓名';
comment on column "InsuComm"."DueAmt" is '應領金額';
comment on column "InsuComm"."MediaCode" is '媒體碼';
comment on column "InsuComm"."CreateDate" is '建檔日期時間';
comment on column "InsuComm"."CreateEmpNo" is '建檔人員';
comment on column "InsuComm"."LastUpdate" is '最後更新日期時間';
comment on column "InsuComm"."LastUpdateEmpNo" is '最後更新人員';
