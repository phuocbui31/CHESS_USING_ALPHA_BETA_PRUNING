package model;

public class Piece {
    public final static int PAWN = 100;
    public final static int KNIGHT = 320;
    public final static int BISHOP = 325;
    public final static int ROOK = 500;
    public final static int QUEEN = 900;
    public final static int KING = 1000000;

    private int scored;
    private int piecePosition;
    private boolean canMove;

    public int getScored() {
        return scored;
    }

    public Piece(int scored, int piecePosition) {
        this(scored, piecePosition, false);
    }

    public Piece(int scored, int piecePosition, boolean canMove) {
        this.scored = scored;
        this.piecePosition = piecePosition;
        this.canMove = canMove;
    }
    public boolean isCanMove(){
        return canMove;
    }
    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

    @Override
    public Piece clone() {
        return new Piece(scored, piecePosition, canMove);
    }
    public void setPositionInBoard(int positionInBoard) {
        this.piecePosition = positionInBoard;
    }
    public int getPositionInBoard() {
        return piecePosition;
    }

}
