DROP TABLE "DailyLoanBalTemp";
CREATE TABLE "DailyLoanBalTemp" (
      "CustNo" NUMBER(7)
    , "FacmNo" NUMBER(3)
    , "BormNo" NUMBER(3)
    , "JsonData" CLOB
    , "ErrorMsg" CLOB
    , CONSTRAINT "DailyLoanBalTempEnsureJson" CHECK ("JsonData" IS JSON)
);