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
import com.st1.itx.db.repository.online.LoanBorMainRepository;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.util.parse.Parse;

@Service("L4931ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L4931ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Autowired
	private Parse parse;

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

		this.info("L4931.findAll");

		int today = parse.stringToInteger(titaVo.getCalDy()) + 19110000;
		int iCustType = parse.stringToInteger(titaVo.getParam("CustType"));

		String iAdjCode = "";
		if (titaVo.getParam("AdjCode") != null || !"".equals(titaVo.getParam("AdjCode"))) {
			iAdjCode = titaVo.getParam("AdjCode").substring(0, 1);
		}
		String iRateKeyInCode = "";
		if (titaVo.getParam("AdjCode").length() == 2) {
			iRateKeyInCode = titaVo.getParam("AdjCode").substring(1, 2);
		}
		int iTxKind = parse.stringToInteger(titaVo.getParam("TxKind"));
		int iAdjDate = parse.stringToInteger(titaVo.getParam("AdjDate")) + 19110000;
		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		//
		String iInqCode = titaVo.getParam("InqCode"); // 0-要處理 9-待處理 A-全部
		int iOvduTerm = parse.stringToInteger(titaVo.getParam("OvduTerm"));

		int custCode1 = 0;
		int custCode2 = 0;
		String adjCode = iAdjCode;
//		1:個金;2:企金（含企金自然人）
		if (iCustType == 2) {
			custCode1 = 1;
			custCode2 = 2;
		}
		if ("9".equals(iAdjCode) || "A".equals(iAdjCode)) {
			adjCode = "";
		}

		this.info("today = " + today);

		String sql = " ";
		sql += " select                              ";
		sql += "   b.\"AdjDate\"                     "; // F0
		sql += " , b.\"CustNo\"                      "; // F1
		sql += " , b.\"FacmNo\"                      "; // F2
		sql += " , b.\"BormNo\"                      "; // F3
		sql += " , b.\"TxKind\"                      "; // F4
		sql += " , b.\"DrawdownAmt\"                 "; // F5
		sql += " , b.\"CityCode\"                    "; // F6
		sql += " , b.\"AreaCode\"                    "; // F7
		sql += " , b.\"IncrFlag\"                    "; // F8
		sql += " , b.\"AdjCode\"                     "; // F9
		sql += " , b.\"RateKeyInCode\"               "; // F10
		sql += " , b.\"ConfirmFlag\"                 "; // F11
		sql += " , b.\"TotBalance\"                  "; // F12
		sql += " , b.\"LoanBalance\"                 "; // F13
		sql += " , b.\"PresEffDate\"                 "; // F14
		sql += " , b.\"CurtEffDate\"                 "; // F15
		sql += " , b.\"PreNextAdjDate\"              "; // F16
		sql += " , b.\"PreNextAdjFreq\"              "; // F17
		sql += " , b.\"PrevIntDate\"                 "; // F18
		sql += " , b.\"CustCode\"                    "; // F19
		sql += " , b.\"ProdNo\"                      "; // F20
		sql += " , b.\"RateIncr\"                    "; // F21
		sql += " , b.\"ContractRate\"                "; // F22
		sql += " , b.\"PresentRate\"                 "; // F23
		sql += " , b.\"ProposalRate\"                "; // F24
		sql += " , b.\"AdjustedRate\"                "; // F25
		sql += " , b.\"ContrBaseRate\"               "; // F26
		sql += " , b.\"ContrRateIncr\"               "; // F27
		sql += " , b.\"IndividualIncr\"              "; // F28
		sql += " , b.\"BaseRateCode\"                "; // F29
		sql += " , b.\"RateCode\"                    "; // F30
		sql += " , b.\"CurrBaseRate\"                "; // F31
		sql += " , b.\"TxEffectDate\"                "; // F32
		sql += " , b.\"TxRateAdjFreq\"               "; // F33
		sql += " , b.\"JsonFields\"                  "; // F34
		sql += " , cm.\"CustName\"                   "; // F35
		sql += " , cc.\"CityItem\"                   "; // F36
		sql += " , cc.\"IntRateCeiling\"             "; // F37
		sql += " , cc.\"IntRateFloor\"               "; // F38
		sql += " , ca.\"AreaItem\"                   "; // F39
		sql += " , b.\"OvduTerm\"                    "; // F40
		sql += " from \"BatxRateChange\" b                                      ";
		sql += " left join \"CustMain\" cm on cm.\"CustNo\" = b.\"CustNo\"      ";
		sql += " left join \"CdCity\" cc  on cc.\"CityCode\" = b.\"CityCode\"   ";
		sql += " left join \"CdArea\" ca  on ca.\"CityCode\" = b.\"CityCode\"   ";
		sql += "                       and ca.\"AreaCode\" = b.\"AreaCode\"     ";
		sql += " where b.\"CustCode\" >= " + custCode1;
		sql += "   and b.\"CustCode\" <= " + custCode2;
		sql += "   and b.\"TxKind\" = " + iTxKind;
		sql += "   and b.\"AdjDate\"  = " + iAdjDate;
		if (iCustNo > 0) {
			sql += "   and b.\"CustNo\"  = " + iCustNo;
		}
		if (!"".equals(adjCode)) {
			sql += "   and b.\"AdjCode\" = " + adjCode;
		}
		if (!"".equals(iRateKeyInCode)) {
			sql += "   and b.\"RateKeyInCode\" = " + iRateKeyInCode;
		}
		if (iOvduTerm != 0) {
			sql += "   and b.\"OvduTerm\"  = " + iOvduTerm;
		}
		if (!"A".equals(iInqCode)) {
			if ("9".equals(iInqCode)) {
				sql += "   and b.\"RateKeyInCode\"  = 9                                 ";
			} else {
				sql += "   and b.\"RateKeyInCode\" != 9                                 ";
			}
		}

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);

		cnt = query.getResultList().size();
		this.info("Total cnt ..." + cnt);

		// *** 折返控制相關 ***
		// 設定從第幾筆開始抓,需在createNativeQuery後設定
		query.setFirstResult(this.index * this.limit);

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);

		List<Object> result = query.getResultList();

		size = result.size();
		this.info("Total size ..." + size);

		return this.convertToMap(query);
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