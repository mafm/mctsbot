package mctsbot;

import mctsbot.opponentmodel.HandRankOpponentModel;
import mctsbot.opponentmodel.NextActionOpponentModel;
import mctsbot.opponentmodel.SimpleWekaHandRankOpponentModel;
import mctsbot.strategies.actionselection.ActionSelectionStrategy;
import mctsbot.strategies.actionselection.HighestEVActionSelectionStrategy;
import mctsbot.strategies.backpropagation.AveragingBackpropagationStrategy;
import mctsbot.strategies.backpropagation.BackpropagationStrategy;
import mctsbot.strategies.selection.SelectionStrategy;
import mctsbot.strategies.selection.UCTSelectionStrategy;
import mctsbot.strategies.simulation.AveragingSimulationStrategy;
import mctsbot.strategies.simulation.SimulationStrategy;
import mctsbot.strategies.simulation.StaticDistributionSimulationStrategy;

import com.biotools.meerkat.util.Preferences;

public class Config {
	
	private final ActionSelectionStrategy actionSelectionStrategy;
	private final SelectionStrategy selectionStrategy;
	private final SimulationStrategy simulationStrategy;
	private final BackpropagationStrategy backpropagationStrategy;
	private final HandRankOpponentModel handRankOpponentModel;
	private final NextActionOpponentModel nextActionOpponentModel;
	
	public Config(
			ActionSelectionStrategy actionSelectionStrategy, 
			SelectionStrategy selectionStrategy, 
			SimulationStrategy simulationStrategy, 
			BackpropagationStrategy backpropagationStrategy, 
			HandRankOpponentModel handRankOpponentModel, 
			NextActionOpponentModel nextActionOpponentModel) {
		
		this.actionSelectionStrategy = actionSelectionStrategy;
		this.selectionStrategy = selectionStrategy;
		this.simulationStrategy = simulationStrategy;
		this.backpropagationStrategy = backpropagationStrategy;
		this.handRankOpponentModel = handRankOpponentModel;
		this.nextActionOpponentModel = nextActionOpponentModel;
	}
	
	public Config(Preferences prefs) {
		
		//TODO: Set from prefs here.
		
		
		
		// ActionSelectionStrategy.
		this.actionSelectionStrategy = new HighestEVActionSelectionStrategy();
		
		// SelectionStrategy.
		this.selectionStrategy = new UCTSelectionStrategy();
		
		// SimulationStrategy.
		this.simulationStrategy = new AveragingSimulationStrategy(
				new StaticDistributionSimulationStrategy(), 20);
		
		// BackpropagationStrategy.
		this.backpropagationStrategy = new AveragingBackpropagationStrategy();
		
		// HandRankOpponentModel.
		this.handRankOpponentModel = new SimpleWekaHandRankOpponentModel();
		
		// NextActionOpponentModel.
		this.nextActionOpponentModel = null;
		
	}

	public ActionSelectionStrategy getActionSelectionStrategy() {
		return actionSelectionStrategy;
	}

	public SelectionStrategy getSelectionStrategy() {
		return selectionStrategy;
	}

	public SimulationStrategy getSimulationStrategy() {
		return simulationStrategy;
	}

	public BackpropagationStrategy getBackpropagationStrategy() {
		return backpropagationStrategy;
	}
	
	public HandRankOpponentModel getHandRankOpponentModel() {
		return handRankOpponentModel;
	}
	
	public NextActionOpponentModel getNextActionOpponentModel() {
		return nextActionOpponentModel;
	}
	
	
}
