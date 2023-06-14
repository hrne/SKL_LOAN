drop table "Lagdtp" purge;

create table "Lagdtp" (
  "Cusbrh" decimal(4, 0) default 0 not null,
  "Gdrid1" decimal(1, 0) default 0 not null,
  "Gdrid2" decimal(2, 0) default 0 not null,
  "Gdrnum" decimal(7, 0) default 0 not null,
  "Loclid" decimal(2, 0) default 0 not null,
  "Gdtidt" decimal(8, 0) default 0 not null,
  "Gdtrdt" decimal(8, 0) default 0 not null,
  "Gdtpty" decimal(1, 0) default 0 not null,
  "Gdtp1a" decimal(11, 0) default 0 not null,
  "Gdtp1m" varchar2(200),
  "Gdtp2a" decimal(11, 0) default 0 not null,
  "Gdtp2m" varchar2(200),
  "Gdtp3a" decimal(11, 0) default 0 not null,
  "Gdtp3m" varchar2(200),
  "Gdttmr" varchar2(200),
  "Aplpam" decimal(11, 0) default 0 not null,
  "Lmsacn" decimal(7, 0) default 0 not null,
  "Lmsapn" decimal(3, 0) default 0 not null,
  "Gdtsdt" decimal(8, 0) default 0 not null,
  "Gdttyp" decimal(1, 0) default 0 not null,
  "Gdtapn" decimal(3, 0) default 0 not null,
  "Estval" decimal(11, 0) default 0 not null,
  "Rstval" decimal(11, 0) default 0 not null,
  "Ettval" decimal(11, 0) default 0 not null,
  "Risval" decimal(11, 0) default 0 not null,
  "Rntval" decimal(11, 0) default 0 not null,
  "Mgttyp" varchar2(20),
  "Mtgagm" varchar2(20),
  "Gdrnum2" decimal(10, 0) default 0 not null,
  "Gdrmrk" varchar2(20),
  "UpdateIdent" decimal(7, 0) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "Lagdtp" add constraint "Lagdtp_PK" primary key("Cusbrh", "Gdrid1", "Gdrid2", "Gdrnum");

comment on table "Lagdtp" is 'AS400不動產押品主檔';
comment on column "Lagdtp"."Cusbrh" is '營業單位別';
comment on column "Lagdtp"."Gdrid1" is '押品別1';
comment on column "Lagdtp"."Gdrid2" is '押品別2';
comment on column "Lagdtp"."Gdrnum" is '押品號碼';
comment on column "Lagdtp"."Loclid" is '地區別';
comment on column "Lagdtp"."Gdtidt" is '鑑價期限';
comment on column "Lagdtp"."Gdtrdt" is '他項存續期限';
comment on column "Lagdtp"."Gdtpty" is '順位';
comment on column "Lagdtp"."Gdtp1a" is '前一順位金額';
comment on column "Lagdtp"."Gdtp1m" is '前一順位債權人';
comment on column "Lagdtp"."Gdtp2a" is '前二順位金額';
comment on column "Lagdtp"."Gdtp2m" is '前二順位債權人';
comment on column "Lagdtp"."Gdtp3a" is '前三順位金額';
comment on column "Lagdtp"."Gdtp3m" is '前三順位債權人';
comment on column "Lagdtp"."Gdttmr" is '建物標示備註';
comment on column "Lagdtp"."Aplpam" is '核准額度';
comment on column "Lagdtp"."Lmsacn" is '借款人戶號';
comment on column "Lagdtp"."Lmsapn" is '額度編號';
comment on column "Lagdtp"."Gdtsdt" is '設定日期';
comment on column "Lagdtp"."Gdttyp" is '不動產別';
comment on column "Lagdtp"."Gdtapn" is '一押品多額度時應註明相同額度';
comment on column "Lagdtp"."Estval" is '評估淨值';
comment on column "Lagdtp"."Rstval" is '出租評估淨值';
comment on column "Lagdtp"."Ettval" is '評估總價';
comment on column "Lagdtp"."Risval" is '總增值稅';
comment on column "Lagdtp"."Rntval" is '押租金';
comment on column "Lagdtp"."Mgttyp" is '抵押權設定種類';
comment on column "Lagdtp"."Mtgagm" is '是否檢附同意書';
comment on column "Lagdtp"."Gdrnum2" is '擔保品群組編號';
comment on column "Lagdtp"."Gdrmrk" is '註記';
comment on column "Lagdtp"."UpdateIdent" is 'Field update / access identifier';
comment on column "Lagdtp"."CreateDate" is '建檔日期時間';
comment on column "Lagdtp"."CreateEmpNo" is '建檔人員';
comment on column "Lagdtp"."LastUpdate" is '最後更新日期時間';
comment on column "Lagdtp"."LastUpdateEmpNo" is '最後更新人員';
