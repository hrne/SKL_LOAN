package com.st1.itx.nettyServer;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.st1.itx.loadVarService.GlobalMG;

import io.netty.channel.ChannelFuture;

/**
 * NettyService
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public class NettyService {
	private static final Logger logger = LoggerFactory.getLogger(NettyService.class);

	@Autowired
	public NettyServer nettyServer;

	@Autowired
	public InetSocketAddress address;

	@Autowired
	public GlobalMG globalMG;

	public void startServer() {

		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					if (System.getProperty("iTXPort") != null && System.getProperty("iTXPort").equals("7005"))
						address = new InetSocketAddress("10.11.50.22", 50002);

					ChannelFuture future = nettyServer.start(address);
					logger.info("closeFuture().syncUninterruptibly()");
					future.channel().closeFuture().syncUninterruptibly();
				} catch (Exception e) {
					StringWriter errors = new StringWriter();
					e.printStackTrace(new PrintWriter(errors));
					logger.error(errors.toString());
				}
			}
		});
		t1.setName("service-Netty");
		t1.start();
	}
}
