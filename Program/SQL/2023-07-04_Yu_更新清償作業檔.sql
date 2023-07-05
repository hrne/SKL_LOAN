alter table "FacClose" drop constraint "FacClose_PK";

MERGE INTO "FacClose" fc 
    USING (SELECT ROW_NUMBER()
           OVER (
             PARTITION BY fc."ApplDate"
             ORDER BY fc."ApplDate"
           )                 AS "CloseNo2", fc.* FROM "FacClose" fc) fc2
     ON (fc2."ApplDate"    = fc."ApplDate"  
     AND fc2."CustNo"    = fc."CustNo"
     AND fc2."FacmNo"    = fc."FacmNo"
     AND fc2."EntryDate" = fc."EntryDate"
     AND fc2."CreateDate" = fc."CreateDate")
    WHEN MATCHED THEN UPDATE SET fc."CloseNo" = fc2."CloseNo2";
    
alter table "FacClose" add constraint "FacClose_PK" primary key("ApplDate", "CloseNo");