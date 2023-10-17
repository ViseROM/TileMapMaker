package manager;

import java.awt.Graphics2D;

import state.*;

/**
 * StateManager class manages the different possible States
 * @author Vachia Thoj
 *
 */
public class StateManager 
{
	//For singleton
	private static StateManager stateManager;
	
	//The current state
	private State currentState;
	
	/**
	 * Constructor (private)
	 */
	private StateManager()
	{
		currentState = new MenuState();
	}
	
	/**
	 * Method to be called to obtain StateManager object (Singleton)
	 * @return StateManager object
	 */
	public static StateManager instance()
	{
		if(stateManager == null)
		{
			stateManager = new StateManager();
		}
		
		return stateManager;
	}
	
	/**
	 * Method that attempts to go to the next state
	 * @param stateType (StateType) The State to go to
	 */
	public void nextState(StateType stateType)
	{
		switch(stateType)
		{
			case MENU_STATE:	//Go to MenuState
				currentState = new MenuState();
				break;
			case PLAY_STATE:	//Go to PlayState
				currentState = new PlayState();
				break;
			case CONTROLS_STATE:	//Go to ControlState
				currentState = new ControlsState();
				break;
			default:
				break;
		}
	}
	
	/**
	 * Method that updates the current state
	 */
	public void update()
	{
		currentState.update();
	}
	
	/**
	 * Method that draws the current state
	 * 
	 * @param g (Graphics2D) The Graphics2D object to be drawn on
	 */
	public void draw(Graphics2D g)
	{
		currentState.draw(g);
	}
}