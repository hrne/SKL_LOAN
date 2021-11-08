package com.st1.itx.trade.L4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.st1.itx.db.service.InsuRenewMediaTempService;
import com.st1.itx.db.service.InsuRenewService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.CustNoticeCom;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.common.data.InsuRenewFileVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

/**
 * L4602p
 * 
 * @author Zi-Jun,Huang ＆ Chih Wei
 * @version 1.0.0
 */
@Service("L4602p")
@Scope("prototype")
public class L4602p extends TradeBuffer {

	@Autowired
	Parse parse;

	@Autowired
	CustNoticeCom custNoticeCom;

	@Autowired
	InsuRenewService insuRenewService;

	@Autowired
	InsuRenewFileVo insuRenewFileVo;

	@Autowired
	MakeFile makeFile;

	@Autowired
	InsuRenewMediaTempService insuRenewMediaTempService;

	@Autowired
	L4600Batch l4600Batch;

	@Autowired
	L4601Batch l4601Batch;

	@Autowired
	L4600Report l4600Report;
	
	@Autowired
	DateUtil dDateUtil;

	@Autowired
	WebClient webClient;

	@Autowired
	L4602Report l4602Report;

	private ArrayList<InsuRenewMediaTemp> lInsuRenewMediaTemp = new ArrayList<>();
	private ArrayList<OccursList> tmpList = new ArrayList<>();
	private List<InsuRenew> lInsuRenew = new ArrayList<InsuRenew>();
	private int iInsuEndMonth = 0;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4602p ");
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

		// 報表明細資料容器
		List<Map<String, Object>> listL4602 = new ArrayList<>();
		boolean isFinished = false;

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

				this.info(occursList.toString());
				this.info(t.toString());
				this.info(tInsuRenewMediaTemp.toString());

				// 報表明細資料容器
				Map<String, Object> tL4602 = new HashMap<>();

				tL4602.put("OOClCode1", t.getClCode1());
				tL4602.put("OOClCode2", t.getClCode2());
				tL4602.put("OOClNo", t.getClNo());
				tL4602.put("OOPrevInsuNo", t.getPrevInsuNo());
				tL4602.put("OOCustNo", t.getCustNo());
				tL4602.put("OOFacmNo", t.getFacmNo());
				tL4602.put("OORepayCodeX", t.getRepayCode());
				tL4602.put("OOCustName", tInsuRenewMediaTemp.getLoanCustName());
				tL4602.put("OONewInsuStartDate", t.getInsuStartDate());
				tL4602.put("OONewInsuEndDate", t.getInsuEndDate());
				tL4602.put("OOFireAmt", t.getFireInsuCovrg());
				tL4602.put("OOFireFee", t.getFireInsuPrem());
				tL4602.put("OOEthqAmt", t.getEthqInsuCovrg());
				tL4602.put("OOEthqFee", t.getEthqInsuPrem());
				tL4602.put("OOTotlFee", t.getTotInsuPrem());
				tL4602.put("OONoticeWay", tInsuRenewMediaTemp.getNoticeFlag());

				// 報表明細資料
				listL4602.add(tL4602);
			}

			// 製作報表
			isFinished = l4602Report.exec(listL4602, titaVo);

		}
		// 報表製作完成，發MESSAGE
		if (isFinished) {
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getTlrNo(), "Y", "LC009", titaVo.getTlrNo(), "L4602火險出單明細表已完成", titaVo);
		} else {
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getTlrNo(), "Y", "LC009", titaVo.getTlrNo(), "L4602火險出單明細表查無資料", titaVo);
		}

		if (lInsuRenewMediaTemp.size() > 0) {
			try {
				insuRenewMediaTempService.insertAll(lInsuRenewMediaTemp, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "InsuRenew : " + e.getErrorMsg());
			}
		}
		// 把明細資料容器裝到檔案資料容器內
		insuRenewFileVo.setOccursList(tmpList);
		// 轉換資料格式
		ArrayList<String> file = insuRenewFileVo.toFile();

		if (file.isEmpty()) {
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getTlrNo(), "Y", "LC009", titaVo.getTlrNo(), "L4602火險到期檔查無資料", titaVo);
		} else {
			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxCode(), titaVo.getTxCode() + "-火險到期檔", "LNM01P.txt", 2);

			for (String line : file) {
				makeFile.put(line);
			}

			long sno = makeFile.close();

			this.info("sno : " + sno);
			makeFile.toFile(sno);

//		totaVo.put("PdfSnoM", "" + sno);

			// TXT製作完成，發MESSAGE
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getTlrNo(), "Y", "LC009", titaVo.getTlrNo(), "L4602火險到期檔LNM01P.txt已完成", titaVo);
		}
		
		// 2021-11-08 智偉新增
		l4600Report.exec(titaVo);
		
		this.addList(this.totaVo);
		return this.sendList();

	}
}
