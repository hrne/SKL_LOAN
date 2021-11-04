drop table "AchAuthLogHistory" purge;

drop sequence "AchAuthLogHistory_SEQ";

create table "AchAuthLogHistory" (
  "LogNo" decimal(11,0) not null,
  "CustNo" decimal(7, 0) default 0 not null,
  "FacmNo" decimal(3, 0) default 0 not null,
  "AuthCreateDate" decimal(8, 0) default 0 not null,
  "RepayBank" varchar2(3),
  "RepayAcct" varchar2(14),
  "CreateFlag" varchar2(1),
  "ProcessDate" decimal(8, 0) default 0 not null,
  "StampFinishDate" decimal(8, 0) default 0 not null,
  "AuthStatus" varchar2(1),
  "AuthMeth" varchar2(1),
  "LimitAmt" decimal(8, 2) default 0 not null,
  "MediaCode" varchar2(1),
  "BatchNo" varchar2(6),
  "PropDate" decimal(8, 0) default 0 not null,
  "RetrDate" decimal(8, 0) default 0 not null,
  "DeleteDate" decimal(8, 0) default 0 not null,
  "RelationCode" varchar2(2),
  "RelAcctName" nvarchar2(100),
  "RelationId" varchar2(10),
  "RelAcctBirthday" decimal(8, 0) default 0 not null,
  "RelAcctGender" varchar2(1),
  "AmlRsp" varchar2(1),
  "CreateEmpNo" varchar2(6),
  "CreateDate" timestamp,
  "LastUpdateEmpNo" varchar2(6),
  "LastUpdate" timestamp
);

alter table "AchAuthLogHistory" add constraint "AchAuthLogHistory_PK" primary key("LogNo");

create sequence "AchAuthLogHistory_SEQ" minvalue 1 maxvalue 2147483647 increment by 1 start with 1 nocache cycle;

comment on table "AchAuthLogHistory" is 'ACH授權記錄歷史檔';
comment on column "AchAuthLogHistory"."LogNo" is '序號';
comment on column "AchAuthLogHistory"."CustNo" is '戶號';
comment on column "AchAuthLogHistory"."FacmNo" is '額度號碼';
comment on column "AchAuthLogHistory"."AuthCreateDate" is '建檔日期';
comment on column "AchAuthLogHistory"."RepayBank" is '扣款銀行';
comment on column "AchAuthLogHistory"."RepayAcct" is '扣款帳號';
comment on column "AchAuthLogHistory"."CreateFlag" is '新增或取消記號';
comment on column "AchAuthLogHistory"."ProcessDate" is '處理日期';
comment on column "AchAuthLogHistory"."StampFinishDate" is '核印完成日期時間';
comment on column "AchAuthLogHistory"."AuthStatus" is '授權狀態';
comment on column "AchAuthLogHistory"."AuthMeth" is '授權方式';
comment on column "AchAuthLogHistory"."LimitAmt" is '每筆扣款限額';
comment on column "AchAuthLogHistory"."MediaCode" is '媒體碼';
comment on column "AchAuthLogHistory"."BatchNo" is '批號';
comment on column "AchAuthLogHistory"."PropDate" is '提出日期';
comment on column "AchAuthLogHistory"."RetrDate" is '提回日期';
comment on column "AchAuthLogHistory"."DeleteDate" is '刪除日期/暫停授權日期';
comment on column "AchAuthLogHistory"."RelationCode" is '與借款人關係';
comment on column "AchAuthLogHistory"."RelAcctName" is '第三人帳戶戶名';
comment on column "AchAuthLogHistory"."RelationId" is '第三人身分證字號';
comment on column "AchAuthLogHistory"."RelAcctBirthday" is '第三人出生日期';
comment on column "AchAuthLogHistory"."RelAcctGender" is '第三人性別';
comment on column "AchAuthLogHistory"."AmlRsp" is 'AML回應碼';
comment on column "AchAuthLogHistory"."CreateEmpNo" is '建立者櫃員編號';
comment on column "AchAuthLogHistory"."CreateDate" is '建立日期時間';
comment on column "AchAuthLogHistory"."LastUpdateEmpNo" is '修改者櫃員編號';
comment on column "AchAuthLogHistory"."LastUpdate" is '修改日期時間';
