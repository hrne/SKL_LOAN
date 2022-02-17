-- 程式功能：清WK檔,FOR維護 LoanIfrs9Fp 每月IFRS9欄位清單F檔
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：由Usp_L7_LoanIfrs9Fp_Upd執行
--

create or replace procedure "Usp_L7_LoanIfrs9Fp_Upd_Prear" AUTHID CURRENT_USER as
    table_or_view_not_exist exception;
    pragma exception_init(table_or_view_not_exist, -942);
    attempted_ddl_on_in_use_GTT exception;
    pragma exception_init(attempted_ddl_on_in_use_GTT, -14452);
begin
    execute immediate 'drop table "Work_FP"';
    dbms_output.put_line('drop table Work_FP');
    execute immediate 'CREATE GLOBAL TEMPORARY TABLE "Work_FP" (
    		    "CustNo"    decimal(7, 0)
     			, "AgreeFg"   varchar2(1)
     			, "FacmNo"    decimal(3, 0)
     			, "BormNo"    decimal(3, 0)
     			, "AgreeSeq"  decimal(3, 0)
     			, "AcDate"    decimal(8, 0)
     	) ON COMMIT DELETE ROWS';
    dbms_output.put_line('create table Work_FP');
    exception 
        when table_or_view_not_exist then
            dbms_output.put_line('Table t did not exist at time of drop. Continuing....');
            execute immediate 'CREATE GLOBAL TEMPORARY TABLE "Work_FP" (
     						    "CustNo"    decimal(7, 0)
     							, "AgreeFg"   varchar2(1)
     							, "FacmNo"    decimal(3, 0)
     							, "BormNo"    decimal(3, 0)
     							, "AgreeSeq"  decimal(3, 0)
     							, "AcDate"    decimal(8, 0)
                         ) ON COMMIT DELETE ROWS';
            dbms_output.put_line('create table Work_FP');
        when attempted_ddl_on_in_use_GTT then
            dbms_output.put_line('Someone is keeping from doing my job!');
            dbms_output.put_line('Please rescue me');
            raise;
end;


