create or replace NONEDITIONABLE PROCEDURE "Usp_L9_MonthlyFacBal_Upd" 
(
    -- �Ѽ�
    TBSDYF         IN  INT,        -- �t����~��(�褸)
    EmpNo          IN  VARCHAR2,   -- �g��
    JobTxSeq       IN  VARCHAR2    -- �Ұʧ妸������Ǹ�
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
    "ThisMonthEndDate" INT;     -- ����멳��
  BEGIN
    INS_CNT :=0;
    UPD_CNT :=0;

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

    SELECT "TmnDyf"
    INTO "ThisMonthEndDate"
    FROM "TxBizDate"
    WHERE "DateCode" = 'ONLINE'
    ;

    -- �R���¸��
    DBMS_OUTPUT.PUT_LINE('DELETE MonthlyFacBal');

    DELETE FROM "MonthlyFacBal"
    WHERE "YearMonth" = YYYYMM
    ;

    -- �g�J���
    DBMS_OUTPUT.PUT_LINE('INSERT MonthlyFacBal');

    INSERT INTO "MonthlyFacBal"
    SELECT
           YYYYMM                     AS "YearMonth"           -- ��Ʀ~��
          ,L."CustNo"                 AS "CustNo"              -- �ḹ
          ,L."FacmNo"                 AS "FacmNo"              -- �B��
          ,L."PrevIntDate"            AS "PrevIntDate"         -- ú������
          ,L."NextIntDate"            AS "NextIntDate"         -- ��ú����
          ,NVL(B."DueDate",0)         AS "DueDate"             -- �̪���ú��
          -- �Y ��ú���� < �t����~��("ThisMonthEndDate")
          -- �h �p��O������
          -- �_�h �\�s
          ,CASE
             WHEN NVL(B."CustNo",0) = 0
             THEN L."OvduTerm" 
             WHEN B."MaturityDate" < "ThisMonthEndDate"
              AND B."MaturityDate" < B."NextPayIntDate" -- ��ú��>�����ɥΨ����p��
              AND L."PrinBalance" + L."BadDebtBal" <> 0
             THEN TRUNC(MONTHS_BETWEEN(TO_DATE("ThisMonthEndDate",'YYYY-MM-DD'), TO_DATE(B."MaturityDate",'YYYY-MM-DD')))
             WHEN B."NextPayIntDate" < "ThisMonthEndDate" AND B."NextPayIntDate" > 0
              AND L."PrinBalance" + L."BadDebtBal" <> 0
             THEN TRUNC(MONTHS_BETWEEN(TO_DATE("ThisMonthEndDate",'YYYY-MM-DD'), TO_DATE(B."NextPayIntDate",'YYYY-MM-DD')))
           ELSE 0 END                 AS "OvduTerm"            -- '�O������';
          -- �Y ��ú���� <= �t����~��("ThisMonthEndDate")
          -- �h �p��O���Ѽ�
          -- �_�h �\�s
          ,CASE
             WHEN NVL(B."CustNo",0) = 0
             THEN L."OvduDays" 
             WHEN B."MaturityDate" < "ThisMonthEndDate"
              AND B."MaturityDate" < B."NextPayIntDate" -- ��ú��>�����ɥΨ����p��
              AND L."PrinBalance" + L."BadDebtBal" <> 0
             THEN TO_DATE("ThisMonthEndDate",'YYYY-MM-DD')  - TO_DATE(B."MaturityDate",'YYYY-MM-DD') 
             WHEN B."NextPayIntDate" <= "ThisMonthEndDate" AND B."NextPayIntDate" > 0
              AND L."PrinBalance" + L."BadDebtBal" <> 0
             THEN TO_DATE("ThisMonthEndDate",'YYYY-MM-DD')  - TO_DATE(B."NextPayIntDate",'YYYY-MM-DD') 
           ELSE 0 END                 AS "OvduDays"            -- '�O���Ѽ�';
          ,L."CurrencyCode"           AS "CurrencyCode"        -- ���O
          ,L."PrinBalance"            AS "PrinBalance"         -- �����l�B
          ,L."BadDebtBal"             AS "BadDebtBal"          -- �b�b�l�B
          ,L."AccCollPsn"             AS "AccCollPsn"          -- �ʦ���
          ,L."LegalPsn"               AS "LegalPsn"            -- �k�ȤH��
          ,DECODE(L."Status", 4, 0, L."Status")
                                      AS "Status"              -- ��p
          ,L."AcctCode"               AS "AcctCode"            -- �~�Ȭ�إN��
          ,L."FacAcctCode"            AS "FacAcctCode"         -- �B�׷~�Ȭ��
          ,L."ClCustNo"               AS "ClCustNo"            -- �P��O�~�ḹ
          ,L."ClFacmNo"               AS "ClFacmNo"            -- �P��O�~�B��
          ,L."ClRowNo"                AS "ClRowNo"             -- �P��O�~�ǦC��
          ,L."RenewCode"              AS "RenewCode"           -- �i���O��
          ,FAC."ProdNo"               AS "ProdNo"              -- �ӫ~�N�X
          ,'000'                      AS "AcBookCode"          -- �b�U�O
          , NULL                      AS "EntCode"             -- �����O
          , NULL                      AS "RelsCode"            -- (��)�Q�`���Y�H¾��
          , NULL                      AS "DepartmentCode"      -- �ץ����ݳ��
          , 0                         AS "UnpaidPrincipal"     -- �w����^������
          , 0                         AS "UnpaidInterest"      -- �w����Q��
          , 0                         AS "UnpaidBreachAmt"     -- �w����H����
          , 0                         AS "UnpaidDelayInt"      -- �w���������
          , 0                         AS "AcdrPrincipal"       -- ������^������
          , 0                         AS "AcdrInterest"        -- ������Q��
          , 0                         AS "AcdrBreachAmt"       -- ������H����
          , 0                         AS "AcdrDelayInt"        -- �����������
          , 0                         AS "FireFee"             -- ���I�O�� DECIMAL 16 2
          , 0                         AS "LawFee"              -- �k�ȶO�� DECIMAL 16 2
          , 0                         AS "ModifyFee"           -- ���ܤ���O DECIMAL 16 2
          , 0                         AS "AcctFee"             -- �b�޶O�� DECIMAL 16 2
          , 0                         AS "ShortfallPrin"       -- �uú���� DECIMAL 16 2
          , 0                         AS "ShortfallInt"        -- �uú�Q�� DECIMAL 16 2
          , 0                         AS "TempAmt"             -- �Ȧ����B DECIMAL 16 2
          , 0                         AS "ClCode1"             -- �D�n��O�~�N��1
          , 0                         AS "ClCode2"             -- �D�n��O�~�N��2
          , 0                         AS "ClNo"                -- �D�n��O�~�s��
          , NULL                      AS "CityCode"            -- �D�n��O�~�a�ϧO 
          , 0                         as "OvduDate"            -- ��ʦ����
          , 0                         AS "OvduPrinBal"         -- �ʦ������l�B           
          , 0                         AS "OvduIntBal"          -- �ʦ��Q���l�B           
          , 0                         AS "OvduBreachBal"       -- �ʦ��H�����l�B          
          , 0                         AS "OvduBal"             -- �ʦ��l�B(�b�b�l�B)   
          , 0                         AS "LawAmount"           -- �L��O���v�]�w���B(�k�ȶi��:901)
          , ''                        AS "AssetClass"          -- �겣�������N��
          , 0                         AS "StoreRate"           -- �p���Q�v
          ,JOB_START_TIME             AS "CreateDate"          -- ���ɤ���ɶ�
          ,EmpNo                      AS "CreateEmpNo"         -- ���ɤH��
          ,JOB_START_TIME             AS "LastUpdate"          -- �̫��s����ɶ�
          ,EmpNo                      AS "LastUpdateEmpNo"     -- �̫��s�H��
          ,'00A'                      AS "AcSubBookCode"       -- �Ϲj�b�U
          ,''                         AS "LawAssetClass"       -- �L��O�겣�����N��
          ,''                         AS "AssetClass2"       -- �겣�������N��2(����O����)
          ,''                         AS "BankRelationFlag"       -- �O�_���Q�`���Y�H
          ,''                      AS "GovProjectFlag"       -- �F���ʱM�׶U��
          ,''                      AS "BuildingFlag"       -- �ؿv�U�ڰO��
          ,''                      AS "SpecialAssetFlag"       -- �S�w�겣�O��
    FROM "CollList" L
    LEFT JOIN "FacMain" FAC ON FAC."CustNo" = L."CustNo"
                           AND FAC."FacmNo" = L."FacmNo"
    LEFT JOIN (
      SELECT "CustNo" -- '�ḹ'
           , "FacmNo" -- '�B��'
           , MIN(CASE
                   WHEN "Status" IN (3,5,6,8,9)
                   THEN  99999999
                 ELSE "NextPayIntDate" END
                ) AS "NextPayIntDate" -- '��ú����'
           , MAX("MaturityDate") AS "MaturityDate"
           -- �̪���ú��:�w����S�O����,�ϥΤ멳����
           , MAX(CASE WHEN "Status" > 0 THEN 0
                      WHEN "MaturityDate" <=  "ThisMonthEndDate" THEN "ThisMonthEndDate" 
                      WHEN "SpecificDd" = 0  THEN "MaturityDate"
                      WHEN "SpecificDd" > MOD("ThisMonthEndDate", 100) THEN "ThisMonthEndDate" 
                      ELSE (TRUNC("ThisMonthEndDate"  / 100) * 100) + "SpecificDd"
                 END) AS "DueDate"   --'�̪���ú��'                   
         FROM "LoanBorMain"
      WHERE "Status" in (0,2,3,4,5,6,7,8,9)
        AND "DrawdownDate" <= TBSDYF
      GROUP BY "CustNo"
             , "FacmNo"
    ) B ON B."CustNo" = L."CustNo"
       AND B."FacmNo" = L."FacmNo"
    WHERE L."CaseCode" = 1
    ;

    INS_CNT := INS_CNT + sql%rowcount;

--   ��s AcBookCode �b�U�O

    DBMS_OUTPUT.PUT_LINE('UPDATE AcSubBookCode ');

    MERGE INTO "MonthlyFacBal" M
    USING (SELECT M."CustNo"
                , M."FacmNo"
                , MAX(NVL(A."AcSubBookCode",'00A')) AS "AcSubBookCode" 
            FROM "MonthlyFacBal" M
           LEFT JOIN "AcReceivable" A
            ON  A."AcctCode" = M."AcctCode"
            AND A."CustNo"   = M."CustNo"
            AND A."FacmNo"   = M."FacmNo"
           WHERE M."YearMonth" = YYYYMM
           GROUP BY M."CustNo", M."FacmNo") B
    ON (    M."YearMonth" = YYYYMM
        AND M."CustNo"    = B."CustNo"
        AND M."FacmNo"    = B."FacmNo")
    WHEN MATCHED THEN UPDATE SET M."AcSubBookCode" = B."AcSubBookCode";

    UPD_CNT := UPD_CNT + sql%rowcount;

    DBMS_OUTPUT.PUT_LINE('UPDATE AcBookCode END');

--   ��s EntCode  �����O, RelsCode (��)�Q�`���Y�H¾��

    DBMS_OUTPUT.PUT_LINE('UPDATE EntCode, RelsCode ');

    MERGE INTO "MonthlyFacBal" M
    USING (SELECT M."CustNo"
                , M."FacmNo"
                , C."EntCode"
                -- , R."RelsCode"
            FROM "MonthlyFacBal" M
            LEFT JOIN "CustMain" C ON C."CustNo" = M."CustNo"
            -- LEFT JOIN "RelsMain" R ON R."RelsId" = C."CustId"
            WHERE M."YearMonth" = YYYYMM
          ) C
     ON (   M."YearMonth" = YYYYMM
        AND M."CustNo"    = C."CustNo"
        AND M."FacmNo"    = C."FacmNo")
    WHEN MATCHED THEN UPDATE 
    SET M."EntCode" = C."EntCode"
      -- , M."RelsCode" = C."RelsCode"
    ;

    UPD_CNT := UPD_CNT + sql%rowcount;

    DBMS_OUTPUT.PUT_LINE('UPDATE EntCode, RelsCode END');

--   ��s DepartmentCode	�ץ����ݳ��

    DBMS_OUTPUT.PUT_LINE('UPDATE DepartmentCode ');

    MERGE INTO "MonthlyFacBal" M
    USING (SELECT "CustNo"
                , "FacmNo"
                , "DepartmentCode"
           FROM "FacMain"
          ) F
     ON (   M."YearMonth" = YYYYMM
         AND M."CustNo"    = F."CustNo"
         AND M."FacmNo"    = F."FacmNo"
         AND F."DepartmentCode" IS NOT NULL
        )
    WHEN MATCHED THEN UPDATE
    SET M."DepartmentCode" = F."DepartmentCode";

    UPD_CNT := UPD_CNT + sql%rowcount;

    DBMS_OUTPUT.PUT_LINE('UPDATE DepartmentCode END');


--   ��s UnpaidPrincipal �w����^������

    DBMS_OUTPUT.PUT_LINE('UPDATE UnpaidPrincipal ');

    MERGE INTO "MonthlyFacBal" M
    USING ( SELECT "CustNo", "FacmNo"
                 , NVL(SUM(F1), 0) F1
                 , NVL(SUM(F2), 0) F2
                 , NVL(SUM(F3), 0) F3
                 , NVL(SUM(F4), 0) F4
                 , NVL(SUM(F5), 0) F5
                 , NVL(SUM(F6), 0) F6
                 , NVL(SUM(F7), 0) F7
                 , NVL(SUM(F8), 0) F8 
            FROM (
                    SELECT M."CustNo", M."FacmNo",
                      CASE WHEN I."PayIntDate" <=  TBSDYF THEN I."Principal"
                          ELSE 0 END  F1,
                      CASE WHEN I."PayIntDate" <=  TBSDYF THEN I."Interest"
                          ELSE 0 END  F2,
                      CASE WHEN I."PayIntDate" <=  TBSDYF THEN I."BreachAmt"
                          ELSE 0 END  F3,
                      CASE WHEN I."PayIntDate" <=  TBSDYF THEN I."DelayInt"
                          ELSE 0 END  F4,
                      CASE WHEN I."PayIntDate" <=  TBSDYF THEN 0
                          ELSE I."Principal" END  F5,
                      CASE WHEN I."PayIntDate" <=  TBSDYF THEN 0
                          ELSE I."Interest" END  F6,
                      CASE WHEN I."PayIntDate" <=  TBSDYF THEN 0
                          ELSE I."BreachAmt" END  F7,
                      CASE WHEN I."PayIntDate" <=  TBSDYF THEN 0
                          ELSE I."DelayInt" END  F8
                        FROM "MonthlyFacBal" M
                        LEFT JOIN "AcLoanInt" I
                          ON  I."YearMonth" = YYYYMM
                          AND I."CustNo"    = M."CustNo"
                          AND I."FacmNo"    = M."FacmNo"
                        WHERE M."YearMonth" = YYYYMM
                 )
            GROUP BY "CustNo", "FacmNo" ) I
     ON (   M."YearMonth" = YYYYMM
        AND M."CustNo"    = I."CustNo"
        AND M."FacmNo"    = I."FacmNo")
    WHEN MATCHED THEN UPDATE SET M."UnpaidPrincipal" = I."F1",
                                 M."UnpaidInterest"  = I."F2",
                                 M."UnpaidBreachAmt" = I."F3",
                                 M."UnpaidDelayInt"  = I."F4",
                                 M."AcdrPrincipal"   = I."F5",
                                 M."AcdrInterest"    = I."F6",
                                 M."AcdrBreachAmt"   = I."F7",
                                 M."AcdrDelayInt"    = I."F8";

    UPD_CNT := UPD_CNT + sql%rowcount;

    DBMS_OUTPUT.PUT_LINE('UPDATE UnpaidPrincipal END');

--   ��s FeeAmt �O��

    DBMS_OUTPUT.PUT_LINE('UPDATE FeeAmt ');

    -- 2021-02-19 ��s ���t�X�s�W���
    MERGE INTO "MonthlyFacBal" M
    USING ( SELECT "CustNo"
                  ,"FacmNo"
                  ,NVL(SUM("FireFee"), 0)       AS "FireFee"
                  ,NVL(SUM("LawFee"), 0)        AS "LawFee"
                  ,NVL(SUM("ModifyFee"), 0)     AS "ModifyFee"
                  ,NVL(SUM("AcctFee"), 0)       AS "AcctFee"
                  ,NVL(SUM("ShortfallPrin"), 0) AS "ShortfallPrin"
                  ,NVL(SUM("ShortfallInt"), 0)  AS "ShortfallInt"
                  ,NVL(SUM("TempAmt"), 0)       AS "TempAmt"
            FROM ( SELECT M."CustNo"
                         ,M."FacmNo"
                         ,CASE
                            WHEN A."AcctCode" IN ('TMI')
                            THEN A."RvBal"
                            WHEN A."AcctCode" IN ('F09', 'F25')
                            THEN A."RvBal"
                            WHEN IR."CustNo" IS NOT NULL
                            THEN IR."TotInsuPrem"
                          ELSE 0 END AS "FireFee" -- ���I�O��
                         ,CASE
                            WHEN A."AcctCode" IN ('F07', 'F24')
                            THEN A."RvBal"
                          ELSE 0 END AS "LawFee" -- �k�ȶO��
                         ,CASE
                            WHEN A."AcctCode" IN ('F29')
                            THEN A."RvBal"
                          ELSE 0 END AS "ModifyFee" -- ���ܤ���O
                         ,CASE
                            WHEN A."AcctCode" IN ('F10')
                            THEN A."RvBal"
                          ELSE 0 END AS "AcctFee" -- �b�޶O��
                         ,CASE
                            WHEN SUBSTR(A."AcctCode",0,1) IN ('Z')
                            THEN A."RvBal"
                          ELSE 0 END AS "ShortfallPrin" -- �uú����
                         ,CASE
                            WHEN SUBSTR(A."AcctCode",0,1) IN ('I')
                            THEN A."RvBal"
                          ELSE 0 END AS "ShortfallInt" -- �uú�Q��
                         ,CASE
                            WHEN A."AcctCode" = 'TAV'
                            THEN A."RvBal"
                          ELSE 0 END AS "TempAmt" -- �Ȧ����B
                   FROM "MonthlyFacBal" M
                   LEFT JOIN "AcReceivable" A ON A."CustNo" = M."CustNo"
                                             AND A."FacmNo" = M."FacmNo"
                   LEFT JOIN "InsuRenew" IR ON IR."CustNo" = A."CustNo"
                                           AND IR."FacmNo" = A."FacmNo"
                                           AND IR."PrevInsuNo" = A."RvNo"                
                   WHERE M."YearMonth" = YYYYMM
                     AND (A."AcctCode" IN ('F10','F29','TMI', 'F09', 'F25', 'F07', 'F24','TAV')
                          OR SUBSTR(A."AcctCode",0,1) IN ('I','Z'))
            )
            GROUP BY "CustNo", "FacmNo" 
          ) D
     ON (   M."YearMonth" = YYYYMM
        AND M."CustNo"    = D."CustNo"
        AND M."FacmNo"    = D."FacmNo")
    WHEN MATCHED THEN UPDATE
    SET M."FireFee"       = D."FireFee"
       ,M."LawFee"        = D."LawFee"
       ,M."ModifyFee"     = D."ModifyFee"
       ,M."AcctFee"       = D."AcctFee"
       ,M."ShortfallPrin" = D."ShortfallPrin"
       ,M."ShortfallInt"  = D."ShortfallInt"
       ,M."TempAmt"       = D."TempAmt"
    ;

    UPD_CNT := UPD_CNT + sql%rowcount;

    DBMS_OUTPUT.PUT_LINE('UPDATE FeeAmt END');

--   ��s ClCode1	��O�~�N��1

    DBMS_OUTPUT.PUT_LINE('UPDATE ClCode1 ');

    MERGE INTO "MonthlyFacBal" M1
    USING (SELECT M."YearMonth"
                 ,M."CustNo"
                 ,M."FacmNo"
                 ,MAX(NVL(F."ClCode1",0))     AS "ClCode1"
                 ,MAX(NVL(F."ClCode2",0))     AS "ClCode2"
                 ,MAX(NVL(F."ClNo",0))        AS "ClNo"
                 ,MAX(NVL(Cl."CityCode",' ')) AS "CityCode"
            FROM "MonthlyFacBal" M
            LEFT JOIN "ClFac" F ON F."CustNo" = M."CustNo"
                               AND F."FacmNo" = M."FacmNo"
                               AND F."MainFlag" = 'Y'
            LEFT JOIN "ClMain" Cl ON Cl."ClCode1" = F."ClCode1"
                                 AND Cl."ClCode2" = F."ClCode2"
                                 AND Cl."ClNo"    = F."ClNo"
                                 AND NVL(F."ClNo",0) > 0 
            WHERE M."YearMonth" = YYYYMM
              AND (F."ClCode1" IN (3,4,5) OR NVL(Cl."CityCode",' ') <> ' ')
            GROUP BY  M."YearMonth"
                     ,M."CustNo"
                     ,M."FacmNo"          
           ) F1
     ON (   M1."YearMonth" = F1."YearMonth"
        AND M1."CustNo"    = F1."CustNo"
        AND M1."FacmNo"    = F1."FacmNo")
    WHEN MATCHED THEN UPDATE SET M1."ClCode1"  = F1."ClCode1"
                                ,M1."ClCode2"  = F1."ClCode2"
                                ,M1."ClNo"     = F1."ClNo"
                                ,M1."CityCode" = F1."CityCode";

    UPD_CNT := UPD_CNT + sql%rowcount;

    DBMS_OUTPUT.PUT_LINE('UPDATE ClCode1 END');

--   ��s OvduPrinBal	�ʦ������l�B

    DBMS_OUTPUT.PUT_LINE('UPDATE OvduPrinBal ');

    MERGE INTO "MonthlyFacBal" M
    USING ( SELECT M."YearMonth"
                  ,M."CustNo"
                  ,M."FacmNo"
                  ,MIN(NVL(O."OvduDate",99991231)) AS "OvduDate"
                  ,SUM(NVL(O."OvduPrinAmt",0))   AS "UnpaidPrincipal" 
                  ,SUM(NVL(O."OvduIntAmt",0))    AS "UnpaidInterest"
                  ,SUM(NVL(O."OvduBreachAmt",0)) AS "UnpaidBreachAmt"
                  ,SUM(NVL(O."OvduPrinBal",0))   AS "OvduPrinBal" 
                  ,SUM(NVL(O."OvduIntBal",0))    AS "OvduIntBal"
                  ,SUM(NVL(O."OvduBreachBal",0)) AS "OvduBreachBal"
                  ,SUM(NVL(O."OvduBal",0))       AS "OvduBal"
              FROM "MonthlyFacBal" M  
             LEFT JOIN "LoanBorMain" L ON L."CustNo" = M."CustNo" 
                                      AND L."FacmNo" = M."FacmNo" 
             LEFT JOIN "LoanOverdue" O ON  O."CustNo" = L."CustNo" 
                                      AND O."FacmNo" = L."FacmNo"
                                      AND O."BormNo" = L."BormNo" 
                                      AND O."OvduNo" = L."LastOvduNo"
             WHERE M."YearMonth" = YYYYMM 
               AND L."Status" IN (2, 7) 
               AND O."Status" IN (1, 2) 
             GROUP BY M."YearMonth",M."CustNo", M."FacmNo"
          ) O
     ON (   M."YearMonth" = O."YearMonth"
        AND M."CustNo"    = O."CustNo"
        AND M."FacmNo"    = O."FacmNo")
    WHEN MATCHED THEN UPDATE SET M."UnpaidPrincipal" = O."UnpaidPrincipal",
                                 M."UnpaidInterest"  = O."UnpaidInterest",
                                 M."UnpaidBreachAmt" = O."UnpaidBreachAmt",
                                 M."OvduPrinBal"     = O."OvduPrinBal",
                                 M."OvduIntBal"      = O."OvduIntBal",
                                 M."OvduBreachBal"   = O."OvduBreachBal",
                                 M."OvduBal"         = O."OvduBal",
                                 M."OvduDate"        = CASE
                                                         WHEN O."OvduDate" = 99991231
                                                         THEN M."OvduDate"
                                                       ELSE O."OvduDate" END
                                 ;

    UPD_CNT := UPD_CNT + sql%rowcount;    

--   ��s StoreRate �p���Q�v

    DBMS_OUTPUT.PUT_LINE('UPDATE StoreRate ');

    MERGE INTO "MonthlyFacBal" M
    USING (SELECT M."CustNo", M."FacmNo"
                , NVL(MIN(L."StoreRate"), 0) "StoreRate" 
            FROM "MonthlyFacBal" M
           LEFT JOIN "MonthlyLoanBal" L
            ON  L."YearMonth" = YYYYMM 
            AND L."CustNo"   = M."CustNo"
            AND L."FacmNo"   = M."FacmNo"
           WHERE M."YearMonth" = YYYYMM
           GROUP BY M."CustNo", M."FacmNo") B
    ON (    M."YearMonth" = YYYYMM
        AND M."CustNo"    = B."CustNo"
        AND M."FacmNo"    = B."FacmNo")
    WHEN MATCHED THEN UPDATE SET M."StoreRate" = B."StoreRate";

    UPD_CNT := UPD_CNT + sql%rowcount;

    DBMS_OUTPUT.PUT_LINE('UPDATE StoreRate END');

--  ��s  �L��O���v�]�w���B(�k�ȶi��:901)
    DBMS_OUTPUT.PUT_LINE('UPDATE LawAmount');

    MERGE INTO "MonthlyFacBal" M
    USING (SELECT L."CustNo"
                 ,L."FacmNo"
                 ,L."Amount"
           FROM (
                 SELECT "CustNo"
                       ,"FacmNo"
                       ,"LegalProg"
                       ,"Amount"
                       ,ROW_NUMBER() OVER (PARTITION BY "CustNo", "FacmNo" 
                                           ORDER BY "RecordDate" DESC, "LastUpdate" DESC
                                          ) AS SEQ
                 FROM "CollLaw"  
                 WHERE "CaseCode"  = '1'   
                   AND "LegalProg" = '901' 
                 ) L
           WHERE L.SEQ   =  1) D
     ON (   M."CustNo"    =  D."CustNo"
        AND M."FacmNo"    =  D."FacmNo")
    WHEN MATCHED THEN UPDATE 
      SET M."LawAmount"   =  D."Amount"
        , M."LawAssetClass"   =  '5'
    ;

    UPD_CNT := UPD_CNT + sql%rowcount;

    DBMS_OUTPUT.PUT_LINE('UPDATE LawAmount END');

--  ��s  �겣������(�D1��)
    DBMS_OUTPUT.PUT_LINE('UPDATE AssetClass1');

    MERGE INTO "MonthlyFacBal" M
    USING (
      SELECT M."YearMonth"
           , M."CustNo"
           , M."FacmNo" 
           , CASE
               WHEN M."PrinBalance" = 1
                AND M."AcctCode" = '990'
               THEN '5'        --(5)�Ĥ���-���^�L��(�����k�ȶi��901�A�{�ȥH�l�B��1���Ĥ���)
                               --   �L��O����--�W�L�M�v��12���
                               --   �Ω�q�f���L��q���l����
                               --   �Ω�ڸ겣�g�����L�k�^����    
               WHEN M."ProdNo" IN ('60','61','62')
                AND M."OvduTerm" > 12
               THEN '3'         --(23)�ĤT��-�i�榬�^�G
                                --    ��ĳ�󦳨��B��O--�Oú�W�L�M�v��12���     

               WHEN M."ProdNo" IN ('60','61','62')
               THEN '2'        --(23)�ĤG��-�����`�N�G
                               --    ��ĳ��
                               --    ��ĳ����ʦ���pú�������`�ѤH�u�ץ���L7205�W�ǧ�s
               WHEN M."AcctCode" = '990'
                AND M."OvduTerm" > 12
               THEN '3'        --(3)�ĤT��-�i�榬�^�G
                               --   �����B��O--�Oú�W�L�M�v��12���
                               --   �εL��O����--�W�L�M�v��3-6���                         
               WHEN M."AcctCode" = '990'
               THEN '2'       --(23)�ĤG��-�����`�N�G
                               --    �����B��O--�Oú�W�L�M�v��7-12���
                               --    �εL��O����--�W�L�M�v��1-3���
               WHEN M."OvduTerm" >= 7
                AND M."OvduTerm" <= 12
               THEN '2'       --(23)�ĤG��-�����`�N�G
                               --    �����B��O--�Oú�W�L�M�v��7-12���
                               --    �εL��O����--�W�L�M�v��1-3���    
               WHEN M."OvduTerm" >= 1
                AND M."OvduTerm" <= 6
               THEN '2'       --(22)�ĤG��-�����`�N�G
                               --    �����B��O--�Oú�W�L�M�v��1-6���
               ELSE '1'       -- ���`ú��
             END                  AS "AssetClass"	--��ڸ겣����	  
      FROM "MonthlyFacBal" M
      LEFT JOIN "FacMain" F ON F."CustNo" = M."CustNo"
                            AND F."FacmNo" = M."FacmNo"
      LEFT JOIN "CustMain" CM ON CM."CustNo" = M."CustNo"
      LEFT JOIN ( SELECT DISTINCT SUBSTR("IndustryCode",3,4) AS "IndustryCode"
                        ,"IndustryItem"
                  FROM "CdIndustry" ) CDI ON CDI."IndustryCode" = SUBSTR(CM."IndustryCode",3,4)
      WHERE M."PrinBalance" > 0 
        AND M."YearMonth" = YYYYMM
    ) TMP
    ON (
      TMP."YearMonth" = M."YearMonth"
      AND TMP."CustNo" = M."CustNo"
      AND TMP."FacmNo" = M."FacmNo"
    )
    WHEN MATCHED THEN UPDATE SET
    "AssetClass" = TMP."AssetClass"
    ;
    
    UPD_CNT := UPD_CNT + sql%rowcount;

    DBMS_OUTPUT.PUT_LINE('UPDATE AssetClass1 END');

    DBMS_OUTPUT.PUT_LINE('UPDATE AssetClass2 START');

    MERGE INTO "MonthlyFacBal" M
    USING (
      SELECT M."YearMonth"
           , M."CustNo"
           , M."FacmNo" 
           ,  CASE
                WHEN M."AssetClass" = 2
                THEN 
                  CASE  WHEN M."AcctCode" = '990'
                      THEN '23'       --(23)�ĤG��-�����`�N�G
                                      --    �����L��O--�Oú�W�L�M�v��7-12���
                                      --    �εL��O����--�W�L�M�v��1-3���        
                      WHEN M."ProdNo" IN ('60','61','62')
                      THEN '21'       --(21)�ĤG��-�����`�N�G
                                      --    �����B��O--���ūH�H���}��
                                      --    (����O������ĳ�B���`�ٴڪ�)
                      ELSE '22'       --(22)�ĤG��-�����`�N�G
                                      --    �����L��O--�Oú�W�L�M�v��1-6���
                  END
                WHEN  M."AssetClass" = 1
                THEN CASE
                    WHEN M."ClCode1" IN (1,2) 
                      AND CDI."IndustryItem" LIKE '%���ʲ�%'
                    THEN '12'              -- �S�w�겣��ڡG�ؿv�U��
                    WHEN M."ClCode1" IN (1,2) 
                      AND CDI."IndustryItem" LIKE '%�ؿv%'
                    THEN '12'              -- �S�w�겣��ڡG�ؿv�U��
                    WHEN M."ClCode1" IN (1,2) 
                      AND F."FirstDrawdownDate" >= 20100101 
                      AND M."FacAcctCode" = 340
                    THEN '11'               -- ���`ú��
                    WHEN M."ClCode1" IN (1,2) 
                      AND F."FirstDrawdownDate" >= 20100101 
                      AND REGEXP_LIKE(M."ProdNo",'I[A-Z]')
                    THEN '11'               -- ���`ú��
                    WHEN M."ClCode1" IN (1,2) 
                      AND F."FirstDrawdownDate" >= 20100101 
                      AND REGEXP_LIKE(M."ProdNo",'8[1-8]')
                    THEN '11'               -- ���`ú��
                    WHEN M."ClCode1" IN (1,2) 
                      AND F."UsageCode" = '02' 
                      AND TRUNC(M."PrevIntDate" / 100) >= LYYYYMM
                    THEN '12'       -- �S�w�겣��ڡG�ʸm��v+��µ�U��              
                    ELSE '11'       
                    END
              ELSE "AssetClass"
              END                  AS "AssetClass2"	--�겣����2	  
      FROM "MonthlyFacBal" M
      LEFT JOIN "FacMain" F ON F."CustNo" = M."CustNo"
                            AND F."FacmNo" = M."FacmNo"
      LEFT JOIN "CustMain" CM ON CM."CustNo" = M."CustNo"
      LEFT JOIN ( SELECT DISTINCT SUBSTR("IndustryCode",3,4) AS "IndustryCode"
                        ,"IndustryItem"
                  FROM "CdIndustry" ) CDI ON CDI."IndustryCode" = SUBSTR(CM."IndustryCode",3,4)
      WHERE M."PrinBalance" > 0 
        AND M."YearMonth" = YYYYMM
    ) TMP
    ON (
      TMP."YearMonth" = M."YearMonth"
      AND TMP."CustNo" = M."CustNo"
      AND TMP."FacmNo" = M."FacmNo"
    )
    WHEN MATCHED THEN UPDATE SET
    "AssetClass2" = TMP."AssetClass2"
    ;
   
    UPD_CNT := UPD_CNT + sql%rowcount;

    DBMS_OUTPUT.PUT_LINE('UPDATE AssetClass2 END');

    COMMIT;
    -- �O���{�������ɶ�
    JOB_END_TIME := SYSTIMESTAMP;

    -- �ҥ~�B�z
    Exception
    WHEN OTHERS THEN
    "Usp_L9_UspErrorLog_Ins"(
        'Usp_L9_MonthlyFacBal_Upd' -- UspName �w�s�{�ǦW��
      , SQLCODE -- Sql Error Code (�T�w��)
      , SQLERRM -- Sql Error Message (�T�w��)
      , dbms_utility.format_error_backtrace -- Sql Error Trace (�T�w��)
      , EmpNo -- �o�ʹw�s�{�Ǫ����u�s��
      , JobTxSeq -- �Ұʧ妸������Ǹ�
    );
    COMMIT;
    RAISE;
  END;
END;