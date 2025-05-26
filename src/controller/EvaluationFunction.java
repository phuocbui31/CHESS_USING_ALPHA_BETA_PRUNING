package controller;
import model.ChessGameData;
import model.Piece;
import model.ChessBoard;

    public class EvaluationFunction {
        public int evaluatePosition(ChessBoard chessBoard) {
            int playerScore = 0;
            int botScore = 0;

            for (int i = 1; i < chessBoard.getPlayerPieces().length; i++) {
                if (chessBoard.getPlayerPieces()[i] != null) {
                    Piece chessPiece = chessBoard.getPlayerPieces()[i];
                    int pieceValue = chessPiece.getScored();
                    playerScore += pieceValue;

                    switch (pieceValue) {
                        case Piece.PAWN:
                            playerScore += ChessGameData.ARRAY_PLAYER_PAWN[chessPiece.getPositionInBoard()];
                            break;
                        case Piece.KNIGHT:
                            playerScore += ChessGameData.ARRAY_PLAYER_KNIGHT[chessPiece.getPositionInBoard()];
                            break;
                        case Piece.BISHOP:
                            playerScore += ChessGameData.ARRAY_PLAYER_BISHOP[chessPiece.getPositionInBoard()];
                            break;
                        case Piece.ROOK:
                            playerScore += ChessGameData.ARRAY_PLAYER_ROOK[chessPiece.getPositionInBoard()];
                            break;
                        case Piece.QUEEN:
                            playerScore += ChessGameData.ARRAY_PLAYER_QUEEN[chessPiece.getPositionInBoard()];
                            break;
                        case Piece.KING:
                            playerScore += ChessGameData.ARRAY_PLAYER_KING[chessPiece.getPositionInBoard()];
                            break;
                    }
                }

                if (chessBoard.getBotPieces()[i] != null) {
                    Piece chessPiece = chessBoard.getBotPieces()[i];
                    int pieceValue = chessPiece.getScored();
                    botScore += pieceValue;

                    switch (pieceValue) {
                        case Piece.PAWN:
                            botScore += ChessGameData.ARRAY_BOT_PAWN[chessPiece.getPositionInBoard()];
                            break;
                        case Piece.KNIGHT:
                            botScore += ChessGameData.ARRAY_BOT_KNIGHT[chessPiece.getPositionInBoard()];
                            break;
                        case Piece.BISHOP:
                            botScore += ChessGameData.ARRAY_PLAYER_BISHOP[chessPiece.getPositionInBoard()];
                            break;
                        case Piece.ROOK:
                            botScore += ChessGameData.ARRAY_BOT_ROOK[chessPiece.getPositionInBoard()];
                            break;
                        case Piece.QUEEN:
                            botScore += ChessGameData.ARRAY_BOT_QUEEN[chessPiece.getPositionInBoard()];
                            break;
                        case Piece.KING:
                            botScore += ChessGameData.ARRAY_BOT_KING[chessPiece.getPositionInBoard()];
                            break;
                    }
                }
            }

            return playerScore - botScore;
        }
    }

