UPDATE "InsuRenew" set "CommericalFlag" = '01' WHERE "CommericalFlag" ='Y';
UPDATE "InsuOrignal" set "CommericalFlag" = '01' WHERE "CommericalFlag" ='Y';

ALTER TABLE "InsuRenew" MODIFY "CommericalFlag" VARCHAR2(2) ;
ALTER TABLE "InsuOrignal" MODIFY "CommericalFlag" VARCHAR2(2) ;