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
package controller;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import map.MapPanel;
import map.MapService;
import module.decode.event.CallEventPanel;
import module.decode.event.MessageActivityPanel;
import module.decode.state.ChannelList;
import net.miginfocom.swing.MigLayout;
import playlist.PlaylistManager;
import settings.SettingsManager;
import spectrum.ChannelSpectrumPanel;
import audio.AudioManager;
import audio.AudioPanel;

import com.jidesoft.swing.JideSplitPane;
import com.jidesoft.swing.JideTabbedPane;

import controller.channel.ChannelManager;

public class ControllerPanel extends JPanel
{
    private static final long serialVersionUID = 1L;

    private ChannelList mChannelStateList;

    private CallEventPanel mCallEventPanel;
    
    private MessageActivityPanel mMessageActivityPanel = 
    		new MessageActivityPanel();
    
    private ChannelSpectrumPanel mChannelSpectrumPanel;
    
    private JideTabbedPane mTabbedPane;

    protected ConfigurationTreePanel mSystemControlViewPanel;
	protected ConfigurationEditor mConfigurationEditor;
    protected JideSplitPane mSystemControlSplitPane;

	protected JTable mChannelActivityTable = new JTable();
	private AudioPanel mAudioPanel;
	private MapPanel mMapPanel;

	private ChannelManager mChannelManager;
	private ConfigurationControllerModel mController;
	protected SettingsManager mSettingsManager;

	public ControllerPanel( AudioManager audioManager,
							ConfigurationControllerModel controller,
							ChannelManager channelManager,
							MapService mapService,
							PlaylistManager playlistManager,
							SettingsManager settingsManager )
	{
		mChannelManager = channelManager;
		mController = controller;
	    mSettingsManager = settingsManager;

    	mAudioPanel = new AudioPanel( mSettingsManager, audioManager );

    	mMapPanel = new MapPanel( mapService, mSettingsManager, mChannelManager );
	    
	    mCallEventPanel = new CallEventPanel( mSettingsManager );	
	    
    	mChannelStateList = new ChannelList( playlistManager, mSettingsManager );
	    
		init();
	}
	
	private void init()
	{
    	setLayout( new MigLayout( "insets 0 0 0 0 ", 
    							  "[grow,fill]", 
    							  "[grow,fill]") );
    	
    	//System Configuration View and Editor
    	mConfigurationEditor = new ConfigurationEditor();

    	mSystemControlViewPanel = new ConfigurationTreePanel( mController );
    	mSystemControlViewPanel.addTreeSelectionListener( mConfigurationEditor );

    	mSystemControlSplitPane = new JideSplitPane( JideSplitPane.HORIZONTAL_SPLIT );
    	mSystemControlSplitPane.setDividerSize( 5 );
    	mSystemControlSplitPane.add( mSystemControlViewPanel );
    	mSystemControlSplitPane.add( mConfigurationEditor );
    	
    	mChannelSpectrumPanel = new ChannelSpectrumPanel( mSettingsManager );
    	
    	//Tabbed View - configuration, calls, messages, map
    	mTabbedPane = new JideTabbedPane();
    	mTabbedPane.setFont( this.getFont() );
    	mTabbedPane.setForeground( Color.BLACK );
    	mTabbedPane.addTab( "Configuration", mSystemControlSplitPane  );
    	mTabbedPane.addTab( "Channel Spectrum", mChannelSpectrumPanel );
    	mTabbedPane.addTab( "Events", mCallEventPanel );
    	mTabbedPane.addTab( "Messages", mMessageActivityPanel );

    	/**
    	 * Change listener to enable/disable the channel spectrum display
    	 * only when the tab is visible, and a channel has been selected
    	 */
    	mTabbedPane.addChangeListener( new ChangeListener()
		{
			@Override
			public void stateChanged( ChangeEvent event )
			{
				int index = mTabbedPane.getSelectedIndex();
				
				Component component = mTabbedPane.getComponentAt( index );
				
				if( component instanceof ChannelSpectrumPanel )
				{
					mChannelSpectrumPanel.setEnabled( true );
				}
				else
				{
					mChannelSpectrumPanel.setEnabled( false );
				}
			}
		} );
    	
    	/**
    	 * Add mapping services and map panel to a new tab
    	 */
    	mTabbedPane.addTab( "Map", mMapPanel );
    	
    	/* Register each of the components to receive channel events when the
    	 * channels are selected or change */
    	mChannelManager.addListener( mCallEventPanel );
    	mChannelManager.addListener( mChannelStateList );
    	mChannelManager.addListener( mChannelSpectrumPanel );
    	mChannelManager.addListener( mMessageActivityPanel );
		
		JScrollPane channelStateListScroll = new JScrollPane();
    	channelStateListScroll.getViewport().setView( mChannelStateList );
    	channelStateListScroll.setPreferredSize( new Dimension( 200, 300 ) ); 


    	JideSplitPane audioChannelListSplit = new JideSplitPane( JideSplitPane.VERTICAL_SPLIT );
    	audioChannelListSplit.setDividerSize( 5 );
    	audioChannelListSplit.add( mAudioPanel );
    	audioChannelListSplit.add( channelStateListScroll );
    	
    	JideSplitPane channelSplit = new JideSplitPane( JideSplitPane.HORIZONTAL_SPLIT );
    	channelSplit.setDividerSize( 5 );
    	channelSplit.add( audioChannelListSplit );
    	channelSplit.add( mTabbedPane );
    	
    	add( channelSplit );
	}

	public ConfigurationControllerModel getController()
	{
		return mController;
	}
}
