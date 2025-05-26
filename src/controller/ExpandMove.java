package controller;

import model.ChessGameData;
import model.Move;
import model.Piece;
import model.ChessBoard;

import java.util.ArrayList;
import java.util.List;

public class ExpandMove {
    ChessBoard chessBoard;
    int player;
    List<ChessBoard> chessBoards = new ArrayList<ChessBoard>();
    ChessGameRule chessGame;

    public ExpandMove(ChessBoard chessBoard, int player) {
        this.chessBoard = chessBoard;
        this.player = player;
        this.chessGame = new ChessGameRule(chessBoard);
    }

    public ChessBoard[] getArrayChessBoard() {
        return chessBoards.toArray(new ChessBoard[chessBoards.size()]);
    }

    public void expandMove() {
        if (player == ChessGameData.PLAYER) {
            for (int i = 0; i < chessBoard.getPlayerPieces().length; i++) {
                Piece piece = chessBoard.getPlayerPieces()[i];
                if (piece == null) continue;
                switch (piece.getScored()) {
                    case ChessGameData.PAWN_SCORED: // tốt
                        expandPawnMove(piece, ChessGameData.PLAYER);
                        break;
                    case ChessGameData.KNIGHT_SCORED: // mã
                        expandKnightMove(piece, ChessGameData.PLAYER);
                        break;
                    case ChessGameData.BISHOP_SCORED:
                        expandBishopMove(piece, ChessGameData.PLAYER);
                        break;
                    case ChessGameData.ROOK_SCORED:
                        expandRookMove(piece, ChessGameData.PLAYER);
                        break;
                    case ChessGameData.QUEEN_SCORED:
                        expandQueenMove(piece, ChessGameData.PLAYER);
                        break;
                    case ChessGameData.KING_SCORED: // vua
                        expandKingMove(piece, ChessGameData.PLAYER);
                        break;
                }
            }
        } else {
            for (int i = 0; i < chessBoard.getBotPieces().length; i++) {
                Piece piece = chessBoard.getBotPieces()[i];
                if (piece == null) continue;
                switch (piece.getScored()) {
                    case ChessGameData.PAWN_SCORED: // tốt
                        expandPawnMove(piece, ChessGameData.AI);
                        break;
                    case ChessGameData.KNIGHT_SCORED: // mã
                        expandKnightMove(piece, ChessGameData.AI);
                        break;
                    case ChessGameData.BISHOP_SCORED:
                        expandBishopMove(piece, ChessGameData.AI);
                        break;
                    case ChessGameData.ROOK_SCORED:
                        expandRookMove(piece, ChessGameData.AI);
                        break;
                    case ChessGameData.QUEEN_SCORED:
                        expandQueenMove(piece, ChessGameData.AI);
                        break;
                    case ChessGameData.KING_SCORED: // vua
                        expandKingMove(piece, ChessGameData.AI);
                        break;
                }
            }
        }
    }

    public void expandPawnMove(Piece pawn, int targetPlayer) {
        if (targetPlayer == 1) {
            int position = pawn.getPositionInBoard();
            int[] arrayDirection = ChessGameData.PAWN_DIRECTIONS;
            int newPosition = position + arrayDirection[0];
            int valueInBoard = chessBoard.getBoard()[newPosition];
            if (valueInBoard != ChessGameData.BORDER) { // đi lên 1 ô
                if (valueInBoard == ChessGameData.EMPTY_SPACE &&
                        chessGame.kingIsSafe(targetPlayer, position, newPosition)) {
                    chessBoards.add(new ChessBoard(this.chessBoard, new Move(position, newPosition)));
                }
            }

            if (position > 80 && valueInBoard == ChessGameData.EMPTY_SPACE &&
                    chessBoard.getBoard()[position + arrayDirection[1]] == ChessGameData.EMPTY_SPACE &&
                    chessGame.kingIsSafe(targetPlayer, position, position + arrayDirection[1])) { // đi lên 2 ô
                chessBoards.add(new ChessBoard(this.chessBoard, new Move(position, position + arrayDirection[1])));
            }

            int topRightPosition = chessBoard.getBoard()[position + arrayDirection[2]];
            if (topRightPosition != ChessGameData.BORDER) { // có thể đi chéo phải
                if (topRightPosition < 0 &&
                        chessGame.kingIsSafe(targetPlayer, position, position + arrayDirection[2])) {
                    chessBoards.add(new ChessBoard(this.chessBoard, new Move(position, position + arrayDirection[2])));
                }
            }

            int topLeftPosition = chessBoard.getBoard()[position + arrayDirection[3]];
            if (topLeftPosition != ChessGameData.BORDER) { // có thể đi chéo trái
                if (topLeftPosition < 0 && chessGame.kingIsSafe(targetPlayer, position, position + arrayDirection[3])) {
                    chessBoards.add(new ChessBoard(this.chessBoard, new Move(position, position + arrayDirection[3])));
                }
            }
        } else {
            int position = pawn.getPositionInBoard();
            int[] arrayDirection = ChessGameData.PAWN_DIRECTIONS;
            int newPosition = chessBoard.getBoard()[position + arrayDirection[4]];
            if (newPosition != ChessGameData.BORDER) { // đi lên 1 ô
                if (newPosition == ChessGameData.EMPTY_SPACE &&
                        chessGame.kingIsSafe(targetPlayer, position, position + arrayDirection[4])) {
                    chessBoards.add(new ChessBoard(this.chessBoard, new Move(position, position + arrayDirection[4])));
                }
            }

            if (position < 39 && newPosition == ChessGameData.EMPTY_SPACE &&
                    chessBoard.getBoard()[arrayDirection[5]] == ChessGameData.EMPTY_SPACE &&
                    chessGame.kingIsSafe(targetPlayer, position, position + arrayDirection[5])) { // đi lên 2 ô
                chessBoards.add(new ChessBoard(this.chessBoard, new Move(position, position + arrayDirection[5])));
            }

            int topRightPosition = chessBoard.getBoard()[position + arrayDirection[6]];
            if (topRightPosition != ChessGameData.BORDER) { // có thể đi chéo phải
                if (topRightPosition > 0 && chessGame.kingIsSafe(targetPlayer, position, position + arrayDirection[6])
                        && topRightPosition != ChessGameData.EMPTY_SPACE) {
                    chessBoards.add(new ChessBoard(this.chessBoard, new Move(position, position + arrayDirection[6])));
                }
            }

            int topLeftPosition = chessBoard.getBoard()[position + arrayDirection[7]];
            if (topLeftPosition != ChessGameData.BORDER) { // có thể đi chéo trái
                if (topLeftPosition > 0 && chessGame.kingIsSafe(targetPlayer, position, position + arrayDirection[7])
                        && topLeftPosition != ChessGameData.EMPTY_SPACE) {
                    chessBoards.add(new ChessBoard(this.chessBoard, new Move(position, position + arrayDirection[7])));
                }
            }
        }

    }

    public void expandBishopMove(Piece bishop, int targetPlayer) {
        int position = bishop.getPositionInBoard();
        int[] arrayDirection = ChessGameData.BISHOP_DIRECTIONS;
        if (targetPlayer == 1) {
            for (int i = 0; i < arrayDirection.length; i++) {
                int newDirection = position + arrayDirection[i];
                while (true) {
                    int valueInBoard = chessBoard.getBoard()[newDirection];
                    if (valueInBoard == ChessGameData.BORDER) break;
                    boolean isKingSafe = chessGame.kingIsSafe(ChessGameData.PLAYER, position, newDirection);
                    if (valueInBoard == ChessGameData.EMPTY_SPACE || valueInBoard < 0) {
                        if (isKingSafe) {
                            chessBoards.add(new ChessBoard(chessBoard, new Move(position, newDirection)));
                            if (valueInBoard != ChessGameData.EMPTY_SPACE || !isKingSafe) {
                                break;
                            }
                        } else if (valueInBoard != ChessGameData.EMPTY_SPACE) break;
                    } else if (valueInBoard > 0 && valueInBoard != ChessGameData.EMPTY_SPACE) break;
                    newDirection += arrayDirection[i];
                }
            }
        } else {
            for (int i = 0; i < arrayDirection.length; i++) {
                int newDirection = position + arrayDirection[i];
                while (true) {
                    int valueInBoard = chessBoard.getBoard()[newDirection];
                    if (valueInBoard == ChessGameData.BORDER) break;
                    boolean isKingSafe = chessGame.kingIsSafe(ChessGameData.AI, position, newDirection);
                    if (valueInBoard == ChessGameData.EMPTY_SPACE || valueInBoard > 0) {
                        if (isKingSafe) {
                            chessBoards.add(new ChessBoard(chessBoard, new Move(position, newDirection)));
                            if (valueInBoard != ChessGameData.EMPTY_SPACE || !isKingSafe) {
                                break;
                            }
                        } else if (valueInBoard != ChessGameData.EMPTY_SPACE) break;
                    } else if (valueInBoard < 0) break;
                    newDirection += arrayDirection[i];
                }
            }
        }
    }

    public void expandRookMove(Piece rook, int targetPlayer) {
        int position = rook.getPositionInBoard();
        int[] arrayDirection = ChessGameData.ROOK_DIRECTIONS;
        if (targetPlayer == 1) {
            for (int i = 0; i < arrayDirection.length; i++) {
                int newDirection = position + arrayDirection[i];
                while (true) {
                    int valueInBoard = chessBoard.getBoard()[newDirection];
                    if (valueInBoard == ChessGameData.BORDER) break;
                    boolean isKingSafe = chessGame.kingIsSafe(ChessGameData.PLAYER, position, newDirection);
                    if (valueInBoard == ChessGameData.EMPTY_SPACE || valueInBoard < 0) {
                        if (isKingSafe) {
                            chessBoards.add(new ChessBoard(chessBoard, new Move(position, newDirection)));
                            if (valueInBoard != ChessGameData.EMPTY_SPACE || !isKingSafe) {
                                break;
                            }
                        } else if (valueInBoard != ChessGameData.EMPTY_SPACE) break;
                    } else if (valueInBoard > 0 && valueInBoard != ChessGameData.EMPTY_SPACE) break;
                    newDirection += arrayDirection[i];
                }
            }
        } else {
            for (int i = 0; i < arrayDirection.length; i++) {
                int newDirection = position + arrayDirection[i];
                while (true) {
                    int valueInBoard = chessBoard.getBoard()[newDirection];
                    if (valueInBoard == ChessGameData.BORDER) break;
                    boolean isKingSafe = chessGame.kingIsSafe(ChessGameData.AI, position, newDirection);
                    if (valueInBoard == ChessGameData.EMPTY_SPACE || valueInBoard > 0) {
                        if (isKingSafe) {
                            chessBoards.add(new ChessBoard(chessBoard, new Move(position, newDirection)));
                            if (valueInBoard != ChessGameData.EMPTY_SPACE || !isKingSafe) {
                                break;
                            }
                        } else if (valueInBoard != ChessGameData.EMPTY_SPACE) break;
                    } else if (valueInBoard < 0) break;
                    newDirection += arrayDirection[i];
                }
            }
        }
    }

    public void expandQueenMove(Piece queen, int targetPlayer) {

        int position = queen.getPositionInBoard();
        int[] arrayDirection = ChessGameData.QUEEN_DIRECTIONS;
        if (targetPlayer == 1) {
            for (int i = 0; i < arrayDirection.length; i++) {
                int newDirection = position + arrayDirection[i];
                while (true) {
                    int valueInBoard = chessBoard.getBoard()[newDirection];
                    if (valueInBoard == ChessGameData.BORDER) break;
                    boolean isKingSafe = chessGame.kingIsSafe(ChessGameData.PLAYER, position, newDirection);
                    if (valueInBoard == ChessGameData.EMPTY_SPACE || valueInBoard < 0) {
                        if (isKingSafe) {
                            chessBoards.add(new ChessBoard(chessBoard, new Move(position, newDirection)));
                            if (valueInBoard != ChessGameData.EMPTY_SPACE || !isKingSafe) {
                                break;
                            }
                        } else if (valueInBoard != ChessGameData.EMPTY_SPACE) break;
                    } else if (valueInBoard > 0 && valueInBoard != ChessGameData.EMPTY_SPACE) break;
                    newDirection += arrayDirection[i];
                }
            }
        } else {
            for (int i = 0; i < arrayDirection.length; i++) {
                int newDirection = position + arrayDirection[i];
                while (true) {
                    int valueInBoard = chessBoard.getBoard()[newDirection];
                    if (valueInBoard == ChessGameData.BORDER) break;
                    boolean isKingSafe = chessGame.kingIsSafe(ChessGameData.AI, position, newDirection);
                    if (valueInBoard == ChessGameData.EMPTY_SPACE || valueInBoard > 0) {
                        if (isKingSafe) {
                            chessBoards.add(new ChessBoard(chessBoard, new Move(position, newDirection)));
                            if (valueInBoard != ChessGameData.EMPTY_SPACE || !isKingSafe) {
                                break;
                            }
                        } else if (valueInBoard != ChessGameData.EMPTY_SPACE) break;
                    } else if (valueInBoard < 0) break;
                    newDirection += arrayDirection[i];
                }
            }
        }
    }

    public void expandKnightMove(Piece knight, int targetPlayer) {
        int k = 1;
        if (targetPlayer != 1) k = -1;
        int position = knight.getPositionInBoard();
        int[] arrayDirection = ChessGameData.KNIGHT_DIRECTIONS;
        for (int i = 0; i < arrayDirection.length; i++) {
            int valueInBoard = chessBoard.getBoard()[position + arrayDirection[i]];
            if (valueInBoard != ChessGameData.BORDER &&
                    (valueInBoard == ChessGameData.EMPTY_SPACE || valueInBoard * k < 0) &&
                    chessGame.kingIsSafe(targetPlayer, position, position + arrayDirection[i])) {
                chessBoards.add(new ChessBoard(chessBoard, new Move(position, position + arrayDirection[i])));
            }
        }
    }


    public void expandKingMove(Piece piece, int targetPlayer) {
        int k = 1;
        if (targetPlayer != 1) k = -1;
        int position = piece.getPositionInBoard();
        int[] arrayDirection = ChessGameData.KING_DIRECTIONS;
        for (int i = 0; i < arrayDirection.length; i++) {
            int valueInBoard = chessBoard.getBoard()[position + arrayDirection[i]];
            if (valueInBoard != ChessGameData.BORDER &&
                    (valueInBoard == ChessGameData.EMPTY_SPACE || valueInBoard * k < 0) &&
                    chessGame.kingIsSafe(targetPlayer, position, position + arrayDirection[i])) {
                chessBoards.add(new ChessBoard(chessBoard, new Move(position, position + arrayDirection[i])));
            }
        }
    }

//    public void newMovesPawnHuman(Piece pawn) {
//        int piecePosition = pawn.piecePosition;
//        // try moving up 1 square
//        int newPosition = chessBoard.board[piecePosition - 10];
//        if (newPosition != ChessGameData.BORDER) {
//            // not hitting the board boundary
//            if (newPosition == ChessGameData.EMPTY_SPACE && chessGame.kingIsSafe(ChessGameData.PLAYER, piecePosition, piecePosition - 10)) {
//                // moving up 1 square if empty and safe
//                chessBoardChessBoards.add(new ChessBoard(chessBoard, new Move(piecePosition, piecePosition - 10)));
//            }
//        }
//
//        if (piecePosition > 80 && newPosition == ChessGameData.EMPTY_SPACE &&
//                chessBoard.board[piecePosition - 20] == ChessGameData.EMPTY_SPACE &&
//                chessGame.kingIsSafe(ChessGameData.PLAYER, piecePosition, piecePosition - 20)) {
//            // moving up 2 squares if in starting position, both squares are empty, and the move is safe
//            chessBoardChessBoards.add(new ChessBoard(chessBoard, new Move(piecePosition, piecePosition - 20)));
//        }
//
//        int rightDiagonalPosition = chessBoard.board[piecePosition - 9];
//        if (rightDiagonalPosition != ChessGameData.BORDER) {
//            if (rightDiagonalPosition < 0 && chessGame.kingIsSafe(ChessGameData.PLAYER, piecePosition, piecePosition - 9)) {
//                // moving diagonally right if capturing an opponent's piece and safe
//                chessBoardChessBoards.add(new ChessBoard(chessBoard, new Move(piecePosition, piecePosition - 9)));
//            }
//        }
//
//        int leftDiagonalPosition = chessBoard.board[piecePosition - 11];
//        if (leftDiagonalPosition != ChessGameData.BORDER) {
//            if (leftDiagonalPosition < 0 && chessGame.kingIsSafe(ChessGameData.PLAYER, piecePosition, piecePosition - 11)) {
//                // moving diagonally left if capturing an opponent's piece and safe
//                chessBoardChessBoards.add(new ChessBoard(chessBoard, new Move(piecePosition, piecePosition - 11)));
//            }
//        }
//    }
//
//    public void newMovesPawnBot(Piece pawn) {
//        int currentPosition = pawn.piecePosition;
//        int newPosition = chessBoard.board[currentPosition + 10];
//
//        if (newPosition != ChessGameData.BORDER) {
//            if (newPosition == ChessGameData.EMPTY_SPACE && chessGame.kingIsSafe(ChessGameData.AI, currentPosition, currentPosition + 10)) {
//                // Can move to the next square
//                chessBoardChessBoards.add(new ChessBoard(chessBoard, new Move(currentPosition, currentPosition + 10)));
//            }
//        }
//
//        if (currentPosition < 39 && newPosition == ChessGameData.EMPTY_SPACE &&
//                chessBoard.board[currentPosition + 20] == ChessGameData.EMPTY_SPACE && chessGame.kingIsSafe(ChessGameData.AI, currentPosition, currentPosition + 20)) {
//            // Can move two squares forward
//            chessBoardChessBoards.add(new ChessBoard(chessBoard, new Move(currentPosition, currentPosition + 20)));
//        }
//
//        int newPositionDiagonalRight = chessBoard.board[currentPosition + 11];
//        if (newPositionDiagonalRight != ChessGameData.BORDER) {
//            if (newPositionDiagonalRight > 0 && newPositionDiagonalRight != ChessGameData.EMPTY_SPACE &&
//                    chessGame.kingIsSafe(ChessGameData.AI, currentPosition, currentPosition + 11))
//                // Can move diagonally to the right
//                chessBoardChessBoards.add(new ChessBoard(chessBoard, new Move(currentPosition, currentPosition + 11)));
//        }
//
//        int newPositionDiagonalLeft = chessBoard.board[currentPosition + 9];
//        if (newPositionDiagonalLeft != ChessGameData.BORDER) {
//            if (newPositionDiagonalLeft > 0 && newPositionDiagonalLeft != ChessGameData.EMPTY_SPACE &&
//                    chessGame.kingIsSafe(ChessGameData.AI, currentPosition, currentPosition + 9))
//                // Can move diagonally to the left
//                chessBoardChessBoards.add(new ChessBoard(chessBoard, new Move(currentPosition, currentPosition + 9)));
//        }
//    }
//
//    public void newMovesKnightHuman(Piece knight) {
//        int currentPosition = knight.piecePosition;
//        int[] possibleMoves = {currentPosition - 21, currentPosition + 21, currentPosition + 19, currentPosition - 19,
//                currentPosition - 12, currentPosition + 12, currentPosition - 8, currentPosition + 8};
//        for (int i = 0; i < possibleMoves.length; i++) {
//            int newPosition = chessBoard.board[possibleMoves[i]];
//            if (newPosition != ChessGameData.BORDER && (newPosition == ChessGameData.EMPTY_SPACE || newPosition < 0)
//                    && chessGame.kingIsSafe(ChessGameData.PLAYER, currentPosition, possibleMoves[i])) {
//                chessBoardChessBoards.add(new ChessBoard(chessBoard, new Move(currentPosition, possibleMoves[i])));
//            }
//        }
//    }
//
//    public void newMovesKnightBot(Piece knight) {
//        int currentPosition = knight.piecePosition;
//        int[] possibleMoves = {currentPosition - 21, currentPosition + 21, currentPosition + 19, currentPosition - 19,
//                currentPosition - 12, currentPosition + 12, currentPosition - 8, currentPosition + 8};
//        for (int i = 0; i < possibleMoves.length; i++) {
//            int newPosition = chessBoard.board[possibleMoves[i]];
//            if (newPosition != ChessGameData.BORDER && (newPosition == ChessGameData.EMPTY_SPACE || newPosition > 0) &&
//                    chessGame.kingIsSafe(ChessGameData.AI, currentPosition, possibleMoves[i])) {
//                chessBoardChessBoards.add(new ChessBoard(chessBoard, new Move(currentPosition, possibleMoves[i])));
//            }
//        }
//    }
//
//    public void newMovesRookHuman(Piece rook) {
//        int positionRook = rook.piecePosition;
//        int[] directions = {1, -1, 10, -10};
//        for (int i = 0; i < directions.length; i++) {
//            int newDirection = positionRook + directions[i];
//            while (true) {
//                int newPosition = chessBoard.board[newDirection];
//                if (newPosition == ChessGameData.BORDER) {
//                    break;
//                }
//                boolean kingSafe = chessGame.kingIsSafe(ChessGameData.PLAYER, positionRook, newDirection);
//                if (newPosition == ChessGameData.EMPTY_SPACE || newPosition < 0) {
//                    if (kingSafe) {
//                        chessBoardChessBoards.add(new ChessBoard(chessBoard, new Move(positionRook, newDirection)));
//                        if (newPosition != ChessGameData.EMPTY_SPACE) {
//                            break;
//                        }
//                    } else if (newPosition != ChessGameData.EMPTY_SPACE) {
//                        break;
//                    }
//                } else if (newPosition > 0 && newPosition != ChessGameData.EMPTY_SPACE) {
//                    break;
//                }
//                newDirection += directions[i];
//            }
//        }
//    }
//
//    public void newMovesRookBot(Piece rook) {
//        int positionRook = rook.piecePosition;
//        int[] directions = {1, -1, 10, -10};
//        for (int i = 0; i < directions.length; i++) {
//            int newDirection = positionRook + directions[i];
//            while (true) {
//                int newPosition = chessBoard.board[newDirection];
//                if (newPosition == ChessGameData.BORDER) {
//                    break;
//                }
//                boolean kingSafe = chessGame.kingIsSafe(ChessGameData.AI, positionRook, newDirection);
//                if (newPosition == ChessGameData.EMPTY_SPACE || newPosition > 0) {
//                    if (kingSafe) {
//                        chessBoardChessBoards.add(new ChessBoard(chessBoard, new Move(positionRook, newDirection)));
//                        if (newPosition != ChessGameData.EMPTY_SPACE) {
//                            break;
//                        }
//                    } else if (newPosition != ChessGameData.EMPTY_SPACE) {
//                        break;
//                    }
//                } else if (newPosition < 0) {
//                    break;
//                }
//                newDirection += directions[i];
//            }
//        }
//    }
//
//    public void newMovesKingHuman(Piece king) {
//        int positionKing = king.piecePosition;
//        int[] possibleMoves = {positionKing + 1, positionKing - 1, positionKing + 10, positionKing - 10,
//                positionKing + 11, positionKing - 11, positionKing + 9, positionKing - 9};
//        for (int i = 0; i < possibleMoves.length; i++) {
//            int newPosition = chessBoard.board[possibleMoves[i]];
//            if (newPosition != ChessGameData.BORDER && (newPosition == ChessGameData.EMPTY_SPACE || newPosition < 0)
//                    && chessGame.kingIsSafe(ChessGameData.PLAYER, positionKing, possibleMoves[i])) {
//                chessBoardChessBoards.add(new ChessBoard(chessBoard, new Move(positionKing, possibleMoves[i])));
//            }
//        }
//    }
//
//    public void newMovesKingBot(Piece king) {
//        int positionKing = king.piecePosition;
//        int[] possibleMoves = {positionKing + 1, positionKing - 1, positionKing + 10, positionKing - 10,
//                positionKing + 11, positionKing - 11, positionKing + 9, positionKing - 9};
//        for (int i = 0; i < possibleMoves.length; i++) {
//            int newPosition = chessBoard.board[possibleMoves[i]];
//            if (newPosition != ChessGameData.BORDER && (newPosition == ChessGameData.EMPTY_SPACE || newPosition > 0) &&
//                    chessGame.kingIsSafe(ChessGameData.AI, positionKing, possibleMoves[i])) {
//                chessBoardChessBoards.add(new ChessBoard(chessBoard, new Move(positionKing, possibleMoves[i])));
//            }
//        }
//    }
//
//    public void newMovesBishopHuman(Piece bishop) {
//        int currentPosition = bishop.piecePosition;
//        int[] directions = {11, -11, 9, -9};
//        for (int i = 0; i < directions.length; i++) {
//            int newDirection = currentPosition + directions[i];
//            while (true) {
//                int newPosition = chessBoard.board[newDirection];
//                if (newPosition == ChessGameData.BORDER) {
//                    break;
//                }
//                boolean isKingSafe = chessGame.kingIsSafe(ChessGameData.PLAYER, currentPosition, newDirection);
//                if (newPosition == ChessGameData.EMPTY_SPACE || newPosition < 0) {
//                    if (isKingSafe) {
//                        chessBoardChessBoards.add(new ChessBoard(chessBoard, new Move(currentPosition, newDirection)));
//                        if (newPosition != ChessGameData.EMPTY_SPACE || !isKingSafe) {
//                            break;
//                        }
//                    } else if (newPosition != ChessGameData.EMPTY_SPACE) {
//                        break;
//                    }
//                } else if (newPosition > 0 && newPosition != ChessGameData.EMPTY_SPACE) {
//                    break;
//                }
//                newDirection += directions[i];
//            }
//        }
//    }
//
//    public void newMovesBishopBot(Piece bishop) {
//        int currentPosition = bishop.piecePosition;
//        int[] directions = {11, -11, 9, -9};
//        for (int i = 0; i < directions.length; i++) {
//            int newDirection = currentPosition + directions[i];
//            while (true) {
//                int newPosition = chessBoard.board[newDirection];
//                if (newPosition == ChessGameData.BORDER) {
//                    break;
//                }
//                boolean isKingSafe = chessGame.kingIsSafe(ChessGameData.AI, currentPosition, newDirection);
//                if (newPosition == ChessGameData.EMPTY_SPACE || newPosition > 0) {
//                    if (isKingSafe) {
//                        chessBoardChessBoards.add(new ChessBoard(chessBoard, new Move(currentPosition, newDirection)));
//                        if (newPosition != ChessGameData.EMPTY_SPACE || !isKingSafe) {
//                            break;
//                        }
//                    } else if (newPosition != ChessGameData.EMPTY_SPACE) {
//                        break;
//                    }
//                } else if (newPosition < 0) {
//                    break;
//                }
//                newDirection += directions[i];
//            }
//        }
//    }
//
//    public void newMovesQueenHuman(Piece queen) {
//        int currentPosition = queen.piecePosition;
//        int[] directions = {1, -1, 10, -10, 11, -11, 9, -9};
//        for (int i = 0; i < directions.length; i++) {
//            int newDirection = currentPosition + directions[i];
//            while (true) {
//                int newPosition = chessBoard.board[newDirection];
//                if (newPosition == ChessGameData.BORDER) {
//                    break;
//                }
//                boolean isKingSafe = chessGame.kingIsSafe(ChessGameData.PLAYER, currentPosition, newDirection);
//                if (newPosition == ChessGameData.EMPTY_SPACE || newPosition < 0) {
//                    if (isKingSafe) {
//                        chessBoardChessBoards.add(new ChessBoard(chessBoard, new Move(currentPosition, newDirection)));
//                        if (newPosition != ChessGameData.EMPTY_SPACE) {
//                            break;
//                        }
//                    } else if (newPosition != ChessGameData.EMPTY_SPACE) {
//                        break;
//                    }
//                } else if (newPosition > 0 && newPosition != ChessGameData.EMPTY_SPACE) {
//                    break;
//                }
//                newDirection += directions[i];
//            }
//        }
//    }
//
//    public void newMovesQueenBot(Piece queen) {
//        int currentPosition = queen.piecePosition;
//        int[] directions = {1, -1, 10, -10, 11, -11, 9, -9};
//        for (int i = 0; i < directions.length; i++) {
//            int newDirection = currentPosition + directions[i];
//            while (true) {
//                int newPosition = chessBoard.board[newDirection];
//                if (newPosition == ChessGameData.BORDER) {
//                    break;
//                }
//                boolean isKingSafe = chessGame.kingIsSafe(ChessGameData.AI, currentPosition, newDirection);
//                if (newPosition == ChessGameData.EMPTY_SPACE || newPosition > 0) {
//                    if (isKingSafe) {
//                        chessBoardChessBoards.add(new ChessBoard(chessBoard, new Move(currentPosition, newDirection)));
//                        if (newPosition != ChessGameData.EMPTY_SPACE) {
//                            break;
//                        }
//                    } else if (newPosition != ChessGameData.EMPTY_SPACE) {
//                        break;
//                    }
//                } else if (newPosition < 0) {
//                    break;
//                }
//                newDirection += directions[i];
//            }
//        }
//    }
}
