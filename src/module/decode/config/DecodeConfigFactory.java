/*******************************************************************************
 *     SDR Trunk 
 *     Copyright (C) 2014 Dennis Sheirer
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>
 ******************************************************************************/
package module.decode.config;

import module.decode.DecoderType;
import module.decode.am.DecodeConfigAM;
import module.decode.ltrnet.DecodeConfigLTRNet;
import module.decode.ltrstandard.DecodeConfigLTRStandard;
import module.decode.mpt1327.DecodeConfigMPT1327;
import module.decode.nbfm.DecodeConfigNBFM;
import module.decode.p25.DecodeConfigP25Phase1;
import module.decode.passport.DecodeConfigPassport;

public class DecodeConfigFactory
{
	public static DecodeConfiguration getDefaultDecodeConfiguration()
	{
		return getDecodeConfiguration( DecoderType.NBFM );
	}
	
	public static DecodeConfiguration 
						getDecodeConfiguration( DecoderType decoder )
	{
		DecodeConfiguration retVal;

		switch( decoder )
		{
		    case AM:
		        retVal = new DecodeConfigAM();
		        break;
			case NBFM:
				retVal = new DecodeConfigNBFM();
				break;
			case LTR_STANDARD:
				retVal = new DecodeConfigLTRStandard();
				break;
			case LTR_NET:
				retVal = new DecodeConfigLTRNet();
				break;
			case MPT1327:
				retVal = new DecodeConfigMPT1327();
				break;
			case PASSPORT:
				retVal = new DecodeConfigPassport();
				break;
			case P25_PHASE1:
				retVal = new DecodeConfigP25Phase1();
				break;
			default:
				throw new IllegalArgumentException( "DecodeConfigFactory - "
						+ "unknown decoder type [" + decoder.toString() + "]" );
		}
		
		return retVal;
	}
}
