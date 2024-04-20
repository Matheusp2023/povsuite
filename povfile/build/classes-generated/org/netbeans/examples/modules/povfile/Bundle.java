package org.netbeans.examples.modules.povfile;
/** Localizable strings for {@link org.netbeans.examples.modules.povfile}. */
class Bundle {
    /**
     * @return <i>Set Main File</i>
     * @see PovrayDataNode
     */
    static String CTL_SetMainFile() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "CTL_SetMainFile");
    }
    /**
     * @return <i>Source</i>
     * @see PovrayDataObject
     */
    static String LBL_Povray_EDITOR() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LBL_Povray_EDITOR");
    }
    /**
     * @return <i>Files of Povray</i>
     * @see PovrayDataObject
     */
    static String LBL_Povray_LOADER() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LBL_Povray_LOADER");
    }
    /**
     * @return <i>Visual</i>
     * @see PovrayVisualElement
     */
    static String LBL_Povray_VISUAL() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LBL_Povray_VISUAL");
    }
    /**
     * @return <i>Render</i>
     * @see RendererAction
     */
    static String LBL_Render() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LBL_Render");
    }
    /**
     * @return <i>View</i>
     * @see PovrayDataNode
     */
    static String LBL_View() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LBL_View");
    }
    private Bundle() {}
}
