package net.java.amateras.uml.testcase.tcg;

import net.java.amateras.uml.testcase.io.Parser;
import net.java.amateras.uml.testcase.model.Automaton;

/**
 * Created by zhangxin on 2016/5/28.
 */
public class TCG {
    public void generate(String modelFileName) {
        long stime = System.currentTimeMillis();
        Automaton automaton = new Parser(modelFileName).parseXML();
        //automaton.dumps("tmp.txt");
        automaton.dumps();
//		Generator generator = new DFSPCGenerator(automaton);
        Generator generator = new BFMCDCGenerator(automaton);
        generator.start();
       // new Writer(modelFileName.substring(0, modelFileName.length() - 4) + ".tsf").writeXML(generator.getTestCases());
        long etime = System.currentTimeMillis();
        System.out.println("Runtime : " + (etime - stime) + "ms");
    }

    public static void main(String args[]) {
        new TCG().generate("Testeia.txt");
//		new TCG().generate("TransAltFlash_5224.eia");
//		new TCG().generate("TransAltFlash_12316.eia");
    }
}
