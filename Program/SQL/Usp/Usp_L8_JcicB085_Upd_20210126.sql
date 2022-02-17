-- �{���\��G���@ JcicB085 �C���p�x�b���ഫ�����
-- ����ɾ��G�C�멳��ק妸(����e)
-- ����覡�GEXEC "Usp_L8_JcicB085_Upd"(20200331,'CSCHEN');
--

CREATE OR REPLACE PROCEDURE "Usp_L8_JcicB085_Upd"
(
    -- �Ѽ�
    TBSDYF         IN  INT,        -- �t����~��(�褸)
    EmpNo          IN  VARCHAR2    -- �g��
)
AS
BEGIN
  DECLARE
    INS_CNT        INT;         -- �s�W����
    UPD_CNT        INT;         -- ��s����
    JOB_START_TIME TIMESTAMP;   -- �O���{���_�l�ɶ�
    JOB_END_TIME   TIMESTAMP;   -- �O���{�������ɶ�
    YYYYMM         INT;         -- ����~��
    LYYYYMM        INT;         -- �W��~��
    MM             INT;         -- ������
    YYYY           INT;         -- ����~��
  BEGIN
    INS_CNT := 0;
    UPD_CNT := 0;

    -- �O���{���_�l�ɶ�
    JOB_START_TIME := SYSTIMESTAMP;

    --�@����~��
    YYYYMM := TBSDYF / 100;
    --
    MM := MOD(YYYYMM, 100);
    YYYY := TRUNC(YYYYMM / 100);
    IF MM = 1 THEN
       LYYYYMM := (YYYY - 1) * 100 + 12;
    ELSE
       LYYYYMM := YYYYMM - 1;
    END IF;


    -- �R���¸��
    DBMS_OUTPUT.PUT_LINE('DELETE JcicB085');

    DELETE FROM "JcicB085"
    WHERE "DataYM" = YYYYMM
      ;


    -- �g�J���
    DBMS_OUTPUT.PUT_LINE('INSERT JcicB085');

    INSERT INTO "JcicB085"
    SELECT
           YYYYMM                                AS "DataYM"            -- ��Ʀ~��
         , '85'                                  AS "DataType"          -- ��ƧO
         , CASE
             WHEN FLOOR( NVL(L."DrawdownDate", 0) / 100 ) < 191100 THEN FLOOR( NVL(L."DrawdownDate", 0) / 100 )
             ELSE FLOOR( NVL(L."DrawdownDate", 19110000) / 100 ) - 191100
           END                                   AS "RenewYM"           -- �ഫ�b���~��
         , "CustMain"."CustId"                   AS "CustId"            -- �«H��IDN/BAN
         , '458'                                 AS "BefBankItem"       -- �ഫ�e�`��N��
         , '0001'                                AS "BefBranchItem"     -- �ഫ�e����N��
         , ' '                                   AS "Filler6"           -- �ť�
         , TRIM(to_char(M."CustNo",'0000000')) || TRIM(to_char(M."OldFacmNo",'000')) || TRIM(to_char(M."OldBormNo",'000'))
                                                 AS "BefAcctNo"         -- �ഫ�e�b��
         , '458'                                 AS "AftBankItem"       -- �ഫ���`��N��
         , '0001'                                AS "AftBranchItem"     -- �ഫ�����N��
         , ' '                                   AS "Filler10"          -- �ť�
         , TRIM(to_char(M."CustNo",'0000000')) || TRIM(to_char(M."NewFacmNo",'000')) || TRIM(to_char(M."NewBormNo",'000'))
                                                 AS "AftAcctNo"         -- �ഫ��b��
         , ' '                                   AS "Filler12"          -- �ť�
         , JOB_START_TIME                        AS "CreateDate"        -- ���ɤ���ɶ�
         , EmpNo                                 AS "CreateEmpNo"       -- ���ɤH��
         , JOB_START_TIME                        AS "LastUpdate"        -- �̫��s����ɶ�
         , EmpNo                                 AS "LastUpdateEmpNo"   -- �̫��s�H��
    FROM   "JcicB201"
        LEFT JOIN "AcLoanRenew"  M  ON M."CustNo"    = SUBSTR("JcicB201"."AcctNo", 1, 7)
                                   AND M."NewFacmNo" = SUBSTR("JcicB201"."AcctNo", 8, 3)
                                   AND M."NewBormNo" = SUBSTR("JcicB201"."AcctNo",11, 3)
        LEFT JOIN "CustMain"        ON "CustMain"."CustNo"  = M."CustNo"
        LEFT JOIN "LoanBorMain"  L  ON L."CustNo"    = M."CustNo"
                                   AND L."FacmNo"    = M."NewFacmNo"
                                   AND L."BormNo"    = M."NewBormNo"
    WHERE  "JcicB201"."DataYM" =  YYYYMM
      AND  FLOOR( NVL(L."DrawdownDate", 19110000) / 100 ) = YYYYMM	-- �����ഫ
      AND  M."CustNo" IS NOT NULL
      ;

    INS_CNT := INS_CNT + sql%rowcount;

    DBMS_OUTPUT.PUT_LINE('INSERT JcicB085 END: INS_CNT=' || INS_CNT);


    -- �O���{�������ɶ�
    JOB_END_TIME := SYSTIMESTAMP;

    commit;

    -- �ҥ~�B�z
--    Exception
--    WHEN OTHERS THEN
--   "Usp_Tf_ErrorLog_Ins"(BATCH_LOG_UKEY,'Usp_L8_JcicB085_Upd',SQLCODE,SQLERRM,dbms_utility.format_error_backtrace);
  END;
END;
