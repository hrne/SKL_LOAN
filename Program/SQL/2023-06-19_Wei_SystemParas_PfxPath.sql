ALTER TABLE "SystemParas" ADD "PfxPath" varchar2(100);
ALTER TABLE "SystemParas" ADD "PfxAuth" varchar2(100);
comment on column "SystemParas"."PfxPath" is '憑證路徑';
comment on column "SystemParas"."PfxAuth" is '憑證認證';