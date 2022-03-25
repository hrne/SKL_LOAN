MERGE INTO "CdCode" TARGET
USING (SELECT main."Code"
            , main."Item"
            , main."MinCodeLength"
            , main."MaxCodeLength"
            , sub."ActualMinLength"
            , sub."ActualMaxLength"
       FROM "CdCode" main
       LEFT JOIN (SELECT "DefCode"
                       , MIN(LENGTH("Code")) "ActualMinLength"
                       , MAX(LENGTH("Code")) "ActualMaxLength"
                  FROM "CdCode"
                  WHERE "DefCode" != 'CodeType'
                  GROUP BY "DefCode"
                 ) sub ON sub."DefCode" = main."Code"
       WHERE main."DefCode" = 'CodeType'
         AND sub."DefCode" is not null
      ) SOURCE
ON (
            SOURCE."Code" = TARGET."Code"
        AND TARGET."DefCode" = 'CodeType'
    )
WHEN MATCHED THEN
    UPDATE
    SET TARGET."MinCodeLength" = SOURCE."ActualMinLength"
      , TARGET."MaxCodeLength" = SOURCE."ActualMaxLength"