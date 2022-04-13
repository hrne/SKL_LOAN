SET DEFINE OFF;
delete from "CdCode" where "DefCode" = 'InsuTypeCode';

Insert into "CdCode" ("DefCode","DefType","Code","Item","Enable","EffectFlag","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo","MinCodeLength","MaxCodeLength") values ('InsuTypeCode',4,'08','商業火災保險','Y',0,to_timestamp('2020-04-16 00:00:00.000000000','YYYY-MM-DD HH24:MI:SS.FF'),'999999',to_timestamp('2020-04-16 00:00:00.000000000','YYYY-MM-DD HH24:MI:SS.FF'),'999999',0,0);

Update "CdCode" set "Item" = '住宅火險(長單)' WHERE "DefCode" = 'InsuTypeCode' AND "DefType" = 4 AND "Code" = '02';
Update "CdCode" set "Enable" = 'N' WHERE "DefCode" = 'InsuTypeCode' AND "DefType" = 4 AND "Code" = '04';
Update "CdCode" set "Enable" = 'N' WHERE "DefCode" = 'InsuTypeCode' AND "DefType" = 4 AND "Code" = '05';