alter table "PostAuthLog" add "ProcessDateTime" timestamp;
comment on column "PostAuthLog"."ProcessDate" is '處理日期時間';

alter table "AchAuthLog" add "ProcessDateTime" timestamp;
comment on column "AchAuthLog"."ProcessDate" is '處理日期時間';