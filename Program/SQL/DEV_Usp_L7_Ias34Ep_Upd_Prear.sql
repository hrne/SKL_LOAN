-- 程式功能：清WK檔,FOR維護 Ias34Ep 每月IAS34資料欄位清單E檔
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：由Usp_L7_Ias34Ep_Upd執行
--

-- Work_EP 資料
create or replace procedure "Usp_L7_Ias34Ep_Upd_Prear" AUTHID CURRENT_USER as
    table_or_view_not_exist exception;
    pragma exception_init(table_or_view_not_exist, -942);
    attempted_ddl_on_in_use_GTT exception;
    pragma exception_init(attempted_ddl_on_in_use_GTT, -14452);
begin
    execute immediate 'drop table "Work_EP"';
    dbms_output.put_line('drop table Work_EP');
    execute immediate 'CREATE GLOBAL TEMPORARY TABLE "Work_EP" (
    		    "CustNo"      decimal(7, 0)
     		  , "FacmNo"      decimal(3, 0)
    		  , "BormNo"      decimal(3, 0)
     		  , "AcCode"      varchar2(11)
    		  , "Status"      decimal(1, 0)
    		  , "BadDebtDate" decimal(8, 0)
    		  , "AcctCode"    varchar2(3)
     	) ON COMMIT DELETE ROWS';
    dbms_output.put_line('create table Work_EP');
    exception 
        when table_or_view_not_exist then
            dbms_output.put_line('Table t did not exist at time of drop. Continuing....');
            execute immediate 'CREATE GLOBAL TEMPORARY TABLE "Work_EP" (
     						    "CustNo"      decimal(7, 0)
    						  , "FacmNo"      decimal(3, 0)
    						  , "BormNo"      decimal(3, 0)
    						  , "AcCode"      varchar2(11)
    						  , "Status"      decimal(1, 0)
    						  , "BadDebtDate" decimal(8, 0)
    						  , "AcctCode"    varchar2(3)
                         ) ON COMMIT DELETE ROWS';
            dbms_output.put_line('create table Work_EP');
        when attempted_ddl_on_in_use_GTT then
            dbms_output.put_line('Someone is keeping from doing my job!');
            dbms_output.put_line('Please rescue me');
            raise;
end;

