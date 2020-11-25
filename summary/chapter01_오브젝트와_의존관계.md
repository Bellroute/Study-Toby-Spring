# 1장 오브젝트와 의존 관계

- 스프링이 자바에서 가장 중요하게 가치를 여기는 것은 **객체지향 프로그래밍이 가능한 언어**라는 점
- 스프링의 핵심 철학 : 엔터프라이즈 기술의 혼란 속에서 잃어버린 객체지향 기술의 진정한 가치를 회복시키고, 그로부터 객체지향 프로그래밍이 제공하는 폭넓은 혜택을 누릴 수 있도록 기본으로 돌아가자
- 스프링은 객체지향 설계와 구현에 관해 특정한 모델과 기법을 억지로 강요하지는 않는다. 하지만 오브젝트를 어떻게 효과적으로 설계하고 구현하고, 사용하고, 이를 개선해나갈 것인가에 대한 명쾌한 기준을 마련 + 객체지향 기술과 설계, 구현에 관한 실용적 전략과 검증된 베스트 프랙티스를 제공

→ 1장에서는 스프링이 관심을 갖는 대상인 오브젝트의 설계와 구현, 동작원리에 대해 집중

</br>

## 1.1 초난감 DAO

> DAO(Data Access Object)란? 
>
> DB를 사용해 데이터를 조회하거나 조작하는 기능을 전담하도록 만든 오브젝트

사용자 정보를 JDBC API를 통해 DB에 저장하고 조회하는 DAO를 만들어보자

</br>

### 1.1.1 User

> 사용자 정보를 저장할 User 클래스를 만든다.

→ 사용자 정보를 저장할 때는 **자바빈** 규약을 따르는 오브젝트를 이용하면 편리함.

- **자바빈(JavaBean)** : 원래비주얼 툴에서 조작 가능한 컴포넌트를 말함. 자바 주력 개발 플랫폼이 웹 기반의 엔터프라이즈 방식으로 바뀌면서 비주얼 컴포넌트로서 자바빈은 인기를 잃어 갔지만, 자바빈의 몇 가지 코딩 관례는 계속 이어져 옴. 이제는 다음 두 가지 관례를 따라 만들어진 오브젝트를 가리킨다.

> - **디폴트 생성자** : 파라미터가 없는 디폴트 생성자를 가지고 있음. 툴이나 프레임워크에서 리플렉션을 이용해 오브젝트를 생성하기 때문에 필요
> - **프로퍼티** : 자바빈이 노출하는 이름을 가진 속성을 프로퍼티라 함. 프로퍼티는 setter와 getter를 이용해 수정 및 조회 가능.

```java
package springbook.user.domain;

public class User {
    String id;
    String name;
    String password;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
```

</br>

### 1.1.2 UserDao

>  사용자 정보를 DB에 넣고 관리할 수 있는 UserDao 클래스를 만든다.

사용자 정보의 등록, 수정, 삭제와 각종 조회 기능을 만들어야 하겠지만, 일단 두 개의 메소드를 먼저 만들어보자

- 새로운 사용자를 생성(add)
- 아이디를 가지고 사용자 정보를 조회(get)

```java
package springbook.user.dao;

import springbook.user.domain.User;

import java.sql.*;

public class UserDao {
    
    public void add(User user) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection c = DriverManager.getConnection(
                "jdbc:mysql://localhost/springbook", "spring", "book");

        PreparedStatement ps = c.prepareStatement(
                "insert into users(id, name, password) values(?, ?, ?)");
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());
        
        ps.executeUpdate();
        
        ps.close();
        c.close();
    }
    
    public User get(String id) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection c = DriverManager.getConnection(
                "jdbc:mysql://localhost/springbook", "spring", "book");

        PreparedStatement ps = c.prepareStatement(
                "select * from users where id = ?");
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();
        rs.next();
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));

        rs.close();
        ps.close();
        c.close();
        
        return user;
    }
}
```

이 클래스가 제대로 동작하는지 어떻게 확인할까?

→ 웹 애플리케이션을 만들어 서버에 배치하고, 웹 브라우저를 통해 기능을 사용하기에는 배보다 배꼽이 더 큰 상황

<br>

### 1.1.3 main()을 이용한 DAO 테스트 코드

>  main 메소드를 만들고 그 안에서 UserDao의 오브젝트를 생성해서 add()와 get() 메소드를 검증해보자.

```java
package springbook.user;

import springbook.user.dao.UserDao;
import springbook.user.domain.User;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        UserDao dao = new UserDao();

        User user = new User();
        user.setId("bellroot");
        user.setName("벨루트");
        user.setPassword("1234");

        dao.add(user);

        System.out.println(user.getId() + " 등록 성공");

        User user2 = dao.get(user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());

        System.out.println(user2.getId() + " 조회 성공");
    }
}
```

지금 만든 UserDao 클래스 코드에는 사실 여러 가지 문제가 있다. 문제점이라고 하면 뭔가 기능이 정상적으로 동작하지 않아야 할 텐데, 위의 DAO는 우리는 우리가 기대했던 기능을 충실하게 동작한다. 그렇다면, 왜 이 코드에 문제가 많다고 하는 것이라까? 잘 동작하는 코드를 굳이 수정하고 개선해야 하는 이유는 뭘까? 그렇게 DAO 코드를 개선했을 때의 장점은 무엇일까?….

**스프링을 공부한다는 건 바로 이런 문제 제기와 의문에 대한 답을 찾아나가는 과정이다.**

스프링은 기계적인 답변이나 성급한 결론을 주지 않는다. 최종 결론은 스프링을 이용해 개발자 스스로 만들어내는 것이지, 스프링이 덥석 줄 수 있는게 아니기 때문.

스프링은 단지  그 과정에서 이런 고민을 제대로 하고 있는지 끊임없이 확인해주고, 좋은 결론을 내릴 수 있도록 객체지향 기술과 자바 개발의 선구자들이 먼저 고민하고 제안한 방법에 대한 힌트를 제공해줄 뿐이다.

</br>

## 1.2 DAO 분리

### 1.2.1 관심사의 분리

소프트웨어 개발에서 끝이란 개념은 없다. 사용자의 비즈니스 프로세스와 그에 따른 요구사항은 끊임없이 바뀌고 발전한다.

-> 그래서 개발자가 객체를 설계할 때 가장 염두에 둬야 할 사항은 바로 **미래의 변화를 어떻게 대비할 것**인가이다.

지혜로운 개발자는 오늘 이 시간에 미래를 위해 설계하고 개발한다. 그리고 그 덕분에 미래에 닥칠지도 모르는 거대한 작업에 대한 부담과 변경에 따른 엄청난 스트레스, 그로 인해 발생하는 고객과의 사이에서 또 개발팀 내에서의 갈등을 최소화할 수 있다.

객체 지향 기술은 흔히 실세계를 최대한 가깝게 모델링해낼 수 있기 때문에 의미가 있다고 여겨진다. 하지만 그보다는 객체지향 기술이 만들어내는 가상의 추상세계 자체를 효과적으로 구성할 수 있고, 이를 자유롭고 편리하게 변경, 발전, 확장시킬 수 있다는 데 더 의미가 있다.

미래를 준비하는 데 있어 가장 중요한 과제는 변화에 어떻게 대비할 것인가이다.

-> 가장 좋은 대책은 변화의 폭을 최소한으로 줄여주는 것이다. (기능을 변경하는 데 5분이 걸리는 개발자 >>>>>>> 5시간이 걸리는 개발자)

어떻게 변경이 일어날 때 필요한 작업을 최소화하고, 그 변경이 다른 곳에 문제를 일으키지 않게 할 수 있었을까?

-> **분리와 확장을 고려한 설계**가 있었기 때문

먼저 분리에 대해 생각해보자. 모든 변경과 발전은 한 번에 한 가지 관심사항에 집중해서 일어난다. 문제는, **변화는 대체로 집중된 한 가지 관심에 대해 일어나지만, 그에 따른 작업은 한 곳에 집중되지 않는 경우가 많다**는 점이다.

-> 단지 DB 접속용 암호를 변경하려고 DAO클래스 수백 개를 모두 수정해야 한다면?

-> 트랜잭션 기술을 다른 것으로 바꿨다고 비즈니스 로직이 담긴 코드의 구조를 모두 변경해야 한다면?

-> 다른 개발자가 개발한 코드에 변경이 일어날 때마다 내가 만든 클래스도 함께 수정을 해줘야 한다면?

변화가 한 번에 한 가지 관심에 집중돼서 일어난다면, 우리가 준비해야 할 일은 한 가지 관심이 한 군데에 집중되게 하는 것이다. 즉 관심이 같은 것끼리는 모으고, 관심이 다른 것은 따로 떨어져 있게 하는 것이다. -> **관심사의 분리(Separation of Concerns)**

</br>

### 1.2.2 커넥션 만들기의 추출

#### UserDao의 관심사항

UserDao의 구현된 메소드를 들여다보면 add() 메소드 하나에서만 적어도 세 가지 관심사항을 발견할 수 있다.

1. DB와 연결을 위한 커넥션을 가져오는 것
2. 사용자 등록을 위해 DB에 보낼 SQL 문장을 담을 Statement를 만들고 실행하는 것
3. 작업이 끝나면 사용자 리소스인 Statement와 Connection 오브젝트를 닫아줘서 소중한 공유 리소스를 시스템에 돌려주는 것

첫째 관심사인 DB 연결을 위한 Connection 오브젝트를 가져오는 부분

- 현재 DB 커넥션을 가져오는 코드는 다른 관심사와 섞여서 같은 add() 메소드에 담겨 있다.
- 더 큰 문제는 add() 메소드에 있는 DB 커넥션을 가져오는 코드와 동일한 코드가 get() 메소드에도 중복되어 있음

-> 하나의 관심사가 중복되어 있고, 여기저기 흩어져 있어서 다른 관심의 대상과 얽혀 있으면, 변경이 일어날 때 엄청난 고통을 일으키는 원인이 됨

</br>

#### 중복 코드의 메소드 추출

중복된 DB 연결 코드를 getConnection()이라는 이름의 독립적인 메소드로 만들어준다. 각 DAO 메소드에서는 분리한 getConnection() 메소드를 호출해서 DB 커넥션을 가져오게 만든다.

```java
package springbook.user.dao;

import springbook.user.domain.User;

import java.sql.*;

public class UserDao {

    public void add(User user) throws ClassNotFoundException, SQLException {
        Connection c = getConnection();

        PreparedStatement ps = c.prepareStatement(
                "insert into users(id, name, password) values(?, ?, ?)");
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        ps.executeUpdate();

        ps.close();
        c.close();
    }

    public User get(String id) throws ClassNotFoundException, SQLException {
        Connection c = getConnection();
        
        PreparedStatement ps = c.prepareStatement(
                "select * from users where id = ?");
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();
        rs.next();
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));

        rs.close();
        ps.close();
        c.close();

        return user;
    }
    
    private Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection c = DriverManager.getConnection(
                "jdbc:mysql://localhost/toby", "root", "1111");
        
        return c;
    }
}

```

DB 연결과 관련된 부분에 변경이 일어났을 경우 (예를 들어 DB 종류와 접속 방법이 바뀌어서 드라이버 클래스와 URL이 바뀌었다거나, 로그인 정보가 변경) 앞으로는 getConnection()이라는 한 메소드의 코드만 수정하면 된다.

**관심의 종류에 따라 코드를 구분해놓았기 때문에 한 가지 관심에 대한 변경이 일어날 경우 그 관심이 집중되는 부분의 코드만 수정하면 됨. 관심이 다른 코드가 있는 메소드에는 영향을 주지도 않을뿐더러, 관심 내용이 독립적으로 존재하므로 수정도 간단해짐.**

</br>

#### 변경사항에 대한 검증: 리팩토링과 테스트

이미 UserDao의 기능이 잘 동작한다는 것을 테스트해봤지만, 코드를 수정한 후에는 기능에 문제가 없다는 게 보장되지 않음. 다시 검증이 필요함.

>  현재 main() 메소드 테스트에는 한 가지 단점이 있는데, main() 메소드를 여러 번 실행하면 두 번째부터는 무조건 예외가 발생한다는 점이다. -> 테이블의 기본키인 id 값이 중복되기 때문. 테스트를 다시 실행하기 전에 User 테이블의 사용자 정보를 모두 삭제해줘야 함.

방금 한 작업은 UserDao의 기능에는 아무런 변화를 주지 않았다. 

- 여러 메소드에 중복돼서 등장하는 특정 관심사항이 담긴 코드를 별도의 메소드로 분리해낸 것. 
- 이 작업은 기능에는 영향을 주지 않으면서 코드의 구조만 변경한다. 
- 기능이 추가되거나 바뀐 것은 없지만 이전보다 훨씬 깔끔해졌고 미래의 변화에 좀 더 쉽게 대응할 수 있는 코드가 됐다.
-  이러한 작업을 **리팩토링(Refactoring)**이라 한다.
-  또한 getConnection()이라고 하는 공통의 기능을 담당하는 메소드로 중복된 코드를 뽑아내는 것을 리팩토링에서는 **메소드 추출(extract method) 기법**이라고 부른다.

</br>

### 1.2.3 DB 커넥션 만들기의 독립

이번엔 좀 더 나아가서 변화에 대응하는 수준이 아니라, 아예 변화를 반기는 DAO를 만들어보자.

*UserDao를 납품한다고 가정할 때, 고객마다 다른 종류의 DB를 사용하고 있고, DB 커넥션을 가져오는 데 있어 독자적으로 만든 방법을 적용하고 싶어한다면? (고객에게 소스를 직접 공개하고 싶지는 않다.)*

#### 상속을 통한 확장

기존 UserDao 코드를 한 단계 더 분리.  UserDao에서 메소드의 구현 코드를 제거하고 getConnection()을 추상 메소드로 만든다.

UserDao를 구입한 포탈사 N 사, D 사는 UserDao 클래스를 상속해서 각각 NUserDao와 DUserDao라는 서브클래스를 만든다. 서브클래스에서는 추상 메소드로 선언했던 getConnection() 메소드를 원하는 방식대로 구현할 수 있다. 이렇게 하면 소스코드를 제공해서 수정해서 쓰도록 하지 않아도 getConnection() 메소드를 원하는 방식으로 확장한 후에 UserDao의 기능과 함께 사용할 수 잇다.

-> 기존에는 같은 클래스에 다른 메소드로 분리됐던 DB 커넥션 연결이라는 관심을 이번에는 상속을 통해 서브클래스로 분리해버리는 것.

```java
package springbook.user.dao;

import springbook.user.domain.User;

import java.sql.*;

public abstract class UserDao {

    public void add(User user) throws ClassNotFoundException, SQLException {
        Connection c = getConnection();

        PreparedStatement ps = c.prepareStatement(
                "insert into users(id, name, password) values(?, ?, ?)");
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        ps.executeUpdate();

        ps.close();
        c.close();
    }

    public User get(String id) throws ClassNotFoundException, SQLException {
        Connection c = getConnection();

        PreparedStatement ps = c.prepareStatement(
                "select * from users where id = ?");
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();
        rs.next();
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));

        rs.close();
        ps.close();
        c.close();

        return user;
    }

    public abstract Connection getConnection() throws ClassNotFoundException, SQLException;
}




public class DUserDao extends UserDao {
    @Override
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        // D 사 DB connection 생성코드
    }
}

public class NUserDao extends UserDao {
    @Override
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        // N 사 DB connection 생성코드
    }
}
```

관심사가 클래스 레벨로 구분 됨.

- UserDao -> DAO의 핵심 기능인 어떻게 데이터를 등록하고 가져올 것인가(SQL 작성, 파라미터 바인딩, 쿼리 실행, 검색정보 전달)라는 관심을 담당
- NUserDao, DUserDao -> DB 연결 방법은 어떻게 할 것인가라는 관심을 담당

**클래스 계층구조를 통해 두 개의 관심이 독립적으로 분리되면서 변경 작업은 한층 용이해짐.** 새로운 DB 연결 방법을 적용해야할 때는 UserDao를 상속을 통해 확장해주기만 하면 된다.

</br>

이렇게 <u>슈퍼클래스에 기본적인 로직의 흐름(커넥션 가져오기, SQL 생성, 실행, 반환)을 만들고, 그 기능의 일부를 추상 메소드나 오버라이딩이 가능한 protected 메소드 등으로 만든 뒤 서브클래스에서 이런 메소드를 필요에 맞게 구현해서 사용하도록 하는 방법</u>을 **템플릿 메소드 패턴(template method pattern)**이라고 함.

UserDao의 getConnection() 메소드는 Connection 타입 오브젝트를 생성하는 기능을 정의해놓은 추상 메소드이며 UserDao의 서브클래스의 getConnection() 메소드는 어떤 Connection 클래스의 오브젝트를 어떻게 생성할 것인지 결정하는 방법이라고도 볼 수 있따. 이렇게 <u>서브 클래스에서 구체적인 오브젝트 생성 방법을 결정하게 하는 것</u>을 **팩토리 메소드 패턴(factory method pattern)**이라고 부르기도 한다.

> 디자인 패턴(design pattern)
>
> - 디자인 패턴은 소프트웨어 설계 시 특정 상황에서 자주 만나는 문제를 해결하기 위해 사용할 수 잇는 재사용 가능한 솔류션을 말함. 
> - 간단히 패턴 이름을 언급하는 것만으로도 설계의 의도와 해결책을 함께 설명할 수 있다는 장점이 있음.
> - 주로 객체지향 설계에 관한 것, 대부분 객체지향적 설계 원칙을 이용해 문제를 해결함

이렇게 템플릿 메소드 패턴 또는 팩토리 메소드 패턴으로 관심사항이 다른 코드를 분리해내고, 서로 독립적으로 변경 또는 확장할 수 있도록 만드는 것은 간단하면서도 매우 효과적인 방법

</br>

하지만 **이 방법은 상속을 사용했다는 단점**이 있다. 상속 자체는 간단해 보이고 사용하기도 편리하지만, 

- 자바는 클래스의 다중상속을 허용하지 않는다.

- 상속을 통한 상하위 클래스의 관계는 생각보다 밀접(긴밀한 결합을 허용)하다.
  - 서브클래스는 슈퍼클래스의 기능을 직접 사용할 수 잇음
  - 슈퍼클래스 내부의 변경이 있을 때 모든 서브클래스를 함께 수정하거나 다시 개발해야할 수도 있음
  - 반대로 그런 변화에 따른 불편을 주지 않기 위해 슈퍼클래스가 더 이상 변화하지 않도록 제약을 가해야 할지도 모름

- 확장된 기능인 DB 커넥션을 생성하는 코드를 다른 DAO 클래스에 적용할 수 없다.
  - 만약 UserDao 외의 DAO 클래스들이 계속 만들어진다면 그 때는 상속을 통해서 만들어진 getConnection()의 구현 코드가 매 DAO 클래스마다 중복돼서 나타나는 심각한 문제 발생