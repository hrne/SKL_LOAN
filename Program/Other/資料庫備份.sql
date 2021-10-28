CREATE OR REPLACE PROCEDURE "Usp_L9_DailyBackup_Ins"
(
    -- 參數
    TBSDYF        IN NUMBER,   -- 系統營業日(西元)
    "EmpNo"       IN VARCHAR2, -- 修改人員
    "UserName"    IN VARCHAR2, -- 來源Schema
    "NewUserName" IN VARCHAR2, -- 目標Schema
    "NewPassword" IN VARCHAR2  -- 目標Schema密碼
)
AS
BEGIN
    -- EXEC "Usp_L9_DailyBackup_Ins"(20211028,'999999','C##ITXADMIN','ITXADMIN' || TO_CHAR(SYSTIMESTAMP,'yyyymmdd'),'st186995619');
    -- DROP USER ITXADMIN20211028 CASCADE; -- 測試用
    -- select * from dba_users; -- 測試用
    -- 建立新的User (Username="原名稱"+"_日期")
    DECLARE
        "vSql" VARCHAR2(3000); -- 動態SQL
        "vTableSpaces" VARCHAR2(30); -- 表空間
        "vUserName" VARCHAR2(30) := "NewUserName"; -- 新使用者名稱
        "vPassword" VARCHAR2(30) := "NewPassword"; -- 新密碼
        "vToday" VARCHAR2(8); -- 今日西元日期yyyymmdd
        "cSql" CLOB;
        "nCount" NUMBER(10);
    BEGIN
        "vSql" := 'alter session set "_oracle_script"=true';
        EXECUTE IMMEDIATE "vSql";

        -- 寫入表空間 (改用SP後可以用參數傳入)
        "vTableSpaces" := 'USERS';

        -- 防呆 避免把ONLINE砍了
        IF ("vUserName" IN ('ITXADMIN','ITXADMIND','ITXADMINH')) THEN
            RETURN;
        END IF;

        -- 重複時，先把舊的刪掉
        SELECT COUNT(*) INTO "nCount" FROM ALL_USERS WHERE USERNAME = "vUserName";

        IF "nCount" > 0 THEN
            "vSql" := 'DROP USER ' || "vUserName" || ' CASCADE';
            EXECUTE IMMEDIATE "vSql";
        END IF;

        -- 建立使用者
        "vSql" := 'CREATE USER ' || "vUserName" || ' IDENTIFIED BY ' || "vPassword" || ' DEFAULT TABLESPACE ' || "vTableSpaces";
        EXECUTE IMMEDIATE "vSql";

        -- 賦予權限 (暫時不用)
        "vSql" := 'GRANT CREATE ANY TABLE TO ' || "vUserName";
        EXECUTE IMMEDIATE "vSql";

        -- 賦予角色 
        "vSql" := 'GRANT RESOURCE TO ' || "vUserName";
        EXECUTE IMMEDIATE "vSql";
        "vSql" := 'GRANT CONNECT TO ' || "vUserName";
        EXECUTE IMMEDIATE "vSql";
        "vSql" := 'GRANT DBA TO ' || "vUserName";
        EXECUTE IMMEDIATE "vSql";

        -- 複製Table
        FOR x
        IN (
            SELECT TABLE_NAME
            FROM ALL_TABLES
            WHERE OWNER = "UserName"
        )
        LOOP
            "vSql" := 'CREATE TABLE "'
                      || "vUserName"
                      || '"."' 
                      || x."TABLE_NAME" 
                      || '" AS SELECT * FROM "'
                      || "UserName"
                      || '"."' 
                      || x."TABLE_NAME" 
                      || '"';
            EXECUTE IMMEDIATE "vSql";
        END LOOP;

        -- 複製Function
        FOR x
        IN (
            SELECT DBMS_METADATA.GET_DDL('FUNCTION',OBJECT_NAME,"UserName") AS "DESC"
            FROM ALL_OBJECTS
            WHERE OWNER = "UserName"
            AND OBJECT_TYPE = 'FUNCTION'
            AND STATUS = 'VALID'
        )
        LOOP
            -- DBMS_OUTPUT.PUT_LINE(x."DESC");
            "cSql" := SUBSTR(x."DESC",4);
            "cSql" := SUBSTR("cSql",0,INSTR("cSql",';',-1));
            -- DBMS_OUTPUT.PUT_LINE("cSql");
            EXECUTE IMMEDIATE REPLACE("cSql","UserName","vUserName");
        END LOOP;

        -- 複製Procedure
        FOR x
        IN (
            SELECT DBMS_METADATA.GET_DDL('PROCEDURE',OBJECT_NAME,"UserName") AS "DESC"
            FROM ALL_OBJECTS
            WHERE OWNER = "UserName"
            AND OBJECT_TYPE = 'PROCEDURE'
            AND STATUS = 'VALID'
        )
        LOOP
            "cSql" := SUBSTR(x."DESC",4);
            "cSql" := SUBSTR("cSql",0,INSTR("cSql",';',-1));
            -- DBMS_OUTPUT.PUT_LINE("cSql");
            EXECUTE IMMEDIATE REPLACE("cSql","UserName","vUserName");
        END LOOP;
    END;
END;