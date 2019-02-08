// BinarySearchTree class
//
// CONSTRUCTION: with no initializer
//
// ******************PUBLIC OPERATIONS*********************
// void insert( x )       --> Insert x
// void remove( x )       --> Remove x
// boolean contains( x )  --> Return true if x is present
// Comparable findMin( )  --> Return smallest item
// Comparable findMax( )  --> Return largest item
// boolean isEmpty( )     --> Return true if empty; else false
// void makeEmpty( )      --> Remove all items
// void printTree( )      --> Print tree in sorted order
// ******************ERRORS********************************
// Throws UnderflowException as appropriate

/**
 * Implements an unbalanced binary search tree.
 * Note that all "matching" is based on the compareTo method.
 * @author Mark Allen Weiss
 */
import java.util.*;
import java.lang.*;
public class BinarySearchTree<AnyType extends Comparable<? super AnyType>>
{
   /**
    * Construct the tree.
    */
   public BinarySearchTree( )
   {
      root = null;
   }
   
   
   /**
    * Insert into the tree; duplicates are ignored.
    * @param x the item to insert.
    */
   public void insert( AnyType x )
   {
      root = insert( x, root );
   }

   /**
    * Remove from the tree. Nothing is done if x is not found.
    * @param x the item to remove.
    */
   public void remove( AnyType x )
   {
      root = remove( x, root );
   }

   /**
    * Find the smallest item in the tree.
    * @return smallest item or null if empty.
    */
   public AnyType findMin( )
   {
      if( isEmpty( ) )
         throw new NullPointerException( );
      return findMin( root ).element;
   }

   /**
    * Find the largest item in the tree.
    * @return the largest item of null if empty.
    */
   public AnyType findMax( )
   {
      if( isEmpty( ) )
         throw new NullPointerException( );
      return findMax( root ).element;
   }

   /**
    * Find an item in the tree.
    * @param x the item to search for.
    * @return true if not found.
    */
   public boolean contains( AnyType x )
   {
      return contains( x, root );
   }

   /**
    * Make the tree logically empty.
    */
   public void makeEmpty( )
   {
      root = null;
   }

   /**
    * Test if the tree is logically empty.
    * @return true if empty, false otherwise.
    */
   public boolean isEmpty( )
   {
      return root == null;
   }

   /**
    * Print the tree contents in sorted order.
    */
   public void printTree( )
   {
      if( isEmpty( ) )
         System.out.println( "Empty tree" );
      else
         printTree( root );
   }

   /**
    * Internal method to insert into a subtree.
    * @param x the item to insert.
    * @param t the node that roots the subtree.
    * @return the new root of the subtree.
    */
   public int nodeCount(){
      return nodeCount(root);
   }
    
      
   private int nodeCount(BinaryNode<AnyType> t){
      if( t == null ){//null tree count is zero
         return 0;
      }
   
      if( t.left == null && t.right == null ){//only root node,return one
         return 1;
      }
   
    
      return nodeCount( t.left ) + nodeCount( t.right ) + 1;   //return the count of nodes in left and right subtree recursively along with the root node which accounts for the +1
   }

   
   public boolean isFull(){
      return isFull(root);
   }
   
   private boolean isFull(BinaryNode<AnyType> t){
      if(t==null){      //null tree return false
         return false;
      }
   
      if(t.left==null && t.right==null){//single node tree return true
         return true;
      }
   
      if(!(t.left==null && t.right==null) && (t.left==null || t.right==null)){//when right child and left child, both together are not null but either one is null return false. 
         return false;
      }
   
      return (isFull(t.left))&&(isFull(t.right));//check the left and right tree recursively
   
   }
   
   public boolean compareStructure(BinarySearchTree<AnyType> t1){
   return compareStructure(t1.root,root);
   }
   
   private boolean compareStructure(BinaryNode<AnyType> t1,BinaryNode<AnyType> t2){
   if(t1==null && t2==null)//return true only if both the tree pointers become null at the same time
   return true;
      
   if(t1!=null && t2!=null)
   return (compareStructure(t1.left,t2.left) && compareStructure(t1.right,t2.right));//compare left and right subtrees recursively
   
   return false;
    
   }

   
   
   public boolean equals(BinarySearchTree<AnyType> tree){
      return equals(tree.root,root);
   }
  
   private boolean equals(BinaryNode<AnyType> t1,BinaryNode<AnyType> t2){
   
      if(t1==null && t2==null)//return true when both pointers become null together
         return true;
    
      if(t1!=null && t2!=null)
         return (t1.element==t2.element && equals(t1.left,t2.left) && equals(t1.right,t2.right));//recursively compare the right and left subtrees along with the values at the node
    
      return false;  
   }
   
    
   public BinaryNode<AnyType> mirror(){
      return mirror(root);
   }
  
   private BinaryNode<AnyType> mirror(BinaryNode<AnyType> t){
      BinaryNode<AnyType> temp;
      if(t!=null){//for each node we have to swap the left and right child
         temp=t.left;
         t.left=t.right;
         t.right=temp;
         mirror(t.left); //recursively swap each node in the right and left subtree
         mirror(t.right);
      }
      return t;
   }
   
   public boolean isMirror(BinarySearchTree<AnyType> tree){
      return isMirror(tree.root,root);
   }
    
   private boolean isMirror(BinaryNode<AnyType> t1,BinaryNode<AnyType> t2){
    
      if(t1==null && t2==null)
         return true;
      if(t1.left==null && t2.left==null && t1.right==null && t2.right==null){
         if(t1.element==t2.element)
            return true;
         else
            return false;
      }
      return (t1.element==t2.element) && isMirror(t1.left,t2.right) && isMirror(t1.right,t2.left);
   
   }
   
   public BinaryNode<AnyType> rotateRight(AnyType val){
      return rotateRight(root,val);
   }
   
   private BinaryNode<AnyType> rotateRight(BinaryNode<AnyType> t,AnyType val){
      //need to find the node with value=val and then perform right rotation on it.
      BinaryNode<AnyType> resultnode=t;
      BinaryNode<AnyType> tempnode=t;
      BinaryNode<AnyType> previousnode=t;
      BinaryNode<AnyType> newroot=null;
      int compareResult = val.compareTo( t.element );
   
      if(compareResult==0){
         resultnode=t;
      }
      while(tempnode!=null && val!=tempnode.element){
         int compareResult1 = val.compareTo( tempnode.element );
      
         if(compareResult1>0){
            tempnode=tempnode.right;
         }
         else if(compareResult1<0){
            tempnode=tempnode.left;
         }
      }
      resultnode=tempnode;//found node where value is equal to val;
      if(resultnode.left==null && resultnode.right==null){//when the resultnode is a leafnode no rotation needed
         newroot=t;
         throw new UnderflowException("Rotation not possible");
      }
      if(resultnode==t){//when rotation is done on the rootnode
      
         if(resultnode.left==null && resultnode.right!=null ){//no rotation possible,if rotated tree will no longer remain bst
         
            newroot=t;
            throw new UnderflowException("Rotation not possible");

         
         }
      
         if(resultnode.right==null && resultnode.left!=null ){
            BinaryNode<AnyType> leftchild=resultnode.left;
            newroot=leftchild;
            if(leftchild.right!=null){
               BinaryNode<AnyType> rightchild=leftchild.right;
               newroot.right=resultnode;
               newroot.right.left=rightchild;
            }
            else{
               newroot.right=resultnode;
            }
         
         }
      
         if(resultnode.right!=null && resultnode.left!=null){
            BinaryNode<AnyType> leftchild=resultnode.left;//rotate the resultnode right 
            newroot=leftchild;//newroot=leftchild of old root
            if(leftchild.right!=null){
               BinaryNode<AnyType> rightchild=resultnode.left.right;
               leftchild.right=resultnode;
               resultnode.left=rightchild;
            }
            else{
               leftchild.right=resultnode;
               leftchild.right.left=null;
            }
         }
      }
      else{//when rotation done on some other node the old root remains intact
         newroot=t;
         if(resultnode.left==null && resultnode.right!=null ){
         throw new UnderflowException("Rotation not possible");

         //do nothing,no rotation
         }
         if(resultnode.left!=null && resultnode.right==null ){
            BinaryNode<AnyType> leftchild=resultnode.left;
            if(leftchild.right!=null){//resultnodes left.right is not null, we need to take care of it also as it will be linked to a different node
               BinaryNode<AnyType> rightchild=resultnode.left.right;
               if(previousnode.right==resultnode){//if resultnode was a rightchild of the previous node we have kept track of
                  previousnode.right=leftchild;
                  previousnode.right.right=resultnode;
                  previousnode.right.right.left=rightchild;
               }
               if(previousnode.left==resultnode){//if resultnode was a leftchild of the previous node we have kept track of
                  previousnode.left=leftchild;
                  previousnode.left.right=resultnode;
                  previousnode.left.right.left=rightchild;
               }
            
            }
            else{//result nodes left.right is null
            
               if(previousnode.right==resultnode){
                  previousnode.right=leftchild;
                  previousnode.right.right=resultnode;
                  previousnode.right.right.left=null;
               }
               if(previousnode.left==resultnode){
                  previousnode.left=leftchild;
                  previousnode.left.right=resultnode;
                  previousnode.left.right.left=null;
               }
            
            }
         
         
         }
      
         if(resultnode.left!=null && resultnode.right!=null ){//both children of resultnode not null
            BinaryNode<AnyType> leftchild=resultnode.left;//rotate the resultnode right 
            if(leftchild.right!=null){
               BinaryNode<AnyType> rightchild=resultnode.left.right;
               if(previousnode.right==resultnode){//after rotation need to link the previous node to the resultnode after rotation
                  previousnode.right=leftchild;
                  previousnode.right.right=resultnode;
                  previousnode.right.right.left=rightchild;
               }
               if(previousnode.left==resultnode){
                  previousnode.left=leftchild;
                  previousnode.left.right=resultnode;
                  previousnode.left.right.left=rightchild;
               }
            }
            else{
            
               if(previousnode.right==resultnode){
                  previousnode.right=leftchild;
                  previousnode.right.right=resultnode;
                  previousnode.right.right.left=null;
               }
               if(previousnode.left==resultnode){
                  previousnode.left=leftchild;
                  previousnode.left.right=resultnode;
                  previousnode.left.right.left=null;
               }
            }
         }
      }         
      
      return newroot;         
   }
   
   public BinaryNode<AnyType> rotateLeft(AnyType val){
      return rotateLeft(root,val);
   }
   
   private BinaryNode<AnyType> rotateLeft(BinaryNode<AnyType> t,AnyType val){
      //need to find the node with value=val and then perform left rotation on it.
      BinaryNode<AnyType> resultnode=t;
      BinaryNode<AnyType> previousnode=t;
      BinaryNode<AnyType> tempnode=t;
      BinaryNode<AnyType> newroot=null;
      int compareResult = val.compareTo( t.element );
   
      if(compareResult==0){
         resultnode=t;
      }
      while(tempnode!=null && val!=tempnode.element){
         int compareResult1 = val.compareTo( tempnode.element );
         previousnode=tempnode;
         if(compareResult1>0){
            tempnode=tempnode.right;
         }
         else if(compareResult1<0){
            tempnode=tempnode.left;
         }
      }
      resultnode=tempnode;//found node where value is equal to val;
      if(resultnode.left==null && resultnode.right==null){//when the resultnode is a leafnode no rotation needed
         newroot=t;
         throw new UnderflowException("rotation not possible");

      }
      if(resultnode==t){//when rotation is done on the rootnode
      
         if(resultnode.right==null && resultnode.left!=null ){//no rotation possible,if rotated tree will no longer remain bst
         
            newroot=t;
         throw new UnderflowException("rotation not possible");
         }
      
         if(resultnode.left==null && resultnode.right!=null ){
            BinaryNode<AnyType> rightchild=resultnode.right;
            newroot=rightchild;
            if(rightchild.left!=null){
               BinaryNode<AnyType> leftchild=rightchild.left;
               newroot.left=resultnode;
               newroot.left.right=leftchild;
            }
            else{
               newroot.left=resultnode;
            }
         
         }
      
         if(resultnode.left!=null && resultnode.right!=null){
            BinaryNode<AnyType> rightchild=resultnode.right;//rotate the resultnode left 
            newroot=rightchild;//newroot=rightchild of old root
            if(rightchild.left!=null){
               BinaryNode<AnyType> leftchild=resultnode.right.left;
               rightchild.left=resultnode;
               resultnode.right=leftchild;
            }
            else{
               rightchild.left=resultnode;
               rightchild.left.right=null;
            }
         }
      }
      else{//when rotation done on some other node the old root remains intact
         newroot=t;
         if(resultnode.right==null && resultnode.left!=null ){
         throw new UnderflowException("rotation not possible");
         //do nothing,no rotation
         }
         if(resultnode.right!=null && resultnode.left==null ){
            BinaryNode<AnyType> rightchild=resultnode.right;
            if(rightchild.left!=null){
               BinaryNode<AnyType> leftchild=resultnode.right.left;
               if(previousnode.right==resultnode){//keeping track of previous node and linking as needed
                  previousnode.right=rightchild;
                  previousnode.right.left=resultnode;
                  previousnode.right.left.right=leftchild;
               }
               else{
                  previousnode.left=rightchild;
                  previousnode.left.left=resultnode;
                  previousnode.left.left.right=leftchild;//keeping track of previous node and linking as needed
               
               }
            }
            else{
               if(previousnode.right==resultnode){
                  previousnode.right=rightchild;
                  previousnode.right.left=resultnode;
                  previousnode.right.left.right=null;//keeping track of previous node and linking as needed
               }
               else{
                  previousnode.left=rightchild;
                  previousnode.left.left=resultnode;
                  previousnode.left.left.right=null;//keeping track of previous node and linking as needed
               }
            }
         }
      
         if(resultnode.right!=null && resultnode.left!=null ){
            BinaryNode<AnyType> resultnodeleftchild=resultnode.left;
            BinaryNode<AnyType> tempresultnode=resultnode;
            BinaryNode<AnyType> rightchild=resultnode.right;//rotate the resultnode left 
            if(rightchild.left!=null){
               BinaryNode<AnyType> leftchild=rightchild.left;//keeping track of previous node and linking as needed
               if(previousnode.right==resultnode){
                  previousnode.right=rightchild;
                  previousnode.right.left=resultnode;
                  previousnode.right.left.right=leftchild;
               }
               else{
                  previousnode.left=rightchild;//keeping track of previous node and linking as needed
                  previousnode.left.left=resultnode;
                  previousnode.left.left.right=leftchild;
               }
            }
            else{
               if(previousnode.right==resultnode){//keeping track of previous node and linking as needed
                  previousnode.right=rightchild;
                  previousnode.right.left=resultnode;
                  previousnode.right.left.right=null;
               }
               if(previousnode.left==resultnode){//keeping track of previous node and linking as needed
                  previousnode.left=rightchild;
                  previousnode.left.left=resultnode;
                  previousnode.left.left.right=null;
               }
               
            }
         }
      }         
      
      return newroot;         
   }
   
   public void printLevels(){
      printLevels(root);
   }
   
   private void printLevels(BinaryNode<AnyType> t){
      if(t==null)
         return;
      Queue<BinaryNode<AnyType>> q = new LinkedList<BinaryNode<AnyType>>();
      q.add(t);
      while(!q.isEmpty()){
         BinaryNode<AnyType> currentnode=q.peek();
         System.out.print(currentnode.element+" ");
         if(currentnode.left!=null)
            q.add(currentnode.left);
         if(currentnode.right!=null)
            q.add(currentnode.right);
         q.remove();
      
      }
   }
   
   
   public BinarySearchTree<AnyType> copy(){
      return copy(root);
   }
  
    
   private BinarySearchTree<AnyType> copy(BinaryNode<AnyType> t){
      BinarySearchTree<AnyType> tree=null ;
      if(t==null)
         return tree;
      if(t!=null){
         tree=new BinarySearchTree<AnyType>();
         Queue<BinaryNode<AnyType>> q=new LinkedList<BinaryNode<AnyType>>();
         q.add(t);
         while(!q.isEmpty()){//inserting the values of the old tree into the new tree level by level
            int size=q.size();
            for(int i=0;i<size;i++){
               BinaryNode<AnyType> current=q.poll();
               if(current!=null){
                  tree.insert(current.element);
                  if(current.left!=null)
                     q.add(current.left);
                  if(current.right!=null)
                     q.add(current.right);
               }
            }
         }
      
      }
      return tree;
   }

   

   
   private BinaryNode<AnyType> insert( AnyType x, BinaryNode<AnyType> t )
   {
      if( t == null )
         return new BinaryNode<>( x, null, null );
      
      int compareResult = x.compareTo( t.element );
          
      if( compareResult < 0 )
         t.left = insert( x, t.left );
      else if( compareResult > 0 )
         t.right = insert( x, t.right );
      else
         ;  // Duplicate; do nothing
      return t;
   }

   /**
    * Internal method to remove from a subtree.
    * @param x the item to remove.
    * @param t the node that roots the subtree.
    * @return the new root of the subtree.
    */
   private BinaryNode<AnyType> remove( AnyType x, BinaryNode<AnyType> t )
   {
      if( t == null )
         return t;   // Item not found; do nothing
          
      int compareResult = x.compareTo( t.element );
          
      if( compareResult < 0 )
         t.left = remove( x, t.left );
      else if( compareResult > 0 )
         t.right = remove( x, t.right );
      else if( t.left != null && t.right != null ) // Two children
      {
         t.element = findMin( t.right ).element;
         t.right = remove( t.element, t.right );
      }
      else
         t = ( t.left != null ) ? t.left : t.right;
      return t;
   }

   /**
    * Internal method to find the smallest item in a subtree.
    * @param t the node that roots the subtree.
    * @return node containing the smallest item.
    */
   private BinaryNode<AnyType> findMin( BinaryNode<AnyType> t )
   {
      if( t == null )
         return null;
      else if( t.left == null )
         return t;
      return findMin( t.left );
   }

   /**
    * Internal method to find the largest item in a subtree.
    * @param t the node that roots the subtree.
    * @return node containing the largest item.
    */
   private BinaryNode<AnyType> findMax( BinaryNode<AnyType> t )
   {
      if( t != null )
         while( t.right != null )
            t = t.right;
   
      return t;
   }

   /**
    * Internal method to find an item in a subtree.
    * @param x is item to search for.
    * @param t the node that roots the subtree.
    * @return node containing the matched item.
    */
   private boolean contains( AnyType x, BinaryNode<AnyType> t )
   {
      if( t == null )
         return false;
          
      int compareResult = x.compareTo( t.element );
          
      if( compareResult < 0 )
         return contains( x, t.left );
      else if( compareResult > 0 )
         return contains( x, t.right );
      else
         return true;    // Match
   }

   /**
    * Internal method to print a subtree in sorted order.
    * @param t the node that roots the subtree.
    */
   private void printTree( BinaryNode<AnyType> t )
   {
      if( t != null )
      {
         printTree( t.left );
         System.out.println( t.element );
         printTree( t.right );
      }
   }

   /**
    * Internal method to compute height of a subtree.
    * @param t the node that roots the subtree.
    */
   private int height( BinaryNode<AnyType> t )
   {
      if( t == null )
         return -1;
      else
         return 1 + Math.max( height( t.left ), height( t.right ) );    
   }
   
   // Basic node stored in unbalanced binary search trees
   private static class BinaryNode<AnyType>
   {
          // Constructors
      BinaryNode( AnyType theElement )
      {
         this( theElement, null, null );
      }
   
      BinaryNode( AnyType theElement, BinaryNode<AnyType> lt, BinaryNode<AnyType> rt )
      {
         element  = theElement;
         left     = lt;
         right    = rt;
      }
   
      AnyType element;            // The data in the node
      BinaryNode<AnyType> left;   // Left child
      BinaryNode<AnyType> right;  // Right child
   }


     /** The tree root. */
   private BinaryNode<AnyType> root;


       // Test program
   public static void main( String [ ] args )
   {
      BinarySearchTree<Integer> t = new BinarySearchTree<>( );
      t.insert(5);
      t.insert(2);
      t.insert(7);
      t.insert(6);
      t.insert(8);
      t.insert(1);
      System.out.println("===========================nodeCount============================");
      int count=t.nodeCount();
      System.out.println("The number of nodes is:"+count);
      System.out.println("===========================nodeCount============================");
      System.out.println("===========================isFull===============================");
      BinarySearchTree<Integer> tfullcheck = new BinarySearchTree<>( );
      tfullcheck.insert(5);
      tfullcheck.insert(2);
      tfullcheck.insert(7);
      tfullcheck.insert(6);
      tfullcheck.insert(8);
      tfullcheck.insert(1);
      boolean fullcheck=tfullcheck.isFull();
      System.out.println("Result for check whether tree is full:"+fullcheck);
      tfullcheck.insert(3);
      boolean fullcheck1=tfullcheck.isFull();//after making the tree full
      System.out.println("Result for check whether tree is full:"+fullcheck1);
      System.out.println("===========================isFull===============================");
      System.out.println("===========================mirror===============================");
      BinarySearchTree<Integer> tmirror = new BinarySearchTree<>( );
      tmirror.insert(5);
      tmirror.insert(2);
      tmirror.insert(7);
      tmirror.insert(6);
      tmirror.insert(8);
      tmirror.insert(1);
      System.out.println("Inorder traversal of Tree before mirroring");//before mirroring inorder gives the elements of tree in sorted order
      tmirror.printTree();
      tmirror.mirror();
      System.out.println("Inorder traversal of Tree after mirroring");//inorder traversal reverses after mirroring,after mirroring decreasing sequence
      tmirror.printTree();
      System.out.println("===========================mirror===============================");
      BinarySearchTree<Integer> tstructcheck = new BinarySearchTree<>( );
      tstructcheck.insert(4);
      tstructcheck.insert(3);
      tstructcheck.insert(8);
      tstructcheck.insert(7);
      tstructcheck.insert(9);
      tstructcheck.insert(2);
      System.out.println("===========================compareStructure======================");
      boolean structresult=t.compareStructure(tstructcheck);
      System.out.println("The result is "+structresult);
      tstructcheck.insert(10);
      boolean structresult1=t.compareStructure(tstructcheck);
      System.out.println("The result after changing the structure is "+structresult1);
      tstructcheck.remove(10);
      boolean structresult2=t.compareStructure(tstructcheck);
      System.out.println("The result after restoring the structure is "+structresult2);
      System.out.println("===========================compareStructure======================");
      
      System.out.println("===========================RightRotate============================");
      System.out.println((t.root).element);
      BinaryNode<Integer> newroot2=t.rotateRight(7);//right rotating and printing all elements of the tree.  
      System.out.println(newroot2.element);                             
      System.out.println((newroot2.left).element);
      System.out.println((newroot2.right).element);
      System.out.println((newroot2.right.right).element);
      System.out.println((newroot2.right.right.right).element);
      System.out.println("===========================RightRotate============================");
      System.out.println("===========================LeftRotate=============================");
      BinarySearchTree<Integer> t1 = new BinarySearchTree<>( );
      t1.insert(5);
      t1.insert(2);
      t1.insert(7);
      t1.insert(6);
      t1.insert(8);
      t1.insert(1);
      BinaryNode<Integer> newroot3=t1.rotateLeft(7);
      System.out.println(newroot3.element);////left rotating and printing all elements of the tree.
      System.out.println((newroot3.left).element);
      System.out.println((newroot3.right).element);
      System.out.println((newroot3.right.left).element);
      System.out.println((newroot3.right.left.left).element);
      System.out.println("===========================LeftRotate=============================");
      System.out.println("===========================printlevels============================");
      t1.printLevels();
      System.out.println();
      System.out.println("===========================printlevels============================");
      System.out.println("===========================copy===================================");
      BinarySearchTree<Integer> t3= new BinarySearchTree<>( );
      t3.insert(5);
      t3.insert(2);
      t3.insert(7);
      t3.insert(6);
      t3.insert(8);
      t3.insert(1);
      System.out.println("printing original tree");
      t3.printTree();
      BinarySearchTree<Integer> t4=t3.copy(); //calling copy to get a copy of t3 in t4
      System.out.println("printing new tree");
      t4.printTree();                   //printTree() on t3 and t4 give the same inorder traversal of tree
      System.out.println("===========================copy===================================");
   
      
   
   
   }
}