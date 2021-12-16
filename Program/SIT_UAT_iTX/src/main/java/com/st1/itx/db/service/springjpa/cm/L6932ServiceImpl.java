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

@Service("l6932ServiceImpl")
@Repository
public class L6932ServiceImpl extends ASpringJpaParm implements InitializingBean {

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

//	public String FindL5051(String FunctionCd, String PerfDateFm, String PerfDateTo, String CustNo, String FacmNo) throws Exception {
	public List<Map<String, String>> FindData(TitaVo titaVo, int index, int limit) throws Exception {
		this.info("L5051FindData");

		int txDate1 = Integer.parseInt(titaVo.getParam("ST_DT").toString());
		int txDate2 = Integer.parseInt(titaVo.getParam("ED_DT").toString());
		int caDate1 = Integer.parseInt(titaVo.getParam("SX_DT").toString());
		int caDate2 = Integer.parseInt(titaVo.getParam("EX_DT").toString());
		String TranNo = titaVo.getParam("TRN_CODE");
		String TranNo2 = titaVo.getParam("TRN_CODE2");
		int CustNo = Integer.parseInt(titaVo.getParam("CUST_NO").toString());
		int FacmNo = Integer.parseInt(titaVo.getParam("FACM_NO").toString());
		int BormNo = Integer.parseInt(titaVo.getParam("BORM_SEQ").toString());
		String iTxtNo = titaVo.getParam("TxtNo").trim();
		String iMrKey = titaVo.getParam("MrKey").trim();

		String sql = "SELECT A.\"TranNo\",";
		sql += "B.\"TranItem\",";
		sql += "A.\"CustNo\",";
		sql += "A.\"FacmNo\",";
		sql += "A.\"BormNo\",";
		sql += "A.\"MrKey\",";
		sql += "A.\"Reason\",";
		sql += "A.\"TxDate\" - 19110000 as \"TxDate\",";
		sql += "A.\"TxSeq\",";
		sql += "A.\"TxSno\",";
		sql += "A.\"LastUpdate\",";
		sql += "A.\"LastUpdateEmpNo\",";
		sql += "C.\"Fullname\" ";
		sql += "FROM \"TxDataLog\" A ";
		sql += "LEFT JOIN \"TxTranCode\" B ON B.\"TranNo\" = A.\"TranNo\" ";
		sql += "LEFT JOIN \"CdEmp\" C ON C.\"EmployeeNo\" = A.\"LastUpdateEmpNo\" ";
		if (txDate1 > 0) {
			sql += "WHERE A.\"TxDate\" >= :TxDate1 AND A.\"TxDate\" <= :TxDate2 ";
		} else {
			sql += "WHERE A.\"LastUpdate\" >= to_date(:TrDate1,'yyyymmdd hh24:mi:ss') AND A.\"LastUpdate\" <= to_date(:TrDate2,'yyyymmdd hh24:mi:ss') ";		
		}
		if (CustNo > 0) {
			sql += "AND A.\"CustNo\" = :CustNo ";
		}
		if (FacmNo > 0) {
			sql += "AND A.\"FacmNo\" = :FacmNo ";
		}
		if (BormNo > 0) {
			sql += "AND A.\"BormNo\" = :BormNo ";
		}
		if (!"".equals(iTxtNo)) {
			sql += "AND A.\"TxSeq\" = :TxSeq ";
		}
		if (!"".equals(TranNo) && !"".equals(TranNo2)) {
			sql += "AND (A.\"TranNo\" = :TranNo1 OR A.\"TranNo\" = :TranNo2) ";
		} else if (!"".equals(TranNo)) {
			sql += "AND A.\"TranNo\" = :TranNo ";
		}
		
		if (!"".equals(iMrKey)) {
			sql += "AND A.\"MrKey\" = :Mrkey ";
		}
		
		sql += "ORDER BY A.\"LastUpdate\" DESC ";


		sql += sqlRow;

		this.info("FindL6932 sql=" + sql);

		// *** 折返控制相關 ***
		this.index = index;
		// *** 折返控制相關 ***
		this.limit = limit;

		Query query;
//			query = em.createNativeQuery(sql,L5051Vo.class);//進SQL 所以不要用.class (要用.class 就要使用HQL)
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("ThisIndex", index);
		query.setParameter("ThisLimit", limit);

		if (txDate1 > 0) {
			query.setParameter("TxDate1", txDate1 + 19110000);
			query.setParameter("TxDate2", txDate2 + 19110000);
		} else {
			sql += "WHERE A.\"LastUpdate\" >= to_date(:TrDate1,'yyyymmdd hh24:mi:ss') AND A.\"LastUpdate\" <= to_date(:TrDate2,'yyyymmdd hh24:mi:ss')";
			caDate1 += 19110000;
			caDate2 += 19110000;
			query.setParameter("TrDate1", caDate1 + " 00:00:00");
			query.setParameter("TrDate2", caDate2 + " 23:59:59");
		}
		if (CustNo > 0) {
			query.setParameter("CustNo", CustNo);
		}
		if (FacmNo > 0) {
			query.setParameter("FacmNo", FacmNo);
		}
		if (BormNo > 0) {
			query.setParameter("BormNo", BormNo);
		}
		if (!"".equals(iTxtNo)) {
			query.setParameter("TxSeq", iTxtNo);
		}
		if (!"".equals(TranNo) && !"".equals(TranNo2)) {
			query.setParameter("TranNo1", TranNo);
			query.setParameter("TranNo2", TranNo2);
		} else if (!"".equals(TranNo)) {
			query.setParameter("TranNo", TranNo);
		}
		
		if (!"".equals(iMrKey)) {
			query.setParameter("Mrkey", iMrKey);
		}

		this.info("L6932Service FindData=" + query);

		// *** 折返控制相關 ***
		// 設定從第幾筆開始抓,需在createNativeQuery後設定
		// query.setFirstResult(this.index*this.limit);
		query.setFirstResult(0);// 因為已經在語法中下好限制條件(筆數),所以每次都從新查詢即可

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);

		return this.convertToMap(query);
	}

}