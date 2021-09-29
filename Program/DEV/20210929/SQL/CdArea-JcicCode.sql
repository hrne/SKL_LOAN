alter table "CdArea" add ("JcicCityCode" varchar2(1));

alter table "CdArea" add ("JcicAreaCode" varchar2(2));

comment on column "CdArea"."JcicCityCode" is 'JCIC縣市碼';
comment on column "CdArea"."JcicAreaCode" is 'JCIC鄉鎮碼';

