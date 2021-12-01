--------------------------------------------------------
--  DDL for Procedure Usp_RefreshSeq
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "Usp_RefreshSeq" (
    "tableName"  IN  VARCHAR2 -- 資料表名
  , "seqName"    IN  VARCHAR2 -- SEQ名
) AS
    MAXVALUE  NUMBER := 0;
    CURRVALUE  NUMBER := 0;
BEGIN
    -- 使用範例
    -- EXEC "Usp_RefreshSeq"('PfItDetail','PfItDetail_SEQ');
    -- EXEC "Usp_RefreshSeq"('PfBsDetail','PfBsDetail_SEQ');
    -- EXEC "Usp_RefreshSeq"('PfReward','PfReward_SEQ');

    -- 取表內目前最大號碼
    EXECUTE IMMEDIATE 'select MAX("LogNo") from "'
                      || "tableName"
                      || '"'
    INTO MAXVALUE;

    LOOP
        -- 取SEQ紀錄的下一號
        EXECUTE IMMEDIATE 'select "'
                          || "seqName"
                          || '".NEXTVAL from DUAL '
        INTO CURRVALUE;

        -- 比較
        IF CURRVALUE >= MAXVALUE
        THEN EXIT;
        END IF;
    END LOOP;
END;

/
