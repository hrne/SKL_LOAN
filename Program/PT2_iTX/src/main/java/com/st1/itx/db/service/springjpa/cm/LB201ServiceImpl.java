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
import com.st1.itx.eum.ContentName;

@Service("lB201ServiceImpl")
@Repository

/*
 * LB201 聯徵授信餘額月報檔
 */

public class LB201ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(int dataMonth, TitaVo titaVo) throws Exception {
//		boolean onLineMode = true;
		boolean onLineMode = false;

		this.info("----------- LB201.findAll ---------------");
		this.info("-----LB201 TitaVo=" + titaVo);
		this.info("-----LB201 Tita ENTDY=" + titaVo.getEntDy().substring(0, 6));

		String sql = "";

		// LB201 聯徵授信餘額月報檔
		sql = "SELECT M.\"BankItem\"" + "     , M.\"BranchItem\"" + "     , M.\"TranCode\"" + "     , M.\"SubTranCode\""
				+ "     , M.\"AcctNo\"" + "     , M.\"TotalAmt\"" + "     , M.\"CustId\"" + "     , M.\"CustIdErr\""
				+ "     , M.\"SuvId\"" + "     , M.\"SuvIdErr\"" + "     , M.\"OverseasId\""
				+ "     , M.\"IndustryCode\"" + "     , M.\"Filler12\"" + "     , M.\"AcctCode\""
				+ "     , M.\"SubAcctCode\"" + "     , M.\"OrigAcctCode\"" + "     , M.\"ConsumeFg\""
				+ "     , M.\"FinCode\"" + "     , M.\"ProjCode\"" + "     , M.\"NonCreditCode\""
				+ "     , M.\"UsageCode\"" + "     , M.\"ApproveRate\"" + "     , M.\"DrawdownDate\""
				+ "     , M.\"MaturityDate\"" + "     , M.\"CurrencyCode\"" + "     , M.\"DrawdownAmt\""
				+ "     , M.\"DrawdownAmtFx\"" + "     , M.\"RecycleCode\"" + "     , M.\"IrrevocableFlag\""
				+ "     , M.\"FacmNo\"" + "     , M.\"UnDelayBal\"" + "     , M.\"UnDelayBalFx\""
				+ "     , M.\"DelayBal\"" + "     , M.\"DelayBalFx\"" + "     , M.\"DelayPeriodCode\""
				+ "     , M.\"RepayCode\"" + "     , M.\"PayAmt\"" + "     , M.\"Principal\"" + "     , M.\"Interest\""
				+ "     , M.\"Fee\"" + "     , M.\"FirstDelayCode\"" + "     , M.\"SecondDelayCode\""
				+ "     , M.\"BadDebtCode\"" + "     , M.\"NegStatus\"" + "     , M.\"NegCreditor\""
				+ "     , M.\"NegNo\"" + "     , M.\"NegTransYM\"" + "     , M.\"Filler443\"" + "     , M.\"ClType\""
				+ "     , M.\"ClEvaAmt\"" + "     , M.\"ClTypeCode\"" + "     , M.\"SyndKind\""
				+ "     , M.\"SyndContractDate\"" + "     , M.\"SyndRatio\"" + "     , M.\"Filler51\""
				+ "     , M.\"Filler52\"" + "     , M.\"PayablesFg\"" + "     , M.\"NegFg\"" + "     , M.\"Filler533\""
				+ "     , M.\"GuaTypeCode1\"" + "     , M.\"GuaId1\"" + "     , M.\"GuaIdErr1\""
				+ "     , M.\"GuaRelCode1\"" + "     , M.\"GuaTypeCode2\"" + "     , M.\"GuaId2\""
				+ "     , M.\"GuaIdErr2\"" + "     , M.\"GuaRelCode2\"" + "     , M.\"GuaTypeCode3\""
				+ "     , M.\"GuaId3\"" + "     , M.\"GuaIdErr3\"" + "     , M.\"GuaRelCode3\""
				+ "     , M.\"GuaTypeCode4\"" + "     , M.\"GuaId4\"" + "     , M.\"GuaIdErr4\""
				+ "     , M.\"GuaRelCode4\"" + "     , M.\"GuaTypeCode5\"" + "     , M.\"GuaId5\""
				+ "     , M.\"GuaIdErr5\"" + "     , M.\"GuaRelCode5\"" + "     , M.\"Filler741\""
				+ "     , M.\"GraceStartYM\"" + "     , M.\"GraceEndYM\"" + "     , M.\"GreenFg\""
				+ "     , M.\"GreenCode\"" + "     , M.\"SustainFg\"" + "     , M.\"SustainCode\""
				+ "     , M.\"SustainNoReachFg\"" + "     , M.\"BadDebtDate\"" + "     , M.\"SyndCode\""
				+ "     , M.\"BankruptDate\"" + "     , M.\"BdLoanFg\"" + "     , M.\"SmallAmt\""
				+ "     , M.\"ExtraAttrCode\"" + "     , M.\"ExtraStatusCode\"" + "     , M.\"Filler74A\""
				+ "     , M.\"JcicDataYM\"" + "     , M.\"DataEnd\"" + " FROM  \"JcicB201\" M"
				+ " WHERE M.\"DataYM\" = :dataMonth " // 2021-12-20 智偉修改
				+ " AND M.\"TranCode\" = 'A' "
				+ " ORDER BY M.\"BankItem\", M.\"BranchItem\", M.\"TranCode\", M.\"AcctNo\" ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em;

		if (onLineMode) {
			em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine); // onLine 資料庫
		} else {
			em = this.baseEntityManager.getCurrentEntityManager(titaVo); // 從 LB201.java 帶入資料庫環境
		}

		query = em.createNativeQuery(sql);
		query.setParameter("dataMonth", dataMonth); // 2021-12-20 智偉新增

		// 轉成 List<HashMap<String, String>>
		return this.convertToMap(query);
	}
}
