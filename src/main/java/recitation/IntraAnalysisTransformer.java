package recitation;

import soot.*;
import soot.jimple.Stmt;
import soot.toolkits.graph.ExceptionalUnitGraph;

import java.util.*;

public class IntraAnalysisTransformer extends BodyTransformer {
    public static final String ANALYSIS_NAME = "jap.defsanalysis";

    private static IntraAnalysisTransformer theInstance = new IntraAnalysisTransformer();

    public static IntraAnalysisTransformer getInstance() {
        return theInstance;
    }

    @Override
    protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
        NormalUnitPrinter printer = new NormalUnitPrinter(body);
        IntraDefsAnalysis analysis = new IntraDefsAnalysis(new ExceptionalUnitGraph(body));

        // Run the intraprocedural analysis
        analysis.run();

        // Print abstract values at all program points
        for (Unit unit: body.getUnits()) {
            Stmt stmt = (Stmt) unit;
            System.out.println(stmt.getClass().getSimpleName() + ":\t" + stmt.toString() + "\t" + analysis.getFlowAfter(stmt).toString());
        }
    }
}