package model;

public class ChessBoard {
    private Move move;
    private int[] chessBoard = new int[120];
    private Piece[] playerPieces = new Piece[17];
    private Piece[] botPieces = new Piece[17];



    public ChessBoard() {
        for (int i = 0; i < chessBoard.length; i++) {
            chessBoard[i] = ChessGameData.EMPTY_SPACE;
        }
    }

    public ChessBoard(ChessBoard chessBoard) {
        this(chessBoard, null);
    }

    @Override
    public String toString() {
        return "Move [" + move + "]";
    }

    public ChessBoard(ChessBoard chessBoard, Move move) {
        System.arraycopy(chessBoard.chessBoard, 0, this.chessBoard, 0, this.chessBoard.length);
        for (int i = 1; i < playerPieces.length; i++) {
            if (chessBoard.playerPieces[i] != null) {
                this.playerPieces[i] = chessBoard.playerPieces[i].clone();
            }
            if (chessBoard.botPieces[i] != null) {
                this.botPieces[i] = chessBoard.botPieces[i].clone();
            }
        }
        if (move != null) update(move);
    }

    public void createChessBoard(boolean isWhite) {
        playerPieces[1] = new Piece(Piece.KNIGHT, 92);
        playerPieces[2] = new Piece(Piece.KNIGHT, 97);
        playerPieces[3] = new Piece(Piece.BISHOP, 93);
        playerPieces[4] = new Piece(Piece.BISHOP, 96);
        playerPieces[5] = new Piece(Piece.ROOK, 91);
        playerPieces[6] = new Piece(Piece.ROOK, 98);
        playerPieces[7] = new Piece(Piece.QUEEN, isWhite ? 94 : 95);
        playerPieces[8] = new Piece(Piece.KING, isWhite ? 95 : 94);

        botPieces[1] = new Piece(Piece.KNIGHT, 22);
        botPieces[2] = new Piece(Piece.KNIGHT, 27);
        botPieces[3] = new Piece(Piece.BISHOP, 23);
        botPieces[4] = new Piece(Piece.BISHOP, 26);
        botPieces[5] = new Piece(Piece.ROOK, 21);
        botPieces[6] = new Piece(Piece.ROOK, 28);
        botPieces[7] = new Piece(Piece.QUEEN, isWhite ? 24 : 25);
        botPieces[8] = new Piece(Piece.KING, isWhite ? 25 : 24);

        int j = 81;
        for (int i = 9; i < playerPieces.length; i++) {
            playerPieces[i] = new Piece(Piece.PAWN, j);
            botPieces[i] = new Piece(Piece.PAWN, j - 50);
            j++;
        }
        chessBoard = new int[]{
                ChessGameData.BORDER, ChessGameData.BORDER, ChessGameData.BORDER, ChessGameData.BORDER, ChessGameData.BORDER, ChessGameData.BORDER, ChessGameData.BORDER, ChessGameData.BORDER, ChessGameData.BORDER, ChessGameData.BORDER,
                ChessGameData.BORDER, ChessGameData.BORDER, ChessGameData.BORDER, ChessGameData.BORDER, ChessGameData.BORDER, ChessGameData.BORDER, ChessGameData.BORDER, ChessGameData.BORDER, ChessGameData.BORDER, ChessGameData.BORDER,
                ChessGameData.BORDER, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.BORDER,
                ChessGameData.BORDER, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.BORDER,
                ChessGameData.BORDER, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.BORDER,
                ChessGameData.BORDER, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.BORDER,
                ChessGameData.BORDER, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.BORDER,
                ChessGameData.BORDER, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.BORDER,
                ChessGameData.BORDER, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.BORDER,
                ChessGameData.BORDER, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.BORDER,
                ChessGameData.BORDER, ChessGameData.BORDER, ChessGameData.BORDER, ChessGameData.BORDER, ChessGameData.BORDER, ChessGameData.BORDER, ChessGameData.BORDER, ChessGameData.BORDER, ChessGameData.BORDER, ChessGameData.BORDER,
                ChessGameData.BORDER, ChessGameData.BORDER, ChessGameData.BORDER, ChessGameData.BORDER, ChessGameData.BORDER, ChessGameData.BORDER, ChessGameData.BORDER, ChessGameData.BORDER, ChessGameData.BORDER, ChessGameData.BORDER
        };
        for (int i = 0; i < chessBoard.length; i++) {
            for (int k = 1; k < playerPieces.length; k++) {
                if (i == playerPieces[k].getPositionInBoard()) {
                    chessBoard[i] = k;
                } else if (i == botPieces[k].getPositionInBoard()) {
                    chessBoard[i] = -k;
                }
            }
        }
    }

    public Piece[] getBotPieces() {
        return botPieces;
    }
    public Piece[] getPlayerPieces() {
        return playerPieces;
    }

    public int[] getBoard() {
        return chessBoard;
    }

    public void setMove(Move move) {
        this.move = move;
    }

    public Move getMove() {
        return move;
    }

    public void update(Move move) {
        this.move = move;
        int currentPosition = chessBoard[move.getCurrentPosition()];
        int destinationPosition = chessBoard[move.getDestinationPosition()];
        if (currentPosition > 0) {
            playerPieces[currentPosition].setCanMove(true);
            playerPieces[currentPosition].setPositionInBoard(move.getDestinationPosition());
            if (destinationPosition < 0) {
                botPieces[-destinationPosition] = null;
            }
        } else {
            botPieces[-currentPosition].setCanMove(true);
            botPieces[-currentPosition].setPositionInBoard(move.getDestinationPosition());
            if (destinationPosition > 0 && destinationPosition != ChessGameData.EMPTY_SPACE) {
                playerPieces[destinationPosition] = null;
            }
        }
        chessBoard[move.getCurrentPosition()] = ChessGameData.EMPTY_SPACE;
        chessBoard[move.getDestinationPosition()] = currentPosition;
    }

    public void displayBoard() {
        int j = 0;
        int i = 0;
        int k = 10;
        while (j < 12) {
            while (i < k) {
                System.out.print(chessBoard[i] + "\t");
                i++;
            }
            System.out.println("\n");
            k += 10;
            j++;
        }
    }
//    public Move move;
//    public int[] board = new int[120];
//    public Piece[] humanPieces = new Piece[17];
//    public Piece[] botPieces = new Piece[17];
//
//    public ChessBoard() {
//        for (int i = 0; i < board.length; i++) {
//            board[i] = ChessGameData.EMPTY_SPACE;
//        }
//    }
//
//    public int[] getBoard() {
//        return board;
//    }
//
//    public Piece[] getPlayerPieces() {
//        return humanPieces;
//    }
//
//    public Piece[] getBotPieces() {
//        return botPieces;
//    }
//
//
//    public ChessBoard(ChessBoard chessBoard) {
//        this(chessBoard, null);
//    }
//
//    @Override
//    public String toString() {
//        return "Position [move=" + move + "]";
//    }
//
//    public ChessBoard(ChessBoard chessBoard, Move move) {
//        System.arraycopy(chessBoard.board, 0, this.board, 0, board.length);
//        for (int i = 1; i < humanPieces.length; i++) {
//            if (chessBoard.humanPieces[i] != null) {
//                this.humanPieces[i] = chessBoard.humanPieces[i].clone();
//            }
//            if (chessBoard.botPieces[i] != null) {
//                this.botPieces[i] = chessBoard.botPieces[i].clone();
//            }
//        }
//        if (move != null)
//            update(move);
//    }
//
//    public void createChessBoard(boolean humanWhite) {
//        humanPieces[1] = new Piece(Piece.KNIGHT, 92);
//        humanPieces[2] = new Piece(Piece.KNIGHT, 97);
//        humanPieces[3] = new Piece(Piece.BISHOP, 93);
//        humanPieces[4] = new Piece(Piece.BISHOP, 96);
//        humanPieces[5] = new Piece(Piece.ROOK, 91);
//        humanPieces[6] = new Piece(Piece.ROOK, 98);
//        humanPieces[7] = new Piece(Piece.QUEEN, humanWhite ? 94 : 95);
//        humanPieces[8] = new Piece(Piece.KING, humanWhite ? 95 : 94);
//
//        botPieces[1] = new Piece(Piece.KNIGHT, 22);
//        botPieces[2] = new Piece(Piece.KNIGHT, 27);
//        botPieces[3] = new Piece(Piece.BISHOP, 23);
//        botPieces[4] = new Piece(Piece.BISHOP, 26);
//        botPieces[5] = new Piece(Piece.ROOK, 21);
//        botPieces[6] = new Piece(Piece.ROOK, 28);
//        botPieces[7] = new Piece(Piece.QUEEN, humanWhite ? 24 : 25);
//        botPieces[8] = new Piece(Piece.KING, humanWhite ? 25 : 24);
//
//        int j = 81;
//        for (int i = 9; i < humanPieces.length; i++) {
//            humanPieces[i] = new Piece(Piece.PAWN, j);
//            botPieces[i] = new Piece(Piece.PAWN, j - 50);
//            j++;
//        }
//        board = new int[]{
//                ChessGameData.BORDER, ChessGameData.BORDER, ChessGameData.BORDER, ChessGameData.BORDER, ChessGameData.BORDER,
//                ChessGameData.BORDER, ChessGameData.BORDER, ChessGameData.BORDER, ChessGameData.BORDER, ChessGameData.BORDER,
//                ChessGameData.BORDER, ChessGameData.BORDER, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE,
//                ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.BORDER,
//                ChessGameData.BORDER, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE,
//                ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.BORDER,
//                ChessGameData.BORDER, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE,
//                ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.BORDER,
//                ChessGameData.BORDER, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE,
//                ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.BORDER,
//                ChessGameData.BORDER, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE,
//                ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.BORDER,
//                ChessGameData.BORDER, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE,
//                ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.EMPTY_SPACE, ChessGameData.BORDER,
//                ChessGameData.BORDER, ChessGameData.BORDER, ChessGameData.BORDER, ChessGameData.BORDER, ChessGameData.BORDER,
//                ChessGameData.BORDER, ChessGameData.BORDER, ChessGameData.BORDER, ChessGameData.BORDER, ChessGameData.BORDER,
//                ChessGameData.BORDER, ChessGameData.BORDER, ChessGameData.BORDER, ChessGameData.BORDER, ChessGameData.BORDER,
//                ChessGameData.BORDER, ChessGameData.BORDER, ChessGameData.BORDER, ChessGameData.BORDER, ChessGameData.BORDER,
//        };
//        for (int i = 0; i < board.length; i++) {
//            for (int k = 1; k < humanPieces.length; k++) {
//                if (i == humanPieces[k].piecePosition) {
//                    board[i] = k;
//                } else if (i == botPieces[k].piecePosition) {
//                    board[i] = -k;
//                }
//            }
//        }
//    }
//
//    public void update(Move move) {
//        this.move = move;
//        int startSquare = board[move.startLocation];
//        int targetLocation = board[move.targetLocation];
//        if (startSquare > 0) {
//            humanPieces[startSquare].canMove = true;
//            humanPieces[startSquare].piecePosition = move.targetLocation;
//            if (targetLocation < 0) {
//                botPieces[-targetLocation] = null;
//            }
//        } else {
//            botPieces[-startSquare].canMove = true;
//            botPieces[-startSquare].piecePosition = move.targetLocation;
//            if (targetLocation > 0 && targetLocation != ChessGameData.EMPTY_SPACE) {
//                humanPieces[targetLocation] = null;
//            }
//        }
//        board[move.startLocation] = ChessGameData.EMPTY_SPACE;
//        board[move.targetLocation] = startSquare;
//    }
//
//    public void displayBoard() {
//        int j = 0;
//        int i = 0;
//        int k = 10;
//        while (j < 12) {
//            while (i < k) {
//                System.out.print(board[i] + "\t");
//                i++;
//            }
//            System.out.println("\n");
//            k += 10;
//            j++;
//        }
//    }
}

