# Sudoku Game
 Two sudoku games, a classic, and an original one.
 
 
 # Regular Sudoku
GUI was added for the regular sudoku. Both were built using a complete search approach, the grid is traversed recursively until a solution is found, by checking if each number in it verifies the sudoku conditions (ex: no number is repeated in its row, column and region for regulat sudoku). The game is played in the following way: if the user enters an accepted number: it is accepted and put into the grid in black. If they enter an invalid number, it is replaced with a right number from a valid solution (as a hint) and written in red. If they click click on the button "solve", the empty fields will be replaced with the rest of the solution in green. (You can check the video below, it shows exactly what I explained). 

https://user-images.githubusercontent.com/76274266/119194917-7705f900-ba8c-11eb-8bab-240e720fffe7.mp4

 # Original Sudoku
This game has no GUI associated. However, it can be replaced by replacing "test.in" by "test1.in" or "test2.in" ... in the main function of the class Game, and run it to see the results on the console. It has a different definition of regions, a region is defined by its cells, in no precise pattern. And a number cannot be repeated in its region, and up, down, left, right, and in diagonals. Here is an example of how regions can be defined:

![Screenshot (397)](https://user-images.githubusercontent.com/76274266/119195518-7457d380-ba8d-11eb-862d-dc6d5c0c3bc8.png)
