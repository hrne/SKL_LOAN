drop table "CdComm" purge;

create table "CdComm" (
  "CdType" varchar2(5),
  "CdItem" varchar2(5),
  "EffectDate" decimal(8, 0) default 0 not null,
  "Enable" varchar2(1),
  "Remark" nvarchar2(40),
  "JsonFields" varchar2(300),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "CdComm" add constraint "CdComm_PK" primary key("CdType", "CdItem", "EffectDate");

comment on table "CdComm" is '雜項代碼檔';
comment on column "CdComm"."CdType" is '代碼類別';
comment on column "CdComm"."CdItem" is '代碼項目';
comment on column "CdComm"."EffectDate" is '生效日期';
comment on column "CdComm"."Enable" is '啟用記號';
comment on column "CdComm"."Remark" is '備註';
comment on column "CdComm"."JsonFields" is 'jason格式紀錄欄';
comment on column "CdComm"."CreateDate" is '建檔日期時間';
comment on column "CdComm"."CreateEmpNo" is '建檔人員';
comment on column "CdComm"."LastUpdate" is '最後更新日期時間';
comment on column "CdComm"."LastUpdateEmpNo" is '最後更新人員';
