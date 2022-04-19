import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

//import com.st1.itx.util.dump.HexDump;

@SuppressWarnings("unused")
public class tConnect {

	// 1122轉{0x11,0x12}
	public static byte[] hexString2Bytes(String s) {
		byte[] bytes;
		bytes = new byte[s.length() / 2];

		for (int i = 0; i < bytes.length; i++) {
			// 十六轉十進制
			bytes[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2), 16);
		}

		return bytes;
	}

	// {0x11,0x12}轉1122
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder();
		if (src == null || src.length <= 0) {
			return null;
		}
		char[] buffer = new char[2];
		for (int i = 0; i < src.length; i++) {
			buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
			buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
			stringBuilder.append(buffer);
		}
		return stringBuilder.toString();
	}

	public static void main(String[] args) {
		String address = "192.168.10.8";// 連線的ip

//		final Socket client = new Socket();
//		final InetSocketAddress isa = new InetSocketAddress(address, port);
		int i = 0;
		while (true) {
			i++;
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						String address = "192.168.10.8";// 連線的ip
						int port = 50001;// 連線的port
						Socket client = new Socket();
						// client.setSendBufferSize(2 * 1024);
						// client.setKeepAlive(true);
						InetSocketAddress isa = new InetSocketAddress(address, port);
						client.connect(isa, 90000);
						BufferedOutputStream out = new BufferedOutputStream(client.getOutputStream());
						// 送出字串

						byte[] msg = "{\"KINBR\":\"0000\",\"TLRNO\":\"E-LOAN\",\"TXTNO\":\"00000000\",\"ENTDY\":\"1090420\",\"ORGKIN\":\"\",\"ORGTLR\":\"\",\"ORGTNO\":\"00000000\",\"ORGDD\":\"00\",\"TRMTYP\":\"EL\",\"TXCD\":\"L7100\",\"MRKEY\":\"\",\"CIFKEY\":\"A123456789\",\"CIFERR\":\"\",\"HCODE\":\"0\",\"CRDB\":\"0\",\"HSUPCD\":\"0\",\"CURCD\":\"00\",\"CURNM\":\"TWD\",\"TXAMT\":\"000000000000.00\",\"EMPNOT\":\"CU3178\",\"EMPNOS\":\"CU3178\",\"CALDY\":\"20201007\",\"CALTM\":\"09100000\",\"MTTPSEQ\":\"0\",\"TOTAFG\":\"0\",\"OBUFG\":\"0\",\"ACBRNO\":\"0000\",\"RBRNO\":\"0000\",\"FBRNO\":\"0000\",\"RELCD\":\"1\",\"ACTFG\":\"0\",\"SECNO\":\"\",\"MCNT\":\"0\",\"TITFCD\":\"0\",\"RELOAD\":\"0\",\"BATCHNO\":\"\",\"DELAY\":\"0\",\"FMTCHK\":\"\",\"FROMMQ\":\"\",\"FUNCIND\":\"0\",\"LockNo\": \"00000000000\", \"LockCustNo\": \"0000000\",\"AUTHNO\": \"      \",\"AGENT\": \"      \",\"BODY\":[{\"TranCode\":\"L1105\",\"FunCd\":\"1\",\"CustId\":\"A123456789\",\"CustTelSeq\":\"1\",\"TelTypeCode\":\"01\",\"TelArea\":\"02   \",\"TelNo\":\"23895858  \",\"TelExt\":\"1142 \",\"Mobile\":\"\",\"TelChgRsnCode\":\"01\",\"RelationCode\":\"\",\"LiaisonName\":\"\",\"Rmk\":\"\",\"Enable\":\"Y\",\"StopReason\":\"1\",\"TelNoUKey\":\"1\"}]}"
								.getBytes("UTF-8");

						byte[] s2;
//						byte[] s3 = new byte[msg.length + (msg.length / 1022 * 2) + 2];
						byte[] s3 = new byte[msg.length + 5];
						byte[] sign = "$_".getBytes("UTF-8");
						int len = msg.length;
						int index = 0, n = 0;

						String sLen = String.format("%05d", msg.length);
						byte[] bb = sLen.getBytes();

						System.arraycopy(bb, 0, s3, 0, 5);
						System.arraycopy(msg, 0, s3, 5, msg.length);
//						System.out.println(HexDump.dumpHexString(s3));
//						System.out.println(s3.length);

//						while (true) {
//							if (!client.isConnected()) {
//								System.out.println("bind off ");
//								break;
//							} else {
//								System.out.println(client.isConnected());
//							}
//							if (len <= 1022) {
//								s2 = new byte[len + 2];
//								System.arraycopy(msg, index, s2, 0, len);
//								System.arraycopy(sign, 0, s2, s2.length - 2, 2);
//
//								System.out.println("f  " + (1024 * n) + "  " + (s2.length) + " s3.length : " + s3.length);
//								System.arraycopy(s2, 0, s3, 1024 * n, s2.length);
//								break;
//							}
//							s2 = new byte[1024];
//							System.arraycopy(msg, index, s2, 0, 1022);
//							System.arraycopy(sign, 0, s2, s2.length - 2, 2);
//
//							System.arraycopy(s2, 0, s3, 1024 * n, 1024);
//
//							len = len - 1022;
//							index += 1022;
//
//							System.out.println(index);
//							// Thread.sleep(2000);
//
//							n++;
//						}

						out.write(s3);
//						out.write(
//								"{\"KINBR\":\"0017\",\"TLRNO\":\"001746\",\"TXTNO\":\"00000000\",\"ENTDD\":\"16\",\"ORGKIN\":\"0000\",\"ORGTLR\":\"  \",\"ORGTNO\":\"00000000\",\"ORGDD\":\"00\",\"TRMTYP\":\"  \",\"TXCD\":\"XX004\",\"MRKEY\":\"                    \",\"CIFKEY\":\"          \",\"CIFERR\":\" \",\"HCODE\":\"0\",\"CRDB\":\"0\",\"HSUPCD\":\"0\",\"CURCD\":\"00\",\"CURNM\":\"   \",\"TXAMT\":\"0.00\",\"EMPNOT\":\"001700\",\"EMPNOS\":\"0017  \",\"CALDY\":\"00000000\",\"CALTM\":\"00000000\",\"MTTPSEQ\":\"00\",\"TOTAFG\":\"0\",\"OBUFG\":\"2\",\"ACBRNO\":\"0017\",\"RBRNO\":\"0017\",\"FBRNO\":\"0017\",\"RELCD\":\"1\",\"ACTFG\":\"0\",\"SECNO\":\"  \",\"MCNT\":\"00\",\"TITFCD\":\"0\",\"RELOAD\":\"0\",\"BATCHNO\":\"            \",\"DELAY\":\"0\",\"FMTCHK\":\" \",\"FROMMQ\":\"    \",\"FUNCIND\":\"0\",\"SYSFIL17\":\"                 \",\"END\":\"$\",\"rim\":\"0\",\"TXCODE\":\"XX004\",\"BRTLRNO\":\"0017001746\",\"RQSP\":\"\",\"SUPNO\":\"\",\"LEVEL\":\"3\",\"PBRNO\":\"\"}$_"
//										.getBytes());
//						String path = "C:\\D\\text.txt";
//						String text = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
//						out.write(text.getBytes());
//						Thread.sleep(10000);

						out.flush();
						InputStream in = client.getInputStream();
						byte[] b = new byte[65535];
						in.read(b);

						System.out.println(new String(b, "UTF-8").trim());

						out.close();
						in.close();
						out = null;
						client.close();
					} catch (Exception e) {
						StringWriter errors = new StringWriter();
						e.printStackTrace(new PrintWriter(errors));
						System.out.println(errors.toString());
					}
				}
			}).start();

//			new Thread(new Runnable() {
//				@Override
//				public void run() {
//					try {
//						String address = "192.168.44.128";// 連線的ip
//						int port = 55688;// 連線的port
//						Socket client = new Socket();
//						InetSocketAddress isa = new InetSocketAddress(address, port);
//						client.connect(isa, 10000);
//						BufferedOutputStream out = new BufferedOutputStream(client.getOutputStream());
//						// 送出字串
//						out.write("Send From Client 你好Netty!!!!!@$$%^&*()".getBytes());
////						Thread.sleep(5000);
//						out.flush();
////						InputStream in = client.getInputStream();
////						byte[] b = new byte[1024];
////						in.read(b);
//
////						System.out.println(new String(b));;
//						out.close();
////						in.close();
//						out = null;
//						client.close();
//					} catch (Exception e) {
//						StringWriter errors = new StringWriter();
//						e.printStackTrace(new PrintWriter(errors));
//						System.out.println(errors.toString());
//					}
//				}
//			}).start();
//			try {
//				Thread.sleep(50);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			if (i > 0)
				break;
		}
//		byte[] a = { 0x41, 0x15, 0x42, 0x15, 0x43 };
//		String b = new String(a).replace((char) 0x15, (char) 0x20);
//		System.out.println(b);
//		System.out.println(HexDump.dumpHexString(b.getBytes()));
//
//		String amlResponse;
//		/* 錯誤回送中心 */
//		amlResponse = "<ns2:NameCheckResponse xmlns:ns2=\"http://nameCheck.webservice.sas.com/\"><return><nc_reference_id>referenceId</nc_reference_id><unique_key>uniqueKey</unique_key><status_code>statusCode</status_code><status_message>statusMessage</status_message><nc_result>abcd\r\nefg\n</nc_result><hit_list_session></hit_list_session><hit_seq></hit_seq><seq><check_seq>01</check_seq><check_result></check_result><hit_list></hit_list></seq></return></ns2:NameCheckResponse>";
//		amlResponse = amlResponse.replaceFirst("referenceId", "0");
//		amlResponse = amlResponse.replaceFirst("uniqueKey", "123");
//		amlResponse.replaceFirst("statusCode", "T");
//		amlResponse.replaceFirst("statusMessage", "Read Timed Out");
//		amlResponse.replaceFirst("statusCode", "E");
//		amlResponse.replaceFirst("statusMessage", "Aml Service Error");
//
//		try {
//			Source xmlInput = new StreamSource(new StringReader(amlResponse));
//			StringWriter stringWriter = new StringWriter();
//			StreamResult xmlOutput = new StreamResult(stringWriter);
//			TransformerFactory transformerFactory = TransformerFactory.newInstance();
//			transformerFactory.setAttribute("indent-number", 2);
//			Transformer transformer = transformerFactory.newTransformer();
//			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//			transformer.transform(xmlInput, xmlOutput);
//			System.out.println(xmlOutput.getWriter().toString());
//		} catch (Exception e) {
//			throw new RuntimeException(e); // simple exception handling, please review it
//		}
//		System.out.println(amlResponse);
	}

}
