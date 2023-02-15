
ALTER TABLE "SystemParas" ADD "ReduceAmtLimit1" DECIMAL(5,0) DEFAULT 0 NOT NULL ;
ALTER TABLE "SystemParas" ADD "ReduceAmtLimit2" DECIMAL(5,0) DEFAULT 0 NOT NULL ;
ALTER TABLE "SystemParas" ADD "ReduceAmtLimit3" DECIMAL(5,0) DEFAULT 0 NOT NULL ;
ALTER TABLE "SystemParas" ADD "CoreRemitLimit" DECIMAL(16,2) DEFAULT 0 NOT NULL ;
comment on column "SystemParas"."ReduceAmtLimit1" is '經辦減免限額';
comment on column "SystemParas"."ReduceAmtLimit2" is '課主管減免限額';
comment on column "SystemParas"."ReduceAmtLimit3" is '部主管減免限額';
comment on column "SystemParas"."CoreRemitLimit" is '核心匯款金額上限';