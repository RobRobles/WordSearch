import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class WordSearch {

	//user input
	private JTextField input; 
	//adding words to the grid 
	private JButton submit; 
	//finish adding words to the grid, when finished is clicked submit button will go away
	private JButton finish; 
	//this will show up after finish is selected and will shuffle the rest of the grid with random letters 
	private JButton scramble; 
	//this will reset the board and the buttons 
	private JButton reset; 
	//Using a label or text area i will print the grid out and show it thought the label or text area 
	private JTextArea area;  
	//This will be a list of words that the user needs to find as they enter them in. 
	private JTextArea ListOfWords; 
	//creating a JFrame 
	private JFrame frame; 
	//this string will take in the what the user typed from the input JTextField
	private String word; 
	//this will be a warning label that will show up if the user types in a word too small or too big. 
	private JLabel warning; 
	//label for show thing user what needs to be found 
	private JLabel ListToFind; 
	//this will hold the alphabet for shuffling the board. 
	private static ArrayList<String> alpha;
	//I will uses this to test edge cases, fitting words along the edges and going up to the corners. 
	private boolean itFits; 
	//radio buttons to indicate what direction the word should be placed and their label
	private ButtonGroup group = new ButtonGroup(); 
	private JLabel dir; 
	//Horizontal
	private JRadioButton H;
	//Vertical 
	private JRadioButton V;
	//Diaganol 
	private JRadioButton D; 

	//matrix dimensions  
	private int row; 
	private int col; 
	private int size; 
	private String [][] Grid; 
	private String [][] scrambleGrid; 
	private String [] temp; 	

	@SuppressWarnings("deprecation")
	public WordSearch()
	{
		//I will use this to create the layout of the grid and buttons and initialize them 
		input = new JTextField(25); 
		submit = new JButton("submit"); 
		finish = new JButton("finish"); 
		scramble = new JButton("scramble"); 
		reset = new JButton("reset"); 
		area = new JTextArea(); 
		frame = new JFrame(); 
		warning = new JLabel(); 
		ListOfWords = new JTextArea();
		ListToFind = new JLabel("Words To Find"); 
		dir = new JLabel();
		H = new JRadioButton(); 
		V = new JRadioButton();
		D = new JRadioButton();

		//closing on exit
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//setting visible to true
		frame.setVisible(true); 
		//setting the size
		frame.setSize(940, 700); 
		//setting the display
		frame.setLayout(null);
		//centering the frame
		frame.setLocationRelativeTo(null);
		//setting the color of the frame
		frame.getContentPane().setBackground(Color.lightGray);  //Whatever color



		//adding everything to the frame 
		frame.add(input); 
		frame.add(submit); 
		frame.add(finish); 
		frame.add(scramble); 
		frame.add(reset); 
		frame.add(area); 
		frame.add(warning); 
		frame.add(ListOfWords); 
		frame.add(ListToFind); 
		frame.add(dir); 
		frame.add(H);
		frame.add(V);
		frame.add(D);

		//new FLowLayout() does not let me position things where I want. I set setLayout = null and have to manually place things on the Frame 
		submit.setBounds(390, 620, 75 , 30); 
		finish.setBounds(470, 620, 75, 30);
		scramble.setBounds(550, 620, 100, 30);
		reset.setBounds(655, 620, 75, 30);
		input.setBounds(235, 620, 150, 30);
		area.setBounds(40, 40, 715, 560);
		ListOfWords.setBounds(760, 40, 150, 560);
		ListToFind.setBounds(760, 10, 150, 40);
		dir.setBounds(40, 615, 110, 50);
		dir.setText("H        V       D       ");
		//setting out the radio buttons for which direction to insert the word 
		//adding all of the buttons to a button Group to make only one of them at a time selectable 
		H.setBounds(35, 615, 20 ,20);
		H.setBackground(Color.LIGHT_GRAY);
		V.setBounds(65, 615, 20 ,20);
		V.setBackground(Color.LIGHT_GRAY);
		D.setBounds(95, 615, 20 ,20);
		D.setBackground(Color.LIGHT_GRAY);
		group.add(H);
		group.add(V);
		group.add(D);
		H.setSelected(true);

		//getting the proper spaces layed out. 
		warning.setBounds(95, 590, 200, 30);

		//making the area non editable and proper spacing
		area.setFont(new Font("monospaced", Font.PLAIN, 14));
		area.setEditable(false);
		ListOfWords.setEditable(false);


		//hiding the scramble button, users should not get ahead of themselves 
		scramble.hide(); 

	}

	public void createGrid()
	{
		//I will use this to create the grid and add it to the label or text area 
		row = 28; 
		col = 45; 
		size = row*col; 
		Grid = new  String [row][col]; 
		temp = new String[size]; 

		//adding the * to the temp array 
		for(int i = 0; i < size; i++)
		{
			temp[i] = "*";
		}

		//adding the temp array to the Grid 
		int a = 0; 
		for(int j = 0; j < row; j++)
		{
			for(int s = 0; s < col; s++)
			{
				Grid[j][s] = temp[a];
				a++;
			}
		}

		//Temp string to hold values of Grid to append them in the text area 
		String holder = ""; 
		//printing out the grid to the TextArea 
		for(int f = 0; f < row; f++)
		{
			for(int t = 0; t < col; t++)
			{
				holder = (Grid[f][t] + " "); 
				area.append(holder);
			}
			holder = "\n";
			area.append(holder); 
		}

	}

	public void addWord()
	{
		//Just making sure I have the right Matrix dementions
		row = 28; 
		col = 45; 
		size = row*col; 
		scrambleGrid = new  String [row][col];

		//a temp array to store all of the values 
		final ArrayList<String> tempWord = new ArrayList<String>(); 

		//I will use this to perform the correct action for SUBMIT
		submit.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{

						//getting the word from the input box, making them all upper case to match the shuffling. 
						word = input.getText(); 
						//clearing the input box for a new word. 
						input.setText("");
						word = word.toUpperCase();

						int wordLength = word.length();

						//making two random number generators that I will use to place the words on the grid
						int r = (int )((Math.random() * 28));
						int c = (int )((Math.random() * 45));
						//making a random number to mix up the directions of the diagnol and direction of the word
						int mixUpWords = (int ) ((Math.random() * 2 + 1));

						//getting rid of leading white spaces if the user decides to click on the submit button over and over 
						if(word.equals("")){}
						else
						{
							ListOfWords.append(word + "\n");
						}

						//adding each letter in word to the tempWord ArrayList 
						for(int i = 0; i < word.length(); i++)
						{
							String temp = (word.charAt(i) + "");
							tempWord.add(temp); 
						}

						//reversing the direction of the word taken in
						if(mixUpWords == 2)
						{
							Collections.reverse(tempWord);
						}

						String placeHolder = ""; 
						String saveMe = ""; 

						//////////////////VERTICLE INSERTION////////////////////
						if(V.isSelected())
						{

							do{
								itFits = true;

								for(int u = 0; u < wordLength; u++)
								{
									//CHECKINGBOUNDS
									if(r > (row - wordLength))
									{
										r = (row - wordLength - 1); 
									}

									if(Grid[r + u][c] != "*")
									{
										r = (int )((Math.random() * 28));
										c = (int )((Math.random() * 45));
										itFits = false;
										break;
									}
								}

							}while(itFits == false);

							int z = 0;
							for(int j = r; j < (r + wordLength); j++)
							{
								Grid[j][c] = tempWord.get(z);
								z++; 
							}

							tempWord.clear();
						}
						////////////////////////////////////////////////////////
						///////////////////HORIZONTAL INSERTION/////////////////
						if(H.isSelected())
						{	
							//this do while loop will check to see if any spaces the newly inserted word will take up is an empty space (*)
							//if it is not then it will change the random numbers until it finds a empty block that the word will fit in. 
							do{
								itFits = true;

								for(int u = 0; u < wordLength; u++)
								{
									//CHECKINGBOUNDS
									if(c > (col - wordLength))
									{
										c = (col - wordLength - 1); 
									}

									if(Grid[r][c + u] != "*")
									{
										r = (int )((Math.random() * 28));
										c = (int )((Math.random() * 45));
										itFits = false;
										break;
									}
								}

							}while(itFits == false);

							int z = 0; 
							for(int j = c; j < (c + wordLength); j++)
							{
								Grid[r][j] = tempWord.get(z);
								z++;
							}

							tempWord.clear();
						}
						/////////////////////////////////////////////////////////
						////////////////////DIAGANOL//////////////////////////////
						if(D.isSelected())
						{
							//this do while loop will check to see if any spaces the newly inserted word will take up is an empty space (*)
							//if it is not then it will change the random numbers until it finds a empty block that the word will fit in. 
							do{
								itFits = true;

								//CHECKINGBOUNDS
								//keeping the words in bounds, I ran this until a diagnol word was placed right up to the edge. 
								if(c >= (col - wordLength))
								{
									c = (col - wordLength); 
								}
								if(r >= (row - wordLength))
								{
									r = (row - wordLength);
								}

								//I am using tempR as a placeholder, I was running into a outofbounds error becase I was incrementing r 
								//to check diagnol spaces, this also increased r that was to be used later in the insertion loop i have. 
								int tempR = r; 
								for(int u = c; u < (c + wordLength); u++)
								{

									if(Grid[tempR][u] != "*")
									{
										r = (int )((Math.random() * 28));
										c = (int )((Math.random() * 45));
										itFits = false;
										break;
									}
									tempR++; 
								}

							}while(itFits == false);

							int y = 0;
							for(int z = c; z < (c + wordLength); z++)
							{								
								Grid[r][z] = tempWord.get(y);
								y++;
								r++;
							}
							tempWord.clear(); 
						}

						//copying the grid to scrambleGrid  
						for(int f = 0; f < row; f++)
						{
							for(int t = 0; t < col; t++)
							{
								if(Grid[f][t] == "*")
								{ 
									scrambleGrid[f][t] = "*"; 
								}
								else if (Grid[f][t] != "*")
								{
									saveMe = Grid[f][t]; 
									scrambleGrid[f][t] = saveMe;
								}
							}

						}

						//setting the previous unscrambled grid to null to make room for the new grid
						area.setText(null); 

						//printing the new scrambleGrid out to the text area. 
						for(int u = 0; u < row; u++)
						{
							for(int a = 0; a < col; a++)
							{
								placeHolder = (scrambleGrid[u][a] + " "); 
								area.append(placeHolder); 
							}

							placeHolder = "\n";
							area.append(placeHolder); 
						}
					}
				});
	}

	@SuppressWarnings("deprecation")
	public void buttonActions()
	{
		row = 28; 
		col = 45; 
		size = row*col; 
		scrambleGrid = new  String [row][col];



		finish.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						scramble.show(); 
						submit.hide(); 
						finish.hide();
						warning.setText(""); 
					}
				}
				); 

		reset.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						warning.setText("");
						input.setText("");
						submit.show(); 
						finish.show(); 
						area.setText(null);
						ListOfWords.setText(null);
						ListToFind = null; 
						createGrid(); 
						group.clearSelection(); 
						H.setSelected(true);
					}
				}
				); 

		scramble.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						scramble.hide(); 

						String placeHolder = ""; 
						String saveMe = ""; 

						//copying the grid to scrambleGrid  
						for(int f = 0; f < row; f++)
						{
							for(int t = 0; t < col; t++)
							{
								if(scrambleGrid[f][t] == "*")
								{
									Grid[f][t] = randomAlpha(); 
									scrambleGrid[f][t] = Grid[f][t]; 
								}
								else if (scrambleGrid[f][t] != "*")
								{
									saveMe = scrambleGrid[f][t]; 

									scrambleGrid[f][t] = saveMe;
								}
							}

						}

						//setting the previous unscrambled grid to null to make room for the new grid
						area.setText(null); 

						//printing the new scrambleGrid out to the text area. 
						for(int u = 0; u < row; u++)
						{
							for(int a = 0; a < col; a++)
							{
								placeHolder = (scrambleGrid[u][a] + " "); 
								area.append(placeHolder); 
							}

							placeHolder = "\n";
							area.append(placeHolder); 
						}
					}


				}
				);
	}

	private static String randomAlpha() {

		String random = ""; 

		//this will give me a random number from 1 to 26. I thought I needed 25 instead but it gave me a out of bounds error 
		int n = (int )(Math.random() * 25);


		alpha = new ArrayList<String>(); 
		alpha.add("A");alpha.add("B");alpha.add("C");alpha.add("D");alpha.add("E");alpha.add("F");alpha.add("G");
		alpha.add("H");alpha.add("I");alpha.add("J");alpha.add("K");alpha.add("L");alpha.add("M");alpha.add("O");
		alpha.add("P");alpha.add("Q");alpha.add("R");alpha.add("S");alpha.add("T");alpha.add("U");alpha.add("V");
		alpha.add("W");alpha.add("X");alpha.add("Y");alpha.add("Z");

		for (int i = 0; i < alpha.size(); i++)
		{
			random = alpha.get(n);
		}

		return random;
	}

	public static void main(String[] args) {

		WordSearch ws = new WordSearch();
		ws.buttonActions();
		ws.addWord();
		ws.createGrid(); 
	}

}
