/*Mishael Zerrudo
  CSC 311 Project 2
  A program that gathers information about browsing patterns of users
  October 25, 2015
*/

import java.util.*;
import java.io.*;
/****************** Stack Implementation ******************/
class Stack {
	
    private int top;
    int size;
    Object[] stack ;
	
    public Stack(int arraySize){
        size=arraySize;
        stack= new Object[size];
        top=-1;
    }
    public void push(Object value){
        if(top==size-1){
            System.out.println("Stack is full, can't push a value");
        }
        else{

            top=top+1;
            stack[top]=value;
        }
    }
    public Object pop(){
		Object result = null;
        if(!isEmpty()){
			result = stack[top];
            top=top-1;
		}
        else{
            System.out.println("Can't pop...stack is empty");
        }
		
		return result;
    }
	
	public Object peek(){
		Object result = null;
        if(!isEmpty()){
			result = stack[top];
		}
        else{
            System.out.println("Can't pop...stack is empty");
        }
		
		return result;
	}
	
    public boolean isEmpty(){
        return top==-1;
    }
    public void display(){

        for(int i=0;i<=top;i++){
            System.out.print(stack[i]+ " ");
        }
        System.out.println();
    }
}

/****************** Queue Implementation ******************/
class Queue
{
	private final int N;
	private Stack s1;
	private Stack s2;
	private static final int capacity = 10;
	private int count = 0;		//number of elements
	
	//default constructor
	public Queue()
	{
		this(capacity);
	}
	
	//one-argument constructor
	public Queue(int capacity)
	{
		N = capacity;
		s1 = new Stack(N);
		s2 = new Stack(N);
	}
	
	//checks if queue is empty
	public boolean isEmpty()
	{
		return (count == 0);
	}
	
	//checks if queue is full
	public boolean isFull()
	{
		return (count == capacity);
	}
	
	//inserts object at the end of queue
	public boolean enqueue(Object c)
	{
		if (isFull())
			return false;
		else
		{
			s1.push(c);
			count++;
		}
		return true;
	}
	
	//removes object at the beginning of queue
	public Object dequeue()
	{
		Object result = null;
		if (isEmpty())
			System.out.println("Queue is empty");
		else
		{
			for (int i = 0; i < count - 1; i++)		//if there are other elements in s1
				s2.push(s1.pop());					//push those elements into s2
			result = s1.pop();
			for (int i = 0; i < count - 1; i++)		//if there are elements in s2
				s1.push(s2.pop());					//push them back into s1
			count--;
		}
		return result;
	}
	
	//displays the value of queue
	public void display()
	{
		for (int i = 0; i < count; i++)
			s2.push(s1.pop());
		for(int i = 0; i < count; i++)
		{
			System.out.print(s2.peek() + " ");
			s1.push(s2.pop());
		}
		System.out.println("");
	}
}

/****************** Data Analysis Program ******************/
public class DataAnalysis
{
	
	private Stack chronological;		//holds visited site from oldest to most recent
	private Queue rChronological;		//holds visited site from most recent to oldest
	private Object[][] webVisited;		//holds number of times a site is visited
	private Object[][] userAccounts;	//holds username and password
	private int checkLogIn;				//holds 1 if a user is logged in, 0 otherwise
	private int numOfUsers;				//holds number of users registered
	private int siteVisited;			//holds number of websites visited
	private int queueCount;				//since queue can have duplicates, it will have different number of elements than the stack and array
	private final int SIZE = 100;
	
	//default constructor
	public DataAnalysis()
	{
		chronological = new Stack(SIZE);
		rChronological = new Queue(SIZE);
		webVisited = new Object[SIZE][SIZE];
		userAccounts = new Object[SIZE][SIZE];
		checkLogIn = 0;
		numOfUsers = 0;
		siteVisited = 0;
		queueCount = 0;
	}
	
	//get all user accounts and passwords from a file and place them on a 2-D array
	private void setUserAccount() throws FileNotFoundException
	{
		int index = 0;
		File inputFile = new File("UserAccount.txt");
		Scanner input = new Scanner(inputFile);
		if (!inputFile.exists())
		{
			System.out.println("The text file \"UserAccount.txt\" is missing");
			System.exit(0);
		}
		while(input.hasNextLine())
		{
			userAccounts[0][index] = input.nextLine();			//stores the username
			userAccounts[1][index] = input.nextLine();			//stores the password
			numOfUsers++;
			index++;
		}
	}
	
	//opens up the menu
	public void mainMenu() throws FileNotFoundException, IOException
	{
		int userChoice = 0;
		Scanner input = new Scanner(System.in);
		setUserAccount();
		while (userChoice != 8)
		{
			System.out.println("Enter--");
			System.out.println("1. Register");
			System.out.println("2. Login");
			System.out.println("3. Visit a site");
			System.out.println("4. Check Browsing History (First visited to Most Recent)");
			System.out.println("5. Check Browsing History (Most Recent to First Visited)");
			System.out.println("6. Check number of times a website is visited");
			System.out.println("7. Check most visited website");
			System.out.println("8. Logout");
			userChoice = input.nextInt();
			System.out.println("");
			if (userChoice == 1)
				registerUser();
			else if (userChoice == 2)
			{
				if (checkLogIn == 1)		//if someone is already logged in
				{
					System.out.println("Another user is already logged in");
					System.out.println("Please log out and try again");
				}
				else
					checkLogIn = logIn();
			}
			else if (userChoice == 3)
			{
				if (checkLogIn == 0)		//if user is not logged in
					System.out.println("You must be logged in to visit a site");
				else
					getWebUrl();
			}
			else if (userChoice == 4)
			{
				if (chronological.isEmpty())	//if the stack is empty
					System.out.println("You have not visited a website");
				else
					printBrowsingHistory();
			}
			else if (userChoice == 5)
			{
				if (rChronological.isEmpty())	//if the queue is empty
					System.out.println("You have not visited a website");
				else
					printHistoryReverse();
			}
			else if (userChoice == 6)
			{
				if (siteVisited == 0)
					System.out.println("You have not visited a website");
				else
					printNumVisited();
			}
			else if (userChoice == 7)
			{
				if (siteVisited == 0)
					System.out.println("You have not visited a website");
				else
					printMostVisited();
			}
			System.out.println("");
		}
	}
	
	//registers a new user
	private void registerUser() throws FileNotFoundException, IOException
	{
		String username;
		String password;
		String fname;
		String lname;
		int attempt = 0;
		Scanner input = new Scanner(System.in);
		Random random = new Random();
		
		//PrintWriter wrapped around a BufferedWriter that is wrapped around a FileWriter
		//used to write on a file
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("UserAccount.txt", true)));	//true allows appending
		
		//gets user's first and last name
		System.out.println("Enter your first name: ");
		fname = input.nextLine();
		System.out.println("Enter your last name: ");
		lname = input.nextLine();
		
		//gets user's desired username
		System.out.println("Enter desired username: ");
		username = input.nextLine();
		attempt++;
		while (checkUserExists(username) != -1)		//username already exists
		{
			System.out.println("\nThat username already exists");
			if (attempt == 2)		//generate random username with 8 characters after 2 attempts
			{
				if (lname.length() < 6)				//if last name has less than six characters
					username = lname;
				else								//else if last name has six characters or greater
					username = lname.substring(0, 6);
				for (int i = username.length(); i < 8; i++)			//loop runs until username has 8 characters
					username += random.nextInt(9) + 1;				//concatenate random number between 0 - 9 to the username
				System.out.println("A randomly generated username has been chosen for you.");
				System.out.println("Your username is " + username);
			}
			else
			{
				System.out.println("Enter desired username: ");
				username = input.nextLine();
				attempt++;
			}
		}
		pw.println(username);
		userAccounts[0][numOfUsers] = username;
		System.out.println("");
		
		//get user's password
		System.out.println("Enter password (Must be 8 characters long): ");
		password = input.nextLine();
		while (password.length() < 8)			//continues to loop until pass is at least 8 characters long
		{
			System.out.println("\nPassword must be at least eight characters long");
			System.out.println("Enter password (Must be 8 characters long): ");
			password = input.nextLine();
		}
		pw.println(password);
		userAccounts[1][numOfUsers] = password;
		numOfUsers++;
		pw.close();
	}
	
	//user login, returns 1 if login is successful, 0 otherwise
	private int logIn()
	{
		Scanner input = new Scanner(System.in);
		String username;
		String password;
		int index;
		
		//prompt user for username and password
		System.out.println("Enter your username");
		username = input.nextLine();
		System.out.println("Enter your password");
		password = input.nextLine();
		
		index = checkUserExists(username);									//get the index of the username so we can find the password
		if (index == -1 || !userAccounts[1][index].equals(password))		//username or password is incorrect
		{
			System.out.println("\nIncorrect Username/Password");
			return 0;
		}
		System.out.println("Login Successful");
		return 1;
	}
	
	//get url of website the user wants to visit
	private void getWebUrl()
	{
		Scanner input = new Scanner(System.in);
		String url =" ";
		
		//prompt user for the URL
		System.out.println("Enter the web URL (Type Q or q to quit): ");
		while (!url.equalsIgnoreCase("Q"))
		{
			url = input.next();	
			if(url.equalsIgnoreCase("Q"))
				break;										//ignores all other code in the loop and exits the loop
			rChronological.enqueue(url);					//store url in queue
			queueCount++;									//increment number of elements in queue
			if (!checkSiteVisited(url))						//if this is the user's first time visiting the url
			{
				chronological.push(url);					//store url in stack
				webVisited[0][siteVisited] = url;			//store url in 2-D array
				webVisited[1][siteVisited] = 1;				//store 1 in 2-D array because it is user's first time visiting the site
				siteVisited++;								//increment number of websites visited
			}
			else											//if the user has visited the website before
			{
				updateStack(url);							//move the url to the top of the stack
				updateNumVisits(url);						//increment the number of times the user has visited the site
			}
		}
	}
	
	//prints user's browsing history from oldest to most recent
	private void printBrowsingHistory()
	{
		Queue temp = new Queue(SIZE);
		Object element;
		
		System.out.println("\nBrowsing History (Oldest to Most Recent):");
		for (int i = 0; i < queueCount; i++)
		{
			element = rChronological.dequeue();				//dequeue first element to the variable element
			System.out.println(element);					//print out the value of element (the URL)
			temp.enqueue(element);							//enqueue element to a temporary queue
		}
		for (int i = 0; i < queueCount; i++)
			rChronological.enqueue(temp.dequeue());			//put all elements in temp back to the original queue
	}
	
	//prints user's browsing history from most recent to oldest
	private void printHistoryReverse()
	{
		Stack temp = new Stack(SIZE);
		
		System.out.println("\nBrowsing History (Most Recent to Oldest):");
		for (int i = 0; i < siteVisited; i++)
		{
			System.out.println(chronological.peek());		//print out value currently at the top of stack
			temp.push(chronological.pop());					//pop the top of stack and push to another stack
		}
		for (int i = 0; i < siteVisited; i++)
			chronological.push(temp.pop());					//push all elements of temp back to the original stack
	}
	
	//prints the number of times a website has been visited by the user
	private void printNumVisited()
	{
		System.out.println("");
		for (int i = 0; i < siteVisited; i++)
			System.out.println(webVisited[0][i] + " visited " + webVisited[1][i] + " time(s)");
	}
	
	//prints the website that has been visited the most
	private void printMostVisited()
	{
		int greatestIndex = 0;
		int greatest = (int)webVisited[1][greatestIndex];
		
		for (int i = 0; i < siteVisited; i++)
		{
			if ((int)webVisited[1][i] > greatest)
			{
				greatestIndex = i;
				greatest = (int)webVisited[1][greatestIndex];
			}
		}
		System.out.println("Most visited website is: " + webVisited[0][greatestIndex]);
	}
	
	//returns i(index) if username is found, -1 otherwise
	private int checkUserExists(String name)
	{
		for (int i = 0; i < numOfUsers; i++)
		{
			if (name.equals(userAccounts[0][i]))
				return i;
		}
		return -1;
	}
	
	//returns true if site has been visited by user before, false otherwise
	private boolean checkSiteVisited(String url)
	{
		for (int i = 0; i < siteVisited; i++)
		{
			if (webVisited[0][i].equals(url))			//this url has been visited by user before
				return true;
		}
		
		return false;
	}
	
	//if url has been visited by user before, move the url to the top of the stack
	private void updateStack(String url)
	{
		Stack temp = new Stack(SIZE);
		Object temp2 = ' ';
		int count = 0;							//keeps track of how many elements are pushed into temp
		
		for (int i = 0; i < siteVisited; i++)
		{
			if (!url.equals(chronological.peek()))
			{
				temp.push(chronological.pop());
				count++;
			}
			else
			{
				temp2 = chronological.pop();
				break;						//break out of the loop
			}
		}
		
		for (int i = 0; i < count; i++)		//move the elements in temp back into the stack
			chronological.push(temp.pop());
		chronological.push(temp2);
	}
	
	//if url has been visited by user before, move the url to the end of the queue
	private void updateQueue(String url)
	{
		Queue temp = new Queue(SIZE);
		Object temp2 = ' ';
		Object target = ' ';
		
		for (int i = 0; i < siteVisited; i++)
		{
			temp2 = rChronological.dequeue();
			if (!url.equals(temp2))
				temp.enqueue(temp2);
			else
				target = temp2;
		}
		
		for (int i = 0; i < siteVisited - 1; i++)
			rChronological.enqueue(temp.dequeue());
		rChronological.enqueue(target);
	}

	//if url has been visited by user before, update the number of times the site has been visited
	private void updateNumVisits(String url)
	{
		for(int i = 0; i < siteVisited; i++)
		{
			if (webVisited[0][i].equals(url))									//if the url is found in the array
			{
				webVisited[1][i] = (int)webVisited[1][i] + 1;					//increment the number of times user visited the site
				break;															//break out of the loop
			}
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException
	{
		DataAnalysis run = new DataAnalysis();
		run.mainMenu();
	}
}