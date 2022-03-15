package com.st1.itx.trade.L4;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/*
 * RimCustId=X,10
 * RimCustNo=9,7
 * */

@Service("L4R02")
@Scope("prototype")
public class L4R02 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L4R02.class);

	/* DB服務注入 */
	@Autowired
	public CustMainService custMainService;

	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4R02 ");
		this.totaVo.init(titaVo);

		// RimCustId=X,10
		String iCustId = titaVo.get("RimCustId").trim();

		// RimCustNo=9,7
		int iCustNo = parse.stringToInteger(titaVo.get("RimCustNo").trim());

		// RimEntryCode=9,1 統編為1(不一定有戶號)，戶號為2必須有值
		int iRimEntryCode = parse.stringToInteger(titaVo.get("RimEntryCode").trim());

		// 預設為N，有資料改為Y，For前端Var使用
		this.totaVo.putParam("L4r02ExistFlag", "N");

		CustMain tCustMain = new CustMain();

		/* 如有有找到資料 */
		if (iRimEntryCode == 1) {

			if (!iCustId.isEmpty()) {
				tCustMain = custMainService.custIdFirst(iCustId);
			} else {
				/*
				 * E0001 查詢資料不存在 E0002 新增資料已存在 E0003 修改資料不存在 E0004 刪除資料不存在
				 */
				throw new LogicException(titaVo, "E0001", " 請輸入統編");
			}

			if (tCustMain != null) {

				/* 存入Tota */
				/* key 名稱需與L4R02.tom相同 */

				this.totaVo.putParam("L4r02CustName", tCustMain.getCustName());
				this.totaVo.putParam("L4r02CustNo", tCustMain.getCustNo());
				this.totaVo.putParam("L4r02ExistFlag", "Y");

			} else {
				this.totaVo.putParam("L4r02CustName", "");
				this.totaVo.putParam("L4r02CustNo", "000000");
				this.totaVo.putParam("L4r02ExistFlag", "N");
			}
		} else if (iRimEntryCode == 2) {

			if (iCustNo > 0) {
				tCustMain = custMainService.custNoFirst(iCustNo, iCustNo);
			} else {
				/*
				 * E0001 查詢資料不存在 E0002 新增資料已存在 E0003 修改資料不存在 E0004 刪除資料不存在
				 */
				throw new LogicException(titaVo, "E0001", " 請輸入戶號");
			}

			if (tCustMain != null) {

				/* 存入Tota */
				/* key 名稱需與L4R02.tom相同 */

				this.totaVo.putParam("L4r02CustName", tCustMain.getCustName());
				this.totaVo.putParam("L4r02CustNo", tCustMain.getCustNo());
			} else {
				this.info("L4R02(CustMain) null");
				this.totaVo.putParam("L4r02CustName", " ");
				this.totaVo.putParam("L4r02CustNo", "000000");

//				throw new LogicException(titaVo, "E0001", "L4R02(CustMain) null");

			}
		}

		this.addList(this.totaVo);

		return this.sendList();
	}
}