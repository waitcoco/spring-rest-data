package com.mycompany.knowledge.miami.publish.engine.gongan;

import com.google.common.collect.Lists;
import com.mycompany.knowledge.miami.publish.engine.PublishEngine;
import com.mycompany.knowledge.miami.publish.jena.FusekiJenaLibrary;
import lombok.val;
import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.query.DatasetAccessorFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.log4j.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class JenaGonganPublishEngine implements PublishEngine{
//    private DatasetAccessor accessor;
    public FusekiJenaLibrary fusekiJenaLibrary;
    private String fusekiURI;
    private Logger logger = Logger.getLogger(JenaGonganPublishEngine.class);
    private String inputModelName;
    public JenaGonganPublishEngine(String fusekiURI, String modelName) {
        this.fusekiURI = fusekiURI;
//        this.accessor = DatasetAccessorFactory.createHTTP(fusekiURI);
        this.inputModelName = modelName;
        this.fusekiJenaLibrary = new FusekiJenaLibrary(fusekiURI);
    }

    @Override
    public String publish() {
        logger.info("In jena gongan publish engine");
        return this.inputModelName;
    }


    public void PublishCases(){
        val model = fusekiJenaLibrary.getModel(inputModelName);
        if (model == null) {
            throw new RuntimeException("Can not get model " + inputModelName);
        }

        val iterator = fusekiJenaLibrary.getStatementsByEntityType(model, "gongan:gongan.case");

        while (iterator.hasNext()) {
            Resource resource = iterator.next().getSubject();

            Case aCase = new Case();

            aCase.setSubjectId(resource.toString());

            List<String> csIds = fusekiJenaLibrary.getStringValueBySP(model, resource, "common:type.object.id");
            if (csIds.size() > 0)
                aCase.setCaseId(csIds.get(0));

            List<String> csNames = fusekiJenaLibrary.getStringValueBySP(model, resource, "common:type.object.name");
            if (csNames.size() > 0)
                aCase.setCaseName(csNames.get(0));

            List<String> csTypes = fusekiJenaLibrary.getStringValueBySP(model, resource, "gongan:gongan.case.category");
            if (csTypes.size() > 0)
                aCase.setCaseType(String.join(",", csTypes));

            val biluIter1 = fusekiJenaLibrary.getStatementsBySP(model, resource, "gongan:gongan.case.bilu");
            while (biluIter1.hasNext()) {
                Resource biluResource = biluIter1.next().getResource();
//                if (!biluCache.containsKey(biluResource)) {
//                    Bilu bilu = getBiluInfo(model, biluResource);
//                    biluCache.put(biluResource.toString(), bilu);
//                }

                // todo @jinzhao
                // 需要在这个地方查bilu的表，看bilu是否已经存在
                Bilu bilu = getBiluInfo(model, biluResource);
                aCase.getBilus().add(bilu);

                for (String pSubjectId : bilu.getConnections().keySet()) {
                    if (!aCase.getConnections().containsKey(pSubjectId))
                        aCase.getConnections().put(pSubjectId, bilu.getConnections().get(pSubjectId));
                    else {
                        if(!aCase.getConnections().get(pSubjectId).contains(bilu.getConnections().get(pSubjectId)))
                            aCase.getConnections().put(pSubjectId, bilu.getConnections().get(pSubjectId) + "；" + aCase.getConnections().get(pSubjectId));
                    }
                }
            }
        }
    }

    private Bilu getBiluInfo(Model model, Resource resource) {
        Bilu bilu = new Bilu();

        bilu.setSubjectId(resource.toString());

        //bilu id
        List<String> bilu_id_list = fusekiJenaLibrary.getStringValueBySP(model, resource, "common:type.object.id");
        bilu.setBiluId(bilu_id_list.size() > 0 ? bilu_id_list.get(0) : "");

        //bilu name
        List<String> bilu_name_list = fusekiJenaLibrary.getStringValueBySP(model, resource, "common:type.object.name");
        bilu.setName(bilu_name_list.size() > 0 ? bilu_name_list.get(0) : "");

        //bilu content
        List<String> bilu_content_list = fusekiJenaLibrary.getStringValueBySP(model, resource, "common:common.document.contentStream");
        bilu.setContent(bilu_content_list.size() > 0 ? bilu_content_list.get(0) : "");

        // set persons
        val entities = Lists.newArrayList(fusekiJenaLibrary.getStatementsBySP(model, resource, "gongan:gongan.bilu.entity")).stream().map(s -> s.getResource().toString()).distinct().collect(Collectors.toList());
        val persons = Lists.newArrayList(fusekiJenaLibrary.getStatementsByPOValue(model, "common:type.object.type", "common:person.person")).stream().map(s -> s.getSubject().toString()).distinct().collect(Collectors.toList());
        persons.retainAll(entities);

        List<String> biluConnections = Lists.newArrayList(fusekiJenaLibrary.getStatementsByPO(model, "common:common.connection.from", resource)).stream().map(s -> s.getSubject().toString()).distinct().collect(Collectors.toList());
        for (String personSubject : persons) {
            // set person information
//            if (!personCache.containsKey(personSubject)) {
//                Person person = getPersonInfo(model, model.getResource(personSubject));
//                personCache.put(personSubject, person);
//            }

            // todo @jinzhao
            // 需要在这个地方查person的表，看person是否已经存在
            Person person = getPersonInfo(model, model.getResource(personSubject));
            bilu.getPersons().add(person);


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
                bilu.getConnections().put(personSubject, role.substring(0, roleLength - 1));
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
                bilu.getPhones().put(phone, phoneNums.get(0));
        }
        // set bank cards
        val bankCards = Lists.newArrayList(fusekiJenaLibrary.getStatementsByPOValue(model, "common:type.object.type", "common:thing.bankcard")).stream().map(s -> s.getSubject().toString()).distinct().collect(Collectors.toList());
        bankCards.retainAll(things);
        for (String bankCard : bankCards) {
            List<String> bankCardNums = fusekiJenaLibrary.getStringValueBySP(model, model.getResource(bankCard), "common:thing.bankcard.bankCardId");
            if (bankCardNums.size() > 0)
                bilu.getBankCards().put(bankCard, bankCardNums.get(0));
        }

        return bilu;
    }

    private Person getPersonInfo(Model model, Resource resource) {
//        Person person = personRelationCache.getOrDefault(resource.toString(), null);

        // todo
        // 需要在这个地方查person 表，看这个person是否已经被处理了；
        Person person = null;
        if(person == null) {
            person = new Person();

            person.setSubjectId(resource.toString());
            val pNames = fusekiJenaLibrary.getStringValueBySP(model, resource, "common:type.object.name");
            if (pNames.size() > 0)
                person.setName(pNames.get(0));

            val personIdentities = fusekiJenaLibrary.getStatementsBySP(model, resource, "common:person.person.identification");
            if (personIdentities.hasNext()) {
                val personIds = fusekiJenaLibrary.getStringValueBySP(model, personIdentities.next().getResource(), "common:person.identification.number");
                if (personIds.size() > 0)
                    person.setIdentity(personIds.get(0));
            }

            // set bilus
            val relatedBilus = Lists.newArrayList(fusekiJenaLibrary.getStatementsByPO(model, "gongan:gongan.bilu.entity", resource))
                    .stream().map(s -> s.getSubject().toString()).distinct().collect(Collectors.toList());

            person.setBiluList(relatedBilus);

            // set cases
            val relatedCases = Lists.newArrayList(fusekiJenaLibrary.getStatementsByBatchPO(model, "gongan:gongan.case.bilu", new HashSet<>(relatedBilus)))
                    .stream().map(s -> s.getSubject().toString()).distinct().collect(Collectors.toList());

            person.setCaseList(relatedCases);
        }

        if(person.getPhone() == null || person.getPhone().isEmpty()) {
            val contactIters = fusekiJenaLibrary.getStatementsBySP(model, resource, "common:person.person.contact");
            if (contactIters.hasNext()) {
                val contacts = fusekiJenaLibrary.getStringValueBySP(model, contactIters.next().getResource(), "common:person.contact.number");
                if (contacts.size() > 0)
                    person.setPhone(contacts.get(0));
            }
        }

        if(person.getBirthDay() == null || person.getBirthDay().isEmpty()) {
            val birthdays = fusekiJenaLibrary.getStringValueBySP(model, resource, "common:person.person.birthDate");
            if (birthdays.size() > 0)
                person.setBirthDay(birthdays.get(0));
        }

        if(person.getGender() == null || person.getGender().isEmpty()) {
            val genders = fusekiJenaLibrary.getStringValueBySP(model, resource, "common:person.person.gender");
            if (genders.size() > 0) {
                if (genders.get(0).toLowerCase().equals("female"))
                    person.setGender("女");
                else if (genders.get(0).toLowerCase().equals("male"))
                    person.setGender("男");
            }
        }
        return person;
    }
}
