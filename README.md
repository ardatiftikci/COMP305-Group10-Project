# COMP305Project
COMP 305 Semester Project by Group 10
Selected Problem: A Game Analysis with the Holmes Family

Group Members (Name - e-Mail - GitHub Name):
- Arda Tiftikçi - atiftikci18@ku.edu.tr - ardatiftikci
- İrem Nur Bulut - ibulut18@ku.edu.tr - iremnurbulut
- İsmail Ozan Kayacan - ikayacan18@ku.edu.tr - ikayacan18
- Ömer Faruk Aksoy - oaksoy18@ku.edu.tr - omeraksoy1


Completed Steps for Meeting 1:
1) Decided on the language (Java)
2) Talked about the problem.
3) Wrote code of game simulation.

TO DO:
1) Try to come up with an idea.
2) Convert the idea to pseudocode.

Completed Steps for Meeting 2: 
1) Analyzed the algorithm further. 
2) Created the algorithm for the base case simulation (test1).

TO DO:
1) Debug the written code. 

Completed Steps for Meeting 3: 
1) Created the algorithm for the test1,2 and 3.
2) Pruning is done.
3) Dynamic programming approach is used. 

TO DO:
1) Debug the written code for test2 (10,000 & 100,000). 

Completed Steps for Meeting 4: 
1) Did more analysis to reduce time spent in 10,000 and 100,000 cases.
2) Included hashsets to be more efficient. 

TO DO:
1) Reduce time spent in 10,000 and 100,000 cases 

Completed Steps for Meeting 5: 
1) Renovated the dynamic programming approach.
2) Reduced the time spent by including more pruning. 
3) Presentation is prepared halfway. 
4) Corrected and improved the commands in the code.

TO DO:
1) Improve Presentation

Completed Steps for Meeting 6:
1) First half of the presentation is finalized.
2) Text for second half of presentation is prepared.
3) Test cases is corrected and code is updated accordingly.

TO DO:
1) Complete Presentation
2) Beautify the code

Completed Steps for Meeting 7:
1) Presentation is completed.
2) Code is beautified.

What we tried (chronologically):


Approach 1:

1) Brute Force

- We used array to represent state (since size is fixed), and arrayList to represent remaining numbers (since size is dynamic).

- Our initial approach was a brute force solution to the problem, focusing strictly on correctness while disregarding performance. By trying all possible moves in every turn, we were able to produce the correct answer for tests of very small size, but for any N > 6 it would not terminate in a reasonable amount of time.

     Time Complexity: T(n) = T(n-1) * n^2 + O(1)   =>   T(n) = O(n^n)
     Space Complexity: We have 2^n subsets and call recursively all permutations of them, so space complexity   =>   O(2^n * n!)

2) Dynamic Programming

- After realizing that same states are encountered over and over during recursive calls, we added memoization by using hashmap to store states and their outcomes. With this addition, we were able to produce the correct result relatively quickly compared to before.

     Time Complexity: O(2^n * n!) unique subproblems, each subproblem takes O(n^2) time (since in each round there are n^2 possible moves, and all of them tried until a result is achieved).     =>     T(n) = O(2^n * n^2 * n!)
     Space Complexity: Same as before   =>   O(2^n * n!)
     

3) Pruning

- Even though we implemented dynamic programming, for each state we were trying all possibilities until the board gets full. But, we realized that in some states we can decide the winner directly. So before recursive calls, when it is Enola's turn we checked whether; 
     a) Enola can directly win by 1 move (when one of the numbers in the board has a adjacent empty cell, and one of the remaining numbers is 1 more or less than that number.)
     b) Enola can win in 2 moves (when there are 3 empty adjacent cells, and 3 consecutive remaining numbers)
- The rule about 3 empty adjacent cells, and 3 consecutive remaining numbers led us to make an important observation: Since during first n/3 - 1 rounds, there certainly exists 3 empty adjacent cells and 3 consecutive numbers, Enola is always optimal winner.
- We implemented 3 ideas above, then our algorithm got much faster for checking the winner when it is Enola's Turn. It worked fast for values of N <= 20. 
- But it was very slow while checking winner in Sherlock's turn. For that, we need to eliminate some cases for Sherlock's turn as well.
      - We checked the states in which there are still at least 2 empty cells and 4 different unused numbers which each of them could make Enola the winner. Sherlock, by 1 move,         can only prevent 3 of these winning moves of Enola. Therefore, after Sherlock's any move, Enola is able to directly win.
- We implemented this idea as well. As a result, our algorithm got much much faster, and it worked sufficiently fasts for tes1, test2, test3. But test4 and test5 were likely to take hours or even days to complete.

     These additions did not change asymptotic complexities, but in practice since a lot of states are eliminated, run times are much more improved! First 3 tests ends very fast, but last 2 tests seems to take hours and maybe even days!

     The reason is number of unique subproblems O(2^n * n!) which is very huge! So we need to decrease number of unique subproblems by using the fact that most of them equivalent in terms of deciding winner. Approach 2 will use different representation of states.


Approach 2:

1) New Representation
- Represented the game as two lists: a sequence of the number of adjacent empty cells & a sequence of the number of consecutive remaining numbers. This allowed us to make use of different *but equivalent* states and keep one of them memoized rather than however many there may be.

     Number of unique subproblems: O(n) with respect to numbers and O(n) with respect to cells, thus it is O(n²).
     Cost of each subproblem: O(n^2)
     Time Complexity: O(n^4) → much better
     Space Complexity: O(n^2)
     
2) Further Pruning
- We realized that depending on the current optimal winner, it is possible for us to "skip" analyzing the move of one of the players. Since the algorithm is focused on the optimal scenario, if Player 1 is winning optimally at the end of their turn, Player 2 cannot change this. The converse of this also holds, allowing us to significantly (N/2) prune the number of turns we perform checks on. (e.g. if Enola is the optimal winner @ round 7000 and she does not make a mistake until round 9000, we would simulate the outcome of 1000 rounds rather than 2000)

3) We thought that we can bound number of moves to try in a particular turn of Sherlock to N instead of N^2. We defined promising moves as any move by placing non-winning numbers to the cells that Enola would prefer, if she was to play in that round. In other words, Sherlock put a number to a place if Enola can win immediately by putting *another* number to that place. Thus, if there is a possibility of Sherlock winning optimally, it would be thanks to these moves. Cost of each subproblem became O(n). so time complexity is reduced to O(n^3).

4) If we get rid of the cases in which Enola can win in maximum 2 moves, Enola's only winning state is when there are 2 remaining numbers, and they are consecutive, and the   remaining cells are adjacent (in Enola's Turn). So, we created EnolaCantWin method if it returns true, Sherlock wins optimally for sure. However, if it returns false the code proceeds to recursive calls. This reduces the required time for Test 4 to 1 minute and Test 5 to 20 minutes. Asymptotic time complexity is still O(n^3).

Our final algorithm (HolmesGameAnalysisApproach2) works with all prunings we described above and Dynamic Programming on new representation of states.

How to Run Code:

- Run as a java application (HolmesGameAnalysisApproach2).
- Enter the test number that you want to run. 

Test Results:
1) 3 Mistakes (<1 second)
2) 0 Mistake (<1 second)
3) 0 Mistake (1 second)
4) 55 Mistakes (1 minute)
5) 287 Mistakes (20 minutes)


Note: Original test format was erroneus since size (number of saucers) was not given. Without knowing size, it is not possible to decide winner (for example if size is very huge, because of n/3 - 1 rounds fact stated above, number of mistake would be 0 for each tests.) Therefore we also added size information to test cases in the first line.
