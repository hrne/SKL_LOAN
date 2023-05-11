CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_L6_ArchiveFiveYearTx_Copy"(
    v_tbsdyf IN NUMBER, v_empNo IN VARCHAR2
) IS
BEGIN
    DECLARE
        -- 此搬運在 TxArchiveTable 中的 Type
        v_archiveType     CHAR(4)        := '5YTX';

        -- 搬運目標資料 Query
        v_query           VARCHAR2(2500) := '
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
                                            SELECT LBT."CustNo"
                                                  ,LBT."FacmNo"
                                                  ,LBT."BormNo"
                                            FROM "LoanBorTx" LBT
                                            LEFT JOIN FC ON FC."Seq" = 1 -- 清償作業檔(FacClose)的最後一筆結清日期
                                                        AND FC."CustNo" = LBT."CustNo"
                                                        AND FC."FacmNo" = LBT."FacmNo"
                                            LEFT JOIN "TxBizDate" TBD ON TBD."DateCode" = ''ONLINE''
                                            WHERE NVL(FC."CloseDate",0) >= 19110101 -- 確保有結清日期
                                              AND TRUNC(MONTHS_BETWEEN(TO_DATE(TO_CHAR(TBD."TbsDyf"), ''YYYYMMDD''), TO_DATE(TO_CHAR(FC."CloseDate"), ''YYYYMMDD''))) >= 5 * 12 -- 結清滿五年';
        TYPE t_dataRecord IS RECORD
                             (
                                 "CustNo" NUMBER(7),
                                 "FacmNo" NUMBER(3),
                                 "BormNo" NUMBER(3)
                             );
        TYPE t_dataTable IS TABLE OF t_dataRecord;
        v_dataRecords     t_dataTable;

        TYPE t_archiveTables IS TABLE OF "TxArchiveTable"%ROWTYPE;
        v_archiveTables   t_archiveTables;
        v_lastResult      NUMBER(1);
        v_successfulCount NUMBER(7)      := 0;
        v_nextBatchNo     NUMBER(3);

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

        -- 存取搬運目標資料
        EXECUTE IMMEDIATE v_query BULK COLLECT INTO v_dataRecords;

        -- 檢查筆數
        IF v_archiveTables.COUNT = 0
        THEN
            DBMS_OUTPUT.PUT_LINE('TxArchiveTables 中, TYPE 為 ' || DBMS_ASSERT.ENQUOTE_LITERAL(v_archiveType) ||
                                 ' 的資料數為 0, 放棄搬運');
            RETURN;
        end if;

        IF v_dataRecords.COUNT = 0
        THEN
            DBMS_OUTPUT.PUT_LINE('沒有需要搬運的資料，結束搬運');
            RETURN;
        end if;

        -- 開始搬運
        FOR i IN v_dataRecords.FIRST .. v_dataRecords.LAST
            LOOP
                FOR j IN v_archiveTables.FIRST .. v_archiveTables.LAST
                    LOOP
                        DBMS_OUTPUT.PUT_LINE('開始搬運 ' || DBMS_ASSERT.ENQUOTE_LITERAL(v_dataRecords(i)."CustNo" ||
                                                                                    '-' ||
                                                                                    v_dataRecords(i)."FacmNo" ||
                                                                                    '-' || v_dataRecords(i)."BormNo") ||
                                             ' 到 ' ||
                                             DBMS_ASSERT.ENQUOTE_NAME(v_archiveTables(j)."TableName", FALSE));

                        -- 搬運 block starts
                        -- MERGE INTO 會跳過已經有的資料，因此不需處理重複資料問題
                        BEGIN
                            v_lastResult := "Fn_MoveTableBetweenSchemas"(v_archiveType,
                                                                         v_nextBatchNo,
                                                                         v_archiveTables(j)."TableName",
                                                                         v_archiveTables(j)."Conditions",
                                                                         v_dataRecords(i)."CustNo",
                                                                         v_dataRecords(i)."FacmNo",
                                                                         v_dataRecords(i)."BormNo",
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
                    end loop;
            end loop;

        DBMS_OUTPUT.PUT_LINE('搬運結束。成功數量：' || v_successfulCount || ' / ' || v_dataRecords.COUNT * v_archiveTables.COUNT);
    end;
end;
/

