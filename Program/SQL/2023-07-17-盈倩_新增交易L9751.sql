--Insert CdReport: �s�W������
Insert into C##ITXADMIN."CdReport" ("FormNo","FormName","Cycle","SendCode","Letter","Message","Email","UsageDesc","Enable","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo","SignCode","WatermarkFlag","MessageFg","EmailFg","LetterFg","Confidentiality","ApLogFlag","GroupNo") 
values ('L9751','�Ȧ���-���I�O�l�B��',7,0,0,0,0,'�Ȧ���-���I�O�l�B��','Y',sysdate,'FC1627',sysdate,'FC1627',0,1,null,null,null,'1',0,'3');

--Insert TxTranCode: �s�W���
Insert into C##ITXADMIN."TxTranCode" ("TranNo","TranItem","Desc","TypeFg","CancelFg","ModifyFg","MenuNo","SubMenuNo","MenuFg","Status","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo","SubmitFg","CustDataCtrlFg","CustRmkFg","ChainTranMsg","ApLogFlag","ApLogRim") values 
('L9751','�Ȧ���-���I�O�l�B��','�Ȧ���-���I�O�l�B��',2,0,0,'L9','2',1,0,sysdate,'FC1627',sysdate,'FC1627',0,0,0,null,0,null);

--Insert TxAuthority: �s�W�s�ե���v��
Insert into C##ITXADMIN."TxAuthority" ("AuthNo","TranNo","AuthFg","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('0000T1','L9751',2,sysdate,'FC1627',sysdate,'FC1627');
Insert into C##ITXADMIN."TxAuthority" ("AuthNo","TranNo","AuthFg","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('0000T2','L9751',2,sysdate,'FC1627',sysdate,'FC1627');
Insert into C##ITXADMIN."TxAuthority" ("AuthNo","TranNo","AuthFg","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('0000T3','L9751',2,sysdate,'FC1627',sysdate,'FC1627');
Insert into C##ITXADMIN."TxAuthority" ("AuthNo","TranNo","AuthFg","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('0000T5','L9751',2,sysdate,'FC1627',sysdate,'FC1627');
Insert into C##ITXADMIN."TxAuthority" ("AuthNo","TranNo","AuthFg","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('0000S1','L9751',2,sysdate,'FC1627',sysdate,'FC1627'); 
Insert into C##ITXADMIN."TxAuthority" ("AuthNo","TranNo","AuthFg","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('0000NE','L9751',2,sysdate,'FC1627',sysdate,'FC1627');

