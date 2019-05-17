package com.gmail.vvb0x56;

import java.util.Stack;
import java.util.Random;

// '*' - totaly unused cell 
// '#' - wall 
// ' ' - road
// '@' - entrance and exit

public class Maze {
    private char [][] map;

    private Vec startPoint;
    private Vec endPoint;
    private Stack<Vec> checkedCells;
    private Vec[] vars;
    private Random random;

    final char freeTag = '*';
    final char wallTag = '#';
    final char passTag = ' ';
    final char entrTag = '@';
    final char roadTag = '+';
    final char beggTag = '^';
    final char endTag = '$';
    final char fillerTag = '.';
    final char outOfArrayTag = '!';


    private enum Direction { UP, DOWN, LEFT, RIGHT, NOWAY}

    Maze() {
        map = new char[21][81];

        init();
    }

    Maze(int r, int c) {
        //Maze can't be less that 5x5 size;
        if (r < 1) r = 1;
        if (c < 1) c = 1;
        
        map = new char[r * 2 - 1][c * 2 - 1];

        init();
    }   


    void init() {
        random = new Random();

        startPoint = new Vec();
        endPoint = new Vec();
        vars = new Vec[4];
        initVars(vars);
        checkedCells = new Stack<>();
    }



// MAZE GENERATION 
    public void gen() {
        clean();

        // starting point for the gen algo 
        // as a starting point we need an even number  so: 
        // we should push it to the top of the stack
        int start_r = random.nextInt(map.length);
        if (start_r % 2 != 0) start_r -= 1;

        int start_c = random.nextInt(map[start_r].length);
        if (start_c % 2 != 0) start_c -= 1;

        checkedCells.push(new Vec(start_r, start_c));
        map[start_r][start_c] = passTag;
        //////////////////////////////////////////////////
        
        while (!checkedCells.empty()) {
            Vec v = new Vec(checkedCells.peek());
            
            int numOfVariants = fillVariants(v);

            if (numOfVariants > 0) {
                int nextCellIndex = random.nextInt(numOfVariants);

                Vec nextV = new Vec(vars[nextCellIndex]);

                map[nextV.r][nextV.c] = passTag;
                

                brakeWallBetween(v, nextV);
                checkedCells.push(nextV); 
            } else {
                checkedCells.pop();
            }
        }

        chooseStartEndPoints();
        set(startPoint, beggTag);
        set(endPoint, endTag);


    }

    private void chooseStartEndPoints() {
        /*
        // Set start-end points 
        //
        // First let's randomly choose dimension: 
        int coin = random.nextInt(2); 

        if (coin == 0) { 
            // so it's vertical dimension
            // it means that start and end point will in up and down 
            int sp = random.nextInt(map[0].length);
            if ( sp % 2 != 0) sp -= 1;
            
            int ep = random.nextInt(map[0].length);
            if ( ep % 2 != 0) ep -= 1;

            startPoint.r = 0; 
            startPoint.c = sp;
            endPoint.r = map.length - 1;
            endPoint.c = ep;
            
        } else {
            int sp = random.nextInt(map.length);
            if ( sp % 2 != 0) sp -= 1;
            
            int ep = random.nextInt(map.length);
            if ( ep % 2 != 0) ep -= 1;

            startPoint.r = sp; 
            startPoint.c = 0;
            endPoint.r = ep;
            endPoint.c = map[0].length - 1;
        }
        */

        // I think it's better to get start-end points from the edges
        startPoint.r = 0;
        startPoint.c = 0;

        int end_r = map.length - 1;
        int end_c = map[end_r].length - 1;
        if ( end_r % 2 != 0 ) end_r -= 1;
        if ( end_c % 2 != 0 ) end_c -= 1;

        endPoint.r = end_r;
        endPoint.c = end_c;
        
    }   

// make a path on a map through wall between cells
    private void brakeWallBetween(Vec a, Vec b) {
        
        int r = (b.r - a.r) / 2 + a.r;
        int c = (b.c - a.c) / 2 + a.c;

        set(r, c, passTag);
    }

// nextCell is an index of 'vars'
    private int getNextCellIndex(Vec v) {
        return getNextCellIndex(v.r, v.c);
    }   

    private int getNextCellIndex(int r, int c) {
        int numOfVars = fillVariants(r, c); 
        return  random.nextInt(numOfVars);
    }

// just filling vars with variants, and return count of variants
//
    private int fillVariants(Vec v) {
        return fillVariants(v.r, v.c);

    }

    private int fillVariants(int r, int c) {
        int numOfVars = 0;
        int rows = map.length;
        int cols = map[0].length;

        if (r - 2 >= 0 && map[r - 2][c] == freeTag) {
            vars[numOfVars].r = r - 2;   
            vars[numOfVars].c = c;   
            numOfVars++;
        }
        
        if (r + 2 < rows && map[r + 2][c] == freeTag) {
            vars[numOfVars].r = r + 2;   
            vars[numOfVars].c = c;   
            numOfVars++;
        }

        if (c - 2 >= 0 && map[r][c - 2] == freeTag) {
            vars[numOfVars].r = r;   
            vars[numOfVars].c = c - 2;   
            numOfVars++;
        }

        if (c + 2 < cols && map[r][c + 2] == freeTag) {
            vars[numOfVars].r = r;   
            vars[numOfVars].c = c + 2;   
            numOfVars++;
        }

        return numOfVars;
    }

    private void clean() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (i % 2 == 0 && j % 2 == 0)
                    map[i][j] = freeTag;
                else 
                    map[i][j] = wallTag;
            }
        }
    }

// print map to the console
    public void print() {
        for (int i = 0; i < map[0].length + 2; i++) {
            System.out.print(wallTag);
        }
        System.out.println();

        for (int i = 0; i < map.length; i++) {
            System.out.print(wallTag);
            for (int j = 0; j < map[i].length; j++) {
                System.out.print(map[i][j]);
            }
            System.out.print(wallTag + "\n");
        }

        for (int i = 0; i < map[0].length + 2; i++) {
            System.out.print(wallTag);
        }
        System.out.println();
    }

    private void printCell(Vec v) {
        System.out.println(get(v.r, v.c));
    }

//initialization of the Vars, used once !
    private void initVars(Vec[] v) {
        for (int i = 0; i < v.length; i++) {
            v[i] = new Vec();
        }
    }

    private void set(int row, int col, char c) {
        if (check(row, col)) 
            map[row][col] = c;
    }
    
    private void set(Vec v, char c) {
        set(v.r, v.c, c);
    }

    public char get(int row, int col) {
        if (check(row, col)) return map[row][col];

        return outOfArrayTag;
    }

    public int numOfRows() { return map.length; }
    public int numOfCols() { return map[0].length; }

    private boolean check(int row, int col) {
        if (row < 0 || 
            col < 0 || 
            row >= map.length || 
            col >= map[0].length)  return false;
        return true;
    }

// MAZE SOLVATION
    public void solve() {

        checkedCells.push(startPoint);

        while ( !checkedCells.empty() ) {
            Vec v = checkedCells.peek();

            Direction dir = getAnyPossibleWay(v.r, v.c);
            if (dir == Direction.NOWAY) { 
                checkedCells.pop();   
                continue;
            }

            int r = v.r;
            int c = v.c;

            switch (dir) {
            case UP:
                r -= 1;
                break;
            case DOWN:
                r += 1;
                break;
            case LEFT:
                c -= 1;
                break;
            case RIGHT:
                c += 1;
                break;
            default: 
                System.out.println("Unknown Direction type");
                break;
            }
            
            if (map[r][c] == endTag) 
                break;
            else 
                map[r][c] = '.';


            checkedCells.push(new Vec(r, c));
        }

        removeFillerTags();

        drawSolvedPass();
        
    }


    private Direction getAnyPossibleWay(int r, int c) {
        if (r + 1 < map.length          && 
            ( map[r + 1][c] == passTag || map[r + 1][c] == endTag )) 
            return Direction.DOWN;

        if (c - 1 >= 0                  && 
            ( map[r][c - 1] == passTag || map[r][c - 1] == endTag )) 
            return Direction.LEFT;
        
        if (r - 1 >= 0                  && 
            ( map[r - 1][c] == passTag || map[r - 1][c] == endTag )) 
            return Direction.UP;

        if (c + 1 < map[0].length       && 
            (map[r][c + 1] == passTag || map[r][c + 1] == endTag)) 
            return Direction.RIGHT;

        return Direction.NOWAY;
    }

    private void removeFillerTags() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == fillerTag) {
                    map[i][j] = passTag;
                }
            }
        }
    }

    private void drawSolvedPass() {
        while ( !checkedCells.empty() ) {
            Vec v = checkedCells.pop();
            // It's little bit stupid, but i added that 
            // to prevent rewriting of my start point .. 
            if ( checkedCells.size() > 0)
                set(v.r, v.c, roadTag);
        }
    }
}






















