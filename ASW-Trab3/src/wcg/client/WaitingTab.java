/**"
 * 
 */
package wcg.client;



import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

import wcg.shared.GameInfo;

/**
 *
 */
public class WaitingTab extends SubPanel {
	
	private Widget waitingTab = new HTML("Waiting for Game to Start");
	
	private String gameID;
	
	private boolean repeat = true;
	
	private static final int TIMER_DELAY = 10 * 1000; //10 seconds

	public WaitingTab(String gameID) {
		super(username, password);
		this.gameID = gameID;
		
		Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {

			@Override
			public boolean execute() {
				Logger logger = Logger.getLogger("nameOfLogger");
				logger.log(Level.SEVERE, "executing waitingTabScheduler");
				if(repeat) verifyStartGame();
				return repeat;
			}
			
		}
		, TIMER_DELAY);
	}
	
	public Widget getWaitingTab() {
		return waitingTab;
	}
	
	private void verifyStartGame() {
		cardGameService.getAvailableGameInfos(new AsyncCallback<List<GameInfo>>() {
			@Override
			public void onFailure(Throwable caught) {
				messages.setHTML(caught.getMessage());
			}

			@Override
			public void onSuccess(List<GameInfo> availableGameInfos) {
				for(GameInfo gameInfo : availableGameInfos) {
					String currentGameName = gameInfo.getGameName();
					int currentPlayersCount = gameInfo.getPlayersCount();
					String currentGameID = gameInfo.getGameId();
					
					if(currentGameID.equals(gameID)) {
						if(currentPlayersCount == ("WAR".equals(currentGameName) ? 2 : 4)) {
							repeat = false;
							tabPanel.remove(2);
							if("WAR".equals(currentGameName)) {
								tabPanel.add(new GamePlayWAR(gameID).getGamePlay(), gameID);
							}
							if("HEARTS".equals(currentGameName)) {
								tabPanel.add(new GamePlayHEARTS(gameID).getGamePlay(), gameID);
							}
							tabPanel.selectTab(2);
						}
					}
				}
			}});
	}
}
