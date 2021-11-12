drop table "InnReCheck" purge;

create table "InnReCheck" (
  "YearMonth" decimal(6, 0) default 0 not null,
  "ConditionCode" decimal(2, 0) default 0 not null,
  "CustNo" decimal(7, 0) default 0 not null,
  "FacmNo" decimal(3, 0) default 0 not null,
  "ReCheckCode" varchar2(1),
  "FollowMark" varchar2(1),
  "ReChkYearMonth" decimal(6, 0) default 0 not null,
  "DrawdownDate" decimal(8, 0) default 0 not null,
  "LoanBal" decimal(16, 2) default 0 not null,
  "Evaluation" decimal(2, 0) default 0 not null,
  "CustTypeItem" nvarchar2(10),
  "UsageItem" nvarchar2(10),
  "CityItem" nvarchar2(10),
  "ReChkUnit" nvarchar2(10),
  "SpecifyFg" varchar2(2),
  "Remark" nvarchar2(300),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "InnReCheck" add constraint "InnReCheck_PK" primary key("YearMonth", "ConditionCode", "CustNo", "FacmNo");

comment on table "InnReCheck" is '覆審案件明細檔';
comment on column "InnReCheck"."YearMonth" is '資料年月';
comment on column "InnReCheck"."ConditionCode" is '條件代碼';
comment on column "InnReCheck"."CustNo" is '借款人戶號';
comment on column "InnReCheck"."FacmNo" is '額度號碼';
comment on column "InnReCheck"."ReCheckCode" is '覆審記號';
comment on column "InnReCheck"."FollowMark" is '追蹤記號';
comment on column "InnReCheck"."ReChkYearMonth" is '覆審年月';
comment on column "InnReCheck"."DrawdownDate" is '撥款日期';
comment on column "InnReCheck"."LoanBal" is '貸放餘額';
comment on column "InnReCheck"."Evaluation" is '評等';
comment on column "InnReCheck"."CustTypeItem" is '客戶別';
comment on column "InnReCheck"."UsageItem" is '用途別';
comment on column "InnReCheck"."CityItem" is '地區別';
comment on column "InnReCheck"."ReChkUnit" is '應覆審單位';
comment on column "InnReCheck"."SpecifyFg" is '指定複審記號';
comment on column "InnReCheck"."Remark" is '備註';
comment on column "InnReCheck"."CreateDate" is '建檔日期時間';
comment on column "InnReCheck"."CreateEmpNo" is '建檔人員';
comment on column "InnReCheck"."LastUpdate" is '最後更新日期時間';
comment on column "InnReCheck"."LastUpdateEmpNo" is '最後更新人員';
