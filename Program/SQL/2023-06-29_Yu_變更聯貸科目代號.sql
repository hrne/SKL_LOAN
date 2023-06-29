UPDATE "CdCode" SET "Item" = '聯貸管理費' WHERE "DefCode" = 'AcctCode' AND "Code" = 'F12';
UPDATE "CdCode" SET "Item" = '企金帳管費' WHERE "DefCode" = 'AcctCode' AND "Code" = 'F27';
UPDATE "CdAcCode" SET "AcctCode" = 'F27' WHERE "AcctItem" = '企金帳管費收入';
UPDATE "CdAcCode" SET "AcctCode" = 'F12' WHERE "AcctItem" = '聯貸管理費收入';
UPDATE "CdSyndFee" SET "SyndFeeItem" = '聯貸管理費'  WHERE "SyndFeeCode" = '01';
UPDATE "CdSyndFee" SET "SyndFeeItem" = '企金帳管費'  WHERE "SyndFeeCode" = '02';
UPDATE "CdCode" SET "Item" = '沖聯貸管理費' WHERE "DefCode" = 'Temp2ItemCode' AND "Code" = '12';
UPDATE "CdCode" SET "Item" = '沖企金帳管費' WHERE "DefCode" = 'Temp2ItemCode' AND "Code" = '27';