package esl.cuenet.generative;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import esl.cuenet.generative.structs.ContextNetwork;
import esl.cuenet.generative.structs.NetworkBuildingHelper;
import esl.cuenet.generative.structs.Propagate;
import esl.cuenet.generative.structs.SpaceTimeValueGenerators;
import esl.system.SysLoggerUtils;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class PropagationUnitTest {

    static{
        SysLoggerUtils.initLogger();
    }

    Logger logger = Logger.getLogger(getClass());

    @Test
    public void testAcrossEventsContainingPhotos() throws IOException {
        String distanceFile = "/data/ranker/real/ontology_cuenet.distances.txt";

        List<String> locationStrings = Lists.newArrayList();
        locationStrings.add("bounds");
        logger.info("Building Network...");
        ContextNetwork network = NetworkBuildingHelper.loadForUnitPropagationTest(locationStrings);
        logger.info(network.count() + " " + locationStrings.size());
        network.printTree(true);

        SpaceTimeValueGenerators stGenerator = new SpaceTimeValueGenerators(locationStrings.iterator());

        Propagate propagator = new Propagate(network, distanceFile, stGenerator);
        propagator.show();

        propagator.prepare(Sets.newHashSet("64"));

        double l1delta;
//        double[] deltas = new double[10];

        for (int i=0; i<10; i++) {
            l1delta = propagator.propagateOnceTable();
            logger.info("delta = " + l1delta);
//            propagator.printScores(4, 9);
//            deltas[i] = l1delta;
        }

        logger.info("-----  5 -----"); propagator.printScores(8, 5);
        logger.info("----- 15 -----"); propagator.printScores(8, 15);
        logger.info("------25 -----"); propagator.printScores(8, 25);
        logger.info("------35 -----"); propagator.printScores(8, 35);
    }

}
