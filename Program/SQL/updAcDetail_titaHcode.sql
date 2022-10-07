
UPDATE "AcDetail" SET "TitaHCode" = '3' WHERE "EntAc" = '3';
UPDATE "AcDetail" SET "TitaHCode" = '4' WHERE "EntAc" = '2';
UPDATE "AcDetail" SET "TitaHCode" = '0' WHERE "EntAc" not in ('3','4');