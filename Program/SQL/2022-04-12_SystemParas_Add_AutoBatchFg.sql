ALTER TABLE "SystemParas" ADD "AutoBatchFg" VARCHAR2(1) ;

UPDATE "SystemParas" SET "AutoBatchFg" = 'N';

commit;