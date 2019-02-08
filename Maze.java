import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


public class Maze {
   int row;
   int col;
   DisjSets d;
   ArrayList<cell> arr;//to load all the cells in the wall
   Maze m;

   public Maze(int row,int col){//constructing an maze with all the walls true initially
      this.row=row;
      this.col=col;
      d= new DisjSets(row * col);
      arr=new ArrayList<>();
      for(int i=0;i<row;i++){
         for(int j=0;j<col;j++){
            arr.add(new cell(i,j));
         }
      }
   }
   
   
   public void generate_maze(){
      while(d.find(0)!=d.find((row*col)-1)){//start from first cell and keep picking random cells and doing union as required untill the first cell and last cell belong to the same set
         int rand = ThreadLocalRandom.current().nextInt(0, arr.size());
         cell c1=arr.get(rand); 
         
         for(int i=0;i<arr.size();i++){//search for right and bottom neighbour of the randomly picked cell
         
         if(arr.get(i).x==(c1.x) && arr.get(i).y==(c1.y+1)){
         if(d.find(c1.x*col+c1.y+1)!=d.find(c1.x*col+c1.y)){//performing find on right neighbour to check if they belong to the same set
         d.union(d.find(c1.x*col+c1.y+1),d.find(c1.x*col+c1.y));//if sets are different then we union it
         arr.get(rand).right_wall=false;//set the right wall of the currently picked cell as false
               } 
         }
         if(arr.get(i).x==(c1.x+1) && arr.get(i).y==(c1.y)){
         if(d.find(c1.x*col+c1.y+col)!=d.find(c1.x*col+c1.y)){//performing find on bottom neighbour to check if they belong to the same set
         d.union(d.find(c1.x*col+c1.y+col),d.find(c1.x*col+c1.y));//if sets are different then we union it
         arr.get(rand).bottom_wall=false;//set the bottom wall of the currently picked cell as false
               }
         }
         
         
         }
      
      }
      
       
      
   }
   
   public void print_maze(){
      for (int i = 0; i < col; i++)
         System.out.print(" -");//top outer boundary
      System.out.println();
      int j;
      int k;
      for (j= 0; j < row; j++) {
         if (j== 0) System.out.print(' ');//leaving the staring point open
         else System.out.print('|');//making walls at the beginning of each row
         for (k = 0; k < col; k++) {
            if (arr.get(j*col + k).bottom_wall==true)
               System.out.print('_');//adding all bottom walls
            else
               System.out.print(' ');
         
            if (arr.get(j*col + k).right_wall==true)
               System.out.print('|');//addding all vertical walls
            else
               System.out.print(' ');
         }
         if(j!=(row-1)){
            System.out.print('|');//adding walls at the end of each row except the last row
         }
         System.out.println();
      }
      for(int m=0;m<col;m++)
         System.out.print(" -");//making the bottom outer boundary
   
   }
   
  private class cell{
      int x;// x-co-ordinate
      int y;// y-co-ordinate
      boolean right_wall;//right_wall of cell
      boolean bottom_wall;//bottom_wall cell
      cell(int x,int y){
         this.x=x;
         this.y=y;
         if(y==(col-1)){//right wall is false for the last column elements as its an outer boundary and will never be knocked down
            right_wall=false;
         }
         else{
            right_wall=true;
         }
      
         if(x==(row-1)){//bottom wall is false for the last column elements as its an outer boundary and will never be knocked down
            bottom_wall=false;
         }
         else{
            bottom_wall=true;
         }
      }
      
      
   
   }
   

   public static void main(String args[]){
      Scanner sc=new Scanner(System.in);
      int rowsize;
      int columnsize;
      System.out.println("Enter number of rows: ");
      rowsize=sc.nextInt();
      System.out.println ("Enter the number of columns: "); 
      columnsize=sc.nextInt();
      Maze m=new Maze(rowsize,columnsize);
      m.generate_maze();
      m.print_maze();
   }


}

