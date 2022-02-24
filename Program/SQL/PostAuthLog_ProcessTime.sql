alter table "AchAuthLog" add ("ProcessTime" Decimal(6,0) default 0 null );
comment on column "AchAuthLog"."ProcessTime" is '處理時間';

alter table "PostAuthLog" add ("ProcessTime" Decimal(6,0) default 0 null );
comment on column "PostAuthLog"."ProcessTime" is '處理時間';