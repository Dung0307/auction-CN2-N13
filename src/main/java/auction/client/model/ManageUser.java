package auction.client.model;

import java.util.ArrayList;
import java.util.List;

public class ManageUser {
    static List<User> userList = new ArrayList<>();
    public List<User> getUserList()  {
        return userList;
    }
}
