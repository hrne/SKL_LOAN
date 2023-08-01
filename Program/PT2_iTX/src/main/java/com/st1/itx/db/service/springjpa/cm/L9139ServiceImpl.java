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
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service
@Repository
public class L9139ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	Parse parse;

	@Autowired
	public DateUtil dateUtil;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo, int runDate) throws Exception {

		int entDy = titaVo.getEntDyI() + 19110000;
//		int bussDate = 0;

		int decision = 1;
		if (runDate == entDy) {
			decision = 2;
		}

		// 輸入日期非營業時間
//		dateUtil.init();
//		bussDate = dateUtil.getbussDate(runDate, -1);// 往前找營業日，參數給-1

//		this.info("runDate = " + runDate + ",entDy = " + entDy + ",decision = " + decision + ",bussDate = " + bussDate);
		this.info("runDate = " + runDate + ",entDy = " + entDy + ",decision = " + decision);

		String sql = "";

		// 在會計日期之前，只要使用每日暫收款餘額檔計算暫收款
		if (decision == 1) {
			sql += "select                                                                                    ";
			sql += " :AcDate as \"AcDate\"                                                                    ";
			sql += ",LPAD(a.\"CustNo\",7,0)  as \"CustNo\"                                                    ";
			sql += ",b.\"CustName\"                                                                           ";
			sql += ",a.\"FacmNo\"                                                                             ";
			sql += ",case when \"TdCkBal\" = \"YdCkBal\" then 0 else \"TdCkBal\" end as \"TdCkBal\"           ";
			sql += ",case when \"TdAvBal\" = \"YdAvBal\" then 0 else \"TdAvBal\" end as \"TdAvBal\"           ";
			sql += ",case when \"TdCkBal\" = \"YdCkBal\" then 0 else \"YdCkBal\" end as \"YdCkBal\"           ";
			sql += ",case when \"TdAvBal\" = \"YdAvBal\" then 0 else \"YdAvBal\" end as \"YdAvBal\"           ";
			sql += "from (                                                                                    ";
			sql += "      select                                                                              ";
			sql += "       \"CustNo\"                                                                         ";
			sql += "      ,\"FacmNo\"                                                                         ";
			sql += "      ,sum(\"TdCkBal\") as \"TdCkBal\"                                                    ";
			sql += "      ,sum(\"TdAvBal\") as \"TdAvBal\"                                                    ";
			sql += "      ,sum(\"YdCkBal\") as \"YdCkBal\"                                                    ";
			sql += "      ,sum(\"YdAvBal\") as \"YdAvBal\"                                                    ";
			sql += "      from (                                                                              ";
			sql += "            select                                                                        ";
			sql += "             \"CustNo\"                                                                   ";
			sql += "            ,\"FacmNo\"                                                                   ";
			sql += "            ,case when \"AcctCode\" in ('TCK') then \"TavBal\" else 0 end  AS \"TdCkBal\" ";
			sql += "            ,case when \"AcctCode\" in ('TCK') then  0 else \"TavBal\" end AS \"TdAvBal\" ";
			sql += "            ,0 AS  \"YdCkBal\"                                                            ";
			sql += "            ,0 AS  \"YdAvBal\"                                                            ";
			sql += "            from (                                                                        ";
			sql += "                  select                                                                  ";
			sql += "                   \"CustNo\"                                                             ";
			sql += "                  ,\"FacmNo\"                                                             ";
			sql += "                  ,\"AcctCode\"                                                           ";
			sql += "                  ,\"TavBal\"                                                             ";
			sql += "                  ,ROW_NUMBER() Over (Partition By \"CustNo\", \"FacmNo\", \"AcctCode\"   ";
			sql += "                                          Order By \"AcDate\" DESC)  as ROWNO             ";
			sql += "                  from \"DailyTav\"                                                       ";
			sql += "                  where \"AcctCode\" in ('TAV','TCK','TAM','TSL')                         ";
			sql += "                   and \"AcDate\" <= :AcDate ";
			sql += "                )                                                                         ";
			sql += "            where  ROWNO = 1                                                              ";
			sql += "            union all                                                                     ";
			sql += "            select                                                                        ";
			sql += "             \"CustNo\"                                                                   ";
			sql += "            ,\"FacmNo\"                                                                   ";
			sql += "            ,0 AS  \"TdCkBal\"                                                            ";
			sql += "            ,0 AS  \"TdAvBal\"                                                            ";
			sql += "            ,case when \"AcctCode\" in ('TCK') then \"TavBal\" else 0 end  AS \"YdCkBal\" ";
			sql += "            ,case when \"AcctCode\" in ('TCK') then  0 else \"TavBal\" end AS \"YdAvBal\" ";
			sql += "            from (                                                                        ";
			sql += "                  select                                                                  ";
			sql += "                   \"CustNo\"                                                             ";
			sql += "                  ,\"FacmNo\"                                                             ";
			sql += "                  ,\"AcctCode\"                                                           ";
			sql += "                  ,\"TavBal\"                                                             ";
			sql += "                  ,ROW_NUMBER() Over (Partition By \"CustNo\", \"FacmNo\", \"AcctCode\"   ";
			sql += "                                          Order By \"AcDate\" DESC)  as ROWNO             ";
			sql += "                  from \"DailyTav\"                                                       ";
			sql += "                  where \"AcctCode\" in ('TAV','TCK','TAM','TSL')                         ";
			sql += "                   and \"AcDate\" < :AcDate ";
			sql += "                )                                                                         ";
			sql += "            where  ROWNO = 1                                                              ";
			sql += "            )                                                                             ";
			sql += "      Group by \"CustNo\",\"FacmNo\"                                                      ";
			sql += "     ) a                                                                                  ";
			sql += "left join \"CustMain\" b on b.\"CustNo\"= a.\"CustNo\"                                    ";
			sql += "where \"TdCkBal\" <> \"YdCkBal\"                                                          ";
			sql += "   or \"TdAvBal\" <> \"YdAvBal\"                                                          ";
			sql += "   order by \"CustNo\",\"FacmNo\"                                                         ";
		}

		// 與會計日期同一日，使用會計銷帳檔(今日)與每日暫收款餘額檔(昨日)計算暫收款
		if (decision == 2) {
			sql += "select                                                                                    ";
			sql += " :AcDate as \"AcDate\"                                                                    ";
			sql += ",LPAD(a.\"CustNo\",7,0)  as \"CustNo\"                                                    ";
			sql += ",b.\"CustName\"                                                                           ";
			sql += ",a.\"FacmNo\"                                                                             ";
			sql += ",case when \"TdCkBal\" = \"YdCkBal\" then 0 else \"TdCkBal\" end as \"TdCkBal\"           ";
			sql += ",case when \"TdAvBal\" = \"YdAvBal\" then 0 else \"TdAvBal\" end as \"TdAvBal\"           ";
			sql += ",case when \"TdCkBal\" = \"YdCkBal\" then 0 else \"YdCkBal\" end as \"YdCkBal\"           ";
			sql += ",case when \"TdAvBal\" = \"YdAvBal\" then 0 else \"YdAvBal\" end as \"YdAvBal\"           ";
			sql += "from (                                                                                    ";
			sql += "      select                                                                              ";
			sql += "       \"CustNo\"                                                                         ";
			sql += "      ,\"FacmNo\"                                                                         ";
			sql += "      ,sum(\"TdCkBal\") as \"TdCkBal\"                                                    ";
			sql += "      ,sum(\"TdAvBal\") as \"TdAvBal\"                                                    ";
			sql += "      ,sum(\"YdCkBal\") as \"YdCkBal\"                                                    ";
			sql += "      ,sum(\"YdAvBal\") as \"YdAvBal\"                                                    ";
			sql += "      from (                                                                              ";
			sql += "            select                                                                        ";
			sql += "             \"CustNo\"                                                                   ";
			sql += "            ,\"FacmNo\"                                                                   ";
			sql += "            ,case when \"AcctCode\" in ('TCK') then \"RvBal\" else 0 end  AS \"TdCkBal\"  ";
			sql += "            ,case when \"AcctCode\" in ('TCK') then  0 else \"RvBal\" end AS \"TdAvBal\"  ";
			sql += "            ,0 AS  \"YdCkBal\"                                                            ";
			sql += "            ,0 AS  \"YdAvBal\"                                                            ";
			sql += "            from \"AcReceivable\"                                                         ";
			sql += "            where \"AcctCode\" in ('TAV','TCK','TAM','TSL')                               ";
			sql += "             and \"RvBal\" > 0                                                            ";
			sql += "            union all                                                                     ";
			sql += "            select                                                                        ";
			sql += "             \"CustNo\"                                                                   ";
			sql += "            ,\"FacmNo\"                                                                   ";
			sql += "            ,0 AS  \"TdCkBal\"                                                            ";
			sql += "            ,0 AS  \"TdAvBal\"                                                            ";
			sql += "            ,case when \"AcctCode\" in ('TCK') then \"TavBal\" else 0 end  AS \"YdCkBal\" ";
			sql += "            ,case when \"AcctCode\" in ('TCK') then  0 else \"TavBal\" end AS \"YdAvBal\" ";
			sql += "            from (                                                                        ";
			sql += "                  select                                                                  ";
			sql += "                   \"CustNo\"                                                             ";
			sql += "                  ,\"FacmNo\"                                                             ";
			sql += "                  ,\"AcctCode\"                                                           ";
			sql += "                  ,\"TavBal\"                                                             ";
			sql += "                  ,ROW_NUMBER() Over (Partition By \"CustNo\", \"FacmNo\", \"AcctCode\"   ";
			sql += "                                          Order By \"AcDate\" DESC)  as ROWNO             ";
			sql += "                  from \"DailyTav\"                                                       ";
			sql += "                  where \"AcctCode\" in ('TAV','TCK','TAM','TSL')                         ";
			sql += "                   and \"AcDate\" < :AcDate                                               ";
			sql += "                )                                                                         ";
			sql += "            where  ROWNO = 1                                                              ";
			sql += "            )                                                                             ";
			sql += "      Group by \"CustNo\",\"FacmNo\"                                                      ";
			sql += "     ) a                                                                                  ";
			sql += "left join \"CustMain\" b on b.\"CustNo\"= a.\"CustNo\"                                    ";
			sql += "where \"TdCkBal\" <> \"YdCkBal\"                                                          ";
			sql += "   or \"TdAvBal\" <> \"YdAvBal\"                                                          ";
			sql += "   order by \"CustNo\",\"FacmNo\"                                                         ";
		}
		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("AcDate", runDate);

		return this.convertToMap(query);
	}

}