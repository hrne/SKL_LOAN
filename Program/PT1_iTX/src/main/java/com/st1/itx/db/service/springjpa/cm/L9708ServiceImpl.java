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

@Service("l9708ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L9708ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo,int acDate) throws Exception {

		this.info("L9708.findAll");
		String iDAY = "";
		if(acDate ==0) {
			iDAY = String.valueOf(Integer.valueOf(titaVo.get("ACCTDATE")) + 19110000);
		}else {
			iDAY = String.valueOf(acDate);
		}
		
		this.info("會計日期     = " + iDAY);
		
		String sql = "";
		sql += "   	   SELECT    B.\"RepayBank\" F0";
		sql += "   	   		   , B.\"RepayBank\"||D.\"BankItem\" F1";
		sql += "               , M.\"DrawdownDate\" F2";
		sql += "               , M.\"CustNo\" F3";
		sql += "               , M.\"FacmNo\" F4";
		sql += "               , M.\"FirstDueDate\" F5";
		sql += "               , B.\"RepayAcct\" F6";
		sql += "               , \"Fn_ParseEOL\"(C.\"CustName\",0) F7 ";
		sql += "        FROM (Select                                  ";
		sql += "                 A.\"CustNo\" ";
		sql += "               , A.\"FacmNo\" ";
		sql += "               , min(L.\"FirstDueDate\")  AS \"FirstDueDate\" ";
		sql += "               , min(L.\"DrawdownDate\")  AS \"DrawdownDate\" ";
		sql += "              FROM \"AcDetail\" A";
		sql += "              LEFT JOIN \"LoanBorMain\" L ON L.\"CustNo\" = A.\"CustNo\"";
		sql += "                                         AND L.\"FacmNo\" = A.\"FacmNo\"";
		sql += "                                         AND L.\"BormNo\" = A.\"BormNo\"";
		sql += "              LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = L.\"CustNo\"";
		sql += "                                     AND F.\"FacmNo\" = L.\"FacmNo\"";
		sql += "              WHERE A.\"AcDate\" = :iDAY ";
		sql += "                AND A.\"EntAc\" = 1 ";
		sql += "                AND A.\"TitaTxCd\" = 'L3100' ";
		sql += "                AND F.\"RepayCode\" IN (2)  ";  // 2: 銀行扣款
		sql += "                group by A.\"CustNo\" , A.\"FacmNo\" ";
		sql += "             ) M  ";		
		sql += "        LEFT JOIN \"CustMain\" C ON  C.\"CustNo\" = M.\"CustNo\"";
		sql += "        LEFT JOIN \"BankAuthAct\" B ON B.\"CustNo\" = M.\"CustNo\"";
		sql += "                                   AND B.\"FacmNo\" = M.\"FacmNo\"";
		sql += "                                   AND B.\"AuthType\" <> '02' ";
		sql += "        LEFT JOIN \"CdBank\" D ON B.\"RepayBank\" = D.\"BankCode\"";
		sql += "        GROUP BY B.\"RepayBank\" ";
		sql += "   	   		   , B.\"RepayBank\"||D.\"BankItem\" ";
		sql += "               , M.\"DrawdownDate\" ";
		sql += "               , M.\"CustNo\" ";
		sql += "               , M.\"FacmNo\" ";
		sql += "               , M.\"FirstDueDate\" ";
		sql += "               , B.\"RepayAcct\" ";
		sql += "               , C.\"CustName\" ";
		sql += "        ORDER BY \"F0\",\"F1\", \"F2\",\"F3\",\"F4\" ";

		this.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("iDAY", iDAY);
		return this.convertToMap(query.getResultList());
	}

}