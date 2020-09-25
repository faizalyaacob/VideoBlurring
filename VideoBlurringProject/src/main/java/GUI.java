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
    private static JPanel panel;
    private static String path;
    private static File imageFolder;
    private static String[] listOfFiles;
    private static java.util.List<String> fileList;
    private static HashMap<String,JCheckBox> boxToFile;


    public static void askForFace(String imgFile){
        fileList = new ArrayList<>();
        boxToFile = new HashMap<>();
        path = imgFile;
        imageFolder = new File(path);
        listOfFiles = imageFolder.list();

        frame = new JFrame();
        JButton button = new JButton("OK");

        panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
        panel.add(new JLabel("PICK THE FACES NOT TO BLUR"));
        panel.setLayout(new GridLayout(0,1));

        for(String image: listOfFiles){
            String imageName = path + "\\" + image;
            boxToFile.put(imageName,new JCheckBox());
            panel.add( new JLabel("face",new ImageIcon(imageName),SwingConstants.LEFT));
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
        frame.setTitle("List of faces");
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
