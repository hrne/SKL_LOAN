
UPDATE "CdAcCode"
SET "AcctItem"='暫收款－暫收款－費用攤提' , "ClsChkFlag" = 2 ,"AcNoItem" ='暫收及待結轉帳項－擔保放款－費用攤提'
WHERE "AcctCode" = 'TSL';
INSERT INTO 
"CdAcCode" 
("AcNoCode","AcSubCode","AcDtlCode","AcNoItem","AcctCode","AcctItem","ClassCode","AcBookFlag","DbCr","AcctFlag","ReceivableFlag","ClsChkFlag","InuseFlag","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo","AcNoCodeOld") 
VALUES ('20222020000','     ','08','暫收及待結轉帳項－擔保放款－沖正','THC','暫收款－沖正','0','0','C','0','2','3','0','','999999','','999999','20232020');

