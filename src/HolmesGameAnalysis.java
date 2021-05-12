import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class HolmesGameAnalysis {
	
	static int roundCounter=1;
	
	public static void main(String[] args) throws FileNotFoundException {

		File file = new File("test1.txt");
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
		while(!gameOver(currentState) && roundCounter <= size){
			arr=sc.nextLine().split(" ");
			place=Integer.parseInt(arr[0]);
			num=Integer.parseInt(arr[1]);
			
			boolean enolaWinsPrev = enolaWins;
			makeMove(currentState, place, num);
			remainingNumbers.remove(new Integer(num));
			enolaWins = enolaWinsOptimally(currentState, remainingNumbers);
			if(enolaWins!=enolaWinsPrev) mistakeCount++;

			System.out.print("After Round " + roundCounter +": [" + currentState[0]);
			for(int i=1; i<size; i++){
				System.out.print(", " + currentState[i]);
			}
			System.out.println("]");
			
			roundCounter++;
			System.out.println(mistakeCount);
		}
		
		sc.close();
	}


	public static boolean gameOver(int[] currentState) {
		for(int i=0; i<currentState.length-2; i++){

			if(currentState[i]==currentState[i+1]+1 || currentState[i]==currentState[i+1]-1) {
				return true;
			}
		}
		boolean fulled=true;

		for(int i=0; i<currentState.length-1; i++){
			if(currentState[i] == -1) {
				fulled=false;
			}
		}
		return fulled;
	}

	public static boolean enolaWinsOptimally(int[] currentState, ArrayList<Integer> numbers) {
		int[] state = new int[currentState.length];
		System.arraycopy(currentState, 0, state, 0, currentState.length);
		
		if(gameOver(state)) {
			for(int i=0; i<currentState.length-2; i++){
				if(currentState[i]==currentState[i+1]+1 || currentState[i]==currentState[i+1]-1) {
					return true;
				}
			}
		}
		
//		if (roundCounter % 2 == 0) {
			return simulate(state, numbers);
//		}
		
//		return false;
	}
	
	public static boolean simulate(int[] state, ArrayList<Integer> numbers) {
		if(gameOver(state)) {
			for(int i=0; i<state.length-2; i++){
				if(state[i]==state[i+1]+1 || state[i]==state[i+1]-1) {
					System.out.println("enolawins");
					return true;
				}
			}
			System.out.println("sherlockwins");
			return false;
		}
		
		for(Integer number : numbers) {
			for(int i=1; i<=state.length; i++) {
				if(state[i-1]==0) {
					makeMove(state, i, number);
					ArrayList<Integer> newNumbers = new ArrayList<Integer>(numbers);
					newNumbers.remove(new Integer(number));
					//System.out.println("alooo");
					if(simulate(state, newNumbers)) return true;
					state[i-1]=0;
				}
			}
		}

		return false;
	}

	public static void  makeMove(int[] currentState, int place, int num) {
		currentState[place-1]=num;
	}

}
