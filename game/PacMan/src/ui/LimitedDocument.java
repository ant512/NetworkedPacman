package ui;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Class representing a document (for use in text areas) that cannot exceed a
 * given string length.
 */
public class LimitedDocument extends PlainDocument {

	// Members
	private int mMaxLength;

	/**
	 * Constructor.
	 * @param length Maximum length of the text in the document.
	 */
	public LimitedDocument(int length) {
		mMaxLength = length;
	}

	/**
	 * Prevent strings being inserted that exceed the maximum length allowed.
	 * @param offs Offset to insert at.
	 * @param str String to insert.
	 * @param a Attributes.
	 * @throws javax.swing.text.BadLocationException Thrown if offset exceeds
	 * allowed offset.
	 */
	@Override
	public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {

		int newLen = getLength() + str.length();

		if (newLen > mMaxLength) {
			if (mMaxLength - newLen > 0) {
				str = str.substring(0, mMaxLength - newLen);
			} else {
				str = "";
			}
		}

		super.insertString(offs, str, a);
	}
}