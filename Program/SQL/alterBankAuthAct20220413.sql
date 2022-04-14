ALTER TABLE "BankAuthAct" Add "LimitAmt2" decimal(10, 2) default 0 not null;

update "BankAuthAct" set "LimitAmt2" = "LimitAmt";

ALTER TABLE "BankAuthAct" drop column "LimitAmt";

ALTER TABLE "BankAuthAct" rename column "LimitAmt2" to "LimitAmt";

comment on column "BankAuthAct"."LimitAmt" is '¨Cµ§¦©´Ú­­ÃB';