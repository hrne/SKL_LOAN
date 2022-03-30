package com.st1.ifx.hcomm.sock;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.st1.ifx.dataVo.TitaVo;
import com.st1.ifx.dataVo.TotaVo;
import com.st1.ifx.filter.FilterUtils;
import com.st1.ifx.hcomm.netty.CallbackService;
import com.st1.ifx.hcomm.netty.ChannelUtils;
import com.st1.ifx.hcomm.netty.IntegerFactory;
import com.st1.ifx.hcomm.netty.NettyChannelPool;
import com.st1.servlet.GlobalValues;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

@Component
@Scope("prototype")
public class SockSender {
	private Logger logger = LoggerFactory.getLogger(SockSender.class);

	@Autowired
	private NettyChannelPool nettyChannelPool;

	@Autowired
	private CallbackService callbackService;

	@Value("${sna_server}")
	private String server;

	@Value("${sna_port}")
	private String sServPort;

	@Value("${sna_ipport_txcd}")
	private String sna_Ipport_txcd;

	@Value("${sna_timeout}")
	private String sTimeout;

	@Value("${sna_ims_code}")
	private String imsCodeFiller = "";

	@Value("${sna_filler}")
	private String sFiller = " ";

	@Value("${sna_rtm}")
	private String sRtm = "";

	@Value("${sna_msglng_offset}")
	private String sMsglngOffset;

	@Value("${sna_mq_msglng_offset}")
	private String smqMsglngOffset;

	private int msglngOffset = 0;

	private int mqmsglngOffset = 0;

	private Socket socket = null;

	private Channel channel = null;

	private final int maxHostTranC = 99999999;

	protected static AtomicInteger atomNext = new AtomicInteger(0);

	public SockSender() {
	}

	@PostConstruct
	public void doSomething() {
		if (sMsglngOffset != null && sMsglngOffset.trim().length() > 0) {
			try {
				msglngOffset = Integer.parseInt(sMsglngOffset);
			} catch (Exception e) {
				// TODO: handle exception
				msglngOffset = 0;
				logger.info("sMsglngOffset parseInt error!" + FilterUtils.escape(sMsglngOffset) + ".");
			}

		}

		if (smqMsglngOffset != null && smqMsglngOffset.trim().length() > 0) {
			try {
				mqmsglngOffset = Integer.parseInt(smqMsglngOffset);
			} catch (Exception e) {
				// TODO: handle exception
				mqmsglngOffset = 0;
				logger.info("smqMsglngOffset parseInt error!" + FilterUtils.escape(smqMsglngOffset) + ".");
			}

		}
		logger.info("sna propertites:");
		logger.info("to server : " + server);
		logger.info("send port : " + sServPort);
	}

	public byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
		byte[] byte_3 = new byte[byte_1.length + byte_2.length];
		System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
		System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
		return byte_3;
	}

	public List<TotaVo> send(TitaVo titaVo, String titaTxcd) throws Exception {
		StopWatch watch = new StopWatch();
		watch.start();

		logger.info(FilterUtils.escape("Msg id :" + titaVo.getMsgId() + ",isUpdatedTran:" + titaVo.isUpdate()));

		// 流水編號 DDHHMMSS+00000001
		String titaS = titaVo.voToString();
		byte[] data = titaS.getBytes(CharsetUtil.UTF_8);
		logger.info("tita source:" + titaS);

		if (GlobalValues.sendIp != null && GlobalValues.sendIp.get(titaVo.getTxCode().trim()) != null)
			this.server = GlobalValues.sendIp.get(titaVo.getTxCode().trim());

		String result = null;

		this.channel = nettyChannelPool.syncGetChannel(titaVo.getTxCode());
		int seq = IntegerFactory.getInstance().incrementAndGet();
		ChannelUtils.putCallback2DataMap(channel, seq, callbackService);

		synchronized (callbackService) {
			UnpooledByteBufAllocator allocator = new UnpooledByteBufAllocator(false, false, false);
			ByteBuf byteBuf = allocator.buffer(data.length + 8);

			byteBuf.writeInt(seq);
			byteBuf.writeInt(data.length);
			byteBuf.writeBytes(data);
			this.channel.writeAndFlush(byteBuf);

			logger.info("Befor Wait CallbackService.....");
			callbackService.wait(80000);
			logger.info("After Wait CallbackService.....");

			ByteBuf resbytes = callbackService.result;

			if (Objects.isNull(resbytes))
				return null;

			int rSeq = resbytes.readInt();
			int rLen = resbytes.readInt();

			byte[] rData = new byte[rLen];
			resbytes.readBytes(rData);
			result = new String(rData, CharsetUtil.UTF_8);

			logger.info("sSeq : [" + seq + "] rSeq : [" + rSeq + "]");
			logger.info("rLen : [" + rLen + "]");
//			logger.info(HexDump.dumpHexString(rData));
			logger.info("result : " + result);

			watch.stop();
			logger.info("Total execution time to send socket in millis: " + watch.getTotalTimeMillis());

			List<TotaVo> totaVoLi = null;
			try {
				totaVoLi = new ObjectMapper().configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true).readValue(result, new TypeReference<List<TotaVo>>() {
				});
				return totaVoLi;
			} catch (Exception e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				logger.error(errors.toString());
			} finally {
//				ReferenceCountUtil.release(byteBuf);
				ReferenceCountUtil.release(resbytes);
				this.closeChannel();
			}
			return totaVoLi;
		}
	}

	protected String getNextHostTranC() {
		atomNext.compareAndSet(maxHostTranC, 0);
		int hosttranc = atomNext.incrementAndGet();
		return String.format("%08d", hosttranc);
	}

	public void closeChannel() {
		if (!Objects.isNull(this.channel)) {
			this.channel.close();
			this.channel.closeFuture();
		}
	}

	public int getMsglngOffset() {
		return msglngOffset;
	}

	public int getmqMsglngOffset() {
		return mqmsglngOffset;
	}
}
