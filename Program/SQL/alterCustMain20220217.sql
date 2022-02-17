ALTER TABLE "CustMain" ADD "Station" VARCHAR2(3) NULL;
ALTER TABLE "CustMain" ADD "BusinessOfficer" VARCHAR2(6) NULL;
comment on column "CustMain"."Station" is '站別';
comment on column "CustMain"."BusinessOfficer" is '房貸專員/企金人員';
