package uk.rayware.nitrogenproxy.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ChatColour {

	public static String translate(String textToTranslate) {
		char[] b = textToTranslate.toCharArray();

		for (int i = 0; i < b.length - 1; i++) {
			if (b[i] == '&' && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i+1]) > -1) {
				b[i] = '\u00A7';
				b[i+1] = Character.toLowerCase(b[i+1]);
			}
		}

		return new String(b);
	}

}
