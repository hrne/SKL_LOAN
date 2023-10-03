ALTER TABLE "AchDeductMedia" ADD  "RelCustIdErrFg" varchar2(1);
comment on column "AchDeductMedia"."RelCustIdErrFg" is '身份證字號/統一編號錯誤註記';
ALTER TABLE "BankDeductDtl" ADD  "RelCustIdErrFg" varchar2(1);
comment on column "BankDeductDtl"."RelCustIdErrFg" is '身份證字號/統一編號錯誤註記';
ALTER TABLE "BankDeductDtl" ADD  "BatchNo" varchar2(6);
comment on column "BankDeductDtl"."BatchNo" is '批號';
ALTER TABLE "BankDeductDtl" ADD  "DepCode" varchar2(2);
comment on column "BankDeductDtl"."DepCode" is '存摺代號';
ALTER TABLE "PostDeductMedia" ADD  "RelCustIdErrFg" varchar2(1);
comment on column "PostDeductMedia"."RelCustIdErrFg" is '身份證字號/統一編號錯誤註記';
ALTER TABLE "PostDeductMedia" ADD  "DepCode" varchar2(2);
comment on column "PostDeductMedia"."DepCode" is '存摺代號';