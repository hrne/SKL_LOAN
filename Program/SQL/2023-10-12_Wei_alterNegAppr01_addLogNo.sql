
alter table "NegAppr01" drop constraint "NegAppr01_PK";

alter table "NegAppr01" add "LogNo" decimal(11,0) default 0 not null;

comment on column "NegAppr01"."LogNo" is '序號';

merge into "NegAppr01" t 
using (
SELECT ROW_NUMBER()
       OVER (
         ORDER BY "AcDate","TitaTlrNo","TitaTxtNo","FinCode","CustNo"
       ) AS "NewLogNo"
     , "AcDate","TitaTlrNo","TitaTxtNo","FinCode","CustNo"
FROM "NegAppr01"
) s
on (
t."AcDate" = s."AcDate" 
and t."TitaTlrNo" = s."TitaTlrNo" 
and t."TitaTxtNo" = s."TitaTxtNo" 
and t."FinCode" = s."FinCode" 
and t."CustNo" = s."CustNo"
)
when matched then update set
"LogNo" = s."NewLogNo"
;

alter table "NegAppr01" add constraint "NegAppr01_PK" primary key("LogNo");