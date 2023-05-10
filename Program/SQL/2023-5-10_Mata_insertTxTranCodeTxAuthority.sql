REM INSERTING into "TxTranCode"
SET DEFINE OFF;
Insert into "TxTranCode" ("TranNo","TranItem","Desc","TypeFg","CancelFg","ModifyFg","MenuNo","SubMenuNo","MenuFg","Status","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo","SubmitFg","CustDataCtrlFg","CustRmkFg","ChainTranMsg","ApLogFlag","ApLogRim") 
values ('L8942','聯徵報送結果查詢','聯徵報送結果查詢',1,0,0,'L8','3',1,0,to_timestamp('28-4月 -23 02.45.59.955000000 下午','DD-MON-RR HH.MI.SSXFF AM'),'001719',to_timestamp('28-4月 -23 02.45.59.955000000 下午','DD-MON-RR HH.MI.SSXFF AM'),'001719',0,0,0,null,0,null);
commit;
SET DEFINE OFF;
Insert into "TxTranCode" ("TranNo","TranItem","Desc","TypeFg","CancelFg","ModifyFg","MenuNo","SubMenuNo","MenuFg","Status","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo","SubmitFg","CustDataCtrlFg","CustRmkFg","ChainTranMsg","ApLogFlag","ApLogRim") 
values ('L8943','聯徵未回檔查詢','聯徵未回檔查詢',1,0,0,'L8','3',1,0,to_timestamp('28-4月 -23 02.45.59.955000000 下午','DD-MON-RR HH.MI.SSXFF AM'),'001719',to_timestamp('28-4月 -23 02.45.59.955000000 下午','DD-MON-RR HH.MI.SSXFF AM'),'001719',0,0,0,null,0,null);
commit;

REM INSERTING into "TxAuthority"
SET DEFINE OFF;
Insert into "TxAuthority" ("AuthNo","TranNo","AuthFg","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") 
values ('0000T1','L8942',2,to_date('01-5月 -23','DD-MON-RR'),'001719',to_date('01-5月 -23','DD-MON-RR'),'001719');
commit;
SET DEFINE OFF;
Insert into "TxAuthority" ("AuthNo","TranNo","AuthFg","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") 
values ('0000T1','L8943',2,to_date('01-5月 -23','DD-MON-RR'),'001719',to_date('01-5月 -23','DD-MON-RR'),'001719');
commit;


