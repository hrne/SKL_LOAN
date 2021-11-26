drop table "InnDocRecord" purge;

create table "InnDocRecord" (
  "CustNo" decimal(7, 0) default 0 not null,
  "FacmNo" decimal(3, 0) default 0 not null,
  "ApplSeq" varchar2(3),
  "TitaActFg" varchar2(1),
  "ApplCode" varchar2(1),
  "ApplEmpNo" varchar2(6),
  "KeeperEmpNo" varchar2(6),
  "UsageCode" varchar2(2),
  "CopyCode" varchar2(1),
  "ApplDate" decimal(8, 0) default 0 not null,
  "ReturnDate" decimal(8, 0) default 0 not null,
  "ReturnEmpNo" varchar2(6),
  "Remark" nvarchar2(60),
  "ApplObj" varchar2(1),
  "TitaEntDy" decimal(8, 0) default 0 not null,
  "TitaTlrNo" varchar2(6),
  "TitaTxtNo" decimal(8, 0) default 0 not null,
  "JsonFields" nvarchar2(300),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "InnDocRecord" add constraint "InnDocRecord_PK" primary key("CustNo", "FacmNo", "ApplSeq");

create index "InnDocRecord_Index1" on "InnDocRecord"("CustNo" asc, "ApplDate" asc, "UsageCode" asc, "ApplCode" asc);

comment on table "InnDocRecord" is '檔案借閱檔';
comment on column "InnDocRecord"."CustNo" is '借款人戶號';
comment on column "InnDocRecord"."FacmNo" is '額度號碼';
comment on column "InnDocRecord"."ApplSeq" is '申請序號';
comment on column "InnDocRecord"."TitaActFg" is '登放記號';
comment on column "InnDocRecord"."ApplCode" is '申請或歸還';
comment on column "InnDocRecord"."ApplEmpNo" is '借閱人';
comment on column "InnDocRecord"."KeeperEmpNo" is '管理人';
comment on column "InnDocRecord"."UsageCode" is '用途';
comment on column "InnDocRecord"."CopyCode" is '正本/影本';
comment on column "InnDocRecord"."ApplDate" is '借閱日期';
comment on column "InnDocRecord"."ReturnDate" is '歸還日期';
comment on column "InnDocRecord"."ReturnEmpNo" is '歸還人';
comment on column "InnDocRecord"."Remark" is '備註';
comment on column "InnDocRecord"."ApplObj" is '借閱項目';
comment on column "InnDocRecord"."TitaEntDy" is '登錄日期';
comment on column "InnDocRecord"."TitaTlrNo" is '登錄經辦';
comment on column "InnDocRecord"."TitaTxtNo" is '登錄交易序號';
comment on column "InnDocRecord"."JsonFields" is 'jason格式紀錄欄';
comment on column "InnDocRecord"."CreateDate" is '建檔日期時間';
comment on column "InnDocRecord"."CreateEmpNo" is '建檔人員';
comment on column "InnDocRecord"."LastUpdate" is '最後更新日期時間';
comment on column "InnDocRecord"."LastUpdateEmpNo" is '最後更新人員';
