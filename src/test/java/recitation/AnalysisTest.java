package recitation;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.Timeout;
import soot.G;
import soot.Main;
import soot.Scene;
import soot.options.Options;

import java.util.HashSet;
import java.util.Set;

import static soot.SootClass.SIGNATURES;

public abstract class AnalysisTest {
    protected String analysisName;

    @Rule
    public Timeout globalTimeout = Timeout.seconds(600);

    /**
     * Reset Soot and errors, then reinitialize Soot
     */
    @Before
    public void initTest() {
        System.out.println("Initializing tests");
        G.reset();
        Options.v().set_keep_line_number(true);
        Options.v().set_output_format(Options.output_format_J);
        Options.v().set_prepend_classpath(true);
        Options.v().set_output_dir("sootOutput");
        String sep = System.getProperty("file.separator");
        String sootClasspath = "build" + sep + "classes" + sep + "java" + sep + "test";
        Options.v().set_soot_classpath(sootClasspath);
        add_analysis();
        System.out.println("Done initializing");
    }

    abstract void add_analysis();

    void analyzeClass(String testClass) {
        Scene.v().addBasicClass(testClass, SIGNATURES);
        Scene.v().loadBasicClasses();
        Scene.v().loadNecessaryClasses();

        Main.main(new String[]{ "-p", analysisName, "on", testClass });
    }
}
