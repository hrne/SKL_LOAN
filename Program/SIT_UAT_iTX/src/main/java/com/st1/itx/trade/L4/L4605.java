package com.st1.itx.trade.L4;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.InsuRenew;
import com.st1.itx.db.service.InsuRenewService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.FileCom;
import com.st1.itx.util.common.data.InsuRenewFileVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L4605")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4605 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L4605.class);
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public Parse parse;

	@Autowired
	public FileCom fileCom;

	@Autowired
	public InsuRenewFileVo insuRenewFileVo;

	@Autowired
	public InsuRenewService insuRenewService;

	private int iInsuEndMonth = 0;
	private int cnt = 0;

	@Value("${iTXInFolder}")
	private String inFolder = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4605 ");
		this.totaVo.init(titaVo);
		HashMap<tmpInsu, Integer> error = new HashMap<>();
		HashMap<tmpInsu, String> alrdyInsu = new HashMap<>();
//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = Integer.MAX_VALUE;

		iInsuEndMonth = parse.stringToInteger(titaVo.getParam("InsuEndMonth")) + 191100;

//		吃檔
//		String filePath1 = "D:\\temp\\test\\火險\\Test\\Return\\201912_final.txt";
		String filePath1 = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo()
				+ File.separatorChar + titaVo.getParam("FILENA").trim();

		ArrayList<String> dataLineList = new ArrayList<>();

//		 編碼參數，設定為UTF-8 || big5
		try {
			dataLineList = fileCom.intputTxt(filePath1, "big5");
		} catch (IOException e) {
			throw new LogicException("E0014", "L4605(" + filePath1 + ") " + e.getMessage());
		}

//		 使用資料容器內定義的方法切資料
		insuRenewFileVo.setValueFromFile(dataLineList);

		ArrayList<OccursList> uploadFile = insuRenewFileVo.getOccursList();

		if (uploadFile != null && uploadFile.size() != 0) {
			for (OccursList tempOccursList : uploadFile) {
				String insuNo = tempOccursList.get("InsuNo");
//				file檔為NewInusNo
				String newInsuNo = tempOccursList.get("NewInusNo");

				if (insuNo != null && !"".equals(insuNo)) {
					insuNo = insuNo.trim();
				}
				if (newInsuNo != null && !"".equals(newInsuNo)) {
					newInsuNo = newInsuNo.trim();
				}

				tmpInsu tmp = new tmpInsu(parse.stringToInteger(tempOccursList.get("CustNo")),
						parse.stringToInteger(tempOccursList.get("FacmNo")), insuNo, newInsuNo);
//				Initial
				error.put(tmp, 0);

				if (iInsuEndMonth != parse.stringToInteger(tempOccursList.get("FireInsuMonth"))) {
					error.put(tmp, 1);
					continue;
				}

				Slice<InsuRenew> sInsuRenew = null;

				List<InsuRenew> lInsuRenew = new ArrayList<InsuRenew>();

//				續保檔若調整內容，增加有批單號碼的筆數。
//				提出檔合為一筆，回傳時以List回頭update續保檔
				sInsuRenew = insuRenewService.findL4605A(parse.stringToInteger(tempOccursList.get("ClCode1")),
						parse.stringToInteger(tempOccursList.get("ClCode2")),
						parse.stringToInteger(tempOccursList.get("ClNo")), insuNo, this.index, this.limit, titaVo);

				lInsuRenew = sInsuRenew == null ? null : sInsuRenew.getContent();

				if (lInsuRenew != null && lInsuRenew.size() != 0) {
					for (InsuRenew tInsuRenew : lInsuRenew) {

						tInsuRenew = insuRenewService.holdById(tInsuRenew.getInsuRenewId());

						this.info("tInsuRenew : " + tInsuRenew.toString());
						if (!"".equals(tInsuRenew.getNowInsuNo().trim())
								&& !tInsuRenew.getNowInsuNo().equals(newInsuNo)) {
							error.put(tmp, 4);
							alrdyInsu.put(tmp, newInsuNo);
							continue;
						}
						if (tInsuRenew.getRenewCode() == 1) {
							error.put(tmp, 5);
							continue;
						}
						if (tInsuRenew.getTotInsuPrem().compareTo(BigDecimal.ZERO) == 0) {
							error.put(tmp, 6);
							continue;
						}
						BigDecimal totFee = tInsuRenew.getEthqInsuPrem().add(tInsuRenew.getFireInsuPrem());

						if (tInsuRenew.getTotInsuPrem().compareTo(totFee) != 0) {
							this.info("EthqInsuPrem : " + tInsuRenew.getEthqInsuPrem());
							this.info("FireInsuPrem : " + tInsuRenew.getFireInsuPrem());
							this.info("totFee : " + totFee);
							this.info("NewTotalFee : " + parse.stringToBigDecimal(tempOccursList.get("NewTotalFee")));
							error.put(tmp, 7);
							continue;
						}

						if (tInsuRenew.getInsuEndDate() + 19110000 != parse
								.stringToInteger(tempOccursList.get("NewInsuEndDate").replace("/", ""))) {
							this.info("InsuEndDate : " + tInsuRenew.getEthqInsuPrem() + 19110000);
							this.info("NewInsuEndDate : "
									+ parse.stringToInteger(tempOccursList.get("NewInsuEndDate").replace("/", "")));
							error.put(tmp, 10);
							continue;
						}
						if (tInsuRenew.getFireInsuCovrg()
								.compareTo(parse.stringToBigDecimal(tempOccursList.get("NewFireInsuAmt"))) != 0
								|| tInsuRenew.getFireInsuPrem().compareTo(
										parse.stringToBigDecimal(tempOccursList.get("NewFireInsuFee"))) != 0) {
							error.put(tmp, 11);
							continue;
						}
						if (tInsuRenew.getEthqInsuCovrg()
								.compareTo(parse.stringToBigDecimal(tempOccursList.get("NewEqInsuAmt"))) != 0
								|| tInsuRenew.getEthqInsuPrem()
										.compareTo(parse.stringToBigDecimal(tempOccursList.get("NewEqInsuFee"))) != 0) {
							error.put(tmp, 12);
							continue;
						}
						if (tInsuRenew.getTotInsuPrem()
								.compareTo(parse.stringToBigDecimal(tempOccursList.get("NewTotalFee"))) != 0) {
							error.put(tmp, 13);
							continue;
						}
						if (!"".equals(tInsuRenew.getNowInsuNo().trim())
								&& tInsuRenew.getNowInsuNo().equals(newInsuNo)) {
							error.put(tmp, 14);
							alrdyInsu.put(tmp, newInsuNo);
							continue;
						}

						tInsuRenew.setNowInsuNo(newInsuNo);

						try {
							insuRenewService.update(tInsuRenew);
						} catch (DBException e) {
							throw new LogicException("E0007", e.getErrorMsg());
						}
					}
				} else {
					error.put(tmp, 3);
					continue;
				}
			}
			Set<tmpInsu> tempSet = error.keySet();

			List<tmpInsu> tempList = new ArrayList<>();

			for (Iterator<tmpInsu> it = tempSet.iterator(); it.hasNext();) {
				tmpInsu tmpInsuVo = it.next();
				tempList.add(tmpInsuVo);
			}

			this.info("L4605 tempList size = " + tempList.size());

			for (tmpInsu tempL4605Vo : tempList) {
				OccursList occursList = new OccursList();

				this.info("L4605 errorCode = " + error.get(tempL4605Vo));

				if (error.get(tempL4605Vo) != 0) {

					cnt = cnt + 1;

					occursList.putParam("ReportCustNo", tempL4605Vo.getCustNo());
					occursList.putParam("ReportFacmNo", tempL4605Vo.getFacmNo());
					occursList.putParam("ReportPrvInsuNo", tempL4605Vo.getpInsuNo());
					if (error.get(tempL4605Vo) == 4 || error.get(tempL4605Vo) == 14) {
						occursList.putParam("ReportAlrInsuNo", alrdyInsu.get(tempL4605Vo));
					} else {
						occursList.putParam("ReportAlrInsuNo", "");
					}
					occursList.putParam("ReportNewInsuNo", tempL4605Vo.getnInsuNo());

					switch (error.get(tempL4605Vo)) {
					case 1:
						occursList.putParam("ErrorMsg", "檔案與畫面年月不符");
						break;
					case 2:
						occursList.putParam("ErrorMsg", "資料有誤，請人工檢核調整");
						break;
					case 3:
						occursList.putParam("ErrorMsg", "不存在火險明細檔");
						break;
					case 4:
						occursList.putParam("ErrorMsg", "與之前上的新保單號碼不同");
						break;
					case 5:
						occursList.putParam("ErrorMsg", "此為不處理之保單資料");
						break;
					case 6:
						occursList.putParam("ErrorMsg", "總保費=0");
						break;
					case 7:
						occursList.putParam("ErrorMsg", "總保費不等於火險保費+地震險保費");
						break;
					case 8:
						occursList.putParam("ErrorMsg", "火險保額與保費不符");
						break;
					case 9:
						occursList.putParam("ErrorMsg", "地震險保額與保費不符");
						break;
					case 10:
						occursList.putParam("ErrorMsg", "上傳之保險起迄日與檔案資料不符");
						break;
					case 11:
						occursList.putParam("ErrorMsg", "上傳之火險保額或保費與檔案資料不符");
						break;
					case 12:
						occursList.putParam("ErrorMsg", "上傳地震險保額或保費與檔案資料不符");
						break;
					case 13:
						occursList.putParam("ErrorMsg", "上傳總保費與檔案資料不符");
						break;
					case 14:
						occursList.putParam("ErrorMsg", "保單資料中已有新保單之保單號碼");
						break;
					default:
						occursList.putParam("ErrorMsg", "");
						break;
					}

					this.totaVo.addOccursList(occursList);

				} else {
					continue;
				}
			}
		}

//		傳回var控制錯誤清單顯示與否
		this.totaVo.putParam("OCnt", cnt);

		this.addList(this.totaVo);
		return this.sendList();
	}

//	暫時紀錄戶號額度
	private class tmpInsu {

		private int custNo = 0;
		private int facmNo = 0;
		private String pInsuNo = "";
//		private String aInsuNo = "";
		private String nInsuNo = "";

		public tmpInsu(int custNo, int facmNo, String pInsuNo, String nInsuNo) {
			this.setCustNo(custNo);
			this.setFacmNo(facmNo);
			this.setpInsuNo(pInsuNo);
			this.setnInsuNo(nInsuNo);
		}

		public int getCustNo() {
			return custNo;
		}

		public void setCustNo(int custNo) {
			this.custNo = custNo;
		}

		public int getFacmNo() {
			return facmNo;
		}

		public void setFacmNo(int facmNo) {
			this.facmNo = facmNo;
		}

		public String getpInsuNo() {
			return pInsuNo;
		}

		public void setpInsuNo(String pInsuNo) {
			this.pInsuNo = pInsuNo;
		}

		public String getnInsuNo() {
			return nInsuNo;
		}

		public void setnInsuNo(String nInsuNo) {
			this.nInsuNo = nInsuNo;
		}
	}
}