ALTER TABLE "FacMain" ADD "ProdBreachFlag" varchar2(1) NULL;
    ALTER TABLE "FacMain" ADD "Breach" nvarchar2(100) NULL;
comment on column "FacMain"."ProdBreachFlag" is '違約適用方式是否按商品設定';
comment on column "FacMain"."Breach" is '違約適用說明';