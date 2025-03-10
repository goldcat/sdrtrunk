package module.decode.p25.message.ldu.lc;

import module.decode.p25.message.IBandIdentifier;
import module.decode.p25.message.ldu.LDU1Message;
import module.decode.p25.reference.LinkControlOpcode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChannelIdentifierUpdateExplicit extends LDU1Message
									 implements IBandIdentifier
{
	private final static Logger mLog = 
			LoggerFactory.getLogger( ChannelIdentifierUpdateExplicit.class );

	public static final int[] IDENTIFIER = { 364,365,366,367 };
	public static final int[] BANDWIDTH = { 372,373,374,375 };
	public static final int[] TRANSMIT_OFFSET = { 376,377,382,383,384,385,386,
		387,536,537,538,539,540,541 };
	public static final int[] CHANNEL_SPACING = { 546,547,548,549,550,551,556,
		557,558,559 };
	public static final int[] BASE_FREQUENCY = { 560,561,566,567,568,569,570,
		571,720,721,722,723,724,725,730,731,732,733,734,735,740,741,742,743,744,
		745,750,751,752,753,754,755 };
	
	public ChannelIdentifierUpdateExplicit( LDU1Message source )
	{
		super( source );
	}
	
    @Override
    public String getEventType()
    {
        return LinkControlOpcode.CHANNEL_IDENTIFIER_UPDATE_EXPLICIT.getDescription();
    }

	@Override
	public String getMessage()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append( getMessageStub() );
		
        sb.append( " IDEN:" + getIdentifier() );

        sb.append( " BASE:" + getBaseFrequency() );
        
        sb.append( " BW:" + getBandwidth() );
        
        sb.append( " SPACING:" + getChannelSpacing() );
        
        sb.append( " OFFSET:" + getTransmitOffset() );
        
		return sb.toString();
	}

	@Override
	public int getIdentifier()
	{
		return mMessage.getInt( IDENTIFIER );
	}

	/**
     * Channel bandwidth in hertz
     */
    @Override
    public int getBandwidth()
    {
    	return mMessage.getInt( BANDWIDTH ) * 125;
    }
	
    @Override
    public long getChannelSpacing()
    {
    	return mMessage.getLong( CHANNEL_SPACING ) * 125l;
    }

    @Override
    public long getBaseFrequency()
    {
        return mMessage.getLong( BASE_FREQUENCY ) * 5l;
    }

	@Override
    public long getTransmitOffset()
    {
		return  -1 * mMessage.getLong( TRANSMIT_OFFSET ) * 250000l;
    }
}
