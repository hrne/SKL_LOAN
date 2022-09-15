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

@Service("lC011ServiceImpl")
@Repository
public class LC011ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
		// 創建程式碼後,檢查初始值
		// org.junit.Assert.assertNotNull(sPfItDetailService);
	}

	// *** 折返控制相關 ***
	private int index;

	// *** 折返控制相關 ***
	private int limit;

	private String sqlRow = "OFFSET :ThisIndex * :ThisLimit ROWS FETCH NEXT :ThisLimit ROW ONLY ";

//	public String FindLC011(String FunctionCd, String PerfDateFm, String PerfDateTo, String CustNo, String FacmNo) throws Exception {
	public List<Map<String, String>> FindData(TitaVo titaVo, int index, int limit) throws Exception {
		this.info("Active LC011FindData");

		int iEntdySt = Integer.valueOf(titaVo.get("iEntdySt").trim());
		int iEntdyEd = Integer.valueOf(titaVo.get("iEntdyEd").trim());
		String iCustNo = titaVo.get("iCustNo").trim();
		this.info("iMrKey = " + iCustNo);
		String iMkey = "";
		if (!iCustNo.equals("0000000")) {
			iMkey = iCustNo + "%";
		}
		this.info("iMkey = " + iMkey);
		
		if (iEntdySt > 0) {
			iEntdySt += 19110000;
			iEntdyEd += 19110000;
		}
		int iTxSt = Integer.valueOf(titaVo.get("iTxSt").trim());
		int iTxEd = Integer.valueOf(titaVo.get("iTxEd").trim());
		if (iTxSt > 0) {
			iTxSt += 19110000;
			iTxEd += 19110000;
		}

		int iStatus = Integer.valueOf(titaVo.getParam("iStatus").trim());

		String sql = "SELECT A.*,F.\"FlowMode\",";
		sql += "B.\"TranItem\" ";
		sql += "FROM \"TxRecord\" A ";
		sql += "LEFT JOIN \"TxTranCode\" B ON B.\"TranNo\" = A.\"TranNo\" ";
		sql += "LEFT JOIN \"TxFlow\" F ON F.\"FlowNo\" = A.\"FlowNo\"  ";
		if (iEntdySt > 0) {
			sql += "WHERE A.\"Entdy\" >= :Entdy1 AND A.\"Entdy\" <= :Entdy2 ";
		} else {
			sql += "WHERE A.\"CalDate\" >= :CalDate1 AND A.\"CalDate\" <= :CalDate2 ";
		}

		sql += "AND A.\"BrNo\" = :BrNo ";
		if (!iCustNo.equals("0000000")) {
			sql += "   and A.\"MrKey\" like :iMkey ";
		}
		if (!"".equals(titaVo.getParam("iTlrNo").trim())) {
			sql += "AND A.\"TlrNo\" = :TlrNo ";
		}

		if (!"".equals(titaVo.getParam("iTranNo").trim())) {
			sql += "AND A.\"TranNo\" = :TranNo ";
		}

		if (iStatus == 0 || iStatus == 3 || iStatus == 4 || iStatus == 5) {
			sql += "AND A.\"ActionFg\" = :ActionFg ";
		} else if (iStatus == 1 || iStatus == 2) {
			sql += "AND A.\"Hcode\" = :Hcode ";
		}

		sql += "AND A.\"TxResult\" = 'S' AND substr(A.\"TranNo\",0,2) not in ('L9','L0','LC') ";

		sql += "ORDER BY A.\"CreateDate\" asc ";

		sql += sqlRow;

		this.info("FindLC011 sql=" + sql);

		// *** 折返控制相關 ***
		this.index = index;
		// *** 折返控制相關 ***
		this.limit = limit;

		this.info("LC011Service FindData=" + sql);

		Query query;
//			query = em.createNativeQuery(sql,LC011Vo.class);//進SQL 所以不要用.class (要用.class 就要使用HQL)
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("ThisIndex", index);
		query.setParameter("ThisLimit", limit);

		if (iEntdySt > 0) {
			query.setParameter("Entdy1", iEntdySt);
			query.setParameter("Entdy2", iEntdyEd);
		} else {
			query.setParameter("CalDate1", iTxSt);
			query.setParameter("CalDate2", iTxEd);
		}

		query.setParameter("BrNo", titaVo.getParam("iBrNo"));

		if (!"".equals(titaVo.getParam("iTlrNo").trim())) {
			query.setParameter("TlrNo", titaVo.getParam("iTlrNo").trim());
		}

		if (!"".equals(titaVo.getParam("iTranNo").trim())) {
			query.setParameter("TranNo", titaVo.getParam("iTranNo").trim());
		}
		if (!iCustNo.equals("0000000")) {
			query.setParameter("iMkey", iMkey);
		}
		if (iStatus == 0) {
			query.setParameter("ActionFg", 0);
		} else if (iStatus == 1) {
			query.setParameter("Hcode", 1);
		} else if (iStatus == 2) {
			query.setParameter("Hcode", 2);
		} else if (iStatus == 3) {
			query.setParameter("ActionFg", 1);
		} else if (iStatus == 4) {
			query.setParameter("ActionFg", 2);
		} else if (iStatus == 5) {
			query.setParameter("ActionFg", 3);
		}

		// *** 折返控制相關 ***
		// 設定從第幾筆開始抓,需在createNativeQuery後設定
		// query.setFirstResult(this.index*this.limit);
		query.setFirstResult(0);// 因為已經在語法中下好限制條件(筆數),所以每次都從新查詢即可

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);

		// List<LC011Vo> LC011VoList =this.convertToMap(query.getResultList());

		return this.convertToMap(query);
	}

}