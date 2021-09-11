drop table "InsuRenewMediaTemp" purge;

drop sequence "InsuRenewMediaTemp_SEQ";

create table "InsuRenewMediaTemp" (
  "LogNo" decimal(11,0) not null,
  "FireInsuMonth" nvarchar2(6),
  "ReturnCode" nvarchar2(2),
  "InsuCampCode" nvarchar2(2),
  "InsuCustId" nvarchar2(10),
  "InsuCustName" nvarchar2(12),
  "LoanCustId" nvarchar2(10),
  "LoanCustName" nvarchar2(12),
  "PostalCode" nvarchar2(5),
  "Address" nvarchar2(58),
  "BuildingSquare" nvarchar2(9),
  "BuildingCode" nvarchar2(2),
  "BuildingYears" nvarchar2(3),
  "BuildingFloors" nvarchar2(2),
  "RoofCode" nvarchar2(2),
  "BusinessUnit" nvarchar2(4),
  "ClCode1" nvarchar2(1),
  "ClCode2" nvarchar2(2),
  "ClNo" nvarchar2(7),
  "Seq" nvarchar2(2),
  "InsuNo" nvarchar2(16),
  "InsuStartDate" nvarchar2(10),
  "InsuEndDate" nvarchar2(10),
  "FireInsuAmt" nvarchar2(11),
  "FireInsuFee" nvarchar2(7),
  "EqInsuAmt" nvarchar2(7),
  "EqInsuFee" nvarchar2(6),
  "CustNo" nvarchar2(7),
  "FacmNo" nvarchar2(3),
  "Space" nvarchar2(4),
  "SendDate" nvarchar2(14),
  "NewInusNo" nvarchar2(16),
  "NewInsuStartDate" nvarchar2(10),
  "NewInsuEndDate" nvarchar2(10),
  "NewFireInsuAmt" nvarchar2(11),
  "NewFireInsuFee" nvarchar2(7),
  "NewEqInsuAmt" nvarchar2(8),
  "NewEqInsuFee" nvarchar2(6),
  "NewTotalFee" nvarchar2(7),
  "Remark1" nvarchar2(16),
  "MailingAddress" nvarchar2(60),
  "Remark2" nvarchar2(39),
  "SklSalesName" nvarchar2(20),
  "SklUnitCode" nvarchar2(6),
  "SklUnitName" nvarchar2(20),
  "SklSalesCode" nvarchar2(6),
  "RenewTrlCode" nvarchar2(8),
  "RenewUnit" nvarchar2(7),
  "CheckResultA" nvarchar2(30),
  "CheckResultB" nvarchar2(30),
  "CheckResultC" nvarchar2(30),
  "RepayCode" decimal(1, 0) default 0 not null,
  "NoticeFlag" decimal(1, 0) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "InsuRenewMediaTemp" add constraint "InsuRenewMediaTemp_PK" primary key("LogNo");

create sequence "InsuRenewMediaTemp_SEQ" minvalue 1 maxvalue 2147483647 increment by 1 start with 1 nocache cycle;

comment on table "InsuRenewMediaTemp" is '火險詢價媒體檔';
comment on column "InsuRenewMediaTemp"."LogNo" is '序號';
comment on column "InsuRenewMediaTemp"."FireInsuMonth" is '火險到期年月';
comment on column "InsuRenewMediaTemp"."ReturnCode" is '回傳碼';
comment on column "InsuRenewMediaTemp"."InsuCampCode" is '保險公司代碼';
comment on column "InsuRenewMediaTemp"."InsuCustId" is '提供人統一編號';
comment on column "InsuRenewMediaTemp"."InsuCustName" is '提供人姓名';
comment on column "InsuRenewMediaTemp"."LoanCustId" is '借款人統一編號';
comment on column "InsuRenewMediaTemp"."LoanCustName" is '借款人姓名';
comment on column "InsuRenewMediaTemp"."PostalCode" is '郵遞區號';
comment on column "InsuRenewMediaTemp"."Address" is '門牌號碼';
comment on column "InsuRenewMediaTemp"."BuildingSquare" is '主建物坪數';
comment on column "InsuRenewMediaTemp"."BuildingCode" is '建物結構代碼';
comment on column "InsuRenewMediaTemp"."BuildingYears" is '建造年份';
comment on column "InsuRenewMediaTemp"."BuildingFloors" is '樓層數';
comment on column "InsuRenewMediaTemp"."RoofCode" is '屋頂結構代碼';
comment on column "InsuRenewMediaTemp"."BusinessUnit" is '營業單位別';
comment on column "InsuRenewMediaTemp"."ClCode1" is '押品別１';
comment on column "InsuRenewMediaTemp"."ClCode2" is '押品別２';
comment on column "InsuRenewMediaTemp"."ClNo" is '押品號碼';
comment on column "InsuRenewMediaTemp"."Seq" is '序號';
comment on column "InsuRenewMediaTemp"."InsuNo" is '保單號碼';
comment on column "InsuRenewMediaTemp"."InsuStartDate" is '保險起日';
comment on column "InsuRenewMediaTemp"."InsuEndDate" is '保險迄日';
comment on column "InsuRenewMediaTemp"."FireInsuAmt" is '火險保額';
comment on column "InsuRenewMediaTemp"."FireInsuFee" is '火險保費';
comment on column "InsuRenewMediaTemp"."EqInsuAmt" is '地震險保額';
comment on column "InsuRenewMediaTemp"."EqInsuFee" is '地震險保費';
comment on column "InsuRenewMediaTemp"."CustNo" is '借款人戶號';
comment on column "InsuRenewMediaTemp"."FacmNo" is '額度編號';
comment on column "InsuRenewMediaTemp"."Space" is '空白';
comment on column "InsuRenewMediaTemp"."SendDate" is '傳檔日期';
comment on column "InsuRenewMediaTemp"."NewInusNo" is '保單號碼(新)';
comment on column "InsuRenewMediaTemp"."NewInsuStartDate" is '保險起日(新)';
comment on column "InsuRenewMediaTemp"."NewInsuEndDate" is '保險迄日(新)';
comment on column "InsuRenewMediaTemp"."NewFireInsuAmt" is '火險保額(新)';
comment on column "InsuRenewMediaTemp"."NewFireInsuFee" is '火險保費(新)';
comment on column "InsuRenewMediaTemp"."NewEqInsuAmt" is '地震險保額(新)';
comment on column "InsuRenewMediaTemp"."NewEqInsuFee" is '地震險保費(新)';
comment on column "InsuRenewMediaTemp"."NewTotalFee" is '總保費(新)';
comment on column "InsuRenewMediaTemp"."Remark1" is '備註一';
comment on column "InsuRenewMediaTemp"."MailingAddress" is '通訊地址';
comment on column "InsuRenewMediaTemp"."Remark2" is '備註二';
comment on column "InsuRenewMediaTemp"."SklSalesName" is '新光人壽業務員名稱';
comment on column "InsuRenewMediaTemp"."SklUnitCode" is '新光人壽單位代號';
comment on column "InsuRenewMediaTemp"."SklUnitName" is '新光人壽單位中文';
comment on column "InsuRenewMediaTemp"."SklSalesCode" is '新光人壽業務員代號';
comment on column "InsuRenewMediaTemp"."RenewTrlCode" is '新產續保經辦代號';
comment on column "InsuRenewMediaTemp"."RenewUnit" is '新產續保單位';
comment on column "InsuRenewMediaTemp"."CheckResultA" is '檢核結果A';
comment on column "InsuRenewMediaTemp"."CheckResultB" is '檢核結果B';
comment on column "InsuRenewMediaTemp"."CheckResultC" is '檢核結果C';
comment on column "InsuRenewMediaTemp"."RepayCode" is '繳款方式';
comment on column "InsuRenewMediaTemp"."NoticeFlag" is '通知方式';
comment on column "InsuRenewMediaTemp"."CreateDate" is '建檔日期時間';
comment on column "InsuRenewMediaTemp"."CreateEmpNo" is '建檔人員';
comment on column "InsuRenewMediaTemp"."LastUpdate" is '最後更新日期時間';
comment on column "InsuRenewMediaTemp"."LastUpdateEmpNo" is '最後更新人員';
