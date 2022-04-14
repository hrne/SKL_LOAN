ALTER TABLE "AchAuthLogHistory" Add "LimitAmt2" decimal(10, 2) default 0 not null;

update "AchAuthLogHistory" set "LimitAmt2" = "LimitAmt";

ALTER TABLE "AchAuthLogHistory" drop column "LimitAmt";

ALTER TABLE "AchAuthLogHistory" rename column "LimitAmt2" to "LimitAmt";

comment on column "AchAuthLogHistory"."LimitAmt" is '¨Cµ§¦©´Ú­­ÃB';