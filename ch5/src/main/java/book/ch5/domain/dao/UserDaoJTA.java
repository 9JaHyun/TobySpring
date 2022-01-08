package book.ch5.domain.dao;

import book.ch5.domain.User;

import javax.naming.InitialContext;
import java.util.List;

// 하나의 connection에서는 제어가 가능하나... 여러 db를 컨트롤은 불가능하다.
// 이런 경우에는 글로벌 트랜잭션을 사용해야 한다.(JTA, Hibernate)
public class UserDaoJTA implements UserDao{    @Override

    public void add(User user) {
    // JNDI를 이용해 서버의 UserTransaction 오브젝트를 가져옴.
//    InitialContext ctx = new InitialContext();
//    ctx.lookup(USER_TX_JNDI_NAME);

    }

    @Override
    public int count() {
        return 0;
    }

    @Override
    public User get(String id) {
        return null;
    }

    @Override
    public List<User> getAll() {
        return null;
    }

    @Override
    public void update(User user) {

    }

    @Override
    public void deleteAll() {

    }
}
