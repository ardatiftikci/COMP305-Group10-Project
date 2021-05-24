import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class HolmesGameAnalysisv2 {

	static int roundCounter=1;
	static HashMap<String, Boolean> hashMap = new HashMap<String, Boolean>();

	public static void main(String[] args) throws FileNotFoundException {

		File file = new File("test3.txt");
		Scanner sc = new Scanner(file);

		int size = Integer.parseInt(sc.nextLine());
		System.out.println("Size:"+size);
		
		int roundNumber = Integer.parseInt(sc.nextLine());
		System.out.println("Round Number:"+roundNumber);
		

		ArrayList<Integer> remainingNumbers = new ArrayList<Integer>();
		for (int i=1; i<=size; i++) {
			remainingNumbers.add(i);
		}
		int mistakeCount=0;
		int currentState[]=new int[size];
		
		//Initialize board with all -1. (-1 means empty.)
		for(int i=0; i<size; i++){
			currentState[i]=-1;
		}

		

		int place, num;
		String arr[];
		boolean enolaWins = true;
		boolean enolaWinsPrev;
		while(roundCounter <= roundNumber){
			
				arr=sc.nextLine().split(" ");
				place=Integer.parseInt(arr[0]);
				num=Integer.parseInt(arr[1]);
				enolaWinsPrev = enolaWins;
				if(roundCounter%2==1 && enolaWinsPrev == true) {
					enolaWins = true;
				} else if (roundCounter%2==0 && enolaWinsPrev == false) {
					enolaWins = false;
				} else {
					enolaWins = enolaWinsOptimally(currentState, remainingNumbers, roundCounter%2==1);
				}
				
				if(enolaWins!=enolaWinsPrev) {
					mistakeCount++;
					System.out.println(enolaWins);
					System.out.println(mistakeCount);
				}
				makeMove(currentState, place, num);
				remainingNumbers.remove(new Integer(num));

				if(roundCounter%100==0) System.out.println("Round: "+ roundCounter);
				/*System.out.print("After Round " + roundCounter +": [" + currentState[0]);
				for(int i=1; i<size; i++){
					System.out.print(", " + currentState[i]);
				}
				System.out.println("]");*/

				roundCounter++;
	
		}

		System.out.println("Total Mistake: " +mistakeCount);
		sc.close();
	}



	public static boolean isEnolaAlreadyWon(int[] currentState) {
		for(int i=0; i<currentState.length-1; i++){
			if(Math.abs(currentState[i]-currentState[i+1])==1) return true;
		}
		return false;
	}

	

	public static boolean enolaWinsOptimally(int[] state, ArrayList<Integer> numbers, boolean enolaTurn) {
		
		if(roundCounter<state.length/3) return true;
		if(numbers.size()==0) return isEnolaAlreadyWon(state);
		if(isEnolaAlreadyWon(state)) return true;
		
		
		if(enolaTurn) {
			
			if(enolaCanWinImmediately(state, numbers)) return true;
			if(enolaCanWinIn2Moves(state, numbers)) return true;
			if(enolaCantWin(state, numbers)) return false; //use it when board size is even
			if(hashMap.containsKey(arrayToString(state, numbers))) return hashMap.get(arrayToString(state, numbers));
			for(Integer number : numbers) {
				for(int i=1; i<=state.length; i++) {
					if(state[i-1]==-1) {
						int[] newState = new int[state.length];
						System.arraycopy(state, 0, newState, 0, newState.length);
						makeMove(newState, i, number);
						ArrayList<Integer> newNumbers = new ArrayList<Integer>(numbers);
						newNumbers.remove(new Integer(number));
						if(enolaWinsOptimally(newState, newNumbers, !enolaTurn)) {
							hashMap.put(arrayToString(state, numbers), true);
							return true;
						}
					}
				}
			}
			hashMap.put(arrayToString(state, numbers), false);
			return false;

		}else {
			if(enolaCanWinImmediately2(state, numbers)) return true;
			boolean flag = false;
			for(Integer number : numbers) {
				for(int i=1; i<=state.length; i++) {
					if((i-2>=0&&state[i-2]!=-1)&&state[i-1]==-1&&(numbers.contains(state[i-2]+1)||numbers.contains(state[i-2]-1))||(i<=state.length-1&&state[i]!=-1)&&state[i-1]==-1&&(numbers.contains(state[i]+1)||numbers.contains(state[i]-1))) {
						flag=true;
						int[] newState = new int[state.length];
						System.arraycopy(state, 0, newState, 0, newState.length);
						makeMove(newState, i, number);
						ArrayList<Integer> newNumbers = new ArrayList<Integer>(numbers);
						newNumbers.remove(new Integer(number));
						if(!enolaWinsOptimally(newState, newNumbers, !enolaTurn)) {
							return false;
						}
					}
				}
			}
			if(!flag) return false;
	
			return true;
		}
	}

	

	private static boolean enolaCantWin(int[] state, ArrayList<Integer> numbers) {
		for(int i=1; i<=state.length; i++) {
			if((i-2>=0&&state[i-2]!=-1)&&state[i-1]==-1&&(numbers.contains(state[i-2]+1)||numbers.contains(state[i-2]-1))||(i<=state.length-1&&state[i]!=-1)&&state[i-1]==-1&&(numbers.contains(state[i]+1)||numbers.contains(state[i]-1))) {
				return false;
			}
		}

		if(numbers.size()==2){
			if(Math.abs(numbers.get(0)-numbers.get(1))==1){
				for(int i=1; i<=state.length; i++) {

					if(state[i-1]==-1&&(i!=state.length&&state[i]==-1)){
						return false;
					}   
				}

			}
		}
		return true;
	}


	private static boolean enolaCanWinIn2Moves(int[] state, ArrayList<Integer> numbers) {
		boolean consecutive3Numbers = false;
		for(int i=0; i<state.length-2 ; i++) {
			if(numbers.contains(i)&&numbers.contains(i+1)&&numbers.contains(i+2)) {
				consecutive3Numbers= true;
				break;
			}
		}
		if(!consecutive3Numbers) return false;

		for(int i=0; i<state.length-2 ;i++) {
			if(state[i]==-1&&state[i+1]==-1&&state[i+2]==-1) return true;
		}
		return false;
	}


	private static boolean enolaCanWinImmediately(int[] state, ArrayList<Integer> numbers) {
		if(state[0]!=-1&&state[1]==-1&&(numbers.contains(new Integer(state[0]+1)) || numbers.contains(new Integer(state[0]-1)))) {
			return true;
		}

		for(int i=1; i<state.length-1;i++) {
			if(state[i]!=-1&&(state[i-1]==-1||state[i+1]==-1)&&(numbers.contains(new Integer(state[i]-1))||numbers.contains(new Integer(state[i]+1)))) {
				return true;
			}
		}
		if(state[state.length-1]!=-1&&state[state.length-2]==-1&&(numbers.contains(new Integer(state[state.length-1]+1)) || numbers.contains(new Integer(state[state.length-1]-1)))) {
			return true;
		}
		return false;
	}


	public static void  makeMove(int[] currentState, int place, int num) {
		currentState[place-1]=num;
	}

	public static String arrayToString(int[] state, ArrayList<Integer> numbers) {
		ArrayList<Integer> adjacentCells = new ArrayList<>();
		int cellCounter=0;
		for(int i=0; i<state.length;i++) {
			if(state[i]==-1) cellCounter++;
			else {
				if(cellCounter!=0) adjacentCells.add(cellCounter);
				cellCounter=0;
			}
		}
		if(cellCounter!=0) adjacentCells.add(cellCounter);
		ArrayList<Integer> consecutiveNumbers = new ArrayList<>();
		ArrayList<Integer> consecutiveNumbersMod2 = new ArrayList<>();

		int tilesCounter=1;
		for(int i=0; i<numbers.size()-1;i++) {
			if(numbers.get(i)==(numbers.get(i+1)-1))tilesCounter++;
			else {
				consecutiveNumbers.add(tilesCounter);
				tilesCounter=1;
			}	
		}
		if(!numbers.isEmpty())consecutiveNumbers.add(tilesCounter);

		for(int i=0;i<consecutiveNumbers.size();i++) {
			int number = consecutiveNumbers.get(i);
			while(number>2) {
				consecutiveNumbersMod2.add(2);
				number-=2;
			}
			if(number!=0)consecutiveNumbersMod2.add(1);
		}
		Collections.sort(adjacentCells);
		Collections.sort(consecutiveNumbersMod2);
		return "N" + consecutiveNumbersMod2 + "C" + adjacentCells;

	}

	private static boolean enolaCanWinImmediately2(int[] state, ArrayList<Integer> numbers) {
		HashSet<Integer> set = new HashSet<>();
		HashSet<Integer> set2 = new HashSet<>();

		if(state[0]!=-1&&state[1]==-1&&(numbers.contains(new Integer(state[0]+1)) && numbers.contains(new Integer(state[0]-1)))) {
			set.add(1);
			if(numbers.contains(new Integer(state[0]+1))) set2.add(state[0]+1);
			if(numbers.contains(new Integer(state[0]-1))) set2.add(state[0]-1);

		}

		for(int i=1; i<state.length-1;i++) {
			if(state[i]!=-1&&(state[i-1]==-1||state[i+1]==-1)&&(numbers.contains(new Integer(state[i]-1))&&numbers.contains(new Integer(state[i]+1)))) {
				if(state[i-1]==-1)set.add(i-1);
				else if (state[i+1]==-1)set.add(i+1);

				if(numbers.contains(new Integer(state[i]+1))) set2.add(state[i]+1);
				if(numbers.contains(new Integer(state[i]-1))) set2.add(state[i]-1);
				if(set.size()>=2&&set2.size()>=4) return true;
			}
		}

		if(state[state.length-1]!=-1&&state[state.length-2]==-1&&(numbers.contains(new Integer(state[state.length-1]+1)) && numbers.contains(new Integer(state[state.length-1]-1)))) {
			set.add(state.length-2);
			if(numbers.contains(new Integer(state[0]+1))) set2.add(state[state.length-1]+1);
			if(numbers.contains(new Integer(state[0]-1))) set2.add(state[state.length-1]-1);
			if(set.size()>=2&&set2.size()>=4) return true;
		}
		return false;
	}
}
