import java.util.ArrayList;
import java.util.concurrent.Callable;

public class Individual implements Callable<Individual> {
	private NeuralNetwork network; 
	private Game game;
	public Individual(int numInputs) {
		network = new NeuralNetwork(numInputs);
		network.addLayer(40, Activation.ReLu);
		network.addLayer(4, Activation.Sigmoid);
	}
	
	/**
	 * @author Sri Kondapalli
	 * @param NeeralNetwork passed in as a requirement for pairs of individuals to reproduce
	 */
	public Individual(NeuralNetwork n) {
		network = n; 
	}
	
	public double getFitness() {
		return network.getFitness();
		
	}
		
	public void play() {
//		System.out.println("PLAYING");
		double[][] state = game.getState();
		ArrayList<Double> newState = new ArrayList<Double>();

		for(int r = 0; r < state.length; r++) {
			for(int c = 0; c < state[r].length; c++) {
				newState.add(state[r][c]); 
			}
		}

		ArrayList<Integer> actions = network.predict(newState, 0);
			
		if (actions.get(0) == -1) {
			return;
		}
			
		if (actions.size() >= 1 && actions.get(0) >= 1) {
			game.moveRight();
		}
		if(actions.size() >= 2 && actions.get(1) >= 1) {
			game.jump();
		}
		if(actions.size() >= 3 && actions.get(2) >= 1) {
			System.out.println("Moving Left");
			game.moveLeft();
		}
	}
	
	public boolean ifDone = false;
	public void setDone(boolean f) {
		//System.out.println("WHAT?"+GeneticAlgorithm.numDone);
		ifDone = f;
	}
	
	public NeuralNetwork getNN() {
		return network;
		
	}

	@Override
	public String toString() {
		return "Individual<fitness: "+ network.getFitness()+">";
	}

	@Override
	public Individual call() throws Exception {
		game = new Game();
		game.indiv = this;
		game.start();
		while (!game.isDone) {
			play();
		}
		network.setFitness(game.getFitness());
		GeneticAlgorithm.numDone++;
		//System.out.println("NUM DONE"+GeneticAlgorithm.numDone);
		return this;
	}
}
