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

@Service
@Repository
public class L6041ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	// *** 折返控制相關 ***
	private int index;

	// *** 折返控制相關 ***
	private int limit;

	public List<Map<String, String>> findByTrol(String iAuthNo , String iTlrNo , String iBrNo , String iGroupNoS , int iLevelFgS ,int index, int limit, TitaVo titaVo) throws LogicException {

		this.info("L6041ServiceImpl findByTrol active");
		
		String sql = " ";
		sql += " select  ";
		sql += " b.\"TlrNo\", ";
		sql += " a.\"BrNo\", ";
		sql += " a.\"GroupNo\", ";
		sql += " a.\"LastUpdate\", ";
		sql += " a.\"LevelFg\", ";
		sql += " a.\"MntEmpNo\" ";
		sql += " from \"TxTeller\" a ";
		sql += " left join \"TxTellerAuth\" b on a.\"TlrNo\" = b.\"TlrNo\" ";
		sql += " left join \"CdEmp\" c on a.\"TlrNo\" = c.\"EmployeeNo\" ";
		sql += " where 1=1 ";
		if (!iAuthNo.equals("")) {
		sql += "   and b.\"AuthNo\" = :AuthNo ";
		}
		if (!iTlrNo.equals("")) {
		sql += "   and b.\"TlrNo\"  = :TlrNo ";
		}
		if(!iBrNo.equals("")) {
		sql += "   	and a.\"BrNo\"  = :BrNo ";
		}
		if (!iGroupNoS.equals("")) {
		sql += "   	and a.\"GroupNo\"  = :GroupNoS  ";
		}
		if(iLevelFgS != 0) {
		sql += "    and a.\"LevelFg\"  = :LevelFgS  ";
		}
		Query query;
		EntityManager em = baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		if (!iAuthNo.equals("")) {
			query.setParameter("AuthNo", iAuthNo);
		}
		if (!iTlrNo.equals("")) {
			query.setParameter("TlrNo", iTlrNo);
		}
		if (!iBrNo.equals("")) {
			query.setParameter("BrNo", iBrNo);
		}
		if (!iGroupNoS.equals("")) {
			query.setParameter("GroupNoS", iGroupNoS);
		}
		if(iLevelFgS != 0) {
			query.setParameter("LevelFgS", iLevelFgS);
		}
		// *** 折返控制相關 ***
		this.index = index;
		// *** 折返控制相關 ***
		this.limit = limit;

		// *** 折返控制相關 ***
		// 設定從第幾筆開始抓,需在createNativeQuery後設定
		// query.setFirstResult(this.index*this.limit);
		query.setFirstResult(0);// 因為已經在語法中下好限制條件(筆數),所以每次都從新查詢即可

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);

		return this.convertToMap(query);
	}
	
	public List<Map<String, String>> findByTrolAll(String iTlro ,int index, int limit, TitaVo titaVo) throws LogicException {
		this.info("L6041ServiceImpl findByTrolAll active");
		
		String sql = " ";
		sql += " select  ";
		sql += " A.\"TlrNo\" , ";
		sql += " A.\"AuthNo\", ";
		sql += " B.\"AuthItem\" , ";
		sql += " A.\"LastUpdate\" , ";
		sql += " A.\"LastUpdateEmpNo\" ";
		sql += " from \"TxTellerAuth\" A ";
		sql += " left join \"TxAuthGroup\" B on a.\"AuthNo\" = b.\"AuthNo\" ";
		
		if (!iTlro.equals("")) {
		sql += " where \"TlrNo\" = :TlrNo ";
		}
		
		Query query;
		EntityManager em = baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		if (!iTlro.equals("")) {
			query.setParameter("TlrNo", iTlro);
		}
		
		// *** 折返控制相關 ***
		this.index = index;
		// *** 折返控制相關 ***
		this.limit = limit;

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
