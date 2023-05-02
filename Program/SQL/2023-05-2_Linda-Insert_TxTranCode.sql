REM INSERTING into "TxTranCode"
SET DEFINE OFF;
Insert into "TxTranCode" ("TranNo","TranItem","Desc","TypeFg","CancelFg","ModifyFg","MenuNo","SubMenuNo","MenuFg","Status","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo","SubmitFg","CustDataCtrlFg","CustRmkFg","ChainTranMsg","ApLogFlag","ApLogRim") values ('L9750','會計師查核','會計師查核Sql產出Execl',2,0,0,'L9','2',1,0,to_timestamp('28-4月 -23 02.45.59.955000000 下午','DD-MON-RR HH.MI.SSXFF AM'),'001703',to_timestamp('28-4月 -23 02.45.59.955000000 下午','DD-MON-RR HH.MI.SSXFF AM'),'001703',0,0,0,null,0,null);
commit;
REM INSERTING into "TxAuthority"
SET DEFINE OFF;
Insert into "TxAuthority" ("AuthNo","TranNo","AuthFg","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('0000T1','L9750',2,to_date('01-5月 -23','DD-MON-RR'),'001722',to_date('01-5月 -23','DD-MON-RR'),'001722');
commit;