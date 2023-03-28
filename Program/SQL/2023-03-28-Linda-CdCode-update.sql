UPDATE "CdCode" SET "Item" = '借新還舊'  WHERE "DefCode"='BatchRepayCode' and "Code"='91' ;
REM INSERTING into "CdCode"
SET DEFINE OFF;
Insert into "CdCode" ("DefCode","DefType","Code","Item","Enable","EffectFlag","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo","MinCodeLength","MaxCodeLength") values ('BatchRepayCode',4,'95','展期','Y',0,to_timestamp('29-12月-20 06.50.17.762000000 下午','DD-MON-RR HH.MI.SSXFF AM'),'999999',to_timestamp('29-12月-20 06.50.17.762000000 下午','DD-MON-RR HH.MI.SSXFF AM'),'999999',0,0);

UPDATE "CdCode" SET "Item" = '月'  WHERE "DefCode"='FreqBase2' and "Code"='2' ;

