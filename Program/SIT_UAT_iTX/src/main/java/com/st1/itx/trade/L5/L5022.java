package com.st1.itx.trade.L5;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.PfCoOfficer;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.PfCoOfficerService;
import com.st1.itx.db.service.springjpa.cm.L5022ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;

@Component("L5022")
@Scope("prototype")

/**
 * 放款專員業績統計作業－協辦人員等級明細資料查詢
 * 
 * @author Fegie
 * @version 1.0.0
 */

public class L5022 extends TradeBuffer {
	/* 轉型共用工具 */
	@Autowired
	public L5022ServiceImpl iL5022ServiceImpl;
	@Autowired
	public PfCoOfficerService iPfCoOfficerService;
	@Autowired
	public CdEmpService iCdEmpService;

	// 輸入的員工編號必須存在於員工檔

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.totaVo.init(titaVo);
		String iEmpNo = titaVo.getParam("EmpNo");
		int cDate = Integer.valueOf(titaVo.getEntDy()) + 19110000;
		int iEffectiveDateS = Integer.valueOf(titaVo.getParam("EffectiveDateS"))+19110000;
		int iEffectiveDateE = Integer.valueOf(titaVo.getParam("EffectiveDateE"))+19110000;
		String iStatusFg = titaVo.getParam("StatusFlag");
		
		this.info("cDate=" + cDate);
		List<Map<String, String>> iL5022SqlReturn = new ArrayList<Map<String, String>>();
		Slice<PfCoOfficer> sPfCoOfficer = null;
		CdEmp iCdEmp = new CdEmp();
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 40;

		if (iStatusFg.trim().isEmpty() && iEffectiveDateS==19110000) { //狀態&生效期間皆無輸入
			if (iEmpNo.equals("")) {
				// user無輸入則查PfCoOfficer全部，且只顯示最近的一筆
				try {
					iL5022SqlReturn = iL5022ServiceImpl.officerNoBlank(cDate, this.index, this.limit, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E5004", "");
				}
			} else {
				// user有輸入則只顯示該員工在PfCoOfficer裡全部資料，由近到遠
				try {
					iL5022SqlReturn = iL5022ServiceImpl.officerNo(iEmpNo, titaVo);
				} catch (Exception e) {
					throw new LogicException(titaVo, "E5004", "");
				}
			}
	
			if (iL5022SqlReturn.size() == 0) {
				throw new LogicException(titaVo, "E0001", "協辦人員等級檔查無 " + iEmpNo + " 資料");
			}
	
			if (iL5022SqlReturn != null && iL5022SqlReturn.size() >= this.limit) {
				/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
				titaVo.setReturnIndex(this.setIndexNext());
				this.totaVo.setMsgEndToEnter();// 手動折返
			}
			for (Map<String, String> r5022SqlReturn : iL5022SqlReturn) {
	
				OccursList occursList = new OccursList();
				occursList.putParam("OOEmpNo", r5022SqlReturn.get("F0"));
				if (r5022SqlReturn.get("F1").equals("") || r5022SqlReturn.get("F1").equals("0")) {
					occursList.putParam("OOEffectiveDate", "");
				} else {
					occursList.putParam("OOEffectiveDate", Integer.valueOf(r5022SqlReturn.get("F1")) - 19110000);
				}
				occursList.putParam("OOEmpClass", r5022SqlReturn.get("F2"));
				occursList.putParam("OOClassPass", r5022SqlReturn.get("F3"));
				occursList.putParam("OOFullname", r5022SqlReturn.get("F4"));
				occursList.putParam("OOUnitCode", r5022SqlReturn.get("F6"));
				occursList.putParam("OODistCode", r5022SqlReturn.get("F7"));
				occursList.putParam("OODeptCode", r5022SqlReturn.get("F9"));
				occursList.putParam("OOUnitCodeX", r5022SqlReturn.get("F5"));
				occursList.putParam("OODistCodeX", r5022SqlReturn.get("F8"));
				occursList.putParam("OODeptCodeX", r5022SqlReturn.get("F10"));
				if (r5022SqlReturn.get("F11").equals("") || r5022SqlReturn.get("F11").equals("0")) {
					occursList.putParam("OOIneffectiveDate", "");
				} else {
					occursList.putParam("OOIneffectiveDate", Integer.valueOf(r5022SqlReturn.get("F11")) - 19110000);
				}
				// 1:已生效 2:已停效 3:未生效
				int a = Integer.valueOf(cDate); // 會計日
				int b = Integer.valueOf(r5022SqlReturn.get("F1")); // 生效日
				int c = Integer.valueOf(r5022SqlReturn.get("F11")); // 停效日
				if (c == 0) {
					if (a < b) {
						occursList.putParam("OOStatusFg", 3);
					} else {
						occursList.putParam("OOStatusFg", 1);
					}
				} else {
					if (a < b) {
						occursList.putParam("OOStatusFg", 3);
					} else {
						if (a > c) {
							occursList.putParam("OOStatusFg", 2);
						} else {
							occursList.putParam("OOStatusFg", 1);
						}
					}
				}
				this.totaVo.addOccursList(occursList);
			}
		} else {
			if (iStatusFg.trim().isEmpty()) { //生效期間
				if (iEmpNo.trim().isEmpty()){ //員編未輸入
					sPfCoOfficer = iPfCoOfficerService.findByEffectiveDateDate(iEffectiveDateS, iEffectiveDateE, this.index, this.limit, titaVo);
				}else {
					sPfCoOfficer = iPfCoOfficerService.effectiveDateEq(iEmpNo, iEffectiveDateS, iEffectiveDateE, this.index, this.limit, titaVo);
				}
			} else { //狀態
				if (iEmpNo.trim().isEmpty()){ //員編未輸入
					sPfCoOfficer = iPfCoOfficerService.findAll(this.index, this.limit, titaVo);
				}else {
					sPfCoOfficer = iPfCoOfficerService.findByEmpNo(iEmpNo,this.index, this.limit, titaVo);
				}
			}
			if (sPfCoOfficer == null) {
				throw new LogicException(titaVo, "E0001", "查無資料");
			}
			for (PfCoOfficer rPfCoOfficer:sPfCoOfficer) {
				OccursList occursList = new OccursList();
				if (iStatusFg.trim().isEmpty() || iStatusFg.equals("9")) { //回傳生效日期間查詢結果 ，狀態無輸入或等於9.全部
					occursList.putParam("OOEmpNo", rPfCoOfficer.getEmpNo());
					occursList.putParam("OOEffectiveDate", Integer.valueOf(rPfCoOfficer.getEffectiveDate()));
					occursList.putParam("OOEmpClass", rPfCoOfficer.getEmpClass());
					occursList.putParam("OOClassPass", rPfCoOfficer.getClassPass());				
					iCdEmp = iCdEmpService.findById(rPfCoOfficer.getEmpNo(), titaVo);
					if (iCdEmp == null) {
						occursList.putParam("OOFullname", "");
					}else {
						occursList.putParam("OOFullname", iCdEmp.getFullname());
					}
					occursList.putParam("OOUnitCode", rPfCoOfficer.getAreaCode());
					occursList.putParam("OODistCode", rPfCoOfficer.getDistCode());
					occursList.putParam("OODeptCode", rPfCoOfficer.getDeptCode());
					occursList.putParam("OOUnitCodeX", rPfCoOfficer.getAreaItem());
					occursList.putParam("OODistCodeX", rPfCoOfficer.getDistItem());
					occursList.putParam("OODeptCodeX", rPfCoOfficer.getDeptItem());
					occursList.putParam("OOIneffectiveDate", Integer.valueOf(rPfCoOfficer.getIneffectiveDate()));
					// 1:已生效 2:已停效 3:未生效
					int a = Integer.valueOf(cDate); // 會計日
					int b = Integer.valueOf(rPfCoOfficer.getEffectiveDate())+19110000; // 生效日
					int c = Integer.valueOf(rPfCoOfficer.getIneffectiveDate())+19110000; // 停效日
					if (c == 19110000) {
						if (a < b) {
							occursList.putParam("OOStatusFg", 3);
						} else {
							occursList.putParam("OOStatusFg", 1);
						}
					} else {
						if (a < b) {
							occursList.putParam("OOStatusFg", 3);
						} else {
							if (a > c) {
								occursList.putParam("OOStatusFg", 2);
							} else {
								occursList.putParam("OOStatusFg", 1);
							}
		
						}
					}
				}else { //回傳全部資料或單員工所有資料，依狀態選項篩選
					int a = Integer.valueOf(cDate); // 會計日
					int b = Integer.valueOf(rPfCoOfficer.getEffectiveDate())+19110000; // 生效日
					int c = Integer.valueOf(rPfCoOfficer.getIneffectiveDate())+19110000; // 停效日
					switch(iStatusFg) {
					case "1":
						if (c == 19110000) {
							if (a > b) {
								occursList.putParam("OOEmpNo", rPfCoOfficer.getEmpNo());
								occursList.putParam("OOEffectiveDate", Integer.valueOf(rPfCoOfficer.getEffectiveDate()));
								occursList.putParam("OOEmpClass", rPfCoOfficer.getEmpClass());
								occursList.putParam("OOClassPass", rPfCoOfficer.getClassPass());				
								iCdEmp = iCdEmpService.findById(rPfCoOfficer.getEmpNo(), titaVo);
								if (iCdEmp == null) {
									occursList.putParam("OOFullname", "");
								}else {
									occursList.putParam("OOFullname", iCdEmp.getFullname());
								}
								occursList.putParam("OOUnitCode", rPfCoOfficer.getAreaCode());
								occursList.putParam("OODistCode", rPfCoOfficer.getDistCode());
								occursList.putParam("OODeptCode", rPfCoOfficer.getDeptCode());
								occursList.putParam("OOUnitCodeX", rPfCoOfficer.getAreaItem());
								occursList.putParam("OODistCodeX", rPfCoOfficer.getDistItem());
								occursList.putParam("OODeptCodeX", rPfCoOfficer.getDeptItem());
								occursList.putParam("OOIneffectiveDate", Integer.valueOf(rPfCoOfficer.getIneffectiveDate()));
								occursList.putParam("OOStatusFg", 1);
							}else {
								continue;
							}
						} else {
							if (a > b) {
								if (a < c) {
									occursList.putParam("OOEmpNo", rPfCoOfficer.getEmpNo());
									occursList.putParam("OOEffectiveDate", Integer.valueOf(rPfCoOfficer.getEffectiveDate()));
									occursList.putParam("OOEmpClass", rPfCoOfficer.getEmpClass());
									occursList.putParam("OOClassPass", rPfCoOfficer.getClassPass());				
									iCdEmp = iCdEmpService.findById(rPfCoOfficer.getEmpNo(), titaVo);
									if (iCdEmp == null) {
										occursList.putParam("OOFullname", "");
									}else {
										occursList.putParam("OOFullname", iCdEmp.getFullname());
									}
									occursList.putParam("OOUnitCode", rPfCoOfficer.getAreaCode());
									occursList.putParam("OODistCode", rPfCoOfficer.getDistCode());
									occursList.putParam("OODeptCode", rPfCoOfficer.getDeptCode());
									occursList.putParam("OOUnitCodeX", rPfCoOfficer.getAreaItem());
									occursList.putParam("OODistCodeX", rPfCoOfficer.getDistItem());
									occursList.putParam("OODeptCodeX", rPfCoOfficer.getDeptItem());
									occursList.putParam("OOIneffectiveDate", Integer.valueOf(rPfCoOfficer.getIneffectiveDate()));
									occursList.putParam("OOStatusFg", 1);
								}else {
									continue;
								}
							}else {
								continue;
							}
						}
						break;
					case "2":
						if (c != 19110000) {					
							if (a > b) {
								if (a > c) {
									occursList.putParam("OOEmpNo", rPfCoOfficer.getEmpNo());
									occursList.putParam("OOEffectiveDate", Integer.valueOf(rPfCoOfficer.getEffectiveDate()));
									occursList.putParam("OOEmpClass", rPfCoOfficer.getEmpClass());
									occursList.putParam("OOClassPass", rPfCoOfficer.getClassPass());				
									iCdEmp = iCdEmpService.findById(rPfCoOfficer.getEmpNo(), titaVo);
									if (iCdEmp == null) {
										occursList.putParam("OOFullname", "");
									}else {
										occursList.putParam("OOFullname", iCdEmp.getFullname());
									}
									occursList.putParam("OOUnitCode", rPfCoOfficer.getAreaCode());
									occursList.putParam("OODistCode", rPfCoOfficer.getDistCode());
									occursList.putParam("OODeptCode", rPfCoOfficer.getDeptCode());
									occursList.putParam("OOUnitCodeX", rPfCoOfficer.getAreaItem());
									occursList.putParam("OODistCodeX", rPfCoOfficer.getDistItem());
									occursList.putParam("OODeptCodeX", rPfCoOfficer.getDeptItem());
									occursList.putParam("OOIneffectiveDate", Integer.valueOf(rPfCoOfficer.getIneffectiveDate()));
									occursList.putParam("OOStatusFg", 2);
								}else {
									continue;
								}
							}else {
								continue;
							}
						}else {
							continue;
						}
						break;
					case "3":
						if (c == 19110000) {
							if (a < b) {
								occursList.putParam("OOEmpNo", rPfCoOfficer.getEmpNo());
								occursList.putParam("OOEffectiveDate", Integer.valueOf(rPfCoOfficer.getEffectiveDate()));
								occursList.putParam("OOEmpClass", rPfCoOfficer.getEmpClass());
								occursList.putParam("OOClassPass", rPfCoOfficer.getClassPass());				
								iCdEmp = iCdEmpService.findById(rPfCoOfficer.getEmpNo(), titaVo);
								if (iCdEmp == null) {
									occursList.putParam("OOFullname", "");
								}else {
									occursList.putParam("OOFullname", iCdEmp.getFullname());
								}
								occursList.putParam("OOUnitCode", rPfCoOfficer.getAreaCode());
								occursList.putParam("OODistCode", rPfCoOfficer.getDistCode());
								occursList.putParam("OODeptCode", rPfCoOfficer.getDeptCode());
								occursList.putParam("OOUnitCodeX", rPfCoOfficer.getAreaItem());
								occursList.putParam("OODistCodeX", rPfCoOfficer.getDistItem());
								occursList.putParam("OODeptCodeX", rPfCoOfficer.getDeptItem());
								occursList.putParam("OOIneffectiveDate", Integer.valueOf(rPfCoOfficer.getIneffectiveDate()));
								occursList.putParam("OOStatusFg", 3);
							}else {
								continue;
							}
						} else {
							if (a < b) {
								occursList.putParam("OOEmpNo", rPfCoOfficer.getEmpNo());
								occursList.putParam("OOEffectiveDate", Integer.valueOf(rPfCoOfficer.getEffectiveDate()));
								occursList.putParam("OOEmpClass", rPfCoOfficer.getEmpClass());
								occursList.putParam("OOClassPass", rPfCoOfficer.getClassPass());				
								iCdEmp = iCdEmpService.findById(rPfCoOfficer.getEmpNo(), titaVo);
								if (iCdEmp == null) {
									occursList.putParam("OOFullname", "");
								}else {
									occursList.putParam("OOFullname", iCdEmp.getFullname());
								}
								occursList.putParam("OOUnitCode", rPfCoOfficer.getAreaCode());
								occursList.putParam("OODistCode", rPfCoOfficer.getDistCode());
								occursList.putParam("OODeptCode", rPfCoOfficer.getDeptCode());
								occursList.putParam("OOUnitCodeX", rPfCoOfficer.getAreaItem());
								occursList.putParam("OODistCodeX", rPfCoOfficer.getDistItem());
								occursList.putParam("OODeptCodeX", rPfCoOfficer.getDeptItem());
								occursList.putParam("OOIneffectiveDate", Integer.valueOf(rPfCoOfficer.getIneffectiveDate()));
								occursList.putParam("OOStatusFg", 3);
							}else {
								continue;
							}
						}
						break;
					}
				}
				this.totaVo.addOccursList(occursList);
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}
