
alter table "CdBcm" add "Enable" varchar2(1)  ;

comment on COLUMN "CdBcm"."Enable" is '啟用記號';

update "CdBcm" set "Enable" = 'Y'
