package edu.cmu.se355.recitation;

import soot.BodyTransformer;
import soot.Body;
import soot.Unit;
import soot.Scene;
import soot.SootField;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.Stmt;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import soot.jimple.DefinitionStmt;
import soot.jimple.FieldRef;
import soot.Value;
import soot.*;
import soot.toolkits.scalar.*;
import soot.toolkits.graph.*;
import soot.NormalUnitPrinter;
import java.util.*;
import soot.jimple.internal.*;

/**
 * This analysis prints the locals guaranteed to be defined at (just before) a
 * given program point.
 */
public class PrintGuaranteedDefs extends BodyTransformer {
    @Override
    protected void internalTransform(Body body, String phaseName, Map options) {
        NormalUnitPrinter printer = new NormalUnitPrinter(body);
        GuaranteedDefs g = new GuaranteedDefs(new ExceptionalUnitGraph(body));

        // print out guaranteed defs for each unit
        for (Unit unit : body.getUnits()) {
            Stmt stmt = (Stmt) unit;
            List<Value> l = g.getGuaranteedDefs(unit);
            for (Value f : l) {
                if (f instanceof JimpleLocal) {
                    System.out.print(f.toString());
                    System.out.print(",");
                }
            }
            System.out.print("\t" + stmt.getClass() + ": ");
            stmt.toString(printer);
            System.out.println(printer.output());
            printer.output().setLength(0);
        }
    }

    private static PrintGuaranteedDefs theInstance = new PrintGuaranteedDefs();

    public static PrintGuaranteedDefs instance() {
        return theInstance;
    }
}
