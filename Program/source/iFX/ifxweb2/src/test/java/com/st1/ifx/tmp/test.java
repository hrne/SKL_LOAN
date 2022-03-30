package com.st1.ifx.tmp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.st1.ifx.filter.FilterUtils;
import com.st1.ifx.hcomm.netty.CallbackService;
import com.st1.ifx.hcomm.netty.ChannelUtils;
import com.st1.ifx.hcomm.netty.IntegerFactory;
import com.st1.ifx.hcomm.netty.NettyChannelPool;
import com.st1.ifx.hcomm.sock.HexDump;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.util.CharsetUtil;

public class test {

	public static void main(String[] args) throws Exception {
		NettyChannelPool netty = new NettyChannelPool();
		CallbackService callbackService = new CallbackService();
		netty.init();
		Channel channel = netty.syncGetChannel(null);

		int seq = IntegerFactory.getInstance().incrementAndGet();
		ChannelUtils.putCallback2DataMap(channel, seq, callbackService);
		
		int seqi = 1181;
		byte[] ii = new byte[] { (byte) ( seqi >> 24), (byte) (seqi >> 16), (byte) (seqi >> 8), (byte) seqi };
		
		System.out.println(HexDump.dumpHexString(ii));
		
		File f = new File(FilterUtils.filter("\\\\192.168.245.8\\ifxDoc\\ifxwriter\\temp\\scr\\0000001746\\"));
		List<File> fileLi = Arrays.asList(f.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				System.out.println(name);
				return name.matches("^L1101_\\d+.txt");
			}
		}));
		for(File fl : fileLi)
			System.out.println(fl.getName());
		
		/*
		byte[] data = "{\"KINBR\":\"0000\",\"TLRNO\":\"E-LOAN\",\"TXTNO\":\"0\",\"ENTDY\":\"0\",\"ORGKIN\":\" \",\"ORGTLR\":\" \",\"ORGTNO\":\"0\",\"ORGDD\":\"0\",\"TRMTYP\":\"EL\",\"TXCD\":\"L7100\",\"MRKEY\":\" \",\"CIFKEY\":\"T122695699\",\"CIFERR\":\" \",\"HCODE\":\"0\",\"CRDB\":\"0\",\"HSUPCD\":\"0\",\"CURCD\":\"0\",\"CURNM\":\"TWD\",\"TXAMT\":\"2000000\",\"EMPNOT\":\"670875\",\"EMPNOS\":\"N55751\",\"CALDY\":\"20220121\",\"CALTM\":\"17264569\",\"MTTPSEQ\":\"0\",\"TOTAFG\":\"0\",\"OBUFG\":\"0\",\"ACBRNO\":\"0000\",\"RBRNO\":\"0000\",\"FBRNO\":\"0000\",\"RELCD\":\"1\",\"ACTFG\":\"0\",\"SECNO\":\" \",\"MCNT\":\"0\",\"TITFCD\":\"0\",\"RELOAD\":\"0\",\"BATCHNO\":\" \",\"DELAY\":\"0\",\"FMTCHK\":\" \",\"FROMMQ\":\" \",\"FUNCIND\":\"0\",\"LockNo\":\"0\",\"LockCustNo\":\"0\",\"AUTHNO\":\"T1000\",\"AGENT\":\" \",\"BODY\":[{\"TranCode\":\"L1105\",\"FunCd\":\"1\",\"CustNo\":\"0\",\"CustId\":\"S126377815\",\"TelTypeCode\":\"03\",\"TelArea\":\"\",\"TelNo\":\"0911222333\",\"TelEx\":\"\",\"TelOther\":\"\",\"TelChgRsnCode\":\"01\",\"RelationCode\":\"00\",\"LiaisonName\":\"\",\"Rmk\":\"\",\"Enable\":\"Y\",\"StopReason\":\"\",\"TelNoUKey\":\"\"},{\"TranCode\":\"L1105\",\"FunCd\":\"1\",\"CustNo\":\"0\",\"CustId\":\"S212911454\",\"TelTypeCode\":\"03\",\"TelArea\":\"\",\"TelNo\":\"0911222333\",\"TelEx\":\"\",\"TelOther\":\"\",\"TelChgRsnCode\":\"01\",\"RelationCode\":\"00\",\"LiaisonName\":\"\",\"Rmk\":\"\",\"Enable\":\"Y\",\"StopReason\":\"\",\"TelNoUKey\":\"\"},{\"TranCode\":\"L1105\",\"FunCd\":\"1\",\"CustNo\":\"57\",\"CustId\":\"T122695699\",\"TelTypeCode\":\"01\",\"TelArea\":\"02\",\"TelNo\":\"89991111\",\"TelEx\":\"1600\",\"TelOther\":\"\",\"TelChgRsnCode\":\"01\",\"RelationCode\":\"00\",\"LiaisonName\":\"\",\"Rmk\":\"\",\"Enable\":\"Y\",\"StopReason\":\"\",\"TelNoUKey\":\"\"},{\"TranCode\":\"L1105\",\"FunCd\":\"1\",\"CustNo\":\"57\",\"CustId\":\"T122695699\",\"TelTypeCode\":\"02\",\"TelArea\":\"02\",\"TelNo\":\"22219999\",\"TelEx\":\"\",\"TelOther\":\"\",\"TelChgRsnCode\":\"01\",\"RelationCode\":\"00\",\"LiaisonName\":\"\",\"Rmk\":\"\",\"Enable\":\"Y\",\"StopReason\":\"\",\"TelNoUKey\":\"\"},{\"TranCode\":\"L1105\",\"FunCd\":\"1\",\"CustNo\":\"57\",\"CustId\":\"T122695699\",\"TelTypeCode\":\"03\",\"TelArea\":\"\",\"TelNo\":\"0988111222\",\"TelEx\":\"\",\"TelOther\":\"\",\"TelChgRsnCode\":\"01\",\"RelationCode\":\"00\",\"LiaisonName\":\"\",\"Rmk\":\"\",\"Enable\":\"Y\",\"StopReason\":\"\",\"TelNoUKey\":\"\"}]}"
				.getBytes("UTF-8");
		String result = "";
		synchronized (callbackService) {
			UnpooledByteBufAllocator allocator = new UnpooledByteBufAllocator(false, false, false);
			ByteBuf byteBuf = allocator.buffer(data.length + 8);

			byteBuf.writeInt(seq);
			byteBuf.writeInt(data.length);
			byteBuf.writeBytes(data);
			channel.writeAndFlush(byteBuf);

			System.out.println("Befor Wait CallbackService.....");
			callbackService.wait(80000);
			System.out.println("After Wait CallbackService.....");

			ByteBuf resbytes = callbackService.result;

			if (Objects.isNull(resbytes))
				return;

			int rSeq = resbytes.readInt();
			int rLen = resbytes.readInt();

			byte[] rData = new byte[rLen];
			resbytes.readBytes(rData);
			result = new String(rData, CharsetUtil.UTF_8);

			System.out.println("sSeq : [" + seq + "] rSeq : [" + rSeq + "]");
			System.out.println("rLen : [" + rLen + "]");
//			System.out.println(HexDump.dumpHexString(rData));
			System.out.println("result : " + result);
		}
		*/
	}
	

}
