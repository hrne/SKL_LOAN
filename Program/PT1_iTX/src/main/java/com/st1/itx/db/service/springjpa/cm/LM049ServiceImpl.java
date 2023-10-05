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

@Service
@Repository
public class LM049ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(int acDate, TitaVo titaVo) throws Exception {

		this.info("lM049.findAll ");

		String sql = "";

		sql += " with \"Main\" AS ( ";
		sql += "   select case  ";
		sql += "        	when \"CompanyName\" like '新%金%' and length(\"CompanyName\") = 4 then 1 ";
		sql += "        	when \"CompanyName\" like '新%投%' then 4 ";
		sql += "        	when \"CompanyName\" like '新%銀%' then 4 ";
		sql += "        	when \"CompanyName\" like '新%人%' then 4 ";
		sql += "          else 9 end as \"comSeq\"";
		sql += "         ,case";
		sql += "        	when \"BusTitle\" like '%董事%' then 1 ";
		sql += "        	when \"BusTitle\" like '%監事%' then 2 ";
		sql += "        	when \"BusTitle\" like '%總經理%' then 3 ";
		sql += "        	when \"BusTitle\" like '%協理%' then 4 ";
		sql += "        	when \"BusTitle\" like '%經理%' then 5 ";
		sql += "        	when \"BusTitle\" like '%副理%' then 6 ";
		sql += "          else 9 end as \"levSeq\"";
		sql += "        , ROW_NUMBER() ";
		sql += "          OVER ( ";
		sql += "            PARTITION BY \"Id\" ";
		sql += "            ORDER BY case  ";
		sql += "        			   when \"CompanyName\" like '新%金%' and length(\"CompanyName\") = 4 then 1 ";
		sql += "        			   when \"CompanyName\" like '新%投%' then 23 ";
		sql += "        			   when \"CompanyName\" like '新%銀%' then 22 ";
		sql += "        		 	   when \"CompanyName\" like '新%人%' then 21 ";
		sql += "          			else 99 end asc";
		sql += "          )  as \"Seq\" ";
		sql += "        , \"CompanyName\"";
		sql += "        , \"Id\"";
		sql += "        , \"Name\"";
		sql += "        , \"BusTitle\"";
		sql += "        , \"LineAmt\"";
		sql += "        , \"LoanBalance\"";
		sql += "   from \"FinHoldRel\"  ";
		sql += "   where trunc(\"AcDate\" / 100 ) = :yymm ";
		// 有半數以上董事與金控公司或其子公司相同之公司 保留(舜雯人工處理)
//		sql += "   union ";
//		sql += "   select 3 as \"comSeq\"";
//		sql += "         ,1 as \"levSeq\"";
//		sql += "        , 1 as \"Seq\" ";
//		sql += "        , \"BusName\" as \"CompanyName\"";
//		sql += "        , \"BusId\" as \"Id\"";
//		sql += "        , \"BusName\" as \"Name\"";
//		sql += "        , \"BusTitle\"";
//		sql += "        , \"LineAmt\"";
//		sql += "        , \"LoanBalance\"";
//		sql += "   from \"LifeRelHead\"  ";
//		sql += "   where trunc(\"AcDate\" / 100 ) = :yymm ";
//		sql += "     and \"BusName\" is not null ";
//		sql += "     and \"LoanBalance\" > 0";
		sql += " ), \"CFSum\" AS ( ";
		sql += "   SELECT \"CustNo\" ";
		sql += "        , \"FacmNo\" ";
		sql += "        , SUM(NVL(\"OriEvaNotWorth\",0)) AS \"EvaNetWorth\" ";
		sql += "   FROM \"ClFac\"";
		sql += "   WHERE \"MainFlag\" = 'Y' ";
		sql += "   GROUP BY \"CustNo\" ";
		sql += "          , \"FacmNo\" ";
		sql += " ), \"LTV\" AS ( ";
		sql += "   select m.\"Id\" ";
		sql += "        , sum(f.\"LineAmt\") as \"LineAmt\"";
		sql += "        , sum(cs.\"EvaNetWorth\") AS \"EvaNetWorth\" ";
		sql += "   from \"Main\" m";
		sql += "   left join \"CustMain\" c on c.\"CustId\" = m.\"Id\"";
		sql += "   left join \"MonthlyFacBal\" mf on mf.\"CustNo\" = c.\"CustNo\"";
		sql += "   								 and mf.\"YearMonth\" = :yymm ";
		sql += "   								 and mf.\"Status\" = 0 ";
		sql += "   left join \"FacMain\" f on f.\"CustNo\" = mf.\"CustNo\"";
		sql += "   						  and f.\"FacmNo\" = mf.\"FacmNo\" ";
		sql += "   left join \"CFSum\" cs on cs.\"CustNo\" = f.\"CustNo\"";
		sql += "   						 and cs.\"FacmNo\" = f.\"FacmNo\" ";
		sql += "   where m.\"Seq\" = 1 ";
		sql += "     and m.\"comSeq\" in (1,2,3,4)";
		sql += "   group by m.\"Id\"";
		sql += " )";
		sql += "   select m.\"comSeq\"";
		sql += "		, m.\"Id\" ";
		sql += "        , m.\"Name\"";
		sql += "        , m.\"CompanyName\" || '(' || m.\"BusTitle\" || ')' as \"BusName\"";
		sql += "        , f.\"FirstDrawdownDate\"";
		sql += "        , f.\"MaturityDate\"";
		sql += "        , mf.\"StoreRate\"";
		sql += "        , ltv.\"EvaNetWorth\"";
		sql += "        , case ";
		sql += "            when NVL(ltv.\"EvaNetWorth\", 0) = 0";
		sql += "            then 0 ";
		sql += "            else round(ltv.\"LineAmt\" / ltv.\"EvaNetWorth\", 2) ";
		sql += "          end                        AS \"LoanRatio\" "; // --貸款成數
		// -- 先判斷是否有超過一億核貸金額，有的話再判斷核決主管是否為董事(董事會決議)
		sql += "        , case ";
		sql += "            when m.\"LineAmt\" >= 100000000 and lr.\"HeadId\" is not null ";
		sql += "            then 'Y' ";
		sql += "            else 'N' end AS \"upToOneHundredMillion\" ";
		sql += "        , trunc(m.\"LoanBalance\" / 1000 ) as \"LoanBal\"";
		sql += "        , mf.\"FacmNo\"";
		sql += "        , mf.\"CustNo\"";
		sql += "        , cl.\"ClNo\"";
		sql += "   from \"Main\" m";
		sql += "   left join \"CustMain\" c on c.\"CustId\" = m.\"Id\"";
		sql += "   left join \"MonthlyFacBal\" mf on mf.\"CustNo\" = c.\"CustNo\"";
		sql += "   								 and mf.\"YearMonth\" = :yymm ";
		sql += "   								 and mf.\"Status\" = 0 ";
		sql += "   left join \"FacMain\" f on f.\"CustNo\" = mf.\"CustNo\"";
		sql += "   						  and f.\"FacmNo\" = mf.\"FacmNo\" ";
		sql += "   left join \"ClFac\" cl on cl.\"CustNo\" = mf.\"CustNo\"";
		sql += "   						 and cl.\"FacmNo\" = mf.\"FacmNo\" ";
		sql += "   						 and cl.\"MainFlag\" = 'Y' ";
		sql += "   left join \"LTV\" ltv on ltv.\"Id\" = m.\"Id\"";
		sql += "   left join \"CdEmp\" ce on ce.\"EmployeeNo\" = f.\"Supervisor\"";
		sql += "   left join \"LifeRelHead\" lr on lr.\"HeadId\" = substr(ce.\"AgentCode\",1,10)";
		sql += "   where m.\"Seq\" = 1 ";
		sql += "     and m.\"comSeq\" in (1,2,3,4)";
		sql += "   order by m.\"comSeq\" asc , m.\"levSeq\" asc , f.\"CustNo\" asc , f.\"FacmNo\" asc";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yymm", acDate / 100);
		return this.convertToMap(query);
	}

	/*
	 * 查前一季底的淨值(查核數)
	 */
	public List<Map<String, String>> findStockHoldersEqt(int acDate, TitaVo titaVo) throws Exception {

		this.info("lM049.Totalequity acdate=" + acDate);
		String sql = " ";
		sql += "     SELECT \"AvailableFunds\" AS \"AvailableFunds\"";
		sql += "           ,\"AcDate\"  AS \"AcDate\"";
		sql += "     FROM \"InnFundApl\" ";
		sql += "     WHERE \"AcDate\" = (";
		sql += "     	SELECT MAX(\"AcDate\") ";
		sql += "     	FROM \"InnFundApl\" ";
		sql += "     	WHERE \"AcDate\" <= :acdate";
		sql += " 	      AND \"AvailableFunds\" > 0 ";
		sql += "     )";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("acdate", acDate);
		return this.convertToMap(query);

	}

}