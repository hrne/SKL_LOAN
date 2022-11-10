package com.st1.itx.trade.L2;

/**
 * 若空白表、回饋檔有增減欄位、順序變換應維護此檔案
 * 
 * @author ST1-Wei
 *
 */
public enum L2419Column {
	
	NO(1),
	CL_CODE_1(2),
	CL_CODE_2(3),
	CL_NO(4),
	INSERT_FLAG(5),
	CL_TYPE(6),
	BUILD_NO_1(7),
	BUILD_NO_2(8),
	LAND_NO_1(9),
	LAND_NO_2(10),
	ZIP_3(11),
	IR_CODE(12),
	ROAD(13),
	USE_CODE(14),
	BUILD_TYPE(15),
	MTRL(16),
	FLOOR_NO(17),
	TOTAL_FLOOR(18),
	BUILD_DATE(19),
	SETTING_DATE(20),
	CLAIM_DATE(21),
	FLOOR_AREA(22),
	UNIT_PRICE(23),
	EVA_AMT(24),
	TAX(25),
	NET_VALUE(26),
	RENT_PRICE(27),
	RENT_EVA_VALUE(28),
	LOAN_TO_VALUE(29),
	LOAN_AMT(30),
	SETTING_AMT(31),
	REPAY_AMT(32),
	INSU_NO(33),
	INSU_COMPANY(34),
	INSU_TYPE(35),
	FIRE_INSU_AMT(36),
	FIRE_INSU_EXPENSE(37),
	EARTHQUAKE_INSU_AMT(38),
	EARTHQUAKE_INSU_EXPENSE(39),
	INSU_START(40),
	INSU_END(41),
	OWNER_TYPE_1(42),
	OWNER_ID_1(43),
	OWNER_NAME_1(44),
	OWNER_RELATION_1(45),
	OWNER_PARTIAL_1(46),
	OWNER_TYPE_2(47)
	;

    public final int index;
    
	L2419Column(int idx) {
        index = idx;
	}
	
    public int getIndex(){
        return index;
    }
}
