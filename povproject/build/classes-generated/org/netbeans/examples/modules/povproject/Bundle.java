package org.netbeans.examples.modules.povproject;
/** Localizable strings for {@link org.netbeans.examples.modules.povproject}. */
class Bundle {
    /**
     * @return <i>Render Project</i>
     * @see PovrayLogicalView
     */
    static String LBL_Build() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LBL_Build");
    }
    /**
     * @return <i>Select Main File</i>
     * @see MainFileChooser
     */
    static String LBL_ChooseMainFile() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LBL_ChooseMainFile");
    }
    /**
     * @return <i>Clean Project</i>
     * @see PovrayLogicalView
     */
    static String LBL_Clean() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LBL_Clean");
    }
    /**
     * @return <i>Close Project</i>
     * @see PovrayLogicalView
     */
    static String LBL_CloseProject() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LBL_CloseProject");
    }
    /**
     * @return <i>Set Main Project</i>
     * @see PovrayLogicalView
     */
    static String LBL_SetMainProject() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "LBL_SetMainProject");
    }
    /**
     * @return <i>POV-Ray for Windows always displays its graphicaluser interface when it runs. You can get a command-line version of POV-Ray at &lt;a href="http://www.imagico.de/files/povcyg_350c.zip">http://www.imagico.de/files/povcyg_350c.zip&lt;/a></i>
     * @see Povray
     */
    static String MSG_WindowsWarning() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "MSG_WindowsWarning");
    }
    /**
     * @return <i>Choose Main File</i>
     * @see RendererServiceImpl
     */
    static String TTL_ChooseMainFile() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "TTL_ChooseMainFile");
    }
    /**
     * @return <i>Find POV-Ray Standard Include File Dir</i>
     * @see Povray
     */
    static String TTL_FindIncludeDir() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "TTL_FindIncludeDir");
    }
    /**
     * @return <i>Locate POV-Ray Executable</i>
     * @see Povray
     */
    static String TTL_FindPovray() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "TTL_FindPovray");
    }
    private Bundle() {}
}
