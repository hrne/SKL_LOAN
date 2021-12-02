-- 程式功能：清WK檔,FOR維護 Ias34Gp 每月IAS34資料欄位清單G檔
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：由Usp_L7_Ias34Gp_Upd執行
--

-- Work_GP 資料
create or replace procedure "Usp_L7_Ias34Gp_Upd_Prear" AUTHID CURRENT_USER as
    table_or_view_not_exist exception;
    pragma exception_init(table_or_view_not_exist, -942);
    attempted_ddl_on_in_use_GTT exception;
    pragma exception_init(attempted_ddl_on_in_use_GTT, -14452);
begin
    execute immediate 'drop table "Work_GP"';
    dbms_output.put_line('drop table Work_GP');
    execute immediate 'CREATE GLOBAL TEMPORARY TABLE "Work_GP" (
    		    "CustNo"    decimal(7, 0)
    			, "AgreeFg"   varchar2(1)
    			, "FacmNo"    decimal(3, 0)
    			, "BormNo"    decimal(3, 0)
   			  , "AgreeSeq"  decimal(3, 0)
    			, "AcDate"    decimal(8, 0)
     	) ON COMMIT DELETE ROWS';
    dbms_output.put_line('create table Work_GP');
    exception 
        when table_or_view_not_exist then
            dbms_output.put_line('Table t did not exist at time of drop. Continuing....');
            execute immediate 'CREATE GLOBAL TEMPORARY TABLE "Work_GP" (
     						    "CustNo"    decimal(7, 0)
    						  , "AgreeFg"   varchar2(1)
    						  , "FacmNo"    decimal(3, 0)
   						 	  , "BormNo"    decimal(3, 0)
    						  , "AgreeSeq"  decimal(3, 0)
    						  , "AcDate"    decimal(8, 0)
                         ) ON COMMIT DELETE ROWS';
            dbms_output.put_line('create table Work_GP');
        when attempted_ddl_on_in_use_GTT then
            dbms_output.put_line('Someone is keeping from doing my job!');
            dbms_output.put_line('Please rescue me');
            raise;
end;

