package module.decode.p25.message.ldu.lc;

import module.decode.p25.message.IAdjacentSite;
import module.decode.p25.message.IBandIdentifier;
import module.decode.p25.message.IdentifierReceiver;
import module.decode.p25.message.ldu.LDU1Message;
import module.decode.p25.reference.LinkControlOpcode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdjacentSiteStatusBroadcastExplicit extends LDU1Message
					implements IdentifierReceiver, IAdjacentSite
{
	private final static Logger mLog = 
			LoggerFactory.getLogger( AdjacentSiteStatusBroadcastExplicit.class );

	public static final int[] LRA = { 364,365,366,367,372,373,374,375 };
	public static final int[] TRANSMIT_IDENTIFIER = { 376,377,382,383 };
	public static final int[] TRANSMIT_CHANNEL = { 384,385,386,387,536,537,538,
		539,540,541,546,547 };
	public static final int[] RFSS_ID = { 548,549,550,551,556,557,558,559 };
	public static final int[] SITE_ID = { 560,561,566,567,568,569,570,571 };
	
	public static final int[] RECEIVE_IDENTIFIER = { 720,721,722,723 };
	public static final int[] RECEIVE_CHANNEL = { 724,725,730,731,732,733,734,
		735,740,741,742,743 };
	
	private IBandIdentifier mTransmitBand;
	private IBandIdentifier mReceiveBand;
	
	public AdjacentSiteStatusBroadcastExplicit( LDU1Message message )
	{
		super( message );
	}
	
    @Override
    public String getEventType()
    {
        return LinkControlOpcode.ADJACENT_SITE_STATUS_BROADCAST_EXPLICIT.getDescription();
    }

	@Override
	public String getMessage()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append( getMessageStub() );
		
		sb.append( " LRA:" + getLRA() );
		
		sb.append( " SITE:" + getRFSS() + "-" + getSiteID() );
		
		sb.append( " DNLINK:" + getDownlinkChannel() );
		
		sb.append( " UPLINK:" + getUplinkChannel() );
		
		return sb.toString();
	}
	
    public String getUniqueID()
    {
    	StringBuilder sb = new StringBuilder();
    	
    	sb.append( getSystemID() );
    	sb.append( ":" );
    	sb.append( getRFSS() );
    	sb.append( ":" );
    	sb.append( getSiteID() );
    	
    	return sb.toString();
    }
    
	public String getLRA()
	{
		return mMessage.getHex( LRA, 2 );
	}
	
	public String getRFSS()
	{
		return mMessage.getHex( RFSS_ID, 2 );
	}
	
	public String getSystemID()
	{
		return "***";
	}
	
	public String getSiteID()
	{
		return mMessage.getHex( SITE_ID, 2 );
	}
	
	public int getTransmitIdentifier()
	{
		return mMessage.getInt( TRANSMIT_IDENTIFIER );
	}
	
	public int getTransmitChannel()
	{
		return mMessage.getInt( TRANSMIT_CHANNEL );
	}
	
	public String getDownlinkChannel()
	{
		return getTransmitIdentifier() + "-" + getTransmitChannel();
	}
	
	public int getReceiveIdentifier()
	{
		return mMessage.getInt( RECEIVE_IDENTIFIER );
	}
	
	public int getReceiveChannel()
	{
		return mMessage.getInt( RECEIVE_CHANNEL );
	}
	
	public String getUplinkChannel()
	{
		return getReceiveIdentifier() + "-" + getReceiveChannel();
	}

	@Override
	public String getSystemServiceClass()
	{
		return "[]";
	}

    public long getDownlinkFrequency()
    {
    	return calculateDownlink( mTransmitBand, getTransmitChannel() );
    }
    
    public long getUplinkFrequency()
    {
    	return calculateUplink( mReceiveBand, getReceiveChannel() );
    }

	@Override
	public void setIdentifierMessage( int identifier, IBandIdentifier message )
	{
		if( identifier == getTransmitIdentifier() )
		{
			mTransmitBand = message;
		}
		
		if( identifier == getReceiveIdentifier() )
		{
			mReceiveBand = message;
		}
	}

	@Override
	public int[] getIdentifiers()
	{
		int[] identifiers = new int[ 2 ];
		
		identifiers[ 0 ] = getTransmitIdentifier();
		identifiers[ 1 ] = getReceiveIdentifier();
		
		return identifiers;
	}
	
}
