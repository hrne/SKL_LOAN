create or replace FUNCTION "Fn_GetDateSequence" 
(
     "Min" IN DECIMAL -- 最小數字
	,"Max" IN DECIMAL -- 最大數字
	,"IsDate" IN VARCHAR2 -- 是否僅合法日期; 設為Y時只接受6位或8位數字
) RETURN sys.odcinumberlist IS "Result" sys.odcinumberlist := sys.odcinumberlist ();
-- "Fn_GetSequence"(Min, Max, IsDate)
-- 實際運用時, 以此方式呼叫:
-- FROM TABLE("Fn_GetDateSequence"(1990,     2021,     'N'))
-- FROM TABLE("Fn_GetDateSequence"(199001,   202112,   'Y'))
-- FROM TABLE("Fn_GetDateSequence"(19900101, 20211231, 'Y'))

-- odcinumberlist 只能容納最多 32767 筆資料
-- 此函數限制最多回傳 32767 筆

-- xiangwei 20211112

BEGIN
	SELECT "Min" + LEVEL - 1 AS "Iteration" -- LEVEL BEGINS ON 1
	BULK COLLECT INTO "Result"
	FROM DUAL
	WHERE CASE WHEN "IsDate" = 'Y' -- IsDate為Y時
	           THEN CASE WHEN LENGTH("Min") = LENGTH("Max") -- Min跟Max長度需一樣
			             THEN CASE WHEN LENGTH("Min") = 6  -- 6位時, 只檢查是否為1到12月
						            AND MOD("Min" + LEVEL - 1, 100) BETWEEN 1 AND 12
						           THEN 1 -- 合法
								   WHEN LENGTH("Min") = 8 
									AND MOD(FLOOR(("Min" + LEVEL - 1)/100), 100) BETWEEN 1 AND 12 -- 是否為1到12月
									AND MOD("Min" + LEVEL - 1, 100) BETWEEN 1 AND (CASE WHEN MOD(FLOOR(("Min" + LEVEL - 1)/100), 100) IN (1,3,5,7,8,10,12)
									                                                    THEN 31 -- 大月. 此資料的日期為 "Min" + LEVEL - 1
																		                WHEN MOD(FLOOR(("Min" + LEVEL - 1)/100), 100) IN (4,6,9,11)
																		                THEN 30 -- 小月
																		                WHEN MOD(MOD(FLOOR(("Min" + LEVEL - 1)/10000), 100), 400) = 0 -- 條件A: 為400 倍數
																						         OR (MOD(MOD(FLOOR(("Min" + LEVEL - 1)/10000), 100), 4) = 0
																			                         AND MOD(MOD(FLOOR(("Min" + LEVEL - 1)/10000), 100), 100) != 0) -- 條件B: 為4 倍數且不為 100 倍數
																					    THEN 29 -- 閏年二月
																				        ELSE 28 -- 非閏年二月
																	               END 
																	             )
								   THEN 1 -- 合法
								   ELSE 0 -- 不合法資料
							  END
						 ELSE 0 -- 長度不一樣, 不會有任何輸出
				    END
			   ELSE 1 -- 非日期時, 不特別做規則
		  END = 1
	  AND "Min" <= "Max" -- Min 大於 Max 時, 不會有任何輸出
	  AND rownum <= 32767 -- 限制最大回傳筆數 32767 筆
	CONNECT BY "Min" + LEVEL - 1 BETWEEN "Min" AND "Max";

    RETURN "Result";

    -- 例外處理
END;