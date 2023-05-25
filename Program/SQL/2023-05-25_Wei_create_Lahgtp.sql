drop table "Lahgtp" purge;

create table "Lahgtp" (
  "Cusbrh" decimal(4, 0) default 0 not null,
  "Gdrid1" decimal(1, 0) default 0 not null,
  "Gdrid2" decimal(2, 0) default 0 not null,
  "Gdrnum" decimal(7, 0) default 0 not null,
  "Lgtseq" decimal(2, 0) default 0 not null,
  "Lgtcif" decimal(6, 0) default 0 not null,
  "Lgtadr" nvarchar2(200),
  "Hgtmhn" decimal(5, 0) default 0 not null,
  "Hgtmhs" decimal(9, 2) default 0 not null,
  "Hgtpsm" decimal(7, 2) default 0 not null,
  "Hgtcam" decimal(6, 2) default 0 not null,
  "Lgtiid" varchar2(20),
  "Lgtunt" decimal(11, 0) default 0 not null,
  "Lgtiam" decimal(11, 0) default 0 not null,
  "Lgtsam" decimal(11, 0) default 0 not null,
  "Lgtsat" decimal(1, 0) default 0 not null,
  "Grtsts" decimal(1, 0) default 0 not null,
  "Hgtstr" varchar2(20),
  "Hgtcdt" decimal(4, 0) default 0 not null,
  "Hgtflr" decimal(2, 0) default 0 not null,
  "Hgtrof" varchar2(20),
  "Salnam" nvarchar2(200),
  "Salid1" varchar2(200),
  "Hgtcap" varchar2(20),
  "Hgtgus" varchar2(20),
  "Hgtaus" varchar2(20),
  "Hgtfor" varchar2(20),
  "Hgtcpe" decimal(8, 0) default 0 not null,
  "Hgtads" decimal(9, 2) default 0 not null,
  "Hgtad1" nvarchar2(20),
  "Hgtad2" nvarchar2(200),
  "Hgtad3" nvarchar2(200),
  "Hgtgtd" decimal(8, 0) default 0 not null,
  "Buyamt" decimal(11, 0) default 0 not null,
  "Buydat" decimal(8, 0) default 0 not null,
  "Gdrnum2" decimal(10, 0) default 0 not null,
  "Gdrmrk" varchar2(20),
  "Hgtmhn2" decimal(3, 0) default 0 not null,
  "Hgtcip" varchar2(20),
  "UpdateIdent" decimal(7, 0) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "Lahgtp" add constraint "Lahgtp_PK" primary key("Cusbrh", "Gdrid1", "Gdrid2", "Gdrnum", "Lgtseq");

comment on table "Lahgtp" is 'AS400建物明細資料檔';
comment on column "Lahgtp"."Cusbrh" is '營業單位別';
comment on column "Lahgtp"."Gdrid1" is '押品別1';
comment on column "Lahgtp"."Gdrid2" is '押品別2';
comment on column "Lahgtp"."Gdrnum" is '押品號碼';
comment on column "Lahgtp"."Lgtseq" is '序號';
comment on column "Lahgtp"."Lgtcif" is '提供人CIFKEY';
comment on column "Lahgtp"."Lgtadr" is '門牌號碼';
comment on column "Lahgtp"."Hgtmhn" is '主建物建號';
comment on column "Lahgtp"."Hgtmhs" is '主建物(坪)';
comment on column "Lahgtp"."Hgtpsm" is '公設(坪)';
comment on column "Lahgtp"."Hgtcam" is '車位(坪)';
comment on column "Lahgtp"."Lgtiid" is '鑑價公司';
comment on column "Lahgtp"."Lgtunt" is '鑑價單價/坪';
comment on column "Lahgtp"."Lgtiam" is '核准金額';
comment on column "Lahgtp"."Lgtsam" is '設定金額';
comment on column "Lahgtp"."Lgtsat" is '代償後謄本';
comment on column "Lahgtp"."Grtsts" is '押品狀況碼';
comment on column "Lahgtp"."Hgtstr" is '建物結構';
comment on column "Lahgtp"."Hgtcdt" is '建造年份';
comment on column "Lahgtp"."Hgtflr" is '樓層數';
comment on column "Lahgtp"."Hgtrof" is '屋頂結構';
comment on column "Lahgtp"."Salnam" is '賣方姓名';
comment on column "Lahgtp"."Salid1" is '賣方ID';
comment on column "Lahgtp"."Hgtcap" is '停車位形式';
comment on column "Lahgtp"."Hgtgus" is '主要用途';
comment on column "Lahgtp"."Hgtaus" is '附屬建物用途';
comment on column "Lahgtp"."Hgtfor" is '所在樓層';
comment on column "Lahgtp"."Hgtcpe" is '建築完成日';
comment on column "Lahgtp"."Hgtads" is '附屬建物(坪)';
comment on column "Lahgtp"."Hgtad1" is '縣市名稱';
comment on column "Lahgtp"."Hgtad2" is '鄉鎮市區名稱';
comment on column "Lahgtp"."Hgtad3" is '街路巷弄';
comment on column "Lahgtp"."Hgtgtd" is '房屋所有權取得日';
comment on column "Lahgtp"."Buyamt" is '買賣契約價格';
comment on column "Lahgtp"."Buydat" is '買賣契約日期';
comment on column "Lahgtp"."Gdrnum2" is '擔保品群組編號';
comment on column "Lahgtp"."Gdrmrk" is '註記';
comment on column "Lahgtp"."Hgtmhn2" is '主建物建號2';
comment on column "Lahgtp"."Hgtcip" is '獨立產權註記';
comment on column "Lahgtp"."UpdateIdent" is 'Field update / access identifier';
comment on column "Lahgtp"."CreateDate" is '建檔日期時間';
comment on column "Lahgtp"."CreateEmpNo" is '建檔人員';
comment on column "Lahgtp"."LastUpdate" is '最後更新日期時間';
comment on column "Lahgtp"."LastUpdateEmpNo" is '最後更新人員';
