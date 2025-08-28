package com.pinback.shared.util;

import java.text.BreakIterator;
import java.util.Locale;

public final class TextUtil {

	private TextUtil() {
	}

	public static int countGraphemeClusters(String text) {
		if (text == null || text.isEmpty()) {
			return 0;
		}

		BreakIterator it = BreakIterator.getCharacterInstance(Locale.getDefault());
		it.setText(text);

		int count = 0;
		int currentBoundary = it.first();

		while (currentBoundary != BreakIterator.DONE) {
			int nextBoundary = it.next();

			if (nextBoundary != BreakIterator.DONE) {
				count++;
			}
			currentBoundary = nextBoundary;
		}
		return count;
	}
}
