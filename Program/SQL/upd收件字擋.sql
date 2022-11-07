drop table "CdLandOffice" purge;

create table "CdLandOffice" (
  "CityCode" varchar2(4),
  "LandOfficeCode" varchar2(2),
  "RecWord" varchar2(3),
  "RecWordItem" nvarchar2(30),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "CdLandOffice" add constraint "CdLandOffice_PK" primary key("CityCode", "LandOfficeCode", "RecWord");

comment on table "CdLandOffice" is '地政收件字檔';
comment on column "CdLandOffice"."CityCode" is '縣市別代碼';
comment on column "CdLandOffice"."LandOfficeCode" is '地政所代碼';
comment on column "CdLandOffice"."RecWord" is '收件字代碼';
comment on column "CdLandOffice"."RecWordItem" is '收件字說明';
comment on column "CdLandOffice"."CreateDate" is '建檔日期時間';
comment on column "CdLandOffice"."CreateEmpNo" is '建檔人員';
comment on column "CdLandOffice"."LastUpdate" is '最後更新日期時間';
comment on column "CdLandOffice"."LastUpdateEmpNo" is '最後更新人員';



INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','AD', '001', '松山', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','AD', '002', '南港', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','AD', '003', '中山', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','AD', '004', '信義', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','AA', '001', '南港', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','AA', '002', '文山', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','AA', '003', '中正一', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','AB', '001', '萬華', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','AE', '001', '松山', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','AE', '002', '士林', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','AE', '003', '北投', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','AE', '004', '大同', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','AC', '001', '松山', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','AC', '002', '南港', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','AC', '003', '文山', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','AC', '004', '北投', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','AC', '005', '大同', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','AC', '006', '大安', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','AC', '007', '中山', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','AC', '008', '內湖', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','AC', '009', '信義', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','AC', '010', '萬華', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','AC', '011', '中正一', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','AC', '012', '普', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','AC', '013', '普登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','AC', '014', '山清普跨', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','AC', '015', '山興登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','AC', '016', '山平普跨', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','AC', '017', '山甲普跨', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','AC', '018', '山雅登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','AC', '019', '山普登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','AC', '020', '山里普跨', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','AC', '021', '山清登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','AF', '001', '大安', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','FA', '001', '板登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','FA', '002', '板登簡', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','FA', '003', '板重登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','FA', '004', '板莊登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','FA', '005', '板樹登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','FA', '006', '板中登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','FA', '007', '板淡登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','FA', '008', '板汐登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','FH', '001', '中淡登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','FH', '002', '北中地登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','FH', '003', '北中地單', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','FH', '004', '北中地資', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','FH', '005', '中重登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','FH', '006', '中板登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','FH', '007', '中莊登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','FG', '001', '重登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','FG', '002', '重簡', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','FG', '003', '重電', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','FG', '004', '重中登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','FG', '005', '重汐登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','FG', '006', '重板登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','FG', '007', '重淡登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','FG', '008', '重莊登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','FG', '009', '重樹登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','FG', '010', '重店登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','FG', '011', '板重登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','FC', '001', '簡易', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','FC', '002', '新登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','FC', '003', '店淡登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','FC', '004', '店重登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','FC', '005', '店莊登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','FC', '006', '店板登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','FB', '001', '莊登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','FB', '002', '莊登簡', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','FB', '003', '板莊登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','FB', '004', '莊板登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','FB', '005', '莊重登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','FI', '001', '樹資', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','FI', '002', '樹資簡', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','FI', '003', '樹登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','FI', '004', '樹莊登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','FI', '005', '樹重登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','FD', '001', '汐地', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','FE', '001', '淡地', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','FE', '002', '淡地登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','FE', '003', '淡地單一', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','CC', '001', '基安', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','CC', '002', '基所', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','CC', '003', '基安速', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','CB', '001', '基信', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','CB', '002', '（空白）', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','CB', '003', '基信速', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','GB', '001', '宜登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','GB', '002', '宜跨', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','GA', '001', '羅登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','GA', '002', '羅', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','UA', '001', '花登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','UA', '002', '花資登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','UC', '001', '玉地普', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','HA', '001', '桃', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','HA', '002', '桃資', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','HA', '003', '桃資速', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','HA', '004', '桃資登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','HA', '005', '大溪跨', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','HA', '006', '桃平登跨', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','HB', '001', '壢速', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','HB', '002', '壢登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','HB', '003', '桃園跨', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','HC', '001', '溪電', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','HC', '002', '溪電速', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','HC', '003', '溪', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','HC', '004', '桃園跨', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','HE', '001', '蘆資', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','HE', '002', '蘆資單', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','HE', '003', '蘆', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','HD', '001', '楊地', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','HD', '002', '楊簡', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','HF', '001', '速資', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','HF', '002', '德資', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','HH', '001', '山平登跨', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','HG', '001', '平速資', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','HG', '002', '平資', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','OA', '001', '空白', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','JB', '001', '竹北', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','JC', '001', '東地', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','JD', '001', '新湖', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','JD', '002', '新', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','KB', '001', '苗地資', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','KB', '002', '苗地所', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','KF', '001', '頭地資', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','KD', '001', '南地所資', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','KC', '001', '通苑資', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','BB', '001', '普', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','BB', '002', '正龍普跨', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','BB', '003', '普登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','BB', '004', '正普登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','BB', '005', '正甲普跨', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','BC', '001', '空白', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','BC', '002', '普登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','BC', '003', '平', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','BJ', '001', '平普資', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','BJ', '002', '普登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','BI', '001', '里普資', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','BI', '002', '普登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','BI', '003', '里山普跨', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','BI', '004', '霧', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','BE', '001', '甲地資', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','BE', '002', '普登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','BE', '003', '甲地簡', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','BG', '001', '普登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','BG', '002', '東地資', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','BG', '003', '東簡資', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','BF', '001', '清登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','BF', '002', '清登資', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','BF', '003', '普登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','BF', '004', '清水', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','BF', '005', '清', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','BH', '001', '雅', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','BH', '002', '雅登資', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','BH', '003', '普登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','BH', '004', '豐登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','BD', '001', '豐', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','BD', '002', '豐登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','BD', '003', '普登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','NA', '001', '彰資', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','ND', '001', '員資', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','ND', '002', '員', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','NE', '001', '田資', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','NF', '001', '北登資', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','NG', '001', '二地', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','NG', '002', '二地資', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','NH', '001', '溪資', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','NC', '001', '鹿登資', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','NB', '001', '彰和資', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','NB', '002', '彰和', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','MA', '001', '南普資', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','MA', '002', '南', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','MA', '003', '南登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','MA', '004', '竹山跨', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','MA', '005', '草屯跨', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','MC', '001', '埔資', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','MC', '002', '埔', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','MB', '001', '草資', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','MB', '002', '南投跨', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','MB', '003', '草', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','MB', '004', '埔里跨', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','MD', '001', '竹', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','MD', '002', '竹普資', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','MD', '003', '竹地登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','ME', '001', '水資登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','IA', '001', '嘉市地', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','IA', '002', '嘉地', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','QB', '001', '朴登普', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','QC', '001', '林地資', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','QC', '002', '林地', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','QD', '001', '上地登1', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','QE', '001', '嘉竹地', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','PB', '001', '南資', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','PD', '001', '虎地資', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','PD', '002', '虎地普', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','PC', '001', '螺資地', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','PE', '001', '北地資', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','PA', '001', '斗地普', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','PF', '001', '台資地', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DA', '001', '台南土', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DA', '002', '南市地', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DA', '003', '台南地', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DA', '004', '台跨東', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DA', '005', '台跨安', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DB', '001', '一般跨所（台南）', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DB', '002', '一般跨所（永康）', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DB', '003', '一般跨所（東南）', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DB', '004', '一般跨所（麻豆）', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DB', '005', '一般跨所（新化）', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DB', '006', '一般跨所（歸仁）', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DB', '007', '一般跨所（鹽水）', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DB', '008', '安南土', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DB', '009', '普', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DB', '010', '普跨（安南台南）', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DB', '011', '普跨（安南永康）', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DB', '012', '普跨（安南東南）', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DB', '013', '普跨（安南新化）', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DB', '014', '普跨（安南歸仁）', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DB', '015', '跨所一般', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DB', '016', '安跨台', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DB', '017', '安南地', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DC', '001', '一般跨所（台南）', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DC', '002', '一般跨所（永康）', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DC', '003', '一般跨所（安南）', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DC', '004', '一般跨所（佳里）', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DC', '005', '一般跨所（新化）', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DC', '006', '一般跨所（歸仁）', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DC', '007', '一般跨所（鹽水）', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DC', '008', '台南土', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DC', '009', '東南地', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DC', '010', '東資地', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DC', '011', '東跨台', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DC', '012', '普', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DC', '013', '普跨（東南永康）', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DE', '001', '白地', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DE', '002', '一般跨所（東南）', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DK', '001', '永', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DK', '002', '永一', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DK', '003', '永地', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DK', '004', '一般跨所（東南）', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DG', '001', '佳地', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DF', '001', '南麻', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DH', '001', '新地普', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DH', '002', '新地化', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DH', '003', '化', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DH', '004', '一般跨所（永康）', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DI', '001', '地', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DI', '002', '歸地', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DD', '001', '一般跨所', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DD', '002', '一般跨所（白河）', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DD', '003', '一般跨所（佳里）', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DD', '004', '一般跨所（東南）', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DD', '005', '一般跨所（麻豆）', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DD', '006', '一般跨所（新化）', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DD', '007', '一般跨所（歸仁）', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DD', '008', '簡易跨所', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DD', '009', '鹽地', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','DD', '010', '鹽登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EA', '001', '鹽專（二）', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EA', '002', '鹽專（三）', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EA', '003', '鹽專', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EA', '004', '鹽登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EA', '005', '鹽地', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EC', '001', '鎭登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EC', '002', '鎮登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EC', '003', '鎮專', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EC', '004', '鎭專', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EC', '005', '前地', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','ED', '001', '三地', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','ED', '002', '三專', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','ED', '003', '三跨楠', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','ED', '004', '三新登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EB', '001', '苓專', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EB', '002', '新三登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EB', '003', '新仁登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EB', '004', '新地苓', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EB', '005', '新地新', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EB', '006', '新岡登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EB', '007', '新前登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EB', '008', '新專', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EB', '009', '新楠登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EB', '010', '新跨三', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EB', '011', '新跨仁', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EB', '012', '新跨岡', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EB', '013', '新跨前', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EB', '014', '新跨楠', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EB', '015', '新跨鳳', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EB', '016', '新跨寮', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EB', '017', '新跨鹽', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EB', '018', '新路登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EB', '019', '新鳳登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EB', '020', '新寮登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EB', '021', '新鹽登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EE', '001', '專左', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EE', '002', '專楠', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EE', '003', '左地', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EE', '004', '楠地', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EG', '001', '抵一', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EG', '002', '抵二', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EG', '003', '專楠', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EG', '004', '鳳三登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EG', '005', '鳳岡登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EG', '006', '鳳前登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EG', '007', '鳳專', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EG', '008', '鳳登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EG', '009', '鳳新登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EG', '010', '鳳楠登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EG', '011', '鳳鹽登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EG', '012', '樹登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EG', '013', '鳳寮登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EF', '001', '岡地', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EF', '002', '岡資簡', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EF', '003', '岡資地', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EF', '004', '岡地簡', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EH', '001', '旗登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EI', '001', '仁登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EI', '002', '仁', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EJ', '001', '路普', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EK', '001', '美登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','EL', '001', '抵登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','TA', '001', '屏登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','TC', '001', '潮登', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','TD', '001', '東地', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','TB', '001', '屏里', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','TE', '001', '屏恒', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','VA', '001', '東地所', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','VC', '001', '東關地所', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','VB', '001', '東關地所', '','','','');
INSERT INTO "CdLandOffice" ("CityCode","LandOfficeCode", "RecWord", "RecWordItem","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo")
VALUES ('0','VD', '001', '東關地所', '','','','');



    MERGE INTO "CdLandOffice" clo 
    USING (SELECT DISTINCT cl."CityCode" ,cl."LandOfficeCode" , clo."RecWord"
            FROM "CdLandOffice" clo  
           LEFT JOIN "CdLand" cl  
             ON  cl."LandOfficeCode" = clo."LandOfficeCode" 
             ) cl
     ON (   clo."LandOfficeCode"    = cl."LandOfficeCode" 
     AND clo."RecWord"    = cl."RecWord"  )
    WHEN MATCHED THEN UPDATE SET clo."CityCode" = cl."CityCode";
