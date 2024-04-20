/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package org.netbeans.examples.modules.povproject;

import java.awt.BorderLayout;
import javax.swing.tree.TreeSelectionModel;
import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.view.BeanTreeView;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.NbBundle;

/**
 *
 * @author mathe
 */
@NbBundle.Messages("LBL_ChooseMainFile=Select Main File")
public class MainFileChooser extends javax.swing.JPanel implements ExplorerManager.Provider{
    
    private final ExplorerManager mgr = new ExplorerManager();

    /**
     * Creates new form MainFileChooser
     */
    public MainFileChooser(PovrayProject proj) {
        initComponents();
        
        setLayout(new BorderLayout());

        LogicalViewProvider logicalView = (LogicalViewProvider) proj.getLookup().lookup(LogicalViewProvider.class);

        Node projectNode = logicalView.createLogicalView();

        mgr.setRootContext(new FilterNode(projectNode, new ProjectFilterChildren(projectNode)));

        BeanTreeView btv = new BeanTreeView();

        jScrollPane1.setViewportView(btv);

        btv.setPopupAllowed(false);

        btv.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        add(btv, BorderLayout.CENTER);
    }
    
    @Override
    public ExplorerManager getExplorerManager() {
        return mgr;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, Bundle.TTL_ChooseMainFile());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
    
    private static final class ProjectFilterChildren extends FilterNode.Children {

        ProjectFilterChildren(Node projectNode) {
            super(projectNode);
        }

        @Override
        protected Node[] createNodes(Node object) {
            Node origChild = (Node) object;
            DataObject dob = (DataObject) origChild.getLookup().lookup(DataObject.class);
            if (dob != null) {
                FileObject fob = dob.getPrimaryFile();
                if ("text/x-povray".equals(fob.getMIMEType())) {
                    return super.createNodes(object);
                } else if (dob instanceof DataFolder) {
                    //Allow child folders of the scenes/ dir
                    return new Node[]{
                                new FilterNode(origChild,
                                new ProjectFilterChildren(origChild))
                            };
                }
            }
            //Don't create any nodes for non-povray files
            return new Node[0];
        }

    }
}