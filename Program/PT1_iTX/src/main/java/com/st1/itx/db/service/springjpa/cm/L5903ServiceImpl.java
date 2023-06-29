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

@Service("L5903ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L5903ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Autowired
	private Parse parse;

	String sql = "";

	// *** 折返控制相關 ***
	private int index;

	// *** 折返控制相關 ***
	private int limit;

	// *** 折返控制相關 ***
	private int cnt;

	// *** 折返控制相關 ***
	private int size;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		int iApplDateFrom = parse.stringToInteger(titaVo.getParam("ApplDateFrom")) + 19110000;
		int iApplDateTo = parse.stringToInteger(titaVo.getParam("ApplDateTo")) + 19110000;
		if (iApplDateTo == 19110000) {//無輸入起訖日時
			iApplDateTo = 99991231;
		}
		String iteller = titaVo.getParam("TLRNO");
		
		String iUsageCode = titaVo.getParam("UsageCode");
		String iApplCode = titaVo.getParam("ApplCode");
//		T(3,01:未還;02:已還;09:全部)

		this.info("L5903.findAll iCustNo=" + iCustNo);
		this.info("L5903.findAll iApplDateFrom=" + iApplDateFrom);
		this.info("L5903.findAll iApplDateTo=" + iApplDateTo);
		this.info("L5903.findAll iUsageCode=" + iUsageCode);
		this.info("L5903.findAll iApplCode=" + iApplCode);

		sql = " select  i.\"CustNo\"                                      ";
		sql += "        ,i.\"FacmNo\"                                     ";
		sql += "        ,i.\"ApplSeq\"                                    ";
		sql += "        ,c.\"CustName\"                                   ";
		sql += "        ,NVL(e1.\"Fullname\",i.\"KeeperEmpNo\")    AS F4  ";
		sql += "        ,NVL(e2.\"Fullname\",i.\"ApplEmpName\")    AS F5  ";
		sql += "        ,i.\"ApplDate\"                                   ";
		sql += "        ,i.\"ReturnDate\"                                 ";
		sql += "        ,NVL(e3.\"Fullname\",i.\"ReturnEmpNo\")    AS F8  ";
		sql += "        ,i.\"UsageCode\"                                  ";
		sql += "        ,i.\"CopyCode\"                                   ";
		sql += "        ,i.\"Remark\"                                     ";
		sql += "        ,i.\"ApplObj\"                                    ";
		sql += "        ,i.\"KeeperEmpNo\"                                ";
		sql += "        ,i.\"ApplEmpNo\"                                  ";
		sql += "        ,i.\"ReturnEmpNo\"                                ";
		sql += "        ,i.\"TitaActFg\"                                  ";
		sql += "        ,i.\"FacmNoMemo\"                                 ";
		sql += "        ,CASE WHEN NVL(cc.\"Enable\",'N') = 'N' THEN 'N'  ";//非管理人則為N不可修改
		sql += "              WHEN NVL(i.\"TitaActFg\",' ') = ' ' THEN 'Y' ";//舊資料無此值則為Y
		sql += "              WHEN i.\"TitaActFg\" = '1' THEN 'N'          ";//未到審查則為N
		sql += "              WHEN i.\"TitaActFg\" = '4'  THEN 'Y'        ";//審查已放行則為Y
		sql += "              WHEN NVL(JSON_VALUE(i.\"JsonFields\", '$.RELCD'), ' ') = '2' "; 
		sql += "                   AND i.\"TitaActFg\" in ('2') THEN 'Y'  ";  // 兩段式已放行則為Y
		sql += "              ELSE 'N'  end                        AS F18 ";
		sql += "        ,case WHEN i.\"TitaEntDy\" > 0 THEN i.\"TitaEntDy\" - 19110000";
		sql += "              ELSE 0  END             AS \"TitaEntDy\"    ";
		sql += "        ,i.\"TitaTlrNo\"                                  ";
		sql += "        ,LPAD(i.\"TitaTxtNo\",8,'0')  AS \"TitaTxtNo\"    ";       
		sql += "        ,i.\"JsonFields\"                                 ";
		sql += "        ,CASE WHEN i.\"ApplCode\" IN ('2') THEN 'N'       "; 
		sql += "              WHEN i.\"TitaTxtNo\" = 0 THEN 'N'       ";
		sql += "              WHEN i.\"TitaActFg\" in ('3','4') THEN 'N'       ";  // 已審核不可修正
		sql += "              WHEN i.\"ApplEmpNo\" <> :iteller THEN 'N'       ";   // 經辦與借閱人相同才可修正
		sql += "              WHEN NVL(JSON_VALUE(i.\"JsonFields\", '$.RELCD'), ' ') = '2' "; 
		sql += "                   AND i.\"TitaActFg\" in ('2') THEN 'N'       ";  // 兩段式已放行不可修正
		sql += "              ELSE 'Y' END            AS  \"ModifyFg\"    ";
		sql += "        ,CASE WHEN i.\"ApplCode\" IN ('2') THEN 'N'       ";
		sql += "              WHEN i.\"TitaTxtNo\" = 0 THEN 'N'       ";
		sql += "              WHEN i.\"TitaActFg\" in ('3','4') THEN 'N'       ";  // 已審核不可訂正
		sql += "              WHEN i.\"ApplEmpNo\" <> :iteller THEN 'N'       ";   // 經辦與借閱人相同才可訂正
		sql += "              ELSE 'Y' END            AS  \"DeleteFg\"    ";
		sql += "        ,CASE WHEN i.\"ApplCode\" IN ('2') THEN 'N'       ";
		sql += "              WHEN i.\"CopyCode\" = '2' THEN 'N'             ";
		sql += "              WHEN i.\"TitaActFg\" in ('1','3') THEN 'N'       ";
		sql += "              WHEN NVL(JSON_VALUE(i.\"JsonFields\", '$.RELCD'), ' ') = '4' ";
		sql += "                   AND i.\"TitaActFg\" in ('2') THEN 'N'       ";
		sql += "              ELSE 'Y' END            AS  \"ReturnFg\"    ";
		sql += "        ,NVL(cc2.\"Enable\",'N')       AS  \"KeeperEnable\" ";//管理人是否啟用中
		
		sql += " from \"InnDocRecord\" i                                  ";
		sql += " left join \"CustMain\" c on c.\"CustNo\" = i.\"CustNo\"  ";
		sql += " left join \"CdEmp\" e1 on e1.\"EmployeeNo\" = i.\"KeeperEmpNo\"  ";
		sql += " left join \"CdEmp\" e2 on e2.\"EmployeeNo\" = i.\"ApplEmpNo\"  ";
		sql += " left join \"CdEmp\" e3 on e3.\"EmployeeNo\" = i.\"ReturnEmpNo\"  ";
		sql += " left join \"CdCode\" cc on cc.\"DefCode\"   = 'InnDocKeeper'  ";
		sql += "                        and cc.\"Code\"      =  :iteller "  ;
		sql += " left join \"CdCode\" cc2 on cc2.\"DefCode\" = 'InnDocKeeper'  ";
		sql += "                         and cc2.\"Code\"    =  i.\"KeeperEmpNo\" "  ;
		sql += " where i.\"ApplDate\" >= " + iApplDateFrom;
		sql += "   and i.\"ApplDate\" <= " + iApplDateTo;

		if (iCustNo != 0) {
			sql += "   and i.\"CustNo\" = " + iCustNo;
		}

		if (!"00".equals(iUsageCode)) {
			sql += "   and i.\"UsageCode\" = " + iUsageCode;
		}

		if (!"09".equals(iApplCode)) {
			if ("01".equals(iApplCode)) {//01:未還
				sql += "   and case when i.\"ApplCode\" = '2' then 0"  ;//已歸還
				sql += "            when i.\"CopyCode\" = '2' then 0"  ;//影本
				sql += "       else 1 end = 1"  ;
				//				sql += " and NVL(i2.\"CustNo\",0) = 0                             ";
			}
			if ("02".equals(iApplCode)) {//01:已還
				sql += "   and case when i.\"ApplCode\" = '2' then 1"  ;
				sql += "            when i.\"CopyCode\" = '2' then 1"  ;
				sql += "       else 0 end = 1"  ;
			}

		}

		sql += " order by i.\"CustNo\",i.\"FacmNo\" ,i.\"ApplSeq\"";
		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);
		query.setParameter("iteller", iteller);

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

		return this.convertToMap(query);
	}

	public List<Map<String, String>> findAll(int index, int limit, TitaVo titaVo) throws Exception {
		this.index = index;
		this.limit = limit;

		return findAll(titaVo);
	}

	public int getSize() {
		return cnt;
	}

}