package se.chalmers.cs.gf.dialogutil;

import com.sri.oaa2.com.*;
import com.sri.oaa2.icl.*;
import com.sri.oaa2.lib.*;

import java.io.IOException;
import java.util.*;

/**
 *  An simple OAA agent which does not have any solvables, but which can
 *  call other agents.
 */
public class OAAClient {

        private LibOaa outOaa;

        public OAAClient(String agentName, String[] libcomargs) throws IOException {
                outOaa = new LibOaa(new LibCom(new LibComTcpProtocol(),libcomargs));

                if (!outOaa.oaaSetupCommunication(agentName))
                        throw new IOException("Could not connect to facilitator");

                if (!outOaa.oaaRegister("parent", agentName, new IclList(), new IclList()))
                        throw new IOException("Could not register");

                outOaa.oaaReady(true);
        }

        /**
         *  Send a task to the agent community.
         *
         *  @param goal The task
         *  @param params Parameters
         *  @return A possibly empty list of solutions.
         */
        public List<IclTerm> solve(IclTerm goal, List<IclTerm> params) {
                IclList answers = new IclList();

                if (!outOaa.oaaSolve(goal, IclUtil.fromList(params), answers))
                        return Collections.<IclTerm>emptyList();

                return IclUtil.toList(answers);
        }

        /**
         *  Send a task to the agent community. Same as solve(goal, empty list).
         */
        public List<IclTerm> solve(IclTerm goal) {
                return solve(goal, Collections.<IclTerm>emptyList());
        }

}
