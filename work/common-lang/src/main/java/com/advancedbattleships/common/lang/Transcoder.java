package com.advancedbattleships.common.lang;

public class Transcoder {

	public static byte[] booleanArrayToByteArray(boolean[] boolArray) {
		if (boolArray == null) {
			return null;
		}

		int nBytes = boolArray.length / 8 + 1;

		byte[] ret = new byte[nBytes];

		int currBoolIdx = 0;
		for (int currByteIdx = 0 ; currByteIdx < nBytes ; currByteIdx++) {
			int currByteValue = 0;
			int pow2 = 1;
			for (int i = 7 ; i >= 0 ; i--) {
				int tempBoolIdx = currBoolIdx + i;
				if (tempBoolIdx < boolArray.length) {
					if (boolArray[tempBoolIdx]) {
						currByteValue += pow2;
					}
				}
				pow2 *= 2;
			}
			ret[currByteIdx] = (byte)currByteValue;

			currBoolIdx += 8;
		}

		return ret;
	}

	public static boolean[] byteArrayToBooleanArray(byte[] byteArray) {
		if (byteArray == null) {
			return null;
		}

		boolean[] ret = new boolean[byteArray.length * 8];

		int currBoolIdx = 0;
		for (int currByteIdx = 0 ; currByteIdx < byteArray.length ; currByteIdx++) {
			int currByte = Byte.toUnsignedInt(byteArray[currByteIdx]);

			int pow2 = (int)Math.pow(2,  7);
			while (pow2 > 0) {
				if (pow2 <= currByte) {
					ret[currBoolIdx] = currByte / pow2 > 0;
					currByte -= pow2;
				} else {
					ret[currBoolIdx] = false;
				}

				pow2 /= 2;
				currBoolIdx++;
			}
		}

		return ret;
	}
}
