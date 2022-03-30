package com.st1.ifx.tmp;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.springframework.util.StopWatch;

import com.st1.ifx.hcomm.sock.HexDump;
import com.st1.util.PoorManUtil;

public class tServer extends java.lang.Thread {

	private boolean OutServer = false;
	private ServerSocket server = null;
	public static int MqserverPort = 55555;

	

	public tServer() {
		System.out.println("MqtestServer");
		try {
			server = new ServerSocket();
			server.setReuseAddress(true);
			System.out.println("setReuseAddress true.");
			server.setReceiveBufferSize(15 * 1024); // 　接收緩衝區大小,15K。
			System.out.println("setReceiveBufferSize 10k.");
			// 設置性能參數，可設置任意整數，數值越大，相應的參數重要性越高（連接時間，延遲，帶寬）
			// server.setPerformancePreferences(3, 2, 1);
			server.bind(new InetSocketAddress(MqserverPort));
			System.out.println("bind port:" + MqserverPort);
		} catch (java.io.IOException e) {
			System.out.println("mqServer error!" + MqserverPort);
			System.out.println("IOException :" + e.toString());
		}
	}

	public void pp() {
		Socket socket;
		InputStream in = null;

		System.out.println("mqServer !!!");
		// System.out.println("OutServer:" +OutServer );
		while (!OutServer) {
			System.out.println("mqServer in while!" + PoorManUtil.getNowwithFormat("yyyy/MM/dd HH:mm:ss.SSS"));
			StopWatch watch = new StopWatch();
			watch.start();
			// System.out.println("only one time!");
			socket = null;
			try {
				System.out.println("before synchronized!");
				synchronized (server) {
					System.out.println("in synchronized!");
					socket = server.accept();

				}
				OutServer = false;
				System.out.println("out synchronized!");
				System.out.println("InetAddress = " + socket.getInetAddress());
				socket.setSoTimeout(120 * 1000);
				System.out.println("mq ok");

				in = socket.getInputStream();
				try {
					System.out.println("mq in:" + in);
					byte[] byteresult = new byte[1024];
					    int bytesRcvd, totalBytesRcvd = 0;
					    do {
						if ((bytesRcvd = in.read(byteresult, totalBytesRcvd, byteresult.length - totalBytesRcvd)) == -1) {
						    break;
						}
						totalBytesRcvd += bytesRcvd;
					    } while (true);
					System.out.println("CP937 data :" + HexDump.dumpHexString(byteresult));

				} catch (Exception e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
					System.out.println("frammqer error" + e.toString());

				}
				in.close();
				in = null;
			} catch (java.io.IOException e) {
				System.out.println("Socket mq error! Mq server close!");
				System.out.println("IOException :" + e.toString());
				OutServer = true;
			} catch (NumberFormatException e1) {
				// TODO Auto-generated catch block
				System.out.println("Socket mq error!  NumberFormatException :" + e1.toString());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				System.out.println("Socket mq error!  Exception :" + e1.toString());
			} finally {
				System.out.println("in socket finally!");

				if (socket != null) {

					try {
						if (in != null) {
							System.out.println("in close!");
							in.close();
							System.out.println("in null!");
							in = null;
						}
						System.out.println("socket close!");
						socket.close();

					} catch (Exception e) {

						socket = null;

						System.out.println("服務端 finally 異常:" + e.getMessage());

					}
				}

			}
			watch.stop();
			System.out.println("Total execution time to MqServer get a while in millis: "
					+ watch.getTotalTimeMillis());
		}
		System.out.println("out while!");
	}

	public static void main(String args[]) {
		System.out.println("Mq main");
		System.out.println("Mq MqserverPort:" + MqserverPort);
		if (MqserverPort != 0) {
			System.out.println("IFX MqtestServer M System started");
			tServer mqServer = new tServer();
			mqServer.pp();
		} else {
			System.out.println("Mq server no start!! ");
		}

	}

}

