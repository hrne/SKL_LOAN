CREATE OR REPLACE NONEDITIONABLE FUNCTION "Fn_GetDirectorManager" 
(
    "InputEmployeeNo" IN VARCHAR2 -- 員工編號
) RETURN VARCHAR2
AS 
  v_EmployeeNo VARCHAR2(10);
  v_AgLevel VARCHAR2(10);
  v_AgentCode VARCHAR2(20);
  v_DirectorId VARCHAR2(20);
  v_Fullname NVARCHAR2(50);
BEGIN
--------------------------------------------------------
--  DDL for Function Fn_GetDirectorManager
--  Example:
--  SELECT "Fn_GetDirectorManager"(CE."EmployeeNo") AS "DirectorManager"
--  FROM "CdEmp" CE
--------------------------------------------------------
  SELECT "EmployeeNo" -- 員工編號
       , "AgLevel" -- 職級
       , "AgentCode" -- 員工系統編號
       , "DirectorId" -- 上級主管系統編號
       , "Fullname" -- 員工姓名
  INTO v_EmployeeNo
     , v_AgLevel
     , v_AgentCode
     , v_DirectorId
     , v_Fullname
  FROM "CdEmp" -- 員工檔
  WHERE "EmployeeNo" = "InputEmployeeNo"
  ;

  IF v_AgLevel LIKE 'H%' THEN -- 若職級為H開頭則為區經理
    RETURN v_Fullname;
  ELSIF v_AgentCode = v_DirectorId THEN -- 若自己是自己的上級,則結束
    RETURN NULL;
  ELSE -- 遞迴處理
    SELECT "EmployeeNo" INTO v_EmployeeNo
    FROM "CdEmp"
    WHERE "AgentCode" = v_DirectorId;

    RETURN "Fn_GetDirectorManager"(v_EmployeeNo);
  END IF;

  -- 例外處理
  Exception
  WHEN OTHERS THEN
  RETURN NULL;
END;

/