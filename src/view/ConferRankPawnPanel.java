
package view;

import model.ChessGameData;
import model.Piece;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ConferRankPawnPanel extends JDialog implements ActionListener {
    int index;
    int location;
    JPanel main_pane;
    Main coVua;

    public ConferRankPawnPanel(Main coVua) {
        setTitle("New Piece");
        this.coVua = coVua;
        main_pane = new JPanel(new GridLayout(1, 4, 10, 0));
        int[] cmdActions = {
                Piece.QUEEN, Piece.ROOK, Piece.BISHOP, Piece.KNIGHT
        };
        for (int i = 0; i < cmdActions.length; i++) {
            JButton button = new JButton();
            button.addActionListener(this);
            button.setActionCommand(cmdActions[i] + "");
            main_pane.add(button);
        }
        setContentPane(main_pane);
        setResizable(false);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                resumeGame(Piece.QUEEN);
            }
        });
    }

    public void setIcons(boolean white) {
        Component[] components = main_pane.getComponents();
        ImgLibrary imgLibrary = new ImgLibrary();
        String[] resourceStrings = {"q", "r", "b", "n"};
        for (int i = 0; i < components.length; i++) {
            JButton button = (JButton) components[i];
            button.setIcon(new ImageIcon(
                    imgLibrary.getResource((white ? "trang" : "den") + resourceStrings[i])));
        }
        pack();
        setLocationRelativeTo(null);
    }

    public void actionPerformed(ActionEvent e) {
        int loaiquancochondethangchuc = Integer.parseInt(e.getActionCommand());
        setVisible(false);
        resumeGame(loaiquancochondethangchuc);
    }

    public void resumeGame(int loaiquancochondethangchuc) {
        coVua.chessBoard.getPlayerPieces()[index] = new Piece(loaiquancochondethangchuc, location);
        coVua.ChessBoardPanel.repaint();
        coVua.status = ChessGameData.AI_MOVE;
    }
}
