package ch.rasc.sec.secureserver;

import ch.rasc.sec.secureserver.model.DocGroup;
import ch.rasc.sec.secureserver.model.Document;
import ch.rasc.sec.secureserver.model.Group;
import ch.rasc.sec.secureserver.model.User;
import ch.rasc.sec.secureserver.repository.DocRepository;
import ch.rasc.sec.secureserver.repository.UserRepository;
import ch.rasc.sec.secureserver.repository.DocGroupRepository;
import ch.rasc.sec.secureserver.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.List;

@Component
public class InitDatabase implements ApplicationListener<ContextRefreshedEvent> {




    @Autowired
    private DocRepository docRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private DocGroupRepository docGroupRepository;

    @Autowired
    public InitDatabase( DocRepository docRepository) {

    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        User user = null;
        Group group = null;
        if (this.userRepository.count() == 0) {
            user = new User("userLogin","user name", "user@mail.com");
            group = new Group("group");
            userRepository.save(user);
            groupRepository.save(group);
            Set<User> users = new HashSet<>();
            users.add(user);
            group.setUsers(users);
            System.out.println("USER:" + group.getUsers().iterator().next());
            //user.getGroups().add(group);
//            userRepository.save(user);
//            groupRepository.save(group);
        }
        if (this.docRepository.count() < 10) {
            Document doc = new Document("doc", "doc");
            doc.setOwnerId(user);
            docRepository.save(doc);
            docGroupRepository.save(new DocGroup(DocGroup.AccessLevel.READWRITE,group, doc));
        }
        if(this.groupRepository.count() < 15){
            List<Group> list = new ArrayList<>();
            String[] groups = new String[]{"Administrators", "Soldiers", "Lance corporals", "Junior sergeants",
                    "Sergeants", "Senior sergeants", "Sergeant majors", "Warrant officers", "Junior lieutenants",
                    "Lieutenants", "Senior lieutenants", "Captains", "Majors", "Lieutenant colonels",
                    "Colonels", "Major-generals", "Lieutenant-generals", "Colonel-generals"};
            for (int i = 0; i < groups.length; i++){
                list.add(new Group(groups[i]));
            }
            this.groupRepository.save(list);
        }
    }

}
