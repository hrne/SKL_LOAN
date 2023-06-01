alter table "InnFundApl" add "AvailableFunds" decimal(16, 2) default 0 not null;
comment on column "InnFundApl"."AvailableFunds" is '可運用資金';