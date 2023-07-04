-- 2023-03-24-Mata_insertTxTranCode
 DELETE from "TxTranCode" where "TranNo" = 'L6046';
 insert into "TxTranCode"
 VALUES('L6046','客戶申請－ID替換對照表','客戶申請－ID替換對照表',1,0,0,'L6','6',0,0,'24-3月 -23 03.26.47.219000000 下午','001719','24-3月 -23 03.26.47.219000000 下午','001719',0,0,0,'請從L6045進入',1,'');
-- 2023-5-10_Mata_insertTxTranCodeTxAuthority
 UPDATE "TxTranCode" SET "TypeFg" = 1 where "TranNo" = 'L8942';
 UPDATE "TxTranCode" SET "TypeFg" = 1 where "TranNo" = 'L8943';
-- 2023-05-11_Yu_upd_TranCode_L4202_L4203_ChainTranMsg
 UPDATE "TxTranCode"
 SET "ChainTranMsg" = '此為連動交易，請從交易：[L4002整批入帳作業]->[L4920整批入帳明細查詢]進入'
 where "TranNo" = 'L4202';	
 UPDATE "TxTranCode"
 SET "ChainTranMsg" = '此為連動交易，請從交易：[L4002整批入帳作業]->[L4920整批入帳明細查詢]進入'
 where "TranNo" = 'L4203';    
-- 2023-05-31_Yu_upd_TxTranCode_L4101_CancelFg
 UPDATE "TxTranCode"
 SET "CancelFg" = 1
 where "TranNo" = 'L4101';
-- 2023-06-08_Yu_Upd_TxTranCode_L2153
 UPDATE "TxTranCode"
 SET "CancelFg" = 0 , "ModifyFg" = 0
 where "TranNo" = 'L2153';
-- 2023-06-19_Yu_更新交易檔L3731名稱呆帳戶戶況維護
 UPDATE "TxTranCode" SET "TranItem" = '呆帳戶戶況維護' , "Desc" = '呆帳戶戶況維護' WHERE  "TranNo" = 'L3731';
-- 2023-06-20_Yu_更新L2917交易名稱
 UPDATE "TxTranCode" SET "TranItem" = '不動產建物土地關聯維護' , "Desc" = '不動產建物土地關聯維護' WHERE  "TranNo" = 'L2917';
--
 UPDATE "TxTranCode" SET "CancelFg" = 0 , "ModifyFg" = 0  where "TranNo" = 'L5814';
--
 UPDATE "TxTranCode" SET "ChainTranMsg" = '此為連動交易，請從交易:[L9803月報]進入' where "TranNo" = 'LM073';	
 UPDATE "TxTranCode" SET "ChainTranMsg" = '此為連動交易，請從交易:[L9803月報]進入' where "TranNo" = 'LM074';	
 UPDATE "TxTranCode" SET "ChainTranMsg" = '此為連動交易，請從交易:[L9803月報]進入' where "TranNo" = 'LM075';	
 UPDATE "TxTranCode" SET "ChainTranMsg" = '此為連動交易，請從交易:[L9803月報]進入' where "TranNo" = 'LM076';	
 UPDATE "TxTranCode" SET "ChainTranMsg" = '此為連動交易，請從交易:[L9803月報]進入' where "TranNo" = 'LM077';	
 UPDATE "TxTranCode" SET "ChainTranMsg" = '此為連動交易，請從交易:[L9803月報]進入' where "TranNo" = 'LM078';	
 UPDATE "TxTranCode" SET "ChainTranMsg" = '此為連動交易，請從交易:[L9803月報]進入' where "TranNo" = 'LM079';	
 UPDATE "TxTranCode" SET "ChainTranMsg" = '此為連動交易，請從交易:[L9803月報]進入' where "TranNo" = 'LM080';	
 UPDATE "TxTranCode" SET "ChainTranMsg" = '此為連動交易，請從交易:[L9803月報]進入' where "TranNo" = 'LM081';	
 UPDATE "TxTranCode" SET "ChainTranMsg" = '此為連動交易，請從交易:[L9803月報]進入' where "TranNo" = 'LM082';	
 UPDATE "TxTranCode" SET "ChainTranMsg" = '此為連動交易，請從交易:[L9804季報]進入' where "TranNo" = 'LQ006';	
