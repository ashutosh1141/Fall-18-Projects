import java.util.*;
import java.io.*;
import javafx.util.Pair;

public class Kruskals {

   public Graph readFile(String path){//read the excel sheet and convert to a graph
      Graph g=new Graph();
      BufferedReader reader = null;
      try {
         reader = new BufferedReader(new FileReader(path));
         String line = null;
         while ((line = reader.readLine()) != null) {
            ArrayList<String> arrlist=new ArrayList<String>(Arrays.asList(line.split(",")));
            String src=arrlist.get(0);
            for(int i=1;i<arrlist.size();i=i+2){
               String dest=arrlist.get(i);
               int dist=Integer.parseInt(arrlist.get(i+1));
               g.addtolists(src,dest,dist);
            }
         }
         reader.close();
      }
      catch(Exception e){
         e.printStackTrace();
      }
      return g;
   }


   class Graph{//keep an arraylist of edges and nodes
   
      ArrayList<edge> edgelist;
      ArrayList<Node> vlist;
   
      Graph(){
         edgelist=new ArrayList<edge>();
         vlist=new ArrayList<Node>();
      }
   
      public ArrayList<edge> kruskal( ArrayList<edge> edgelst,int numVertices)//krusklas algo for minimum spanning tree
      {
         DisjSets ds = new DisjSets( numVertices );
         PriorityQueue<edge> pq = new PriorityQueue<>( edgelist );
         ArrayList<edge> mst = new ArrayList<>( );
         while( mst.size( ) != numVertices - 1 )
         {
            edge e = pq.poll(); 
            int uset = ds.find( e.p.getKey().topnum );
            int vset = ds.find( e.p.getValue().topnum);
            if( uset != vset )
            {
            
               mst.add( e );
               ds.union( uset, vset );
            }
         }
         return mst;
      }
   
   
      public void addtolists(String from,String to,int distance){//add nodes and vertices to respective lists
         Node n1=addtonodelist(from);
         Node n2=addtonodelist(to);
         addtoedgelist(n1,n2,distance);
      
      }
      public void addtoedgelist(Node from,Node to,int distance){//adds unique edges to the edgelist
         edge e1=new edge(from,to,distance);
         if(edgelist.size()==0){
            edgelist.add(e1);
         }
         else{
            boolean hasedge=false;
            for(int i=0;i<edgelist.size();i++){
               edge el=edgelist.get(i);
               Pair<Node,Node> p1=el.p;
               Node start=p1.getKey();
               Node end=p1.getValue();
               if((start.name.equals(from.name) && end.name.equals(to.name)) || (start.name.equals(to.name) && end.name.equals(from.name))){
                  hasedge=true;
                  break;
               }
            }
            if(!hasedge){
               edgelist.add(e1);
            }
         }
      }
   
      public Node addtonodelist(String from){//add unique nodes to the nodelist
         Node n=new Node(from,Integer.MIN_VALUE);
         for(int i=0;i<vlist.size();i++){
            if(vlist.get(i).name.equals(from))
            {
               n=vlist.get(i);
               return n;
            }
         }
         if(n.topnum==Integer.MIN_VALUE){
            n.topnum=vlist.size();
            vlist.add(n);
         }
         return n;
      }
   
   
   }

   class Node{
      String name;
      int topnum;//associate a topological number to the node to later map to the disjoint set
      
   
      Node(String name,int topnum){
         this.name=name;
         this.topnum=topnum;
      }
   
   }


   class edge implements Comparable<edge>{
      Pair<Node,Node> p;
      int weight;
   
      edge(Node v1,Node v2,int weight){
         p=new Pair<Node,Node>(v1,v2);
         this.weight=weight;
      }
   
      public int compareTo(edge e) {
         return this.weight - e.weight;
      }
   
   }

   public static void main(String args[]){
      Kruskals k1=new Kruskals();
      Graph g1=k1.readFile("assn9_data.csv");
      ArrayList<edge> edlist=g1.kruskal(g1.edgelist,g1.vlist.size());
      int sum=0;
      System.out.println("printing Edges after applying kruskals Algorithm:");
      for(edge e:edlist){
         System.out.println(e.p.getKey().name+","+e.p.getValue().name+","+e.weight);
         sum+=e.weight;
      }
      System.out.println();
      System.out.println("The sum of distances is:"+sum);
   }

}