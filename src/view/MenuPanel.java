package view;

import model.ChessGameData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class MenuPanel extends JFrame implements ActionListener{

     JRadioButton buttonWhite;
     JRadioButton buttonBlack;
     JButton ok;
     JButton cancel;
     final static int WHITE = 0;
     final static int BLACK = 1;
     Main CoVua;

    public MenuPanel(Main covua){
        // Tiêu đề
        super("Option");
        this.CoVua = covua;
        JPanel mainPane = new JPanel(new BorderLayout());
        //mainPane.add(taoPanelChonMucDo(),BorderLayout.CENTER);

        // Thêm chọn màu quân cờ ở phía Bắc mainPane
        mainPane.add(chonQuanCotheomau(),BorderLayout.NORTH);

        // Thêm nút chọn ở phía Nam mainPane
        mainPane.add(taonutchon(),BorderLayout.SOUTH);

        // Set Border , EmptyBorder là border trống
        // dùng để tạo khoảng trống , công dụng như padding để đặt khoảng cách giữa nội dung và bên ngoài
        mainPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        setContentPane(mainPane);

        //Phương thức pack() sẽ tự động thay đổi kích thước của JFrame dựa trên kích thước của các component mà nó chứa
        pack();

        // Hiển thị cửa sổ ở giữa màn hình
        setLocationRelativeTo(null);

        // Đặt xem JFrame có được thay đổi kích thước hay không
        setResizable(false);

        // Hành động xảy ra khi người dùng đóng JFrame , chỉ đóng frame đó, các frame khác liên quan sẽ không bị đóng (2).
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Cài đặt để click vào button
        ok.addActionListener(this);
        cancel.addActionListener(this);
    }
    public JPanel chonQuanCotheomau(){
        // Tạo một JPanel biểu diễn các button thành 1 hàng và 2 cột
        JPanel chonQuanCoTheoMau = new JPanel(new GridLayout(1,2));

        // Button "Chơi cờ trắng" được chọn auto
        buttonWhite = new JRadioButton("White Piece",true);
        // Button "Chơi cờ đen" phải click vào thì mới chọn
        buttonBlack = new JRadioButton("Black Piece");
        ButtonGroup group = new ButtonGroup();
        group.add(buttonWhite);
        group.add(buttonBlack);

        // Thêm button trên vào JPanel
        chonQuanCoTheoMau.add(buttonWhite);
        chonQuanCoTheoMau.add(buttonBlack);

        //Compound Border là sự kết hợp các border lại với nhau.
        // Chúng ta có thể sử dụng nó để tạo ra các border đẹp theo ý muốn hoặc có thể tạo padding
        // hoặc margin nội dung với border qua việc kết hợp empty border với các border khác bằng Compound border.
        chonQuanCoTheoMau.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5,5,5,5),
                BorderFactory.createTitledBorder("Choose color")));
        return chonQuanCoTheoMau;
    }
    public JPanel taonutchon(){
        JPanel buttonPane = new JPanel(new BorderLayout());

        // Tạo JPanel biểu diễn các button thành 1 hàng , 2 cột , cách nhau ngang 5 pixel , dọc 0 pixel
        JPanel pane = new JPanel(new GridLayout(1,2,5,0));

        // Thêm Button "Chơi" vào pane
        pane.add(ok = new JButton("Play"));

        // Thêm Button "Hủy bỏ" vào pane
        pane.add(cancel = new JButton("Canel"));

        // Thêm pane vào buttonPane ở vị trí phía Đông
        buttonPane.add(pane,BorderLayout.EAST);

        //Compound Border là sự kết hợp các border lại với nhau.
        // Chúng ta có thể sử dụng nó để tạo ra các border đẹp theo ý muốn hoặc có thể tạo padding
        // hoặc margin nội dung với border qua việc kết hợp empty border với các border khác bằng Compound border.
        buttonPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5,5,5,5), // Kích cỡ của EmtyBorder
                BorderFactory.createTitledBorder("What do you do ?"))); // Titled
        return buttonPane;
    }
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == ok){
            CoVua.status = ChessGameData.END;
            CoVua.init();           
        }
        setVisible(false);
    }
}
