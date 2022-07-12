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

	// *** 折返控制相關 ***
	private int cnt;

	// *** 折返控制相關 ***
	private int size;

//	private String sqlRow = "OFFSET :ThisIndex * :ThisLimit ROWS FETCH NEXT :ThisLimit ROW ONLY ";

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@SuppressWarnings("unchecked")
	private List<Map<String, String>> execSql(TitaVo titaVo) throws Exception {
		this.info("L2038ServiceImpl.find");

		String sql = "";
		sql += "     SELECT MIN(cf.\"ApproveNo\")                           AS \"ApproveNo\"";
		sql += "           ,MIN(cf.\"FacmNo\")                           AS \"FacmNo\"";
		sql += "           ,MIN(cu.\"CustId\")                       AS \"CustId\"";
		sql += "           ,MIN(cf.\"CustNo\")                           AS \"CustNo\"";
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
		sql += "           ,COUNT(cor.\"ClCode1\")              AS \"OtherRightsCount\" ";
		sql += "     FROM \"ClMain\" cm";

		sql += "    LEFT JOIN (SELECT \"ClCode1\"";
		sql += "                     ,\"ClCode2\"";
		sql += "                     ,\"ClNo\"";
		sql += "                     ,\"ApproveNo\"";
		sql += "                     ,\"FacmNo\"";
		sql += "                     ,\"CustNo\"";
		sql += "                      FROM \"ClFac\"";

		sql += "                     GROUP BY \"ClCode1\",\"ClCode2\",\"ClNo\",\"ApproveNo\",\"FacmNo\",\"CustNo\"";
		sql += "                     ) cf ON cf.\"ClCode1\" = cm.\"ClCode1\"";
		sql += "                         AND cf.\"ClCode2\" = cm.\"ClCode2\"";
		sql += "                         AND cf.\"ClNo\"    = cm.\"ClNo\"";

		sql += "     LEFT JOIN \"CustMain\" cu ON cu.\"CustNo\" = cf.\"CustNo\"";

		sql += "     LEFT JOIN (SELECT \"ClCode1\"";
		sql += "                      ,\"ClCode2\"";
		sql += "                      ,\"ClNo\"";
		sql += "                      ,\"OwnerCustUKey\"";
		sql += "                      ,CASE WHEN \"ClNo\" IS NOT NULL THEN 'Y'";
		sql += "                          ELSE 'N' END AS \"OwnerFlag\"";
		sql += "                FROM \"ClBuildingOwner\"";
		sql += "                UNION";
		sql += "                SELECT \"ClCode1\"";
		sql += "                      ,\"ClCode2\"";
		sql += "                      ,\"ClNo\"";
		sql += "                      ,\"OwnerCustUKey\"";
		sql += "                      ,CASE WHEN \"ClNo\" IS NOT NULL THEN 'Y'";
		sql += "                          ELSE 'N' END AS \"OwnerFlag\"";
		sql += "                FROM \"ClLandOwner\"";
		sql += "    ) cblo ON cblo.\"ClCode1\" = cm.\"ClCode1\"";
		sql += "          AND cblo.\"ClCode2\" = cm.\"ClCode2\"";
		sql += "          AND cblo.\"ClNo\"    = cm.\"ClNo\"";

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

		sql += "    LEFT JOIN (SELECT \"ClCode1\"";
		sql += "                     ,\"ClCode2\"";
		sql += "                     ,\"ClNo\"";
		sql += "                     ,TO_NUMBER(\"LandNo1\" || \"LandNo2\") AS \"LandNo\"";
		sql += "               FROM \"ClLand\" ";
		sql += "    ) lNo ON lNo.\"ClCode1\" = cm.\"ClCode1\"";
		sql += "          AND lNo.\"ClCode2\" = cm.\"ClCode2\"";
		sql += "          AND lNo.\"ClNo\"    = cm.\"ClNo\"";

		sql += "    LEFT JOIN (SELECT \"ClCode1\"";
		sql += "                     ,\"ClCode2\"";
		sql += "                     ,\"ClNo\"";
		sql += "                     ,TO_NUMBER(\"BdNo1\" || \"BdNo2\") AS \"BdNo\"";
		sql += "               FROM \"ClBuilding\" ";
		sql += "    ) bNo ON bNo.\"ClCode1\" = cm.\"ClCode1\"";
		sql += "          AND bNo.\"ClCode2\" = cm.\"ClCode2\"";
		sql += "          AND bNo.\"ClNo\"    = cm.\"ClNo\"";

		sql += "     LEFT JOIN (SELECT \"ClCode1\"";
		sql += "                      ,\"ClCode2\"";
		sql += "                      ,\"ClNo\"";
		sql += "                      ,\"CityCode\"";
		sql += "                      ,\"AreaCode\"";
		sql += "                      ,\"IrCode\"";
		sql += "                FROM \"ClBuilding\"";
		sql += "                UNION";
		sql += "                SELECT \"ClCode1\"";
		sql += "                      ,\"ClCode2\"";
		sql += "                      ,\"ClNo\"";
		sql += "                      ,\"CityCode\"";
		sql += "                      ,\"AreaCode\"";
		sql += "                      ,\"IrCode\"";
		sql += "                FROM \"ClLand\"";
		sql += "    ) cbl ON cbl.\"ClCode1\" = cm.\"ClCode1\"";
		sql += "          AND cbl.\"ClCode2\" = cm.\"ClCode2\"";
		sql += "          AND cbl.\"ClNo\"    = cm.\"ClNo\"";
		
		// 他項權利
		
		sql += "     LEFT JOIN \"ClOtherRights\" cor ON cor.\"ClCode1\" = cm.\"ClCode1\" ";
		sql += "                                    AND cor.\"ClCode2\" = cm.\"ClCode2\" ";
		sql += "                                    AND cor.\"ClNo\"    = cm.\"ClNo\"    ";

		sql += conditionSql;
		sql += "     GROUP BY cm.\"ClCode1\",cm.\"ClCode2\",cm.\"ClNo\"";
		sql += "     ORDER BY cm.\"ClCode1\",cm.\"ClCode2\",cm.\"ClNo\"";
//		sql += sqlRow;
		this.info("sql = " + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		query = em.createNativeQuery(sql);

		setConditionValue(titaVo);

		cnt = query.getResultList().size();
		this.info("Total cnt ..." + cnt);

//		// *** 折返控制相關 ***
//		// 設定從第幾筆開始抓,需在createNativeQuery後設定
		query.setFirstResult(this.index * this.limit);

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);

		List<Object> result = query.getResultList();

		size = result.size();
		this.info("Total size ..." + size);

		return this.convertToMap(query);
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
		
		// ClTypeCode 擔保品類別
		String ClTypeCode = titaVo.getParam("ClTypeCode");
		if (ClTypeCode != null && !ClTypeCode.isEmpty()) {
			conditionList.add(" cm.\"ClTypeCode\" = :ClTypeCode ");
		}
		
		// ApproveNo 核准號碼
		int approveNo = parse.stringToInteger(titaVo.getParam("ApproveNo"));
		if (approveNo > 0) {
			conditionList.add(" cf.\"ApproveNo\" = :approveNo ");
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
			conditionList.add(" cf.\"CustNo\" = :custNo ");
		}

		if (facmNo > 0) {
			conditionList.add(" cf.\"FacmNo\" = :facmNo ");
		}
		// OwnerId 所有權人統編
		String ownerId = titaVo.getParam("OwnerId");

		CustMain tCustMain = new CustMain();

		tCustMain = sCustMainService.custIdFirst(ownerId, titaVo);

		if (tCustMain != null) {
			conditionList.add(" CASE WHEN cm.\"ClCode1\" IN (1,2)     THEN cblo.\"OwnerCustUKey\"" + "      WHEN cm.\"ClCode1\" IN (3,4) THEN cs.\"OwnerCustUKey\""
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
//			CompanyId = "%" + CompanyId + "%";
			conditionList.add(" cs.\"CompanyId\" LIKE :CompanyId ");
		}

		// CityCode 縣市區域
		String cityCode = titaVo.getParam("CityCode");
		if (parse.stringToInteger(cityCode) > 0) {
			conditionList.add(" cbl.\"CityCode\" = :cityCode ");
		}

		// AreaCode 鄉鎮市區
		String areaCode = titaVo.getParam("AreaCode");
		if (parse.stringToInteger(areaCode) > 0) {
			conditionList.add(" cbl.\"AreaCode\" = :areaCode ");
		}

		// IrCode 段小段
		String irCode = titaVo.getParam("IrCode");
		if (parse.stringToInteger(irCode) > 0) {
			conditionList.add(" cbl.\"IrCode\" = :irCode ");
		}

		// 地號區間
		int landNo1 = parse.stringToInteger(titaVo.getParam("LandNo1"));
		int landNo2 = parse.stringToInteger(titaVo.getParam("LandNo2"));
		int landNo3 = parse.stringToInteger(titaVo.getParam("LandNo3"));
		int landNo4 = parse.stringToInteger(titaVo.getParam("LandNo4"));

		if (landNo1 > 0 || landNo2 > 0) {
			conditionList.add(" lNo.\"LandNo\" >= :startlandNo ");
		}

		if (landNo3 > 0 || landNo4 > 0) {
			conditionList.add(" lNo.\"LandNo\" <= :endlandNo ");
		}

		// 建號區間
		int bdNo1 = parse.stringToInteger(titaVo.getParam("BdNo1"));
		int bdNo2 = parse.stringToInteger(titaVo.getParam("BdNo2"));
		int bdNo3 = parse.stringToInteger(titaVo.getParam("BdNo3"));
		int bdNo4 = parse.stringToInteger(titaVo.getParam("BdNo4"));

		if (bdNo1 > 0 || bdNo2 > 0) {
			conditionList.add(" bNo.\"BdNo\" >= :startbdNo ");
		}
		if (bdNo3 > 0 || bdNo4 > 0) {
			conditionList.add(" bNo.\"BdNo\" <= :endbdNo ");
		}

		// Road 路
		String road = titaVo.getParam("Road");
		if (road != null && !road.isEmpty()) {
//			road = "%" + road + "%";
			conditionList.add(" cb.\"Road\" LIKE :road ");
		}
		// Section 段
		String section = titaVo.getParam("Section");
		if (section != null && !section.isEmpty()) {
			conditionList.add(" cb.\"Section\" = :section ");
		}
		// Alley 巷
		String alley = titaVo.getParam("Alley");
		if (alley != null && !alley.isEmpty()) {
			conditionList.add(" cb.\"Alley\" = :alley ");
		}
		// Lane 弄
		String lane = titaVo.getParam("Lane");
		if (lane != null && !lane.isEmpty()) {
			conditionList.add(" cb.\"Lane\" = :lane ");
		}
		// Num 號
		String num = titaVo.getParam("Num");
		if (num != null && !num.isEmpty()) {
			conditionList.add(" cb.\"Num\" = :num ");
		}
		// NumDash 號之幾
		String numDash = titaVo.getParam("NumDash");
		if (numDash != null && !numDash.isEmpty()) {
			conditionList.add(" cb.\"NumDash\" = :numDash ");
		}
		// Floor 樓
		String floor = titaVo.getParam("Floor");
		if (floor != null && !floor.isEmpty()) {
			conditionList.add(" cb.\"Floor\" = :floor ");
		}
		// FloorDash 樓之幾
		String floorDash = titaVo.getParam("FloorDash");
		if (floorDash != null && !floor.isEmpty()) {
			conditionList.add(" cb.\"FloorDash\" = :floorDash ");
		}

		// LicenseNo 牌照號碼
		String licenseNo = titaVo.getParam("LicenseNo");
		if (licenseNo != null && !licenseNo.isEmpty()) {
//			licenseNo = "%" + licenseNo + "%";
			conditionList.add(" cmv.\"LicenseNo\" LIKE :licenseNo ");
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

		String ClTypeCode = titaVo.getParam("ClTypeCode");
		if (ClTypeCode != null && !ClTypeCode.isEmpty()) {
			query.setParameter("ClTypeCode", ClTypeCode);
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

		if (tCustMain != null) {
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
		} // TODO

		/*
		 * 土地建物相關
		 * 
		 * 
		 */
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

//		String landNo1 = titaVo.getParam("LandNo1");
//		if (parse.stringToInteger(landNo1) > 0) {
//			query.setParameter("landNo1", landNo1);
//		}
//
//		String landNo2 = titaVo.getParam("LandNo2");
//		if (parse.stringToInteger(landNo2) > 0) {
//			query.setParameter("landNo2", landNo2);
//		}
		// 土地地號區間
		int startlandNo = 0;
		int endlandNo = 0;
		int startbdNo = 0;
		int endbdNo = 0;

		int landNo1 = parse.stringToInteger(titaVo.getParam("LandNo1"));
		int landNo2 = parse.stringToInteger(titaVo.getParam("LandNo2"));
		int landNo3 = parse.stringToInteger(titaVo.getParam("LandNo3"));
		int landNo4 = parse.stringToInteger(titaVo.getParam("LandNo4"));

		if (landNo1 > 0) {
			startlandNo = landNo1 * 10000;
		}
		if (landNo2 > 0) {
			startlandNo = startlandNo + landNo2;
		}
		if (landNo3 > 0) {
			endlandNo = landNo3 * 10000;
		}
		if (landNo4 > 0) {
			endlandNo = endlandNo + landNo4;
		}

		if (landNo1 > 0 || landNo2 > 0) {
			query.setParameter("startlandNo", startlandNo);
		}
		if (landNo3 > 0 || landNo4 > 0) {
			query.setParameter("endlandNo", endlandNo);
		}

		// 建物建號區間
		int bdNo1 = parse.stringToInteger(titaVo.getParam("BdNo1"));
		int bdNo2 = parse.stringToInteger(titaVo.getParam("BdNo2"));
		int bdNo3 = parse.stringToInteger(titaVo.getParam("BdNo3"));
		int bdNo4 = parse.stringToInteger(titaVo.getParam("BdNo4"));

		if (bdNo1 > 0) {
			startbdNo = bdNo1 * 1000;
		}
		if (bdNo2 > 0) {
			startbdNo = startbdNo + bdNo2;
		}
		if (bdNo3 > 0) {
			endbdNo = bdNo3 * 1000;
		}
		if (bdNo4 > 0) {
			endbdNo = endbdNo + bdNo4;
		}

		if (bdNo1 > 0 || bdNo2 > 0) {
			query.setParameter("startbdNo", startbdNo);
		}
		if (bdNo3 > 0 || bdNo4 > 0) {
			query.setParameter("endbdNo", endbdNo);
		}

		String road = titaVo.getParam("Road");
		if (road != null && !road.isEmpty()) {
			road = "%" + road + "%";
			query.setParameter("road", road);
		}

		String section = titaVo.getParam("Section");
		if (section != null && !section.isEmpty()) {
			query.setParameter("section", String.valueOf(section));
		}

		String alley = titaVo.getParam("Alley");
		if (alley != null && !alley.isEmpty()) {
			query.setParameter("alley", String.valueOf(alley));
		}

		String lane = titaVo.getParam("Lane");
		if (lane != null && !lane.isEmpty()) {
			query.setParameter("lane", String.valueOf(lane));
		}

		String num = titaVo.getParam("Num");
		if (num != null && !num.isEmpty()) {
			query.setParameter("num", String.valueOf(num));
		}

		String numDash = titaVo.getParam("NumDash");
		if (numDash != null && !numDash.isEmpty()) {
			query.setParameter("numDash", String.valueOf(numDash));
		}

		String floor = titaVo.getParam("Floor");
		if (floor != null && !floor.isEmpty()) {
			query.setParameter("floor", String.valueOf(floor));
		}

		String floorDash = titaVo.getParam("FloorDash");
		if (floorDash != null && !floorDash.isEmpty()) {
			query.setParameter("floorDash", String.valueOf(floorDash));
		}

		String licenseNo = titaVo.getParam("LicenseNo");
		if (licenseNo != null && !licenseNo.isEmpty()) {
			licenseNo = "%" + licenseNo + "%";
			query.setParameter("licenseNo", licenseNo);
		}

		return;
	}

	public int getSize() {
		return cnt;
	}
}