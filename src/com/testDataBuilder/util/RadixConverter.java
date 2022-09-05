package com.testDataBuilder.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class RadixConverter {
	public static final int DWORD_BYTES = 4;

	int _radix;

	char[] _charMap;

	public RadixConverter(int radix, char[] charMap) throws Exception {
		if (radix < 2 || radix > charMap.length) {
			throw new Exception("Invalid Radix or Charmap");
		}
		_radix = radix;
		_charMap = charMap;
	}

	public RadixConverter(int radix, String strCharMap) throws Exception {
		if (radix < 2 || radix > strCharMap.length()) {
			throw new Exception("Invalid Radix or Charmap");
		}
		_radix = radix;
		_charMap = new char[radix];
		strCharMap.getChars(0, radix, _charMap, 0);
	}

	/**
	 * ����ȡ���뵥Ԫ�ַ�� ���� [in]dwBytesLen:16�������ֽ��� ���أ� ת����������󳤶� 0
	 * ��ʾ�������
	 */
	public int getUnitStringLength() {
		long dwTemp = 0xffffffffl;
		long dwCharsPerUnit = 0;
		while (dwTemp != 0) {
			dwTemp /= _radix;
			dwCharsPerUnit++;
		}
		return (int) dwCharsPerUnit;
	}

	/**
	 * ����ȡ�������ת������Ҫ����󻺳���� ���� [in]dwBytesLen:16�������ֽ���
	 * [in]_radix:�����ַ���� ���أ� ת����������󳤶� 0 ��ʾ�������
	 */
	public int getMaxRadixStringLength(int dwBytesLen) {
		int dwUnits = dwBytesLen / DWORD_BYTES;
		return dwUnits * getUnitStringLength();

	}

	/**
	 * ����DWORD�������ת�� ���� [in]dwValue:��ת����DWORD ���أ�
	 * [out]szEncoded:ת������ַ�
	 */
	public String encodeDWORD(int dwValue) {
		int dwCharsPerUnit = getUnitStringLength();
		char[] szEncoded = new char[dwCharsPerUnit];
		long dwRemain, dwTmp;
		dwTmp = (long) dwValue & 0xffffffffl;
		for (int i = 0; i < szEncoded.length; i++) {
			szEncoded[i] = _charMap[0];
		}
		for (int i = 0; i < dwCharsPerUnit; i++) {
			dwRemain = dwTmp % _radix;
			dwTmp = dwTmp / _radix;
			// dwCharsPerUnit-1-i="+(dwCharsPerUnit-1-i));
			szEncoded[dwCharsPerUnit - 1 - i] = _charMap[(int) dwRemain];
		}
		return new String(szEncoded);
	}

	/**
	 * ����������ת�� ���� [in]hexBuf:��ת�����ֽ�����
	 * [in]dwHexBufLen:��ת�����ֽ����鳤�� ���أ� [out]szEncoded:ת������ַ�
	 */
	public String encodeHexBytes(byte[] hexBuf) {
		if (hexBuf == null) {
			return null;
		}
		int dwHexBufLen = hexBuf.length;
		StringBuffer sbEncoded = new StringBuffer();
		int dwCharsPerUnit = getMaxRadixStringLength(DWORD_BYTES);
		int dwOddBytes = dwHexBufLen % DWORD_BYTES;

		if (dwOddBytes != 0) {
			return null;
		}
		int dwOffset = 0;
		while (dwOffset < dwHexBufLen) {
			long dwUnitValue = 0;
			for (int i = 0; i < DWORD_BYTES; i++) {
				dwUnitValue = dwUnitValue * 0x100l + (hexBuf[dwOffset++] & 0xff);
			}
			sbEncoded.append(encodeDWORD((int) dwUnitValue));
		}
		return sbEncoded.toString();

	}

	/**
	 * ������ұ���ת����λ�� ���� [in]ch:Ҫ���ҵ��ַ� ���أ� -1,û���ҵ�,����
	 * 0��ʼ��λ��ƫ����
	 */
	public int findInCharMap(char ch) {
		for (int i = 0; i < _charMap.length; i++) {
			if (ch == _charMap[i]) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * ����DWORD������ƻָ� ���� [in]szEncoded:��������ַ� ���أ� ת�����ֵ
	 */
	public int decodeDWORD(String szEncoded) throws Exception {
		long dwValue;
		int dwMaxChars = getMaxRadixStringLength(DWORD_BYTES);
		dwValue = 0;
		for (int i = 0; i < dwMaxChars; i++) {
			int pChPos = findInCharMap(szEncoded.charAt(i));
			if (pChPos == -1) {
				throw new Exception("Invalid Charset");
			}
			dwValue = (long) dwValue * (long) _radix + (long) pChPos;
		}
		return (int) dwValue;
	}

	/**
	 * �������кŽ��� ���� [in]szEncoded:�������ַ� ���أ� [out]hexBuf:���뻺����
	 */
	public byte[] decodeHexBytes(String szEncoded) throws Exception {
		byte[] hexBuf;
		if (szEncoded == null) {
			return null;
		}
		int dwEncStrLen = szEncoded.length();
		int dwCharsPerUnit = getUnitStringLength();
		int dwOddBytes = dwEncStrLen % dwCharsPerUnit;

		if (dwOddBytes != 0) {
			return null;
		}
		hexBuf = new byte[(dwEncStrLen / dwCharsPerUnit) * DWORD_BYTES];
		char[] pbUnitBuf = new char[dwCharsPerUnit + 1];
		int dwEncStrOffset = dwOddBytes;
		int dwDecBytesOffset = 0;
		while (dwEncStrOffset < dwEncStrLen) {
			int dwUnitValue = 0;
			dwUnitValue = decodeDWORD(szEncoded.substring(dwEncStrOffset, dwEncStrOffset + dwCharsPerUnit));
			for (int i = 0; i < DWORD_BYTES; i++) {
				hexBuf[dwDecBytesOffset++] = (byte) ((dwUnitValue >> (3 - i) * 8) & 0xFF);
			}
			dwEncStrOffset += dwCharsPerUnit;
		}
		return hexBuf;
	}

	public static byte[] intToByte(int n) {
		byte[] buf = new byte[4];
		for (int i = 0; i < buf.length; i++) {
			if (i == 0) {
				buf[i] = (byte) n;
			} else {
				buf[i] = (byte) (n >> 8 * i);
			}
		}
		return buf;
	}

	public static byte[] longToByte(long l) {
		byte[] buf = new byte[8];
		for (int i = 0; i < buf.length; i++) {
			if (i == 0) {
				buf[i] = (byte) l;
			} else {
				buf[i] = (byte) (l >> 8 * i);
			}
		}
		return buf;
	}

	public static int byteToInt(byte[] buf) {
		return ((buf[3] & 0xff) << 24) + ((buf[2] & 0xff) << 16) + ((buf[1] & 0xff) << 8) + (buf[0] & 0xff);
	}

	public static int byteToLong(byte[] buf) {
		return ((buf[7] & 0xff) << 56) + ((buf[6] & 0xff) << 48) + ((buf[5] & 0xff) << 40) + ((buf[4] & 0xff) << 32)
				+ ((buf[3] & 0xff) << 24) + ((buf[2] & 0xff) << 16) + ((buf[1] & 0xff) << 8) + (buf[0] & 0xff);
	}

	public static Set toHashSet(List list) {
		Set hs = new HashSet();
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				hs.add(list.get(i));
			}
		}
		return hs;
	}

	public static List toList(Set set) {
		List list = new ArrayList();
		if (set != null) {
			Iterator it = set.iterator();
			while (it.hasNext()) {
				list.add(it.next());
			}
		}
		return list;
	}

	public static List toList(Collection coll) {
		List list = new ArrayList();
		
		if (coll != null) {
			Iterator it = coll.iterator();
			while (it.hasNext()) {
				list.add(it.next());
			}
		}
		return list;
	}

	public static Boolean parseBoolean(Boolean value) {
		if (value == null || value.booleanValue() == false)
			return new Boolean(false);
		else
			return new Boolean(true);
	}

	public static long ipStrToLong(String ip) throws Exception {
		String[] num = ip.split("\\.");
		if (num.length != 4) {
			throw new Exception("Wrong number of octets");
		}

		long r = 0;
		for (int i = 0; i < 4; i++) {
			int t = Integer.parseInt(num[i]);
			if ((t < 0) || (t > 255)) {
				throw new Exception("Range exceeded for octet at " + i);
			}
			r <<= 8;
			r |= t;
		}
		return r;
	}

	public static String ipLongToStr(long ip) {

		StringBuffer b = new StringBuffer(15);

		b.append(Integer.toString((int) ((ip >> 24) & 0xff)));
		b.append('.');
		b.append(Integer.toString((int) ((ip >> 16) & 0xff)));
		b.append('.');
		b.append(Integer.toString((int) ((ip >> 8) & 0xff)));
		b.append('.');
		b.append(Integer.toString((int) (ip & 0xff)));
		return b.toString();
	}

	public static String bytesToHexString(byte[] b) throws Exception {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			int tmp = b[i] & 0xff;
			String sTemp = Integer.toHexString(tmp);
			if (tmp < 0x10) {
				sTemp = "0" + sTemp;
			}
			sb.append(sTemp);
		}
		return sb.toString();
	}

	public static byte[] hexStringToBytes(String s) throws Exception {
		byte result[] = new byte[s.length() / 2];
		for (int i = 0; i < s.length() / 2; i++) {
			result[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2), 16);
		}
		return result;
	}

}