package view;

import controller.ChessBot;
import controller.ChessGameRule;
import model.ChessBoard;
import model.ChessGameData;
import model.Move;
import model.Piece;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main extends JFrame implements MouseListener {
    private boolean currentColor;
    private boolean isWhite;
    private ImgLibrary imgLibrary = new ImgLibrary();
    private ChessBot chessBot;
    private ChessGameRule chessGameRule;
    private Move move = new Move();
    private Map<Integer, Image> images = new HashMap<>();
    private Map<Integer, Icon> iconImages = new HashMap<>();

    private JLabel newGameButton, surrenderButton, exitButton, undoButton;
    JPanel mainPanel = new JPanel(new BorderLayout());
    MenuPanel menu;
    ChessBoard chessBoard;
    Main.ChessBoardPanel ChessBoardPanel;
    int status;
    boolean swapRookAndKing;
    ConferRankPawnPanel ConferRankPanel;
    Color themeColor = Color.gray;
    List<ChessBoard> movingHistory = new ArrayList<>();
    int undoPosition;
    JSlider levelSlider;

    public Main() {
        super("GAME CỜ VUA ");
        setContentPane(mainPanel);
        chessBoard = new ChessBoard();

        //Khởi tạo hàm thăng cấp cho tốt
        ConferRankPanel = new ConferRankPawnPanel(this);
        showBoardGame();
        ChessBoardPanel = new ChessBoardPanel();

        //Thêm menu ở vị trí phía đông
        mainPanel.add(createMenu(), BorderLayout.EAST);

        //Thêm bàn cờ ở vị trí trung tâm
        mainPanel.add(ChessBoardPanel, BorderLayout.CENTER);

        //
        mainPanel.add(level(), BorderLayout.NORTH);

        //Set màu của mainPanel
        mainPanel.setBackground(themeColor);

        //Tùy chỉnh kích thước cửa sổ sao cho phù hợp với các thành phần panel , button trong JFrame
        pack();
        setSize(1000, 850);

        //Thêm đối tượng WindowListener vào cửa sổ
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                //Khi người dùng nhấn vào dấu X để đóng cửa sổ sẽ chạy hàm exit();
                exit();
            }
        });
    }

    public JPanel createMenu() {
        //Tạo panel sử dụng GirdLayout để thêm vào các button gồm 6 hàng , 1 cột
        JPanel menuPanel = new JPanel(new GridLayout(4, 1, 0, 0));

        //Tạo các JLabel để hiển thị ảnh của các button
        newGameButton = new JLabel(new ImageIcon(imgLibrary.getResource("new_game_1")));
        undoButton = new JLabel(new ImageIcon(imgLibrary.getResource("undo_1")));
        exitButton = new JLabel(new ImageIcon(imgLibrary.getResource("exit_1")));
        surrenderButton = new JLabel(new ImageIcon(imgLibrary.getResource("surrender_1")));

        //Thêm vào phương thức addMouseListener để có thể click chuột vào các button đó
        newGameButton.addMouseListener(this);
        undoButton.addMouseListener(this);
        surrenderButton.addMouseListener(this);
        exitButton.addMouseListener(this);

        //Thêm các button vào Panel menuPanel đã tạo ở trên
        menuPanel.add(newGameButton);
        menuPanel.add(undoButton);
        menuPanel.add(surrenderButton);
        menuPanel.add(exitButton);

        //Set màu background cho menuPanel
        menuPanel.setBackground(themeColor);

        //SetBorder để tạo border rỗng với khoảng trắng ở đầu và cuối là 100 , ở hai bên là 20
        menuPanel.setBorder(BorderFactory.createEmptyBorder(150, 20, 150, 20));
        return menuPanel;
    }

    public JPanel level() {
        JPanel level = new JPanel();
        levelSlider = new JSlider(SwingConstants.HORIZONTAL, 1, 5, 3);
        levelSlider.setPreferredSize(new Dimension(600, 40));


        //Khoảng cách giữa các mức là 1
        levelSlider.setMajorTickSpacing(1);

        //Cho phép hiển thị các mức trên thanh slider
        levelSlider.setPaintTicks(true);

        //Hiển thị số trên thanh slider
        levelSlider.setPaintLabels(true);

        level.setBackground(new Color(0x53A85E));
        level.add(levelSlider);
        return level;
    }

    public void init() {//Bắt đầu chơi game
        //Người dùng chọn quân cờ trắng
        isWhite = menu.buttonWhite.isSelected();
        move.setCurrentPosition(-1);
        move.setDestinationPosition(-1);

        //Tạo bàn cờ
        chessBoard = new ChessBoard();
        chessBoard.createChessBoard(isWhite);

        //Thêm luật chơi vào bàn cờ
        chessGameRule = new ChessGameRule(chessBoard);

        //Khởi tạo ảnh của quân cờ theo màu sắc người dùng đã chọn
        loadBoardImg();

        //Khởi tạo ảnh phong tốt các quân cờ màu trắng
        ConferRankPanel.setIcons(isWhite);

        //Hàm này dùng để vẽ lại ảnh cùa bàn cờ mỗi khi thao tác
        ChessBoardPanel.repaint();

        //Nếu người dùng là quân trắng
        if (isWhite)
            //Trạng thái người dùng di chuyển
            status = ChessGameData.PLAYER_MOVE;
        else
            //Trạng thái bot di chuyển
            status = ChessGameData.AI_MOVE;

        swapRookAndKing = false;
        //Xóa lịch sử

        movingHistory.clear();
        undoPosition = 0;

        playGame();
    }

    public void playGame() {
        //Luồng sử lý thứ tự
        Thread t = new Thread() {
            public void run() {
                while (true) {
                    switch (status) {

                        //Trạng thái (status) là người di chuyển
                        case ChessGameData.PLAYER_MOVE:
                            //Nếu endGame
                            if (endGame(ChessGameData.PLAYER)) {
                                //Chuyển trạng thái về END
                                status = ChessGameData.END;
                                break;
                            }
                            break;

                        //Trạng thái bot di chuyển
                        case ChessGameData.AI_MOVE:
                            //Nếu endGame
                            if (endGame(ChessGameData.AI)) {
                                //Chuyển trạng thái về END
                                status = ChessGameData.END;
                                break;
                            }
                            //Thuật toán alphaBetaPruning cho bot
                            long time = System.currentTimeMillis();
                            move = chessBot.alphaBetaPruning(ChessGameData.AI, chessBoard, Integer.MIN_VALUE,
                                    Integer.MAX_VALUE, levelSlider.getValue()).getMove();
                            long end = System.currentTimeMillis() - time;
                            System.out.println("Time: " + end);
                            System.out.println("Best move for bot: ");
                            System.out.println(move.toString());

                            //Chuyển trạng thái về di chuyển ảnh
                            status = ChessGameData.MOVE_IMAGE;
                            break;

                        //Trạng thái di chuyển ảnh
                        case ChessGameData.MOVE_IMAGE:
                            //Update vị trí ảnh quân cờ trên bàn cờ
                            updateImg();
                            break;

                        //Trạng thái update
                        //Update vị trí của quân cờ trong mảng
                        case ChessGameData.UPDATE:
                            updatePiecePosition();
                            break;

                        //Trạng thái END (kết thúc game)
                        case ChessGameData.END:
                            return;
                    }
                    try {
                        //Việc cho luồng tạm dừng hoạt động để điều chỉnh tốc độ , tạo độ trễ
                        Thread.sleep(3);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();
    }

    public boolean endGame(int player) {

        int status = chessGameRule.status(player);
        boolean isEnd = false;
        String color = "";

        //Nếu player là Bot
        if (player == ChessGameData.AI) {
            color = (isWhite) ? "White" : "Black";

            // Nếu player là người
        } else
            color = (isWhite) ? "Black" : "White";


        if (status == ChessGameData.CHECKMATE) {
            showExitMessage(color + "GG EZ");
            isEnd = true;

            //Nếu
        } else if (status == ChessGameData.LOSE) {
            showExitMessage("GG ");
            isEnd = true;
        }
        return isEnd;
    }

    public void showExitMessage(String message) {
        // Hiện ô thông báo kết thúc game
        // 0 ở đây là kiểu hộp thoại , null ở tham số đầu tiên thể hiện không có cửa sổ cha
        // null ở tham số cuối là icon sử dụng trong hộp thoại
        int option = JOptionPane.showOptionDialog(null, message, "End Game", 0,
                JOptionPane.PLAIN_MESSAGE, null,
                //Tạo mảng các lựa chọn
                new Object[]{" Play again", "Exit"}, " Play again");

        //Nếu người dùng chọn "Chơi lại" ứng với vị trí 0 trong mảng Object
        if (option == 0) {
            //Hiển thị lại thanh menu
            menu.setVisible(true);

            //Nếu ấn vào "Thoát" thì đóng game
        } else if (option == 1) {
            System.exit(0);

            //Nếu ấn vào dấu X trên ô thông báo thì đóng ô thông báo thôi
        } else {
            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        }
    }

    public void showSurrderMessages() {
        // Hiện ô thông báo đầu hàng
        // 0 ở đây là kiểu hộp thoại , null ở tham số đầu tiên thể hiện không có cửa sổ cha
        // null ở tham số cuối là icon sử dụng trong hộp thoại
        int option = JOptionPane.showOptionDialog(null, "Do you want surrender",
                "Surrender", 0, JOptionPane.PLAIN_MESSAGE, null,

                //Tạo mảng các lựa chọn
                new Object[]{" Play again", "Exit"}, " Play again");

        //Nếu người dùng chọn "Chơi lại" ứng với vị trí 0 trong mảng Object
        if (option == 0) {
            //Hiển thị lại thanh menu
            menu.setVisible(true);
            //Nếu người dùng nhấn "Thoát" đóng game
        } else if (option == 1) {
            System.exit(0);

            //Nếu ấn vào dấu X ở ô thông báo thì chỉ thoát khỏi ô thông báo
        } else {
            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        }
    }

    public void showInformation() {
        JOptionPane.showMessageDialog(null, "wait \n", "Message", JOptionPane.PLAIN_MESSAGE);
    }

    public void showSavedMassage() {
        JOptionPane.showMessageDialog(null, "Saved board", "Message", JOptionPane.PLAIN_MESSAGE);
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        Object source = e.getSource();

        //Click vào buttonExit sẽ thoát khỏi chương trình
        if (source == exitButton) {
            //Mở lên cửa sổ lựa chọn exit game
            exit();
        }

        //Click vào ô newGame sẽ bắt đầu game
        else if (source == newGameButton) {
            if (status == ChessGameData.AI_MOVE) {
                showInformation();
                return;
            }
            if (menu == null) {
                menu = new MenuPanel(this);
                chessBot = new ChessBot();
            }
            //Thiết lập khả năng hiển thị của các thành phần trong menu
            menu.setVisible(true);
        }

        //Click vào ô undo thì quay lại nước đi trước
        else if (source == undoButton) {
            System.out.println("Translating the current chessboard is in progress: " + movingHistory.size());
            //Trừ đi 2 bàn cờ
            undoPosition = movingHistory.size() - 2;

            System.out.println("Redo: " + undoPosition);

            //Nếu > 0
            if (undoPosition > 0) {
                returnMove();
                deleteBoard();
                movingHistory();
            } else {
                System.out.println("End");
                this.status = ChessGameData.END;
                this.init();
            }
        }
        // Click vào ô đầu hàng thì show
        else if (source == surrenderButton) {
            showSurrderMessages();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    public class ChessBoardPanel extends JPanel implements MouseListener {
        Image pieceImage;
        int movingX, movingY, desX, desY, deltaX, deltaY;

        public ChessBoardPanel() {
            // Set kích thước của Panel chứa bàn cờ
            setPreferredSize(new Dimension(500, 500));

            // Set màu backGround
            setBackground(themeColor);

            // Thêm vào mouseListener để thực hiện các thao tác chuột với trò chơi
            addMouseListener(this);
        }

        @Override
        public void paintComponent(Graphics g) {
            if (chessBoard.getBoard() == null)
                return;

            //Bằng cách gọi super.paintComponent(g);,
            // bạn đảm bảo rằng bất kỳ vẽ nền hay các tác động vẽ mặc định của lớp cha được thực hiện
            // trước khi bạn thêm logic vẽ của riêng mình.
            super.paintComponent(g);
//            g.drawImage(images.get(ChessGameData.CHESS_GAME), 0, 0, this);

            // Vẽ hình ảnh bàn cờ
            g.drawImage(images.get(ChessGameData.BOARD_IMAGE), 75, 75, this);
            for (int i = 0; i < chessBoard.getBoard().length - 11; i++) {
                if (chessBoard.getBoard()[i] == ChessGameData.BORDER)
                    continue;
                int x = i % 10;
                int y = (i - x) / 10;

                //Quân cờ đang chọn in hình GLOW trước
                if (currentColor && i == move.getCurrentPosition()) {
                    g.drawImage(images.get(ChessGameData.GLOW), x * 75, y * 75 - 75, this);

                    //Vị trí mới của quân cờ in hình GLOW2
                } else if (!currentColor && move.getDestinationPosition() == i
                        && (chessBoard.getBoard()[i] == ChessGameData.EMPTY_SPACE || chessBoard.getBoard()[i] < 0)) {
                    g.drawImage(images.get(ChessGameData.GLOW2), x * 75, y * 75 - 75, this);
                }

                //Nếu là ô trống trong bàn cờ , không làm gì cả
                if (chessBoard.getBoard()[i] == ChessGameData.EMPTY_SPACE)
                    continue;

                //Nếu trạng thái đang là UPDATE và biến i đang là vị trí người chọn , không làm gì cả
                if (status == ChessGameData.UPDATE && i == move.getCurrentPosition())
                    continue;

                //In hình các quân cờ của người chơi và bot
                if (chessBoard.getBoard()[i] > 0) {
                    int piece = chessBoard.getPlayerPieces()[chessBoard.getBoard()[i]].getScored();
                    g.drawImage(images.get(piece), x * 75, y * 75 - 75, this);
                } else {
                    int piece = chessBoard.getBotPieces()[-chessBoard.getBoard()[i]].getScored();
                    g.drawImage(images.get(-piece), x * 75, y * 75 - 75, this);
                }
            }

            //UPDATE ảnh quân cờ tại vị trí mới
            if (status == ChessGameData.UPDATE) {
                g.drawImage(pieceImage, movingX, movingY, this);
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            //Nếu trạng thái hiện tại không phải là của người di chuyển
            if (status != ChessGameData.PLAYER_MOVE)
                return;
            //Vị trí hiện tại của quân cờ
            // e.getY và e.getX là những tọa độ mà chuột click vào
            int piecePosition = boardValue(e.getY() + 75) * 10 + boardValue(e.getX());
            if (chessBoard.getBoard()[piecePosition] == ChessGameData.BORDER)
                return;
            if ((!currentColor || chessBoard.getBoard()[piecePosition] > 0) &&
                    chessBoard.getBoard()[piecePosition] != ChessGameData.EMPTY_SPACE) {
                if (chessBoard.getBoard()[piecePosition] > 0) {
                    currentColor = true;
                    //Set vj trí hiện tại đang chọn
                    move.setCurrentPosition(piecePosition);
                }
            } else if (currentColor && canMove(piecePosition)) {
                currentColor = false;
                move.setDestinationPosition(piecePosition);
                status = ChessGameData.MOVE_IMAGE;

            }
            repaint();

        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

    }

    public boolean canMove(int position) {
        //Vị trí hiện tại
        int currentPosition = move.getCurrentPosition();

        //Giá trị của vị trí đích trong mảng
        int destinationValuePosition = chessBoard.getBoard()[position];

        //Nếu ở vị trí đích là khung
        if (destinationValuePosition == ChessGameData.BORDER)
            return false;

        //Nếu di chuyển lên vị trí mới vua không an toàn
        if (!chessGameRule.kingIsSafe(ChessGameData.PLAYER, currentPosition, position))
            return false;

        boolean canMove = false;
        //Lấy ra điểm của quân cờ người ở vị trí hiện tại
        int pieceScored = chessBoard.getPlayerPieces()[chessBoard.getBoard()[currentPosition]].getScored();
        switch (pieceScored) {
            case Piece.PAWN:
                if (position == currentPosition - 10 && destinationValuePosition == ChessGameData.EMPTY_SPACE)
                    canMove = true;
                if (position == currentPosition - 20 && chessBoard.getBoard()[currentPosition - 10] == ChessGameData.EMPTY_SPACE
                        && destinationValuePosition == ChessGameData.EMPTY_SPACE && currentPosition > 80)
                    canMove = true;
                if (position == currentPosition - 9 && destinationValuePosition < 0)
                    canMove = true;
                if (position == currentPosition - 11 && destinationValuePosition < 0)
                    canMove = true;
                break;
            case Piece.KNIGHT:
            case Piece.KING:
                if (pieceScored == Piece.KING)
                    canMove = canSwapRookAKing(position);
                int[] newDirecions = null;
                if (pieceScored == Piece.KNIGHT)
                    newDirecions = new int[]{currentPosition - 21, currentPosition + 21, currentPosition + 19, currentPosition - 19, currentPosition - 12,
                            currentPosition + 12, currentPosition - 8, currentPosition + 8};
                else
                    newDirecions = new int[]{currentPosition + 1, currentPosition - 1, currentPosition + 10, currentPosition - 10, currentPosition + 11,
                            currentPosition - 11, currentPosition + 9, currentPosition - 9};
                for (int i = 0; i < newDirecions.length; i++) {
                    if (newDirecions[i] == position) {
                        if (destinationValuePosition == ChessGameData.EMPTY_SPACE || destinationValuePosition < 0) {
                            canMove = true;
                            break;
                        }
                    }
                }
                break;
            case Piece.BISHOP:
            case Piece.ROOK:
            case Piece.QUEEN:
                int[] directions = null;
                if (pieceScored == Piece.BISHOP)
                    directions = ChessGameData.BISHOP_DIRECTIONS;
                if (pieceScored == Piece.ROOK)
                    directions = ChessGameData.ROOK_DIRECTIONS;
                if (pieceScored == Piece.QUEEN)
                    directions = ChessGameData.QUEEN_DIRECTIONS;
                for (int i = 0; i < directions.length; i++) {
                    int newPosition = currentPosition + directions[i];
                    canMove = true;
                    while (position != newPosition) {
                        destinationValuePosition = chessBoard.getBoard()[newPosition];
                        if (destinationValuePosition != ChessGameData.EMPTY_SPACE) {
                            canMove = false;
                            break;
                        }
                        newPosition += directions[i];
                    }
                    if (canMove)
                        break;
                }
                break;
        }
        return canMove;
    }

    public boolean canSwapRookAKing(int destinationPosition) {
        //Quân vua của người
        Piece king = chessBoard.getPlayerPieces()[8];

        //Quân xe bên trái của người
        Piece leftRook = chessBoard.getPlayerPieces()[6];

        //Quân xe bên phải của người
        Piece rightRook = chessBoard.getPlayerPieces()[5];

        //Nếu vua không thể di chuyển
        if (king.isCanMove())
            return false;
        int currentPosition = move.getCurrentPosition();

        //Nếu mất quân xe bên trái và bên phải
        if (leftRook == null && rightRook == null)
            return false;

        //Nếu các quân xe không thể di chuyển
        if (leftRook != null && leftRook.isCanMove() && rightRook != null && rightRook.isCanMove())
            return false;

        if (isWhite) {
            if (currentPosition != 95)
                return false;

            //Nếu vị trí đich != 97 và != 93
            if (destinationPosition != 97 && destinationPosition != 93)
                return false;

            //Nếu vị trí đích = 97
            if (destinationPosition == 97) {

                //Nếu ở vị trí 96,97 trong mảng tồn tại vật cản
                if (chessBoard.getBoard()[96] != ChessGameData.EMPTY_SPACE)
                    return false;
                if (chessBoard.getBoard()[97] != ChessGameData.EMPTY_SPACE)
                    return false;

                //Nếu vua không an toàn ở vị trí 96,97
                if (!chessGameRule.kingIsSafe(ChessGameData.PLAYER, currentPosition, 96))
                    return false;
                if (!chessGameRule.kingIsSafe(ChessGameData.PLAYER, currentPosition, 97))
                    return false;

                //Nếu vị trí đích = 93
            } else if (destinationPosition == 93) {

                //Nếu ở các vị trí 93,94 trong mảng có tồn tại vật cản
                if (chessBoard.getBoard()[94] != ChessGameData.EMPTY_SPACE)
                    return false;
                if (chessBoard.getBoard()[93] != ChessGameData.EMPTY_SPACE)
                    return false;

                //Nếu vua không an toàn ở vị trí 94,93
                if (!chessGameRule.kingIsSafe(ChessGameData.PLAYER, currentPosition, 94))
                    return false;
                if (!chessGameRule.kingIsSafe(ChessGameData.PLAYER, currentPosition, 93))
                    return false;
            }

            //Nếu người chơi chọn quân đen
        } else {
            if (currentPosition != 94)
                return false;
            if (destinationPosition != 92 && destinationPosition != 96)
                return false;
            if (destinationPosition == 92) {
                if (chessBoard.getBoard()[93] != ChessGameData.EMPTY_SPACE)
                    return false;
                if (chessBoard.getBoard()[92] != ChessGameData.EMPTY_SPACE)
                    return false;
                if (!chessGameRule.kingIsSafe(ChessGameData.PLAYER, currentPosition, 93))
                    return false;
                if (!chessGameRule.kingIsSafe(ChessGameData.PLAYER, currentPosition, 92))
                    return false;
            } else if (destinationPosition == 96) {
                if (chessBoard.getBoard()[95] != ChessGameData.EMPTY_SPACE)
                    return false;
                if (chessBoard.getBoard()[96] != ChessGameData.EMPTY_SPACE)
                    return false;
                if (!chessGameRule.kingIsSafe(ChessGameData.PLAYER, currentPosition, 95))
                    return false;
                if (!chessGameRule.kingIsSafe(ChessGameData.PLAYER, currentPosition, 96))
                    return false;
            }
        }
        return swapRookAndKing = true;
    }

    //Map giá trị ban cờ và giao diện
    public int boardValue(int value) {
        return value / 75;
    }

    public void updateImg() {
        int pieceImg = 0;

        //Nếu quân ô nhớ trong mảng bàn cờ mang giá trị dương [1,16]
        if (chessBoard.getBoard()[move.getCurrentPosition()] > 0) {
            //Gán bằng điểm của quân cờ đó
            pieceImg = chessBoard.getPlayerPieces()[chessBoard.getBoard()[move.getCurrentPosition()]].getScored();
        } else {
            pieceImg = -chessBoard.getBotPieces()[-chessBoard.getBoard()[move.getCurrentPosition()]].getScored();
        }

        //Gán ảnh quân cờ
        ChessBoardPanel.pieceImage = images.get(pieceImg);

        //Lấy ra cặp số x và y tương ứng với vị trí hiện tại của quân cờ
        int x = move.getCurrentPosition() % 10;
        int y = (move.getCurrentPosition() - x) / 10;

        // Xác định desX đích đến của quân cờ
        ChessBoardPanel.desX = move.getDestinationPosition() % 10;

        // Xác định desY đích đến của quân cờ
        ChessBoardPanel.desY = (move.getDestinationPosition() - ChessBoardPanel.desX) / 10;

        // Lấy các tọa độ mới trừ đi tọa độ cũ (chênh lệch giữa tọa độ mới và tọa độ cũ)
        int dX = ChessBoardPanel.desX - x;
        int dY = ChessBoardPanel.desY - y;

        // Tính toán vị trí dịch chuyển của quân cờ
        ChessBoardPanel.movingX = x * 75;
        ChessBoardPanel.movingY = y * 75 - 75;

        if (Math.abs(dX) > Math.abs(dY)) {
            if (dY == 0) {
                ChessBoardPanel.deltaX = (dX > 0) ? 1 : -1;
                ChessBoardPanel.deltaY = 0;
            } else {
                ChessBoardPanel.deltaX = (dX > 0) ? Math.abs(dX / dY) : -(Math.abs(dX / dY));
                ChessBoardPanel.deltaY = (dY > 0) ? 1 : -1;
            }
        } else {
            if (dX == 0) {
                ChessBoardPanel.deltaY = (dY > 0) ? 1 : -1;
                ChessBoardPanel.deltaX = 0;
            } else {
                ChessBoardPanel.deltaX = (dX > 0) ? 1 : -1;
                ChessBoardPanel.deltaY = (dY > 0) ? Math.abs(dY / dX) : -(Math.abs(dY / dX));
            }
        }
        //Chuyển trạng thái về update
        status = ChessGameData.UPDATE;
    }

    public void updatePiecePosition() {
        if (ChessBoardPanel.movingX == ChessBoardPanel.desX * 75 && ChessBoardPanel.movingY == ChessBoardPanel.desY * 75 - 75) {
            ChessBoardPanel.repaint();
            int currentValuePosition = chessBoard.getBoard()[move.getCurrentPosition()];
            if (currentValuePosition > 0) {
                status = ChessGameData.AI_MOVE;
            } else {
                if (move.getDestinationPosition() > 90 && move.getDestinationPosition() < 98
                        && chessBoard.getBotPieces()[-currentValuePosition].getScored() == Piece.PAWN)
                    conferRankBotPawn();
                if (endGame(ChessGameData.PLAYER))
                    status = ChessGameData.END;
                status = ChessGameData.PLAYER_MOVE;
            }
            chessBoard.update(move);
            movingHistory();
            if (currentValuePosition > 0) {
                if (swapRookAndKing) {
                    System.out.println("Swap rook and king");
                    swapRookAndKing();
                    status = ChessGameData.MOVE_IMAGE;
                } else if (move.getDestinationPosition() > 20 && move.getDestinationPosition() < 29
                        && chessBoard.getPlayerPieces()[currentValuePosition].getScored() == Piece.PAWN) {
                    conferRankPlayerPawn();
                }
            } else {
                if (endGame(ChessGameData.PLAYER)) {
                    status = ChessGameData.END;
                    return;
                }
            }
            if (!swapRookAndKing && status != ChessGameData.RANK_CONFERMENT)
                movingHistory.remove(movingHistory.size() - 1);

            movingHistory();
            if (swapRookAndKing)
                swapRookAndKing = false;
        }
        ChessBoardPanel.movingX += ChessBoardPanel.deltaX;
        ChessBoardPanel.movingY += ChessBoardPanel.deltaY;
        //Vẽ lại bàn cờ
        ChessBoardPanel.repaint();

    }

    public void conferRankPlayerPawn() {
        // Hiện panel chọn thăng chức

        //location = vị trí đích của quân tốt trong bàn cờ
        ConferRankPanel.location = move.getDestinationPosition();

        //index = vị trí của quân tốt trong mảng các quân cờ người
        ConferRankPanel.index = chessBoard.getBoard()[move.getDestinationPosition()];

        //Hiển thị ra màn hình
        ConferRankPanel.setVisible(true);
        status = ChessGameData.RANK_CONFERMENT;
    }

    public void conferRankBotPawn() {
        //Tự động đổi tốt thành hậu cho máy khi ở trạng thái thăng chức
        int conferRankPosition = chessBoard.getBoard()[move.getCurrentPosition()];
        chessBoard.getPlayerPieces()[-conferRankPosition] = new Piece(Piece.QUEEN, move.getDestinationPosition());
    }

    public void swapRookAndKing() {
        if (move.getDestinationPosition() == 97 || move.getDestinationPosition() == 96) {
            move.setCurrentPosition(98);
            move.setDestinationPosition(move.getDestinationPosition() - 1);
        } else if (move.getDestinationPosition() == 92 || move.getDestinationPosition() == 93) {
            move.setCurrentPosition(91);
            move.setDestinationPosition(move.getDestinationPosition() + 1);
        }
    }

    public void movingHistory() {
        chessBoard.setMove(null);

        //Thêm bàn cờ vào list sau mỗi nước đi của người và bot
        movingHistory.add(new ChessBoard(chessBoard));
        System.out.println("Saved " + movingHistory.size() + " move");
        System.out.println("Saved chess bỏad==============================");

        //In ra mảng bàn cờ
        chessBoard.displayBoard();
        System.out.println("==============================");
    }

    public void returnMove() {
        //Nếu lịch sử di chuyển >= 1
        if (movingHistory.size() >= 1) {
            undoPosition--;

            //Lấy ra bàn cờ lúc trước
            ChessBoard chessBoard1 = movingHistory.get(undoPosition);
            this.chessBoard = chessBoard1;

            //Set bàn cờ hiện tại thành bàn cờ lúc trước
            this.chessGameRule.setChessBoard(chessBoard1);

            //Chuyển về lại trạng thái người chơi di chuyển
            status = ChessGameData.PLAYER_MOVE;
            //Vẽ lại bàn cờ
            ChessBoardPanel.repaint();
            System.out.println("Number redo turn " + movingHistory.size() / 2);
        }

    }

    public void deleteBoard() {
        int s = movingHistory.size() - 1;
        System.out.println("deleted board");
        movingHistory.get(s).displayBoard();
        movingHistory.remove(s);

        System.out.println("deleted board");
        movingHistory.get(s - 1).displayBoard();
        movingHistory.remove(s - 1);

        System.out.println("deleted board");
        movingHistory.get(s - 2).displayBoard();
        movingHistory.remove(s - 2);


    }

    public void loadBoardImg() {
        char[] resource_keys = {'p', 'n', 'b', 'r', 'q', 'k'};
        int[] images_keys = {Piece.PAWN, Piece.KNIGHT, Piece.BISHOP, Piece.ROOK, Piece.QUEEN, Piece.KING};
        try {
            //Tạo hashMap ứng với giá trị từng quân cờ sẽ là ảnh của quân cờ đó
            for (int i = 0; i < resource_keys.length; i++) {
                images.put(images_keys[i],
                        ImageIO.read(imgLibrary.getResource((isWhite ? "trang" : "den") + resource_keys[i])));
                images.put(-images_keys[i],
                        ImageIO.read(imgLibrary.getResource((isWhite ? "den" : "trang") + resource_keys[i])));

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void showBoardGame() {
        try {
            //Ảnh bàn cờ
            images.put(ChessGameData.BOARD_IMAGE, ImageIO.read(imgLibrary.getResource("banco")));

            //Ảnh glow
            images.put(ChessGameData.GLOW, ImageIO.read(imgLibrary.getResource("glow")));

            //Ảnh glow2
            images.put(ChessGameData.GLOW2, ImageIO.read(imgLibrary.getResource("glow2")));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public void exit() {
        //Hiện lên cửa sổ thông báo
        int option = JOptionPane.showConfirmDialog(null, "Do you want Exit?", "Chess HUS",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (option == JOptionPane.YES_OPTION)
            // Chương trình kết thúc thành công
            System.exit(0);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    public void save() throws URISyntaxException, IOException {
        File file = new File(imgLibrary.getResource("fil eSave").toURI());

        try {
            FileWriter fw = new FileWriter(file);

        } catch (Exception e) {

        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    boolean nimbusFound = false;
                    for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                        if (info.getName().equals("Nimbus")) {
                            UIManager.setLookAndFeel(info.getClassName());
                            nimbusFound = true;
                            break;
                        }
                    }
                    if (!nimbusFound) {
                        int option = JOptionPane.showConfirmDialog(null,
                                " không hỗ trợ UI này\n" + "ban muốn tiếp tục không?", "cảnh báo",
                                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                        if (option == JOptionPane.NO_OPTION) {
                            //Đóng chương trình thành công
                            System.exit(0);
                        }
                    }

                    //đổi cách hiển thị giao diện máy mac
                    // System.setProperty(
                    // "Quaqua.tabLayoutPolicy","wrap"
                    // );
                    // UIManager.setLookAndFeel(
                    // "ch.randelshofer.quaqua.QuaquaLookAndFeel");
                    //

                    Main mcg = new Main();
                    // mcg.pack();

                    //Hiển thị cửa sổ lên vị trí giữa màn hình.
                    mcg.setLocationRelativeTo(null);

                    // Đặt hành động mặc định sẽ xảy ra khi người dùng đóng JFrame.
                    mcg.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                    //Đặt JFrame có được thay đổi kích thước hay không
                    mcg.setResizable(false);

                    //thiết lập khả năng hiển thị của thành phần.
                    mcg.setVisible(true);

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e.getStackTrace());
                    e.printStackTrace();
                }
            }
        });
    }
}