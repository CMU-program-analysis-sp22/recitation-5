/* Soot - a J*va Optimization Framework
 * Copyright (C) 2003 Navindra Umanee <navindra@cs.mcgill.ca>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 */

package edu.cmu.se355.recitation;

import java.util.*;

import soot.*;
import soot.jimple.DefinitionStmt;
import soot.options.*;
import soot.toolkits.graph.*;
import soot.toolkits.scalar.*;

/**
 * Find all locals guaranteed to be defined at (just before) a given
 * program point.
 *
 * @author Navindra Umanee; modified by Jenna Wise, Jeremy Lacomis, and Priya Varra
 **/
public class GuaranteedDefs {
    protected Map<Unit, List> unitToGuaranteedDefs;

    public GuaranteedDefs(UnitGraph graph) {
        G.v().out.println("[" + graph.getBody().getMethod().getName() +
                          "]     Constructing GuaranteedDefs...");

        GuaranteedDefsAnalysis analysis = new GuaranteedDefsAnalysis(graph);

        // build map of program statement to locals guaranteed to be defined
        // just before it
        unitToGuaranteedDefs = new HashMap<Unit, List>(graph.size() * 2 + 1, 0.7f);

        Iterator unitIt = graph.iterator();
        while(unitIt.hasNext()) {
            Unit s = (Unit) unitIt.next();
            FlowSet<Unit> set = (FlowSet) analysis.getFlowBefore(s);
         
            unitToGuaranteedDefs.put(s, Collections.unmodifiableList(set.toList()));
        }
    }

    /**
     * Returns a list of locals guaranteed to be defined at (just
     * before) program point <tt>s</tt>.
     **/
    public List getGuaranteedDefs(Unit s) {
        return unitToGuaranteedDefs.get(s);
    }
}

/**
 * Flow analysis to determine all locals guaranteed to be defined at a
 * given program point.
 **/
class GuaranteedDefsAnalysis extends ForwardFlowAnalysis {
    FlowSet emptySet = new ArraySparseSet();
    // maps Units (program stmts) to FlowSets (Soot's set lattice) representing
    // the GEN set
    Map<Unit, FlowSet> unitToGenerateSet;

    /**
     * All INs are initialized to emptySet
     **/
    protected Object newInitialFlow() {
        // TODO: return All INs
        return emptySet.clone();
    }

    /**
     * IN (Start) is emptySet
     **/
    protected Object entryInitialFlow() {
        // TODO: return IN (Start)
        return emptySet.clone();
    }

    protected void copy(Object source, Object dest) {
        FlowSet sourceSet = (FlowSet) source;
        FlowSet destSet = (FlowSet) dest;

        sourceSet.copy(destSet);
    }

    /**
     * All paths joined by intersection
     **/
    protected void merge(Object in1, Object in2, Object out) {
        FlowSet inSet1 = (FlowSet) in1;
        FlowSet inSet2 = (FlowSet) in2;
        FlowSet outSet = (FlowSet) out;

        // TODO: implement the join operator on data flow information
        inSet1.intersection(inSet2, outSet);

    }

    /**
     * OUT is union with variables defined by the statement
     **/
    protected void flowThrough(Object inValue, Object unit, Object outValue) {
        FlowSet in = (FlowSet) inValue;
        FlowSet out = (FlowSet) outValue;
        
        in.union(unitToGenerateSet.get(unit), out);
    }

    GuaranteedDefsAnalysis(UnitGraph graph) {
        super(graph);

        // Initial empty GEN set
        unitToGenerateSet = new HashMap<Unit, FlowSet>(graph.size() * 2 + 1, 0.7f);
       
        // TODO: collect defs at each unit
        Iterator unitIt = graph.iterator();
        while (unitIt.hasNext()) {
            Unit s = (Unit) unitIt.next();
            FlowSet genSet = emptySet.clone();
                if (s instanceof DefinitionStmt) {
                Value leftOp = ((DefinitionStmt) s).getLeftOp();
                    if(leftOp instanceof Local) {
                    Local v = (Local) leftOp;
                    genSet.add(v, genSet);
                    }
                }
            unitToGenerateSet.put(s, genSet);
        }

        // Run the analysis
        doAnalysis();
    }
}
