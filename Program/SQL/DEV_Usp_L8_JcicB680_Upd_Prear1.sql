-- 程式功能：清WK檔,FOR維護 JcicB680 每月聯徵「貸款餘額(擔保放款餘額加上部分擔保、副擔保貸款餘額)扣除擔保品鑑估值」之金額資料檔
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：由Usp_L8_JcicB680_Upd執行
--

create or replace procedure "Usp_L8_JcicB680_Upd_Prear1" AUTHID CURRENT_USER as
    table_or_view_not_exist exception;
    pragma exception_init(table_or_view_not_exist, -942);
    attempted_ddl_on_in_use_GTT exception;
    pragma exception_init(attempted_ddl_on_in_use_GTT, -14452);
begin
    execute immediate 'drop table "Work_B680"';
    dbms_output.put_line('drop table Work_B680');
    execute immediate 'CREATE GLOBAL TEMPORARY TABLE "Work_B680" (
    		    "CustId"      varchar2(10)
     			, "CustNo"      decimal(7, 0)   default 0 not null
     			, "FacmNo"      decimal(3, 0)   default 0 not null
     			, "BormNo"      decimal(3, 0)   default 0 not null
     			, "LoanBal"     decimal(16, 2)  default 0 not null
     			, "LineAmt"     decimal(16, 2)  default 0 not null
     			, "AcctCode"    varchar2(1)
     			, "BadDebtDate" decimal(5, 0)   default 0 not null
     			, "TotalAmt"    decimal(10, 0)  default 0 not null      -- B201 金額合計(千元)
     	) ON COMMIT DELETE ROWS';
    dbms_output.put_line('create table Work_B680');
    exception 
        when table_or_view_not_exist then
            dbms_output.put_line('Table t did not exist at time of drop. Continuing....');
            execute immediate 'CREATE GLOBAL TEMPORARY TABLE "Work_B680" (
     						    "CustId"      varchar2(10)
     							, "CustNo"      decimal(7, 0)   default 0 not null
     							, "FacmNo"      decimal(3, 0)   default 0 not null
     							, "BormNo"      decimal(3, 0)   default 0 not null
     							, "LoanBal"     decimal(16, 2)  default 0 not null
     							, "LineAmt"     decimal(16, 2)  default 0 not null
     							, "AcctCode"    varchar2(1)
     							, "BadDebtDate" decimal(5, 0)   default 0 not null
     							, "TotalAmt"    decimal(10, 0)  default 0 not null      -- B201 金額合計(千元)
                         ) ON COMMIT DELETE ROWS';
            dbms_output.put_line('create table Work_B680');
        when attempted_ddl_on_in_use_GTT then
            dbms_output.put_line('Someone is keeping from doing my job!');
            dbms_output.put_line('Please rescue me');
            raise;
end;

