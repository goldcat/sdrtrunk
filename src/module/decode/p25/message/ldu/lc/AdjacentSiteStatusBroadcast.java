package module.decode.p25.message.ldu.lc;

import module.decode.p25.message.IAdjacentSite;
import module.decode.p25.message.IBandIdentifier;
import module.decode.p25.message.IdentifierReceiver;
import module.decode.p25.message.ldu.LDU1Message;
import module.decode.p25.message.tsbk.osp.control.SystemService;
import module.decode.p25.reference.LinkControlOpcode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdjacentSiteStatusBroadcast extends LDU1Message
					 implements IdentifierReceiver, IAdjacentSite
{
	private final static Logger mLog = 
			LoggerFactory.getLogger( AdjacentSiteStatusBroadcast.class );

	public static final int[] LRA = { 364,365,366,367,372,373,374,375 };
	public static final int[] SYSTEM_ID = { 384,385,386,387,536,537,538,539,540,
		541,546,547 };
	public static final int[] RFSS_ID = { 548,549,550,551,556,557,558,559 };
	public static final int[] SITE_ID = { 560,561,566,567,568,569,570,571 };
	public static final int[] IDENTIFIER = { 720,721,722,723 };
	public static final int[] CHANNEL = { 724,725,730,731,732,733,734,735,740,
		741,742,743 };
	public static final int[] SYSTEM_SERVICE_CLASS = { 744,745,750,751,752,753,
		754,755 };
	
	private IBandIdentifier mIdentifierUpdate;
	
	public AdjacentSiteStatusBroadcast( LDU1Message message )
	{
		super( message );
	}
	
    @Override
    public String getEventType()
    {
        return LinkControlOpcode.ADJACENT_SITE_STATUS_BROADCAST.getDescription();
    }

	@Override
	public String getMessage()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append( getMessageStub() );
		
		sb.append( " LRA:" + getLRA() );

		sb.append( " SYS:" + getSystemID() );
		
		sb.append( " SITE:" + getRFSS() + "-" + getSiteID() );
		
		sb.append( " CHAN:" + getChannel() );

		sb.append( " " + getSystemServiceClass() );
		
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
	
	public String getSystemID()
	{
		return mMessage.getHex( SYSTEM_ID, 3 );
	}
	
	public String getRFSS()
	{
		return mMessage.getHex( RFSS_ID, 2 );
	}
	
	public String getSiteID()
	{
		return mMessage.getHex( SITE_ID, 2 );
	}
	
	public int getIdentifier()
	{
		return mMessage.getInt( IDENTIFIER );
	}
	
	public String getChannel()
	{
		return getIdentifier() + "-" + getChannelNumber();
	}
	
	public int getChannelNumber()
	{
		return mMessage.getInt( CHANNEL );
	}
	
	public String getSystemServiceClass()
	{
		return SystemService.toString( mMessage.getInt( SYSTEM_SERVICE_CLASS ) );
	}

	@Override
    public void setIdentifierMessage( int identifier, IBandIdentifier message )
    {
		mIdentifierUpdate = message;
    }

	@Override
    public int[] getIdentifiers()
    {
	    int[] identifiers = new int[ 1 ];
	    
	    identifiers[ 0 ] = getIdentifier();
	    
	    return identifiers;
    }

	
    @Override
	public String getDownlinkChannel()
	{
		return getChannel();
	}

	@Override
	public String getUplinkChannel()
	{
		return getChannel();
	}

	public long getDownlinkFrequency()
    {
    	return calculateDownlink( mIdentifierUpdate, getChannelNumber() );
    }
    
    public long getUplinkFrequency()
    {
    	return calculateUplink( mIdentifierUpdate, getChannelNumber() );
    }
}
