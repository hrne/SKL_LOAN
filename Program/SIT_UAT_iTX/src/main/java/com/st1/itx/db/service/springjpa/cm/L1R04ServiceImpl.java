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
import com.st1.itx.util.parse.Parse;;

@Service("l1R04ServiceImpl")
@Repository
public class L1R04ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	Parse parse;

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
	public List<Map<String, String>> FindData(TitaVo titaVo) throws Exception {
		this.info("L1R04FindData");

		int custNo = parse.stringToInteger(titaVo.getParam("RimCustNo").trim());
		int facmNo = parse.stringToInteger(titaVo.getParam("RimFacmNo").trim());

		this.info("L1R04 parm = " + custNo + "/" + facmNo);

		String sql = "SELECT A.\"FormNo\",A.\"FormName\",A.\"SendCode\",A.\"LetterFg\",A.\"MessageFg\",A.\"EmailFg\"";
		sql += ",NVL(B.\"PaperNotice\",'Y') AS \"PaperNotice\"";
		sql += ",NVL(B.\"MsgNotice\",'Y') AS \"MsgNotice\"";
		sql += ",NVL(B.\"EmailNotice\",'Y') AS \"EmailNotice\"";
		sql += ",NVL(B.\"ApplyDate\",0) AS \"ApplyDate\" ";
		sql += "FROM \"CdReport\" A ";
		sql += "LEFT JOIN \"CustNotice\" B ON B.\"CustNo\"=:CustNo AND B.\"FacmNo\"=:FacmNo AND B.\"FormNo\"=A.\"FormNo\" ";
		sql += "WHERE A.\"SendCode\" > 0 ";

		sql += "ORDER BY A.\"FormNo\" ";

//		sql += sqlRow;

		this.info("FindL1R04 sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
//		query.setParameter("ThisIndex", index);
//		query.setParameter("ThisLimit", limit);

		query.setParameter("CustNo", custNo);
		query.setParameter("FacmNo", facmNo);

		this.info("FindL1R04 FindData=" + query);

		return this.convertToMap(query);
	}

}