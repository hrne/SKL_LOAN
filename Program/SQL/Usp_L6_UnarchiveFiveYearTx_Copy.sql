CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_L6_UnarchiveFiveYearTx_Copy"(
    v_custNo IN NUMBER, v_facmNo IN NUMBER, v_bormNo IN NUMBER, v_tbsdyf IN NUMBER, v_empNo IN VARCHAR2
) IS
BEGIN
    DECLARE
        v_archiveType VARCHAR2(4) := '5YTX';
        v_tableName   VARCHAR2(9) := 'LoanBorTx';
        v_result      NUMBER(1);
        v_nextBatchNo NUMBER(3);
    BEGIN
        -- 先存取TxArchiveTableLog, 取得這次執行的批次編號
        SELECT NVL(MAX(TATL."BatchNo"), 0) + 1 AS "NextBatchNo"
        INTO v_nextBatchNo
        FROM "TxArchiveTableLog" TATL
        WHERE TATL."ExecuteDate" = v_tbsdyf;

        -- 執行解封存
        v_result := "Fn_MoveTableBetweenSchemas"(v_archiveType,
                                                 v_nextBatchNo,
                                                 v_tableName,
                                                 3,
                                                 v_custNo,
                                                 v_facmNo,
                                                 v_bormNo,
                                                 1,
                                                 v_tbsdyf,
                                                 v_empNo);
    END;
end;
/

