package auction.client.service;

import auction.client.model.User;
//Định làm cái này để nhờ nó check passwork , nhưng có lẽ BỎ
public class UserService {
    public boolean checkpasswork(User user, String pw,String name) {
        if (user.getPassword().equals(pw) && user.getName().equals(name)) return true;
        return false;
    }
}
