ALTER TABLE "BatxOthers" ADD "TitaEntdy" decimal(8, 0) default 0 not null ;
ALTER TABLE "BatxOthers" ADD "TitaTlrNo" varchar2(6) NULL;
ALTER TABLE "BatxOthers" ADD "TitaTxtNo" varchar2(8) NULL;
comment on column "BatxOthers"."TitaEntdy" is '作帳會計日';
comment on column "BatxOthers"."TitaTlrNo" is '經辦';
comment on column "BatxOthers"."TitaTxtNo" is '交易序號';