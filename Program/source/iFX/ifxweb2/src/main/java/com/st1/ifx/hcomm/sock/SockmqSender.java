package com.st1.ifx.hcomm.sock;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.ifx.filter.FilterUtils;
import com.st1.servlet.GlobalValues;
import com.st1.util.PoorManUtil;

@Component
@Scope("prototype")
public class SockmqSender {
	// 柯 新增 前面時間 8位與 流水號8位 START
	static DateTime Time = new DateTime();
	// BUG: getMinuteOfHour() 不會補0
	// public static String HostHeadTime = Integer.toString(Time.getDayOfMonth()) +
	// Integer.toString(Time.getHourOfDay()) +
	// Integer.toString(Time.getMinuteOfHour())+
	// Integer.toString(Time.getSecondOfMinute());
	//
	static int minHostTranC = 1;
	static int maxHostTranC = 99999999;
	// public static int hosttranc = 0;

	static AtomicInteger atomNext = new AtomicInteger(0);

	protected String getNextHostTranC() {
		// 在很多人同時做交易時 序號會錯誤
		// hosttranc++;
		// if (hosttranc > maxHostTranC)
		// hosttranc = minHostTranC;
		atomNext.compareAndSet(maxHostTranC, 0); // 如果到底了 就歸零
		int hosttranc = atomNext.incrementAndGet();
		return String.format("%08d", hosttranc);
	}

	// END
	static final Logger logger = LoggerFactory.getLogger(SockmqSender.class);
	@Value("${sna_server}")
	String server;// = "127.0.0.1";

	@Value("${sna_mq_port}")
	String sServmqPort;// = 10001; R6=5000

	@Value("${sna_ipport_txcd}")
	String sna_Ipport_txcd;

	@Value("${sna_mq_timeout}")
	String sTimeout;

	@Value("${sna_mq_id_offset}")
	String sMsgmqidOffset = "";

	@Value("${sna_mqmsgidl}")
	String sMsgidln = "";

	// imscode with filler
	@Value("${sna_ims_code}")
	String imsCodeFiller = "";
	// 特殊通道----------------------------
	@Value("${sna_ims_xs_code}")
	String imsxsCodeFiller = "";

	@Value("${sna_ims_xf_code}")
	String imsxfCodeFiller = "";

	@Value("${sna_ims_xa_code}")
	String imsxaCodeFiller = "";

	@Value("${sna_ims_xt_code}")
	String imsxtCodeFiller = "";

	@Value("${sna_ims_xr_code}")
	String imsxrCodeFiller = "";

	@Value("${sna_ims_xi_code}")
	String imsxiCodeFiller = "";

	@Value("${sna_ims_xe_code}")
	String imsxeCodeFiller = "";

	@Value("${sna_ims_xg_code}")
	String imsxgCodeFiller = "";

	@Value("${sna_ims_xx_code}")
	String imsxxCodeFiller = "";

	@Value("${sna_ims_xy_code}")
	String imsxyCodeFiller = "";

	@Value("${sna_ims_xw_code}")
	String imsxwCodeFiller = "";

	@Value("${sna_ims_xm_code}")
	String imsxmCodeFiller = "";

	@Value("${sna_ims_xl_code}")
	String imsxlCodeFiller = "";

	@Value("${sna_ims_xh_code}")
	String imsxhCodeFiller = "";

	@Value("${sna_ims_xc_code}")
	String imsxcCodeFiller = "";

	@Value("${sna_rtm}")
	String sRtm = "";

	int msgmqidOffset = 0;
	private Socket socket = null;

	public int getMsgmqidOffset() {
		return msgmqidOffset;
	}

	@PostConstruct
	public void doSomething() {

		if (sMsgmqidOffset != null && sMsgmqidOffset.trim().length() > 0) {
			msgmqidOffset = Integer.parseInt(sMsgmqidOffset);
		}
		logger.info("sna mq propertites:");
		logger.info("sTimeout        offset:" + FilterUtils.escape(sTimeout));
		logger.info(FilterUtils.escape("sMsgmqidOffset offset:" + sMsgmqidOffset));
		logger.info("sMsgidlng       offset:" + sMsgidln);
		logger.info(FilterUtils.escape("msgmqlng        offset:" + msgmqidOffset));
		logger.info(FilterUtils.escape("ims code with filler :[" + imsCodeFiller + "]"));
		logger.info("rtm:[" + sRtm + "]");
	}

	public SockmqSender() {
		logger.info("construct a SockmqSender");
	}

	public static byte[] toByteArray(int iSource, int iArrayLen) {
		byte[] bLocalArr = new byte[iArrayLen];
		for (int i = 0; (i < 4) && (i < iArrayLen); i++) {
			bLocalArr[i] = (byte) (iSource >> 8 * i & 0xFF);
		}
		return bLocalArr;
	}

	// public SockSender(String server, int port){
	// this.server = server;
	// this.servPort = port;
	// }
	public String send(String tita, String titaTxcd, String msgTxcd, boolean isUpdatedTran) throws Exception {
		doSomething();
		int sendport = Integer.parseInt(sServmqPort);
		String limsCode = imsCodeFiller; // 預設值，依照設定參數替換
		String checkCodeFiller = "";
		logger.info("limsCode:" + FilterUtils.escape(limsCode));
		logger.info(FilterUtils.escape("try connect to mq " + server + ":" + Integer.toString(sendport)));

		// 一直改規格故先這樣寫
		if ("XA".equals(msgTxcd)) {
			checkCodeFiller = splitefiller(imsxaCodeFiller, isUpdatedTran);
		} else if ("XC".equals(msgTxcd)) {
			checkCodeFiller = splitefiller(imsxcCodeFiller, isUpdatedTran);
		} else if ("XE".equals(msgTxcd)) {
			checkCodeFiller = splitefiller(imsxeCodeFiller, isUpdatedTran);
		} else if ("XF".equals(msgTxcd)) {
			checkCodeFiller = splitefiller(imsxfCodeFiller, isUpdatedTran);
		} else if ("XG".equals(msgTxcd)) {
			checkCodeFiller = splitefiller(imsxgCodeFiller, isUpdatedTran);
		} else if ("XH".equals(msgTxcd)) {
			checkCodeFiller = splitefiller(imsxhCodeFiller, isUpdatedTran);
		} else if ("XI".equals(msgTxcd)) {
			checkCodeFiller = splitefiller(imsxiCodeFiller, isUpdatedTran);
		} else if ("XL".equals(msgTxcd)) {
			checkCodeFiller = splitefiller(imsxlCodeFiller, isUpdatedTran);
		} else if ("XM".equals(msgTxcd)) {
			checkCodeFiller = splitefiller(imsxmCodeFiller, isUpdatedTran);
		} else if ("XR".equals(msgTxcd)) {
			checkCodeFiller = splitefiller(imsxrCodeFiller, isUpdatedTran);
		} else if ("XS".equals(msgTxcd)) {
			checkCodeFiller = splitefiller(imsxsCodeFiller, isUpdatedTran);
		} else if ("XT".equals(msgTxcd)) {
			checkCodeFiller = splitefiller(imsxtCodeFiller, isUpdatedTran);
		} else if ("XW".equals(msgTxcd)) {
			checkCodeFiller = splitefiller(imsxwCodeFiller, isUpdatedTran);
		} else if ("XX".equals(msgTxcd)) {
			checkCodeFiller = splitefiller(imsxxCodeFiller, isUpdatedTran);
		} else if ("XY".equals(msgTxcd)) {
			checkCodeFiller = splitefiller(imsxyCodeFiller, isUpdatedTran);
		}

		//

		// 新增 不同 通道走法
		// if(imsxstype.length() > 0 ){
		// String [] othxsType = imsxstype.trim().split(",");
		// logger.info("msgTxcd:" + msgTxcd);
		// for(int i = 0; i < othxsType.length; i++){
		// logger.info("othxsType["+i+"]:" + othxsType[i]);
		// if(msgTxcd.equals(othxsType[i]) && !imsxsCodeFiller.isEmpty()){
		// logger.info("Msg id :" + msgTxcd + ",is in [sna_ims_xs_type].");
		// limsCode = imsxsCodeFiller; //走特殊規格
		// logger.info("limsCode ->>:" + imsxsCodeFiller);
		// }
		// }
		// }
		logger.info("Msg id :" + FilterUtils.escape(msgTxcd));
		limsCode = checkCodeFiller; // 走特殊規格
		logger.info("limsCode ->>:" + FilterUtils.escape(checkCodeFiller));

		byte[] msgoffset = toByteArray(Integer.parseInt(sMsgmqidOffset), 1);
		logger.info("..........");
		byte[] msgidleng = toByteArray(Integer.parseInt(sMsgidln), 1);
		logger.info("msgoffset:" + Integer.parseInt(sMsgmqidOffset) + "," + HexDump.dumpHexString(msgoffset));
		logger.info("msgidleng:" + Integer.parseInt(sMsgidln) + "," + HexDump.dumpHexString(msgidleng));

		byte[] temp = byteMerger(msgoffset, msgidleng);
		byte[] data = null;
		String headTime = PoorManUtil.getNowwithFormat("yyyyMMddHHmmss") + getNextHostTranC();
		logger.info("Mq msgid:" + headTime);
		tita = limsCode + sRtm + tita;
		byte[] datatmp = tita.getBytes();
		byte[] titahead = headTime.getBytes();
		byte[] titab = tita.getBytes("cp937");
		// 原先中心api自動增加之llzz,MQ需自行給 START
		byte[] ll = new byte[2];
		byte[] zz = new byte[2];
		byte[] llzz = new byte[4];
		ll = IntToByteArray(tita.length() + 4); // 計算TITA長度,包含llzz
		llzz = byteMerger(ll, zz); // 組合
		titab = byteMerger(llzz, titab); // 組合
		temp = byteMerger(temp, titahead);
		logger.info("temp+titahead source:" + temp);
		logger.info("llzz:" + HexDump.dumpHexString(llzz));
		logger.info("tita source:" + HexDump.dumpHexString(datatmp));
		data = byteMerger(temp, titab); // + titab(真的MSG)
		data = HexConvert.convertHexString(data);
		logger.info(FilterUtils.escape("tita mq@" + (GlobalValues.bR6 ? "R6" : "Win") + ":" + tita));
		logger.info("data source:" + HexDump.dumpHexString(data));

		// Create socket that is connected to server on specified port
		// Socket socket = new Socket(server, servPort);

		SocketAddress socketAddress;
		// 先設定過去後才能PARSING
		logger.info("sna_Ipport_txcd:" + FilterUtils.escape(sna_Ipport_txcd));
		GlobalValues.setSna_Ipport_txcd(sna_Ipport_txcd);
		String[] socketConfig = GlobalValues.parsingIpportBytxcd(titaTxcd);
		logger.info("MQ try connect to socketConfig: " + socketConfig[0] + ",socketConfig:" + socketConfig[1]);
		if (socketConfig[0] != null && socketConfig[1] != null) {
			socketAddress = new InetSocketAddress(socketConfig[0], Integer.parseInt(socketConfig[1]));
			logger.info("MQ try connect to different: " + socketConfig[0] + ",sendport:" + socketConfig[1]);
		} else {
			socketAddress = new InetSocketAddress(server, sendport);
			logger.info(FilterUtils.escape("MQ try connect to: " + server + ",sendport:" + sendport));
		}
		String result = null;
		String result2 = null;
		String rSna = null;
		socket = new Socket();
		// socket.setSendBufferSize(12 * 1024);// 發送緩衝區大小,12K。
		socket.connect(socketAddress);

		logger.info("Connected to mq server... sending tita, timeout:" + FilterUtils.escape(sTimeout));
		socket.setSoTimeout(Integer.parseInt(sTimeout) * 1000); // 設定 TIME OUT 時間
		InputStream in = socket.getInputStream();
		OutputStream out = socket.getOutputStream();

		LengthmqFramer frammqer = new LengthmqFramer(in);
		frammqer.frameMsg(data, out); // 會補上長度
		socket.shutdownOutput();
		data = frammqer.nextMsg();
		byte[] dataSna = Arrays.copyOfRange(data, 0, 3);
		byte[] dataUse = Arrays.copyOfRange(data, 3, data.length);

		logger.info("CP937 mq data:" + HexDump.dumpHexString(data));
		int sosiCount = 0;

		for (int i = 0; i < dataUse.length; i++)
			if ((dataUse[i] == 0x0E) || (dataUse[i] == 0x0F))
				sosiCount++;

		byte[] newData2 = new byte[dataUse.length + sosiCount];

		for (int i = 0, j = 0; i < dataUse.length; i++) {
			if (dataUse[i] == 0x0E) {
				newData2[j++] = 0x37; // 0x37 會被轉成 0x04
				newData2[j++] = 0x0E; // 0x0E 會被刪除
			} else {
				if (dataUse[i] == 0x0F) {
					newData2[j++] = 0x0F; // 0x0F 會被刪除
					newData2[j++] = 0x2F; // 0x2F 會被轉成 0x07
					// newData2[j++] = 0x40;
					// newData2[j++] = 0x40;
				} else {
					newData2[j++] = dataUse[i];
				}
			}
		}
		rSna = new String(dataSna);
		result = new String(dataUse, "cp937");
		result2 = new String(newData2, "cp937");
		result = rSna + result;
		result2 = rSna + result2;

		byte[] datar1 = result.getBytes();
		byte[] datar2 = result2.getBytes();
		logger.info("MS950 mq datar1:" + HexDump.dumpHexString(datar1));
		logger.info("MS950 mq datar2:" + HexDump.dumpHexString(datar2));
		// /結束插入/
		logger.info("Received tota: " + result);
		logger.info("Received tota2: " + result2);
		logger.info("Sockmq shutdownInput & close");
		socket.shutdownInput();
		socket.close();
		if (GlobalValues.bR6)
			return result2; // R6
		else
			return result; // WINDOWS
	}

	byte[] IntToByteArray(int data) {

		byte[] result = new byte[2];

		result[0] = (byte) ((data & 0xFF00) >> 8);
		result[1] = (byte) ((data & 0x00FF) >> 0);

		return result;
	}

	// java 合并两个byte数组
	public static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
		byte[] byte_3 = new byte[byte_1.length + byte_2.length];
		System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
		System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
		return byte_3;
	}

	// 參數規格為 -> 修改,查詢
	private String splitefiller(String filler, boolean isupdate) {
		String[] temp = filler.split(",");
		if (isupdate) {
			return temp[0];
		} else {
			if (temp.length > 1) {
				return temp[1];
			} else {
				return temp[0];
			}
		}
	}

	public void stopSocket() {
		if (!socket.isClosed()) {
			try {
				socket.shutdownInput();
				socket.close();
			} catch (IOException e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				logger.error(errors.toString());
			}
		}
	}
}
