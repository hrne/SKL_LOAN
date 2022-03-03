ALTER TABLE "CdBuildingCost" ADD "VersionDate" DECIMAL(8);
UPDATE "CdBuildingCost" SET "VersionDate" = (SELECT "TbsDyf" FROM "TxBizDate" WHERE "DateCode" = 'ONLINE');