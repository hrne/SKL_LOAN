package com.st1.util.dump;

//System imports

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

//Local imports

//(None)

/*******************************************************************************
 * Hexadecimal dump utility methods.
 * 
 * @version $Revision: 1.4 $ $Date: 2003/02/18 00:33:11 $
 * @since 2000-02-08
 * @author David R. Tribble,
 *         <a href="mailto:david@tribble.com">david@tribble.com</a>. <br>
 *         Copyright @2000-2003 by David R. Tribble, all rights reserved. <br>
 *         Permission is granted to freely use and distribute this source code
 *         provided that the original copyright and authorship notices remain
 *         intact.
 * 
 * @see HexDumper
 */

public abstract class HexDump2 {
	// Identification

	/** Class version number. */
	public static final int VERS = 100;

	/** Revision info. */
	static final String REV = "@(#)tribble/util/HexDump.java $Revision: 1.4 $ $Date: 2003/02/18 00:33:11 $\n";

	/** Copyright info. */
	static final String COPYRIGHT = "Copyright @2000-2003 by David R. Tribble, all rights reserved.";

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	// - -
	// Constants

	// (None)

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	// - -
	// Static class initializers

	/*
	 * +++TEST CODE, NO LONGER USED // Test vectors for hexDumpString() static {
	 * String s;
	 * 
	 * hexDump(System.out, "Hello, world\n................................." +
	 * "xxxxx\0xxx\u202Bxx\uFFFFxxxx/" +
	 * "4%%%%\t%%%%%%%%%%4%%%%%%%%%%%%%%%%4%%%%%%%%%%%%4%%%%%%%%%%%%4[]" +
	 * "5%%%%%%%%%%%%%%%5%%%%%%%%%%%%%%%%5%%%%%%%%%%%%5%%%%%%%%%%%%5[]" +
	 * "6%%%%%%%%%%%%%%%6%%%%%%%%%%%%%%%%6%%%%%%%%%%%%6%%%%%%%%%%%%6[]");
	 * System.out.println();
	 * 
	 * hexDump(System.out, "Hello, world\n"); System.out.println();
	 * 
	 * hexDump(System.out, "abuuuuuuuukkkkkkkkk"); System.out.println();
	 * 
	 * hexDump(System.out, "abuuuuuuuukkkkkkkkkk"); System.out.println();
	 * 
	 * hexDump(System.out, "abuuuuuuuukkkkkkkkkkx"); System.out.println();
	 * 
	 * s = "abuuuuuuuukkkkkkkkkkx";
	 * 
	 * hexDump(System.out, s, 0, 0); System.out.println();
	 * 
	 * hexDump(System.out, s, 0, 4); System.out.println();
	 * 
	 * hexDump(System.out, s, 0, s.length()+100); System.out.println();
	 * 
	 * hexDump(System.out, s, 1, s.length()); System.out.println();
	 * 
	 * hexDump(System.out, s, 100, 4); System.out.println(); } +++
	 */

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	// - -
	// Public static methods

	/***************************************************************************
	 * Print the contents of a byte array as a hexadecimal dump to an output stream.
	 * 
	 * <p>
	 * This method is equivalent to:
	 * 
	 * <pre>
	 * hexDump(out, data, 0, data.length);
	 * </pre>
	 * 
	 * @param out  An output stream.
	 * 
	 * @param data An array of bytes to dump to <tt>out</tt>.
	 * 
	 * @since 1.3, 2003-02-15
	 */

	public static void hexDump(PrintWriter out, byte[] data) {
		// Dump the characters in 'data'
		hexDump(out, data, 0, data.length);
	}

	/***************************************************************************
	 * Print the contents of a byte array as a hexadecimal dump to an output stream.
	 * 
	 * @param out  An output stream.
	 * 
	 * @param data An array of bytes to dump to <tt>out</tt>.
	 * 
	 * @param off  The index of the first byte within the byte array to dump.
	 * 
	 * @param len  The number of bytes within the byte array to dump.
	 * 
	 * @since 1.3, 2003-02-15
	 */

	public static void hexDump(PrintWriter out, byte[] data, int off, int len) {
		// Check args
		if (out == null)
			return;

		if (data == null) {
			out.println("Null");
			return;
		}

		// Dump the bytes in 'data'
		hexDumpAt(out, data, off, len, 0);
		out.flush();
	}

	/***************************************************************************
	 * Print the contents of a byte array as a hexadecimal dump to an output stream.
	 * 
	 * <p>
	 * This method is equivalent to:
	 * 
	 * <pre>
	 * hexDump(out, data, 0, data.length);
	 * </pre>
	 * 
	 * @param out  An output stream.
	 * 
	 * @param data An array of bytes to dump to <tt>out</tt>.
	 * 
	 * @since 1.3, 2003-02-15
	 */

	public static void hexDump(PrintStream out, byte[] data) {
		// Dump the bytes in 'data'
		hexDump(new PrintWriter(out, false), data, 0, data.length);
	}

	/***************************************************************************
	 * Print the contents of a byte array as a hexadecimal dump to an output stream.
	 * 
	 * @param out  An output stream.
	 * 
	 * @param data An array of bytes to dump to <tt>out</tt>.
	 * 
	 * @param off  The index of the first byte within the byte array to dump.
	 * 
	 * @param len  The number of bytes within the byte array to dump.
	 * 
	 * @since 1.3, 2003-02-15
	 */

	public static void hexDump(PrintStream out, byte[] data, int off, int len) {
		// Dump the bytes in 'data'
		hexDump(new PrintWriter(out, false), data, off, len);
	}

	/***************************************************************************
	 * Print the contents of a string as a hexadecimal dump to an output stream.
	 * 
	 * <p>
	 * This method is equivalent to:
	 * 
	 * <pre>
	 * hexDump(out, s, 0, s.length());
	 * </pre>
	 * 
	 * @param out An output stream.
	 * 
	 * @param s   A string of Unicode characters to dump to <tt>out</tt>.
	 * 
	 * @since 1.3, 2003-02-15
	 */

	public static void hexDump(PrintWriter out, String s) {
		// Dump the characters in 's'
		hexDump(out, s, 0, s.length());
	}

	/***************************************************************************
	 * Print the contents of a string as a hexadecimal dump to an output stream.
	 * 
	 * @param out An output stream.
	 * 
	 * @param off The index of the first character within the string to dump.
	 * 
	 * @param len The number of bytes within the byte array to dump.
	 * 
	 * @param s   A string of Unicode characters to dump to <tt>out</tt>.
	 * 
	 * @since 1.3, 2003-02-15
	 */

	public static void hexDump(PrintWriter out, String s, int off, int len) {
		// Check args
		if (out == null)
			return;

		if (s == null) {
			out.println("Null");
			return;
		}

		// Dump the characters in 's'
		hexDumpStringAt(out, s, off, len, 0);
		out.flush();
	}

	/***************************************************************************
	 * Print the contents of a string as a hexadecimal dump to an output stream.
	 * 
	 * <p>
	 * This method is equivalent to:
	 * 
	 * <pre>
	 * hexDump(out, s, 0, s.length());
	 * </pre>
	 * 
	 * @param out An output stream.
	 * 
	 * @param s   A string of Unicode characters to dump to <tt>out</tt>.
	 * 
	 * @since 1.3, 2003-02-15
	 */

	public static void hexDump(PrintStream out, String s) {
		// Dump the characters in 's'
		hexDump(new PrintWriter(out, false), s, 0, s.length());
	}

	/***************************************************************************
	 * Print the contents of a string as a hexadecimal dump to an output stream.
	 * 
	 * @param out An output stream.
	 * 
	 * @param off The index of the first character within the string to dump.
	 * 
	 * @param len The number of bytes within the byte array to dump.
	 * 
	 * @param s   A string of Unicode characters to dump to <tt>out</tt>.
	 * 
	 * @since 1.3, 2003-02-15
	 */

	public static void hexDump(PrintStream out, String s, int off, int len) {
		hexDump(new PrintWriter(out, false), s, off, len);
	}

	/***************************************************************************
	 * Print the contents of an input stream as a hexadecimal dump to an output
	 * stream.
	 * 
	 * <p>
	 * Note that any {@link java.io.IOException}s thrown while reading from the
	 * input stream or writing to the output stream are ignored.
	 * 
	 * @param in  An input (byte) stream. Note that this stream is <i>not</i> closed
	 *            after reading its contents.
	 * 
	 * @param out An output stream.
	 * 
	 * @since 1.3, 2003-02-15
	 */

	public static void hexDump(InputStream in, PrintWriter out) {
		// Check args
		if (out == null)
			return;

		if (in == null)
			throw new NullPointerException("Null input stream");

		// Read and dump the contents of the input stream
		try {
			byte[] data; // Input data block
			int off; // Leftover data bytes
			int addr; // Data address

			// Read and dump the contents of the input stream
			data = new byte[64 * 1024];
			off = 0;
			addr = 0;

			for (;;) {
				int len;
				int dlen;

				// Read a block of data from the file
				len = in.read(data, off, data.length - off);
				if (len <= 0)
					len = 0;

				// Dump the data block in hexadecimal
				dlen = (off + len) & ~0x0000000F; // = (off+len)/16 * 16
				hexDumpAt(out, data, 0, dlen, addr);
				addr += dlen;

				// Save any leftover bytes for the next iteration
				off = off + len - dlen;
				if (off > 0)
					System.arraycopy(data, dlen, data, 0, off);

				// Check for end of file
				if (len == 0) {
					// End of file reached, dump the last remaining data bytes
					hexDumpAt(out, data, 0, off, addr);
					addr += off;

					// Stop reading and dumping
					break;
				}
			}

			out.flush();
		} catch (IOException ex) {
			// Ignore
		}
	}

	/***************************************************************************
	 * Print the contents of an input stream as a hexadecimal dump to an output
	 * stream.
	 * 
	 * <p>
	 * Note that any {@link java.io.IOException}s thrown while reading from the
	 * input stream or writing to the output stream are ignored.
	 * 
	 * @param in  An input (byte) stream. Note that this stream is <i>not</i> closed
	 *            after reading its contents.
	 * 
	 * @param out An output stream.
	 * 
	 * @since 1.3, 2003-02-15
	 */

	public static void hexDump(InputStream in, PrintStream out) {
		hexDump(in, new PrintWriter(out, false));
	}

	/***************************************************************************
	 * Print the contents of a byte array as a hexadecimal dump to an output stream.
	 * 
	 * @param out  An output stream.
	 * 
	 * @param data An array of bytes to dump to <tt>out</tt>.
	 * 
	 * @param off  The index of the first byte within the byte array to dump.
	 * 
	 * @param len  The number of bytes within the byte array to dump
	 * 
	 * @param base The base address to assume in the printed dump.
	 * 
	 * @since 1.4, 2003-02-17 (2003-02-15)
	 */

	public static void hexDumpAt(PrintWriter out, byte[] data, int off, int len, int base) {
		int loc;
		int end;

		// Print a hexadecimal dump of 'data[off...off+len-1]'
		if (off >= data.length)
			off = data.length;

		end = off + len;
		if (end >= data.length)
			end = data.length;

		len = end - off;
		if (len <= 0)
			return;

		loc = (off / 0x10) * 0x10;

		for (int i = loc; i < end; i += 0x10, loc += 0x10) {
			int j;

			// Print the location/offset
			{
				int v;

				v = base + loc;
				for (j = (8 - 1) * 4; j >= 0; j -= 4) {
					int d;

					d = (v >>> j) & 0x0F;
					d = (d < 0xA ? d + '0' : d - 0xA + 'A');
					out.print((char) d);
				}
			}

			// Print a row of hex bytes
			out.print("  ");
			for (j = 0x00; i + j < off; j++)
				out.print(".. ");

			for (; j < 0x10 && i + j < end; j++) {
				int ch;
				int d;

				if (j == 0x08)
					out.print(' ');

				ch = data[i + j] & 0xFF;

				d = (ch >>> 4);
				d = (d < 0xA ? d + '0' : d - 0xA + 'A');
				out.print((char) d);

				d = (ch & 0x0F);
				d = (d < 0xA ? d + '0' : d - 0xA + 'A');
				out.print((char) d);

				out.print(' ');
			}

			for (; j < 0x10; j++) {
				if (j == 0x08)
					out.print(' ');

				out.print(".. ");
			}

			// Print a row of printable characters
			out.print(" |");
			for (j = 0x00; i + j < off; j++)
				out.print(' ');

			for (; j < 0x10 && i + j < end; j++) {
				int ch;

				ch = data[i + j] & 0xFF;

				if (ch < 0x20 || ch >= 0x7F && ch < 0xA0 || ch > 0xFF) {
					// The character is unprintable
					ch = '.';
				}

				out.print((char) ch);
			}

			for (; j < 0x10; j++)
				out.print(' ');

			out.println("|");
		}
	}

	/***************************************************************************
	 * Print the contents of a string as a hexadecimal dump to an output stream.
	 * 
	 * @param out  An output stream.
	 * 
	 * @param data A string of Unicode characters to dump to <tt>out</tt>.
	 * 
	 * @param off  The index of the first byte within the byte array to dump.
	 * 
	 * @param len  The number of bytes within the byte array to dump
	 * 
	 * @param base The base address to assume in the printed dump.
	 * 
	 * @since 1.4, 2003-02-17 (2003-02-15)
	 */

	public static void hexDumpStringAt(PrintWriter out, String data, int off, int len, int base) {
		int loc;
		int end;
		char[] h;

		// Print a hexadecimal dump of 'data[off...off+len-1]'
		if (off >= data.length())
			off = data.length();

		end = off + len;
		if (end >= data.length())
			end = data.length();

		len = end - off;
		if (len <= 0)
			return;

		loc = (off / 0x10) * 0x10;
		h = new char[10];

		for (int i = loc; i < end; i += 10, loc += 10) {
			int j;

			// Print the location/offset
			{
				int v;

				v = base + loc;

				for (j = 1; j <= 10; j++) {
					int d;

					d = (v % 10) + '0';
					v = (v / 10);
					h[10 - j] = (char) d;
				}

				// Print the location/offset as a 4-10 digit decimal number
				for (j = 0; j < 10 - 1; j++) {
					if (h[j] == '0')
						h[j] = ' ';
					else
						break;
				}
				for (j = 0; j < 6; j++)
					if (h[j] != ' ')
						break;
				for (; j < 10; j++)
					out.print(h[j]);
			}

			// Print a row of hex bytes
			out.print(": ");
			for (j = 0; i + j < off; j++)
				out.print(".... ");

			for (; j < 10 && i + j < end; j++) {
				int ch;
				int d;

				ch = (int) data.charAt(i + j);

				d = (ch >>> 12) & 0x0F;
				d = (d < 0xA ? d + '0' : d - 0xA + 'A');
				out.print((char) d);

				d = (ch >>> 8) & 0x0F;
				d = (d < 0xA ? d + '0' : d - 0xA + 'A');
				out.print((char) d);

				d = (ch >>> 4) & 0x0F;
				d = (d < 0xA ? d + '0' : d - 0xA + 'A');
				out.print((char) d);

				d = (ch & 0x0F);
				d = (d < 0xA ? d + '0' : d - 0xA + 'A');
				out.print((char) d);

				out.print(' ');
			}

			for (; j < 10; j++)
				out.print(".... ");

			// Print a row of printable characters
			out.print('|');
			for (j = 0; i + j < off; j++)
				out.print(' ');

			for (; j < 10 && i + j < end; j++) {
				int ch;

				ch = (int) data.charAt(i + j);

				if (ch < 0x0020 || ch >= 0x007F && ch < 0x00A0 || ch > 0x00FF) {
					// Assume that the character is unprintable
					ch = '.';
				}

				out.print((char) ch);
			}

			for (; j < 10; j++)
				out.print(' ');

			out.println("|");
		}
	}

	/***************************************************************************
	 * Print the contents of one or more files as a hexadecimal dump to the standard
	 * output stream.
	 * 
	 * <!-------------------------------------------------------------------- -->
	 * <p>
	 * <b> Usage </b>
	 * 
	 * <pre>
	 *    java tribble.util.HexDump <i>file...</i>
	 * </pre>
	 * 
	 * @param args Command line arguments (filenames to dump).
	 * 
	 * @since 1.1, 2000-02-08
	 */

	public static void main(String[] args) {
		PrintWriter out;

		// // Check args
		// if (args.length < 1) {
		// // Display a usage message
		// System.out.println("Dump the contents of one or more files in "
		// + "hexadecimal.");
		// System.out.println();
		// System.out.println("usage: java " + HexDump.class.getName()
		// + " file...");
		// System.out.println();
		// System.out.println("A filename of \"-\" indicates input is to be "
		// + "read from the standard input.");
		//
		// // Punt
		// System.exit(255);
		// }
		//
		// // Set up
		// out = new PrintWriter(System.out, true);
		//
		// // Dump the contents of the filename args
		// for (int i = 0; i < args.length; i++) {
		// String fname;
		// InputStream in = null;
		//
		// // Open the next filename
		// fname = args[i];
		//
		// try {
		// // Open the next filename
		// if (fname.equals("-")) {
		// // Read input from standard input
		// in = System.in;
		// } else {
		// // Read input from a named file
		// in = new FileInputStream(new File(fname));
		// }
		//
		// // Dump the contents of the file
		// if (i > 0)
		// out.println();
		//
		// out.print(fname);
		// out.println(":");
		//
		// hexDump(in, out);
		// } catch (FileNotFoundException ex) {
		// // Can't open/read the file
		// System.out.println("Can't open/read file: " + fname + ": "
		// + ex.getMessage());
		// } catch (IOException ex) {
		// // Can't open/read the file
		// System.out.println("I/O error for: " + fname + ": "
		// + ex.getMessage());
		// }
		//
		// try {
		// // Clean up
		// out.flush();
		// if (in != null && in != System.in)
		// in.close();
		// } catch (IOException ex) {
		// // Ignore
		// }
		// }

		hexDump(System.out, "abuuuuuuuukkkkkkkkk");
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	// - -
	// Variables

	// (None)

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	// - -
	// Constructors

	/***************************************************************************
	 * Default constructor.
	 * 
	 * @since 1.1, 2000-02-08
	 */

	private HexDump2() {
		// Do nothing
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	// - -
	// Methods

	// (None)
}

// End HexDump.java
