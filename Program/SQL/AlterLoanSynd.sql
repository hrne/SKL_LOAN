

ALTER TABLE "LoanSynd" ADD "SyndTypeCodeFlag" VARCHAR2(1) NULL;
comment on column "LoanSynd"."SyndTypeCodeFlag" is '國內或國際聯貸';