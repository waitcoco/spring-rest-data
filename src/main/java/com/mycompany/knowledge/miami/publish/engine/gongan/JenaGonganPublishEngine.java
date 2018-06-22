package com.mycompany.knowledge.miami.publish.engine.gongan;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.mycompany.knowledge.miami.publish.engine.PublishEngine;
import com.mycompany.knowledge.miami.publish.jena.FusekiJenaLibrary;
import com.mycompany.knowledge.miami.publish.model.gongan.Bilu;
import com.mycompany.knowledge.miami.publish.model.gongan.Case;
import com.mycompany.knowledge.miami.publish.model.gongan.Person;
import com.mycompany.knowledge.miami.publish.model.gongan.Relation;
import com.mycompany.knowledge.miami.publish.repository.BiluRepository;
import com.mycompany.knowledge.miami.publish.repository.CaseRepository;
import com.mycompany.knowledge.miami.publish.repository.PersonRepository;
import com.mycompany.knowledge.miami.publish.repository.RelationRepository;
import lombok.val;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JenaGonganPublishEngine implements PublishEngine{
//    private DatasetAccessor accessor;
    @Autowired
    private BiluRepository biluRepository;
    @Autowired
    private CaseRepository caseRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private RelationRepository relationRepository;

    public FusekiJenaLibrary fusekiJenaLibrary;
    private Logger logger = Logger.getLogger(JenaGonganPublishEngine.class);
    private String inputModelName;
    public JenaGonganPublishEngine(String fusekiURI, String modelName) {
        this.inputModelName = modelName;
        this.fusekiJenaLibrary = new FusekiJenaLibrary(fusekiURI);
    }

    @Override
    public String publish() {
        logger.info("In jena gongan publish engine");
        publishCases();
        return "finished";
    }


    public void publishCases(){
        val model = fusekiJenaLibrary.getModel(inputModelName);
        if (model == null) {
            throw new RuntimeException("Can not get model " + inputModelName);
        }

        val iterator = fusekiJenaLibrary.getStatementsByEntityType(model, "gongan:gongan.case");

        while (iterator.hasNext()) {
            Resource resource = iterator.next().getSubject();

            CaseBase aCaseBase = new CaseBase();
            Case aCase = new Case();
            aCaseBase.setSubjectId(resource.toString());
            aCase.setId(resource.toString());
            List<String> csIds = fusekiJenaLibrary.getStringValueBySP(model, resource, "common:type.object.id");
            if (csIds.size() > 0)
                aCaseBase.setCaseId(csIds.get(0));
                aCase.setCaseId(csIds.get(0));

            List<String> csNames = fusekiJenaLibrary.getStringValueBySP(model, resource, "common:type.object.name");
            if (csNames.size() > 0)
                aCaseBase.setCaseName(csNames.get(0));
                aCase.setName(csNames.get(0));

            List<String> csTypes = fusekiJenaLibrary.getStringValueBySP(model, resource, "gongan:gongan.case.category");
            if (csTypes.size() > 0)
                aCaseBase.setCaseType(String.join(",", csTypes));
                aCase.setType(String.join(",",csTypes));

            val biluIter1 = fusekiJenaLibrary.getStatementsBySP(model, resource, "gongan:gongan.case.bilu");
            while (biluIter1.hasNext()) {
                Resource biluResource = biluIter1.next().getResource();
//                if (!biluCache.containsKey(biluResource)) {
//                   BiluBase bilu = getBiluInfo(model, biluResource);
//                    biluCache.put(biluResource.toString(), bilu);
//                }

                // todo @jinzhao
                // 需要在这个地方查bilu的表，看bilu是否已经存在

                BiluBase biluBase = getBiluInfo(model, biluResource);
                aCaseBase.getBilus().add(biluBase);

                for (String pSubjectId : biluBase.getConnections().keySet()) {
                    if (!aCaseBase.getConnections().containsKey(pSubjectId))
                        aCaseBase.getConnections().put(pSubjectId, biluBase.getConnections().get(pSubjectId));
                    else {
                        if(!aCaseBase.getConnections().get(pSubjectId).contains(biluBase.getConnections().get(pSubjectId)))
                            aCaseBase.getConnections().put(pSubjectId, biluBase.getConnections().get(pSubjectId) + "；" + aCaseBase.getConnections().get(pSubjectId));
                    }
                }
            }
            val biluList = new ArrayList<Bilu>();
            for(val biluBase:aCaseBase.getBilus()){
                val bilu  = new Bilu();
                Gson gson = new Gson();
                bilu.setContent(biluBase.getContent());
                bilu.setName(biluBase.getName());
                bilu.setBiluId(biluBase.getBiluId());
                bilu.setSubjectId(biluBase.getSubjectId());
                bilu.setACase(aCase);
                bilu.setBankcards(gson.toJson(biluBase.getBankCards()));
                bilu.setPhones(gson.toJson(biluBase.getPhones()));
                biluList.add(bilu);
                biluRepository.save(bilu);
                for(val perosn : biluBase.getPerson()){
                    int i = 0;
                    val relation = new Relation();
                    relation.setSubjectId(i);
                    i++;
                    relation.setPersonSubjectId(perosn.getSubjectId());
                    relation.setBiluSubjectId(biluBase.getSubjectId());
                    relation.setCaseSubjectId(aCase.getId());
                    relationRepository.save(relation);
                }
            }
            aCase.setBilus(biluList);
            caseRepository.save(aCase);
        }
    }

    private BiluBase getBiluInfo(Model model, Resource resource) {
        BiluBase biluBase = new BiluBase();

        biluBase.setSubjectId(resource.toString());

        //bilu id
        List<String> bilu_id_list = fusekiJenaLibrary.getStringValueBySP(model, resource, "common:type.object.id");
        biluBase.setBiluId(bilu_id_list.size() > 0 ? bilu_id_list.get(0) : "");

        //bilu name
        List<String> bilu_name_list = fusekiJenaLibrary.getStringValueBySP(model, resource, "common:type.object.name");
        biluBase.setName(bilu_name_list.size() > 0 ? bilu_name_list.get(0) : "");

        //bilu content
        List<String> bilu_content_list = fusekiJenaLibrary.getStringValueBySP(model, resource, "common:common.document.contentStream");
        biluBase.setContent(bilu_content_list.size() > 0 ? bilu_content_list.get(0) : "");

        // set personBases
        val entities = Lists.newArrayList(fusekiJenaLibrary.getStatementsBySP(model, resource, "gongan:gongan.bilu.entity")).stream().map(s -> s.getResource().toString()).distinct().collect(Collectors.toList());
        val persons = Lists.newArrayList(fusekiJenaLibrary.getStatementsByPOValue(model, "common:type.object.type", "common:person.person")).stream().map(s -> s.getSubject().toString()).distinct().collect(Collectors.toList());
        persons.retainAll(entities);

        List<String> biluConnections = Lists.newArrayList(fusekiJenaLibrary.getStatementsByPO(model, "common:common.connection.from", resource)).stream().map(s -> s.getSubject().toString()).distinct().collect(Collectors.toList());
        for (String personSubject : persons) {
            // set personBase information
//            if (!personCache.containsKey(personSubject)) {
//                PersonBase personBase = getPersonInfo(model, model.getResource(personSubject));
//                personCache.put(personSubject, personBase);
//            }

            // todo @jinzhao
            // 需要在这个地方查person的表，看person是否已经存在
            Person person = getPersonInfo(model, model.getResource(personSubject));
            biluBase.getPerson().add(person);


            // set connection
            List<String> connectionVal = Lists.newArrayList(fusekiJenaLibrary.getStatementsByPO(model, "common:common.connection.to", model.getResource(personSubject))).stream().map(s -> s.getSubject().toString()).distinct().collect(Collectors.toList());
            connectionVal.retainAll(biluConnections);

            String role = "";
            for (String connection : connectionVal) {
                val connectionType = fusekiJenaLibrary.getStringValueBySP(model, model.getResource(connection), "common:common.connection.type");

                if (connectionType.contains("common:common.connection.BiluEntityXianyiren"))
                    role += role.contains(Role.BiluEntityXianyiren.toString())? "" : (Role.BiluEntityXianyiren.toString() + "；");
                if (connectionType.contains("common:common.connection.BiluEntityZhengren"))
                    role += role.contains(Role.BiluEntityZhengren.toString())? "" : (Role.BiluEntityZhengren.toString() + "；");
                if (connectionType.contains("common:common.connection.BiluEntityBaoanren"))
                    role += role.contains(Role.BiluEntityBaoanren.toString())? "" : (Role.BiluEntityBaoanren.toString() + "；");
                if (connectionType.contains("common:common.connection.BiluEntityDangshiren"))
                    role += role.contains(Role.BiluEntityDangshiren.toString())? "" : (Role.BiluEntityDangshiren.toString() + "；");
                if (connectionType.contains("common:common.connection.BiluEntityShouhairen"))
                    role += role.contains(Role.BiluEntityShouhairen.toString())? "" : (Role.BiluEntityShouhairen.toString() + "；");
            }

            int roleLength = role.length();
            if (roleLength > 0) {
                biluBase.getConnections().put(personSubject, role.substring(0, roleLength - 1));
            }
        }

        // get all things
        val things = Lists.newArrayList(fusekiJenaLibrary.getStatementsBySP(model, resource, "gongan:gongan.bilu.thing")).stream().map(s -> s.getResource().toString()).distinct().collect(Collectors.toList());

        // set phone
        val phones = Lists.newArrayList(fusekiJenaLibrary.getStatementsByPOValue(model, "common:type.object.type", "common:thing.phone")).stream().map(s -> s.getSubject().toString()).distinct().collect(Collectors.toList());
        phones.retainAll(things);
        for (String phone : phones) {
            List<String> phoneNums = fusekiJenaLibrary.getStringValueBySP(model, model.getResource(phone), "common:thing.phone.phoneNumber");
            if (phoneNums.size() > 0)
                biluBase.getPhones().put(phone, phoneNums.get(0));
        }
        // set bank cards
        val bankCards = Lists.newArrayList(fusekiJenaLibrary.getStatementsByPOValue(model, "common:type.object.type", "common:thing.bankcard")).stream().map(s -> s.getSubject().toString()).distinct().collect(Collectors.toList());
        bankCards.retainAll(things);
        for (String bankCard : bankCards) {
            List<String> bankCardNums = fusekiJenaLibrary.getStringValueBySP(model, model.getResource(bankCard), "common:thing.bankcard.bankCardId");
            if (bankCardNums.size() > 0)
                biluBase.getBankCards().put(bankCard, bankCardNums.get(0));
        }

        return biluBase;
    }

    private Person getPersonInfo(Model model, Resource resource) {
//        PersonBase personBase = personRelationCache.getOrDefault(resource.toString(), null);

        // todo
        // 需要在这个地方查person 表，看这个person是否已经有了；
        PersonBase personBase = null;
        if(personBase == null) {
            personBase = new PersonBase();

            personBase.setSubjectId(resource.toString());
            val pNames = fusekiJenaLibrary.getStringValueBySP(model, resource, "common:type.object.name");
            if (pNames.size() > 0)
                personBase.setName(pNames.get(0));

            val personIdentities = fusekiJenaLibrary.getStatementsBySP(model, resource, "common:personBase.personBase.identification");
            if (personIdentities.hasNext()) {
                val personIds = fusekiJenaLibrary.getStringValueBySP(model, personIdentities.next().getResource(), "common:personBase.identification.number");
                if (personIds.size() > 0)
                    personBase.setIdentity(personIds.get(0));
            }

            // set bilus
            val relatedBilus = Lists.newArrayList(fusekiJenaLibrary.getStatementsByPO(model, "gongan:gongan.bilu.entity", resource))
                    .stream().map(s -> s.getSubject().toString()).distinct().collect(Collectors.toList());

            personBase.setBiluList(relatedBilus);

            // set cases
            val relatedCases = Lists.newArrayList(fusekiJenaLibrary.getStatementsByBatchPO(model, "gongan:gongan.case.bilu", new HashSet<>(relatedBilus)))
                    .stream().map(s -> s.getSubject().toString()).distinct().collect(Collectors.toList());

            personBase.setCaseList(relatedCases);
        }

        if(personBase.getPhone() == null || personBase.getPhone().isEmpty()) {
            val contactIters = fusekiJenaLibrary.getStatementsBySP(model, resource, "common:personBase.personBase.contact");
            if (contactIters.hasNext()) {
                val contacts = fusekiJenaLibrary.getStringValueBySP(model, contactIters.next().getResource(), "common:personBase.contact.number");
                if (contacts.size() > 0)
                    personBase.setPhone(contacts.get(0));
            }
        }

        if(personBase.getBirthDay() == null || personBase.getBirthDay().isEmpty()) {
            val birthdays = fusekiJenaLibrary.getStringValueBySP(model, resource, "common:personBase.personBase.birthDate");
            if (birthdays.size() > 0)
                personBase.setBirthDay(birthdays.get(0));
        }

        if(personBase.getGender() == null || personBase.getGender().isEmpty()) {
            val genders = fusekiJenaLibrary.getStringValueBySP(model, resource, "common:personBase.personBase.gender");
            if (genders.size() > 0) {
                if (genders.get(0).toLowerCase().equals("female"))
                    personBase.setGender("女");
                else if (genders.get(0).toLowerCase().equals("male"))
                    personBase.setGender("男");
            }
        }
        val person = new Person();
        person.setName(personBase.getName());
        person.setPhone(personBase.getPhone());
        person.setSubjectId(personBase.getSubjectId());
        person.setBirthDay(personBase.getBirthDay());
        person.setGender(personBase.getGender());
        person.setIdentity(personBase.getIdentity());
        personRepository.save(person);
        return person;

    }
}
