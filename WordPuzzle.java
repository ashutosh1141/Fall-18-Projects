import java.util.*;
import java.io.*;
class WordPuzzle{
static int max_word_length;

   public static char[][] gnerateGrid(int rowsize,int colsize){
      char[][] charr=new char[rowsize][colsize];
      Random r = new Random();
      for (int i = 0; i <rowsize ;  i++){
         for (int j = 0; j<colsize; j++){
            charr[i][j] =(char)(r.nextInt(26) + 'a');
         }
      }
      return charr; 
   }
   
   public static ArrayList<String> SolvePuzzle_efficiently(char[][] charr,MyHashTable<String> H){//solve the puzzle with enhancement
      ArrayList<String> wordlst=new ArrayList<String>();
      ArrayList<ArrayList<String>> rowwordsfianl_eff=rowwords_eff(charr,H);
      List<String> rowresult = new ArrayList<String>();
      for (List<String> list : rowwordsfianl_eff) {
         rowresult.addAll(list);
      }
      System.out.println("printing row elements");
      for(int q=0;q<rowresult.size();q++){
         System.out.println(rowresult.get(q));
      }
   
      ArrayList<ArrayList<String>> columnwrdsfinal_eff=columnwords_eff(charr,H);
      List<String> colresult = new ArrayList<String>();
      for (List<String> list : columnwrdsfinal_eff) {
         colresult.addAll(list);
      }
      System.out.println("printing column elements");
      for(int q=0;q<colresult.size();q++){
         System.out.println(colresult.get(q));
      }
   
      ArrayList<ArrayList<String>> eff_right_daig=scandaigonalsfromright_eff(charr,H);
      ArrayList<String> rightresult = new ArrayList<String>();
      for (List<String> list : eff_right_daig) {
         rightresult.addAll(list);
      }
      System.out.println("printing right daigonals");
      for(int q=0;q<rightresult.size();q++){
         System.out.println(rightresult.get(q));
      }
   
      ArrayList<ArrayList<String>> eff_left_daig=scandaigonalsfromleft_eff(charr,H);
      ArrayList<String> leftresult = new ArrayList<String>();
      for (List<String> list : eff_left_daig) {
         leftresult.addAll(list);
      }
      System.out.println("printing left daigonals");
      for(int q=0;q<leftresult.size();q++){
         System.out.println(leftresult.get(q));
      }
   
      wordlst.addAll(rowresult);
      wordlst.addAll(leftresult);
      wordlst.addAll(rightresult);
      wordlst.addAll(colresult);
   
      ArrayList<String> finallistofwords=removeDuplicates(wordlst);
   
      return finallistofwords;
   }
   
   public static ArrayList<ArrayList<String>> rowwords_eff(char[][] arr,MyHashTable<String> H){
      ArrayList<ArrayList<String>> columnwordsfnl=new ArrayList<ArrayList<String>>();
      for(int i=0;i<=(arr.length-1);i++){
         for(int j=0;j<=(arr[0].length-1);j++){
            columnwordsfnl.add(rowright_eff(i,j,arr,max_word_length,H));//words from left to right of the row called for each character in the grid with max size of a word found from dictionary
         }
      }
   
      for(int i=0;i<=(arr.length-1);i++){
         for(int j=0;j<=(arr[0].length-1);j++){
            columnwordsfnl.add(rowleft_eff(i,j,arr,max_word_length,H));//words from right to left of the row called for each character in the grid with max size of a word found from dictionary

         }
      }
      return columnwordsfnl; 
   } 
   
   public static ArrayList<String> rowright_eff(int row,int col,char[][] arr,int size,MyHashTable<String> H){
      ArrayList<String> rowright=new ArrayList<String>();
      String s="";
      while(row>=0 && row<=(arr.length-1) && col>=0 && col<=(arr[0].length-1) && s.length()<=size){
         s+=arr[row][col];
         if(H.contains(s)){
            col++;
            if(H.isWord(s)){//check whether string is a prefix or a word
               rowright.add(s);
            }
         }
         else{
            break;//exiting loop if prefixes in the hashtable not found in the direction
         }
      }
      return rowright; 
   }
   
   public static ArrayList<String> rowleft_eff(int row,int col,char[][] arr,int size,MyHashTable<String> H){
      ArrayList<String> rowleft=new ArrayList<String>();
      String s="";
      while(row>=0 && row<=(arr.length-1) && col>=0 && col<=(arr[0].length-1) && s.length()<=size){
         s+=arr[row][col];
         if((H.contains(s))){
            col--;
            if((H.isWord(s))){
               rowleft.add(s);
            }
         }
         else{
            break;
         }
      }
      return rowleft; 
   } 

   
   
   
   public static ArrayList<ArrayList<String>> columnwords_eff(char[][] arr,MyHashTable<String> H){
   ArrayList<ArrayList<String>> columnwordsfnl=new ArrayList<ArrayList<String>>();
      for(int i=0;i<=(arr.length-1);i++){
         for(int j=0;j<=(arr[0].length-1);j++){
            columnwordsfnl.add(verticaldowndir_eff(i,j,arr,max_word_length,H));//check words vertically down called for each character in the grid with max size of a word found from dictionary
         }
      }
   
      for(int i=0;i<=(arr.length-1);i++){
         for(int j=0;j<=(arr[0].length-1);j++){
            columnwordsfnl.add(verticalupdir_eff(i,j,arr,max_word_length,H));//check words vertically up called for each character in the grid with max size of a word found from dictionary
         }
      }
      return columnwordsfnl; 
   } 
   
   public static ArrayList<String> verticaldowndir_eff(int row,int col,char[][] arr,int size,MyHashTable<String> H){
      ArrayList<String> verticaldown=new ArrayList<String>();
      String s="";
      while(row>=0 && row<=(arr.length-1) && col>=0 && col<=(arr[0].length-1) && s.length()<=size){
         s+=arr[row][col];
         if(H.contains(s)){
            row++;
            if((H.isWord(s))){
               verticaldown.add(s);
            }
         }
         else{
            break;
         }
      }
      return verticaldown; 
   }
   
   public static ArrayList<String> verticalupdir_eff(int row,int col,char[][] arr,int size,MyHashTable<String> H){
      ArrayList<String> verticalup=new ArrayList<String>();
      String s="";
      while(row>=0 && row<=(arr.length-1) && col>=0 && col<=(arr[0].length-1) && s.length()<=size){
         s+=arr[row][col];
         if(H.contains(s)){
            row--;
            if((H.isWord(s))){
               verticalup.add(s);
            }
         }
         else{
            break;
         }
      }
      return verticalup; 
   } 
 

   
   public static ArrayList<ArrayList<String>> scandaigonalsfromright_eff(char[][] arr,MyHashTable<String> H){
      ArrayList<String> lst1=new ArrayList<String>();
      ArrayList<String> lst2=new ArrayList<String>();
      ArrayList<ArrayList<String>> fnlrtdaig=new ArrayList<ArrayList<String>>();
      int columns=arr[0].length;
      int rows=arr.length;
      for(int i=0;i<rows;i++){
         for(int j=0;j<columns;j++){
            fnlrtdaig.add(scanrighttoleftdaigonaldown_eff(i,j,arr,H,max_word_length));//scan right daigonal in downward direction called for each character in the grid with max size of a word found from dictionary
         }
      }
      for(int i=0;i<rows;i++){
         for(int j=0;j<columns;j++){
            fnlrtdaig.add(scanrighttoleftdaigonalup_eff(i,j,arr,H,max_word_length));//scan right daigonal in upward direction called for each character in the grid with max size of a word found from dictionary
         }
      }
   
       
      return fnlrtdaig; 
   }

   public static ArrayList<String> scanrighttoleftdaigonaldown_eff(int row,int col,char[][] arr,MyHashTable<String> H,int size){
      ArrayList<String> righttoleftdaigdownwards=new ArrayList<String>(); 
      String s="";
      while(row<=(arr.length-1) && col>=0 && s.length()<=size){
         s+=arr[row][col];
         if(H.contains(s)){
            row++;
            col--;
            if((H.isWord(s))){
               righttoleftdaigdownwards.add(s);
            }
         }
         else{
            break;
         }
      }
      return righttoleftdaigdownwards; 
   }
   
   public static ArrayList<String> scanrighttoleftdaigonalup_eff(int row,int col,char[][] arr,MyHashTable<String> H,int size){
      ArrayList<String> righttoleftdaigupwards=new ArrayList<String>(); 
      String s="";
      while(row>=0 && col<=(arr[0].length-1) && s.length()<=size){
         s+=arr[row][col];
         if(H.contains(s)){
            row--;
            col++;
            if((H.isWord(s))){
               righttoleftdaigupwards.add(s);
            }
         }
         else{
            break;
         }
      }
      return righttoleftdaigupwards;
   }
   
   public static ArrayList<ArrayList<String>> scandaigonalsfromleft_eff(char[][] arr,MyHashTable<String> H){
      
      ArrayList<String> lst1=new ArrayList<String>();
      ArrayList<String> lst2=new ArrayList<String>();
      ArrayList<ArrayList<String>> fnlrtdaig=new ArrayList<ArrayList<String>>();
      int columns=arr[0].length;
      int rows=arr.length;
      for(int i=0;i<rows;i++){
         for(int j=0;j<columns;j++){
            fnlrtdaig.add(scanleftdaigonaldown_eff(i,j,arr,H,max_word_length));//scan left daigonal in downward direction called for each character in the grid with max size of a word found from dictionary
         }
      }
      for(int i=0;i<rows;i++){
         for(int j=0;j<columns;j++){
            fnlrtdaig.add(scanleftdaigonalup_eff(i,j,arr,H,max_word_length));//scan left daigonal in downward direction called for each character in the grid with max size of a word found from dictionary
         }
      }
   
       
      return fnlrtdaig; 
   }

   public static ArrayList<String> scanleftdaigonaldown_eff(int row,int col,char[][] arr,MyHashTable<String> H,int size){
      ArrayList<String> righttoleftdaigdownwards=new ArrayList<String>(); 
      String s="";
      while(row<=(arr.length-1) && col<=(arr[0].length-1) && s.length()<=size){//s.length()<=size checks for scan length and does not scan beyond max word length possible
         s+=arr[row][col];
         if(H.contains(s)){
            row++;
            col++;
            if((H.isWord(s))){
               righttoleftdaigdownwards.add(s);
            }
         }
         else{
            break;
         }
      }
      return righttoleftdaigdownwards; 
   }
   
   public static ArrayList<String> scanleftdaigonalup_eff(int row,int col,char[][] arr,MyHashTable<String> H,int size){
      ArrayList<String> righttoleftdaigupwards=new ArrayList<String>(); 
      String s="";
      while(row>=0 && col>=0 && s.length()<=size){
         s+=arr[row][col];
         if(H.contains(s)){
            row--;
            col--;
            if((H.isWord(s))){
               righttoleftdaigupwards.add(s);
            }
         }
         else{
            break;
         }
      }
      return righttoleftdaigupwards;
   }

   
   
   public static ArrayList<String> SolvePuzzle(char[][] charr,MyHashTable<String> H){//solving the puzzle without enhancement
   
      ArrayList<String> wordlst=new ArrayList<String>();
      ArrayList<ArrayList<String>> rowwordsfianl=rowwords(charr,H);
      List<String> rowresult = new ArrayList<String>();
      for (List<String> list : rowwordsfianl) {
         rowresult.addAll(list);
      }
      System.out.println("printing row elements");
      for(int q=0;q<rowresult.size();q++){
         System.out.println(rowresult.get(q));
      }
   
   
      ArrayList<ArrayList<String>> columnwrdsfinal=columnwords(charr,H);
      List<String> colresult = new ArrayList<String>();
      for (List<String> list : columnwrdsfinal) {
         colresult.addAll(list);
      }
      System.out.println("printing column elements");
      for(int q=0;q<colresult.size();q++){
         System.out.println(colresult.get(q));
      }
   
   
      ArrayList<ArrayList<String>> rightdaigwordsfinal=scandaigonalsfromright(charr,H);
      List<String> rightresult = new ArrayList<String>();
      for (List<String> list : rightdaigwordsfinal) {
         rightresult.addAll(list);
      }
      System.out.println("printing right daigonals");
      for(int q=0;q<rightresult.size();q++){
         System.out.println(rightresult.get(q));
      }
   
   
      ArrayList<ArrayList<String>> leftdaigonalwordsfinal=scandaiglefttoright(charr,H);
      List<String> leftresult = new ArrayList<String>();
      for (List<String> list : leftdaigonalwordsfinal) {
         leftresult.addAll(list);
      }
      System.out.println("printing left daigonals");
      for(int q=0;q<leftresult.size();q++){
         System.out.println(leftresult.get(q));
      }
   
      wordlst.addAll(rowresult);
      wordlst.addAll(leftresult);
      wordlst.addAll(rightresult);
      wordlst.addAll(colresult);
   
      ArrayList<String> finallistofwords=removeDuplicates(wordlst);
   
      return finallistofwords;
   }
   
   public static ArrayList<String> removeDuplicates(ArrayList<String> arr){//remove duplicates from the final list of words
      Set<String> hs = new HashSet<>();
      hs.addAll(arr);
      arr.clear();
      arr.addAll(hs);
   
      return arr;
   }
   
   public static ArrayList<ArrayList<String>> rowwords(char[][] arr,MyHashTable<String> H){
      
      ArrayList<ArrayList<String>> rowwordsfnl=new ArrayList<ArrayList<String>>();
      for(int i=0;i<=(arr.length-1);i++){
         for(int j=0;j<=(arr[0].length-1);j++){
            rowwordsfnl.add(rightrow(i,j,arr,max_word_length,H));
         }
      }
   
      for(int i=0;i<=(arr.length-1);i++){
         for(int j=0;j<=(arr[0].length-1);j++){
            rowwordsfnl.add(leftrow(i,j,arr,max_word_length,H));
         }
      }
      return rowwordsfnl; 
   } 
   
   public static ArrayList<String> rightrow(int row,int col,char[][] arr,int size,MyHashTable<String> H){
      ArrayList<String> right_row=new ArrayList<String>();
      String s="";
      while(row>=0 && row<=(arr.length-1) && col>=0 && col<=(arr[0].length-1) && s.length()<=size){
         s+=arr[row][col];
         col++;
         if(H.contains(s)){
            right_row.add(s);
         }
      }
      return right_row; 
   }
   
   public static ArrayList<String> leftrow(int row,int col,char[][] arr,int size,MyHashTable<String> H){
      ArrayList<String> lft_row=new ArrayList<String>();
      String s="";
      while(row>=0 && row<=(arr.length-1) && col>=0 && col<=(arr[0].length-1) && s.length()<=size){
         s+=arr[row][col];
         col--;
         if(H.contains(s)){
            lft_row.add(s);
         }
      }
      return lft_row; 
   } 

   
      
   public static ArrayList<ArrayList<String>> columnwords(char[][] arr,MyHashTable<String> H){
      
      ArrayList<ArrayList<String>> columnwordsfnl=new ArrayList<ArrayList<String>>();
      for(int i=0;i<=(arr.length-1);i++){
         for(int j=0;j<=(arr[0].length-1);j++){
            columnwordsfnl.add(verticaldowndir(i,j,arr,max_word_length,H));
         }
      }
   
      for(int i=0;i<=(arr.length-1);i++){
         for(int j=0;j<=(arr[0].length-1);j++){
            columnwordsfnl.add(verticalupdir(i,j,arr,max_word_length,H));
         }
      }
      return columnwordsfnl; 
   } 
   
   public static ArrayList<String> verticaldowndir(int row,int col,char[][] arr,int size,MyHashTable<String> H){
      ArrayList<String> verticaldown=new ArrayList<String>();
      String s="";
      while(row>=0 && row<=(arr.length-1) && col>=0 && col<=(arr[0].length-1) && s.length()<=size){
         s+=arr[row][col];
         row++;
         if(H.contains(s)){
            verticaldown.add(s);
         }
      }
      return verticaldown; 
   }
   
   public static ArrayList<String> verticalupdir(int row,int col,char[][] arr,int size,MyHashTable<String> H){
      ArrayList<String> verticalup=new ArrayList<String>();
      String s="";
      while(row>=0 && row<=(arr.length-1) && col>=0 && col<=(arr[0].length-1) && s.length()<=size){
         s+=arr[row][col];
         row--;
         if(H.contains(s)){
            verticalup.add(s);
         }
      }
      return verticalup; 
   } 
 
      
         
   public static ArrayList<ArrayList<String>> scandaigonalsfromright(char[][] arr,MyHashTable<String> H){
      
      ArrayList<String> lst1=new ArrayList<String>();
      ArrayList<String> lst2=new ArrayList<String>();
      ArrayList<ArrayList<String>> fnlrtdaig=new ArrayList<ArrayList<String>>();
      int columns=arr[0].length;
      int rows=arr.length;
      for(int i=0;i<rows;i++){
         for(int j=0;j<columns;j++){
            fnlrtdaig.add(scanrighttoleftdaigonaldown(i,j,arr,H,max_word_length));
         }
      }
      for(int i=0;i<rows;i++){
         for(int j=0;j<columns;j++){
            fnlrtdaig.add(scanrighttoleftdaigonalup(i,j,arr,H,max_word_length));
         }
      }
   
       
      return fnlrtdaig; 
   }
   
   public static ArrayList<String> scanrighttoleftdaigonaldown(int row,int col,char[][] arr,MyHashTable<String> H,int size){
      ArrayList<String> righttoleftdaigdownwards=new ArrayList<String>(); 
      String s="";
      while(row<=(arr.length-1) && col>=0 && s.length()<=size){
         s+=arr[row][col];
         if(H.contains(s)){
            righttoleftdaigdownwards.add(s);
         }
         row++;
         col--;
      }
      return righttoleftdaigdownwards; 
   }
   
   public static ArrayList<String> scanrighttoleftdaigonalup(int row,int col,char[][] arr,MyHashTable<String> H,int size){
      ArrayList<String> righttoleftdaigupwards=new ArrayList<String>(); 
      String s="";
      while(row>=0 && col<=(arr[0].length-1) && s.length()<=size){
         s+=arr[row][col];
         if(H.contains(s)){
            righttoleftdaigupwards.add(s);
         }
         row--;
         col++;
      }
      return righttoleftdaigupwards;
   }
   
   public static ArrayList<ArrayList<String>> scandaiglefttoright(char[][] arr,MyHashTable<String> H){
      
      ArrayList<String> lst3=new ArrayList<String>();
      ArrayList<String> lst4=new ArrayList<String>();
      ArrayList<ArrayList<String>> fnllftdaig=new ArrayList<ArrayList<String>>();
      int columns=arr[0].length;
      int rows=arr.length;
      for(int i=0;i<rows;i++){
         for(int j=0;j<columns;j++){
            fnllftdaig.add(scanlefttorightdaigdown(i,j,arr,H,max_word_length));
         }
      }
      for(int i=0;i<rows;i++){
         for(int j=0;j<columns;j++){
            fnllftdaig.add(scanlefttorightdaigup(i,j,arr,H,max_word_length));
         }
      }
   
       
      return fnllftdaig; 
   }

   public static ArrayList<String> scanlefttorightdaigdown(int row,int col,char[][] arr,MyHashTable<String> H,int size){
      ArrayList<String> lefttorightdaigdown=new ArrayList<String>(); 
      String s="";
      while(row<=(arr.length-1) && col<=(arr[0].length-1) && s.length()<=size){
         s+=arr[row][col];
         if(H.contains(s)){
            lefttorightdaigdown.add(s);
         }
         row++;
         col++;
      }
      return lefttorightdaigdown; 
   }
   
   public static ArrayList<String> scanlefttorightdaigup(int row,int col,char[][] arr,MyHashTable<String> H,int size){
      ArrayList<String> lefttorightdaigup=new ArrayList<String>(); 
      String s="";
      while(row>=0 && col>=0 && s.length()<=size){
         s+=arr[row][col];
         if(H.contains(s)){
            lefttorightdaigup.add(s);
         }
         row--;
         col--;
      }
      return lefttorightdaigup; 
   }

   public static void main(String[] args) throws Exception{
      Scanner sc=new Scanner(System.in);
      int rowsize;
      int columnsize;
      System.out.println("Enter number of rows: ");
      rowsize=sc.nextInt();
      System.out.println ("Now enter the number of columns: "); 
      columnsize=sc.nextInt();
      if(rowsize==0 && columnsize==0){
      System.out.println("Empty characcter array");
      System.exit(0);
      } 
      char[][] randarr=gnerateGrid(rowsize,columnsize);
      System.out.println ("Printing Random Grid elements");
      for (int k=0; k<rowsize; k++){
         for (int l = 0; l<columnsize; l++){
            System.out.print(randarr[k][l]+" ");
         }
         System.out.println();
      
      }
      ArrayList<String> arr=new ArrayList<String>();//scanning dictionary and adding to a list for hashing
      File file=new File("dictionary.txt");
      Scanner scf = new Scanner(file);
      while(scf.hasNextLine()){
         arr.add(scf.nextLine());
      }
      MyHashTable<String> H = new MyHashTable<>(2*arr.size());//creating a hash table double the size of list to avoid collisions,lambda for linear probing should be 0.5 or less
      for(int i=0;i<arr.size();i++){
         H.insert(arr.get(i));
      }
      int max_length=0;
      File file1=new File("dictionary.txt");
      Scanner scf1 = new Scanner(file1);
      
      MyHashTable<String> H2 = new MyHashTable<>(2*arr.size());//making a second hash table to solve puzzle effieciently
      while(scf1.hasNextLine()){
         String word=scf1.nextLine();
         int length_of_string=word.length();
            if(length_of_string>max_length){//determining the largest word in the dictionary
            max_length=length_of_string;
            }

         char[] charr=word.toCharArray(); //converting the words into a char array to scan all prefixes
         String scan="";
         for(int i=0;i<charr.length;i++){
            scan+=charr[i];
            if(!H2.contains(scan) && scan.length()<charr.length){//inserting the prefix with flag false so that it is not considered a word and avoiding duplicates by condition checking
               H2.insert(scan,false);
            }
            else if(!H2.contains(scan) && scan.length()==charr.length){//inserting the actual word with flag true and avoiding duplicates by condition checking
               H2.insert(scan,true);            
               }
         }
      
      }
      max_word_length=max_length;
    
      System.out.println("solving puzzle bruteforce");
      long startTime =System.nanoTime();
      ArrayList<String> wordlist=SolvePuzzle(randarr,H);//algorithm without enhancement
      long endTime = System.nanoTime();
      long executiontime=endTime-startTime;
   
      System.out.println("solving puzzle efficiently");
      long startTime_eff =System.nanoTime();
      ArrayList<String> wordlist_eff=SolvePuzzle_efficiently(randarr,H2);//algorithm with enhancement
      long endTime_eff = System.nanoTime();
      long executiontime_eff=endTime_eff-startTime_eff;
   
   
      System.out.println("Printing final list of lesser efficient algorithm words");//printing final list of searched words
      for(int k=0;k<wordlist.size();k++){
         System.out.println(wordlist.get(k));
      }
   
      System.out.println("Printing final list of efficient algorithm words");//printing final list of searched words
      for(int k=0;k<wordlist_eff.size();k++){
         System.out.println(wordlist_eff.get(k));
      }
      
    Collections.sort(wordlist);
    Collections.sort(wordlist_eff);      
    boolean is_final_list_equal=wordlist.equals(wordlist_eff);//comapring if both the algorithms solve it exactly same by comparing the list of words
    System.out.println("The final list of words are equal in both algorithms: "+is_final_list_equal);
      
      System.out.println("The elapsed time without enhancement is:"+executiontime);//execution time of non-effiecient
      System.out.println("The elapsed time with enhancement is:"+executiontime_eff);//execution time of efficient one
      
      
      
   
   }





}