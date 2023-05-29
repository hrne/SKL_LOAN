CREATE OR REPLACE NONEDITIONABLE FUNCTION "Fn_MoveTableBetweenSchemas"(
    -- 固定處理 history 和 online
    -- 此程式不處理 EXCEPTION，請在呼叫處處理
    v_type IN CHAR -- 此批分類
,   v_batchNo IN NUMBER -- 批次編號
,   v_tableName IN VARCHAR2 -- table名
,   v_conditions IN NUMBER -- 定義參照 TxArchiveTable
,   v_custNo IN NUMBER
,   v_facmNo IN NUMBER
,   v_bormNo IN NUMBER
,   v_flipRoute IN NUMBER -- 0: ONLINE TO HISTORY; 1: HISTORY TO ONLINE
,   v_tbsdyf IN NUMBER -- 當日會計日
,   v_empNo IN VARCHAR2
) RETURN NUMBER IS
    "Result" NUMBER(1);
BEGIN
    DECLARE
        v_cursor             SYS_REFCURSOR;
        v_schemaNameSource   VARCHAR2(14);
        v_schemaNameTarget   VARCHAR2(14);
        v_schemaNameHistory  VARCHAR2(14)  := 'ITXADMINDAYD';
        v_schemaNameOnline   VARCHAR2(14)  := 'ITXADMIND';
        v_targetColCount     NUMBER(3) := 0;
        TYPE t_tabColumnRecord IS RECORD
                                  (
                                      COLUMN_NAME    VARCHAR(128),
                                      DATA_TYPE      VARCHAR(128),
                                      DATA_LENGTH    NUMBER,
                                      DATA_PRECISION NUMBER,
                                      DATA_SCALE     NUMBER,
                                      NULLABLE       VARCHAR2(1)
                                  );
        TYPE t_tabCols IS TABLE OF t_tabColumnRecord;
        v_diffCols           t_tabCols;

        TYPE t_colRecord IS RECORD (COLUMN_NAME VARCHAR2(4000));
        TYPE t_keyCols IS TABLE OF t_colRecord;
        v_targetKeyCols      t_keyCols;
        v_sourceKeyCols      t_keyCols;
        v_currentColumn      t_colRecord;
        v_columnsString      VARCHAR2(2000);
        v_insertValuesString VARCHAR2(2000);
        v_pkString           VARCHAR2(2000);
        v_sql                VARCHAR2(4000);

        PROCEDURE INSERT_LOG(
            p_result NUMBER, p_description VARCHAR2, p_records NUMBER
        ) IS
        BEGIN
            BEGIN
                INSERT INTO "TxArchiveTableLog" (
                    "LogNo"
                    , "Type"
                    , "DataFrom"
                    , "DataTo"
                    , "ExecuteDate"
                    , "TableName"
                    , "BatchNo"
                    , "Result"
                    , "CustNo"
                    , "FacmNo"
                    , "BormNo"
                    , "Description"
                    , "CreateDate"
                    , "CreateEmpNo"
                    , "LastUpdate"
                    , "LastUpdateEmpNo"
                    , "Records")
                VALUES (
                    "TxArchiveTableLog_SEQ".nextval
                    , v_type
                    , CASE
                        WHEN v_flipRoute = 1
                        THEN 'HISTORY'
                      ELSE 'ONLINE' END
                    , CASE 
                        WHEN v_flipRoute = 1
                        THEN 'ONLINE'
                      ELSE 'HISTORY' END
                    , v_tbsdyf
                    , v_tableName
                    , v_batchNo
                    , p_result
                    , v_custNo
                    , v_facmNo
                    , v_bormNo
                    , SUBSTR(p_description, 1, 200)
                    , SYSDATE
                    , v_empNo
                    , SYSDATE
                    , v_empNo
                    , p_records
                );
            END;
            -- 例外處理
            Exception
            WHEN OTHERS THEN
            "Usp_L9_UspErrorLog_Ins"(
                'INSERT_LOG' -- UspName 預存程序名稱
            , SQLCODE -- Sql Error Code (固定值)
            , SQLERRM -- Sql Error Message (固定值)
            , dbms_utility.format_error_backtrace -- Sql Error Trace (固定值)
            , v_empNo -- 發動預存程序的員工編號
            );
        END;


    BEGIN
        -- 當 v_flipRoute
        -- 為 1 時，表示要從 HISTORY 搬進 ONLINE
        -- 為 0 時，表示要從 ONLINE 搬進 HISTORY

        IF v_flipRoute = 1
        THEN
            DBMS_OUTPUT.PUT_LINE('flipRoute: ' || 1);
            v_schemaNameSource := v_schemaNameHistory;
            v_schemaNameTarget := v_schemaNameOnline;
        ELSE
            DBMS_OUTPUT.PUT_LINE('flipRoute: ' || v_flipRoute);
            v_schemaNameSource := TRIM(v_schemaNameOnline);
            v_schemaNameTarget := TRIM(v_schemaNameHistory);
        end if;

        -- Step 0. 檢查欄位差異
        --
        -- 確認 table 在兩個 schemas 裡面都結構相同
        -- 只檢查下列情況:
        --    1. ONLINE 欄位在 HISTORY 不存在
        --    2. ONLINE 欄位在 HISTORY 定義不同
        --    3. HISTORY 沒有讀到欄位（權限未 GRANT）
        -- 不處理下列情況:
        --    1. HISTORY 欄位比 ONLINE 多
        --    2. COLUMN ID 不同（搬運時看欄位名稱塞值）
        --

        -- 先檢查權限
        SELECT TO_NUMBER(COUNT(1))
        INTO v_targetColCount
        FROM ALL_TAB_COLUMNS
        WHERE OWNER = DBMS_ASSERT.SCHEMA_NAME(v_schemaNameTarget)
          AND TABLE_NAME = DBMS_ASSERT.SIMPLE_SQL_NAME(v_tableName)
        ;

        IF v_targetColCount = 0
        THEN
            DBMS_OUTPUT.PUT_LINE('v_schemaNameTarget=' || v_schemaNameTarget);
            DBMS_OUTPUT.PUT_LINE('v_tableName=' || v_tableName);
            DBMS_OUTPUT.PUT_LINE('v_targetColCount=' || v_targetColCount);
            DBMS_OUTPUT.PUT_LINE(DBMS_ASSERT.ENQUOTE_LITERAL(v_tableName) ||
                                 ' 在 HISTORY('||v_schemaNameTarget||').ALL_TAB_COLS 讀取不到('||v_tableName||')欄位! 是不是 SELECT 權限尚未 GRANT ?');
            INSERT_LOG(0, 'HISTORY('||v_schemaNameTarget||').ALL_TAB_COLS 讀取不到('||v_tableName||')欄位，可能為權限問題', 0);
            RETURN 0;
        end if;


        SELECT COLUMN_NAME
             , DATA_TYPE
             , DATA_LENGTH
             , NVL(DATA_PRECISION, 0) AS DATA_PRECISION
             , NVL(DATA_SCALE, 0)     AS DATA_SCALE
             , NULLABLE BULK COLLECT
        INTO v_diffCols
        FROM ALL_TAB_COLUMNS
        WHERE OWNER = DBMS_ASSERT.SCHEMA_NAME(v_schemaNameSource)
          AND TABLE_NAME = DBMS_ASSERT.SIMPLE_SQL_NAME(v_tableName)
        MINUS
        SELECT COLUMN_NAME
             , DATA_TYPE
             , DATA_LENGTH
             , NVL(DATA_PRECISION, 0) AS DATA_PRECISION
             , NVL(DATA_SCALE, 0)     AS DATA_SCALE
             , NULLABLE
        FROM ALL_TAB_COLUMNS
        WHERE OWNER = DBMS_ASSERT.SCHEMA_NAME(v_schemaNameTarget)
          AND TABLE_NAME = DBMS_ASSERT.SIMPLE_SQL_NAME(v_tableName);

        -- diffCols 有結果表示有不同的欄位，逐一輸出，最後中斷程式
        IF v_diffCols.COUNT > 0
        THEN
            dbms_OUTPUT.PUT_LINE(DBMS_ASSERT.ENQUOTE_LITERAL(v_tableName) ||
                                 ' 在 ONLINE 與 HISTORY 發現欄位有差異，以下為差異欄位在 ONLINE 的狀態');
            FOR i IN v_diffCols.FIRST .. v_diffCols.LAST
                LOOP
                    dbms_OUTPUT.PUT_LINE('Different col found: '
                        || 'name: ' || v_diffCols(i).COLUMN_NAME || ', '
                        || 'type: ' || v_diffCols(i).DATA_TYPE || ', '
                        || 'len: ' || v_diffCols(i).DATA_LENGTH || ', '
                        || 'precision: ' || v_diffCols(i).DATA_PRECISION || ', '
                        || 'scale: ' || v_diffCols(i).DATA_SCALE || ', '
                        || 'nullable: ' || v_diffCols(i).NULLABLE);
                END LOOP;
            INSERT_LOG(0, '兩個環境欄位有差異，共 ' || v_diffCols.COUNT || ' 個', 0);
            RETURN 0; -- 中斷程式
        END IF;

        -- 檢查通過
        dbms_OUTPUT.PUT_LINE('欄位檢查通過');

        --
        -- Step 1. 檢查 PK 差異
        --
        -- 只允許 ONLINE 與 HISTORY PKs 完全相同
        --

        -- 取得 ONLINE PKs
        OPEN v_cursor FOR 'SELECT COLUMN_NAME ' ||
                          'FROM ALL_CONS_COLUMNS ' ||
                          'WHERE OWNER = ' ||
                          DBMS_ASSERT.ENQUOTE_LITERAL(DBMS_ASSERT.SCHEMA_NAME(v_schemaNameSource)) || ' ' ||
                          '  AND TABLE_NAME = ' || DBMS_ASSERT.ENQUOTE_LITERAL(v_tableName) || ' ' ||
                          '  AND CONSTRAINT_NAME = ' ||
                          DBMS_ASSERT.ENQUOTE_LITERAL(DBMS_ASSERT.QUALIFIED_SQL_NAME(v_tableName) || '_PK') || ' ' ||
                          'ORDER BY COLUMN_NAME';
        FETCH v_cursor BULK COLLECT INTO v_sourceKeyCols;
        CLOSE v_cursor;

        -- 取得 HISTORY PKs
        OPEN v_cursor FOR 'SELECT COLUMN_NAME ' ||
                          'FROM ALL_CONS_COLUMNS ' ||
                          'WHERE OWNER = ' ||
                          DBMS_ASSERT.ENQUOTE_LITERAL(DBMS_ASSERT.SCHEMA_NAME(v_schemaNameTarget)) || ' ' ||
                          '  AND TABLE_NAME = ' || DBMS_ASSERT.ENQUOTE_LITERAL(v_tableName) || ' ' ||
                          '  AND CONSTRAINT_NAME = ' ||
                          DBMS_ASSERT.ENQUOTE_LITERAL(DBMS_ASSERT.QUALIFIED_SQL_NAME(v_tableName) || '_PK') || ' ' ||
                          'ORDER BY COLUMN_NAME';
        FETCH v_cursor BULK COLLECT INTO v_targetKeyCols;
        CLOSE v_cursor;

        IF v_sourceKeyCols.COUNT = 0
        THEN
            DBMS_OUTPUT.PUT_LINE('錯誤! ' || DBMS_ASSERT.ENQUOTE_LITERAL(v_tableName) ||
                                 ' 在 ' || DBMS_ASSERT.SCHEMA_NAME(v_schemaNameSource) || '沒有 PK，本程式不支援此類 TABLE!');
            INSERT_LOG(0, '在搬移目標環境未設定 PK', 0);
            RETURN 0;
        end if;

        IF v_sourceKeyCols.COUNT != v_targetKeyCols.COUNT
        THEN
            DBMS_OUTPUT.PUT_LINE('錯誤! ' || DBMS_ASSERT.ENQUOTE_LITERAL(v_tableName) ||
                                 ' 在 ' || DBMS_ASSERT.SCHEMA_NAME(v_schemaNameSource) || ' 與 ' ||
                                 DBMS_ASSERT.SCHEMA_NAME(v_schemaNameTarget) || ' 中 PK 數量不同! 放棄搬移!');
            DBMS_OUTPUT.PUT_LINE(v_sourceKeyCols.COUNT || ' v.s. ' || v_targetKeyCols.COUNT);
            INSERT_LOG(0, '在兩個環境的 PK 數量不同', 0);
            RETURN 0;
        END IF;

        FOR i IN v_sourceKeyCols.FIRST .. v_sourceKeyCols.LAST
            LOOP
                IF v_sourceKeyCols(i).COLUMN_NAME != v_targetKeyCols(i).COLUMN_NAME
                THEN
                    DBMS_OUTPUT.PUT_LINE('錯誤! ' || DBMS_ASSERT.ENQUOTE_LITERAL(v_tableName) ||
                                         ' 在 ONLINE 與 HISTORY 中 PK 內容不同! 放棄搬移!');
                    DBMS_OUTPUT.PUT_LINE(v_sourceKeyCols(i).COLUMN_NAME || ' v.s. ' || v_targetKeyCols(i).COLUMN_NAME);
                    INSERT_LOG(0, '在兩個環境的 PK 有差異', 0);
                    RETURN 0;
                end if;
            end loop;

        --
        -- Step 2. 搬運資料
        --
        -- 前提:
        --    1. SOURCE 欄位必定存在於 TARGET
        --    2. SOURCE 欄位數必定 <= TARGET
        --    3. SOURCE  PK = TARGET PK
        -- 讀取 SOURCE 所有欄位，組欄位 list string "column","column","column"
        -- 讀取 PK，組 PK 條件 string SOURCE.KEY = TARGET.KEY
        -- 組 INSERT INTO
        -- 執行
        --

        -- 組 columnsString 和 insertValuesString
        OPEN v_cursor FOR ' SELECT COLUMN_NAME ' ||
                          ' FROM USER_TAB_COLS ' ||
                          ' WHERE TABLE_NAME = ' || DBMS_ASSERT.ENQUOTE_LITERAL(v_tableName) ||
                          '   AND USER_GENERATED = ''YES'''; -- ORACLE 某些情況下會自動產生隱藏的欄位

        LOOP
            FETCH v_cursor INTO v_currentColumn;
            EXIT WHEN v_cursor%NOTFOUND;

            v_columnsString := v_columnsString || ',' || DBMS_ASSERT.ENQUOTE_NAME(v_currentColumn.COLUMN_NAME, FALSE);
            v_insertValuesString := v_insertValuesString || ', SOURCE.' ||
                                    DBMS_ASSERT.ENQUOTE_NAME(v_currentColumn.COLUMN_NAME, FALSE);
        END LOOP;

        CLOSE v_cursor;

        -- 去掉最開始的逗號
        IF LENGTH(v_columnsString) < 2 OR LENGTH(v_insertValuesString) < 2
        THEN
            DBMS_OUTPUT.PUT_LINE('錯誤! ONLINE columns 抓取失敗, 放棄搬運!');
            INSERT_LOG(0, '在搬移目標環境抓不到任何欄位', 0);
            RETURN 0;
        end if;
        v_columnsString := SUBSTR(v_columnsString, 2);
        v_insertValuesString := SUBSTR(v_insertValuesString, 2);

        -- 組 PKsString

        FOR i IN v_sourceKeyCols.FIRST .. v_sourceKeyCols.LAST
            LOOP
                v_pkString := v_pkString || 'AND SOURCE.' ||
                              DBMS_ASSERT.ENQUOTE_NAME(v_sourceKeyCols(i).COLUMN_NAME, FALSE) ||
                              ' = TARGET.' || DBMS_ASSERT.ENQUOTE_NAME(v_sourceKeyCols(i).COLUMN_NAME, FALSE) || ' ';
            end loop;

        -- 去除最開始的 AND
        IF LENGTH(v_pkString) < 5
        THEN
            DBMS_OUTPUT.PUT_LINE('錯誤! PK columns 組字串時有異常, 放棄搬運!');
            INSERT_LOG(0, '抓取 PK 欄位並組成字串時發生異常', 0);
            RETURN 0;
        end if;
        v_pkString := SUBSTR(v_pkString, 5);

        --         MERGE INTO v_schemaNameHistory.v_tableName TARGET
        --         USING (
        --             SELECT v_columnsString
        --             FROM v_schemaNameOnline.v_tableName SOURCE
        --             WHERE custNo facmNo bormNo
        --               )
        --         ON (
        --             v_pkString
        --             )
        --         WHEN NOT MATCHED THEN INSERT ( v_columnsString ) VALUES ( v_columnsString );

        v_sql := 'MERGE INTO ' || DBMS_ASSERT.SCHEMA_NAME(v_schemaNameTarget) || '.' ||
                 DBMS_ASSERT.ENQUOTE_NAME(v_tableName, FALSE) || ' TARGET ' ||
                 'USING ( ' ||
                 '    SELECT ' || v_columnsString || ' ' ||
                 '      FROM ' || DBMS_ASSERT.SCHEMA_NAME(v_schemaNameSource) || '.' ||
                 DBMS_ASSERT.ENQUOTE_NAME(v_tableName, FALSE) ||
                 '      WHERE 1 = 1 ' || -- dummy
                 CASE WHEN v_conditions BETWEEN 1 AND 3
                          THEN
                          '        AND "CustNo" = ' || v_custNo || ' '
                          ELSE ''
                 END ||
                 CASE WHEN v_conditions BETWEEN 2 AND 3
                          THEN
                          '        AND "FacmNo" = ' || v_facmNo || ' '
                          ELSE ''
                 END ||
                 CASE WHEN v_conditions = 3
                          THEN
                          '        AND "BormNo" = ' || v_bormNo || ' '
                          ELSE ''
                 END ||
                 '      ) SOURCE ' ||
                 'ON ( ' || v_pkString || ' ) ' ||
                 'WHEN NOT MATCHED THEN INSERT ( ' || v_columnsString || ' ) VALUES ( ' || v_insertValuesString ||
                 ' ) ';

        DBMS_OUTPUT.PUT_LINE(v_sql);

        EXECUTE IMMEDIATE v_sql;

        --
        -- Step 3. 完成
        --
        -- MISSION ACCOMPLISHED!
        --
        DBMS_OUTPUT.PUT_LINE('搬移成功, 共 ' || SQL%ROWCOUNT || ' 筆資料');

        IF ( SQL%ROWCOUNT > 0 )
        THEN
            INSERT_LOG(1, '順利搬運共 ' || SQL%ROWCOUNT || ' 筆資料', SQL%ROWCOUNT);
        ELSE
            INSERT_LOG(1, '查無需要搬運的資料', 0);
        END IF;
        RETURN 1;
    EXCEPTION
        WHEN
            OTHERS THEN
            DBMS_OUTPUT.PUT_LINE(sqlerrm || ', ' || SUBSTR(DBMS_UTILITY.FORMAT_ERROR_BACKTRACE(), 1, 200));
            INSERT_LOG(0, sqlerrm || ', ' || SUBSTR(DBMS_UTILITY.FORMAT_ERROR_BACKTRACE(), 1, 200), 0);
            RETURN 0;
    END;
END;
/