
    MERGE INTO "FacMain" fm 
    USING (SELECT DISTINCT fp."ProdNo",fp."BreachFlag",
    fp."BreachCode",fp."BreachGetCode",fp."ProhibitMonth",
    fp."BreachPercent",fp."BreachDecreaseMonth",fp."BreachDecrease",
    fp."BreachStartPercent"
            FROM "FacMain" fm  
           LEFT JOIN "FacProd" fp  
             ON  fp."ProdNo" = fm."ProdNo" 
             ) fp
     ON (   fm."ProdNo"    = fp."ProdNo"  )
    WHEN MATCHED THEN UPDATE SET fm."BreachFlag" = NVL(fp."BreachFlag",'N'),fm."BreachCode" = fp."BreachCode",fm."BreachGetCode" = fp."BreachGetCode",
    fm."ProhibitMonth" = fp."ProhibitMonth",fm."BreachPercent" = fp."BreachPercent",fm."BreachDecreaseMonth" = fp."BreachDecreaseMonth",
    fm."BreachDecrease" = fp."BreachDecrease" ,fm."BreachStartPercent" = fp."BreachStartPercent";