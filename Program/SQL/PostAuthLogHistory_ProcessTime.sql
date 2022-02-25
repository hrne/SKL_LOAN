alter table "AchAuthLogHistory" add ("ProcessTime" Decimal(6,0) default 0 null );
comment on column "AchAuthLogHistory"."ProcessTime" is '處理時間';

alter table "PostAuthLogHistory" add ("ProcessTime" Decimal(6,0) default 0 null );
comment on column "PostAuthLogHistory"."ProcessTime" is '處理時間';