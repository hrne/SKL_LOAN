--Insert CdReport: 新增報表資料
Insert into C##ITXADMIN."CdReport" ("FormNo","FormName","Cycle","SendCode","Letter","Message","Email","UsageDesc","Enable","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo","SignCode","WatermarkFlag","MessageFg","EmailFg","LetterFg","Confidentiality","ApLogFlag","GroupNo") 
values ('L9751','暫收款-火險費餘額表',7,0,0,0,0,'暫收款-火險費餘額表','Y',sysdate,'FC1627',sysdate,'FC1627',0,1,null,null,null,'1',0,'3');

--Insert TxTranCode: 新增交易
Insert into C##ITXADMIN."TxTranCode" ("TranNo","TranItem","Desc","TypeFg","CancelFg","ModifyFg","MenuNo","SubMenuNo","MenuFg","Status","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo","SubmitFg","CustDataCtrlFg","CustRmkFg","ChainTranMsg","ApLogFlag","ApLogRim") values 
('L9751','暫收款-火險費餘額表','暫收款-火險費餘額表',2,0,0,'L9','2',1,0,sysdate,'FC1627',sysdate,'FC1627',0,0,0,null,0,null);

--Insert TxAuthority: 新增群組交易權限
Insert into C##ITXADMIN."TxAuthority" ("AuthNo","TranNo","AuthFg","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('0000T1','L9751',2,sysdate,'FC1627',sysdate,'FC1627');
Insert into C##ITXADMIN."TxAuthority" ("AuthNo","TranNo","AuthFg","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('0000T2','L9751',2,sysdate,'FC1627',sysdate,'FC1627');
Insert into C##ITXADMIN."TxAuthority" ("AuthNo","TranNo","AuthFg","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('0000T3','L9751',2,sysdate,'FC1627',sysdate,'FC1627');
Insert into C##ITXADMIN."TxAuthority" ("AuthNo","TranNo","AuthFg","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('0000T5','L9751',2,sysdate,'FC1627',sysdate,'FC1627');
Insert into C##ITXADMIN."TxAuthority" ("AuthNo","TranNo","AuthFg","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('0000S1','L9751',2,sysdate,'FC1627',sysdate,'FC1627'); 
Insert into C##ITXADMIN."TxAuthority" ("AuthNo","TranNo","AuthFg","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('0000NE','L9751',2,sysdate,'FC1627',sysdate,'FC1627');

