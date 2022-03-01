ALTER TABLE "AcReceivable" ADD "OpenTxCd"  VARCHAR2(5) ; -- 起帳交易代號 
ALTER TABLE "AcReceivable" ADD "OpenKinBr" VARCHAR2(4) ; -- 起帳單位別   
ALTER TABLE "AcReceivable" ADD "OpenTlrNo" VARCHAR2(6) ; -- 起帳經辦     
ALTER TABLE "AcReceivable" ADD "OpenTxtNo" DECIMAL(8) DEFAULT 0 ; -- 起帳交易序號 
