import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class HolmesGameAnalysis {

	static int roundCounter=1;

	public static void main(String[] args) throws FileNotFoundException {

		File file = new File("test2.txt");
		Scanner sc = new Scanner(file);

		int size = Integer.parseInt(sc.nextLine());
		System.out.println("Size:"+size);


		ArrayList<Integer> remainingNumbers = new ArrayList<Integer>();
		for (int i=1; i<=size; i++) {
			remainingNumbers.add(i);
		}
		int mistakeCount=0;
		int currentState[]=new int[size];
		for(int i=0; i<size; i++){
			currentState[i]=-1;
		}

		System.out.print("Initial state: [" + currentState[0]);
		for(int i=1; i<size; i++){
			System.out.print(", " + currentState[i]);
		}
		System.out.println("]");

		int place, num;
		String arr[];
		boolean enolaWins = true;
		while(!isGameFinished(currentState) && roundCounter <= size){
			arr=sc.nextLine().split(" ");
			place=Integer.parseInt(arr[0]);
			num=Integer.parseInt(arr[1]);

			boolean enolaWinsPrev = enolaWins;
			enolaWins = enolaWinsOptimally(currentState, remainingNumbers);
			if(enolaWins!=enolaWinsPrev) {
				mistakeCount++;
			}
			System.out.println(enolaWinsOptimally(currentState, remainingNumbers));
			System.out.println(mistakeCount);

			makeMove(currentState, place, num);
			remainingNumbers.remove(new Integer(num));


			System.out.print("After Round " + roundCounter +": [" + currentState[0]);
			for(int i=1; i<size; i++){
				System.out.print(", " + currentState[i]);
			}
			System.out.println("]");

			roundCounter++;
		}

		sc.close();
	}


	public static boolean gameOver(int[] currentState) {
		return isEnolaAlreadyWon(currentState)||isGameFinished(currentState);
	}

	public static boolean isEnolaAlreadyWon(int[] currentState) {
		for(int i=0; i<currentState.length-1; i++){
			if(Math.abs(currentState[i]-currentState[i+1])==1) return true;
		}
		return false;
	}

	public static boolean isGameFinished(int[] currentState) {
		for(int i=0; i<currentState.length; i++){
			if(currentState[i] == -1) return false;
		}
		return true;
	}

	public static boolean enolaWinsOptimally(int[] currentState, ArrayList<Integer> numbers) {
		int[] state = new int[currentState.length];
		System.arraycopy(currentState, 0, state, 0, currentState.length);

		if(gameOver(state)) {
			return isEnolaAlreadyWon(state);
		}

		return simulate(state, numbers, roundCounter%2==1, 0);

	}

	public static boolean simulate(int[] state, ArrayList<Integer> numbers, boolean enolaTurn, int recLevel) {
		if(gameOver(state)) {
			if(isEnolaAlreadyWon(state)) {
				return true;
			}
			else{
				return false;
			}
		}
		if(enolaTurn) {
			for(Integer number : numbers) {
				for(int i=1; i<=state.length; i++) {
					if(state[i-1]==-1) {
						int[] newState = new int[state.length];
						System.arraycopy(state, 0, newState, 0, newState.length);
						makeMove(newState, i, number);
						ArrayList<Integer> newNumbers = new ArrayList<Integer>(numbers);
						newNumbers.remove(new Integer(number));
						if(simulate(newState, newNumbers, !enolaTurn, recLevel+1)) return true;
					}
				}
			}

			return false;

		}else {
			for(Integer number : numbers) {
				for(int i=1; i<=state.length; i++) {
					if(state[i-1]==-1) {
						int[] newState = new int[state.length];
						System.arraycopy(state, 0, newState, 0, newState.length);
						makeMove(newState, i, number);
						ArrayList<Integer> newNumbers = new ArrayList<Integer>(numbers);
						newNumbers.remove(new Integer(number));
						if(!simulate(newState, newNumbers, !enolaTurn, recLevel+1)) return false;
					}
				}
			}
			return true;
		}
	}

	public static void  makeMove(int[] currentState, int place, int num) {
		currentState[place-1]=num;
	}

}
