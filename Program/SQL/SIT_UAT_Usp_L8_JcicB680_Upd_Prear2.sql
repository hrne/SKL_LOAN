-- 程式功能：清WK檔,FOR維護 JcicB680 每月聯徵「貸款餘額(擔保放款餘額加上部分擔保、副擔保貸款餘額)扣除擔保品鑑估值」之金額資料檔
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：由Usp_L8_JcicB680_Upd執行
--


create or replace procedure "Usp_L8_JcicB680_Upd_Prear2" AUTHID CURRENT_USER as
    table_or_view_not_exist exception;
    pragma exception_init(table_or_view_not_exist, -942);
    attempted_ddl_on_in_use_GTT exception;
    pragma exception_init(attempted_ddl_on_in_use_GTT, -14452);
begin
    execute immediate 'drop table "Work_B680_F"';
    dbms_output.put_line('drop table Work_B680_F');
    execute immediate 'CREATE GLOBAL TEMPORARY TABLE "Work_B680_F" (
    		    "CustId"      varchar2(10)
     			, "CustNo"      decimal(7, 0)   default 0 not null
     			, "LineAmt"     decimal(16, 2)  default 0 not null
     	) ON COMMIT DELETE ROWS';
    dbms_output.put_line('create table Work_B680_F');
    exception 
        when table_or_view_not_exist then
            dbms_output.put_line('Table t did not exist at time of drop. Continuing....');
            execute immediate 'CREATE GLOBAL TEMPORARY TABLE "Work_B680_F" (
     						    "CustId"      varchar2(10)
     							, "CustNo"      decimal(7, 0)   default 0 not null
     							, "LineAmt"     decimal(16, 2)  default 0 not null
                         ) ON COMMIT DELETE ROWS';
            dbms_output.put_line('create table Work_B680_F');
        when attempted_ddl_on_in_use_GTT then
            dbms_output.put_line('Someone is keeping from doing my job!');
            dbms_output.put_line('Please rescue me');
            raise;
end;


