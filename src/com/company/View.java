package com.company;

import static com.company.ExceptionHandler.log;
import com.company.listeners.*;

import javax.swing.*;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class View extends JFrame implements ActionListener {
    private Controller controller;
    private JTabbedPane tabbedPane = new JTabbedPane();
    private JTextPane htmlTextPane = new JTextPane();
    private JEditorPane plainTextPane = new JEditorPane();
    private UndoManager undoManager = new UndoManager();
    private UndoListener undoListener = new UndoListener(undoManager);

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        String command = actionEvent.getActionCommand();
        switch (command){
            case "New" : controller.createNewDocument(); break;
            case "Open" : controller.openDocument(); break;
            case "Save" : controller.saveDocument(); break;
            case "Save as..." : controller.saveDocumentAs(); break;
            case "Exit" : controller.exit(); break;
            case "About" : this.showAbout(); break;
        }
    }

    public void init() {
        initGui();
        addWindowListener(new FrameListener(this));
        setVisible(true);
    }

    public void exit() {
        controller.exit();
    }

    public void initEditor() {
        htmlTextPane.setContentType("text/html");
        tabbedPane.add("HTML", new JScrollPane(htmlTextPane));
        tabbedPane.add(".code ", new JScrollPane(plainTextPane));
        tabbedPane.setPreferredSize(new Dimension(1200, 800));
        tabbedPane.addChangeListener(new TabbedPaneChangeListener(this));
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }

    public void initGui() {
        initMenuBar();
        initEditor();
        pack();
    }

    public void selectedTabChanged() {
        switch (tabbedPane.getSelectedIndex()){
            case 0 : controller.setPlainText(plainTextPane.getText()); break;
            case 1 : plainTextPane.setText(controller.getPlainText()); break;
        }
        resetUndo();
    }

    public void initMenuBar() {
        JMenuBar jMenuBar = new JMenuBar();
        MenuHelper.initFileMenu(this, jMenuBar);
        MenuHelper.initEditMenu(this, jMenuBar);
        MenuHelper.initStyleMenu(this, jMenuBar);
        MenuHelper.initAlignMenu(this, jMenuBar);
        MenuHelper.initColorMenu(this, jMenuBar);
        MenuHelper.initFontMenu(this, jMenuBar);
        MenuHelper.initHelpMenu(this, jMenuBar);
        this.getContentPane().add(jMenuBar, BorderLayout.NORTH);

    }

    public View() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            log(e);
        }
    }

    public boolean canUndo() {
        return undoManager.canUndo();
    }

    public boolean canRedo() {
        return undoManager.canRedo();
    }

    public void undo() {
        try {
            undoManager.undo();
        } catch (Exception e) {
            log(e);
        }
    }

    public void redo() {
        try {
            undoManager.redo();
        } catch (Exception e) {
            log(e);
        }
    }

    public void resetUndo() {
        undoManager.discardAllEdits();
    }

    public UndoableEditListener getUndoListener() {
        return undoListener;
    }

    public boolean isHtmlTabSelected() {
        if (tabbedPane.getSelectedIndex() == 0) return true;
        else return false;
    }

    public void selectHtmlTab() {
        tabbedPane.setSelectedIndex(0);
        resetUndo();
    }

    public void update() {
        htmlTextPane.setDocument(controller.getDocument());
    }

    public void showAbout() {
        String message = "It's my first great app";
        JOptionPane optionPane = new JOptionPane();
        JOptionPane.showMessageDialog(optionPane, message, "About", JOptionPane.INFORMATION_MESSAGE);
    }
}