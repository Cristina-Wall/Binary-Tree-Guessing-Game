
import java.util.Scanner;
import java.io.*;

public class GuessingGame {
	
	//create a new variable that will keep track of if the answer was guessed
	public boolean guessed = false;
	//create a new instance of the scanner that will be used to answer the queations
	Scanner sc = new Scanner(System.in);
	//create a new binary tree
	BinaryTree<String> testTree = new BinaryTree<String>();
	public String leftmostNode = "";
	
	public GuessingGame() {
		//the constructor calls methods to create a tree and starts the game by asking questions
		createTree(testTree);
		askQuestions(testTree);
		
	}
	
	public void createTree(BinaryTree<String> tree) {
		//create the binary tree
		BinaryTree<String> hTree = new BinaryTree<String>();
		
		//populate the binary tree with all the leaf nodes
		hTree.setTree("Is it a dog?");
		BinaryTree<String> iTree = new BinaryTree<String>("Is it a lion?");
		BinaryTree<String> jTree = new BinaryTree<String>("Is it salmon?");
		BinaryTree<String> kTree = new BinaryTree<String>("Is it a snake?");
		BinaryTree<String> lTree = new BinaryTree<String>("Is it chocolate?");
		BinaryTree<String> mTree = new BinaryTree<String>("Is it chips?");
		BinaryTree<String> nTree = new BinaryTree<String>("Is it timothee chalamet?");
		BinaryTree<String> oTree = new BinaryTree<String>("Is it a table?");
		
		//add all of the other nodes to the tree and connect them to their parent nodes
		BinaryTree<String> dTree = new BinaryTree<String>("Is it a pet?", hTree, iTree);
		BinaryTree<String> eTree = new BinaryTree<String>("Is it a fish?", jTree, kTree);
		BinaryTree<String> fTree = new BinaryTree<String>("Is it sweet?", lTree, mTree);
		BinaryTree<String> gTree = new BinaryTree<String>("Is it a person?", nTree, oTree);
		
		BinaryTree<String> bTree = new BinaryTree<String>("Is it a mammal?", dTree, eTree);
		BinaryTree<String> cTree = new BinaryTree<String>("Is it food?", fTree, gTree);
		
		tree.setTree("Is it an animal?", bTree, cTree);
	}
	
	public void treeInfo(BinaryTree<String> tree) {
		//this method prints out the details of the binary tree, if it is not empty
		if (tree.isEmpty())
			System.out.println("empty");
		else
			System.out.println("not empty");
		
		System.out.println("Root: \"" + tree.getRootData() +"\"");
		System.out.println("Height: " + tree.getHeight());
		System.out.println("Num nodes: " + tree.getNumberOfNodes());
	}
	
	public void correctGuess() {
		//this method is called when the answer is guessed correctly
		if(guessed) {
			System.out.println("Great! I guessed correctly!");
			//it calls the ending() method which prints out the options for playing again, loading and saving
			ending();
		}
	}

	public void ending() {
		//this method prints out all the options to allow the player to play again, save the tree and load a tree
		System.out.println("Would you like to: \n1.Play again?\n2.Save the tree?\n3.Load another tree?\n4.Quit?\n");
		
		//the scanner gets the input from the player
		int input = sc.nextInt();
		
		if(input == 1) {
			askQuestions(testTree);
		}
		else if(input == 2) {
			System.out.println("saving");
			saveToFile();
		}
		else if(input == 3) {
			System.out.println("loading");
			loadFromFile();
		}
		else if(input == 4) {
			System.out.println("quit");
			//quit
		}
	}
	
	public void fail(BinaryNodeInterface<String> failNode) {
		//if the answer is not guessed, then this method asks the player to add a new question to the tree
		if(!guessed) {
			
			System.out.println("What were you thinking of?");
			String newNode = sc.next();
			BinaryNode<String> newBinaryNode = new BinaryNode<String>(newNode);
			
			System.out.println("Type a question to distinguish this from the last guess: ");
			String newQuestion = sc.next();
			System.out.println("Is the guess you entered the correct answer to your question?");
			String correct = sc.next();
			
			boolean newInputCorrect = false;
			boolean temp = false;
			
			while(temp==false) {
				if(correct.toLowerCase().equals("yes")) {
					//if the answer to the new question is 'yes' then the old node needs to be the new right node
					newInputCorrect = false;
					//set the new right child to the old parent node
					failNode.setRightChild(failNode);
					//set the new left child to the new node input by the player
					failNode.setLeftChild(newBinaryNode);
					//set the new parent node to the new question node input by the player
					failNode.setData(newNode);
					temp = true;
				}
				else if(correct.toLowerCase().equals("no")) {
					//if the answer to the new question is 'no' then the old node needs to be the new left node
					//this is the same as the previous if statement, except change around new left and right child nodes
					newInputCorrect = true;
					failNode.setLeftChild(failNode);
					failNode.setRightChild(newBinaryNode);
					failNode.setData(newNode);
					temp = true;
				}
			}
			//call the method to restart, load or save a tree
			ending();	
		}
	}
	
	public void askQuestions(BinaryTree<String> tree) {
		//this method gets the root node of the tree and calls the method to start asking questions
		BinaryNodeInterface<String> currNode = tree.getRootNode();
		checkCurrNode(currNode);
	}
	
	public void checkCurrNode(BinaryNodeInterface<String> node) {
		//this method asks the questions and checks the answers from the player
		
		//read node prints out the data in the node
		readNode(node);
		
		String answer = sc.next();
		
		if(!node.hasLeftChild() && !node.hasRightChild() && answer.toLowerCase().equals("yes")) {
			//if the node is a leaf node and the answer is 'yes' then the answer has been guessed
			guessed = true;
			//call the correctGuess() method and then that will restart the game also
			correctGuess();
		}
		else if(!node.hasLeftChild() && !node.hasRightChild() && answer.toLowerCase().equals("no")) {
			//if the node is a leaf node and the answer is 'no' then the answer has not been guessed
			guessed = false;
			//call the fail method and pass in the node, which will change the tree
			fail(node);
		}
		
		if(answer.toLowerCase().equals("yes")) {
			if(node.hasLeftChild()) {
				//if the answer is 'yes' and the node has a left child, call the method again and pass in the left child or the current node
				checkCurrNode(node.getLeftChild());
			}
		}
		
		else if(answer.toLowerCase().equals("no")) {
			if(node.hasRightChild()) {
				//if the answer is 'no' and the node has a right child, call the method again and pass in the right child or the current node
				checkCurrNode(node.getRightChild());
			}
		}
		else {
			//if the answer is not 'yes' or 'no', call the method again and pass in the current node again
			System.out.println("Enter a valid answer");
			checkCurrNode(node);
		}
	}
	
	public void readNode(BinaryNodeInterface<String> node) {
		//this prints out the details of the node
		String printNode = node.getData();
		System.out.println(printNode);
		
	}
	
	public void loadFromFile() {
		
		String filename = "savedTree.txt";
		String line = null;
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			line = reader.readLine();
			
			
			while(line != null) {
				
				//read line
				//
			}
			reader.close();
		} catch (IOException e) { }
	}
	
	public void getLeftmostNode(BinaryNodeInterface<String> node) {
		
		String toFile = "";
		
		String nodeTemp = node.getData();
		toFile += nodeTemp;
		
		if(node.getLeftChild() != null) {
			getLeftmostNode(node.getLeftChild());
			
		}
		else {
			leftmostNode = node.getData();
		}
		
	}
	
	public void saveToFile() {
		
		System.out.println("test");
		
		String filename = "savedTree.txt";
		String toFile = "";
		
		//toFile += BinaryTree.getToFile();
		
		toFile += leftmostNode;
		
		System.out.println("toFile: " + toFile);
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
			writer.write(toFile);
			writer.close();
		} catch (IOException e) { }
		
		System.out.println("Tree saved\n");
		ending();
	}
	
	public static void main(String[] args) {
		
		GuessingGame g = new GuessingGame();
		
	}
}


