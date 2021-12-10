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

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.repository.online.LoanBorMainRepository;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.util.parse.Parse;

@Service("l4320ServiceImpl")
@Repository
/* 整批利率調整 */
public class L4320ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Autowired
	public Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
		int iTxKind = Integer.parseInt(titaVo.getParam("TxKind"));
		int iEffectMonth = Integer.parseInt(titaVo.getParam("EffectMonth"));
		int iEffectDate = Integer.parseInt(titaVo.getParam("EffectDate"));
		int iEffectDateS = 0;
		int iEffectDateE = 0;
		if (iEffectMonth > 0) {
			iEffectDateS = iEffectMonth * 100 + 19110001;
			iEffectDateE = iEffectMonth * 100 + 19110031;
		} else {
			iEffectDate = iEffectDate + 19110000;
			iEffectDateS = iEffectDate;
			iEffectDateE = iEffectDate;
		}

		// 業務科目 ..... 999 + TO 999 + 4-員工利率調整 5.按商品別調整
		int iAcctCodeS = parse.stringToInteger(titaVo.getParam("AcctCodeS")); // 業務科目 from, 可為 0
		int iAcctCodeE = parse.stringToInteger(titaVo.getParam("AcctCodeE")); // 業務科目 to, , >= from

		// 員工年資＞＝ . 99 月 , 4-員工利率調整
		int iEmploeeMonth = parse.stringToInteger(titaVo.getParam("EmploeeMonth")); // 可為 0

		// 利率 ......... 99.9999 % TO 99.9999 % 4-員工利率調整 5.按商品別調整
		BigDecimal iFitRateS = parse.stringToBigDecimal(titaVo.getParam("FitRateS")); // 可為 0
		BigDecimal iFitRateE = parse.stringToBigDecimal(titaVo.getParam("FitRateE")); // >= from

		// 撥款日期 ..... 9999999999 TO 9999999999 4-員工利率調整 5.按商品別調整
		int iDrawDownDateS = parse.stringToInteger(titaVo.getParam("DrawDownDateS")); // 可為 0
		if (iDrawDownDateS > 0) {
			iDrawDownDateS = iDrawDownDateS + 19110000;
		}
		int iDrawDownDateE = parse.stringToInteger(titaVo.getParam("DrawDownDateE")); // >= from
		if (iDrawDownDateE > 0) {
			iDrawDownDateE = iDrawDownDateE + 19110000;
		}

		// 繳息迄日 ..... 9 9999999999 ( 1. ＞＝ , 2. ＜＝ ) 4-員工利率調整 5.按商品別調整
		int iPrevPayIntDateC = parse.stringToInteger(titaVo.getParam("PrevPayIntDateC")); // 限 1,2 預設 1
		int iPrevPayIntDate = parse.stringToInteger(titaVo.getParam("PrevPayIntDate")); // 1-可為 0, 2-需 > 0
		if (iPrevPayIntDate > 0) {
			iPrevPayIntDate = iPrevPayIntDate + 19110000;
		}

		// 地區別 ....... 99 - 99 ( 區間 ) 4-員工利率調整 5.按商品別調整
		String iCityCodeS = titaVo.getParam("CityCodeS"); // 可為 0
		String iCityCodeE = titaVo.getParam("CityCodeE"); // >= from

		// 並且＞＝ ..... 9999999999 9 ( 1. 起未調整過利率者 2. 起已調整過利率者 ) 4-員工利率調整 5.按商品別調整
		int iAdjustDate = parse.stringToInteger(titaVo.getParam("AdjustDateD")); // 限 1,2 預設 1
		if (iAdjustDate > 0) {
			iAdjustDate = iAdjustDate + 19110000;
		}
		int iAdjustDateC = parse.stringToInteger(titaVo.getParam("AdjustDateC")); // 可為 0

		// 團體戶 ....... 9999999999 + => 團體戶統編 , 5.按商品別調整
		String iGroupUKey = titaVo.getParam("GroupUKey"); // 可為空白

		// 超過 2 年調為 99.9999 % ??????

		int iBaseRateCode = Integer.parseInt(titaVo.getParam("BaseRateCode"));

//		輸入畫面 戶別 CustType 1:個金;2:企金（含企金自然人）
//		客戶檔 0:個金1:企金2:企金自然人
		int iEntCode1 = 0;
		int iEntCode2 = 0;
		if (Integer.parseInt(titaVo.getParam("EntCode")) == 2) {
			iEntCode1 = 1;
			iEntCode2 = 2;
		}

		String sql = "　";

		sql += " select   ";
		sql += "    b.\"CustNo\"                                 as F0  "; // 戶號
		sql += "   ,b.\"FacmNo\"                                 as F1  "; // 額度
		sql += "   ,b.\"BormNo\"                                 as F2  "; // 撥款
		sql += "   ,b.\"NextAdjRateDate\"                        as F3  "; // 下次利率調整日期
		sql += "   ,b.\"PrevPayIntDate\"                         as F4  "; // 上次繳息日,繳息迄日
		sql += "   ,b.\"RateAdjFreq\"                            as F5  "; // 利率調整週期
		sql += "   ,b.\"LoanBal\"                                as F6  "; // 餘額
		sql += "   ,b.\"DrawdownAmt\"                            as F7  "; // 撥款金額
		sql += "   ,NVL(f.\"ApproveRate\", 0)                    as F8  "; // 額度核准利率
		sql += "   ,NVL(f.\"RateIncr\", 0)                       as F9  "; // 額度加碼利率
		sql += "   ,NVL(f.\"IndividualIncr\" , 0)                as F10 "; // 額度個人加碼利率
		sql += "   ,NVL(p.\"EmpFlag\", ' ')                      as F11 "; // 員工利率記號
		sql += "   ,NVL(r.\"IncrFlag\", ' ')                     as F12 "; // 借戶利率檔是否依合約記號
		sql += "   ,NVL(r.\"BaseRateCode\", ' ')                 as F13 "; // 借戶利率檔商品指標利率代碼
		sql += "   ,NVL(r.\"FitRate\", 0)                        as F14 "; // 借戶利率檔適用利率
		sql += "   ,NVL(r.\"RateCode\", ' ')                     as F15 "; // 借戶利率檔利率區分 共用代碼檔 1: 機動 2: 固動 3: 定期機動
		sql += "   ,NVL(r.\"ProdNo\", ' ')                       as F16 "; // 借戶利率檔商品代碼
		sql += "   ,NVL(r.\"RateIncr\", 0)                       as F17 "; // 借戶利率檔加碼利率
		sql += "   ,NVL(r.\"IndividualIncr\", 0)                 as F18 "; // 借戶利率檔個人加碼利率
		sql += "   ,NVL(r.\"EffectDate\", 0)                     as F19 "; // 借戶利率檔生效日
		sql += "   ,NVL(c.\"EntCode\", ' ')                      as F20 "; // 企金別 共用代碼檔 0:個金 1:企金 2:企金自然人
		sql += "   ,NVL(cm.\"CityCode\", ' ')                    as F21 "; // 擔保品地區別
		sql += "   ,NVL(cm.\"AreaCode\", ' ')                    as F22 "; // 擔保品鄉鎮別
		sql += "   ,NVL(cc.\"IntRateCeiling\", 0)                as F23 "; // 地區別利率上限
		sql += "   ,NVL(cc.\"IntRateFloor\", 0)                  as F24 "; // 地區別利率下限
		sql += "   ,NVL(cc.\"IntRateIncr\", 0)                   as F25 "; // 地區別利率加減碼
		sql += "   ,b.\"NextPayIntDate\"                         as F26 "; // 下次繳息日,下次應繳日
		sql += "   ,b.\"DrawdownDate\"                           as F27 "; // 撥款日期
		sql += "   ,NVL(r2.\"FitRate\", 0)                       as F28 "; // 機動非指數目前利率
		sql += "   ,NVL(r2.\"EffectDate\", 0)                    as F29 "; // 機動非指數目前生效日
		sql += " from \"LoanBorMain\" b                                 ";
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
		sql += "           ,row_number() over (partition by rr.\"CustNo\", rr.\"FacmNo\", rr.\"BormNo\" order by rr.\"EffectDate\" Desc) as seq ";
		sql += "           from \"LoanRateChange\" rr                          ";
		sql += "           where rr.\"EffectDate\" <= " + iEffectDateE;
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
		sql += "           where rb.\"RateCode\" = '1' and rb.\"BaseRateCode\" = 99 ";
		sql += "            and  rb.\"EffectDate\" < " + iEffectDateS;
		sql += "        ) r2            on  r2.\"CustNo\" = b.\"CustNo\"        ";
		sql += "                       and  r2.\"FacmNo\" = b.\"FacmNo\"        ";
		sql += "                       and  r2.\"BormNo\" = b.\"BormNo\"        ";
		sql += "                       and  r2.seq = 1                          ";
		sql += " left join \"FacProd\"  p on  p.\"ProdNo\" = r.\"ProdNo\"      ";
		sql += " left join \"CustMain\" c on  c.\"CustNo\" = b.\"CustNo\"      ";
		sql += " left join \"FacMain\"  f on  f.\"CustNo\" = b.\"CustNo\"      ";
		sql += "                       and  f.\"FacmNo\" = b.\"FacmNo\"        ";
		sql += " left join \"ClFac\"   cf on cf.\"CustNo\" = b.\"CustNo\"      ";
		sql += "                       and cf.\"FacmNo\" = b.\"FacmNo\"        ";
		sql += "                       and cf.\"MainFlag\" = 'Y'               ";
		sql += " left join \"ClMain\"  cm on cm.\"ClCode1\" = cf.\"ClCode1\"   ";
		sql += "                       and cm.\"ClCode2\" = cf.\"ClCode2\"     ";
		sql += "                       and cm.\"ClNo\" = cf.\"ClNo\"           ";
		sql += " left join \"CdCity\" cc  on cc.\"CityCode\" = cm.\"CityCode\" ";
		if (iTxKind == 4) {
			sql += " left join \"CdEmp\" e on  e.\"EmployeeNo\" = c.\"EmpNo\"  ";
		}
		if (iTxKind == 5 && !iGroupUKey.isEmpty()) {
			sql += " left join \"FacCaseAppl\" a on  a.\"ApplNo\" = f.\"ApplNo\"  ";
		}
		sql += " where b.\"Status\" = 0                                        ";
		sql += "   and b.\"MaturityDate\" > b.\"NextAdjRateDate\"              ";
		sql += "   and b.\"MaturityDate\" > " + iEffectDateE;
		sql += "   and c.\"EntCode\" >= " + iEntCode1;
		sql += "   and c.\"EntCode\" <= " + iEntCode2;
//  1.定期機動調整 ==>  1.撥款主檔的利率區分=3.定期機動，下次利率調整日為調整月份
//	                    2.借戶利率檔的的利率區分=3.定期機動，指標利率種類=該指標利率種類抓，生效日期 <= 調整月份 
		if (iTxKind == 1) {
			sql += "       and b.\"RateCode\" = '3' " + "    and  b.\"NextAdjRateDate\" >= " + iEffectDateS;
			sql += "       and b.\"NextAdjRateDate\" <= " + iEffectDateE;
			sql += "       and r.\"BaseRateCode\" = " + iBaseRateCode;
			sql += "       and r.\"RateCode\" = '3'                  ";
		}
// 2.指數型利率調整 ==> 1.撥款主檔的利率區分=1.機動
//                      2.借戶利率檔的利率區分=1.機動,指標利率種類=該指標利率種類,生效日期 <= 調整日期 
		if (iTxKind == 2) {
			sql += "       and b.\"RateCode\" = '1'                  ";
			sql += "       and r.\"BaseRateCode\" = " + iBaseRateCode;
			sql += "       and r.\"RateCode\" = '1'  ";
		}
// 3.機動利率調整 ==> 1.撥款主檔的利率區分=1.機動
//                    2.借戶利率檔的利率區分=1.機動,指標利率種類=99，生效日期 = 調整月份，商品<>員工利率
		if (iTxKind == 3) {
			sql += "           and b.\"RateCode\" = '1' ";
			sql += "           and r.\"RateCode\" = '1' ";
			sql += "           and r.\"BaseRateCode\" = 99 ";
			sql += "           and r.\"EffectDate\" >= " + iEffectDateS;
			sql += "	 	   and r.\"EffectDate\" <= " + iEffectDateE;
			sql += "           and p.\"EmpFlag\" <> 'Y' ";
		}
// 4.員工利率調整 ==>1.撥款主檔的利率區分=1.機動
//				     2.借戶利率檔的利率區分=1.機動,生效日期 <= 調整日期，商品=員工利率
		if (iTxKind == 4) {
			sql += "           and b.\"RateCode\" = '1' ";
			sql += "           and p.\"EmpFlag\" = 'Y' ";
		}
// 5.按商品別調整 [iAdjDateS=iAdjDateE=利率生效日]    

		// 商品代碼
		String prodNoString = getProdNoString(titaVo);
		if (!"".equals(prodNoString)) {
			sql += "   and p.\"ProdNo\" in ( " + prodNoString + " ) ";
		}

		// 業務科目 ..... 999 + TO 999 + 4-員工利率調整 5.按商品別調整
		if (iAcctCodeE > 0) {
			sql += "   and f.\"AcctCode\" between '" + iAcctCodeS + "' and '" + iAcctCodeE + "'";
		}

		// 員工年資＞＝ . 99 月 , 4-員工利率調整
		if (iEmploeeMonth > 0 && iTxKind == 4) {
			sql += "   and (NVL(e.\"SeniorityYY\",0) * 12 + NVL(e.\"SeniorityMM\",0)) > " + iEmploeeMonth;
		}

		// 利率 ......... 99.9999 % TO 99.9999 % 4-員工利率調整 5.按商品別調整
		if (iFitRateE.compareTo(BigDecimal.ZERO) > 0) {
			sql += "   and r.\"FitRate\" between " + iFitRateS + " and " + iFitRateE;
		}

		// 撥款日期 ..... 9999999999 TO 9999999999 4-員工利率調整 5.按商品別調整
		if (iDrawDownDateE > 0) {
			sql += "   and f.\"FirstDrawdownDate\" between " + iDrawDownDateS + " and " + iDrawDownDateE;
		}

		// 繳息迄日 ..... 9 9999999999 ( 1. ＞＝ , 2. ＜＝ ) 4-員工利率調整 5.按商品別調整
		if (iPrevPayIntDateC == 1 && iPrevPayIntDate > 0) {
			sql += "   and b.\"PrevPayIntDate\" >= " + iPrevPayIntDate;
		}
		if (iPrevPayIntDateC == 2 && iPrevPayIntDate > 0) {
			sql += "   and b.\"PrevPayIntDate\" <= " + iPrevPayIntDate;
		}

		// 地區別 ....... 99 - 99 ( 區間 ) 4-員工利率調整 5.按商品別調整 int iCityCodeS =
		if (parse.stringToInteger(iCityCodeE) > 0) {
			sql += "   and NVL(cm.\"CityCode\", -1)  between " + iCityCodeS + " and " + iCityCodeE;
		}

		// 並且＞＝ ..... 9999999999 9 ( 1. 起未調整過利率者 2. 起已調整過利率者 ) 4-員工利率調整 5.按商品別調整 int
		if (iAdjustDate > 0 && iAdjustDateC == 1) {
			sql += "   and r.\"EffectDate\"  < " + iAdjustDate;
		}
		if (iAdjustDate > 0 && iAdjustDateC == 2) {
			sql += "   and r.\"EffectDate\"  >= " + iAdjustDate;
		}
		
		// 團體戶 ....... 9999999999 + => 團體戶統編 , 5.按商品別調整
		if (iTxKind == 5 && !iGroupUKey.isEmpty()) {
			sql += "   and a.\"GroupUKey\" = '" + iGroupUKey + "' ";
		}
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		return this.convertToMap(query.getResultList());
	}

	private String getProdNoString(TitaVo titaVo) throws LogicException {
		String result = "";
		for (int i = 1; i <= 10; i++) {
			String titaName = "ProdNo" + i;
//			titaVo.getParam("ProdNo1");
			if (titaVo.getParam(titaName) != null && !"".equals(titaVo.getParam(titaName))) {
				if (result.length() > 0) {
					result += ",'" + titaVo.getParam(titaName) + "'";
				} else {
					result = "'" + titaVo.getParam(titaName) + "'";
				}
			} else {
				this.info("i ..." + i + " null...");
				break;
			}
		}

		this.info("result ..." + result);

		return result;
	}
}