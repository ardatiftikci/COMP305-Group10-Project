import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class HolmesGameAnalysis {

	public static void main(String[] args) throws FileNotFoundException {

		File file = new File("test1.txt");
		Scanner sc = new Scanner(file);

		int size = Integer.parseInt(sc.nextLine());
		System.out.println("Size:"+size);


		int roundCounter=1;
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

		while(!gameOver(currentState) && roundCounter <= size){
			arr=sc.nextLine().split(" ");
			place=Integer.parseInt(arr[0]);
			num=Integer.parseInt(arr[1]);

			if(roundCounter % 2 == 0){ //Enola's turn
				if(enolaWinsOptimally(currentState)){
					makeMove(currentState, place, num);
					if(!enolaWinsOptimally(currentState)){
						mistakeCount++;
					}
				} else {
					makeMove(currentState, place, num);
				}
			} else { //Sherlock's turn
				if(!enolaWinsOptimally(currentState)){
					makeMove(currentState, place, num);
					if(enolaWinsOptimally(currentState)){
						mistakeCount++;
					}
				} else {
					makeMove(currentState, place, num);
				}
			}
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

	public static boolean enolaWinsOptimally(int[] currentState) {
		return false;
	}

	public static void  makeMove(int[] currentState, int place, int num) {
		currentState[place-1]=num;
	}

}
