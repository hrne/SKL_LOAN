package com.st1.itx.trade.L4;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.InsuRenew;
import com.st1.itx.db.domain.InsuRenewMediaTemp;
import com.st1.itx.db.service.CdAreaService;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.ClBuildingOwnerService;
import com.st1.itx.db.service.ClBuildingParkingService;
import com.st1.itx.db.service.ClBuildingPublicService;
import com.st1.itx.db.service.ClBuildingService;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.InsuOrignalService;
import com.st1.itx.db.service.InsuRenewMediaTempService;
import com.st1.itx.db.service.InsuRenewService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.CustNoticeCom;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.common.data.InsuRenewFileVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Service("L4602Batch")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4602Batch extends TradeBuffer {

	@Autowired
	public Parse parse;

	@Autowired
	public InsuRenewService insuRenewService;

	@Autowired
	public CustMainService custMainService;

	@Autowired
	public CustNoticeCom custNoticeCom;

	@Autowired
	public ClBuildingService clBuildingService;

	@Autowired
	public ClBuildingParkingService clBuildingParkingService;

	@Autowired
	public ClBuildingPublicService clBuildingPublicService;

	@Autowired
	public CdEmpService cdEmpService;

	@Autowired
	public ClBuildingOwnerService clBuildingOwnerService;

	@Autowired
	public InsuOrignalService insuOrignalService;

	@Autowired
	public ClFacService clFacService;

	@Autowired
	public LoanBorMainService loanBorMainService;

	@Autowired
	public InsuRenewFileVo insuRenewFileVo;

	@Autowired
	public MakeFile makeFile;

	@Autowired
	DateUtil dateUtil;

	@Autowired
	public CdAreaService cdAreaService;

	@Autowired
	public CdCityService cdCityService;

	@Autowired
	public InsuRenewMediaTempService insuRenewMediaTempService;

	@Autowired
	public L4600Batch l4600Batch;

	@Autowired
	public L4601Batch l4601Batch;

	@Autowired
	public WebClient webClient;

	private ArrayList<InsuRenewMediaTemp> lInsuRenewMediaTemp = new ArrayList<>();
	private ArrayList<OccursList> tmpList = new ArrayList<>();
	private List<InsuRenew> lInsuRenew = new ArrayList<InsuRenew>();
	private int iInsuEndMonth = 0;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4602Batch ");
		this.totaVo.init(titaVo);

		iInsuEndMonth = parse.stringToInteger(titaVo.getParam("InsuEndMonth")) + 191100;

		// 刪除暫存檔
		Slice<InsuRenewMediaTemp> slInsuRenewMediaTemp = insuRenewMediaTempService.fireInsuMonthRg("" + iInsuEndMonth, "" + iInsuEndMonth, 0, Integer.MAX_VALUE, titaVo);
		if (slInsuRenewMediaTemp != null) {
			try {
				insuRenewMediaTempService.deleteAll(slInsuRenewMediaTemp.getContent(), titaVo);
			} catch (DBException e) {
				throw new LogicException("E0008", "InsuRenew : " + e.getErrorMsg());
			}
		}

//		出表明細
		Slice<InsuRenew> slInsuRenew = insuRenewService.selectC(iInsuEndMonth, 0, Integer.MAX_VALUE, titaVo);

		if (slInsuRenew != null) {
			for (InsuRenew t : slInsuRenew.getContent()) {
//				排除自保件
				if (t.getRenewCode() == 1) {
					continue;
				}

				OccursList occursList = new OccursList();
				occursList = l4600Batch.getOccurs("L4602", occursList, t, titaVo);
				tmpList.add(occursList);
				InsuRenewMediaTemp tInsuRenewMediaTemp = new InsuRenewMediaTemp();
				tInsuRenewMediaTemp = l4601Batch.getInsuRenewMediaTemp(occursList, tInsuRenewMediaTemp, titaVo);

				TempVo tempVo = new TempVo();
				tempVo = custNoticeCom.getCustNotice("L4603", t.getCustNo(), t.getFacmNo(), titaVo);
				int noticeFlag = parse.stringToInteger(tempVo.getParam("NoticeFlag"));
				tInsuRenewMediaTemp.setRepayCode(t.getRepayCode());
				tInsuRenewMediaTemp.setNoticeFlag(noticeFlag);
				lInsuRenewMediaTemp.add(tInsuRenewMediaTemp);
				lInsuRenew.add(t);
			}
		}

		if (lInsuRenewMediaTemp.size() > 0) {
			try {
				insuRenewMediaTempService.insertAll(lInsuRenewMediaTemp, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "InsuRenew : " + e.getErrorMsg());
			}
		}
		for (InsuRenewMediaTemp t : slInsuRenewMediaTemp.getContent()) {
			OccursList occursListReport = new OccursList();
			occursListReport.putParam("OOClCode1", t.getClCode1());
			occursListReport.putParam("OOClCode2", t.getClCode2());
			occursListReport.putParam("OOClNo", t.getClNo());
			occursListReport.putParam("OOPrevInsuNo", t.getInsuNo());
			occursListReport.putParam("OOCustNo", t.getCustNo());
			occursListReport.putParam("OOFacmNo", t.getFacmNo());
			// occursListReport.putParam("OORepayCodeX", getRepayCode(t.getRepayCode()));
			occursListReport.putParam("OOCustName", t.getLoanCustName());
			// occursListReport.putParam("OONewInsuStartDate", InsuStartDate);
			// occursListReport.putParam("OONewInsuEndDate", InsuEndDate);
			occursListReport.putParam("OOFireAmt", t.getNewEqInsuAmt());
			occursListReport.putParam("OOFireFee", t.getNewFireInsuFee());
			occursListReport.putParam("OOEthqAmt", t.getNewEqInsuAmt());
			occursListReport.putParam("OOEthqFee", t.getNewEqInsuFee());
			occursListReport.putParam("OOTotlFee", t.getNewTotalFee());
			// occursListReport.putParam("OONoticeWay", getNoticeWay(t.getNoticeFlag());

			this.totaVo.addOccursList(occursListReport);
		}

		// 把明細資料容器裝到檔案資料容器內
		insuRenewFileVo.setOccursList(tmpList);
		// 轉換資料格式
		ArrayList<String> file = insuRenewFileVo.toFile();

		makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxCode(), titaVo.getTxCode() + "-火險到期檔", "LNM01P.txt", 2);

		for (String line : file) {
			makeFile.put(line);
		}

		long sno = makeFile.close();

		this.info("sno : " + sno);

		makeFile.toFile(sno);
		webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009", titaVo.getTlrNo(), "L4602 已產生火險到期檔", titaVo);

		return null;
	}

}