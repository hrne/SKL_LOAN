CREATE OR REPLACE PROCEDURE "Usp_Cp_StgCdEmp_Ins"
(
"EmpNo" IN VARCHAR2
)
AS
BEGIN
  IF USER = 'ITXADMIN' THEN
    RETURN;
  END IF;

  EXECUTE IMMEDIATE 'TRUNCATE TABLE "StgCdEmp" DROP STORAGE';

  INSERT INTO "StgCdEmp" (
    "AGENT_CODE",
    "COMM_LINE_CODE",
    "COMM_LINE_TYPE",
    "ORIG_INTRODUCER_ID",
    "INTRODUCER_IND",
    "REGISTER_LEVEL",
    "REGISTER_DATE",
    "CENTER_CODE",
    "ADMINISTRAT_ID",
    "INPUT_DATE",
    "INPUT_USER",
    "AG_STATUS_CODE",
    "AG_STATUS_DATE",
    "TRAN_DATE",
    "TRAN_USER",
    "RE_REGISTER_DATE",
    "DIRECTOR_ID",
    "DIRECTOR_ID_F",
    "INTRODUCER_ID",
    "INTRODUCER_ID_F",
    "AG_LEVEL",
    "LAST_LEVEL",
    "LEVEL_DATE",
    "TOP_LEVEL",
    "OCCP_IND",
    "QUOTA_AMT",
    "APPL_TYPE",
    "TAX_RATE",
    "SOCIAL_INSU_CLASS",
    "PROMOT_LEVEL_YM",
    "DIRECTOR_YM",
    "RECORD_DATE",
    "EX_RECORD_DATE",
    "EX_TR_DATE",
    "EX_TR_IDENT",
    "EX_TR_IDENT2",
    "EX_TR_IDENT3",
    "EX_TR_DATE3",
    "REGISTER_BEFORE",
    "DIRECTOR_AFTER",
    "MEDICAL_CODE",
    "EX_CHG_DATE",
    "EX_DEL_DATE",
    "APPL_CODE",
    "FIRST_REG_DATE",
    "AGIN_SOURCE",
    "AGUI_CENTER",
    "AGENT_ID",
    "TOP_ID",
    "AG_DEGREE",
    "COLLECT_IND",
    "AG_TYPE_1",
    "EMPLOYEE_NO",
    "CONTRACT_IND",
    "CONTRACT_IND_YM",
    "AG_TYPE_2",
    "AG_TYPE_3",
    "AG_TYPE_4",
    "AGIN_IND1",
    "AG_PO_IND",
    "AG_DOC_IND",
    "NEW_HIRE_TYPE",
    "AG_CUR_IND",
    "AG_SEND_TYPE",
    "AG_SEND_NO",
    "REGISTER_DATE_2",
    "AG_RETURN_DATE",
    "AG_TRANSFER_DATE_F",
    "AG_TRANSFER_DATE",
    "PROMOT_YM",
    "PROMOT_YM_F",
    "AG_POST_CHG_DATE",
    "FAMILIES_TAX",
    "AGENT_CODE_I",
    "AG_LEVEL_SYS",
    "AG_POST_IN",
    "CENTER_CODE_ACC",
    "EVALUE_IND",
    "EVALUE_IND_1",
    "BATCH_NO",
    "EVALUE_YM",
    "AG_TRANSFER_CODE",
    "FULLNAME",
    "BIRTH",
    "EDUCATION",
    "LR_IND",
    "PROCESS_DATE",
    "QUIT_DATE",
    "CENTER_SHORT_NAME",
    "CENTER_CODE_NAME",
    "CENTER_CODE_1",
    "CENTER_CODE_1_SHORT",
    "CENTER_CODE_1_NAME",
    "CENTER_CODE_2",
    "CENTER_CODE_2_SHORT",
    "CENTER_CODE_2_NAME",
    "CENTER_CODE_ACC_1",
    "CENTER_CODE_ACC_1_NAME",
    "CENTER_CODE_ACC_2",
    "CENTER_CODE_ACC_2_NAME",
    "AG_POST",
    "LEVEL_NAME_CHS",
    "LR_SYSTEM_TYPE",
    "SENIORITY_YY",
    "SENIORITY_MM",
    "SENIORITY_DD",
    "AGLA_PROCESS_IND",
    "STATUS_CODE",
    "AGLA_CANCEL_REASON",
    "IS_ANN_APPL_DATE",
    "RECORD_DATE_C",
    "STOP_REASON",
    "STOP_STR_DATE",
    "STOP_END_DATE",
    "IFP_DATE",
    "EFFECT_STR_DATE",
    "EFFECT_END_DATE",
    "ANN_APPL_DATE",
    "CENTER_CODE_ACC_NAME",
    "RE_HIRE_CODE",
    "RSVD_ADMIN_CODE",
    "ACCOUNT",
    "PRP_DATE",
    "ZIP",
    "ADDRESS",
    "PHONE_H",
    "PHONE_C",
    "SALES_QUAL_IND",
    "AGSQ_START_DATE",
    "PINYIN_NAME_INDI"
)
  SELECT
    "AGENT_CODE",
    "COMM_LINE_CODE",
    "COMM_LINE_TYPE",
    "ORIG_INTRODUCER_ID",
    "INTRODUCER_IND",
    "REGISTER_LEVEL",
    "REGISTER_DATE",
    "CENTER_CODE",
    "ADMINISTRAT_ID",
    "INPUT_DATE",
    "INPUT_USER",
    "AG_STATUS_CODE",
    "AG_STATUS_DATE",
    "TRAN_DATE",
    "TRAN_USER",
    "RE_REGISTER_DATE",
    "DIRECTOR_ID",
    "DIRECTOR_ID_F",
    "INTRODUCER_ID",
    "INTRODUCER_ID_F",
    "AG_LEVEL",
    "LAST_LEVEL",
    "LEVEL_DATE",
    "TOP_LEVEL",
    "OCCP_IND",
    "QUOTA_AMT",
    "APPL_TYPE",
    "TAX_RATE",
    "SOCIAL_INSU_CLASS",
    "PROMOT_LEVEL_YM",
    "DIRECTOR_YM",
    "RECORD_DATE",
    "EX_RECORD_DATE",
    "EX_TR_DATE",
    "EX_TR_IDENT",
    "EX_TR_IDENT2",
    "EX_TR_IDENT3",
    "EX_TR_DATE3",
    "REGISTER_BEFORE",
    "DIRECTOR_AFTER",
    "MEDICAL_CODE",
    "EX_CHG_DATE",
    "EX_DEL_DATE",
    "APPL_CODE",
    "FIRST_REG_DATE",
    "AGIN_SOURCE",
    "AGUI_CENTER",
    "AGENT_ID",
    "TOP_ID",
    "AG_DEGREE",
    "COLLECT_IND",
    "AG_TYPE_1",
    "EMPLOYEE_NO",
    "CONTRACT_IND",
    "CONTRACT_IND_YM",
    "AG_TYPE_2",
    "AG_TYPE_3",
    "AG_TYPE_4",
    "AGIN_IND1",
    "AG_PO_IND",
    "AG_DOC_IND",
    "NEW_HIRE_TYPE",
    "AG_CUR_IND",
    "AG_SEND_TYPE",
    "AG_SEND_NO",
    "REGISTER_DATE_2",
    "AG_RETURN_DATE",
    "AG_TRANSFER_DATE_F",
    "AG_TRANSFER_DATE",
    "PROMOT_YM",
    "PROMOT_YM_F",
    "AG_POST_CHG_DATE",
    "FAMILIES_TAX",
    "AGENT_CODE_I",
    "AG_LEVEL_SYS",
    "AG_POST_IN",
    "CENTER_CODE_ACC",
    "EVALUE_IND",
    "EVALUE_IND_1",
    "BATCH_NO",
    "EVALUE_YM",
    "AG_TRANSFER_CODE",
    "FULLNAME",
    "BIRTH",
    "EDUCATION",
    "LR_IND",
    "PROCESS_DATE",
    "QUIT_DATE",
    "CENTER_SHORT_NAME",
    "CENTER_CODE_NAME",
    "CENTER_CODE_1",
    "CENTER_CODE_1_SHORT",
    "CENTER_CODE_1_NAME",
    "CENTER_CODE_2",
    "CENTER_CODE_2_SHORT",
    "CENTER_CODE_2_NAME",
    "CENTER_CODE_ACC_1",
    "CENTER_CODE_ACC_1_NAME",
    "CENTER_CODE_ACC_2",
    "CENTER_CODE_ACC_2_NAME",
    "AG_POST",
    "LEVEL_NAME_CHS",
    "LR_SYSTEM_TYPE",
    "SENIORITY_YY",
    "SENIORITY_MM",
    "SENIORITY_DD",
    "AGLA_PROCESS_IND",
    "STATUS_CODE",
    "AGLA_CANCEL_REASON",
    "IS_ANN_APPL_DATE",
    "RECORD_DATE_C",
    "STOP_REASON",
    "STOP_STR_DATE",
    "STOP_END_DATE",
    "IFP_DATE",
    "EFFECT_STR_DATE",
    "EFFECT_END_DATE",
    "ANN_APPL_DATE",
    "CENTER_CODE_ACC_NAME",
    "RE_HIRE_CODE",
    "RSVD_ADMIN_CODE",
    "ACCOUNT",
    "PRP_DATE",
    "ZIP",
    "ADDRESS",
    "PHONE_H",
    "PHONE_C",
    "SALES_QUAL_IND",
    "AGSQ_START_DATE",
    "PINYIN_NAME_INDI"
  FROM ITXADMIN."StgCdEmp";

  Exception 
  WHEN OTHERS THEN
  "Usp_L9_UspErrorLog_Ins"(
    'Usp_Cp_StgCdEmp_Ins',
    SQLCODE,
    SQLERRM,
    dbms_utility.format_error_backtrace,
    "EmpNo"
  );
END;