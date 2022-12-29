drop table "CdReport" purge;

create table "CdReport" (
  "FormNo" varchar2(10),
  "FormName" nvarchar2(40),
  "Cycle" decimal(2, 0) default 0 not null,
  "SendCode" decimal(1, 0) default 0 not null,
  "LetterFg" varchar2(1),
  "MessageFg" varchar2(1),
  "EmailFg" varchar2(1),
  "Letter" decimal(1, 0) default 0 not null,
  "Message" decimal(1, 0) default 0 not null,
  "Email" decimal(1, 0) default 0 not null,
  "UsageDesc" nvarchar2(40),
  "SignCode" decimal(1, 0) default 0 not null,
  "WatermarkFlag" decimal(1, 0) default 0 not null,
  "Enable" varchar2(1),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "CdReport" add constraint "CdReport_PK" primary key("FormNo");

create index "CdReport_Index1" on "CdReport"("Cycle" asc);

create index "CdReport_Index2" on "CdReport"("Enable" asc);

comment on table "CdReport" is '報表代號對照檔';
comment on column "CdReport"."FormNo" is '報表代號';
comment on column "CdReport"."FormName" is '報表名稱';
comment on column "CdReport"."Cycle" is '報表週期';
comment on column "CdReport"."SendCode" is '寄送記號';
comment on column "CdReport"."LetterFg" is '提供書面寄送';
comment on column "CdReport"."MessageFg" is '提供簡訊寄送';
comment on column "CdReport"."EmailFg" is '提供電子郵件寄送';
comment on column "CdReport"."Letter" is '書面寄送順序';
comment on column "CdReport"."Message" is '簡訊寄送順序';
comment on column "CdReport"."Email" is '電子郵件寄送順序';
comment on column "CdReport"."UsageDesc" is '用途說明';
comment on column "CdReport"."SignCode" is '簽核記號';
comment on column "CdReport"."WatermarkFlag" is '浮水印記號';
comment on column "CdReport"."Enable" is '啟用記號';
comment on column "CdReport"."CreateDate" is '建檔日期時間';
comment on column "CdReport"."CreateEmpNo" is '建檔人員';
comment on column "CdReport"."LastUpdate" is '最後更新日期時間';
comment on column "CdReport"."LastUpdateEmpNo" is '最後更新人員';
