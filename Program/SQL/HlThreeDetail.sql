drop table "HlThreeDetail" purge;

create table "HlThreeDetail" (
  "BrNo" varchar2(4),
  "CustNo" decimal(7, 0) default 0 not null,
  "FacmNo" decimal(3, 0) default 0 not null,
  "ActAmt" decimal(14, 2) default 0 not null,
  "PieceCode" varchar2(1),
  "CntingCode" varchar2(1),
  "TActAmt" decimal(14, 2) default 0 not null,
  "EmpNo" varchar2(6),
  "EmpId" varchar2(10),
  "EmpName" nvarchar2(15),
  "DeptCode" varchar2(6),
  "DistCode" varchar2(6),
  "UnitCode" varchar2(6),
  "DeptName" nvarchar2(20),
  "DistName" nvarchar2(20),
  "UnitName" nvarchar2(20),
  "FirAppDate" decimal(8, 0) default 0 not null,
  "BiReteNo" varchar2(2),
  "TwoYag" decimal(14, 2) default 0 not null,
  "ThreeYag" decimal(14, 2) default 0 not null,
  "TwoPay" decimal(14, 2) default 0 not null,
  "ThreePay" decimal(14, 2) default 0 not null,
  "UnitChiefNo" varchar2(10),
  "UnitChiefName" nvarchar2(15),
  "AreaChiefNo" varchar2(10),
  "AreaChiefName" nvarchar2(15),
  "Id3" varchar2(10),
  "Id3Name" nvarchar2(15),
  "TeamChiefNo" varchar2(10),
  "TeamChiefName" nvarchar2(15),
  "Id0" varchar2(10),
  "Id0Name" nvarchar2(15),
  "UpNo" decimal(7, 0) default 0 not null,
  "CalDate" decimal(8, 0) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "HlThreeDetail" add constraint "HlThreeDetail_PK" primary key("BrNo", "CustNo", "FacmNo");

comment on table "HlThreeDetail" is '介紹人業績明細檔';
comment on column "HlThreeDetail"."BrNo" is '營業單位別';
comment on column "HlThreeDetail"."CustNo" is '借款人戶號';
comment on column "HlThreeDetail"."FacmNo" is '額度編號';
comment on column "HlThreeDetail"."ActAmt" is '已用額度';
comment on column "HlThreeDetail"."PieceCode" is '計件代碼';
comment on column "HlThreeDetail"."CntingCode" is '是否已計件';
comment on column "HlThreeDetail"."TActAmt" is '累計已用額度';
comment on column "HlThreeDetail"."EmpNo" is '員工代號(介紹人)';
comment on column "HlThreeDetail"."EmpId" is '統一編號(介紹人)';
comment on column "HlThreeDetail"."EmpName" is '員工姓名(介紹人)';
comment on column "HlThreeDetail"."DeptCode" is '部室代號';
comment on column "HlThreeDetail"."DistCode" is '區部代號';
comment on column "HlThreeDetail"."UnitCode" is '單位代號';
comment on column "HlThreeDetail"."DeptName" is '部室中文';
comment on column "HlThreeDetail"."DistName" is '區部中文';
comment on column "HlThreeDetail"."UnitName" is '單位中文';
comment on column "HlThreeDetail"."FirAppDate" is '首次撥款日';
comment on column "HlThreeDetail"."BiReteNo" is '基本利率代碼';
comment on column "HlThreeDetail"."TwoYag" is '二階換算業績';
comment on column "HlThreeDetail"."ThreeYag" is '三階換算業績';
comment on column "HlThreeDetail"."TwoPay" is '二階業務報酬';
comment on column "HlThreeDetail"."ThreePay" is '三階業務報酬';
comment on column "HlThreeDetail"."UnitChiefNo" is '統一編號(單位主管/處長)';
comment on column "HlThreeDetail"."UnitChiefName" is '員工姓名';
comment on column "HlThreeDetail"."AreaChiefNo" is '統一編號(主任)';
comment on column "HlThreeDetail"."AreaChiefName" is '員工姓名';
comment on column "HlThreeDetail"."Id3" is '統一編號(組長)';
comment on column "HlThreeDetail"."Id3Name" is '員工姓名';
comment on column "HlThreeDetail"."TeamChiefNo" is '統一編號(展業代表)';
comment on column "HlThreeDetail"."TeamChiefName" is '員工姓名';
comment on column "HlThreeDetail"."Id0" is '統一編號';
comment on column "HlThreeDetail"."Id0Name" is '員工姓名';
comment on column "HlThreeDetail"."UpNo" is 'UpdateIdentifier';
comment on column "HlThreeDetail"."CalDate" is '更新日期';
comment on column "HlThreeDetail"."CreateDate" is '建檔日期時間';
comment on column "HlThreeDetail"."CreateEmpNo" is '建檔人員';
comment on column "HlThreeDetail"."LastUpdate" is '最後更新日期時間';
comment on column "HlThreeDetail"."LastUpdateEmpNo" is '最後更新人員';
