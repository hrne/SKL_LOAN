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
import com.st1.itx.eum.ContentName;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L4041ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L4041ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(L4041ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Autowired
	private Parse parse;

	@Autowired
	private DateUtil dateUtil;

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

		logger.info("L4041.findAll");

		String sql = "";
		String status = "";
		String searchMediaCode = "";
		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		int iPropDate = parse.stringToInteger(titaVo.getParam("PropDate"));
		int iFunctionCode = parse.stringToInteger(titaVo.getParam("FunctionCode"));
		int iAuthApplCode = parse.stringToInteger(titaVo.getParam("AuthApplCode"));

		if (iPropDate > 0) {
			iPropDate = iPropDate + 19110000;
		}

		switch (iFunctionCode) {
		// 1. 篩選 2.重新授權 3.取消
		case 1:
			switch (iAuthApplCode) {
			case 1:
				status = "' '";
				searchMediaCode = " is null ";
				break;
			case 2:
				status = "'03','06','07','08','09','10','11','12','13','14','16','17','18','19','91','98'";
				searchMediaCode = " = 'Y' ";
				break;
			case 3:
				status = "'00'";
				searchMediaCode = " = 'Y' ";
				break;
			}
			break;
		// 產出媒體
		case 2:
			status = "' ','03','06','07','08','09','10','11','12','13','14','16','17','18','19','91','98'";
			break;
		// 重製媒體碼
		case 3:
			status = "' '";
			searchMediaCode = " = 'Y' ";
			break;
		}
		logger.info("status = " + status);
		logger.info("searchMediaCode = " + searchMediaCode);
		logger.info("iCustNo = " + iCustNo);
		logger.info("iPropDate = " + iPropDate);
		logger.info("iFunctionCode = " + iFunctionCode);
		logger.info("iAuthApplCode = " + iAuthApplCode);

		sql += " select                         ";
		sql += "   \"AuthCreateDate\"   as F0   ";
		sql += " , \"AuthApplCode\"     as F1   ";
		sql += " , \"CustNo\"           as F2   ";
		sql += " , \"PostDepCode\"      as F3   ";
		sql += " , \"RepayAcct\"        as F4   ";
		sql += " , \"AuthCode\"         as F5   ";
		sql += " , \"FacmNo\"           as F6   ";
		sql += " , \"CustId\"           as F7   ";
		sql += " , \"RepayAcctSeq\"     as F8   ";
		sql += " , \"ProcessDate\"      as F9   ";
		sql += " , \"StampFinishDate\"  as F10  ";
		sql += " , \"StampCancelDate\"  as F11  ";
		sql += " , \"StampCode\"        as F12  ";
		sql += " , \"PostMediaCode\"    as F13  ";
		sql += " , \"AuthErrorCode\"    as F14  ";
		sql += " , \"FileSeq\"          as F15  ";
		sql += " , \"PropDate\"         as F16  ";
		sql += " , \"RetrDate\"         as F17  ";
		sql += " , \"DeleteDate\"       as F18  ";
		sql += " , \"RelationCode\"     as F19  ";
		sql += " , \"RelAcctName\"      as F20  ";
		sql += " , \"RelationId\"       as F21  ";
		sql += " , \"RelAcctBirthday\"  as F22  ";
		sql += " , \"RelAcctGender\"    as F23  ";
		sql += " , \"AmlRsp\"           as F24  ";
		sql += " from (                         ";
		sql += "     select                     ";
		sql += "   \"AuthCreateDate\"           ";
		sql += " , \"AuthApplCode\"             ";
		sql += " , \"CustNo\"                   ";
		sql += " , \"PostDepCode\"              ";
		sql += " , \"RepayAcct\"                ";
		sql += " , \"AuthCode\"                 ";
		sql += " , \"FacmNo\"                   ";
		sql += " , \"CustId\"                   ";
		sql += " , \"RepayAcctSeq\"             ";
		sql += " , \"ProcessDate\"              ";
		sql += " , \"StampFinishDate\"          ";
		sql += " , \"StampCancelDate\"          ";
		sql += " , \"StampCode\"                ";
		sql += " , \"PostMediaCode\"            ";
		sql += " , \"AuthErrorCode\"            ";
		sql += " , \"FileSeq\"                  ";
		sql += " , \"PropDate\"                 ";
		sql += " , \"RetrDate\"                 ";
		sql += " , \"DeleteDate\"               ";
		sql += " , \"RelationCode\"             ";
		sql += " , \"RelAcctName\"              ";
		sql += " , \"RelationId\"               ";
		sql += " , \"RelAcctBirthday\"          ";
		sql += " , \"RelAcctGender\"            ";
		sql += " , \"AmlRsp\"                   ";
//		取消時須看到其他帳號
		sql += " ,row_number() over (partition by \"CustNo\",\"RepayAcct\",\"AuthCode\",\"PostDepCode\" order by \"CreateDate\" Desc) as seq         ";
		sql += " from \"PostAuthLog\"           ";
		sql += " ) p                            ";
		sql += " where seq = 1                ";

		sql += "   and \"AuthErrorCode\" in ( " + status + " ) ";
		sql += "   and \"AuthApplCode\" != '9'  ";
		switch (iFunctionCode) {
		case 1:
			sql += "   and \"PostMediaCode\" " + searchMediaCode;
			if (iCustNo > 0) {
				sql += "   and \"CustNo\" = " + iCustNo;
			}
			if (iPropDate > 0) {
				sql += "   and \"PropDate\" <= " + iPropDate;
			}
			if (iPropDate == 0 && iCustNo == 0) {
				sql += "   and \"PropDate\" != " + propDate;
			}
			break;
		case 2:
			sql += "   and \"PropDate\" = " + propDate;
			break;
		case 3:
			sql += "   and \"PropDate\" = " + propDate;
			sql += "   and \"PostMediaCode\" " + searchMediaCode;
			break;
		}

//		排序用
		if (iFunctionCode == 2) {
			sql += "   order by \"AuthCreateDate\",\"AuthApplCode\" Desc,\"CustNo\",\"PostDepCode\",\"RepayAcct\",\"AuthCode\" ";
		}

//		 篩選               -> 輸入條件
//		 產出媒體       -> 提出日+狀態
//		 重製媒體碼   -> 提出日+狀態+媒體碼

		logger.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);

		cnt = query.getResultList().size();
		logger.info("Total cnt ..." + cnt);

		// *** 折返控制相關 ***
		// 設定從第幾筆開始抓,需在createNativeQuery後設定
		query.setFirstResult(this.index * this.limit);

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);

		List<Object> result = query.getResultList();

		size = result.size();
		logger.info("Total size ..." + size);

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