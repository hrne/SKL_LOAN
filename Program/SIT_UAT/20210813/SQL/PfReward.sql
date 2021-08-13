drop table "PfReward" purge;

drop sequence "PfReward_SEQ";

create table "PfReward" (
  "LogNo" decimal(11,0) not null,
  "PerfDate" decimal(8, 0) default 0 not null,
  "CustNo" decimal(7, 0) default 0 not null,
  "FacmNo" decimal(3, 0) default 0 not null,
  "BormNo" decimal(3, 0) default 0 not null,
  "RepayType" decimal(1, 0) default 0 not null,
  "PieceCode" varchar2(1),
  "ProdCode" varchar2(5),
  "Introducer" varchar2(6),
  "Coorgnizer" varchar2(6),
  "InterviewerA" varchar2(6),
  "InterviewerB" varchar2(6),
  "IntroducerBonus" decimal(16, 2) default 0 not null,
  "IntroducerBonusDate" decimal(8, 0) default 0 not null,
  "IntroducerAddBonus" decimal(16, 2) default 0 not null,
  "IntroducerAddBonusDate" decimal(8, 0) default 0 not null,
  "CoorgnizerBonus" decimal(16, 2) default 0 not null,
  "CoorgnizerBonusDate" decimal(8, 0) default 0 not null,
  "WorkMonth" decimal(6, 0) default 0 not null,
  "WorkSeason" decimal(5, 0) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "PfReward" add constraint "PfReward_PK" primary key("LogNo");

create sequence "PfReward_SEQ" minvalue 1 maxvalue 2147483647 increment by 1 start with 1 nocache cycle;

create index "PfReward_Index1" on "PfReward"("WorkMonth" asc, "CustNo" asc, "FacmNo" asc, "BormNo" asc, "PerfDate" asc);

create index "PfReward_Index2" on "PfReward"("CustNo" asc, "FacmNo" asc, "BormNo" asc, "PerfDate" asc);

comment on table "PfReward" is '介紹、協辦獎金明細檔';
comment on column "PfReward"."LogNo" is '序號';
comment on column "PfReward"."PerfDate" is '業績日期';
comment on column "PfReward"."CustNo" is '戶號';
comment on column "PfReward"."FacmNo" is '額度編號';
comment on column "PfReward"."BormNo" is '撥款序號';
comment on column "PfReward"."RepayType" is '還款類別';
comment on column "PfReward"."PieceCode" is '計件代碼';
comment on column "PfReward"."ProdCode" is '商品代碼';
comment on column "PfReward"."Introducer" is '介紹人員編';
comment on column "PfReward"."Coorgnizer" is '協辦人員編';
comment on column "PfReward"."InterviewerA" is '晤談一員編';
comment on column "PfReward"."InterviewerB" is '晤談二員編';
comment on column "PfReward"."IntroducerBonus" is '介紹人介紹獎金';
comment on column "PfReward"."IntroducerBonusDate" is '介紹獎金轉檔日';
comment on column "PfReward"."IntroducerAddBonus" is '介紹人加碼獎勵津貼';
comment on column "PfReward"."IntroducerAddBonusDate" is '獎勵津貼轉檔日';
comment on column "PfReward"."CoorgnizerBonus" is '協辦人員協辦獎金';
comment on column "PfReward"."CoorgnizerBonusDate" is '協辦獎金轉檔日';
comment on column "PfReward"."WorkMonth" is '工作月';
comment on column "PfReward"."WorkSeason" is '工作季';
comment on column "PfReward"."CreateDate" is '建檔日期時間';
comment on column "PfReward"."CreateEmpNo" is '建檔人員';
comment on column "PfReward"."LastUpdate" is '最後更新日期時間';
comment on column "PfReward"."LastUpdateEmpNo" is '最後更新人員';
