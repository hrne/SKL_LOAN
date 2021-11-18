drop table "TxToDoDetailReserve" purge;

drop sequence "TxToDoDetailReserve_SEQ";

create table "TxToDoDetailReserve" (
  "LogNo" decimal(11,0) not null,
  "ItemCode" varchar2(6),
  "CustNo" decimal(7, 0) default 0 not null,
  "FacmNo" decimal(3, 0) default 0 not null,
  "BormNo" decimal(3, 0) default 0 not null,
  "DtlValue" varchar2(30),
  "Status" decimal(1, 0) default 0 not null,
  "ProcessNote" nvarchar2(300),
  "ExcuteTxcd" varchar2(5),
  "DataDate" decimal(8, 0) default 0 not null,
  "TitaEntdy" decimal(8, 0) default 0 not null,
  "TitaKinbr" varchar2(4),
  "TitaTlrNo" varchar2(6),
  "TitaTxtNo" decimal(8, 0) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "TxToDoDetailReserve" add constraint "TxToDoDetailReserve_PK" primary key("LogNo");

create sequence "TxToDoDetailReserve_SEQ" minvalue 1 maxvalue 2147483647 increment by 1 start with 1 nocache cycle;

create index "TxToDoDetailReserve_Index1" on "TxToDoDetailReserve"("ItemCode" asc, "CustNo" asc, "FacmNo" asc, "BormNo" asc, "DtlValue" asc);

comment on table "TxToDoDetailReserve" is '應處理明細留存檔';
comment on column "TxToDoDetailReserve"."LogNo" is '序號';
comment on column "TxToDoDetailReserve"."ItemCode" is '項目';
comment on column "TxToDoDetailReserve"."CustNo" is '借款人戶號';
comment on column "TxToDoDetailReserve"."FacmNo" is '額度編號';
comment on column "TxToDoDetailReserve"."BormNo" is '撥款序號';
comment on column "TxToDoDetailReserve"."DtlValue" is '明細鍵值';
comment on column "TxToDoDetailReserve"."Status" is '資料狀態';
comment on column "TxToDoDetailReserve"."ProcessNote" is '處理事項說明';
comment on column "TxToDoDetailReserve"."ExcuteTxcd" is '執行交易';
comment on column "TxToDoDetailReserve"."DataDate" is '資料日期';
comment on column "TxToDoDetailReserve"."TitaEntdy" is '作帳日期';
comment on column "TxToDoDetailReserve"."TitaKinbr" is '登錄單位別';
comment on column "TxToDoDetailReserve"."TitaTlrNo" is '登錄經辦';
comment on column "TxToDoDetailReserve"."TitaTxtNo" is '登錄交易序號';
comment on column "TxToDoDetailReserve"."CreateDate" is '建檔日期時間';
comment on column "TxToDoDetailReserve"."CreateEmpNo" is '建檔人員';
comment on column "TxToDoDetailReserve"."LastUpdate" is '最後更新日期時間';
comment on column "TxToDoDetailReserve"."LastUpdateEmpNo" is '最後更新人員';
