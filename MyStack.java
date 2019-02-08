import java.util.ArrayList;


public class MyStack<AnyType>  {

   ArrayList<AnyType> arrlist;

   public MyStack(){
      arrlist=new ArrayList<AnyType>();
   }

   public void push(AnyType x){
      try{
         arrlist.add(x);
      }
      catch(Exception e){
         e.printStackTrace();
      }
   }

   public void pop(){
      if(!arrlist.isEmpty())
         arrlist.remove(arrlist.size()-1);
      else
         throw new IllegalStateException();
   		
   }

   public AnyType top(){
      return arrlist.get(arrlist.size()-1);
   }

   public boolean isempty(){
      return arrlist.size()==0;
   }



   public void display(){
      System.out.println(arrlist.toString());
   }
   public static void main(String[] args) {
      MyStack<Character> stack=new MyStack<Character>();
      String balanceexpression="({[";
      for(int i=0;i<balanceexpression.length();i++){
         char ch=balanceexpression.charAt(i);
         if(ch=='['||ch=='('||ch=='{'){
            stack.push(ch);
            stack.display();
         }
      }
      for(int i=0;i<balanceexpression.length();i++){
         char ch=balanceexpression.charAt(i);
         if(ch==']'||ch==')'||ch=='}'){
         	
            if(ch==']' && !stack.isempty() && stack.top()=='['){
               stack.pop(); 
               stack.display();
            }
            else if(ch==')' && !stack.isempty() && stack.top()=='('){
               stack.pop();
               stack.display();
            }
            else if(ch=='}' && !stack.isempty() && stack.top()=='{'){
               stack.pop();	
               stack.display();
            }
            else if(stack.isempty()){
               System.out.println("The Expression is not balanced as there are no opening symbols");
               stack.display();
               break;
            }
            else{
               System.out.println("The Expression is not balanced");
               break;
            }
         
         }
      }
      if(stack.isempty()){
         System.out.println("The Expression is balanced");
      }
      else{
         System.out.println("The Expression is not balanced as it contains only opening symbols");	     
      }
   	
   
   }

}
class testmystack extends MyStack{
public static void main(String args[]){

testmystack t1=new testmystack();
t1.

}


}