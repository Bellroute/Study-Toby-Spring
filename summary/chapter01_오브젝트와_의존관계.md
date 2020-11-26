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

</br>

## 1.3 DAO의 확장

모든 오브젝트는 변한다. 그런데 다 동일한 방식으로 변하는 것은 아니다. 관심사에 따라서 분리한 오브젝트들은 제각기 독특한 변화의 특징이 있다. 변화의 성격이 다르다는 건 변화의 이유와 시기, 주기 등이 다르다는 뜻

추상 클래스를 만들고 이를 상속한 서브클래스에서 변화가 필요한 부분을 바꿔서 쓸 수 있게 만든 이유는 바로 이렇게 변화의 성격이 다른 것을 분리해서, 서로 영향을 주지 않은 채로 각각 필요한 시점에 독립적으로 변경할 수 있게 하기 위해서임. 그러나 여러 가지 단점이 많은, 상속이라는 방법을 사용했다는 사실이 불편하게 느껴짐.

</br>

### 1.3.1 클래스의 분리

두 개의 관심사를 본격적으로 독립시키면서 동시에 손쉽게 확장할 수 있는 방법을 알아보자.

지금까지는 성격이 다른, 그래서 다르게 변할 수 있는 관심사를 분리하는 작업을 점진적으로 진행했음. (독립된 메소드로 분리 -> 상하위 클래스로 분리)

이번에는 아예 상속관계도 아닌 완전히 독립된 클래스로 만들어 보겠다.

-> DB 커넥션과 관련된 부분을 서브클래스가 아니라, 별도의 클래스에 담는다. 그리고 이 클래스를 UserDao가 이용하게 한다.

**[UserDao.java]**

```java
package springbook.user.dao;

import springbook.user.domain.User;

import java.sql.*;

public abstract class UserDao {
  
    private SimpleConnectionMaker simpleConnectionMaker;

    public UserDao() {
        simpleConnectionMaker = new SimpleConnectionMaker(); // SimpleConnectionMaker 클래스의 오브젝트를 만들어 둔다. 각 메소드에서 매번 만들 수도 있지만, 한 번만 만들어서 저장해두고 이를 사용하는 편이 낫다.
    }

    public void add(User user) throws ClassNotFoundException, SQLException {
        Connection c = simpleConnectionMaker.makeNewConnection(); // 만들어둔 오브젝트를 사용
      	...
    }

    public User get(String id) throws ClassNotFoundException, SQLException {
        Connection c = simpleConnectionMaker.makeNewConnection(); // 만들어둔 오브젝트를 사용
      	...
    }
}

```

**[SimpelConnectionMaker.java]**

```java
package springbook.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SimpleConnectionMaker {
    public Connection makeNewConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection c = DriverManager.getConnection(
                "jdbc:mysql://localhost/toby", "root", "1111");
        return c;
    }
}

```

분리는 잘 했지만, N 사와 D 사에 UserDao 클래스만 공급하고 상속을 통해 DB 커넥션 기능을 확장해서 사용하게 했던 게 다시 불가능해졌다. UserDao의 코드가 SimpleConnectionMaker라는 특정 클래스에 종속되어 있기 때문에 상속을 사용했을 때처럼 UserDao 코드의 수정 없이 DB 커넥션 생성 기능을 변경할 방법이 없음.

클래스를 분리한 경우에도 상속을 이용했을 때와 마찬가지로 자유로운 확장이 가능하게 하려면 두 가지 문제를 해결해야 한다.

1. SimpelConnectionMaker의 메소드 문제

   - 현재 makeNewConnection()을 사용해 DB 커넥션을 가져옴

   - 그런데 D 사가 만든 커넥션 제공 클래스는 openConnection()이라는 이름을 사용한다면 add(), get()에서 사용된 코드를 일일이 변경해야함.

2. DB 커넥션을 제공하는 클래스가 어떤 것인지를 UserDao가 구체적으로 알고 있어야 한다.

   - UserDao에 SimpleConnectionMaker라는 클래스 타입의 인스턴스 변수까지 정의해놓고 있음
   - N 사에서 다른 클래스를 구현하게 된다면 UserDao 자체를 다시 수정해야한다.

-> 근본적인 원인은 UserDao가 바뀔 수 있는 정보(DB 커넥션을 가져오는 클래스)에 대해 너무 많이 알고 있기 때문 (어떤 클래스가 쓰일지, 그 클래스에서 사용하는 메소드 이름이 뭔지까지 알고 있어야 함)

-> UserDao는 DB 커넥션을 가져오는 구체적인 방법에 종속되어 버렸음. 고객이 DB 커넥션을 가져오는 방법을 자유롭게 확장하기가 힘들어짐

</br>

### 1.3.2 인터페이스의 도입

상속의 단점을 해결하고자 클래스를 분리했더니 코드가 종속적이게 되어 확장이 어려워짐. 어떻게 해결해야 할까?

-> 가장 좋은 방법은 두 개의 클래스가 서로 긴밀하게 연결되어 있지 않도록 중간에 추상적인 느슨한 연결 고리를 만들어주는 것

> 추상화?
>
> 어떤 것들의 공통적인 성격을 뽑아내어 이를 따로 분리해내는 작업

자바가 추상화를 위해 제고아는 가장 유용한 도구는 **인터페이스**!

- 인터페이스는 자신을 구현한 클래스에 대한 구체적인 정보는 모두 감춰버림.
- 인터페이스로 추상화해놓은 최소한의 통로를 통해 접근하는 쪽에서 오브젝트를 만들 때 사용할 클래스가 무엇인지 몰라도 됨.
- 실제 구현 클래스를 바꿔도 신경 쓸 일이 없음.
- 인터페이스는 어떤 일을 하겠다는 기능만 정의한 것임. 따라서 어떻게 하겠다는 구현 방법은 구현할 클래스들이 알아서 할 일



ConnectionMaker 인터페이스를 정의하고 DB 커넥션을 가져오는 메소드 이름은 makeConnection()이라고 정하자. 이 인터페이스를 사용하는 UserDao 입장에서는 ConnectionMaker 인터페이스 타입의 오브젝트라면 어떤 클래스로 만들어졌는지 상관없이 makerConnection() 메소드를 호출하기만 하면 Connection 타입의 오브젝트를 만들어서 돌려줄 것이라고 기대할 수 있음.

**[ConnectionMaker.java]**

```java
package springbook.user.dao;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionMaker {
    public Connection makeConnection() throws ClassNotFoundException, SQLException;
}
```

그리고 D 사의 개발사라면 ConnectionMaker 인터페이스를 구현할 클래스를 만들고, 자신의 DB 연결 기술을 이용해 DB 커넥션을 가져오도록 메소드를 작성해주면 된다.

**[DConnectionMaker.java]**

```java
package springbook.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DConnectionMaker implements ConnectionMaker{

    @Override
    public Connection makeConnection() throws ClassNotFoundException, SQLException {
      // D 사의 독자적인 방법으로 Connection을 생성하는 코드
        Class.forName("com.mysql.jdbc.Driver");
        Connection c = DriverManager.getConnection(
                "jdbc:mysql://localhost/toby", "root", "1111");
        return c;
    }
}

```

UserDao에서도 특정 클래스 대신 인터페이스를 사용해서 DB 커넥션을 가져와 사용하도록 수정한다.

**[UserDao.java]**

```java
package springbook.user.dao;

import springbook.user.domain.User;

import java.sql.*;

public abstract class UserDao {
    private ConnectionMaker connectionMaker; // 인터페이스를 통해 오브젝트에 접근. 구체적인 클래스 알 필요 없음.

    public UserDao() {
        connectionMaker = new DConnectionMaker(); // *여기서 구체적인 클래스로 오브젝트 생성
    }

    public void add(User user) throws ClassNotFoundException, SQLException {
        Connection c = connectionMaker.makeConnection(); // 인터페이스에 정의된 메소드를 사용
      	...
    }

    public User get(String id) throws ClassNotFoundException, SQLException {
        Connection c = connectionMaker.makeConnection();
      	...
    }
}
```

\* UserDao 코드를 자세히 살펴보면 DConnection이라는 클래스 이름이 보인다. Connection 클래스의 생성자를 호출해서 오브젝트를 생성하는 코드가 여전히 남아있다는 문제점이 여전히 존재한다.

</br>

### 1.3.3 관계설정 책임의 분리

인터페이스를 이용한 분리에도 불구하고 여전히 UserDao 변경 없이는 DB 커넥션 기능의 확장이 자유롭지 못함.

-> UserDao 안에 분리되지 않은, 또 다른 관심 사항이 존재하고 있기 때문

```java
connectionMaker = new DConnectionMaker();
```

이 코드는 짧지만 그 자체로 독립적인 관심사를 담고 있다. 앞서 분리한 두 개의 관심사(UserDao의 관심사, ConnectionMaker의 관심사)와는 다름 

-> UserDao가 어떤 ConnectionMaker 구현 클래스의 오브젝트를 이용하게 할지를 결정하는 것. UserDao와 UserDao가 사용할 ConnectionMaker의 특정 구현 클래스 사이의 관계를 설정해주는 것에 관한 관심.

두 개의 오브젝트가 있고 한 오브젝트가 다른 오브젝트의 기능을 사용한다면, 사용되는 쪽이 사용하는 쪽에게 서비스를 제공하는 셈. 따라서 사용되는 오브젝트를 **서비스** , 사용하는 오브젝트를 **클라이언트** 라 부를 수 있다.

-> UserDao와 ConnectionMaker 구현 클래스의 관계를 결정해주는 기능을 분리해서 두기에 적절한 곳이 바로 **클라이언트 오브젝트**

오브젝트 사이의 관계가 만들어지려면 만들어진 오브젝트가 있어야함. 지금 작성한 코드처럼 직접 생성자를 호출해서 직접 만드는 방법도 있지만 **외부에서 만들어준 것을 가져오는 방법** 도 있다. 

- 외부에서 만든 오브젝트를 전달 받으려면 메소드 파라미터나 생성자 파라미터를 이용하면 된다. 
- 이 때 파라미터의 타입을 인터페이스로 선언해뒀다면 특정 클래스를 전혀 알지 못하더라도 해당 인터페이스를 구했했다면 인터페이스 타입으로 받아서 사용 가능하다. 자바의 **다형성** 특징 덕분.

그렇다면 제 3의 오브젝트라고 했던 UserDao의 클라이언트는 무슨 역할을 하는 건가?

- UserDao 오브젝트가 DConnectionManager 오브젝트를 사용하게 하려면 두 오브젝트 사이에 런타임 사용관계 또는 링크, 또는 의존관계라 불리는 관계를 맺어주면 된다.
- 클라이언트는 클래스들을 이러한 런타임 오브젝트 관계를 갖는 구조로 만들어주는 책임을 갖는다.
- 기존의 UserDao에서는 생성자에게 책임이 있었는데, 이것은 UserDao의 관심사도 아니고 책임이 아니었음. 다른 관심사가 함께 있었기 때문에 확장성이 떨어졌던 것임.

<br>

관심을 분리해서 클라이언트에게 떠넘겨보자. 현재 코드에서는 main() 메소드가 UserDao 클라이언트임.

**[UserDao.java 생성자]**

```java
 public UserDao(ConnectionMaker connectionMaker) {
        this.connectionMater = connectionMaker;
    }
```

클라이언트가 미리 만들어둔 ConnectionMaker 오브젝트를 전달 받아 사용하도록 생성자 수정한다. 

**[UserDaoTest.java]** //Main.java 였던 것

```java
...

public class UserDaoTest {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        ConnectionMaker connectionMaker = new DConnectionMaker();

        UserDao dao = new UserDao(connectionMaker);
      
      	...
    }
}

```

기존에 DConnectionMaker를 생성했던 코드는 UserDao와 특정 ConnectionMaker 오브젝트와 관계를 맺는 책임을 담당하는 코드였음. 그 책임을 맡게 된 것이 클라이언트인 main() 메소드이다. UserDao의 생성자에서 보이던 new DConnectionMaker()라는 코드가 여기서 등장한다.

이렇게 해서 UserDao에는 전혀 손대지 않고도 모든 고객이 만족스럽게 DB 연결 기능을 확장해서 사용할 수 있게 됐다. 이제 UserDao의 자신의 관심사이자 핵심인 사용자 데이터 액세스 작업을 위해 SQL을 생성하고, 이를 실행하는 데만 집중할 수 있게 됐다.

이렇게 인터페이스를 도입하고 클라이언트의 도움을 얻는 방법은 상속을 사용해 비슷한 시도를 했을 경우에 비해서 훨씬 유연하다.

- ConnectionMaker라는 인터페이스만 사용하면 다른 DAO 클래스에도 ConnectionMaker의 구현 클래스들을 그대로 적용할 수 있기 때문.
- DAO가 아무리 많아져도 DB 접속 방법에 대한 관심은 오직 한 군데 집중되게 할 수 있음.
- DB 접속 방법을 변경해야 할 때도 오직 한 곳의 코드만 수정하면 됨.

</br>

### 1.3.4 원칙과 패턴

지금까지 초난감 DAO 코드를 개선해온 결과를 객체지향 기술의 여러 가지 이론을 통해 설명하려 한다.

#### 개방 폐쇄 원칙(OCP, Open-Closed Principle)

> 클래스나 모듈은 확장에는 열려 있어야 하고 변경에는 닫혀 있어야 한다.

UserDao는 DB 연결 방법이라는 기능을 확장하는 데는 열려 있는 동시에 UserDao 자신의 핵심 기능을 구현한 코드는 그런 변화에 영향을 받지 않고 유지할 수 있으므로 변경에는 닫혀 있다.

처음의 초난감 DAO는 DB 연결 방법을 확장하기 불편하니 확장에 필요한 유연성은 부족, 확장하고자 하면 DAO 내부도 변경해야 하니 변화에 구멍이 나있는 셈 -> 개방 폐쇄 원칙을 잘 따르지 못한 설계

> 객체지향 설계 원칙(SOLID)
>
> 객체지향의 특징을 잘 살릴 수 있는 설계의 특징. SOLID는 아래 5가지 원칙의 첫 글자를 따서 만든 단어.
>
> - SRP(The Single Responsibility Principle): 단일 책임 원칙
> - OCP(The Open Closed Principle): 개방 폐쇄 원칙
> - LSP(The Liskov Substitution Principle): 리스코프 치환 원칙
> - ISP(The Interface Segregation Principle): 인터페이스 분리 원칙
> - DIP(The Dependency Inversion Principle): 의존관계 역전 원칙

</br>

#### 높은 응집도와 낮은 결합도

**높은 응집도**

- 응집도가 높다는 건 하나의 모듈, 클래스가 하나의 책임 또는 관심사에만 집중되어 있다는 뜻.
- 변화가 일어날 때 해당 모듈에서 변하는 부분이 크다는 것으로 설명할 수도 있다.
- 초난감 DAO처럼 여러 관심사와 책임이 얽혀 있는 복잡한 코드에서는 변경이 필요한 부분을 찾아내는 것도 어렵고, 그렇게 변경한 것이 다른 기능에 영향을 주는지도 일일이 확인해야함.
- 반면, 관심사와 책임을 독립시킨다면, 무엇을 변경할지 명확해지고 다른 클래스 수정을 요구하지도 않고, 기능에 영향을 주지 않는다는 사실을 쉽게 파악할 수 있음

**낮은 결합도**

- 책임과 관심사가 다른 오브젝트 또는 모듈과는 낮은 결합도, 즉 느슨하게 연결된 형태를 유지하는 것이 바람직함.
- 결합도가 낮아지면 변화에 대응하는 속도가 높아지고, 구성이 깔끔해짐. 또한 확장하기에도 매우 편리하다.
- 결합도란 '하나의 오브젝트가 변경이 일어날 때에 관계를 맺고 있는 다른 오브젝트에게 변화를 요구하는 정도'
- ConnectionMaker의 클래스를 결정하는 책임을 DAO의 클라이언트로 분리한 덕분에 사용할 ConnectionMaker 구현 클래스가 바뀌어도, DAO 클래스의 코드를 수정할 필요가 없게 됐다. 결합도가 더욱 낮아진 것.
- 결합도가 높아지면 변경에 따르는 작업량이 많아지고, 변경으로 인해 버그가 발생할 가능성이 높아짐

</br>

#### 전략 패턴

> 자신의 기능 맥락(context)에서, 필요에 따라 변경이 필요한 알고리즘을 인터페이스를 통해 통째로 외부로 분리시키고, 이를 구현한 구체적인 알고리즘 클래스를 필요에 따라 바꿔서 사용할 수 있게 하는 디자인 패턴

- UserDAO는 전략 패턴의 컨텍스트에 해당.

- 컨텍스트는 필요한 기능 중 변경 가능한 DB 연결 방식이라는 알고리즘을 ConnectionMaker라는 인터페이스로 정의
- 이를 구현한 클래스, DConnectionMaker 등, 전략을 바꿔가면서 사용할 수 있게 분리했음.
- 클라이언트인 UserDaoTest는 컨텍스트가 사용할 전략을 컨텍스트의 생성자 등을 통해 제공하는 역할 수행

*스프링이란 바로 지금까지 설명한 객체지향적 설계 원칙과 디자인 패턴에 나타난 장점을 자연스럽게 개발자들이 활용할 수 있게 해주는 프레임워크다.*

