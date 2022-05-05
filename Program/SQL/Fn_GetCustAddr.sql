--------------------------------------------------------
--  DDL for Function Fn_GetCustAddr
--  Example:
--  SELECT "Fn_GetCustAddr"(CM."CustUKey",'0')
--  FROM "CustMain" CM
--------------------------------------------------------

CREATE OR REPLACE NONEDITIONABLE FUNCTION "Fn_GetCustAddr" 
(
    "InputCustUKey" IN VARCHAR2,  -- 客戶主檔.客戶識別碼(CustMain.CustUKey)
    "ReturnType"    IN VARCHAR2   -- 回傳種類(0:戶籍地址;1:通訊地址)
) RETURN NVARCHAR2 IS "ResultCustAddr" NVARCHAR2(1000);
BEGIN
    SELECT CASE
             -- 0:戶籍地址
             WHEN "ReturnType" = 0
                  AND CM."RegSection" IS NULL
                  AND CM."RegAlley" IS NULL
                  AND CM."RegLane" IS NULL
                  AND CM."RegNum" IS NULL
                  AND CM."RegNumDash" IS NULL
                  AND CM."RegFloor" IS NULL
                  AND CM."RegFloorDash" IS NULL
             THEN NVL(CM."RegRoad",N'') -- 路名
             WHEN "ReturnType" = 0
             THEN NVL(CITY."CityItem",N'') -- 縣市
               || NVL(AREA."AreaItem",N'') -- 行政區
               || NVL(CM."RegRoad",N'') -- 路名
               -- 段
               || CASE
                    WHEN CM."RegSection" IS NOT NULL
                    THEN TO_NCHAR(CM."RegSection") || N'段'
                  ELSE N'' END
               -- 巷
               || CASE
                    WHEN CM."RegAlley" IS NOT NULL
                    THEN TO_NCHAR(CM."RegAlley") || N'巷'
                  ELSE N'' END
               -- 弄
               || CASE
                    WHEN CM."RegLane" IS NOT NULL
                    THEN TO_NCHAR(CM."RegLane") || N'弄'
                  ELSE N'' END
               -- 號
               || CASE
                    WHEN CM."RegNum" IS NOT NULL
                    THEN TO_NCHAR(CM."RegNum") || N'號'
                  ELSE N'' END
               -- 號之
               || CASE
                    WHEN CM."RegNumDash" IS NOT NULL
                    THEN N'之' || TO_NCHAR(CM."RegNumDash")
                  ELSE N'' END
               -- 號之與樓的分隔符號
               || CASE
                    WHEN CM."RegNumDash" IS NOT NULL AND CM."RegFloor" IS NOT NULL
                    THEN N','
                  ELSE N'' END
               -- 樓
               || CASE
                    WHEN CM."RegFloor" IS NOT NULL
                    THEN TO_NCHAR(CM."RegFloor") || N'樓'
                  ELSE N'' END
               -- 樓之
               || CASE
                    WHEN CM."RegFloorDash" IS NOT NULL
                    THEN N'之' || TO_NCHAR(CM."RegFloorDash")
                  ELSE N'' END
             -- 1:通訊地址
             WHEN "ReturnType" = 1
                  AND CM."CurrSection" IS NULL
                  AND CM."CurrAlley" IS NULL
                  AND CM."CurrLane" IS NULL
                  AND CM."CurrNum" IS NULL
                  AND CM."CurrNumDash" IS NULL
                  AND CM."CurrFloor" IS NULL
                  AND CM."CurrFloorDash" IS NULL
             THEN NVL(CM."CurrRoad",N'') -- 路名
             WHEN "ReturnType" = 1
             THEN NVL(CITY."CityItem",N'') -- 縣市
               || NVL(AREA."AreaItem",N'') -- 行政區
               || NVL(CM."CurrRoad",N'') -- 路名
               -- 段
               || CASE
                    WHEN CM."CurrSection" IS NOT NULL
                    THEN TO_NCHAR(CM."CurrSection") || N'段'
                  ELSE N'' END
               -- 巷
               || CASE
                    WHEN CM."CurrAlley" IS NOT NULL
                    THEN TO_NCHAR(CM."CurrAlley") || N'巷'
                  ELSE N'' END
               -- 弄
               || CASE
                    WHEN CM."CurrLane" IS NOT NULL
                    THEN TO_NCHAR(CM."CurrLane") || N'弄'
                  ELSE N'' END
               -- 號
               || CASE
                    WHEN CM."CurrNum" IS NOT NULL
                    THEN TO_NCHAR(CM."CurrNum") || N'號'
                  ELSE N'' END
               -- 號之
               || CASE
                    WHEN CM."CurrNumDash" IS NOT NULL
                    THEN N'之' || TO_NCHAR(CM."CurrNumDash")
                  ELSE N'' END
               -- 號之與樓的分隔符號
               || CASE
                    WHEN CM."CurrNumDash" IS NOT NULL AND CM."CurrFloor" IS NOT NULL
                    THEN N','
                  ELSE N'' END
               -- 樓
               || CASE
                    WHEN CM."CurrFloor" IS NOT NULL
                    THEN TO_NCHAR(CM."CurrFloor") || N'樓'
                  ELSE N'' END
               -- 樓之
               || CASE
                    WHEN CM."CurrFloorDash" IS NOT NULL
                    THEN N'之' || TO_NCHAR(CM."CurrFloorDash")
                  ELSE N'' END
             ELSE N'' END AS "TempAddr"
    INTO "ResultCustAddr"
    FROM "CustMain" CM
    LEFT JOIN "CdCity" CITY ON CITY."CityCode" = CASE
                                                   WHEN "ReturnType" = 0 THEN CM."RegCityCode"
                                                   WHEN "ReturnType" = 1 THEN CM."CurrCityCode"
                                                 ELSE '' END
    LEFT JOIN "CdArea" AREA ON AREA."CityCode" = CASE
                                                   WHEN "ReturnType" = 0 THEN CM."RegCityCode"
                                                   WHEN "ReturnType" = 1 THEN CM."CurrCityCode"
                                                 ELSE '' END
                           AND AREA."AreaCode" = CASE
                                                   WHEN "ReturnType" = 0 THEN CM."RegAreaCode"
                                                   WHEN "ReturnType" = 1 THEN CM."CurrAreaCode"
                                                 ELSE '' END
    WHERE CM."CustUKey" = "InputCustUKey";

    RETURN "ResultCustAddr";

    -- 例外處理
    Exception
    WHEN OTHERS THEN
    RETURN N'';
END;

/
