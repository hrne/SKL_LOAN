REM INSERTING into "CdCode"
SET DEFINE OFF;
Insert into "CdCode" ("DefCode","DefType","Code","Item","Enable","EffectFlag","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo","MinCodeLength","MaxCodeLength") values ('AcctCode',6,'T11','債協暫收款','Y',0,to_timestamp('25-10月-21 02.18.32.869584000 下午','DD-MON-RR HH.MI.SSXFF AM'),'999999',to_timestamp('10-2月 -22 09.36.18.088000000 上午','DD-MON-RR HH.MI.SSXFF AM'),'001715',3,3);
Insert into "CdCode" ("DefCode","DefType","Code","Item","Enable","EffectFlag","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo","MinCodeLength","MaxCodeLength") values ('AcctCode',6,'T12','前調暫收款','Y',0,to_timestamp('25-10月-21 02.18.32.869584000 下午','DD-MON-RR HH.MI.SSXFF AM'),'999999',to_timestamp('10-2月 -22 09.36.18.088000000 上午','DD-MON-RR HH.MI.SSXFF AM'),'001715',3,3);
Insert into "CdCode" ("DefCode","DefType","Code","Item","Enable","EffectFlag","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo","MinCodeLength","MaxCodeLength") values ('AcctCode',6,'T13','更生暫收款','Y',0,to_timestamp('25-10月-21 02.18.32.869584000 下午','DD-MON-RR HH.MI.SSXFF AM'),'999999',to_timestamp('10-2月 -22 09.36.18.088000000 上午','DD-MON-RR HH.MI.SSXFF AM'),'001715',3,3);
commit;