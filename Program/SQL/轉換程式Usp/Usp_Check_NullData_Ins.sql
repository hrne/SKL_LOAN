CREATE OR REPLACE PROCEDURE "Usp_Check_NullData_Ins" AS
  v_sql       VARCHAR2(1000);
  v_count     NUMBER;
  v_table     VARCHAR2(100);
  v_column    VARCHAR2(100);
  v_cur       SYS_REFCURSOR;
BEGIN
  
  EXECUTE IMMEDIATE 'TRUNCATE TABLE "NullData" DROP STORAGE'; 

  -- 遍歷 user_tab_columns 來查詢每個表的第一個欄位
  OPEN v_cur FOR 
  SELECT table_name, column_name 
  FROM user_tab_columns 
  WHERE column_id = 1;

  LOOP
    FETCH v_cur INTO v_table, v_column;
    EXIT WHEN v_cur%NOTFOUND;
    
    -- 創建動態SQL以查詢是否有NULL值
    v_sql := 'SELECT COUNT(*) FROM "' || v_table || '" WHERE "' || v_column || '" IS NULL';
    EXECUTE IMMEDIATE v_sql INTO v_count;

    IF v_count > 0 THEN
      -- 如果該欄位有一筆以上的NULL值，則將表名寫入NullData表
      v_sql := 'INSERT INTO "NullData"("TableName") VALUES (''' || v_table || ''')';
      EXECUTE IMMEDIATE v_sql;
    END IF;

  END LOOP;
  CLOSE v_cur;
  COMMIT;
EXCEPTION 
  WHEN OTHERS THEN
    RAISE;
END "Usp_Check_NullData_Ins";
/
