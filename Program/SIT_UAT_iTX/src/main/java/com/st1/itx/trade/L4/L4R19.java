package com.st1.itx.trade.L4;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.Guarantor;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.GuarantorService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L4R19")
@Scope("prototype")
/**
 * 
 * 
 * @author Zijin
 * @version 1.0.0
 */
public class L4R19 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L4R19.class);

	@Autowired
	public Parse parse;

	@Autowired
	public FacMainService facMainService;

	/* DB服務注入 */
	@Autowired
	public CustMainService custMainService;

	/* DB服務注入 */
	@Autowired
	public GuarantorService guarantorService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4R19 ");
		this.totaVo.init(titaVo);

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 500;

//		QC.692 L4210
//		1.輸入戶號為本人
//		2.輸入戶號為保證人---->戶號->客戶檔Ukey->保證人檔.核准號碼->額度主檔之戶號->輸入ID之戶號是否與輸入相同

		int iCustNo = parse.stringToInteger(titaVo.getParam("RimCustNo"));
		String iCustId = titaVo.getParam("RimCustId");
		int result = 0;

		CustMain tCustMain = new CustMain();
		Slice<Guarantor> sGuarantor = null;
		List<Guarantor> lGuarantor = new ArrayList<Guarantor>();

		tCustMain = custMainService.custNoFirst(iCustNo, iCustNo, titaVo);

		if (tCustMain != null) {
//			1.輸入戶號為本人
			if (tCustMain.getCustId().equals(iCustId)) {
				result = 1;
			}
//			2.輸入戶號為保證人
			else {
				sGuarantor = guarantorService.guaUKeyEq(tCustMain.getCustUKey(), this.index, this.limit, titaVo);
				lGuarantor = sGuarantor == null ? null : sGuarantor.getContent();

				if (lGuarantor != null && lGuarantor.size() != 0) {
					// 統編取客戶主檔custukey
					tCustMain = new CustMain();
					tCustMain = custMainService.custIdFirst(iCustId, titaVo);

					for (Guarantor tGuarantor : lGuarantor) {
						FacMain tFacMain = new FacMain();
						tFacMain = facMainService.facmApplNoFirst(tGuarantor.getApproveNo(), titaVo);

						if (tFacMain != null) {
							if (tFacMain.getCustNo() == tCustMain.getCustNo()) {
								result = 1;
								break;
							}
						} else {
							this.info("無此核准號碼於額度檔...");
						}
					}
				}
			}
		}

		this.totaVo.putParam("L4r19CheckCode", result);

		this.addList(this.totaVo);
		return this.sendList();
	}
}