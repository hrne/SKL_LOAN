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

/**
 * L4450ServiceImpl
 * 
 * @author st1
 *
 */
@Service("l4450ServiceImpl")
@Repository
public class L4450ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
		;
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws LogicException {

		// ACH扣款應繳日起日
		int iAchSpecificDdFrom = 0;

		// ACH扣款應繳日止日
		int iAchSpecificDdTo = 0;

		// ACH二扣應繳日起日
		int iAchSecondSpecificDdFrom = 0;

		// ACH二扣應繳日止日
		int iAchSecondSpecificDdTo = 0;

		// 郵局扣款應繳日
		int iPostSpecificDd = 0;

		// 郵局二扣應繳日
		int iPostSecondSpecificDd = 0;

		// 郵局扣款應繳日的應繳日(2碼)
		int iPostSpecificDay = 0;

		// 郵局二扣應繳日的應繳日(2碼)
		int iPostSecondSpecificDay = 0;

		int iDeductDate = 0; // 追繳
		int iOpItem = Integer.parseInt(titaVo.getParam("OpItem"));

		if (Integer.parseInt(titaVo.getParam("AchSpecificDdFrom")) > 0) {
			iAchSpecificDdFrom = Integer.parseInt(titaVo.getParam("AchSpecificDdFrom")) + 19110000;
		}
		if (Integer.parseInt(titaVo.getParam("AchSpecificDdTo")) > 0) {
			iAchSpecificDdTo = Integer.parseInt(titaVo.getParam("AchSpecificDdTo")) + 19110000;
		}
		if (Integer.parseInt(titaVo.getParam("AchSecondSpecificDdFrom")) > 0) {
			iAchSecondSpecificDdFrom = Integer.parseInt(titaVo.getParam("AchSecondSpecificDdFrom")) + 19110000;
		}
		if (Integer.parseInt(titaVo.getParam("AchSecondSpecificDdTo")) > 0) {
			iAchSecondSpecificDdTo = Integer.parseInt(titaVo.getParam("AchSecondSpecificDdTo")) + 19110000;
		}
		if (Integer.parseInt(titaVo.getParam("PostSpecificDd")) > 0) {
			iPostSpecificDd = Integer.parseInt(titaVo.getParam("PostSpecificDd")) + 19110000;
			iPostSpecificDay = iPostSpecificDd % 100;
		}
		if (Integer.parseInt(titaVo.getParam("PostSecondSpecificDd")) > 0) {
			iPostSecondSpecificDd = Integer.parseInt(titaVo.getParam("PostSecondSpecificDd")) + 19110000;
			iPostSecondSpecificDay = iPostSecondSpecificDd % 100;
		}
		if (Integer.parseInt(titaVo.getParam("DeductDate")) > 0) {
			iDeductDate = Integer.parseInt(titaVo.getParam("DeductDate")) + 19110000;
		}

		String sql = "  select ";
		sql += "  b.\"CustNo\"                                               AS \"CustNo\" ";
		sql += " ,b.\"FacmNo\"                                               AS \"FacmNo\" ";
		sql += " ,b.\"BormNo\"                                               AS \"BormNo\" ";
		sql += " ,NVL(f.\"AcctCode\",' ')                                    AS \"AcctCode\" ";
		sql += " ,NVL(ba.\"RepayBank\",' ')                                  AS \"RepayBank\" ";
		sql += " ,NVL(ba.\"RepayAcct\",' ')                                  AS \"RepayAcct\" ";
		sql += " ,NVL(ba.\"PostDepCode\",' ')                                AS \"PostDepCode\" ";
		sql += " ,NVL(p.\"RelationCode\",NVL(a.\"RelationCode\",' '))        AS \"RelationCode\" ";
		sql += " ,NVL(p.\"RelAcctName\",NVL(a.\"RelAcctName\",' '))          AS \"RelAcctName\" ";
		sql += " ,NVL(p.\"RelationId\",NVL(a.\"RelationId\",' '))            AS \"RelationId\" ";
		sql += " ,NVL(p.\"RelAcctBirthday\",NVL(a.\"RelAcctBirthday\", 0))   AS \"RelAcctBirthday\" ";
		sql += " ,NVL(p.\"RelAcctGender\",NVL(a.\"RelAcctGender\",' '))      AS \"RelAcctGender\" ";
		sql += " ,NVL(ba.\"AcctSeq\",' ')                                    AS \"AcctSeq\" ";
		sql += " ,NVL(ba.\"Status\",' ')                                     AS \"Status\" ";
		sql += "  from \"LoanBorMain\" b ";
		sql += "  left join \"FacMain\" f on f.\"CustNo\" = b.\"CustNo\" ";
		sql += "                      and f.\"FacmNo\" = b.\"FacmNo\" ";
		sql += "  left join \"BankAuthAct\" ba on ba.\"CustNo\" = b.\"CustNo\" ";
		sql += "                      and ba.\"FacmNo\" = b.\"FacmNo\" ";
		sql += "                  and ba.\"AuthType\" in ('00','01') ";
		sql += "  left join ( ";
		sql += "   select ";
		sql += "    \"CustNo\" ";
		sql += "   ,\"AuthCode\" ";
		sql += "   ,\"PostDepCode\" ";
		sql += "   ,\"RepayAcct\" ";
		sql += "   ,\"RelationCode\" ";
		sql += "   ,\"RelAcctName\" ";
		sql += "   ,\"RelationId\" ";
		sql += "   ,\"RelAcctBirthday\" ";
		sql += "   ,\"RelAcctGender\" ";
		sql += "   ,row_number() over (partition by \"CustNo\", \"RepayAcct\" order by \"AuthCreateDate\" Desc) as seq ";
		sql += "   from \"PostAuthLog\") p   on  ba.\"RepayBank\"   = '700' ";
		sql += "                            and  p.\"CustNo\"       = ba.\"CustNo\" ";
		sql += "                            and  p.\"AuthCode\"     = '1' ";
		sql += "                            and  p.\"PostDepCode\"  = ba.\"PostDepCode\" ";
		sql += "                            and  p.\"RepayAcct\"    = ba.\"RepayAcct\" ";
		sql += "                            and  p.seq = 1 ";
		sql += "  left join ( ";
		sql += "   select ";
		sql += "    \"RepayBank\" ";
		sql += "   ,\"CustNo\" ";
		sql += "   ,\"RepayAcct\" ";
		sql += "   ,\"RelationCode\" ";
		sql += "   ,\"RelAcctName\" ";
		sql += "   ,\"RelationId\" ";
		sql += "   ,\"RelAcctBirthday\" ";
		sql += "   ,\"RelAcctGender\" ";
		sql += "   ,row_number() over (partition by \"CustNo\", \"RepayAcct\" order by \"AuthCreateDate\" Desc) as seq ";
		sql += "   from \"AchAuthLog\") a    on  ba.\"RepayBank\"   <> 700               ";
		sql += "                            and  a.\"RepayBank\"    = ba.\"RepayBank\" ";
		sql += "                            and  a.\"CustNo\"       = ba.\"CustNo\"      ";
		sql += "                            and  a.\"RepayAcct\"    = ba.\"RepayAcct\"   ";
		sql += "                            and  a.seq = 1 ";
		sql += "  where b.\"Status\"= 0 ";
//		追加逾期
		sql += "    and b.\"NextPayIntDate\" >= :iDeductDate ";
		sql += "    and f.\"RepayCode\" = 2 ";
		sql += "    and case ";
		sql += "          when b.\"AmortizedCode\" IN (3,4) ";
		sql += "          then ";
		sql += "            case ";
		sql += "              when b.\"DueAmt\" > 0 ";
		sql += "              then 1 ";
		sql += "            else 0 ";
		sql += "            end ";
		sql += "        else 1 ";
		sql += "        end = 1 ";
		if (iOpItem == 1) {
			sql += "    and nvl(ba.\"RepayBank\",'000') not in ('000','700') ";
		} else if (iOpItem == 2) {
			sql += "    and nvl(ba.\"RepayBank\",'000') = '700' ";
		} else {
			sql += "    and nvl(ba.\"RepayBank\",'000') != '000' ";
		}
		sql += "    and case ";
		// RepayBank = 700 = 郵局
		// 條件1: 下繳日(8碼yyyymmdd) <= 郵局扣款應繳日(8碼yyyymmdd)
		// 且 下繳日的應繳日(2碼dd) = 郵局扣款應繳日的應繳日(2碼dd)
		sql += "          when nvl(ba.\"RepayBank\", '000') = '700' ";
		sql += "               and b.\"NextPayIntDate\" <= :iPostSpecificDd ";
		sql += "               and MOD(b.\"NextPayIntDate\",100) = :iPostSpecificDay ";
		sql += "          then 1 ";
		// 條件2: 下繳日(8碼yyyymmdd) <= 郵局扣款應繳日(8碼yyyymmdd)
		// 且 下繳日的應繳日(2碼dd) = 郵局二扣應繳日的應繳日(2碼dd)
		sql += "          when nvl(ba.\"RepayBank\", '000') = '700' ";
		sql += "               and b.\"NextPayIntDate\" <= :iPostSecondSpecificDd ";
		sql += "               and MOD(b.\"NextPayIntDate\",100) = :iPostSecondSpecificDay ";
		sql += "          then 1 ";
		// RepayBank not in (null,700) = ACH
		// 條件1: 下繳日(8碼yyyymmdd) 在 ACH扣款應繳日(8碼yyyymmdd) 的區間
		sql += "          when nvl(ba.\"RepayBank\", '000') not in ('000','700') ";
		sql += "               and b.\"NextPayIntDate\" between :iAchSpecificDdFrom  and :iAchSpecificDdTo ";
		sql += "          then 1 ";
		// 條件2: 下繳日(8碼yyyymmdd) 在 ACH二扣應繳日(8碼yyyymmdd) 的區間
		sql += "          when nvl(ba.\"RepayBank\", '000') not in ('000','700') ";
		sql += "               and b.\"NextPayIntDate\" between :iAchSecondSpecificDdFrom and :iAchSecondSpecificDdTo ";
		sql += "          then 1 ";
		sql += "        else 0 ";
		sql += "        end = 1 ";
		sql += "   order by \"RepayBank\",\"AcctCode\",\"CustNo\",\"FacmNo\",\"BormNo\" ";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("iDeductDate", iDeductDate);

		query.setParameter("iAchSpecificDdFrom", iAchSpecificDdFrom);
		query.setParameter("iAchSpecificDdTo", iAchSpecificDdTo);

		query.setParameter("iAchSecondSpecificDdFrom", iAchSecondSpecificDdFrom);
		query.setParameter("iAchSecondSpecificDdTo", iAchSecondSpecificDdTo);

		query.setParameter("iPostSpecificDd", iPostSpecificDd);
		query.setParameter("iPostSecondSpecificDd", iPostSecondSpecificDd);

		query.setParameter("iPostSpecificDay", iPostSpecificDay);
		query.setParameter("iPostSecondSpecificDay", iPostSecondSpecificDay);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> findSingle(TitaVo titaVo) throws Exception {
		int custNo = Integer.parseInt(titaVo.getParam("CustNo").trim());
		int facmNo = Integer.parseInt(titaVo.getParam("FacmNo").trim());
		int entryDate = Integer.parseInt(titaVo.getParam("EntryDate")) + 19110000;
		int repayType = Integer.parseInt(titaVo.getParam("RepayType")); // 還款類別
		String sql = "  select                                                          ";
		sql += "  b.\"CustNo\"                                               AS \"CustNo\"          ";
		sql += " ,b.\"FacmNo\"                                               AS \"FacmNo\"          ";
		sql += " ,b.\"BormNo\"                                               AS \"BormNo\"          ";
		sql += " ,NVL(f.\"AcctCode\",' ')                                    AS \"AcctCode\"        ";
		sql += " ,NVL(ba.\"RepayBank\",' ')                                  AS \"RepayBank\"       ";
		sql += " ,NVL(ba.\"RepayAcct\",' ')                                  AS \"RepayAcct\"       ";
		sql += " ,NVL(ba.\"PostDepCode\",' ')                                AS \"PostDepCode\"     ";
		sql += " ,NVL(p.\"RelationCode\",NVL(a.\"RelationCode\",' '))        AS \"RelationCode\"    ";
		sql += " ,NVL(p.\"RelAcctName\",NVL(a.\"RelAcctName\",' '))          AS \"RelAcctName\"     ";
		sql += " ,NVL(p.\"RelationId\",NVL(a.\"RelationId\",' '))            AS \"RelationId\"      ";
		sql += " ,NVL(p.\"RelAcctBirthday\",NVL(a.\"RelAcctBirthday\", 0))   AS \"RelAcctBirthday\" ";
		sql += " ,NVL(p.\"RelAcctGender\",NVL(a.\"RelAcctGender\",' '))      AS \"RelAcctGender\"   ";
		sql += " ,NVL(ba.\"AcctSeq\",' ')                                    AS \"AcctSeq\"         ";
		sql += " ,NVL(ba.\"Status\",' ')                                     AS \"Status\"          ";
		sql += " ,NVL(ba.\"LimitAmt\",0)                                     AS \"LimitAmt\"        ";
		sql += "  from \"LoanBorMain\" b                                                 ";
		sql += "  left join \"FacMain\" f on f.\"CustNo\" = b.\"CustNo\"                 ";
		sql += "                      and f.\"FacmNo\" = b.\"FacmNo\"                    ";
		sql += "  left join \"BankAuthAct\" ba on ba.\"CustNo\" = b.\"CustNo\"           ";
		sql += "                      and ba.\"FacmNo\" = b.\"FacmNo\"                   ";
		sql += "                  and ba.\"AuthType\" in ('00','01')                     ";
		sql += "  left join (                                                            ";
		sql += "   select                                                                ";
		sql += "    \"CustNo\"                                                           ";
		sql += "   ,\"AuthCode\"                                                           ";
		sql += "   ,\"PostDepCode\"                                                        ";
		sql += "   ,\"RepayAcct\"                                                        ";
		sql += "   ,\"RelationCode\"                                                     ";
		sql += "   ,\"RelAcctName\"                                                      ";
		sql += "   ,\"RelationId\"                                                       ";
		sql += "   ,\"RelAcctBirthday\"                                                  ";
		sql += "   ,\"RelAcctGender\"                                                    ";
		sql += "   ,row_number() over (partition by \"CustNo\", \"RepayAcct\" order by \"AuthCreateDate\" Desc) as seq ";
		sql += "   from \"PostAuthLog\") p   on  ba.\"RepayBank\"   = 700               ";
		sql += "                            and  p.\"CustNo\"       = ba.\"CustNo\"      ";
		sql += "                            and  p.\"AuthCode\"     = '1'                ";
		sql += "                            and  p.\"PostDepCode\"  = ba.\"PostDepCode\" ";
		sql += "                            and  p.\"RepayAcct\"    = ba.\"RepayAcct\"   ";
		sql += "                            and  p.seq = 1                 ";
		sql += "  left join (                                                            ";
		sql += "   select                                                                ";
		sql += "    \"RepayBank\"                                                        ";
		sql += "   ,\"CustNo\"                                                           ";
		sql += "   ,\"RepayAcct\"                                                        ";
		sql += "   ,\"RelationCode\"                                                     ";
		sql += "   ,\"RelAcctName\"                                                      ";
		sql += "   ,\"RelationId\"                                                       ";
		sql += "   ,\"RelAcctBirthday\"                                                  ";
		sql += "   ,\"RelAcctGender\"                                                    ";
		sql += "   ,row_number() over (partition by \"CustNo\", \"RepayAcct\" order by \"AuthCreateDate\" Desc) as seq ";
		sql += "   from \"AchAuthLog\") a    on  ba.\"RepayBank\"   <> 700               ";
		sql += "                            and  a.\"RepayBank\"    = ba.\"RepayBank\" ";
		sql += "                            and  a.\"CustNo\"       = ba.\"CustNo\"      ";
		sql += "                            and  a.\"RepayAcct\"    = ba.\"RepayAcct\"   ";
		sql += "                            and  a.seq = 1                 ";
		sql += "   where f.\"RepayCode\" = 2                               ";
		sql += "    and nvl(ba.\"RepayBank\",'000') <> '000'               ";
		sql += "    and b.\"CustNo\"= " + custNo;
		if (facmNo > 0) {
			sql += "    and b.\"FacmNo\"= " + facmNo;
		}

		if (repayType == 1) {
			sql += "    and b.\"Status\"= 0                                    ";
			sql += "    and b.\"NextPayIntDate\" <= " + entryDate;
			sql += "    and case                                               ";
			sql += "          when b.\"AmortizedCode\" IN (3,4)                ";
			sql += "          then                                             ";
			sql += "            case                                           ";
			sql += "              when b.\"DueAmt\" > 0                        ";
			sql += "              then 1                                       ";
			sql += "            else 0                                         ";
			sql += "            end                                            ";
			sql += "        else 1                                             ";
			sql += "        end = 1                                            ";
		}

		sql += "   order by \"RepayBank\",\"AcctCode\",\"CustNo\",\"FacmNo\",\"BormNo\"    ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		return this.convertToMap(query);
	}

}