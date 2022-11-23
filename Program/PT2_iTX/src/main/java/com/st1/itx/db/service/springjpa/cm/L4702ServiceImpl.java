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

		int entDy = parse.stringToInteger(titaVo.get("ACCTDATE_ED")) + 19110000;
		this.info("entDy = " + entDy);
		// 本日匯款轉帳且有逾期未繳
		String sql = " select                                            ";
		sql += "  l.\"CustNo\"                          AS \"CustNo\"    ";
		sql += " ,NVL(l.\"FacmNo\", x.\"FacmNo\")       AS \"FacmNo\"    ";
		sql += " ,c.\"CustName\"                        AS \"CustName\"  ";
		sql += " ,d.\"RepayCode\"                       AS \"RepayCode\" ";
		sql += " ,c.\"EntCode\"                         AS \"EntCode\"   ";
		sql += " ,d.\"EntryDate\"                       AS \"EntryDate\" ";
		sql += " ,NVL(x.\"TxAmt\",0)                    AS \"RepayAmt\"  ";
		sql += " ,d.\"ReconCode\"                       AS \"ReconCode\"  ";
		sql += " ,JSON_VALUE(x.\"OtherFields\",'$.RepayKindCode') AS \"RepayKindCode\" ";
		sql += " from \"BatxDetail\" d                                   ";
		sql += " left join \"CustMain\" c on c.\"CustNo\" = d.\"CustNo\" ";
		sql += " left join \"LoanBorTx\" x on x.\"AcDate\" = d.\"AcDate\"        ";
		sql += "                          and x.\"TitaTlrNo\" = d.\"TitaTlrNo\"  ";
		sql += "                          and x.\"TitaTxtNo\"= d.\"TitaTxtNo\"   ";
//		sql += "                          and x.\"TxAmt\"= d.\"RepayAmt\"        ";
		sql += "                          and x.\"Displayflag\"= 'F'        ";
		sql += " left join (                                             ";
		sql += "     select                                              ";
		sql += "       \"CustNo\"                                        ";
		sql += "      ,\"FacmNo\"                                        ";
		sql += "      ,min(\"NextPayIntDate\") as  \"NextPayIntDate\"    ";
		sql += "      from \"LoanBorMain\"                               ";
		sql += "      where \"Status\" = 0                               ";
		sql += "       and \"NextPayIntDate\" <= :entDy                  ";
		sql += "      group by \"CustNo\", \"FacmNo\"                    ";
		sql += " ) l   on l.\"CustNo\" = d.\"CustNo\"                    ";
		sql += "      and l.\"NextPayIntDate\" <= d.\"EntryDate\"        "; // 有逾期
		sql += " left join \"CustNotice\" n                              "; //客戶通知設定檔
		sql += "        on \"FormNo\" = 'L4702'                          ";
		sql += "       and n.\"CustNo\" = l.\"CustNo\"                   ";
		sql += "       and n.\"FacmNo\" = l.\"FacmNo\"                   ";  
		sql += " where d.\"RepayCode\" = 1                               "; // 01.匯款轉帳
		sql += "   and d.\"ProcStsCode\" <> 'D'                          ";
		sql += "   and d.\"RepayType\" = 1                               ";
		sql += "   and d.\"CustNo\" <> 0                                 ";
		sql += "   and d.\"AcDate\" = :entDy                             ";
		sql += "   and l.\"CustNo\" is not null                          "; // 有逾期
		sql += "   and (l.\"NextPayIntDate\" <= d.\"EntryDate\" or x.\"FacmNo\" is not null) ";
		sql += "   and nvl(n.\"PaperNotice\",'Y') = 'Y'                  "; // 發送
		sql += " order by l.\"CustNo\", NVL(l.\"FacmNo\", x.\"FacmNo\")  ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);
		query.setParameter("entDy", entDy);
		return this.convertToMap(query);
	}

}