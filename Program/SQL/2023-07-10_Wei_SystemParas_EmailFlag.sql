ALTER TABLE "SystemParas" ADD "EmailFlag" varchar2(1);
comment on column "SystemParas"."EmailFlag" is 'Email記號';
UPDATE "SystemParas" SET "EmailFlag" = 'T'; 