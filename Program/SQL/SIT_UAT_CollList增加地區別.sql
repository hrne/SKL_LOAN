alter table "CollList" add "CityCode" VARCHAR(2);
commit;


comment on column "CollList"."CityCode" is '擔保品地區別';
commit;