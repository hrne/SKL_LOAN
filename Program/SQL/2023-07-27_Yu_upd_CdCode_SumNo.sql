INSERT INTO "CdCode" ("DefCode","DefType","Code","Item","Enable","EffectFlag","MinCodeLength","MaxCodeLength") 
VALUES ('SumNo','6','203','單筆匯款','Y',0,0,0);
	UPDATE "CdCode"
    SET "Item" = '核心匯款' 
    where "DefCode" = 'SumNo' and "Code" = '201';
    	UPDATE "CdCode"
    SET "Item" = '整批匯款' 
    where "DefCode" = 'SumNo' and "Code" = '202' ;