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
		String iFlag = titaVo.get("ClsFlag").trim();
		int iFacmNo = Integer.valueOf(titaVo.get("FacmNo"));
		this.info("iDAY    =" + iDAY);
		this.info("iENTDAY =" + iENTDAY);
		this.info("iFlag   =" + iFlag);
		this.info("iFacmNo =" + iFacmNo);

		String sql = "SELECT  \"Fn_ParseEOL\"(C.\"CustName\",0) \"CustName\" ";
		sql += "             , F.\"FirstDrawdownDate\"          \"FirstDrawdownDate\"  ";
		sql += "             , F.\"LoanTermYy\"                 \"LoanTermYy\"  ";
		sql += "             , F.\"LoanTermMm\"                 \"LoanTermMm\"  ";
		sql += "             , F.\"LoanTermDd\"                 \"LoanTermDd\"  ";
		sql += "             , F.\"LineAmt\"                    \"LineAmt\"  ";
		sql += "             , M.\"CustNo\"                     \"CustNo\"  ";
		sql += "             , M.\"FacmNo\"                     \"FacmNo\"  ";
		sql += "             , M.\"LoanBal\"                    \"LoanBal\"  ";
		sql += "             , C.\"CustId\"                     \"CustId\"  ";
		sql += "             , C.\"CuscCd\"                     \"CuscCd\"  ";
		sql += "             , F.\"ProdNo\"                     \"ProdNo\"  ";
		sql += "             , P.\"GovOfferFlag\"               \"GovOfferFlag\"  ";
		sql += "             , P.\"ProdName\"                   \"ProdName\"  ";
		sql += "             , F.\"ApplNo\"                     \"ApplNo\"  ";
		sql += "       FROM ( SELECT  m.\"CustNo\"                   ";
		sql += "                     ,m.\"FacmNo\"                 ";
		//sql += "                   ,SUM(\"LoanBal\") AS \"LoanBal\"  ";
		sql += "                     ,l.\"LoanBal\"   "; 
		sql += "              FROM ( SELECT  \"CustNo\"    ";
		sql += "                             ,\"FacmNo\"    ";
		sql += "                             ,\"BormNo\"    ";
		sql += "                             ,MAX(\"AcDate\") as \"AcDate\" ";
		//sql += "                             ,\"LoanBal\"   ";
		//sql += "                             ,ROW_NUMBER() OVER (PARTITION By \"CustNo\", \"FacmNo\", \"BormNo\" ORDER BY \"BorxNo\" DESC) as ROW_NO   ";
		sql += "                     FROM  \"LoanBorTx\"              ";
		sql += "                     WHERE \"CustNo\" = :iCUSTNO    ";
		sql += "                      and  \"AcDate\" <= :iDAY      ";
		sql += "                      and \"TitaTxCd\" in ('L3100','L3200','L3240','L3410','L3420')  ";
		sql += "                      and \"BormNo\" > 0  ";
		sql += "                      group by ";
		sql += "                             \"CustNo\"    ";
		sql += "                             ,\"FacmNo\"    ";
		sql += "                             ,\"BormNo\"    ";
		sql += "               ) M";
		sql += "                Left join \"LoanBorMain\" l ";
		sql += "                        on  m.\"CustNo\" = l.\"CustNo\"    ";
		sql += "                       and  m.\"FacmNo\" = l.\"FacmNo\"    ";
		sql += "                       and  m.\"BormNo\" = l.\"BormNo\"    ";
		//sql += "               where ROW_NO = 1              ";
		//sql += "               GROUP BY \"CustNo\", \"FacmNo\"   ";
		sql += "             ) M ";
		sql += "       LEFT JOIN \"CustMain\" C";
		sql += "              ON   C.\"CustNo\" = M.\"CustNo\"";
		sql += "       LEFT JOIN \"FacMain\" F ON F.\"CustNo\"= M.\"CustNo\"";
		sql += "              AND  F.\"FacmNo\" = M.\"FacmNo\"";
		sql += "       LEFT JOIN \"FacProd\" P ON P.\"ProdNo\"= F.\"ProdNo\"";
		if (iFlag.equals("Y")) {
			sql += "   where M.\"LoanBal\" >= 0 ";
		} else {
			sql += "   where M.\"LoanBal\" > 0 ";
		}
		if (iFacmNo != 0) {
			sql += "     and M.\"FacmNo\" = :iFacmNo  ";
		}
		sql += " order by \"FacmNo\" ";

		this.info("L9706 Simplsql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("iCUSTNO", iCUSTNO);
		query.setParameter("iDAY", iDAY);
		if (iFacmNo != 0) {
			query.setParameter("iFacmNo", iFacmNo);
		}
		return this.convertToMap(query);
	}

}