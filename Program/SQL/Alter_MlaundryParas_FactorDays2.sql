
ALTER TABLE "MlaundryParas" ADD "FactorDays2" decimal(3, 0) default 0 not null;
comment on column "MlaundryParas"."FactorDays2" is '樣態二統計期間天數';
comment on column "MlaundryParas"."FactorDays" is '樣態一統計期間天數';
UPDATE "MlaundryParas" SET "FactorDays2" = 1 WHERE "BusinessType" = 'LN'

