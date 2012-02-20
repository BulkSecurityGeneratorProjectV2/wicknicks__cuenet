package esl.cuenet.mapper.tree;

import com.hp.hpl.jena.ontology.OntModel;
import esl.cuenet.source.Source;
import org.apache.log4j.Logger;

import java.util.Iterator;

public class ParseTreeInterpreter {

    private IParseTree parseTree = null;
    private Logger logger = Logger.getLogger(ParseTreeInterpreter.class);
    private SourceMapper sourceMapper = new SourceMapper();
    private OntModel model = null;

    public ParseTreeInterpreter(IParseTree parseTree) {
        this.parseTree = parseTree;

//        logger.info("Loading Ontology Models");
//        model = ModelFactory.createOntologyModel();
//        try {
//            model.read(new FileReader("/home/arjun/Documents/Dropbox/Ontologies/cuenet-main/cuenet-main.owl"),
//                    "http://www.semanticweb.org/arjun/cuenet-main.owl");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

    }

    public void interpret() {

        //process namespace mappings
        IParseTreeNode document = parseTree.getDocument();
        for (IParseTreeNode child: document.children()) {
            if (child.getType() != IParseTreeNode.Type.OPERATOR) continue;

            if (child.getLabel().equalsIgnoreCase(MappingOperators.NAMESPACE))
                interpretNamespace(child);
        }

        for (IParseTreeNode child: document.children()) {
            if (child.getType() != IParseTreeNode.Type.OPERATOR) continue;

            if (child.getLabel().equalsIgnoreCase(MappingOperators.SOURCE))
                interpretSourceDeclaration(child);
        }

    }

    private void interpretSourceDeclaration(IParseTreeNode sourceNode) {
        Source source = null;
        for (IParseTreeNode child: sourceNode.children()) {
            if (child.getType() == IParseTreeNode.Type.OPERAND) {
                source = sourceMapper.createSource(child.getLabel());
            }
        }

        for (IParseTreeNode child: sourceNode.children()) {
            if (child.getType() != IParseTreeNode.Type.OPERATOR) continue;

            if (child.getLabel().equalsIgnoreCase(MappingOperators.SOURCE_IO))
                associateIO(source, child);

            if (child.getLabel().equalsIgnoreCase(MappingOperators.SOURCE_TYPE))
                associateType(source, child);

            if (child.getLabel().equalsIgnoreCase(MappingOperators.AXIOM))
                logger.info("Found axiom operator");
        }

    }

    private void associateType(Source source, IParseTreeNode type) {
        IParseTreeNode node = type.children().get(0);
        if (node.getLabel().equalsIgnoreCase("personal")) source.setType(Source.TYPE.PERSONAL);
        else if (node.getLabel().equalsIgnoreCase("social")) source.setType(Source.TYPE.SOCIAL);
        else if (node.getLabel().equalsIgnoreCase("public")) source.setType(Source.TYPE.PUBLIC);
        else throw new RuntimeException("Unknown Source type associated with source " + source.getName());
    }

    private void associateIO(Source source, IParseTreeNode io) {
        IParseTreeNode node = io.children().get(0);
        if (node.getLabel().equalsIgnoreCase("disk")) source.setIO(Source.IO.DISK);
        else if (node.getLabel().equalsIgnoreCase("network")) source.setIO(Source.IO.NETWORK);
        else throw new RuntimeException("Unknown IO method associated to source " + source.getName());
    }

    private void interpretNamespace(IParseTreeNode namespaceNode) {
        if (namespaceNode.children().size() != 2) throw new RuntimeException("Bad namespace node");
        
        Iterator<IParseTreeNode> children = namespaceNode.children().iterator();

        IParseTreeNode uriNode = children.next();   // get the uri
        IParseTreeNode shortHandNode = children.next();   // get the shorthand

        sourceMapper.addNamespaceMapping(uriNode.getLabel(), shortHandNode.getLabel());
    }

}