package com.ifx.fmt;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.st1.ifx.domain.MsgBox;
import com.st1.util.cbl.Cobol;
import com.st1.util.cbl.CobolProcessor;
import com.st1.util.cbl.FieldList;

@FieldList({ "occurs", "vdate", "vtime", "msgno", "text" })
public class FormCS002 {
	static final Logger logger = LoggerFactory.getLogger(FormCS002.class);

	public FormCS002() {
		for (int i = 0; i < 1; i++)
			occurs[i] = new FormCS002Occur();
	}

	@Cobol("O,5")
	private FormCS002Occur[] occurs = new FormCS002Occur[1];

	@Cobol("X,8")
	String vdate;

	@Cobol("X,4")
	String vtime;

	@Cobol("X,5")
	String msgno;

	@Cobol("X,200")
	String text;

	public List<MsgBox> toMsgBoxes(String entDay) {
		List<MsgBox> boxes = new ArrayList<MsgBox>();

		java.util.Date today = new java.util.Date();

		logger.info("系統日期:" + today + ",entDay:" + entDay);

		// 設定日期格式
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 進行轉換
		// Date date = sdf.parse(dateString);

		long t = today.getTime();

		Set<String> brnoTlrSet = new HashSet<String>();

		for (int i = 0; i < this.occurs.length; i++) {
			FormCS002Occur o = this.occurs[i];
			String k = o.getBrno() + o.getTlrno();
			if (brnoTlrSet.contains(k))
				continue;
			brnoTlrSet.add(k);

			if (o.isValid()) {
				MsgBox b = new MsgBox();
				b.setBrno(o.brno);
				b.setTlrno(o.tlrno);
				b.setRcvDate(new java.sql.Date(t));
				b.setRcvTime(new Time(t));
				b.setMsgno(msgno);
				// 移除後方可能之多餘值
				b.setContent(text.replaceAll("\\s+$", "").replaceAll("\0+$", "")); // 合併儲存在db2
				b.setValidTime(Long.parseLong(vdate + vtime));
				b.setViewDate(null);
				b.setViewTime(null);
				b.setDone('N');

				boxes.add(b);
			}

		}
		logger.info("boxes:" + boxes.size());
		return boxes;
	}

	public FormCS002Occur[] getOccurs() {
		return occurs;
	}

	public void setOccurs(FormCS002Occur[] occurs) {
		this.occurs = occurs;
	}

	public String getVdate() {
		return vdate;
	}

	public void setVdate(String vdate) {
		this.vdate = vdate;
	}

	public String getVtime() {
		return vtime;
	}

	public void setVtime(String vtime) {
		this.vtime = vtime;
	}

	public String getMsgno() {
		return msgno;
	}

	public void setMsgno(String msgno) {
		this.msgno = msgno;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public static void main(String[] args) {
		String s = "02925050290000128720140324112220350SCS002         50502950502000000000000000000020140404180000000升息響起金價空頭死灰復燃                                                                                                                                                                                ";
		FormCS002 form = new FormCS002();
		try {
			CobolProcessor.parse(s.substring(50), form);
			System.out.println(form.toString());
			// for(FormCS002Occur o :form.occurs){
			// System.out.println(o);
			// }
		} catch (Exception e) {
			;
		}
	}

	@Override
	public String toString() {
		return "FormCS002 [occurs=" + Arrays.toString(occurs) + ", vdate=" + vdate + ", vtime=" + vtime + ", msgno="
				+ msgno + ", text=" + text + "]";
	}

}
