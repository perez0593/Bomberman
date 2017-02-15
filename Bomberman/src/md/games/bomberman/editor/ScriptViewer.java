/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.editor;

import java.awt.Container;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import md.games.bomberman.script.Script;
import md.games.bomberman.script.ScriptManager;
import md.games.bomberman.util.SortedListModel;

/**
 *
 * @author Asus
 */
public final class ScriptViewer extends JDialog
{
    private final ScriptManager scripts;
    private final String oldName;
    private final boolean save;
    private SortedListModel<Script> model;
    
    private ScriptViewer(ScriptEditor parent, ScriptManager scripts, String oldName, boolean save)
    {
        super(parent,true);
        initComponents();
        this.scripts = scripts;
        this.oldName = oldName;
        this.save = save;
        osbut.setText(save ? "Save" : "Open");
        setTitle(save ? "Save script" : "Open script");
        fill();
        focus();
    }
    
    /*private ScriptViewer(ScriptCreator parent, ScriptManager scripts, boolean save)
    {
        super(parent,true);
        initComponents();
        this.scripts = scripts;
        this.save = save;
        osbut.setText(save ? "Save" : "Open");
        setTitle(save ? "Save script" : "Open script");
        fill();
        focus();
    }*/
    
    private void focus()
    {
        Container parent = getParent();
        java.awt.Dimension screen = parent.getSize();
        java.awt.Dimension window = getSize();
        setLocation(parent.getX() + (screen.width - window.width) / 2,
                        parent.getY() + (screen.height - window.height) / 2);
    }
    
    private void fill()
    {
        model.clear();
        if(scripts.getScriptCount() <= 0)
            return;
        final Pattern p = filter.getText().isEmpty() ? null : Pattern.compile(filter.getText());
        model.addAll(scripts.getAllScripts().stream()
                .filter(script -> p == null || p.matcher(script.getName()).matches())
                .collect(Collectors.toList()));
    }
    
    public static final Script showOpen(ScriptEditor parent)
    {
        ScriptManager man = parent.getScriptManager();
        ScriptViewer viewer = new ScriptViewer(parent,man,null,false);
        viewer.setVisible(true);
        return man.getScript(viewer.scriptName.getText());
    }
    
    public static final Script showSave(ScriptEditor parent, String defaultName)
    {
        ScriptManager man = parent.getScriptManager();
        ScriptViewer viewer = new ScriptViewer(parent,man,defaultName,true);
        if(defaultName != null)
            viewer.scriptName.setText(defaultName);
        viewer.setVisible(true);
        return man.getScript(viewer.scriptName.getText());
    }
    public static final Script showSave(ScriptEditor parent)
    {
        return showSave(parent,null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        slist = new javax.swing.JList<>();
        cbut = new javax.swing.JButton();
        osbut = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        scriptName = new javax.swing.JTextField();
        filter = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        slist.setModel(model = new SortedListModel<>());
        slist.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        slist.setLayoutOrientation(javax.swing.JList.VERTICAL_WRAP);
        slist.setVisibleRowCount(-1);
        slist.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                slistValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(slist);

        cbut.setText("Cancel");
        cbut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbutActionPerformed(evt);
            }
        });

        osbut.setText("Open/Save");
        osbut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                osbutActionPerformed(evt);
            }
        });

        jLabel1.setText("Script name:");

        filter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filterKeyReleased(evt);
            }
        });

        jLabel2.setText("Filter:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(filter)
                            .addComponent(scriptName, javax.swing.GroupLayout.DEFAULT_SIZE, 327, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(osbut)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbut)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(filter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbut)
                    .addComponent(osbut)
                    .addComponent(jLabel1)
                    .addComponent(scriptName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void filterKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_filterKeyReleased
        fill();
    }//GEN-LAST:event_filterKeyReleased

    private void slistValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_slistValueChanged
        int sel = slist.getSelectedIndex();
        if(sel < 0)
            return;
        Script ssel = model.getElementAt(sel);
        if(ssel == null)
            return;
        scriptName.setText(ssel.getName());
    }//GEN-LAST:event_slistValueChanged

    private void osbutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_osbutActionPerformed
        if(save)
        {
            String name = scriptName.getText();
            try
            {
                Script s = scripts.getScript(name);
                if(s != null)
                {
                    if(oldName == null || !oldName.equals(name))
                    {
                        int res = JOptionPane.showConfirmDialog(this,"Script \"" + name +
                                "\" already exists. Do you want overwrite it?");
                        if(res != JOptionPane.YES_OPTION)
                            return;
                    }
                }
                scripts.createScript(name);
                //scripts.checkIsValidScriptName(name);
            }
            catch(IllegalArgumentException ex)
            {
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Invalid Script Name",JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        dispose();
    }//GEN-LAST:event_osbutActionPerformed

    private void cbutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbutActionPerformed
        scriptName.setText("");
        dispose();
    }//GEN-LAST:event_cbutActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        scriptName.setText("");
        dispose();
    }//GEN-LAST:event_formWindowClosing


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cbut;
    private javax.swing.JTextField filter;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton osbut;
    private javax.swing.JTextField scriptName;
    private javax.swing.JList<Script> slist;
    // End of variables declaration//GEN-END:variables
}
