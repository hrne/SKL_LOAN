ALTER TABLE "MlaundryDetail" ADD "FlEntdy" DECIMAL(8) ;
comment on column "MlaundryDetail"."FlEntdy" is '�y�{����b�Ȥ�';
ALTER TABLE "MlaundryDetail" ADD "FlowNo" VARCHAR2(18) ;
comment on column "MlaundryDetail"."FlowNo" is '�y�{����Ǹ�';
UPDATE "MlaundryDetail" SET "FlEntdy" = 0 ;
