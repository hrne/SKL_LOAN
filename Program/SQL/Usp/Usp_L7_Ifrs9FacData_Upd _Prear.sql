-- 程式功能：清WK檔,FOR維護 Ifrs9FacData 每月IFRS9額度資料檔
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：由Usp_L7_Ifrs9FacData_Upd執行
--

-- 額度資料

create or replace procedure "Usp_L7_Ifrs9FacData_Upd_Prear" AUTHID CURRENT_USER as
    table_or_view_not_exist exception;
    pragma exception_init(table_or_view_not_exist, -942);
    attempted_ddl_on_in_use_GTT exception;
    pragma exception_init(attempted_ddl_on_in_use_GTT, -14452);
begin
    execute immediate 'drop table "Work_Ifrs9FacData"';
    dbms_output.put_line('drop table Work_Ifrs9FacData');
    execute immediate 'CREATE GLOBAL TEMPORARY TABLE "Work_Ifrs9FacData" (
    		    "CustNo"          decimal(7, 0)   default 0 not null    -- 戶號
		      , "FacmNo"          decimal(3, 0)   default 0 not null    -- 額度編號
		      , "DrawdownFg"      decimal(1, 0)   default 0 not null    -- 已核撥記號 (0: 未核撥 1: 已核撥)
		      , "TotalLoanBal"    decimal(16, 2)  default 0 not null    -- 本金餘額(撥款)合計
     	) ON COMMIT DELETE ROWS';
    dbms_output.put_line('create table Work_Ifrs9FacData');
    exception 
        when table_or_view_not_exist then
            dbms_output.put_line('Table t did not exist at time of drop. Continuing....');
            execute immediate 'CREATE GLOBAL TEMPORARY TABLE "Work_Ifrs9FacData" (
     						    "CustNo"          decimal(7, 0)   default 0 not null    -- 戶號
						      , "FacmNo"          decimal(3, 0)   default 0 not null    -- 額度編號
						      , "DrawdownFg"      decimal(1, 0)   default 0 not null    -- 已核撥記號 (0: 未核撥 1: 已核撥)
						      , "TotalLoanBal"    decimal(16, 2)  default 0 not null    -- 本金餘額(撥款)合計
                         ) ON COMMIT DELETE ROWS';
            dbms_output.put_line('create table Work_Ifrs9FacData');
        when attempted_ddl_on_in_use_GTT then
            dbms_output.put_line('Someone is keeping from doing my job!');
            dbms_output.put_line('Please rescue me');
            raise;
end;

