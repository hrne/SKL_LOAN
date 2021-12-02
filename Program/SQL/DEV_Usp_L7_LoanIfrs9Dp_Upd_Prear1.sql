-- 程式功能：清WK檔,FOR維護 LoanIfrs9Dp 每月IFRS9欄位清單D檔
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：由Usp_L7_LoanIfrs9Dp_Upd執行
--

-- Work_DP_Data 資料
create or replace procedure "Usp_L7_LoanIfrs9Dp_Upd_Prear1" AUTHID CURRENT_USER as
    table_or_view_not_exist exception;
    pragma exception_init(table_or_view_not_exist, -942);
    attempted_ddl_on_in_use_GTT exception;
    pragma exception_init(attempted_ddl_on_in_use_GTT, -14452);
begin
    execute immediate 'drop table "Work_DP_Data"';
    dbms_output.put_line('drop table Work_DP_Data');
    execute immediate 'CREATE GLOBAL TEMPORARY TABLE "Work_DP_Data" (
    		    "CustNo"       decimal(7, 0)
    		  , "FacmNo"       decimal(3, 0)
		      , "BormNo"       decimal(3, 0)
 		      , "DataFg"       decimal(1, 0)  -- 資料類別 -- 1=法拍完成資料檔
                                                      -- 2=逾期天數>=90天且未在法拍完成資料檔中
		      , "FinishedDate" decimal(8, 0)  -- 拍定完成日
     	) ON COMMIT DELETE ROWS';
    dbms_output.put_line('create table Work_DP_Data');
    exception 
        when table_or_view_not_exist then
            dbms_output.put_line('Table t did not exist at time of drop. Continuing....');
            execute immediate 'CREATE GLOBAL TEMPORARY TABLE "Work_DP_Data" (
     						    "CustNo"       decimal(7, 0)
     							, "FacmNo"       decimal(3, 0)
     							, "BormNo"       decimal(3, 0)
     							, "DataFg"       decimal(1, 0)  -- 資料類別 -- 1=法拍完成資料檔
                                                 							-- 2=逾期天數>=90天且未在法拍完成資料檔中
     							, "FinishedDate" decimal(8, 0)  -- 拍定完成日
                         ) ON COMMIT DELETE ROWS';
            dbms_output.put_line('create table Work_DP_Data');
        when attempted_ddl_on_in_use_GTT then
            dbms_output.put_line('Someone is keeping from doing my job!');
            dbms_output.put_line('Please rescue me');
            raise;
end;


