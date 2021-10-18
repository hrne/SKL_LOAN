MERGE INTO "CdEmp" T
USING (
    SELECT T."TlrNo"
         , T."TlrItem"
    FROM "TxTeller" T
    LEFT JOIN "CdEmp" E ON E."EmployeeNo" = T."TlrNo"
    WHERE NVL(E."EmployeeNo",' ') = ' '
) S
ON (T."EmployeeNo" = S."TlrNo")
WHEN NOT MATCHED THEN INSERT
("EmployeeNo","Fullname")
VALUES
(S."TlrNo",S."TlrItem")
;
COMMIT
;