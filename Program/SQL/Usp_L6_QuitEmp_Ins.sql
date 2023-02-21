create or replace NONEDITIONABLE PROCEDURE "Usp_L6_QuitEmp_Ins" 
( 
    -- 參數 
    "InputEmpNo" IN VARCHAR2   --執行人員員編 
) 
AS 
BEGIN  
-- exec "Usp_L6_QuitEmp_Ins"('001702'); 

  -- 刪除舊資料 
  EXECUTE IMMEDIATE 'ALTER TABLE "QuitEmp" DISABLE PRIMARY KEY CASCADE'; 
  EXECUTE IMMEDIATE 'TRUNCATE TABLE "QuitEmp" DROP STORAGE'; 
  EXECUTE IMMEDIATE 'ALTER TABLE "QuitEmp" ENABLE PRIMARY KEY'; 
 
  -- 寫入資料 
  INSERT INTO "QuitEmp" (
      "EmpNo"
    , "QuitDate"
    , "CreateDate"
    , "CreateEmpNo"
    , "LastUpdate"
    , "LastUpdateEmpNo"
  )
  WITH sortingData AS (
    SELECT TRIM(EMPLOYEE_NO)  AS "EmpNo"
         , NVL(TO_NUMBER(TO_CHAR(QUIT_DATE,'YYYYMMDD')),0)
                              AS "QuitDate"
         , ROW_NUMBER()
           OVER (
            PARTITION BY TRIM(EMPLOYEE_NO)
            ORDER BY QUIT_DATE DESC
           )                  AS "Seq"
    FROM TPSAS047
    WHERE AG_STATUS_CODE = 5
      AND NVL(TRIM(EMPLOYEE_NO),' ') != ' '
      AND NVL(TO_NUMBER(TO_CHAR(QUIT_DATE,'YYYYMMDD')),0) != 0
  )
  SELECT "EmpNo"
       , "QuitDate"
       , SYSTIMESTAMP             AS "CreateDate"      -- 建檔日期時間 DATE	
       , SUBSTR("InputEmpNo",0,6) AS "CreateEmpNo"     -- 建檔人員 VARCHAR2 6
       , SYSTIMESTAMP             AS "LastUpdate"      -- 最後更新日期時間 DATE	
       , SUBSTR("InputEmpNo",0,6) AS "LastUpdateEmpNo" -- 最後更新人員 VARCHAR2 6
  FROM sortingData
  WHERE "Seq" = 1
  ; 
  
  commit;
END;