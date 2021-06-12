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
			ret[currByteIdx] = (byte)(
				(boolArray[currBoolIdx + 0]?1<<7:0) +
				(((currBoolIdx + 1) >= boolArray.length) ? 0 : (boolArray[currBoolIdx + 1]?1<<6:0)) +
				(((currBoolIdx + 2) >= boolArray.length) ? 0 : (boolArray[currBoolIdx + 2]?1<<5:0)) +
				(((currBoolIdx + 3) >= boolArray.length) ? 0 : (boolArray[currBoolIdx + 3]?1<<4:0)) +
				(((currBoolIdx + 4) >= boolArray.length) ? 0 : (boolArray[currBoolIdx + 4]?1<<3:0)) +
				(((currBoolIdx + 5) >= boolArray.length) ? 0 : (boolArray[currBoolIdx + 5]?1<<2:0)) +
				(((currBoolIdx + 6) >= boolArray.length) ? 0 : (boolArray[currBoolIdx + 6]?1<<1:0)) +
				(((currBoolIdx + 7) >= boolArray.length) ? 0 : (boolArray[currBoolIdx + 7]?1:0)) 
			);
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
			byte currByte = byteArray[currByteIdx];

			ret[currBoolIdx + 0] = ((currByte &  0x01) != 0);
			ret[currBoolIdx + 1] = ((currByte &  0x02) != 0);
			ret[currBoolIdx + 2] = ((currByte &  0x04) != 0);
			ret[currBoolIdx + 3] = ((currByte &  0x08) != 0);
			ret[currBoolIdx + 4] = ((currByte &  0x16) != 0);
			ret[currBoolIdx + 5] = ((currByte &  0x32) != 0);
			ret[currBoolIdx + 6] = ((currByte &  0x64) != 0);
			ret[currBoolIdx + 7] = ((currByte & 0x128) != 0);

			currBoolIdx += 8;
		}

		return ret;
	}
}
