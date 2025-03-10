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
package source.config;

import java.text.DecimalFormat;

import javax.xml.bind.annotation.XmlAttribute;

import source.SourceType;
import source.tuner.TunerChannel;
import source.tuner.TunerChannel.Type;

public class SourceConfigTuner extends SourceConfiguration
{
	private static DecimalFormat sFORMAT = new DecimalFormat( "0.00000" );

	private long mFrequency = 0;
	private int mBandwidth = 15000;
	
	public SourceConfigTuner()
    {
	    super( SourceType.TUNER );
    }
	
	public SourceConfigTuner( TunerChannel tunerChannel )
	{
		this();
		
		mFrequency = tunerChannel.getFrequency();
		mBandwidth = tunerChannel.getBandwidth();
	}
	
	@XmlAttribute( name = "frequency" )
	public long getFrequency()
	{
		return mFrequency;
	}
	
	public void setFrequency( long frequency )
	{
		mFrequency = frequency;
	}

	@Override
    public String getDescription()
    {
	    return sFORMAT.format( (double)mFrequency / 1000000.0d ) + " MHz";
    }
	
	public TunerChannel getTunerChannel()
	{
		return new TunerChannel( Type.LOCKED, mFrequency, mBandwidth );
	}
}
