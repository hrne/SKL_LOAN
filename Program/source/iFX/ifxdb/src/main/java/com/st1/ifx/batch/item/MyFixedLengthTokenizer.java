package com.st1.ifx.batch.item;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.transform.AbstractLineTokenizer;
import org.springframework.batch.item.file.transform.IncorrectLineLengthException;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.batch.item.file.transform.RangeArrayPropertyEditor;

public class MyFixedLengthTokenizer extends AbstractLineTokenizer {
	private static final Logger logger = LoggerFactory.getLogger(MyFixedLengthTokenizer.class);
	private Range[] ranges;

	private int maxRange = 0;

	boolean open = false;

	private String encoding;

	public void setEncoding(String enc) {
		this.encoding = enc;
		logger.info("Encoding" + enc);
	}

	private boolean strict = false;

	private String endOfLine = null;

	public void setEndOfLine(String eol) {
		this.endOfLine = eol;
	}

	/**
	 * Public setter for the strict flag. If true (the default) then lines must be
	 * precisely the length specified by the columns. If false then shorter lines
	 * will be tolerated and padded with empty columns, and longer strings will
	 * simply be truncated.
	 * 
	 * @see #setColumns(Range[])
	 * 
	 * @param strict the strict to set
	 */
	public void setStrict(boolean strict) {
		this.strict = strict;
	}

	/**
	 * Set the column ranges. Used in conjunction with the
	 * {@link RangeArrayPropertyEditor} this property can be set in the form of a
	 * String describing the range boundaries, e.g. "1,4,7" or "1-3,4-6,7" or
	 * "1-2,4-5,7-10". If the last range is open then the rest of the line is read
	 * into that column (irrespective of the strict flag setting).
	 * 
	 * @see #setStrict(boolean)
	 * 
	 * @param ranges the column ranges expected in the input
	 */
	public void setColumns(Range[] ranges) {
		this.ranges = ranges;
		calculateMaxRange(ranges);
	}

	/*
	 * Calculate the highest value within an array of ranges. The ranges aren't
	 * necessarily in order. For example: "5-10, 1-4,11-15". Furthermore, there
	 * isn't always a min and max, such as: "1,4-20, 22"
	 */
	private void calculateMaxRange(Range[] ranges) {
		if (ranges == null || ranges.length == 0) {
			maxRange = 0;
			return;
		}

		open = false;
		maxRange = ranges[0].getMin();

		for (int i = 0; i < ranges.length; i++) {
			int upperBound;
			if (ranges[i].hasMaxValue()) {
				upperBound = ranges[i].getMax();
			} else {
				upperBound = ranges[i].getMin();
				if (upperBound > maxRange) {
					open = true;
				}
			}

			if (upperBound > maxRange) {
				maxRange = upperBound;
			}
		}
	}

	/**
	 * Yields the tokens resulting from the splitting of the supplied
	 * <code>line</code>.
	 * 
	 * @param line the line to be tokenised (can be <code>null</code>)
	 * 
	 * @return the resulting tokens (empty if the line is null)
	 * @throws IncorrectLineLengthException if line length is greater than or less
	 *                                      than the max range set.
	 */
	protected List<String> doTokenize(String line) {
		List<String> tokens = new ArrayList<String>(ranges.length);
		int lineLength;
		String token;
		String myToken;

		MyTextBuf textbuf = new MyTextBuf(line, this.encoding);

		// lineLength = line.length();
		lineLength = textbuf.length();

		if (this.endOfLine != null) {
			if (line.charAt(line.length() - 1) != this.endOfLine.charAt(0)) {
				throw new IncorrectLineLengthException("Line is not end with " + this.endOfLine, maxRange, lineLength);
			}
		}
		if (lineLength < maxRange && strict) {
			throw new IncorrectLineLengthException("Line is shorter than max range " + maxRange, maxRange, lineLength);
		}

		if (!open && lineLength > maxRange && strict) {
			throw new IncorrectLineLengthException("Line is longer than max range " + maxRange, maxRange, lineLength);
		}

		for (int i = 0; i < ranges.length; i++) {

			int startPos = ranges[i].getMin() - 1;
			int endPos = ranges[i].getMax();

			if (lineLength >= endPos) {
				// token = line.substring(startPos, endPos);
				myToken = textbuf.substring(startPos, endPos);

			} else if (lineLength >= startPos) {
				// token = line.substring(startPos);
				myToken = textbuf.substring(startPos);
			} else {
				token = "";
				myToken = "";
			}

			// System.out.println(" token:[" + token + "]");
			logger.info("mytoken:[" + myToken + "]");

			tokens.add(myToken);
		}

		return tokens;
	}
}
