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

@Service("l4450ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L4450ServiceImpl extends ASpringJpaParm implements InitializingBean {

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
		String iAchSpecificDdFrom = "0";
		String iAchSpecificDdTo = "0";
		String iAchSecondSpecificDdFrom = "0";
		String iAchSecondSpecificDdTo = "0";
		String iPostSpecificDd = "0";
		String iPostSecondSpecificDd = "0";
		int iDeductDate = 0; // 追繳
		int nextPayIntDate = 0; //
		int iOpItem = Integer.parseInt(titaVo.getParam("OpItem"));

		if (Integer.parseInt(titaVo.getParam("AchSpecificDdFrom").trim()) > 0) {
			iAchSpecificDdFrom = titaVo.getParam("AchSpecificDdFrom").trim().substring(5, 7);
		}
		if (Integer.parseInt(titaVo.getParam("AchSpecificDdTo").trim()) > 0) {
			iAchSpecificDdTo = titaVo.getParam("AchSpecificDdTo").trim().substring(5, 7);
		}
		if (Integer.parseInt(titaVo.getParam("AchSecondSpecificDdFrom").trim()) > 0) {
			iAchSecondSpecificDdFrom = titaVo.getParam("AchSecondSpecificDdFrom").trim().substring(5, 7);
		}
		if (Integer.parseInt(titaVo.getParam("AchSecondSpecificDdTo").trim()) > 0) {
			iAchSecondSpecificDdTo = titaVo.getParam("AchSecondSpecificDdTo").trim().substring(5, 7);
		}
		if (Integer.parseInt(titaVo.getParam("PostSpecificDd").trim()) > 0) {
			iPostSpecificDd = titaVo.getParam("PostSpecificDd").trim().substring(5, 7);
		}
		if (Integer.parseInt(titaVo.getParam("PostSecondSpecificDd").trim()) > 0) {
			iPostSecondSpecificDd = titaVo.getParam("PostSecondSpecificDd").trim().substring(5, 7);
		}
		if (Integer.parseInt(titaVo.getParam("DeductDate").trim()) > 0) {
			iDeductDate = Integer.parseInt(titaVo.getParam("DeductDate").trim()) + 19110000;
		}

//		僅輸入單一項目時需分開計算
		if (Integer.parseInt(titaVo.getParam("AchSpecificDdTo").trim()) > 0) {
			nextPayIntDate = Integer.parseInt(titaVo.getParam("AchSpecificDdTo").trim()) + 19110000;
		} else if (Integer.parseInt(titaVo.getParam("PostSpecificDd").trim()) > 0) {
			nextPayIntDate = Integer.parseInt(titaVo.getParam("PostSpecificDd").trim()) + 19110000;
		} else if (Integer.parseInt(titaVo.getParam("PostSecondSpecificDd").trim()) > 0) {
			nextPayIntDate = Integer.parseInt(titaVo.getParam("PostSecondSpecificDd").trim()) + 19110000;
		}

//		20210412 b.NextPayIntDate <= nextPayIntDate edited

		String sql = "  select                                                           ";
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
		sql += "  from \"LoanBorMain\" b                                                 ";
		sql += "  left join \"FacMain\" f on f.\"CustNo\" = b.\"CustNo\"                 ";
		sql += "                      and f.\"FacmNo\" = b.\"FacmNo\"                    ";
		sql += "  left join \"BankAuthAct\" ba on ba.\"CustNo\" = b.\"CustNo\"           ";
		sql += "                      and ba.\"FacmNo\" = b.\"FacmNo\"                   ";
		sql += "                  and ba.\"AuthType\" in ('00','01')                     ";
		sql += "  left join (                                                            ";
		sql += "   select                                                                ";
		sql += "    \"CustNo\"                                                           ";
		sql += "   ,\"AuthCode\"                                                         ";
		sql += "   ,\"PostDepCode\"                                                      ";
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
		sql += "  where b.\"Status\"= 0                                    ";
		sql += "    and b.\"NextPayIntDate\" <= " + nextPayIntDate;
//		追加逾期
		sql += "    and b.\"NextPayIntDate\" >= " + iDeductDate;
		sql += "    and f.\"RepayCode\" = 2                                ";
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
		if (iOpItem == 1) {
			sql += "    and nvl(ba.\"RepayBank\",'000') not in ('000','700') ";
		} else if (iOpItem == 2) {
			sql += "    and nvl(ba.\"RepayBank\",'000') in ('700')           ";
		} else {
			sql += "    and nvl(ba.\"RepayBank\",'000') not in ('000')       ";
		}
		sql += "    and case                                               ";
		sql += "         when ba.\"RepayBank\" = 700                      ";
		sql += "           then case                                        ";
		sql += "                 when substr(b.\"NextPayIntDate\",-2,2) IN ( '" + iPostSpecificDd + "' , '"
				+ iPostSecondSpecificDd + "') ";
		sql += "                 then 1                                    ";
		sql += "                    else 0                                 ";
		sql += "               end                                         ";
		sql += "         else case                                          ";
		sql += "               when substr(b.\"NextPayIntDate\",-2,2) between " + iAchSpecificDdFrom + " and "
				+ iAchSpecificDdTo;
		sql += "               then 1                                      ";
		sql += "               when substr(b.\"NextPayIntDate\",-2,2) between " + iAchSecondSpecificDdFrom + " and "
				+ iAchSecondSpecificDdTo;
		sql += "               then 1                                      ";
		sql += "                  else 0                                   ";
		sql += "             end                                           ";
		sql += "        end = 1                                           ";
		sql += "   order by \"F4\",\"F3\",\"F0\",\"F1\",\"F2\"                              ";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		return this.convertToMap(query.getResultList());
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