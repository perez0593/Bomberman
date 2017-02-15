/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.editor;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.Objects;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import md.games.bomberman.script.Script;
import md.games.bomberman.script.ScriptManager;
import md.games.bomberman.util.ButtonTabComponent;
import nt.lpl.compiler.LPLCompilerException;

/**
 *
 * @author Asus
 */
public class ScriptEditor extends JFrame
{
    private final LevelEditorInterface base;
    private final ScriptManager scripts;
    
    public ScriptEditor(LevelEditorInterface base, ScriptManager scripts)
    {
        initComponents();
        this.base = Objects.requireNonNull(base);
        this.scripts = Objects.requireNonNull(scripts);
        //createTab(scripts.createScript(generateScriptName(),false));
        init();
        focus();
    }
    public ScriptEditor(LevelEditorInterface base) { this(base,new ScriptManager()); }
    
    private void focus()
    {
        java.awt.Dimension screen = getParent().getSize();
        java.awt.Dimension window = getSize();
        setLocation((screen.width - window.width) / 2,
                        (screen.height - window.height) / 2);
    }
    
    private void init()
    {
        
    }
    
    public final ScriptManager getScriptManager() { return scripts; }
    
    private void print(String line)
    {
        Document doc = terminal.getDocument();
        int len = doc.getLength();
        try { doc.insertString(len,line,null); }
        catch(BadLocationException ex)
        {
            ex.printStackTrace(System.err);
        }
    }
    private void println(String line) { print(line + '\n'); }
    
    private ScriptPane createTab(Script script)
    {
        JScrollPane scroll = new JScrollPane();
        ButtonTabComponent but = new ButtonTabComponent(pages,scroll);
        final ScriptPane pane = new ScriptPane(script,scroll,but);
        scroll.setViewportView(pane);
        return createTab0(pane,scroll,but,script.getCode());
    }
    
    private ScriptPane createTab(String name)
    {
        JScrollPane scroll = new JScrollPane();
        ButtonTabComponent but = new ButtonTabComponent(pages,scroll);
        final ScriptPane pane = new ScriptPane(name,scroll,but);
        scroll.setViewportView(pane);
        return createTab0(pane,scroll,but,"");
    }
    
    private ScriptPane createTab0(ScriptPane pane, JScrollPane scroll, ButtonTabComponent but, String code)
    {
        pane.setContentType("text/lpl");
        pane.setText(code);
        pane.addKeyListener(new KeyAdapter() {
            @Override public final void keyTyped(KeyEvent e)
            {
                pane.codeSaved = false;
                updateCurrentTabTitle();
            }
        });
        
        pages.addTab(pane.getTitle(),scroll);
        pages.setTabComponentAt(pages.getTabCount()-1,but);
        return pane;
    }
    
    private void updateCurrentTabTitle()
    {
        int tab = pages.getSelectedIndex();
        if(tab < 0)
            return;
        ScriptPane pane = (ScriptPane) ((JScrollPane)pages.getComponentAt(tab)).getViewport().getView();
        pages.setTitleAt(tab,pane.getTitle());
        pane.doRepaint();
    }
    
    private String generateScriptName()
    {
        int count = scripts.getScriptCount();
        String name = "NewScript";
        if(scripts.getScript(name) == null)
            return name;
        name += count;
        while(scripts.getScript(name) != null)
            name = name.substring(0,"NewScript".length()) + (++count);
        return name;
    }
    
    private void open()
    {
        Script script = ScriptViewer.showOpen(this);
        if(script == null)
            return;
        for(int i=0;i<pages.getTabCount();i++)
            if(((ScriptPane) ((JScrollPane)pages.getComponentAt(i))
                    .getViewport().getView()).script.equals(script))
                return;
        createTab(script);
    }
    
    
    
    private final class ScriptPane extends JEditorPane
    {
        private Script script;
        private String name;
        private final JScrollPane base;
        private final ButtonTabComponent but;
        private boolean codeSaved;
        
        private ScriptPane(Script script, JScrollPane base, ButtonTabComponent but)
        {
            this.script = Objects.requireNonNull(script);
            this.name = script.getName();
            this.base = Objects.requireNonNull(base);
            this.but = Objects.requireNonNull(but);
            this.codeSaved = true;
        }
        
        private ScriptPane(String name, JScrollPane base, ButtonTabComponent but)
        {
            this.script = null;
            this.name = name;
            this.base = Objects.requireNonNull(base);
            this.but = Objects.requireNonNull(but);
            this.codeSaved = false;
        }
        
        public final String getTitle()
        {
            return !codeSaved ? name + '*' : name;
        }
        
        /*public final void setScriptName(String name)
        {
            if(script.isLocked())
            {
                JOptionPane.showMessageDialog(this, "Cannot change name of script " + script.getScriptName() + ".",
                        "Unmodificable Script error",JOptionPane.ERROR_MESSAGE);
                return;
            }
            scripts.setScriptName(script,name);
        }*/
        
        public final void saveCode()
        {
            script.setCode(getText());
            codeSaved = true;
            updateCurrentTabTitle();
        }
        
        public final void loadCode()
        {
            setText(script.getCode());
        }
        
        public final boolean compile()
        {
            if(script == null)
                return false;
            //LPLEnvironment env = new LPLEnvironment();
            terminal.setText("");
            String oldCode = script.getCode();
            script.setCode(getText());
            try
            {
                scripts.compileScript(script.getName());
                //scripts.compileSingleScript(env,script);
            }
            catch(LPLCompilerException ex)
            {
                terminal.setText("");
                println("Compilation script \"" + script.getName() + "\" failed at " + new Date() + " by:");
                println(ex.getMessage());
                script.setCode(oldCode);
                return false;
            }
            script.setCode(oldCode);
            println("Compilation script \"" + script.getName() + "\" success at " + new Date());
            return true;
        }
        
        public final void doRepaint()
        {
            but.repaint();
            repaint();
            pages.repaint();
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        pages = new javax.swing.JTabbedPane();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        terminal = new javax.swing.JTextPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        helpPanel = new javax.swing.JTextPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setSize(new java.awt.Dimension(640, 480));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jSplitPane1.setDividerLocation(330);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setResizeWeight(1.0);
        jSplitPane1.setMinimumSize(new java.awt.Dimension(640, 480));
        jSplitPane1.setLeftComponent(pages);

        terminal.setEditable(false);
        jScrollPane1.setViewportView(terminal);

        jTabbedPane1.addTab("Terminal", jScrollPane1);

        helpPanel.setEditable(false);
        jScrollPane3.setViewportView(helpPanel);

        jTabbedPane1.addTab("Help", jScrollPane3);

        jSplitPane1.setRightComponent(jTabbedPane1);

        jMenu1.setText("Script");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("New Script");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setText("Open Script");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Actions");

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem4.setText("Compile and Save");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem4);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        /*Script script = scripts.createScript(generateScriptName(),false);
        if(!ScriptCreator.createScript(this,script))
            return;*/
        createTab(generateScriptName());
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        open();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        int tab = pages.getSelectedIndex();
        if(tab < 0)
            return;
        ScriptPane pane = (ScriptPane) ((JScrollPane)pages.getComponentAt(tab)).getViewport().getView();
        if(!pane.compile())
            return;
        pane.saveCode();
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        //base.closeScriptEditor();
    }//GEN-LAST:event_formWindowClosing


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextPane helpPanel;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane pages;
    private javax.swing.JTextPane terminal;
    // End of variables declaration//GEN-END:variables
}
