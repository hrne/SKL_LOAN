package com.st1.itx.db.service.springjpa.cm;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;

@Service
@Repository
public class L9705ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws LogicException {

		this.info("l9705.findAll");

		String acctDateStart = titaVo.getParam("ACCTDATE_ST");
		String acctDateEnd = titaVo.getParam("ACCTDATE_ED");
		String custNoStart = titaVo.getParam("CUSTNO");
		String custNoEnd = titaVo.getParam("CUSTNOB");
		String condition1 = titaVo.getParam("CONDITION1");
		String condition2 = titaVo.getParam("CONDITION2");
		String idType = titaVo.getParam("ID_TYPE");
		String corpInd = titaVo.getParam("CORP_IND");
		String apNo = titaVo.getParam("APNO");
		String dayFg = "";

		this.info("custno1 ..." + custNoStart);
		this.info("custno2 ..." + custNoEnd);
		this.info("corpInd ..." + corpInd);
		this.info("acctDateStart ..." + acctDateStart);
		this.info("acctDateEnd ..." + acctDateEnd);

		String sql = "SELECT DISTINCT M.\"CustNo\"    AS \"CustNo\"    ";
		sql += "            ,M.\"FacmNo\"             AS \"FacmNo\"    ";
		sql += "            ,C.\"CustName\"           AS \"CustName\"  ";
		sql += "            ,F.\"RepayCode\"          AS \"RepayCode\" ";
		sql += "            ,C.\"EntCode\"            AS \"EntCode\"   ";
		if ("4".equals(condition1)) {
			sql += "            ,BATX.\"ReconCode\"            AS \"ReconCode\"   ";
		}
		sql += "      FROM \"LoanBorMain\" M";
		sql += "      LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = M.\"CustNo\"";
		sql += "                             AND F.\"FacmNo\" = M.\"FacmNo\"";
		sql += "      LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = M.\"CustNo\"";
		if ("2".equals(condition1)) {
			sql += " left join (                                  ";
			sql += "      select                                  ";
			sql += "       \"CustNo\"                             ";
			sql += "      ,\"FacmNo\"                             ";
			sql += "      ,\"BormNo\"                             ";
			sql += "      from \"LoanBorTx\"                      ";
			sql += "      where \"TitaTxCd\" in ('L3711','L3712') ";
			sql += "        and \"TitaHCode\" = 0                 ";
			sql += "        and \"AcDate\" >= :sday            ";
			sql += "        and \"AcDate\" <= :eday            ";
			sql += " ) LBT on LBT.\"CustNo\" = M.\"CustNo\"       ";
			sql += "      and LBT.\"FacmNo\" = M.\"FacmNo\"       ";
			sql += "      and LBT.\"BormNo\" = M.\"BormNo\"       ";
		}
		if ("3".equals(condition1)) {
			sql += " left join (                                  ";
			sql += "      select                                  ";
			sql += "       \"CustNo\"                             ";
			sql += "      ,\"FacmNo\"                             ";
			sql += "      ,\"BormNo\"                             ";
			sql += "      from \"LoanRateChange\"                 ";
			sql += "      where \"EffectDate\" >= :sday           ";
			sql += "        and \"EffectDate\" <= :eday           ";
			sql += "        and \"Status\" = 1                    ";
			sql += " ) LRC on LRC.\"CustNo\" = M.\"CustNo\"       ";
			sql += "      and LRC.\"FacmNo\" = M.\"FacmNo\"       ";
			sql += "      and LRC.\"BormNo\" = M.\"BormNo\"       ";
		}
		if ("4".equals(condition1)) {
			sql += " left join (                                  ";
			sql += "      select                                  ";
			sql += "       \"CustNo\"                             ";
			sql += "      ,\"FacmNo\"                             ";
			sql += "      ,\"BormNo\"                             ";
			sql += "      ,\"TitaTxtNo\"                             ";
			sql += "      from \"LoanBorTx\"                      ";
			sql += "      where \"TitaTxCd\" in ('L3200')         ";
			sql += "        and \"TitaHCode\" = 0                 ";
			sql += "        and \"ExtraRepay\" > 0                ";
			sql += "        and \"AcDate\" >= :sday            ";
			sql += "        and \"AcDate\" <= :eday            ";
			sql += " ) LBT on LBT.\"CustNo\" = M.\"CustNo\"       ";
			sql += "      and LBT.\"FacmNo\" = M.\"FacmNo\"       ";
			sql += "      and LBT.\"BormNo\" = M.\"BormNo\"       ";
			sql += " left join \"BatxDetail\" BATX";
			sql += " 		on BATX.\"TitaTxtNo\" = LBT.\"TitaTxtNo\"";
		}
		sql += "      WHERE F.\"DepartmentCode\" = :corpInd ";
		if (Integer.valueOf(custNoStart) > 0) {
			sql += "        AND M.\"CustNo\" >= :custno1 ";
			sql += "        AND M.\"CustNo\" <= :custno2 ";
		}

		switch (condition1) {
		case "1": // 新貸戶
			sql += "    AND M.\"DrawdownDate\" >= :sday ";
			sql += "    AND M.\"DrawdownDate\" <= :eday ";
			dayFg = "1";
			break;
		case "2": // 應繳日變更
		case "3": // 利率變動
		case "4": // 部份還款
			sql += "    AND M.\"CustNo\" > 0 ";
			sql += "    AND LBT.\"CustNo\" IS NOT NULL";
			dayFg = "1";
			break;
		case "5": // 償還方式變更
			break;
		default:
			break;
		}

		switch (condition2) {
		// 匯款支票不印
		case "1":
			sql += "    AND F.\"RepayCode\" NOT IN (1, 4)";
			break;
		// 銀扣不印
		case "2":
			sql += "    AND F.\"RepayCode\" != 2";
			break;
		default:
			break;
		}

		switch (apNo) {
		// 310 短期擔保放款
		case "1":
			sql += "    AND F.\"AcctCode\" = '310'";
			break;
		// 320 中期擔保放款
		case "2":
			sql += "    AND F.\"AcctCode\" = '320'";
			break;
		// 330 長期擔保放款
		case "3":
			sql += "    AND F.\"AcctCode\" = '330'";
			break;
		// 340 三十年房貸
		case "4":
			sql += "    AND F.\"AcctCode\" = '340'";
			break;
		default:
			break;
		}

		switch (idType) {
		case "1": // 戶別 1.自然人
			sql += "    AND C.\"EntCode\" IN ('0', '2')";
			break;
		case "2": // 戶別 2.法人
			sql += "    AND C.\"EntCode\" = '1'";
			break;
		default:
			break;
		}
		if ("4".equals(condition1)) {
			sql += " order by DECODE(BATX.\"ReconCode\",'A3','0',BATX.\"ReconCode\"),M.\"CustNo\", M.\"FacmNo\"                     ";
		} else {
			sql += " order by M.\"CustNo\", M.\"FacmNo\"                     ";
		}
		/*
		 * SELECT DISTINCT M."FacmNo" , C."CustName" FROM "LoanBorMain" M LEFT JOIN
		 * "FacMain" F ON F."CustNo" = M."CustNo" AND F."FacmNo" = M."FacmNo" LEFT JOIN
		 * "CustMain" C ON C."CustNo" = M."CustNo" WHERE M."CustNo" =
		 * 
		 * 新貸戶 AND M."DrawdownDate" >= AND M."DrawdownDate" <=
		 * 
		 * 應繳日變更 AND M."PrevPayIntDate" >= AND M."PrevPayIntDate" <=
		 * 
		 * 利率變動 AND M."" >= AND M."" <=
		 * 
		 * 部份還款 AND M."PrevRepaidDate" >= AND M."PrevRepaidDate" <=
		 * 
		 * 償還方式變更 AND M."" >= AND M."" <=
		 * 
		 * 匯款支票不印 F."RepayCode" NOT IN (1, 4)
		 * 
		 * 銀扣不印 F."RepayCode" != 2
		 * 
		 * 戶別 1.自然人 AND C."EntCode" IN ('0', '2') 戶別 2.自然人 AND C."EntCode" = '1'
		 * 
		 * 企金別 F."DepartmentCode" =
		 * 
		 * 業務科目 F."AcctCode" =
		 * 
		 */

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		if ("2".equals(condition1)) {
			query.setParameter("sday", acctDateStart);
			query.setParameter("eday", acctDateEnd);
		}

		if ("3".equals(condition1)) {
			query.setParameter("sday", acctDateStart);
			query.setParameter("eday", acctDateEnd);
		}
		if ("4".equals(condition1)) {
			query.setParameter("sday", acctDateStart);
			query.setParameter("eday", acctDateEnd);
		}
		if (Integer.valueOf(custNoStart) > 0) {
			query.setParameter("custno1", custNoStart);
			query.setParameter("custno2", custNoEnd);
		}

		switch (condition1) {
		case "1": // 新貸戶
			query.setParameter("sday", acctDateStart);
			query.setParameter("eday", acctDateEnd);
			break;
		case "2": // 應繳日變更
		case "3": // 利率變動
		case "4": // 部份還款
			break;
		case "5": // 償還方式變更
			break;
		default:
			break;
		}

		query.setParameter("corpInd", corpInd);

		if (dayFg.equals("1")) {
			query.setParameter("sday", acctDateStart);
			query.setParameter("eday", acctDateEnd);
		}

		return this.convertToMap(query);
	}

}