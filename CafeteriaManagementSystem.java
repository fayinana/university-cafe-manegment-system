import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CafeteriaManagementSystem extends JFrame {
    private LocalTime currentTime = LocalTime.now();
    private int hour = currentTime.getHour();
    private int minute = currentTime.getMinute();
    private int meal;
    private String studentInfoLine = null;
    String[][] mealData = new String[500][4];
    boolean exists = false;

    private JPanel cardPanel;
    private CardLayout cardLayout;

    
    private JTextField usernameField;
    private JPasswordField passwordField;
    JPanel StatistiscsPage;

    JButton  loginButton;
     int windowWidth;
     int windowHeight;
     int BreakFastCount, LunchCount, DinnerCount;
     JButton refreshButton;
     JButton resetButton;
     String photoPath = "ASTU.png";
    public CafeteriaManagementSystem() {
        windowHeight = 900;
        windowWidth = 1500;
        setTitle("Astu Cafeteria Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(windowWidth, windowHeight);
        setLayout(new BorderLayout());

        cardPanel = new JPanel();
        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);


        //the login page
        JPanel login = createLoginPage("login page");
        //the manageent page
        JPanel managementPage = createMnagementPage();

        //data page
        StatistiscsPage = createStaticsPage();

        cardPanel.add(login, "login page");
        cardPanel.add(managementPage, "management");
        cardPanel.add(StatistiscsPage, "stp");

        
        cardLayout.show(cardPanel, "login");
        
        add(cardPanel, BorderLayout.CENTER);


      
        //cardLayout.previous(cardPanel);
        //pack();
        
        

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (validateLogin(username, password)) {
                    cardLayout.next(cardPanel);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username or password. Please try again.");
                }
            }
        });


    }

    //login page 
    private JPanel createLoginPage(String name){
        JPanel panel = new JPanel();
        panel.setBackground(new Color(52, 86, 139));
        panel.setLayout(null);

        panel.add(new JLabel(name, SwingConstants.CENTER), BorderLayout.CENTER);

        JLabel usernameLabel = new JLabel("Username:");
       JLabel passwordLabel = new JLabel("Password:");
        usernameLabel.setForeground(Color.WHITE);
        passwordLabel.setForeground(Color.WHITE);
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Login");

        loginButton.setBackground(new Color(17, 120, 6));

        usernameLabel.setBounds(windowWidth/3-20, windowHeight/3+40, 200, 30);
        usernameField.setBounds(windowWidth/3 +80, windowHeight/3+40, 300, 30);

        passwordLabel.setBounds(windowWidth/3-20, windowHeight/3+100, 200, 30);
        passwordField.setBounds(windowWidth/3 +80, windowHeight/3+100, 300, 30);
        loginButton.setBounds(windowWidth/3+80, windowHeight/3+150, 300, 30);
        ImageIcon imageIcon = new ImageIcon("ASTU.png");
        JLabel Imagelabel = new JLabel(imageIcon);
  
        Imagelabel.setBounds(windowWidth/2-110, 10, 150,120);

        JLabel title = new JLabel("Astu Cafeteria Management System");
        
        Font font = title.getFont();
        Font newFont = font.deriveFont(font.getSize() + 10f); // Increase font size by 10
        title.setFont(newFont);
        title.setForeground(Color.WHITE);


        title.setBounds(windowWidth/3+50,150, 500, 40 );
        panel.add(Imagelabel);


        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        //add(new JLabel()); // Placeholder for spacing
        panel.add(loginButton);
        panel.add(title);

        return panel;
    }
//validating if credentials are correct then if true redirect to nxt page of the card
    private boolean validateLogin(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader("admin.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                String storedUsername = parts[0];
                String storedPassword = parts[1];
                if (username.equals(storedUsername) && password.equals(storedPassword)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

//management page
private JPanel createMnagementPage(){
    JPanel panel = new JPanel();
    panel.setBackground(new Color(52, 86, 139));
    panel.setLayout(null);

    panel.add(new JLabel("management", SwingConstants.CENTER), BorderLayout.CENTER);
    String mealNow = "Register for "+ getMealType();
    JButton mealButton = new JButton(mealNow);
    JButton extraInfoButton = new JButton("view today's meal info");

    mealButton.setBackground(new Color(64, 117, 50));
    extraInfoButton.setBackground(new Color(64, 117, 50));
    mealButton.setForeground(Color.WHITE);
    extraInfoButton.setForeground(Color.WHITE);


    mealButton.setBounds(windowWidth/3-180, windowHeight/3+150, 300, 50);
    extraInfoButton.setBounds(windowWidth/3+260, windowHeight/3+150, 300, 50);
    panel.add(extraInfoButton);
    panel.add(mealButton);

     ImageIcon imageIcon = new ImageIcon("ASTU.png");
    JLabel Imagelabel = new JLabel(imageIcon);
  
    Imagelabel.setBounds(windowWidth/2-110, 10, 150,120);
    panel.add(Imagelabel);

    ImageIcon icon = new ImageIcon(photoPath);
    JLabel studImageLabel = new JLabel(icon);
    studImageLabel.setBounds(windowWidth/2+300, 100, 250, 180);
    panel.add(studImageLabel);
    studImageLabel.setVisible(false);

    mealButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e){
            
                String studentId = JOptionPane.showInputDialog("Enter Student ID:");
                String mealNow = getMealType();
                boolean status = checkMealStatus(studentId);
                if (exists){
                    studImageLabel.setVisible(true);
                    ImageIcon newIcon = new ImageIcon(photoPath);
                    studImageLabel.setIcon(newIcon);
                    panel.repaint();
                    if (!status){
                        JOptionPane.showMessageDialog(null, "Warning: Student has already entered " +mealNow+" today.");
                    }
                    else{
                        updateMealStatus(studentId, false);
                        JOptionPane.showMessageDialog(null, "Happy "+mealNow);
                    }
                }

                else{
                    JOptionPane.showMessageDialog(null, "a student with this id is is not registered");
                }
                studImageLabel.setVisible(false);
            }
        });


    extraInfoButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent ae){
            cardLayout.next(cardPanel);
        }
    });

    return panel;
}
//adds table to show the todays meal data
private JPanel createStaticsPage(){
    JPanel panel = new JPanel();
    panel.setBackground(new Color(52, 86, 139));
    panel.setLayout(null);
    panel.add(new JLabel("management", SwingConstants.CENTER), BorderLayout.CENTER);

    ImageIcon imageIcon = new ImageIcon("ASTU.png");
    JLabel Imagelabel = new JLabel(imageIcon);
  
    Imagelabel.setBounds(20, 10, 150,120);
    panel.add(Imagelabel);
        try {
             JScrollPane scrollPane = mealDataTable();
             scrollPane.setBounds(300, 10, 650,520);

             JLabel totalStatLabel = new JLabel("Breakfast     Lunch     Dinner");
             JLabel totalStatData = new JLabel(Integer.toString(BreakFastCount)+"                    "+Integer.toString(LunchCount) + "                    "+Integer.toString(DinnerCount));

            Font font = totalStatLabel.getFont();
            Font newFont = font.deriveFont(font.getSize() + 7f); // Increase font size by 10
            totalStatLabel.setFont(newFont);
            totalStatData.setFont(newFont);

             totalStatLabel.setBounds(1000, 100, 400, 100);
             totalStatLabel.setForeground(Color.WHITE);
             totalStatData.setBounds(1000, 150, 400, 100);
             totalStatData.setForeground(Color.WHITE);

            panel.add(scrollPane);
            panel.add(totalStatData);
            panel.add(totalStatLabel);
    

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
    refreshButton = new JButton("Refresh");
    panel.add(refreshButton, BorderLayout.SOUTH);
    JButton backButton = new JButton("Back");
    panel.add(backButton, BorderLayout.BEFORE_LINE_BEGINS);

    refreshButton.setBackground(new Color(64, 117, 50));
    refreshButton.setBounds(700, 600, 200,50);
    backButton.setBackground(new Color(64, 117, 50) );
    backButton.setBounds(400,600, 200, 50);

    refreshButton.setForeground(Color.WHITE);
    backButton.setForeground(Color.WHITE);

    resetButton = new JButton("Reset Data");
     resetButton.setBackground(Color.RED);
    resetButton.setBackground(Color.RED );
     resetButton.setBounds(550,660, 200, 50);
     panel.add(resetButton);

     resetButton.setForeground(Color.WHITE);

     resetButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent
        aee){
            updateMealStatus("0",true);
        }
     });
    backButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e){
            cardLayout.previous(cardPanel);
        }
    });

      refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent aee) {
                cardPanel.remove(StatistiscsPage);
                StatistiscsPage = createStaticsPage();
                cardPanel.add(StatistiscsPage, "stp");
                cardLayout.previous(cardPanel);
            }
    });


    return panel;
}


//designs table for meals data
private JScrollPane mealDataTable(){
    
    try {
             FileReader fr = new FileReader("students.txt");
             BufferedReader br = new BufferedReader(fr);
             mealData[0] = new String[] {"ID", "Breakfast", "Lunch", "Dinner"};
             DinnerCount = 0;LunchCount = 0; BreakFastCount = 0;


             String line;
             int index = 1;
             while ((line = br.readLine()) != null) {

                 String[] studData = line.split(" ");

                 String bf = studData[1], ln = studData[2], dn = studData[3];
                 if (bf.equals("0")){
                    BreakFastCount += 1;
                    studData[1] = "Used";
                 }
                 else{
                    studData[1] = "not used";
                 }
                 if (ln.equals("0")){
                    LunchCount += 1;
                    studData[2] = "Used";
                 }
                 else{
                    studData[2] = "not used";
                 }
                 if (dn.equals("0")){
                    DinnerCount += 1;
                    studData[3] = "Used";
                 }
                 else{
                    studData[3] = "not used";
                 }
                 mealData[index] = studData;
                 index += 1;
    
             }
             String[] counts = {"Total", Integer.toString(BreakFastCount),  Integer.toString(LunchCount), Integer.toString(DinnerCount)};
             mealData[index] = counts;
             index += 1;
             String[] col = {"ID", "Breakfast", "Lunch", "Dinner"};

             String[][] slicedData = sliceArray(mealData, 0, index+1);
        
             JTable infoTable = new JTable(slicedData, col);
             infoTable.setBackground(Color.DARK_GRAY);
             infoTable.setForeground(Color.WHITE);
             
             JScrollPane scrollPane = new JScrollPane(infoTable);
             scrollPane.setColumnHeaderView(infoTable.getTableHeader()); // Set the table header
             
             //panel.add(scrollPane); // Add the scroll pane to the panel instead of the table
             
             return scrollPane;
            }
             catch (Exception e){
                System.out.println(e.getLocalizedMessage());
             }
             return new JScrollPane();

}

//checks if a student has already the current meal according to the local time
//the students data is written in the file in a form id beakfasr lunch dinner
//breakfast lunch or dinner is 0 if the student haven't taken a dinner and so on

    private boolean checkMealStatus(String studentId) {
        exists = false;
        getMealType();
        String fileName = "students.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                String storedStudentId = parts[0];
                String storedMealType = parts[meal];
                if (storedStudentId.equals(studentId)){
                    if (!parts[4].equals(null)){
                        try{
                            photoPath = parts[4];
                        }
                        catch (Exception e){
                            System.out.println("no inage was forund for id "+ parts[0]);
                        }
                        
                    }
                    exists = true;
                }
                //exists and not taken
                if (storedStudentId.equals(studentId) && storedMealType.equals("1")) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
//update the meal data of a student after he enters to cafe
    private void updateMealStatus(String studentId, boolean reset) {
        studentInfoLine = null;
        List<String> lines = new ArrayList<>();

        String fileName = "students.txt";

        try (FileReader reader = new FileReader(fileName)) {
            String line;
            BufferedReader br = new BufferedReader(reader);
            while ((line = br.readLine()) != null) {
                String[] studentInfo = line.split(" ");

                String Id = studentInfo[0];
                if (!Id.equals(studentId)) {
                    lines.add(line);
                } else {
                    studentInfoLine = line;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileWriter fr = new FileWriter("students.txt")) {
            BufferedWriter bfr = new BufferedWriter(fr);
            for (String data : lines) {
                if (!reset){
                    bfr.write(data);
                    bfr.newLine();
                }
                else{
                    String [] dt = data.split(" ");
                    dt[1] = "1"; dt[2] = "1"; dt[3] = "1";
                    bfr.write(String.join(" ", dt));
                    bfr.newLine();
                }
                
            }
            if (studentInfoLine!="" && studentInfoLine !=null){
                String[] splitted = studentInfoLine.split(" ");
                splitted[meal] = "0";//mark as taken
                String ln = String.join(" ", splitted);
                bfr.write(ln);
                bfr.newLine();
            }
            

            bfr.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getMealType() {
        if (hour + minute / 60 <= 3.5) {
            meal = 1;
            return "breakfast";
        } else if (hour + minute / 60 < 7.5) {
meal = 2;
return "lunch";
        } else {
            meal = 3;
            return "dinner";
        }
    }

    private String[][] sliceArray(String[][] array, int startIndex, int endIndex) {
        int length = endIndex - startIndex;
        String[][] slicedArray = new String[length][array[0].length];

        System.arraycopy(array, startIndex, slicedArray, 0, length);

        return slicedArray;
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CafeteriaManagementSystem().setVisible(true);
            }
        });
    }
}