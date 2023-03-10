package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM017ServiceImpl;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.date.DateUtil;

@Component
@Scope("prototype")

public class LM017Report extends MakeReport {

	@Autowired
	LM017ServiceImpl lM017ServiceImpl;

	@Autowired
	DateUtil dDateUtil;

//	自訂表頭
	@Override
	public void printHeader() {

		this.info("MakeReport.printHeader");

		printHeaderL();

		this.setBeginRow(8);

		this.setMaxRows(35);
	}

	public void printHeaderL() {
		this.print(-3, 95, "本表每月5日上午10時前報送　機密等級："+this.getSecurity());
		this.print(-4, 8, "新光人壽承作「購置高價住宅貸款」統計表（包含自然人及公司法人）　");
		this.print(-6, 4, "資料期間：　　　　" + showRocDate(this.getReportDate()).substring(0, 7));
		this.print(-8, 4, "表號：　　　　　　" + this.getRptCode());
	}

	public void exec(TitaVo titaVo) throws LogicException {

		ReportVo reportVo = ReportVo.builder().setBrno(titaVo.getBrno()).setRptDate(titaVo.getEntDyI())
				.setRptCode("LM017").setRptItem("金融機構承作購置高價住宅貸款統計").setRptSize("A4")
				.setSecurity(this.getSecurity()).setPageOrientation("L").build();

		
		this.open(titaVo, reportVo);
		
//		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM017", "金融機構承作購置高價住宅貸款統計", "密", "", "L");

		List<Map<String, String>> LM017List = null;
		try {
			LM017List = lM017ServiceImpl.findAll(titaVo);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("lM017ServiceImpl.findAll error = " + errors.toString());
		}
		BigDecimal num = new BigDecimal("0");
		BigDecimal amt = new BigDecimal("0");
		BigDecimal area[] = { new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0") };
		if (LM017List != null && LM017List.size() != 0) {

			DecimalFormat df1 = new DecimalFormat("#,##0");

			for (Map<String, String> tLM017Vo : LM017List) {
				BigDecimal f1 = new BigDecimal(tLM017Vo.get("F1").toString());
				BigDecimal f2 = new BigDecimal(tLM017Vo.get("F2").toString());

				switch (Integer.valueOf(tLM017Vo.get("F0"))) {

				case 1:
					this.print(-14, 59, tLM017Vo.get("F1"), "R");

					this.print(-14, 79, df1.format(f2), "R");
					area[0] = new BigDecimal("1");
					break;
				case 2:
					this.print(-17, 59, tLM017Vo.get("F1"), "R");
					this.print(-17, 79, df1.format(f2), "R");
					area[1] = new BigDecimal("1");
					break;
				case 3:
					this.print(-20, 59, tLM017Vo.get("F1"), "R");
					this.print(-20, 79, df1.format(f2), "R");
					area[2] = new BigDecimal("1");
					break;
				case 4:
					this.print(-23, 59, tLM017Vo.get("F1"), "R");
					this.print(-23, 79, df1.format(f2), "R");
					area[3] = new BigDecimal("1");
					break;
				case 5:
					this.print(-26, 59, tLM017Vo.get("F1"), "R");
					this.print(-26, 79, df1.format(f2), "R");
					area[4] = new BigDecimal("1");
					break;
				case 6:
					this.print(-29, 59, tLM017Vo.get("F1"), "R");
					this.print(-29, 79, df1.format(f2), "R");
					area[5] = new BigDecimal("1");
					break;
				case 7:
					this.print(-32, 59, tLM017Vo.get("F1"), "R");
					this.print(-32, 79, df1.format(f2), "R");
					area[6] = new BigDecimal("1");
					break;
				default:
					break;
				}

				num = num.add(f1);
				amt = amt.add(f2);

			}

			int temp = 0;
			for (int i = 0; i < area.length; i++) {
				if (area[i].intValue() == 0) {
					this.print(-14 - temp, 59, "0", "R");
					this.print(-14 - temp, 79, "0", "R");
				}
				temp = temp + 3;

			}

			this.print(-34, 59, String.valueOf(num.toString()), "R");
			this.print(-34, 79, df1.format(amt), "R");

			this.setCharSpaces(0);
			this.print(1, 3, "");
			this.print(-9, 3, "┌────────────┬────────┬───────────────────────────────────────────────┐");
			this.print(-10, 3, "│　　　　地區別　　　　　│　　貸款項目　　│　　　　撥款戶數　　　　　　　撥款金額　　　　　　　　加權平均　　　　　　　　　加權平均　　　│");
			this.print(-11, 3, "│﹙以擔保品座落地點區分﹚│　　　　　　　　│　　　　（戶）　　　　　　　　（億元）　　　　　　　貸款成數（％）　　　　　　貸款利率（％）　│");
			this.print(-12, 3, "├────────────┼────────┼───────────┬───────────┬───────────┬───────────┤");
			this.print(-13, 3, "│　　　　　　　　　　　　│　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│");
			this.print(-14, 3, "│　　　　台北市　　　　　│　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│");
			this.print(-15, 3, "├────────────┤　　　　　　　　├───────────┼───────────┼───────────┼───────────┤");
			this.print(-16, 3, "│　　　　　　（註１）　　│　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│");
			this.print(-17, 3, "│　　　　新北市　　　　　│　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│");
			this.print(-18, 3, "├────────────┤　　　　　　　　├───────────┼───────────┼───────────┼───────────┤");
			this.print(-19, 3, "│　　　　　　　　　　　　│　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│");
			this.print(-20, 3, "│　　　　桃園縣　　　　　│　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│");
			this.print(-21, 3, "├────────────┤　　　　（註２）├───────────┼───────────┼───────────┼───────────┤");
			this.print(-22, 3, "│　　　　　　（註１）　　│　　購置高價　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│");
			this.print(-23, 3, "│　　　　台中市　　　　　│　　　住宅貸款　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│");
			this.print(-24, 3, "├────────────┤　　　　　　　　├───────────┼───────────┼───────────┼───────────┤");
			this.print(-25, 3, "│　　　　　　（註１）　　│　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│");
			this.print(-26, 3, "│　　　　台南市　　　　　│　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│");
			this.print(-27, 3, "├────────────┤　　　　　　　　├───────────┼───────────┼───────────┼───────────┤");
			this.print(-28, 3, "│　　　　　　（註１）　　│　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│");
			this.print(-29, 3, "│　　　　高雄市　　　　　│　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│");
			this.print(-30, 3, "├────────────┤　　　　　　　　├───────────┼───────────┼───────────┼───────────┤");
			this.print(-31, 3, "│　　　　　　　　　　　　│　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│");
			this.print(-32, 3, "│　　　　其他地區　　　　│　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│");
			this.print(-33, 3, "├────────────┴────────┼───────────┼───────────┼───────────┼───────────┤");
			this.print(-34, 3, "│　　　　　　　　　合計數　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│");
			this.print(-35, 3, "└─────────────────────┴───────────┴───────────┴───────────┴───────────┘");
			this.print(-38, 20, "製表人：　　　　　　　　　　　　　　　 　覆　核：　　　　　　　　　　　　　　　　部門主管：");
			this.print(-39, 20, "電　話：　　　　　　　　　　　　　　　 　電　話：　　　　　　　　　　　　　　　　　電　話：");
			this.print(-40, 20, "E-mail：　　　　　　　　　　　　　　　  E-mail：　　　　　　　　　　　　　　　　 E-mail：");
		} else {
			this.print(1, 30, "本日無資料");
			this.print(-9, 3, "┌────────────┬────────┬───────────────────────────────────────────────┐");
			this.print(-10, 3, "│　　　　地區別　　　　　│　　貸款項目　　│　　　　撥款戶數　　　　　　　撥款金額　　　　　　　　加權平均　　　　　　　　　加權平均　　　│");
			this.print(-11, 3, "│﹙以擔保品座落地點區分﹚│　　　　　　　　│　　　　（戶）　　　　　　　　（億元）　　　　　　　貸款成數（％）　　　　　　貸款利率（％）　│");
			this.print(-12, 3, "├────────────┼────────┼───────────┬───────────┬───────────┬───────────┤");
			this.print(-13, 3, "│　　　　　　　　　　　　│　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│");
			this.print(-14, 3, "│　　　　台北市　　　　　│　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│");
			this.print(-15, 3, "├────────────┤　　　　　　　　├───────────┼───────────┼───────────┼───────────┤");
			this.print(-16, 3, "│　　　　　　（註１）　　│　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│");
			this.print(-17, 3, "│　　　　新北市　　　　　│　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│");
			this.print(-18, 3, "├────────────┤　　　　　　　　├───────────┼───────────┼───────────┼───────────┤");
			this.print(-19, 3, "│　　　　　　　　　　　　│　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│");
			this.print(-20, 3, "│　　　　桃園縣　　　　　│　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│");
			this.print(-21, 3, "├────────────┤　　　　（註２）├───────────┼───────────┼───────────┼───────────┤");
			this.print(-22, 3, "│　　　　　　（註１）　　│　　購置高價　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│");
			this.print(-23, 3, "│　　　　台中市　　　　　│　　　住宅貸款　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│");
			this.print(-24, 3, "├────────────┤　　　　　　　　├───────────┼───────────┼───────────┼───────────┤");
			this.print(-25, 3, "│　　　　　　（註１）　　│　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│");
			this.print(-26, 3, "│　　　　台南市　　　　　│　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│");
			this.print(-27, 3, "├────────────┤　　　　　　　　├───────────┼───────────┼───────────┼───────────┤");
			this.print(-28, 3, "│　　　　　　（註１）　　│　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│");
			this.print(-29, 3, "│　　　　高雄市　　　　　│　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│");
			this.print(-30, 3, "├────────────┤　　　　　　　　├───────────┼───────────┼───────────┼───────────┤");
			this.print(-31, 3, "│　　　　　　　　　　　　│　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│");
			this.print(-32, 3, "│　　　　其他地區　　　　│　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│");
			this.print(-33, 3, "├────────────┴────────┼───────────┼───────────┼───────────┼───────────┤");
			this.print(-34, 3, "│　　　　　　　　　合計數　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│　　　　　　　　　　　│");
			this.print(-35, 3, "└─────────────────────┴───────────┴───────────┴───────────┴───────────┘");
			this.print(-38, 20, "製表人：　　　　　　　　　　　　　　　 　覆　核：　　　　　　　　　　　　　　　　部門主管：");
			this.print(-39, 20, "電　話：　　　　　　　　　　　　　　　 　電　話：　　　　　　　　　　　　　　　　　電　話：");
			this.print(-40, 20, "E-mail：　　　　　　　　　　　　　　　  E-mail：　　　　　　　　　　　　　　　　 E-mail：");
		}
		long sno = this.close();
		this.toPdf(sno);
	}

}
