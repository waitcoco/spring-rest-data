package com.mycompany.knowledge.miami.publish.engine.gongan;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.mycompany.knowledge.miami.publish.engine.PublishEngine;
import com.mycompany.knowledge.miami.publish.jena.FusekiJenaLibrary;
import com.mycompany.knowledge.miami.publish.model.gongan.*;
import com.mycompany.knowledge.miami.publish.repository.*;
import lombok.val;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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
    @Autowired
    private PhoneRelationRepository phoneRelationRepository;
    @Autowired
    private IdentityRelationRepository identityRelationRepository;
    @Autowired
    private MongoCaseBasicRepo mongoCaseBasicRepo;
    @Autowired
    private PersonInfoGetter personInfoGetter;
    @Autowired
    private DataSaver dataSaver;

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



    public void publishCases() {
        caseRepository.deleteAll();
        personRepository.deleteAll();
        biluRepository.deleteAll();
        relationRepository.deleteAll();
        phoneRelationRepository.deleteAll();
        identityRelationRepository.deleteAll();

        val model = fusekiJenaLibrary.getModel(inputModelName);
        if (model == null) {
            throw new RuntimeException("Can not get model " + inputModelName);
        }

        val iterator = fusekiJenaLibrary.getStatementsByEntityType(model, "gongan:gongan.case");
        int i = 0;
        while (iterator.hasNext()) {
            Resource resource = iterator.next().getSubject();

            CaseBase aCaseBase = new CaseBase();
            aCaseBase.setSubjectId(resource.toString());
            List<String> csIds = fusekiJenaLibrary.getStringValueBySP(model, resource, "common:type.object.id");
            if (csIds.size() > 0)
                aCaseBase.setCaseId(csIds.get(0));

            List<String> csNames = fusekiJenaLibrary.getStringValueBySP(model, resource, "common:type.object.name");
            if (csNames.size() > 0)
                aCaseBase.setCaseName(csNames.get(0));

            List<String> csTypes = fusekiJenaLibrary.getStringValueBySP(model, resource, "gongan:gongan.case.category");
            if (csTypes.size() > 0)
                aCaseBase.setCaseType(String.join(",", csTypes));

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
                logger.info(biluBase.getPerson().size() + " persons in bilu " + biluBase.getSubjectId());
            }

            logger.info(aCaseBase.getBilus().size() + " bilus in case " + aCaseBase.getSubjectId());
            val aCase = new Case();
            aCase.setSubjectId(aCaseBase.getSubjectId());
            aCase.setName(aCaseBase.getCaseName());
            aCase.setCaseId(aCaseBase.getCaseId());
            aCase.setType(aCaseBase.getCaseType());
            List<Bilu> biluList = new ArrayList<>();
            List<Relation> relationList = new ArrayList<>();
            List<Person> personList = new ArrayList<>();
            List<PhoneRelation> phoneRelations = new ArrayList<>();
            List<IdentityRelation> identityRelations = new ArrayList<>();

            for (val biluBase : aCaseBase.getBilus()) {
                val bilu = new Bilu();
                Gson gson = new Gson();
                bilu.setContent(biluBase.getContent());
                bilu.setName(biluBase.getName());
                bilu.setBiluId(biluBase.getBiluId());
                bilu.setSubjectId(biluBase.getSubjectId());
                bilu.setACase(aCase);
                bilu.setBankcards(gson.toJson(biluBase.getBankCards()));
                bilu.setPhones(gson.toJson(biluBase.getPhones()));
                biluList.add(bilu);

                for (val personBase : biluBase.getPerson()) {
                    val relation = new Relation();

                    relation.setSubjectId(UUID.nameUUIDFromBytes((biluBase.getSubjectId() + personBase.getSubjectId()).getBytes()).toString());
                    relation.setPersonSubjectId(personBase.getSubjectId());
                    relation.setBiluSubjectId(biluBase.getSubjectId());
                    relation.setCaseSubjectId(aCase.getSubjectId());
                    if(personBase.getIdentity()!=null) {
                        val identityRelation = new IdentityRelation();
                        identityRelation.setSubjectId(UUID.nameUUIDFromBytes((aCase.getSubjectId() + bilu.getSubjectId() + personBase.getSubjectId()).getBytes()).toString());
                        identityRelation.setIdentity(personBase.getIdentity());
                        identityRelation.setBiluSubjectId(biluBase.getSubjectId());
                        identityRelation.setCaseSubjectId(aCase.getSubjectId());
                        identityRelations.add(identityRelation);
                    }
                    if(biluBase.getConnections().containsKey(personBase.getSubjectId()))
                        relation.setRole(biluBase.getConnections().get(personBase.getSubjectId()));
                    relationList.add(relation);

                    val person = new Person();
                    person.setName(personBase.getName());
                    person.setPhone(personBase.getPhone());
                    person.setSubjectId(personBase.getSubjectId());
                    person.setBirthDay(personBase.getBirthDay());
                    person.setGender(personBase.getGender());
                    person.setIdentity(personBase.getIdentity());
                    if(person.getIdentity()!= null){
                        try{
                            enrichPerson(person);
                        }
                        catch (Exception e){
                            //logger.error("person: person cannot be enriched!" + person.getSubjectId() + " " + e.getMessage());
                        }
                    }
                    personList.add(person);
                }
                for(val phone : biluBase.getPhones().keySet()){
                    val phoneRelation = new PhoneRelation();
                    phoneRelation.setSubjectId(UUID.nameUUIDFromBytes((bilu.getSubjectId()+phone).getBytes()).toString());
                    phoneRelation.setPhoneNumber(biluBase.getPhones().get(phone));
                    phoneRelation.setCaseSubjectId(aCase.getSubjectId());
                    phoneRelation.setBiluSubjectId(biluBase.getSubjectId());
                    phoneRelations.add(phoneRelation);
                }
            }
            try {
                try{
                    if(relationList.size()!=0){
                        dataSaver.saveRelation(relationList);
                    }
                }
                catch(Exception e){
                    logger.error("save relationlist error!"+ aCase.getSubjectId() + " " + e.getMessage());
                }
                //relationRepository.save(relationList);
                //logger.info(relationList.size() + " relations in case " + aCaseBase.getSubjectId());

                try{
                    if(personList.size()!=0){
                        dataSaver.savePerson(personList);
                    }
                }
                catch(Exception e){
                    logger.error("save personlist error!"+ aCase.getSubjectId() + " " + e.getMessage());
                }
                //personRepository.save(personList);
                //logger.info(personList.size() + " persons in case " + aCaseBase.getSubjectId());

                //phoneRelationRepository.save(phoneRelations);
                try{
                    if(phoneRelations.size()!=0){
                        dataSaver.savePhoneRelation(phoneRelations);
                    }
                }
                catch(Exception e){
                    logger.error("save phonerelation error!"+ aCase.getSubjectId() + " " + e.getMessage());
                }
                //logger.info(phoneRelations.size() + "phoneRelation in case" + aCaseBase.getSubjectId());

                //identityRelationRepository.save(identityRelations);
                try{
                    if(identityRelations.size()!=0){
                        dataSaver.saveIdentityRelation(identityRelations);
                    }
                }
                catch(Exception e){
                    logger.error("save identityrelation error!"+ aCase.getSubjectId() + " " + e.getMessage());
                }
                //logger.info(identityRelations.size() + "identityRelation in case" + aCaseBase.getSubjectId());

                aCase.setBilus(biluList);
                try{
                    enrichCaseFromMongo(aCase);
                }
                catch (Exception e){
                    logger.error("case: case cannot be enriched!" + aCase.getSubjectId() + " " + e.getMessage());
                }
                caseRepository.save(aCase);
                i++;
                //System.out.println("时间:" +(endTime-startTime)+"ms");
            }
            catch(Exception e)
            {
                logger.error("case: data save error!" + aCase.getSubjectId() + " " + e.getMessage());
            }
        }
        System.out.println("案件数量："+i);
    }

    private void enrichCaseFromMongo(Case acase) throws Exception{
        // todo
        // 从mongo 里面拿case basic info, 然后塞到Case里面
        Case mongoCase = new Case();
        mongoCase = mongoCaseBasicRepo.getCaseByAJBH(acase.getCaseId());
        acase.setAJBH(mongoCase.getAJBH());
        acase.setAJLX(mongoCase.getAJLX());
        acase.setAJLXName(mongoCase.getAJLXName());
        acase.setAJMC(mongoCase.getAJMC());
        acase.setAJZT(mongoCase.getAJZT());
        acase.setAJZTName(mongoCase.getAJZTName());
        acase.setCBDW_BH(mongoCase.getCBDW_BH());
        acase.setCBDW_MC(mongoCase.getCBDW_MC());
        acase.setFADD(mongoCase.getFADD());
        acase.setJQBH(mongoCase.getJQBH());
        acase.setJYAQ(mongoCase.getJYAQ());
        acase.setLASJ(mongoCase.getLASJ());
        acase.setLRSJ(mongoCase.getLRSJ());
        acase.setSLDW_MC(mongoCase.getSLDW_MC());
        acase.setSLSJ(mongoCase.getSLSJ());
        acase.setXYR_XM(mongoCase.getXYR_XM());
        acase.setZBR_SFZH(mongoCase.getZBR_SFZH());
        acase.setZBR_XM(mongoCase.getZBR_XM());
    }

    private void enrichPerson(Person person) throws Exception{
        Person basicPersonInfo = new Person();
        basicPersonInfo = personInfoGetter.getPersonByIdentity(person.getIdentity());
        person.setAddress(basicPersonInfo.getAddress());
        person.setAge(basicPersonInfo.getAge());
        person.setBloodType(basicPersonInfo.getBloodType());
        person.setEthnicGroup(basicPersonInfo.getEthnicGroup());
        person.setFormerName(basicPersonInfo.getFormerName());
        person.setHeight(basicPersonInfo.getHeight());
        person.setMaritalStatus(basicPersonInfo.getMaritalStatus());
        person.setNativePlace(basicPersonInfo.getNativePlace());
        person.setOccupation(basicPersonInfo.getOccupation());
    }

    private BiluBase getBiluInfo(Model model, Resource resource) {
        BiluBase biluBase = new BiluBase();

        biluBase.setSubjectId(resource.toString());

        //bilu subjectId
        List<String> bilu_id_list = fusekiJenaLibrary.getStringValueBySP(model, resource, "common:type.object.subjectId");
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
            PersonBase person = getPersonInfo(model, model.getResource(personSubject));
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

    private PersonBase getPersonInfo(Model model, Resource resource) {
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

            val personIdentities = fusekiJenaLibrary.getStatementsBySP(model, resource, "common:person.person.identification");
            if (personIdentities.hasNext()) {
                val personIds = fusekiJenaLibrary.getStringValueBySP(model, personIdentities.next().getResource(), "common:person.identification.number");
                if (personIds.size() > 0)
                    personBase.setIdentity(personIds.get(0));
            }

//            // set bilus
//            val relatedBilus = Lists.newArrayList(fusekiJenaLibrary.getStatementsByPO(model, "gongan:gongan.bilu.entity", resource))
//                    .stream().map(s -> s.getSubject().toString()).distinct().collect(Collectors.toList());
//
//            personBase.setBiluList(relatedBilus);
//
//            // set cases
//            val relatedCases = Lists.newArrayList(fusekiJenaLibrary.getStatementsByBatchPO(model, "gongan:gongan.case.bilu", new HashSet<>(relatedBilus)))
//                    .stream().map(s -> s.getSubject().toString()).distinct().collect(Collectors.toList());
//
//            personBase.setCaseList(relatedCases);
        }

        if(personBase.getPhone() == null || personBase.getPhone().isEmpty()) {
            val contactIters = fusekiJenaLibrary.getStatementsBySP(model, resource, "common:person.person.contact");
            if (contactIters.hasNext()) {
                val contacts = fusekiJenaLibrary.getStringValueBySP(model, contactIters.next().getResource(), "common:person.contact.number");
                if (contacts.size() > 0)
                    personBase.setPhone(contacts.get(0));
            }
        }

        if(personBase.getBirthDay() == null || personBase.getBirthDay().isEmpty()) {
            val birthdays = fusekiJenaLibrary.getStringValueBySP(model, resource, "common:person.person.birthDate");
            if (birthdays.size() > 0)
                personBase.setBirthDay(birthdays.get(0));
        }

        if(personBase.getGender() == null || personBase.getGender().isEmpty()) {
            val genders = fusekiJenaLibrary.getStringValueBySP(model, resource, "common:person.person.gender");
            if (genders.size() > 0) {
                if (genders.get(0).toLowerCase().equals("female"))
                    personBase.setGender("女");
                else if (genders.get(0).toLowerCase().equals("male"))
                    personBase.setGender("男");
            }
        }

        return personBase;
    }
}
