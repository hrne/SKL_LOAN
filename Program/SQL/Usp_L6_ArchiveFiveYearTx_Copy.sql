CREATE OR REPLACE PROCEDURE "Usp_L6_ArchiveFiveYearTx_Copy"(
    v_tbsdyf IN NUMBER, v_empNo IN VARCHAR2
) IS
BEGIN
    DECLARE
        -- 此搬運在 TxArchiveTable 中的 Type
        v_archiveType     CHAR(4)        := '5YTX';

        TYPE t_archiveTables IS TABLE OF "TxArchiveTable"%ROWTYPE;
        v_archiveTables   t_archiveTables;
        v_lastResult      NUMBER(1);
        v_successfulCount NUMBER(7) := 0;
        v_nextBatchNo     NUMBER(3);
        
        v_tempCount       NUMBER(10) := 0;
    BEGIN

        -- 先存取TxArchiveTableLog, 取得這次執行的批次編號
        SELECT NVL(MAX(TATL."BatchNo"), 0) + 1 AS "NextBatchNo"
        INTO v_nextBatchNo
        FROM "TxArchiveTableLog" TATL
        WHERE TATL."ExecuteDate" = v_tbsdyf;

        -- 存取TxArchiveTable
        SELECT * BULK COLLECT
        INTO v_archiveTables
        FROM "TxArchiveTable"
        WHERE "Type" = v_archiveType
          AND "Enabled" = 1;

        EXECUTE IMMEDIATE 'TRUNCATE TABLE "TxArchivedTemp" DROP STORAGE';

        -- 搬運目標資料 Query
        INSERT INTO "TxArchivedTemp"
        WITH LBM AS (
            -- 在放款主檔(LoanBorMain)放款餘額皆為0
            SELECT "CustNo"
                 , SUM("LoanBal") AS "SumLoanBal"
            FROM "LoanBorMain"
            GROUP BY "CustNo"
        )
        , FC AS (
            SELECT FC."CustNo"
                 , FC."FacmNo"
                 , FC."CloseDate"
                 , FC."ReceiveFg"
                 , FC."ReceiveDate"
                 , FC."CollectFlag"
                 , ROW_NUMBER() OVER (
                    PARTITION BY FC."CustNo"
                               , FC."FacmNo"
                    ORDER BY NVL(FC."CloseDate",0) DESC -- 清償作業檔(FacClose)的最後一筆結清日期
                   ) "Seq"
            FROM LBM
            LEFT JOIN "FacClose" FC ON FC."CustNo" = LBM."CustNo"
            WHERE LBM."SumLoanBal" = 0 -- 在放款主檔(LoanBorMain)放款餘額皆為0
              AND FC."CloseDate" >= 19110101 -- 確保有結清日期
        )
        SELECT DISTINCT
               LBT."CustNo"
              ,LBT."FacmNo"
              ,LBT."BormNo"
        FROM "LoanBorTx" LBT
        LEFT JOIN FC ON FC."Seq" = 1 -- 清償作業檔(FacClose)的最後一筆結清日期
                    AND FC."CustNo" = LBT."CustNo"
                    AND FC."FacmNo" = LBT."FacmNo"
        LEFT JOIN "TxBizDate" TBD ON TBD."DateCode" = 'ONLINE'
        WHERE NVL(FC."CloseDate",0) >= 19110101 -- 確保有結清日期
          AND TRUNC(MONTHS_BETWEEN(TO_DATE(TO_CHAR(TBD."TbsDyf"), 'YYYYMMDD'), TO_DATE(TO_CHAR(FC."CloseDate"), 'YYYYMMDD'))) >= 5 * 12 -- 結清滿五年
        ;
        
        SELECT COUNT(*) INTO v_tempCount FROM "TxArchivedTemp";
        
        -- 檢查筆數
        IF v_archiveTables.COUNT = 0
        THEN
            DBMS_OUTPUT.PUT_LINE('TxArchiveTables 中, TYPE 為 ' || DBMS_ASSERT.ENQUOTE_LITERAL(v_archiveType) ||
                                 ' 的資料數為 0, 放棄搬運');
            RETURN;
        end if;

        IF v_tempCount = 0
        THEN
            DBMS_OUTPUT.PUT_LINE('沒有需要搬運的資料，結束搬運');
            RETURN;
        end if;

        -- 開始搬運
        FOR i IN (
            SELECT "CustNo"
                 , "FacmNo"
                 , "BormNo"
            FROM "TxArchivedTemp"
            ORDER BY "CustNo"
                   , "FacmNo"
                   , "BormNo"
        )
            LOOP
                FOR j IN v_archiveTables.FIRST .. v_archiveTables.LAST
                    LOOP
                        DBMS_OUTPUT.PUT_LINE('開始搬運 ' || DBMS_ASSERT.ENQUOTE_LITERAL(i."CustNo" ||
                                                                                    '-' || i."FacmNo" ||
                                                                                    '-' || i."BormNo") ||
                                             ' 到 ' ||
                                             DBMS_ASSERT.ENQUOTE_NAME(v_archiveTables(j)."TableName", FALSE));

                        -- 搬運 block starts
                        -- MERGE INTO 會跳過已經有的資料，因此不需處理重複資料問題
                        BEGIN
                            v_lastResult := "Fn_MoveTableBetweenSchemas"(v_archiveType,
                                                                         v_nextBatchNo,
                                                                         v_archiveTables(j)."TableName",
                                                                         v_archiveTables(j)."Conditions",
                                                                         i."CustNo",
                                                                         i."FacmNo",
                                                                         i."BormNo",
                                                                         0,
                                                                         v_tbsdyf,
                                                                         v_empNo);
                        END;
                        -- 搬運 block ends

                        if v_lastResult = 1
                        THEN
                            DBMS_OUTPUT.PUT_LINE('搬運成功!');
                            v_successfulCount := v_successfulCount + 1;
                        ELSE
                            DBMS_OUTPUT.PUT_LINE('搬運失敗!');
                        end if;
                        COMMIT;
                    end loop;
            end loop;

        DBMS_OUTPUT.PUT_LINE('搬運結束。成功數量：' || v_successfulCount || ' / ' || v_tempCount * v_archiveTables.COUNT);
    end;
end;
/

