package com.st1.itx.db.service.springjpa.cm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.util.parse.Parse;

@Service("l2038ServiceImpl")
@Repository
public class L2038ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;
	
	/* 轉換工具 */
	@Autowired
	public Parse parse;

	private String conditionSql;

	private Query query;

	// *** 折返控制相關 ***
	private int index;

	// *** 折返控制相關 ***
	private int limit;

	private String sqlRow = "OFFSET :ThisIndex * :ThisLimit ROWS FETCH NEXT :ThisLimit ROW ONLY ";
	
	@Override
	public void afterPropertiesSet() throws Exception {
	}

	private List<Map<String, String>> execSql(TitaVo titaVo) throws Exception {
		this.info("L2038ServiceImpl.find");

		String sql = "";
		sql += "     SELECT MIN(cf.\"F1\")                           AS \"ApproveNo\"";
		sql += "           ,MIN(cf.\"F2\")                           AS \"FacmNo\"";
		sql += "           ,MIN(cu.\"CustId\")                       AS \"CustId\"";
		sql += "           ,MIN(cf.\"F3\")                           AS \"CustNo\"";
		sql += "           ,cm.\"ClCode1\"                      AS \"ClCode1\"";
		sql += "           ,cm.\"ClCode2\"                      AS \"ClCode2\"";
		sql += "           ,cm.\"ClNo\"                         AS \"ClNo\"";
		sql += "           ,MIN(cm.\"NewNote\")                      AS \"NewNote\"";
		sql += "           ,MIN(cm.\"ClTypeCode\")                   AS \"ClTypeCode\"";
		sql += "           ,CASE WHEN cm.\"ClCode1\" IN (1,2) THEN MIN(cblo.\"OwnerCustUKey\")";
		sql += "                 WHEN cm.\"ClCode1\" IN (3,4) THEN MIN(cs.\"OwnerCustUKey\")";
		sql += "                 WHEN cm.\"ClCode1\" = 5      THEN MIN(co.\"OwnerCustUKey\")";
		sql += "                 WHEN cm.\"ClCode1\" = 9      THEN MIN(cmv.\"OwnerCustUKey\")";
		sql += "            ELSE NULL END                       AS \"OwnerCustUKey\"";
		sql += "           ,CASE WHEN cm.\"ClCode1\" IN (1,2) THEN MIN(cblo.\"OwnerFlag\")";
		sql += "            ELSE 'N' END                        AS \"OwnerFlag\"";
		sql += "           ,CASE WHEN cm.\"ClCode1\" IN (1,2) THEN MIN(ci.\"SettingStat\")";
		sql += "                 WHEN cm.\"ClCode1\" IN (3,4) THEN MIN(cs.\"SettingStat\")";
		sql += "                 WHEN cm.\"ClCode1\" = 5      THEN MIN(co.\"SettingStat\")";
		sql += "                 WHEN cm.\"ClCode1\" = 9      THEN MIN(cmv.\"SettingStat\")";
		sql += "            ELSE NULL END                       AS \"SettingStat\"";
		sql += "           ,CASE WHEN cm.\"ClCode1\" IN (1,2) THEN MIN(ci.\"SettingAmt\")";
		sql += "                 WHEN cm.\"ClCode1\" IN (3,4) THEN MIN(cs.\"SettingBalance\")";
		sql += "                 WHEN cm.\"ClCode1\" = 5      THEN MIN(co.\"SettingAmt\")";
		sql += "                 WHEN cm.\"ClCode1\" = 9      THEN MIN(cmv.\"SettingAmt\")";
		sql += "            ELSE NULL END                       AS \"SettingAmt\"";
		sql += "           ,CASE WHEN cm.\"ClCode1\" IN (1,2) THEN MIN(ci.\"ClStat\")";
		sql += "                 WHEN cm.\"ClCode1\" IN (3,4) THEN MIN(cs.\"ClStat\")";
		sql += "                 WHEN cm.\"ClCode1\" = 5      THEN MIN(co.\"ClStat\")";
		sql += "                 WHEN cm.\"ClCode1\" = 9      THEN MIN(cmv.\"ClStat\")";
		sql += "            ELSE NULL END                       AS \"ClStat\"";
		sql += "           ,MIN(cm.\"ShareTotal\")                   AS \"ShareTotal\"";
		sql += "     FROM \"ClMain\" cm";
		
		
		
		sql += "    LEFT JOIN (SELECT \"ClCode1\"";
		sql += "                     ,\"ClCode2\"";
		sql += "                     ,\"ClNo\"";
		sql += "                     ,MIN(\"ApproveNo\") AS \"F1\"";
		sql += "                     ,MIN(\"FacmNo\") AS \"F2\"";
		sql += "                     ,MIN(\"CustNo\") AS \"F3\"";		
		sql += "                      FROM \"ClFac\"";
				
		sql += "                     GROUP BY \"ClCode1\",\"ClCode2\",\"ClNo\"";
		sql += "                     ) cf ON cf.\"ClCode1\" = cm.\"ClCode1\"";
		sql += "                         AND cf.\"ClCode2\" = cm.\"ClCode2\"";
		sql += "                         AND cf.\"ClNo\"    = cm.\"ClNo\"";
		
		sql += "     LEFT JOIN \"CustMain\" cu ON cu.\"CustNo\" = cf.\"F3\"";		
		
		sql += "     LEFT JOIN (SELECT \"ClCode1\"";
		sql += "                      ,\"ClCode2\"";
		sql += "                      ,\"ClNo\"";
		sql += "                      ,\"OwnerCustUKey\"";
		sql += "                      ,CASE WHEN \"ClNo\" IS NOT NULL THEN 'Y'"; 
		sql += "                          ELSE 'N' END AS \"OwnerFlag\"" ;
		sql	+= "                FROM \"ClBuildingOwner\"" ;
	    sql	+= "                UNION" ; 
	    sql	+= "                SELECT \"ClCode1\"";
		sql += "                      ,\"ClCode2\"";
		sql += "                      ,\"ClNo\"";
		sql += "                      ,\"OwnerCustUKey\"";
		sql += "                      ,CASE WHEN \"ClNo\" IS NOT NULL THEN 'Y'"; 
		sql += "                          ELSE 'N' END AS \"OwnerFlag\"" ;
		sql	+= "                FROM \"ClLandOwner\"" ;	    
	    sql	+= "    ) cblo ON cblo.\"ClCode1\" = cm.\"ClCode1\""; 
	    sql	+= "          AND cblo.\"ClCode2\" = cm.\"ClCode2\""; 
	    sql	+= "          AND cblo.\"ClNo\"    = cm.\"ClNo\"";
		
		
		// 各類大檔
		sql += "     LEFT JOIN \"ClStock\" cs ON cs.\"ClCode1\" = cm.\"ClCode1\"";
		sql += "                           AND cs.\"ClCode2\" = cm.\"ClCode2\"";
		sql += "                           AND cs.\"ClNo\"    = cm.\"ClNo\"";
		sql += "                           AND cm.\"ClCode1\" IN (3,4)";
		sql += "     LEFT JOIN \"ClOther\" co ON co.\"ClCode1\" = cm.\"ClCode1\"";
		sql += "                           AND co.\"ClCode2\" = cm.\"ClCode2\"";
		sql += "                           AND co.\"ClNo\"    = cm.\"ClNo\"";
		sql += "                           AND cm.\"ClCode1\" = 5";
		sql += "     LEFT JOIN \"ClMovables\" cmv ON cmv.\"ClCode1\" = cm.\"ClCode1\"";
		sql += "                                 AND cmv.\"ClCode2\" = cm.\"ClCode2\"";
		sql += "                                 AND cmv.\"ClNo\"    = cm.\"ClNo\"";
		sql += "                                 AND cm.\"ClCode1\" = 9";
		sql += "     LEFT JOIN \"ClImm\" ci ON ci.\"ClCode1\" = cm.\"ClCode1\"";
		sql += "                           AND ci.\"ClCode2\" = cm.\"ClCode2\"";
		sql += "                           AND ci.\"ClNo\"    = cm.\"ClNo\"";
		sql += "                           AND cm.\"ClCode1\" IN (1,2)";
		sql += "     LEFT JOIN \"ClBuilding\" cb ON cb.\"ClCode1\" = cm.\"ClCode1\"";
		sql += "                                AND cb.\"ClCode2\" = cm.\"ClCode2\"";
		sql += "                                AND cb.\"ClNo\"    = cm.\"ClNo\"";
		sql += "                                AND cm.\"ClCode1\" = 1";
		
		sql += "    LEFT JOIN \"ClLand\" cl ON cl.\"ClCode1\" = cm.\"ClCode1\"";			
		sql += "                         AND cl.\"ClCode2\" = cm.\"ClCode2\"";
		sql += "                         AND cl.\"ClNo\"    = cm.\"ClNo\"";
		
		sql += "     LEFT JOIN (SELECT \"ClCode1\"";
		sql += "                      ,\"ClCode2\"";
		sql += "                      ,\"ClNo\"";
		sql += "                      ,\"CityCode\"";
		sql += "                      ,\"AreaCode\"";
		sql += "                      ,\"IrCode\"";
		sql	+= "                FROM \"ClBuilding\"" ;
	    sql	+= "                UNION" ; 
	    sql	+= "                SELECT \"ClCode1\"";
		sql += "                      ,\"ClCode2\"";
		sql += "                      ,\"ClNo\"";
		sql += "                      ,\"CityCode\"";
		sql += "                      ,\"AreaCode\"";
		sql += "                      ,\"IrCode\"";
		sql	+= "                FROM \"ClLand\"" ;	    
	    sql	+= "    ) cbl ON cbl.\"ClCode1\" = cm.\"ClCode1\""; 
	    sql	+= "          AND cbl.\"ClCode2\" = cm.\"ClCode2\""; 
	    sql	+= "          AND cbl.\"ClNo\"    = cm.\"ClNo\"";
		
		
		sql += conditionSql;
		sql += "     GROUP BY cm.\"ClCode1\",cm.\"ClCode2\",cm.\"ClNo\"";
		sql += "     ORDER BY cm.\"ClCode1\",cm.\"ClCode2\",cm.\"ClNo\"";
		sql += sqlRow;
		this.info("sql = " + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		this.info("LIMIT = " + this.limit + "INDEX = " + this.index) ;
		
		query = em.createNativeQuery(sql);

		setConditionValue(titaVo);
		
//		// *** 折返控制相關 ***
//		// 設定從第幾筆開始抓,需在createNativeQuery後設定
//		query.setFirstResult(this.index * this.limit);
		query.setFirstResult(0);
		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);

		@SuppressWarnings("unchecked")
		List<Object> result = query.getResultList();

		return this.convertToMap(result);
	}

	/**
	 * *** 折返控制相關 ***
	 * 
	 * @param index  從第幾筆開始抓
	 * @param limit  每次抓幾筆
	 * @param titaVo titaVO
	 * @return 查詢結果
	 * @throws Exception 錯誤
	 */
	public List<Map<String, String>> findByCondition(int index, int limit, TitaVo titaVo) throws Exception {
		this.info("L2038ServiceImpl.findByCondition");

		// *** 折返控制相關 ***
		this.index = index;
		// *** 折返控制相關 ***
		this.limit = limit;
		
		// 初始化
		ArrayList<String> conditionList = new ArrayList<String>();
		conditionSql = "";

		// 擔保品大類
		int clMainType = parse.stringToInteger(titaVo.getParam("ClMainType"));

		if (clMainType > 0) {
			switch (clMainType) {
			case 1:
				// clMainType為1時,擔保品代號為1,2 (不動產)
				conditionList.add(" cm.\"ClCode1\" IN (1,2) ");
				break;
			case 2:
				// clMainType為2時,擔保品代號為9 (動產)
				conditionList.add(" cm.\"ClCode1\" = 9 ");
				break;
			case 3:
				// clMainType為3時,擔保品代號為3,4 (股票)
				conditionList.add(" cm.\"ClCode1\" IN (3,4) ");
				break;
			case 4:
				// clMainType為4時,擔保品代號為5 (銀行保證)
				conditionList.add(" cm.\"ClCode1\" = 5 ");
				break;
			default:
				break;
			}
		}
		

		// ClCode1 擔保品代號1
		int ClCode1 = parse.stringToInteger(titaVo.getParam("ClCode1"));
		if (ClCode1 > 0) {
			conditionList.add(" cm.\"ClCode1\"  = :ClCode1 ");
		}
		// ClCode2 擔保品代號2
		int ClCode2 = parse.stringToInteger(titaVo.getParam("ClCode2"));
		if (ClCode2 > 0) {
			conditionList.add(" cm.\"ClCode2\"  = :ClCode2 ");
		}
		// ClNo 擔保品編號
		int clNo = parse.stringToInteger(titaVo.getParam("ClNo"));
		if (clNo > 0) {
			conditionList.add(" cm.\"ClNo\" = :clNo ");
		}
        // ApproveNo 核准號碼 
		int approveNo = parse.stringToInteger(titaVo.getParam("ApproveNo"));
		if (approveNo > 0) {
			conditionList.add(" cf.\"F1\" = :approveNo ");
		}
		// CustId 借款戶統編 
		String custId = titaVo.getParam("CustId");
		if (custId != null && !custId.isEmpty()) {
			conditionList.add(" cu.\"CustId\" = :custId ");
		}
        // CustNo 戶號
        // FacmNo 額度編號
		int custNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		int facmNo = parse.stringToInteger(titaVo.getParam("FacmNo"));
		
		if (custNo > 0) {
			conditionList.add(" cf.\"F3\" = :custNo ");
		}

		if (facmNo > 0) {
			conditionList.add(" cf.\"F2\" = :facmNo ");
		}
		// OwnerId 所有權人統編
		String ownerId = titaVo.getParam("OwnerId");
		
		CustMain tCustMain = new CustMain();
		
		tCustMain = sCustMainService.custIdFirst(ownerId, titaVo);

		
		if (tCustMain != null ) {
			conditionList.add(
					" CASE WHEN cm.\"ClCode1\" IN (1,2)     THEN cblo.\"OwnerCustUKey\"" +   "      WHEN cm.\"ClCode1\" IN (3,4) THEN cs.\"OwnerCustUKey\""
							+ "      WHEN cm.\"ClCode1\" = 5      THEN co.\"OwnerCustUKey\"" + "      WHEN cm.\"ClCode1\" = 9      THEN cmv.\"OwnerCustUKey\"" + " ELSE NULL END = :OwnerCustUKey");
		}
        // SettingStat 設定狀態
		int settingStat = parse.stringToInteger(titaVo.getParam("SettingStat"));
		if (settingStat > 0) {
			conditionList.add(" CASE WHEN cm.\"ClCode1\" IN (1,2) THEN ci.\"SettingStat\"" + "              WHEN cm.\"ClCode1\" IN (3,4) THEN cs.\"SettingStat\""
					+ "              WHEN cm.\"ClCode1\" = 5      THEN co.\"SettingStat\"" + "              WHEN cm.\"ClCode1\" = 9      THEN cmv.\"SettingStat\""
					+ "         ELSE '0' END = :settingStat ");
		}
        // 擔保品狀態
		int clStat = parse.stringToInteger(titaVo.getParam("ClStat"));
		if (clStat > 0) {
			conditionList.add(" CASE WHEN cm.\"ClCode1\" IN (1,2) THEN ci.\"ClStat\"" + "              WHEN cm.\"ClCode1\" IN (3,4) THEN cs.\"ClStat\""
					+ "              WHEN cm.\"ClCode1\" = 5      THEN co.\"ClStat\"" + "              WHEN cm.\"ClCode1\" = 9      THEN cmv.\"ClStat\"" + "         ELSE '0' END = :clStat ");
		}

		// 發行公司統編
		String CompanyId = titaVo.getParam("CompanyId");
		if (CompanyId != null && !CompanyId.isEmpty()) {
			CompanyId = "%" + CompanyId + "%";
			conditionList.add(" cs.\"CompanyId\" LIKE :CompanyId ");
			
		}// TODO
		// CityCode 縣市區域
		String cityCode = titaVo.getParam("CityCode");
		if (parse.stringToInteger(cityCode) > 0) {
//			if(ClCode1 == 1) {
				conditionList.add(" cbl.\"CityCode\" = :cityCode ");				
//			} else {
//				conditionList.add(" cl.\"CityCode\" = :cityCode ");				
//			}
		}
		// AreaCode 鄉鎮市區
		String areaCode = titaVo.getParam("AreaCode");
		if (parse.stringToInteger(areaCode) > 0) {
//			if(ClCode1 == 1) {
				conditionList.add(" cbl.\"AreaCode\" = :areaCode ");				
//			} else {
//				conditionList.add(" cl.\"AreaCode\" = :areaCode ");				
//			}
		}
		// IrCode 段小段
		String irCode = titaVo.getParam("IrCode");
		if (parse.stringToInteger(irCode) > 0) {
//			if(ClCode1 == 1) {
				conditionList.add(" cbl.\"IrCode\" = :irCode ");				
//			} else {
//				conditionList.add(" cl.\"IrCode\" = :irCode ");				
//			}

		}
		// LandNo1 土地地號1
		String landNo1 = titaVo.getParam("LandNo1");
		if (parse.stringToInteger(landNo1) > 0) {
			conditionList.add(" cl.\"LandNo1\" = :landNo1 ");
		}
		// LandNo2 土地地號2
		String landNo2 = titaVo.getParam("LandNo2");
		if (parse.stringToInteger(landNo2) > 0) {
			conditionList.add(" cl.\"LandNo2\" = :landNo2 ");
		}
		// BdNo1 建物建號1
		String bdNo1 = titaVo.getParam("BdNo1");
		if (parse.stringToInteger(bdNo1) > 0) {
			conditionList.add(" cb.\"BdNo1\" = :bdNo1 ");
		}
		// BdNo2 建物建號2
		String bdNo2 = titaVo.getParam("BdNo2");
		if (parse.stringToInteger(bdNo2) > 0) {
			conditionList.add(" cb.\"BdNo2\" = :bdNo2 ");
		}
		// Road 路
		String road = titaVo.getParam("Road");
		if (road != null && !road.isEmpty()) {
			road = "%" + road + "%";
			conditionList.add(" cb.\"Road\" LIKE :road ");
		}
		// Section 段
		int section = parse.stringToInteger(titaVo.getParam("Section"));
		if (section > 0) {
			conditionList.add(" cb.\"Section\" = :section ");
		}
		// Alley 巷
		int alley = parse.stringToInteger(titaVo.getParam("Alley"));
		if (alley > 0) {
			conditionList.add(" cb.\"Alley\" = :alley ");
		}
		// Lane 弄
		int lane = parse.stringToInteger(titaVo.getParam("Lane"));
		if (lane > 0) {
			conditionList.add(" cb.\"Lane\" = :lane ");
		}
		// Num 號
		int num = parse.stringToInteger(titaVo.getParam("Num"));
		if (num > 0) {
			conditionList.add(" cb.\"Num\" = :num ");
		}
		// NumDash 號之幾
		int numDash = parse.stringToInteger(titaVo.getParam("NumDash"));
		if (numDash > 0) {
			conditionList.add(" cb.\"NumDash\" = :numDash ");
		}
		// Floor 樓
		int floor = parse.stringToInteger(titaVo.getParam("Floor"));
		if (floor > 0) {
			conditionList.add(" cb.\"Floor\" = :floor ");
		}
		// FloorDash 樓之幾
		int floorDash = parse.stringToInteger(titaVo.getParam("FloorDash"));
		if (floorDash > 0) {
			conditionList.add(" cb.\"FloorDash\" = :floorDash ");
		}
		
		this.info("L2038ServiceImpl conditionList.size() = " + conditionList.size());

		// 根據篩選條件語句數量,組成一句where語句,若無則維持空白
		if (conditionList.size() > 0) {
			for (int i = 0; i < conditionList.size(); i++) {
				String tmpCondition = conditionList.get(i);
				if (i == 0) {
					conditionSql += " WHERE ";
				} else {
					conditionSql += " AND ";
				}
				conditionSql += tmpCondition;
			}
		}
		this.info("L2038ServiceImpl conditionSql = " + conditionSql);

		return execSql(titaVo);
	}

	public void setConditionValue(TitaVo titaVo) throws Exception {
		this.info("L2038ServiceImpl.setConditionValue");
		
		int ClCode1 = parse.stringToInteger(titaVo.getParam("ClCode1"));
		if (ClCode1 > 0) {
			query.setParameter("ClCode1", ClCode1);
		}
		int ClCode2 = parse.stringToInteger(titaVo.getParam("ClCode2"));
		if (ClCode2 > 0) {
			query.setParameter("ClCode2", ClCode2);
		}

		int clNo = parse.stringToInteger(titaVo.getParam("ClNo"));
		if (clNo > 0) {
			query.setParameter("clNo", clNo);
		}
		
		int approveNo = parse.stringToInteger(titaVo.getParam("ApproveNo"));
		if (approveNo > 0) {
			query.setParameter("approveNo", approveNo);
		}

		String custId = titaVo.getParam("CustId");
		if (custId != null && !custId.isEmpty()) {
			query.setParameter("custId", custId);
		}

		int custNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		if (custNo > 0) {
			query.setParameter("custNo", custNo);
		}

		int facmNo = parse.stringToInteger(titaVo.getParam("FacmNo"));
		if (facmNo > 0) {
			query.setParameter("facmNo", facmNo);
		}
		
		String ownerId = titaVo.getParam("OwnerId");
		
		CustMain tCustMain = new CustMain();
		
		tCustMain = sCustMainService.custIdFirst(ownerId, titaVo);
		
		if (tCustMain != null ) {
			String UKey = tCustMain.getCustUKey();
			query.setParameter("OwnerCustUKey", UKey);
		}

		int settingStat = parse.stringToInteger(titaVo.getParam("SettingStat"));
		if (settingStat > 0) {
			query.setParameter("settingStat", String.valueOf(settingStat));
		}

		int clStat = parse.stringToInteger(titaVo.getParam("ClStat"));
		if (clStat > 0) {
			query.setParameter("clStat", String.valueOf(clStat));
		}
		String CompanyId = titaVo.getParam("CompanyId");
		if (CompanyId != null && !CompanyId.isEmpty()) {
			CompanyId = "%" + CompanyId + "%";
			query.setParameter("CompanyId", CompanyId);
		} //TODO
		
		/* 土地建物相關
		 * 
		 * 
		 * */
		String cityCode = titaVo.getParam("CityCode");
		if (parse.stringToInteger(cityCode) > 0) {
			query.setParameter("cityCode", cityCode);
		}

		String areaCode = titaVo.getParam("AreaCode");
		if (parse.stringToInteger(areaCode) > 0) {
			query.setParameter("areaCode", areaCode);
		}

		String irCode = titaVo.getParam("IrCode");
		if (parse.stringToInteger(irCode) > 0) {
			query.setParameter("irCode", irCode);
		}

		String landNo1 = titaVo.getParam("LandNo1");
		if (parse.stringToInteger(landNo1) > 0) {
			query.setParameter("landNo1", landNo1);
		}

		String landNo2 = titaVo.getParam("LandNo2");
		if (parse.stringToInteger(landNo2) > 0) {
			query.setParameter("landNo2", landNo2);
		}

		String bdNo1 = titaVo.getParam("BdNo1");
		if (parse.stringToInteger(bdNo1) > 0) {
			query.setParameter("bdNo1", bdNo1);
		}

		String bdNo2 = titaVo.getParam("BdNo2");
		if (parse.stringToInteger(bdNo2) > 0) {
			query.setParameter("bdNo2", bdNo2);
		}

		String road = titaVo.getParam("Road");
		if (road != null && !road.isEmpty()) {
			road = "%" + road + "%";
			query.setParameter("road", road);
		}
	
		int section = parse.stringToInteger(titaVo.getParam("Section"));
		if (section > 0) {
			query.setParameter("section", String.valueOf(section));
		}

		int alley = parse.stringToInteger(titaVo.getParam("Alley"));
		if (alley > 0) {
			query.setParameter("alley", String.valueOf(alley));
		}

		int lane = parse.stringToInteger(titaVo.getParam("Lane"));
		if (lane > 0) {
			query.setParameter("lane", String.valueOf(lane));
		}

		int num = parse.stringToInteger(titaVo.getParam("Num"));
		if (num > 0) {
			query.setParameter("num", String.valueOf(num));
		}

		int numDash = parse.stringToInteger(titaVo.getParam("NumDash"));
		if (numDash > 0) {
			query.setParameter("numDash", String.valueOf(numDash));
		}

		int floor = parse.stringToInteger(titaVo.getParam("Floor"));
		if (floor > 0) {
			query.setParameter("floor", String.valueOf(floor));
		}

		int floorDash = parse.stringToInteger(titaVo.getParam("FloorDash"));
		if (floorDash > 0) {
			query.setParameter("floorDash", String.valueOf(floorDash));
		}
		
		query.setParameter("ThisIndex", this.index);
		query.setParameter("ThisLimit", this.limit);
		return;
	}

}