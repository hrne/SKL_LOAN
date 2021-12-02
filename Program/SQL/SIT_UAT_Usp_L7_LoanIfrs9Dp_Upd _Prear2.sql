-- 程式功能：清WK檔,FOR維護 LoanIfrs9Dp 每月IFRS9欄位清單D檔
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：由Usp_L7_LoanIfrs9Dp_Upd執行
--


-- Work_DP 回收金額
create or replace procedure "Usp_L7_LoanIfrs9Dp_Upd_Prear2" AUTHID CURRENT_USER as
    table_or_view_not_exist exception;
    pragma exception_init(table_or_view_not_exist, -942);
    attempted_ddl_on_in_use_GTT exception;
    pragma exception_init(attempted_ddl_on_in_use_GTT, -14452);
begin
    execute immediate 'drop table "Work_DP"';
    dbms_output.put_line('drop table Work_DP');
    execute immediate 'CREATE GLOBAL TEMPORARY TABLE "Work_DP" (
    		    "CustNo"          decimal(7, 0)   default 0 not null    -- 戶號
     			, "FacmNo"          decimal(3, 0)   default 0 not null    -- 額度編號
     			, "BormNo"          decimal(3, 0)   default 0 not null    -- 撥款序號
     			, "TotalLoanBal"    decimal(16, 2)  default 0 not null    -- 本金餘額合計
     			, "StoreRate"       decimal(8, 6)   default 0 not null    -- 減損發生日月底 計息利率
     			, "LoanBalance"     decimal(16, 2)  default 0 not null    -- 減損發生日月底 放款餘額
     			, "IntAmt"          decimal(16, 2)  default 0 not null    -- 減損發生日月底 應收利息
     			, "Fee"             decimal(16, 2)  default 0 not null    -- 減損發生日月底 費用 (火險+法務)
     			, "DerY1Amt"        decimal(16, 2)  default 0 not null    -- 個案減損客觀證據發生後第一年本金回收金額
     			, "DerY2Amt"        decimal(16, 2)  default 0 not null    -- 個案減損客觀證據發生後第二年本金回收金額
     			, "DerY3Amt"        decimal(16, 2)  default 0 not null    -- 個案減損客觀證據發生後第三年本金回收金額
     			, "DerY4Amt"        decimal(16, 2)  default 0 not null    -- 個案減損客觀證據發生後第四年本金回收金額
     			, "DerY5Amt"        decimal(16, 2)  default 0 not null    -- 個案減損客觀證據發生後第五年本金回收金額
     			, "DerY1Int"        decimal(16, 2)  default 0 not null    -- 個案減損客觀證據發生後第一年應收利息回收金額
     			, "DerY2Int"        decimal(16, 2)  default 0 not null    -- 個案減損客觀證據發生後第二年應收利息回收金額
     			, "DerY3Int"        decimal(16, 2)  default 0 not null    -- 個案減損客觀證據發生後第三年應收利息回收金額
     			, "DerY4Int"        decimal(16, 2)  default 0 not null    -- 個案減損客觀證據發生後第四年應收利息回收金額
     			, "DerY5Int"        decimal(16, 2)  default 0 not null    -- 個案減損客觀證據發生後第五年應收利息回收金額
     			, "DerY1Fee"        decimal(16, 2)  default 0 not null    -- 個案減損客觀證據發生後第一年法拍及火險費用回收金額
     			, "DerY2Fee"        decimal(16, 2)  default 0 not null    -- 個案減損客觀證據發生後第二年法拍及火險費用回收金額
     			, "DerY3Fee"        decimal(16, 2)  default 0 not null    -- 個案減損客觀證據發生後第三年法拍及火險費用回收金額
     			, "DerY4Fee"        decimal(16, 2)  default 0 not null    -- 個案減損客觀證據發生後第四年法拍及火險費用回收金額
     			, "DerY5Fee"        decimal(16, 2)  default 0 not null    -- 個案減損客觀證據發生後第五年法拍及火險費用回收金額
     	) ON COMMIT DELETE ROWS';
    dbms_output.put_line('create table Work_DP');
    exception 
        when table_or_view_not_exist then
            dbms_output.put_line('Table t did not exist at time of drop. Continuing....');
            execute immediate 'CREATE GLOBAL TEMPORARY TABLE "Work_DP" (
     						    "CustNo"          decimal(7, 0)   default 0 not null    -- 戶號
     							, "FacmNo"          decimal(3, 0)   default 0 not null    -- 額度編號
     							, "BormNo"          decimal(3, 0)   default 0 not null    -- 撥款序號
     							, "TotalLoanBal"    decimal(16, 2)  default 0 not null    -- 本金餘額合計
     							, "StoreRate"       decimal(8, 6)   default 0 not null    -- 減損發生日月底 計息利率
     							, "LoanBalance"     decimal(16, 2)  default 0 not null    -- 減損發生日月底 放款餘額
     							, "IntAmt"          decimal(16, 2)  default 0 not null    -- 減損發生日月底 應收利息
     							, "Fee"             decimal(16, 2)  default 0 not null    -- 減損發生日月底 費用 (火險+法務)
     							, "DerY1Amt"        decimal(16, 2)  default 0 not null    -- 個案減損客觀證據發生後第一年本金回收金額
     							, "DerY2Amt"        decimal(16, 2)  default 0 not null    -- 個案減損客觀證據發生後第二年本金回收金額
     							, "DerY3Amt"        decimal(16, 2)  default 0 not null    -- 個案減損客觀證據發生後第三年本金回收金額
     							, "DerY4Amt"        decimal(16, 2)  default 0 not null    -- 個案減損客觀證據發生後第四年本金回收金額
     							, "DerY5Amt"        decimal(16, 2)  default 0 not null    -- 個案減損客觀證據發生後第五年本金回收金額
     							, "DerY1Int"        decimal(16, 2)  default 0 not null    -- 個案減損客觀證據發生後第一年應收利息回收金額
     							, "DerY2Int"        decimal(16, 2)  default 0 not null    -- 個案減損客觀證據發生後第二年應收利息回收金額
     							, "DerY3Int"        decimal(16, 2)  default 0 not null    -- 個案減損客觀證據發生後第三年應收利息回收金額
     							, "DerY4Int"        decimal(16, 2)  default 0 not null    -- 個案減損客觀證據發生後第四年應收利息回收金額
     							, "DerY5Int"        decimal(16, 2)  default 0 not null    -- 個案減損客觀證據發生後第五年應收利息回收金額
     							, "DerY1Fee"        decimal(16, 2)  default 0 not null    -- 個案減損客觀證據發生後第一年法拍及火險費用回收金額
     							, "DerY2Fee"        decimal(16, 2)  default 0 not null    -- 個案減損客觀證據發生後第二年法拍及火險費用回收金額
     							, "DerY3Fee"        decimal(16, 2)  default 0 not null    -- 個案減損客觀證據發生後第三年法拍及火險費用回收金額
     							, "DerY4Fee"        decimal(16, 2)  default 0 not null    -- 個案減損客觀證據發生後第四年法拍及火險費用回收金額
     							, "DerY5Fee"        decimal(16, 2)  default 0 not null    -- 個案減損客觀證據發生後第五年法拍及火險費用回收金額
                         ) ON COMMIT DELETE ROWS';
            dbms_output.put_line('create table Work_DP');
        when attempted_ddl_on_in_use_GTT then
            dbms_output.put_line('Someone is keeping from doing my job!');
            dbms_output.put_line('Please rescue me');
            raise;
end;


