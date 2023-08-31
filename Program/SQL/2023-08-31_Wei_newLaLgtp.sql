drop table "LaLgtp" purge;

create table "LaLgtp" (
  "Cusbrh" decimal(4, 0) default 0 not null,
  "Gdrid1" decimal(1, 0) default 0 not null,
  "Gdrid2" decimal(2, 0) default 0 not null,
  "Gdrnum" decimal(7, 0) default 0 not null,
  "Lgtseq" decimal(2, 0) default 0 not null,
  "Gdrmrk" varchar2(20),
  "Gdrnum2" decimal(10, 0) default 0 not null,
  "Grtsts" decimal(1, 0) default 0 not null,
  "Lgtcif" decimal(6, 0) default 0 not null,
  "Lgtiam" decimal(11, 0) default 0 not null,
  "Lgtiid" varchar2(20),
  "Lgtory" varchar2(20),
  "Lgtpta" decimal(11, 0) default 0 not null,
  "Lgtsam" decimal(11, 0) default 0 not null,
  "Lgtsat" decimal(1, 0) default 0 not null,
  "Lgtcty" varchar2(20),
  "Lgttwn" varchar2(20),
  "Lgtsgm" varchar2(200),
  "Lgtssg" varchar2(200),
  "Lgtnm1" decimal(4, 0) default 0 not null,
  "Lgtnm2" decimal(4, 0) default 0 not null,
  "Lgtsqm" decimal(9, 2) default 0 not null,
  "Lgttax" decimal(11, 0) default 0 not null,
  "Lgttay" decimal(6, 0) default 0 not null,
  "Lgttyp" varchar2(20),
  "Lgttyr" decimal(4, 0) default 0 not null,
  "Lgtunt" decimal(11, 0) default 0 not null,
  "Lgtuse" varchar2(20),
  "Lgtval" decimal(11, 0) default 0 not null,
  "Lgtvym" decimal(6, 0) default 0 not null,
  "Updateident" decimal(7, 0) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "LaLgtp" add constraint "LaLgtp_PK" primary key("Cusbrh", "Gdrid1", "Gdrid2", "Gdrnum", "Lgtseq");

comment on table "LaLgtp" is 'AS400土地明細資料檔';
comment on column "LaLgtp"."Cusbrh" is '營業單位別';
comment on column "LaLgtp"."Gdrid1" is '押品別１';
comment on column "LaLgtp"."Gdrid2" is '押品別２';
comment on column "LaLgtp"."Gdrnum" is '押品號碼';
comment on column "LaLgtp"."Lgtseq" is '序號';
comment on column "LaLgtp"."Gdrmrk" is '註記';
comment on column "LaLgtp"."Gdrnum2" is '擔保品群組編號';
comment on column "LaLgtp"."Grtsts" is '押品狀況碼';
comment on column "LaLgtp"."Lgtcif" is '提供人CIFKEY';
comment on column "LaLgtp"."Lgtiam" is '核准金額';
comment on column "LaLgtp"."Lgtiid" is '鑑價公司';
comment on column "LaLgtp"."Lgtory" is '地目代號';
comment on column "LaLgtp"."Lgtpta" is '前次移轉金額';
comment on column "LaLgtp"."Lgtsam" is '設定金額';
comment on column "LaLgtp"."Lgtsat" is '代償後謄本';
comment on column "LaLgtp"."Lgtcty" is '縣市';
comment on column "LaLgtp"."Lgttwn" is '鄉鎮區';
comment on column "LaLgtp"."Lgtsgm" is '段';
comment on column "LaLgtp"."Lgtssg" is '小段';
comment on column "LaLgtp"."Lgtnm1" is '地號１';
comment on column "LaLgtp"."Lgtnm2" is '地號２';
comment on column "LaLgtp"."Lgtsqm" is '面積（坪）';
comment on column "LaLgtp"."Lgttax" is '土地增值稅';
comment on column "LaLgtp"."Lgttay" is '土增稅年月';
comment on column "LaLgtp"."Lgttyp" is '使用地類別';
comment on column "LaLgtp"."Lgttyr" is '移轉年度';
comment on column "LaLgtp"."Lgtunt" is '鑑價單價／坪';
comment on column "LaLgtp"."Lgtuse" is '使用分區';
comment on column "LaLgtp"."Lgtval" is '公告土地現值';
comment on column "LaLgtp"."Lgtvym" is '土地現值年月';
comment on column "LaLgtp"."Updateident" is 'Fieldupdateaccessidentifier';
comment on column "LaLgtp"."CreateDate" is '建檔日期時間';
comment on column "LaLgtp"."CreateEmpNo" is '建檔人員';
comment on column "LaLgtp"."LastUpdate" is '最後更新日期時間';
comment on column "LaLgtp"."LastUpdateEmpNo" is '最後更新人員';
