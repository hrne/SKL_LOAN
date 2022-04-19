package com.st1.itx.db.service.springjpa.cm;

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

@Service("bS430ServiceImpl")
@Repository
/* 逾期放款明細 */
public class BS430ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

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
		sql += "   ,NVL(r2.\"FitRate\", 0)                       as F29 "; // 機動非指數目前利率
		sql += "   ,NVL(r2.\"EffectDate\", 0)                    as F30 "; // 機動非指數目前生效日
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
		sql += " where b.\"Status\" = 0                                             ";
		sql += "   and b.\"MaturityDate\" > b.\"NextAdjRateDate\"                   ";
		sql += "   and c.\"EntCode\" >= " + iEntCode1;
		sql += "   and c.\"EntCode\" <= " + iEntCode2;
		sql += "   and case " + iTxKind;
//  1.定期機動調整 ==>  1.撥款主檔的利率區分=3.定期機動，下次利率調整日為調整月份
//	                    2.借戶利率檔的的利率區分=3.定期機動，指標利率種類=該指標利率種類抓，生效日期 <= 調整月份 
		sql += "      when 1  then case when b.\"RateCode\" = '3' " + "    and  b.\"NextAdjRateDate\" >= " + iEffectDateS;
		sql += "       and  b.\"NextAdjRateDate\" <= " + iEffectDateE;
		sql += "                         and r.\"BaseRateCode\" = " + iBaseRateCode;
		sql += "                         and r.\"RateCode\" = '3'                  ";
		sql += "                        then 1                                     ";
		sql += "                        else 0                                     ";
		sql += "                   end                                             ";
// 2.指數型利率調整 ==> 1.撥款主檔的利率區分=1.機動
//                      2.借戶利率檔的利率區分=1.機動,指標利率種類=該指標利率種類,生效日期 <= 調整日期 
		sql += "      when 2  then case when b.\"RateCode\" = '1'                  ";
		sql += "                         and r.\"BaseRateCode\" = " + iBaseRateCode + " and  r.\"RateCode\" = '1'  ";
		sql += "                        then 1                                     ";
		sql += "                        else 0                                     ";
		sql += "                   end                                             ";
// 3.機動利率調整 ==> 1.撥款主檔的利率區分=1.機動
//                    2.借戶利率檔的利率區分=1.機動,指標利率種類=99，生效日期 = 調整月份，商品<>員工利率
		sql += "      when 3  then case when b.\"RateCode\" = '1' and  p.\"EmpFlag\" <> 'Y' ";
		sql += "                         and r.\"RateCode\" = '1' and r.\"BaseRateCode\" = 99 and  r.\"EffectDate\" >= " + iEffectDateS;
		sql += "	 	                 and r.\"EffectDate\" <= " + iEffectDateE;
		sql += "                        then 1                                     ";
		sql += "                        else 0                                     ";
		sql += "                   end                                             ";
// 4.員工利率調整 ==>1.撥款主檔的利率區分=1.機動 
//				     2.借戶利率檔的利率區分=1.機動,生效日期 <= 調整日期，商品=員工利率
		sql += "      when 4 then case when b.\"RateCode\" = '1' and p.\"EmpFlag\" = 'Y' ";
		sql += "                        then 1                                     ";
		sql += "                        else 0                                     ";
		sql += "                   end                                             ";
// 5.按商品別調整 [iAdjDateS=iAdjDateE=利率生效日]    
		sql += "      when 5 then  1                                               ";
		sql += "      else 0                                                       ";
		sql += "      end = 1                                                      ";

		String prodNoString = getProdNoString(titaVo);

		if (!"".equals(prodNoString)) {
			sql += "   and p.\"ProdNo\" in ( " + prodNoString + " ) ";
		}

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		return this.convertToMap(query);
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