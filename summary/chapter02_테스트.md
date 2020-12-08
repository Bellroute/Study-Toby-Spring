# 2장 테스트

스프링이 개발자에게 제공하는 가장 중요한 가치가 무엇이냐고 질문한다면 **객체지향** 과 **테스트** 라고 대답할 것이다. 스프링은 IoC/DI를 이용해 객체지향 프로그래밍 언어의 근본과 가치를 개발자가 손쉽게 적용하고 사용할 수 있는 기술. 동시에 복잡한 엔터프라이즈 애플리케이션을 효과적으로 개발하기 위한 기술. 

이러한 복잡한 애플리케이션을 개발하는 데 필요한 도구 하나는 객체지향 기술이고 다른 하나의 도구는 스프링이 강조하고 가치를 두고 있는 테스트이다.

애플리케이션은 계속 변하고 복잡해져 간다. 변화에 대응하는

- 첫 번째 전략이 확장과 변화를 고려한 객체지향적 설계와 그것을 효과적으로 담을 수 있는 IoC/DI 같은 기술이라면,
- 두 번째 전략은 <u>만들어진 코드를 확신할 수 있게 해주고, 변화에 유연하게 대처할 수 있는 자신감을 주는</u> 테스트 기술이다.

테스트는 스프링을 학습하는 데 있어 가장 효과적인 방법의 하나임. 테스트의 작성은 스프링의 다양한 기술을 활용하는 방법을 이해하고 검증하고, 실전에 적용하는 방법을 익히는 데 효과적으로 사용될 수 있다.

→ 2장에서는 테스트란 무엇이며, 그 가치와 장점, 활용 전략, 스프링과의 관계를 살펴봄. + 테스트 프레임워크와 이를 활용한 학습 전략

---

#### [INDEX]

**2.1 [UserDaoTest 다시 보기](https://github.com/Bellroute/Study-Toby-Spring/blob/master/summary/chapter02_%ED%85%8C%EC%8A%A4%ED%8A%B8.md#21-userdaotest-%EB%8B%A4%EC%8B%9C-%EB%B3%B4%EA%B8%B0)**

**2.2. [UserDaoTest 개선](https://github.com/Bellroute/Study-Toby-Spring/blob/master/summary/chapter02_%ED%85%8C%EC%8A%A4%ED%8A%B8.md#22-userdaotest-%EA%B0%9C%EC%84%A0)**

**2.3 [개발자를 위한 테스팅 프레임워크 JUnit](https://github.com/Bellroute/Study-Toby-Spring/blob/master/summary/chapter02_%ED%85%8C%EC%8A%A4%ED%8A%B8.md#23-%EA%B0%9C%EB%B0%9C%EC%9E%90%EB%A5%BC-%EC%9C%84%ED%95%9C-%ED%85%8C%EC%8A%A4%ED%8C%85-%ED%94%84%EB%A0%88%EC%9E%84%EC%9B%8C%ED%81%AC-junit)**

</br>

## 2.1 UserDaoTest 다시 보기

### 2.1.1 테스트의 유용성

1장에서 만든 UserDao가 기대했던 대로 동작하는지 확인하기 위해 간단한 테스트 코드를 만들었다. 테스트용 main() 메소드를 반복적으로 실행해가면서 처음 설계한 대로 기능이 동작하는지를 매 단계 확인한 덕분에, 다양한 방법으로 초난감 UserDao 코드의 설계와 코드를 개선했고, 심지어 스프링을 적용해서 동작하게 만들 수도 있었다. 

테스트를 통해 확인할 수 없었다면, 그 코드를 개선하는 과정 내내 뭔가 꺼림칙하고 불안했을 것이다. 코드를 들여다보며 머릿속으로 시뮬레이션하는 방법이 있긴 하지만 그것만으로는 100% 확신할 수 없다.

테스트란 결국 **내가 예상하고 의도했던 대로 코드가 정확히 동작하는지를 확인해서, 만든 코드를 확신할 수 있게 해주는 작업** 이다. 테스트 결과가 원하는 대로 나오지 않은 경우, 코드나 설계에 결함이 있음을 알고 디버깅을 거치게 되고, 결국 최종적으로 테스트가 성공하면 모든 결함이 제거됐다는 확신을 얻을 수 있다.

</br>

### 2.1.2 UserDaoTest 특징

1장에서 만들었던 main() 메소드로 작성된 테스트 코드를 다시 한번 살펴보자.

```java
public class UserDaoTest {

    public static void main(String[] args) throws SQLException {
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao dao = context.getBean("userDao", UserDao.class);

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

이 테스트 코드의 내용을 정리하면 다음과 같다.

- 자바에서 가장 쉽게 실행 가능한 main() 메소드를 이용함
- 테스트할 대상인 UserDao의 오브젝트를 가져와 메소드를 호출함
- 테스트에 사용할 입력 값(User 오브젝트)을 직접 코드에서 만들어 넣어줌
- 테스트의 결과를 콘솔에 출력함
- 각 단계의 작업이 에러 없이 끝나면 콘솔에 성공 메시지로 출력함

가장 돋보이는 건, main() 메소드를 이용해 테스트 수행을 가능하게 했다는 점과 테스트할 대상인 UserDao를 직접 호출해서 사용한다는 점

#### 웹을 통한 DAO 테스트 방법의 문제점

보통 웹 프로그램에서 사용하는 DAO를 테스트하는 방법은 다음과 같다. 

DAO를 만든 뒤, 서비스 계층, MVC 프레젠테이션 계층까지 포함한 모든 입출력 기능을 대충이라도 코드로 다 만든다. 이렇게 만들어진 테스트용 웹 애플리케이션을 서버에 배치한 뒤......(이하 생략)

이렇게 웹 화면을 통해 값을 입력하고, 기능을 수행하고, 결과를 확인하는 방법은 DAO에 대한 테스트로서는 단점이 너무 많음. DAO 뿐만 아니라 필요한 모든 레이어의 기능을 다 만들고 나서야 테스트가 가능하다. 테스트 도중 에러가 나거나 실패하면 어디에서 문제가 발생했는지를 찾아내야 하는 수고도 필요함.

→ **하나의 테스트를 수행하는 데 참여하는 클래스와 코드가 너무 많기 때문.** 테스트하고 싶었던 건 UserDao였는데 다른 계층의 코드와 컴포넌트, 심지어 서버의 설정 상태까지 모두 테스트에 영향을 줄 수 있기 때문에 이런 방식의 테스트는 번거롭고, 오류가 있을 때 빠르고 정확한 대응이 어렵다.

#### 작은 단위의 테스트

테스트하고자 하는 대상이 명확하다면 그 대상에만 집중해서 테스트하는 것이 바람직함. 가능하면 작은 단위로 쪼개서 집중해서 할 수 있어야 함. 관심사의 분리라는 원리가 여기에도 적용 됨. **테스트의 관심이 다르다면 테스트할 대상을 분리하고 집중해서 접근해야 함.**

UserDaoTest는 한 가지 관심에 집중할 수 있게 작은 단위로 만들어진 테스트다. 간단히 IDE나 도스창에서도 테스트 수행이 가능함. 에러가 나거나 원치 않는 결과가 나온다면, UserDao 코드나 DB 연결 방법 정도에서 문제가 있는 것이니 원인을 빠르게 찾아낼 수 있음.

이렇게 작은 단위의 코드에 대해 테스트를 수행한 것을 **단위 테스트(unit test)** 라고 한다. 

- 여기서 말하는 단위는 크기와 범위가 딱 정해진 것이 아니라 충분히 하나의 관심에 집중해서 효율적으로 테스트할 만한 범위의 단위라고 보면 된다.

- 단위는 작을수록 좋다. 단위를 넘어서는 코드들은 신경 쓰지 않고, 참여하지도 않고 테스트가 동작할 수 있으면 좋다.
- 통제할 수 없는 외부의 리소스에 의존하는 테스트는 단위 테스트가 아니라 보기도 한다. 
  - DB의 상태가 매번 달라지고, 테스트를 위해 DB를 특정 상태로 만들어줄 수 없다면 UserDaoTest는 단위 테스트로서 가치가 없어짐.

때로는 각 단위 기능은 잘 동작하는데 묶어놓으면 안 되는 경우가 종종 발생하기 때문에, 웹 사용자 인터페이스로부터 시작해 DB에 이르기까지 애플리케이션 전 계층이 참여하는 등 길고 많은 단위가 참여하는 테스트도 언젠가는 필요함.

<u>단위 테스트 없이 긴 테스트만 하게 되면</u> 수많은 에러를 만나거나 에러는 안 나지만 제대로 기능이 동작하지 않는 경험을 하게 될 것이다. 이 때는 <u>문제의 원인을 찾기가 매우 힘들다.</u> 만약 각 단위별로 테스트를 먼저 진행하고 나서 이런 긴 테스트를 시작했다면 역시 예외가 발생하거나 테스트가 실패하더라도, 이미 각 단위별로 충분한 검증을 마치고 오류를 잡았으므로 훨씬 나을 것이다.

단위 테스트를 하는 이유는 개발자가 설계하고 만든 코드가 원래 의도한 대로 동작하는지를 개발자 스스로 빨리 확인받기 위해서다. 테스터나 고객이 테스트를 하는 시점이면 이미 개발자가 코드를 작성하고 나서 한참 뒤일 것. 그때서야 오류가 처음 발견되고 개발자에게 문제가 통보된다면 개발자는 오래 전에 만든 코드를 뒤져서 버그를 수정해야 한다.

#### 자동수행 테스트 코드

UserDaoTest의 한 가지 특징은 테스트할 데이터가 코드를 통해 제공되고, 테스트 작업 역시 코드를 통해 자동으로 실행한다는 점이다. User 오브젝트를 만들어 적절한 값을 넣고, 이미 DB 연결 준비까지 다 되어 있는 UserDao 오브젝트를 스프링 컨테이너에서 가져와서 add() 메소드를 호출하고, 그 키 값으로 get()을 호출하는 것까지 자동으로 진행됨. 번거롭게 매번 입력할 필요도 없고, 테스트를 시작하기 위해 서버를 띄우고, 브라우저를 열어야 하는 불편함도 없다.

이렇게 테스트는 자동으로 수행되도록 코드로 만들어지는 것이 중요하다. (어떤 개발자는 모든 클래스는 스스로 자신을 테스트하는 main() 메소드를 가지고 있어야 한다고 주장하기도 하지만, 애플리케이션을 구성하는 클래스 안에 테스트 코드를 포함시키는 것보다는 별도로 테스트용 클래스를 만들어서 테스트 코드를 넣는 편이 낫다.)

자동으로 수행되는 테스트의 장점은 자주 반복할 수 있다는 것이다. 번거로운 작업없이 빠르게 실행할 수 있기 때문에 언제든 코드를 수정하고 나서 테스트를 해볼 수 있다.

#### 지속적인 개선과 점진적인 개발을 위한 테스트

초난감 DAO 코드를 스프링을 이용한 완성도 높은 객체지향적 코드로 발전시키는 과정의 일등 공신은 테스트였다.

**테스트가 없었다면, 다양한 방법을 동원해서 코드를 수정하고 설계를 개선해나가는 과정이 그다지 미덥지 않을 수도 있고, 그래서 마음이 불편해지면 이쯤에서 그만두자는 생각이 들 수도 있기 때문.**

또 UserDao의 기능을 추가하려고 할 때도 미리 만들어둔 테스트코드는 유용하게 쓰일 수 있다. 가장 단순한 등록과 조회 기능을 만들고 , 이를 테스트로 검증해서 만든 코드에 대한 확신을 갖는다. 그리고 거기에 조금씩 기능을 추가해가면서 그에 대한 테스트도 함께 추가하는 식으로 점진적인 개발이 가능해진다.

테스트를 이용하면 **새로운 기능도 기대한 대로 동작하는지 확인할 수 있을 뿐 아니라, 기존에 만들어뒀던 기능들이 새로운 기능을 추가하느라 수정한 코드에 영향을 받지 않고 여전히 잘 동작하는지를 확인** 할 수 도 있다.

</br>

### 2.1.3 UserDaoTest의 문제점

- **수동 확인 작업의 번거로움**
  - add()에서 User 정보를 DB에 등록하고, 이를 다시 get()을 이용해 가져왔을 때 입력한 값과 가져온 값이 일치하는지를 테스트 코드는 확인해주지 않음
  - 단지 콘솔에 값만 출력해줄 뿐. 그 콘솔 값을 보고 확인하는 건 사람의 책임
  - 테스트 수행은 코드에 의해 자동으로 진행되지만 테스트의 결과를 확인하는 일은 사람의 책임 → 완전한 자동 테스트 x
- **실행 작업의 번거로움**
  - 간단히 실행 가능한 main() 메소드라도 매번 실행하는 것은 번거로움
  - 수백 개의 DAO가 있고 그에 대한 수백 개의 main() 메소드가 만들어진다면 메소드를 수백 번 실행하는 수고가 필요
  - 이를 종합해서 전체 기능에 대한 테스트 결과를 정리하는 것도 큰 작업이 됨

</br>

## 2.2 UserDaoTest 개선

UserDaoTest의 두 가지 문제점을 개선해보자.

### 2.2.1 테스트 검증의 자동화

모든 테스트는 다음과 같은 결과를 가질 수 있다.

- 성공
- 실패
  - 테스트가 진행되는 동안 에러가 발생하는 경우
  - 결과가 기대한 것과 다르게 나오는 경우

테스트 중에 에러가 발생하는 것은 쉽게 확인 가능. 하지만 테스트가 실패하는 것은 별도의 확인 작업과 그 결과가 있어야만 알 수 있다.

기존의 테스트 코드에서는 get()에서 가져온 결과를 사람이 눈으로 확인하도록 단순히 콘솔에 출력하기만 했다면, 이번에는 테스트 코드에서 결과를 직접 확인하고, 기대한 결과와 달라서 실패했을 경우에는 "테스트 실패", 모든 확인 작업을 통과하면 "테스트 성공"이라고 출력하도록 하겠다.

[수정 전]

```java
System.out.println(user2.getName());
System.out.println(user2.getPassword());
System.out.println(user2.getId() + " 조회 성공");  
```

[수정 후]

```java
if (!user.getName().equals(user2.getName())) {
  System.out.println("테스트 실패 (name)");
}
else if (!user.getPassword().equals(user2.getPassword())) {
  System.out.println("테스트 실패 (password)");
}
else {
  System.out.println("조회 테스트 성공");
}
```

이렇게 해서 테스트의 수행과 테스트 값 적용, 그리고 결과를 검증하는 것까지 모두 자동화했다.

자동화된 테스트를 위한 xUnit 프레임워크를 만든 켄트 벡은 "테스트란 개발자가 마음 편하게 잠자리에 들 수 있게 해주는 것"이라고 했다. 짧은 시간에 화면에서 하는 수동 테스트로는 당장 수정한 기능의 가장 간단한 케이스를 확인하기에도 벅차기 때문에 전체 기능에 문제가 없는지 점검하는 것은 불가능에 가깝다. 하지만 만들어진 코드의 기능을 모두 점검할 수 있는 포괄적인 테스트(comprehensive test)를 만들면서부터는, 개발한 애플리케이션은 이후에 어떤 과감한 수정을 하고 나서도 테스트를 통해 코드의 기능 점검뿐 아니라 그 변경에 영향을 받는 부분이 정확히 확인된다면 빠르게 조치를 취할 수 있다.

이렇게 개발 과정 또는 유지보수를 하면서 기존 애플리케이션 코드에 수정을 할 때 마음의 평안을 얻고, 자신이 만지는 코드에 대해 항상 자신감을 가질 수 있으며, 새로 도입한 기술의 적용에 문제가 없는지 확인할 수 있는 가장 좋은 방법은 빠르게 실행 가능하고 스스로 테스트 수행과 기대하는 결과에 대한 확인까지 해주는 코드로 된 자동화된 테스트를 만들어두는 것

</br>

### 2.2.2 테스트의 효율적인 수행과 결과 관리

좀 더 편리하게 테스트를 수행하고 편리하게 결과를 확인하려면 단순한 main() 메소드로는 한계가 있다. 

- 일정한 패턴을 가진 테스트를 만들 수 있고, 
- 많은 테스트를 간단히 실행시킬 수 있으며, 
- 테스트 결과를 종합해서 볼 수 있고, 
- 테스트가 실패한 곳을 빠르게 찾을 수 있는 기능을 갖춘

테스트 지원 도구과 그에 맞는 테스트 작성 방법이 필요하다. 자바 테스팅 프레임워크라 불리는 JUnit은 이름 그대로 자바로 단위 테스트를 만들 때 유용하게 쓸 수 있다.

#### JUnit 테스트로 전환

지금까지 만들었던 main() 메소드 테스트를 JUnit을 이용해 다시 작성해보자. JUnit은 프레임워크로 개발자가 만든 클래스의 오브젝트를 생성하고 실행하는 일은 프레임워크에 의해 진행된다. 따라서 프레임워크에서 동작하는 코드는 main() 메소드도 필요 없고 오브젝트를 만들어서 실행시키는 코드를 만들 필요도 없다.

#### 테스트 메소드 전환

테스트가 main() 메소드 만들어졌다는 건 제어권을 직접 갖는다는 의미. 프레임워크에 적용하기엔 적합하지 않음. 그래서 가장 먼저 할 일은 main() 메소드에 있던 테스트 코드를 일반 메소드로 옮기는 것.

새로 만들 테스트 메소드는 JUnit 프레임워크가 요구하는 조건 두 가지를 따라야 함.

- 메소드가 public으로 선언돼야 함
- 메소드에 @Test라는 어노테이션을 붙여야 함

```java
import org.junit.Test;
...

public class UserDaoTest {

    @Test
    public void addAndGet() throws SQLException {
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao dao = context.getBean("userDao", UserDao.class);
      	...
    }
}
```

main() 대신 일반 메소드로 만들고 적절한 이름을 붙여준다. 테스트의 의도가 무엇인지 알 수 있는 이름이 좋다.

#### 검증 코드 전환

테스트의 결과를 검증하는 if/else 문장을 JUnit이 제공하는 방법을 이용해 전환해보자. UserDaoTest에서 if 문장의 기능을 JUnit이 제공해주는 assertThat이라는 스태틱 메소드를 이용해 다음과 같이 변경할 수 있다.

```java
// before
if (!user.getName().equals(user2.getName())) {...}

// after
assertThat(user2.getName(), is(user.getName()));
```

assertThat() 메소드는 첫 번째 파라미터의 값을 뒤에 나오는 매처(matcher)라고 불리는 조건으로 비교해서 일치하면 당므으로 넘어가고, 아니면 테스트가 실패하도록 만들어준다. is()는 매처의 일종으로 euqals()로 비교해주는 기능을 가졌다.

JUnit은 예외가 발생하거나 assertThat()에서 실패하지 않고 테스트 메소드의 실행이 완료된면 테스트가 성공했다고 인식한다. "테스트 성공"이라는 메시지를 굳이 출력할 필요 없이 다양한 방법으로 테스트 결과를 알려준다.

#### JUnit 테스트 실행

JUnit 프레임워크를 이용해 앞에서 만든 테스트 메소드를 실행하도록 코드를 만들어보자. 스프링 컨테이너와 마찬가지로 JUnit 도 어디선가 한 번은 시작시켜줘야 한다. 어디에든 main() 메소드를 하나 추가하고, 그 안에 JUnitCore 클래스의 main 메소드를 호출해주는 간단한 코드를 넣어주면 된다.

```java
import org.junit.runner.JUnitCore;
...
public static void main(String[] args) {
  JUnitCore.main("springbook.user.dao.UserDaoTest");
}
```

이 클래스를 실행하면 테스트를 실행하는 데 걸린 시간과 테스트 결과, 그리고 몇 개의 테스트 메소드가 실행됐는지를 알려준다. 만약 테스트가 실패하면 OK 대신 FAILURES라는 내용이 출력되고, 총 수행한 테스트 중에서 몇 개의 테스트가 실패했는지 보여준다.

JUnit은 assertThat()을 이용해 검증을 했을 때 기대한 결과가 아니면 AssertionError를 던진다. 따라서 assertThat()의 조건을 만족하지 못하면 테스트는 더 이상 진행되지 않고 JUnit은 테스트가 실패했음을 알게 된다. 테스트 수행 중에 일반 예외가 발생한 경우에도 마찬가지다.

</br>

## 2.3 개발자를 위한 테스팅 프레임워크 JUnit

JUnit은 사실상 자바의 표준 테스팅 프레임워크라고 불릴 만큼 폭넓게 사용되고 있음. 테스트 없이는 스프링도 의미 없다. 스프링 프레임워크 자체도 JUnit 프레임워크를 이용해 테스트를 만들어가며 개발 됐다. 스프링의 핵심 기능 중 하나인 스프링 테스트 모듈도 JUnit을 이용한다. 따라서 스프링의 기능을 익히기 위해서라도 JUnit은 꼭 사용할 줄 알아야 한다.

### 2.3.2. 테스트 실행 방법

JUnitCore를 이용해 테스트를 실행하고 결과를 확인하는 방법은 가장 간단하지만 테스트의 수가 많아지면 관리가 힘들어진다. 가장 좋은 방법은 자바 IDE에 내장된 JUnit 테스트 지원 도구를 사용하는 것

#### IDE

@Test가 들어 있는 테스트 클래스를 선택한 뒤에, 이클립스 run 메뉴의 Run As 항목 중에서 JUnit test를 선택하면 테스트가 자동으로 실행됨. JUnitCore 처럼 main() 메소드를 만들지 않아도 됨.

테스트가 시작되면 JUnit 테스트 정보를 표시해주는 뷰(view)가 나타나 진행 상황을 보여주고, 완료되면 테스트의 최종 결과가 깔끔하게 나타난다. 이 뷰에서 테스트의 총 수행시간, 실행한 테스트의 수, 에러의 수, 실패의 수 등을 확인할 수 있음.

JUnit은 한 번에 여러 테스트 클래스를 동시에 실행할 수도 있다. 이클립스에서는 소스 트리에서 특정 패키지를 선택하고 컨텍스트 메뉴의 [Run As] > [JUnit Test]를 실행하면, 해당 패키지 아래 모든 테스트를 한 번에 실행해준다. 소스 폴더나 프로젝트 전체를 대상으로도 가능함. 이런 면에서 JUnitCore를 사용하는 것보다 편리하다.

-> IDE의 지원을 받으면 JUnit 테스트의 실행과 결과를 확인하는 방법이 간단하고 직관적이며 소스와 긴밀하게 연동돼서 결과를 볼 수 있음.

#### 빌드 툴

빌드 툴에서 제공하는 JUnit 플러그인이나 태스크를 이용해 JUnit 테스트를 실행할 수 있다. 개발자 개인별로는 IDE에서 JUnit  도구를 활용해 테스트하는 게 가장 편리하지만, 여러 개발자가 만든 코드를 모두 통합해서 테스트를 수행해야하는 경우는 서버에서 모든 코드를 가져와 통합하고 빌드한 뒤에 테스트를 수행하는 것이 좋다.

</br>

### 2.3.2 테스트 결과의 일관성

지금까지 테스트를 실행하면서 가장 불편했던 일 == 매번 UserDaoTest 테스트를 실행하기 전에 DB의 USER 테이블 데이터를 모두 삭제해줘야 했던 것

테스트는 외부 상태에 따라 성공하기도 하고 실패하기도 함. 지금 발생하는 문제는 별도의 준비 작업 없이는 성공해야 마땅한 테스트가 실패하기도 한다는 점. 반복적으로 테스트를 했을 때 결과가 일관적이지 않으면 좋은 테스트라고 할 수 없음. 코드에 변경사항이 없다면 테스트는 항상 동일한 결과를 내야 한다.

UserDaoTest의 addAndGet() 테스트를 마치고 나면 테스트가 등록한 사용자 정보를 삭제해서, 테스트를 수행하기 이전 상태로 만들어 주면, 테스트를 여러 번 반복해 실행하더라도 항상 동일한 결과를 얻을 수 있다.

#### deleteAll()의 getCount() 추가

일관성 있는 결과 보장하는 테스트 작성 전, UserDao에 새로운 기능을 추가한다.

- **deleteAll()** : USER 테이블의 모든 레코드를 삭제하는 기능

```java
    public void deleteAll() throws SQLException {
        Connection c = dataSource.getConnection();

        PreparedStatement ps = c.prepareStatement("delete from usersr");
        ps.executeUpdate();

        ps.close();
        c.close();
    }
```

- **getCount()** : USER 테이블의 레코드 개수를 돌려주는 기능

```java
 public int getCount() throws SQLException {
        Connection c = dataSource.getConnection();

        PreparedStatement ps = c.prepareStatement("select count(*) from users");

        ResultSet rs = ps.executeQuery();
        rs.next();

        int count = rs.getInt(1);

        rs.close();
        ps.close();
        c.close();

        return count;
    }
```

#### deleteAll()과 getCount()의 테스트

추가된 기능에 대한 테스트도 만들어야 한다. 테스트를 하려면 USER 테이블에 수동으로 데이터를 넣고 deleteAll()을 실행한 뒤에 테이블에 남은 게 있는지 확인해야하는데, 사람이 테스트 과정에 참여해야 하니 자동화돼서 반복적으로 실행 가능한 테스트 방법은 아님.

새로운 테스트를 만들기보다는 기존에 addAndGet() 테스트를 확장하는 방법을 사용하는 편이 나을 것 같음. addAndGet() 테스트를 시작할 때 deleteAll()를 실행하도록 함. 만약 deleteAll()이 잘 동작한다면 addAndGet() 테스트를 위해 매번 수동으로 테이블을 삭제하지 않아도 됨.

deleteAll()을 넣는 것만으로는 조금 부족함. getCount()를 함께 적용한다. deleteAll()이 잘 동작한다면 getCount()로 레코드 개수를 가져올 경우 0이 나와야 한다. 그런데 getCount()에 0만 돌려주는 버그가 있다면 deleteAll()을 검증하는 데 사용할 수 없다.

getCount()에 대한 검증 작업을 하나 더 추가한다. add() 메소드를 수행하고 나면 레코드 수가 0개에서 1로 바뀌어야한다. add() 메소드를 실행한 뒤에 getCount()의 결과를 한 번 더 확인하도록 해 getCount() 의 기능이 바르게 동작함을 검증할 수 있다.

```java
    @Test
    public void addAndGet() throws SQLException {
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao dao = context.getBean("userDao", UserDao.class);

        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        User user = new User();
        user.setId("bellroot6");
        user.setName("벨루트");
        user.setPassword("1234");

        dao.add(user);
        assertThat(dao.getCount(), is(1));

        User user2 = dao.get(user.getId());

        assertThat(user2.getName(), is(user.getName()));
        assertThat(user2.getPassword(), is(user.getPassword()));
    }
```

#### 동일한 결과를 보장하는 테스트

이전에는 테스트를 하기 전에 매번 직접 DB에서 데이터를 삭제해야 했지만, 이제는 그런 번거로운 과정이 필요 없어졌다. 테스트가 어떤 상황에서 반복적으로 실행된다고 하더라도 동일한 결과가 나올 수 있게 된 것이다. 단위 테스트는 코드가 바뀌지 않는다면 매번 실행할 때마다 동일한 테스트 결과를 얻을 수 있어야 한다.

동일한 테스트 결과를 얻을 수 있는 다른 방법 -> 테스트를 마치기 직전에 테스트가 변경하거나 추가한 데이터를 모두 원래 상태로 만들어 줌. (add() 메소드 후 deleteAll() 실행하는 방법)

나쁜 방법은 아니지만 addAndGet() 테스트 실행 이전에 다른 이유로 USER 테이블에 데이터가 들어가 있다면 이 때는 테스트가 실패할 수도 있음.

테스트 후에 USER 테이블을 지워주는 것도 좋지만, 그보다는 테스트하기 전에 테스트 실행에 문제가 되지 않는 상태를 만들어주는 편이 더 나을 것임.

**단위 테스트는 항상 일관성 있는 결과가 보장돼야 한다.**

</br>

### 2.3.3 포괄적인 테스트

두 개 이상의 레코드를 add() 했을 때는 getCount()의 실행 결과가 어떻게 될까? -> 미처 생각하지 못한 문제가 숨어 있을 지도 모르니 더 꼼꼼한 테스트가 필요

테스트를 안 만드는 것도 위험한 일이지만, 성의 없이 테스트를 만드는 바람에 문제가 있는 코드인데도 테스트가 성공하게 만드는 건 더 위험하다. 특히 한 가지 결과만 검증하고 마는 것은 상당히 위험!

#### getCount() 테스트

이번에는 여러 개의 User를 등록해가면서 getCount()의 결과를 매번 확인해보자. 이 테스트 기능을 기존의 addAndGet() 메소드에 추가하는 건 별로 좋은 생각 아님. <u>테스트 메소드는 한 번에 한 가지 검증 목적에만 충실한 것이 좋다.</u> getCount()를 위한 새로운 테스트 메소드를 만들어보자.

테스트 시나리오는 이렇다.

- USER 테이블의 데이터를 모두 지우고 getCount()로 레코드 개수가 0임을 확인
- 3개의 사용자 정보를 하나씩 추가하면서 매번 getCount() 결과가 하나씩 증가하는지 확인

테스트를 만들기 전에 User 클래스에 한 번에 모든 정보를 넣을 수 있도록 초기화 가능한 생성자를 추가. User 오브젝트를 여러 번 만들고 값을 넣어야 하니, 한 번에 설정 가능한 생성자를 만들어두면 편리하다. -> (프로덕션 코드에 테스트를 위한 코드가 존재해도 될까..?)

```java
    public User(String id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }
    
    public User() {
    }
```

새로 만든 생성자를 이용하면 addAndGet() 테스트의 코드도 다음과 같이 간략히 수정할 수 있다.

```java
UserDao dao = context.getBean("userDao", UserDao.class);
User user = new User("gyumee", "박성철", "springno1");
```

모든 코드의 수정 후에는 그 수정에 영향을 받을 만한 테스트를 실행하는 것을 잊지 말자.

```java
  @Test
    public void count() throws SQLException {
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao dao = context.getBean("userDao", UserDao.class);
        User user1 = new User("user1", "u1", "no1");
        User user2 = new User("user2", "u2", "no2");
        User user3 = new User("user3", "u3", "no3");

        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.add(user1);
        assertThat(dao.getCount(), is(1));

        dao.add(user2);
        assertThat(dao.getCount(), is(2));

        dao.add(user3);
        assertThat(dao.getCount(), is(3));
    }
```

세 개의 User 오브젝틀르 준비해두고 deleteAll()을 불러 테이블의 내용을 모두 삭제 한 뒤 getCount()가 0임을 확인하고, 만들어둔 User 오브젝트들을 하나씩 넣으면서 매번 getCount()가 하나씩 증가되는지를 확인하면 된다.

주의할 점은 JUnit은 특정한 테스트 메소드의 실행 순서를 보장해주지 않기 때문에, 두 개의 테스트(addAndGet, count)가 실행 순서에 영향을 받는다면 테스트를 잘못 만든 것이다. **모든 테스트는 실행 순서에 상관없이 독립적으로 항상 동일한 결과를 낼 수 있도록 해야 함.**

#### addAndGet() 테스트 보완

이번엔 addAndGet() 테스트를  좀 더 보완해보자. id를 조건으로 해서 사용자를 검색하는 기능을 가진 get()에 대한 테스트는 조금 부족한 감이 있다. get()이 파라미터로 주어진 id에 해당하는 사용자를 가져온 것인지, 그냥 아무거나 가져온 것인지 테스트에서 검증하지는 못했음.

User를 하나 더 추가해서 두 개의 User를 add()하고, 각 User id를 파라미터로 전달해 get()을 실행하도록 한다.

```java
		@Test
    public void addAndGet() throws SQLException {
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao dao = context.getBean("userDao", UserDao.class);

        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        User user1 = new User("user1", "u1", "no1");
        User user2 = new User("user2", "u2", "no2");

        dao.add(user1);
        dao.add(user2);
        assertThat(dao.getCount(), is(1));

        User userget1 = dao.get(user1.getId());
        assertThat(userget1.getName(), is(user1.getName()));
        assertThat(userget1.getPassword(), is(user1.getPassword()));

        User userget2 = dao.get(user2.getId());
        assertThat(userget2.getName(), is(user2.getName()));
        assertThat(userget2.getPassword(), is(user2.getPassword()));
    }
```

주어진 id에 해당하는 정확한 User 정보를 가져오는 지 확인할 수 있게 됐다.

#### get() 예외조건에 대한 테스트

get() 메소드에 전달된 id 값에 해당하는 사용자 정보가 없다면 어떻게 되나? 이럴 땐 어떤 결과가 나오면 좋을까?

- null과 같은 특별한 값을 리턴
- id에 해당하는 정보를 찾을 수 없다고 예외를 던지는 것

후자의 방법을 써보자.

UserDao의 get() 메소드에서 쿼리를 실행해 결과를 가져왔을 때 아무것도 없으면 예외를 던지도록 만든다. 일반적으로 테스트 중에 예외가 던져지면 테스트 메소드 실행이 중단되고 테스트는 실패한다. 예외 발생 여부는 메소드를 실행해서 리턴 값을 비교하는 assertThat() 으로는 검증이 불가능 하다.

JUnit은 예외조건 테스트를 위한 특별한 방법을 제공해준다.

```java
    @Test(expected = EmptyResultDataAccessException.class) // 테스트 중에 발생할 것으로 기대하는 예외 클래스를 지정
    public void getUserFailure() throws SQLException {
        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
        UserDao dao = context.getBean("userDao", UserDao.class);

        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.get("unknown_id");
    }
```

이 테스트에서 중요한 것은 @Test 어노테이션의 expected 엘리먼트다. 테스트 메소드 실행 중에 발생하리라 기대하는 예외 클래스를 넣어주면 된다. expected를 추가하면 정상적으로 테스트 메소드를 마치만 테스트가 실패하고, 지정한 예외가 던져지면 테스트가 성공한다.

지금 이 테스트를 실행시키면 실패함. get() 메소드에서 쿼리 결과의 첫 번째 row를 가져오는 rs.next()를 실행할 때 가져올 row가 없다는 SQLException이 발생하기 때문.

#### 테스트를 성공시키기 위한 코드의 수정

```java
    public User get(String id) throws SQLException {
      	...
        ResultSet rs = ps.executeQuery();

        User user = null;

        if (rs.next()) {
            user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
        }

        if (user == null) {
            throw new EmptyResultDataAccessException(1);
        }

        rs.close();
        ps.close();
        c.close();

        return user;
    }
```

#### 포괄적인 테스트

이렇게 DAO의 메소드에 대한 포괄적인 테스트를 만들어두는 편이 훨씬 안전하고 유용함. 특히 평소에는 정상적으로 잘 동작하는 것처럼 보이지만 막상 특별한 상황이 되면 엉뚱하게 동작하는 코드를 만들었는데 테스트도 안 해봤다면, 나중에 문제가 발생했을 때 원인을 찾기 힘들어서 고생하게 될지도 모름.

개발자가 테스트를 직접 만들 때 자주 하는 실수 == 성공하는 테스트만 골라서 만드는 것

"내 PC에서는 잘 되는데"라는 변명 == 개발자 PC에서 테스트 할 때는 예외적인 상황은 모두 피하고 정상적인 케이스만 테스트 해봤다는 뜻

스프링의 창시자인 로드 존슨은 "항상 네거티브 테스트를 먼저 만들라"는 조언을 했다.

테스트를 작성할 때 부정적인 케이스를 먼저 만드는 습관을 들이는 게 좋다. 예외적인 상황을 빠뜨리지 않는 꼼꼼한 개발이 가능하다.

</br>

### 2.3.4 테스트가 이끄는 개발

get() 메소드의 예외 테스트를 만드는 과정을 다시 돌아보면, 테스트를 먼저 만들어 테스트가 실패하는 것을 보고 나서 UserDao의 코드에 손을 대기 시작했다. 이상하다고 생각할지 모르겠지만 이런 순서를 따라서 개발을 진행하는 구체적인 개발 전략이 실제로 존재하고, 이런 개발 방법이 적극적으로 사용되고 있다.

#### 기능설계를 위한 테스트

테스트할 코드도 없는데 어떻게 테스트를 만들 수 있었을까? 그것은 만들어진 코드를 보고 이것을 어떻게 테스트할까라고 생각하면서 테스트 코드를 작성하는 것이 아니라, <u>추가하고 싶은 기능을 코드로 표현하려고 했기 때문에 가능</u>했다.

getUserFailure() 테스트에는 만들고 싶은 기능에 대한 조건과 행위, 결과에 대한 내용이 잘 표현되어 있음.

|      | 단계                | 내용                                    | 코드                                                     |
| ---- | ------------------- | --------------------------------------- | -------------------------------------------------------- |
| 조건 | 어떤 조건을 가지고  | 가져올 사용자 정보가 존재하지 않는 경우 | dao.deleteAll();<br />assertThat(dar.getCount(), is(0)); |
| 행위 | 무엇을 할 때        | 존재하지 않는 id로 get()을 실행하면     | get("unknown_url")                                       |
| 결과 | 어떤 결과가 나온다. | 특별한 예외가 던져진다.                 | @Test(expected=EmptyResultDataAccessException.class)     |

이 테스트 코드는 마치 잘 작성된 하나의 기능정의서처럼 보인다. 추가하고 싶은 기능을 일반 언어가 아니라 테스트 코드로 표현해서, 마치 코드로 된 설계문서처럼 만들어놓은 것이라 생각해보자. 그러고 나서 실제 기능을 가진 애플리케이션 코드를 만들고 나면, 바로 이 테스트를 실행해서 설계한 대로 코드가 동작하는지를 빠르게 검증할 수 있다. 만약 테스트가 실패하면 이때는 설계한 대로 코드가 만들어지지 않았음을 바로 알 수 있다.

#### 테스트 주도 개발

만들고자 하는 기능의 내용을 담고 있으면서 만들어진 코드를 검증도 해줄 수 있도록 테스트 코드를 먼저 만들고, 테스트를 성공하게 해주는 코드를 작성하는 방식의 개발 방법을 **테스트 주도 개발(TDD, Test Driven Development)** 이라고 한다.

- TDD는 개발자가 테스트를 만들어가며 개발하는 방법이 주는 장점을 극대화한 방법.

- "실패한 테스트를 성공시키기 위한 목적이 나닌 코드는 만들지 않는다"는 것이 TDD의 기본 원칙임. 이 원칙을 따랐다면 만들어진 모든 코드는 빠짐없이 테스트로 검증된 것이라고 볼 수 있다.

- TDD는 아예 테스트를 먼저 만들고 그 테스트가 성공하도록 하는 코드만 만드는 식으로 진행하기 때문에 테스트를 빼먹지 않고 꼼꼼하게 만들어 낼 수 있다. 
- 테스트를 작성하는 시간과 애플리케이션 코드를 작성하는 시간의 간격이 짧아진다.
- 테스트를 바로바로 실행해볼 수 있기 때문에 코드에 대한 피드백을 매우 빠르게 받을 수 있게 된다.
- 매번 테스트가 성공하는 것을 보면 작성한 코드에 대한 확신을 가질 수 있어, 가벼운 마음으로 다음단계로 넘어갈 수 있다.

- TDD에서는 테스트를 작성하고 이를 성공시키는 코드를 만드는 작업의 주기를 가능한 한 짧게 가져가도록 권장함.
- TDD를 하면 자연스럽게 단위 테스트를 만들 수 있다.

- TDD의 장점 중 하나는 코드를 만들어 테스트를 실행하는 그 사이의 간격이 매우 짧다는 점
  - 개발한 코드의 오류는 빨리 발견할수록 좋다. 빨리 발견된 오류는 쉽게 대응 가능하기 때문이다.
  - 테스트 없이 오랜 시간 동안 코드를 만들고 나서 테스트를 하면, 오류가 발생했을 때 원인을 찾기가 쉽지 않기 때문.

테스트를 만들고 자주 실행하면 개발이 지연되지 않을까 염려할지도 모르겠다.

- 테스트 코드를 만들지 않아도 언젠가는 웹 화면을 통한 테스트라도 하게 될 것
- 이런 테스트의 비효율성을 생각해보면 미리미리 단위 테스트를 만들어서 코드를 검증해두는 편이 낫다.

그럼에도 왜 개발자가 테스트를 잘 만들지 않는 것일까?

- 엔터프라이즈 애플리케이션의 테스트를 만들기가 매우 어렵다고 생각하기 때문
- 하지만 스프링은 테스트하기 편리한 구조의 애플리케이션을 만들게 도와줄 뿐만 아니라, 엔터프라이즈 애플리케이션 테스트를 빠르고 쉽게 작성할 수 있는 매우 편리한 기능을 제공.

(그럼에도 스프링에서 제공하는 테스트 프레임워크를 이용하기 위한 학습비용은 만만치 않은 것 같다..)

</br>

### 2.3.5 테스트 코드 개선

테스트 코드를 리팩토링해보자. 애플리케이션 코드만이 리팩토링의 대상은 아니다. 필요하다면 테스트 코드도 언제든지 내부 구조와 설계를 개선해서 좀 더 깔끔하고 이해하기 쉬우며 변경이 용이한 코드로 만들 필요가 있다.

UserDaoTest 코드의 기계적으로 반복되는 부분 -> 스프링의 애플리케이션 컨텍스트를 만드는 부분과 userDao를 가져오는 부분

```java
ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
UserDao dao = context.getBean("userDao", UserDao.class);

```

JUnit 프레임워크는 테스트 메소드를 실행할 때 부가적으로 해주는 작업이 몇 가지 있음. 그중에서 테스트를 실행할 때마다 반복되는 준비 작업을 별도의 메소드를 넣게 해주고, 이를 매번 테스트 메소드를 실행하기 전에 먼저 실행시켜주는 기능이다.

#### @Before

> JUnit이 제공하는 어노테이션. @Test 메소드가 실행되기 전에 먼저 실행돼야 하는 메소드를 정의한다.

```java
public class UserDaoTest {

    private UserDao dao; // setUp() 메소드에서 만드는 오브젝트를 테스트 메소드에서 사용할 수 있도록 인스턴스 변수로 선언

    @Before
    public void setUp() { // 각 테스트 메소드에 반복적으로 나타났던 코드를 제거하고 별도의 메소드로 옮긴다.
        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
        this.dao = context.getBean("userDao", UserDao.class);
    }

    @Test
    public void addAndGet() throws SQLException {
      //        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
			//        UserDao dao = context.getBean("userDao", UserDao.class);
      ...
    }

    @Test
    public void count() throws SQLException {
      //        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
			//        UserDao dao = context.getBean("userDao", UserDao.class);
      ...
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getUserFailure() throws SQLException {
      //        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
			//        UserDao dao = context.getBean("userDao", UserDao.class);
      ...
    }
}

```

프레임워크는 스스로 제어권을 가지고 주도적으로 동작하고, 개발자가 만든 코드는 프레임워크에 의해 수동적으로 실행된다.  그래서 <u>프레임워크에 사용되는 코드만으로는 실행 흐름이 잘 보이지 않기 때문에 프레임워크가 어떻게 사용할지를 잘 이해하고 있어야 한다.</u>

JUnit이 하나의 테스트 클래스를 가져와 테스트를 수행하는 방식

1. 테스트 클래스에서 @Test가 붙은 public이고 void형이며 파라미터가 없는 테스트 메소드를 모두 찾는다.
2. 테스트 클래스의 오브젝트를 하나 만든다.
3. @Before가 붙은 메소드가 있으면 실행한다.
4. @Test가 붙은 메소드를 하나 호출하고 테스트 결과를 저장해둔다.
5. @After가 붙은 메소드가 있으면 실행한다.
6. 나머지 테스트 메소드에 대해 2~5번을 반복한다.
7. 모든 테스트의 결과를 종합해서 돌려준다.

JUnit은 @Test가 붙은 메소드를 실행하기 전과 후에 각각 @Before와 @After가 붙은 메소드를 자동으로 실행함. 대신 서로 주고 받을 정보나 오브젝트가 있다면 인스턴스 변수를 이용해야 한다.

또 한 가지 꼭 기억해야 할 사항은 각 테스트 메소드를 실행할 때마다 테스트 클래스의 오브젝트를 새로 만든다는 점이다.

![image](https://user-images.githubusercontent.com/41420639/101443907-bbc82880-3961-11eb-85f9-e26fc908d184.png)

한번 만들어진 테스트 클래스의 오브젝트는 하나의 테스트 메소드를 사용하고 나면 버려진다.

왜 테스트 메소드를 실행할 때마다 새로운 오브젝트를 만드는 것일까?

- JUnit 개발자는 각 테스트가 서로 영향을 주지 않고 독립적으로 실행됨을 확실히 보장해주기 위해 매번 새로운 오브젝트를 만들게 했다.
- 덕분에 인스턴스 변수도 부담없이 사용할 수 있다.

테스트 메소드의 일부에서만 공통적으로 사용되는 코드가 있다면 어떻게 해야할까?

- @Before를 사용하기보다는, 일반적인 메소드 추출 방법을 써서 메소드를 분리하고 테스트 메소드에서 직접 호출해 사용하도록 만드는 편이 낫다.
- 아니면 아예 공통적인 특징을 지닌 테스트 메소드를 모아서 별도의 테스트 클래스로 만드는 방법도 있다.

#### 픽스처

> 텍스트를 수행하는 데 필요한 정보나 오브젝트를 픽스처(fixture)라고 한다.

일반적으로 픽스처는 여러 테스트에서 반복적으로 사용되기 때문에 @Before 메소드를 이용해 생성해두면 편리함. UserDaoTest에서라면 dao가 대표적인 픽스처다. add() 메소드에 전달하는 User 오브젝트도 픽스처라고 볼 수 있다. 이 부분도 중복된 코드가 보인다. 중복을 제거하기 위해 @Before 메소드로 추출해보자.

```java
public class UserDaoTest {

    private UserDao dao;
    private User user1;
    private User user2;
    private User user3;

    @Before
    public void setUp() {
				...
        this.user1 = new User("user1", "u1", "no1");
        this.user2 = new User("user2", "u2", "no2");
        this.user3 = new User("user3", "u3", "no3");
    }
}
```



