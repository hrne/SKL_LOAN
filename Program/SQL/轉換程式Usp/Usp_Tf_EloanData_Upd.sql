--------------------------------------------------------
--  DDL for Procedure Usp_Tf_EloanData_Upd
--------------------------------------------------------
set define off;

  CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Usp_Tf_EloanData_Upd" 
-- 2022-05-17 ST1 Wei 新增: 更新段小段資料,資料來源為SKL承憲提供
(
    -- 參數
    JOB_START_TIME OUT TIMESTAMP, --程式起始時間
    JOB_END_TIME   OUT TIMESTAMP, --程式結束時間
    INS_CNT        OUT INT,       --新增資料筆數
    ERROR_MSG      OUT VARCHAR2   --錯誤訊息
)
AS
BEGIN
    -- 筆數預設0
    INS_CNT:=0;
    -- 記錄程式起始時間
    JOB_START_TIME := SYSTIMESTAMP;

    -- 更新資料
    MERGE INTO "ClBuilding" T
    using (
    select ECL.SEC_CODE
        , CB."ClCode1"
        , CB."ClCode2"
        , CB."ClNo"
        , ROW_NUMBER()
        OVER (
            PARTITION BY CB."ClCode1"
                    , CB."ClCode2"
                    , CB."ClNo"
            ORDER BY CASE
                    WHEN ECL.CLNUM = CB."ClNo"
                    THEN 0
                    ELSE 1 END
                , CB."ClNo"
        ) AS "Seq"
    from ELOAN_CL_DATA ECL
    left join "ClNoMap" CNM ON CNM."GdrId1" = ECL.CLNO1
                        AND CNM."GdrId2" = ECL.CLNO2
                        AND CNM."GdrNum" = ECL.CLNUM
                        AND CNM."LgtSeq" = NVL(ECL.CLSEQ,CNM."LgtSeq")
    left join "ClBuilding" CB ON CB."ClCode1" = CNM."ClCode1"
                            AND CB."ClCode2" = CNM."ClCode2"
                            AND CB."ClNo" = CNM."ClNo"
                            AND ECL.CLNO1 = 1
    where CASE
            WHEN CLNO1 = 1
                AND LPAD(CB."BdNo1",5,'0') || LPAD(CB."BdNo2",3,'0') = ECL.BUILDING_NO
                AND CB."AreaCode" = ECL.AREA_CODE
            THEN 1
        ELSE 0 END = 1
    ) S
    on (
        t."ClCode1" = s."ClCode1"
        and t."ClCode2" = s."ClCode2"
        and t."ClNo" = s."ClNo"
        and s."Seq" = 1
    )
    when matched then update set
    "IrCode" = S."SEC_CODE"
    ;

    MERGE INTO "ClLand" T
    using (
    select ECL.SEC_CODE
        , CL."ClCode1"
        , CL."ClCode2"
        , CL."ClNo"
        , ROW_NUMBER()
        OVER (
            PARTITION BY CL."ClCode1"
                    , CL."ClCode2"
                    , CL."ClNo"
            ORDER BY CASE
                    WHEN ECL.CLNUM = CL."ClNo"
                    THEN 0
                    ELSE 1 END
                , CL."ClNo"
        ) AS "Seq"
    from ELOAN_CL_DATA ECL
    left join "ClNoMap" CNM ON CNM."GdrId1" = ECL.CLNO1
                        AND CNM."GdrId2" = ECL.CLNO2
                        AND CNM."GdrNum" = ECL.CLNUM
                        AND CNM."LgtSeq" = NVL(ECL.CLSEQ,CNM."LgtSeq")
    left join "ClLand" CL ON CL."ClCode1" = CNM."ClCode1"
                        AND CL."ClCode2" = CNM."ClCode2"
                        AND CL."ClNo" = CNM."ClNo"
                        AND ECL.CLNO1 = 2
    where CASE
        WHEN CLNO1 = 2
                AND CL."AreaCode" = ECL.AREA_CODE
        THEN 1
        ELSE 0 END = 1
    ) S
    on (
        t."ClCode1" = s."ClCode1"
        and t."ClCode2" = s."ClCode2"
        and t."ClNo" = s."ClNo"
        and s."Seq" = 1
    )
    when matched then update set
    "IrCode" = S."SEC_CODE"
    ;

    -- 記錄程式結束時間
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    ERROR_MSG := SQLERRM || CHR(13) || CHR(10) || dbms_utility.format_error_backtrace;
    -- "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_Tf_EloanData_Upd',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
END;

/
