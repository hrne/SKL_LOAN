package com.st1.itx.db.service.springjpa.cm;

import java.math.BigDecimal;
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
import com.st1.itx.util.date.DateUtil;

@Service
@Repository
public class L4721ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	DateUtil dateUtil;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(int itxkind, int iCustType, int sDate, int eDate, String prodNos,
			TitaVo titaVo) throws Exception {
//		// iExecCode 0: 一般、 9.定期機動檢核件
		int iTxKind = itxkind;
//		輸入畫面 戶別 CustType 1:個金;2:企金（含企金自然人）
//		客戶檔 0:個金1:企金2:企金自然人
		int iEntCode1 = 0;
		int iEntCode2 = 0;
		if (iCustType == 2) {
			iEntCode1 = 1;
			iEntCode2 = 2;
		}

		String sql = "　";

		sql += " select   ";
		sql += "    b.\"CustNo\"                                 as \"CustNo\"  "; // 戶號
		sql += "   ,b.\"FacmNo\"                                 as \"FacmNo\"  "; // 額度
		sql += "   ,b.\"BormNo\"                                 as \"BormNo\"  "; // 撥款
		sql += "   ,b.\"NextAdjRateDate\"                        as \"NextAdjRateDate\"  "; // 下次利率調整日期
		sql += "   ,b.\"PrevPayIntDate\"                         as \"PrevPayIntDate\"  "; // 上次繳息日,繳息迄日
		sql += "   ,b.\"RateAdjFreq\"                            as \"RateAdjFreq\" "; // 利率調整週期
		sql += "   ,b.\"LoanBal\"                                as \"LoanBal\"  "; // 餘額
		sql += "   ,b.\"DrawdownAmt\"                            as \"DrawdownAmt\"  "; // 撥款金額
		sql += "   ,NVL(f.\"ApproveRate\", 0)                    as \"FacApproveRate\"  "; // 額度核准利率
		sql += "   ,NVL(f.\"RateIncr\", 0)                       as \"FacRateIncr\"  "; // 額度加碼利率
		sql += "   ,NVL(f.\"IndividualIncr\" , 0)                as \"FacIndividualIncr\" "; // 額度個人加碼利率
		sql += "   ,NVL(f.\"FirstDrawdownDate\",0)               as \"FirstDrawdownDate\" "; // 首撥日
		sql += "   ,NVL(p.\"EmpFlag\", ' ')                      as \"EmpFlag\" "; // 員工利率記號
		sql += "   ,NVL(r.\"IncrFlag\", ' ')                     as \"IncrFlag\" "; // 借戶利率檔是否依合約記號
		sql += "   ,NVL(r.\"BaseRateCode\", ' ')                 as \"BaseRateCode\" "; // 借戶利率檔商品指標利率代碼
		sql += "   ,NVL(r.\"FitRate\", 0)                        as \"FitRate\" "; // 借戶利率檔適用利率(目前利率)
		sql += "   ,NVL(r.\"RateCode\", ' ')                     as \"RateCode\" "; // 借戶利率檔利率區分 1: 機動 2: 固動 3:定期機動
		sql += "   ,NVL(r.\"ProdNo\", ' ')                       as \"ProdNo\" "; // 借戶利率檔商品代碼
		sql += "   ,NVL(r.\"RateIncr\", 0)                       as \"RateIncr\" "; // 借戶利率檔加碼利率
		sql += "   ,NVL(r.\"IndividualIncr\", 0)                 as \"IndividualIncr\" "; // 借戶利率檔個人加碼利率
		sql += "   ,NVL(r.\"EffectDate\", 0)                     as \"EffectDate\" "; // 借戶利率檔生效日
		sql += "   ,NVL(c.\"EntCode\", ' ')                      as \"EntCode\" "; // 企金別 共用代碼檔 0:個金 1:企金 2:企金自然人
		sql += "   ,NVL(cm.\"CityCode\", ' ')                    as \"CityCode\""; // 擔保品地區別
		sql += "   ,NVL(cm.\"AreaCode\", ' ')                    as \"AreaCode\" "; // 擔保品鄉鎮別
//		sql += "   ,NVL(\"Fn_GetCdCityIntRateCeiling\"(NVL(cm.\"CityCode\", ' '), r2.\"EffectDate\" ), 0) ";
//		sql += "                                                 as \"CityRateCeiling\" "; // 地區別利率上限
//		sql += "   ,NVL(\"Fn_GetCdCityIntRateFloor\"(NVL(cm.\"CityCode\", ' '), r2.\"EffectDate\" ), 0) ";
//		sql += "                                                 as \"CityRateFloor\" "; // 地區別利率下限
//		sql += "   ,NVL(\"Fn_GetCdCityIntRateIncr\"(NVL(cm.\"CityCode\", ' '), r2.\"EffectDate\",  :inputEffectDateE ), 0) ";
//		sql += "                                                 as \"CityRateIncr\" "; // 地區別利率加減碼
		sql += "   ,b.\"NextPayIntDate\"                         as \"NextPayIntDate\" "; // 下次繳息日,下次應繳日
		sql += "   ,b.\"DrawdownDate\"                           as \"DrawdownDate\" "; // 撥款日期
		sql += "   ,b.\"MaturityDate\"                           as \"MaturityDate\" "; // 到期日期
		sql += "   ,b.\"FirstAdjRateDate\"                       as \"FirstAdjRateDate\"  "; // 首次利率調整日期
		sql += "   ,NVL(r2.\"EffectDate\", 0)                    as \"PresEffDate\" "; // 目前生效日
		sql += "   ,NVL(r2.\"FitRate\", 0)                       as \"PresentRate\" "; // 目前利率
		sql += "   ,NVL(tx.\"EntryDate\", 0)                     as \"EntryDate\" "; // 入帳日期
		sql += "   ,NVL(tot.\"TotBalance\",0)                    as \"TotBalance\"  "; // 全戶餘額
		sql += "   ,b.\"ActFg\"                                  as \"ActFg\"  "; // 交易進行記號
		sql += "   ,p.\"GovOfferFlag\"                           as \"GovOfferFlag\"  "; // 政府優惠房貸記號
		sql += " from \"LoanBorMain\" b                                 ";
		sql += " left join \"FacMain\"  f on  f.\"CustNo\" = b.\"CustNo\"      ";
		sql += "                         and  f.\"FacmNo\" = b.\"FacmNo\"      ";
		// 要調整的利率資料
		sql += " left join(                                             ";
		sql += "           select                                       ";
		sql += "            rr.\"CustNo\"                               ";
		sql += "           ,rr.\"FacmNo\"                               ";
		sql += "           ,rr.\"BormNo\"                               ";
		sql += "           ,rr.\"BaseRateCode\"                         ";
		sql += "           ,rr.\"IncrFlag\"                             ";
		sql += "           ,rr.\"FitRate\"                              ";
		sql += "           ,rr.\"RateCode\"                             ";
		sql += "           ,rr.\"ProdNo\"                               ";
		sql += "           ,rr.\"RateIncr\"                             ";
		sql += "           ,rr.\"IndividualIncr\"                       ";
		sql += "           ,rr.\"EffectDate\"                           ";
		// 指標利率調整
		switch (iTxKind) {
		case 1: // 定期機動調整
			sql += "       ,row_number() over (partition by rr.\"CustNo\", rr.\"FacmNo\", rr.\"BormNo\" order by rr.\"EffectDate\" Desc) as seq ";
			sql += "       from \"LoanRateChange\" rr                          ";
			sql += "       left join \"LoanBorMain\" LBM on LBM.\"CustNo\" = rr.\"CustNo\" ";
			sql += "                                    AND LBM.\"FacmNo\" = rr.\"FacmNo\" ";
			sql += "                                    AND LBM.\"BormNo\" = rr.\"BormNo\" ";
			sql += "       where rr.\"EffectDate\" >= " + sDate;
			sql += "         and rr.\"EffectDate\" <= " + eDate;
			break;
		case 2: // 指數型利率調整
			sql += "       ,row_number() over (partition by rr.\"CustNo\", rr.\"FacmNo\", rr.\"BormNo\" order by rr.\"EffectDate\" Desc) as seq ";
			sql += "       from \"LoanRateChange\" rr                          ";
			sql += "       where rr.\"EffectDate\" >= " + sDate;
			sql += "         and rr.\"EffectDate\" <= " + eDate;
			break;
		case 3: // 機動利率調整
		case 4: // 員工利率調整
		case 5: // 按商品別調整
			sql += "       ,row_number() over (partition by rr.\"CustNo\", rr.\"FacmNo\", rr.\"BormNo\" order by rr.\"EffectDate\" Desc) as seq ";
			sql += "       from \"LoanRateChange\" rr                          ";
			sql += "       where rr.\"EffectDate\" >= " + sDate;
			sql += "         and rr.\"EffectDate\" <= " + eDate;
			break;
		}
		sql += "        ) r             on  r.\"CustNo\" = b.\"CustNo\"        ";
		sql += "                       and  r.\"FacmNo\" = b.\"FacmNo\"        ";
		sql += "                       and  r.\"BormNo\" = b.\"BormNo\"        ";
		sql += "                       and  r.seq = 1                          ";
		sql += " left join(                                             ";
		sql += "           select                                       ";
		sql += "            rb.\"CustNo\"                              ";
		sql += "           ,rb.\"FacmNo\"                              ";
		sql += "           ,rb.\"BormNo\"                              ";
		sql += "           ,rb.\"FitRate\"                              ";
		sql += "           ,rb.\"EffectDate\"                           ";
		sql += "           ,row_number() over (partition by rb.\"CustNo\", rb.\"FacmNo\", rb.\"BormNo\" order by rb.\"EffectDate\" Desc) as seq ";
		sql += "           from \"LoanRateChange\" rb                          ";
		sql += "       	   where rb.\"EffectDate\" >= " + sDate;
		sql += "             and rb.\"EffectDate\" <= " + eDate;
		sql += "        ) r2            on  r2.\"CustNo\" = b.\"CustNo\"        ";
		sql += "                       and  r2.\"FacmNo\" = b.\"FacmNo\"        ";
		sql += "                       and  r2.\"BormNo\" = b.\"BormNo\"        ";
		sql += "                       and  r2.seq = 1                          ";
		sql += " left join(                                             ";
		sql += "           select                                       ";
		sql += "             \"CustNo\"                              ";
		sql += "            ,SUM(\"LoanBal\")   AS  \"TotBalance\"  ";
		sql += "           from \"LoanBorMain\"                      ";
		sql += "           group by  \"CustNo\"                      ";
		sql += "        ) tot        on  tot.\"CustNo\" = b.\"CustNo\"        ";
		sql += " left join \"FacProd\"  p on  p.\"ProdNo\" = r.\"ProdNo\"      ";
		sql += " left join \"CustMain\" c on  c.\"CustNo\" = b.\"CustNo\"      ";
		sql += " left join \"ClFac\"   cf on cf.\"CustNo\" = b.\"CustNo\"      ";
		sql += "                       and cf.\"FacmNo\" = b.\"FacmNo\"        ";
		sql += "                       and cf.\"MainFlag\" = 'Y'               ";
		sql += " left join \"ClMain\"  cm on cm.\"ClCode1\" = cf.\"ClCode1\"   ";
		sql += "                       and cm.\"ClCode2\" = cf.\"ClCode2\"     ";
		sql += "                       and cm.\"ClNo\" = cf.\"ClNo\"           ";
		sql += " left join \"CdCity\" cc  on cc.\"CityCode\" = cm.\"CityCode\" ";
		sql += " left join \"LoanBorTx\" tx  on tx.\"CustNo\" = b.\"CustNo\" ";
		sql += "                       and  tx.\"FacmNo\" = b.\"FacmNo\"        ";
		sql += "                       and  tx.\"BormNo\" = b.\"BormNo\"        ";
		sql += "                       and  tx.\"IntEndDate\" = b.\"PrevPayIntDate\"  ";
		sql += "                       and  tx.\"TitaTxCd\" = 'L3200'           ";
		sql += "                       and  tx.\"TitaHCode\" = '0'              ";
		sql += "                       and  tx.\"ExtraRepay\" = 0               "; // 不含提前償還本金
		if (iTxKind == 4) {
			sql += " left join \"CdEmp\" e on  e.\"EmployeeNo\" = c.\"EmpNo\"  ";
		}
		if (iTxKind == 5) {

			sql += " left join \"FacCaseAppl\" a on  a.\"ApplNo\" = f.\"ApplNo\"  ";
		}
		sql += " where b.\"Status\" = 0                                        ";
		sql += "   and b.\"MaturityDate\" >= " + sDate;
		sql += "   and b.\"MaturityDate\" <= " + eDate;
		sql += "   and c.\"EntCode\" >= " + iEntCode1;
		sql += "   and c.\"EntCode\" <= " + iEntCode2;
		// 1.定期機動調整
		if (iTxKind == 1) {
			sql += "       and b.\"RateCode\" = '3' ";
			sql += "       and r.\"RateCode\" = '3'  ";
			sql += "       and b.\"NextAdjRateDate\" >= " + sDate;
			sql += "       and b.\"NextAdjRateDate\" <= " + eDate;
		}
		// 2.指數型利率調整
		if (iTxKind == 2) {
			sql += "       and b.\"RateCode\" = '1'  ";
			sql += "       and r.\"RateCode\" = '1'  ";
		}
		// 3.機動利率調整
		if (iTxKind == 3) {
			sql += "           and b.\"RateCode\" = '1' ";
			sql += "           and r.\"RateCode\" = '1' ";
			sql += "           and r.\"BaseRateCode\" = 99 ";
			sql += "           and r.\"EffectDate\" >= " + sDate;
			sql += "	 	   and r.\"EffectDate\" <= " + eDate;
			sql += "           and p.\"EmpFlag\" <> 'Y' ";
		}
		// 4.員工利率調整
		if (iTxKind == 4) {
			sql += "           and b.\"RateCode\" = '1' ";
			sql += "           and p.\"EmpFlag\" = 'Y' ";
		}
		// 5.按商品別調整
		// 商品代碼
		if (iTxKind == 5) {
			sql += "   and p.\"ProdNo\" in ( " + prodNos + " ) ";
			sql += "   and r.\"EffectDate\"  <= " + sDate;
			sql += "   and r.\"EffectDate\"  >= " + eDate;
		}

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		return this.convertToMap(query);
	}

	public List<Map<String, String>> TempQuery(int custNo, int isday, int ieday, TitaVo titaVo) throws Exception {

		this.info("BankStatementServiceImpl Temp");
//		dateUtil.init();
//		int ieday = titaVo.getEntDyI() + 19110000;
//		dateUtil.setDate_1(ieday);
//		dateUtil.setMons(-6);
//		int isday = Integer.parseInt(String.valueOf(dateUtil.getCalenderDay()).substring(0, 6) + "01");
		this.info("custNo ... " + custNo);
		this.info("isday ... " + isday);
		this.info("ieday ... " + ieday);

//		尋找該戶號是否有暫收款(回傳入帳日，金額，還款類別)

		String sql = "SELECT    ";
		sql += "    lb.\"EntryDate\"  AS \"EntryDate\",";
		sql += "    cd.\"Item\"       AS \"RepayCodeX\",";
		sql += "    lb.\"TxAmt\"      AS \"TxAmt\"";
		sql += "    FROM";
		sql += "    \"LoanBorTx\"   lb";
		sql += "    LEFT JOIN \"CdCode\"      cd ON cd.\"DefCode\" = 'RepayCode'";
		sql += "                             AND cd.\"Code\" = lb.\"RepayCode\"";
		sql += "    WHERE";
		sql += "    lb.\"CustNo\" = " + custNo;
		sql += "    AND lb.\"FacmNo\" = 0";
		sql += "    AND lb.\"BormNo\" = 0";
		sql += "    AND lb.\"EntryDate\" >= " + isday;
		sql += "    AND lb.\"EntryDate\" <= " + ieday;

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> doQuery(int custNo, int isday, int ieday, TitaVo titaVo) throws Exception {

		this.info("BankStatementServiceImpl doQuery");
//		dateUtil.init();
//		int ieday = titaVo.getEntDyI() + 19110000;
//		dateUtil.setDate_1(ieday);
//		dateUtil.setMons(-6);
//		int isday = Integer.parseInt(String.valueOf(dateUtil.getCalenderDay()).substring(0, 6) + "01");
		this.info("custNo ... " + custNo);
		this.info("isday ... " + isday);
		this.info("ieday ... " + ieday);

//		因抓取不到費用類，第一層group by 到額度(同額度同調整日、同源利率

		String sql = " ";
		sql += "  SELECT DISTINCT ";
		sql += "        B.\"CustNo\"                                          "; // 戶號
		sql += "       ,B.\"FacmNo\"                                          "; // 額度
		sql += "       ,C.\"CustName\"                                        "; // 戶名
		sql += "       ,B.\"SpecificDd\"                                      "; // 應繳日
		sql += "       ,CD.\"Item\"                      AS \"RepayCodeX\"    "; // 繳款方式
		sql += "       ,B.\"LoanBal\"                                         "; // 貸放餘額
		sql += "       ,B.\"NextPayIntDate\"                                  "; // 下繳日
		sql += "       ,NVL(CB.\"BdLocation\", ' ')      AS \"Location\"      "; // 押品地址
		sql += "       ,NVL(CN.\"PaperNotice\", 'Y')     AS \"PaperNotice\"   "; // 書面通知與否 Y:寄送 N:不寄送
		sql += "       ,CASE WHEN BR.\"TxEffectDate\" = 0 THEN 0 WHEN BR.\"TxEffectDate\" IS NOT NULL THEN BR.\"TxEffectDate\" - 19110000 ELSE 0 END AS \"TxEffectDate\""; // 利率生效日
//		sql += "       ,CASE WHEN BR.\"TxEffectDate\" IS NOT NULL THEN BR.\"TxEffectDate\" - 19110000 ELSE 0 END AS \"TxEffectDate\""; // 利率生效日
		sql += "       ,BR.\"PresentRate\"                ";
		sql += "       ,BR.\"AdjustedRate\"               ";
		sql += "       ,CASE WHEN B.\"FacmNo\" = BR.\"FacmNo\" THEN 'Y' ELSE 'N' END AS \"Flag\"  "; // 放款利率變動檔生效日，利率未變動為零
																										// Y,N
		sql += "  FROM (SELECT \"CustNo\"                                             ";
		sql += "                   ,\"FacmNo\"                                             ";
		sql += "                   ,SUM(\"LoanBal\")             AS \"LoanBal\"            ";
		sql += "                   ,MIN(\"NextPayIntDate\")      AS \"NextPayIntDate\"     ";
		sql += "                   ,MAX(\"SpecificDd\")          AS \"SpecificDd\"         ";
		sql += "             FROM \"LoanBorMain\"                                          ";
		sql += "             WHERE \"CustNo\" = " + custNo;
		sql += "               AND \"Status\" = 0                                           ";
		sql += "             GROUP By \"CustNo\", \"FacmNo\"                                ";
		sql += "             ) B                                                            ";
		sql += " left join \"CustNotice\" CN                             "; // 客戶通知設定檔
		sql += "        on CN.\"FormNo\" = 'L4721'                       ";
		sql += "       and CN.\"CustNo\" = B.\"CustNo\"                  ";
		sql += "       and CN.\"FacmNo\" = B.\"FacmNo\"                  ";
		sql += " LEFT JOIN \"CustMain\" C ON C.\"CustNo\"   = B.\"CustNo\"                            ";
		sql += " LEFT JOIN \"FacMain\" FM ON FM.\"CustNo\"  = B.\"CustNo\"                            ";
		sql += "                         AND FM.\"FacmNo\"  = B.\"FacmNo\"                            ";
		sql += " LEFT JOIN \"ClFac\" F ON F.\"CustNo\"   =  B.\"CustNo\"                              ";
		sql += "                      AND F.\"FacmNo\"   =  B.\"FacmNo\"                              ";
		sql += "                      AND F.\"MainFlag\" = 'Y'                                        ";
		sql += " LEFT JOIN \"ClBuilding\" CB ON CB.\"ClCode1\" =  F.\"ClCode1\"                       ";
		sql += "                            AND CB.\"ClCode2\" =  F.\"ClCode2\"                       ";
		sql += "                            AND CB.\"ClNo\"    =  F.\"ClNo\"                          ";
		sql += " LEFT JOIN \"CdCode\" CD ON CD.\"DefCode\" = 'RepayCode'                              ";
		sql += "                        AND CD.\"Code\"    =  FM.\"RepayCode\"                        ";
		sql += " LEFT JOIN \"BatxRateChange\" BR ON BR.\"CustNo\" = B.\"CustNo\"                      ";
		sql += "                                AND BR.\"FacmNo\" = B.\"FacmNo\"                      ";
		sql += " ORDER BY B.\"CustNo\",B.\"FacmNo\"                                                   ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> doDetail(int custNo, int isday, int ieday, int adjDate, TitaVo titaVo)
			throws Exception {
		dateUtil.init();

		this.info("BankStatementServiceImpl doDetail");
//		int adjDate = Integer.parseInt(titaVo.getParam("AdjDate")) + 19110000;
//		int ieday = titaVo.getEntDyI() + 19110000;
//		dateUtil.setDate_1(ieday);
//		dateUtil.setMons(-6);
//		int isday = Integer.parseInt(String.valueOf(dateUtil.getCalenderDay()).substring(0, 6) + "01");
		this.info("custNo ... " + custNo);
		this.info("isday ... " + isday);
		this.info("ieday ... " + ieday);
		this.info("adjDate ... " + adjDate);
//		因抓取不到費用類，第一層group by 到額度(同額度同調整日、同源利率

		String sql = " ";
		sql += " SELECT DISTINCT X.\"CustNo\"                                         AS \"CustNo\"   ";
		sql += "       ,X.\"FacmNo\"                                         AS \"FacmNo\"   ";
		sql += "       ,C.\"CustName\"                                        "; // 戶名
		sql += "       ,LB.\"SpecificDd\"                                      "; // 應繳日
		sql += "       ,LB.\"LoanBal\"                                         "; // 貸放餘額
		sql += "       ,LB.\"DueAmt\"                                         "; // 貸放餘額
		sql += "       ,X.\"EntryDate\"                                       ";
		sql += "       ,X.\"IntStartDate\"                                    "; // F1:計息期間
		sql += "       ,X.\"IntEndDate\"                                      "; // F2:計息期間
		sql += "       ,CD.\"Item\"                                          AS \"RepayCodeX\" "; // F13:繳款方式
		sql += "       ,X.\"TxAmt\"                                           "; // F4:繳款金額
		sql += "       ,X.\"Principal\"                                       "; // F5:攤還本金
		sql += "       ,X.\"Interest\"                                        "; // F6:繳息金額
		sql += "       ,X.\"BreachAmt\" + \"DelayInt\"                       AS \"BreachAmt\"  "; // F7:違約金+延滯息
		sql += "       ,X.\"FEE1\" + X.\"FEE2\" + X.\"FEE3\" + X.\"FEE4\"    AS \"OtherFee\"   "; // F8:火險費或其他費用
		sql += "       ,CASE WHEN BR.\"TxEffectDate\" = 0 THEN 0 WHEN BR.\"TxEffectDate\" IS NOT NULL THEN BR.\"TxEffectDate\" - 19110000 ELSE 0 END AS \"TxEffectDate\"";
		sql += "       ,NVL(BR.\"PresentRate\",0)                            AS \"PresentRate\"";
		sql += "       ,NVL(BR.\"AdjustedRate\",0)                           AS \"AdjustedRate\"";
		sql += "       ,X.\"AcDate\"                      ";
		sql += "       ,X.\"RepayCode\"                   ";
		sql += "       ,NVL(CB.\"BdLocation\", ' ')      AS \"Location\"      "; // 押品地址
		sql += " FROM ( SELECT MAX(T.\"CustNo\")                                            AS \"CustNo\"            ";
		sql += "             ,T.\"FacmNo\"                                                  AS \"FacmNo\"            ";
		sql += "             ,MAX(T.\"EntryDate\")                                          AS \"EntryDate\"         ";
		sql += "             ,MIN(T.\"IntStartDate\")                                       AS \"IntStartDate\"      ";
		sql += "             ,MAX(T.\"IntEndDate\")                                         AS \"IntEndDate\"        ";
		sql += "             ,MAX(T.\"Rate\")                                               AS \"Rate\"              ";
		sql += "             ,SUM(T.\"Interest\")                                           AS \"Interest\"          ";
		sql += "             ,SUM(T.\"DelayInt\")                                           AS \"DelayInt\"          ";
		sql += "             ,SUM(T.\"BreachAmt\" + T.\"CloseBreachAmt\")                   AS \"BreachAmt\"         ";
		sql += "             ,SUM(T.\"Principal\")                                          AS \"Principal\"         ";
		sql += "             ,MAX(T.\"RepayCode\")                                          AS \"RepayCode\"         ";
		sql += "             ,SUM(T.\"TxAmt\")                                              AS \"TxAmt\"             ";
		sql += "             ,SUM(NVL(JSON_VALUE(T.\"OtherFields\",  '$.AcctFee'),0))       AS FEE1                  ";
		sql += "             ,SUM(NVL(JSON_VALUE(T.\"OtherFields\",  '$.ModifyFee'),0))     AS FEE2                  ";
		sql += "             ,SUM(NVL(JSON_VALUE(T.\"OtherFields\",  '$.FireFee'),0))       AS FEE3                  ";
		sql += "             ,SUM(NVL(JSON_VALUE(T.\"OtherFields\",  '$.LawFee'),0))        AS FEE4                  ";
		sql += "             ,T.\"AcDate\"                      ";
		sql += "             , CASE";
		sql += "                WHEN t.\"IntStartDate\" = 0";
		sql += "                  AND t.\"IntEndDate\" = 0 THEN";
		sql += "                    'Y'";
		sql += "                ELSE";
		sql += "                    'N' END AS \"Flag\"";
		sql += "        FROM \"LoanBorTx\" T                                             ";
		sql += "        WHERE T.\"CustNo\" = " + custNo;
		sql += "         AND  T.\"FacmNo\" != 0 ";
		sql += "         AND  T.\"TitaHCode\" = 0                                          ";
		sql += "         AND (T.\"Principal\" + T.\"ExtraRepay\" + T.\"Interest\" + T.\"BreachAmt\" + T.\"CloseBreachAmt\"";
		sql += "              +  NVL(JSON_VALUE(T.\"OtherFields\",  '$.AcctFee'),0)                   ";
		sql += "              +  NVL(JSON_VALUE(T.\"OtherFields\",  '$.ModifyFee'),0)                 ";
		sql += "              +	NVL(JSON_VALUE(T.\"OtherFields\",  '$.FireFee'),0)                    ";
		sql += "              +	NVL(JSON_VALUE(T.\"OtherFields\",  '$.LawFee'),0) > 0                 ";
		sql += "                or T.\"TitaTxCd\" = 'L3210' )                                         ";
		sql += "         AND  T.\"EntryDate\" >= " + isday; // tbsdy六個月前的月初日
		sql += "         AND  T.\"EntryDate\" <= " + ieday; // tbsdy
		sql += "       GROUP BY  t.\"FacmNo\", t.\"EntryDate\", t.\"AcDate\", CASE WHEN t.\"IntStartDate\" = 0 AND t.\"IntEndDate\" = 0 THEN 'Y' ELSE 'N' END                      ";
		sql += "      ) X                                                                             ";
		sql += " LEFT JOIN \"CdCode\" CD ON CD.\"DefCode\" = 'RepayCode'                              ";
		sql += "                        AND CD.\"Code\"    =  X.\"RepayCode\"                         ";
		sql += " LEFT JOIN \"BatxRateChange\" BR ON BR.\"CustNo\" = " + custNo;
		sql += "                                AND BR.\"FacmNo\" =  X.\"FacmNo\"                     ";
		sql += "                                AND BR.\"AdjDate\" = " + adjDate;
		sql += " LEFT JOIN \"CustMain\" C ON C.\"CustNo\"   = X.\"CustNo\"                            ";
		sql += " LEFT JOIN ( SELECT  \"CustNo\" AS \"CustNo\"";
		sql += "                    ,\"FacmNo\"";
		sql += "                    ,SUM(\"LoanBal\") AS \"LoanBal\"";
		sql += "                    ,SUM(\"DueAmt\") AS \"DueAmt\"";
		sql += "                    ,MIN(\"NextPayIntDate\") AS \"NextPayIntDate\"";
		sql += "                    ,MAX(\"SpecificDd\") AS \"SpecificDd\"";
		sql += "        FROM  \"LoanBorMain\"";
		sql += "        WHERE  \"CustNo\" = " + custNo;
		sql += "               AND \"Status\" != 3                                           ";
		sql += "        GROUP BY   \"CustNo\",\"FacmNo\") LB ON LB.\"CustNo\" = X.\"CustNo\"                         ";
		sql += "                                            AND LB.\"FacmNo\" =  X.\"FacmNo\"                        ";
		sql += " LEFT JOIN \"ClFac\" F ON F.\"CustNo\"   =  LB.\"CustNo\"                              ";
		sql += "                      AND F.\"FacmNo\"   =  LB.\"FacmNo\"                              ";
		sql += "                      AND F.\"MainFlag\" = 'Y'                                        ";
		sql += " LEFT JOIN \"ClBuilding\" CB ON CB.\"ClCode1\" =  F.\"ClCode1\"                       ";
		sql += "                            AND CB.\"ClCode2\" =  F.\"ClCode2\"                       ";
		sql += "                            AND CB.\"ClNo\"    =  F.\"ClNo\"                          ";
		sql += " WHERE LB.\"SpecificDd\" IS NOT NULL";
		sql += " ORDER BY X.\"FacmNo\",X.\"EntryDate\"                                                             ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		return this.convertToMap(query);
	}

}