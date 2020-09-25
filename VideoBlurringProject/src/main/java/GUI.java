import org.bytedeco.opencv.global.opencv_highgui;
import org.bytedeco.opencv.opencv_core.Mat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;


public class GUI implements ActionListener {

    int count = 0;
    JLabel label;
    JFrame frame;
    JPanel panel;


    public GUI(){
        String path = "C:\\Users\\Asus\\Desktop\\CDLE project data\\output\\";
        File imageFolder = new File(path);
        String[] listofFiles = imageFolder.list();

        frame = new JFrame();
        JButton button = new JButton("OK");

        panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
        panel.add(new JLabel("PICK THE FACES NOT TO BLUR"));
        panel.setLayout(new GridLayout(0,1));

        ArrayList<JCheckBox> lists = new ArrayList<>();
        for(String image: listofFiles){
            String imageName = path + "\\" + image;
            lists.add( new JCheckBox());
            panel.add( new JLabel("face",new ImageIcon(imageName),SwingConstants.LEFT));
            panel.add( lists.get(lists.size()-1));
        }
        panel.add(button);
        button.addActionListener(this);

        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("List of faces");
        frame.pack();
        frame.setVisible(true);

    }
    public static void main(String[] args) {
        new GUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.exit(0);
    }


}
