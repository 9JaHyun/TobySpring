package book.ch6.domain;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {
    private String id;
    private String name;
    private String password;
    private Level level;
    private int login;
    private int recommend;


    public void upgradeLevel() {
        Level nextLevel = this.level.nextLevel();
        if (nextLevel == null) {
            throw new IllegalStateException(this.level + "은 업그레이드가 불가능합니다");
        } else {
            this.level = nextLevel;
        }
    }
}
