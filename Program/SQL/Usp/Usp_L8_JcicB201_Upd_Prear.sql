-- 程式功能：清WK檔,FOR維護 JcicB201 每月聯徵授信餘額月報資料檔
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：由Usp_L8_JcicB201_Upd執行
--

-- 產生 JcicB201 的 保證人/所有權人
create or replace procedure "Usp_L8_JcicB201_Upd_Prear" AUTHID CURRENT_USER as
    table_or_view_not_exist exception;
    pragma exception_init(table_or_view_not_exist, -942);
    attempted_ddl_on_in_use_GTT exception;
    pragma exception_init(attempted_ddl_on_in_use_GTT, -14452);
begin
    execute immediate 'drop table "Work_B201_Guarantor"';
    dbms_output.put_line('drop table Work_B201_Guarantor');
    execute immediate 'CREATE GLOBAL TEMPORARY TABLE "Work_B201_Guarantor" (
    		    "DataYM"               decimal(6, 0) default 0 not null
     			, "ApplNo"               decimal(7, 0) default 0 not null  -- 核准號碼
     			, "CustId"               varchar2(10)                      -- 保證人身份統一編號
     			, "ROW_NUM"              decimal(2, 0) default 0 not null  -- 序號（同一核准號碼編列流水號）
     			, "ApplNoCount"          decimal(2, 0) default 0 not null  -- 筆數（同一核准號碼）
     			, "Source"               decimal(2, 0) default 0 not null  -- 資料來源(1=保證人檔 2=所有權人檔)
     			, "GuaRelCode"           varchar2(2)                       -- 保證人關係代碼
     			, "GuaRelJcic"           varchar2(2)                       -- 保證人關係ＪＣＩＣ代碼
     			, "GuaTypeCode"          varchar2(2)                       -- 保證類別代碼
     			, "GuaTypeJcic"          varchar2(1)                       -- 保證類別ＪＣＩＣ代碼
     	) ON COMMIT DELETE ROWS';
    dbms_output.put_line('create table Work_B201_Guarantor');
    exception 
        when table_or_view_not_exist then
            dbms_output.put_line('Table t did not exist at time of drop. Continuing....');
            execute immediate 'CREATE GLOBAL TEMPORARY TABLE "Work_B201_Guarantor" (
     						    "DataYM"               decimal(6, 0) default 0 not null
     							, "ApplNo"               decimal(7, 0) default 0 not null  -- 核准號碼
     							, "CustId"               varchar2(10)                      -- 保證人身份統一編號
     							, "ROW_NUM"              decimal(2, 0) default 0 not null  -- 序號（同一核准號碼編列流水號）
     							, "ApplNoCount"          decimal(2, 0) default 0 not null  -- 筆數（同一核准號碼）
     							, "Source"               decimal(2, 0) default 0 not null  -- 資料來源(1=保證人檔 2=所有權人檔)
     							, "GuaRelCode"           varchar2(2)                       -- 保證人關係代碼
     							, "GuaRelJcic"           varchar2(2)                       -- 保證人關係ＪＣＩＣ代碼
     							, "GuaTypeCode"          varchar2(2)                       -- 保證類別代碼
     							, "GuaTypeJcic"          varchar2(1)                       -- 保證類別ＪＣＩＣ代碼
                         ) ON COMMIT DELETE ROWS';
            dbms_output.put_line('create table Work_B201_Guarantor');
        when attempted_ddl_on_in_use_GTT then
            dbms_output.put_line('Someone is keeping from doing my job!');
            dbms_output.put_line('Please rescue me');
            raise;
end;

