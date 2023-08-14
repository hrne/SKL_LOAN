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

@Service("L5R32ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L5R32ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	String sql = "";

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		int acDate = Integer.valueOf(titaVo.get("RimAcDate")) + 19110000;
		int acDate2 = acDate/100;
		this.info("100acDate  = " +  acDate2);
		this.info("L5R32.findAll AcDate=" + acDate);

//		sql = " SELECT SUM(\"TdBal\")    AS F0                         ";
//		sql = " SELECT  \"TdBal\"        AS F0                         ";
//		陣列取法需多筆
//		sql += "        ,''               AS F1                        ";
//		sql += " FROM  \"AcMain\" BD                                   ";
//		sql += " WHERE \"AcDate\" = " + acDate;
		//舜文新增篩選代碼
//		sql += "   AND \"AcctCode\" in ('310','320','330','340','F25','AIO') ";
//		sql += "   AND \"AcBookCode\" = '000'                          ";
//		sql += "   GROUP BY \"AcDate\"         ";
		
		sql =  "select sum(\"TdBal\")  AS F0                                      ";
		sql += "     ,''               AS F1                                      ";
		sql += "  from \"AcMain\"                                                 ";
		sql += "where \"AcDate\" =  " + acDate                                     ;
		sql += "  and \"AcctCode\" in ('310','320','330','340','F25','AIO')       ";
		sql += "  and \"AcBookCode\" = '000'                                      ";                                        
		sql += " union                                                            ";
		sql += "select NVL(round(sum(NVL(I.\"AccumDPAmortized\",0)),0) ,0)  AS F0 ";
		sql += "     ,''               AS F1                                      ";
		sql += "  from \"Ias39IntMethod\" I                                       ";
		sql += "left join \"MonthlyLoanBal\" MLB                                  ";
		sql += "       on MLB.\"YearMonth\"  = I.\"YearMonth\"                    ";
		sql += "      and MLB.\"CustNo\"     = I.\"CustNo\"                       ";
		sql += "      and MLB.\"FacmNo\"     = I.\"FacmNo\"                       ";
		sql += "      and MLB.\"BormNo\"     = I.\"BormNo\"                       ";
		sql += "where I.\"YearMonth\"  = " + acDate2                               ;
		sql += "  and MLB.\"AcctCode\" <> '990'                                   ";
		sql += " union                                                            ";
		sql += "select sum(\"PrinBalance\") AS F0                                 ";
		sql += "     ,''               AS F1                                      ";
		sql += "  from \"MonthlyFacBal\"                                          ";
		sql += "where \"YearMonth\"  = " + acDate2                                 ;
		sql += "  and \"PrinBalance\" > 0                                         ";
		sql += "  and \"AcctCode\" = '990'                                        ";
		sql += " union 															  ";
		sql += "select sum(\"TdBal\")   AS F0                                     ";
		sql += "     ,''               AS F1                                      ";
		sql += " from  \"AcMain\"                                                 ";
		sql += "where \"AcNoCode\" in('10601301000')                              ";
		sql += "  and \"MonthEndYm\"  = " + acDate2                                ;
		
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		return this.convertToMap(query);
	}

}