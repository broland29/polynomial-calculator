/** The implementation of the Graphical User Interface
 *   - independent of rest of the program
 *   - I will not comment in detail, since there is not much to comment
 *   - Note that I did not make the swing components private, since then intellij fills me with the warning
 *      "Field can be converted to a local variable". Anyway, "View" is the only class in "view", so package
 *      access is very limited.
 */
package view;

import model.Message;
import model.MessageType;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URI;

public class View extends JFrame {
    final String[] OPERATIONS_TEXT_LEFT = {"Add","Multiply","Integrate"};
    final String[] OPERATIONS_TEXT_RIGHT = {"Subtract","Divide","Differentiate"};
    final String[] BUTTONS_TEXT_LEFT = {"1","2","3","4","5","6","7","8","9"};
    final String[] BUTTONS_TEXT_RIGHT = {"0","+","-","^","x","del","sw","rst","help"};
    final String HELP = "Write polynomials as text in minimized form,\nwith exponents in decreasing order, " +
            "without\nusing spaces or any illegal characters.\n\nFor example: x^2-3x+8.\n\nIntegration and differentiation"+
            " works on the\nfirst input. For more details, see documentation.\n\n\nA project realised by broland29.\n\n";

    private String inputTextFirst;
    private String inputTextSecond;

    private boolean op;

    JFrame mainFrame;
    JPanel contentPane;

    JPanel titlePanel;
    JPanel inputPanel;
    JPanel operationPanel;
    JPanel buttonPanel;
    JPanel inputPanelLeft;
    JPanel inputPanelRight;
    JPanel operationPanelLeft;
    JPanel operationPanelRight;
    JPanel buttonPanelLeft;
    JPanel buttonPanelRight;

    JLabel titleLabel;

    JLabel inputLabelFirst;
    JLabel inputLabelSecond;
    JTextField inputTextFieldFirst;
    JTextField inputTextFieldSecond;

    JPanel resultPanel;
    JLabel resultLabel;

    JPanel inputAndResultPanel;

    JButton[] operationButtonsLeft;
    JButton[] operationButtonsRight;
    JButton[] buttonButtonsLeft;
    JButton[] buttonButtonsRight;

    public View(){
        inputTextFirst = "";
        inputTextSecond = "";

        mainFrame = new JFrame("Polynomial Calculator");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(400,500);
        mainFrame.setResizable(false);

        contentPane = new JPanel();
        contentPane.setLayout(new GridLayout(4,1));
        contentPane.setBackground(new Color(217,216,248));

        //titlePanel related
        titlePanel = new JPanel();
        titleLabel = new JLabel("Polynomial Calculator");
        titleLabel.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,30));
        titlePanel.setLayout(new GridBagLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel);


        //inputPanel related
        inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(1,2));
        inputPanel.setOpaque(false);

        inputPanelLeft = new JPanel();
        inputPanelLeft.setBorder(BorderFactory.createMatteBorder(0, 6, 0, 6, new Color(217,216,248)));
        inputPanelLeft.setLayout(new GridLayout(2,1));
        inputPanelLeft.setOpaque(false);
        inputLabelFirst = new JLabel("      First Polynomial :");
        inputLabelSecond = new JLabel("    Second Polynomial :");
        inputLabelFirst.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,16));
        inputLabelSecond.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,16));
        inputPanelLeft.add(inputLabelFirst);
        inputPanelLeft.add(inputLabelSecond);

        inputPanelRight = new JPanel();
        inputPanelRight.setBorder(BorderFactory.createMatteBorder(0, 6, 0, 6, new Color(217,216,248)));
        inputPanelRight.setLayout(new BoxLayout(inputPanelRight,BoxLayout.Y_AXIS));
        inputPanelRight.setOpaque(false);
        inputTextFieldFirst = new JTextField(30);
        inputTextFieldSecond = new JTextField(30);
        inputTextFieldFirst.setBackground(new Color(249,248,253));
        inputTextFieldSecond.setBackground(new Color(249,248,253));

        inputPanelRight.add(inputTextFieldFirst);
        inputPanelRight.add(inputTextFieldSecond);
        inputPanel.add(inputPanelLeft);
        inputPanel.add(inputPanelRight);

        //resultPanel related
        resultPanel = new JPanel();
        resultPanel.setLayout(new GridBagLayout());
        resultPanel.setOpaque(false);
        resultLabel = new JLabel("Result : ");
        resultLabel.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,16));
        resultPanel.add(resultLabel);

        //inputAndResultLabel related
        inputAndResultPanel = new JPanel();
        inputAndResultPanel.setLayout(new BoxLayout(inputAndResultPanel,BoxLayout.Y_AXIS));
        inputAndResultPanel.setOpaque(false);
        inputAndResultPanel.add(inputPanel);
        inputAndResultPanel.add(resultPanel);

        //operationPanel related
        operationPanel = new JPanel();
        operationPanel.setLayout(new GridLayout(1,2));

        operationPanelLeft = new JPanel();
        operationPanelLeft.setLayout(new GridLayout(3,1));
        operationButtonsLeft = new JButton[3];
        for (int i=0; i<3; i++){
            operationButtonsLeft[i] = new JButton(OPERATIONS_TEXT_LEFT[i]);
            operationButtonsLeft[i].setBackground(new Color(224,239,236));
            operationPanelLeft.add(operationButtonsLeft[i]);
        }

        operationPanelRight = new JPanel();
        operationPanelRight.setLayout(new GridLayout(3,1));
        operationButtonsRight = new JButton[3];
        for (int i=0; i<3; i++){
            operationButtonsRight[i] = new JButton(OPERATIONS_TEXT_RIGHT[i]);
            operationButtonsRight[i].setBackground(new Color(224,239,236));
            operationPanelRight.add(operationButtonsRight[i]);
        }

        operationPanelLeft.setBorder(BorderFactory.createMatteBorder(6, 6, 3, 3, new Color(217,216,248)));
        operationPanelRight.setBorder(BorderFactory.createMatteBorder(6, 3, 3, 6, new Color(217,216,248)));
        operationPanel.add(operationPanelLeft);
        operationPanel.add(operationPanelRight);

        //buttonPanel related
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1,2));

        buttonPanelLeft = new JPanel();
        buttonPanelLeft.setLayout(new GridLayout(3,3));
        buttonButtonsLeft = new JButton[9];
        for (int i=0; i<9; i++){
            buttonButtonsLeft[i] = new JButton(BUTTONS_TEXT_LEFT[i]);
            buttonButtonsLeft[i].setBackground(new Color(248,248,193));
            buttonButtonsLeft[i].setFocusable(false);
            buttonPanelLeft.add(buttonButtonsLeft[i]);
        }

        buttonPanelRight = new JPanel();
        buttonPanelRight.setLayout(new GridLayout(3,3));
        buttonButtonsRight = new JButton[9];
        for (int i=0; i<9; i++){
            buttonButtonsRight[i] = new JButton(BUTTONS_TEXT_RIGHT[i]);
            buttonButtonsRight[i].setBackground(new Color(248,248,193));
            buttonButtonsRight[i].setFocusable(false);
            buttonPanelRight.add(buttonButtonsRight[i]);
        }

        buttonPanelLeft.setBorder(BorderFactory.createMatteBorder(3, 6, 6, 3, new Color(217,216,248)));
        buttonPanelRight.setBorder(BorderFactory.createMatteBorder(3, 3, 6, 6, new Color(217,216,248)));
        buttonPanel.add(buttonPanelLeft);
        buttonPanel.add(buttonPanelRight);

        contentPane.add(titlePanel);
        contentPane.add(inputAndResultPanel);
        contentPane.add(operationPanel);
        contentPane.add(buttonPanel);

        mainFrame.add(contentPane);
        mainFrame.setLocationRelativeTo(null);  //put mainFrame in middle when instantiated
        mainFrame.setVisible(true);
    }

    public void addButtonButtonsListener(ActionListener bbl){
        for (int i=0; i<9; i++)
            buttonButtonsLeft[i].addActionListener(bbl);
        for (int i=0; i<9; i++)
            buttonButtonsRight[i].addActionListener(bbl);
    }

    public void addOperationButtonsListener(ActionListener obl){
        for (int i=0; i<3; i++)
            operationButtonsLeft[i].addActionListener(obl);
        for (int i=0; i<3; i++)
            operationButtonsRight[i].addActionListener(obl);
    }

    public void addInputDocumentListener(DocumentListener idl){
        inputTextFieldFirst.getDocument().addDocumentListener(idl);
        inputTextFieldSecond.getDocument().addDocumentListener(idl);
    }

    //append a character to the text of inputLabel (1 or 2, depending on which is focused)
    public void addInputCharacter(String character){
        if (inputTextFieldFirst.isFocusOwner()){
            inputTextFirst = inputTextFirst + character;
            inputTextFieldFirst.setText(inputTextFirst);
        }
        else if (inputTextFieldSecond.isFocusOwner()){
            inputTextSecond = inputTextSecond + character;
            inputTextFieldSecond.setText(inputTextSecond);
        }
    }

    //delete a character to the text of inputLabel (1 or 2, depending on which is focused)
    public void deleteInputCharacter(){
        int len;
        if (inputTextFieldFirst.isFocusOwner()){
            len = inputTextFirst.length();
            if (len > 0){
                inputTextFirst = inputTextFirst.substring(0,len-1);
                inputTextFieldFirst.setText(inputTextFirst);
            }
        }
        else if (inputTextFieldSecond.isFocusOwner()){
            len = inputTextSecond.length();
            if (len > 0){
                inputTextSecond = inputTextSecond.substring(0,len-1);
                inputTextFieldSecond.setText(inputTextSecond);
            }
        }
    }

    //swaps content of two inputs, both visually and internally
    public void swapInputs(){
        op = true;
        String aux = inputTextFirst;
        inputTextFirst = inputTextSecond;
        inputTextSecond = aux;
        resetInputTextFields();
        op = false;
    }

    //delete content of inputs, both visually and internally
    public void resetInputs(){
        op = true;
        inputTextFirst = "";
        inputTextSecond = "";
        resetInputTextFields();
        op = false;
    }

    //open up the help popup
    public void openHelp(){
        final String[] options = {"Visit Me","Back"};
        int input = JOptionPane.showOptionDialog(null, HELP, "Help", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, 0);
        if (input == JOptionPane.OK_OPTION){
            try{
                Desktop.getDesktop().browse(new URI("https://github.com/broland29"));
            }
            catch (Exception e){
                System.out.println("Something went wrong");
            }
        }
    }

    //get the input text from the text fields
    public String getInputText(int id){
        if (id == 1)
            return inputTextFieldFirst.getText();
        else if (id == 2)
            return inputTextFieldSecond.getText();
        return "";
    }

    //setter of internal state
    public void setInputTexts(String inputTextFirst , String inputTextSecond){
        this.inputTextFirst = inputTextFirst;
        this.inputTextSecond = inputTextSecond;
    }

    //re-setter of internal state, used for example when swapping
    public void resetInputTextFields(){
        inputTextFieldFirst.setText(inputTextFirst);
        inputTextFieldSecond.setText(inputTextSecond);
    }

    //getter
    public boolean getOp(){
        return op;
    }

    //set the text of result panel, depending on the content of "message"
    public void setResultPanel(Message message, int errorSource){
        MessageType messageType = message.getMessageType();
        if (messageType == MessageType.INCORRECT){
            resultLabel.setForeground(Color.RED);
            resultLabel.setText(message.getText() + " (" + errorSource + ")");
        }
        else if (messageType == MessageType.CORRECT){
            resultLabel.setForeground(Color.BLACK);
            resultLabel.setText(message.getText());
        }
    }
}
