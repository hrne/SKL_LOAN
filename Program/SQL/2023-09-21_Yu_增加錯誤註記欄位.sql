ALTER TABLE "CustMain" ADD "CustIdErrFg" varchar2(1) NULL;
ALTER TABLE "CustMain" ADD "SpouseIdErrFg" varchar2(1) NULL;
comment on column "CustMain"."CustIdErrFg" is '身份證字號/統一編號錯誤註記';
comment on column "CustMain"."SpouseIdErrFg" is '配偶身份證號/負責人身分證錯誤註記';