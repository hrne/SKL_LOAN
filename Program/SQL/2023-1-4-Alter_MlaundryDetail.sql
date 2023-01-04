ALTER TABLE "MlaundryDetail" ADD "FlEntdy" DECIMAL(8) ;
comment on column "MlaundryDetail"."FlEntdy" is '流程控制帳務日';
ALTER TABLE "MlaundryDetail" ADD "FlowNo" VARCHAR2(18) ;
comment on column "MlaundryDetail"."FlowNo" is '流程控制序號';
UPDATE "MlaundryDetail" SET "FlEntdy" = 0 ;
