--------------------------------------------------------
--  已建立檔案 - 星期四-三月-16-2023   
--------------------------------------------------------
DROP TABLE "TfActblMapping";
--------------------------------------------------------
--  DDL for Table TfActblMapping
--------------------------------------------------------

  CREATE TABLE "TfActblMapping" 
   (	"Pk" NUMBER(3,0) DEFAULT 0, 
	"ACNACC" CHAR(5 CHAR), 
	"ACNACS" CHAR(5 CHAR), 
	"ACNASS" CHAR(2 CHAR), 
	"LCDDTN" NVARCHAR2(100), 
	"AcNoCode" CHAR(11 CHAR), 
	"AcSubCode" CHAR(5 CHAR), 
	"AcDtlCode" CHAR(2 CHAR), 
	"AcItem" NVARCHAR2(100), 
	"AcDesc" NVARCHAR2(100), 
	"Remark" NVARCHAR2(100)
   ) ;

   COMMENT ON COLUMN "TfActblMapping"."Pk" IS '主鍵';
   COMMENT ON COLUMN "TfActblMapping"."ACNACC" IS '原科目';
   COMMENT ON COLUMN "TfActblMapping"."ACNACS" IS '原子目';
   COMMENT ON COLUMN "TfActblMapping"."ACNASS" IS '原細目';
   COMMENT ON COLUMN "TfActblMapping"."LCDDTN" IS '原科目名稱';
   COMMENT ON COLUMN "TfActblMapping"."AcNoCode" IS '新科目';
   COMMENT ON COLUMN "TfActblMapping"."AcSubCode" IS '新子目';
   COMMENT ON COLUMN "TfActblMapping"."AcDtlCode" IS '新細目';
   COMMENT ON COLUMN "TfActblMapping"."AcItem" IS '新科目名稱';
   COMMENT ON COLUMN "TfActblMapping"."AcDesc" IS '科目說明';
   COMMENT ON COLUMN "TfActblMapping"."Remark" IS '備註';
   COMMENT ON TABLE "TfActblMapping"  IS '資料轉換會科對照表';
REM INSERTING into "TfActblMapping"
SET DEFINE OFF;
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (1,'11020','01110','12','００２０７－７合庫城東','Z1102001110','     ','12','００２０７－７合庫城東','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (2,'11020','01121','01','９１１８８－９合庫城東活存','Z1102001121','     ','01','９１１８８－９合庫城東活存','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (3,'11020','01121','02','９１８００－０合庫城東活存','Z1102001121','     ','02','９１８００－０合庫城東活存','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (4,'11020','01121','04','０００６６６－０台新活存','Z1102001121','     ','04','０００６６６－０台新活存','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (5,'11020','01121','06','０８３８８－９合庫城東','Z1102001121','     ','06','０８３８８－９合庫城東','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (6,'11020','01121','08','０７６７６－９合庫城東','Z1102001121','     ','08','０７６７６－９合庫城東','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (7,'11020','01121','09','台北郵局１６１５６１８９','Z1102001121','     ','09','台北郵局１６１５６１８９','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (8,'11020','01121','10','０００００２－４台新敦南','Z1102001121','     ','10','０００００２－４台新敦南','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (9,'11020','01121','11','００００３３－０台新活存','Z1102001121','     ','11','００００３３－０台新活存','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (10,'11020','01121','13','９２６９１－６合庫城東活存','Z1102001121','     ','13','９２６９１－６合庫城東活存','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (11,'11410','40183',null,null,'Z1141040183','     ','  ','應收票據','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (12,'11480',null,null,null,'Z1148000000','     ','  ','應收退稅款','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (13,'11780','04216','01',null,'Z1178004216','     ','01','其他應收款','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (14,'11780','04216','02',null,'Z1178004216','     ','02','其他應收款','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (15,'11780','15217',null,null,'Z1178015217','     ','  ','其他應收款','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (16,'11780',null,null,null,'Z1178000000','     ','  ','其他應收款','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (17,'13220',null,null,null,'Z1322000000','     ','  ','短期擔保放款','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (18,'13410',null,null,null,'Z1341000000','     ','  ','中期擔保放款','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (19,'13610','00012',null,null,'Z1361000012','     ','  ','長期擔保放款-一般件','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (20,'13610','00048',null,null,'Z1361000048','     ','  ','長期擔保放款-無自用住宅放款','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (21,'14480','04411','01',null,'Z1448004411','     ','01','不動產投資','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (22,'14480','14522',null,'預付房地款','10711040000','     ','  ','預付房地款(投資用)','現有總帳科目未使用','總帳有設放款目前沒用到，應用[使用記號]區別');
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (23,'18220','31995','01',null,'Z1822031995','     ','01','催收款項','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (24,'18220',null,null,null,'Z1822000000','     ','  ','催收款項','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (25,'18230','11232','01',null,'Z1823011232','     ','  ','備抵呆帳－催收款項','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (26,'18230','13231',null,'備呆－一般催收','Z1823013231','     ','  ','備抵呆帳－催收款項','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (27,'18230',null,null,null,'Z1823000000','     ','  ','備抵呆帳－催收款項　','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (28,'18240',null,'01',null,'Z1824000000','     ','01','承受擔保品','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (29,'18240',null,'05',null,'Z1824000000','     ','05','承受擔保品','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (30,'18240',null,null,null,'Z1824000000','     ','  ','承受擔保品','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (31,'18250','10123',null,null,'Z1825010123','     ','  ','暫付及待結轉帳項','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (32,'18250','30217','01',null,'Z1825030217','     ','01','暫付及待結轉帳項','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (33,'19010','80189','01',null,'Z1901080189','     ','01','內部往來','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (34,'19010','80189','06',null,'Z1901080189','     ','06','內部往來','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (35,'19010','80189',null,'跨區抽票','C1901080189','     ','  ','內部往來','舊科目(8碼會科10990001)',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (36,'21450','31475','01',null,'Z2145031475','     ','01','應付代收款','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (37,'21780','15217',null,'放款部','Z2178015217','     ','  ','其他應付款','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (38,'21780','30217','01',null,'Z2178030217','     ','01','其他應付款','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (39,'22140','81456','01',null,'Z2214081456','     ','01','銷項稅額','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (40,'28250','00954','01',null,'Z2825000954','     ','01','暫收及待結轉帳項－擔保放款　　　　　','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (41,'28250','01285',null,'擔保放款－站前','Z2825001285','     ','  ','暫收及待結轉帳項－擔保放款　　　　　','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (42,'28250','04216','00',null,'Z2825004216','     ','00','暫收及待結轉帳項－擔保放款　　　　　','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (43,'28250','04216','02',null,'Z2825004216','     ','02','暫收及待結轉帳項－擔保放款　　　　　','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (44,'28250','04216','03',null,'Z2825004216','     ','03','暫收及待結轉帳項－擔保放款　　　　　','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (45,'28250','04216','06',null,'Z2825004216','     ','06','暫收及待結轉帳項－擔保放款　　　　　','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (46,'28250','04216','15',null,'Z2825004216','     ','15','暫收及待結轉帳項－擔保放款　　　　　','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (47,'28250','60113','01',null,'Z2825060113','     ','01','暫收及待結轉帳項－擔保放款　　　　　','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (48,'28250','71242','09',null,'Z2825071242','     ','09','暫收及待結轉帳項－擔保放款　　　　　','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (49,'45010','31466','01',null,'Z4501031466','     ','01','利息收入－中期擔保放款息　　　','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (50,'45010','34458','12',null,'Z4501034458','     ','12','利息收入－長期擔保放款息　　','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (51,'45010','41474','01',null,'Z4501041474','     ','01','利息收入－九二一貸款戶　　　','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (52,'45010','41474','04',null,'Z4501041474','     ','04','利息收入－九二一貸款戶　　　','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (53,'45010','41486','01',null,'Z4501041486','     ','01','利息收入-３２００億專案息','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (54,'45010','77357','01',null,'Z4501077357','     ','01','利息收入-３２００億專案息(利變A)','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (55,'45160','00243','01',null,'Z4516000243','     ','01','手續費收入－放款帳管費　','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (56,'45160','243  ',null,null,'40903030000','     ','01','手續費-放款帳管費',null,'11筆資料皆為20040326入帳之帳管費，轉為正確科目');
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (57,'49140','10100',null,null,'Z4914010100','     ','  ','收回呆帳及過期帳','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (58,'49140',null,'01',null,'Z4914000000','     ','01','收回呆帳及過期帳','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (59,'49140',null,null,null,'Z4914000000','     ','  ','收回呆帳及過期帳','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (60,'49290','70212','01',null,'Z4929070212','     ','01','什項收入－其他　','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (61,'49290',null,null,null,'Z4929000000','     ','  ','什項收入－其他　','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (62,'501H0','10100',null,null,'Z501H010100','     ','  ','管理費用－放款部','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (63,'501H0','28229',null,'一般手續費支出','C501H028229','     ','  ','手續費支出　　　　　　　　　　　　　　','舊科目(8碼會科50903000)',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (64,'501H0','29260','01',null,'Z501H029260','     ','01','管理費用－放款部-其他勞務費','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (65,'55160','24104',null,null,'Z5516024104','     ','  ','手續費支出','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (66,'58111','11438','01',null,'Z5811111438','     ','01','業務費用（新）','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (67,'58111','11438',null,null,'Z5811111438','     ','  ','業務費用（新）','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (68,'58210','10100',null,null,'Z5821010100','     ','  ','管理費用','舊科目',null);
Insert into "TfActblMapping" ("Pk",ACNACC,ACNACS,ACNASS,LCDDTN,"AcNoCode","AcSubCode","AcDtlCode","AcItem","AcDesc","Remark") values (69,'58210','28229',null,null,'Z5821028229','     ','  ','管理費用','舊科目',null);
--------------------------------------------------------
--  DDL for Index TfActblMapping_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "TfActblMapping_PK" ON "TfActblMapping" ("Pk") 
  ;
--------------------------------------------------------
--  Constraints for Table TfActblMapping
--------------------------------------------------------

  ALTER TABLE "TfActblMapping" MODIFY ("Pk" NOT NULL ENABLE);
  ALTER TABLE "TfActblMapping" ADD CONSTRAINT "TfActblMapping_PK" PRIMARY KEY ("Pk")
  USING INDEX "TfActblMapping_PK"  ENABLE;
