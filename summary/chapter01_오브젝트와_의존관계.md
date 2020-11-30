# 1장 오브젝트와 의존 관계

- 스프링이 자바에서 가장 중요하게 가치를 여기는 것은 **객체지향 프로그래밍이 가능한 언어**라는 점
- 스프링의 핵심 철학 : 엔터프라이즈 기술의 혼란 속에서 잃어버린 객체지향 기술의 진정한 가치를 회복시키고, 그로부터 객체지향 프로그래밍이 제공하는 폭넓은 혜택을 누릴 수 있도록 기본으로 돌아가자
- 스프링은 객체지향 설계와 구현에 관해 특정한 모델과 기법을 억지로 강요하지는 않는다. 하지만 오브젝트를 어떻게 효과적으로 설계하고 구현하고, 사용하고, 이를 개선해나갈 것인가에 대한 명쾌한 기준을 마련 + 객체지향 기술과 설계, 구현에 관한 실용적 전략과 검증된 베스트 프랙티스를 제공

→ 1장에서는 스프링이 관심을 갖는 대상인 오브젝트의 설계와 구현, 동작원리에 대해 집중

---

#### [INDEX]

**1.1 [초난감 DAO](https://github.com/Bellroute/Study-Toby-Spring/blob/master/summary/chapter01_%EC%98%A4%EB%B8%8C%EC%A0%9D%ED%8A%B8%EC%99%80_%EC%9D%98%EC%A1%B4%EA%B4%80%EA%B3%84.md#11-%EC%B4%88%EB%82%9C%EA%B0%90-dao)**

**1.2 [DAO의 분리](https://github.com/Bellroute/Study-Toby-Spring/blob/master/summary/chapter01_%EC%98%A4%EB%B8%8C%EC%A0%9D%ED%8A%B8%EC%99%80_%EC%9D%98%EC%A1%B4%EA%B4%80%EA%B3%84.md#12-dao%EC%9D%98-%EB%B6%84%EB%A6%AC)**

**1.3 [DAO의 확장](https://github.com/Bellroute/Study-Toby-Spring/blob/master/summary/chapter01_%EC%98%A4%EB%B8%8C%EC%A0%9D%ED%8A%B8%EC%99%80_%EC%9D%98%EC%A1%B4%EA%B4%80%EA%B3%84.md#13-dao%EC%9D%98-%ED%99%95%EC%9E%A5)**

**1.4 [제어의 역전(IoC)](https://github.com/Bellroute/Study-Toby-Spring/blob/master/summary/chapter01_%EC%98%A4%EB%B8%8C%EC%A0%9D%ED%8A%B8%EC%99%80_%EC%9D%98%EC%A1%B4%EA%B4%80%EA%B3%84.md#14-%EC%A0%9C%EC%96%B4%EC%9D%98-%EC%97%AD%EC%A0%84ioc)**

**1.5 [스프링의 IoC](https://github.com/Bellroute/Study-Toby-Spring/blob/master/summary/chapter01_%EC%98%A4%EB%B8%8C%EC%A0%9D%ED%8A%B8%EC%99%80_%EC%9D%98%EC%A1%B4%EA%B4%80%EA%B3%84.md#15-%EC%8A%A4%ED%94%84%EB%A7%81%EC%9D%98-ioc)**

**1.6 [싱글톤 레지스트리와 오브젝트 스코프](https://github.com/Bellroute/Study-Toby-Spring/blob/master/summary/chapter01_%EC%98%A4%EB%B8%8C%EC%A0%9D%ED%8A%B8%EC%99%80_%EC%9D%98%EC%A1%B4%EA%B4%80%EA%B3%84.md#16-%EC%8B%B1%EA%B8%80%ED%86%A4-%EB%A0%88%EC%A7%80%EC%8A%A4%ED%8A%B8%EB%A6%AC%EC%99%80-%EC%98%A4%EB%B8%8C%EC%A0%9D%ED%8A%B8-%EC%8A%A4%EC%BD%94%ED%94%84)**

**1.7 [의존관계 주입(DI)](https://github.com/Bellroute/Study-Toby-Spring/blob/master/summary/chapter01_%EC%98%A4%EB%B8%8C%EC%A0%9D%ED%8A%B8%EC%99%80_%EC%9D%98%EC%A1%B4%EA%B4%80%EA%B3%84.md#17-%EC%9D%98%EC%A1%B4%EA%B4%80%EA%B3%84-%EC%A3%BC%EC%9E%85di)**

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

## 1.2 DAO의 분리

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

</br>

## 1.4 제어의 역전(IoC)

IoC라는 약자로 많이 사용되는 제어의 역전(Inversion of Control)이 무엇인지 살펴보기 위해 UserDao 코드를 좀 더 개선해보자.

### 1.4.1 오브젝트 팩토리

UserDaoTest는 기존에 UserDao가 직접 담당하던 기능(어떤 ConnectionMaker 구현 클래스를 사용할지를 결정하는 기능)을 떠맡음. UserDao가 ConnectionMaker 인터페이스를 구현한 특정 클래스로부터 독립할 수 있게 되었지만, 사실 UserDaoTest의 역할은 UserDao의 기능이 잘 동작하는지 테스트하는 것임. 이 역시 분리가 필요해 보임.

-> 분리될 기능

- UserDao와 ConnectionMaker 구현 클래스의 오브젝트를 만드는 것
- 그렇게 만들어진 두 개의 오브젝트가 연결돼서 사용될 수 있도록 관계를 맺어 주는 것.

</br>

#### 팩토리

> 객체의 생성 방법을 결정하고 그렇게 만들어진 오브젝트를 돌려주는 역할을 하는 오브젝트를 흔히 팩토리(Factory)라고 부른다.

- 디자인 패턴에서 말하는 추상 팩토리 패턴, 팩토리 메소드 패턴과는 다른 개념
- 단지 오브젝트를 생성하는 쪽과 생성된 오브젝트를 사용하는 쪽의 역할과 책임을 깔끔하게 분리하려는 목적으로 사용

팩토리 역할을 맡을 클래스를 DaoFactory라고 하자. 그리고 UserDaoTest에 담겨 있던 UserDao, ConnectionMaker 관련 생성 작업을 DaoFactory로 옮기고, UserDaoTest에서는 DaoFactory에 요청해서 미리 만들어진 UserDao 오브젝트를 가져와 사용하게 만든다.

**[DaoFactory.java]**

```java
package springbook.user.dao;

public class DaoFactory {

    public UserDao userDao() {
        // 팩토리의 메소드는 UserDao 타입의 오브젝트를 어떻게 만들고, 어떻게 준비시킬지를 결정
        ConnectionMaker connectionMaker = new DConnectionMaker();
        UserDao userDao = new UserDao(connectionMaker);

        return userDao;
    }
}
```

DaoFactory의 userDao 메소드를 호출하면 DConnectionMaker를 사용해 DB 커넥션을 가져오도록 이미 설정된 UserDao 오브젝트를 되돌려줌.

**[UserDaoTest.java]**

```java
package springbook.user;

import springbook.user.dao.DaoFactory;
import springbook.user.dao.UserDao;
import springbook.user.domain.User;

import java.sql.SQLException;

public class UserDaoTest {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        UserDao dao = new DaoFactory().userDao();
      	...
    }
}

```

UserDaoTest는 이제 UserDao가 어떻게 만들어지는지 신경 쓰지 않고 팩토리로부터 UserDao 오브젝트를 받아다가, 자신의 관심사인 테스트를 위해 활용하기만 하면 그만. ~~(리팩토링 뒤엔 잊지 말고 테스트)~~

</br>

#### 설계도로서의 팩토리

분리됨 오브젝트들의 역할과 관계를 분석해보자.

- UserDao, ConnectionMaker - 애플리케이션의 핵심적인 데이터 로직과 기술 로직을 담당
- DaoFactory - 애플리케이션의 오브젝트들을 구성하고 그 관계를 정의하는 책임

전자가 실질적인 로직을 담당하는 **컴포넌트** 라면, 
후자는 애플리케이션을 구성하는 컴포넌트의 구조와 관계를 정의한 **설계도** 같은 역할을 수행

설계도는 <u>간단히 어떤 오브젝트가 어떤 오브젝트를 사용하는지를 정의해놓은 코드.</u> 이런 작업이 애플리케이션 전체에 걸쳐 일어난다면 컴포넌트의 의존관계에 대한 설계도와 같은 역할을 하게 될 것.

이제 새로운 ConnectionMaker 구현 클래스로 변경이 필요하면 DaoFactory를 수정해서 변경된 클래스를 생성해 설정해주도록 코드를 수정해주면 된다. 여전히 우리의 핵심 기술이 담긴 UserDao는 변경이 필요 없어 안전하게 소스코드를 보존할 수 있음. 동시에 DB 연결 방식은 자유로운 확장이 가능

DaoFactory를 분리 함으로써, **애플리케이션의 컴포넌트 역할을 하는 오브젝트와 애플리케이션의 구조를 결정하는 오브젝트를 분리했다** 는 데 가장 큰 의미가 있다.

</br>

### 1.4.2 오브젝트 팩토리의 활용

DaoFactory에 UserDao가 아닌 다른 DAO의 생성 기능을 넣으면 어떻게 될까? AccountDao, MessageDao 등을 만들었다고 해보자.

userDao() 메소드를 복사해서 accountDao(), messageDao()를 만든다면?

-> ConnectionMaker 구현 클래스의 오브젝트를 생성하는 코드가 메소드마다 반복됨. 기능이 중복돼서 나타남.

**[DaoFactory.java]**

```java
package springbook.user.dao;

public class DaoFactory {

    public UserDao userDao() {
        // 팩토리의 메소드는 UserDao 타입의 오브젝트를 어떻게 만들고, 어떻게 준비시킬지를 결정
        return new UserDao(new DConnectionMaker());
    }

    public AccountDao accountDao() {
        return new AccountDao(new DConnectionMaker());
    }

    public MessageDao messageDao() {
        return new MessageDao(new DConnectionMaker());
    }
}
```

새 개의 DAO를 만드는 팩토리 메소드 안에 모두 new DConnectionMaker() 라는 인스턴스를 만드는 부분이 반복돼서 나타남. DAO가 더 많아지면 ConnectionMaker의 구현 클래스를 바꿀 때마다 일일이 수정해야함.

**[DaoFactory.java]**

```java
package springbook.user.dao;

public class DaoFactory {

    public UserDao userDao() {
        // 팩토리의 메소드는 UserDao 타입의 오브젝트를 어떻게 만들고, 어떻게 준비시킬지를 결정
        return new UserDao(connectionMaker());
    }

    public AccountDao accountDao() {
        return new AccountDao(connectionMaker());
    }

    public MessageDao messageDao() {
        return new MessageDao(connectionMaker());
    }
    
    public ConnectionMaker connectionMaker() {
        return new DConnectionMaker(); // 분리해서 중복을 제거한 ConnectionMaker 타입 오브젝트 생성 코드
    }
}
```

ConnectionMaker의 구현 클래스를 결정하고 오브젝트를 만드는 코드를 별도의 메소드로 분리하여 중복 해결 가능. ConnectionMaker의 구현 클래스를 바꿀 필요가 있을 때도 한 군데(connectionMaker())만 수정하면 모든 DAO 팩토리 메소드에 적용됨.

</br>

### 1.4.3 제어권의 이전을 통한 제어관계 역전

제어의 역전이라는 건, 간단히 프로그램의 **제어 흐름 구조가 뒤바뀌는 것** 이라고 설명할 수 있다.

**일반적인 프로그램의 흐름은**

- main() 메소드와 같이 프로그램이 시작되는 지점에서 다음에 사용할 오브젝트를 결정, 생성, 호출하는 식의 작업이 반복
- 각 오브젝트는 프로그램 흐름을 결정하거나 사용할 오브젝트를 구성하는 작업에 능동적으로 참여함
- 모든 오브젝트가 능동적으로 자신이 사용할 클래스를 결정하고, 언제 어떻게 만들지를 스스로 관장
- 모든 종류의 작업을 사용하는 쪽에서 제어하는 구조

**제어의 역전에서는 이런 제어 흐름의 개념을 거꾸로 뒤집는다.**

- 오브젝트가 자신이 사용할 오브젝트를 스스로 선택, 생성하지 않음.
- 자신도 어떻게 만들어지고 어디서 사용되는지를 알 수 없음
- 모든 제어 권한을 자신이 아닌 다른 대상에게 위임하기 때문
- 프로그램의 시작을 담당하는 엔트리 포인트(main() 메소드)를 제외하면 모든 오브젝트는 위임받은 제어 권한을 갖는 특별한 오브젝트에 의해 결정되고 만들어짐

</br>

제어의 역전 개념은 이미 폭넓게 적용되어 있다.

- 서블릿 - 서블릿에 대한 제어 권한을 가진 컨테이너가 적절한 시점에서 서블릿 클래스의 오브젝트를 만들고 그 안에 메소드를 호출
- 템플릿 메소드 패턴 - 서브클래스가 구현을 담당하지만 언제 어떻게 사용될지는 슈퍼클래스에서 결정. 제어권을 상위 템플릿 메소드에 넘기고 자신은 필요할 때 호출되어 사용되도록 함.
- 프레임워크 - 프레임워크는 라이브러리의 다른 이름이 아님. 프레임워크는 분명한 제어의 역전 개념이 적용됨

</br>

**프레임워크 VS 라이브러리**

- 라이프러리를 사용하는 애플리케이션 코드는 애플리케이션 흐름을 직접 제어함. 단지 동작 중에 필요한 기능이 있을 때 능동적으로 라이브러리를 사용
- 프레임워크는 거꾸로 애플리케이션 코드가 프레임워크에 의해 사용됨. 프레임워크 위에 개발한 클래스를 등록해두고, 프레임워크가 흐름을 주도하는 중에 개발자가 만든 애플리케이션을 사용

IoC는 기본적으로 프레임워크만의 기술도 아니고 프레임워크가 꼭 필요한 개념도 아님. 상당히 폭넓게 사용되는 프로그래밍 모델임. IoC를 적용함으로써 설계가 깔끔해지고 유연성이 증가하며 확장성이 좋아지기 때문.

제어의 역전에서는 프레임워크 또는 컨테이너와 같이 애플리케이션 컴포넌트의 생성과 관계설정, 사용, 생명주기 관리 등을 관장하는 존재가 필요함. IoC를 애플리케이션 전반에 걸쳐 본격적으로 적용하려면 스프링과 같은 IoC 프레임워크의 도움을 받는 편이 훨씬 유리하다. 

*스프링은 IoC를 모든 기능의 기초가 되는 기반기술로 삼고 있으며, IoC를 극한까지 적용하고 있는 프레임워크다.*

</br>

## 1.5 스프링의 IoC

스프링의 핵심을 담당하는 것은 빅 팩토리 또는 애플리케이션 컨텍스트라고 불리는 것이다. 이 두 가지는 우리가 만든 DaoFactory가 하는 일을 좀 더 일반화한 것이라고 설명할 수 있다.

### 1.5.1 오브젝트 팩토리를 이용한 스프링 IoC

#### 애플리케이션 컨텍스트와 설정정보

DaoFactory를 스프링에서 사용이 가능하도록 변신시켜보자.

스프링에서는 <u>스프링이 제어권을 가지고 직접 만들고 관계를 부여하는 오브젝트</u>를 **빈(bean)** 이라고 부른다. 

- 자바빈 또는 엔터프라이즈 자바빈(EJB)에서 말하는 빈과 비슷한 오브젝트 단위의 애플리케이션 컴포넌트를 말한다. 
- 동시에 스프링 빈은 <u>스프링 컨테이너가 생성과 관계설정, 사용 등을 제어해주는 제어의 역전이 적용된 오브젝트</u>를 가리키는 말이다.

스프링에서는 <u>빈의 생성과 관계설정 같은 제어를 담당하는 IoC 오브젝트</u>를 **빈 팩토리(bean factory)** 라고 부른다. 보통 빈 팩토리보다는 이를 좀 더 확장한 **애플리케이션 컨텍스트(application context)** 를 주로 사용한다. 

- 애플리케이션 컨텍스트는 IoC 방식을 따라 만들어진 일종의 빈 팩토리 (빈 팩토리 == 애플리케이션 컨텍스트)

- 빈 팩토리라고 말할 때는 빈을 생성하고 관계를 설정하는 IoC의 기본 기능에 초점
- 애플리케이션 컨텍스트라고 말할 때는 애플리케이션 전반에 걸쳐 모든 구성요소의 제어 작업을 담당하는 IoC 엔진의 의미

**애플리케이션 컨텍스트**는 

- 별도의 정보를 참고해서 빈(오브젝트)의 생성, 관계설정 등의 제어 작업을 총괄. 
- DaoFactory 코드에 있는 설정정보를 직접 담고 있지는 않지만, 별도로 설정정보를 담고 있는 무언가를 가져와 이를 활용하는 범용적인 IoC 엔진 같은 것.

앞에서는 DaoFactory 자체가 설정정보까지 담고 있는 IoC 엔진이었는데, 자바 코드로 만든 애플리케이션 컨텍스트의 설정정보로 활용될 것임. 

앞서 애플리케이션의 로직을 담고 있는 컴포넌트와 설계도 역할을 하는 팩토리를 구분했음. 이 설계도라는 게 바로 애플리케이션 컨텍스트와 그 설정정보를 말함. 마치 건물이 설계도면을 따라 만들어지듯이, 애플리케이션도 애플리케이션 컨텍스트와 그 설정정보를 따라서 만들어지고 구성된다.

</br>

#### DaoFactory를 사용하는 애플리케이션 컨텍스트

DaoFactory를 스프링의 빈 팩토리가 사용할 수 있는 본격적인 설정정보로 만들어보자. (스프링 기능을 사용했으니 필요한 라이브러리를 추가해야 함. 본인은 maven을 이용함.)

- 먼저 <u>스프링이 빈 팩토리를 위한 오브젝트 설정을 담당하는 클래스라고 인식</u>할 수 있도록 **@Configuration** 어노테이션 추가

- <u>오브젝트를 만들어 주는 메소드</u>에는 **@Bean** 어노테이션을 붙임

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoFactory {

    @Bean
    public UserDao userDao() {
        return new UserDao(connectionMaker());
    }
  
  	...

    @Bean
    public ConnectionMaker connectionMaker() {
        return new DConnectionMaker();
    }
}
```

이 두가지 어노테이션만으로 스프링 프레임워크의 빈 팩토리 또는 애플리케이션 컨텍스트가  IoC 방식의 기능을 제공할 때 사용할 완벽한 설정정보가 된 것. 자바 코드의 탈을 쓰고 있지만, 사실은 XML과 같은 스프링 전용 설정정보라고 보는 것이 좋다.

이제 DaoFactory를 설정정보로 사용하는 애플리케이션 컨텍스트를 만들어보자.

애플리케이션 컨텍스트는 ApplicationContext 타입의 오브젝트다. ApplicationContext를 구현한 클래스는 여러 가지가 있는데 DaoFactory처럼 @Configuration이 붙은 자바 코드를 설정정보로 사용하려면 AnnotaionConfigApplicationContext를 이용한다. 애플리케이션 컨텍스트를 만들 때 생성자 파라미터로  DaoFactory 클래스를 넣어 준다. ApplicationContext의 getBean()이라는 메소드를 이용해 UserDao의 오브젝트를 가져올 수 있다.

```java
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
...

import java.sql.SQLException;

public class UserDaoTest {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        ApplicationContext context = 
          new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao dao = context.getBean("userDao", UserDao.class);

      	...
    }
}

```

- getBean() 메소드는 ApplicationContext가 관리하는 오브젝트를 요청하는 메소드다.

- getBean()의 첫 번째 파라미터 "userDao"는 등록된 빈의 이름이다.
- @Bean 어노테이션을 붙인 메소드 이름이 빈의 이름이 된다.
- userDao 이름의 빈을 가져온다는 것은 DaoFactory의 userDao() 메소드를 호출한 결과를 가져오는 것임.

UserDao를 가져오는 메소드는 하나뿐인데 왜 굳이 이름을 사용하나?
-> UserDao를 생성하는 방식이나 구성을 다르게 가져가는 메소드를 추가할 수 있기 때문.

두 번째 파라미터의 역할은?

- getBean()은 기본적으로 Object 타입으로 리턴하게 되어 있음.
- 매번 리턴되는 오브젝트에 다시 캐스팅 해줘야하는 부담
- 자바 5 이상의 제너릭(Generic) 메소드 방식을 사용해 getBean()의 두 번째 파라미터에 리턴 타입을 주면, 지저분한 캐스팅 코드를 사용하지 않아도 됨!

스프링을 적용하긴 했지만 사실 앞에서 만든 DaoFactory를 직접 사용한 것과 기능적으로 다를 바 없음. 오히려 번거로운 준비 작업과 코드가 필요했음. 굳이 그래도 스프링을 사용해야하나 의문이 들지만, 스프링은 지금까지 우리가 구현했던 DaoFactory를 통해서 얻을 수 없는 방대한 기능과 활용 방법을 제공해 준다.

</br>

### 1.5.2 애플리케이션 컨텍스트의 동작방식

기존에 오브젝트 팩토리를 이용했던 방식과 스프링의 애플리케이션 컨텍스트를 사용한 방식을 비교해보자.

애플리케이션 컨텍스트는

- 스프링에서는 IoC 컨테이너, 스프링 컨테이너, 빈 팩토리라고 부른다.
- ApplicationContext 인터페이스를 구현하는데, ApplicationContext는 빈 팩토리가 구현하는 BeanFactory 인터페이스를 상속했으므로 애플리케이션 컨텍스트는 일종의 빈 팩토리인 셈
- 애플리케이션 컨텍스트가 스프링의 가장 대표적인 오브젝트이므로, 이 자체를 그냥 스프링이라고 부르는 개발자도 있음.

**오브젝트 팩토리 VS 애플리케이션 컨텍스트**

- DaoFactory가 UserDao를 비롯한 Dao 오브젝트를 생성하고  DB 생성 오브젝트와 관계를 맺어주는 제한적인 역할을 하는 데 반해, 
- 애플리케이션 컨텍스트는 애플리케이션에서 IoC를 적용해서 관리할 모든 오브젝트에 대한 생성과 관계설정을 담당함.
  - 대신 DaoFactory와 달리 직접 오브젝트를 생성하고 관계를 맺어주는 코드가 없고, 
  - 그런 생성정보와 연관관계 정보를 별도의 설정정보를 통해 얻음.
  - 때로는 외부의 오브젝트 팩토리에 그 작업을 위임하고 그 결과를 가져다가 사용함

@Configuration이 붙은 DaoFactory는  애플리케이션 컨텍스트가 활용하는 IoC 설정정보임. 내부적으로는 애플리케이션 컨텍스트가 DaoFactory의 userDao() 메소드를 호출해서 오브젝트를 가져온 것을 getBean()으로 요청할 때 전달해줌.

- 애플리케이션 컨텍스트는 DaoFactory 클래스를 설정정보로 등록해두고  @Bean이 붙은 메소드의 이름을 가져와 빈 목록을 만들어 둠. 
- 클라이언트가 애플리케이션 컨텍스트의 getBean() 메소드를 호출하면 자신의 빈 목록에서 요청한 이름이 있는지 찾고, 있다면 빈을 생성하는 메소드를 호출해서 오브젝트를 생성시킨 후 클라이언트에 돌려줌.

오브젝트 팩토리가 아닌 애플리케이션 컨텍스트를 사용하는 이유는 범용적이고 유연한 방법으로 IoC 기능을 확장하기 위함.

- **클라이언트는 구체적인 팩토리 클래스를 알 필요가 없다.**
  - 오브젝트 팩토리가 아무리 많아져도 이를 알아야하거나 직접 사용할 필요 없음
  - 자바 코드를 작성하는 대신 XML처럼 단순한 방법을 사용해 설정정보를 만들 수 있음
- **애플리케이션 컨텍스트는 종합 IoC 서비스를 제공해준다.**
  - 오브젝트 간 관계설정 외에도 만들어지는 방식, 시점과 전략을 다르게 가져갈 수 있음
  - 자동생성, 오브젝트에 대한 후처리, 정보 조합, 설정방식 다변화, 인터셉팅 등 다양한 기능 제공
- **애플리케이션 컨텍스트는 빈을 검색하는 다양한 방법을 제공한다.**
  - 빈의 이름, 타입, 특별한 어노테이션 설정 등 다양한 방법으로 빈을 찾을 수 있음

</br>

### 1.5.3 스프링 IoC 용어 정리

#### 빈(Bean)

- 스프링이 IoC 방식으로 관리하는 오브젝트. 
- 주의할 점은 스프링을 사용하는 애플리케이션에서 만들어지는 모든 오브젝트가 다 빈은 아니라는 것. 
- 스프링이 직접 그 생성과 제어를 담당하는 오브젝트만 빈에 해당

#### 빈 팩토리(Bean Factory)

- 스프링의 IoC를 담당하는 핵심 컨테이너
- 빈을 등록, 생성, 조회, 돌려주는 등 빈을 관리하는 기능
- 보통은 빈 팩토리를 확장한 애플리케이션 컨텍스트를 이용
- BeanFactory 라는 인터페이스를 가리키며 getBean()과 같은 메소드가 정의되어 있음

#### 애플리케이션 컨텍스트(Application Context)

- 빈 팩토리를 확장한 IoC 컨테이너
- 빈 팩토리의 기본적인 기능 + 스프링이 제공하는 각종 부가 서비스 제공
- 빈 팩토리라고 부를 때는 주로 빈의 생성과 제어의 관점, 애플리케이션 컨텍스트라고 할 때는 스프링이 제공하는 애플리케션 지원 기능을 포함
- ApplicationContext라는 인터페이스를 가리킴. ApplicationContext는 BeanFactory를 상속함

#### 설정정보/설정 메타정보(Configuration metadata)

- 애플리케이션 컨텍스트 또는 빈 팩토리가 IoC를 적용하기 위해 사용하는 메타정보
- 스프링 설정정보는 컨테이너에 어떤 기능을 세팅하거나 조정하는 경우에도 사용
- 하지만 그보자는 IoC 컨테이너에 의해 관리되는 애플리케이션 오브젝트를 생성하고 구성할 때 사용

#### 컨테이너(Container) 또는 IoC 컨테이너

- IoC 방식으로 빈을 관리한다는 의미에서 애플리케이션 컨텍스트나 빈 팩토리를 컨테이너 또는 IoC 컨테이너라고도 함.
- IoC 컨테이너는 주로 빈 팩토리 관점, 그냥 컨테이너 혹은 스프링 컨테이너는 애플리케이션 컨텍스트를 가리키는 것
- 컨테이너라는 말 자체가 IoC 개념 내포. 애플리케이션 컨텍스트보다 더 추상적인 표현
- 애플리케이션 컨텍스트 오브젝트는 하나의 애플이케이션에서 여러 개가 만들어져 사용. 이를 통틀어 스프링 컨테이너라고 부를 수 있음
- '스프링에 빈을 등록하고' -> 스프링 == 스프링 컨테이너 or 애플리케이션 컨텍스트

#### 스프링 프레임워크

- IoC 컨테이너, 애플리케이션 컨텍스트를 포함해서 스프링이 제공하는 모든 기능을 통틀어 말할 때 주로 사용

</br>

## 1.6 싱글톤 레지스트리와 오브젝트 스코프

스프링의 애플리케이션 컨텍스트는 기존에 직접 만들었던 오브젝트 팩토리와는 중요한 차이점이 있다.

먼저 DaoFactory의 userDao()를 여러 번 호출했을 때 리턴되는 UserDao는 과연 동일한 오브젝트일까?

-> 매번 다른 오브젝트가 리턴된다. 코드를 보면 매번 userDao 메소드를 호출할 때마다 new 연산자에 의해 새로운 오브젝트가 만들어지게 되어있기 때문.

> 오브젝트의 동일성과 동등성
>
> - 동일성(identical) : 두 개의 오브젝트가 완전히 같음. 하나의 오브젝트를 두 변수가 가리키는 것.
> - 동등성(equality) : 두 개의 오브젝트가 동일한 정보를 담고 있음. 두 개의 각기 다른 오브젝트가 메모리상에 존재.
>
> 자바 클래스를 만들 때 equals()를 따로 구현하지 않으면 최상위 클래스인 Object 클래스에 구현된 equals()가 사용된다. Object의 equals()는 두 오브젝트의 동일성을 비교한다.

[직접 생성한 DaoFactory 오브젝트 출력 코드]

```java
DaoFactory factory = new DaoFactory();
UserDao dao1 = factory.userDao();
UserDao dao2 = factory.userDao();

System.out.println(dao1);
System.out.println(dao2);
```

이 코드를 실행하면 다음과 같은 결과가 나온다.

```
springbook.dao.UserDao@118f375
springbook.dao.UserDao@117a8bd
```

두 개는 각기 다른 값을 가진 동일하지 않은 오브젝트임을 알 수 있다.

[스프링 컨텍스트로부터 가져온 오브젝트 출력 코드]

```java
ApplicationContext context = new AnnotationConfigurationContext(DaoFactory.class);
UserDao dao3 = context.getBean("userDao", UserDao.class);
UserDao dao4 = context.getBean("userDao", UserDao.class);

System.out.println(dao3);
System.out.println(dao3);
```

반면, 스프링 애플리케이션 컨텍스트에  DaoFactory를 설정 정보로 등록하고 g etBean() 메소드를 이용해 오브젝트를 가져오면 다음과 같은 결과가 나온다.

```
springbook.dao.UserDao@ee22f7
springbook.dao.UserDao@ee22f7
```

두 오브젝트의 출력 값이 같다. getBean()을 두 번 호출해서 가져온 오브젝트가 동일함을 알 수 있다. 오브젝트 팩토리와 스프링 애플리케이션 컨텍스트의 동작방식에는 무언가 차이가 있는 것 같다.

</br>

### 1.6.1 싱글톤 레지스트리로서의 애플리케이션 컨텍스트

애플리케이션 컨텍스트는 우리가 만들었던 오브젝트 팩토리와 비슷한 방식으로 동작하는 IoC 컨테이너이면서, 싱글톤을 저장하고 관리하는 **싱글톤 레지스트리(singleton registry)** 이다. 스프링은 기본적으로 별다른 설정이 없다면 빈 오브젝트를 싱글톤으로 만든다.

> 싱글톤(singleton)
>
> - 애플리케이션 전체 영역에서 하나의 클래스에 단 하나의 인스턴스를 생성하는 것
>
> (여기서 싱글톤은 디자인 패턴에서 나오는 싱글톤 패턴과 비슷한 개념이지만 구현 방법은 다름)

#### 서버 애플리케이션과 싱글톤

왜 스프링은 싱글톤으로 빈을 만드나?

- 스프링이 주로 적용되는 대상이 자바 엔터프라이즈 기술을 사용하는 서버환경이기 때문. 
- 태생적으로 스프링은 엔터프라이즈 시스템을 위해 고안된 기술이기 때문에 서버 환경에서 사용될 때 그 가치가 있다.
- 매번 클라이언트에서 요청이 올 때마다 각 로직을 담당하는 오브젝트를 새로 만들어서 사용한다면?
  - 요청 한 번에 5개 오브젝트, 초당 500개의 요청이라고 가정하면 -> 초당 2500개의 새로운 오브젝트가 생성됨
  - 자바의 오브젝트 생성과 GC 성능이 좋아졌다고 하더라도 부하가 걸리면 서버가 감당하기 힘들다;;
- 자바 엔터프라이즈 기술의 가장 기본이 되는 서비스 오브젝트인 서블릿 역시 싱글톤으로 동작.

이렇게 애플리케이션 안에 제한된 수, 대개 한 개의 오브젝트만 마들어 사용하는 것이 싱글톤 패턴의 원리이고, 서버환경에서는 서비스 싱글톤의 사용이 권장된다.

</br>

#### 싱글톤 패턴의 한계

자바에서 싱글톤을 구현하는 방법

- 클래스 밖에서는 오브젝트를 생성하지 못하도록 생성자를 private로
- 생성된 싱글톤 오브젝트를 저장할 수 있는 자신과 같은 타입의 스태택 필드 정의
- 스태틱 팩토리 메소드인 getInstance()를 만들고 이 메소드가 최초로 호출되는 시접에서 한 번만 오브젝트가 만들어지게 함. 생성된 오브젝트는 스태택 필드에 저장.
- 한번 오브젝트(싱글톤)가 만들어지고 난 후에는 getInstance() 메소들을 통해 이미 만들진 스태틱 필드에 저장해둔 오브젝트를 넘겨준다.

```java
public class UserDao {
    private static UserDao INSTANCE;
		...
    private UserDao(ConnectionMaker connectionMaker) {
        this.connectionMater = connectionMaker;
    }

    public static synchronized UserDao getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserDao(???);
        }

        return INSTANCE;
    }
  
		...
}

```

전형적인 싱글톤 패턴을 이용해 UserDao 코드를 수정하면 위와 같다. 코드가 지저분해지기도 하고, ConnectionMaker 오브젝트를 외부에서 넣어주는 것이 불가능해졌다.

일반적으로 싱글톤 패턴 구현 방식에는 다음과 같은 문제가 있음.

- **private 생성자를 갖고 있기 때문에 상속할 수 없다.**
  - private 생성자를 가진 클래스는 다른 생성자가 없다면 상속이 불가능. 
  - 객체지향적인 설계의 장점을 적용하기 어려워짐.
  - 객체지향의 특징이 적용되지 않는 스태틱 필드와 메소드 역시 동일한 문제점을 가짐.
- **싱글톤은 테스트하기가 힘들다.**
  - 싱글톤은 만들어지는 방식이 제한적이기 때문에 테스트에 사용될 목 오브젝트 등으로 대체하기 힘듦
  - 싱글톤은 초기화 과정에서 생성자 등을 통해 사용할 오브젝트를 주입하기 힘들기 때문에 필요한 오브젝트는 직접 만들어 사용할 수 밖에 없음
- **서버환경에서는 싱글톤이 하나만 만들어지는 것을 보장하지 못한다.**
  - 서버에서 클래스 로더를 어떻게 구성하고 있느냐에 따라서 싱글톤 클래스임에도 하나 이상의 오브젝트가 만들어질 수 있음.
  - 여러 개의 JVM에 분산돼서 설치가 되는 경우에도 각각 독립적으로 오브젝트가 생겨 싱글톤으로서 가치가 떨어짐.
- **싱글톤의 사용은 전역 상태를 만들 수 있기 때문에 바람직하지 못하다.**
  - 아무 객체나 자유롭게 접근하고 수정하고 공유하는 전역 상태를 갖는 것은 객체지향 프로그래밍에서는 권장 x.
  - 싱글톤은 스태틱 메소드를 이용해 언제든지 쉽게 접근할 수 있기 때문에 애플리케이션 어디서든지 사용되어 전역 상태로 사용되기 쉬움.

</br>

#### 싱글톤 레지스트리

자바의 기본적인 싱글톤 패턴의 구현 방식은 여러 가지 단점이 있기 때문에, 스프링은 직접 싱글톤 형태의 오브젝트를 만들고 관리하는 기능을 제공한다. 그것이 바로 **싱글톤 레지스트리(singleton registry)** !

- 스프링 컨테이너는 싱글톤을 생성하고, 관리하고, 공급하는 싱글톤 관리 컨테이너이기도 하다.

- 싱글톤 레지스트리의 장점은 스태틱 메소드와 private 생성자를 사용해야 하는 비정상적인 클래스가 아니라 <u>평범한 자바 클래스를 싱글톤으로 활용하게 해준다</u>는 점
  - public 생성자를 가질 수 있음
  - 싱글톤으로 사용돼야 하는 환경이 아니면 간단히 오브젝트 생성 가능 -> 테스트 환경에서 자유롭게 오브젝트 만들 수 있음
  - 생성자 파라미터를 이용해서 사용할 오브젝트를 넣어주게 하는 것도 가능
  - (중요) 객체지향적인 설계 방식과 원칙, 디자인 패턴 등을 적용하는 데 아무런 제약이 없다!
- 스프링이 빈을 싱글톤으로 만드는 것은 결국 오브젝트의 생성 방법을 제어하는 IoC 컨테이너로서의 역할임

</br>

### 1.6.2 싱글톤과 오브젝트의 상태

싱글톤은 멀티스레드 환경이라면 여러 스레드가 동시에 접근해서 사용할 수 있음 -> 상태 관리에 주의!!

기본적으로 싱글톤이 멀티스레드 환경에서 서비스 형태의 오브젝트로 사용되는 경우에는 상태 정보를 내부에 가지고 있지 않은 **무상태 방식**으로 만들어져야 함.

상태가 없는 방식으로 클래스를 만드는 경우에 각 요청에 대한 정보나, DB나 서버의 리소스로부터 생성한 정보는 어떻게 다뤄야 할까?

-> 파라미터와 로컬 변수, 리턴 값 등을 이용. 얘네는 매번 새로운 값을 저장할 독립적인 공간(스택)이 만들어지기 때문에 덮어쓸 일 없음.

[인스턴스 변수를 사용하도록 수정한 UserDao]

```java
public class UserDao {
    private ConnectionMaker connectionMater; // 초기에 설정하면 사용 중에는 바뀌지 않는 읽기전용 인스턴스 변수
    private Connection c;
    private User user; // 매번 새로운 값으로 바뀌는 정보를 담은 인스턴스 변수. 멀티스레드 환경에서 심각한 문제 발생

    public User get(String id) throws ClassNotFoundException, SQLException {
        this.c = connectionMater.makeConnection();

        PreparedStatement ps = c.prepareStatement(
                "select * from users where id = ?");
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();
        rs.next();
        this.user = new User();
        this.user.setId(rs.getString("id"));
        this.user.setName(rs.getString("name"));
        this.user.setPassword(rs.getString("password"));

        rs.close();
        ps.close();
        c.close();

        return this.user;
    }
}
```

기존에 만들었던 UserDao와 다른 점은 기존에 로컬 변수 였던 Connection과 User 클래스를 인스턴스 필드로 선언했다는 것

-> 싱글톤으로 만들어져서 멀티스레드 환경에서 사용하면 심각한 문제가 발생. 스프링의 싱글톤 **빈으로 사용되는 클래스를 만들 때는 기존의 UserDao 처럼 개별적으로 바뀌는 정보는 로컬 변수로 정의하거나, 파라미터로 주고받으면서 사용** 해야함.

하지만 기존의 UserDao에서도 인스턴스 변수로 사용한 ConnectionMaker는 괜찮다.

-> connectionMaker는 읽기 전용의 정보이기 때문. DaoFactory에 @Bean을 붙여 만들었으니 스프링이 관리하는 빈이고, 하나의 오브젝트만 생성될 것임. 이렇게 **자신이 사용하는 다른 싱글톤 빈을 저장하려는 용도라면 인스턴스 변수를 사용해도 좋다.**

-> 읽기 전용의 속성을 가진 정보도 싱글톤에서 인스턴스 변수로 사용해도 좋다. 단순한 읽기전용 값이라면 static final이나 final로 선언하는 편이 좋음.

</br>

### 1.6.3 스프링 빈의 스코프

스프링이 관리하는 <u>빈이 생성되고, 존재하고, 적용되는 범위</u>에 대해 알아보자. 스프링에서는 이것을 **빈의 스코프(scope)** 라고 함

- **싱글톤 스코프** - 스프링 빈의 기본 스코프. 컨테이너 내에 한 개의 오브젝트만 만들어져서, 강제로 제거하지 않는 한 계속 유지. 대부분의 빈이 여기에 해당.
- **프로토타입 스코프** - 컨테이너에 빈을 요청할 때마다 매번 새로운 오브젝트 생성
- **요청 스코프** - HTTP 요청이 생길 때마다 생성
- **세션 스코프** - 웹의 세션과 유사한 스코프

(싱글톤 스코프 외의 스코프는 10장에서 자세히 다룬다)

</br>

## 1.7 의존관계 주입(DI)

### 1.7.1 제어의 역전(IoC)과 의존관계 주입

IoC라는 용어는 매우 느슨하게 정의돼서 폭넓게 사용 됨. 때문에 스프링을 IoC 컨테이너라고만 해서는 스프링이 제공하는 기능의 특징을 명확하게 설명하지 못함

-> 스프링이 제공하는 IoC 방식을 핵심을 짚어주는 **의존관계 주입(Dependency Injection)**이라는, 좀 더 의도가 명확히 드러나는 이름을 사용하기 시작. 초기에는 주로 IoC 컨테이너라고 불리던 스프링이 지금은 의존관계 주입 컨테이너 or DI 컨테이너라고 불리고 있음.

> 의존관계 주입, 의존성 주입, 의존 오브젝트 주입?
>
> 'Dependency Injection'은 여러 가지 우리말로 변연돼서 사용됨. DI의 핵심은 **오브젝트 레퍼런스를 외부로부터 제공(주입) 받고 이를 통해 여타 오브젝트와 다이내믹하게 의존관계가 만들어지는 것** . 용어는 동작방식(메커니즘)보다는 의도를 가지고 이름 짓는 것이 좋다는 면에서 의존관계 주입이라는 번역이 적절할 듯 싶다.

</br>

### 1.7.2 런타임 의존관계 설정

#### 의존관계

두 개의 클래스 또는 모듈이 의존관계에 있다고 말할 때는 항상 **방향성**을 부여해야 함. 즉 누가 누구에게 의존하는 관계에 있다는 식이어야 함.

그렇다면 의존하고 있다는 건 무슨 의미일까? (A가 B에 의존하고 있다 하자)

의존한다는 건 의존대상(B)이 변하면 그것이 A에 영향을 미친다는 뜻. B의 기능이 추가되거나 변경되거나, 형식이 바뀌거나 하면 그 영향이 A로 전달된다는 것. (예를 들면 A에서 B에 정의된 메소드를 호출해서 사용하는 경우) 

반대로 B는 A에 의존하지 않는다. 의존하지 않는다는 말은 B는 A의 변화에 영향을 받지 않는다는 뜻

#### UserDao의 의존관계

UserDao는 ConnectionMaker에 의존하고 있는 형태임. ConnectionMaekr 인터페이스가 변한다면 그 영향을 UserDao가 직접적으로 받게 됨. 

하지만 ConnectionMaker 인터페이스를 구현한 클래스, 즉 DConnectionMaker 등이 다른 것으로 바뀌거나 그 내부에서 사용하는 메소드에 변화가 생겨도 UserDao에 영향을 주지 않음.

이렇게 <u>인터페이스에 대해서만 의존관계를 만들어두면 인터페이스 구현 클래스와의 관계는 느슨해지면서 변화에 영향을 덜 받는 상태</u>가 됨. 결합도가 낮다고 설명할 수 있음. 인터페이스를 통해 의존관계를 제한해주면 그만큼 변경에서 자유로워지는 셈이다.

UML에서 말하는 의존관계란 설계 모델의 관점에서 이야기하는 것. 그런데 모델이나 코드에서 클래스와 인터페이스를 통해 드러나는 의존관계 말고, 런타임 시에 오브젝트 사이에서 만들어지는 의존관계도 있음(**런타임 의존관계**). 설계 시점의 의존관계가 실체화된 것이라고 볼 수 있음.

인터페이스를 통해 설계 시점에 느슨한 의존관계를 갖는 경우에는 UserDao의 오브젝트가 런타임 시에 사용할 오브젝트가 어떤 클래스로 만든 것인지 미리 알 수가 없다. 프로그램이 시작되고 UserDao 오브젝트가 만들어지고 나서 런타임 시에 의존관계를 맺는 대상, 즉 실제 사용대상인 오브젝트를 의존 오브젝트(dependent object)라고 말한다.

<u>의존관계 주입은 이렇게 구체적인 의존 오브젝트와 그것을 사용할 주체, 보통 클라이언트라고 부르는 오브젝트를 런타임 시에 연결해주는 작업을 말함</u>.

정리하자면 **의존관계 주입이란 다음과 같은 세 가지 조건을 충족하는 작업** 을 말한다.

- 클래스 모델이나 코드에는 런타임 시점의 의존관계가 드러나지 않는다. 그러기 위해서는 인터페이스만 의존하고 있어야 한다.
- 런타임 시점의 의존관계는 컨테이너나 팩토리 같은 제 3의 존재가 결정한다.
- 의존관계는 사용할 오브젝트에 대한 레퍼런스를 외부에서 제공(주입)해줌으로써 만들어진다.

의존관계 주입의 핵심은 설계 시점에는 알지 못했던 두 오브젝트의 관계를 맺도록 도와주는 제3의 존재가 있다는 것. DI에서 말하는 제 3의 존재는 바로 관계설정 책임을 가진 코드를 분리해서 만들어진 오브젝트라고 볼 수 있음. 전략 패턴에 등장하는 클라이언트, DaoFacotry, 스프링의 애플리케이션 컨텍스트, 빈 팩토리, IoC 컨테이너 등이 모두 외부에서 오브젝트 사이의 런타임 관계를 맺어주는 책임을 지닌 제 3의 존재라고 볼 수 있음.

#### UserDao의 의존관계 주입

인터페이스를 사이에 두고 UserDao와 ConnectionMaker 구현 클래스 간에 의존관계를 느슨하게 만들긴 했지만, UserDao가 사용할 구체적인 클래스를 알고 있어야 한다는 문제가 남았음.

관계설정의 책임을 분리하기 전에 UserDao 클래스의 생성자는 다음과 같았다.

```java
public UserDao() {
  connectionMaker = new DConnectionMaker();
}
```

UserDao는 이미 설계 시점에서 DConnectionMaker라는 구체적인 클래스의 존재를 알고 있음. 모델링 때의 의존관계(ConnectionMaker 인터페이스의 관계) 뿐 아니라 런타임 의존관계(DConnectionMaker 오브젝트를 사용하겠다는 것)까지 UserDao가 결정하고 관리하고 있는 셈.

런타임 시의 의존관계가 코드 속에 다 미리 결정되어 있다는 점이 문제. IoC 방식을 써서 UserDao로부터 런타임 의존관계를 드러내는 코드를 제거하고, 제 3의 존재에 런타임 의존관계 결정 권한을 위임해야 한다. 그래서 최종적으로 만들어진 것이 DaoFactory였음. DaoFactory는 런타임 시점에 UserDao가 사용할 ConnectionMaker 타입의 오브젝트를 결정하고 이를 생성한 후에 UserDao의 생성자 파라미터로 주입해서 UserDao가 DConnectionMaker의 오브젝트와 런타임 의존관계를 맺게 해줌. 따라서 의존관계 주입의 세 가지 조건을 모두 충족한다고 볼 수 있고, 이미 DaoFactory를 만든 시점에서 DI를 이용한 셈.

런타임 시점의 의존관계를 결정하고 만들려면 제 3의 존재가 필요하다고 했다. DaoFactory가 그 역할을 담당한다고 해보자. 

- DaoFactory는 두 오브젝트 사이의 런타임 의존관계를 설정해주는 의존관계 주입 작업을 주도하는 존재이며,
-  동시에 IoC 방식으로 오브젝트의 생성과 초기화, 제공 등의 작업을 수행하는 컨테이어다. 
- 따라서 의존관계 주입을 담당하는 컨테이너라고 볼 수 있고, 줄여서 DI 컨테이너라고 불러도 된다. 

DI 컨테이너는 UserDao를 만드는 시점에서 생성자의 파라미터로 이미 만들어진 DConnectionMaker의 오브젝트를 전달함. 주입이라는 건 외부에서 내부로 무엇인가를 넘겨줘야 하는 것인데, 자바에서 오브젝트에 무엇인가를 넣어준다는 개념은 메소드를 실행하면서 파라미터로 오브젝트의 레퍼런스를 전달해주는 방법뿐이다. 가장 손쉽게 사용할 수 있는 파라미터 전달이 가능한 메소드는 바로 생성자다.

DI 컨테이너는 자신이 결장한 의존관계를 맺어줄 클래스의 오브젝트를 만들고 이 생성자의 파라미터로 오브젝트의 레퍼런스를 전달해줌. 이렇게 생성자 파라미터를 통해 전달받은 런타임 의존관계를 갖는 오브젝트는 인스턴스 변수에 저장

```java
public class UserDao {
  private ConnectionMaker connectionMaker;
  
  public UserDao(ConnectionMaker connectionMaker) {
    this.connectionMaekr = connectionMaker;
  }
  ...
}
```

이렇게 해서 두 개의 오브젝트 간에 런타임 의존관계가 만들어졌다. UserDao 오브젝트는 이제 생성자를 통해 주입받은 DConnectionMaker 오브젝트를 언제든지 사용하면 된다.

이렇게 DI 컨테이너에 의해 런타임 시에 의존 오브젝트를 사용할 수 있도록 그 레퍼런스를 전달받는 과정이 마치 메소드(생성자)를 통해 DI 컨테이너가 UserDao에게 주입해 주는 것과 같다고 해서 이를 의존관계 주입이라고 한다.

DI는 자신이 사용할 오브젝트에 대한 선택과 생성 제어권을 외부로 넘기고 자신은 수동적으로 주입받은 오브젝트를 사용한다는 점에서 IoC의 개념에 잘 들어맞는다. 스프링 컨테이너의 IoC는 주로 의존관계 주입 또는 DI라는 데 초점이 맞춰져 있다. 그래서 스프링을 IoC 컨테이너 외에도 DI 컨테이너 또는 DI 프레임워크라고 부르는 것이다.

</br>

### 1.7.3 의존관계 검색과 주입

스프링이 제공하는 IoC 방법에는 의존관계 주입만 있는 것이 아니다. 의존관계를 맺는 방법이 외부로부터의 주입이 아니라 스스로 검색을 이용하기 때문에 **의존관계 검색(dependency lookup)** 이라고 불리는 것도 있다. 의존관계 검색은 <u>런타임 시 의존관계를 맺을 오브젝트를 결정하는 것과 오브젝트의 생성 작업은 외부 컨테이너에게 IoC로 맡기지만, 이를 가져올 때는 메소드나 생성자를 통한 주입 대신 스스로 컨테이너에게 요청하는 방법</u>을 사용

```java
public UserDao() {
  DaoFactory daoFactory = new DaoFactory();
  this.connectionMaker = daoFactory.connectionMaker();
}
```

이렇게 해도 UserDao는 여전히 자신이 어떤 ConnectionMaker 오브젝트를 사용할지 미리 알지 못함. 여전히 의존대상은 ConnectionMaker 인터페이스뿐임. IoC 개념을 잘 따르고 있음. 하지만 외부로부터의 주입이 아니라 스스로 IoC 컨테이너인 DaoFactory에게 요청하는 것임. 

이런 작업을 일반화한 스프링의 애플리케이션 컨텍스트라면 미리 정해놓은 이름을 전달해서 그 이름에 해당하는 오브젝트를 찾게 된다. 이를 일종의 검색이라고 볼 수 잇다. 또한 그 대상이 런타임 의존관계를 가질 오브젝트이므로 의존관계 검색이라고 부르는 것임.

스프링의 IoC 컨테이너인 애플리케이션 컨텍스트는 getBean()이라는 메소드를 제공함으로써 의존관계 검색 기능을 제공한다.

```java
public UserDao() {
  AnnotationConfigApplicationContext context = 
    new AnnotationConfigApplicationContext(DaoFactory.class);
  this.connectionMaker = context.getBean("connectionMaker", ConnectionMaker.class);
}
```

의존관계 검색은 기존 의존관계 주입의 거의 모든 장점을 갖고 있다. IoC 원칙에도 잘 들어맞는다.

**의존관계 검색과 의존관계 주입 방법 중 어떤 것이 더 나을까?**

-> **의존관계 주입 쪽이 훨씬 단순하고 깔끔함** 의존관계 검색 방법은 코드 안에 오브젝트 팩토리 클래스나 스프링 API가 나타남. 애플리케이션 컴포넌트가 컨테이너와 같이 성격이 다른 오브젝트에 의존하게 되는 것이므로 바람직하지 않음.

**의존관계 검색 방식을 사용해야 할 때도 있다.** 

-> 스프링의 IoC와 DI 컨테이너를 적용했다고 하더라도 애플리케이션의 기동 시점에서 적어도 한 번은 의존관계 검색 방식을 사용해 오브젝트를 가져와야 함. 스태틱 메소드인 main() 에서는 DI를 이용해 오브젝트를 주입받을 방법이 없기 때문. 서버에서도 마찬가지. 사용자의 요청을 받을 때마다 main() 메소드와 비슷한 역할을 하는 서블릿에서 스프링 컨테이너에 담긴 오브젝트를 사용하려면 한 번은 의존관계 검색 방식을 사용해 오브젝트를 가져와야 한다. (이러한 서블릿은 스프링에서 이미 제공)

**의존관계 검색과 의존관계 주입의 중요한 차이점.** 

- 의존관계 검색 방식에서는 검색하는 오브젝트는 자신이 스프링 빈일 필요가 없다는 점.
- 반면에 의존관계 주입에서는 DI를 원하는 오브젝트는 먼저 자기 자신이 컨테이너가 관리하는 빈이 돼야 함.
  - 컨테이너가 UserDao에 ConnectionMaker 오브젝트를 주입해주려면 UserDao에 대한 생성과 초기화 권한을 갖고 있어야하고, 그러려면 UserDao 역시 빈이어야하기 때문.

> DI 받는다?
>
> - 단지 외부에서 파라미터로 오브젝트를 주입해줬다고 해서 다 DI가 아니다.
> - DI에서 말하는 주입은 다이내믹하게 구현 클래스를 결정해서 제공받을 수 있도록 **인터페이스 타입**의 파라미터를 통해 이뤄져야 함.
> - 때문에 DI 원리를 지키며 외부에서 오브젝트를 제공받을 방법을 단순히 '주입 받는다'라고 하는 대신 'DI 받는다'라고 표현. DI 개념을 따르는 주입임을 강조하기 위함

</br>

### 1.7.4 의존관계 주입의 응용

런타임 시에 사용 의존관계를 맺을 오브젝트를 주입해준다는 DI 기술의 장점은 무엇일까?

- 앞서 설명한 모든 객체지향 설계와 프로그래밍 원칙을 따랐을 때 얻을 수 있는 장점이 DI 기술에도 적용됨
  - 코드에 런타임 클래스에 대한 의존관계 나타나지 않음
  - 인터페이스를 통해 결합도가 낮은 코드
  - 의존관계에 있는 대상이 변경되더라도 자신은 영향 받지 않음
  - 다양한 확장에 자유로움

스프링의 각종 기술은 모두 다 이 DI 없이는 불가능한 것들임. DI 없이는 스프링도 없다. DI를 활용할 방법도 다양하다.

몇 가지 응용 사례를 생각해보자.

#### 기능 구현의 교환

개발 중에는 개발자 PC에 설치한 로컬 DB를 사용해야한다고 해보자. 개발이 진행되다가 어느 시점이 되면 지금까지 개발한 것을 그대로 운영서버로 배치해서 사용할 것이다. 

그런데 만약 DI 방식을 적용하지 않았다면?

- 개발 중에는 로컬 DB에 대한 연결 기능이 있는 LocalDBConnectionMaker라는 클래스를 만들고, 모든 DAO에서 이 클래스의 오브젝트를 매번 생성해서 사용하게 했을 것임.

- 그런데 서버에 배포할 때는 다시 서버가 제공하는 특별한 DB 연결 클래스를 사용해야 함. 
- DI를 안했으니 모든 DAO 코드에서 LocalDBConnectionMaker에 의존하고 있는 것을 운영서버에서 DB 연결할 때 필요한 ProductionDBConnectionMaker라는 클래스로 변경해줘야 함. 
- DAO가 100개라면 최소 100군데 코드를 수정해야 함. 다시 개발을 진행하게 되면 또 다시 100군데 코드를 수정... 끔찍

반면 DI 방식을 적용한다면?

- 모든 DAO는 생성 시점에 ConnectionMaker 타입의 오브젝트를 컨테이너로부터 제공 받음.

- 구체적인 사용 클래스 이름은 컨테이너가 사용할 설정정보에 있다.

  ```java 
  @Configuration
  public DaoFactory {
  	 @Bean
  	 public ConnectionMaker connectionMaker(){
    	return new LocalDBConnectionMaker();
  	 } 
  }
  ```

- 서버에 배포할 때는 DaoFacotry에서 connectionMaker()에 구현 클래스만 ProductionDBConnectionMaker()로 변경 해주면 됨
- 딱 한줄이면 된다!

DI의 설정정보만 다르게 만들어 두면 나머지 코드에는 전혀 손대지 않고 개발 시와 운영 시에 각각 다른 런타임 오브젝트에 의존관계를 갖게 해줘서 문제를 해결할 수 있다.

#### 부가기능 추가

DAO가 DB를 얼마나 많이 연결해서 사용하는지 파악하고 싶다고 해보자. DB 연결 횟수를 카운팅하기 위해 모든 DAO의 makeConnection() 메소드를 호출하는 부분에 새로 추가한 카운터를 증가시키는 코드를 작성해야할까? 낭비, 노가다일 뿐만아니라 DB 연결 횟수를 세는 일은 DAO의 관심사항이 아니기 때문에 분리되어야 함.

DI 컨테이너에서라면 아주 간단한 방법으로 가능하다. DAO와 DB 커넥션을 만드는 오브젝트 사이에 연결 횟수를 카운팅하는 오브젝트를 하나 더 추가하는 것이다. 당연히 기존 코드는 수정하지 않아도 된다. 그리고 컨테이너가 사용하는 설정정보만 수정해서 런타임 의존관계만 새롭게 정의해주면 된다.

[CountingConnectionMaker.java]

```java
package springbook.user.dao;
...

public class CountingConnectionMaker implements ConnectionMaker {

    int counter = 0;
    private ConnectionMaker realConnectionMaker;

    public CountingConnectionMaker(ConnectionMaker realConnectionMaker) {
        this.realConnectionMaker = realConnectionMaker;
    }

    @Override
    public Connection makeConnection() throws ClassNotFoundException, SQLException {
        this.counter++;

        return realConnectionMaker.makeConnection();
    }

    public int getCounter() {
        return this.counter;
    }
}

```

중요한 것은 ConnectionMaker 인터페이스를 구현해서 만든다는 점이다. DAO가 대상이될 것이기 때문이다.

CountingConnectionMaker 클래스는 Connection 인터페이스를 구현했지만 내부에서 직접 DB 커넥션을 만들지 않는다. 대신 DAO가 DB 커넥션을 가져올 때마다 호출하는 makeConnection()에서 DB 연결 횟수 카운터를 증가시킨다. CountingConnectionMaker는 자신의 관심사인 DB 연결 횟수 카운팅 작업을 마치면 실제 DB 커넥션을 만들어주는 realConnectionMaker에 저장된 ConnectionMaker 타입 오브젝트의 makeConnection()을 호출해서 그 결과를 DAO에게 돌려준다.

생성자를 보면 CountingConnectionMaker도 DI를 받는 것을 알 수 있다.

CountingConnectionMaker가 추가되면서 런타임 의존관계가 바뀌었다. UserDao 오브젝트가 DI 받는 대상의 설정을 조정해서 DConnection 오브젝트 대신 CountingConnectionMaker 오브젝트로 바꿔치기하는 것이다. 이렇게 해두면 UserDao가 DB 커넥션을 가져오려고 할 때마다 CountinConnectionMaker의 makeConnection() 메소드가 실행되고 카운터는 하나씩 증가한다. 이후 DAO 본래의 기능이 동작하도록 CountingConnectionMaker가 다시 실제 사용할 DB 커넥션을 제공해주는 DConnectionMaker를 호출하도록 한다.

새로운 의존관계를 컨테이너가 사용할 설정정보를 이용해 만들어보자. 

- 기존 DaoFactory와 달리 connectionMaker() 메소드에서 CountingConnectionMaker 타입 오브젝트를 생성하도록 만든다.
- 실제 DB 커넥션을 만들어주는 DConnectionMaker는 이름이 realConnectionMaker()인 메소드에서 생성하게 한다.
- realConnectionMaker() 메소드가 만들어주는 오브젝트는 connectionMaker()에서 만드는 오브젝트의 생성자를 통해 DI 해준다.

[CountingDaoFactory.java]

```java
package springbook.user.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CountingDaoFactory {

    @Bean
    public UserDao userDao() {
        return new UserDao(connectionMaker());
    }

    @Bean
    public ConnectionMaker connectionMaker() {
        return new CountingConnectionMaker(realConnectionMaker());
    }

    @Bean
    public ConnectionMaker realConnectionMaker() {
        return new DConnectionMaker();
    }
}

```

이제 커넥션 카운팅을 위한 실행 코드를 만든다.

- UserDaoTest와 기본적으로 같지만 설정용 클래스를 CountingDaoFactory로 변경해줘야 한다.
- DAO를 DL 방식으로 가져와 여러 번 실행시킴.
- CountingConnectionMaker 빈을 가져온다.
- CountingConnectionMaker에는 그동안 DAO를 통해 DB 커넥션을 요청한 횟수만큼 카운터가 증가해 있어야 한다.

[UserDaoConnectionCountingTest.java]

```java
package springbook.user;

...

public class UserDaoConnectionCountingTest {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        ApplicationContext context = new AnnotationConfigApplicationContext(CountingDaoFactory.class);
        UserDao dao = context.getBean("userDao", UserDao.class);

        for (int i = 0; i < 4; i++) { // user 생성을 4번 실행 시킴
            User user = new User();
            user.setId("id" + i);
            user.setName("name" + i);
            user.setPassword("1234");

            dao.add(user);
        }

        CountingConnectionMaker ccm = context.getBean("connectionMaker", CountingConnectionMaker.class);
        System.out.println("Connection counter : " + ccm.getCounter());
      	// 결과 : "Connection counter : 4"
    }
}
```

DI의 장점은 관심사의 분리를 통해 얻어지는 높은 응집도에서 나온다. 모든 DAO가 직접 의존해서 사용할 ConnectionMaekr 타입 오브젝트는 connectionMaker() 메소드에서 만들기 때문에 CountingConnectionMaker의 의존관계를 추가하려면 이 메소드만 수정하면 됨. 또한 CountingDaoFactory 설정 클래스를 DaoFactory로 변경하거나 connectionMaker() 메소드를 수정하는 것만으로 DAO의 런타임 의존관계는 이전 상태로 복구됨.

스프링이 제공하는 대부분의 기능은 DI 없이는 존재할 수도 없는 것들이다. 스프링은 DI를 편하게 사용할 수 있도록 도와주는 도구이면서 그 자체로 DI를 적극 활용한 프레임워크이기도 하다. 그래서 스프링을 공부하는 건 DI를 어떻게 활용해야 할지를 공부하는 것이기도 하다.

</br>

### 1.7.5 메소드를 이용한 의존관계 주입

지금까지는 UserDao의 의존관계 주입을 위해 생성자를 사용했다. 그런데 의존관계 주입 시 반드시 생성자를 사용해야 하는 것은 아니다. 생성자가 아닌 일반 메소드를 이용해 의존관계를 주입하는 데는 크게 두 가지 방법이 있다.

- 수정자 메소드를 이용한 주입
  - 수정자(setter) 메소드는 외부에서 오브젝트 내부의 애트리뷰트 값을 변경하려는 용도로 주로 사용.
  - 메소드는 항상 set으로 시작
  - 파라미터로 전달된 값을 보통 내부의 인스턴스 변수에 저장하는 것
  - 부가적으로, 입력 값에 대한 검증이나 그 밖에 작업을 수행할 수 있음

- 일반 메소드를 이용한 주입
  - 여러 개의 파라미터를 갖는 일반 메소드를 DI로 사용할 수 있음.
  - 적절한 개수의 파라미터를 가진 여러 개의 초기화 메소드를 만들 수도 있기 때문에 한 번에 모든 필요한 파라미터를 다 받아야 하는 생성자보자 낫다.

스프링은 전통적으로 수정자 메소드를 이용한 DI 방법을 가장 많이 사용해 왔다. DaoFactory 같은 자바 코드 대신 XML을 사용하는 경우에는 자바빈 규약을 따르는 수정자 메소드가 가장 사용하기 편리했기 때문.

실제로 스프링은 생성자, 수정자 메소드, 초기화 메소드를 이용한 방법 외에도 다양한 의존관계 주입 방법을 지원하는데, 자세한 내용은 Vol.2에서 다룰 것임.