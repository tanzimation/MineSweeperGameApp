
package cs1.MineSweeperApp;

import  cs1.app.*;

public class MineSweeperApp
{
    void plantMines(int[][]field)
    {
        for(int r=0; r<field.length ; r++)
        {
            for(int c=0;c<field[r].length;c++)
            {
                int num= canvas.getRandomInt(1,10);
                if(num== 1 || num== 2)
                {
                    field[r][c] = 9;
                }
            }
        }
    }

    void setFlag ( int[][]field, int row, int col)
    {
        if(field[row][col]<10)
        {
            field[row][col]=field[row][col]+20;
        }
        else if(19<field[row][col] &&field[row][col]<30)
        {
            field[row][col]=field[row][col]-20;
        }
    }

    void printTable(int[][] field)
    {
        for (int r = 0; r < field.length; r = r + 1)               // Step 1
        {
            for (int c = 0; c < field[r].length  ;  c = c + 1)     // Step 2
            {
                System.out.print( field[r][c] + " ");              // Step 3
            }
            System.out.println();
        }
    }

    boolean isCleared(int[][]field)
    {
        for(int i=0; i<field.length; i++ )
        {
            for(int j=0; j<field[i].length; j++)
            {
                if(field[i][j]<10)
                {
                    return false;
                }
                else if(field[i][j]>19 && field[i][j]<29)
                {
                    return false;
                }
            }
        }
        return true;
    }

    void openMines(int [][] field)
    {
        for(int i=0; i<field.length; i++)
        {
            for(int j=0; j<field[i].length; j++)
            {
                if(field[i][j] == 9)
                {
                    field[i][j]= field[i][j]+10;
                }
            }
        }
    }

    boolean isValid( int[][]field,int row,int col )
    {
        if(row>=field.length || row < 0)
        {
            return false;
        }
        else if(col>= field[0].length || col < 0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    int countMines( int[][] field, int row, int col)
    {
        int count = 0;
        for( int r = row - 1; r <= row + 1; r++)
        {
            for( int c = col - 1; c <= col + 1; c++)
            {
                if( isValid( field, r, c)== true && field[r][c] == 9)
                {
                    count++ ;
                }
            }
        }
        return count;
    }

    void updateField(int [][] field)
    {
        for(int i = 0; i < field.length; i ++)
        {
            for(int j = 0 ; j < field[i].length;  j ++)
            {
                if(field[i][j] != 9)
                {
                    field[i][j] = countMines(field, i, j);
                }
            }
        }
    }

    int [][] generateField(int rows, int cols)
    {
        int [ ][ ]field = new int [rows][cols];
        plantMines(field);
        updateField(field);
        return field;
    }

    void openCell( int[][]field, int row, int col )
    {
        if(field[row][col]>=0 && field[row][col]<=8)
        {
            field[row][col] = field[row][col]+10;
        }
        else if( field[row][col] == 0)
        {
            for( int r = row - 1; r <= row + 1; r++)
            {
                for( int c = col - 1; c <= col + 1; c++)
                {
                    if( isValid( field, r, c) == true && field[r][c] <= 8)
                    {
                        field[r][c] = field[r][c] + 10;
                    }
                }
            }
        }
    }

    void drawCell(double x, double y, int value)
    {
        if( value <= 9)
        {
            canvas.drawImage( x, y, "hidden.png");
        }
        else
        {
            canvas.drawImage(x, y, "m" + value + ".png");
        }
    }

    void drawField( int[][] field, double startX, double startY)
    {
        double curX = startX;
        double curY = startY;

        for( int r = 0; r < field.length; r++)
        {
            for( int c = 0; c < field[r].length; c++)
            {
                drawCell( curX, curY, field[r][c]);
                curX = curX + 36;
            }
            curY = curY + 36;
            curX = startX;
        }
    }

    void playMineSweeper()
    {
        int rows = canvas.getRandomInt(7,10);
        int cols = canvas.getRandomInt(6,8);

        int imageSize = 36;

        double curX = (canvas.getWidth() - (imageSize*cols) + imageSize)/2;
        double curY = (canvas.getHeight() - (imageSize*rows) + imageSize)/2;

        boolean mineTrack = true;

        int[][] field = generateField( rows, cols);

        while( isCleared(field) == false && mineTrack== true)
        {
            drawField( field, curX, curY);

            Touch touch = canvas.waitForTouch();
            double touchX = touch.getX();
            double touchY = touch.getY();
            int taps = touch.getTaps();

            int cellRow = (int) ((touchY - (curY - imageSize/2)) / imageSize);
            int cellCol = (int) ((touchX - (curX - imageSize/2)) / imageSize);

            if( field[cellRow][cellCol] < 9 && taps == 1)
            {
                openCell(field, cellRow, cellCol);
                canvas.drawImage( canvas.getWidth()/2, canvas.getHeight()/8, "winky.png");
                canvas.sleep(0.5);
                canvas.drawImage( canvas.getWidth()/2,canvas.getHeight()/8 , "smiley.png");
            }
            else if( field[cellRow][cellCol] == 9 && taps == 1)
            {
                mineTrack = false;
                openMines( field );
            }
            else if(taps == 2)
            {
                setFlag( field, cellRow,cellCol);
            }
        }

        drawField( field, curX, curY);

        double textX = canvas.getWidth()/2;
        double textY = canvas.getHeight()/5;
        if( mineTrack == true)
        {
            canvas.drawImage( canvas.getWidth()/2, canvas.getHeight()/8, "happy.png");
            canvas.drawText( textX, textY, "You Won", "Green|30");
        }
        else
        {
            canvas.drawImage( canvas.getWidth()/2, canvas.getHeight()/8, "sad.png");
            canvas.drawText( textX, textY, "You Lost", "Red|25");
        }
    }


    public void run()
    {
//        int [][] field = new int [6][7]; //prob showing different amount of 9
//        plantMines(field);
//        printTable(field);

        //     int[][] field = { { 29,  9,  2,  1,  0 },{ 3,  4,  9,  2,  1 }, { 9, 12,  1,  3,  9 },{ 1,  1,  0,  2,  9 } }; // r,c error

//    setFlag( field, 0, 0 );  // put a flag: 9→29
//        setFlag( field, 3, 2 );  // put a flag: 0→20
//        setFlag( field, 1, 1 );  // put a flag: 4→24
//        setFlag( field, 2, 1 );  // no change -- hit on 12
        //   printTable(field);
        // field not cleared -- has incorrect flags (23, 24)
//        int[][] field = { {29,  29,  12,  11, 10 }, {29,  29,  29,  12, 11 },{29,  12,  11,  13, 29 },{29,  11,  10,  12, 29 } };
//        System.out.println( "is field cleared: " + isCleared( field ) );    // displays false

//
//        int[][] field = new int[6][7];
//
//        System.out.println("Is (7, 0) valid: " + isValid( field, 7, 4 ));    // displays true
//System.out.println("Is (7, 0) valid: " + isValid( field, 7, 0 ));    // displays false
//System.out.println("Is (?, ?) valid: " + isValid( field, ?, ? ));    // displays false
//System.out.println("Is (?, ?) valid: " + isValid( field, ?, ? ));    // displays false

//        int[][] field = { { 9,  9,  0,  0,  0 },{ 0,  0,  9,  0,  0 },{ 9,  0,  0,  0,  9 },{ 0,  0,  0,  0,  9 } };
//
//        int mines = countMines( field, 1, 1 );
//        System.out.println( "mines around cell (1,1): " + mines);    // displays 4

//        int mines = countMines( field, 0, 4 );
//        System.out.println( "mines around cell (0,4): " + mines);    // displays 0

//        int[][] field = { { 9,  9,  0,  0,  0 },{ 0,  0,  9,  0,  0 },{ 9,  0,  0,  0,  9 },{ 0,  0,  0,  0,  9 } };
//
//        updateField( field );
//        printTable( field );
//        // displays
        //  9  9  2  1  0
        //  3  4  9  2  1
        //  9  2  1  3  9
        //  1  1  0  2  9

//        int[][] field = generateField( 4, 5 );

//    printTable( field );             // displays different 4x5 fields each time
        // but they should look correct; for example:
        //  9  9  2  1  0
        //  3  4  9  2  1
        //  9  2  1  3  9
        //  1  1  0  2  9
//
//        int[][] field = {{ 9,  9,  2,  1,  0 },{ 3,  4,  9,  2,  1 },{ 9,  2,  1,  3,  9 },{ 1,  1,  0,  2,  9 } };
//
//        openCell( field, 1, 3 );
//        printTable( field );
// displays [the ~ are put for emphasis, not shown]
        //  9  9  2  1    0
        //  3  4  9 ~12~  1    2→12
        //  9  2  1  3    9
        //  1  1  0  2    9

//        openCell( field, 0, 4 );
//        printTable( field );
        // displays [the ~ are put for emphasis, not shown]
        //  9  9  2 ~11  10~   1→11
        //  3  4  9 ~12  11~   12 is unchanged
        //  9  2  1  3   9
        //  1  1  0  2   9

//        openCell( field, 0, 3 );
//        printTable( field );
        // displays: no change, cell 11
        // in top row already revealed

        //        int[][] field = {{ 9,  29,   2,   1, 20 },{23,  24,   9,  12,  1 },{ 9,  12,  11,  13,  9 },{ 1,  11,  10,  12,  9 } };
//
//        drawField( field, 50, 150 );

        playMineSweeper( );
    }
}

