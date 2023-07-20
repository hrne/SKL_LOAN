Insert into "TxTranCode" ("TranNo","TranItem","Desc","TypeFg","CancelFg","ModifyFg","MenuNo","SubMenuNo","MenuFg","Status","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo","SubmitFg","CustDataCtrlFg","CustRmkFg","ChainTranMsg","ApLogFlag","ApLogRim") values ('L7074','總帳傳票資料-依會計日期','總帳傳票資料-依會計日期',1,0,0,'L7','4',1,0,to_timestamp('10-7月 -23 09.45.59.871000000 上午','DD-MON-RR HH.MI.SSXFF AM'),'001719',to_timestamp('10-7月 -23 09.45.59.871000000 上午','DD-MON-RR HH.MI.SSXFF AM'),'001719',0,0,0,null,0,null);
Insert into "TxTranCode" ("TranNo","TranItem","Desc","TypeFg","CancelFg","ModifyFg","MenuNo","SubMenuNo","MenuFg","Status","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo","SubmitFg","CustDataCtrlFg","CustRmkFg","ChainTranMsg","ApLogFlag","ApLogRim") values ('L7974','總帳傳票資料明細','總帳傳票資料明細',1,0,0,'L7','4',1,0,to_timestamp('11-7月 -23 02.17.16.444000000 下午','DD-MON-RR HH.MI.SSXFF AM'),'001719',to_timestamp('11-7月 -23 02.17.16.444000000 下午','DD-MON-RR HH.MI.SSXFF AM'),'001719',0,0,0,null,0,null);

Insert into "TxAuthority" ("AuthNo","TranNo","AuthFg","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('0000T1','L7074',2,to_date('11-7月 -23','DD-MON-RR'),'001719',to_date('11-7月 -23','DD-MON-RR'),'001719');
Insert into "TxAuthority" ("AuthNo","TranNo","AuthFg","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('0000T1','L7974',2,to_date('11-7月 -23','DD-MON-RR'),'001719',to_date('11-7月 -23','DD-MON-RR'),'001719');
