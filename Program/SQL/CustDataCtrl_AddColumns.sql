alter table "CustDataCtrl" add "SetDate" DECIMAL(8);

comment on column "CustDataCtrl"."SetDate" is '設定日期';

alter table "CustDataCtrl" add "SetEmpNo" VARCHAR2(6);

comment on column "CustDataCtrl"."SetEmpNo" is '設定人員';

alter table "CustDataCtrl" add "ReSetDate" DECIMAL(8);

comment on column "CustDataCtrl"."ReSetDate" is '解除日期';

alter table "CustDataCtrl" add "ReSetEmpNo" VARCHAR2(6);