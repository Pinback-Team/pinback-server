package com.pinback.pinback_server.global.common.util;

import java.text.BreakIterator;
import java.util.Locale;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TextUtil {

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
		log.info("글자수: {}", count);
		return count;
	}
}
