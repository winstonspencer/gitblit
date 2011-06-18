/*
 * Copyright 2011 gitblit.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gitblit.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;

public class StringUtils {

	public static final String MD5_TYPE = "MD5:";

	public static boolean isEmpty(String value) {
		return value == null || value.trim().length() == 0;
	}

	public static String breakLinesForHtml(String string) {
		return string.replace("\r\n", "<br/>").replace("\r", "<br/>").replace("\n", "<br/>");
	}

	public static String escapeForHtml(String inStr, boolean changeSpace) {
		StringBuffer retStr = new StringBuffer();
		int i = 0;
		while (i < inStr.length()) {
			if (inStr.charAt(i) == '&') {
				retStr.append("&amp;");
			} else if (inStr.charAt(i) == '<') {
				retStr.append("&lt;");
			} else if (inStr.charAt(i) == '>') {
				retStr.append("&gt;");
			} else if (inStr.charAt(i) == '\"') {
				retStr.append("&quot;");
			} else if (changeSpace && inStr.charAt(i) == ' ') {
				retStr.append("&nbsp;");
			} else if (changeSpace && inStr.charAt(i) == '\t') {
				retStr.append(" &nbsp; &nbsp;");
			} else {
				retStr.append(inStr.charAt(i));
			}
			i++;
		}
		return retStr.toString();
	}

	public static String encodeURL(String inStr) {
		StringBuffer retStr = new StringBuffer();
		int i = 0;
		while (i < inStr.length()) {
			if (inStr.charAt(i) == '/') {
				retStr.append("%2F");
			} else if (inStr.charAt(i) == ' ') {
				retStr.append("%20");
			} else {
				retStr.append(inStr.charAt(i));
			}
			i++;
		}
		return retStr.toString();
	}

	public static String flattenStrings(List<String> values) {
		return flattenStrings(values, " ");
	}

	public static String flattenStrings(List<String> values, String separator) {
		StringBuilder sb = new StringBuilder();
		for (String value : values) {
			sb.append(value).append(separator);
		}
		return sb.toString().trim();
	}

	public static String trimString(String value, int max) {
		if (value.length() <= max) {
			return value;
		}
		return value.substring(0, max - 3) + "...";
	}

	public static String trimShortLog(String string) {
		return trimString(string, 60);
	}

	public static String leftPad(String input, int length, char pad) {
		if (input.length() < length) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0, len = length - input.length(); i < len; i++) {
				sb.append(pad);
			}
			sb.append(input);
			return sb.toString();
		}
		return input;
	}

	public static String rightPad(String input, int length, char pad) {
		if (input.length() < length) {
			StringBuilder sb = new StringBuilder();
			sb.append(input);
			for (int i = 0, len = length - input.length(); i < len; i++) {
				sb.append(pad);
			}
			return sb.toString();
		}
		return input;
	}

	public static String getSHA1(String text) {
		try {
			byte[] bytes = text.getBytes("iso-8859-1");
			return getSHA1(bytes);
		} catch (UnsupportedEncodingException u) {
			throw new RuntimeException(u);
		}
	}

	public static String getSHA1(byte[] bytes) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(bytes, 0, bytes.length);
			byte[] digest = md.digest();
			return toHex(digest);
		} catch (NoSuchAlgorithmException t) {
			throw new RuntimeException(t);
		}
	}

	public static String getMD5(String string) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.reset();
			md.update(string.getBytes("iso-8859-1"));
			byte[] digest = md.digest();
			return toHex(digest);
		} catch (UnsupportedEncodingException u) {
			throw new RuntimeException(u);
		} catch (NoSuchAlgorithmException t) {
			throw new RuntimeException(t);
		}
	}

	private static String toHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			if (((int) bytes[i] & 0xff) < 0x10) {
				sb.append('0');
			}
			sb.append(Long.toString((int) bytes[i] & 0xff, 16));
		}
		return sb.toString();
	}
	
	public static String getRootPath(String path) {
		if (path.indexOf('/') > -1) {
			return path.substring(0, path.lastIndexOf('/'));
		}
		return "";
	}

	public static String getRelativePath(String basePath, String fullPath) {
		String relativePath = fullPath.substring(basePath.length()).replace('\\', '/');
		if (relativePath.charAt(0) == '/') {
			relativePath = relativePath.substring(1);
		}
		return relativePath;
	}

	public static List<String> getStringsFromValue(String value) {
		return getStringsFromValue(value, " ");
	}

	public static List<String> getStringsFromValue(String value, String separator) {
		List<String> strings = new ArrayList<String>();
		try {
			String[] chunks = value.split(separator);
			for (String chunk : chunks) {
				chunk = chunk.trim();
				if (chunk.length() > 0) {
					strings.add(chunk);
				}
			}
		} catch (PatternSyntaxException e) {
			throw new RuntimeException(e);
		}
		return strings;
	}
}
