package dynamicProxy;

public class TargetClassImpl implements TargetClass{
    @Override
    public void say() {
        System.out.println("method say execute...");
    }

    @Override
    public void doSomething() {
        System.out.println("method doSomething execute...");
    }
}
