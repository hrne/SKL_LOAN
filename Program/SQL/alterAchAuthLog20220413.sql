ALTER TABLE "AchAuthLog" Add "LimitAmt2" decimal(10, 2) default 0 not null;

update "AchAuthLog" set "LimitAmt2" = "LimitAmt";

ALTER TABLE "AchAuthLog" drop column "LimitAmt";

ALTER TABLE "AchAuthLog" rename column "LimitAmt2" to "LimitAmt";

comment on column "AchAuthLog"."LimitAmt" is '¨Cµ§¦©´Ú­­ÃB';