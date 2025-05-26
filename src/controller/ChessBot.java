package controller;

import model.ChessGameData;
import view.Main;
import model.ChessBoard;

public class ChessBot {
    Main chessMain;
    public int depthLimit;


    public ChessBoard alphaBetaPruning(int player, ChessBoard currentChessBoard, int alpha, int beta, int depth){
        // The position here is the initial chessboard state
        if (depth == 0) return currentChessBoard;
        ChessBoard bestMove = null;
        ExpandMove expandMove = new ExpandMove(currentChessBoard, player);
        expandMove.expandMove();
        ChessBoard[] moveList = expandMove.getArrayChessBoard();

        if (moveList.length == 0) return currentChessBoard;

        // Only expand branches within the range of beta <= score <= alpha
        EvaluationFunction evaluationFunction = new EvaluationFunction();

        // All possible moves
        for (ChessBoard newChessBoard : moveList) {
            if (bestMove == null) bestMove = newChessBoard;

            if (player == ChessGameData.PLAYER) {
                // Human player chooses the minimum score
                // Alpha is the largest value selected
                ChessBoard opponentResponse = alphaBetaPruning(ChessGameData.AI, newChessBoard, alpha, beta, depth - 1);
                int score = evaluationFunction.evaluatePosition(opponentResponse);

                // Prune the branch corresponding to the score, when the score has a value of alpha, min will skip the move corresponding to that score and will not expand according to that chess position anymore
                if (score > alpha) {
                    bestMove = newChessBoard;
                    // Reassign the value of alpha
                    alpha = score;
                }
            } else {
                // AI player chooses the maximum score
                // Beta is the smallest value selected
                ChessBoard opponentResponse = alphaBetaPruning(ChessGameData.PLAYER, newChessBoard, alpha, beta, depth - 1);

                // Check for checkmate, if the opponent is in checkmate, return the move immediately
                if (new ChessGameRule(opponentResponse).checkmate(ChessGameData.PLAYER)) {
                    return newChessBoard;
                }

                int score = evaluationFunction.evaluatePosition(opponentResponse);

                // Limit the depth to 5 because a larger depth runs quite slowly
                if (score < beta && depthLimit > 5) return newChessBoard;

                // Prune the branch corresponding to the score, when the score is worse than beta, max will skip the move corresponding to that score and will not expand according to that chess position anymore
                if (score < beta) {
                    bestMove = newChessBoard;
                    // Reassign the value of beta
                    beta = score;
                }
            }
        }

//        System.out.println("Currently at depth "+depth);
//    	System.out.println("The score of this board is: "+evaluationFunction.calculateScore(bestMove));
//        bestMove.displayBoard();
        return bestMove;
    }
}
