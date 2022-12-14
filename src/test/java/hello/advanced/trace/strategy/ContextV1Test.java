package hello.advanced.trace.strategy;

import hello.advanced.trace.strategy.code.strategy.ContextV1;
import hello.advanced.trace.strategy.code.strategy.Strategy;
import hello.advanced.trace.strategy.code.strategy.StrategyLogic1;
import hello.advanced.trace.strategy.code.strategy.StrategyLogic2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ContextV1Test {

    @Test
    void strategyV0() {
        logic1();
        logic2();
    }

    private void logic1() {
        long startTime = System.currentTimeMillis();
        // 비즈니스 로직 실행
        log.info("비즈니스 로직1 실행");
        // 비즈니스 로직 종료
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTime={}", resultTime);
    }

    private void logic2() {
        long startTime = System.currentTimeMillis();
        // 비즈니스 로직 실행
        log.info("비즈니스 로직2 실행");
        // 비즈니스 로직 종료
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTime={}", resultTime);
    }

    /*
     * 전략 패턴 사용
     * */
    @Test
    void strategyV1() {
        StrategyLogic1 strategyLogic1 = new StrategyLogic1();
        ContextV1 context1 = new ContextV1(strategyLogic1);
        context1.execute();

        StrategyLogic2 strategyLogic2 = new StrategyLogic2();
        ContextV1 context2 = new ContextV1(strategyLogic2);
        context2.execute();

    }


    @Test
    void strategyV2() {
        Strategy strategyLogic1 = new Strategy() {
            @Override
            public void call() {
                log.info("비즈니스 로직1 실행");
            }
        };
        ContextV1 context1 = new ContextV1(strategyLogic1);
        context1.execute();

        Strategy strategyLogic2 = new Strategy() {
            @Override
            public void call() {
                log.info("비즈니스 로직2 실행");
            }
        };
        ContextV1 context2 = new ContextV1(strategyLogic2);
        context2.execute();

    }

    /*
     *  익명 내부 클래스를 변수에 담아두지 말고, 생성하면서 바로 ContextV1에 전달해도 된다.
     * */
    @Test
    void strategyV3() {
        ContextV1 context1 = new ContextV1(new Strategy() {
            @Override
            public void call() {
                log.info("비즈니스 로직1 실행");
            }
        });
        context1.execute();

        ContextV1 context2 = new ContextV1(new Strategy() {
            @Override
            public void call() {
                log.info("비즈니스 로직2 실행");
            }
        });
        context2.execute();

    }


    /*
    *  익명 내부 클래스를 자바8부터 제공하는 람다로 변경할 수 있다.
    *  람다로 변경하려면 인터페이스에 메서드가 1개만 있으면 되는데 Strategy 인터페이스는 메서드가 1개만 있으므로 람다로 사용할 수
    *  있다.
    *
    * */
    @Test
    void strategyV4() {
        ContextV1 context1 = new ContextV1(() -> log.info("비즈니스 로직1 실행"));
        context1.execute();

        ContextV1 context2 = new ContextV1(() -> log.info("비즈니스 로직2 실행"));
        context2.execute();

    }

    /*
    * 정리
    * 변하지 않는 부분을 Context에 두고 변하는 부분을 Strategy를 구현해서 만든다.
    * 그리고 Context의 내부 필드에 Strategy를 주입해서 사용했다.
    *
    * 선 조립, 후 실행
    * Context의 내부 필드에 Strategy를 두고 사용하는 부분이다.
    * 이 방식은 Context와 Strategy를 실행전에 원하는 모양으로 조립해두고, Context를 실행하는 선 조립, 후 실행 방식에서
    * 매우 유용하다.
    * Context와 Strategy를 한번 조립하고 나면 이후로는 Context를 실행하기만 하면 된다. 우리가 스프링으로 애플리케이션을
    * 개발할 때 애플리케이션 로딩 시점에 의존관계 주입을 통해 필요한 의존관계를 모두 맺어두고 난 다음에 실제 요청을
    * 처리하는 것과 같은 원리이다.
    * 이 방식의 단점은 Context와 Strategy를 조립한 이후에는 전략을 변경하기가 번거롭다는 점이다. 물론 Context에
    * setter를 제공해서 Strategy 를 넘겨받아 변경하면 되지만, Context를 싱글톤으로 사용할 때는 동시성 이슈등 고려할 점이
    * 많다. 그래서 전략을 실시간으로 변경해야 하면 차라리 이전에 개발한 테스트 코드 처럼 Context를 하나 더 생성하고
    * 그 곳에 다른 Strategy를 주입하는 것이 더 나은 선택일 수 있다.
    *
    * 이렇게 먼저 조립하고 사용하는 방식보다 더 유연하게 전략 패턴을 사용하는방법은 없을까?
    *
    * */
}
