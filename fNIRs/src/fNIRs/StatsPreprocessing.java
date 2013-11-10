/******************************************************************************
 * File: StatsPreprocessing.java                                              *
 * Author: Nicholas Kolesar                                                   *
 * Hamilton College                                                           *
 * Fall 2013                                                                  *
 * Description:                                                               *
 ******************************************************************************/

package fNIRs;

// HASNEXTDOUBLE DOES DETECT INTEGERS IN THE FILE, IT JUST READS THEM AS LEGAL
//    DOUBLE VALUES
// FIND OUT MORE ABOUT THE STATIC KEYWORD...MAYBE ASK A PROFESSOR
// * C-x C-u to make region uppercase :D
// * nkolesar/AppData/Roaming/.emacs
// SEE ABOUT CATCHING ANY UNFORESEEN INPUTMISMATCHEXCEPTIONS FROM THE
//    SCANNER READS LATER
// the static keyword lets me instantiate GroupedChannels without having an 
//    instance of HelloWorldNick first...Object Oriented is hard....
// MAKE A NOTE SOMEWHERE APPROPRIATE THAT WE CALL THE SEQUENCE OF DATA
//    FROM AN FNIRS CHANNEL A CHANNEL
// CONVENTION FOR VALUES THAT SHOULD BE A SIZE_T = ?????


//import org.apache.commons.math3.stat.inference.*; // probably only need a
//    couple classes from here
import java.util.Scanner; // to read doubles simply
// I found out Locale is apparently for things like what to use for decimal
//    points, etc. e.g. the European style of writing U.S. "8.5" as "8,5" and
//    U.S "8,000" as "8.000"
import java.util.Locale; // for Scanner (defines character encoding)
import java.util.InputMismatchException;
import java.util.ArrayList; // like C++ Vector and like Java Vector, which is
                            //    apparently deprecated
import java.io.File; // included elsewhere in project?
import java.io.FileReader;  // for making the BufferedReader
import java.io.FileNotFoundException; // in case creating a FileReader fails
import java.io.BufferedReader; // for making the Scanner
//import java.io.IOException;
//import java.util.Collection; // for sillily general Group constructor
import java.util.List; // for generalized findNextOccurrence method

// import HelloWorldNick.GroupedChannels; // WUT

public class StatsPreprocessing {
    private static void error(String errMsg){
	System.out.println("Error: " + errMsg + ".");
	System.exit(1); // PROBABLY DON'T WANT THIS IN FINAL PROGRAM--
                        //    HANDLE THE EXCEPTION SOME OTHER WAY<
    }
    /* makeScanner
     * IN:  a File object
     * Throws a FileNotFoundException if the file does not exist.
     * OUT: a Scanner for the input File
     */
    private static Scanner makeScanner(File file) {
	FileReader reader;
	try { // the file may or may not exist at this point
	    reader = new FileReader(file);
	} catch (FileNotFoundException fnf_exception) {
	    // WE WILL PROBABLY WANT TO INTEGRATE THIS INTO THE GUI
	    // Print the name of the missing file:
	    error(fnf_exception.getMessage());
	    return null; // never executed
	}
	// create Scanner to read easily from the input files:
	Scanner s = new Scanner(new BufferedReader(reader));
	s.useLocale(Locale.US); // tell the scanner that numbers in the input 
                                //    are formatted with decimal points, not
                                //    commas, etc.
	return s;
    }
    public static class GroupedChannels { // BETTER NAME PROBABLY

	// PUT ANALYSIS FUNCTION IN THIS CLASS? GOTTA FIGURE OUT HOW TO RETURN
	//    SOMETHING FROM IT....

	/* GroupedChannels
	 * IN:  groupFile, A file specifying channel groupings for statistical 
	 *         analysis. Format is a string to name the grouping, then 
	 *         natural numbers identifying the channels in that group, 
	 *         terminated by the next grouping's name or EOF.
	 *      numChannels, the number of channels the Hb and HbO input files
	 *         will have (not counting the extra condition column)
	 * This constructor creates all the necessary Groups to read from an Hb
	 *    or HbO file and checks to see if any channels are specified to be
	 *    in multiple groups or not specified to be in a group.
	 */
	public GroupedChannels (int numChannels, File groups, File data) {
	    NumChannels = numChannels; // set number of channels in Hb/HbO
                                       //    file
	    GroupList = new ArrayList<Group>(); // initialize GroupList
	    makeGroups(groups);
	    readData(data);
	}
	/* makeGroups
	 * IN:  groupFile, A file specifying channel groupings for statistical 
	 *         analysis. Format is a string to name the grouping, then 
	 *         natural numbers identifying the channels in that group, 
	 *         terminated by the next grouping's name or EOF.
	 * Checks to see if any channels are specified to be in multiple groups 
	 *    or not specified to be in any group.
	 */
	private void makeGroups(File groupFile){
	    Scanner s = makeScanner(groupFile);
	    //int groupNum = 0; //to keep track of Group to put the chans in
	    String groupName;
	    ArrayList<Integer> channels = new ArrayList<Integer>();
	    while(s.hasNext()) { // checks to see there is a token in the input
		groupName = s.next(); // next() gets the next token 
		while (s.hasNextInt()) {
		    channels.add(s.nextInt());
		}
		GroupList.add(new Group(groupName, channels));
		channels.clear(); // empty list for next iteration
	    }
	    s.close(); // scanner must be closed to signal it's okay to 
                       //    close its underlying stream
	    checkForMissingChannels();
	}
	private void checkForMissingChannels(){ // ANOTHER GUI THING
	    ArrayList<Integer> missing = new ArrayList<Integer>();
	    ArrayList<Integer> duplicates = new ArrayList<Integer>();
	    for (int chan = 1; chan <= NumChannels; chan++) {
		int found = 0;  // number of the Groups that contain the channel
		for (Group g : GroupList) {
		    if (g.hasChannel(chan)){
			found++;
		    }
		}
		switch (found) {
		case 0: // channel is missing from all groups
		    missing.add(chan);
		    break;
		case 1: // all is well
		    break;
		default:
		    duplicates.add(chan);
		    break;
		}
	    }
	    if (!duplicates.isEmpty()) {
		// THIS IS STATISTICALLY PRETTY BAD UNLESS THEY'RE NOT COMPARING
		//    THOSE TWO GROUPS...WHICH IS TOTALLY POSSIBLE, I GUESS.
		//    MAYBE THIS IS BEYOND THE SCOPE OF OUR PROJECT TO CHECK?
		System.out.print("Warning! The following channel(s) are in " +
				 "multiple groups! Channel(s) ");
		// IT WOULD BE REALLY NICE OF US TO TELL THEM WHICH GROUPS THE
		//    CHANNELS ARE IN, TOO, BUT LET'S LEAVE THAT FOR LATER.
		for (int chan : duplicates) {
		    System.out.print(chan);
		    if (chan != duplicates.get(duplicates.size()-1))
			System.out.print(", ");
		}
		System.out.println(".");
	    }
	    if (!missing.isEmpty()) {
		System.out.print("Warning: the following channel(s) are not " +
				 "in any group: ");
		for (int chan : missing) {
		    System.out.print(chan);
		    if (chan != missing.get(missing.size()-1))
			System.out.print(", ");
		}
		System.out.println(".");
	    }
	}
	/* readData
	 * IN:  dataFile, a preprocessed Hb or HbO file
	 * Reads all the data from the file, averaging channels' values together
	 *    to produce group average value(s) for each row, which are stored
	 *    along with the condition that row was part of.
	 */
	public void readData(File dataFile) {
	    Scanner s = makeScanner(dataFile);
	    //int line = 1;
	    try { // in case the input doesn't match the specification
		while(s.hasNextDouble()){ //while there is another row of values
		    //System.out.println("Reading line " + line++);
		    Double[] values = new Double[NumChannels];
		    for (int channel = 0; channel < NumChannels; channel++){
			values[channel] = s.nextDouble(); // read all data
			//values.add(s.nextDouble()); // read all data
		    }
		    int condition = s.nextInt(); // read condition for this row
		    //System.out.print("Line: ");
		    //for (int channel = 0; channel < NumChannels; channel++){
		    //System.out.print(values[channel] + " ");
		    //}
		    //System.out.println();
		    //System.out.print("Sorting: ");
		    for (int channel = 1; channel <= NumChannels; channel++){
			//System.out.print(values[channel] + ", ");
			for (Group g : GroupList){
			    if (g.hasChannel(channel)){
				g.addValue(values[channel-1], condition);
			    }
			}
		    }
		    //System.out.print("\n");
		}
		//getGroup(channel).addValue(
	    } catch (InputMismatchException e) {
		error(e.getMessage());
	    } catch (Exception e){
		// System.out.println(e.getCause());
		error(e.getMessage());
	    } finally {
		s.close(); // scanner must be closed to signal it's okay to 
                           //    close its underlying stream
	    }
	}
	public ArrayList<ArrayList<TaggedDataSequence>>
	    getData(List<String> groups, List<Integer> conditions){
	    // AT CERTAIN POINTS, IT SEEMS LIKE I SHOULD BE USING A LINKED,
	    //    RATHER THAN ARRAY-BASED, STRUCTURE?

	    // Initialize two-dimensional array of tagged sequences:
	    ArrayList<ArrayList<TaggedDataSequence>> result =
		new ArrayList<ArrayList<TaggedDataSequence>>();

	    // Iterate through groups:
	    for (int i = 0; i < GroupList.size(); i++){
		Group group = GroupList.get(i);
		// Initialize the new one-dimensional array of tagged
		//    sequences:
		ArrayList<TaggedDataSequence> temp =
		    new ArrayList<TaggedDataSequence>();
		// Iterate through conditions:
		for (int j = 0; j < conditions.size(); j++){
		    int condition = conditions.get(j);
		    // get the data from the group for the given condition,
		    //    adding it to the temporary 1D result list
		    temp.add(new TaggedDataSequence(group.getName(),
						    condition,
						    group.getData(condition
								  )));
		}
		// put list of results for this group in the result 2D list
		result.add(temp); 
	    }
	    return result;
	}
	class TaggedDataSequence{
	    TaggedDataSequence(String name,
			       int condition,
			       ArrayList<Double> data){
		GroupName = name;
		Condition = condition;
		Data = data;
	    }
	    public String getGroup(){ return GroupName; }
	    public ArrayList<Double> getData(){	return Data; }
	    public int getCondition(){ return Condition; }
	    public void print(){
		System.out.println("Data from group \"" + getGroup() +
				 "\" under condition " + getCondition() + ":");
		System.out.println("\t Row   Value ");
		for (int i = 0; i < Data.size(); i++)
		    System.out.println("\t " + i + "\t " + Data.get(i));
	    }
	    private ArrayList<Double> Data;
	    private String GroupName;
	    private int Condition;
	}
	/* getGroup
	 * IN:  groupNum, the number of a group, starting from 1, in the order
	 *         the groups were specified
	 * OUT: a reference to the specified group
	 */
	public Group getGroup(int groupNum){
	    //System.out.println("Looking for: \"" + groupNum + "\"");
	    if (1 <= groupNum && groupNum <= GroupList.size())
		return GroupList.get(groupNum - 1);
	    else {
		error("Group number " + groupNum + " not found");
		return null; // never executed
	    }
	}
	/* getGroup
	 * IN:  name, the name of a channel grouping
	 * OUT: a reference to the specified group	 
	 */
	public Group getGroup(String name){
	    //System.out.println("Looking for: \"" + name + "\"");
	    for (Group g : GroupList) {
		//System.out.println("Comparing with: \"" + g.getName() + "\"");
		if (name.equals(g.getName())){
		    return g;
		}
	    }
	    error("Group \"" + name + "\" not found");
	    //System.out.println("Group \"" + name + "\" not found");
	    return null; // never executed
	}
	public void print(){
	    System.out.println("Groups:");
	    for (Group g : GroupList) {
		System.out.print("\t");
		g.printChannels();
		System.out.print("\t");		
		g.printData();
		//System.out.print("\t");
		g.printConditions();
		if (g != GroupList.get(GroupList.size()-1))
		    System.out.println("\t-----------------------------------");
	    }
	}
	// M-$ IS SPELLCHECK! WHO KNEW?!
	public void print(String groupName){
	    getGroup(groupName).print();
	}
	public void printChannels(){
	    System.out.println("Groups:");
	    for (Group g : GroupList) {
		System.out.print("\t");
		g.printChannels();
	    }
	}
	/*
	  public Group getGroup(int channel){
	  for (Group g: GroupList){
	  if (g.hasChannel(channel))
	  return g;
	  }
	  // CHANGE TO RAISE AN EXCEPTION LATER	    
	  System.out.println("invalid channel"); 
	  return null;
	  }
	*/
	private final int NumChannels;
	private ArrayList<Group> GroupList;
	private class Group {
	    //	    public Group(String name, Collection<Integer> channels){
	    public Group(String name, ArrayList<Integer> channels){	    
		Name = name;
		DataSequence = new ArrayList<Double>();
		Condition = new ArrayList<Integer>();
		//Channels = channels.toArray(); // THIS IS STARTING TO SEEM
		//    REALLY REALLY SILLY
		Channels = new ArrayList<Integer>(channels); // copy channels 
                                                             //    into Channels
		NumChannels = channels.size();
		Sum = 0;
		NumVals = 0;
	    }
	    /* addValue
	     * IN:  value, an Hb/HbO value to be added to the Group's sequence 
	     *         of averages
	     *      condition, the experimental condition associated with the
	     *         value
	     * Adds the value to the running sum for the current row, and, if
	     *    enough values have been added to account for all the channels
	     *    in the group, then they are averaged and the average value is
	     *    added to the sequence.
	     * OUT: If an average value was calculated, Sum is 0. Else it now
	     *         contains value.
	     *      If an average value was calcluated, NumVals is 0. Else it is
	     *         increased by 1 to account for the new value.
	     *      Condition's value for the current row is set to condition.
	     * Note: Sum is initialized to 0 in the constructor.
	     */
	    public void addValue(double value, int condition){ // BETTER NAME?
		assert(NumVals < NumChannels);
		if (NumVals == 0) // if this is the first value for the current 
		    Condition.add(condition); // row, then set row's condition
		//    number
		Sum += value; // add this value to the total
		NumVals++; // increase the count of values in the sum
		// if we now have enough data to add another average value...
		if(NumVals == NumChannels) { // ...to the time series,
		    //System.out.println("Averaging " + NumVals + " values, " + 
		    //                   "equal to " + Sum);
		    // then average & add it:
		    DataSequence.add(Sum / ((double) NumChannels));
		    Sum = 0; // reset Sum... 
		    NumVals = 0; // ...and the number of values in it
		}
	    }
	    public void print(){
		System.out.print("Group \"" + getName() +
				 "\" has channels: \t");
		printChannels();
		System.out.println("\t\tRow \tValue    Condition");
		for (int i = 0; i < DataSequence.size(); i++)
		    System.out.println("\t\t " + i +
				       "\t " + DataSequence.get(i) +
				       "\t  " + Condition.get(i));
	    }
	    /* printChannels
	     * IN: none
	     * OUT: prints the name of the group and its channels, which are
	     *         tabbed in so that groups' channels can easily be compared
	     *         when printed on separate lines
	     */
	    public void printChannels(){
		//System.out.print("Group \"" + getName() +
		//		 "\" has channels\t");
		for (int i = 0; i < this.getNumChannels(); i++){
		    System.out.print(Channels.get(i));
		    if (i < this.getNumChannels() - 1)
			System.out.print(" ");
		}
		System.out.println();
	    }
	    /* printData
	     * IN: none
	     * OUT: prints the name of the group followed by its entire data 
	     *         sequence, which begins tabbed in so that, for small test 
	     *         data sequences, groups' data sequences can easily be 
	     *         compared when printed on separate lines
	     */
	    public void printData(){
		//System.out.print("Group \"" + getName() +
		//		 "\" has data: \t");
		for (Double i : DataSequence){
		    System.out.print(i + " ");
		}
		System.out.println();
	    }
	    /* printConditions
	     * IN:  none
	     * OUT: prints the name of the group followed by its condition
	     *         sequence, which is preceded by a tab character so it
	     *         lines up with that of another group printed on a
	     *         separate line
	     */
	    public void printConditions(){
		//System.out.print("\tGroup \"" + getName() +
		//		 "\" conditions: \t");
		for (int c : Condition){
		    System.out.print(c + " ");
		}
		System.out.println();
		System.out.print("\t\t\t\t\t");
		//for (int c = 1; c <= 10; c++)
		//  System.out.print(c + " ");
		System.out.print("\n");
	    }
	    public String getName() {
		return Name;
	    }
	    public int getNumChannels() {
		return NumChannels;
	    }
	    /* hasChannel
	     * IN:  channel, an integer specifying an fNIRs device channel in
	     *         the input file (actually, could be any int)
	     * OUT: returns true iff the channel is in this channel grouping
	     */
	    public boolean hasChannel(int channel){
		return Channels.contains(channel);
	    }
	    /* getData
	     * IN:  condition, the number of the condition whose trials are to
	     *         be averaged
	     * OUT: returns an ArrayList containing the average of the first
	     *         rows of all occurrences of condition in the input, the
	     *         average of the second rows, etc., with the resulting
	     *         ArrayList only as long as the shortest occurrence. That
	     *         is, it returns a data sequence that is the average of all
	     *         the times the experimental condition occurred in the 
	     *         input sequence.
	     *      returns an empty ArrayList if there were no occurrences of
	     *         the condition in the original input Hb or HbO file
	     */
	    public ArrayList<Double> getData(int condition) {
		/*
		System.out.println("Getting data for condition " + condition +
				   " from group \"" + getName() + "\":");
		*/
		ArrayList<Double> avgSequence = new ArrayList<Double>();
		int numOccurrences = 0; // number of separate times we've found 
		//    the condition in the data sequence
		//    (for calculating the averages)
		int i = 0;
		while (true){
		    /*
		    System.out.print("avgSequence: "); 
		    for (Double d : avgSequence)
			System.out.print(d + " ");
		    System.out.println();
		    */
		    // length of current occurrence of condition:
		    int numRowsFound = 0; 
		    // find the starting index of the next occurrence of 
		    //    condition in the sequence:
		    //i = findNextOccurrence(Condition, i, condition);
		    /*
		    System.out.println("Java thinks next occurrence is: " +
				       findNextOccurrence(Condition,
							  i,
							  condition
							  )
				       );
		    System.out.println("looking for next occurrence after " +
				       i);
		    */
		    i = myFindNextOccurrence(i, condition);
		    /*
		    System.out.println("Next occurrence: " + i);
		    // If there are no more occurrences of this condition:
		    System.out.println(i==-1);
		    */
		    if (i == -1) { // -1 returned if not found
			// If we found at least one occurrence of the
			//    condition:
			if (!avgSequence.isEmpty())
			    // To average, divide the sum in each row by the
			    //    number of occurrences of the condition
			    //    that have been found:
			    for (int j = 0; j < avgSequence.size(); j++)
				avgSequence.set(j,(avgSequence.get(j) /
						   (double) numOccurrences));
			// return (possibly empty) avgSequence:			
			return avgSequence; 
		    }
		    do {
			/* System.out.println("Adding index: " + i); */
			// If this is the first occurrence of the condition,
			if (numOccurrences == 0) {
			    //System.out.println("if");			    
			    // add each row of this occurrence of the condition 
			    //    to the average by simply copying its value to 
			    //    the average sequence as a new element:
			    avgSequence.add(DataSequence.get(i));
			    // Else, if this occurrence of the condition is 
			    //    longer than the shortest one found so far,    
			} else if (numRowsFound >= avgSequence.size()){
			    //System.out.println("elif");
			    skip(i); // skip the rest of it...
			    break; // and find the next occurrence of the
                                   //    condition 
			} else {
			    //System.out.println("else");
			    // add each row of this occurrence of the condition
			    //    to the average by adding it to the running sum
			    //    contained in that row of the average sequence:
			    avgSequence.set(numRowsFound,
					    (avgSequence.get(numRowsFound) +
					     DataSequence.get(i)));
			}
			numRowsFound++;
			// move on to the next row until we reach the next
			//    condition or there are no more rows:
			i++;
		    } while ((i < Condition.size()) && 
			     (Condition.get(i) == condition)); 
		    // If this interval was shorter than the others:
		    if (numRowsFound < avgSequence.size())
			// shorten the average sequence to match this one's
			//    length (by removing the extra trailing elements):
			avgSequence.subList(numRowsFound,
					    avgSequence.size()).clear();
		    numOccurrences++;
		}
	    }
	    private int myFindNextOccurrence(int startIndex, int condition){
		if (startIndex >= Condition.size() - 1)
		    return -1;
		while (Condition.get(startIndex) != condition) {
		    if (startIndex >= Condition.size() - 1)
			return -1;
		    startIndex++;		    
		}
		return startIndex;
	    }
	    /* findNextOccurrence
	     * IN:  lst, a collection that implements the List interface (???)
	     *      startIndex, the first index in the List to check
	     *      value, the value to search lst for
	     * OUT: returns the first index, starting from startIndex, of value
	     *         in lst
	     *      returns -1 if value is not found in lst (because indexOf 
	     *         does)
	     */
	    private <T> int findNextOccurrence(List<T> lst,
					       int startIndex,
					       T value){
		// get the sub-list that we want to search for the value:
		List<T> toSearch = lst.subList(startIndex, lst.size());
		return startIndex + // since the array we're searching starts at
		                    //    this index in the whole array
		    toSearch.indexOf(value); // first index of value in toSearch
	    }
	    /* skip
	     * IN:  startIndex, the index of the condition to skip
	     * OUT: returns the index of the first row in the data sequence that
	     *         is not part of an occurrence of the condition
	     */
	    private int skip(int startIndex) {
		// store the condition to skip:
		int condition = Condition.get(startIndex);
		// increment the index until we pass the current condition:
		while (Condition.get(++startIndex) == condition);
		return startIndex;
	    }
	    private final String Name; 
	    //	    private final Object[] Channels; // DOES THIS EVEN MEAN THE 
	    //    ARRAY CAN'T BE CHANGED?
	    private final ArrayList<Integer> Channels;
	    private double Sum;
	    private ArrayList<Double> DataSequence; // contains actual "time series" data
	    private ArrayList<Integer> Condition; // stores a condition associated with every row of the time series
	    private final int NumChannels; 
	    private int NumVals;
	}
    }
    public static void print(List<?> lst){
	System.out.println("Index\t Value");
	for (int i = 0; i < lst.size(); i++) {
	    System.out.println(i + "\t " + lst.get(i));
	}
    }
    public static void main(String[] args) {
	// think about saving things so it will be quicker for the user
	// Prepare to read files: (the GUI will give these files somehow)
	//File dataFile = new File("/home/nkolesar/cs410-Senior-Seminar/Zombie_MiNIR/linux-version/testHb");
	File dataFile = new File("C:/Users/nkolesar/Desktop/sub19/testHb");
	int numChannels = 8; // number of channels in the input file (GUI)	
	//File groupFile = new File("/home/nkolesar/cs410-Senior-Seminar/Zombie_MiNIR/linux-version/groups");
	File groupFile = new File("C:/Users/nkolesar/Desktop/sub19/groups");
	
	//--------------------------------------------------------------------//
	//                  CLASS TESTING: Groups classes                     //
	//--------------------------------------------------------------------//
	System.out.println("Reading group information.");
	// read channel grouping information:
	GroupedChannels groups = new GroupedChannels(numChannels,
						     groupFile,
						     dataFile);
	//groups.printChannels();
	System.out.println("-------------------------------------------------");

	groups.print("first");
	int cond = 1;
	System.out.println("Average sequence for condition " + cond + " is:");
	print(groups.getGroup("first").getData(cond));

	System.out.println("-------------------------------------------------");

	groups.print("second");
	System.out.println("Average sequence for condition " + cond + " is:");	
	groups.getGroup("second").getData(1);

	System.out.println("-------------------------------------------------");

	groups.print("all_chans");
	System.out.println("Average sequence for condition " + cond + " is:");	
	groups.getGroup("all_chans").getData(1);

	System.out.println("-------------------------------------------------");
		
	System.out.println("Done!");

	System.out.println("-------------------------------------------------");

	ArrayList<String> groupNames = new ArrayList<String>();
	groupNames.add("first");
	groupNames.add("second");
	groupNames.add("all_chans");

	ArrayList<Integer> conditions = new ArrayList<Integer>();
	conditions.add(1);
	conditions.add(2);
	conditions.add(3);
	
	ArrayList<ArrayList<GroupedChannels.TaggedDataSequence>> output =
	    new ArrayList<ArrayList<GroupedChannels.TaggedDataSequence>>();
	
	output = groups.getData(groupNames, conditions);

	for (ArrayList<GroupedChannels.TaggedDataSequence> ary :
		 output) {
	    for (GroupedChannels.TaggedDataSequence data : ary){
		data.print();
	    }
	}
	    
	
	//--------------------------------------------------------------------//
	//                  DONE TESTING CLASS: Groups classes                //
	//--------------------------------------------------------------------//
	System.exit(0);
    }
}
