drop table "CollLaw" purge;

create table "CollLaw" (
  "CaseCode" varchar2(1),
  "ClCode1" decimal(1, 0) default 0 not null,
  "ClCode2" decimal(2, 0) default 0 not null,
  "ClNo" decimal(7, 0) default 0 not null,
  "CustNo" decimal(7, 0) default 0 not null,
  "FacmNo" decimal(3, 0) default 0 not null,
  "AcDate" decimal(8, 0) default 0 not null,
  "TitaTlrNo" varchar2(6),
  "TitaTxtNo" varchar2(8),
  "RecordDate" decimal(8, 0) default 0 not null,
  "LegalProg" varchar2(3),
  "Amount" decimal(16, 2) default 0 not null,
  "Remark" varchar2(1),
  "Memo" nvarchar2(500),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "CollLaw" add constraint "CollLaw_PK" primary key("CaseCode", "CustNo", "FacmNo", "AcDate", "TitaTlrNo", "TitaTxtNo");

comment on table "CollLaw" is '法催紀錄法務進度檔';
comment on column "CollLaw"."CaseCode" is '案件種類';
comment on column "CollLaw"."ClCode1" is '擔保品代號1';
comment on column "CollLaw"."ClCode2" is '擔保品代號2';
comment on column "CollLaw"."ClNo" is '擔保品編號';
comment on column "CollLaw"."CustNo" is '借款人戶號';
comment on column "CollLaw"."FacmNo" is '額度編號';
comment on column "CollLaw"."AcDate" is '作業日期';
comment on column "CollLaw"."TitaTlrNo" is '經辦';
comment on column "CollLaw"."TitaTxtNo" is '交易序號';
comment on column "CollLaw"."RecordDate" is '記錄日期';
comment on column "CollLaw"."LegalProg" is '法務進度';
comment on column "CollLaw"."Amount" is '金額';
comment on column "CollLaw"."Remark" is '其他記錄選項';
comment on column "CollLaw"."Memo" is '其他紀錄內容';
comment on column "CollLaw"."CreateDate" is '建檔日期時間';
comment on column "CollLaw"."CreateEmpNo" is '建檔人員';
comment on column "CollLaw"."LastUpdate" is '最後更新日期時間';
comment on column "CollLaw"."LastUpdateEmpNo" is '最後更新人員';
