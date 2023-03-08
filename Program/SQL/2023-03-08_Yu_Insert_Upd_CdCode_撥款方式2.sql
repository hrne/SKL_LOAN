INSERT INTO "CdCode" ("DefCode","DefType","Code","Item","Enable","EffectFlag","MinCodeLength","MaxCodeLength") VALUES ('DrawdownCode2','4','03','單筆匯款','Y',0,0,0);
UPDATE "CdCode" SET "Item" ='核心匯款' WHERE "DefCode" = 'DrawdownCode2' AND "Code" = '01';
UPDATE "CdCode" SET "Item" ='整批匯款' WHERE "DefCode" = 'DrawdownCode2' AND "Code" = '02';
