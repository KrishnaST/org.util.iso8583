package org.util.iso8583.format;

import static org.util.iso8583.api.Encoding.BCDL;
import static org.util.iso8583.api.Encoding.BIN;
import static org.util.iso8583.api.Encoding.CHAR;
import static org.util.iso8583.api.Encoding.LLBCDL;
import static org.util.iso8583.api.Encoding.LLCHAR;
import static org.util.iso8583.api.Encoding.LLLBIN;
import static org.util.iso8583.api.Encoding.LLLCHAR;

import org.util.iso8583.api.ISOFormat;
import org.util.iso8583.api.LengthEncoding;


public final class POSFormat extends ISOFormat {

	private static final POSFormat POS_FORMAT = new POSFormat("POS");
	
	public static final ISOFormat getInstance() {
		return POS_FORMAT;
	}
	
	public static final ISOFormat provider() {
		return POS_FORMAT;
	}
	
	private POSFormat(final String name) {
		super(name);

		this.messageLengthLength = 2;
		
		lencoder[MESSAGE_LENGTH_ENCODER_INDEX] = LengthEncoding.BIN.encoder;
		lencoder[CHAR_LENGTH_ENCODER_INDEX]    = LengthEncoding.BCD.encoder;
		lencoder[BCD_LENGTH_ENCODER_INDEX]     = LengthEncoding.BCD.encoder;
		lencoder[BIN_LENGTH_ENCODER_INDEX]     = LengthEncoding.BIN.encoder;

		length[NET_HEADER_INDEX]   = 0;
		encoder[NET_HEADER_INDEX] = BCDL.encoder;
		
		length[0]   = 4;
		encoder[0] = BCDL.encoder;

		length[1]   = 8;
		encoder[1] = BIN.encoder;

		length[2]   = 19;
		encoder[2] = LLBCDL.encoder;

		length[3]   = 6;
		encoder[3] = BCDL.encoder;

		length[4]   = 12;
		encoder[4] = BCDL.encoder;

		length[5]   = 12;
		encoder[5] = BCDL.encoder;

		length[6]   = 12;
		encoder[6] = BCDL.encoder;

		length[7]   = 10;
		encoder[7] = BCDL.encoder;

		length[8]   = 8;
		encoder[8] = BCDL.encoder;

		length[9]   = 8;
		encoder[9] = BCDL.encoder;

		length[10]   = 8;
		encoder[10] = BCDL.encoder;

		length[11]   = 6;
		encoder[11] = BCDL.encoder;

		length[12]   = 6;
		encoder[12] = BCDL.encoder;

		length[13]   = 4;
		encoder[13] = BCDL.encoder;

		length[14]   = 4;
		encoder[14] = BCDL.encoder;

		length[15]   = 4;
		encoder[15] = BCDL.encoder;

		length[16]   = 4;
		encoder[16] = BCDL.encoder;

		length[17]   = 4;
		encoder[17] = BCDL.encoder;

		length[18]   = 4;
		encoder[18] = BCDL.encoder;

		length[19]   = 3;
		encoder[19] = BCDL.encoder;

		length[20]   = 3;
		encoder[20] = BCDL.encoder;

		length[21]   = 3;
		encoder[21] = BCDL.encoder;

		length[22]   = 3;
		encoder[22] = BCDL.encoder;

		length[23]   = 3;
		encoder[23] = BCDL.encoder;

		length[24]   = 3;
		encoder[24] = BCDL.encoder;

		length[25]   = 2;
		encoder[25] = BCDL.encoder;

		length[26]   = 2;
		encoder[26] = BCDL.encoder;

		length[27]   = 1;
		encoder[27] = BCDL.encoder;

		length[28]   = 8;
		encoder[28] = BCDL.encoder;

		length[29]   = 8;
		encoder[29] = BCDL.encoder;

		length[30]   = 8;
		encoder[30] = BCDL.encoder;

		length[31]   = 8;
		encoder[31] = BCDL.encoder;

		length[32]   = 11;
		encoder[32] = LLBCDL.encoder;

		length[33]   = 11;
		encoder[33] = LLBCDL.encoder;

		length[34]   = 28;
		encoder[34] = LLBCDL.encoder;

		length[35]   = 37;
		encoder[35] = LLBCDL.encoder;

		length[36]   = 104;
		encoder[36] = LLBCDL.encoder;

		length[37]   = 12;
		encoder[37] = CHAR.encoder;

		length[38]   = 6;
		encoder[38] = CHAR.encoder;

		length[39]   = 2;
		encoder[39] = CHAR.encoder;

		length[40]   = 3;
		encoder[40] = CHAR.encoder;

		length[41]   = 8;
		encoder[41] = CHAR.encoder;

		length[42]   = 15;
		encoder[42] = CHAR.encoder;

		length[43]   = 40;
		encoder[43] = CHAR.encoder;

		length[44]   = 25;
		encoder[44] = LLCHAR.encoder;

		length[45]   = 76;
		encoder[45] = LLCHAR.encoder;

		length[46]   = 999;
		encoder[46] = LLLCHAR.encoder;

		length[47]   = 999;
		encoder[47] = LLLCHAR.encoder;

		length[48]   = 999;
		encoder[48] = LLLCHAR.encoder;

		length[49]   = 3;
		encoder[49] = BCDL.encoder;

		length[50]   = 3;
		encoder[50] = BCDL.encoder;

		length[51]   = 3;
		encoder[51] = BCDL.encoder;

		length[52]   = 16;
		encoder[52] = BCDL.encoder;

		length[53]   = 16;
		encoder[53] = BCDL.encoder;

		length[54]   = 120;
		encoder[54] = LLLCHAR.encoder;

		length[55]   = 255;
		encoder[55] = LLLBIN.encoder;

		length[56]   = 999;
		encoder[56] = LLLCHAR.encoder;

		length[57]   = 999;
		encoder[57] = LLLCHAR.encoder;

		length[58]   = 999;
		encoder[58] = LLLCHAR.encoder;

		length[59]   = 999;
		encoder[59] = LLLCHAR.encoder;

		length[60]   = 7;
		encoder[60] = LLCHAR.encoder;

		length[61]   = 999;
		encoder[61] = LLLCHAR.encoder;

		length[62]   = 999;
		encoder[62] = LLLCHAR.encoder;

		length[63]   = 999;
		encoder[63] = LLLCHAR.encoder;

		length[64]   = 8;
		encoder[64] = BIN.encoder;

		length[65]   = 1;
		encoder[65] = BIN.encoder;

		length[66]   = 1;
		encoder[66] = BCDL.encoder;

		length[67]   = 2;
		encoder[67] = BCDL.encoder;

		length[68]   = 3;
		encoder[68] = BCDL.encoder;

		length[69]   = 3;
		encoder[69] = BCDL.encoder;

		length[70]   = 3;
		encoder[70] = BCDL.encoder;

		length[71]   = 4;
		encoder[71] = BCDL.encoder;

		length[72]   = 4;
		encoder[72] = BCDL.encoder;

		length[73]   = 6;
		encoder[73] = BCDL.encoder;

		length[74]   = 10;
		encoder[74] = BCDL.encoder;

		length[75]   = 10;
		encoder[75] = BCDL.encoder;

		length[76]   = 10;
		encoder[76] = BCDL.encoder;

		length[77]   = 10;
		encoder[77] = BCDL.encoder;

		length[78]   = 10;
		encoder[78] = BCDL.encoder;

		length[79]   = 10;
		encoder[79] = BCDL.encoder;

		length[80]   = 10;
		encoder[80] = BCDL.encoder;

		length[81]   = 10;
		encoder[81] = BCDL.encoder;

		length[82]   = 12;
		encoder[82] = BCDL.encoder;

		length[83]   = 12;
		encoder[83] = BCDL.encoder;

		length[84]   = 12;
		encoder[84] = BCDL.encoder;

		length[85]   = 12;
		encoder[85] = BCDL.encoder;

		length[86]   = 16;
		encoder[86] = BCDL.encoder;

		length[87]   = 16;
		encoder[87] = BCDL.encoder;

		length[88]   = 16;
		encoder[88] = BCDL.encoder;

		length[89]   = 16;
		encoder[89] = BCDL.encoder;

		length[90]   = 42;
		encoder[90] = BCDL.encoder;

		length[91]   = 1;
		encoder[91] = CHAR.encoder;

		length[92]   = 2;
		encoder[92] = CHAR.encoder;

		length[93]   = 5;
		encoder[93] = CHAR.encoder;

		length[94]   = 7;
		encoder[94] = CHAR.encoder;

		length[95]   = 42;
		encoder[95] = CHAR.encoder;

		length[96]   = 8;
		encoder[96] = BIN.encoder;

		length[97]   = 16;
		encoder[97] = BCDL.encoder;

		length[98]   = 25;
		encoder[98] = CHAR.encoder;

		length[99]   = 11;
		encoder[99] = LLBCDL.encoder;

		length[100]   = 11;
		encoder[100] = LLBCDL.encoder;

		length[101]   = 17;
		encoder[101] = LLCHAR.encoder;

		length[102]   = 19;
		encoder[102] = LLCHAR.encoder;

		length[103]   = 19;
		encoder[103] = LLCHAR.encoder;

		length[104]   = 999;
		encoder[104] = LLLCHAR.encoder;

		length[105]   = 999;
		encoder[105] = LLLCHAR.encoder;

		length[106]   = 999;
		encoder[106] = LLLCHAR.encoder;

		length[107]   = 999;
		encoder[107] = LLLCHAR.encoder;

		length[108]   = 999;
		encoder[108] = LLLCHAR.encoder;

		length[109]   = 999;
		encoder[109] = LLLCHAR.encoder;

		length[110]   = 999;
		encoder[110] = LLLCHAR.encoder;

		length[111]   = 999;
		encoder[111] = LLLCHAR.encoder;

		length[112]   = 999;
		encoder[112] = LLLCHAR.encoder;

		length[113]   = 999;
		encoder[113] = LLLCHAR.encoder;

		length[114]   = 999;
		encoder[114] = LLLCHAR.encoder;

		length[115]   = 999;
		encoder[115] = LLLCHAR.encoder;

		length[116]   = 999;
		encoder[116] = LLLCHAR.encoder;

		length[117]   = 999;
		encoder[117] = LLLCHAR.encoder;

		length[118]   = 999;
		encoder[118] = LLLCHAR.encoder;

		length[119]   = 999;
		encoder[119] = LLLCHAR.encoder;

		length[120]   = 999;
		encoder[120] = LLLCHAR.encoder;

		length[121]   = 999;
		encoder[121] = LLLCHAR.encoder;

		length[122]   = 999;
		encoder[122] = LLLCHAR.encoder;

		length[123]   = 999;
		encoder[123] = LLLCHAR.encoder;

		length[124]   = 999;
		encoder[124] = LLLCHAR.encoder;

		length[125]   = 999;
		encoder[125] = LLLCHAR.encoder;

		length[126]   = 999;
		encoder[126] = LLLCHAR.encoder;

		length[127]   = 999;
		encoder[127] = LLLCHAR.encoder;

		length[128]   = 8;
		encoder[128] = BIN.encoder;

	}

}
