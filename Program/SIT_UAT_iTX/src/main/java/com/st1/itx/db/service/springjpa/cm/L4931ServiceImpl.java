package com.st1.itx.db.service.springjpa.cm;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.repository.online.LoanBorMainRepository;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L4931ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L4931ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(L4931ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Autowired
	private Parse parse;

	@Autowired
	private DateUtil dateUtil;

	// *** 折返控制相關 ***
	private int index;

	// *** 折返控制相關 ***
	private int limit;

	// *** 折返控制相關 ***
	private int cnt;

	// *** 折返控制相關 ***
	private int size;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		logger.info("L4931.findAll");

		int today = parse.stringToInteger(titaVo.getCalDy()) + 19110000;
		int iCustType = parse.stringToInteger(titaVo.getParam("CustType"));
		String iAdjCode = titaVo.getParam("AdjCode");
		int iTxKind = parse.stringToInteger(titaVo.getParam("TxKind"));
		int iAdjDate = parse.stringToInteger(titaVo.getParam("AdjDate")) + 19110000;
		String iInqCode = titaVo.getParam("InqCode");

		int custCode1 = 0;
		int custCode2 = 0;
		String adjCode1 = iAdjCode;
		String adjCode2 = iAdjCode;
//		1:個金;2:企金（含企金自然人）
		if (iCustType == 2) {
			custCode1 = 1;
			custCode2 = 2;
		}
		if ("9".equals(iAdjCode) || "A".equals(iAdjCode)) {
			adjCode1 = "0";
			adjCode2 = "8";
		}

		logger.info("today = " + today);

		String sql = " ";
		sql += " select                                                         ";
		sql += "   b.\"AdjDate\"          as F0                                 ";
		sql += " , b.\"CustNo\"           as F1                                 ";
		sql += " , b.\"FacmNo\"           as F2                                 ";
		sql += " , b.\"BormNo\"           as F3                                 ";
		sql += " , b.\"TxKind\"           as F4                                 ";
		sql += " , b.\"DrawdownAmt\"      as F5                                 ";
		sql += " , b.\"CityCode\"         as F6                                 ";
		sql += " , b.\"AreaCode\"         as F7                                 ";
		sql += " , b.\"IncrFlag\"         as F8                                 ";
		sql += " , b.\"AdjCode\"          as F9                                 ";
		sql += " , b.\"RateKeyInCode\"    as F10                                ";
		sql += " , b.\"ConfirmFlag\"      as F11                                ";
		sql += " , b.\"TotBalance\"       as F12                                ";
		sql += " , b.\"LoanBalance\"      as F13                                ";
		sql += " , b.\"PresEffDate\"      as F14                                ";
		sql += " , b.\"CurtEffDate\"      as F15                                ";
		sql += " , b.\"PreNextAdjDate\"   as F16                                ";
		sql += " , b.\"PreNextAdjFreq\"   as F17                                ";
		sql += " , b.\"PrevIntDate\"      as F18                                ";
		sql += " , b.\"CustCode\"         as F19                                ";
		sql += " , b.\"ProdNo\"           as F20                                ";
		sql += " , b.\"RateIncr\"         as F21                                ";
		sql += " , b.\"ContractRate\"     as F22                                ";
		sql += " , b.\"PresentRate\"      as F23                                ";
		sql += " , b.\"ProposalRate\"     as F24                                ";
		sql += " , b.\"AdjustedRate\"     as F25                                ";
		sql += " , b.\"ContrBaseRate\"    as F26                                ";
		sql += " , b.\"ContrRateIncr\"    as F27                                ";
		sql += " , b.\"IndividualIncr\"   as F28                                ";
		sql += " , b.\"BaseRateCode\"     as F29                                ";
		sql += " , b.\"RateCode\"         as F30                                ";
		sql += " , b.\"CurrBaseRate\"     as F31                                ";
		sql += " , b.\"TxEffectDate\"     as F32                                ";
		sql += " , b.\"TxRateAdjFreq\"    as F33                                ";
		sql += " , b.\"JsonFields\"       as F34                                ";
		sql += " , cm.\"CustName\"        as F35                                ";
		sql += " , cb.\"BaseRate\"        as F36                                ";
		sql += " , cc.\"CityItem\"        as F37                                ";
		sql += " , cc.\"IntRateCeiling\"  as F38                                ";
		sql += " , cc.\"IntRateFloor\"    as F39                                ";
		sql += " , ca.\"AreaItem\"        as F40                                ";
		sql += " from \"BatxRateChange\" b                                      ";
		sql += " left join \"CustMain\" cm on cm.\"CustNo\" = b.\"CustNo\"      ";
		sql += " left join (                                                    ";
		sql += "     select                                                     ";
		sql += "      \"BaseRateCode\"                                          ";
		sql += "     ,\"BaseRate\"                                              ";
		sql += "     ,row_number() over (partition by \"BaseRateCode\" order by \"EffectDate\" Desc) as seq ";
		sql += "     from \"CdBaseRate\"                                        ";
		sql += "     where \"CurrencyCode\" = 'TWD'                             ";
		sql += "       and  \"EffectDate\" <= " + today;
		sql += " ) cb on cb.\"BaseRateCode\" = b.\"BaseRateCode\"               ";
		sql += "     and cb.seq = 1                                             ";
		sql += " left join \"CdCity\" cc  on cc.\"CityCode\" = b.\"CityCode\"   ";
		sql += " left join \"CdArea\" ca  on ca.\"CityCode\" = b.\"CityCode\"   ";
		sql += "                       and ca.\"AreaCode\" = b.\"AreaCode\"     ";
		sql += " where b.\"CustCode\" >= " + custCode1;
		sql += "   and b.\"CustCode\" <= " + custCode2;
		sql += "   and b.\"TxKind\" = " + iTxKind;
		sql += "   and b.\"AdjCode\" >= " + adjCode1;
		sql += "   and b.\"AdjCode\" <= " + adjCode2;
		sql += "   and b.\"AdjDate\"  = " + iAdjDate;

		if ("2".equals(iInqCode)) {
			sql += "   and b.\"RateKeyInCode\"  = 2                                 ";
		} else {
			sql += "   and b.\"RateKeyInCode\" != 2                                 ";
		}

		logger.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);

		cnt = query.getResultList().size();
		logger.info("Total cnt ..." + cnt);

		// *** 折返控制相關 ***
		// 設定從第幾筆開始抓,需在createNativeQuery後設定
		query.setFirstResult(this.index * this.limit);

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);

		List<Object> result = query.getResultList();

		size = result.size();
		logger.info("Total size ..." + size);

		return this.convertToMap(result);
	}

	public List<Map<String, String>> findAll(int index, int limit, TitaVo titaVo) throws Exception {
		this.index = index;
		this.limit = limit;

		return findAll(titaVo);
	}

	public int getSize() {
		return cnt;
	}
}