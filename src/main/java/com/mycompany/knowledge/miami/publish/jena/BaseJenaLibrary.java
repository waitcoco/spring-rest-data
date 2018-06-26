package com.mycompany.knowledge.miami.publish.jena;

import lombok.Data;
import lombok.val;
import org.apache.jena.rdf.model.*;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class BaseJenaLibrary implements JenaLibrary{
    protected boolean jenaDropExistModel = false;
    protected String modelName;
    protected Model cacheModel = null;

    @Override
    public String getModelName(){
        return modelName;
    }

    @Override
    public Model getModel(String modelName) {
        return null;
    }

    @Override
    public void removeModel(String modelName) {

    }

    @Override
    public void saveModel(Model newModel, String newModelName) {
    }

    @Override
    public Model deepCopyModel(Model model){
        return ModelFactory.createDefaultModel().add(model);
    }

    @Override
    public void closeDB() {

    }

    /**
     * 获取模型中所有Statement
     * @param model
     * @return
     */
    @Override
    public List<Statement> getStatements(Model model) {
        List<Statement> stmts;
        try {
            StmtIterator sIter = model.listStatements() ;
            stmts = new LinkedList<>();
            for ( ; sIter.hasNext() ; )
            {
                stmts.add(sIter.nextStatement());
            }
            sIter.close();
        } finally {
        }
        return stmts;
    }

    @Override
    public Iterator<Statement> getStatementsByEntityType(Model model, String type) {
        Property property = model.getProperty("common:type.object.type");
        SimpleSelector simpleSelector = new SimpleSelector(null, property, type);
        return model.listStatements(simpleSelector);
    }

    @Override
    public Iterator<Statement> getStatementsById(Model model, String id) {
        Property property = model.getProperty("common:type.object.subjectId");
        SimpleSelector simpleSelector = new SimpleSelector(null, property, id);
        return model.listStatements(simpleSelector);
    }

    @Override
    public Iterator<Statement> getStatementsBySP(Model model, Resource resource, String property) {
        Property p = model.getProperty(property);
        SimpleSelector simpleSelector = new SimpleSelector(resource, p, (RDFNode) null);
        return model.listStatements(simpleSelector);
    }

    @Override
    public Iterator<Statement> getStatementsByBatchSP(Model model, HashSet<String> subjects, String property_str) {
        SimpleSelector selector = new SimpleSelector(null, null, (RDFNode) null) {
            Property property = model.getProperty(property_str);

            @Override
            public boolean selects(Statement s) {
                return subjects.contains(s.getSubject().toString()) && s.getPredicate().equals(property);
            }
        };
        return model.listStatements(selector);
    }

    @Override
    public Map<String, List<String>> getSOMapByBatchSP(Model model, HashSet<String> subjects, String property_str)
    {
        Iterator<Statement> iter = getStatementsByBatchSP(model, subjects, property_str);

        Map<String, List<String>> spMap = new HashMap<>();
        while (iter.hasNext()) {
            Statement statement = iter.next();
            String object = statement.getObject().toString();
            String subject = statement.getSubject().toString();
            if (!spMap.containsKey(subject))
                spMap.put(subject, new LinkedList<>());
            spMap.get(subject).add(object);
        }
        return spMap;
    }

    @Override
    public Map<String, List<String>> getOSMapByBatchSP(Model model, HashSet<String> subjects, String property_str)
    {
        Iterator<Statement> iter = getStatementsByBatchSP(model, subjects, property_str);

        Map<String, List<String>> spMap = new HashMap<>();
        while (iter.hasNext()) {
            Statement statement = iter.next();
            String object = statement.getObject().toString();
            String subject = statement.getSubject().toString();
            if (!spMap.containsKey(object))
                spMap.put(object, new LinkedList<>());
            spMap.get(object).add(subject);
        }
        return spMap;
    }

    @Override
    public Iterator<Statement> getStatementsByPOValue(Model model, String property, String value)
    {
        SimpleSelector simpleSelector = new SimpleSelector(null, model.getProperty(property), value);
        return model.listStatements(simpleSelector);
    }

    @Override
    public Iterator<Statement> getStatementsByPO(Model model, String property, Resource object)
    {
        SimpleSelector simpleSelector = new SimpleSelector(null, model.getProperty(property), object);
        return model.listStatements(simpleSelector);
    }

    @Override
    public Iterator<Statement> getStatementsByBatchPO(Model model, String property, HashSet<String> objects)
    {
        SimpleSelector simpleSelector = new SimpleSelector(null, model.getProperty(property), (RDFNode)null){
            public boolean selects(Statement st) {
                return objects.contains(st.getObject().toString());
            }
        };
        return model.listStatements(simpleSelector);
    }

    @Override
    public Map<String, List<String>> getSOMapByBatchPO(Model model, String property, HashSet<String> objects)
    {
        Iterator<Statement> iter = getStatementsByBatchPO(model, property, objects);

        Map<String, List<String>> spMap = new HashMap<>();
        while (iter.hasNext()) {
            Statement statement = iter.next();
            String object = statement.getObject().toString();
            String subject = statement.getSubject().toString();
            if (!spMap.containsKey(subject))
                spMap.put(subject, new LinkedList<>());
            spMap.get(subject).add(object);
        }
        return spMap;
    }

    @Override
    public Map<String, List<String>> getOSMapByBatchPO(Model model, String property, HashSet<String> objects)
    {
        Iterator<Statement> iter = getStatementsByBatchPO(model, property, objects);

        Map<String, List<String>> spMap = new HashMap<>();
        while (iter.hasNext()) {
            Statement statement = iter.next();
            String object = statement.getObject().toString();
            String subject = statement.getSubject().toString();
            if (!spMap.containsKey(object))
                spMap.put(object, new LinkedList<>());
            spMap.get(object).add(subject);
        }
        return spMap;
    }

    @Override
    public Iterator<Statement> getStatementsBySourceAndType(Model model, String source, String type) {
        SimpleSelector selector = new SimpleSelector(null, null, (RDFNode)null) {
            Property property = model.getProperty("common:type.object.type");
            public boolean selects(Statement st) {
                return st.getSubject().toString().startsWith(source) && Objects.equals(property, st.getPredicate()) && st.getObject().toString().equals(type);
            }
        };
        return model.listStatements(selector);
    }

    @Override
    public Iterator<Statement> getStatementsBySubjectSubStr(Model model, String substr) {
        SimpleSelector selector = new SimpleSelector(null, null, (RDFNode)null) {
            Property property = model.getProperty("common:type.object.type");
            public boolean selects(Statement st) {
                return st.getSubject().toString().contains(substr);
            }
        };
        return model.listStatements(selector);
    }

    @Override
    public List<String> getStringValueBySP(Model model, Resource resource, String property)
    {
        Property p = model.getProperty(property);
        SimpleSelector simpleSelector = new SimpleSelector(resource, p, (RDFNode) null);
        val iterator = model.listStatements(simpleSelector);

        List<String> values = new LinkedList<>();
        while(iterator.hasNext())
        {
            Statement statement = iterator.next();
            values.add(statement.getString());
        }
        return values;
    }

    @Override
    public List<String> getStringValuesByBatchSP(Model model, HashSet<String> subjects, String property)
    {
        Property p = model.getProperty(property);
        SimpleSelector simpleSelector = new SimpleSelector(null, p, (RDFNode) null) {
            @Override
            public boolean selects(Statement s) {
                return subjects.contains(s.getSubject().toString());
            }
        };

        val iterator = model.listStatements(simpleSelector);

        List<String> values = new LinkedList<>();
        while(iterator.hasNext())
        {
            Statement statement = iterator.next();
            values.add(statement.getString());
        }
        return values;
    }

    @Override
    public List<String> getObjectNamesBySP(Model model, Resource resource, String property)
    {
        Property p = model.getProperty(property);
        SimpleSelector simpleSelector = new SimpleSelector(resource, p, (RDFNode) null);
        val iterator = model.listStatements(simpleSelector);

        val subjects = iterator.toList().stream().map(iter->iter.getResource().toString()).collect(Collectors.toList());

        SimpleSelector selector = new SimpleSelector(null, model.getProperty("common:type.object.name"),(RDFNode) null) {
            @Override
            public boolean selects(Statement s) {
                return subjects.contains(s.getSubject().toString());
            }
        };

        val nameIters = model.listStatements(selector);

        return nameIters.toList().stream().map(iter->iter.getString()).collect(Collectors.toList());
    }
}
