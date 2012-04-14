package esl.cuenet.algorithms.firstk.structs.eventgraph;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Property;
import esl.datastructures.graph.relationgraph.RelationGraphNode;
import org.apache.log4j.Logger;

public abstract class EventGraphNode extends RelationGraphNode {

    protected Individual individual = null;
    private EventGraph.NodeType type = null;
    private static Logger logger = Logger.getLogger(EventGraphNode.class);

    public EventGraphNode(Individual individual, EventGraph.NodeType type) {
        super(individual.getURI());
        if (individual.getURI() == null) logger.error("Individual with NULL URI");
        this.individual = individual;
        this.type = type;
    }

    public Individual getIndividual() {
        return individual;
    }

    public EventGraph.NodeType getType() {
        return type;
    }

    public void addLiteral(Property property, Literal literal) {
        individual.addLiteral(property, literal);
    }

    public void addResource(Property property, Individual p) {
        individual.addProperty(property, p);
    }

}
