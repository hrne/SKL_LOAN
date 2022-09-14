ALTER TABLE "BankRmtf" ADD "ReconCode" VARCHAR2(3) null;
comment on column "BankRmtf"."ReconCode" is '對帳類別';
ALTER TABLE "BankRmtf" ADD "TitaTlrNo" VARCHAR2(6) null;
comment on column "BankRmtf"."TitaTlrNo" is '經辦';
ALTER TABLE "BankRmtf" ADD "TitaTxtNo" VARCHAR2(8) null;
comment on column "BankRmtf"."TitaTxtNo" is '交易序號';
