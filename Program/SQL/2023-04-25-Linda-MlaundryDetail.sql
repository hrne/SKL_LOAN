comment on column "MlaundryDetail"."FlEntdy" is '流程控制帳務日';
comment on column "MlaundryDetail"."FlowNo" is '流程控制序號';
UPDATE "MlaundryDetail" SET "FlEntdy" = 0 WHERE NVL("FlEntdy",0)=0;

ALTER TABLE "MlaundryDetail" MODIFY "FlEntdy" decimal(8, 0) DEFAULT 0 NOT NULL  ;

