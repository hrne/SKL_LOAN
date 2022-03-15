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

@Service("l1908ServiceImpl")
@Repository
public class L1908ServiceImpl extends ASpringJpaParm implements InitializingBean {

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

	public List<Map<String, String>> FindData(TitaVo titaVo, int custNo, int facmNo) throws Exception {
		this.info("L1908FindData");

//		String sql = "SELECT A.\"FormNo\",B.\"FormName\",B.\"SendCode\",B.\"LetterFg\",B.\"MessageFg\",B.\"EmailFg\" ";
//		sql += ",A.\"CustNo\"";
//		sql += ",A.\"FacmNo\"";
//		sql += ",A.\"PaperNotice\"";
//		sql += ",A.\"MsgNotice\"";
//		sql += ",A.\"EmailNotice\"";
//		sql += ",(A.\"ApplyDate\" - 19110000) AS \"ApplyDate\"";
//		sql += ",A.\"LastUpdate\"";
//		sql += ",A.\"LastUpdateEmpNo\"";
//		sql += ",C.\"Fullname\"";
//		sql += "FROM \"CustNotice\" A ";
//		sql += "LEFT JOIN \"CdReport\" B ON B.\"FormNo\"=A.\"FormNo\" ";
//		sql += "LEFT JOIN \"CdEmp\" C ON C.\"EmployeeNo\"=A.\"LastUpdateEmpNo\" ";
//		sql += "WHERE B.\"SendCode\" > 0 ";
//		sql += "AND A.\"CustNo\"=:CustNo ";
//		if (facmNo > 0) {
//			sql += "AND A.\"FacmNo\"=:FacmNo ";
//		}
//		sql += "AND (A.\"PaperNotice\"='N' OR A.\"MsgNotice\"='N' OR A.\"EmailNotice\"='N') ";
//		sql += "ORDER BY A.\"CustNo\",A.\"FacmNo\",A.\"FormNo\" ";

		
		String sql = "SELECT A.\"FormNo\",A.\"FormName\",A.\"SendCode\",A.\"LetterFg\",A.\"MessageFg\",A.\"EmailFg\"";
		sql += ",NVL(B.\"PaperNotice\",'Y') AS \"PaperNotice\"";
		sql += ",NVL(B.\"MsgNotice\",'Y') AS \"MsgNotice\"";
		sql += ",NVL(B.\"EmailNotice\",'Y') AS \"EmailNotice\"";
		sql += ",NVL(B.\"ApplyDate\"-19110000,0) AS \"ApplyDate\" ";
		sql += ",B.\"LastUpdate\"";
		sql += ",B.\"LastUpdateEmpNo\"";
		sql += ",C.\"Fullname\"";
		sql += "FROM \"CdReport\" A ";
		sql += "LEFT JOIN \"CustNotice\" B ON B.\"CustNo\"=:CustNo AND B.\"FacmNo\"=:FacmNo AND B.\"FormNo\"=A.\"FormNo\" ";
		sql += "LEFT JOIN \"CdEmp\" C ON C.\"EmployeeNo\"=B.\"LastUpdateEmpNo\" ";
		sql += "WHERE A.\"SendCode\" > 0 ";
		sql += "ORDER BY A.\"FormNo\" ";

//		sql += sqlRow;

		this.info("Find L1908 sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
//		query.setParameter("ThisIndex", index);
//		query.setParameter("ThisLimit", limit);

		query.setParameter("CustNo", custNo);
		query.setParameter("FacmNo", facmNo);

		this.info("Find L1908 FindData=" + query);

		return this.convertToMap(query);
	}
}