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

@Service("L4702ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L4702ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Autowired
	private Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		this.info("L4702.findAll");

		int acDate = parse.stringToInteger(titaVo.get("ACCTDATE_ED")) + 19110000;
		this.info("acDate = " + acDate);
		// 本日有匯款轉帳A3且部份還本
		String sql = "";
		sql += " WITH BATX AS (                             ";
		sql += "  SELECT                                    ";
		sql += "    \"AcDate\"                              ";
		sql += "   ,\"CustNo\"		                        ";
		sql += "   ,\"ReconCode\"                           ";
		sql += "   ,MAX(\"EntryDate\")   AS \"EntryDate\"   ";
		sql += "   ,SUM(\"RepayAmt\")    AS \"RepayAmt\"    ";
		sql += "  FROM \"BatxDetail\"                       ";
		sql += " where \"AcDate\" = :acDate                 ";
		sql += "   and \"ReconCode\" in ('A3')              ";
		sql += "   and \"ProcStsCode\" in ('5','6','7')     ";
		sql += " group by \"AcDate\", \"CustNo\", \"ReconCode\" ";
		sql += " )                       ";
		sql += " , LNTX AS (                             ";
		sql += "  SELECT                                    ";
		sql += "    \"AcDate\"                              ";
		sql += "   ,\"CustNo\"		                        ";
		sql += "   ,\"FacmNo\"		                        ";
		sql += "   ,MAX(\"EntryDate\")   AS \"EntryDate\"   ";
		sql += "   ,SUM(\"ExtraRepay\")  AS \"ExtraRepay\"  ";
		sql += "  FROM \"LoanBorTx\"                        ";
		sql += " where \"AcDate\" = :acDate                 ";
		sql += "   and \"TitaTxCd\" in ('L3200')            ";
		sql += "   and \"TitaHCode\" = '0'                  ";
		sql += "   and \"ExtraRepay\" > 0                   ";
		sql += " group by \"AcDate\", \"CustNo\", \"FacmNo\" ";
		sql += " )                       ";
		sql += " select                                            ";
		sql += "  x.\"CustNo\"                          AS \"CustNo\"    ";
		sql += " ,x.\"FacmNo\"                          AS \"FacmNo\"    ";
		sql += " ,c.\"CustName\"                        AS \"CustName\"  ";
		sql += " ,f.\"RepayCode\"                       AS \"RepayCode\" ";
		sql += " ,c.\"EntCode\"                         AS \"EntCode\"   ";
		sql += " ,d.\"EntryDate\"                       AS \"EntryDate\" ";
		sql += " ,d.\"RepayAmt\"                        AS \"RepayAmt\"  ";
		sql += " ,d.\"ReconCode\"                       AS \"ReconCode\" ";
		sql += " from BATX d                                             ";
		sql += " left join LNTX x on x.\"AcDate\" = d.\"AcDate\" ";
		sql += "                 and x.\"EntryDate\" = d.\"EntryDate\"   ";
		sql += "                 and x.\"CustNo\" = d.\"CustNo\" ";
		sql += " left join \"CustMain\" c on c.\"CustNo\" = d.\"CustNo\" ";
		sql += " left join \"FacMain\" f on f.\"CustNo\" = x.\"CustNo\"  ";
		sql += "                        and f.\"FacmNo\" = x.\"FacmNo\"  ";  
     	sql += " left join \"CustNotice\" n                              "; //客戶通知設定檔 By 額度
		sql += "        on n.\"FormNo\" = 'L4702'                        ";
		sql += "       and n.\"CustNo\" = x.\"CustNo\"                   ";
		sql += "       and n.\"FacmNo\" = x.\"FacmNo\"                   ";  
		sql += " left join \"CustNotice\" n0                             "; //客戶通知設定檔 by 戶號
		sql += "        on n0.\"FormNo\" = 'L4702'                       ";
		sql += "       and n0.\"CustNo\" = x.\"CustNo\"                  ";
		sql += "       and n0.\"FacmNo\" = 0                             ";  
		sql += " where x.\"CustNo\" is not null                          "; 
		sql += "   and nvl(n.\"PaperNotice\",'Y') = 'Y'                  "; // 發送
		sql += "   and nvl(n0.\"PaperNotice\",'Y') = 'Y'                 "; // 發送
		sql += " order by x.\"CustNo\", x.\"FacmNo\"                     ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);
		query.setParameter("acDate", acDate);
		return this.convertToMap(query);
	}

}