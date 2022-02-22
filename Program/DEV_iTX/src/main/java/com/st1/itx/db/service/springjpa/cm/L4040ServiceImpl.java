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

@Service("L4040ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L4040ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Autowired
	private Parse parse;

	// *** 折返控制相關 ***
	private int index;

	// *** 折返控制相關 ***
	private int limit;

	// *** 折返控制相關 ***
	private int cnt;

	// *** 折返控制相關 ***
	private int size;

	private int propDate;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		this.info("L4040.findAll");

		String sql = "";
		String searchstatus = "";
		String searchMediaCode = "";
		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		int iPropDate = parse.stringToInteger(titaVo.getParam("PropDate"));
		int iFunctionCode = parse.stringToInteger(titaVo.getParam("FunctionCode"));
		int iCreateFlag = parse.stringToInteger(titaVo.getParam("CreateFlag"));

		if (iPropDate > 0) {
			iPropDate = iPropDate + 19110000;
		}

		// iFunctionCode 1.篩選資料 2.產出媒體 3.重製媒體碼
		// iCreateFlag 1.新增授權 2.再次授權 3.取消授權
		switch (iFunctionCode) {
		case 1: // 篩選資料
			switch (iCreateFlag) {
			case 1: // 新增授權
				searchstatus = " in (' ')";
				searchMediaCode = " is null ";
				break;
			case 2: // 再次授權
				searchstatus = " not in (' ','0')";
				searchMediaCode = " = 'Y' ";
				break;
			case 3: // 取消授權
				searchstatus = " in '0'";
				searchMediaCode = " = 'Y' ";
				break;
			}
			break;
		case 2:// 產出媒體
			searchstatus = " in (' ')";
			searchMediaCode = " is null ";
			break;

		case 3:// 重製媒體碼
			searchstatus = " in (' ')";
			searchMediaCode = " = 'Y' ";
			break;
		}
		this.info("searchstatus = " + searchstatus);
		this.info("searchMediaCode = " + searchMediaCode);
		this.info("iCustNo = " + iCustNo);
		this.info("iPropDate = " + iPropDate);
		this.info("iFunctionCode = " + iFunctionCode);
		this.info("iCreateFlag = " + iCreateFlag);

		sql += " select                                     ";
		sql += "    c.\"CustId\"               as F0        ";
		sql += "  , a.\"AuthCreateDate\"       as F1        ";
		sql += "  , a.\"CustNo\"               as F2        ";
		sql += "  , a.\"RepayBank\"            as F3        ";
		sql += "  , a.\"RepayAcct\"            as F4        ";
		sql += "  , a.\"CreateFlag\"           as F5        ";
		sql += "  , a.\"FacmNo\"               as F6        ";
		sql += "  , a.\"ProcessDate\"          as F7        ";
		sql += "  , a.\"StampFinishDate\"      as F8        ";
		sql += "  , a.\"AuthStatus\"           as F9        ";
		sql += "  , a.\"AuthMeth\"             as F10       ";
		sql += "  , a.\"LimitAmt\"             as F11       ";
		sql += "  , a.\"MediaCode\"            as F12       ";
		sql += "  , a.\"BatchNo\"              as F13       ";
		sql += "  , a.\"PropDate\"             as F14       ";
		sql += "  , a.\"RetrDate\"             as F15       ";
		sql += "  , a.\"DeleteDate\"           as F16       ";
		sql += "  , a.\"RelationCode\"         as F17       ";
		sql += "  , a.\"RelAcctName\"          as F18       ";
		sql += "  , a.\"RelationId\"           as F19       ";
		sql += "  , a.\"RelAcctBirthday\"      as F20       ";
		sql += "  , a.\"RelAcctGender\"        as F21       ";
		sql += "  , a.\"AmlRsp\"               as F22       ";
		sql += " from (                                     ";
		sql += " select                                     ";
		sql += "   \"AuthCreateDate\"                       ";
		sql += " , \"CustNo\"                               ";
		sql += " , \"RepayBank\"                            ";
		sql += " , \"RepayAcct\"                            ";
		sql += " , \"CreateFlag\"                           ";
		sql += " , \"FacmNo\"                               ";
		sql += " , \"ProcessDate\"                          ";
		sql += " , \"StampFinishDate\"                      ";
		sql += " , \"AuthStatus\"                           ";
		sql += " , \"AuthMeth\"                             ";
		sql += " , \"LimitAmt\"                             ";
		sql += " , \"MediaCode\"                            ";
		sql += " , \"BatchNo\"                              ";
		sql += " , \"PropDate\"                             ";
		sql += " , \"RetrDate\"                             ";
		sql += " , \"DeleteDate\"                           ";
		sql += " , \"RelationCode\"                         ";
		sql += " , \"RelAcctName\"                          ";
		sql += " , \"RelationId\"                           ";
		sql += " , \"RelAcctBirthday\"                      ";
		sql += " , \"RelAcctGender\"                        ";
		sql += " , \"AmlRsp\"                               ";
//		取消時須看到其他帳號
		sql += " ,row_number() over (partition by \"CustNo\",\"RepayBank\",\"RepayAcct\"  order by \"CreateDate\" Desc) as seq  ";
		sql += " from \"AchAuthLog\"                        ";
		sql += " ) a                                        ";
		sql += " left join \"CustMain\" c on c.\"CustNo\" = a.\"CustNo\" ";
		sql += " where a.seq = 1                            ";
		sql += "   and  nvl(a.\"AuthStatus\", ' ') " + searchstatus;
		// iFunctionCode 1.篩選資料 2.產出媒體 3.重製媒體碼
		// iCreateFlag 1.新增授權 2.再次授權 3.取消授權
		switch (iFunctionCode) {
		case 1:
			sql += "   and a.\"MediaCode\" " + searchMediaCode;
			if (iCustNo > 0) {
				sql += "   and a.\"CustNo\" = " + iCustNo;
			}
			if (iPropDate > 0) {
				sql += "   and a.\"PropDate\" >= " + iPropDate;
			}
			if (iPropDate == 0 && iCustNo == 0) {
				sql += "   and a.\"PropDate\" = 0 " ;
			}
			if (iCreateFlag == 3) {
				sql += "   and a.\"CreateFlag\" = 'A'";
			}
			break;
		case 2:
			sql += "   and a.\"MediaCode\" " + searchMediaCode;
			break;
		case 3:
			sql += "   and a.\"PropDate\" = " + propDate;
			sql += "   and a.\"MediaCode\" " + searchMediaCode;
			break;
		}
//		排序用
		if (iFunctionCode == 2) {
			sql += "   order by a.\"AuthCreateDate\",a.\"CustNo\",a.\"RepayAcct\",a.\"CreateFlag\" DESC ";
		}

//		 篩選               -> 輸入條件
//		 產出媒體       -> 提出日+狀態s
//		 重製媒體碼   -> 提出日+狀態+媒體碼

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);

		cnt = query.getResultList().size();
		this.info("Total cnt ..." + cnt);

		// *** 折返控制相關 ***
		// 設定從第幾筆開始抓,需在createNativeQuery後設定
		query.setFirstResult(this.index * this.limit);

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);

		List<Object> result = query.getResultList();

		size = result.size();
		this.info("Total size ..." + size);

		return this.convertToMap(result);
	}

	public List<Map<String, String>> findAll(int nPropDate, int index, int limit, TitaVo titaVo) throws Exception {
		this.index = index;
		this.limit = limit;

		propDate = nPropDate;

		return findAll(titaVo);
	}

	public int getSize() {
		return cnt;
	}
}