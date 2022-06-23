CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_L8_JcicB092_Upd_Prear" AUTHID CURRENT_USER as
    table_or_view_not_exist exception;
    pragma exception_init(table_or_view_not_exist, -942);
    attempted_ddl_on_in_use_GTT exception;
    pragma exception_init(attempted_ddl_on_in_use_GTT, -14452);
-- 程式功能：清WK檔,維護 JcicB092 每月聯徵不動產擔保品明細檔
-- 執行時機：每月底日終批次(換日前)
-- 執行方式：由Usp_L8_JcicB092_Upd執行
--
begin
    execute immediate 'drop table "Work_B092"';
    dbms_output.put_line('drop table Work_B092');
    execute immediate 'CREATE GLOBAL TEMPORARY TABLE "Work_B092" (
       			"FacmNo"           varchar2(50)
     			, "MainClActNo"      varchar2(50)
     			, "ClCode1"          decimal(1, 0)   default 0 not null
     			, "ClCode2"          decimal(2, 0)   default 0 not null
     			, "ClNo"             decimal(7, 0)   default 0 not null
     			, "ClTypeJCIC"       varchar2(2)
     			, "OwnerId"          varchar2(10)
     			, "EvaAmt"           varchar2(8)
     			, "EvaDate"          decimal(5, 0)   default 0 not null
     			, "SettingDate"      decimal(5, 0)   default 0 not null
     			, "MonthSettingAmt"  varchar2(8)
     			, "SettingSeq"       decimal(1, 0)   default 0 not null
     			, "PreSettingAmt"    varchar2(8)
     			, "DispPrice"        varchar2(8)
     			, "IssueEndDate"     decimal(5, 0)   default 0 not null
     			, "CityCode"         varchar2(1)
     			, "AreaCode"         varchar2(2)
     			, "IrCode"           varchar2(4)
     			, "LandNo1"          decimal(4, 0)   default 0 not null
     			, "LandNo2"          decimal(4, 0)   default 0 not null
     			, "BdNo1"            decimal(5, 0)   default 0 not null
     			, "BdNo2"            decimal(3, 0)   default 0 not null
     			, "Zip"              varchar2(5)
     			, "LVITax"           decimal(14,2)   default 0 not null
     			, "LVITaxYearMonth"  decimal(5, 0)   default 0 not null
     			, "ContractPrice"    varchar2(8)
     			, "ContractDate"     varchar2(8)
     			, "ParkingTypeCode"  varchar2(1)
     			, "Area"             varchar2(9)
     			, "LandOwnedArea"    decimal(14, 2)  default 0 not null
     			, "LineAmt"          decimal(14, 0)  default 0 not null
     			, "CiSettingAmt"     decimal(14, 0)  default 0 not null
     	) ON COMMIT DELETE ROWS';
    dbms_output.put_line('create table Work_B092');
    exception 
        when table_or_view_not_exist then
            dbms_output.put_line('Table t did not exist at time of drop. Continuing....');
            execute immediate 'CREATE GLOBAL TEMPORARY TABLE "Work_B092" (
     						    "FacmNo"           varchar2(50)
     							, "MainClActNo"      varchar2(50)
     							, "ClCode1"          decimal(1, 0)   default 0 not null
     							, "ClCode2"          decimal(2, 0)   default 0 not null
     							, "ClNo"             decimal(7, 0)   default 0 not null
     							, "ClTypeJCIC"       varchar2(2)
     							, "OwnerId"          varchar2(10)
     							, "EvaAmt"           varchar2(8)
     							, "EvaDate"          decimal(5, 0)   default 0 not null
     							, "SettingDate"      decimal(5, 0)   default 0 not null
     							, "MonthSettingAmt"  varchar2(8)
     							, "SettingSeq"       decimal(1, 0)   default 0 not null
     							, "PreSettingAmt"    varchar2(8)
     							, "DispPrice"        varchar2(8)
     							, "IssueEndDate"     decimal(5, 0)   default 0 not null
     							, "CityCode"         varchar2(1)
     							, "AreaCode"         varchar2(2)
     							, "IrCode"           varchar2(4)
     							, "LandNo1"          decimal(4, 0)   default 0 not null
     							, "LandNo2"          decimal(4, 0)   default 0 not null
     							, "BdNo1"            decimal(5, 0)   default 0 not null
     							, "BdNo2"            decimal(3, 0)   default 0 not null
     							, "Zip"              varchar2(5)
     							, "LVITax"           decimal(14,2)   default 0 not null
     							, "LVITaxYearMonth"  decimal(5, 0)   default 0 not null
     							, "ContractPrice"    varchar2(8)
     							, "ContractDate"     varchar2(8)
     							, "ParkingTypeCode"  varchar2(1)
     							, "Area"             varchar2(9)
     							, "LandOwnedArea"    decimal(14, 2)  default 0 not null
     							, "LineAmt"          decimal(14, 0)  default 0 not null
     			        , "CiSettingAmt"     decimal(14, 0)  default 0 not null
                         ) ON COMMIT DELETE ROWS';
            dbms_output.put_line('create table Work_B092');
        when attempted_ddl_on_in_use_GTT then
            dbms_output.put_line('Someone is keeping from doing my job!');
            dbms_output.put_line('Please rescue me');
            raise;
end;


