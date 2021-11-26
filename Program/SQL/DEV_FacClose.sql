drop table "FacClose" purge;

create table "FacClose" (
  "CustNo" decimal(7, 0) default 0 not null,
  "CloseNo" decimal(3, 0) default 0 not null,
  "FacmNo" decimal(3, 0) default 0 not null,
  "ActFlag" decimal(1, 0) default 0 not null,
  "FunCode" varchar2(1),
  "CarLoan" decimal(1, 0) default 0 not null,
  "ApplDate" decimal(8, 0) default 0 not null,
  "CloseDate" decimal(8, 0) default 0 not null,
  "CloseInd" varchar2(1),
  "CloseReasonCode" varchar2(2),
  "CloseAmt" decimal(16, 2) default 0 not null,
  "CollectFlag" varchar2(1),
  "CollectWayCode" varchar2(2),
  "ReceiveDate" decimal(8, 0) default 0 not null,
  "TelNo1" varchar2(15),
  "TelNo2" varchar2(15),
  "TelNo3" varchar2(15),
  "EntryDate" decimal(8, 0) default 0 not null,
  "AgreeNo" varchar2(10),
  "DocNo" decimal(7, 0) default 0 not null,
  "ClsNo" nvarchar2(18),
  "Rmk" nvarchar2(100),
  "ClCode1" decimal(1, 0) default 0 not null,
  "ClCode2" decimal(2, 0) default 0 not null,
  "ClNo" decimal(7, 0) default 0 not null,
  "ReceiveFg" decimal(1, 0) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "FacClose" add constraint "FacClose_PK" primary key("CustNo", "CloseNo");

create index "FacClose_Index1" on "FacClose"("CustNo" asc);

create index "FacClose_Index2" on "FacClose"("CustNo" asc, "FacmNo" asc);

create index "FacClose_Index3" on "FacClose"("CustNo" asc, "FacmNo" asc, "CloseDate" asc);

comment on table "FacClose" is '清償作業檔';
comment on column "FacClose"."CustNo" is '戶號';
comment on column "FacClose"."CloseNo" is '清償序號';
comment on column "FacClose"."FacmNo" is '額度編號';
comment on column "FacClose"."ActFlag" is '登放記號';
comment on column "FacClose"."FunCode" is '作業功能';
comment on column "FacClose"."CarLoan" is '車貸';
comment on column "FacClose"."ApplDate" is '申請日期';
comment on column "FacClose"."CloseDate" is '結案日期(入帳日期)';
comment on column "FacClose"."CloseInd" is '結案區分';
comment on column "FacClose"."CloseReasonCode" is '清償原因';
comment on column "FacClose"."CloseAmt" is '還清金額';
comment on column "FacClose"."CollectFlag" is '是否領取清償證明(Y/N/'')';
comment on column "FacClose"."CollectWayCode" is '領取方式';
comment on column "FacClose"."ReceiveDate" is '領取日期';
comment on column "FacClose"."TelNo1" is '連絡電話1';
comment on column "FacClose"."TelNo2" is '連絡電話2';
comment on column "FacClose"."TelNo3" is '連絡電話3';
comment on column "FacClose"."EntryDate" is '入帳日期';
comment on column "FacClose"."AgreeNo" is '塗銷同意書編號';
comment on column "FacClose"."DocNo" is '公文編號';
comment on column "FacClose"."ClsNo" is '銷號欄';
comment on column "FacClose"."Rmk" is '備註';
comment on column "FacClose"."ClCode1" is '擔保品代號1';
comment on column "FacClose"."ClCode2" is '擔保品代號2';
comment on column "FacClose"."ClNo" is '擔保品編號';
comment on column "FacClose"."ReceiveFg" is '領取記號';
comment on column "FacClose"."CreateDate" is '建檔日期時間';
comment on column "FacClose"."CreateEmpNo" is '建檔人員';
comment on column "FacClose"."LastUpdate" is '最後更新日期時間';
comment on column "FacClose"."LastUpdateEmpNo" is '最後更新人員';
