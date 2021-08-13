package com.st1.itx.db.service.springjpa.cm;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.repository.online.LoanBorMainRepository;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;

@Service("bS440ServiceImpl")
@Repository
/* 逾期放款明細 */
public class BS440ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(BS440ServiceImpl.class);

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
		int iDeductDate = 0;
		int nextPayIntDate = 0;

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

		String sql = "  select                                                          ";
		sql += "  b.\"CustNo\"                                               AS F0       ";
		sql += " ,b.\"FacmNo\"                                               AS F1       ";
		sql += " ,b.\"BormNo\"                                               AS F2       ";
		sql += " ,NVL(f.\"AcctCode\",' ')                                    AS F3       ";
		sql += " ,NVL(ba.\"RepayBank\",' ')                                  AS F4       ";
		sql += " ,NVL(ba.\"RepayAcct\",' ')                                  AS F5       ";
		sql += " ,NVL(p.\"PostDepCode\",' ')                                 AS F6       ";
		sql += " ,NVL(p.\"RelationCode\",NVL(a.\"RelationCode\",' '))        AS F7       ";
		sql += " ,NVL(p.\"RelAcctName\",NVL(a.\"RelAcctName\",' '))          AS F8       ";
		sql += " ,NVL(p.\"RelationId\",NVL(a.\"RelationId\",' '))            AS F9       ";
		sql += " ,NVL(p.\"RelAcctBirthday\",NVL(a.\"RelAcctBirthday\", 0))   AS F10      ";
		sql += " ,NVL(p.\"RelAcctGender\",NVL(a.\"RelAcctGender\",' '))      AS F11      ";
		sql += " ,NVL(p.\"RepayAcctSeq\",' ')                                AS F12      ";
		sql += "  from \"LoanBorMain\" b                                                 ";
		sql += "  left join \"FacMain\" f on f.\"CustNo\" = b.\"CustNo\"                 ";
		sql += "                      and f.\"FacmNo\" = b.\"FacmNo\"                    ";
		sql += "  left join (                                                            ";
		sql += "  select                                                                 ";
		sql += "   \"CustNo\"                                                            ";
		sql += "  ,\"FacmNo\"                                                            ";
		sql += "  ,\"RepayBank\"                                                         ";
		sql += "  ,\"PostDepCode\"                                                       ";
		sql += "  ,\"AuthType\"                                                          ";
		sql += "  ,\"RepayAcct\"                                                         ";
		sql += "  ,row_number() over (partition by \"CustNo\", \"FacmNo\" order by \"AcctSeq\" Desc) as seq ";
		sql += "  from \"BankAuthAct\") ba                                               ";
		sql += "                        on ba.\"CustNo\" = b.\"CustNo\"                  ";
		sql += "                      and seq = 1                                        ";
		sql += "                      and case                                           ";
		sql += "                            when ba.\"RepayBank\" = 700                  ";
		sql += "                            then case                                    ";
		sql += "                                   when ba.\"FacmNo\" = 0                ";
		sql += "                                   then b.\"FacmNo\"                     ";
		sql += "                                 else ba.\"FacmNo\"                      ";
		sql += "                                 end                                     ";
		sql += "                          else ba.\"FacmNo\"                             ";
		sql += "                          end = b.\"FacmNo\"                             ";
		sql += "   left join \"PostAuthLog\" p on  ba.\"RepayBank\"   = 700              ";
		sql += "                            and  p.\"CustNo\"       = ba.\"CustNo\"      ";
		sql += "                            and  p.\"PostDepCode\"  = ba.\"PostDepCode\" ";
		sql += "                            and  p.\"AuthCode\"     = substr(ba.\"AuthType\",-1,1) ";
		sql += "                            and  p.\"RepayAcct\"    = ba.\"RepayAcct\"   ";
		sql += "                            and  p.\"AuthApplCode\" = 1                  ";
		sql += "   left join (                                                           ";
		sql += "   select                                                                ";
		sql += "    \"RepayBank\"                                                        ";
		sql += "   ,\"CustNo\"                                                           ";
		sql += "   ,\"FacmNo\"                                                           ";
		sql += "   ,\"RepayAcct\"                                                        ";
		sql += "   ,\"RelationCode\"                                                     ";
		sql += "   ,\"RelAcctName\"                                                      ";
		sql += "   ,\"RelationId\"                                                       ";
		sql += "   ,\"RelAcctBirthday\"                                                  ";
		sql += "   ,\"RelAcctGender\"                                                    ";
		sql += "   ,row_number() over (partition by \"CustNo\", \"RepayAcct\" order by \"AuthCreateDate\" Desc) as seq ";
		sql += "   from \"AchAuthLog\") a      on  a.\"RepayBank\"    = ba.\"RepayBank\" ";
		sql += "                            and  a.\"CustNo\"       = ba.\"CustNo\"      ";
		sql += "                            and  a.\"RepayAcct\"    = ba.\"RepayAcct\"   ";
		sql += "                            and  a.seq = 1                 ";
		sql += "  where b.\"Status\"= 0                                    ";
		sql += "    and b.\"NextPayIntDate\" <= " + nextPayIntDate;
//		追加逾期
		sql += "    and b.\"NextPayIntDate\" >= " + iDeductDate;
		sql += "    and b.\"AmortizedCode\" <> 5                           ";
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
		sql += "    and case                                               ";
		sql += "          when ba.\"RepayBank\" = 700                      ";
		sql += "          then case                                        ";
		sql += "                 when substr(b.\"NextPayIntDate\",-2,2) IN ( '" + iPostSpecificDd + "' , '"
				+ iPostSecondSpecificDd + "') ";
		sql += "                 then 1                                    ";
//		sql	+="               else case                                   ";
//		sql	+="                      when " + iDeductDate + " > 0 then    ";
//		sql	+="                        case                               ";
//		sql	+="                          when b.\"NextPayIntDate\" <= " + iDeductDate;
//		sql	+="                          then 1                           ";
//		sql	+="                        else 0                             ";
//		sql	+="                        end                                ";
		sql += "                    else 0                                 ";
//		sql	+="                    end                                    ";
		sql += "               end                                         ";
		sql += "        else case                                          ";
		sql += "               when substr(b.\"NextPayIntDate\",-2,2) between " + iAchSpecificDdFrom + " and "
				+ iAchSpecificDdTo;
		sql += "               then 1                                      ";
		sql += "               when substr(b.\"NextPayIntDate\",-2,2) between " + iAchSecondSpecificDdFrom + " and "
				+ iAchSecondSpecificDdTo;
		sql += "               then 1                                      ";
//		sql += "             else case                                     ";
//		sql += "                      when " + iDeductDate + " > 0 then    ";
//		sql += "                      case                                 ";
//		sql += "                        when b.\"NextPayIntDate\" <=  " + iDeductDate;
//		sql += "                        then 1                             ";
//		sql += "                      else 0                               ";
//		sql += "                      end                                  ";
		sql += "                  else 0                                   ";
//		sql += "                  end                                      ";
		sql += "             end                                           ";
		sql += "        end = 1                                           ";
		logger.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		return this.convertToMap(query.getResultList());
	}
}