package controller;


import model.ChessGameData;
import model.Piece;
import model.ChessBoard;
import model.Move;

public class ChessGameRule {

    ChessBoard chessBoard;
    Piece playerKing;
    Piece botKing;

    public ChessGameRule(ChessBoard chessBoard) {
        playerKing = chessBoard.getPlayerPieces()[8];
        botKing = chessBoard.getBotPieces()[8];
        this.chessBoard = chessBoard;
    }

    public int status(int player) {
        int gameState = -1;
        ExpandMove expandMove = new ExpandMove(chessBoard, player);
        expandMove.expandMove();
        ChessBoard[] chessBoardList = expandMove.getArrayChessBoard();
        if (chessBoardList.length == 0) {
            if (checkmate(player)) {
                gameState = ChessGameData.CHECKMATE;
            } else gameState = ChessGameData.LOSE;
        }
        return gameState;
    }

    public boolean kingIsSafe(int player, int startLocation, int targetLocation) {
        Move move = new Move(startLocation, targetLocation);
        ChessBoard pieceChessBoard = new ChessBoard(chessBoard, move);
        ChessGameRule chessGame = new ChessGameRule(pieceChessBoard);
        return !chessGame.checkmate(player);
    }

    public boolean checkmate(int player) {
        boolean isCheckmate = false;
        Piece king = (player == ChessGameData.PLAYER) ? playerKing : botKing;
        if (king == null) return true;
        isCheckmate = pawnCheck(king);
        if (!isCheckmate) isCheckmate = knightCheck(king);
        if (!isCheckmate) isCheckmate = bishopCheck(king);
        if (!isCheckmate) isCheckmate = rookCheck(king);
        if (!isCheckmate) isCheckmate = queenCheck(king);
        if (!isCheckmate) isCheckmate = kingCheck(king);
        return isCheckmate;
    }

    private boolean pawnCheck(Piece king) {
        boolean isCheck = false;
        int kingPosition = king.getPositionInBoard();
        if (king == playerKing) {
            int pawnRightComputer = chessBoard.getBoard()[kingPosition - 9];
            int pawnLeftComputer = chessBoard.getBoard()[kingPosition - 11];
            if (pawnRightComputer == ChessGameData.BORDER || pawnLeftComputer == ChessGameData.BORDER)
                return false;
            if (pawnRightComputer < 0 && chessBoard.getBotPieces()[-pawnRightComputer].getScored() == Piece.PAWN)
                isCheck = true;
            if (pawnLeftComputer < 0 && chessBoard.getBotPieces()[-pawnLeftComputer].getScored() == Piece.PAWN)
                isCheck = true;
        } else {
            int pawnRightHuman = chessBoard.getBoard()[kingPosition + 11];
            int pawnLeftHuman = chessBoard.getBoard()[kingPosition + 9];
            if (pawnRightHuman != ChessGameData.BORDER) {
                if (pawnRightHuman > 0 && pawnRightHuman != ChessGameData.EMPTY_SPACE &&
                        chessBoard.getPlayerPieces()[pawnRightHuman].getScored() == Piece.PAWN)
                    isCheck = true;
            }
            if (pawnLeftHuman != ChessGameData.BORDER) {
                if (pawnLeftHuman > 0 && pawnLeftHuman != ChessGameData.EMPTY_SPACE &&
                        chessBoard.getPlayerPieces()[pawnLeftHuman].getScored() == Piece.PAWN)
                    isCheck = true;
            }
        }
        return isCheck;
    }

    private boolean knightCheck(Piece king) {
        boolean isCheck = false;
        int kingPosition = king.getPositionInBoard();
        int[] knightCheckingKing =
                {kingPosition + ChessGameData.KNIGHT_DIRECTIONS[0], kingPosition + ChessGameData.KNIGHT_DIRECTIONS[1],
                        kingPosition + ChessGameData.KNIGHT_DIRECTIONS[2], kingPosition + ChessGameData.KNIGHT_DIRECTIONS[3],
                        kingPosition + ChessGameData.KNIGHT_DIRECTIONS[4], kingPosition + ChessGameData.KNIGHT_DIRECTIONS[5],
                        kingPosition + ChessGameData.KNIGHT_DIRECTIONS[6], kingPosition + ChessGameData.KNIGHT_DIRECTIONS[7]};
        for (int checkPosition : knightCheckingKing) {
            int valueInBoard = chessBoard.getBoard()[checkPosition];
            if (valueInBoard == ChessGameData.BORDER) continue;
            if (king == playerKing) {
                if (valueInBoard < 0 && chessBoard.getBotPieces()[-valueInBoard].getScored() == Piece.KNIGHT) {
                    isCheck = true;
                    break;
                }
            } else {
                if (valueInBoard > 0 && valueInBoard != ChessGameData.EMPTY_SPACE &&
                        chessBoard.getPlayerPieces()[valueInBoard].getScored() == Piece.KNIGHT) {
                    isCheck = true;
                    break;
                }
            }
        }
        return isCheck;
    }

    private boolean kingCheck(Piece king) {
        boolean isChecking = false;
        int kingPosition = king.getPositionInBoard();
        int[] checkDirections =
                {kingPosition + ChessGameData.KING_DIRECTIONS[0], kingPosition + ChessGameData.KING_DIRECTIONS[1],
                        kingPosition + ChessGameData.KING_DIRECTIONS[2], kingPosition + ChessGameData.KING_DIRECTIONS[3],
                        kingPosition + ChessGameData.KING_DIRECTIONS[4], kingPosition + ChessGameData.KING_DIRECTIONS[5],
                        kingPosition + ChessGameData.KING_DIRECTIONS[6], kingPosition + ChessGameData.KING_DIRECTIONS[7]};
        for (int checkPosition : checkDirections) {
            int valueInBoard = chessBoard.getBoard()[checkPosition];
            if (valueInBoard == ChessGameData.BORDER) continue;
            if (king == playerKing) {
                if (valueInBoard < 0 && chessBoard.getBotPieces()[-valueInBoard].getScored() == Piece.KING) {
                    isChecking = true;
                    break;
                }
            } else {
                if (valueInBoard > 0 && valueInBoard != ChessGameData.EMPTY_SPACE &&
                        chessBoard.getPlayerPieces()[valueInBoard].getScored() == Piece.KING) {
                    isChecking = true;
                    break;
                }
            }
        }
        return isChecking;
    }

    private boolean bishopCheck(Piece king) {
        boolean isChecked = false;
        int[] directions = ChessGameData.BISHOP_DIRECTIONS;
        for (int i = 0; i < directions.length; i++) {
            int checkDirections = king.getPositionInBoard() + directions[i];
            while (true) {
                int valueInBoard = chessBoard.getBoard()[checkDirections];
                if (valueInBoard == ChessGameData.BORDER) {
                    isChecked = false;
                    break;
                }
                if (king == playerKing) {
                    if (valueInBoard < 0 && chessBoard.getBotPieces()[-valueInBoard].getScored() == Piece.BISHOP) {
                        isChecked = true;
                        break;
                    } else if (valueInBoard != ChessGameData.EMPTY_SPACE) break;
                } else if (king == botKing) {
                    if (valueInBoard > 0 && valueInBoard != ChessGameData.EMPTY_SPACE &&
                            chessBoard.getPlayerPieces()[valueInBoard].getScored() == Piece.BISHOP) {
                        isChecked = true;
                        break;
                    } else if (valueInBoard != ChessGameData.EMPTY_SPACE) break;
                }
                checkDirections += directions[i];
            }
            if (isChecked) break;
        }
        return isChecked;
    }

    private boolean rookCheck(Piece king) {
        boolean isChecked = false;
        int[] directions = ChessGameData.ROOK_DIRECTIONS;
        for (int i = 0; i < directions.length; i++) {
            int checkDirections = king.getPositionInBoard() + directions[i];
            while (true) {
                int valueInBoard = chessBoard.getBoard()[checkDirections];
                if (valueInBoard == ChessGameData.BORDER) {
                    isChecked = false;
                    break;
                }
                if (king == playerKing) {
                    if (valueInBoard < 0 && chessBoard.getBotPieces()[-valueInBoard].getScored() == Piece.ROOK) {
                        isChecked = true;
                        break;
                    } else if (valueInBoard != ChessGameData.EMPTY_SPACE) break;
                } else if (king == botKing) {
                    if (valueInBoard > 0 && valueInBoard != ChessGameData.EMPTY_SPACE &&
                            chessBoard.getPlayerPieces()[valueInBoard].getScored() == Piece.ROOK) {
                        isChecked = true;
                        break;
                    } else if (valueInBoard != ChessGameData.EMPTY_SPACE) break;
                }
                checkDirections += directions[i];
            }
            if (isChecked) break;
        }
        return isChecked;
    }

    private boolean queenCheck(Piece king) {
        boolean isChecked = false;
        int[] directions = ChessGameData.QUEEN_DIRECTIONS;
        for (int i = 0; i < directions.length; i++) {
            int checkPosition = king.getPositionInBoard() + directions[i];
            while (true) {
                int valueInBoard = chessBoard.getBoard()[checkPosition];
                if (valueInBoard == ChessGameData.BORDER) {
                    isChecked = false;
                    break;
                }
                if (king == playerKing) {
                    if (valueInBoard < 0 && chessBoard.getBotPieces()[-valueInBoard].getScored() == Piece.QUEEN) {
                        isChecked = true;
                        break;
                    } else if (valueInBoard != ChessGameData.EMPTY_SPACE) break;
                } else if (king == botKing) {
                    if (valueInBoard > 0 && valueInBoard != ChessGameData.EMPTY_SPACE &&
                            chessBoard.getPlayerPieces()[valueInBoard].getScored() == Piece.QUEEN) {
                        isChecked = true;
                        break;
                    } else if (valueInBoard != ChessGameData.EMPTY_SPACE) break;
                }
                checkPosition += directions[i];
            }
            if (isChecked) break;
        }
        return isChecked;
    }

    public ChessBoard getPosition() {
        return chessBoard;
    }

    public void setChessBoard(ChessBoard chessBoard) {
        this.chessBoard = chessBoard;
    }

    public Piece getPlayerKing() {
        return playerKing;
    }

    public void setPlayerKing(Piece playerKing) {
        this.playerKing = playerKing;
    }

    public Piece getBotKing() {
        return botKing;
    }

    public void setComputerKing(Piece botKing) {
        this.botKing = botKing;
    }
}

