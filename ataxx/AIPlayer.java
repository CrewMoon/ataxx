package ataxx;

import java.util.ArrayList;
import java.util.Random;


// Final Project Part A.2 Ataxx AI Player (A group project)

/** A Player that computes its own moves. */
class AIPlayer extends Player {

    /** Maximum minimax search depth before going to static evaluation. */
    private static final int MAX_DEPTH = 4;

    /** Infinity number used in Negamax algorithm. */
    private static final int INFINITY = Integer.MAX_VALUE;

    /** Generate random numbers to select when manipulating moves in same level.
     * This generation is based on the given seed passed by. */
    private final Random randomSelector;

    /** A new AIPlayer for GAME that will play MYCOLOR.
     *  SEED is used to initialize a random-number generator,
     *  increase the value of SEED would make the AIPlayer move automatically.
     *  Identical seeds produce identical behaviour. */
    AIPlayer(Game game, PieceState myColor, long seed) {
        super(game, myColor);
        randomSelector = new Random(seed);
    }

    @Override
    boolean isAuto() {
        return true;
    }

    @Override
    String getAtaxxMove() {
        Move move = findMove();
        getAtaxxGame().reportMove(move, getMyState());
        return move.toString();
    }

    /** Return a move for me from the current position, assuming there
     *  is a move. */
    private Move findMove() {
        Board b = new Board(getAtaxxBoard());
        lastFoundMove = null;

        // Minimax Search
        miniMax(b, MAX_DEPTH, true, 1, -INFINITY, INFINITY);

        // Please do not change the codes below
        if (lastFoundMove == null) {
            lastFoundMove = Move.pass();
        }
        return lastFoundMove;
    }

    /** Use Minimax algorithm with alpha-beta pruning
     *  to RECURSIVELY find a move from position BOARD and return its
     *  alpha or beta value, recording the move found in lastFoundMove iff saveMove.
     *  For Max Operation, if SENSE == 1,
     *  The move should have maximal value or have value >= BETA -> pruning,
     *  For Min Operation, if SENSE == -1,
     *  The move should have minimal value or have value <= ALPHA -> pruning.
     *  Searches up to DEPTH levels. Searching at level 0 simply returns a static estimate
     *  of the board value and does not set lastFoundMove. If the game is over
     *  on BOARD, does not set lastFoundMove either.
     * @param board current board to find a move.
     * @param depth denotes search depth from MAX_DEPTH to 0, minus 1 reach call.
     * @param saveMove indicates only the first recursive call
     *                 should save the found move.
     * @param sense uses 1 and -1 to indicate Min (-1) or Max (1) operations.
     * @param alpha denotes the lower bound of the best known choice
     *              when searching to the current node.
     * @param beta denotes the upper bound of the worst ending
     *             when searching down from this node.
     * @return the alpha or beta value depending on min or max operation.
     */
    private int miniMax(Board board, int depth, boolean saveMove, int sense,
                        int alpha, int beta) {
        assert (sense == 1 || sense == -1) : "sense is not 1 or -1";
        assert depth >= 0 : "depth is less than 0";

        if (depth == 0 || board.getWinner() != null) {
            return score(board);
        }

        // initialize the return value
        int alphaOrBeta = 0;
        // get list of all possible MOVE including PASS
        ArrayList<Move> listOfMoves =
                possibleMoves(board, board.nextMove());
        boolean passLegal;
        try {
            passLegal = board.moveLegal(Move.pass());
        } catch (GameException illegalPass) {
            passLegal = false;
        }
        if (passLegal) {
            listOfMoves.add(Move.pass());
        }
        // make the sequence of the move list random
        makeSequenceRandom(listOfMoves);

        // max operation
        if (sense == 1) {
            for (Move move : listOfMoves) {
                Board copy = new Board(board);
                copy.createMove(move);
                // get the worst scores selected from its child moves
                int response = miniMax(copy, depth - 1, false,
                        -1, alpha, beta);
                // select the best one from these worst scores
                if (response > alpha) {
                    alpha = response;
                    if (saveMove) {
                        lastFoundMove = move;
                    }
                }
                // pruning
                if (alpha >= beta) {
                    break;
                }
            }
            alphaOrBeta = alpha;
        }
        // min operation
        else if (sense == -1) {
            for (Move move : listOfMoves) {
                Board copy = new Board(board);
                copy.createMove(move);
                // get the best scores selected from its child moves
                int response = miniMax(copy, depth - 1, false,
                        1, alpha, beta);
                // select the worst score from these best scores
                if (response < beta) {
                    beta = response;
                }
                // pruning
                if (alpha >= beta) {
                    break;
                }
            }
            alphaOrBeta = beta;
        }

        return alphaOrBeta;
    }

    /** Returns a static estimate of the board value.
     *  @return score of given Board
     */
    private int score(Board board) {
        return board.getColorNums(getMyState())
                - board.getColorNums(getMyState().opposite());
    }

    /** Make the sequence of the given moves random
     *  based on given randomSelector.
     *  @param moves the list of moves to be set randomly,
     *               it must NOT be null.
     *  @effects
     */
    private void makeSequenceRandom(ArrayList<Move> moves) {
        assert moves != null : "move list is null";

        for (int i = 0; i < moves.size(); i++) {
            int p = randomSelector.nextInt(moves.size());
            Move tmp = moves.get(i);
            moves.set(i, moves.get(p));
            moves.set(p, tmp);
        }
    }


    /** The move found by the last call to the findMove method above. */
    private Move lastFoundMove;


    /** Return all possible moves for a color.
     * @param board the current board.
     * @param myColor the specified color.
     * @return an ArrayList of all possible moves for the specified color. */
    private ArrayList<Move> possibleMoves(Board board, PieceState myColor) {
        ArrayList<Move> possibleMoves = new ArrayList<>();
        for (char row = '7'; row >= '1'; row--) {
            for (char col = 'a'; col <= 'g'; col++) {
                int index = Board.index(col, row);
                if (board.getContent(index) == myColor) {
                    ArrayList<Move> addMoves
                            = assistPossibleMoves(board, row, col);
                    possibleMoves.addAll(addMoves);
                }
            }
        }
        return possibleMoves;
    }


    /** Returns an Arraylist of legal moves.
     * @param board the board for testing
     * @param row the row coordinate of the center
     * @param col the col coordinate of the center */
    private ArrayList<Move>
        assistPossibleMoves(Board board, char row, char col) {
        ArrayList<Move> assistPossibleMoves = new ArrayList<>();
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                if (i != 0 || j != 0) {
                    char row2 = (char) (row + j);
                    char col2 = (char) (col + i);
                    Move currMove = Move.move(col, row, col2, row2);
                    if (board.moveLegal(currMove)) {
                        assistPossibleMoves.add(currMove);
                    }
                }
            }
        }
        return assistPossibleMoves;
    }
}
