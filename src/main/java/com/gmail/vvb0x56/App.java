package com.gmail.vvb0x56;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Maze maze = new Maze();
      //  maze.print();
        maze.gen();
      //  System.out.print(":::\n");
      //  maze.print();

        maze.solve();

        maze.print();
    }

}
