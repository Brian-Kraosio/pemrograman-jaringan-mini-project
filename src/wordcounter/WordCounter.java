/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordcounter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

/**
 *
 * @author Asus
 */
public class WordCounter {

    /**
     * @param args the command line arguments
     */
    private frontEnd frontend;
    private List<Integer> list = new ArrayList<>();

    public WordCounter(frontEnd frontend) {
        this.frontend = frontend;
        this.frontend.getBtnOpen().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFile();
            }
        });

        this.frontend.getBtnSave().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                saveFile();
            }
        });

        this.frontend.getSearchButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                countWord();
            }
        });

        this.frontend.getBtnDetails().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                Details();
            }
        });

    }

    private void openFile() {
        JFileChooser openFile = frontend.getLoadFile();
        StyledDocument doc = frontend.getTextPane().getStyledDocument();
        if (JFileChooser.APPROVE_OPTION == openFile.showOpenDialog(frontend)) {
            BufferedInputStream reader = null;
            try {
                frontend.getTextPane().setText("");
                reader = new BufferedInputStream(new FileInputStream(openFile.getSelectedFile()));
                doc.insertString(0, "", null);
                int temp = 0;
                List<Integer> list = new ArrayList<>();
                while ((temp = reader.read()) != -1) {
                    list.add(temp);
                }
                if (!list.isEmpty()) {
                    byte[] dt = new byte[list.size()];
                    int i = 0;
                    for (Integer integer : list) {
                        dt[i] = integer.byteValue();
                        i++;
                    }
                    doc.insertString(doc.getLength(), new String(dt), null);
                    JOptionPane.showMessageDialog(frontend, "File Opened Successfully.", "Informasi", JOptionPane.INFORMATION_MESSAGE);
                    frontend.getNameLabel().setText(openFile.getSelectedFile().getName());
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(WordCounter.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException | BadLocationException ex) {
                Logger.getLogger(WordCounter.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException ex) {
                        Logger.getLogger(WordCounter.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    private void saveFile() {
        JFileChooser loadFile = frontend.getLoadFile();
        if (JFileChooser.APPROVE_OPTION == loadFile.showSaveDialog(frontend)) {
            BufferedOutputStream writer = null;
            try {
                String contents = frontend.getTextPane().getText();
                if (contents != null && !contents.isEmpty()) {
                    writer = new BufferedOutputStream(new FileOutputStream(loadFile.getSelectedFile()));
                    writer.write(contents.getBytes());
                    JOptionPane.showMessageDialog(frontend, "File berhasil ditulis.", "Informasi", JOptionPane.INFORMATION_MESSAGE);
                }

            } catch (FileNotFoundException ex) {
                Logger.getLogger(WordCounter.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(WordCounter.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (writer != null) {
                    try {
                        writer.flush();
                        writer.close();
                        frontend.getTextPane().setText("");
                    } catch (IOException ex) {
                        Logger.getLogger(WordCounter.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    private void countWord() {
        try {
            String searched = frontend.getSearchSpecWord().getText();
            int count = 0;
            String word;
            int line = 1;
            String[] wording = null;
            String lineInfo = "";
            String lineCount = "";
            JFileChooser openFile = frontend.getLoadFile();
            StyledDocument doc = frontend.getTextPane().getStyledDocument();
            LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(openFile.getSelectedFile()));
            while ((word = lineNumberReader.readLine()) != null) {
                wording = word.split("\\s+");                
                for (String specWord : wording) {
                    if (specWord.equalsIgnoreCase(searched)) {
                        count++;
                        lineCount = searched + " at line " + lineNumberReader.getLineNumber() + "\n";
                        lineInfo = lineInfo + lineCount;
                    }  
                } 
            }
            JOptionPane.showMessageDialog(frontend, "Searched Words Details !!!\n"
                    + lineInfo
                    + "\nTotal Unique Word : " + count, "Search Specific Words !", JOptionPane.INFORMATION_MESSAGE);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(WordCounter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(WordCounter.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void Details() {
        try {
            int countword = 0;
            char ascii;
            String word;
            int countchar = 0;
            int lines = 1;

            JFileChooser openFile = frontend.getLoadFile();
            StyledDocument doc = frontend.getTextPane().getStyledDocument();
            LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(openFile.getSelectedFile()));
            while ((word = lineNumberReader.readLine()) != null) {

                String[] wording = word.split("\\s+");

                countword += wording.length;
                countchar += word.length();
            }
            lines += lineNumberReader.getLineNumber();
            JOptionPane.showMessageDialog(frontend, "File Details !!!\n"
                    + "Numbers of line : " + lines
                    + "\nNumber of Word : " + countword
                    + "\nNumber of Char : " + countchar, "Details Information!", JOptionPane.INFORMATION_MESSAGE);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(WordCounter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(WordCounter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
