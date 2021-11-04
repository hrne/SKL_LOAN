drop table "PostAuthLogHistory" purge;

drop sequence "PostAuthLogHistory_SEQ";

create table "PostAuthLogHistory" (
  "LogNo" decimal(11,0) not null,
  "CustNo" decimal(7, 0) default 0 not null,
  "FacmNo" decimal(3, 0) default 0 not null,
  "AuthCode" varchar2(1),
  "AuthCreateDate" decimal(8, 0) default 0 not null,
  "AuthApplCode" varchar2(1),
  "PostDepCode" varchar2(1),
  "RepayAcct" varchar2(14),
  "CustId" varchar2(10),
  "RepayAcctSeq" varchar2(2),
  "ProcessDate" decimal(8, 0) default 0 not null,
  "StampFinishDate" decimal(8, 0) default 0 not null,
  "StampCancelDate" decimal(8, 0) default 0 not null,
  "StampCode" varchar2(1),
  "PostMediaCode" varchar2(1),
  "AuthErrorCode" varchar2(2),
  "FileSeq" decimal(6, 0) default 0 not null,
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

alter table "PostAuthLogHistory" add constraint "PostAuthLogHistory_PK" primary key("LogNo");

create sequence "PostAuthLogHistory_SEQ" minvalue 1 maxvalue 2147483647 increment by 1 start with 1 nocache cycle;

create index "PostAuthLogHistory_Index1" on "PostAuthLogHistory"("AuthCreateDate" asc);

create index "PostAuthLogHistory_Index2" on "PostAuthLogHistory"("PostMediaCode" asc, "AuthErrorCode" asc);

create index "PostAuthLogHistory_Index3" on "PostAuthLogHistory"("PropDate" asc);

create index "PostAuthLogHistory_Index4" on "PostAuthLogHistory"("RetrDate" asc);

comment on table "PostAuthLogHistory" is '郵局授權記錄歷史檔';
comment on column "PostAuthLogHistory"."LogNo" is '序號';
comment on column "PostAuthLogHistory"."CustNo" is '戶號';
comment on column "PostAuthLogHistory"."FacmNo" is '額度';
comment on column "PostAuthLogHistory"."AuthCode" is '授權方式';
comment on column "PostAuthLogHistory"."AuthCreateDate" is '建檔日期';
comment on column "PostAuthLogHistory"."AuthApplCode" is '申請代號，狀態碼';
comment on column "PostAuthLogHistory"."PostDepCode" is '帳戶別';
comment on column "PostAuthLogHistory"."RepayAcct" is '儲金帳號';
comment on column "PostAuthLogHistory"."CustId" is '統一編號';
comment on column "PostAuthLogHistory"."RepayAcctSeq" is '帳號碼';
comment on column "PostAuthLogHistory"."ProcessDate" is '處理日期';
comment on column "PostAuthLogHistory"."StampFinishDate" is '核印完成日期';
comment on column "PostAuthLogHistory"."StampCancelDate" is '核印取消日期';
comment on column "PostAuthLogHistory"."StampCode" is '核印註記';
comment on column "PostAuthLogHistory"."PostMediaCode" is '媒體碼';
comment on column "PostAuthLogHistory"."AuthErrorCode" is '狀況代號，授權狀態';
comment on column "PostAuthLogHistory"."FileSeq" is '媒體檔流水編號';
comment on column "PostAuthLogHistory"."PropDate" is '提出日期(媒體產出日)';
comment on column "PostAuthLogHistory"."RetrDate" is '提回日期';
comment on column "PostAuthLogHistory"."DeleteDate" is '刪除日期/暫停授權日期';
comment on column "PostAuthLogHistory"."RelationCode" is '與借款人關係';
comment on column "PostAuthLogHistory"."RelAcctName" is '第三人帳戶戶名';
comment on column "PostAuthLogHistory"."RelationId" is '第三人身分證字號';
comment on column "PostAuthLogHistory"."RelAcctBirthday" is '第三人出生日期';
comment on column "PostAuthLogHistory"."RelAcctGender" is '第三人性別';
comment on column "PostAuthLogHistory"."AmlRsp" is 'AML回應碼';
comment on column "PostAuthLogHistory"."CreateEmpNo" is '建立者櫃員編號';
comment on column "PostAuthLogHistory"."CreateDate" is '建檔日期';
comment on column "PostAuthLogHistory"."LastUpdateEmpNo" is '修改者櫃員編號';
comment on column "PostAuthLogHistory"."LastUpdate" is '異動日期';
