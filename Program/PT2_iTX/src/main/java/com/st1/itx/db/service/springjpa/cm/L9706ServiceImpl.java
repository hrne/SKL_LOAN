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

@Service("l9706ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L9706ServiceImpl extends ASpringJpaParm implements InitializingBean {

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

		this.info("L9706.findAll");

		String iCUSTNO = titaVo.get("CUSTNO");
		String iDAY = String.valueOf(Integer.valueOf(titaVo.get("ACCTDATE")) + 19110000);
		String iENTDAY = String.valueOf(Integer.valueOf(titaVo.get("ENTDY")) + 19110000);
		String iFlag = titaVo.get("ClsFlag");
		int iFacmMo = Integer.valueOf(titaVo.get("FacmNo"));
		this.info("iDAY    =" + iDAY);
		this.info("iENTDAY =" + iENTDAY);
		this.info("iFlag   =" + iFlag);
		this.info("iFacmMo =" + iFacmMo);

		String sql = "SELECT  \"Fn_ParseEOL\"(C.\"CustName\",0) \"F0\" ";
		sql += "             , F.\"FirstDrawdownDate\" \"F1\" ";
		sql += "             , F.\"LoanTermYy\" \"F2\" ";
		sql += "             , F.\"LoanTermMm\" \"F3\" ";
		sql += "             , F.\"LoanTermDd\" \"F4\" ";
		sql += "             , F.\"LineAmt\" \"F5\" ";
		sql += "             , M.\"CustNo\" \"F6\" ";
		sql += "             , M.\"FacmNo\" \"F7\" ";
		sql += "             , M.\"LoanBal\" \"F8\" ";
		sql += "             , C.\"CustId\" \"F9\" ";
		sql += "             , H.\"BdLocation\"  \"F10\" ";
		if (iDAY.equals(iENTDAY)) {
			sql += "       FROM ( SELECT  M.\"CustNo\"";
			sql += "                    , M.\"FacmNo\"";
			sql += "                    , SUM(M.\"LoanBal\") \"LoanBal\"";
			sql += "              FROM  \"LoanBorMain\" M";
			sql += "              WHERE  M.\"CustNo\" = :icustno ";
			sql += "                AND  M.\"Status\" IN (0, 2, 6, 7)";
			sql += "              GROUP BY M.\"CustNo\", M.\"FacmNo\" ) M";
		} else {
			sql += "        FROM ( SELECT  M.\"CustNo\"";
			sql += "                    , M.\"FacmNo\"";
			sql += "                    , SUM(M.\"LoanBalance\") \"LoanBal\"";
			sql += "              FROM  \"DailyLoanBal\" M";
			sql += "              WHERE  M.\"DataDate\" = :iday ";
			sql += "                AND  M.\"CustNo\"   = :icustno ";
			sql += "              GROUP BY M.\"CustNo\", M.\"FacmNo\" ) M";
		}

		sql += "       LEFT JOIN \"CustMain\" C";
		sql += "              ON   C.\"CustNo\" = M.\"CustNo\"";
		sql += "       LEFT JOIN \"FacMain\" F ON F.\"CustNo\"= M.\"CustNo\"";
		sql += "              AND  F.\"FacmNo\" = M.\"FacmNo\"";
		sql += "       LEFT JOIN \"ClFac\"      g ON g.\"CustNo\" = m.\"CustNo\"";
		sql += "              AND g.\"FacmNo\" = m.\"FacmNo\"";
        sql += "       LEFT JOIN \"ClBuilding\" h ON g.\"ClCode1\" = h.\"ClCode1\"";
        sql += "              AND g.\"ClCode2\" = h.\"ClCode2\"";
        sql += "              AND g.\"ClNo\" = h.\"ClNo\"";
		// 20220523新增條件
		if (iFlag.equals("N") && iFacmMo == 0) {
			sql += "                 where m.\"LoanBal\" != 0 ";
		} else if (iFlag.equals("Y") && iFacmMo != 0) {
			sql += "                 where m.\"LoanBal\" = 0 ";
			sql += "                 and M.\"FacmNo\" = :iFamcNo  ";
		}

		this.info("L9706 Simplsql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("icustno", iCUSTNO);
		if (!iDAY.equals(iENTDAY)) {
			query.setParameter("iday", iDAY);
		}
		if (iFacmMo != 0) {
			query.setParameter("iFacmMo", iFacmMo);
		}
		return this.convertToMap(query.getResultList());
	}

}