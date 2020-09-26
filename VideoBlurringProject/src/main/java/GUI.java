import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


public class GUI implements ActionListener {

    int count = 0;
    private static JLabel label;
    private static JFrame frame;
    private static HashMap<String,JCheckBox> boxToFile;
    private static HashMap<String,JLabel> fileLabels;


    public static void askForFace(String imgFile){
        boxToFile = new HashMap<>();
        fileLabels = new HashMap<>();
        File imageFolder = new File(imgFile);
        String[] listOfFiles = imageFolder.list();

        frame = new JFrame();
        JButton button = new JButton("OK");

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
        panel.setLayout(new GridLayout(0,8,5,10));

        for(String image: listOfFiles){
            String imageName = imgFile + "\\" + image;
            boxToFile.put(imageName,new JCheckBox());
            fileLabels.put(imageName,new JLabel(new ImageIcon(imageName),SwingConstants.LEFT));
            fileLabels.get(imageName).setSize(50,50);
            panel.add(fileLabels.get(imageName));
            panel.add( boxToFile.get(imageName));
        }
        panel.add(button);
        button.addActionListener(e -> {
            for(String name: boxToFile.keySet()){
                if(!boxToFile.get(name).isSelected()){
                    File f = new File(name);
                    f.delete();
                }
            }
            frame.dispose();

        });

        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Pick the face not to blur...");
        frame.pack();
        frame.setVisible(true);

    }

    public static void main(String[] args) {
        askForFace("C:\\Users\\Asus\\Desktop\\CDLE project data\\output");
    }

    public static JFrame getFrame(){
        return frame;
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
