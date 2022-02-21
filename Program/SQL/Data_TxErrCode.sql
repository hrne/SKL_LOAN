--------------------------------------------------------
--  已建立檔案 - 星期一-二月-21-2022   
--------------------------------------------------------
REM INSERTING into "TxErrCode"
SET DEFINE OFF;
DELETE FROM "TxErrCode";
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E6005','非開帳狀態',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E6006','資負明細科目不可經由[其他傳票輸入]交易入帳',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E6007','非可入帳科目',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E6008','金額錯誤',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E6009','銷帳科目記號不符',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E6010','新增資料不可為空白',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E6011','查詢資料不可為空白',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E6012','該員工非現職人員',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E6013','行庫代碼不可為空白',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E6014','行庫代碼長度不符',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E6015','查無資料',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E6016','行庫代碼不得為零',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E6017','該利率已生效',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E6018','刪除資料錯誤',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E6019','該利率未生效',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E6101','功能選擇錯誤   ',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E6102','縣市別代碼錯誤',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E6103','行政區代碼錯誤',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E6104','縣市別代碼或行政區代碼錯誤',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E6105','交易序號長度不符',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E6106','登放序號長度不符',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E8001','超過一筆資料請查驗',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E8002','前置協商受理申請暨請求償權通知資料(表40)查無此協商債務人資料，請先至表40新增資料',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E8003','金融機構無擔保債務協議資料檔案(表47)查無此協商債務人資料，請先至表47新增資料',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E8004','案件狀態未曾報送1或6狀態，不能報送其他狀態，請重新選擇',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E8005','案件狀態未曾報送3狀態更生方案認可確定前，不能報送4更生方案履行完畢或5更生裁定免責確定',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E8006','案件狀態未曾報送A(清算程序開始)或E(清算調查程序)狀態，不能報送其他狀態，請重新選擇',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E8007','案件狀態未曾報送A(清算程序開始)或C(清算程序開始同時終止)狀態，且法院裁定免責確定不為Y，不能報送D(清算撤消免責確定)狀態，請重新選擇',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E8101','前置調解無擔保債務分配表資料(表448)查無此調解債務人資料，請先至表448新增資料',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E8102','前置調解金融機構無擔保債務協議資料(表447)查無此調解人債務資料，請先至表447新增資料',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E8103','本檔案報送時間為每月11-15日',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E8104','結案日期不可早於申請日，亦不可晚於報送本檔案日期',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E8105','調解申請日不可大於資料報送日',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E8106','最後繳息日需小於資料報送日',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E8107','前置調解受理申請暨請求回報債權通知資料(表440)查無此調解債務人資料，請先至表440新增資料',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E8108','此調解債務人資料報送前置調解金融機構無擔保債務協議資料(表447)後，僅能以「毀諾」、「毀諾後清償」或「依債務清償方案履行完畢」結案。',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E8109','簽約完成日期不可大於資料報送日或首期應繳款日不可小於簽約完成日',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E8112','前置調解金融機構無擔保債務協議資料(表447)已結案，無法新增資料。',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E8115','同意書取得日期不可大於資料報送日',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E8117','此調解債務人資料報送前置調解結案通知資料(表446)結案後，不得新增、異動、刪除或補件本檔案。',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E9001','該核准號碼不存在',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E9002','該會計科子細目不存在',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E9003','產製報表時，查詢資料庫語法有誤',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('EC001','資料不存在',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('EC002','新增資料錯誤',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('EC003','修改資料錯誤',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('EC004','資料錯誤',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('EC005','交易流程控管錯誤，請洽資訊人員處理',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('EC006','刪除資料錯誤',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('EC009','資料處理錯誤',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('EC010','讀取文字檔，編碼限為UTF-8或big5',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E9004','傳票資料上傳EBS失敗',to_timestamp('2022-02-17 18:07:14.796000000','YYYY-MM-DD HH24:MI:SS.FF'),'001702',to_timestamp('2022-02-17 18:07:14.796000000','YYYY-MM-DD HH24:MI:SS.FF'),'001702');
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E5010','資料格式轉換有誤',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E6001','分錄檢核有誤',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E6002','收付欄檢核有誤',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E6003','會計入帳檢核錯誤',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E6004','非關帳狀態',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E0001','查詢資料不存在',null,null,to_timestamp('2022-02-15 17:31:40.555000000','YYYY-MM-DD HH24:MI:SS.FF'),'001715');
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E0002','新增資料已存在',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E0003','修改資料不存在',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E0004','刪除資料不存在',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E0005','新增資料時，發生錯誤',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E0006','鎖定資料時，發生錯誤',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E0007','更新資料時，發生錯誤',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E0008','刪除資料時，發生錯誤',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E0009','交易代號不可為空白',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E0010','功能選擇錯誤',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E0011','該筆資料已被刪除',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E0012','該筆資料已存在',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E0013','程式邏輯有誤',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E0014','檔案錯誤',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E0015','檢查錯誤',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E0016','該筆交易狀態非登錄，不可做訂正登錄交易',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E0017','該筆交易狀態非待放行，不可做交易放行',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E0018','該筆交易狀態非已放行，不可做訂正已放行交易',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E0019','輸入資料錯誤',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E0020','已刪除資料，不可做修正',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E0021','該筆資料待放行中',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E0022','該筆資料需進行AML審查/確認',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E0023','AML凍結名單/未確定名單',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E0024','上傳檔案格式有誤',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E1001','統一編號、戶號需擇一輸入',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E1002','戶號不得為0',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E1003','此統一編號不存在客戶主檔',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E1004','此戶號不存在客戶主檔',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E1005','此統編已存在客戶主檔',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E2002','交易代號不可為空白',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E2003','查無資料',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E2004','功能選擇錯誤',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E2005','新增資料已存在',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E2006','修改資料不存在',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E2007','刪除資料不存在',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E2008','刪除資料時，發生錯誤',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E2009','新增資料時，發生錯誤',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E2019','此申請號碼不存在案件申請檔',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E2020','此擔保品號碼在額度與擔保品關聯檔無資料',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E2021','此核准號碼尚未核准',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E2022','此核准號碼在額度主檔無資料',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E2023','此擔保品號碼在不動產主檔無資料',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E2024','此擔保品號碼在擔保品股票檔無資料',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E2025','此擔保品號碼在擔保品其他檔無資料',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E2026','此統編不存在於關係人主檔',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E2027','此擔保品號碼在擔保品主檔無資料',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E2028','此戶號額度查無貸後契變手續費檔',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E2029','此戶號查無資料',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E2030','此統編之戶號為0',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E2031','此案號查無資料',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E2032','申請號碼不得為0',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E2033','同擔保品在擔保品與額度關聯檔的分配金額加總不得大於擔保品主檔的可分配金額',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E2034','變更後之可分配金額不可小於同一擔保品在額度關聯檔的分配金額加總',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E2035','該擔保品於額度關聯檔尚有資料,不可刪除',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E2051',' 統一編號，戶號須擇一輸入      ',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E2052',' 商品參數生效後禁止刪除        ',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E2053',' 此商品尚未生效                ',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E2054',' 此商品已截止                  ',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E2055',' 此商品已停用                  ',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E2056',' 商品參數生效後禁止刪除        ',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E2061',' 統一編號與申請案件不符        ',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E2062',' 申請案件檔，資料已存在        ',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E2063',' 已作過准駁處理之案件不可刪除  ',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E2064',' 申請案件已核准                ',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E2065',' 申請案件已駁回                ',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E2066','已撥款不可再建檔',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E2067','約定部分償還，未過期者僅能一筆',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E2071','額度已撥款後，禁止刪除',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E2072','該筆額度編號沒有可用額度',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E2073','該額度與擔保品關聯，不可刪除',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E2074','未全部結案，不可列印',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E2081','該員工非現職人員 ',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E2082','該行庫代號未啟用',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3040','指定應繳日與扣款特定日設定不符',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3050','非展期借新還舊撥款日期不可小於本營業日',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3051','額度不足撥款金額',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3052','已超過動支期限',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3053','已超過循環動用期限',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3054','該筆預約撥款資料已撥款',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3055','該筆資料非預約撥款',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3056','該筆約定部分償還金額已回收',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3057','該票據狀況碼非為兌現，不可入帳',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3058','該票據狀況碼非為未處理',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3059','該票據狀況碼非為抽票或退票',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3060','退還金額大於目前客戶之暫收款',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3061','該戶暫收款為零',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3062','有1期(含)以上期款未繳,不可變更繳款日',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3063','該筆放款戶況非正常戶',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3064','該筆放款應繳日尚未到',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3065','還款分配金額不足',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3066','該筆放款攤還方式非本息平均法及本金平均法',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3067','因該筆放款有最後本金餘額，新攤還金額不足以繳息',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3068','該筆放款戶況非正常戶及逾期戶',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3069','該筆放款戶況非催收戶',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3070','查無可計息的放款資料',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3071','金額不足',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3072','該筆放款尚有期款未回收',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3073','查無清償違約金',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3074','回收金額超過應回收清償違約金額',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3075','變更應繳日需落在上次繳息迄日與下次應繳息日之內',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3076','查無可變更應繳日的資料',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3077','新核准號碼與原核准號碼，非屬於同一人',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3078','該筆撥款戶況為結案戶',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3079','該筆撥款戶況為催收結案戶',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3080','該筆撥款戶況為呆帳結案戶',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3081','無符合結案區分之撥款資料',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3082','回收期數超過可預收期數 ',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3083','撥款審核資料表尚未列印，請先作L9110交易',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3084','查無資料可內容變更',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3085','該戶號沒有額度編號，不可做內容變更',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3086','放款內容變更，必須至少變更一個項目',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3087','帳管費已達帳,不可修改',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3088','放款交易訂正須由最近一筆交易開始訂正',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3089','查無未收帳管費',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3090','查無未收火險費',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3091','查無未收契變收續費',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3092','查無未收法務費',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3093','查無暫收款(暫收款金額為零)',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3094','不可有短繳金額',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3095','短繳本金超過規定百分比金額',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3096','短繳利息超過規定百分比金額',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3097','請先將帳管費收回',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3098','請先將契變手續費收回',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3099','該筆額度(以房養老)已撥款',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3901','計算期金錯誤，總期數小於已還清期數                             ',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3902','計算期金錯誤，還本週期為零                                     ',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3903','計算期金錯誤，計算金額為零                                     ',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3904','計算期金錯誤，利率為零                                         ',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3911','計算利息錯誤，利率區分錯誤                                     ',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3912','計算利息錯誤，計息本金為零                                     ',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3913','計算利息錯誤，計息起日為零                                     ',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3914','計算利息錯誤，計息止日小於計息起日                             ',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3915','計算利息錯誤，收息週期為零                                     ',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3916','計算利息錯誤，下次利率調整日為零                               ',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3917','計算利息錯誤，利率調整週期為零                                 ',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3918','計算利息錯誤，利率基準天數為零                                 ',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3919','計算利息錯誤，每期攤還金額為零                                 ',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3920','計算利息錯誤，還本週期為零                                     ',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3921','計算利息錯誤，繳息週期及還本週期錯誤                           ',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3922','計算利息錯誤，還本週期不為繳息週期的倍數                       ',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3923','計算利息錯誤，違約金生效日為零                                 ',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3924','計算利息錯誤，週期基準錯誤                                     ',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3925','計算利息錯誤，計息止日及計息期數都為零                         ',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3926','計算利息錯誤，放款利率變動檔查無資料       ',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3927','計算利息錯誤，部分償還本金不足償還本金利息',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3928','計算利息錯誤，部分償還本金超過應償還本金利息',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E3929','計算利息錯誤，計息方式需為1:按日計息 2:按月計息',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E5001','資料筆數有誤',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E5002','繳款期數/繳納期款，擇一輸入',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E5003','組建SQL語法發生問題',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E5004','讀取DB時發生問題',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E5005','戶號不可為零',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E5006','未預期的錯誤',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E5007','身分證字號為空值',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E5008','戶號為零',null,null,null,null);
Insert into "TxErrCode" ("ErrCode","ErrContent","CreateDate","CreateEmpNo","LastUpdate","LastUpdateEmpNo") values ('E5009','資料檢核錯誤',null,null,null,null);
