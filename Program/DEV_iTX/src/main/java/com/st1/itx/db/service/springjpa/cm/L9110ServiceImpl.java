package com.st1.itx.db.service.springjpa.cm;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;

@Service("l9110ServiceImpl")
@Repository
public class L9110ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryLegalPerson(TitaVo titaVo, String applNo) throws Exception {

		this.info("L9110.findAll LegalPerson");

		// -- 法人格式
		String sql = "SELECT LPAD(CM.\"CustNo\",7,0) AS F0              "; // -- 戶號 F0
		sql += "           , LPAD(FAC.\"FacmNo\",3,0) AS F1             "; // -- 額度號碼 F1
		sql += "           , CM.\"CustName\"                            "; // -- 戶名 F2
		sql += "           , CM.\"CustId\"                              "; // -- 統編 F3
		sql += "           , FC.\"ApplNo\"                              "; // -- 核准號碼 F4
		sql += "           , CM.\"SpouseName\"                          "; // -- 負責人姓名 F5
		sql += "           , CM.\"SpouseId\"                            "; // -- 負責人身分證 F6
		sql += "           , CI.\"IndustryItem\"                        "; // -- 行業別(中文) F7
		sql += "           , CDC1.\"Item\"        AS \"CustTypeItem\"   "; // -- 客戶別(中文) F8
		sql += "           , CM.\"Birthday\"                            "; // -- 設立日期 F9
		sql += "           , \"Fn_GetCustAddr\"(CM.\"CustUKey\",'0')    ";
		sql += "                                AS \"CompanyAddr\"      "; // -- 公司地址 F10
		sql += "           , NVL(CM.\"RegZip3\",'') || NVL(CM.\"RegZip2\",'')";
		sql += "                                AS \"CompanyZip\"       "; // -- 郵遞區號(公司地址) F11
		sql += "           , ''                 AS \"CompanyTel\"       "; // -- 公司電話 ??? 新系統有特別分類嗎? F12
		sql += "           , \"Fn_GetCustAddr\"(CM.\"CustUKey\",'1')    ";
		sql += "                                AS \"ContactAddr\"      "; // -- 通訊地址 F13
		sql += "           , NVL(CM.\"CurrZip3\",'') || NVL(CM.\"CurrZip2\",'')";
		sql += "                                AS \"ContactZip\"       "; // -- 郵遞區號(通訊地址) F14
		sql += "           , ''                 AS \"ContactName\"      "; // -- 聯絡人姓名 F15
		sql += "           , ''                 AS \"ContactTel\"       "; // -- 聯絡電話(市話) F16
		sql += "           , ''                 AS \"ContactCellPhone\" "; // -- 聯絡電話(手機) F17
		sql += "           , ''                 AS \"Fax\"              "; // -- 傳真 F18
		sql += "           , ''                 AS \"Station\"          "; // -- 站別 ??? 待確認是否刪除? F19
		sql += "           , ''                 AS \"CrossUse\"         "; // -- 交互運用 F20
		sql += "           , ''                 AS \"RelName\"          "; // -- 關聯戶戶名 ??? 如何呈現? F21
		sql += "           , GUA.\"GuaId\"                              "; // -- 保證人統編 F22
		sql += "           , GUA.\"GuaName\"                            "; // -- 保證人姓名 F23
		sql += "           , GUA.\"GuaRelItem\"                         "; // -- 保證人關係 F24
		sql += "           , GUA.\"GuaAmt\"                             "; // -- 保證金額 F25
		sql += "           , GUA.\"GuaAddr\"                            "; // -- 保證人通訊地址 F26
		sql += "           , GUA.\"GuaZip\"                             "; // -- 保證人郵遞區號(通訊地址) F27
		sql += "           , FC.\"ApplDate\"                            "; // -- 鍵檔日期 F28
		sql += "           , FAC.\"LineAmt\"                            "; // -- 核准額度 F29
		sql += "           , CDC2.\"Item\" || FAC.\"AcctCode\"          ";
		sql += "                                AS \"FacAcctCode\"      "; // -- 核准科目 F30
		sql += "           , FAC.\"LoanTermYy\"                         "; // -- 貸款期間年 F31
		sql += "           , FAC.\"LoanTermMm\"                         "; // -- 貸款期間月 F32
		sql += "           , FAC.\"LoanTermDd\"                         "; // -- 貸款期間日 F33
		sql += "           , FAC.\"ProdNo\"                             "; // -- 商品代碼(原基本利率代碼) F34
		sql += "           , FAC.\"ApproveRate\"                        "; // -- 核准利率 F35
		sql += "           , FAC.\"RateAdjFreq\"                        "; // -- 利率調整週期 F36
		sql += "           , CDC3.\"Item\"        AS \"ExtraRepayItem\" "; // -- 利率調整不變攤還額 F37
		sql += "           , FAC.\"CreditScore\"                        "; // -- 信用評分 F38
		sql += "           , FAC.\"UtilDeadline\"                       "; // -- 動支期限 F39
		sql += "           , FAC.\"RateIncr\"                           "; // -- 利率加減碼 F40
		sql += "           , CDC4.\"Item\"        AS \"UsageItem\"      "; // -- 用途別 F41
		sql += "           , FAC.\"RecycleDeadline\"                    "; // -- 循環動用期限 F42
		sql += "           , NVL(CDE1.\"Fullname\",N'')                 ";
		sql += "                                AS \"IntroduceName\"    "; // -- 介紹人姓名 F43
		sql += "           , CASE                                       ";
		sql += "               WHEN FAC.\"IncomeTaxFlag\" = 'Y'         ";
		sql += "               THEN '代繳'                              ";
		sql += "             ELSE '不代繳' END   AS \"IncomeTax\"       "; // -- 代繳所得稅 F44
		sql += "           , CASE                                      ";
		sql += "               WHEN FAC.\"CompensateFlag\" = 'Y'       ";
		sql += "               THEN '代償件'                            ";
		sql += "             ELSE '非代償件' END AS \"Compensate\"      "; // -- 代償碼 F45
		sql += "           , CDC5.\"Item\"         AS \"AmortizedItem\"   "; // -- 攤還方式 F46
		sql += "           , FAC.\"GracePeriod\"                        "; // -- 寬限總月數 F47
		sql += "           , FAC.\"FirstRateAdjFreq\"                   "; // -- 首次調整週期 F48
		sql += "           , CDC6.\"Item\"         AS \"RepayItem\"       "; // -- 繳款方式 F49
		sql += "           , CDC7.\"Item\"         AS \"RepayBank\"       "; // -- 扣款銀行 F50
		sql += "           , \"Fn_GetRepayAcct\"(FAC.\"CustNo\",FAC.\"FacmNo\",'0')";
		sql += "                                 AS \"RepayAcct\"       "; // -- 扣款帳號 F51
		sql += "           ,FAC.\"PayIntFreq\"                          "; // -- 繳息週期 F52
		sql += "           ,FAC.\"RateCode\"                            "; // -- 利率區分 F53
		sql += "           ,PROD.\"BreachCode\"                         "; // -- 違約適用方式 F54
		sql += "           ,PROD.\"BreachPercent\"                      "; // -- 違約率-金額 F55
		sql += "           ,PROD.\"BreachDecreaseMonth\"                "; // -- 違約率-月數 F56
		sql += "           ,''                   AS \"BreachMonth\"     "; // -- 違約還款月數 ??? F57
		sql += "           ,''                   AS \"LastMonth\"       "; // -- 前段月數 ??? F58
		sql += "           ,FAC.\"PieceCode\"                           "; // -- 計件代碼 F59
		sql += "           ,NVL(CDE2.\"Fullname\",FAC.\"FireOfficer\")  ";
		sql += "                                 AS \"FireOfficer\"     "; // -- 火險服務姓名 *** 串不到姓名時顯示員編 F60
		sql += "           ,NVL(CDE3.\"Fullname\",FAC.\"LoanOfficer\")  ";
		sql += "                                 AS \"LoanOfficer\"     "; // -- 放款專員 *** 串不到姓名時顯示員編 F61
		sql += "           ,NVL(CDE4.\"Fullname\",FAC.\"Supervisor\")   ";
		sql += "                                 AS \"Supervisor\"      "; // -- 督辦姓名 *** 串不到姓名時顯示員編 F62
		sql += "           ,PROD.\"ProhibitMonth\"                      "; // -- 限制清償年限 *** 原\"禁領清償年限\" F63
		sql += "           ,FAC.\"AcctFee\"                             "; // -- 帳管費 F64
		sql += "           ,NVL(CDE5.\"Fullname\",FAC.\"EstimateReview\") ";
		sql += "                                 AS \"EstimateReview\"  "; // -- 估價覆核姓名 *** 串不到姓名時顯示員編 F65
		sql += "           ,CM.\"CustTypeCode\" || ' ' || CDC1.\"Item\"   ";
		sql += "                                 AS \"CustTypeItem2\"   "; // -- 客戶別 *** 與第一段的客戶別差異: 代碼+中文 F66
		sql += "           ,NVL(CDE6.\"Fullname\",FAC.\"InvestigateOfficer\") ";
		sql += "                                 AS \"InvestigateOfficer\" ";
		sql += "                                                      "; // -- 徵信姓名 *** 串不到姓名時顯示員編 F67
		sql += "           ,NVL(CDE7.\"Fullname\",FAC.\"CreditOfficer\") ";
		sql += "                                 AS \"CreditOfficer\"   "; // -- 授信姓名 *** 串不到姓名時顯示員編 F68
		sql += "           ,NVL(CDE8.\"Fullname\",FAC.\"Coorgnizer\") ";
		sql += "                                 AS \"Coorgnizer\"      "; // -- 協辦姓名 *** 串不到姓名時顯示員編 F69
		sql += "           ,NVL(LBM.\"LoanBal\",0) AS \"LoanBal\"       "; // -- 本戶目前總額 F70
		sql += "      FROM \"FacCaseAppl\" FC "; // -- 案件申請檔 ";
		sql += "      LEFT JOIN \"CustMain\" CM ON CM.\"CustUKey\" = FC.\"CustUKey\" "; // -- 客戶資料主檔
		sql += "      LEFT JOIN \"FacMain\" FAC ON FAC.\"ApplNo\" = FC.\"ApplNo\" "; // -- 額度主檔
		sql += "      LEFT JOIN \"CdIndustry\" CI ON CI.\"IndustryCode\" = CM.\"IndustryCode\" "; // -- 行業別代碼檔
		sql += "      LEFT JOIN \"CdCode\" CDC1 ON CDC1.\"DefCode\" = 'CustTypeCode' "; // -- 共用代碼檔(客戶別)
		sql += "                             AND CDC1.\"Code\" = CM.\"CustTypeCode\" ";
		sql += "      LEFT JOIN (SELECT GUA.\"ApproveNo\"                         AS \"ApplNo\"  "; // -- *** ApproveNo
																									// 將會統一改為 ApplNo
		sql += "                      , GUACM.\"CustId\"                          AS \"GuaId\"   "; // -- 保證人統編
		sql += "                      , GUACM.\"CustName\"                        AS \"GuaName\" "; // -- 保證人姓名
		sql += "                      , CDG.\"GuaRelItem\"                                     "; // -- 保證人關係
		sql += "                      , GUA.\"GuaAmt\"                                         "; // -- 保證金額
		sql += "                      , \"Fn_GetCustAddr\"(GUACM.\"CustUKey\",'1')  AS \"GuaAddr\" "; // -- 保證人通訊地址
		sql += "                      , NVL(GUACM.\"CurrZip3\",'') || NVL(GUACM.\"CurrZip2\",'') ";
		sql += "                                                                AS \"GuaZip\"  "; // -- 保證人郵遞區號(通訊地址)
		sql += "                      , ROW_NUMBER() OVER (PARTITION BY GUA.\"ApproveNo\" ORDER BY GUA.\"GuaAmt\" DESC) ";
		sql += "                                                                AS \"Seq\" ";
		sql += "                 FROM \"Guarantor\" GUA ";
		sql += "                 LEFT JOIN \"CustMain\" GUACM ON GUACM.\"CustUKey\" = GUA.\"GuaUKey\" "; // --
																											// 客戶資料主檔(保證人)
		sql += "                 LEFT JOIN \"CdGuarantor\" CDG ON CDG.\"GuaRelCode\" = GUA.\"GuaRelCode\" "; // --
																												// 保證人關係代碼檔
		sql += "                 WHERE GUA.\"GuaStatCode\" = '1' "; // -- 設定
		sql += "                ) GUA ON GUA.\"ApplNo\" = \"FC\".\"ApplNo\"  "; // -- 保證人檔
		sql += "                     AND GUA.\"Seq\"    = 1 "; // -- 取保證金額最大者一筆
		sql += "      LEFT JOIN \"CdCode\" CDC2 ON CDC2.\"DefCode\" = 'AcctCode' "; // -- 共用代碼檔(核准科目)
		sql += "                             AND CDC2.\"Code\" = FAC.\"AcctCode\" ";
		sql += "      LEFT JOIN \"CdCode\" CDC3 ON CDC3.\"DefCode\" = 'ExtraRepayCode' "; // -- 共用代碼檔(利率調整不變攤還額)
		sql += "                             AND CDC3.\"Code\" = FAC.\"ExtraRepayCode\" ";
		sql += "      LEFT JOIN \"CdCode\" CDC4 ON CDC4.\"DefCode\" = 'UsageCode' "; // -- 共用代碼檔(用途別)
		sql += "                             AND CDC4.\"Code\" = FAC.\"UsageCode\" ";
		sql += "      LEFT JOIN \"CdEmp\" CDE1 ON CDE1.\"EmployeeNo\" = FAC.\"Introducer\" "; // --員工資料檔(介紹人)
		sql += "      LEFT JOIN \"CdCode\" CDC5 ON CDC5.\"DefCode\" = 'FacmAmortizedCode' "; // -- 共用代碼檔(攤還方式)
		sql += "                             AND CDC5.\"Code\" = FAC.\"AmortizedCode\" ";
		sql += "      LEFT JOIN \"CdCode\" CDC6 ON CDC6.\"DefCode\" = 'FacmRepayCode' "; // -- 共用代碼檔(繳款方式)
		sql += "                             AND CDC6.\"Code\" = FAC.\"RepayCode\" ";
		sql += "      LEFT JOIN \"CdCode\" CDC7 ON CDC7.\"DefCode\" = 'BankDeductCd' "; // -- 共用代碼檔(扣款銀行)
		sql += "                             AND CDC7.\"Code\" = \"Fn_GetRepayAcct\"(FAC.\"CustNo\",FAC.\"FacmNo\",'0') ";
		sql += "      LEFT JOIN \"FacProd\" PROD ON PROD.\"ProdNo\" = FAC.\"ProdNo\" ";
		sql += "      LEFT JOIN \"CdEmp\" CDE2 ON CDE2.\"EmployeeNo\" = FAC.\"FireOfficer\" "; // --員工資料檔(火險服務)
		sql += "      LEFT JOIN \"CdEmp\" CDE3 ON CDE3.\"EmployeeNo\" = FAC.\"LoanOfficer\" "; // --員工資料檔(放款專員)
		sql += "      LEFT JOIN \"CdEmp\" CDE4 ON CDE4.\"EmployeeNo\" = FAC.\"Supervisor\" "; // --員工資料檔(督辦)
		sql += "      LEFT JOIN \"CdEmp\" CDE5 ON CDE5.\"EmployeeNo\" = FAC.\"EstimateReview\" "; // --員工資料檔(估價覆核)
		sql += "      LEFT JOIN \"CdEmp\" CDE6 ON CDE6.\"EmployeeNo\" = FAC.\"InvestigateOfficer\" "; // --員工資料檔(徵信)
		sql += "      LEFT JOIN \"CdEmp\" CDE7 ON CDE7.\"EmployeeNo\" = FAC.\"CreditOfficer\" "; // --員工資料檔(授信)
		sql += "      LEFT JOIN \"CdEmp\" CDE8 ON CDE8.\"EmployeeNo\" = FAC.\"Coorgnizer\" "; // --員工資料檔(協辦)
		sql += "      LEFT JOIN ( SELECT \"CustNo\"";
		sql += "                        ,SUM(\"LoanBal\") AS \"LoanBal\"";
		sql += "                  FROM \"LoanBorMain\" ";
		sql += "                  GROUP BY \"CustNo\"";
		sql += "                ) LBM ON LBM.\"CustNo\" = CM.\"CustNo\""; // --總額
		sql += "      WHERE FC.\"ApplNo\" = :applNo";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("applNo", applNo);

		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryCl(TitaVo titaVo, String applNo) throws Exception {

		this.info("L9110ServiceImpl.queryCl ");

		// -- L9110 Query(擔保品)
		String sql = "SELECT CASE";
		sql += "                WHEN CF.\"ClCode1\" IN ('1','2')";
		sql += "                THEN '不動產'";
		sql += "                WHEN CF.\"ClCode1\" IN ('3','4')";
		sql += "                THEN '股票'";
		sql += "                WHEN CF.\"ClCode1\" = '5'";
		sql += "                THEN '其他'";
		sql += "                WHEN CF.\"ClCode1\" = '9'";
		sql += "                THEN '動產'";
		sql += "              ELSE ' ' END             AS \"Collateral\"  "; // -- F0 擔保品資料 *** 原\"押品資料\"
		sql += "            , LPAD(CF.\"ClNo\",7,'0')  AS \"ClNo\"        "; // -- F1 擔保品號碼 *** 原\"押品號碼\"
		sql += "            , CDC1.\"Item\"            AS \"ClItem\"      "; // -- F2 擔保品別 *** 原\"押品別\"
		sql += "            , CLM.\"EvaDate\"          AS \"EvaDate\"     "; // -- F3 鑑價日期
		sql += "            , ''                       AS \"OtherDate\"   "; // -- F4 他項存續期限 ???
		sql += "            , CLI.\"SettingSeq\"                       "; // -- F5 順位 ** 只有不動產會有此欄位，動產、股票、其他擔保品會是null
		sql += "            , CLI.\"FirstAmt\"                         "; // -- F6 前順位金額 ***
																			// 只有不動產會有此欄位，動產、股票、其他擔保品會是null
		sql += "            , CITY.\"CityItem\"                        "; // -- F7 地區別
		sql += "            , CDC2.\"Item\"            AS \"EvaCompany\"  "; // -- F8 鑑定公司 ***
																				// 只有不動產會有此欄位，動產、股票、其他擔保品會是null
		sql += "            , CLI.\"BdRmk\"                            "; // -- F9 建物標示備註 ***
																			// 只有不動產會有此欄位，動產、股票、其他擔保品會是null
		sql += "            , CASE";
		sql += "                WHEN CF.\"ClCode1\" IN ('1','2')";
		sql += "                THEN CLI.\"SettingDate\" ";
		sql += "                WHEN CF.\"ClCode1\" IN ('3','4')";
		sql += "                THEN CLS.\"SettingDate\" ";
		sql += "                WHEN CF.\"ClCode1\" = '5'";
		sql += "                THEN CLO.\"SettingDate\" ";
		sql += "                WHEN CF.\"ClCode1\" = '9'";
		sql += "                THEN CLMOV.\"SettingDate\" ";
		sql += "              ELSE 0 END               AS \"SettingDate\" "; // -- F10 設定日期
		sql += "            , CF.\"ClCode1\"           AS \"ClCode1\"  "; // -- F11 擔保品代號1
		sql += "            , CLS.\"StockCode\"";
		sql += "              || ' '";
		sql += "              || CDS.\"StockItem\"     AS \"StockName\" "; // F12 股票代號及股票名稱
		sql += "            , CLS.\"PledgeNo\"         AS \"PledgeNo\" "; // F13 質權設定書號
		sql += "            , CLS.\"ThreeMonthAvg\"    AS \"ThreeMonthAvg\" ";// F14 三個月平均價
		sql += "            , CLS.\"YdClosingPrice\"   AS \"YdClosingPrice\" "; // F15 前日收盤價
		sql += "            , CLS.\"EvaUnitPrice\"     AS \"EvaUnitPrice\" "; // F16 鑑定單價
		sql += "            , CLM.\"EvaAmt\"           AS \"EvaAmt\" ";// F17 鑑定總價
		sql += "            , CLS.\"LoanToValue\"      AS \"LTV\" "; // F18 貸放成數
		sql += "            , FAC.\"LineAmt\"          AS \"LineAmt\" "; // F19 核准額度
		sql += "            , CLS.\"CustodyNo\"        AS \"CustodyNo\" ";// F20 保管條號碼
		sql += "       FROM \"ClFac\" CF";
		sql += "       LEFT JOIN \"FacMain\" FAC ON FAC.\"ApplNo\" = CF.\"ApproveNo\"";
		sql += "       LEFT JOIN \"CdCode\" CDC1 ON CDC1.\"DefCode\" = 'ClCode2' || CF.\"ClCode1\"";
		sql += "                                AND CDC1.\"Code\"    = LPAD(CF.\"ClCode2\",2,'0')";
		sql += "       LEFT JOIN \"ClMain\" CLM ON CLM.\"ClCode1\" = CF.\"ClCode1\"";
		sql += "                               AND CLM.\"ClCode2\" = CF.\"ClCode2\"";
		sql += "                               AND CLM.\"ClNo\"    = CF.\"ClNo\"";
		sql += "       LEFT JOIN \"ClImm\" CLI ON CLI.\"ClCode1\" = CF.\"ClCode1\"";
		sql += "                              AND CLI.\"ClCode2\" = CF.\"ClCode2\"";
		sql += "                              AND CLI.\"ClNo\"    = CF.\"ClNo\"";
		sql += "       LEFT JOIN \"CdCity\" CITY ON CITY.\"CityCode\" = CLM.\"CityCode\"";
		sql += "       LEFT JOIN \"ClStock\" CLS ON CLS.\"ClCode1\" = CF.\"ClCode1\"";
		sql += "                                AND CLS.\"ClCode2\" = CF.\"ClCode2\"";
		sql += "                                AND CLS.\"ClNo\"    = CF.\"ClNo\"";
		sql += "       LEFT JOIN \"CdStock\" CDS ON CDS.\"StockCode\" = CLS.\"StockCode\"";
		sql += "       LEFT JOIN \"ClOther\" CLO ON CLO.\"ClCode1\" = CF.\"ClCode1\"";
		sql += "                                AND CLO.\"ClCode2\" = CF.\"ClCode2\"";
		sql += "                                AND CLO.\"ClNo\"    = CF.\"ClNo\"";
		sql += "       LEFT JOIN \"ClMovables\" CLMOV ON CLMOV.\"ClCode1\" = CF.\"ClCode1\"";
		sql += "                                     AND CLMOV.\"ClCode2\" = CF.\"ClCode2\"";
		sql += "                                     AND CLMOV.\"ClNo\"    = CF.\"ClNo\"";
		sql += "       LEFT JOIN \"CdCode\" CDC2 ON CDC2.\"DefCode\" = 'EvaCompanyCode'";
		sql += "                                AND CDC2.\"Code\" = NVL(CLI.\"EvaCompanyCode\",' ')";
		sql += "       WHERE CF.\"ApproveNo\" = :applNo";
		sql += "         AND CF.\"MainFlag\" = 'Y' "; // -- 主要擔保品

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("applNo", applNo);

		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryLand(TitaVo titaVo, String applNo) throws Exception {

		this.info("L9110ServiceImpl.queryLand");

		String sql = ""; // -- L9110 Query(土地)
		sql += "SELECT ROW_NUMBER() OVER (PARTITION BY CF.\"ClCode1\"";
		sql += "                                     , CF.\"ClCode2\"";
		sql += "                                     , CF.\"ClNo\"";
		sql += "                          ORDER BY LO.\"LandSeq\""; // -- ??? 房地只取一筆?
		sql += "                         ) AS \"Seq\"     "; // -- 序號
		sql += "     , LO.\"OwnerId\"                     "; // -- 提供人 ??? 房地只取一筆?
		sql += "     , CITY.\"CityItem\"                  "; // -- 縣市
		sql += "     , AREA.\"AreaItem\"                  "; // -- 鄉鎮區
		sql += "     , CDLS.\"IrItem\"                    "; // -- 段小段
		sql += "     , L.\"LandNo1\" || '-' || L.\"LandNo2\"";
		sql += "                           AS \"LandNo\"  "; // -- 地號
		sql += "     , L.\"Area\"                         "; // -- 面積
		sql += "     , L.\"TransferedYear\"               "; // -- 年度
		sql += "     , L.\"LastTransferedAmt\"            "; // -- 前次移轉
		sql += "     , L.\"EvaUnitPrice\"                 "; // -- 鑑定單價
		sql += "     , 0                   AS \"ApplyAmt\""; // -- 核貸 ???
		sql += "     , CLI.\"SettingAmt\"                 "; // -- 設定
		sql += " FROM \"ClFac\" CF ";
		sql += " LEFT JOIN \"ClLand\" L ON L.\"ClCode1\" = CF.\"ClCode1\"";
		sql += "                    AND L.\"ClCode2\" = CF.\"ClCode2\"";
		sql += "                    AND L.\"ClNo\"    = CF.\"ClNo\"";
		sql += " LEFT JOIN \"ClLandOwner\" LO ON LO.\"ClCode1\" = CF.\"ClCode1\"";
		sql += "                             AND LO.\"ClCode2\" = CF.\"ClCode2\"";
		sql += "                             AND LO.\"ClNo\"    = CF.\"ClNo\"";
		sql += "                            AND LO.\"LandSeq\" <= 1"; // -- 房地從1開始編,土地固定0 ??? 房地只取一筆?
		sql += " LEFT JOIN \"CdCity\" CITY ON CITY.\"CityCode\" = L.\"CityCode\"";
		sql += " LEFT JOIN \"CdArea\" AREA ON AREA.\"CityCode\" = L.\"CityCode\"";
		sql += "                          AND AREA.\"AreaCode\" = L.\"AreaCode\"";
		sql += " LEFT JOIN \"CdLandSection\" CDLS ON CDLS.\"CityCode\" = L.\"CityCode\"";
		sql += "                                 AND CDLS.\"AreaCode\" = L.\"AreaCode\"";
		sql += "                                 AND CDLS.\"IrCode\"   = L.\"IrCode\"";
		sql += " LEFT JOIN \"ClImm\" CLI ON CLI.\"ClCode1\" = CF.\"ClCode1\"";
		sql += "                        AND CLI.\"ClCode2\" = CF.\"ClCode2\"";
		sql += "                        AND CLI.\"ClNo\"    = CF.\"ClNo\"";
		sql += " WHERE CF.\"ApproveNo\" = :applNo";
		sql += "   AND CF.\"MainFlag\" = 'Y'"; // -- 主要擔保品

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("applNo", applNo);

		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryBuilding(TitaVo titaVo, String applNo) throws Exception {

		this.info("L9110ServiceImpl.queryBuilding");

		// -- L9110 法人Query(建物)
		String sql = "";
		sql += "SELECT ROW_NUMBER() OVER (PARTITION BY CF.\"ClCode1\"";
		sql += "                                     , CF.\"ClCode2\"";
		sql += "                                     , CF.\"ClNo\"";
		sql += "                          ORDER BY LO.\"OwnerPart\" DESC "; // -- ??? 待確認排序方式
		sql += "                         )   AS \"Seq\"    "; // -- 序號 F0
		sql += "     , LO.\"OwnerId\"                      "; // -- 提供人 F1
		sql += "     , LPAD(L.\"BdNo1\",5,'0') AS \"BdNo\"   "; // -- 主建物建號 F2
		sql += "     , LPAD(CBP1.\"PublicBdNo1\",5,'0') ";
		sql += "                            AS \"PublicBdNo\"";
		sql += "                                         "; // -- 公設建號 F3
		sql += "     , L.\"FloorArea\"                     "; // -- 主建物 F4
		sql += "     , CBP2.\"PublicArea\"                 "; // -- 公設 F5
		sql += "     , CBPK.\"ParkingArea\"                "; // -- 車位 F6
		sql += "     , L.\"EvaUnitPrice\"                  "; // -- 鑑定單價 F7
		sql += "     , 0                   AS \"ApplyAmt\" "; // -- 核貸 ??? F8
		sql += "     , CLI.\"SettingAmt\"                  "; // -- 設定 F9
		sql += "     , L.\"BdLocation\"                    "; // -- 門牌地址 F10
		sql += "FROM \"ClFac\" CF ";
		sql += "LEFT JOIN \"ClBuilding\" L ON L.\"ClCode1\" = CF.\"ClCode1\"";
		sql += "                        AND L.\"ClCode2\" = CF.\"ClCode2\"";
		sql += "                        AND L.\"ClNo\"    = CF.\"ClNo\"";
		sql += "LEFT JOIN \"ClBuildingOwner\" LO ON LO.\"ClCode1\" = CF.\"ClCode1\"";
		sql += "                              AND LO.\"ClCode2\" = CF.\"ClCode2\"";
		sql += "                              AND LO.\"ClNo\"    = CF.\"ClNo\"";
		sql += "LEFT JOIN \"ClImm\" CLI ON CLI.\"ClCode1\" = CF.\"ClCode1\"";
		sql += "                     AND CLI.\"ClCode2\" = CF.\"ClCode2\"";
		sql += "                     AND CLI.\"ClNo\"    = CF.\"ClNo\"";
		sql += "LEFT JOIN ( SELECT \"ClCode1\"";
		sql += "                 , \"ClCode2\"";
		sql += "                 , \"ClNo\"";
		sql += "                 , \"PublicBdNo1\"";
		sql += "                 , ROW_NUMBER() OVER (PARTITION BY \"ClCode1\"";
		sql += "                                                 , \"ClCode2\"";
		sql += "                                                 , \"ClNo\"";
		sql += "                                      ORDER BY \"PublicBdNo1\"";
		sql += "                                     ) AS \"Seq\"";
		sql += "            FROM \"ClBuildingPublic\"";
		sql += "          ) CBP1 ON CBP1.\"ClCode1\" = CF.\"ClCode1\"";
		sql += "                AND CBP1.\"ClCode2\" = CF.\"ClCode2\"";
		sql += "                AND CBP1.\"ClNo\"    = CF.\"ClNo\"";
		sql += "                AND CBP1.\"Seq\"     = 1 ";
		sql += "LEFT JOIN ( SELECT \"ClCode1\"";
		sql += "                 , \"ClCode2\"";
		sql += "                 , \"ClNo\"";
		sql += "                 , SUM(\"Area\") AS \"PublicArea\"";
		sql += "            FROM \"ClBuildingPublic\"";
		sql += "            GROUP BY \"ClCode1\"";
		sql += "                   , \"ClCode2\"";
		sql += "                   , \"ClNo\"";
		sql += "          ) CBP2 ON CBP2.\"ClCode1\" = CF.\"ClCode1\"";
		sql += "                AND CBP2.\"ClCode2\" = CF.\"ClCode2\"";
		sql += "                AND CBP2.\"ClNo\"    = CF.\"ClNo\"";
		sql += "LEFT JOIN ( SELECT \"ClCode1\"";
		sql += "                 , \"ClCode2\"";
		sql += "                 , \"ClNo\"";
		sql += "                 , SUM(\"Area\") AS \"ParkingArea\"";
		sql += "            FROM \"ClBuildingParking\"";
		sql += "            GROUP BY \"ClCode1\"";
		sql += "                   , \"ClCode2\"";
		sql += "                   , \"ClNo\"";
		sql += "          ) CBPK ON CBPK.\"ClCode1\" = CF.\"ClCode1\"";
		sql += "                AND CBPK.\"ClCode2\" = CF.\"ClCode2\"";
		sql += "                AND CBPK.\"ClNo\"    = CF.\"ClNo\"";
		sql += "WHERE CF.\"ApproveNo\" = :applNo";
		sql += "  AND CF.\"MainFlag\" = 'Y' "; // -- 主要擔保品

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("applNo", applNo);

		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryInsu(TitaVo titaVo, String applNo) throws Exception {

		this.info("L9110ServiceImpl.queryInsu");

		// -- L9110 法人Query(火險)
		String sql = "";
		sql += " SELECT ROW_NUMBER() OVER (PARTITION BY CF.\"ApproveNo\"";
		sql += "                           ORDER BY IR.\"PrevInsuNo\" DESC"; // -- ??? 待確認排序方式
		sql += "                          )   AS \"Seq\""; // -- 序號
		sql += "      , IR.\"NowInsuNo\""; // -- 保單號碼
		sql += "      , IR.\"InsuAmt\""; // -- 保險金額
		sql += "      , IR.\"InsuStartDate\""; // -- 保險起日
		sql += "      , IR.\"InsuEndDate\""; // -- 保險迄日
		sql += "      , IR.\"InsuCompany\""; // -- 保險公司
		sql += " FROM \"ClFac\" CF";
		sql += " LEFT JOIN ( SELECT IR.\"ClCode1\"";
		sql += "                  , IR.\"ClCode2\"";
		sql += "                  , IR.\"ClNo\"";
		sql += "                  , IR.\"PrevInsuNo\"";
		sql += "                  , IR.\"NowInsuNo\"";
		sql += "                  , IR.\"FireInsuCovrg\" + IR.\"EthqInsuCovrg\" AS \"InsuAmt\"";
		sql += "                  , IR.\"InsuStartDate\"";
		sql += "                  , IR.\"InsuEndDate\"";
		sql += "                  , CDC1.\"Item\"                             AS \"InsuCompany\"";
		sql += "                  , ROW_NUMBER() OVER (PARTITION BY IR.\"ClCode1\"";
		sql += "                                                  , IR.\"ClCode2\"";
		sql += "                                                  , IR.\"ClNo\"";
		sql += "                                                  , IR.\"PrevInsuNo\"";
		sql += "                                       ORDER BY IR.\"InsuStartDate\" DESC";
		sql += "                                      ) AS \"Seq\"";
		sql += "             FROM \"InsuRenew\" IR ";
		sql += "             LEFT JOIN \"CdCode\" CDC1 ON CDC1.\"DefCode\" = 'InsuCompany'"; // -- 共用代碼檔(保險公司)
		sql += "                                    AND CDC1.\"Code\"    = IR.\"InsuCompany\"";
		sql += "           ) IR ON IR.\"ClCode1\" = CF.\"ClCode1\"";
		sql += "               AND IR.\"ClCode2\" = CF.\"ClCode2\"";
		sql += "               AND IR.\"ClNo\"    = CF.\"ClNo\"";
		sql += "               AND IR.\"Seq\" = 1"; // -- 每張保單取最新一筆 ??? 待確認此邏輯是否正確
		sql += " WHERE CF.\"ApproveNo\" = :applNo";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("applNo", applNo);

		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryStock(TitaVo titaVo, String applNo) throws Exception {

		this.info("L9110ServiceImpl.queryStock");

		// -- L9110 Query(股票)
		String sql = "";
		sql += " SELECT CS.\"OwnerId\" "; // F0 股票持有人統編
		sql += "      , CS.\"SettingBalance\" "; // F1 設質股數餘額
		sql += "      , CS.\"SettingBalance\" ";
		sql += "        * CS.\"ParValue\" AS \"TotalParValue\" "; // F2 面額合計
		sql += " FROM \"ClFac\" CF";
		sql += " LEFT JOIN \"ClStock\" CS ON CS.\"ClCode1\" = CF.\"ClCode1\" ";
		sql += "                         AND CS.\"ClCode2\" = CF.\"ClCode2\" ";
		sql += "                         AND CS.\"ClNo\" = CF.\"ClNo\" ";
		sql += " WHERE CF.\"ApproveNo\" = :applNo ";
		sql += "   AND CS.\"ClCode1\" = 3 "; // 串得到股票擔保品的資料才進表 

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("applNo", applNo);

		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryNaturalPerson(TitaVo titaVo, String applNo) throws Exception {

		this.info("L9110.findAll NaturalPerson");

		// -- L9110 自然人Query
		String sql = "";
		sql += " SELECT LPAD(FM.\"CustNo\",7,0) || '-' || LPAD(FM.\"FacmNo\",3,0) AS \"F0 戶號\" ";
		sql += "       ,CM.\"CustName\" AS \"F1 戶名\" ";
		sql += "       ,CM.\"CustId\" AS \"F2 統編\" ";
		sql += "       ,FCA.\"ApplNo\" AS \"F3 核准編號\" ";
		sql += "       ,CdSex.\"Item\" AS \"F4 性別\" ";
		sql += "       ,CM.\"Birthday\" AS \"F5 出生年月日\" ";
		sql += "       ,CdCustType.\"Item\" AS \"F6 客戶別\" ";
		sql += "       ,CM.\"EmpNo\" AS \"F7 員工代號\" ";
		sql += "       ,CASE";
		sql += "          WHEN CM.\"EmpNo\" IS NULL";
		sql += "          THEN '非員工'";
		sql += "          WHEN NVL(CDE9.\"CommLineCode\",' ') = '35'";
		sql += "          THEN '十五日薪'";
		sql += "        ELSE '非十五日薪' END AS \"F8 十五日薪\""; // -- ??? 未確認邏輯
		sql += "       ,CM.\"SpouseName\" AS \"F9 配偶姓名\" ";
		sql += "       ,CM.\"SpouseId\" AS \"F10 配偶統一編號\" ";
		sql += "       ,CTNCell.\"TelNo\" AS \"F11 BBC\"  "; // -- ?? 手機?
		sql += "       ,\"Fn_GetCustAddr\"(CM.\"CustUKey\",'0') AS \"F12 戶籍地址\" ";
		sql += "       ,CM.\"RegZip3\" || CM.\"RegZip2\" AS \"F13 戶籍-郵遞區號\" ";
		sql += "       ,CTNHome.\"TelArea\" || CTNHome.\"TelNo\" || CTNHome.\"TelExt\" AS \"F14 戶籍電話\" ";
		sql += "       ,\"Fn_GetCustAddr\"(CM.\"CustUKey\",'1') AS \"F15 通訊地址\" ";
		sql += "       ,CM.\"CurrZip3\" || CM.\"CurrZip2\" AS \"F16 通訊-郵遞區號\" ";
		sql += "       ,' ' AS \"F17 聯絡人姓名\"  "; // -- ??
		sql += "       ,'(O) '  ";
		sql += "       || RPAD(NVL(CTNWork.\"TelArea\" || CTNWork.\"TelNo\" || CTNWork.\"TelExt\", ' '),30) ";
		sql += "       || ' (H) '  ";
		sql += "       || CTNHome.\"TelArea\" || CTNHome.\"TelNo\" || CTNHome.\"TelExt\" AS \"F18 聯絡電話\" ";
		sql += "       ,' ' AS \"F19 交互運用\"  "; // -- ??
		sql += "       ,CTNFax.\"TelArea\" || CTNFax.\"TelNo\" || CTNFax.\"TelExt\" AS \"F20 傳真\" ";
		sql += "       ,GUA.\"GuaId\" AS \"F21 保證人統編\" ";
		sql += "       ,GUA.\"GuaName\" AS \"F22 保證人姓名\" ";
		sql += "       ,GUA.\"GuaRelItem\" AS \"F23 保證人關係\" ";
		sql += "       ,GUA.\"GuaAmt\" AS \"F24 保證金額\" ";
		sql += "       ,GUA.\"GuaAddr\" AS \"F25 保證人通訊地址\" ";
		sql += "       ,GUA.\"GuaZip\" AS \"F26 保證人郵遞區號(通訊)\" ";
		sql += "       ,FCA.\"ApplDate\" AS \"F27 鍵檔日期\" ";
		sql += "       ,FM.\"LineAmt\" AS \"F28 核准額度\" ";
		sql += "       ,CDC2.\"Item\" || FM.\"AcctCode\" AS \"F29 核准科目\" ";
		sql += "       ,FM.\"LoanTermYy\" AS \"F30 貸款期間年\" ";
		sql += "       ,FM.\"LoanTermMm\" AS \"F31 貸款期間月\" ";
		sql += "       ,FM.\"LoanTermDd\" AS \"F32 貸款期間日\" ";
		sql += "       ,FM.\"ProdNo\" AS \"F33 商品代碼\" ";
		sql += "       ,FM.\"ApproveRate\" AS \"F34 核准利率\" ";
		sql += "       ,FM.\"RateAdjFreq\" AS \"F35 利率調整週期\" ";
		sql += "       ,CDC3.\"Item\" AS \"F36 利率調整不變攤還額\" ";
		sql += "       ,FM.\"CreditScore\" AS \"F37 信用評分\" ";
		sql += "       ,FM.\"UtilDeadline\" AS \"F38 動支期限\" ";
		sql += "       ,FM.\"RateIncr\" AS \"F39 利率加減碼\" ";
		sql += "       ,CDC4.\"Item\" AS \"F40 用途別\" ";
		sql += "       ,FM.\"RecycleDeadline\" AS \"F41 循環動用期限\" ";
		sql += "       ,NVL(CDE1.\"Fullname\",' ')  AS \"F42 介紹人姓名\" ";
		sql += "       ,CASE ";
		sql += "          WHEN FM.\"IncomeTaxFlag\" = 'Y' ";
		sql += "          THEN '代繳' ";
		sql += "        ELSE '不代繳' END AS \"F43 代繳所得稅\" ";
		sql += "       ,CASE ";
		sql += "          WHEN FM.\"CompensateFlag\" = 'Y' ";
		sql += "          THEN '代償件' ";
		sql += "        ELSE '非代償件' END AS \"F44 代償碼\" ";
		sql += "       ,CDC5.\"Item\" AS \"F45 攤還方式\" ";
		sql += "       ,FM.\"GracePeriod\" AS \"F46 寬限總期數\" ";
		sql += "       ,FM.\"FirstRateAdjFreq\" AS \"F47 首次調整週期\" ";
		sql += "       ,CDC6.\"Item\" AS \"F48 繳款方式\" ";
		sql += "       ,CDC7.\"Item\" AS \"F49 扣款銀行\" ";
		sql += "       ,\"Fn_GetRepayAcct\"(FM.\"CustNo\",FM.\"FacmNo\",'0') AS \"F50 扣款帳號\" ";
		sql += "       ,FM.\"PayIntFreq\" AS \"F51 繳息週期\" ";
		sql += "       ,FM.\"RateCode\" AS \"F52 利率區分\" ";
		sql += "       ,PROD.\"BreachCode\" AS \"F53 違約適用方式\" ";
		sql += "       ,PROD.\"BreachPercent\" AS \"F54 違約率-金額\" ";
		sql += "       ,PROD.\"BreachDecreaseMonth\" AS \"F55 違約率-月數\" ";
		sql += "       ,' ' AS \"F56 違約還款月數\"  "; // -- ??
		sql += "       ,' ' AS \"F57 前段月數\"  "; // -- ??
		sql += "       ,GROUPCM.\"CustName\" AS \"F58 團體戶名\" ";
		sql += "       ,FM.\"PieceCode\" AS \"F59 計件代碼\" ";
		sql += "       ,NVL(CDE2.\"Fullname\",FM.\"FireOfficer\") AS \"F60 火險服務姓名\" "; // -- 串不到時顯示員編
		sql += "       ,NVL(CDE3.\"Fullname\",FM.\"LoanOfficer\") AS \"F61 放款專員\" "; // -- 串不到時顯示員編
		sql += "       ,NVL(CDE4.\"Fullname\",FM.\"Supervisor\") AS \"F62 督辦姓名\" "; // -- 串不到姓名時顯示員編
		sql += "       ,PROD.\"ProhibitMonth\" AS \"F63 限制清償年限\"  "; // -- 原\"禁領清償年限\"
		sql += "       ,FM.\"AcctFee\" AS \"F64 帳管費\" ";
		sql += "       ,NVL(CDE5.\"Fullname\",FM.\"EstimateReview\") AS \"F65 估價覆核姓名\" "; // -- 串不到姓名時顯示員編
		sql += "       ,CM.\"CustTypeCode\" || ' ' || CDC1.\"Item\" AS \"F66 客戶別\" "; // -- 與第一段的客戶別差異: 代碼+中文
		sql += "       ,NVL(CDE6.\"Fullname\",FM.\"InvestigateOfficer\") AS \"F67 徵信姓名\" "; // -- 串不到姓名時顯示員編
		sql += "       ,NVL(CDE7.\"Fullname\",FM.\"CreditOfficer\") AS \"F68 授信姓名\" "; // -- 串不到姓名時顯示員編
		sql += "       ,NVL(CDE8.\"Fullname\",FM.\"Coorgnizer\") AS \"F69 協辦姓名\" "; // -- 串不到姓名時顯示員編
		sql += "       ,NVL(LBM.\"LoanBal\",0) AS \"F70 本戶目前總額\" ";
		sql += " FROM \"FacCaseAppl\" FCA ";
		sql += " LEFT JOIN \"FacMain\" FM ON FM.\"ApplNo\" = FCA.\"ApplNo\" ";
		sql += " LEFT JOIN \"CustMain\" CM ON CM.\"CustUKey\" = FCA.\"CustUKey\" ";
		sql += " LEFT JOIN \"CdCode\" CdSex ON CdSex.\"DefCode\" = 'Sex'  ";
		sql += "                         AND CdSex.\"Code\" = CM.\"Sex\" ";
		sql += " LEFT JOIN \"CdCode\" CdCustType ON CdCustType.\"DefCode\" = 'CustTypeCode' ";
		sql += "                              AND CdCustType.\"Code\" = CM.\"CustTypeCode\" ";
		sql += " LEFT JOIN \"CustTelNo\" CTNHome ON CM.\"CustUKey\" = CTNHome.\"CustUKey\" ";
		sql += "                              AND CTNHome.\"TelTypeCode\" = '01' ";
		sql += " LEFT JOIN \"CustTelNo\" CTNWork ON CM.\"CustUKey\" = CTNWork.\"CustUKey\" ";
		sql += "                              AND CTNWork.\"TelTypeCode\" = '02' ";
		sql += " LEFT JOIN \"CustTelNo\" CTNCell ON CM.\"CustUKey\" = CTNCell.\"CustUKey\" ";
		sql += "                              AND CTNCell.\"TelTypeCode\" = '03' ";
		sql += " LEFT JOIN \"CustTelNo\" CTNFax  ON CM.\"CustUKey\" = CTNFax.\"CustUKey\" ";
		sql += "                              AND CTNFax.\"TelTypeCode\" = '04' ";
		sql += " LEFT JOIN (SELECT GUA.\"ApproveNo\"                         AS \"ApplNo\"  ";
		sql += "                 , GUACM.\"CustId\"                          AS \"GuaId\"   ";
		sql += "                 , GUACM.\"CustName\"                        AS \"GuaName\" ";
		sql += "                 , CDG.\"GuaRelItem\"                                     ";
		sql += "                 , GUA.\"GuaAmt\"                                         ";
		sql += "                 , \"Fn_GetCustAddr\"(GUACM.\"CustUKey\",'1')  AS \"GuaAddr\" ";
		sql += "                 , NVL(GUACM.\"CurrZip3\",'') || NVL(GUACM.\"CurrZip2\",'') ";
		sql += "                                                           AS \"GuaZip\"  ";
		sql += "                 , ROW_NUMBER() OVER (PARTITION BY GUA.\"ApproveNo\" ORDER BY GUA.\"GuaAmt\" DESC) ";
		sql += "                                                           AS \"Seq\" ";
		sql += "            FROM \"Guarantor\" GUA ";
		sql += "            LEFT JOIN \"CustMain\" GUACM ON GUACM.\"CustUKey\" = GUA.\"GuaUKey\"  ";
		sql += "            LEFT JOIN \"CdGuarantor\" CDG ON CDG.\"GuaRelCode\" = GUA.\"GuaRelCode\" ";
		sql += "            WHERE GUA.\"GuaStatCode\" = '1' ";
		sql += "           ) GUA ON GUA.\"ApplNo\" = FCA.\"ApplNo\" ";
		sql += "                AND GUA.\"Seq\"    = 1  ";
		sql += " LEFT JOIN \"CdCode\" CDC1 ON CDC1.\"DefCode\" = 'CustTypeCode'  ";
		sql += "                        AND CDC1.\"Code\" = CM.\"CustTypeCode\" ";
		sql += " LEFT JOIN \"CdCode\" CDC2 ON CDC2.\"DefCode\" = 'AcctCode' ";
		sql += "                        AND CDC2.\"Code\" = FM.\"AcctCode\" ";
		sql += " LEFT JOIN \"CdCode\" CDC3 ON CDC3.\"DefCode\" = 'ExtraRepayCode' ";
		sql += "                        AND CDC3.\"Code\" = FM.\"ExtraRepayCode\" ";
		sql += " LEFT JOIN \"CdCode\" CDC4 ON CDC4.\"DefCode\" = 'UsageCode'  ";
		sql += "                        AND CDC4.\"Code\" = FM.\"UsageCode\" ";
		sql += " LEFT JOIN \"CdEmp\" CDE1 ON CDE1.\"EmployeeNo\" = FM.\"Introducer\" ";
		sql += " LEFT JOIN \"CdCode\" CDC5 ON CDC5.\"DefCode\" = 'FacmAmortizedCode' ";
		sql += "                        AND CDC5.\"Code\" = FM.\"AmortizedCode\" ";
		sql += " LEFT JOIN \"CdCode\" CDC6 ON CDC6.\"DefCode\" = 'FacmRepayCode'  ";
		sql += "                        AND CDC6.\"Code\" = FM.\"RepayCode\" ";
		sql += " LEFT JOIN \"CdCode\" CDC7 ON CDC7.\"DefCode\" = 'BankDeductCd' ";
		sql += "                        AND CDC7.\"Code\" = \"Fn_GetRepayAcct\"(FM.\"CustNo\",FM.\"FacmNo\",'0') ";
		sql += " LEFT JOIN \"FacProd\" PROD ON PROD.\"ProdNo\" = FM.\"ProdNo\" ";
		sql += " LEFT JOIN \"CdEmp\" CDE2 ON CDE2.\"EmployeeNo\" = FM.\"FireOfficer\" ";
		sql += " LEFT JOIN \"CdEmp\" CDE3 ON CDE3.\"EmployeeNo\" = FM.\"LoanOfficer\" ";
		sql += " LEFT JOIN \"CdEmp\" CDE4 ON CDE4.\"EmployeeNo\" = FM.\"Supervisor\" ";
		sql += " LEFT JOIN \"CdEmp\" CDE5 ON CDE5.\"EmployeeNo\" = FM.\"EstimateReview\" ";
		sql += " LEFT JOIN \"CdEmp\" CDE6 ON CDE6.\"EmployeeNo\" = FM.\"InvestigateOfficer\" ";
		sql += " LEFT JOIN \"CdEmp\" CDE7 ON CDE7.\"EmployeeNo\" = FM.\"CreditOfficer\" ";
		sql += " LEFT JOIN \"CdEmp\" CDE8 ON CDE8.\"EmployeeNo\" = FM.\"Coorgnizer\" ";
		sql += " LEFT JOIN ( SELECT \"CustNo\" ";
		sql += "                   ,SUM(\"LoanBal\") AS \"LoanBal\" ";
		sql += "             FROM \"LoanBorMain\"  ";
		sql += "             GROUP BY \"CustNo\" ";
		sql += "           ) LBM ON LBM.\"CustNo\" = CM.\"CustNo\" ";
		sql += "  LEFT JOIN \"CustMain\" GROUPCM ON GROUPCM.\"CustUKey\" = FCA.\"GroupUKey\" ";
		sql += "  LEFT JOIN \"CdEmp\" CDE9 ON CDE9.\"EmployeeNo\" = CM.\"EmpNo\" ";
		sql += " WHERE FCA.\"ApplNo\" = :applNo ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("applNo", applNo);

		return this.convertToMap(query.getResultList());
	}

}