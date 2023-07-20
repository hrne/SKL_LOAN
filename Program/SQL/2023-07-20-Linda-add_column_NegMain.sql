ALTER TABLE "NegMain" ADD "NegCustId" VARCHAR2(10)  ;
comment on column "NegMain"."NegCustId" is '保證人/保貸戶ID';
ALTER TABLE "NegMain" ADD "NegCustName" NVARCHAR2(100)  ;
comment on column "NegMain"."NegCustName" is '保證人/保貸戶戶名';
comment on column "NegMain"."PayerCustNo" is '借款人戶號';
comment on column "NegMain"."TwoStepCode" is '階段註記';
