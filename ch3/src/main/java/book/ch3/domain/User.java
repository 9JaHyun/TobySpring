package book.ch3.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class User {
    private String id;
    private String name;
    private String password;
}
