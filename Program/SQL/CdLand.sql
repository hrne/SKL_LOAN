drop table "CdLand" purge;

create table "CdLand" (
  "CityCode" varchar2(2),
  "LandOfficeCode" varchar2(2),
  "LandOfficeItem" nvarchar2(30),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "CdLand" add constraint "CdLand_PK" primary key("LandOfficeCode");

comment on table "CdLand" is '縣市地政檔';
comment on column "CdLand"."CityCode" is '縣市代碼';
comment on column "CdLand"."LandOfficeCode" is '地政所代碼';
comment on column "CdLand"."LandOfficeItem" is '地政所說明';
comment on column "CdLand"."CreateDate" is '建檔日期時間';
comment on column "CdLand"."CreateEmpNo" is '建檔人員';
comment on column "CdLand"."LastUpdate" is '最後更新日期時間';
comment on column "CdLand"."LastUpdateEmpNo" is '最後更新人員';


INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('03','CB','信義');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('03','CC','安樂');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('05','AA','古亭');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('05','AB','建成');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('05','AC','中山');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('05','AD','松山');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('05','AE','士林');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('05','AF','大安');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('10','FA','板橋');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('10','FB','新莊');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('10','FC','新店');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('10','FD','汐止');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('10','FE','淡水');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('10','FF','瑞芳');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('10','FG','三重');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('10','FH','中和');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('10','FI','樹林');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('15','HA','桃園');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('15','HB','中壢');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('15','HC','大溪');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('15','HD','楊梅');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('15','HE','蘆竹');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('15','HF','八德');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('15','HG','平鎮');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('15','HH','龜山');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('17','OA','新竹');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('20','JB','竹北');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('20','JC','竹東');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('20','JD','新湖');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('25','KA','大湖');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('25','KB','苗栗');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('25','KC','通霄');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('25','KD','竹南');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('25','KE','銅鑼');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('25','KF','頭份');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('35','BA','中山');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('35','BB','中正');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('35','BC','中興');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('35','BD','豐原');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('35','BE','大甲');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('35','BF','清水');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('35','BG','東勢');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('35','BH','雅潭');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('35','BI','大里');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('35','BJ','太平');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('35','BK','龍井');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('40','NA','彰化');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('40','NB','和美');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('40','NC','鹿港');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('40','ND','員林');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('40','NE','田中');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('40','NF','北斗');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('40','NG','二林');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('40','NH','溪湖');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('45','MA','南投');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('45','MB','草屯');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('45','MC','埔里');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('45','MD','竹山');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('45','ME','水里');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('50','PA','斗六');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('50','PB','斗南');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('50','PC','西螺');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('50','PD','虎尾');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('50','PE','北港');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('50','PF','台西');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('54','IA','嘉義');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('55','QB','朴子');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('55','QC','大林');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('55','QD','水上');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('55','QE','竹崎');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('65','DA','台南');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('65','DB','安南');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('65','DC','東南');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('65','DD','鹽水');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('65','DE','白河');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('65','DF','麻豆');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('65','DG','佳里');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('65','DH','新化');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('65','DI','歸仁');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('65','DJ','玉井');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('65','DK','永康');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('70','EA','鹽埕');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('70','EB','新興');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('70','EC','前鎮');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('70','ED','三民');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('70','EE','楠梓');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('70','EF','岡山');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('70','EG','鳳山');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('70','EH','旗山');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('70','EI','仁武');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('70','EJ','路竹');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('70','EK','美濃');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('70','EL','大寮');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('80','TA','屏東');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('80','TB','里港');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('80','TC','潮州');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('80','TD','東港');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('80','TE','恆春');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('80','TF','枋寮');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('85','VA','台東');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('85','VB','成功');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('85','VC','關山');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('85','VD','太麻里');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('90','UA','花蓮');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('90','UB','鳳林');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('90','UC','玉里');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('95','GA','羅東');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('95','GB','宜蘭');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('96','WA','金門');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('97','XA','澎湖');
INSERT INTO "CdLand" ("CityCode","LandOfficeCode","LandOfficeItem") VALUES ('98','ZA','連江');