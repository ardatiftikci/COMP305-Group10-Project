import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.*;

public class HolmesGameAnalysisApproach1 {

	static int roundCounter=1;
	static HashMap<String, Boolean> hashMap = new HashMap<String, Boolean>();

	public static void main(String[] args) throws FileNotFoundException {
		
		Scanner s=new Scanner(System.in);  
   		System.out.print("Enter which test you want to run:\n ");  
    		String str= s.nextLine();
   		File file = new File(str+".txt");
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
			enolaWins = enolaWinsOptimally(currentState, remainingNumbers, roundCounter%2==1);
			if(enolaWins!=enolaWinsPrev) {
				mistakeCount++;
				System.out.println(enolaWins);
				System.out.println(mistakeCount);
			}
			makeMove(currentState, place, num);
			remainingNumbers.remove(new Integer(num));
			
			if(roundCounter%100==0) System.out.println("Round: "+roundCounter);

			/*System.out.print("After Round " + roundCounter +": [" + currentState[0]);
			for(int i=1; i<size; i++){
				System.out.print(", " + currentState[i]);
			}
			System.out.println("]");*/

			roundCounter++;
	}

		System.out.println("Total Mistake: "+ mistakeCount);
		sc.close();
	}


	public static boolean gameOver(int[] currentState) {
		if(isEnolaAlreadyWon(currentState)) {
			hashMap.put(arrayToString(currentState), true);
			return true;
		}
		if(isGameFinished(currentState)) {
			hashMap.put(arrayToString(currentState), false);
			return true;
		}
		return false;
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

	public static boolean enolaWinsOptimally(int[] state, ArrayList<Integer> numbers, boolean enolaTurn) {
	
		if(roundCounter<state.length/3) return true;
		
		if(gameOver(state)) {
			return hashMap.get(arrayToString(state));
		}

		if(hashMap.containsKey(arrayToString(state))) {
			return hashMap.get(arrayToString(state));
		}


		if(enolaTurn) {
			if(enolaCanWinImmediately(state, numbers)) return true;
			if(enolaCanWinIn2Moves(state, numbers)) return true;
			for(Integer number : numbers) {
				for(int i=1; i<=state.length; i++) {
					if(state[i-1]==-1) {
						int[] newState = new int[state.length];
						System.arraycopy(state, 0, newState, 0, newState.length);
						makeMove(newState, i, number);
						ArrayList<Integer> newNumbers = new ArrayList<Integer>(numbers);
						newNumbers.remove(new Integer(number));
						if(enolaWinsOptimally(newState, newNumbers, !enolaTurn)) {
							hashMap.put(arrayToString(state), true);
							return true;
						}
					}
				}
			}
			hashMap.put(arrayToString(state), false);
			return false;

		}else {
			if(enolaCanWinImmediately2(state, numbers)) return true;
			for(Integer number : numbers) {
				for(int i=1; i<=state.length; i++) {
					if(state[i-1]==-1) {
						int[] newState = new int[state.length];
						System.arraycopy(state, 0, newState, 0, newState.length);
						makeMove(newState, i, number);
						ArrayList<Integer> newNumbers = new ArrayList<Integer>(numbers);
						newNumbers.remove(new Integer(number));
						if(!enolaWinsOptimally(newState, newNumbers, !enolaTurn)) {
							hashMap.put(arrayToString(state), false);
							return false;
						}
					}
				}
			}
			hashMap.put(arrayToString(state), true);
			return true;
		}

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


	public static void  makeMove(int[] currentState, int place, int num) {
		currentState[place-1]=num;
	}

	public static String arrayToString(int[] state) {
		String result = "";
		for(int i=0; i<state.length-1;i++) {
			result+=state[i];
			result+=",";
		}
		result+=state[state.length-1];
		return result;
	}

}
