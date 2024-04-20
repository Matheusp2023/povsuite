package org.netbeans.examples.modules.povproject.templates;
/** Localizable strings for {@link org.netbeans.examples.modules.povproject.templates}. */
class Bundle {
    /**
     * @return <i>Empty Povray Project</i>
     * @see EmptyPovrayProjectWizardIterator
     */
    static String EmptyPovrayProject_displayName() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "EmptyPovrayProject_displayName");
    }
    /**
     * @return <i>Sample Povray Project</i>
     * @see SamplePovrayProjectWizardIterator
     */
    static String SamplePovrayProject_displayName() {
        return org.openide.util.NbBundle.getMessage(Bundle.class, "SamplePovrayProject_displayName");
    }
    private Bundle() {}
}
