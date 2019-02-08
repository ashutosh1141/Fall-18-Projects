/**
 * LinkedList class implements a doubly-linked list.
 */
public class MyLinkedList<AnyType> implements Iterable<AnyType>
{
    /**
     * Construct an empty LinkedList.
     */
   public MyLinkedList( )
   {
      doClear( );
   }
    
   private void clear( )
   {
      doClear( );
   }
    
    /**
     * Change the size of this collection to zero.
     */
   public void doClear( )
   {
      beginMarker = new Node<>( null, null, null );
      endMarker = new Node<>( null, beginMarker, null );
      beginMarker.next = endMarker;
        
      theSize = 0;
      modCount++;
   }
    
    /**
     * Returns the number of items in this collection.
     * @return the number of items in this collection.
     */
   public int size( )
   {
      return theSize;
   }
    
   public boolean isEmpty( )
   {
      return size( ) == 0;
   }
    
    /**
     * Adds an item to this collection, at the end.
     * @param x any object.
     * @return true.
     */
   public boolean add( AnyType x )
   {
      add( size( ), x );   
      return true;         
   }
    
    /**
    *swaps the nodes at two positions by changing the links 
    *provided both positions are within the current size
    *@receives two index positions as parameters 
    *solution 1(a) 
    */
   public void swap(int idx1,int idx2){
    
      if(idx1>=0 && idx1<=(size()-1) && idx2>=0 && idx2<=(size()-1)){//checking if the indices are within the bounds
      
         Node<AnyType> p1 = getNode( idx1 );
         Node<AnyType> p2 = getNode( idx2 );
       
         if(Math.abs(idx1-idx2)==1){//adjacent nodes
         
            if(idx2>idx1){// idx2 passed greater than idx1,something like swap(1,2)
               p1.next = p2.next;
               p2.prev = p1.prev;
               p1.next.prev = p1;
               p2.prev.next = p2;
               p2.next = p1;
               p1.prev = p2;
            }
            else{//idx2 passed less than idx1,something like swap(3,2)
               p2.next = p1.next;
               p1.prev=p2.prev;
               p2.next.prev=p2;
               p1.prev.next=p1;
               p1.next=p2;
               p2.prev=p1;
            }  
         
         }
         
         else{//swapping any two random nodes
         
            Node<AnyType> temp1=p2.next;
            Node<AnyType> temp2=p2.prev;
            p2.next=p1.next;
            p2.prev=p1.prev;
            p2.prev.next=p2;
            p2.next.prev=p2;
            p1.next=temp1;
            p1.prev=temp2;
            p1.prev.next=p1;
            p1.next.prev=p1;
         
                
         }
      }
      else{//index is out of bounds
          
         throw new IndexOutOfBoundsException( "index 1: " + idx1 + " index 2: " + idx2 + "; size: " + size( ) );
      
      }
   }


  /**
  *receives an integer (positive or negative) and shifts the list this
  * many positions forward (if positive) or backward (if negative).  
  *1,2,3,4    shifted +2    3,4,1,2
  *1,2,3,4    shifted -1    4,1,2,3
  *solution 1(b) 
     */
     
   public void shift(int shiftcount){
   
      if(shiftcount>0){//right shift
      
         for(int i=0;i<shiftcount;i++){
         
            Node<AnyType> firstelement=getNode(0);
            Node<AnyType> lastelement=getNode(size()-1);
            Node<AnyType> lastbutoneelement=getNode(size()-2);
            Node<AnyType> temp1=lastelement.next;
            lastelement.prev=firstelement.prev;
            lastelement.next=firstelement;
            lastelement.prev.next=lastelement;
            lastelement.next.prev=lastelement;
            lastbutoneelement.next=temp1;
            lastbutoneelement.next.prev=lastbutoneelement;
         
         }
      
      }
      else{//left shift
      
         for(int i=0;i<java.lang.Math.abs(shiftcount);i++){ 
         
            Node<AnyType> firstelement=getNode(0);
            Node<AnyType> secondelement=getNode(1);
            Node<AnyType> lastelement=getNode(size()-1);
            Node<AnyType> temp1=firstelement.prev;
            firstelement.next=lastelement.next;
            firstelement.prev=lastelement;
            firstelement.next.prev=firstelement;
            firstelement.prev.next=firstelement;
            secondelement.prev=temp1;
            secondelement.prev.next=secondelement;
         
         }
      
      
      
      }
   
   
   }
  
   
    /** 
    *receives an index position and number of elements as parameters, and
    *removes elements beginning at the index position for the number of 
    *elements specified, provided the index position is within the size
    *and together with the number of elements does not exceed the size
    *solution 1(c) 
    */
  
   public void erase(int idx,int num){
      if((idx+num)<=size()){
         for(int i=0;i<num;i++){
            remove(idx);
         }
      }
      else{
         throw new IllegalStateException("number of elements to be deleted from index "+ idx + " is "+ num+" but number of elements after index "+ idx +" including it is "+(size()-idx));
      }
   
   }
   
   /**
   *solution 1(d) 
      *receives another MyLinkedList and an index position as parameters, and 
   *copies the list from the passed list into the list at the specified
   *position, provided the index position does not exceed the size.
   */
   public void insertList(MyLinkedList<AnyType> lst,int idx){
      if(idx<=(size())){
         for(int i=(lst.size()-1);i>=0;i--){
            add(idx,lst.get(i));
         }
      }
      else{
         throw new IndexOutOfBoundsException( "index: "+ idx + " size: " + size( ) );
      
      }
   
   }
    
    /**
     * Adds an item to this collection, at specified position.
     * Items at or after that position are slid one position higher.
     * @param x any object.
     * @param idx position to add at.
     * @throws IndexOutOfBoundsException if idx is not between 0 and size(), inclusive.
     */
   public void add( int idx, AnyType x )
   {
      addBefore( getNode( idx, 0, size( ) ), x );
   }
    
    /**
     * Adds an item to this collection, at specified position p.
     * Items at or after that position are slid one position higher.
     * @param p Node to add before.
     * @param x any object.
     * @throws IndexOutOfBoundsException if idx is not between 0 and size(), inclusive.
     */    
   private void addBefore( Node<AnyType> p, AnyType x )
   {
      Node<AnyType> newNode = new Node<>( x, p.prev, p );
      newNode.prev.next = newNode;
      p.prev = newNode;         
      theSize++;
      modCount++;
   }   
    
    
    /**
     * Returns the item at position idx.
     * @param idx the index to search in.
     * @throws IndexOutOfBoundsException if index is out of range.
     */
   public AnyType get( int idx )
   {
      return getNode( idx ).data;
   }
        
    /**
     * Changes the item at position idx.
     * @param idx the index to change.
     * @param newVal the new value.
     * @return the old value.
     * @throws IndexOutOfBoundsException if index is out of range.
     */
   public AnyType set( int idx, AnyType newVal )
   {
      Node<AnyType> p = getNode( idx );
      AnyType oldVal = p.data;
        
      p.data = newVal;   
      return oldVal;
   }
    
    /**
     * Gets the Node at position idx, which must range from 0 to size( ) - 1.
     * @param idx index to search at.
     * @return internal node corresponding to idx.
     * @throws IndexOutOfBoundsException if idx is not between 0 and size( ) - 1, inclusive.
     */
   private Node<AnyType> getNode( int idx )
   {
      return getNode( idx, 0, size( ) - 1 );
   }

    /**
     * Gets the Node at position idx, which must range from lower to upper.
     * @param idx index to search at.
     * @param lower lowest valid index.
     * @param upper highest valid index.
     * @return internal node corresponding to idx.
     * @throws IndexOutOfBoundsException if idx is not between lower and upper, inclusive.
     */    
   private Node<AnyType> getNode( int idx, int lower, int upper )
   {
      Node<AnyType> p;
        
      if( idx < lower || idx > upper )
         throw new IndexOutOfBoundsException( "getNode index: " + idx + "; size: " + size( ) );
            
      if( idx < size( ) / 2 )
      {
         p = beginMarker.next;
         for( int i = 0; i < idx; i++ )
            p = p.next;            
      }
      else
      {
         p = endMarker;
         for( int i = size( ); i > idx; i-- )
            p = p.prev;
      } 
        
      return p;
   }
    
    /**
     * Removes an item from this collection.
     * @param idx the index of the object.
     * @return the item was removed from the collection.
     */
   public AnyType remove( int idx )
   {
      return remove( getNode( idx ) );
   }
    
    /**
     * Removes the object contained in Node p.
     * @param p the Node containing the object.
     * @return the item was removed from the collection.
     */
   private AnyType remove( Node<AnyType> p )
   {
      p.next.prev = p.prev;
      p.prev.next = p.next;
      theSize--;
      modCount++;
        
      return p.data;
   }
    
    /**
     * Returns a String representation of this collection.
     */
   public String toString( )
   {
      StringBuilder sb = new StringBuilder( "[ " );
   
      for( AnyType x : this )
         sb.append( x + " " );
      sb.append( "]" );
   
      return new String( sb );
   }

    /**
     * Obtains an Iterator object used to traverse the collection.
     * @return an iterator positioned prior to the first element.
     */
   public java.util.Iterator<AnyType> iterator( )
   {
      return new LinkedListIterator( );
   }
   
   
   /**
     *solution 1(e) 
     *main method to demonstrate swapping shifting erase and insertlist
     *will demonstrate the use of all methods at one go by just executing MyLinkedList.java
   */

   
   public static void main( String [ ] args )
   {
      MyLinkedList<Integer> lst = new MyLinkedList<>( );
      for( int i = 0; i < 10; i++ )
         lst.add( i );
      for( int i = 20; i < 40; i++ )
         lst.add( 0, i );
      System.out.println("List:"+lst);
      System.out.println( "===================================swapping=======================================" );
      System.out.println( "Before swapping" );             //code to demonstrate swapping
      System.out.println( lst );
      System.out.println( "After swapping 1st time" );
      lst.swap(2,4);
      System.out.println( lst );
      System.out.println( "After swapping 2nd time" );
      lst.swap(4,2);
      System.out.println( lst );
      System.out.println( "After swapping 3rd time" );
      lst.swap(0,19);
      System.out.println( lst );
      System.out.println( "swapping adjacent elements1" );
      lst.swap(4,5);
      System.out.println( lst );
      System.out.println( "swapping adjacent elements2" );
      lst.swap(5,4);
      System.out.println( lst );
      System.out.println( "===================================swapping=======================================" );
      System.out.println( "===================================shifting=======================================" );
      System.out.println( "After shifting right by 2 positions" ); //code for demonstrating shift
      lst.shift(2);
      System.out.println( lst );
      System.out.println( "After shifting left by 2 positions" );
      lst.shift(-2);
      System.out.println( lst );
      System.out.println( "===================================shifting=======================================" );
      System.out.println( "===================================erasing=======================================" );
      System.out.println("Before erasing 1st time size is "+ lst.size());//code for demonstrating erase
      lst.erase(0,4);
      System.out.println("After erasing 1st time size is "+ lst.size());
      System.out.println("After erasing 1st time list is");
      System.out.println( lst );
      System.out.println( "===================================erasing=======================================" );
      System.out.println( "===================================insertList=======================================" );
      System.out.println("Before adding new list");//code for demonstrating insertlist
      System.out.println( "list: "+lst );
      MyLinkedList<Integer> lst1 = new MyLinkedList<>( );
      for(int i=1;i<10;i++){
         lst1.add(i);
      }
      System.out.println("new list to be added: "+ lst1 );
      lst.insertList(lst1,16);
      System.out.println("After adding new list");
      System.out.println( "Edited list: "+lst );
      System.out.println( "===================================insertList=======================================" );
   }


    /**
     * This is the implementation of the LinkedListIterator.
     * It maintains a notion of a current position and of
     * course the implicit reference to the MyLinkedList.
     */
   private class LinkedListIterator implements java.util.Iterator<AnyType>
   {
      private Node<AnyType> current = beginMarker.next;
      private int expectedModCount = modCount;
      private boolean okToRemove = false;
        
      public boolean hasNext( )
      {
         return current != endMarker;
      }
        
      public AnyType next( )
      {
         if( modCount != expectedModCount )
            throw new java.util.ConcurrentModificationException( );
         if( !hasNext( ) )
            throw new java.util.NoSuchElementException( ); 
                   
         AnyType nextItem = current.data;
         current = current.next;
         okToRemove = true;
         return nextItem;
      }
        
      public void remove( )
      {
         if( modCount != expectedModCount )
            throw new java.util.ConcurrentModificationException( );
         if( !okToRemove )
            throw new IllegalStateException( );
                
         MyLinkedList.this.remove( current.prev );
         expectedModCount++;
         okToRemove = false;       
      }
   }
    
    /**
     * This is the doubly-linked list node.
     */
   private static class Node<AnyType>
   {
      public Node( AnyType d, Node<AnyType> p, Node<AnyType> n )
      {
         data = d; prev = p; next = n;
      }
        
      public AnyType data;
      public Node<AnyType>   prev;
      public Node<AnyType>   next;
   }
    
   private int theSize;
   private int modCount = 0;
   private Node<AnyType> beginMarker;
   private Node<AnyType> endMarker;
}

/**
  *same set of demonstrations added to the existing main method to show swap,shift,insert and delete operations
*/
class TestLinkedList
{
   public static void main( String [ ] args )
   {
      MyLinkedList<Integer> lst = new MyLinkedList<>( );
   
      for( int i = 0; i < 10; i++ )
         lst.add( i );
      for( int i = 20; i < 30; i++ )
         lst.add( 0, i );
      System.out.println( "===================================swapping=======================================" );
      System.out.println( "Before swapping" );             //code to demonstrate swapping
      System.out.println( lst );
      System.out.println( "After swapping 1st time" );
      lst.swap(2,4);
      System.out.println( lst );
      System.out.println( "After swapping 2nd time" );
      lst.swap(4,2);
      System.out.println( lst );
      System.out.println( "After swapping 3rd time" );
      lst.swap(0,19);
      System.out.println( lst );
      System.out.println( "swapping adjacent elements1" );
      lst.swap(4,5);
      System.out.println( lst );
      System.out.println( "swapping adjacent elements2" );
      lst.swap(5,4);
      System.out.println( lst );
      System.out.println( "===================================swapping=======================================" );
      System.out.println( "===================================shifting=======================================" );
      System.out.println( "After shifting right by 2 positions" ); //code for demonstrating shift
      lst.shift(2);
      System.out.println( lst );
      System.out.println( "After shifting left by 2 positions" );
      lst.shift(-2);
      System.out.println( lst );
      System.out.println( "===================================shifting=======================================" );
      System.out.println( "===================================erasing=======================================" );
      System.out.println("Before erasing 1st time size is "+ lst.size());//code for demonstrating erase
      lst.erase(0,4);
      System.out.println("After erasing 1st time size is "+ lst.size());
      System.out.println("After erasing 1st time list is");
      System.out.println( lst );
      System.out.println( "===================================erasing=======================================" );
      System.out.println( "===================================insertList=======================================" );
      System.out.println("Before adding new list");//code for demonstrating insertlist
      System.out.println( "list: "+lst );
      MyLinkedList<Integer> lst1 = new MyLinkedList<>( );
      for(int i=1;i<10;i++){
         lst1.add(i);
      }
      System.out.println("new list to be added: "+ lst1 );
      lst.insertList(lst1,16);
      System.out.println("After adding new list");
      System.out.println( "Edited list: "+lst );
      System.out.println( "===================================insertList=======================================" );
   
      lst.remove( 0 );
      lst.remove( lst.size( ) - 1 );
   
      System.out.println( lst );
   
      java.util.Iterator<Integer> itr = lst.iterator( );
      while( itr.hasNext( ) )
      {
         itr.next( );
         itr.remove( );
         System.out.println( lst );
      }      
      
   }
}